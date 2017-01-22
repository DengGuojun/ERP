package com.lpmas.erp.inventory.config;

import java.util.HashMap;
import java.util.Map;

public class WarehouseTransferLogDisplayConfig {
	public static Map<String, Map<?, ?>> CONFIG_VALUE_MAP = new HashMap<String, Map<?, ?>>();
	static {
		CONFIG_VALUE_MAP = new HashMap<String, Map<?, ?>>();
		CONFIG_VALUE_MAP.put("审核状态", WarehouseTransferOrderConfig.APPROVE_STATUS_MAP);
		CONFIG_VALUE_MAP.put("调拨单状态", WarehouseTransferOrderConfig.TO_STATUS_MAP);
		CONFIG_VALUE_MAP.put("制品类型", InventoryConsoleConfig.WARE_TYPE_MAP);
	}
}
