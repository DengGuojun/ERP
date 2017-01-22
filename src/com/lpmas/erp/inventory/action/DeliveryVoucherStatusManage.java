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
import com.lpmas.erp.inventory.bean.DeliveryVoucherInfoBean;
import com.lpmas.erp.inventory.business.DeliveryVoucherInfoBusiness;
import com.lpmas.erp.inventory.business.DeliveryVoucherStatusProcessor;
import com.lpmas.erp.inventory.config.DeliveryVoucherConfig;
import com.lpmas.erp.inventory.config.InventoryResource;
import com.lpmas.erp.inventory.config.SourceOrderTypeConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;

@WebServlet("/erp/DeliveryVoucherStatusManage.do")
public class DeliveryVoucherStatusManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(DeliveryVoucherStatusManage.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeliveryVoucherStatusManage() {
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
		int dvId = ParamKit.getIntParameter(request, "dvId", 0);
		String statusAction = ParamKit.getParameter(request, "statusAction", "");
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (statusAction.equals(DeliveryVoucherConfig.DV_ACTION_APPROVE) || statusAction.equals(DeliveryVoucherConfig.DV_ACTION_REJECT)) {
			// 判断是否具有审批权限
			if (!adminUserHelper.checkPermission(InventoryResource.DELIVERY_VOUCHER_INFO, OperationConfig.APPROVE)) {
				return;
			}
		} else {
			// 判断是否具有修改权限
			if (!adminUserHelper.checkPermission(InventoryResource.DELIVERY_VOUCHER_INFO, OperationConfig.UPDATE)) {
				return;
			}
		}

		try {
			DeliveryVoucherStatusProcessor.statusLock.lock();

			DeliveryVoucherInfoBusiness business = new DeliveryVoucherInfoBusiness();
			DeliveryVoucherInfoBean bean = business.getDeliveryVoucherInfoByKey(dvId);
			if (bean == null) {
				HttpResponseKit.alertMessage(response, "该出库单不存在", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			bean.setModifyUser(adminUserHelper.getAdminUserId());
			DeliveryVoucherStatusProcessor statusBusiness = new DeliveryVoucherStatusProcessor(bean);
			int result = Constants.STATUS_NOT_VALID;
			if (statusAction.equals(DeliveryVoucherConfig.DV_ACTION_COMMIT)) {
				if (statusBusiness.committable()) {
					result = statusBusiness.commit();
				}
			} else if (statusAction.equals(DeliveryVoucherConfig.DV_ACTION_CANCEL_COMMIT)) {
				if (statusBusiness.cancelCommittable()) {
					result = statusBusiness.cancelCommit();
				}
			} else if (statusAction.equals(DeliveryVoucherConfig.DV_ACTION_APPROVE)) {
				if (statusBusiness.approvable()) {
					result = statusBusiness.approve();
				}
			} else if (statusAction.equals(DeliveryVoucherConfig.DV_ACTION_REJECT)) {
				if (statusBusiness.rejectable()) {
					result = statusBusiness.reject();
				}
			} else if (statusAction.equals(DeliveryVoucherConfig.DV_ACTION_CLOSE)) {
				// 改库存，调拨单在途，建入库单
				if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_INVENTORY, OperationConfig.UPDATE)) {
					return;
				}
				if (bean.getSourceOrderType() == SourceOrderTypeConfig.SOURCE_ORDER_TYPE_WAREHOUSE_TRANSFER_ORDER
						&& (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_TRANSFER_ORDER_INFO, OperationConfig.UPDATE)
								|| !adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_VOUCHER_INFO, OperationConfig.CREATE))) {
					return;
				}
				if (statusBusiness.closable()) {
					result = statusBusiness.close();
				}
			} else {
				HttpResponseKit.alertMessage(response, "出库单操作代码不合法", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			if (result == Constants.STATUS_VALID) {
				HttpResponseKit.alertMessage(response, "处理成功",
						"/erp/DeliveryVoucherInfoList.do?wareType=" + bean.getWareType() + "&dvType=" + bean.getDvType());
			} else {
				HttpResponseKit.alertMessage(response, "操作失败！您的页面已过期，请刷新页面重新操作", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

		} catch (Exception e) {
			log.error("", e);
			HttpResponseKit.alertMessage(response, "操作失败", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		} finally {
			DeliveryVoucherStatusProcessor.statusLock.unlock();
		}
	}

}
