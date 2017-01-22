package com.lpmas.erp.inventory.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.erp.config.ErpConsoleConfig;
import com.lpmas.erp.inventory.bean.WarehouseInventoryReportBean;
import com.lpmas.erp.inventory.bean.WarehouseVoucherInfoBean;
import com.lpmas.erp.inventory.business.WarehouseInventoryReportBusiness;
import com.lpmas.erp.inventory.business.WarehouseVoucherInfoBusiness;
import com.lpmas.erp.inventory.config.InventoryConsoleConfig;
import com.lpmas.erp.inventory.config.InventoryResource;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;

/**
 * Servlet implementation class WarehouseInventoryPutOnSelect
 */
@WebServlet("/erp/WarehouseInventoryPutOnSelect.do")
public class WarehouseInventoryPutOnSelect extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WarehouseInventoryPutOnSelect() {
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
		if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_INVENTORY, OperationConfig.UPDATE)) {
			return;
		}
		if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_VOUCHER_INFO, OperationConfig.SEARCH)) {
			return;
		}
		if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_VOUCHER_INFO, OperationConfig.UPDATE)) {
			return;
		}

		int pageNum = ParamKit.getIntParameter(request, "pageNum", ErpConsoleConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", ErpConsoleConfig.DEFAULT_PAGE_SIZE);
		HashMap<String, String> condMap = new HashMap<String, String>();
		PageBean pageBean = new PageBean(pageNum, pageSize);

		String reportId = ParamKit.getParameter(request, "reportId", "");
		WarehouseInventoryReportBusiness reportBusiness = new WarehouseInventoryReportBusiness();
		WarehouseInventoryReportBean bean;
		try {
			bean = reportBusiness.getWarehouseInventoryReportByKey(String.valueOf(reportId));
			if (bean == null) {
				HttpResponseKit.alertMessage(response, "库存信息错误", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			List<Integer> pendingShelfWvIdList = bean.getPendingShelfWvIdList();
			WarehouseVoucherInfoBusiness wvInfoBusiness = new WarehouseVoucherInfoBusiness();
			List<WarehouseVoucherInfoBean> result = wvInfoBusiness.getWarehouseVoucherInfoListByWvIdList(pendingShelfWvIdList, pageBean);

			pageBean.init(pageNum, pageSize, result.size());
			request.setAttribute("PageResult", pageBean);
			request.setAttribute("CondList", MapKit.map2List(condMap));
			request.setAttribute("WarehouseVoucherList", result);
			RequestDispatcher rd = request.getRequestDispatcher(InventoryConsoleConfig.INVENTORY_PAGE_PATH + "WarehouseInventoryPutOnSelect.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			HttpResponseKit.alertMessage(response, "库存信息错误", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}

	}
}
