package com.lpmas.erp.contract.action;

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

import com.lpmas.erp.config.ErpConsoleConfig;
import com.lpmas.erp.contract.bean.PurchaseContractInfoBean;
import com.lpmas.erp.contract.bean.PurchaseContractInfoEntityBean;
import com.lpmas.erp.contract.business.PurchaseContractInfoBusiness;
import com.lpmas.erp.contract.config.PurchaseContractConsoleConfig;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.srm.bean.SupplierInfoBean;
import com.lpmas.srm.client.cache.SupplierInfoClientCache;

/**
 * Servlet implementation class PurchaseContractInfoSelect
 */
@WebServlet("/erp/PurchaseContractInfoSelect.do")
public class PurchaseContractInfoSelect extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PurchaseContractInfoSelect() {
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
		int pageNum = ParamKit.getIntParameter(request, "pageNum", ErpConsoleConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", ErpConsoleConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);

		HashMap<String, String> condMap = new HashMap<String, String>();
		int contractId = ParamKit.getIntParameter(request, "contractId", 0);
		String queryParam = ParamKit.getParameter(request, "queryParam", "").trim();

		PurchaseContractInfoBusiness business = new PurchaseContractInfoBusiness();
		SupplierInfoClientCache cache = new SupplierInfoClientCache();
		List<PurchaseContractInfoEntityBean> contractInfoEntityBeanList = new ArrayList<PurchaseContractInfoEntityBean>();
		PageResultBean<PurchaseContractInfoBean> result = business.getPurchaseContractInfoPageListByFuzzyQueryParam(queryParam, pageBean);

		for (PurchaseContractInfoBean bean : result.getRecordList()) {
			SupplierInfoBean supplierInfoBean = cache.getSupplierInfoByKey(bean.getSupplierId());
			PurchaseContractInfoEntityBean entityBean = new PurchaseContractInfoEntityBean(bean, supplierInfoBean);
			contractInfoEntityBeanList.add(entityBean);
		}

		request.setAttribute("PurchaseContractInfoList", contractInfoEntityBeanList);
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("ContractId", contractId);
		RequestDispatcher rd = request
				.getRequestDispatcher(PurchaseContractConsoleConfig.PURCHASE_CONTRACT_PAGE_PATH + "PurchaseContractInfoSelect.jsp");
		rd.forward(request, response);
	}

}
