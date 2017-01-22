package com.lpmas.erp.purchase.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.business.PurchaseOrderInfoBusiness;
import com.lpmas.erp.purchase.business.PurchaseOrderStatusProcessor;
import com.lpmas.erp.purchase.config.PurchaseOrderConsoleConfig;
import com.lpmas.erp.purchase.config.PurchaseOrderResource;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;

/**
 * Servlet implementation class PurchaseOrderReviewResultSelect
 */
@WebServlet("/erp/PurchaseOrderReviewResultSelect.do")
public class PurchaseOrderReviewResultSelect extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PurchaseOrderReviewResultSelect() {
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
		// 判断是否具有审批权限
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(PurchaseOrderResource.PURCHASE_ORDER_INFO, OperationConfig.APPROVE)) {
			return;
		}
		try {
			PurchaseOrderStatusProcessor.statusLock.lock();

			int poId = ParamKit.getIntParameter(request, "poId", 0);
			PurchaseOrderInfoBusiness business = new PurchaseOrderInfoBusiness();
			PurchaseOrderInfoBean bean = business.getPurchaseOrderInfoByKey(poId);
			if (bean == null) {
				HttpResponseKit.alertMessage(response, "采购订单ID不合法", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			PurchaseOrderStatusProcessor statusBusiness = new PurchaseOrderStatusProcessor(bean);
			if (!statusBusiness.approvable() && !statusBusiness.rejectable()) {
				HttpResponseKit.alertMessage(response, "采购订单目前的状态不允许审核", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			RequestDispatcher rd = request.getRequestDispatcher(
					PurchaseOrderConsoleConfig.PURCHASE_ORDER_PAGE_PATH + "PurchaseOrderReviewResultSelect.jsp");
			rd.forward(request, response);
		} finally {
			PurchaseOrderStatusProcessor.statusLock.unlock();
		}
	}

}
