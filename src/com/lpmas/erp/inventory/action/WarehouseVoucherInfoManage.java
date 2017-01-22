package com.lpmas.erp.inventory.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.lpmas.erp.inventory.bean.WarehouseVoucherInfoBean;
import com.lpmas.erp.inventory.bean.WarehouseVoucherInfoEntityBean;
import com.lpmas.erp.inventory.bean.WarehouseVoucherItemBean;
import com.lpmas.erp.inventory.business.WarehouesVoucherNumberGenerator;
import com.lpmas.erp.inventory.business.WarehouseVoucherInfoBusiness;
import com.lpmas.erp.inventory.business.WarehouseVoucherItemBusiness;
import com.lpmas.erp.inventory.business.WarehouseVoucherStatusHelper;
import com.lpmas.erp.inventory.config.InventoryConsoleConfig;
import com.lpmas.erp.inventory.config.InventoryResource;
import com.lpmas.erp.inventory.config.SourceOrderTypeConfig;
import com.lpmas.erp.inventory.config.WarehouseVoucherConfig;
import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.business.PurchaseOrderInfoBusiness;
import com.lpmas.erp.warehouse.bean.WarehouseInfoBean;
import com.lpmas.erp.warehouse.business.WarehouseInfoBusiness;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.DateKit;
import com.lpmas.framework.util.ListKit;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.srm.bean.SupplierInfoBean;
import com.lpmas.srm.client.SrmServiceClient;

/**
 * Servlet implementation class WarehouseVoucherInfoManage
 */
@WebServlet("/erp/WarehouseVoucherInfoManage.do")
public class WarehouseVoucherInfoManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(WarehouseVoucherInfoManage.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WarehouseVoucherInfoManage() {
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
		int wvId = ParamKit.getIntParameter(request, "wvId", 0);
		WarehouseVoucherInfoBusiness business = new WarehouseVoucherInfoBusiness();
		WarehouseVoucherInfoBean bean = new WarehouseVoucherInfoBean();
		PurchaseOrderInfoBean purchaseOrderInfoBean = new PurchaseOrderInfoBean();
		SupplierInfoBean supplierInfoBean = new SupplierInfoBean();
		WarehouseInfoBean warehouseInfoBean = new WarehouseInfoBean();
		boolean hasSourceSupplier = false;// 判断源单是否有供应商
		if (wvId > 0) {
			// 修改入库单
			if (!readOnly && !adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_VOUCHER_INFO, OperationConfig.UPDATE)) {
				return;
			}
			if (readOnly && !adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_VOUCHER_INFO, OperationConfig.SEARCH)) {
				return;
			}
			bean = business.getWarehouseVoucherInfoByKey(wvId);
			if (bean == null) {
				HttpResponseKit.alertMessage(response, "入库单号缺失", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			if (bean.getStatus() == Constants.STATUS_NOT_VALID) {
				HttpResponseKit.alertMessage(response, "入库单已被删除", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			if (WarehouseVoucherStatusHelper.isLock(bean) && !readOnly) {
				HttpResponseKit.alertMessage(response, "入库单被锁定，不能编辑", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			// 获取关联对象信息
			if (bean.getSourceOrderType() == SourceOrderTypeConfig.SOURCE_ORDER_TYPE_PURCHASE_ORDER) {
				PurchaseOrderInfoBusiness purchaseOrderInfoBusiness = new PurchaseOrderInfoBusiness();
				purchaseOrderInfoBean = purchaseOrderInfoBusiness.getPurchaseOrderInfoByKey(bean.getSourceOrderId());
				hasSourceSupplier = true;
			}
			SrmServiceClient srmServiceClient = new SrmServiceClient();
			supplierInfoBean = srmServiceClient.getSupplierInfoByKey(bean.getSupplierId());
			WarehouseInfoBusiness warehouseInfoBusiness = new WarehouseInfoBusiness();
			warehouseInfoBean = warehouseInfoBusiness.getWarehouseInfoByKey(bean.getWarehouseId());
		} else {
			// 新增入库单
			if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_VOUCHER_INFO, OperationConfig.CREATE)) {
				return;
			}
			int wareType = ParamKit.getIntParameter(request, "wareType", 0);
			if (wareType != InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM && wareType != InfoTypeConfig.INFO_TYPE_MATERIAL) {
				HttpResponseKit.alertMessage(response, "制品类型不合法", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			int wvType = ParamKit.getIntParameter(request, "wvType", 0);
			if (!WarehouseVoucherConfig.WAREHOUSE_VOUCHER_TYPE_MAP.containsKey(wvType) || wvType == WarehouseVoucherConfig.WVT_TRANSFER) {
				HttpResponseKit.alertMessage(response, "入库类型不合法", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			bean.setWareType(wareType);
			bean.setWvType(wvType);
			bean.setStockInTime(DateKit.getCurrentTimestamp());
			bean.setApproveStatus(WarehouseVoucherConfig.REVIEW_STATUS_UNCOMMIT);
			bean.setWvStatus(WarehouseVoucherConfig.WV_STATUS_EDIT);
			bean.setSyncStatus(WarehouseVoucherConfig.SYNC_STATUS_NOT_SYNCHORIZED);
			bean.setStatus(Constants.STATUS_VALID);
			bean.setCreateUser(adminUserHelper.getAdminUserId());
		}
		WarehouseVoucherInfoEntityBean warehouseVoucherInfoEntityBean = new WarehouseVoucherInfoEntityBean(bean, purchaseOrderInfoBean,
				supplierInfoBean, warehouseInfoBean);
		request.setAttribute("WarehouseVoucherInfoEntityBean", warehouseVoucherInfoEntityBean);
		request.setAttribute("HasSourceSupplier", hasSourceSupplier);
		// 请求转发
		RequestDispatcher rd = request.getRequestDispatcher(InventoryConsoleConfig.INVENTORY_PAGE_PATH + "WarehouseVoucherInfoManage.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		WarehouseVoucherInfoBean bean = new WarehouseVoucherInfoBean();
		WarehouesVoucherNumberGenerator.lock.lock();// 获取lock，在采购订单生成完成后或发生异常后释放
		try {
			bean = BeanKit.request2Bean(request, WarehouseVoucherInfoBean.class);
			if (bean.getWvId() == 0) {
				String wvNumber = WarehouesVoucherNumberGenerator.generateWarehouseVoucherNumber(bean);
				bean.setWvNumber(wvNumber);
			}
			WarehouseVoucherInfoBusiness business = new WarehouseVoucherInfoBusiness();

			ReturnMessageBean messageBean = business.verifyWarehouseVoucherInfo(bean);
			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			int wareType = ParamKit.getIntParameter(request, "wareType", 0);
			if (wareType != InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM && wareType != InfoTypeConfig.INFO_TYPE_MATERIAL) {
				HttpResponseKit.alertMessage(response, "制品类型不合法", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			int wvType = ParamKit.getIntParameter(request, "wvType", 0);
			if (!WarehouseVoucherConfig.WAREHOUSE_VOUCHER_TYPE_MAP.containsKey(wvType) || wvType == WarehouseVoucherConfig.WVT_TRANSFER) {
				HttpResponseKit.alertMessage(response, "入库类型不合法", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			// 处理ITEM
			int[] selectedItems = ParamKit.getIntParameters(request, "selectedItem", 0);
			WarehouseVoucherItemBusiness warehouseVoucherItemBusiness = new WarehouseVoucherItemBusiness();
			WarehouseVoucherItemBean itemBean = null;
			// 处理通过弹窗添加的
			Map<Integer, WarehouseVoucherItemBean> newPurchaseOrderItemMap = new HashMap<Integer, WarehouseVoucherItemBean>();
			if (selectedItems != null) {
				for (int wareId : selectedItems) {
					if (wareId != 0) {
						itemBean = new WarehouseVoucherItemBean();
						String itemValue = ParamKit.getParameter(request, "selectedItemValue_" + wareId, "");
						Boolean hasExpiration = warehouseVoucherItemBusiness.checkHasExpiration(wareType, wareId);
						if (StringKit.isValid(itemValue.trim())) {
							Map<String, String> itemValueMap = MapKit.string2Map(itemValue.trim(), ",", "=");
							ReturnMessageBean message = warehouseVoucherItemBusiness.validateWvDetail(itemValueMap, hasExpiration);
							if (StringKit.isValid(message.getMessage())) {
								HttpResponseKit.alertMessage(response, message.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
								return;
							}
							// 避免类型转换错误
							try {
								Timestamp productionDate = DateKit.str2Timestamp(itemValueMap.get("productionDate"), DateKit.DEFAULT_DATE_FORMAT);
								itemBean.setWareType(wareType);
								itemBean.setWareId(wareId);
								itemBean.setUnit(itemValueMap.get("unit"));
								itemBean.setReceivableQuantity(Double.valueOf(itemValueMap.get("receivableQuantity")));
								itemBean.setStockInQuantity(Double.valueOf(itemValueMap.get("stockInQuantity")));
								itemBean.setDefectQuantity(Double.valueOf(itemValueMap.get("defectQuantity")));
								itemBean.setBatchNumber(itemValueMap.get("batchNumber"));
								itemBean.setProductionDate(productionDate);
								if (hasExpiration) {
									// 是保质期管理才做转换
									itemBean.setGuaranteePeriod(Double.valueOf(itemValueMap.get("guaranteePeriod")));
									Timestamp expirationDate = DateKit.str2Timestamp(itemValueMap.get("expirationDate"), DateKit.DEFAULT_DATE_FORMAT);
									itemBean.setExpirationDate(expirationDate);
								} else {
									itemBean.setGuaranteePeriod(-1f);
								}
								itemBean.setStatus(Constants.STATUS_VALID);
								itemBean.setMemo(itemValueMap.get("memo"));
								newPurchaseOrderItemMap.put(wareId, itemBean);
							} catch (Exception e) {
								continue;
							}
						}
					}
				}
			}

			// 获取已有的ITEM（不是通过新增产生的，有原单的或者已存在于数据库的）
			List<WarehouseVoucherItemBean> existItemList = new ArrayList<WarehouseVoucherItemBean>();
			// 获取所有的制品ID
			int[] itemIds = ParamKit.getIntParameters(request, "itemId", 0);
			if (itemIds != null) {
				for (int wareId : itemIds) {
					// 数据准备
					Boolean hasExpiration = warehouseVoucherItemBusiness.checkHasExpiration(wareType, wareId);
					Timestamp productionDate = null;
					String productionDateStr = ParamKit.getParameter(request, "productionDate_" + wareId, "");
					if (!StringKit.isValid(productionDateStr)) {
						HttpResponseKit.alertMessage(response, "生产日期缺失!", HttpResponseKit.ACTION_HISTORY_BACK);
						return;
					} else {
						try {
							productionDate = DateKit.str2Timestamp(productionDateStr, DateKit.DEFAULT_DATE_FORMAT);
						} catch (Exception e) {
							HttpResponseKit.alertMessage(response, "生产日期格式错误!", HttpResponseKit.ACTION_HISTORY_BACK);
							return;
						}
					}
					// 使用-1约定为无保质期管理
					Double guaranteePeriod = ParamKit.getDoubleParameter(request, "guaranteePeriod_" + wareId, -1f);
					if (hasExpiration && guaranteePeriod == 0f) {
						// 是保质期管理要填保质期
						HttpResponseKit.alertMessage(response, "保质期缺失!", HttpResponseKit.ACTION_HISTORY_BACK);
						return;
					}
					Timestamp expirationDate = null;
					String expirationDateStr = ParamKit.getParameter(request, "expirationDate_" + wareId, "");
					if (hasExpiration && !StringKit.isValid(expirationDateStr)) {
						// 是保质期管理要填保质期至
						HttpResponseKit.alertMessage(response, "保质期至缺失!", HttpResponseKit.ACTION_HISTORY_BACK);
						return;
					} else {
						// 是保质期管理，并且有填保质期至
						// 尝试转换
						try {
							expirationDate = DateKit.str2Timestamp(expirationDateStr, DateKit.DEFAULT_DATE_FORMAT);
						} catch (Exception e) {
							if (hasExpiration) {
								HttpResponseKit.alertMessage(response, "保质期至格式错误!", HttpResponseKit.ACTION_HISTORY_BACK);
								return;
							}
						}
					}
					itemBean = new WarehouseVoucherItemBean();
					itemBean.setWareType(wareType);
					itemBean.setWareId(wareId);
					itemBean.setUnit(ParamKit.getParameter(request, "unit_" + wareId, ""));
					itemBean.setReceivableQuantity(ParamKit.getDoubleParameter(request, "receivableQuantity_" + wareId, new Double(0)));
					itemBean.setStockInQuantity(ParamKit.getDoubleParameter(request, "stockInQuantity_" + wareId, new Double(0)));
					itemBean.setDefectQuantity(ParamKit.getDoubleParameter(request, "defectQuantity_" + wareId, new Double(0)));
					itemBean.setProductionDate(productionDate);
					itemBean.setGuaranteePeriod(guaranteePeriod);
					itemBean.setExpirationDate(expirationDate);
					itemBean.setBatchNumber(ParamKit.getParameter(request, "batchNumber_" + wareId, ""));
					itemBean.setStatus(Constants.STATUS_VALID);
					itemBean.setMemo(ParamKit.getParameter(request, "memo_" + wareId, ""));
					existItemList.add(itemBean);
				}
			}

			int result = 0;
			if (bean.getWvId() > 0) {
				if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_VOUCHER_INFO, OperationConfig.UPDATE)) {
					return;
				}
				bean.setModifyUser(adminUserHelper.getAdminUserId());
				result = business.updateWarehouseVoucherInfo(bean);
				// 处理明细
				// 找出当前存在于数据库的ITEM列表
				List<WarehouseVoucherItemBean> dbExistItemList = warehouseVoucherItemBusiness.getWarehouseVoucherItemAllListByWvId(bean.getWvId());
				Map<Integer, WarehouseVoucherItemBean> dbExistItemMap = ListKit.list2Map(dbExistItemList, "wareId");
				// 应该要检查这个ITEM是否存在于数据库中，存在的就UPDATE 不存在的就是ADD
				int adminUser = adminUserHelper.getAdminUserId();
				if (result > 0) {
					if (!existItemList.isEmpty()) {
						for (WarehouseVoucherItemBean item : existItemList) {
							item.setWvId(bean.getWvId());
							warehouseVoucherItemBusiness.checkBeanForUpdateOrAdd(item, dbExistItemMap, adminUser);
							dbExistItemMap.remove(item.getWareId());
						}
					}
					if (!newPurchaseOrderItemMap.isEmpty()) {
						for (Entry<Integer, WarehouseVoucherItemBean> entry : newPurchaseOrderItemMap.entrySet()) {
							WarehouseVoucherItemBean tempBean = entry.getValue();
							tempBean.setWvId(bean.getWvId());
							warehouseVoucherItemBusiness.checkBeanForUpdateOrAdd(tempBean, dbExistItemMap, adminUser);
							dbExistItemMap.remove(tempBean.getWareId());
						}
					}

					// 剩下的要删除
					if (!dbExistItemMap.isEmpty()) {
						for (Entry<Integer, WarehouseVoucherItemBean> entry : dbExistItemMap.entrySet()) {
							WarehouseVoucherItemBean tempBean = entry.getValue();
							tempBean.setModifyUser(adminUserHelper.getAdminUserId());
							tempBean.setStatus(Constants.STATUS_NOT_VALID);
							warehouseVoucherItemBusiness.updateWarehouseVoucherItem(tempBean);
						}
					}
				}
				// 明细处理结束

			} else {
				if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_VOUCHER_INFO, OperationConfig.CREATE)) {
					return;
				}
				// 检查是否有明细，新建必须有明细,newPurchaseOrderItemMap和existItemList有且只有一个不是空
				if ((existItemList.isEmpty() && newPurchaseOrderItemMap.isEmpty())
						|| (!existItemList.isEmpty() && !newPurchaseOrderItemMap.isEmpty())) {
					HttpResponseKit.alertMessage(response, "入库单明细错误!", HttpResponseKit.ACTION_HISTORY_BACK);
					return;
				}
				bean.setCreateUser(adminUserHelper.getAdminUserId());
				result = business.addWarehouseVoucherInfo(bean);

				// 处理明细
				if (result > 0) {
					if (!existItemList.isEmpty()) {
						for (WarehouseVoucherItemBean item : existItemList) {
							item.setCreateUser(adminUserHelper.getAdminUserId());
							item.setWvId(result);
							warehouseVoucherItemBusiness.addWarehouseVoucherItem(item);
						}
					}
					if (!newPurchaseOrderItemMap.isEmpty()) {
						for (Entry<Integer, WarehouseVoucherItemBean> entry : newPurchaseOrderItemMap.entrySet()) {
							WarehouseVoucherItemBean tempBean = entry.getValue();
							tempBean.setCreateUser(adminUserHelper.getAdminUserId());
							tempBean.setWvId(result);
							warehouseVoucherItemBusiness.addWarehouseVoucherItem(tempBean);
						}
					}
				}
				// 明细处理结束
			}

			if (result > 0) {
				HttpResponseKit.alertMessage(response, "处理成功",
						"/erp/WarehouseVoucherInfoList.do?wareType=" + bean.getWareType() + "&wvType=" + bean.getWvType());
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
			WarehouesVoucherNumberGenerator.lock.unlock();
		}
	}

}
