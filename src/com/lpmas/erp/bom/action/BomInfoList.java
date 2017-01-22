package com.lpmas.erp.bom.action;

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
import com.lpmas.erp.bom.bean.BomInfoBean;
import com.lpmas.erp.bom.business.BomInfoBusiness;
import com.lpmas.erp.bom.config.BomConsoleConfig;
import com.lpmas.erp.bom.config.BomResource;
import com.lpmas.erp.config.ErpConsoleConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ParamKit;

@WebServlet("/erp/BomInfoList.do")
public class BomInfoList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BomInfoList() {
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
		if (!adminUserHelper.checkPermission(BomResource.BOM_INFO, OperationConfig.SEARCH)) {
			return;
		}
		int pageNum = ParamKit.getIntParameter(request, "pageNum", ErpConsoleConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", ErpConsoleConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);

		HashMap<String, String> condMap = new HashMap<String, String>();
		// 筛选参数处理
		condMap = ParamKit.getParameterMap(request, "bomNumber,bomName,useStartTime,useEndTime", "");
		String isActive = ParamKit.getParameter(request, "isActive", String.valueOf(Constants.STATUS_VALID)).trim();
		if (StringKit.isValid(isActive)) {
			condMap.put("isActive", isActive);
		}
		int bomType = ParamKit.getIntParameter(request, "bomType", 0);
		if (bomType != 0) {
			condMap.put("bomType", String.valueOf(bomType));
		}
		// 查询
		BomInfoBusiness business = new BomInfoBusiness();
		PageResultBean<BomInfoBean> result = business.getBomInfoPageListByMap(condMap, pageBean);
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());

		request.setAttribute("BomInfoList", result.getRecordList());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("AdminUserHelper", adminUserHelper);

		// 请求转发
		RequestDispatcher rd = request.getRequestDispatcher(BomConsoleConfig.BOM_PAGE_PATH + "BomInfoList.jsp");
		rd.forward(request, response);
	}

}
