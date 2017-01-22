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
import com.lpmas.erp.config.ErpConsoleConfig;
import com.lpmas.erp.purchase.bean.DeliveryNoteInfoBean;
import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.business.DeliveryNoteInfoBusiness;
import com.lpmas.erp.purchase.business.PurchaseOrderInfoBusiness;
import com.lpmas.erp.purchase.config.PurchaseOrderConsoleConfig;
import com.lpmas.erp.purchase.config.PurchaseOrderResource;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.tms.transporter.business.TransporterInfoMediator;


/**
 * Servlet implementation class DeliveryNoteInfoList
 */
@WebServlet("/erp/DeliveryNoteInfoList.do")
public class DeliveryNoteInfoList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeliveryNoteInfoList() {
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
		boolean readOnly = ParamKit.getBooleanParameter(request, "readOnly", false);
		if (!readOnly && !adminUserHelper.checkPermission(PurchaseOrderResource.PURCHASE_ORDER_INFO,
				OperationConfig.UPDATE)) {
			return;
		}

		// 初始化页面分页参数
		int pageNum = ParamKit.getIntParameter(request, "pageNum", ErpConsoleConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", ErpConsoleConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);
		// 获取页面请求参数
		// 插入查询参数MAP
		HashMap<String, String> condMap = new HashMap<String, String>();
		String poId = ParamKit.getParameter(request, "poId", "").trim();
		PurchaseOrderInfoBusiness poInfoBusiness = new PurchaseOrderInfoBusiness();
		PurchaseOrderInfoBean poInfoBean = poInfoBusiness.getPurchaseOrderInfoByKey(Integer.valueOf(poId));
		if (poInfoBean != null) {
			if (poInfoBean.getStatus() == Constants.STATUS_NOT_VALID) {
				HttpResponseKit.alertMessage(response, "采购订单已被删除", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			} else {
				condMap.put("poId", poId);
			}
		} else {
			HttpResponseKit.alertMessage(response, "采购单号缺失", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}

		// 从数据库中获取相应的数据
		DeliveryNoteInfoBusiness deliveryNoteInfoBusiness = new DeliveryNoteInfoBusiness();
		PageResultBean<DeliveryNoteInfoBean> result = deliveryNoteInfoBusiness.getDeliveryNoteInfoPageListByMap(condMap,
				pageBean);
		
		// 把数据放到页面
		request.setAttribute("DeliveryNoteInfoList", result.getRecordList());
		request.setAttribute("PurchaseOrderInfoBean", poInfoBean);
		// 初始化分页数据
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("AdminUserHelper", adminUserHelper);
		
		TransporterInfoMediator transporterInfoMediator = new TransporterInfoMediator();
		request.setAttribute("TransporterInfoMediator", transporterInfoMediator);

		// 请求转发
		RequestDispatcher rd = request
				.getRequestDispatcher(PurchaseOrderConsoleConfig.PURCHASE_ORDER_PAGE_PATH + "DeliveryNoteInfoList.jsp");
		rd.forward(request, response);
	}
}
