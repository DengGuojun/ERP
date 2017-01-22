package com.lpmas.erp.inventory.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import com.lpmas.erp.inventory.bean.WarehouseInventoryBean;
import com.lpmas.erp.inventory.bean.WarehouseTransferOrderInfoBean;
import com.lpmas.erp.inventory.bean.WarehouseTransferOrderItemBean;
import com.lpmas.erp.inventory.bean.WarehouseTransferOrderItemEntityBean;
import com.lpmas.erp.inventory.business.WarehouseInventoryBusiness;
import com.lpmas.erp.inventory.business.WarehouseTransferOrderHelper;
import com.lpmas.erp.inventory.business.WarehouseTransferOrderInfoBusiness;
import com.lpmas.erp.inventory.business.WarehouseTransferOrderItemBusiness;
import com.lpmas.erp.inventory.business.WarehouseTransferOrderNumberGenerator;
import com.lpmas.erp.inventory.config.InventoryConsoleConfig;
import com.lpmas.erp.inventory.config.InventoryResource;
import com.lpmas.erp.inventory.config.WarehouseInventoryConfig;
import com.lpmas.erp.inventory.config.WarehouseTransferOrderConfig;
import com.lpmas.erp.warehouse.bean.WarehouseInfoBean;
import com.lpmas.erp.warehouse.business.WarehouseInfoBusiness;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.framework.web.ReturnMessageBean;

@WebServlet("/erp/WarehouseTransferOrderInfoManage.do")
public class WarehouseTransferOrderInfoManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(WarehouseTransferOrderInfoManage.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WarehouseTransferOrderInfoManage() {
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
		int toId = ParamKit.getIntParameter(request, "toId", 0);
		WarehouseTransferOrderInfoBusiness business = new WarehouseTransferOrderInfoBusiness();
		WarehouseTransferOrderInfoBean bean = new WarehouseTransferOrderInfoBean();
		WarehouseInfoBean sourceWarehouseInfoBean = new WarehouseInfoBean();
		WarehouseInfoBean targetWarehouseInfoBean = new WarehouseInfoBean();
		if (toId > 0) {
			// 修改调拨单
			if (!readOnly && !adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_TRANSFER_ORDER_INFO, OperationConfig.UPDATE)) {
				return;
			}
			if (readOnly && !adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_TRANSFER_ORDER_INFO, OperationConfig.SEARCH)) {
				return;
			}
			bean = business.getWarehouseTransferOrderInfoByKey(toId);
			if (bean == null) {
				HttpResponseKit.alertMessage(response, "调拨单号缺失", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			if (bean.getStatus() == Constants.STATUS_NOT_VALID) {
				HttpResponseKit.alertMessage(response, "调拨单已被删除", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			if (WarehouseTransferOrderHelper.isLock(bean) && !readOnly) {
				HttpResponseKit.alertMessage(response, "调拨单被锁定，不能编辑", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			// 获取关联对象信息
			WarehouseInfoBusiness warehouseInfoBusiness = new WarehouseInfoBusiness();
			sourceWarehouseInfoBean = warehouseInfoBusiness.getWarehouseInfoByKey(bean.getSourceWarehouseId());
			targetWarehouseInfoBean = warehouseInfoBusiness.getWarehouseInfoByKey(bean.getTargetWarehouseId());
		} else {
			// 新增调拨单
			if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_TRANSFER_ORDER_INFO, OperationConfig.CREATE)) {
				return;
			}
			int wareType = ParamKit.getIntParameter(request, "wareType", 0);
			if (wareType != InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM && wareType != InfoTypeConfig.INFO_TYPE_MATERIAL) {
				HttpResponseKit.alertMessage(response, "制品类型不合法", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			bean.setWareType(wareType);
			bean.setApproveStatus(WarehouseTransferOrderConfig.APPROVE_STATUS_UNCOMMIT);
			bean.setToStatus(WarehouseTransferOrderConfig.TO_STATUS_EDIT);
			bean.setStatus(Constants.STATUS_VALID);
			bean.setCreateUser(adminUserHelper.getAdminUserId());
		}
		// 查明细
		WarehouseTransferOrderItemBusiness warehouseTransferOrderItemBusiness = new WarehouseTransferOrderItemBusiness();
		List<WarehouseTransferOrderItemEntityBean> warehouseTransferOrderItemEntityList = new ArrayList<WarehouseTransferOrderItemEntityBean>();

		// 从出库明细中获取数据
		WarehouseInventoryBusiness inventoryBusiness = new WarehouseInventoryBusiness();
		int warehouseId = sourceWarehouseInfoBean.getWarehouseId();
		List<WarehouseTransferOrderItemBean> warehouseTransferItemList = warehouseTransferOrderItemBusiness.getTransferOrderItemValidListByToId(toId);
		for (WarehouseTransferOrderItemBean warehouseTransferOrderItemBean : warehouseTransferItemList) {
			// 获取制品信息
			WarehouseInventoryBean inventoryBean = inventoryBusiness.getWarehouseInventoryBeanByCondition(warehouseId,
					warehouseTransferOrderItemBean.getWareType(), warehouseTransferOrderItemBean.getWareId(), WarehouseInventoryConfig.WIT_NORMAL,
					warehouseTransferOrderItemBean.getBatchNumber(), warehouseTransferOrderItemBean.getProductionDate());
			WarehouseTransferOrderItemEntityBean warehouseTransferOrderItemEntityBean = new WarehouseTransferOrderItemEntityBean(
					warehouseTransferOrderItemBean, inventoryBean);
			warehouseTransferOrderItemEntityList.add(warehouseTransferOrderItemEntityBean);
		}
		request.setAttribute("WarehouseTransferOrderItemEntityList", warehouseTransferOrderItemEntityList);

		request.setAttribute("SourceWarehouseInfoBean", sourceWarehouseInfoBean);
		request.setAttribute("TargetWarehouseInfoBean", targetWarehouseInfoBean);
		request.setAttribute("WarehouseTransferOrderInfoBean", bean);
		// 请求转发
		RequestDispatcher rd = request.getRequestDispatcher(InventoryConsoleConfig.INVENTORY_PAGE_PATH + "WarehouseTransferOrderInfoManage.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		WarehouseTransferOrderInfoBean bean = new WarehouseTransferOrderInfoBean();
		WarehouseTransferOrderNumberGenerator.lock.lock();// 获取lock，在调拨单生成完成后或发生异常后释放
		try {
			bean = BeanKit.request2Bean(request, WarehouseTransferOrderInfoBean.class);
			if (bean.getToId() == 0) {
				String toNumber = WarehouseTransferOrderNumberGenerator.generateDeliveryVoucherNumber();
				bean.setToNumber(toNumber);
			}
			WarehouseTransferOrderInfoBusiness business = new WarehouseTransferOrderInfoBusiness();

			ReturnMessageBean messageBean = business.verifyWarehouseTransferInfo(bean);
			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			int wareType = ParamKit.getIntParameter(request, "wareType", 0);
			if (wareType != InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM && wareType != InfoTypeConfig.INFO_TYPE_MATERIAL) {
				HttpResponseKit.alertMessage(response, "制品类型不合法", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			// 处理ITEM
			String[] selectedItemValue = new String[] {};
			try {
				selectedItemValue = ParamKit.getArrayParameter(request, "selectedItemValue");
				if (!business.isVaildArray(selectedItemValue) || selectedItemValue == null) {
					HttpResponseKit.alertMessage(response, "调拨明细必须填写", HttpResponseKit.ACTION_HISTORY_BACK);
					return;
				}
			} catch (Exception e) {
				HttpResponseKit.alertMessage(response, "调拨明细必须填写", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			WarehouseTransferOrderItemBusiness warehouseTransferOrderItemBusiness = new WarehouseTransferOrderItemBusiness();
			List<WarehouseTransferOrderItemBean> newTransferOrderItemList = new ArrayList<WarehouseTransferOrderItemBean>();
			if (selectedItemValue != null) {
				for (String itemValue : selectedItemValue) {
					if (StringKit.isValid(itemValue.trim())) {
						Map<String, String> itemValueMap = MapKit.string2Map(itemValue.trim(), ",", "=");
						ReturnMessageBean message = warehouseTransferOrderItemBusiness.validateTransferOrderDetail(itemValueMap, wareType);
						if (StringKit.isValid(message.getMessage())) {
							HttpResponseKit.alertMessage(response, message.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
							return;
						} // 生成item的list
						warehouseTransferOrderItemBusiness.processTransferItem(itemValueMap, wareType, newTransferOrderItemList);
					}
				}
			}
			// 验证出库数量
			if (!warehouseTransferOrderItemBusiness.validateQuantity(newTransferOrderItemList, bean.getSourceWarehouseId())) {
				HttpResponseKit.alertMessage(response, "调拨数量少于实际库存", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			// 检查是否有明细，新建必须有明细,newPurchaseOrderItemMap不是空
			if (newTransferOrderItemList.isEmpty()) {
				HttpResponseKit.alertMessage(response, "调拨单明细错误!", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			int result = 0;
			int userId = adminUserHelper.getAdminUserId();
			if (bean.getToId() > 0) {
				if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_TRANSFER_ORDER_INFO, OperationConfig.UPDATE)) {
					return;
				}
				bean.setModifyUser(adminUserHelper.getAdminUserId());
				result = business.updateWarehouseTransferOrderInfo(bean);

			} else {
				if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_TRANSFER_ORDER_INFO, OperationConfig.CREATE)) {
					return;
				}
				bean.setCreateUser(adminUserHelper.getAdminUserId());
				result = business.addWarehouseTransferOrderInfo(bean);
				bean.setToId(result);
			}
			// 处理明细
			if (result > 0) {
				result = warehouseTransferOrderItemBusiness.processTransferItemOrder(newTransferOrderItemList, bean.getToId(), userId);
			}

			if (result > 0) {
				HttpResponseKit.alertMessage(response, "处理成功", "/erp/WarehouseTransferOrderInfoList.do?wareType=" + bean.getWareType());
				return;
			} else {
				HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
		} catch (Exception e) {
			log.error("", e);
			HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		} finally {
			WarehouseTransferOrderNumberGenerator.lock.unlock();
		}
	}

}
