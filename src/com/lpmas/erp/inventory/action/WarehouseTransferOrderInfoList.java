package com.lpmas.erp.inventory.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.config.ErpConsoleConfig;
import com.lpmas.erp.inventory.bean.WarehouseTransferOrderInfoBean;
import com.lpmas.erp.inventory.business.WarehouseTransferOrderInfoBusiness;
import com.lpmas.erp.inventory.config.InventoryConsoleConfig;
import com.lpmas.erp.inventory.config.InventoryResource;
import com.lpmas.erp.warehouse.bean.WarehouseInfoBean;
import com.lpmas.erp.warehouse.business.WarehouseInfoBusiness;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.ListKit;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;

/**
 * Servlet implementation class WarehouseTransferOrderInfoList
 */
@WebServlet("/erp/WarehouseTransferOrderInfoList.do")
public class WarehouseTransferOrderInfoList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WarehouseTransferOrderInfoList() {
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
		if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_TRANSFER_ORDER_INFO, OperationConfig.SEARCH)) {
			return;
		}
		int wareType = ParamKit.getIntParameter(request, "wareType", 0);
		if (wareType != InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM && wareType != InfoTypeConfig.INFO_TYPE_MATERIAL) {
			HttpResponseKit.alertMessage(response, "制品类型不合法", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}
		int pageNum = ParamKit.getIntParameter(request, "pageNum", ErpConsoleConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", ErpConsoleConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);
		HashMap<String, String> condMap = new HashMap<String, String>();
		// 此处应有分页参数
		condMap = ParamKit.getParameterMap(request, "toNumber,reviewStatus,toStatus,createUser,sourceWarehouseId,targetWarehouseId", "");
		condMap.put("wareType", String.valueOf(wareType));
		condMap.put("status", ParamKit.getParameter(request, "status", String.valueOf(Constants.STATUS_VALID)));
		WarehouseTransferOrderInfoBusiness business = new WarehouseTransferOrderInfoBusiness();
		PageResultBean<WarehouseTransferOrderInfoBean> result = business.getWarehouseTransferOrderInfoPageListByMap(condMap, pageBean);

		request.setAttribute("WarehouseTransferOrderInfoList", result.getRecordList());
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("WareType", wareType);
		request.setAttribute("AdminUserHelper", adminUserHelper);
		// 查出仓库信息列表筛选用
		WarehouseInfoBusiness warehouseInfoBusiness = new WarehouseInfoBusiness();
		List<WarehouseInfoBean> warehouseInfoList = warehouseInfoBusiness.getWarehouseInfoAllList();
		request.setAttribute("WarehouseInfoList", warehouseInfoList);
		request.setAttribute("WarehouseInfoMap", ListKit.list2Map(warehouseInfoList, "warehouseId", "warehouseName"));
		// 查出创建人信息列表筛选用
		List<Integer> warehouseTransferOrderCreaterUserList = business.getWarehouseTransferOrderCreaterUserAllList();
		request.setAttribute("WarehouseTransferOrderCreaterUserList", warehouseTransferOrderCreaterUserList);
		// 请求转发
		RequestDispatcher rd = request.getRequestDispatcher(InventoryConsoleConfig.INVENTORY_PAGE_PATH + "WarehouseTransferOrderInfoList.jsp");
		rd.forward(request, response);
	}

}
