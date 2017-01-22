package com.lpmas.erp.inventory.action;

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
import com.lpmas.erp.inventory.bean.WarehouseTransferOrderInfoBean;
import com.lpmas.erp.inventory.business.WarehouseTransferOrderInfoBusiness;
import com.lpmas.erp.inventory.business.WarehouseTransferOrderStatusProcessor;
import com.lpmas.erp.inventory.config.InventoryResource;
import com.lpmas.erp.inventory.config.WarehouseTransferOrderConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;

/**
 * Servlet implementation class WarehouseTransferStatusManage
 */
@WebServlet("/erp/WarehouseTransferStatusManage.do")
public class WarehouseTransferStatusManage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(WarehouseTransferStatusManage.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WarehouseTransferStatusManage() {
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
		int toId = ParamKit.getIntParameter(request, "toId", 0);
		String statusAction = ParamKit.getParameter(request, "statusAction", "");
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (statusAction.equals(WarehouseTransferOrderConfig.TO_ACTION_APPROVE)
				|| statusAction.equals(WarehouseTransferOrderConfig.TO_ACTION_REJECT)) {
			// 判断是否具有审批权限
			if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_TRANSFER_ORDER_INFO, OperationConfig.APPROVE)) {
				return;
			}
		} else {
			// 判断是否具有修改权限
			if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_TRANSFER_ORDER_INFO, OperationConfig.UPDATE)) {
				return;
			}
		}

		try {
			WarehouseTransferOrderStatusProcessor.statusLock.lock();

			WarehouseTransferOrderInfoBusiness business = new WarehouseTransferOrderInfoBusiness();
			WarehouseTransferOrderInfoBean bean = business.getWarehouseTransferOrderInfoByKey(toId);
			if (bean == null) {
				HttpResponseKit.alertMessage(response, "该调拨单不存在", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			bean.setModifyUser(adminUserHelper.getAdminUserId());
			WarehouseTransferOrderStatusProcessor statusBusiness = new WarehouseTransferOrderStatusProcessor(bean);
			int result = Constants.STATUS_NOT_VALID;
			if (statusAction.equals(WarehouseTransferOrderConfig.TO_ACTION_COMMIT)) {
				if (statusBusiness.committable()) {
					result = statusBusiness.commit();
				}
			} else if (statusAction.equals(WarehouseTransferOrderConfig.TO_ACTION_CANCEL_COMMIT)) {
				if (statusBusiness.cancelCommittable()) {
					result = statusBusiness.cancelCommit();
				}
			} else if (statusAction.equals(WarehouseTransferOrderConfig.TO_ACTION_APPROVE)) {
				// 要新建出库单判断权限
				if (!adminUserHelper.checkPermission(InventoryResource.DELIVERY_VOUCHER_INFO, OperationConfig.CREATE)) {
					return;
				}
				if (statusBusiness.approvable()) {
					result = statusBusiness.approve();
				}
			} else if (statusAction.equals(WarehouseTransferOrderConfig.TO_ACTION_REJECT)) {
				if (statusBusiness.rejectable()) {
					result = statusBusiness.reject();
				}
			} else if (statusAction.equals(WarehouseTransferOrderConfig.TO_ACTION_CLOSE)) {
				if (statusBusiness.closable()) {
					result = statusBusiness.close();
				}
			} else {
				HttpResponseKit.alertMessage(response, "调拨单操作代码不合法", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			if (result == Constants.STATUS_VALID) {
				HttpResponseKit.alertMessage(response, "处理成功", "/erp/WarehouseTransferOrderInfoList.do?wareType=" + bean.getWareType());
			} else {
				HttpResponseKit.alertMessage(response, "操作失败！您的页面已过期，请刷新页面重新操作", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

		} catch (Exception e) {
			log.error("", e);
			HttpResponseKit.alertMessage(response, "操作失败", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		} finally {
			WarehouseTransferOrderStatusProcessor.statusLock.unlock();
		}
	}

}
