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
import com.lpmas.erp.inventory.config.DeliveryVoucherConfig;
import com.lpmas.erp.inventory.config.InventoryResource;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;

@WebServlet("/erp/DeliveryVoucherInfoRemove.do")
public class DeliveryVoucherInfoRemove extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(WarehouseVoucherInfoRemove.class);
	private static final long serialVersionUID = 1L;

	public DeliveryVoucherInfoRemove() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(InventoryResource.DELIVERY_VOUCHER_INFO, OperationConfig.REMOVE)) {
			return;
		}
		int dvId = ParamKit.getIntParameter(request, "dvId", 0);
		try {
			DeliveryVoucherInfoBusiness business = new DeliveryVoucherInfoBusiness();
			DeliveryVoucherInfoBean bean = business.getDeliveryVoucherInfoByKey(dvId);
			if (bean == null) {
				HttpResponseKit.alertMessage(response, "出库单ID参数错误", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			if (bean.getDvStatus().equals(DeliveryVoucherConfig.DV_STATUS_EDIT)
					|| bean.getDvStatus().equals(DeliveryVoucherConfig.DV_STATUS_WAIT_APPROVE)) {
				bean.setModifyUser(adminUserHelper.getAdminUserId());
				int result = business.removeDeliveryVoucherInfo(bean);
				if (result > 0) {
					HttpResponseKit.alertMessage(response, "处理成功",
							"/erp/DeliveryVoucherInfoList.do?wareType=" + bean.getWareType() + "&dvType=" + bean.getDvType());
				} else {
					HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
				}
			} else {
				HttpResponseKit.alertMessage(response, "只允许删除审批通过之前的出库单", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
		} catch (Exception e) {
			log.error("", e);
		} finally {
		}

	}

}
