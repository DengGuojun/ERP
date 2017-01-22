package com.lpmas.erp.purchase.action;

import java.io.IOException;
import java.util.ArrayList;
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
import com.lpmas.erp.config.ErpConsoleConfig;
import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.bean.PurchaseOrderInfoEntityBean;
import com.lpmas.erp.purchase.business.PurchaseOrderInfoBusiness;
import com.lpmas.erp.purchase.config.PurchaseOrderConsoleConfig;
import com.lpmas.erp.purchase.config.PurchaseOrderResource;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.srm.bean.SupplierInfoBean;
import com.lpmas.srm.client.SrmServiceClient;

/**
 * Servlet implementation class PurchaseOrderInfoSelect
 */
@WebServlet("/erp/PurchaseOrderInfoSelect.do")
public class PurchaseOrderInfoSelect extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PurchaseOrderInfoSelect() {
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
		if (!adminUserHelper.checkPermission(PurchaseOrderResource.PURCHASE_ORDER_INFO, OperationConfig.SEARCH)) {
			return;
		}
		int poId = ParamKit.getIntParameter(request, "poId", 0);
		String mode = ParamKit.getParameter(request, "mode", "general");
		String poNumber = ParamKit.getParameter(request, "poNumber", "").trim();
		int pageNum = ParamKit.getIntParameter(request, "pageNum", ErpConsoleConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", ErpConsoleConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);
		HashMap<String, String> condMap = ParamKit.getParameterMap(request, "poNumber,wareType", "");
		if (mode.equals("log")) {
			condMap.put("status", "");
		} else {
			condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		}
		if(StringKit.isValid(poNumber)){
			condMap.put("poNumber", poNumber);
		}
		request.setAttribute("mode", mode);
		List<PurchaseOrderInfoEntityBean> poInfoEntityBeanList = new ArrayList<PurchaseOrderInfoEntityBean>();
		PurchaseOrderInfoBusiness business = new PurchaseOrderInfoBusiness();
		SrmServiceClient srmServiceClient = new SrmServiceClient();
		PageResultBean<PurchaseOrderInfoBean> result = business.getPurchaseOrderInfoPageListByMap(condMap, pageBean);
		for (PurchaseOrderInfoBean bean : result.getRecordList()) {
			SupplierInfoBean supplierInfoBean = srmServiceClient.getSupplierInfoByKey(bean.getSupplierId());
			PurchaseOrderInfoEntityBean entityBean = new PurchaseOrderInfoEntityBean(bean, null, supplierInfoBean, null,
					null);
			poInfoEntityBeanList.add(entityBean);
		}
		request.setAttribute("PurchaseOrderInfoEntityList", poInfoEntityBeanList);
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("PoId", poId);
		RequestDispatcher rd = request.getRequestDispatcher(
				PurchaseOrderConsoleConfig.PURCHASE_ORDER_PAGE_PATH + "PurchaseOrderInfoSelect.jsp");
		rd.forward(request, response);
	}
}
