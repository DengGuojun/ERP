package com.lpmas.erp.purchase.business.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lpmas.erp.config.ErpConsoleConfig;
import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.bean.PurchaseOrderItemBean;
import com.lpmas.erp.purchase.business.PurchaseOrderInfoBusiness;
import com.lpmas.erp.purchase.business.PurchaseOrderItemBusiness;
import com.lpmas.erp.purchase.business.PurchaseOrderItemDisplayBusiness;
import com.lpmas.framework.util.FreemarkerKit;
import com.lpmas.pdm.bean.ProductInfoBean;
import com.lpmas.pdm.bean.ProductItemBean;
import com.lpmas.pdm.client.PdmServiceClient;

public class ProductPoItemDisplayBusinessImpl extends PurchaseOrderItemDisplayBusiness {

	@Override
	public String getDetailDisplayStr(int poId, Boolean isModify) {
		// 显示商品采购明细
		String templeteName = "productPoDetailDisplay.html";

		FreemarkerKit freemarkerKit = new FreemarkerKit();
		HashMap<String, Object> contentMap = freemarkerKit.getContentMap();
		// 查出PO单对应的POINFO
		PurchaseOrderInfoBusiness purchaseOrderInfoBusiness = new PurchaseOrderInfoBusiness();
		PurchaseOrderInfoBean orderInfoBean = purchaseOrderInfoBusiness.getPurchaseOrderInfoByKey(poId);
		// 查出PO单对应的PO ITEM
		PurchaseOrderItemBusiness purchaseOrderItemBusiness = new PurchaseOrderItemBusiness();
		List<PurchaseOrderItemBean> itemList = purchaseOrderItemBusiness.getPurchaseOrderItemListByPoId(poId);
		// 根据po item 查出对应的制品信息
		PdmServiceClient client = new PdmServiceClient();
		Map<String, ProductItemBean> itemDetailMap = new HashMap<String, ProductItemBean>();
		Map<String, String> itemTypeMap = new HashMap<String, String>();
		Map<String, String> unitNameMap = new HashMap<String, String>();
		for (PurchaseOrderItemBean bean : itemList) {
			ProductItemBean productItemBean = client.getProductItemByKey(bean.getWareId());
			ProductInfoBean productInfoBean = client.getProductInfoByKey(productItemBean.getProductId());
			itemDetailMap.put(String.valueOf(bean.getItemId()), productItemBean);
			itemTypeMap.put(String.valueOf(bean.getItemId()),
					client.getProductTypeByKey(productInfoBean.getTypeId2()).getTypeName());
			unitNameMap.put(String.valueOf(bean.getItemId()), client.getUnitInfoByCode(bean.getUnit()).getUnitName());
		}

		// 把数据存在contentMap中
		contentMap.put("ItemList", itemList);
		contentMap.put("ItemDetailMap", itemDetailMap);
		contentMap.put("isModify", isModify);
		contentMap.put("ItemTypeMap", itemTypeMap);
		contentMap.put("OrderInfoBean", orderInfoBean);
		contentMap.put("UnitNameMap",unitNameMap);

		return freemarkerKit.mergeTemplate(contentMap, ErpConsoleConfig.TEMPLATE_PATH + "/purchase", templeteName);

	}

}
