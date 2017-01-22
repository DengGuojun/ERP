package com.lpmas.erp.purchase.action;

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
import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.business.PurchaseOrderInfoBusiness;
import com.lpmas.erp.purchase.config.PurchaseOrderConsoleConfig;
import com.lpmas.erp.purchase.config.PurchaseOrderResource;
import com.lpmas.erp.warehouse.bean.WarehouseInfoBean;
import com.lpmas.erp.warehouse.business.WarehouseInfoBusiness;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.ListKit;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.srm.bean.SupplierInfoBean;
import com.lpmas.srm.client.SrmServiceClient;

/**
 * Servlet implementation class PurchaseOrderInfoList
 */
@WebServlet("/erp/PurchaseOrderInfoList.do")
public class PurchaseOrderInfoList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PurchaseOrderInfoList() {
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
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(PurchaseOrderResource.PURCHASE_ORDER_INFO, OperationConfig.SEARCH)) {
			return;
		}

		int pageNum = ParamKit.getIntParameter(request, "pageNum", ErpConsoleConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", ErpConsoleConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);
		int wareType = ParamKit.getIntParameter(request, "wareType", 0);
		if (wareType != InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM && wareType != InfoTypeConfig.INFO_TYPE_MATERIAL) {
			HttpResponseKit.alertMessage(response, "制品类型不合法", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}
		HashMap<String, String> condMap = new HashMap<String, String>();
		// 筛选参数处理
		condMap = ParamKit.getParameterMap(request,
				"poNumber,poType,contractStatus,invoiceStatus,paymentStatus,poStatus,reviewStatus,supplierId," + "warehouseId,wareNumber", "");
		condMap.put("wareType", String.valueOf(wareType));
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		PurchaseOrderInfoBusiness business = new PurchaseOrderInfoBusiness();
		PageResultBean<PurchaseOrderInfoBean> result = business.getPurchaseOrderInfoPageListByMap(condMap, pageBean);
		request.setAttribute("POList", result.getRecordList());
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("WareType", wareType);

		// 查出供应商信息列表筛选用
		SrmServiceClient srmServiceClient = new SrmServiceClient();
		List<SupplierInfoBean> supplierInfoList = srmServiceClient.getSupplierInfoAllList();
		request.setAttribute("SupplierInfoList", supplierInfoList);
		request.setAttribute("SupplierInfoMap", ListKit.list2Map(supplierInfoList, "supplierId", "supplierName"));
		// 查出仓库信息列表筛选用
		WarehouseInfoBusiness warehouseInfoBusiness = new WarehouseInfoBusiness();
		List<WarehouseInfoBean> warehouseInfoList = warehouseInfoBusiness.getWarehouseInfoAllList();
		request.setAttribute("WarehouseInfoList", warehouseInfoList);
		request.setAttribute("WarehouseInfoMap", ListKit.list2Map(warehouseInfoList, "warehouseId", "warehouseName"));
		request.setAttribute("AdminUserHelper", adminUserHelper);

		// 请求转发
		RequestDispatcher rd = request.getRequestDispatcher(PurchaseOrderConsoleConfig.PURCHASE_ORDER_PAGE_PATH + "PurchaseOrderInfoList.jsp");
		rd.forward(request, response);
	}
}
