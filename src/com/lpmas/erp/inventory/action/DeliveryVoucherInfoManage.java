package com.lpmas.erp.inventory.action;

import java.io.IOException;
import java.sql.Timestamp;
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
import com.lpmas.erp.inventory.bean.DeliveryVoucherInfoBean;
import com.lpmas.erp.inventory.bean.DeliveryVoucherItemBean;
import com.lpmas.erp.inventory.business.DeliveryVoucherInfoBusiness;
import com.lpmas.erp.inventory.business.DeliveryVoucherItemBusiness;
import com.lpmas.erp.inventory.business.DeliveryVoucherNumberGenerator;
import com.lpmas.erp.inventory.business.DeliveryVoucherStatusHelper;
import com.lpmas.erp.inventory.config.DeliveryVoucherConfig;
import com.lpmas.erp.inventory.config.InventoryConsoleConfig;
import com.lpmas.erp.inventory.config.InventoryResource;
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

@WebServlet("/erp/DeliveryVoucherInfoManage.do")
public class DeliveryVoucherInfoManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(DeliveryVoucherInfoManage.class);
	private static final long serialVersionUID = 1L;

	public DeliveryVoucherInfoManage() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		boolean readOnly = ParamKit.getBooleanParameter(request, "readOnly", false);
		int dvId = ParamKit.getIntParameter(request, "dvId", 0);
		DeliveryVoucherInfoBusiness business = new DeliveryVoucherInfoBusiness();
		DeliveryVoucherInfoBean bean = new DeliveryVoucherInfoBean();
		WarehouseInfoBusiness warehouseInfoBusiness = new WarehouseInfoBusiness();
		WarehouseInfoBean warehouseInfoBean = new WarehouseInfoBean();
		if (dvId > 0) {
			// 修改出库单
			if (!readOnly && !adminUserHelper.checkPermission(InventoryResource.DELIVERY_VOUCHER_INFO,
					OperationConfig.UPDATE)) {
				return;
			}
			if (readOnly && !adminUserHelper.checkPermission(InventoryResource.DELIVERY_VOUCHER_INFO,
					OperationConfig.SEARCH)) {
				return;
			}
			bean = business.getDeliveryVoucherInfoByKey(dvId);
			if (bean == null) {
				HttpResponseKit.alertMessage(response, "出库单号缺失", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			if (bean.getStatus() == Constants.STATUS_NOT_VALID) {
				HttpResponseKit.alertMessage(response, "出库单已被删除", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			if (DeliveryVoucherStatusHelper.isLock(bean) && !readOnly) {
				HttpResponseKit.alertMessage(response, "出库单被锁定，不能编辑", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			warehouseInfoBean = warehouseInfoBusiness.getWarehouseInfoByKey(bean.getWarehouseId());
		} else {
			// 新增出库单
			if (!adminUserHelper.checkPermission(InventoryResource.DELIVERY_VOUCHER_INFO,
					OperationConfig.CREATE)) {
				return;
			}
			int wareType = ParamKit.getIntParameter(request, "wareType", 0);
			if (wareType != InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM && wareType != InfoTypeConfig.INFO_TYPE_MATERIAL) {
				HttpResponseKit.alertMessage(response, "制品类型不合法", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			int dvType = ParamKit.getIntParameter(request, "dvType", 0);
			if (!DeliveryVoucherConfig.DELIVERY_VOUCHER_TYPE_MAP.containsKey(dvType)
					|| dvType == DeliveryVoucherConfig.DVT_TRANSFER) {
				HttpResponseKit.alertMessage(response, "出库类型不合法", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			bean.setWareType(wareType);
			bean.setDvType(dvType);
			bean.setStockOutTime(DateKit.getCurrentTimestamp());
			bean.setApproveStatus(DeliveryVoucherConfig.REVIEW_STATUS_UNCOMMIT);
			bean.setDvStatus(DeliveryVoucherConfig.DV_STATUS_EDIT);
			bean.setSyncStatus(DeliveryVoucherConfig.SYNC_STATUS_NOT_SYNCHORIZED);
			bean.setStatus(Constants.STATUS_VALID);
			bean.setCreateUser(adminUserHelper.getAdminUserId());
		}
		request.setAttribute("DeliveryVoucherInfoBean", bean);
		request.setAttribute("warehouseInfoBean", warehouseInfoBean);
		// 请求转发
		RequestDispatcher rd = request
				.getRequestDispatcher(InventoryConsoleConfig.INVENTORY_PAGE_PATH + "DeliveryVoucherInfoManage.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		DeliveryVoucherInfoBean bean = new DeliveryVoucherInfoBean();
		DeliveryVoucherNumberGenerator.lock.lock();// 获取lock，在订单生成完成后或发生异常后释放
		try {
			bean = BeanKit.request2Bean(request, DeliveryVoucherInfoBean.class);
			if (bean.getDvId() == 0) {
				String dvNumber = DeliveryVoucherNumberGenerator.generateDeliveryVoucherNumber(bean);
				bean.setDvNumber(dvNumber);
			}
			DeliveryVoucherInfoBusiness business = new DeliveryVoucherInfoBusiness();

			ReturnMessageBean messageBean = business.verifyDeliveryVoucherInfo(bean);
			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			int wareType = ParamKit.getIntParameter(request, "wareType", 0);
			if (wareType != InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM && wareType != InfoTypeConfig.INFO_TYPE_MATERIAL) {
				HttpResponseKit.alertMessage(response, "制品类型不合法", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			int dvType = ParamKit.getIntParameter(request, "dvType", 0);
			if (!DeliveryVoucherConfig.DELIVERY_VOUCHER_TYPE_MAP.containsKey(dvType)
					|| dvType == DeliveryVoucherConfig.DVT_TRANSFER) {
				HttpResponseKit.alertMessage(response, "出库类型不合法", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			// 处理ITEM
			int[] selectedItems = ParamKit.getIntParameters(request, "selectedItem", 0);
			DeliveryVoucherItemBusiness deliveryVoucherItemBusiness = new DeliveryVoucherItemBusiness();
			DeliveryVoucherItemBean itemBean = null;
			// 处理通过弹窗添加的
			List<DeliveryVoucherItemBean> newDvItemList = new ArrayList<DeliveryVoucherItemBean>();
			if (selectedItems != null) {
				for (int wiId : selectedItems) {
					if (wiId != 0) {
						itemBean = new DeliveryVoucherItemBean();
						String itemValue = ParamKit.getParameter(request, "selectedItemValue_" + wiId, "");
						int wareId = ParamKit.getIntParameter(request, "wareId_" + wiId, 0);
						if (StringKit.isValid(itemValue.trim())) {
							Map<String, String> itemValueMap = MapKit.string2Map(itemValue.trim(), ",", "=");
							ReturnMessageBean message = deliveryVoucherItemBusiness.validateDvDetail(itemValueMap);
							if (StringKit.isValid(message.getMessage())) {
								HttpResponseKit.alertMessage(response, message.getMessage(),
										HttpResponseKit.ACTION_HISTORY_BACK);
								return;
							}
							// 避免类型转换错误
							try {
								Timestamp productionDate = DateKit.str2Timestamp(itemValueMap.get("productionDate"),
										DateKit.DEFAULT_DATE_FORMAT);
								itemBean.setWareType(wareType);
								itemBean.setWareId(wareId);
								itemBean.setUnit(itemValueMap.get("unit"));
								itemBean.setStockOutQuantity(Double.valueOf(itemValueMap.get("stockOutQuantity")));
								itemBean.setBatchNumber(itemValueMap.get("batchNumber"));
								itemBean.setProductionDate(productionDate);
								itemBean.setInventoryType(Integer.valueOf(itemValueMap.get("inventoryType")));
								itemBean.setMemo(itemValueMap.get("memo"));
								try {
									// 如果没有保质期，则设置为-1
									itemBean.setGuaranteePeriod(Double.valueOf(itemValueMap.get("guaranteePeriod")));
									Timestamp expirationDate = DateKit.str2Timestamp(itemValueMap.get("expirationDate"),
											DateKit.DEFAULT_DATE_FORMAT);
									itemBean.setExpirationDate(expirationDate);
								} catch (NumberFormatException e) {
									itemBean.setGuaranteePeriod(-1f);
								}
								itemBean.setStatus(Constants.STATUS_VALID);
								itemBean.setMemo(itemValueMap.get("memo"));
								newDvItemList.add(itemBean);
							} catch (Exception e) {
								continue;
							}
						}
					}
				}
			}

			// 获取已有的ITEM（不是通过新增产生的，有原单的或者已存在于数据库的）
			List<DeliveryVoucherItemBean> existItemList = new ArrayList<DeliveryVoucherItemBean>();
			// 获取所有的制品ID
			int[] itemIds = ParamKit.getIntParameters(request, "itemId", 0);
			if (itemIds != null) {
				for (int dvItemId : itemIds) {
					// 数据准备
					int wareId = ParamKit.getIntParameter(request, "wareId_" + dvItemId, 0);
					Boolean hasExpiration = deliveryVoucherItemBusiness.checkHasExpiration(wareType, wareId);
					Timestamp productionDate = null;
					String productionDateStr = ParamKit.getParameter(request, "productionDate_" + dvItemId, "");
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
					Double guaranteePeriod = ParamKit.getDoubleParameter(request, "guaranteePeriod_" + dvItemId, -1f);
					if (hasExpiration && guaranteePeriod == 0f) {
						// 是保质期管理要填保质期
						HttpResponseKit.alertMessage(response, "保质期缺失!", HttpResponseKit.ACTION_HISTORY_BACK);
						return;
					}
					Timestamp expirationDate = null;
					String expirationDateStr = ParamKit.getParameter(request, "expirationDate_" + dvItemId, "");
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
								HttpResponseKit.alertMessage(response, "保质期至格式错误!",
										HttpResponseKit.ACTION_HISTORY_BACK);
								return;
							}
						}
					}
					int inventoryType = ParamKit.getIntParameter(request, "inventoryType_" + dvItemId, 0);
					if (inventoryType == 0) {
						HttpResponseKit.alertMessage(response, "库存类型缺失!", HttpResponseKit.ACTION_HISTORY_BACK);
						return;
					}
					itemBean = new DeliveryVoucherItemBean();
					itemBean.setWareType(wareType);
					itemBean.setWareId(wareId);
					itemBean.setUnit(ParamKit.getParameter(request, "unit_" + dvItemId, ""));
					itemBean.setStockOutQuantity(
							ParamKit.getDoubleParameter(request, "stockInQuantity_" + dvItemId, new Double(0)));
					itemBean.setProductionDate(productionDate);
					itemBean.setInventoryType(inventoryType);
					itemBean.setGuaranteePeriod(guaranteePeriod);
					itemBean.setExpirationDate(expirationDate);
					itemBean.setBatchNumber(ParamKit.getParameter(request, "batchNumber_" + dvItemId, ""));
					itemBean.setStatus(Constants.STATUS_VALID);
					itemBean.setMemo(ParamKit.getParameter(request, "memo_" + dvItemId, ""));
					existItemList.add(itemBean);
				}
			}
			// 验证出库数量
			if (!deliveryVoucherItemBusiness.validateStockOutQuantity(ListKit.combineList(existItemList, newDvItemList),
					bean.getWarehouseId())) {
				HttpResponseKit.alertMessage(response, "出库数量少于实际库存", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			// 检查是否有明细，新建必须有明细,newPurchaseOrderItemMap和existItemList有且只有一个不是空
			if (existItemList.isEmpty() && newDvItemList.isEmpty()) {
				HttpResponseKit.alertMessage(response, "出库单明细错误!", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			int result = 0;
			int userId = adminUserHelper.getAdminUserId();
			if (bean.getDvId() > 0) {
				if (!adminUserHelper.checkPermission(InventoryResource.DELIVERY_VOUCHER_INFO,
						OperationConfig.UPDATE)) {
					return;
				}
				bean.setModifyUser(adminUserHelper.getAdminUserId());
				result = business.updateDeliveryVoucherInfo(bean);

				// 处理明细
				if (result > 0) {
					result = deliveryVoucherItemBusiness.processDvItem(existItemList, newDvItemList, bean.getDvId(),
							userId);
				}
				// 明细处理结束

			} else {
				if (!adminUserHelper.checkPermission(InventoryResource.DELIVERY_VOUCHER_INFO,
						OperationConfig.CREATE)) {
					return;
				}
				bean.setCreateUser(adminUserHelper.getAdminUserId());
				result = business.addDeliveryVoucherInfo(bean);

				// 处理明细
				if (result > 0) {
					result = deliveryVoucherItemBusiness.processDvItem(existItemList, newDvItemList, bean.getDvId(),
							userId);
				}
				// 明细处理结束

			}

			if (result > 0) {
				HttpResponseKit.alertMessage(response, "处理成功", "/erp/DeliveryVoucherInfoList.do?wareType="
						+ bean.getWareType() + "&dvType=" + bean.getDvType());
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
			DeliveryVoucherNumberGenerator.lock.unlock();
		}
	}
}
