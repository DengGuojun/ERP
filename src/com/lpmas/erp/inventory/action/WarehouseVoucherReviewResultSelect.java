package com.lpmas.erp.inventory.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
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
import com.lpmas.erp.inventory.config.InventoryConsoleConfig;
import com.lpmas.erp.inventory.config.InventoryResource;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;

/**
 * Servlet implementation class WarehouseVoucherReviewResultSelect
 */
@WebServlet("/erp/WarehouseVoucherReviewResultSelect.do")
public class WarehouseVoucherReviewResultSelect extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(WarehouseVoucherReviewResultSelect.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WarehouseVoucherReviewResultSelect() {
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
		if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_VOUCHER_INFO, OperationConfig.APPROVE)) {
			return;
		}
		try {
			// 获得锁
			WarehouseVoucherStatusProcessor.statusLock.lock();

			int wvId = ParamKit.getIntParameter(request, "wvId", 0);
			WarehouseVoucherInfoBusiness business = new WarehouseVoucherInfoBusiness();
			WarehouseVoucherInfoBean bean = business.getWarehouseVoucherInfoByKey(wvId);
			if (bean == null) {
				HttpResponseKit.alertMessage(response, "入库单ID不合法", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			WarehouseVoucherStatusProcessor statusBusiness = new WarehouseVoucherStatusProcessor(bean);
			if (!statusBusiness.approvable() && !statusBusiness.rejectable()) {
				HttpResponseKit.alertMessage(response, "入库单目前的状态不允许审核", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			RequestDispatcher rd = request
					.getRequestDispatcher(InventoryConsoleConfig.INVENTORY_PAGE_PATH + "WarehouseVoucherReviewResultSelect.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			logger.error("", e);
			HttpResponseKit.alertMessage(response, "操作失败", HttpResponseKit.ACTION_NONE);
		} finally {
			// 释放锁
			WarehouseVoucherStatusProcessor.statusLock.unlock();
		}
	}

}
