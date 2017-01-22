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
import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.config.ErpConsoleConfig;
import com.lpmas.erp.inventory.bean.WarehouseTransferOrderInfoBean;
import com.lpmas.erp.inventory.business.WarehouseTransferOrderInfoBusiness;
import com.lpmas.erp.inventory.config.InventoryConsoleConfig;
import com.lpmas.erp.inventory.config.InventoryResource;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.log.bean.DataLogBean;
import com.lpmas.log.client.LogServiceClient;
import com.lpmas.system.client.cache.SysApplicationInfoClientCache;

@WebServlet("/erp/WarehouseTransferOperationLogList.do")
public class WarehouseTransferOperationLogList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WarehouseTransferOperationLogList() {
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
		if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_TRANSFER_ORDER_INFO_LOG, OperationConfig.SEARCH)) {
			return;
		}
		SysApplicationInfoClientCache sysCache = new SysApplicationInfoClientCache();
		HashMap<String, String> condMap = new HashMap<String, String>();
		// 初始化页面分页参数
		int pageNum = ParamKit.getIntParameter(request, "pageNum", ErpConsoleConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", ErpConsoleConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);
		condMap = ParamKit.getParameterMap(request, "field1,begDateTime,endDateTime", "");
		// 获取页面请求参数
		int toId = ParamKit.getIntParameter(request, "toId", 0);
		String mode = ParamKit.getParameter(request, "mode", "general");
		WarehouseTransferOrderInfoBusiness warehouseTransferOrderInfoBusiness = new WarehouseTransferOrderInfoBusiness();
		WarehouseTransferOrderInfoBean warehouseTransferOrderInfoBean = warehouseTransferOrderInfoBusiness.getWarehouseTransferOrderInfoByKey(toId);
		if (mode.equals("po")) {
			if (warehouseTransferOrderInfoBean == null) {
				HttpResponseKit.alertMessage(response, "调拨单号缺失", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			} else {
				if (warehouseTransferOrderInfoBean.getStatus() == Constants.STATUS_NOT_VALID) {
					HttpResponseKit.alertMessage(response, "调拨单已被删除", HttpResponseKit.ACTION_HISTORY_BACK);
					return;
				}
			}
		}
		if (warehouseTransferOrderInfoBean != null) {
			condMap.put("infoId1", String.valueOf(toId));
			request.setAttribute("WarehouseTransferOrderInfoBean", warehouseTransferOrderInfoBean);
		}
		condMap.put("appId", String.valueOf(sysCache.getSysApplicationIdByCode(ErpConsoleConfig.APP_ID)));
		condMap.put("infoType", String.valueOf(InfoTypeConfig.INFO_TYPE_WAREHOUSE_TRANSFER_ORDER));
		// 查询
		LogServiceClient client = new LogServiceClient();
		PageResultBean<DataLogBean> result = client.getDataLogPageListByMap(condMap, pageBean);
		// 装载参数
		request.setAttribute("DataLogList", result.getRecordList());
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));

		// 请求转发
		RequestDispatcher rd = request.getRequestDispatcher(InventoryConsoleConfig.INVENTORY_PAGE_PATH + "WarehouseTransferOperationLogList.jsp");
		rd.forward(request, response);
	}

}
