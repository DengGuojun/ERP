package com.lpmas.erp.inventory.action;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.erp.config.ErpConsoleConfig;
import com.lpmas.erp.inventory.bean.DeliveryVoucherInfoBean;
import com.lpmas.erp.inventory.business.DeliveryVoucherInfoBusiness;
import com.lpmas.erp.inventory.config.InventoryConsoleConfig;
import com.lpmas.erp.inventory.config.InventoryResource;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.web.ParamKit;

@WebServlet("/erp/DeliveryVoucherInfoSelect.do")
public class DeliveryVoucherInfoSelect extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DeliveryVoucherInfoSelect() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(InventoryResource.DELIVERY_VOUCHER_INFO, OperationConfig.SEARCH)) {
			return;
		}
		int dvId = ParamKit.getIntParameter(request, "dvId", 0);
		String mode = ParamKit.getParameter(request, "mode", "general");
		int pageNum = ParamKit.getIntParameter(request, "pageNum", ErpConsoleConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", ErpConsoleConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);
		HashMap<String, String> condMap = ParamKit.getParameterMap(request, "dvNumber,wareType", "");
		if (mode.equals("log")) {
			condMap.put("status", "");
		} else {
			condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		}
		request.setAttribute("mode", mode);
		DeliveryVoucherInfoBusiness business = new DeliveryVoucherInfoBusiness();
		PageResultBean<DeliveryVoucherInfoBean> result = business.getDeliveryVoucherInfoPageListByMap(condMap, pageBean);
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("DeliveryVoucherList", result.getRecordList());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("dvId", dvId);
		// 请求转发
		RequestDispatcher rd = request.getRequestDispatcher(InventoryConsoleConfig.INVENTORY_PAGE_PATH + "DeliveryVoucherInfoSelect.jsp");
		rd.forward(request, response);
	}

}
