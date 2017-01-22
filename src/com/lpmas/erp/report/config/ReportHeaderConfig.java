package com.lpmas.erp.report.config;

import java.util.ArrayList;
import java.util.List;

import com.lpmas.erp.report.business.ReportUtil;

public class ReportHeaderConfig {
	public static List<Object> MONTHLY_INVENTORY_REPORT_HEADER_LIST = new ArrayList<Object>();// 补充表头
	public static List<Object> MONTHLY_INVENTORY_REPORT_SECOND_HEADER_LIST = new ArrayList<Object>();// 处理栏位
	static {
		initMonthlyInventoryReportHeaderList();
		initMonthlyInventoryReportSecondHeaderList();
	}

	public static void initMonthlyInventoryReportHeaderList() {
		MONTHLY_INVENTORY_REPORT_HEADER_LIST = new ArrayList<Object>();
		ReportUtil.putEmptyCell(MONTHLY_INVENTORY_REPORT_HEADER_LIST, 3);
		MONTHLY_INVENTORY_REPORT_HEADER_LIST.add("期初库存");
		ReportUtil.putEmptyCell(MONTHLY_INVENTORY_REPORT_HEADER_LIST, 2);
		MONTHLY_INVENTORY_REPORT_HEADER_LIST.add("本期入库");
		ReportUtil.putEmptyCell(MONTHLY_INVENTORY_REPORT_HEADER_LIST, 2);
		MONTHLY_INVENTORY_REPORT_HEADER_LIST.add("本期出库");
		ReportUtil.putEmptyCell(MONTHLY_INVENTORY_REPORT_HEADER_LIST, 2);
		MONTHLY_INVENTORY_REPORT_HEADER_LIST.add("期末库存");
	}

	public static void initMonthlyInventoryReportSecondHeaderList() {
		MONTHLY_INVENTORY_REPORT_SECOND_HEADER_LIST.add("品名");
		MONTHLY_INVENTORY_REPORT_SECOND_HEADER_LIST.add("存放地");
		MONTHLY_INVENTORY_REPORT_SECOND_HEADER_LIST.add("单位");
		MONTHLY_INVENTORY_REPORT_SECOND_HEADER_LIST.add("数量");
		MONTHLY_INVENTORY_REPORT_SECOND_HEADER_LIST.add("单价");
		MONTHLY_INVENTORY_REPORT_SECOND_HEADER_LIST.add("金额");
		MONTHLY_INVENTORY_REPORT_SECOND_HEADER_LIST.add("数量");
		MONTHLY_INVENTORY_REPORT_SECOND_HEADER_LIST.add("单价");
		MONTHLY_INVENTORY_REPORT_SECOND_HEADER_LIST.add("金额");
		MONTHLY_INVENTORY_REPORT_SECOND_HEADER_LIST.add("数量");
		MONTHLY_INVENTORY_REPORT_SECOND_HEADER_LIST.add("单价");
		MONTHLY_INVENTORY_REPORT_SECOND_HEADER_LIST.add("金额");
		MONTHLY_INVENTORY_REPORT_SECOND_HEADER_LIST.add("数量");
		MONTHLY_INVENTORY_REPORT_SECOND_HEADER_LIST.add("单价");
		MONTHLY_INVENTORY_REPORT_SECOND_HEADER_LIST.add("金额");
		MONTHLY_INVENTORY_REPORT_SECOND_HEADER_LIST.add("备注");

	}
}
