package com.lpmas.erp.component.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lpmas.erp.bean.OrderInfoBean;
import com.lpmas.erp.cache.OrderInfoCache;
import com.lpmas.erp.component._ErpServiceDisp;
import com.lpmas.erp.config.ErpClientConfig;
import com.lpmas.erp.warehouse.bean.WarehouseInfoBean;
import com.lpmas.erp.warehouse.business.WarehouseInfoBusiness;
import com.lpmas.framework.util.BooleanKit;
import com.lpmas.framework.util.JsonKit;

import Ice.Current;

public class ErpServiceImpl extends _ErpServiceDisp {
	private static final long serialVersionUID = 1L;

	@Override
	public String rpc(String method, String params, Current __current) {
		if (method.equals(ErpClientConfig.GET_WAREHOUSE_LIST_BY_TYPE_AND_IS_DELIVERY_TO_CUSTOMER)) {
			return getWarehouseListByTypeAndIsDeleveryToCumstomer(params);
		} else if (method.equals(ErpClientConfig.GET_WAREHOUSE_INFO_BY_KEY)) {
			return getWarehouseInfoByKey(params);
		} else if (method.equals(ErpClientConfig.GET_WAREHOUSE_INFO_BY_NUMBER)) {
			return getWarehouseInfoByNumber(params);
		} else if (method.equals(ErpClientConfig.GET_ORDER_BY_KEY)) {
			return getOrderByKey(params);
		} else if (method.equals(ErpClientConfig.GET_ORDER_BY_NUMBER)) {
			return getOrderInfoByNumber(params);
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	private String getWarehouseListByTypeAndIsDeleveryToCumstomer(String params) {
		Map<String, String> condMap = JsonKit.toBean(params, Map.class);
		String warehouseType = condMap.get("warehouseType");
		boolean isDeliveryToCustomer = BooleanKit.toBoolean(Integer.valueOf(condMap.get("isDeliveryToCustomer")));
		WarehouseInfoBusiness warehouseInfoBusiness = new WarehouseInfoBusiness();
		List<WarehouseInfoBean> result = warehouseInfoBusiness.getWarehouseListByTypeAndIsDeleveryToCumstomer(warehouseType, isDeliveryToCustomer);
		if (result == null)
			result = new ArrayList<WarehouseInfoBean>(1);
		return JsonKit.toJson(result);
	}

	private String getWarehouseInfoByKey(String params) {
		Map<String, String> condMap = JsonKit.toBean(params, Map.class);
		String warehouseId = condMap.get("warehouseId");
		WarehouseInfoBusiness business = new WarehouseInfoBusiness();
		WarehouseInfoBean bean = business.getWarehouseInfoByKey(Integer.valueOf(warehouseId));
		return JsonKit.toJson(bean);
	}

	private String getWarehouseInfoByNumber(String params) {
		Map<String, String> condMap = JsonKit.toBean(params, Map.class);
		String warehouseNumber = condMap.get("warehouseNumber");
		WarehouseInfoBusiness business = new WarehouseInfoBusiness();
		WarehouseInfoBean bean = business.getWarehouseInfoByNumber(warehouseNumber);
		return JsonKit.toJson(bean);
	}

	@SuppressWarnings("unchecked")
	private String getOrderByKey(String params) {
		HashMap<String, String> paramMap = JsonKit.toBean(params, HashMap.class);
		int orderId = Integer.valueOf(paramMap.get("orderId"));
		int orderType = Integer.valueOf(paramMap.get("orderType"));
		OrderInfoCache cache = new OrderInfoCache();
		OrderInfoBean bean = cache.getOrderInfoByKey(orderType, orderId);
		if (bean == null) {
			bean = new OrderInfoBean();
		}
		return JsonKit.toJson(bean);
	}

	@SuppressWarnings("unchecked")
	private String getOrderInfoByNumber(String params) {
		HashMap<String, String> paramMap = JsonKit.toBean(params, HashMap.class);
		String orderNumber = paramMap.get("orderNumber");
		int orderType = Integer.valueOf(paramMap.get("orderType"));
		OrderInfoCache cache = new OrderInfoCache();
		OrderInfoBean bean = cache.getOrderInfoByKey(orderType, orderNumber);
		if (bean == null) {
			bean = new OrderInfoBean();
		}
		return JsonKit.toJson(bean);
	}
}
