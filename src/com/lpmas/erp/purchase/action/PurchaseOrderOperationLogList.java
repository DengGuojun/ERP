package com.lpmas.erp.purchase.action;

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
import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.business.PurchaseOrderInfoBusiness;
import com.lpmas.erp.purchase.config.PurchaseOrderConsoleConfig;
import com.lpmas.erp.purchase.config.PurchaseOrderResource;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.log.bean.DataLogBean;
import com.lpmas.log.client.LogServiceClient;
import com.lpmas.system.client.cache.SysApplicationInfoClientCache;

/**
 * Servlet implementation class OperationLogList
 */
@WebServlet("/erp/PurchaseOrderOperationLogList.do")
public class PurchaseOrderOperationLogList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PurchaseOrderOperationLogList() {
		super();
		// TODO Auto-generated constructor stub
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
		if (!adminUserHelper.checkPermission(PurchaseOrderResource.PURCHASE_ORDER_OPERATION_LOG,
				OperationConfig.SEARCH)) {
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
		int poId = ParamKit.getIntParameter(request, "poId", 0);
		String mode = ParamKit.getParameter(request, "mode", "general");
		PurchaseOrderInfoBusiness poInfoBusiness = new PurchaseOrderInfoBusiness();
		PurchaseOrderInfoBean poInfoBean = poInfoBusiness.getPurchaseOrderInfoByKey(poId);
		if (mode.equals("po")) {
			if (poInfoBean == null) {
				HttpResponseKit.alertMessage(response, "采购单号缺失", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			} else {
				if (poInfoBean.getStatus() == Constants.STATUS_NOT_VALID) {
					HttpResponseKit.alertMessage(response, "采购订单已被删除", HttpResponseKit.ACTION_HISTORY_BACK);
					return;
				}
			}
		}
		if (poInfoBean != null) {
			condMap.put("infoId1", String.valueOf(poId));
			request.setAttribute("PurchaseOrderInfoBean", poInfoBean);
		}
		condMap.put("appId", sysCache.getSysApplicationIdByCode(ErpConsoleConfig.APP_ID) + "");
		condMap.put("infoType", String.valueOf(InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER));
		// 查询
		LogServiceClient client = new LogServiceClient();
		PageResultBean<DataLogBean> result = client.getDataLogPageListByMap(condMap, pageBean);
		// 装载参数
		request.setAttribute("DataLogList", result.getRecordList());
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));

		// 请求转发
		RequestDispatcher rd = request.getRequestDispatcher(
				PurchaseOrderConsoleConfig.PURCHASE_ORDER_PAGE_PATH + "PurchaseOrderOperationLogList.jsp");
		rd.forward(request, response);
	}

}
