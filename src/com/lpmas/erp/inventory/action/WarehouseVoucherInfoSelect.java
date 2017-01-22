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
import com.lpmas.erp.inventory.bean.WarehouseVoucherInfoBean;
import com.lpmas.erp.inventory.business.WarehouseVoucherInfoBusiness;
import com.lpmas.erp.inventory.config.InventoryConsoleConfig;
import com.lpmas.erp.inventory.config.InventoryResource;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.web.ParamKit;

@WebServlet("/erp/WarehouseVoucherInfoSelect.do")
public class WarehouseVoucherInfoSelect extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public WarehouseVoucherInfoSelect() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_VOUCHER_INFO, OperationConfig.SEARCH)) {
			return;
		}
		int wvId = ParamKit.getIntParameter(request, "wvId", 0);
		String mode = ParamKit.getParameter(request, "mode", "general");
		int pageNum = ParamKit.getIntParameter(request, "pageNum", ErpConsoleConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", ErpConsoleConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);
		HashMap<String, String> condMap = ParamKit.getParameterMap(request, "wvNumber,wareType", "");
		if (mode.equals("log")) {
			condMap.put("status", "");
		} else {
			condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		}
		request.setAttribute("mode", mode);
		WarehouseVoucherInfoBusiness business = new WarehouseVoucherInfoBusiness();
		PageResultBean<WarehouseVoucherInfoBean> result = business.getWarehouseVoucherInfoPageListByMap(condMap, pageBean);
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("WarehouseVoucherList", result.getRecordList());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("wvId", wvId);
		RequestDispatcher rd = request.getRequestDispatcher(InventoryConsoleConfig.INVENTORY_PAGE_PATH + "WarehouseVoucherInfoSelect.jsp");
		rd.forward(request, response);
	}
}
