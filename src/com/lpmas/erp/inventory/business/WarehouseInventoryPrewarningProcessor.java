package com.lpmas.erp.inventory.business;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.erp.inventory.bean.WarehouseInventoryAggregateReportBean;
import com.lpmas.erp.inventory.bean.WarehouseInventoryPrewarningBean;
import com.lpmas.erp.inventory.bean.WarehouseInventoryPrewarningContentBean;
import com.lpmas.erp.inventory.bean.WarehouseInventoryReportBean;
import com.lpmas.erp.inventory.config.WarehouseInventoryPrewarningConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.DateKit;
import com.lpmas.framework.util.JsonKit;

public class WarehouseInventoryPrewarningProcessor {

	private static Logger log = LoggerFactory.getLogger(WarehouseInventoryPrewarningProcessor.class);

	public WarehouseInventoryPrewarningProcessor() {
	}

	public String getPrewarningStrByWiReportBean(WarehouseInventoryAggregateReportBean reportBean) {
		return getPrewarningStrByWiReportBean(reportBean, 0);
	}

	public String getPrewarningStrByWiReportBean(WarehouseInventoryAggregateReportBean reportBean, int prewarningType) {
		WarehouseInventoryPrewarningBusiness business = new WarehouseInventoryPrewarningBusiness();
		HashMap<String, String> condMap = new HashMap<String, String>();
		if (prewarningType > 0) {
			condMap.put("prewarningType", String.valueOf(prewarningType));
		}
		condMap.put("wareType", String.valueOf(reportBean.getWareType()));
		condMap.put("wareId", String.valueOf(reportBean.getWareId()));
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		List<WarehouseInventoryPrewarningBean> list = business.getWarehouseInventoryPrewarningListByMap(condMap);

		StringBuffer stringBuffer = new StringBuffer();
		try {
			for (WarehouseInventoryPrewarningBean prewarningBean : list) {
				List<WarehouseInventoryPrewarningContentBean> contentList = JsonKit
						.toList(prewarningBean.getPrewarningContent(), WarehouseInventoryPrewarningContentBean.class);
				for (WarehouseInventoryPrewarningContentBean contentBean : contentList) {
					if (isPrewarningTrigger(reportBean, prewarningBean, contentBean)) {
						// 触发了预警
						stringBuffer.append("<span>" + contentBean.getName() + "预警!</span><br>");
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		if (stringBuffer.length() > 0) {
			return stringBuffer.substring(0, stringBuffer.length() - 4);
		}
		return "<span>无</span>";
	}

	public boolean isPrewarningTrigger(WarehouseInventoryAggregateReportBean reportBean,
			WarehouseInventoryPrewarningBean bean, WarehouseInventoryPrewarningContentBean contentBean) {
		try {
			Double left = null;
			Double right = Double.valueOf(contentBean.getValue());
			if (bean.getPrewarningType() == WarehouseInventoryPrewarningConfig.PREWARNING_TYPE_EXPIRATION) {
				// 保质期预警
				// 获取保质期
				if (((WarehouseInventoryReportBean) reportBean).getGuaranteePeriod() == -1f) {
					// 无保质期管理商品
					return false;
				}
				left = calculateRemainGuaranteePeriod(((WarehouseInventoryReportBean) reportBean).getExpirationDate());
			} else if (bean.getPrewarningType() == WarehouseInventoryPrewarningConfig.PREWARNING_TYPE_INVENTORY) {
				// 库存预警
				// 获取库存值
				left = reportBean.getNormalQuantity();
			} else {
				return false;
			}
			return compareBetween(contentBean.getOperator(), left, right);
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
	}

	public String getInventoryPrewarningStr(WarehouseInventoryAggregateReportBean reportBean) {
		WarehouseInventoryPrewarningBusiness business = new WarehouseInventoryPrewarningBusiness();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("prewarningType", String.valueOf(WarehouseInventoryPrewarningConfig.PREWARNING_TYPE_INVENTORY));
		condMap.put("wareType", String.valueOf(reportBean.getWareType()));
		condMap.put("wareId", String.valueOf(reportBean.getWareId()));
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		List<WarehouseInventoryPrewarningBean> list = business.getWarehouseInventoryPrewarningListByMap(condMap);

		StringBuffer stringBuffer = new StringBuffer();
		try {
			for (WarehouseInventoryPrewarningBean prewarningBean : list) {
				List<WarehouseInventoryPrewarningContentBean> contentList = JsonKit
						.toList(prewarningBean.getPrewarningContent(), WarehouseInventoryPrewarningContentBean.class);
				for (WarehouseInventoryPrewarningContentBean contentBean : contentList) {
					stringBuffer.append("<span>" + contentBean.getKey() + ":" + contentBean.getValue() + "</span><br>");
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		if (stringBuffer.length() > 0) {
			return stringBuffer.substring(0, stringBuffer.length() - 4);
		}
		return "<span>无</span>";
	}

	private boolean compareBetween(String operator, Double left, Double right) {
		BigDecimal leftDecimal = new BigDecimal(left);
		BigDecimal rightDecimal = new BigDecimal(right);
		// -1是左比又小
		// 0是相等
		// 1是左比右大
		int compareResult = leftDecimal.compareTo(rightDecimal);

		switch (operator.trim()) {
		case "eq":
			return compareResult == 0;// 等于
		case "neq":
			return !(compareResult == 0);// 不等于
		case "gt":
			return compareResult == 1;// 左大于右
		case "gte":
			return compareResult == 1 || compareResult == 0;// 左大于等于右
		case "lt":
			return compareResult == -1;// 左小于右
		case "lte":
			return compareResult == -1 || compareResult == 0;// 左小于等于右
		default:
			return false;
		}
	}

	public Double calculateRemainGuaranteePeriod(Timestamp expirationDate) {
		if (expirationDate == null) {
			return null;
		}
		String dateStr = "";
		try {
			dateStr = DateKit.formatTimestamp(expirationDate, DateKit.DEFAULT_DATE_FORMAT);
			return calculateRemainGuaranteePeriod(dateStr);
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}

	}

	public Double calculateRemainGuaranteePeriod(String expirationDate) {
		if (expirationDate == null) {
			return null;
		}
		try {
			Date date = DateKit.str2Date(expirationDate, DateKit.DEFAULT_DATE_FORMAT);
			int result = DateKit.diffTime(DateKit.REGEX_DATE, DateKit.getCurrentTimestamp(), date);
			// 加上最后1天
			return new Double(result + 1);
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

}
