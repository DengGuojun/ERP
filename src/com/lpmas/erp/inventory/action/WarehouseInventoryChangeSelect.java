package com.lpmas.erp.inventory.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.erp.inventory.config.InventoryConsoleConfig;
import com.lpmas.erp.inventory.config.InventoryResource;
import com.lpmas.erp.inventory.config.WarehouseInventoryConfig;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;

/**
 * Servlet implementation class WarehouseInventoryChangeSelect
 */
@WebServlet("/erp/WarehouseInventoryChangeSelect.do")
public class WarehouseInventoryChangeSelect extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WarehouseInventoryChangeSelect() {
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
		int statusAction = ParamKit.getIntParameter(request, "statusAction", 0);
		if (!WarehouseInventoryConfig.WAREHOUSE_INVENTORY_OPERATION_TYPE_MAP.containsKey(statusAction)) {
			HttpResponseKit.alertMessage(response, "操作类型错误", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}
		request.setAttribute("statusAction", statusAction);
		RequestDispatcher rd = request.getRequestDispatcher(InventoryConsoleConfig.INVENTORY_PAGE_PATH + "WarehouseInventoryChangeSelect.jsp");
		rd.forward(request, response);
	}
}
