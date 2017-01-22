package com.lpmas.erp.purchase.business.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lpmas.erp.config.ErpConsoleConfig;
import com.lpmas.erp.purchase.bean.DeliveryNoteInfoBean;
import com.lpmas.erp.purchase.bean.DeliveryNoteItemBean;
import com.lpmas.erp.purchase.bean.PurchaseOrderItemBean;
import com.lpmas.erp.purchase.bean.ReceivingNoteInfoBean;
import com.lpmas.erp.purchase.bean.ReceivingNoteItemBean;
import com.lpmas.erp.purchase.business.DeliveryNoteInfoBusiness;
import com.lpmas.erp.purchase.business.DeliveryNoteItemBusiness;
import com.lpmas.erp.purchase.business.DeliveryReceivingDisplayBusiness;
import com.lpmas.erp.purchase.business.PurchaseOrderItemBusiness;
import com.lpmas.erp.purchase.business.ReceivingNoteInfoBusiness;
import com.lpmas.erp.purchase.business.ReceivingNoteItemBusiness;
import com.lpmas.erp.purchase.config.PurchaseOrderItemDisplayConfig;
import com.lpmas.framework.util.FreemarkerKit;
import com.lpmas.pdm.bean.ProductInfoBean;
import com.lpmas.pdm.bean.ProductItemBean;
import com.lpmas.pdm.client.PdmServiceClient;

public class ProductDeliveryReceivingItemDisplayBusinessImpl extends DeliveryReceivingDisplayBusiness {

	@Override
	public String getDeliveryReceivingDisplayStr(int poId, int dnId, Boolean isModify, int DeliveryOrReceiving) {
		// 根据是商品还是物料判断使用那个模板
		String templeteName = "productDeliveryReceivingDetailDisplay.html";
		Boolean isDelivery = true;
		// 开始查询值并准备放到页面
		FreemarkerKit freemarkerKit = new FreemarkerKit();
		HashMap<String, Object> contentMap = freemarkerKit.getContentMap();

		// 根据订单信息查出订单明细
		PurchaseOrderItemBusiness purchaseOrderItemBusiness = new PurchaseOrderItemBusiness();
		List<PurchaseOrderItemBean> orderItemList = purchaseOrderItemBusiness.getPurchaseOrderItemListByPoId(poId);

		// 根据po item 查出对应的制品信息
		PdmServiceClient client = new PdmServiceClient();
		Map<String, ProductItemBean> itemDetailMap = new HashMap<String, ProductItemBean>();
		Map<String, String> itemTypeMap = new HashMap<String, String>();

		// 发货明细
		// 查出对应的发货单
		DeliveryNoteInfoBusiness deliveryNoteInfoBusiness = new DeliveryNoteInfoBusiness();
		DeliveryNoteInfoBean deliveryNoteInfoBean = deliveryNoteInfoBusiness.getDeliveryNoteInfoByKey(dnId);
		// 查出发货明细
		DeliveryNoteItemBusiness deliveryNoteItemBusiness = new DeliveryNoteItemBusiness();
		List<DeliveryNoteItemBean> deliveryNoteItemList = deliveryNoteItemBusiness.getDeliveryNoteItemListByDnId(dnId);
		// 转换成Map
		Map<String, DeliveryNoteItemBean> deliveryNoteItemMap = new HashMap<String, DeliveryNoteItemBean>();
		for (DeliveryNoteItemBean bean : deliveryNoteItemList) {
			deliveryNoteItemMap.put(String.valueOf(bean.getWareId()), bean);
		}
		contentMap.put("DeliveryNoteItemMap", deliveryNoteItemMap);
		contentMap.put("NoteItemList", deliveryNoteItemList);
		contentMap.put("NoteInfoBean", deliveryNoteInfoBean);

		// 如果是收货明细查出对应的ItemList
		if (DeliveryOrReceiving == PurchaseOrderItemDisplayConfig.RECEVING_DETAIL) {
			ReceivingNoteInfoBusiness receivingNoteInfoBusiness = new ReceivingNoteInfoBusiness();
			ReceivingNoteInfoBean receivingNoteInfoBean = receivingNoteInfoBusiness.getReceivingNoteInfoByDnId(dnId);
			ReceivingNoteItemBusiness receivingNoteItemBusiness = new ReceivingNoteItemBusiness();
			Map<String, ReceivingNoteItemBean> receivingNoteItemMap = new HashMap<String, ReceivingNoteItemBean>();
			if (receivingNoteInfoBean != null) {
				List<ReceivingNoteItemBean> receivingNoteItemList = receivingNoteItemBusiness
						.getReceivingNoteItemListByRnId(receivingNoteInfoBean.getRnId());
				// 转化成MAP,KEY是dnItemId
				for (ReceivingNoteItemBean bean : receivingNoteItemList) {
					receivingNoteItemMap.put(String.valueOf(bean.getDnItemId()), bean);
				}
			}
			isDelivery = false;
			contentMap.put("ReceivingNoteItemMap", receivingNoteItemMap);
			// 收货单时根据收货明细转换MAP
			for (DeliveryNoteItemBean bean : deliveryNoteItemList) {
				ProductItemBean productItemBean = client.getProductItemByKey(bean.getWareId());
				ProductInfoBean productInfoBean = client.getProductInfoByKey(productItemBean.getProductId());
				itemDetailMap.put(String.valueOf(bean.getDnItemId()), productItemBean);
				itemTypeMap.put(String.valueOf(bean.getDnItemId()),
						client.getProductTypeByKey(productInfoBean.getTypeId2()).getTypeName());
			}
		} else {
			// 发货单时根据采购明细转换MAP
			for (PurchaseOrderItemBean bean : orderItemList) {
				ProductItemBean productItemBean = client.getProductItemByKey(bean.getWareId());
				ProductInfoBean productInfoBean = client.getProductInfoByKey(productItemBean.getProductId());
				itemDetailMap.put(String.valueOf(bean.getItemId()), productItemBean);
				itemTypeMap.put(String.valueOf(bean.getItemId()),
						client.getProductTypeByKey(productInfoBean.getTypeId2()).getTypeName());
			}
		}
		contentMap.put("ItemDetailMap", itemDetailMap);
		contentMap.put("ItemTypeMap", itemTypeMap);
		contentMap.put("isModify", isModify);
		contentMap.put("isDelivery", isDelivery);
		contentMap.put("dnId", dnId);
		contentMap.put("OrderItemList", orderItemList);

		return freemarkerKit.mergeTemplate(contentMap, ErpConsoleConfig.TEMPLATE_PATH + "/purchase", templeteName);
	}

}
