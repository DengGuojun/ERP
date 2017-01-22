package com.lpmas.erp.inventory.action;

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

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.erp.config.ErpConsoleConfig;
import com.lpmas.erp.inventory.bean.WarehouseInventoryPrewarningBean;
import com.lpmas.erp.inventory.bean.WarehouseInventoryPrewarningContentBean;
import com.lpmas.erp.inventory.bean.WarehouseInventoryReportBean;
import com.lpmas.erp.inventory.business.WarehouseInventoryPrewarningBusiness;
import com.lpmas.erp.inventory.business.WarehouseInventoryReportBusiness;
import com.lpmas.erp.inventory.config.InventoryConsoleConfig;
import com.lpmas.erp.inventory.config.InventoryResource;
import com.lpmas.erp.inventory.config.WarehouseInventoryPrewarningConfig;
import com.lpmas.erp.warehouse.bean.WarehouseInfoBean;
import com.lpmas.erp.warehouse.business.WarehouseInfoBusiness;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.framework.util.ListKit;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ParamKit;

/**
 * Servlet implementation class WarehouseInventoryList
 */
@WebServlet("/erp/WarehouseInventoryReportList.do")
public class WarehouseInventoryReportList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WarehouseInventoryReportList() {
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
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_INVENTORY, OperationConfig.SEARCH)) {
			return;
		}

		// 初始化页面分页参数
		int pageNum = ParamKit.getIntParameter(request, "pageNum", ErpConsoleConfig.DEFAULT_PAGE_NUM);
		int pageSize = ParamKit.getIntParameter(request, "pageSize", ErpConsoleConfig.DEFAULT_PAGE_SIZE);
		PageBean pageBean = new PageBean(pageNum, pageSize);

		HashMap<String, String> condMap = new HashMap<String, String>();
		HashMap<String, String> timeMap = new HashMap<String, String>();
		String batchNumber = ParamKit.getParameter(request, "batchNumber", "").trim();
		if (StringKit.isValid(batchNumber)) {
			condMap.put("batchNumber", batchNumber);
		}
		String productionDate = ParamKit.getParameter(request, "productionDate", "").trim();
		if (StringKit.isValid(productionDate)) {
			condMap.put("productionDate", productionDate);
		}
		int wareType = ParamKit.getIntParameter(request, "wareType", 0);
		if (wareType != 0) {
			condMap.put("wareType", String.valueOf(wareType));
		}
		String warehouseId = ParamKit.getParameter(request, "warehouseId", "").trim();
		if (StringKit.isValid(warehouseId)) {
			condMap.put("warehouseId", warehouseId);
		}
		String gt_dateTime = ParamKit.getParameter(request, "gt_dateTime", "").trim();
		if (StringKit.isValid(gt_dateTime)) {
			timeMap.put("gt_dateTime", gt_dateTime);
		}
		String lt_dateTime = ParamKit.getParameter(request, "lt_dateTime", "").trim();
		if (StringKit.isValid(lt_dateTime)) {
			timeMap.put("lt_dateTime", lt_dateTime);
		}
		HashMap<String, HashMap<String, String>> scopeMap = new HashMap<String, HashMap<String, String>>();
		if (!timeMap.isEmpty()) {
			scopeMap.put("productionDate", timeMap);
		}
		// 从Mongo中获取相应的数据
		WarehouseInventoryReportBusiness business = new WarehouseInventoryReportBusiness();
		try {
			PageResultBean<WarehouseInventoryReportBean> result = business.getWarehouseInventoryReportPageListByMap(condMap, scopeMap, pageBean);

			// 获取仓库名称
			Map<String, String> warehouseMap = new HashMap<String, String>();
			WarehouseInfoBusiness warehouseInfoBusiness = new WarehouseInfoBusiness();
			// 获取当前保质期危急值
			Map<String, Double> inventoryPrewarningMap = new HashMap<String, Double>();
			WarehouseInventoryPrewarningBusiness prewarningBusiness = new WarehouseInventoryPrewarningBusiness();
			for (WarehouseInventoryReportBean reportBean : result.getRecordList()) {

				// 获取仓库
				WarehouseInfoBean warehouseBean = warehouseInfoBusiness.getWarehouseInfoByKey(reportBean.getWarehouseId());
				warehouseMap.put(reportBean.get_id(), warehouseBean.getWarehouseName());

				// 有保质期管理的才去做下面的操作
				if (reportBean.getGuaranteePeriod() > 0f) {
					WarehouseInventoryPrewarningBean prewarningBean = prewarningBusiness.getWarehouseInventoryPrewarningByKey(
							reportBean.getWareType(), reportBean.getWareId(), WarehouseInventoryPrewarningConfig.PREWARNING_TYPE_EXPIRATION);
					// 尝试转化预警内容
					try {
						List<WarehouseInventoryPrewarningContentBean> contentList = JsonKit.toList(prewarningBean.getPrewarningContent(),
								WarehouseInventoryPrewarningContentBean.class);
						String value = MapKit.getValueFromMap(WarehouseInventoryPrewarningConfig.GUARANTEE_PERIOD,
								ListKit.list2Map(contentList, "key", "value"));
						inventoryPrewarningMap.put(reportBean.get_id(), Double.valueOf(value));
					} catch (Exception e) {
						inventoryPrewarningMap.put(reportBean.get_id(), Double.valueOf(0));
					}
				}
			}

			request.setAttribute("WarehouseMap", warehouseMap);
			request.setAttribute("InventoryPrewarningMap", inventoryPrewarningMap);
			request.setAttribute("WarehouseInventoryReportList", result.getRecordList());
			// 初始化分页数据
			pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
			request.setAttribute("PageResult", pageBean);
			request.setAttribute("CondList", MapKit.map2List(condMap));
			request.setAttribute("AdminUserHelper", adminUserHelper);
		} catch (Exception e) {
			e.printStackTrace();
		}

		RequestDispatcher rd = request.getRequestDispatcher(InventoryConsoleConfig.INVENTORY_PAGE_PATH + "WarehouseInventoryReportList.jsp");
		rd.forward(request, response);

	}

}
