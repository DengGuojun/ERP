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
import com.lpmas.erp.inventory.config.InventoryResource;
import com.lpmas.erp.inventory.config.WarehouseVoucherConfig;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;

@WebServlet("/erp/WarehouseVoucherInfoRemove.do")
public class WarehouseVoucherInfoRemove extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(WarehouseVoucherInfoRemove.class);
	private static final long serialVersionUID = 1L;

	public WarehouseVoucherInfoRemove() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_VOUCHER_INFO, OperationConfig.REMOVE)) {
			return;
		}
		int wvId = ParamKit.getIntParameter(request, "wvId", 0);
		try {
			WarehouseVoucherInfoBusiness business = new WarehouseVoucherInfoBusiness();
			WarehouseVoucherInfoBean bean = business.getWarehouseVoucherInfoByKey(wvId);
			if (bean == null) {
				HttpResponseKit.alertMessage(response, "入库单ID参数错误", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			if (bean.getWvStatus().equals(WarehouseVoucherConfig.WV_STATUS_EDIT)
					|| bean.getWvStatus().equals(WarehouseVoucherConfig.WV_STATUS_WAIT_APPROVE)) {
				bean.setModifyUser(adminUserHelper.getAdminUserId());
				int result = business.removeWarehouseVoucherInfo(bean);
				if (result > 0) {
					HttpResponseKit.alertMessage(response, "处理成功",
							"/erp/WarehouseVoucherInfoList.do?wareType=" + bean.getWareType() + "&wvType=" + bean.getWvType());
				} else {
					HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
				}
			} else {
				HttpResponseKit.alertMessage(response, "只允许删除审批通过之前的入库单", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
		} catch (Exception e) {
			log.error("", e);
		} finally {
		}
	}

}
