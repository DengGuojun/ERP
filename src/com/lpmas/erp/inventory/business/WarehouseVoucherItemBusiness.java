package com.lpmas.erp.inventory.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.inventory.bean.WarehouseVoucherItemBean;
import com.lpmas.erp.inventory.config.WarehouseVoucherLogConfig;
import com.lpmas.erp.inventory.dao.WarehouseVoucherItemDao;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.MaterialInfoBean;
import com.lpmas.pdm.bean.ProductItemBean;
import com.lpmas.pdm.client.PdmServiceClient;

public class WarehouseVoucherItemBusiness {
	public int addWarehouseVoucherItem(WarehouseVoucherItemBean bean) {
		WarehouseVoucherItemDao dao = new WarehouseVoucherItemDao();
		int result = dao.insertWarehouseVoucherItem(bean);
		if (result > 0) {
			bean.setWvItemId(result);
			ErpLogSender helper = new ErpLogSender();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_WAREHOUSE_VOUCHER, bean.getWvId(), bean.getWvItemId(), 0,
					WarehouseVoucherLogConfig.LOG_WV_ITEM);
		}
		return result;
	}

	public int updateWarehouseVoucherItem(WarehouseVoucherItemBean bean) {
		WarehouseVoucherItemDao dao = new WarehouseVoucherItemDao();
		WarehouseVoucherItemBean orgBean = getWarehouseVoucherItemByKey(bean.getWvItemId());
		int result = dao.updateWarehouseVoucherItem(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(orgBean, bean, InfoTypeConfig.INFO_TYPE_WAREHOUSE_VOUCHER, bean.getWvId(),
					bean.getWvItemId(), 0, WarehouseVoucherLogConfig.LOG_WV_ITEM);
		}
		return result;
	}

	public WarehouseVoucherItemBean getWarehouseVoucherItemByKey(int wvItmeId) {
		WarehouseVoucherItemDao dao = new WarehouseVoucherItemDao();
		return dao.getWarehouseVoucherItemByKey(wvItmeId);
	}

	/***
	 * 获取特定的入库单下有效的所有ITEM
	 * 
	 * @param wvId
	 * @return
	 */
	public List<WarehouseVoucherItemBean> getWarehouseVoucherItemListByWvId(int wvId) {
		WarehouseVoucherItemDao dao = new WarehouseVoucherItemDao();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("wvId", String.valueOf(wvId));
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		return dao.getWarehouseVoucherItemListByMap(condMap);
	}

	/***
	 * 获取特定的入库单下的所有ITEM，包括状态为0的
	 * 
	 * @param wvId
	 * @return
	 */
	public List<WarehouseVoucherItemBean> getWarehouseVoucherItemAllListByWvId(int wvId) {
		WarehouseVoucherItemDao dao = new WarehouseVoucherItemDao();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("wvId", String.valueOf(wvId));
		return dao.getWarehouseVoucherItemListByMap(condMap);
	}

	public PageResultBean<WarehouseVoucherItemBean> getWarehouseVoucherItemPageListByMap(
			HashMap<String, String> condMap, PageBean pageBean) {
		WarehouseVoucherItemDao dao = new WarehouseVoucherItemDao();
		return dao.getWarehouseVoucherItemPageListByMap(condMap, pageBean);
	}

	public int checkBeanForUpdateOrAdd(WarehouseVoucherItemBean bean,
			Map<Integer, WarehouseVoucherItemBean> dbExistItemMap, int adminUser) {
		int result = -1;
		// 先判断是否存在于数据库
		WarehouseVoucherItemBean existBean = dbExistItemMap.get(bean.getWareId());
		if (existBean != null) {
			// 存在就UPDATE
			bean.setWvItemId(existBean.getWvItemId());
			bean.setModifyUser(adminUser);
			updateWarehouseVoucherItem(bean);
		} else {
			// 不存在就ADD
			bean.setCreateUser(adminUser);
			addWarehouseVoucherItem(bean);
		}
		return result;
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

	public ReturnMessageBean validateWvDetail(Map<String, String> itemMap, Boolean hasExpiration) {
		ReturnMessageBean bean = new ReturnMessageBean();
		if (itemMap.get("unit").equals("")) {
			bean.setMessage("采购计量单位必须填写");
		} else if (itemMap.get("receivableQuantity").equals("")) {
			bean.setMessage("应收数量必须填写");
		} else if (itemMap.get("stockInQuantity").equals("")) {
			bean.setMessage("实收数量必须填写");
		} else if (itemMap.get("productionDate").equals("")) {
			bean.setMessage("生产日期必须填写");
		} else if (itemMap.get("guaranteePeriod").equals("") && hasExpiration) {
			bean.setMessage("保质期必须填写");
		} else if (itemMap.get("expirationDate").equals("") && hasExpiration) {
			bean.setMessage("保质期至必须填写");
		} else if (itemMap.get("batchNumber").equals("")) {
			bean.setMessage("批次号必须填写");
		}
		return bean;
	}

	public int removeWarehouseVoucherItem(WarehouseVoucherItemBean bean) {
		WarehouseVoucherItemDao dao = new WarehouseVoucherItemDao();
		bean.setStatus(Constants.STATUS_NOT_VALID);
		int result = dao.updateWarehouseVoucherItem(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendRemoveLog(bean, InfoTypeConfig.INFO_TYPE_WAREHOUSE_VOUCHER, bean.getWvId(), bean.getWvItemId(),
					0, WarehouseVoucherLogConfig.LOG_WV_ITEM);
		}
		return result;
	}

}