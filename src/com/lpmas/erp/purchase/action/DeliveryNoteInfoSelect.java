package com.lpmas.erp.purchase.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lpmas.erp.config.ErpConsoleConfig;
import com.lpmas.erp.purchase.bean.DeliveryNoteInfoBean;
import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.business.DeliveryNoteInfoBusiness;
import com.lpmas.erp.purchase.business.PurchaseOrderInfoBusiness;
import com.lpmas.erp.purchase.config.PurchaseOrderConsoleConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ParamKit;

@WebServlet("/erp/DeliveryNoteInfoSelect.do")
public class DeliveryNoteInfoSelect extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeliveryNoteInfoSelect() {
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
		// 初始化页面分页参数
		int pageNum = ParamKit.getIntParameter(request, "pageNum", ErpConsoleConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", ErpConsoleConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);
		// 获取页面请求参数
		// 插入查询参数MAP
		int dnId = ParamKit.getIntParameter(request, "dnId", 0);
		String dnNumber = ParamKit.getParameter(request, "dnNumber", "");
		HashMap<String, String> condMap = new HashMap<String, String>();
		if(StringKit.isValid(dnNumber)){
			condMap.put("dnNumber", dnNumber);
		}
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		// 从数据库中获取相应的数据
		DeliveryNoteInfoBusiness deliveryNoteInfoBusiness = new DeliveryNoteInfoBusiness();
		PageResultBean<DeliveryNoteInfoBean> result = deliveryNoteInfoBusiness.getDeliveryNoteInfoPageListByMap(condMap, pageBean);

		Map<Integer, PurchaseOrderInfoBean> purchaseOrderInfoMap = new HashMap<Integer, PurchaseOrderInfoBean>();
		PurchaseOrderInfoBusiness purchaseOrderInfoBusiness = new PurchaseOrderInfoBusiness();
		for (DeliveryNoteInfoBean deliveryNoteInfoBean : result.getRecordList()) {
			if (!purchaseOrderInfoMap.containsKey(deliveryNoteInfoBean.getPoId())) {
				PurchaseOrderInfoBean purchaseOrderInfoBean = purchaseOrderInfoBusiness.getPurchaseOrderInfoByKey(deliveryNoteInfoBean.getPoId());
				if (purchaseOrderInfoBean != null) {
					purchaseOrderInfoMap.put(deliveryNoteInfoBean.getPoId(), purchaseOrderInfoBean);
				}
			}
		}

		// 把数据放到页面
		request.setAttribute("DeliveryNoteInfoList", result.getRecordList());
		request.setAttribute("PurchaseOrderInfoMap", purchaseOrderInfoMap);
		// 初始化分页数据
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("dnId", dnId);

		// 请求转发
		RequestDispatcher rd = request.getRequestDispatcher(PurchaseOrderConsoleConfig.PURCHASE_ORDER_PAGE_PATH + "DeliveryNoteInfoSelect.jsp");
		rd.forward(request, response);
	}
}