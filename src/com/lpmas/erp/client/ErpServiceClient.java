package com.lpmas.erp.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.erp.bean.OrderInfoBean;
import com.lpmas.erp.component.ErpServicePrx;
import com.lpmas.erp.config.ErpClientConfig;
import com.lpmas.erp.config.ErpConsoleConfig;
import com.lpmas.erp.warehouse.bean.WarehouseInfoBean;
import com.lpmas.framework.component.ComponentClient;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;

public class ErpServiceClient {

	private static Logger log = LoggerFactory.getLogger(ErpServiceClient.class);

	private String rpc(String method, String params) {
		ComponentClient client = new ComponentClient();
		ErpServicePrx erpServicePrx = (ErpServicePrx) client.getProxy(ErpConsoleConfig.APP_ID, ErpServicePrx.class);
		String result = erpServicePrx.rpc(method, params);
		log.info("result : {}", result);
		return result;
	}

	public WarehouseInfoBean getWarehouseInfoByKey(int warehouseId) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("warehouseId", String.valueOf(warehouseId));
		return JsonKit.toBean(rpc(ErpClientConfig.GET_WAREHOUSE_INFO_BY_KEY, JsonKit.toJson(param)), WarehouseInfoBean.class);
	}

	public WarehouseInfoBean getWarehouseInfoByNumber(String warehouseNumber) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("warehouseNumber", warehouseNumber);
		return JsonKit.toBean(rpc(ErpClientConfig.GET_WAREHOUSE_INFO_BY_NUMBER, JsonKit.toJson(param)), WarehouseInfoBean.class);
	}

	public List<WarehouseInfoBean> getWarehouseListByTypeAndIsDeleveryToCumstomer(String warehouseType, boolean isDeliveryToCustomer) {
		Map<String, String> condMap = new HashMap<String, String>();
		condMap.put("warehouseType", warehouseType);
		condMap.put("isDeliveryToCustomer",
				isDeliveryToCustomer ? String.valueOf(Constants.STATUS_VALID) : String.valueOf(Constants.STATUS_NOT_VALID));
		String remoteResult = rpc(ErpClientConfig.GET_WAREHOUSE_LIST_BY_TYPE_AND_IS_DELIVERY_TO_CUSTOMER, JsonKit.toJson(condMap));
		try {
			return JsonKit.toList(remoteResult, WarehouseInfoBean.class);
		} catch (Exception e) {
			return new ArrayList<WarehouseInfoBean>(1);
		}
	}

	public OrderInfoBean getOrderInfoByKey(int orderType, int orderId) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("orderType", String.valueOf(orderType));
		param.put("orderId", String.valueOf(orderId));
		OrderInfoBean orderInfoBean = JsonKit.toBean(rpc(ErpClientConfig.GET_ORDER_BY_KEY, JsonKit.toJson(param)), OrderInfoBean.class);
		return orderInfoBean.getOrderId() == orderId ? orderInfoBean : null;
	}

	public OrderInfoBean getOrderInfoByKey(int orderType, String orderNumber) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("orderType", String.valueOf(orderType));
		param.put("orderNumber", orderNumber);
		OrderInfoBean orderInfoBean = JsonKit.toBean(rpc(ErpClientConfig.GET_ORDER_BY_NUMBER, JsonKit.toJson(param)), OrderInfoBean.class);
		return orderInfoBean.getOrderNumber().equals(orderNumber) ? orderInfoBean : null;
	}

}
