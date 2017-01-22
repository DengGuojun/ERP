package com.lpmas.erp.config;

import com.lpmas.erp.inventory.bean.WarehouseInventoryAggregateReportBean;
import com.lpmas.erp.inventory.bean.WarehouseInventoryReportBean;
import com.lpmas.framework.crypto.MD5;

public class ErpMongoConfig {

	public static final String DB_NAME = "erp";

	public static final String COLLECTION_WAREHOUSE_INVENTORY_REPORT = "warehouse_inventory_report";

	public static final String COLLECTION_WAREHOUSE_INVENTORY_AGGREGATE_REPORT = "warehouse_inventory_aggregate_report";

	public static final String COLLECTION_MONTHLY_INVENTORY_REPORT = "monthly_inventory_report";

	public static String getWarehouseInventoryReportUnqueId(WarehouseInventoryReportBean bean) {
		String key = bean.getWarehouseId() + "-" + bean.getWareType() + "-" + bean.getWareId() + "-"
				+ bean.getBatchNumber() + "-" + bean.getProductionDate();
		return MD5.getMD5(key);
	}

	public static String getWarehouseInventoryAggregateReportUnqueId(WarehouseInventoryAggregateReportBean bean) {
		String key = bean.getWarehouseId() + "-" + bean.getWareType() + "-" + bean.getWareId();
		return MD5.getMD5(key);
	}

	public static String getMonthlyInventoryReportKey(int wareType, int wareId, int inventoryType, int warehouseId,
			String reportEndDate) {
		return MD5.getMD5(wareType + "_" + wareId + "_" + inventoryType + "_" + warehouseId + "_" + reportEndDate);
	}
}
