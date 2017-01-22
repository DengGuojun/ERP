package com.lpmas.erp.config;

public class ErpClientCacheConfig {

	public static final String GET_WAREHOUSE_LIST_BY_TYPE_AND_IS_DELIVERY_TO_CUSTOMER_CACHE_KEY = "GET_WAREHOUSE_LIST_BY_TYPE_AND_IS_DELIVERY_TO_CUSTOMER_";

	public static String getWarehouseListCacheKeyByTypeAndIsDeleveryToCumstomer(String warehouseType, boolean isDeliveryToCustomer) {
		return GET_WAREHOUSE_LIST_BY_TYPE_AND_IS_DELIVERY_TO_CUSTOMER_CACHE_KEY + warehouseType + "_" + isDeliveryToCustomer;
	}

	public static final String GET_WAREHOUSE_INFO_BY_KEY = "GET_WAREHOUSE_INFO_BY_KEY_";

	public static String getwarehouseInfoCacheKeyByKey(int warehouseId) {
		return GET_WAREHOUSE_INFO_BY_KEY + warehouseId;
	}

	public static final String GET_WAREHOUSE_INFO_BY_NUMBER = "GET_WAREHOUSE_INFO_BY_NUMBER_";

	public static String getwarehouseInfoCacheKeyByNumber(String warehouseNumber) {
		return GET_WAREHOUSE_INFO_BY_NUMBER + warehouseNumber;
	}

	public static final String GET_WAREHOUSE_TRANSFER_ORDERINFO_NUMBER_BY_ID_CACHE_KEY = "GET_WAREHOUSE_TRANSFER_ORDERINFO_NUMBER_BY_ID_";

	public static String getWarehouseTransferOrderinfoNumberCacheById(int toId) {
		return GET_WAREHOUSE_TRANSFER_ORDERINFO_NUMBER_BY_ID_CACHE_KEY + toId;
	}

	public static final String GET_DELIVERY_NOTE_INFO_NUMBER_BY_ID_CACHE_KEY = "GET_DELIVERY_NOTE_INFO_NUMBER_BY_ID_";

	public static String getDeliveryNoteInfoNumberCacheById(int dnId) {
		return GET_DELIVERY_NOTE_INFO_NUMBER_BY_ID_CACHE_KEY + dnId;
	}

}
