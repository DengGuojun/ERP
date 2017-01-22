package com.lpmas.erp.contract.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lpmas.erp.config.ErpConsoleConfig;
import com.lpmas.erp.contract.bean.PurchaseContractInfoBean;
import com.lpmas.erp.contract.business.PurchaseContractInfoBusiness;
import com.lpmas.erp.contract.config.PurchaseContractConsoleConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.ListKit;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.srm.bean.SupplierInfoBean;
import com.lpmas.srm.client.cache.SupplierInfoClientCache;

/**
 * Servlet implementation class PurchaseContractInfoList
 */
@WebServlet("/erp/PurchaseContractInfoList.do")
public class PurchaseContractInfoList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PurchaseContractInfoList() {
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
		int pageNum = ParamKit.getIntParameter(request, "pageNum", ErpConsoleConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", ErpConsoleConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);

		HashMap<String, String> condMap = new HashMap<String, String>();
		// 筛选参数处理
		String pcName = ParamKit.getParameter(request, "pcName", "");
		if (StringKit.isValid(pcName)) {
			condMap.put("pcName", pcName);
		}
		String status = ParamKit.getParameter(request, "status", String.valueOf(Constants.STATUS_VALID)).trim();
		if (StringKit.isValid(status)) {
			condMap.put("status", status);
		}

		// 查询
		PurchaseContractInfoBusiness business = new PurchaseContractInfoBusiness();
		PageResultBean<PurchaseContractInfoBean> result = business.getPurchaseContractInfoPageListByMap(condMap, pageBean);
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());

		SupplierInfoClientCache cache = new SupplierInfoClientCache();
		List<SupplierInfoBean> supplierInfoList = cache.getSupplierInfoAllList();
		Map<Integer, SupplierInfoBean> supplierInfoMap = ListKit.list2Map(supplierInfoList, "supplierId");

		request.setAttribute("SupplierInfoMap", supplierInfoMap);
		request.setAttribute("PurchaseContractInfoList", result.getRecordList());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));

		// 请求转发
		RequestDispatcher rd = request
				.getRequestDispatcher(PurchaseContractConsoleConfig.PURCHASE_CONTRACT_PAGE_PATH + "PurchaseContractInfoList.jsp");
		rd.forward(request, response);
	}

}
