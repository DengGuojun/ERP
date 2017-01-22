package com.lpmas.erp.bom.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.bom.bean.BomInfoBean;
import com.lpmas.erp.bom.bean.BomItemBean;
import com.lpmas.erp.bom.bean.BomItemEntityBean;
import com.lpmas.erp.bom.business.BomInfoBusiness;
import com.lpmas.erp.bom.business.BomItemBusiness;
import com.lpmas.erp.bom.config.BomConsoleConfig;
import com.lpmas.erp.bom.config.BomResource;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;

@WebServlet("/erp/BomInfoManage.do")
public class BomInfoManage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BomInfoManage() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		boolean readOnly = ParamKit.getBooleanParameter(request, "readOnly", false);
		int bomId = ParamKit.getIntParameter(request, "bomId", 0);
		int bomType = ParamKit.getIntParameter(request, "bomType", 0);
		BomInfoBusiness bomInfoBusiness = new BomInfoBusiness();
		BomInfoBean bomInfoBean = null;
		List<BomItemEntityBean> bomItemEntityProductList = new ArrayList<BomItemEntityBean>();
		List<BomItemEntityBean> bomItemEntityMaterialList = new ArrayList<BomItemEntityBean>();
		if (bomId > 0) {
			if (!readOnly && !adminUserHelper.checkPermission(BomResource.BOM_INFO, OperationConfig.UPDATE)) {
				return;
			}
			if (readOnly && !adminUserHelper.checkPermission(BomResource.BOM_INFO, OperationConfig.SEARCH)) {
				return;
			}
			bomInfoBean = bomInfoBusiness.getBomInfoByKey(bomId);
			BomItemBusiness bomItemBusiness = new BomItemBusiness();
			// 根据bom item 查出对应的制品信息
			List<BomItemBean> bomItemList = bomItemBusiness.getBomItemListByBomId(bomId);
			for (BomItemBean bean : bomItemList) {
				BomItemEntityBean bomItemEntityBean = new BomItemEntityBean(bean);
				// 商品项
				if (bean.getWareType() == InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM) {
					bomItemEntityProductList.add(bomItemEntityBean);
					// 物料
				} else if (bean.getWareType() == InfoTypeConfig.INFO_TYPE_MATERIAL) {
					bomItemEntityMaterialList.add(bomItemEntityBean);
				}
			}
		} else {
			if (!adminUserHelper.checkPermission(BomResource.BOM_INFO, OperationConfig.CREATE)) {
				return;
			}
			if (bomType == 0) {
				HttpResponseKit.alertMessage(response, "BOM类型错误", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			bomInfoBean = new BomInfoBean();
			bomInfoBean.setStatus(Constants.STATUS_VALID);
			bomInfoBean.setIsActive(Constants.STATUS_VALID);
			bomInfoBean.setBomType(bomType);
		}
		request.setAttribute("BomItemEntityProductList", bomItemEntityProductList);
		request.setAttribute("BomItemEntityMaterialList", bomItemEntityMaterialList);
		request.setAttribute("BomInfoBean", bomInfoBean);

		// 请求转发
		RequestDispatcher rd = request.getRequestDispatcher(BomConsoleConfig.BOM_PAGE_PATH + "BomInfoManage.jsp");
		rd.forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int bomId = ParamKit.getIntParameter(request, "bomId", 0);
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		BomInfoBean bomInfoBean = BeanKit.request2Bean(request, BomInfoBean.class);
		BomInfoBusiness bomInfoBusiness = new BomInfoBusiness();
		BomItemBusiness bomItemBusiness = new BomItemBusiness();

		// 新建时必须录入明细
		String[] selectedItemValue = new String[] {};
		try {
			selectedItemValue = ParamKit.getArrayParameter(request, "selectedItemValue");
			if (!bomItemBusiness.isVaildArray(selectedItemValue) || selectedItemValue == null) {
				HttpResponseKit.alertMessage(response, "BOM明细必须填写", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
		} catch (Exception e) {
			HttpResponseKit.alertMessage(response, "BOM明细必须填写", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}
		// 开始存储BOM的INFO
		int result = 0;
		if (bomId > 0) {
			// 修改
			if (!adminUserHelper.checkPermission(BomResource.BOM_INFO, OperationConfig.UPDATE)) {
				return;
			}
			bomInfoBean.setModifyUser(adminUserHelper.getAdminUserId());
			result = bomInfoBusiness.updateBomInfo(bomInfoBean);
			if (result < 0) {
				HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
		} else {
			// 新增
			if (!adminUserHelper.checkPermission(BomResource.BOM_INFO, OperationConfig.CREATE)) {
				return;
			}
			bomInfoBean.setCreateUser(adminUserHelper.getAdminUserId());
			result = bomInfoBusiness.addBomInfo(bomInfoBean);
			bomId = result;
		}
		if (result > 0) {
			// 处理明细
			result = bomItemBusiness.processBomItem(selectedItemValue, bomId, adminUserHelper.getAdminUserId());
		}

		if (result > 0) {
			HttpResponseKit.alertMessage(response, "操作成功", "/erp/BomInfoList.do");
		} else {
			HttpResponseKit.alertMessage(response, "操作失败", HttpResponseKit.ACTION_HISTORY_BACK);
		}

	}

}
