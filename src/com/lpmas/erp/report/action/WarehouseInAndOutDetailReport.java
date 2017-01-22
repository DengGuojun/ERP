package com.lpmas.erp.report.action;

import java.io.IOException;
import java.util.ArrayList;
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
import com.lpmas.erp.inventory.bean.WarehouseInventoryBean;
import com.lpmas.erp.inventory.business.WarehouseInventoryBusiness;
import com.lpmas.erp.report.business.WarehouseInAndOutDetailReportBusiness;
import com.lpmas.erp.report.config.ReportConsoleConfig;
import com.lpmas.erp.report.config.ReportResource;
import com.lpmas.framework.excel.ExcelWriteBean;
import com.lpmas.framework.excel.WebExcelWriteKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.pdm.bean.MaterialInfoBean;
import com.lpmas.pdm.bean.ProductItemBean;
import com.lpmas.pdm.client.PdmServiceClient;

/**
 * Servlet implementation class WarehouseInAndOutDetailReport
 */
@WebServlet("/erp/WarehouseInAndOutDetailReport.do")
public class WarehouseInAndOutDetailReport extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(WarehouseInAndOutDetailReport.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WarehouseInAndOutDetailReport() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(ReportResource.ERP_REPORT_WAREHOUSE_IN_OUT, OperationConfig.EXPORT)) {
			return;
		}

		String openDateTime = ParamKit.getParameter(request, "openDateTime", "");
		String endDateTime = ParamKit.getParameter(request, "endDateTime", "");
		int wareType = ParamKit.getIntParameter(request, "wareType", 0);
		int wareId = ParamKit.getIntParameter(request, "wareId", 0);
		boolean isExport = ParamKit.getBooleanParameter(request, "isExport", false);

		// 数据校验
		if (StringKit.isValid(openDateTime) && StringKit.isValid(endDateTime) && wareType != 0 && wareId != 0) {
			try {
				// 对象准备
				WarehouseInAndOutDetailReportBusiness warehouseInAndOutDetailReportBusiness = new WarehouseInAndOutDetailReportBusiness();
				PdmServiceClient pdmServiceClient = new PdmServiceClient();
				MaterialInfoBean materialInfoBean = null;
				ProductItemBean productItemBean = null;
				String itemName = "";
				String itemUnit = "";
				WebExcelWriteKit excelWriteKit = new WebExcelWriteKit();
				WarehouseInventoryBusiness warehouseInventoryBusiness = new WarehouseInventoryBusiness();
				List<WarehouseInventoryBean> inventoryList = null;

				// 处理制品信息
				boolean isVaildWare = false;
				if (wareType == InfoTypeConfig.INFO_TYPE_MATERIAL) {
					materialInfoBean = pdmServiceClient.getMaterialInfoByKey(wareId);
					if (materialInfoBean != null && materialInfoBean.getMaterialId() == wareId) {
						itemName = materialInfoBean.getMaterialName();
						itemUnit = materialInfoBean.getUnit();
						isVaildWare = true;
					}
				} else if (wareType == InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM) {
					productItemBean = pdmServiceClient.getProductItemByKey(wareId);
					if (productItemBean != null && productItemBean.getItemId() == wareId) {
						itemName = productItemBean.getItemName();
						itemUnit = productItemBean.getUnit();
						isVaildWare = true;
					}
				}
				if (!isVaildWare) {
					HttpResponseKit.alertMessage(response, "不存在该制品", "/erp/WarehouseInAndOutDetailReport.do");
					return;
				}
				// 查出该制品的库存记录
				inventoryList = warehouseInventoryBusiness.getWareWarehouseInventoryListByInventoryType(wareType, wareId);
				if (inventoryList.isEmpty()) {
					HttpResponseKit.alertMessage(response, "该制品无库存记录，不能导出", "/erp/WarehouseInAndOutDetailReport.do");
					return;
				}
				ExcelWriteBean excelWriteBean = warehouseInAndOutDetailReportBusiness.createWarehouseInAndOutReport(wareType, wareId, openDateTime,
						endDateTime, itemName, itemUnit, inventoryList, adminUserHelper.getAdminUserInfo().getAdminUserName(), isExport);
				// 输出到页面
				if (isExport) {
					excelWriteKit.outputExcel(excelWriteBean, request, response);
					return;
				} else {
					// 头部
					// 处理制品名，制表人
					List<String> headerList = new ArrayList<String>();
					headerList.add("品名:");
					headerList.add(itemName);
					for (int i = 0; i < 11; i++) {
						headerList.add("");
					}
					headerList.add("制表人:");
					headerList.add(adminUserHelper.getAdminUserInfo().getAdminUserName());
					request.setAttribute("headerList", headerList);
					request.setAttribute("reprotContent", excelWriteBean.getContentList());
					request.setAttribute("AdminUserHelper", adminUserHelper);
				}
			} catch (Exception e) {
				logger.error("", e);
				HttpResponseKit.alertMessage(response, "导出失败:" + e.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
		} else if (isExport) {
			HttpResponseKit.alertMessage(response, "参数错误", "/erp/WarehouseInAndOutDetailReport.do");
			return;
		}
		if (!isExport) {
			// 请求转发
			RequestDispatcher rd = request.getRequestDispatcher(ReportConsoleConfig.REPORT_PAGE_PATH + "WarehouseInAndOutDetailReport.jsp");
			rd.forward(request, response);
		}
	}
}
