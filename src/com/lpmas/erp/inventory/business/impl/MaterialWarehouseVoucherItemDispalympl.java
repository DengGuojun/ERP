package com.lpmas.erp.inventory.business.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.config.ErpConsoleConfig;
import com.lpmas.erp.inventory.bean.WarehouseVoucherItemBean;
import com.lpmas.erp.inventory.business.WarehouseVoucherItemBusiness;
import com.lpmas.erp.inventory.business.WarehouseVoucherItemDisplayBusiness;
import com.lpmas.erp.inventory.config.SourceOrderTypeConfig;
import com.lpmas.erp.purchase.bean.PurchaseOrderItemBean;
import com.lpmas.erp.purchase.business.PurchaseOrderItemBusiness;
import com.lpmas.framework.util.FreemarkerKit;
import com.lpmas.framework.util.ListKit;
import com.lpmas.framework.util.NumeralOperationKit;
import com.lpmas.pdm.bean.MaterialInfoBean;
import com.lpmas.pdm.client.PdmServiceClient;

public class MaterialWarehouseVoucherItemDispalympl extends WarehouseVoucherItemDisplayBusiness {

	@Override
	public String getWarehouseDetailDisStr(int wvId, int sourceType, int sourceOrderId, Boolean isModify) {

		String templeteName = "materialWvDetailDisplay.html";

		FreemarkerKit freemarkerKit = new FreemarkerKit();
		HashMap<String, Object> contentMap = freemarkerKit.getContentMap();
		WarehouseVoucherItemBusiness voucherItemBusiness = new WarehouseVoucherItemBusiness();
		contentMap.put("WvId", wvId);
		contentMap.put("sourceType", sourceType);
		contentMap.put("sourceOrderId", sourceOrderId);
		// 判断有无原单，任意一个不为NULL判定为有原单
		Boolean fromSource = false;
		Boolean hasSource = false;
		if (sourceType != 0 && sourceOrderId != 0) {
			// 有原单
			hasSource = true;
		} else {
			// 无原单
			hasSource = false;
		}
		contentMap.put("hasSource", hasSource);

		List<Integer> wareIdList = new ArrayList<Integer>();
		Map<String, String> unitNameMap = new HashMap<String, String>();
		PdmServiceClient client = new PdmServiceClient();
		if (wvId == 0 && hasSource) {
			// 新建，或者是强制从别的源单取数据
			fromSource = true;
			// 判断源单类型
			switch (sourceType) {
			case SourceOrderTypeConfig.SOURCE_ORDER_TYPE_PURCHASE_ORDER: {
				// 原单是PO单,此时sourceOrderId就是POID
				// 查出PO单对应的PO ITEM
				PurchaseOrderItemBusiness purchaseOrderItemBusiness = new PurchaseOrderItemBusiness();
				List<PurchaseOrderItemBean> poItemList = purchaseOrderItemBusiness
						.getPurchaseOrderItemListByPoId(sourceOrderId);
				Map<Integer, PurchaseOrderItemBean> poItemMap = ListKit.list2Map(poItemList, "wareId");
				if (!poItemList.isEmpty()) {
					wareIdList = ListKit.string2List(ListKit.list2String(poItemList, "wareId", ","), ",",
							Integer.class);
					// 获取单位
					for (PurchaseOrderItemBean orderItemBean : poItemList) {
						unitNameMap.put(String.valueOf(orderItemBean.getWareId()),
								client.getUnitInfoByCode(orderItemBean.getUnit()).getUnitName());
					}
				}
				contentMap.put("ItemList", poItemList);
				contentMap.put("ItemMap", poItemMap);
				break;
			}
			default:
				return "";
			}
		} else {
			// 修改，从入库明细表取数据
			List<WarehouseVoucherItemBean> warehouseVoucherItemList = voucherItemBusiness
					.getWarehouseVoucherItemListByWvId(wvId);
			Map<String, String> discrepancyMap = new HashMap<String, String>();
			// 生成差异值MAP
			for (WarehouseVoucherItemBean temp : warehouseVoucherItemList) {
				discrepancyMap.put(String.valueOf(temp.getWareId()), String.valueOf(
						NumeralOperationKit.subtract(temp.getReceivableQuantity(), temp.getStockInQuantity())));
				// 获取单位
				unitNameMap.put(String.valueOf(temp.getWareId()),
						client.getUnitInfoByCode(temp.getUnit()).getUnitName());
			}
			Map<Integer, WarehouseVoucherItemBean> warehouseVoucherItemMap = ListKit.list2Map(warehouseVoucherItemList,
					"wareId");
			if (!warehouseVoucherItemList.isEmpty()) {
				wareIdList = ListKit.string2List(ListKit.list2String(warehouseVoucherItemList, "wareId", ","), ",",
						Integer.class);
			}
			contentMap.put("DiscrepancyMap", discrepancyMap);
			contentMap.put("ItemList", warehouseVoucherItemList);
			contentMap.put("ItemMap", warehouseVoucherItemMap);
		}

		// 查CLIENT
		// 获取是否保质期管理，和保质期
		Map<String, Boolean> hasExpirationMap = new HashMap<String, Boolean>();
		// 获取制品信息
		Map<String, MaterialInfoBean> itemDetailMap = new HashMap<String, MaterialInfoBean>();
		for (Integer id : wareIdList) {
			MaterialInfoBean materialInfoBean = client.getMaterialInfoByKey(id);
			itemDetailMap.put(String.valueOf(id), materialInfoBean);
			hasExpirationMap.put(String.valueOf(id),
					voucherItemBusiness.checkHasExpiration(InfoTypeConfig.INFO_TYPE_MATERIAL, materialInfoBean));
		}
		// 如果有保质期管理的，获取对应的保质期天数/保质期至
		Map<String, String> guaranteePeriodMap = new HashMap<String, String>();
		for (Entry<String, Boolean> entry : hasExpirationMap.entrySet()) {
			if (entry.getValue()) {
				guaranteePeriodMap.put(entry.getKey(),
						String.valueOf(itemDetailMap.get(entry.getKey()).getGuaranteePeriod()));
			}
		}

		// 把数据存在contentMap中
		contentMap.put("hasExpirationMap", hasExpirationMap);
		contentMap.put("guaranteePeriodMap", guaranteePeriodMap);
		contentMap.put("fromSource", fromSource);
		contentMap.put("ItemDetailMap", itemDetailMap);
		contentMap.put("isModify", isModify);
		contentMap.put("UnitNameMap", unitNameMap);

		return freemarkerKit.mergeTemplate(contentMap, ErpConsoleConfig.TEMPLATE_PATH + "/inventory", templeteName);
	}

}
