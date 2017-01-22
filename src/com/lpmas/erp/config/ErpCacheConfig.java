package com.lpmas.erp.config;

import java.text.MessageFormat;

public class ErpCacheConfig {

	public static final String WAREHOUSE_INFO_CACHE_KEY = "WAREHOUSE_INFO_CACHE_KEY_";
	public static final String ORDER_INFO_CACHE_KEY = "WARE_INFO_CACHE_KEY";
	public static final String ORDER_INFO_CACHE_NUMBER = "WARE_INFO_NUMBER_CACHE_KEY";

	public static String getWarehouseInfoCacheKey(int warehouseId) {
		return WAREHOUSE_INFO_CACHE_KEY + warehouseId;
	}

	public static String getOrderInfoCacheKey(int orderType, int orderId) {
		return MessageFormat.format("{0}_{1}_{2}", ORDER_INFO_CACHE_KEY, orderType, orderId);
	}

	public static String getOrderInfoCacheKey(int orderType, String orderNumber) {
		return MessageFormat.format("{0}_{1}_{2}", ORDER_INFO_CACHE_NUMBER, orderType, orderNumber);
	}
}
