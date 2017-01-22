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
import com.lpmas.erp.inventory.bean.DeliveryVoucherInfoBean;
import com.lpmas.erp.inventory.business.DeliveryVoucherInfoBusiness;
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

@WebServlet("/erp/DeliveryVoucherInfoList.do")
public class DeliveryVoucherInfoList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DeliveryVoucherInfoList() {
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
		int wareType = ParamKit.getIntParameter(request, "wareType", 0);
		if (wareType != InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM && wareType != InfoTypeConfig.INFO_TYPE_MATERIAL) {
			HttpResponseKit.alertMessage(response, "制品类型不合法", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}
		int dvType = ParamKit.getIntParameter(request, "dvType", 0);
		int pageNum = ParamKit.getIntParameter(request, "pageNum", ErpConsoleConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", ErpConsoleConfig.DEFAULT_PAGE_SIZE);
		HashMap<String, String> condMap = new HashMap<String, String>();
		PageBean pageBean = new PageBean(pageNum, pageSize);
		condMap = ParamKit.getParameterMap(request, "dvNumber,reviewStatus,poStatus,createUser,warehouseName,ncStatus,warehouseId,createUser", "");
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
		if (dvType > 0) {
			condMap.put("dvType", String.valueOf(dvType));
		}
		condMap.put("wareType", String.valueOf(wareType));
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		DeliveryVoucherInfoBusiness business = new DeliveryVoucherInfoBusiness();
		PageResultBean<DeliveryVoucherInfoBean> result = business.getDeliveryVoucherInfoPageListByMap(condMap, pageBean);
		request.setAttribute("DeliveryVoucherList", result.getRecordList());
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("WareType", wareType);
		request.setAttribute("dvType", dvType);
		request.setAttribute("AdminUserHelper", adminUserHelper);
		// 查出仓库信息列表筛选用
		WarehouseInfoBusiness warehouseInfoBusiness = new WarehouseInfoBusiness();
		List<WarehouseInfoBean> warehouseInfoList = warehouseInfoBusiness.getWarehouseInfoAllList();
		request.setAttribute("WarehouseInfoList", warehouseInfoList);
		request.setAttribute("WarehouseInfoMap", ListKit.list2Map(warehouseInfoList, "warehouseId", "warehouseName"));
		// 查出创建人信息列表筛选用
		WarehouseVoucherInfoBusiness warehouseVoucherInfoBusiness = new WarehouseVoucherInfoBusiness();
		List<Integer> warehouseVoucherCreaterUserList = warehouseVoucherInfoBusiness.getWarehouseVoucherCreaterUserAllList();
		request.setAttribute("warehouseVoucherCreaterUserList", warehouseVoucherCreaterUserList);
		// 请求转发
		RequestDispatcher rd = request.getRequestDispatcher(InventoryConsoleConfig.INVENTORY_PAGE_PATH + "DeliveryVoucherInfoList.jsp");
		rd.forward(request, response);
	}

}
