package com.lpmas.erp.inventory.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import com.lpmas.erp.inventory.bean.WarehouseInventoryAggregateReportBean;
import com.lpmas.erp.inventory.business.WarehouseInventoryAggregateReportBusiness;
import com.lpmas.erp.inventory.config.InventoryConsoleConfig;
import com.lpmas.erp.inventory.config.InventoryResource;
import com.lpmas.erp.warehouse.bean.WarehouseInfoBean;
import com.lpmas.erp.warehouse.business.WarehouseInfoBusiness;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.pdm.bean.MaterialInfoBean;
import com.lpmas.pdm.bean.ProductItemBean;
import com.lpmas.pdm.client.PdmServiceClient;

/**
 * Servlet implementation class WarehouseInventoryList
 */
@WebServlet("/erp/WarehouseInventoryAggregateReportList.do")
public class WarehouseInventoryAggregateReportList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WarehouseInventoryAggregateReportList() {
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
		if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_INVENTORY, OperationConfig.SEARCH)) {
			return;
		}

		int wareType = ParamKit.getIntParameter(request, "wareType", 0);
		if (wareType != InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM && wareType != InfoTypeConfig.INFO_TYPE_MATERIAL) {
			HttpResponseKit.alertMessage(response, "制品类型不合法", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}

		// 初始化页面分页参数
		int pageNum = ParamKit.getIntParameter(request, "pageNum", ErpConsoleConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", ErpConsoleConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);

		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("wareType", String.valueOf(wareType));
		String materialNumber = ParamKit.getParameter(request, "materialNumber", "").trim();
		String productNumber = ParamKit.getParameter(request, "productNumber", "").trim();
		PdmServiceClient client = new PdmServiceClient();
		if (StringKit.isValid(materialNumber)) {
			MaterialInfoBean marterialInfoBean = client.getMaterialInfoByNumber(materialNumber);
			if (marterialInfoBean != null) {
				condMap.put("wareId", String.valueOf(marterialInfoBean.getMaterialId()));
			}
		}
		if (StringKit.isValid(productNumber)) {
			ProductItemBean productItemBean = client.getProductItemByNumber(productNumber);
			if (productItemBean != null) {
				condMap.put("wareId", String.valueOf(productItemBean.getItemId()));
			}
		}
		int typeId = ParamKit.getIntParameter(request, "typeId", 0);
		if (typeId != 0) {
			condMap.put("typeId", String.valueOf(typeId));
		}
		HashMap<String, HashMap<String, String>> scopeMap = new HashMap<String, HashMap<String, String>>();
		// 从Mongo中获取相应的数据
		WarehouseInventoryAggregateReportBusiness business = new WarehouseInventoryAggregateReportBusiness();
		try {
			PageResultBean<WarehouseInventoryAggregateReportBean> result = business.getWarehouseInventoryAggregateReportPageListByMap(condMap,
					scopeMap, pageBean);

			// 获取仓库名称
			Map<String, String> warehouseMap = new HashMap<String, String>();
			WarehouseInfoBusiness warehouseInfoBusiness = new WarehouseInfoBusiness();
			for (WarehouseInventoryAggregateReportBean reportBean : result.getRecordList()) {
				// 获取仓库
				WarehouseInfoBean warehouseBean = warehouseInfoBusiness.getWarehouseInfoByKey(reportBean.getWarehouseId());
				warehouseMap.put(reportBean.get_id(), warehouseBean.getWarehouseName());
			}

			request.setAttribute("WarehouseMap", warehouseMap);
			request.setAttribute("WarehouseInventoryReportList", result.getRecordList());
			// 初始化分页数据
			pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
			request.setAttribute("PageResult", pageBean);
			request.setAttribute("CondList", MapKit.map2List(condMap));
			request.setAttribute("AdminUserHelper", adminUserHelper);
			request.setAttribute("WareType", wareType);
		} catch (Exception e) {
			e.printStackTrace();
		}

		RequestDispatcher rd = request.getRequestDispatcher(InventoryConsoleConfig.INVENTORY_PAGE_PATH + "WarehouseInventoryAggregateReportList.jsp");
		rd.forward(request, response);

	}
}
