package com.lpmas.erp.report.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.erp.report.business.MonthlyInventoryReportBusniess;
import com.lpmas.erp.report.config.ReportConsoleConfig;
import com.lpmas.erp.report.config.ReportResource;
import com.lpmas.framework.util.DateKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;

/**
 * Servlet implementation class Temp
 */
@WebServlet("/erp/MonthlyInventoryReportMenualSummary.do")
public class MonthlyInventoryReportMenualSummary extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MonthlyInventoryReportMenualSummary() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(ReportResource.ERP_REPORT_INFO, OperationConfig.EXPORT)) {
			return;
		}

		request.setAttribute("AdminUserHelper", adminUserHelper);
		// 请求转发
		RequestDispatcher rd = request
				.getRequestDispatcher(ReportConsoleConfig.REPORT_PAGE_PATH + "MonthlyInventoryReportMenualSummary.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(ReportResource.ERP_REPORT_INFO, OperationConfig.EXPORT)) {
			return;
		}

		String reportMonth = ParamKit.getParameter(request, "reportMonth", "").trim();

		try {
			// 验证一下查询月份
			DateKit.str2Date(reportMonth, ReportConsoleConfig.MONTHLY_REPORT_DATE_FORMAT);
			String currentMonth = DateKit.getCurrentDateTime(ReportConsoleConfig.MONTHLY_REPORT_DATE_FORMAT);
			if (!DateKit.compareTime(reportMonth, currentMonth, ReportConsoleConfig.MONTHLY_REPORT_DATE_FORMAT)) {
				HttpResponseKit.alertMessage(response, "不能汇总未来的月份", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			boolean result = new MonthlyInventoryReportBusniess().summaryMonthlyInvenoryByDate(reportMonth);
			if (result) {
				HttpResponseKit.alertMessage(response, "汇总成功", HttpResponseKit.ACTION_HISTORY_BACK);
			} else {
				HttpResponseKit.alertMessage(response, "汇总失败", HttpResponseKit.ACTION_HISTORY_BACK);
			}
		} catch (Exception e) {
			HttpResponseKit.alertMessage(response, "汇总失败:" + e.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}
	}

}
