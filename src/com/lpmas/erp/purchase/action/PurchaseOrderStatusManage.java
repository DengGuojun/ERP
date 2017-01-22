package com.lpmas.erp.purchase.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.business.PurchaseOrderInfoBusiness;
import com.lpmas.erp.purchase.business.PurchaseOrderStatusProcessor;
import com.lpmas.erp.purchase.config.PurchaseOrderConfig;
import com.lpmas.erp.purchase.config.PurchaseOrderResource;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;

/**
 * Servlet implementation class PurchaseOrderStatusManage
 */
@WebServlet("/erp/PurchaseOrderStatusManage.do")
public class PurchaseOrderStatusManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(PurchaseOrderStatusManage.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PurchaseOrderStatusManage() {
		super();
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
		int poId = ParamKit.getIntParameter(request, "poId", 0);
		String statusAction = ParamKit.getParameter(request, "statusAction", "");
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (statusAction.equals(PurchaseOrderConfig.PO_ACTION_APPROVE)
				|| statusAction.equals(PurchaseOrderConfig.PO_ACTION_REJECT)) {
			// 判断是否具有审批权限
			if (!adminUserHelper.checkPermission(PurchaseOrderResource.PURCHASE_ORDER_INFO, OperationConfig.APPROVE)) {
				return;
			}
		} else {
			// 判断是否具有修改权限
			if (!adminUserHelper.checkPermission(PurchaseOrderResource.PURCHASE_ORDER_INFO, OperationConfig.UPDATE)) {
				return;
			}
		}

		try {
			PurchaseOrderStatusProcessor.statusLock.lock();
			
			PurchaseOrderInfoBusiness business = new PurchaseOrderInfoBusiness();
			PurchaseOrderInfoBean bean = business.getPurchaseOrderInfoByKey(poId);
			bean.setModifyUser(adminUserHelper.getAdminUserId());
			PurchaseOrderStatusProcessor statusBusiness = new PurchaseOrderStatusProcessor(bean);
			int result = Constants.STATUS_NOT_VALID;
			if (statusAction.equals(PurchaseOrderConfig.PO_ACTION_COMMIT)) {
				if (statusBusiness.committable()) {
					result = statusBusiness.commit();
				}
			} else if (statusAction.equals(PurchaseOrderConfig.PO_ACTION_CANCEL_COMMIT)) {
				if (statusBusiness.cancelCommittable()) {
					result = statusBusiness.cancelCommit();
				}
			} else if (statusAction.equals(PurchaseOrderConfig.PO_ACTION_APPROVE)) {
				if (statusBusiness.approvable()) {
					result = statusBusiness.approve();
				}
			} else if (statusAction.equals(PurchaseOrderConfig.PO_ACTION_REJECT)) {
				if (statusBusiness.rejectable()) {
					result = statusBusiness.reject();
				}
			} else if (statusAction.equals(PurchaseOrderConfig.PO_ACTION_PLACE_ORDER)) {
				if (statusBusiness.placeOrderable()) {
					result = statusBusiness.placeOrder();
				}
			} else if (statusAction.equals(PurchaseOrderConfig.PO_ACTION_CANCEL_PLACE_ORDER)) {
				if (statusBusiness.cancelOrderable()) {
					result = statusBusiness.cancelOrder();
				}
			} else if (statusAction.equals(PurchaseOrderConfig.PO_ACTION_RECEIVE)) {
				if (statusBusiness.receivable()) {
					result = statusBusiness.receive();
				}
			} else if (statusAction.equals(PurchaseOrderConfig.PO_ACTION_ARCHIVE)) {
				if (statusBusiness.archivable()) {
					result = statusBusiness.archive();
				}
			} else {
				HttpResponseKit.alertMessage(response, "采购订单操作代码不合法", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			if (result == Constants.STATUS_VALID) {
				HttpResponseKit.alertMessage(response, "处理成功",
						"/erp/PurchaseOrderInfoList.do?wareType=" + bean.getWareType());
			} else {
				HttpResponseKit.alertMessage(response, "操作失败！您的页面已过期，请刷新页面重新操作", HttpResponseKit.ACTION_HISTORY_BACK);
			}

		} catch (Exception e) {
			log.error("", e);
		} finally {
			PurchaseOrderStatusProcessor.statusLock.unlock();
		}
	}
}
