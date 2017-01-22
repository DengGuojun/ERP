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
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.log.bean.DataLogBean;
import com.lpmas.log.client.LogServiceClient;

@WebServlet("/erp/WarehouseTransferOperationLogManage.do")
public class WarehouseTransferOperationLogManage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WarehouseTransferOperationLogManage() {
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
		if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_TRANSFER_ORDER_INFO_LOG, OperationConfig.SEARCH)) {
			return;
		}
		String logId = ParamKit.getParameter(request, "logId", "");
		if (!StringKit.isValid(logId)) {
			HttpResponseKit.alertMessage(response, "日志ID缺失!", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}
		// 查出对应的LOG BEAN
		LogServiceClient client = new LogServiceClient();
		DataLogBean dataLogBean = client.getDataLogBeanByKey(logId);

		// 放到页面
		request.setAttribute("DataLogBean", dataLogBean);
		// 请求转发
		RequestDispatcher rd = request.getRequestDispatcher(InventoryConsoleConfig.INVENTORY_PAGE_PATH + "WarehouseTransferOperationLogManage.jsp");
		rd.forward(request, response);
	}

}
