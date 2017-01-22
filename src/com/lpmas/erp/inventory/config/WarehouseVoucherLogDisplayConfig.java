package com.lpmas.erp.inventory.config;

import java.util.HashMap;
import java.util.Map;

public class WarehouseVoucherLogDisplayConfig {
	public static Map<String, Map<?, ?>> CONFIG_VALUE_MAP = new HashMap<String, Map<?, ?>>();
	static {
		CONFIG_VALUE_MAP = new HashMap<String, Map<?, ?>>();
		CONFIG_VALUE_MAP.put("源单类型", SourceOrderTypeConfig.SOURCE_ORDER_TYPE_MAP);
		CONFIG_VALUE_MAP.put("入库类型", WarehouseVoucherConfig.WAREHOUSE_VOUCHER_TYPE_MAP);
		CONFIG_VALUE_MAP.put("审核状态", WarehouseVoucherConfig.REVIEW_STATUS_MAP);
		CONFIG_VALUE_MAP.put("同步状态", WarehouseVoucherConfig.SYNC_STATUS_MAP);
		CONFIG_VALUE_MAP.put("入库单状态", WarehouseVoucherConfig.WV_STATUS_MAP);
		CONFIG_VALUE_MAP.put("制品类型", InventoryConsoleConfig.WARE_TYPE_MAP);
	}

}
