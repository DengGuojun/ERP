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
import com.lpmas.erp.inventory.bean.WarePlannedPriceBean;
import com.lpmas.erp.inventory.business.WarePlannedPriceBusiness;
import com.lpmas.erp.inventory.config.InventoryConsoleConfig;
import com.lpmas.erp.report.config.ReportResource;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.pdm.bean.MaterialInfoBean;
import com.lpmas.pdm.bean.ProductItemBean;
import com.lpmas.pdm.client.PdmServiceClient;

/**
 * Servlet implementation class WarePlannedPriceList
 */
@WebServlet("/erp/WarePlannedPriceList.do")
public class WarePlannedPriceList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WarePlannedPriceList() {
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
		if (!adminUserHelper.checkPermission(ReportResource.ERP_REPORT_INFO, OperationConfig.SEARCH)) {
			return;
		}

		// 初始化页面分页参数
		int pageNum = ParamKit.getIntParameter(request, "pageNum", ErpConsoleConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", ErpConsoleConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);

		PdmServiceClient pdmServiceClient = new PdmServiceClient();
		WarePlannedPriceBusiness business = new WarePlannedPriceBusiness();

		HashMap<String, String> condMap = new HashMap<String, String>();
		String wareNumber = ParamKit.getParameter(request, "wareNumber", "").trim();
		if (StringKit.isValid(wareNumber)) {
			// 去PDM查询,优先匹配商品
			ProductItemBean productItemBean = pdmServiceClient.getProductItemByNumber(wareNumber);
			if (productItemBean != null) {
				condMap.put("wareType", String.valueOf(InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM));
				condMap.put("wareId", String.valueOf(productItemBean.getItemId()));
			} else {
				MaterialInfoBean materialInfoBean = pdmServiceClient.getMaterialInfoByNumber(wareNumber);
				if (materialInfoBean != null) {
					condMap.put("wareType", String.valueOf(InfoTypeConfig.INFO_TYPE_MATERIAL));
					condMap.put("wareId", String.valueOf(materialInfoBean.getMaterialId()));
				} else {
					condMap.put("wareType", String.valueOf(Constants.STATUS_NOT_VALID));
					condMap.put("wareId", String.valueOf(Constants.STATUS_NOT_VALID));
				}
			}
		}
		int wareType = ParamKit.getIntParameter(request, "wareType", 0);
		if (wareType > 0) {
			condMap.put("wareType", String.valueOf(wareType));
		}
		int planMonth = ParamKit.getIntParameter(request, "planMonth", 0);
		if (planMonth > 0) {
			condMap.put("planMonth", String.valueOf(planMonth));
		}

		PageResultBean<WarePlannedPriceBean> result = business.getWarePlannedPricePageListByMap(condMap, pageBean);
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("WarePlannedPriceList", result.getRecordList());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("AdminUserHelper", adminUserHelper);
		// 请求转发
		RequestDispatcher rd = request
				.getRequestDispatcher(InventoryConsoleConfig.INVENTORY_PAGE_PATH + "WarePlannedPriceList.jsp");
		rd.forward(request, response);

	}

}
