package com.lpmas.erp.inventory.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.util.StatusKit;

public class WarehouseTransferOrderLogConfig {
	
	// 出库操作日志配置项
		public static String LOG_WAREHOUSE_TRANSFER_ORDER_INFO = "WAREHOUSE_TRANSFER_ORDER_INFO";
		public static String LOG_WAREHOUSE_TRANSFER_ORDER_ITEM = "WAREHOUSE_TRANSFER_ORDER_ITEM";
		public static List<StatusBean<String, String>> LOG_WAREHOUSE_TRANSFER_ORDER_LIST = new ArrayList<StatusBean<String, String>>();
		public static Map<String, String> LOG_WAREHOUSE_TRANSFER_ORDER_MAP = new HashMap<String, String>();

		static {

			initLogDeliveryVoucherList();
			initLogDeliveryVoucherMap();

		}

		public static void initLogDeliveryVoucherList() {
			LOG_WAREHOUSE_TRANSFER_ORDER_LIST = new ArrayList<StatusBean<String, String>>();
			LOG_WAREHOUSE_TRANSFER_ORDER_LIST.add(new StatusBean<String, String>(LOG_WAREHOUSE_TRANSFER_ORDER_INFO, "调拨单"));
			LOG_WAREHOUSE_TRANSFER_ORDER_LIST.add(new StatusBean<String, String>(LOG_WAREHOUSE_TRANSFER_ORDER_ITEM, "调拨单明细"));
		}

		public static void initLogDeliveryVoucherMap() {
			LOG_WAREHOUSE_TRANSFER_ORDER_MAP = new HashMap<String, String>();
			LOG_WAREHOUSE_TRANSFER_ORDER_MAP = StatusKit.toMap(LOG_WAREHOUSE_TRANSFER_ORDER_LIST);
		}

}
