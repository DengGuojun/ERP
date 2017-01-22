package com.lpmas.erp.inventory.business;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.inventory.bean.WarehouseInventoryBean;
import com.lpmas.erp.inventory.bean.WarehouseTransferOrderItemBean;
import com.lpmas.erp.inventory.config.WarehouseInventoryConfig;
import com.lpmas.erp.inventory.config.WarehouseTransferOrderLogConfig;
import com.lpmas.erp.inventory.dao.WarehouseTransferOrderItemDao;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.DateKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.client.cache.MaterialInfoClientCache;
import com.lpmas.pdm.client.cache.ProductItemClientCache;

public class WarehouseTransferOrderItemBusiness {
	public int addWarehouseTransferOrderItem(WarehouseTransferOrderItemBean bean) {
		WarehouseTransferOrderItemDao dao = new WarehouseTransferOrderItemDao();
		int result = dao.insertWarehouseTransferOrderItem(bean);
		if (result > 0) {
			bean.setToItemId(result);
			ErpLogSender helper = new ErpLogSender();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_WAREHOUSE_TRANSFER_ORDER, bean.getToId(), bean.getToItemId(), 0,
					WarehouseTransferOrderLogConfig.LOG_WAREHOUSE_TRANSFER_ORDER_ITEM);
		}
		return result;
	}

	public int updateWarehouseTransferOrderItem(WarehouseTransferOrderItemBean bean) {
		WarehouseTransferOrderItemDao dao = new WarehouseTransferOrderItemDao();
		WarehouseTransferOrderItemBean orgBean = getWarehouseTransferOrderItemByKey(bean.getToItemId());
		int result = dao.updateWarehouseTransferOrderItem(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(orgBean, bean, InfoTypeConfig.INFO_TYPE_WAREHOUSE_TRANSFER_ORDER, bean.getToId(), bean.getToItemId(), 0,
					WarehouseTransferOrderLogConfig.LOG_WAREHOUSE_TRANSFER_ORDER_ITEM);
		}
		return result;
	}

	public WarehouseTransferOrderItemBean getWarehouseTransferOrderItemByKey(int toItemId) {
		WarehouseTransferOrderItemDao dao = new WarehouseTransferOrderItemDao();
		return dao.getWarehouseTransferOrderItemByKey(toItemId);
	}

	public List<WarehouseTransferOrderItemBean> getTransferOrderItemListByToId(int toId) {
		WarehouseTransferOrderItemDao dao = new WarehouseTransferOrderItemDao();
		return dao.getTransferOrderItemListByToId(toId);
	}

	public PageResultBean<WarehouseTransferOrderItemBean> getWarehouseTransferOrderItemPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		WarehouseTransferOrderItemDao dao = new WarehouseTransferOrderItemDao();
		return dao.getWarehouseTransferOrderItemPageListByMap(condMap, pageBean);
	}

	public ReturnMessageBean validateTransferOrderDetail(Map<String, String> itemMap, int wareType) {
		ReturnMessageBean bean = new ReturnMessageBean();
		if (!StringKit.isValid(itemMap.get("quantity"))) {
			bean.setMessage("调拨数量必须填写");
		} // 验证保质期
		else if (checkHasExpiration(wareType, Integer.valueOf(itemMap.get("wareId")))) {
			if (StringKit.isValid(itemMap.get("guaranteePeriod"))) {
				try {
					Double guaranteePeriod = Double.valueOf(itemMap.get("guaranteePeriod"));
					if (guaranteePeriod == 0f) {
						// 是保质期管理要填保质期
						bean.setMessage("保质期缺失!");
					}
				} catch (Exception e) {
					bean.setMessage("保质期缺失!");
				}
			} else if (!StringKit.isValid(itemMap.get("expirationDate"))) {
				bean.setMessage("保质期至缺失");
			} else {
				// 是保质期管理，并且有填保质期至
				// 尝试转换
				try {
					DateKit.str2Timestamp(itemMap.get("expirationDate"), DateKit.DEFAULT_DATE_FORMAT);
				} catch (Exception e) {
					bean.setMessage("保质期至格式错误!");
				}
			}
		}
		// 验证生产日期
		String productionDateStr = itemMap.get("productionDate");
		if (!StringKit.isValid(itemMap.get("productionDate"))) {
			bean.setMessage("生产日期缺失!");
		} else {
			try {
				DateKit.str2Timestamp(productionDateStr, DateKit.DEFAULT_DATE_FORMAT);
			} catch (Exception e) {
				bean.setMessage("生产日期格式错误!");
			}
		}
		return bean;
	}

	public boolean validateQuantity(List<WarehouseTransferOrderItemBean> itemList, int warehouseId) {
		WarehouseInventoryBusiness business = new WarehouseInventoryBusiness();
		for (WarehouseTransferOrderItemBean bean : itemList) {
			WarehouseInventoryBean inventoryBean = business.getWarehouseInventoryBeanByCondition(warehouseId, bean.getWareType(), bean.getWareId(),
					WarehouseInventoryConfig.WIT_NORMAL, bean.getBatchNumber(), bean.getProductionDate());
			BigDecimal inventoryQty = new BigDecimal(inventoryBean.getQuantity());
			BigDecimal StockOutQty = new BigDecimal(bean.getQuanlity());
			// 库存小于要出库的数量
			if (inventoryQty.compareTo(StockOutQty) == -1) {
				return false;
			}
		}
		return true;
	}

	public Boolean checkHasExpiration(int wareType, int wareId) {
		Double guaranteePeriod = null;
		if (wareType == InfoTypeConfig.INFO_TYPE_MATERIAL) {
			// 是物料
			MaterialInfoClientCache materialInfoClientCache = new MaterialInfoClientCache();
			guaranteePeriod = materialInfoClientCache.getMaterialInfoByKey(wareId).getGuaranteePeriod();
		} else if (wareType == InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM) {
			// 是商品项
			ProductItemClientCache productItemClientCache = new ProductItemClientCache();
			guaranteePeriod = productItemClientCache.getProductItemByKey(wareId).getGuaranteePeriod();
		}
		if (guaranteePeriod == null || guaranteePeriod == -1f) {
			return false;
		}
		return true;
	}

	public int processTransferItemOrder(List<WarehouseTransferOrderItemBean> newDvItemList, int toId, int userId) {
		int result = 0;
		// 找出当前存在于数据库的ITEM列表
		List<WarehouseTransferOrderItemBean> dbExistItemList = getTransferOrderItemListByToId(toId);
		// 应该要检查这个ITEM是否存在于数据库中，存在的就UPDATE 不存在的就是ADD
		if (!newDvItemList.isEmpty()) {
			for (WarehouseTransferOrderItemBean tempBean : newDvItemList) {
				tempBean.setToId(toId);
				result = checkBeanForUpdateOrAdd(tempBean, dbExistItemList, userId);
			}
		}

		// 剩下的要删除
		if (!dbExistItemList.isEmpty()) {
			for (WarehouseTransferOrderItemBean tempBean : dbExistItemList) {
				tempBean.setModifyUser(userId);
				tempBean.setStatus(Constants.STATUS_NOT_VALID);
				result = updateWarehouseTransferOrderItem(tempBean);
			}
		}
		return result;
	}

	public void processTransferItem(Map<String, String> itemValueMap, int wareType, List<WarehouseTransferOrderItemBean> newTransferOrderItemList) {
		WarehouseTransferOrderItemBean itemBean = new WarehouseTransferOrderItemBean();
		// 避免类型转换错误
		try {
			Timestamp productionDate = DateKit.str2Timestamp(itemValueMap.get("productionDate"), DateKit.DEFAULT_DATE_FORMAT);
			itemBean.setWareType(wareType);
			int wareId = Integer.valueOf(itemValueMap.get("wareId"));
			itemBean.setWareId(wareId);
			itemBean.setUnit(itemValueMap.get("unit"));
			itemBean.setBatchNumber(itemValueMap.get("batchNumber"));
			itemBean.setProductionDate(productionDate);
			itemBean.setQuanlity(Double.valueOf(itemValueMap.get("quantity")));
			itemBean.setBatchNumber(itemValueMap.get("batchNumber"));
			itemBean.setMemo(itemValueMap.get("memo"));
			try {
				// 如果没有保质期，则设置为-1
				itemBean.setGuaranteePeriod(Double.valueOf(itemValueMap.get("guaranteePeriod")));
				if (StringKit.isValid(itemValueMap.get("expirationDate")) && !itemValueMap.get("expirationDate").equals("null")) {
					Timestamp expirationDate = DateKit.str2Timestamp(itemValueMap.get("expirationDate"), DateKit.DEFAULT_DATE_FORMAT);
					itemBean.setExpirationDate(expirationDate);
				}
			} catch (NumberFormatException e) {
				itemBean.setGuaranteePeriod(-1f);
			}
			itemBean.setStatus(Constants.STATUS_VALID);
			itemBean.setMemo(itemValueMap.get("memo"));
			newTransferOrderItemList.add(itemBean);
		} catch (Exception e) {
		}
	}

	// 获取所有ITEM，只包括有效的
	public List<WarehouseTransferOrderItemBean> getTransferOrderItemValidListByToId(int toId) {
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("toId", String.valueOf(toId));
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		WarehouseTransferOrderItemDao dao = new WarehouseTransferOrderItemDao();
		return dao.getWarehouseTransferOrderItemListByMap(condMap);
	}

	public int checkBeanForUpdateOrAdd(WarehouseTransferOrderItemBean bean, List<WarehouseTransferOrderItemBean> dbExistItemList, int adminUser) {
		int result = -1;
		// 先判断是否存在于数据库
		WarehouseTransferOrderItemBean existBean = null;
		Iterator<WarehouseTransferOrderItemBean> iterable = dbExistItemList.iterator();
		while (iterable.hasNext()) {
			existBean = iterable.next();
			if (existBean.getWareType() == existBean.getWareType() && existBean.getWareId() == bean.getWareId()) {
				// 这个ITEM存在于数据库
				iterable.remove();
				break;
			} else {
				existBean = null;
			}
		}
		if (existBean != null) {
			// 存在就UPDATE
			bean.setToItemId(existBean.getToItemId());
			bean.setModifyUser(adminUser);
			result = updateWarehouseTransferOrderItem(bean);
		} else {
			// 不存在就ADD
			bean.setCreateUser(adminUser);
			result = addWarehouseTransferOrderItem(bean);
		}
		return result;
	}
}
