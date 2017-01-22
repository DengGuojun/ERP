package com.lpmas.erp.warehouse.action;

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
import com.lpmas.erp.warehouse.bean.WarehouseInfoBean;
import com.lpmas.erp.warehouse.business.WarehouseInfoBusiness;
import com.lpmas.erp.warehouse.config.WarehouseConsoleConfig;
import com.lpmas.erp.warehouse.config.WarehouseResource;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.web.ParamKit;

/**
 * Servlet implementation class WarehouseInfoSelect
 */
@WebServlet("/erp/WarehouseInfoSelect.do")
public class WarehouseInfoSelect extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WarehouseInfoSelect() {
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
		if (!adminUserHelper.checkPermission(WarehouseResource.WAREHOUSE_INFO, OperationConfig.SEARCH)) {
			return;
		}
		int pageNum = ParamKit.getIntParameter(request, "pageNum", ErpConsoleConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", ErpConsoleConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);

		HashMap<String, String> condMap = new HashMap<String, String>();
		int warehouseId = ParamKit.getIntParameter(request, "warehouseId", 0);
		String queryParam = ParamKit.getParameter(request, "queryParam", "").trim();

		WarehouseInfoBusiness business = new WarehouseInfoBusiness();
		PageResultBean<WarehouseInfoBean> result = business.getWarehouseInfoPageListByFuzzyQueryParam(queryParam,
				pageBean);
		request.setAttribute("WarehouseInfoList", result.getRecordList());

		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("WarehouseId", warehouseId);
		RequestDispatcher rd = request
				.getRequestDispatcher(WarehouseConsoleConfig.WAREHOUSE_PAGE_PATH + "WarehouseInfoSelect.jsp");
		rd.forward(request, response);
	}

}
