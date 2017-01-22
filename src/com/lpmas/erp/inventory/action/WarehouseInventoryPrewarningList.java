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
import com.lpmas.erp.inventory.bean.WarehouseInventoryPrewarningBean;
import com.lpmas.erp.inventory.business.WarehouseInventoryPrewarningBusiness;
import com.lpmas.erp.inventory.config.InventoryConsoleConfig;
import com.lpmas.erp.inventory.config.InventoryResource;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ParamKit;

/**
 * Servlet implementation class InventotyPrewarningList
 */
@WebServlet("/erp/WarehouseInventoryPrewarningList.do")
public class WarehouseInventoryPrewarningList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WarehouseInventoryPrewarningList() {
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
		if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_INVENTORY_PREWARNING, OperationConfig.SEARCH)) {
			return;
		}

		// 初始化页面分页参数
		int pageNum = ParamKit.getIntParameter(request, "pageNum", ErpConsoleConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", ErpConsoleConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);

		HashMap<String, String> condMap = new HashMap<String, String>();
		int wareType = ParamKit.getIntParameter(request, "wareType", 0);
		if (wareType != 0) {
			condMap.put("wareType", String.valueOf(wareType));
		}
		int prewarningType = ParamKit.getIntParameter(request, "prewarningType", 0);
		if (prewarningType != 0) {
			condMap.put("prewarningType", String.valueOf(prewarningType));
		}
		String status = ParamKit.getParameter(request, "status", String.valueOf(Constants.STATUS_VALID)).trim();
		if (StringKit.isValid(status)) {
			condMap.put("status", status);
		}

		// 从数据库中获取相应的数据
		WarehouseInventoryPrewarningBusiness business = new WarehouseInventoryPrewarningBusiness();
		PageResultBean<WarehouseInventoryPrewarningBean> result = business.getWarehouseInventoryPrewarningPageListByMap(condMap, pageBean);

		// 数据放到页面
		request.setAttribute("WarehouseInventoryPrewarningList", result.getRecordList());

		// 初始化分页数据
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("AdminUserHelper", adminUserHelper);
		RequestDispatcher rd = request.getRequestDispatcher(InventoryConsoleConfig.INVENTORY_PAGE_PATH + "WarehouseInventoryPrewarningList.jsp");
		rd.forward(request, response);

	}

}
