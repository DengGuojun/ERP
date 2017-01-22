package com.lpmas.erp.purchase.action;

import java.io.IOException;
import java.util.List;

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
import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.business.PurchaseOrderInfoBusiness;
import com.lpmas.erp.purchase.config.PurchaseOrderConfig;
import com.lpmas.erp.purchase.config.PurchaseOrderResource;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;

/**
 * Servlet implementation class PurchaseOrderInfoRemove
 */
@WebServlet("/erp/PurchaseOrderInfoRemove.do")
public class PurchaseOrderInfoRemove extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(PurchaseOrderInfoRemove.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PurchaseOrderInfoRemove() {
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
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(PurchaseOrderResource.PURCHASE_ORDER_INFO, OperationConfig.REMOVE)) {
			return;
		}
		int poId = ParamKit.getIntParameter(request, "poId", 0);
		try {
			PurchaseOrderInfoBusiness business = new PurchaseOrderInfoBusiness();
			PurchaseOrderInfoBean bean = business.getPurchaseOrderInfoByKey(poId);
			if (bean == null) {
				HttpResponseKit.alertMessage(response, "采购订单ID参数错误", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			if (bean.getPoStatus().equals(PurchaseOrderConfig.PO_STATUS_ARCHIVED)) {
				HttpResponseKit.alertMessage(response, "已归档的订单不能删除", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			WarehouseVoucherInfoBusiness warehouseVoucherInfoBusiness = new WarehouseVoucherInfoBusiness();
			List<WarehouseVoucherInfoBean> warehouseVoucherList = warehouseVoucherInfoBusiness
					.getWarehouseVoucherInfoListByOrderId(bean.getPoId());
			if (!warehouseVoucherList.isEmpty()) {
				HttpResponseKit.alertMessage(response, "含有有效的入库单，该订单不能删除", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			bean.setModifyUser(adminUserHelper.getAdminUserId());
			int result = business.removePurchaseOrderInfo(bean);
			if (result > 0) {
				HttpResponseKit.alertMessage(response, "处理成功",
						"/erp/PurchaseOrderInfoList.do?wareType=" + bean.getWareType());
			} else {
				HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
			}
		} catch (Exception e) {
			log.error("", e);
		} finally {
		}
	}
}
