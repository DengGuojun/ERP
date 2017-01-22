package com.lpmas.erp.report.business;

import com.lpmas.erp.report.config.ReportConsoleConfig;
import com.lpmas.framework.util.DateKit;

public class MonthlyInventorySummary {

	public static void summaryMonthlyInvenory(String[] args) {
		String date = DateKit.formatDate(DateKit.addTime(DateKit.REGEX_MONTH, -1, DateKit.getCurrentTimestamp()),
				ReportConsoleConfig.MONTHLY_REPORT_DATE_FORMAT);
		new MonthlyInventoryReportBusniess().summaryMonthlyInvenoryByDate(date);
	}
}
