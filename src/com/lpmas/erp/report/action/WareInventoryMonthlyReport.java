package com.lpmas.erp.report.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.report.business.MonthlyInventoryReportBusniess;
import com.lpmas.erp.report.business.ReportUtil;
import com.lpmas.erp.report.config.ReportConsoleConfig;
import com.lpmas.erp.report.config.ReportResource;
import com.lpmas.erp.warehouse.bean.WarehouseInfoBean;
import com.lpmas.erp.warehouse.business.WarehouseInfoBusiness;
import com.lpmas.framework.excel.ExcelWriteBean;
import com.lpmas.framework.excel.WebExcelWriteKit;
import com.lpmas.framework.util.DateKit;
import com.lpmas.framework.util.ListKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;

/**
 * Servlet implementation class WareInventoryMonthlyReport
 */
@WebServlet("/erp/WareInventoryMonthlyReport.do")
public class WareInventoryMonthlyReport extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(WareInventoryMonthlyReport.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WareInventoryMonthlyReport() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(ReportResource.ERP_REPORT_WI_MONTHLY, OperationConfig.EXPORT)) {
			return;
		}

		String reportMonth = ParamKit.getParameter(request, "reportMonth", "").trim();
		int wareType = ParamKit.getIntParameter(request, "wareType", 0);
		String warehouseIds = ParamKit.getParameter(request, "warehouseIds", "").trim();
		boolean isExport = ParamKit.getBooleanParameter(request, "isExport", false);
		boolean isShowWarehouseInfo = false;
		
		WarehouseInfoBusiness warehouseInfoBusiness = new WarehouseInfoBusiness();
		List<WarehouseInfoBean> allWarehouse = warehouseInfoBusiness.getWarehouseInfoAllList();
		warehouseIds = ListKit.list2String(allWarehouse, "warehouseId",",");
		request.setAttribute("warehouseIds", warehouseIds);
		request.setAttribute("AdminUserHelper", adminUserHelper);

		// 数据校验
		if (StringKit.isValid(reportMonth)
				&& (wareType == InfoTypeConfig.INFO_TYPE_MATERIAL || wareType == InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM)
				&& StringKit.isValid(warehouseIds)) {
			try {
				// 查出仓库
				List<Integer> warehouseIdList = new ArrayList<Integer>();
				ListKit.string2List(warehouseIds, ",").forEach(id->warehouseIdList.add(Integer.valueOf(id)));
				List<WarehouseInfoBean> warehouseInfoList = warehouseInfoBusiness.getWarehouseInfoListByKeys(warehouseIdList);
				if (warehouseInfoList.isEmpty()) {
					HttpResponseKit.alertMessage(response, "仓库不存在", HttpResponseKit.ACTION_HISTORY_BACK);
					return;
				}
				if(allWarehouse.size()<warehouseInfoList.size()) isShowWarehouseInfo = true;
				
				// 验证一下查询月份
				Date endDate = DateKit.str2Date(reportMonth, ReportConsoleConfig.MONTHLY_REPORT_DATE_FORMAT);
				Date openDate = DateKit.addTime(DateKit.REGEX_MONTH, -1, endDate);

				String queryOpenMonth = DateKit.formatDate(openDate, ReportConsoleConfig.MONTHLY_REPORT_DATE_FORMAT);
				String queryEndMonth = DateKit.formatDate(endDate, ReportConsoleConfig.MONTHLY_REPORT_DATE_FORMAT);

				// 检查时间
				if (!DateKit.compareTime(ReportUtil.getLastSecondOfMonth(endDate))) {
					HttpResponseKit.alertMessage(response, "当前日期小于报表日期，不能生成报表", HttpResponseKit.ACTION_HISTORY_BACK);
					return;
				}

				// 对象准备
				WebExcelWriteKit excelWriteKit = new WebExcelWriteKit();
				MonthlyInventoryReportBusniess monthlyInventoryReportBusniess = new MonthlyInventoryReportBusniess();
				ExcelWriteBean excelWriteBean = monthlyInventoryReportBusniess.createMonthlyReport(wareType,
						warehouseInfoList, queryOpenMonth, queryEndMonth, reportMonth,
						adminUserHelper.getAdminUserInfo().getAdminUserName(), isExport);
				List<List<Object>> warehouseInfoExcelList = monthlyInventoryReportBusniess.getWarehouseInfoExcelList(warehouseInfoList, 16);
				// 输出到页面
				if (isExport) {
					if(isShowWarehouseInfo){
						List<List<Object>> content = excelWriteBean.getContentList();
						content = ListKit.combineList(content, warehouseInfoExcelList);
						excelWriteBean.setContentList(content);
					}
					excelWriteKit.outputExcel(excelWriteBean, request, response);
					return;
				} else {
					request.setAttribute("reprotContent", excelWriteBean.getContentList());
					request.setAttribute("warehouseInfoExcelList", warehouseInfoExcelList);
				}

			} catch (Exception e) {
				logger.error("", e);
				HttpResponseKit.alertMessage(response, "导出失败:" + e.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
		} else if (isExport) {
			HttpResponseKit.alertMessage(response, "参数错误", "/erp/WareInventoryMonthlyReport.do");
			return;
		}
		request.setAttribute("isShowWarehouseInfo", isShowWarehouseInfo);
		if (!isExport) {
			// 请求转发
			RequestDispatcher rd = request
					.getRequestDispatcher(ReportConsoleConfig.REPORT_PAGE_PATH + "WareInventoryMonthlyReport.jsp");
			rd.forward(request, response);
		}
	}

}
