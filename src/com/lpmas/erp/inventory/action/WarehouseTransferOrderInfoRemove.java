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
import com.lpmas.erp.inventory.config.InventoryResource;
import com.lpmas.erp.inventory.config.WarehouseTransferOrderConfig;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;

/**
 * Servlet implementation class WarehouseTransferOrderInfoRemove
 */
@WebServlet("/erp/WarehouseTransferOrderInfoRemove.do")
public class WarehouseTransferOrderInfoRemove extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(WarehouseTransferOrderInfoRemove.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WarehouseTransferOrderInfoRemove() {
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
		if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_TRANSFER_ORDER_INFO, OperationConfig.REMOVE)) {
			return;
		}
		int toId = ParamKit.getIntParameter(request, "toId", 0);
		try {
			WarehouseTransferOrderInfoBusiness business = new WarehouseTransferOrderInfoBusiness();
			WarehouseTransferOrderInfoBean bean = business.getWarehouseTransferOrderInfoByKey(toId);
			if (bean == null) {
				HttpResponseKit.alertMessage(response, "调拨单ID参数错误", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			if (bean.getToStatus().equals(WarehouseTransferOrderConfig.TO_STATUS_EDIT)
					|| bean.getToStatus().equals(WarehouseTransferOrderConfig.TO_STATUS_WAIT_APPROVE)) {
				bean.setModifyUser(adminUserHelper.getAdminUserId());
				int result = business.removeWarehouseTransferOrderInfo(bean, adminUserHelper.getAdminUserId());
				if (result > 0) {
					HttpResponseKit.alertMessage(response, "处理成功", "/erp/WarehouseTransferOrderInfoList.do?wareType=" + bean.getWareType());
				} else {
					HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
				}
			} else {
				HttpResponseKit.alertMessage(response, "只允许删除审批通过之前的调拨单", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
		} catch (Exception e) {
			log.error("", e);
		} finally {
		}
	}

}
