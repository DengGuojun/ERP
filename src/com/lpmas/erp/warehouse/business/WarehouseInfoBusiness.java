package com.lpmas.erp.warehouse.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.inventory.business.DeliveryVoucherInfoBusiness;
import com.lpmas.erp.inventory.business.WarehouseVoucherInfoBusiness;
import com.lpmas.erp.purchase.business.PurchaseOrderInfoBusiness;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.erp.warehouse.bean.WarehouseInfoBean;
import com.lpmas.erp.warehouse.config.WarehouseLogConfig;
import com.lpmas.erp.warehouse.config.WarehousePropertyConfig;
import com.lpmas.erp.warehouse.dao.WarehouseInfoDao;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.db.SqlKit;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;

public class WarehouseInfoBusiness {
	public int addWarehouseInfo(WarehouseInfoBean bean) {
		WarehouseInfoDao dao = new WarehouseInfoDao();
		int result = dao.insertWarehouseInfo(bean);
		if (result > 0) {
			bean.setWarehouseId(result);
			ErpLogSender helper = new ErpLogSender();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_WAREHOUSE, bean.getWarehouseId(), WarehouseLogConfig.LOG_WAREHOUSE_INFO);
		}
		return result;
	}

	public int updateWarehouseInfo(WarehouseInfoBean bean) {
		WarehouseInfoDao dao = new WarehouseInfoDao();
		WarehouseInfoBean orginalBean = getWarehouseInfoByKey(bean.getWarehouseId());
		int result = dao.updateWarehouseInfo(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(orginalBean, bean, InfoTypeConfig.INFO_TYPE_WAREHOUSE, bean.getWarehouseId(), WarehouseLogConfig.LOG_WAREHOUSE_INFO);
		}
		return result;
	}

	public WarehouseInfoBean getWarehouseInfoByKey(int warehouseId) {
		WarehouseInfoDao dao = new WarehouseInfoDao();
		return dao.getWarehouseInfoByKey(warehouseId);
	}

	public WarehouseInfoBean getWarehouseInfoByNumber(String warehouseNumber) {
		WarehouseInfoDao dao = new WarehouseInfoDao();
		return dao.getWarehouseInfoByNumber(warehouseNumber);
	}

	public List<WarehouseInfoBean> getWarehouseInfoListByKeys(List<Integer> warehouseIdList) {
		WarehouseInfoDao dao = new WarehouseInfoDao();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		condMap.put("warehouseIds", SqlKit.getQueryStmt(warehouseIdList));
		return dao.getWarehouseInfoListByMap(condMap);
	}

	public PageResultBean<WarehouseInfoBean> getWarehouseInfoPageListByMap(HashMap<String, String> condMap, PageBean pageBean) {
		WarehouseInfoDao dao = new WarehouseInfoDao();
		return dao.getWarehouseInfoPageListByMap(condMap, pageBean);
	}

	public PageResultBean<WarehouseInfoBean> getWarehouseInfoPageListByFuzzyQueryParam(String param, PageBean pageBean) {
		WarehouseInfoDao dao = new WarehouseInfoDao();
		return dao.getWarehouseInfoPageListByFuzzyQueryParam(param, pageBean);
	}

	public List<WarehouseInfoBean> getWarehouseInfoAllList() {
		WarehouseInfoDao dao = new WarehouseInfoDao();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		return dao.getWarehouseInfoListByMap(condMap);
	}

	public List<WarehouseInfoBean> getWarehouseInfoListByMap(HashMap<String, String> condMap) {
		WarehouseInfoDao dao = new WarehouseInfoDao();
		return dao.getWarehouseInfoListByMap(condMap);
	}

	public List<WarehouseInfoBean> getWarehouseListByTypeAndIsDeleveryToCumstomer(String warehouseType, boolean isDeleveryToCumstomer) {
		WarehousePropertyBusiness propertyBusiness = new WarehousePropertyBusiness();
		Map<String, String> condMap = new HashMap<String, String>();
		condMap.put(WarehousePropertyConfig.PROPERTY_DELIVERY_TO_CUSTOMER,
				isDeleveryToCumstomer ? String.valueOf(Constants.STATUS_VALID) : String.valueOf(Constants.STATUS_NOT_VALID));
		List<Integer> warehouseIdList = propertyBusiness.getWarehouseIdListByMap(condMap);
		WarehouseInfoDao dao = new WarehouseInfoDao();
		return dao.getWarehouseListConditon(warehouseType, warehouseIdList);
	}

	public ReturnMessageBean verifyWarehouseInfo(WarehouseInfoBean bean) {
		ReturnMessageBean result = new ReturnMessageBean();
		if (!StringKit.isValid(bean.getWarehouseName())) {
			result.setMessage("仓库名称必须填写");
		} else if (!StringKit.isValid(bean.getWarehouseNumber())) {
			result.setMessage("仓库编号必须填写");
		} else if (!StringKit.isValid(bean.getWarehouseType())) {
			result.setMessage("仓库类型必须填写");
		} else if (!StringKit.isValid(bean.getContactName())) {
			result.setMessage("联系人必须填写");
		} else if (!StringKit.isValid(bean.getCountry())) {
			result.setMessage("国家必须填写");
		} else if (!StringKit.isValid(bean.getProvince())) {
			result.setMessage("省份必须填写");
		} else if (!StringKit.isValid(bean.getCity())) {
			result.setMessage("城市必须填写");
		} else if (!StringKit.isValid(bean.getRegion())) {
			result.setMessage("地区必须填写");
		} else if (!StringKit.isValid(bean.getAddress())) {
			result.setMessage("地址必须填写");
		}
		if (bean.getWarehouseId() > 0 && bean.getStatus() == Constants.STATUS_NOT_VALID) {
			// 采购订单下判断是否使用该仓库
			PurchaseOrderInfoBusiness purchaseOrderInfoBusiness = new PurchaseOrderInfoBusiness();
			// 入库单下判断是否使用该仓库
			WarehouseVoucherInfoBusiness warehouseVoucherInfoBusiness = new WarehouseVoucherInfoBusiness();
			// 出库单下判断是否使用该仓库
			DeliveryVoucherInfoBusiness deliveryVoucherInfoBusiness = new DeliveryVoucherInfoBusiness();
			if (purchaseOrderInfoBusiness.checkHasPurchaseOrderInfoByWarehouseId(bean.getWarehouseId())
					|| warehouseVoucherInfoBusiness.checkHasWarehouseVoucherInfoByWarehouseId(bean.getWarehouseId())
					|| deliveryVoucherInfoBusiness.checkHasDeliveryVoucherInfoByWarehouseId(bean.getWarehouseId())) {
				result.setMessage("该仓库已被使用，不能设置为无效");
			}
		}
		return result;
	}

}