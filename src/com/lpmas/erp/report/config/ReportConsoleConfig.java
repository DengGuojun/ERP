package com.lpmas.erp.report.config;

import java.util.HashSet;
import java.util.Set;

import com.lpmas.erp.config.ErpConsoleConfig;
import com.lpmas.erp.inventory.config.WarehouseInventoryConfig;

public class ReportConsoleConfig {

	public static final String REPORT_PAGE_PATH = ErpConsoleConfig.PAGE_PATH + "report/";

	public static final String MONTHLY_REPORT_DATE_FORMAT = "yyyyMM";
	public static final String MONTHLY_REPORT_NAME_PATTERN = "库存月报";
	public static final String IN_AND_OUT_DETAIL_REPORT_NAME_PATTERN = "出入库明细表";

	// 统计库存类型
	public static Set<Integer> EFFECT_INVENTORY_TYPE_SET = new HashSet<Integer>();

	static {
		initEffectInventoryTypeSet();
	}

	private static void initEffectInventoryTypeSet() {
		EFFECT_INVENTORY_TYPE_SET = new HashSet<Integer>();
		EFFECT_INVENTORY_TYPE_SET.add(WarehouseInventoryConfig.WIT_NORMAL);
		EFFECT_INVENTORY_TYPE_SET.add(WarehouseInventoryConfig.WIT_DAMAGE);
		EFFECT_INVENTORY_TYPE_SET.add(WarehouseInventoryConfig.WIT_DEFECT);
	}

}
