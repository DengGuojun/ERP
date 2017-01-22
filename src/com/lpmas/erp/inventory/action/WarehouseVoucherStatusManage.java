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
import com.lpmas.erp.inventory.bean.WarehouseVoucherInfoBean;
import com.lpmas.erp.inventory.business.WarehouseVoucherInfoBusiness;
import com.lpmas.erp.inventory.business.WarehouseVoucherStatusProcessor;
import com.lpmas.erp.inventory.config.InventoryResource;
import com.lpmas.erp.inventory.config.SourceOrderTypeConfig;
import com.lpmas.erp.inventory.config.WarehouseVoucherConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;

/**
 * Servlet implementation class WarehouseVoucherStatusManage
 */
@WebServlet("/erp/WarehouseVoucherStatusManage.do")
public class WarehouseVoucherStatusManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(WarehouseVoucherStatusManage.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WarehouseVoucherStatusManage() {
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
		int wvId = ParamKit.getIntParameter(request, "wvId", 0);
		String statusAction = ParamKit.getParameter(request, "statusAction", "");
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (statusAction.equals(WarehouseVoucherConfig.WV_ACTION_APPROVE) || statusAction.equals(WarehouseVoucherConfig.WV_ACTION_REJECT)) {
			// 判断是否具有审批权限
			if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_VOUCHER_INFO, OperationConfig.APPROVE)) {
				return;
			}
		} else {
			// 判断是否具有修改权限
			if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_VOUCHER_INFO, OperationConfig.UPDATE)) {
				return;
			}
		}

		try {
			// 获得锁
			WarehouseVoucherStatusProcessor.statusLock.lock();

			WarehouseVoucherInfoBusiness business = new WarehouseVoucherInfoBusiness();
			WarehouseVoucherInfoBean bean = business.getWarehouseVoucherInfoByKey(wvId);
			if (bean == null) {
				HttpResponseKit.alertMessage(response, "该入库单不存在", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			bean.setModifyUser(adminUserHelper.getAdminUserId());
			WarehouseVoucherStatusProcessor statusBusiness = new WarehouseVoucherStatusProcessor(bean);
			int result = Constants.STATUS_NOT_VALID;
			if (statusAction.equals(WarehouseVoucherConfig.WV_ACTION_COMMIT)) {
				if (statusBusiness.committable()) {
					result = statusBusiness.commit();
				}
			} else if (statusAction.equals(WarehouseVoucherConfig.WV_ACTION_CANCEL_COMMIT)) {
				if (statusBusiness.cancelCommittable()) {
					result = statusBusiness.cancelCommit();
				}
			} else if (statusAction.equals(WarehouseVoucherConfig.WV_ACTION_APPROVE)) {
				if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_INVENTORY, OperationConfig.UPDATE)) {
					return;
				}
				if (statusBusiness.approvable()) {
					result = statusBusiness.approve();
				}
			} else if (statusAction.equals(WarehouseVoucherConfig.WV_ACTION_REJECT)) {
				if (statusBusiness.rejectable()) {
					result = statusBusiness.reject();
				}
			} else if (statusAction.equals(WarehouseVoucherConfig.WV_ACTION_CLOSE)) {
				// 改库存，关闭调拨单
				if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_INVENTORY, OperationConfig.UPDATE)
						|| (bean.getSourceOrderType() == SourceOrderTypeConfig.SOURCE_ORDER_TYPE_WAREHOUSE_TRANSFER_ORDER
								&& !adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_TRANSFER_ORDER_INFO, OperationConfig.UPDATE))) {
					return;
				}
				if (statusBusiness.closable()) {
					result = statusBusiness.close();
				}
			} else {
				HttpResponseKit.alertMessage(response, "入库单操作代码不合法", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			if (result == Constants.STATUS_VALID) {
				HttpResponseKit.alertMessage(response, "处理成功",
						"/erp/WarehouseVoucherInfoList.do?wareType=" + bean.getWareType() + "&wvType=" + bean.getWvType());
			} else {
				HttpResponseKit.alertMessage(response, "操作失败！您的页面已过期，请刷新页面重新操作", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

		} catch (Exception e) {
			log.error("", e);
			HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		} finally {
			// 释放锁
			WarehouseVoucherStatusProcessor.statusLock.unlock();
		}
	}
}
