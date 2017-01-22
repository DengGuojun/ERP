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
import com.lpmas.erp.inventory.bean.WarehouseInventoryPrewarningBean;
import com.lpmas.erp.inventory.business.WarehouseInventoryPrewarningBusiness;
import com.lpmas.erp.inventory.config.InventoryConsoleConfig;
import com.lpmas.erp.inventory.config.InventoryResource;
import com.lpmas.erp.inventory.config.WarehouseInventoryPrewarningConfig;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.client.PdmServiceClient;

/**
 * Servlet implementation class WarehouseInventoryPrewarningManage
 */
@WebServlet("/erp/WarehouseInventoryPrewarningManage.do")
public class WarehouseInventoryPrewarningManage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WarehouseInventoryPrewarningManage() {
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
		// 获取制品类型并判断
		int wareType = ParamKit.getIntParameter(request, "wareType", 0);
		int wareId = ParamKit.getIntParameter(request, "wareId", 0);
		int prewarningType = ParamKit.getIntParameter(request, "prewarningType", 0);

		WarehouseInventoryPrewarningBean prewarningBean = null;
		WarehouseInventoryPrewarningBusiness business = new WarehouseInventoryPrewarningBusiness();
		String wareName = "";
		// 当且仅当wareId和prewarningType都不为0是判定为更新
		if (wareId != 0 && prewarningType != 0 && wareType != 0) {
			// 更新
			// 修改入库单
			if (!readOnly && !adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_INVENTORY_PREWARNING, OperationConfig.UPDATE)) {
				return;
			}
			if (readOnly && !adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_INVENTORY_PREWARNING, OperationConfig.SEARCH)) {
				return;
			}
			prewarningBean = business.getWarehouseInventoryPrewarningByKey(wareType, wareId, prewarningType);
			// 根据WARETYPE和WAREID去查PDM获得制品名称
			PdmServiceClient client = new PdmServiceClient();
			if (wareType == InfoTypeConfig.INFO_TYPE_MATERIAL) {
				wareName = client.getMaterialInfoByKey(wareId).getMaterialName();
			} else {
				wareName = client.getProductItemByKey(wareId).getItemName();
			}
		} else {
			// 新建
			if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_INVENTORY_PREWARNING, OperationConfig.CREATE)) {
				return;
			}
			prewarningBean = new WarehouseInventoryPrewarningBean();
			prewarningBean.setStatus(Constants.STATUS_VALID);
		}

		request.setAttribute("WareName", wareName);
		request.setAttribute("AdminUserHelper", adminUserHelper);
		request.setAttribute("PrewarningBean", prewarningBean);

		RequestDispatcher rd = request.getRequestDispatcher(InventoryConsoleConfig.INVENTORY_PAGE_PATH + "WarehouseInventoryPrewarningManage.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);

		WarehouseInventoryPrewarningBean bean = BeanKit.request2Bean(request, WarehouseInventoryPrewarningBean.class);
		WarehouseInventoryPrewarningBusiness business = new WarehouseInventoryPrewarningBusiness();

		if (!WarehouseInventoryPrewarningConfig.PREWARNING_TYPE_MAP.containsKey(bean.getPrewarningType())) {
			HttpResponseKit.alertMessage(response, "预警类型非法", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}

		String guaranteePeriod = "";
		String maxInventory = "";
		String minInventory = "";
		// 数据准备阶段
		if (bean.getPrewarningType() == WarehouseInventoryPrewarningConfig.PREWARNING_TYPE_EXPIRATION) {
			guaranteePeriod = ParamKit.getParameter(request, WarehouseInventoryPrewarningConfig.GUARANTEE_PERIOD, "");
			if (!StringKit.isValid(guaranteePeriod)) {
				HttpResponseKit.alertMessage(response, "保质期预警值必须填写", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			try {
				Double.valueOf(guaranteePeriod);
			} catch (Exception e) {
				HttpResponseKit.alertMessage(response, "预警值非法", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
		} else {
			maxInventory = ParamKit.getParameter(request, WarehouseInventoryPrewarningConfig.MAX_INVENTORY, "");
			minInventory = ParamKit.getParameter(request, WarehouseInventoryPrewarningConfig.MIN_INVENTORY, "");
			if (!StringKit.isValid(maxInventory) || !StringKit.isValid(minInventory)) {
				HttpResponseKit.alertMessage(response, "最大和最小库存警值必须填写", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			try {
				if (Double.valueOf(maxInventory) < Double.valueOf(minInventory)) {
					HttpResponseKit.alertMessage(response, "最大库存预警值不能小于最小库存预警值", HttpResponseKit.ACTION_HISTORY_BACK);
					return;
				}
			} catch (Exception e) {
				HttpResponseKit.alertMessage(response, "预警值非法", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
		}

		try {
			// 组装预警内容
			String prewarningContent = business.getPrewarningContentBean(guaranteePeriod, maxInventory, minInventory, bean.getPrewarningType());
			bean.setPrewarningContent(prewarningContent);

			// 表单验证
			ReturnMessageBean message = business.validateBean(bean);
			if (StringKit.isValid(message.getMessage())) {
				HttpResponseKit.alertMessage(response, message.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			// 尝试查出数据库对应的bean
			WarehouseInventoryPrewarningBean exitBean = business.getWarehouseInventoryPrewarningByKey(bean.getWareType(), bean.getWareId(),
					bean.getPrewarningType());

			int result = 0;
			if (exitBean == null) {
				// 新建
				if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_INVENTORY_PREWARNING, OperationConfig.CREATE)) {
					return;
				}
				bean.setCreateUser(adminUserHelper.getAdminUserId());
				result = business.addWarehouseInventoryPrewarning(bean);
			} else {
				// 修改
				if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_INVENTORY_PREWARNING, OperationConfig.UPDATE)) {
					return;
				}
				bean.setModifyUser(adminUserHelper.getAdminUserId());
				result = business.updateWarehouseInventoryPrewarning(bean);
			}
			if (result < 0) {
				HttpResponseKit.alertMessage(response, "操作失败", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			} else {
				HttpResponseKit.alertMessage(response, "操作成功", "/erp/WarehouseInventoryPrewarningList.do");
				return;
			}
		} catch (Exception e) {
			HttpResponseKit.alertMessage(response, "操作失败", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		}

	}

}
