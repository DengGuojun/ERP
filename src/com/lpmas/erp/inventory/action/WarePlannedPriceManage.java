package com.lpmas.erp.inventory.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.inventory.bean.WarePlannedPriceBean;
import com.lpmas.erp.inventory.business.WarePlannedPriceBusiness;
import com.lpmas.erp.inventory.config.InventoryConsoleConfig;
import com.lpmas.erp.report.config.ReportResource;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.MaterialInfoBean;
import com.lpmas.pdm.bean.ProductItemBean;
import com.lpmas.pdm.bean.UnitInfoBean;
import com.lpmas.pdm.client.PdmServiceClient;

/**
 * Servlet implementation class WarePlannedPriceManage
 */
@WebServlet("/erp/WarePlannedPriceManage.do")
public class WarePlannedPriceManage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WarePlannedPriceManage() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(ReportResource.ERP_REPORT_INFO, OperationConfig.SEARCH)) {
			return;
		}

		int wareId = ParamKit.getIntParameter(request, "wareId", 0);
		int wareType = ParamKit.getIntParameter(request, "wareType", 0);
		String planMonth = ParamKit.getParameter(request, "planMonth", "").trim();
		String unit = ParamKit.getParameter(request, "unit", "").trim();

		boolean isModify = true;
		WarePlannedPriceBusiness business = new WarePlannedPriceBusiness();
		WarePlannedPriceBean warePlannedPriceBean = business.getWarePlannedPriceByKey(wareType, wareId, planMonth,
				unit);

		if (warePlannedPriceBean == null) {
			warePlannedPriceBean = new WarePlannedPriceBean();
			isModify = false;
		}

		String wareNumber = "";
		String wareName = "";
		String wareTypeName = "";
		String unitName = "";

		if (isModify) {
			PdmServiceClient client = new PdmServiceClient();
			MaterialInfoBean materialInfoBean = null;
			ProductItemBean productItemBean = null;
			if (warePlannedPriceBean.getWareType() == InfoTypeConfig.INFO_TYPE_MATERIAL) {
				materialInfoBean = client.getMaterialInfoByKey(warePlannedPriceBean.getWareId());
				if (materialInfoBean != null && materialInfoBean.getMaterialId() == warePlannedPriceBean.getWareId()) {
					wareNumber = materialInfoBean.getMaterialNumber();
					wareName = materialInfoBean.getMaterialName();
					wareTypeName = "物料";
				}
			} else if (warePlannedPriceBean.getWareType() == InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM) {
				productItemBean = client.getProductItemByKey(warePlannedPriceBean.getWareId());
				if (productItemBean != null && productItemBean.getItemId() == warePlannedPriceBean.getWareId()) {
					wareNumber = productItemBean.getItemNumber();
					wareName = productItemBean.getItemName();
					wareTypeName = "商品项";
				}
			}

			UnitInfoBean unitInfoBean = client.getUnitInfoByCode(warePlannedPriceBean.getUnit());
			if (unitInfoBean != null)
				unitName = unitInfoBean.getUnitName();
		}

		request.setAttribute("unitName", unitName);
		request.setAttribute("wareNumber", wareNumber);
		request.setAttribute("wareName", wareName);
		request.setAttribute("wareTypeName", wareTypeName);
		request.setAttribute("isModify", isModify);
		request.setAttribute("WarePlannedPriceBean", warePlannedPriceBean);
		request.setAttribute("AdminUserHelper", adminUserHelper);
		// 请求转发
		RequestDispatcher rd = request
				.getRequestDispatcher(InventoryConsoleConfig.INVENTORY_PAGE_PATH + "WarePlannedPriceManage.jsp");
		rd.forward(request, response);
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

		try {
			WarePlannedPriceBean bean = BeanKit.request2Bean(request, WarePlannedPriceBean.class);

			// 数据验证
			WarePlannedPriceBusiness business = new WarePlannedPriceBusiness();
			ReturnMessageBean messageBean = business.verifyWarePlannedPriceBean(bean);

			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			int result = -1;
			WarePlannedPriceBean existBean = business.getWarePlannedPriceByKey(bean.getWareType(), bean.getWareId(),
					bean.getPlanMonth(), bean.getUnit());
			if (existBean == null) {
				// 新增
				if (!adminUserHelper.checkPermission(ReportResource.ERP_REPORT_INFO, OperationConfig.CREATE)) {
					return;
				}
				bean.setCreateUser(adminUserHelper.getAdminUserId());
				result = business.addWarePlannedPrice(bean);
			} else {
				// 修改
				if (!adminUserHelper.checkPermission(ReportResource.ERP_REPORT_INFO, OperationConfig.UPDATE)) {
					return;
				}
				bean.setModifyUser(adminUserHelper.getAdminUserId());
				result = business.updateWarePlannedPrice(bean);
			}

			if (result >= 0) {
				HttpResponseKit.alertMessage(response, "操作成功", "/erp/WarePlannedPriceList.do");
				return;
			} else {
				HttpResponseKit.alertMessage(response, "操作失败", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
		} catch (Exception e) {
			HttpResponseKit.alertMessage(response, "操作失败:" + e.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}

	}

}
