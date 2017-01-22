package com.lpmas.erp.contract.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.erp.contract.bean.PurchaseContractInfoBean;
import com.lpmas.erp.contract.business.PurchaseContractInfoBusiness;
import com.lpmas.erp.contract.config.PurchaseContractConsoleConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.ListKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.srm.bean.SupplierInfoBean;
import com.lpmas.srm.client.cache.SupplierInfoClientCache;

/**
 * Servlet implementation class PurchaseContractInfoManage
 */
@WebServlet("/erp/PurchaseContractInfoManage.do")
public class PurchaseContractInfoManage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PurchaseContractInfoManage() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int pcId = ParamKit.getIntParameter(request, "pcId", 0);

		PurchaseContractInfoBusiness purchaseContractInfoBusiness = new PurchaseContractInfoBusiness();
		PurchaseContractInfoBean purchaseContractInfoBean = null;
		if (pcId > 0) {
			purchaseContractInfoBean = purchaseContractInfoBusiness.getPurchaseContractInfoByKey(pcId);
		} else {
			purchaseContractInfoBean = new PurchaseContractInfoBean();
			purchaseContractInfoBean.setStatus(Constants.STATUS_VALID);
		}

		SupplierInfoClientCache cache = new SupplierInfoClientCache();
		List<SupplierInfoBean> supplierInfoList = cache.getSupplierInfoAllList();
		Map<Integer, SupplierInfoBean> supplierInfoMap = ListKit.list2Map(supplierInfoList, "supplierId");

		request.setAttribute("SupplierInfoList", supplierInfoList);
		request.setAttribute("SupplierInfoMap", supplierInfoMap);
		request.setAttribute("PurchaseContractInfoBean", purchaseContractInfoBean);

		// 请求转发
		RequestDispatcher rd = request
				.getRequestDispatcher(PurchaseContractConsoleConfig.PURCHASE_CONTRACT_PAGE_PATH + "PurchaseContractInfoManage.jsp");
		rd.forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int pcId = ParamKit.getIntParameter(request, "pcId", 0);
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		PurchaseContractInfoBean purchaseContractInfoBean = BeanKit.request2Bean(request, PurchaseContractInfoBean.class);
		PurchaseContractInfoBusiness purchaseContractInfoBusiness = new PurchaseContractInfoBusiness();

		int result = 0;
		if (pcId > 0) {
			// 修改
			purchaseContractInfoBean.setModifyUser(adminUserHelper.getAdminUserId());
			result = purchaseContractInfoBusiness.updatePurchaseContractInfo(purchaseContractInfoBean);
		} else {
			// 新增
			purchaseContractInfoBean.setCreateUser(adminUserHelper.getAdminUserId());
			result = purchaseContractInfoBusiness.addPurchaseContractInfo(purchaseContractInfoBean);
		}

		if (result > 0) {
			HttpResponseKit.alertMessage(response, "操作成功", "/erp/PurchaseContractInfoList.do");
		} else {
			HttpResponseKit.alertMessage(response, "操作失败", HttpResponseKit.ACTION_HISTORY_BACK);
		}

	}

}
