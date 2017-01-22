package com.lpmas.erp.inventory.config;

import java.util.HashMap;
import java.util.Map;

public class DeliveryVoucherLogDisplayConfig {
	public static Map<String, Map<?, ?>> CONFIG_VALUE_MAP = new HashMap<String, Map<?, ?>>();
	static {
		CONFIG_VALUE_MAP = new HashMap<String, Map<?, ?>>();
		CONFIG_VALUE_MAP.put("出库类型", DeliveryVoucherConfig.DELIVERY_VOUCHER_TYPE_MAP);
		CONFIG_VALUE_MAP.put("审核状态", DeliveryVoucherConfig.REVIEW_STATUS_MAP);
		CONFIG_VALUE_MAP.put("同步状态", DeliveryVoucherConfig.SYNC_STATUS_MAP);
		CONFIG_VALUE_MAP.put("出库单状态", DeliveryVoucherConfig.DV_STATUS_MAP);
		CONFIG_VALUE_MAP.put("制品类型", InventoryConsoleConfig.WARE_TYPE_MAP);
	}

}
