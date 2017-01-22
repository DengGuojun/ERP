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
import com.lpmas.erp.inventory.bean.WarehouseVoucherInfoBean;
import com.lpmas.erp.inventory.business.WarehouseVoucherInfoBusiness;
import com.lpmas.erp.inventory.config.InventoryConsoleConfig;
import com.lpmas.erp.inventory.config.InventoryResource;
import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.business.PurchaseOrderInfoBusiness;
import com.lpmas.erp.warehouse.bean.WarehouseInfoBean;
import com.lpmas.erp.warehouse.business.WarehouseInfoBusiness;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.ListKit;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.srm.bean.SupplierInfoBean;
import com.lpmas.srm.client.SrmServiceClient;

/**
 * Servlet implementation class WarehouseVoucherInfoList
 */
@WebServlet("/erp/WarehouseVoucherInfoList.do")
public class WarehouseVoucherInfoList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WarehouseVoucherInfoList() {
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
		if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_VOUCHER_INFO, OperationConfig.SEARCH)) {
			return;
		}
		int wareType = ParamKit.getIntParameter(request, "wareType", 0);
		if (wareType != InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM && wareType != InfoTypeConfig.INFO_TYPE_MATERIAL) {
			HttpResponseKit.alertMessage(response, "制品类型不合法", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}
		int wvType = ParamKit.getIntParameter(request, "wvType", 0);
		int pageNum = ParamKit.getIntParameter(request, "pageNum", ErpConsoleConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", ErpConsoleConfig.DEFAULT_PAGE_SIZE);
		HashMap<String, String> condMap = new HashMap<String, String>();
		PageBean pageBean = new PageBean(pageNum, pageSize);
		condMap = ParamKit.getParameterMap(request, "wvNumber,reviewStatus,poStatus,createUser,warehouseName,ncStatus,warehouseId,createUser", "");
		String sourceOrderNumber = ParamKit.getParameter(request, "sourceOrderNumber", "");
		if (StringKit.isValid(sourceOrderNumber)) {
			PurchaseOrderInfoBusiness purchaseOrderInfoBusiness = new PurchaseOrderInfoBusiness();
			PurchaseOrderInfoBean purchaseOrderInfoBean = purchaseOrderInfoBusiness.getPurchaseOrderInfoByNumber(sourceOrderNumber);
			if (purchaseOrderInfoBean != null) {
				condMap.put("sourceOrderId", String.valueOf(purchaseOrderInfoBean.getPoId()));
			} else {
				condMap.put("sourceOrderId", "-1");
			}
		}
		if (wvType > 0) {
			condMap.put("wvType", String.valueOf(wvType));
		}
		condMap.put("wareType", String.valueOf(wareType));
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		WarehouseVoucherInfoBusiness business = new WarehouseVoucherInfoBusiness();
		PageResultBean<WarehouseVoucherInfoBean> result = business.getWarehouseVoucherInfoPageListByMap(condMap, pageBean);
		request.setAttribute("WarehouseVoucherList", result.getRecordList());
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("WareType", wareType);
		request.setAttribute("WvType", wvType);
		request.setAttribute("AdminUserHelper", adminUserHelper);
		// 查出仓库信息列表筛选用
		WarehouseInfoBusiness warehouseInfoBusiness = new WarehouseInfoBusiness();
		List<WarehouseInfoBean> warehouseInfoList = warehouseInfoBusiness.getWarehouseInfoAllList();
		request.setAttribute("WarehouseInfoList", warehouseInfoList);
		request.setAttribute("WarehouseInfoMap", ListKit.list2Map(warehouseInfoList, "warehouseId", "warehouseName"));
		// 查出供应商信息列表
		SrmServiceClient srmServiceClient = new SrmServiceClient();
		List<SupplierInfoBean> supplierInfoList = srmServiceClient.getSupplierInfoAllList();
		request.setAttribute("SupplierInfoMap", ListKit.list2Map(supplierInfoList, "supplierId", "supplierName"));
		// 查出创建人信息列表筛选用
		WarehouseVoucherInfoBusiness warehouseVoucherInfoBusiness = new WarehouseVoucherInfoBusiness();
		List<Integer> warehouseVoucherCreaterUserList = warehouseVoucherInfoBusiness.getWarehouseVoucherCreaterUserAllList();
		request.setAttribute("warehouseVoucherCreaterUserList", warehouseVoucherCreaterUserList);
		// 请求转发
		RequestDispatcher rd = request.getRequestDispatcher(InventoryConsoleConfig.INVENTORY_PAGE_PATH + "WarehouseVoucherInfoList.jsp");
		rd.forward(request, response);
	}
}
