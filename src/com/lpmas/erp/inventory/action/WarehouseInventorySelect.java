package com.lpmas.erp.inventory.action;

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
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.config.ErpConsoleConfig;
import com.lpmas.erp.inventory.bean.WarehouseInventoryBean;
import com.lpmas.erp.inventory.business.WarehouseInventoryBusiness;
import com.lpmas.erp.inventory.config.InventoryConsoleConfig;
import com.lpmas.erp.inventory.config.InventoryResource;
import com.lpmas.erp.inventory.config.WarehouseInventoryConfig;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.pdm.bean.MaterialInfoBean;
import com.lpmas.pdm.bean.MaterialTypeBean;
import com.lpmas.pdm.bean.ProductInfoBean;
import com.lpmas.pdm.bean.ProductItemBean;
import com.lpmas.pdm.bean.ProductTypeBean;
import com.lpmas.pdm.client.PdmServiceClient;

/**
 * Servlet implementation class WarehouseInventorySelect
 */
@WebServlet("/erp/WarehouseInventorySelect.do")
public class WarehouseInventorySelect extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(WarehouseInventorySelect.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WarehouseInventorySelect() {
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
		int warehouseId = ParamKit.getIntParameter(request, "warehouseId", 0);
		if (warehouseId != 0) {
			condMap.put("warehouseId", String.valueOf(warehouseId));
		} else {
			HttpResponseKit.alertMessage(response, "仓库ID缺失", HttpResponseKit.ACTION_WINDOW_CLOSE);
			return;
		}
		int wareType = ParamKit.getIntParameter(request, "wareType", 0);
		if (wareType != 0) {
			condMap.put("wareType", String.valueOf(wareType));
		} else {
			HttpResponseKit.alertMessage(response, "制品类型缺失", HttpResponseKit.ACTION_WINDOW_CLOSE);
			return;
		}
		String batchNumber = ParamKit.getParameter(request, "batchNumber", "");
		if (StringKit.isValid(batchNumber)) {
			condMap.put("batchNumber", batchNumber);
		}
		String mode = ParamKit.getParameter(request, "mode", "");
		if (StringKit.isValid(mode)) {
			request.setAttribute("mode", mode);
			condMap.put("inventoryType", String.valueOf(WarehouseInventoryConfig.WIT_NORMAL));
		} else {
			int inventoryType = ParamKit.getIntParameter(request, "inventoryType", 0);
			if (inventoryType > 0) {
				condMap.put("inventoryType", String.valueOf(inventoryType));
			}
		}
		// 固定传入库存数量大于0的条件
		condMap.put("gt_quantity", "0");

		// 查数据库
		WarehouseInventoryBusiness business = new WarehouseInventoryBusiness();
		PageResultBean<WarehouseInventoryBean> result = null;
		try {
			result = business.getWarehouseInventoryPageListByMap(condMap, pageBean);
		} catch (Exception e) {
			log.error(e.getMessage());
			HttpResponseKit.alertMessage(response, "查询错误", HttpResponseKit.ACTION_WINDOW_CLOSE);
			return;
		}

		// 单位信息
		Map<String, String> unitMap = new HashMap<String, String>();

		// 获取根据制品类型获取类型列表
		PdmServiceClient client = new PdmServiceClient();
		Map<Integer, MaterialTypeBean> materialTypeMap = null;
		Map<Integer, MaterialInfoBean> materialInfoMap = null;
		Map<Integer, ProductTypeBean> productTypeMap = null;
		Map<Integer, ProductItemBean> productItemMap = null;

		if (wareType == InfoTypeConfig.INFO_TYPE_MATERIAL) {
			materialTypeMap = new HashMap<Integer, MaterialTypeBean>();
			materialInfoMap = new HashMap<Integer, MaterialInfoBean>();
			Map<Integer, MaterialTypeBean> tempTypeMap = new HashMap<Integer, MaterialTypeBean>();
			int wareId = 0;
			for (WarehouseInventoryBean inventoryBean : result.getRecordList()) {
				wareId = inventoryBean.getWareId();
				MaterialInfoBean materialInfo = null;
				// 判断是否存在，减少CLIENT调用次数
				if (materialInfoMap.get(wareId) == null) {
					materialInfo = client.getMaterialInfoByKey(wareId);
					materialInfoMap.put(wareId, materialInfo);
				} else {
					materialInfo = materialInfoMap.get(wareId);
				}
				if (tempTypeMap.get(materialInfo.getTypeId1()) == null) {
					tempTypeMap.put(materialInfo.getTypeId1(), client.getMaterialTypeByKey(materialInfo.getTypeId1()));
				}
				materialTypeMap.put(wareId, tempTypeMap.get(materialInfo.getTypeId1()));
				unitMap.put(inventoryBean.getUnit(), client.getUnitInfoByCode(inventoryBean.getUnit()).getUnitName());
			}

		} else if (wareType == InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM) {
			productTypeMap = new HashMap<Integer, ProductTypeBean>();
			productItemMap = new HashMap<Integer, ProductItemBean>();
			Map<Integer, ProductTypeBean> tempTypeMap = new HashMap<Integer, ProductTypeBean>();
			int wareId = 0;
			for (WarehouseInventoryBean inventoryBean : result.getRecordList()) {
				wareId = inventoryBean.getWareId();
				ProductItemBean productItem = null;
				ProductInfoBean productInfo = null;
				// 判断是否存在，减少CLIENT调用次数
				if (productItemMap.get(wareId) == null) {
					productItem = client.getProductItemByKey(wareId);
					productItemMap.put(wareId, productItem);
				} else {
					productItem = productItemMap.get(wareId);
				}
				productInfo = client.getProductInfoByKey(productItem.getProductId());

				if (tempTypeMap.get(productInfo.getTypeId2()) == null) {
					tempTypeMap.put(productInfo.getTypeId2(), client.getProductTypeByKey(productInfo.getTypeId2()));
				}
				productTypeMap.put(wareId, tempTypeMap.get(productInfo.getTypeId2()));
				unitMap.put(inventoryBean.getUnit(), client.getUnitInfoByCode(inventoryBean.getUnit()).getUnitName());
			}
		}

		// 数据放到页面
		request.setAttribute("UnitMap", unitMap);
		request.setAttribute("WarehouseInventoryList", result.getRecordList());
		request.setAttribute("MaterialTypeMap", materialTypeMap);
		request.setAttribute("MaterialInfoMap", materialInfoMap);
		request.setAttribute("ProductTypeMap", productTypeMap);
		request.setAttribute("ProductItemMap", productItemMap);

		// 初始化分页数据
		pageBean.init(pageNum, pageSize, result.getTotalRecordNumber());
		request.setAttribute("PageResult", pageBean);
		request.setAttribute("CondList", MapKit.map2List(condMap));
		request.setAttribute("AdminUserHelper", adminUserHelper);
		RequestDispatcher rd = request.getRequestDispatcher(InventoryConsoleConfig.INVENTORY_PAGE_PATH + "WarehouseInventorySelect.jsp");
		rd.forward(request, response);

	}

}
