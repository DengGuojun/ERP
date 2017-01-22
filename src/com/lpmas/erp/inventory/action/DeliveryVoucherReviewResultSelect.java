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
import com.lpmas.erp.inventory.bean.DeliveryVoucherInfoBean;
import com.lpmas.erp.inventory.business.DeliveryVoucherInfoBusiness;
import com.lpmas.erp.inventory.business.DeliveryVoucherStatusProcessor;
import com.lpmas.erp.inventory.config.InventoryConsoleConfig;
import com.lpmas.erp.inventory.config.InventoryResource;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;

@WebServlet("/erp/DeliveryVoucherReviewResultSelect.do")
public class DeliveryVoucherReviewResultSelect extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeliveryVoucherReviewResultSelect() {
		super();
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
		if (!adminUserHelper.checkPermission(InventoryResource.DELIVERY_VOUCHER_INFO, OperationConfig.APPROVE)) {
			return;
		}
		try {
			DeliveryVoucherStatusProcessor.statusLock.lock();

			int dvId = ParamKit.getIntParameter(request, "dvId", 0);
			DeliveryVoucherInfoBusiness business = new DeliveryVoucherInfoBusiness();
			DeliveryVoucherInfoBean bean = business.getDeliveryVoucherInfoByKey(dvId);
			if (bean == null) {
				HttpResponseKit.alertMessage(response, "出库单ID不合法", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			DeliveryVoucherStatusProcessor statusBusiness = new DeliveryVoucherStatusProcessor(bean);
			if (!statusBusiness.approvable() && !statusBusiness.rejectable()) {
				HttpResponseKit.alertMessage(response, "出库单目前的状态不允许审核", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			RequestDispatcher rd = request.getRequestDispatcher(InventoryConsoleConfig.INVENTORY_PAGE_PATH + "DeliveryVoucherReviewResultSelect.jsp");
			rd.forward(request, response);
		} finally {
			DeliveryVoucherStatusProcessor.statusLock.unlock();
		}
	}

}
