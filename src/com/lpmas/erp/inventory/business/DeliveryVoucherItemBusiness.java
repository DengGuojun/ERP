package com.lpmas.erp.inventory.business;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.inventory.bean.DeliveryVoucherItemBean;
import com.lpmas.erp.inventory.bean.WarehouseInventoryBean;
import com.lpmas.erp.inventory.config.DeliveryVoucherLogConfig;
import com.lpmas.erp.inventory.dao.DeliveryVoucherItemDao;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.MaterialInfoBean;
import com.lpmas.pdm.bean.ProductItemBean;
import com.lpmas.pdm.client.PdmServiceClient;

public class DeliveryVoucherItemBusiness {
	public int addDeliveryVoucherItem(DeliveryVoucherItemBean bean) {
		DeliveryVoucherItemDao dao = new DeliveryVoucherItemDao();
		int result = dao.insertDeliveryVoucherItem(bean);
		if (result > 0) {
			bean.setDvItemId(result);
			ErpLogSender helper = new ErpLogSender();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_DELIVERY_VOUCHER, bean.getDvId(), bean.getDvItemId(), 0,
					DeliveryVoucherLogConfig.LOG_DELIVERY_VOUCHER_ITEM);
		}
		return result;
	}

	public int updateDeliveryVoucherItem(DeliveryVoucherItemBean bean) {
		DeliveryVoucherItemDao dao = new DeliveryVoucherItemDao();
		DeliveryVoucherItemBean orgBean = getDeliveryVoucherItemByKey(bean.getDvItemId());
		int result = dao.updateDeliveryVoucherItem(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(orgBean, bean, InfoTypeConfig.INFO_TYPE_DELIVERY_VOUCHER, bean.getDvId(),
					bean.getDvItemId(), 0, DeliveryVoucherLogConfig.LOG_DELIVERY_VOUCHER_ITEM);
		}
		return result;
	}

	public DeliveryVoucherItemBean getDeliveryVoucherItemByKey(int dvItemId) {
		DeliveryVoucherItemDao dao = new DeliveryVoucherItemDao();
		return dao.getDeliveryVoucherItemByKey(dvItemId);
	}

	public PageResultBean<DeliveryVoucherItemBean> getDeliveryVoucherItemPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		DeliveryVoucherItemDao dao = new DeliveryVoucherItemDao();
		return dao.getDeliveryVoucherItemPageListByMap(condMap, pageBean);
	}

	public int processDvItem(List<DeliveryVoucherItemBean> existItemList, List<DeliveryVoucherItemBean> newDvItemList,
			int dvId, int userId) {
		int result = 0;
		if (dvId > 0) {
			// 找出当前存在于数据库的ITEM列表
			List<DeliveryVoucherItemBean> dbExistItemList = getDeliveryVoucherItemAllListByDvId(dvId);
			// 应该要检查这个ITEM是否存在于数据库中，存在的就UPDATE 不存在的就是ADD
			if (!existItemList.isEmpty()) {
				for (DeliveryVoucherItemBean item : existItemList) {
					item.setDvId(dvId);
					result = checkBeanForUpdateOrAdd(item, dbExistItemList, userId);
				}
			}
			if (!newDvItemList.isEmpty()) {
				for (DeliveryVoucherItemBean tempBean : newDvItemList) {
					tempBean.setDvId(dvId);
					result = checkBeanForUpdateOrAdd(tempBean, dbExistItemList, userId);
				}
			}

			// 剩下的要删除
			if (!dbExistItemList.isEmpty()) {
				for (DeliveryVoucherItemBean tempBean : dbExistItemList) {
					tempBean.setModifyUser(userId);
					tempBean.setStatus(Constants.STATUS_NOT_VALID);
					result = updateDeliveryVoucherItem(tempBean);
				}
			}
		} else {
			if (!existItemList.isEmpty()) {
				for (DeliveryVoucherItemBean item : existItemList) {
					item.setCreateUser(userId);
					item.setDvId(result);
					result = addDeliveryVoucherItem(item);
				}
			}
			if (!newDvItemList.isEmpty()) {
				for (DeliveryVoucherItemBean tempBean : newDvItemList) {
					tempBean.setCreateUser(userId);
					tempBean.setDvId(result);
					result = addDeliveryVoucherItem(tempBean);
				}
			}
		}
		return result;
	}

	/***
	 * 获取所有ITEM，包括无效的
	 * 
	 * @param dvId
	 * @return
	 */
	public List<DeliveryVoucherItemBean> getDeliveryVoucherItemAllListByDvId(int dvId) {
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("dvId", String.valueOf(dvId));
		DeliveryVoucherItemDao dao = new DeliveryVoucherItemDao();
		return dao.getDeliveryVoucherItemListByMap(condMap);
	}

	/**
	 * 取出所有ITEM，去除无效的
	 * 
	 * @param condMap
	 * @return
	 */
	public List<DeliveryVoucherItemBean> getDeliveryVoucherItemListByDvId(int dvId) {
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("dvId", String.valueOf(dvId));
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		DeliveryVoucherItemDao dao = new DeliveryVoucherItemDao();
		return dao.getDeliveryVoucherItemListByMap(condMap);
	}

	public Boolean checkHasExpiration(int wareType, Object item) {

		Double guaranteePeriod = null;
		if (wareType == InfoTypeConfig.INFO_TYPE_MATERIAL) {
			// 是物料
			guaranteePeriod = ((MaterialInfoBean) item).getGuaranteePeriod();
		} else if (wareType == InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM) {
			// 是商品项
			guaranteePeriod = ((ProductItemBean) item).getGuaranteePeriod();
		}

		if (guaranteePeriod == null || guaranteePeriod == -1f) {
			return false;
		}
		return true;
	}

	public Boolean checkHasExpiration(int wareType, int wareId) {
		PdmServiceClient client = new PdmServiceClient();
		Double guaranteePeriod = null;
		if (wareType == InfoTypeConfig.INFO_TYPE_MATERIAL) {
			// 是物料
			guaranteePeriod = client.getMaterialInfoByKey(wareId).getGuaranteePeriod();
		} else if (wareType == InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM) {
			// 是商品项
			guaranteePeriod = client.getProductItemByKey(wareId).getGuaranteePeriod();
		}
		if (guaranteePeriod == null || guaranteePeriod == -1f) {
			return false;
		}
		return true;
	}

	public ReturnMessageBean validateDvDetail(Map<String, String> itemMap) {
		ReturnMessageBean bean = new ReturnMessageBean();
		if (itemMap.get("stockOutQuantity").equals("")) {
			bean.setMessage("出库数量必须填写");
		}
		return bean;
	}

	public int checkBeanForUpdateOrAdd(DeliveryVoucherItemBean bean, List<DeliveryVoucherItemBean> dbExistItemList,
			int adminUser) {
		int result = -1;
		// 先判断是否存在于数据库
		DeliveryVoucherItemBean existBean = null;
		Iterator<DeliveryVoucherItemBean> iterable = dbExistItemList.iterator();
		while (iterable.hasNext()) {
			existBean = iterable.next();
			if (existBean.getWareType() == existBean.getWareType() && existBean.getWareId() == bean.getWareId()
					&& existBean.getInventoryType() == bean.getInventoryType()) {
				// 这个ITEM存在于数据库
				iterable.remove();
				break;
			} else {
				existBean = null;
			}
		}
		if (existBean != null) {
			// 存在就UPDATE
			bean.setDvItemId(existBean.getDvItemId());
			bean.setModifyUser(adminUser);
			result = updateDeliveryVoucherItem(bean);
		} else {
			// 不存在就ADD
			bean.setCreateUser(adminUser);
			result = addDeliveryVoucherItem(bean);
		}
		return result;
	}

	public int removeDeliveryVoucherItem(DeliveryVoucherItemBean bean) {
		DeliveryVoucherItemDao dao = new DeliveryVoucherItemDao();
		bean.setStatus(Constants.STATUS_NOT_VALID);
		int result = dao.updateDeliveryVoucherItem(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendRemoveLog(bean, InfoTypeConfig.INFO_TYPE_DELIVERY_VOUCHER, bean.getDvId(), bean.getDvItemId(), 0,
					DeliveryVoucherLogConfig.LOG_DELIVERY_VOUCHER_ITEM);
		}
		return result;
	}

	public boolean validateStockOutQuantity(List<DeliveryVoucherItemBean> itemList, int warehouseId) {
		WarehouseInventoryBusiness business = new WarehouseInventoryBusiness();
		for (DeliveryVoucherItemBean bean : itemList) {
			WarehouseInventoryBean inventoryBean = business.getWarehouseInventoryBeanByCondition(warehouseId,
					bean.getWareType(), bean.getWareId(), bean.getInventoryType(), bean.getBatchNumber(),
					bean.getProductionDate());
			BigDecimal inventoryQty = new BigDecimal(inventoryBean.getQuantity());
			BigDecimal StockOutQty = new BigDecimal(bean.getStockOutQuantity());
			// 库存小于要出库的数量
			if (inventoryQty.compareTo(StockOutQty) == -1) {
				return false;
			}
		}
		return true;
	}

}