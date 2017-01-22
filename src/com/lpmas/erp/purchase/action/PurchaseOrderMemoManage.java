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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.admin.client.cache.AdminUserInfoClientCache;
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.erp.config.ErpConsoleConfig;
import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.bean.PurchaseOrderMemoBean;
import com.lpmas.erp.purchase.business.PurchaseOrderInfoBusiness;
import com.lpmas.erp.purchase.business.PurchaseOrderMemoBusiness;
import com.lpmas.erp.purchase.config.PurchaseOrderConsoleConfig;
import com.lpmas.erp.purchase.config.PurchaseOrderResource;
import com.lpmas.erp.purchase.util.PurchaseOrderStatusHelper;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.framework.web.ReturnMessageBean;

/**
 * Servlet implementation class PurchaseOrderMemoManage
 */
@WebServlet("/erp/PurchaseOrderMemoManage.do")
public class PurchaseOrderMemoManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(PurchaseOrderInfoManage.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PurchaseOrderMemoManage() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		boolean readOnly = ParamKit.getBooleanParameter(request, "readOnly", false);
		if (!readOnly && !adminUserHelper.checkPermission(PurchaseOrderResource.PURCHASE_ORDER_INFO,
				OperationConfig.UPDATE)) {
			return;
		}
		if (readOnly && !adminUserHelper.checkPermission(PurchaseOrderResource.PURCHASE_ORDER_INFO,
				OperationConfig.SEARCH)) {
			return;
		}

		int pageNum = ParamKit.getIntParameter(request, "pageNum", ErpConsoleConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", ErpConsoleConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);

		int poId = ParamKit.getIntParameter(request, "poId", 0);
		PurchaseOrderInfoBusiness infoBusiness = new PurchaseOrderInfoBusiness();
		PurchaseOrderInfoBean poInfoBean = infoBusiness.getPurchaseOrderInfoByKey(poId);
		if (poInfoBean == null) {
			HttpResponseKit.alertMessage(response, "采购单号缺失", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}
		if (poInfoBean.getStatus() == Constants.STATUS_NOT_VALID) {
			HttpResponseKit.alertMessage(response, "采购订单已被删除", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}
		if (PurchaseOrderStatusHelper.isLock(poInfoBean) && !readOnly) {
			HttpResponseKit.alertMessage(response, "采购订单被锁定，不能编辑", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}

		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("poId", String.valueOf(poId));

		PurchaseOrderMemoBusiness business = new PurchaseOrderMemoBusiness();
		PageResultBean<PurchaseOrderMemoBean> result = business.getPurchaseOrderMemoPageListByMap(condMap, pageBean);

		// 根据CreateUser获取对应的UserName
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (PurchaseOrderMemoBean bean : result.getRecordList()) {
			AdminUserInfoClientCache clientCache = new AdminUserInfoClientCache();
			map.put(bean.getCreateUser(), clientCache.getAdminUserNameByKey(bean.getCreateUser()));
		}

		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("AdminUserInfoMap", map);
		request.setAttribute("MemoList", result.getRecordList());
		request.setAttribute("PurchaseOrderInfoBean", poInfoBean);
		request.setAttribute("AdminUserHelper", adminUserHelper);
		RequestDispatcher rd = this.getServletContext().getRequestDispatcher(
				PurchaseOrderConsoleConfig.PURCHASE_ORDER_PAGE_PATH + "PurchaseOrderMemoManage.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(PurchaseOrderResource.PURCHASE_ORDER_INFO, OperationConfig.UPDATE)) {
			return;
		}
		PurchaseOrderMemoBean bean = new PurchaseOrderMemoBean();
		try {
			bean = BeanKit.request2Bean(request, PurchaseOrderMemoBean.class);
			PurchaseOrderMemoBusiness business = new PurchaseOrderMemoBusiness();
			ReturnMessageBean messageBean = business.verifyPurchaseOrderMemo(bean);
			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			int result = 0;
			bean.setCreateUser(adminUserHelper.getAdminUserId());
			result = business.addPurchaseOrderMemo(bean);
			if (result > 0) {
				HttpResponseKit.alertMessage(response, "处理成功",
						"/erp/PurchaseOrderMemoManage.do?poId=" + bean.getPoId());
			} else {
				HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
			}
		} catch (Exception e) {
			log.error("", e);
		} finally {
		}
	}
}
