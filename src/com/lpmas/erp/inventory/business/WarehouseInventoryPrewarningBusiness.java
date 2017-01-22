package com.lpmas.erp.inventory.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.inventory.bean.WarehouseInventoryPrewarningBean;
import com.lpmas.erp.inventory.bean.WarehouseInventoryPrewarningContentBean;
import com.lpmas.erp.inventory.config.WarehouseInventoryLogConfig;
import com.lpmas.erp.inventory.config.WarehouseInventoryPrewarningConfig;
import com.lpmas.erp.inventory.dao.WarehouseInventoryPrewarningDao;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.client.PdmServiceClient;

public class WarehouseInventoryPrewarningBusiness {
	public int addWarehouseInventoryPrewarning(WarehouseInventoryPrewarningBean bean) {
		WarehouseInventoryPrewarningDao dao = new WarehouseInventoryPrewarningDao();
		int result = dao.insertWarehouseInventoryPrewarning(bean);
		if (result >= 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_WAREHOUSE_INVENTORY, bean.getWareType(), bean.getWareId(),
					bean.getPrewarningType(), WarehouseInventoryLogConfig.WAREHOUSE_INVENTORY_PREWARNING);
		}
		return result;
	}

	public int updateWarehouseInventoryPrewarning(WarehouseInventoryPrewarningBean bean) {
		WarehouseInventoryPrewarningDao dao = new WarehouseInventoryPrewarningDao();
		WarehouseInventoryPrewarningBean originalBean = getWarehouseInventoryPrewarningByKey(bean.getWareType(),
				bean.getWareId(), bean.getPrewarningType());
		int result = dao.updateWarehouseInventoryPrewarning(bean);
		if (result >= 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_WAREHOUSE_INVENTORY, bean.getWareType(),
					bean.getWareId(), bean.getPrewarningType(),
					WarehouseInventoryLogConfig.WAREHOUSE_INVENTORY_PREWARNING);
		}
		return result;
	}

	public String getPrewarningContentBean(String guaranteePeriod, String maxInventory, String minInventory,
			int prewarningType) {
		// 组装预警内容
		List<WarehouseInventoryPrewarningContentBean> contentList = null;
		if (prewarningType == WarehouseInventoryPrewarningConfig.PREWARNING_TYPE_EXPIRATION) {
			WarehouseInventoryPrewarningContentBean contentBean = new WarehouseInventoryPrewarningContentBean();
			contentList = new ArrayList<WarehouseInventoryPrewarningContentBean>();
			contentBean.setKey(WarehouseInventoryPrewarningConfig.GUARANTEE_PERIOD);
			contentBean.setValue(guaranteePeriod);
			contentBean.setName("保质期");
			contentBean.setOperator("lt");
			contentBean.setDiscretion("保质期预警");
			contentList.add(contentBean);
			return JsonKit.toJson(contentList);
		} else if (prewarningType == WarehouseInventoryPrewarningConfig.PREWARNING_TYPE_INVENTORY) {
			WarehouseInventoryPrewarningContentBean maxContentBean = new WarehouseInventoryPrewarningContentBean();
			contentList = new ArrayList<WarehouseInventoryPrewarningContentBean>();
			maxContentBean.setValue(maxInventory);
			maxContentBean.setKey(WarehouseInventoryPrewarningConfig.MAX_INVENTORY);
			maxContentBean.setName("最大库存");
			maxContentBean.setOperator("gt");
			maxContentBean.setDiscretion("最大库存预警");
			contentList.add(maxContentBean);

			WarehouseInventoryPrewarningContentBean mincontentBean = new WarehouseInventoryPrewarningContentBean();
			mincontentBean.setValue(minInventory);
			mincontentBean.setKey(WarehouseInventoryPrewarningConfig.MIN_INVENTORY);
			mincontentBean.setName("最小库存");
			mincontentBean.setOperator("lt");
			mincontentBean.setDiscretion("最小库存预警");
			contentList.add(mincontentBean);
			return JsonKit.toJson(contentList);
		}
		return "";
	}

	public WarehouseInventoryPrewarningBean getWarehouseInventoryPrewarningByKey(int wareType, int wareId,
			int prewarningType) {
		WarehouseInventoryPrewarningDao dao = new WarehouseInventoryPrewarningDao();
		return dao.getWarehouseInventoryPrewarningByKey(wareType, wareId, prewarningType);
	}

	public List<WarehouseInventoryPrewarningBean> getWarehouseInventoryPrewarningListByMap(
			HashMap<String, String> condMap) {
		WarehouseInventoryPrewarningDao dao = new WarehouseInventoryPrewarningDao();
		return dao.getWarehouseInventoryPrewarningListByMap(condMap);
	}

	public PageResultBean<WarehouseInventoryPrewarningBean> getWarehouseInventoryPrewarningPageListByMap(
			HashMap<String, String> condMap, PageBean pageBean) {
		WarehouseInventoryPrewarningDao dao = new WarehouseInventoryPrewarningDao();
		return dao.getWarehouseInventoryPrewarningPageListByMap(condMap, pageBean);
	}

	public ReturnMessageBean validateBean(WarehouseInventoryPrewarningBean bean) {
		ReturnMessageBean result = new ReturnMessageBean();
		PdmServiceClient client = new PdmServiceClient();
		if (bean.getWareType() == 0
				|| WarehouseInventoryPrewarningConfig.WARE_TYPE_MAP.get(bean.getWareType()) == null) {
			result.setMessage("制品类型不合法");
		} else if (bean.getWareId() == 0) {
			result.setMessage("制品ID缺失");
			Object ware = null;
			if (bean.getWareType() == InfoTypeConfig.INFO_TYPE_MATERIAL) {
				ware = client.getMaterialInfoByKey(bean.getWareId());
			} else {
				ware = client.getProductItemByKey(bean.getWareId());
			}
			if (ware == null)
				result.setMessage("制品ID不合法");
		} else if (bean.getPrewarningType() == 0
				|| WarehouseInventoryPrewarningConfig.PREWARNING_TYPE_MAP.get(bean.getPrewarningType()) == null) {
			result.setMessage("预警类型不合法");
		} else {
			try {
				List<WarehouseInventoryPrewarningContentBean> contentList = JsonKit.toList(bean.getPrewarningContent(),
						WarehouseInventoryPrewarningContentBean.class);
				if (contentList.isEmpty()) {
					result.setMessage("预警内容必须填写");
				}
				for (WarehouseInventoryPrewarningContentBean contentBean : contentList) {
					int warningType = WarehouseInventoryPrewarningConfig.PREWARNING_CONTENT_MAP
							.get(contentBean.getKey());
					if (warningType == 0) {
						result.setMessage("不存在该预警内容字段");
						break;
					} else if (warningType != bean.getPrewarningType()) {
						result.setMessage("预警内容与预警类型不匹配");
						break;
					}
				}
			} catch (Exception e) {
				result.setMessage("预警内容格式错误!");
			}
		}

		return result;
	}

}