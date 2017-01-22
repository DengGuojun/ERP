package com.lpmas.erp.purchase.action;

import java.io.IOException;
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
import com.lpmas.constant.currency.CurrencyConfig;
import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.contract.bean.PurchaseContractInfoBean;
import com.lpmas.erp.contract.business.PurchaseContractInfoBusiness;
import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.bean.PurchaseOrderInfoEntityBean;
import com.lpmas.erp.purchase.bean.PurchaseOrderItemBean;
import com.lpmas.erp.purchase.business.PurchaseOrderInfoBusiness;
import com.lpmas.erp.purchase.business.PurchaseOrderItemBusiness;
import com.lpmas.erp.purchase.config.PurchaseOrderConfig;
import com.lpmas.erp.purchase.config.PurchaseOrderConsoleConfig;
import com.lpmas.erp.purchase.config.PurchaseOrderResource;
import com.lpmas.erp.purchase.util.PurchaseOrderNumberGenerator;
import com.lpmas.erp.purchase.util.PurchaseOrderStatusHelper;
import com.lpmas.erp.warehouse.bean.WarehouseInfoBean;
import com.lpmas.erp.warehouse.business.WarehouseInfoBusiness;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.ListKit;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.NumeralOperationKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.region.bean.CountryInfoBean;
import com.lpmas.region.client.RegionServiceClient;
import com.lpmas.srm.bean.SupplierInfoBean;
import com.lpmas.srm.client.SrmServiceClient;

/**
 * Servlet implementation class PurchaseOrderInfoManage
 */
@WebServlet("/erp/PurchaseOrderInfoManage.do")
public class PurchaseOrderInfoManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(PurchaseOrderInfoManage.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PurchaseOrderInfoManage() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		int poId = ParamKit.getIntParameter(request, "poId", 0);
		boolean readOnly = ParamKit.getBooleanParameter(request, "readOnly", false);

		PurchaseOrderInfoBean bean = new PurchaseOrderInfoBean();
		PurchaseContractInfoBean purchaseContractInfoBean = new PurchaseContractInfoBean();
		SupplierInfoBean supplierInfoBean = new SupplierInfoBean();
		SupplierInfoBean receiverInfoBean = new SupplierInfoBean();
		WarehouseInfoBean warehouseInfoBean = new WarehouseInfoBean();
		if (poId > 0) {
			// 修改采购订单
			if (!readOnly && !adminUserHelper.checkPermission(PurchaseOrderResource.PURCHASE_ORDER_INFO,
					OperationConfig.UPDATE)) {
				return;
			}
			if (readOnly && !adminUserHelper.checkPermission(PurchaseOrderResource.PURCHASE_ORDER_INFO,
					OperationConfig.SEARCH)) {
				return;
			}
			PurchaseOrderInfoBusiness business = new PurchaseOrderInfoBusiness();
			bean = business.getPurchaseOrderInfoByKey(poId);
			if (bean == null) {
				HttpResponseKit.alertMessage(response, "采购单号缺失", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			if (bean.getStatus() == Constants.STATUS_NOT_VALID) {
				HttpResponseKit.alertMessage(response, "采购订单已被删除", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			if (PurchaseOrderStatusHelper.isLock(bean) && !readOnly) {
				HttpResponseKit.alertMessage(response, "采购订单被锁定，不能编辑", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			// 获取关联对象信息
			PurchaseContractInfoBusiness purchaseContractInfoBusiness = new PurchaseContractInfoBusiness();
			purchaseContractInfoBean = purchaseContractInfoBusiness.getPurchaseContractInfoByKey(bean.getContractId());
			SrmServiceClient srmServiceClient = new SrmServiceClient();
			supplierInfoBean = srmServiceClient.getSupplierInfoByKey(bean.getSupplierId());
			WarehouseInfoBusiness warehouseInfoBusiness = new WarehouseInfoBusiness();
			if (bean.getWareType() == InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM
					&& bean.getPurchaseType() == PurchaseOrderConfig.PURCHASE_TYPE_ORDINARY) {
				warehouseInfoBean = warehouseInfoBusiness.getWarehouseInfoByKey(bean.getReceiverId());
			} else if (bean.getWareType() == InfoTypeConfig.INFO_TYPE_MATERIAL) {
				receiverInfoBean = srmServiceClient.getSupplierInfoByKey(bean.getReceiverId());
			}
		} else {
			// 新增采购订单
			if (!adminUserHelper.checkPermission(PurchaseOrderResource.PURCHASE_ORDER_INFO, OperationConfig.CREATE)) {
				return;
			}
			int wareType = ParamKit.getIntParameter(request, "wareType", 0);
			if (wareType != InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM && wareType != InfoTypeConfig.INFO_TYPE_MATERIAL) {
				HttpResponseKit.alertMessage(response, "制品类型不合法", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			int purchaseType = ParamKit.getIntParameter(request, "purchaseType", 0);
			// 商品采购分为一般采购和直运采购，物料采购只有一般采购
			if (wareType == InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM) {
				if (purchaseType != PurchaseOrderConfig.PURCHASE_TYPE_ORDINARY
						&& purchaseType != PurchaseOrderConfig.PURCHASE_TYPE_DIRECT_SHIPMENT) {
					HttpResponseKit.alertMessage(response, "采购类型不合法", HttpResponseKit.ACTION_HISTORY_BACK);
					return;
				}
			} else {
				purchaseType = PurchaseOrderConfig.PURCHASE_TYPE_ORDINARY;
			}
			bean.setWareType(wareType);
			bean.setPurchaseType(purchaseType);
			bean.setApprovalStatus(PurchaseOrderConfig.REVIEW_STATUS_UNCOMMIT);
			bean.setPoStatus(PurchaseOrderConfig.PO_STATUS_EDIT);
			// 根据制品类型和商品采购类型决定收货方类型
			if (wareType == InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM
					&& purchaseType == PurchaseOrderConfig.PURCHASE_TYPE_ORDINARY) {
				bean.setReceiverType(PurchaseOrderConfig.RECEIVER_TYPE_WAREHOUSE);
			} else if (wareType == InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM
					&& purchaseType == PurchaseOrderConfig.PURCHASE_TYPE_DIRECT_SHIPMENT) {
				bean.setReceiverType(PurchaseOrderConfig.RECEIVER_TYPE_CUSTOMER);
			} else {
				bean.setReceiverType(PurchaseOrderConfig.RECEIVER_TYPE_FACTORY);
			}
			bean.setCurrency(CurrencyConfig.CUR_CNY);
			bean.setStatus(Constants.STATUS_VALID);
		}
		PurchaseOrderInfoEntityBean purchaseOrderInfoEntityBean = new PurchaseOrderInfoEntityBean(bean,
				purchaseContractInfoBean, supplierInfoBean, receiverInfoBean, warehouseInfoBean);

		if (bean.getReceiverType() == PurchaseOrderConfig.RECEIVER_TYPE_CUSTOMER) {
			// 获取国家Name
			RegionServiceClient client = new RegionServiceClient();
			List<CountryInfoBean> countryList = client.getCountryInfoAllList();
			CountryInfoBean countryBean = new CountryInfoBean();
			for (CountryInfoBean country : countryList) {
				if (country.getCountryCode().equals(PurchaseOrderConfig.REGION_COUNTRY_CODE_CHINA)) {
					countryBean = country;
					break;
				}
			}
			request.setAttribute("CountryName", countryBean.getCountryName());
		}
		request.setAttribute("PurchaseOrderInfoEntity", purchaseOrderInfoEntityBean);
		request.setAttribute("AdminUserHelper", adminUserHelper);
		RequestDispatcher rd = this.getServletContext().getRequestDispatcher(
				PurchaseOrderConsoleConfig.PURCHASE_ORDER_PAGE_PATH + "PurchaseOrderInfoManage.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		PurchaseOrderNumberGenerator.lock.lock();// 获取lock，在采购订单生成完成后或发生异常后释放
		PurchaseOrderInfoBean bean = new PurchaseOrderInfoBean();
		int poId = 0;
		try {
			bean = BeanKit.request2Bean(request, PurchaseOrderInfoBean.class);
			if (bean.getPoId() == 0) {
				String poNumber = PurchaseOrderNumberGenerator.generatePurchaseOrderNumber(bean);
				bean.setPoNumber(poNumber);
			}
			PurchaseOrderItemBusiness purchaseOrderItemBusiness = new PurchaseOrderItemBusiness();
			PurchaseOrderInfoBusiness business = new PurchaseOrderInfoBusiness();
			ReturnMessageBean messageBean = business.verifyPurchaseOrderInfo(bean);
			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			// 获取新增的ITEM的ID
			// 新建时必须录入明细
			String[] selectedItemIds = new String[] {};
			try {
				selectedItemIds = ParamKit.getArrayParameter(request, "selectedItem");
				if (bean.getPoId() == 0
						&& (!purchaseOrderItemBusiness.isVaildArray(selectedItemIds) || selectedItemIds == null)) {
					HttpResponseKit.alertMessage(response, "采购明细必须填写", HttpResponseKit.ACTION_HISTORY_BACK);
					return;
				}
			} catch (Exception e) {
				HttpResponseKit.alertMessage(response, "采购明细必须填写", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			Map<Integer, PurchaseOrderItemBean> newPurchaseOrderItemMap = new HashMap<Integer, PurchaseOrderItemBean>();
			List<Integer> selectedItemIdList = new ArrayList<Integer>();
			if (selectedItemIds != null) {
				for (String id : selectedItemIds) {
					if (!id.trim().equals("")) {

						PurchaseOrderItemBean itemBean = new PurchaseOrderItemBean();
						String itemValue = ParamKit.getParameter(request, "selectedItemValue_" + id, "");
						if (StringKit.isValid(itemValue.trim())) {
							Map<String, String> itemValueMap = MapKit.string2Map(itemValue.trim(), ",", "=");
							ReturnMessageBean message = purchaseOrderItemBusiness.verifyOrderInfo(itemValueMap);
							if (StringKit.isValid(message.getMessage())) {
								continue;
							}
							selectedItemIdList.add(Integer.valueOf(id.trim()));
							itemBean.setUnit(itemValueMap.get("unit"));
							itemBean.setUnitPrice(Double.valueOf(itemValueMap.get("unitPrice")));
							itemBean.setQuatity(Double.valueOf(itemValueMap.get("quatity")));
							itemBean.setTotalAmount(
									NumeralOperationKit.multiply(itemBean.getQuatity(), itemBean.getUnitPrice(), 2));
							itemBean.setMemo(itemValueMap.get("memo"));
							itemBean.setWareNumber(itemValueMap.get("typecode"));
							itemBean.setWareType(bean.getWareType());
							itemBean.setWareId(Integer.valueOf(id.trim()));
							itemBean.setCurrency(CurrencyConfig.CUR_CNY);
							itemBean.setStatus(Constants.STATUS_VALID);
							itemBean.setCreateUser(adminUserHelper.getAdminUserId());
							newPurchaseOrderItemMap.put(Integer.valueOf(id.trim()), itemBean);
						}
					}
				}
			}
			// 判断是否所有新增明细都有填信息
			if (bean.getPoId() == 0 && newPurchaseOrderItemMap.isEmpty()) {
				HttpResponseKit.alertMessage(response, "采购明细必须填写", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			// 计算PO_AMOUNT
			Double poAmount = 0.00;
			// 新增的
			for (Entry<Integer, PurchaseOrderItemBean> entry : newPurchaseOrderItemMap.entrySet()) {
				poAmount = NumeralOperationKit.add(poAmount, entry.getValue().getTotalAmount());
			}

			// 开始存储PO单INFO
			int result = 0;
			if (bean.getPoId() > 0) {
				// 是修改
				if (!adminUserHelper.checkPermission(PurchaseOrderResource.PURCHASE_ORDER_INFO,
						OperationConfig.UPDATE)) {
					return;
				}
				bean.setModifyUser(adminUserHelper.getAdminUserId());
				poId = bean.getPoId();
				// 获取这个PO单所包括的ITEM,并转化成Map
				List<PurchaseOrderItemBean> purchaseOrderItemList = purchaseOrderItemBusiness
						.getPurchaseOrderItemListByPoId(bean.getPoId());
				Map<Integer, PurchaseOrderItemBean> exitPurchaseOrderItemMap = ListKit.list2Map(purchaseOrderItemList,
						"itemId");
				// 获取修改的ITEM
				String[] modifyItem = ParamKit.getArrayParameter(request, "itemId");
				// 目前还在页面的，且又在库里面的ITEM
				Map<Integer, PurchaseOrderItemBean> exitInPagePurchaseOrderItemMap = new HashMap<Integer, PurchaseOrderItemBean>();
				if (modifyItem != null) {
					// 已经在库里面的ITEM没有完全在页面删除
					if (purchaseOrderItemBusiness.isVaildArray(modifyItem)) {
						// 遍历获取和修改值
						for (String mId : modifyItem) {
							if (!mId.trim().equals("")) {
								PurchaseOrderItemBean orderItemBean = purchaseOrderItemBusiness
										.getPurchaseOrderItemByKey(Integer.valueOf(mId.trim()));
								orderItemBean.setUnit(ParamKit.getParameter(request, "unit_" + mId, ""));
								orderItemBean.setUnitPrice(ParamKit.getDoubleParameter(request, "unitPrice_" + mId, 0));
								orderItemBean.setQuatity(ParamKit.getDoubleParameter(request, "quatity_" + mId, 0));
								orderItemBean.setMemo(ParamKit.getParameter(request, "memo_" + mId, ""));
								orderItemBean.setModifyUser(adminUserHelper.getAdminUserId());
								orderItemBean.setTotalAmount(NumeralOperationKit.multiply(orderItemBean.getQuatity(),
										orderItemBean.getUnitPrice(), 2));
								exitInPagePurchaseOrderItemMap.put(orderItemBean.getItemId(), orderItemBean);
								exitPurchaseOrderItemMap.remove(orderItemBean.getItemId());// 删除已经修改过的，剩下的就是被前台删除的
							}
						}
					}
				}
				if (newPurchaseOrderItemMap.isEmpty() && exitInPagePurchaseOrderItemMap.isEmpty()) {
					// 已经在库里面的ITEM在页面被完全删除
					// 并且没有新的ITEM插进来
					HttpResponseKit.alertMessage(response, "采购明细必须填写", HttpResponseKit.ACTION_HISTORY_BACK);
					return;
				}
				// 计算PO_AMOUNT
				// 现存的
				for (Entry<Integer, PurchaseOrderItemBean> entry : exitInPagePurchaseOrderItemMap.entrySet()) {
					poAmount = NumeralOperationKit.add(poAmount, entry.getValue().getTotalAmount());
				}
				// 更新INFO BEAN
				bean.setPoAmount(poAmount);
				// 如果前台改成了非已签署,合同ID要去掉
				if (!bean.getContractStatus().equals(PurchaseOrderConfig.PCS_SIGNED)) {
					bean.setContractId(Constants.STATUS_NOT_VALID);
				}
				result = business.updatePurchaseOrderInfo(bean);
				if (result < 0) {
					HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
					return;
				}
				// 更新ITEM
				if (!exitInPagePurchaseOrderItemMap.isEmpty()) {
					for (Entry<Integer, PurchaseOrderItemBean> entry : exitInPagePurchaseOrderItemMap.entrySet()) {
						PurchaseOrderItemBean tempItem = entry.getValue();
						purchaseOrderItemBusiness.updatePurchaseOrderItem(tempItem);
					}
				}

				// 逻辑删除
				if (!exitPurchaseOrderItemMap.isEmpty()) {
					for (Entry<Integer, PurchaseOrderItemBean> entry : exitPurchaseOrderItemMap.entrySet()) {
						PurchaseOrderItemBean temp = entry.getValue();
						temp.setStatus(Constants.STATUS_NOT_VALID);
						temp.setModifyUser(adminUserHelper.getAdminUserId());
						purchaseOrderItemBusiness.updatePurchaseOrderItem(temp);
					}
				}

			} else {
				// 是新建
				if (!adminUserHelper.checkPermission(PurchaseOrderResource.PURCHASE_ORDER_INFO,
						OperationConfig.CREATE)) {
					return;
				}
				bean.setCreateUser(adminUserHelper.getAdminUserId());
				bean.setPoAmount(poAmount);
				if (!bean.getContractStatus().equals(PurchaseOrderConfig.PCS_SIGNED)) {
					bean.setContractId(Constants.STATUS_NOT_VALID);
				}
				result = business.addPurchaseOrderInfo(bean);
				poId = result;
			}
			if (result > 0) {
				// 插入新加进来的采购明细条目
				// 插入到采购明细表
				for (Entry<Integer, PurchaseOrderItemBean> entry : newPurchaseOrderItemMap.entrySet()) {
					PurchaseOrderItemBean tempItem = entry.getValue();
					tempItem.setPoId(poId);
					result = purchaseOrderItemBusiness.addPurchaseOrderItem(tempItem);
				}
			}
			if (result > 0) {
				HttpResponseKit.alertMessage(response, "处理成功",
						"/erp/PurchaseOrderInfoList.do?wareType=" + bean.getWareType());
			} else {
				HttpResponseKit.alertMessage(response, "处理失败", HttpResponseKit.ACTION_HISTORY_BACK);
			}
		} catch (Exception e) {
			log.error("", e);
		} finally {
			PurchaseOrderNumberGenerator.lock.unlock();
		}
	}
}
