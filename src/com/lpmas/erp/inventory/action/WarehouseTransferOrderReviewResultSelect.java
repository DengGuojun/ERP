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
import com.lpmas.erp.inventory.bean.WarehouseTransferOrderInfoBean;
import com.lpmas.erp.inventory.business.WarehouseTransferOrderInfoBusiness;
import com.lpmas.erp.inventory.business.WarehouseTransferOrderStatusProcessor;
import com.lpmas.erp.inventory.config.InventoryConsoleConfig;
import com.lpmas.erp.inventory.config.InventoryResource;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;

/**
 * Servlet implementation class WarehouseTransferOrderReviewResultSelect
 */
@WebServlet("/erp/WarehouseTransferOrderReviewResultSelect.do")
public class WarehouseTransferOrderReviewResultSelect extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WarehouseTransferOrderReviewResultSelect() {
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
		// 判断是否具有审批权限
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_TRANSFER_ORDER_INFO, OperationConfig.APPROVE)) {
			return;
		}
		try {
			WarehouseTransferOrderStatusProcessor.statusLock.lock();

			int toId = ParamKit.getIntParameter(request, "toId", 0);
			WarehouseTransferOrderInfoBusiness business = new WarehouseTransferOrderInfoBusiness();
			WarehouseTransferOrderInfoBean bean = business.getWarehouseTransferOrderInfoByKey(toId);
			if (bean == null) {
				HttpResponseKit.alertMessage(response, "调拨单ID不合法", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			WarehouseTransferOrderStatusProcessor statusBusiness = new WarehouseTransferOrderStatusProcessor(bean);
			if (!statusBusiness.approvable() && !statusBusiness.rejectable()) {
				HttpResponseKit.alertMessage(response, "调拨单目前的状态不允许审核", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			RequestDispatcher rd = request
					.getRequestDispatcher(InventoryConsoleConfig.INVENTORY_PAGE_PATH + "WarehouseTransferOrderReviewResultSelect.jsp");
			rd.forward(request, response);
		} finally {
			WarehouseTransferOrderStatusProcessor.statusLock.unlock();
		}
	}

}
