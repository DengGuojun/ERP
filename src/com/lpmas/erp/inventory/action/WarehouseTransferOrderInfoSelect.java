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
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.web.ParamKit;

@WebServlet("/erp/WarehouseTransferOrderInfoSelect.do")
public class WarehouseTransferOrderInfoSelect extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public WarehouseTransferOrderInfoSelect() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_TRANSFER_ORDER_INFO, OperationConfig.SEARCH)) {
			return;
		}
		int toId = ParamKit.getIntParameter(request, "toId", 0);
		int pageNum = ParamKit.getIntParameter(request, "pageNum", ErpConsoleConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", ErpConsoleConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);
		HashMap<String, String> condMap = ParamKit.getParameterMap(request, "toNumber,wareType", "");
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		WarehouseTransferOrderInfoBusiness business = new WarehouseTransferOrderInfoBusiness();
		PageResultBean<WarehouseTransferOrderInfoBean> result = business.getWarehouseTransferOrderInfoPageListByMap(condMap, pageBean);
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("TransferOrderInfoList", result.getRecordList());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("toId", toId);
		// 查出仓库信息列表筛选用
		WarehouseInfoBusiness warehouseInfoBusiness = new WarehouseInfoBusiness();
		Map<Integer,WarehouseInfoBean> warehouseInfoMap = new HashMap<Integer,WarehouseInfoBean>();
		for(WarehouseTransferOrderInfoBean bean: result.getRecordList()){
			WarehouseInfoBean warehouseInfoBean = warehouseInfoBusiness.getWarehouseInfoByKey(bean.getSourceWarehouseId());
			warehouseInfoMap.put(warehouseInfoBean.getWarehouseId(), warehouseInfoBean);
			warehouseInfoBean = warehouseInfoBusiness.getWarehouseInfoByKey(bean.getTargetWarehouseId());
			warehouseInfoMap.put(warehouseInfoBean.getWarehouseId(), warehouseInfoBean);
		}
		request.setAttribute("WarehouseInfoMap", warehouseInfoMap);
		// 请求转发
		RequestDispatcher rd = request.getRequestDispatcher(InventoryConsoleConfig.INVENTORY_PAGE_PATH + "WarehouseTransferOrderInfoSelect.jsp");
		rd.forward(request, response);
	}

}
