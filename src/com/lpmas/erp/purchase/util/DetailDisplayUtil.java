package com.lpmas.erp.purchase.util;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.inventory.business.impl.MaterialWarehouseVoucherItemDispalympl;
import com.lpmas.erp.inventory.business.impl.ProductItemWarehouseVoucherItemDisplayImpl;
import com.lpmas.erp.purchase.business.impl.MaterialDeliveryReceivingItemDisplayBusinessImpl;
import com.lpmas.erp.purchase.business.impl.MaterialPoItemDisplayBusinessImpl;
import com.lpmas.erp.purchase.business.impl.ProductDeliveryReceivingItemDisplayBusinessImpl;
import com.lpmas.erp.purchase.business.impl.ProductPoItemDisplayBusinessImpl;
import com.lpmas.erp.purchase.config.PurchaseOrderItemDisplayConfig;

public class DetailDisplayUtil {

	public static String getDisplayStr(int detailType, int poId, Boolean isModify) {
		if (detailType == PurchaseOrderItemDisplayConfig.PRODUCT_PO_DETAIL) {
			return new ProductPoItemDisplayBusinessImpl().getDetailDisplayStr(poId, isModify);
		}
		if (detailType == PurchaseOrderItemDisplayConfig.MATERIAL_PO_DETAIL) {
			return new MaterialPoItemDisplayBusinessImpl().getDetailDisplayStr(poId, isModify);
		}
		return "";
	}

	public static String getDisplayStr(int detailType, int poId, int dnId, Boolean isModify, int DeliveryOrReceiving) {
		if (detailType == PurchaseOrderItemDisplayConfig.PRODUCT_PO_DETAIL) {
			return new ProductDeliveryReceivingItemDisplayBusinessImpl().getDeliveryReceivingDisplayStr(poId, dnId,
					isModify, DeliveryOrReceiving);
		}
		if (detailType == PurchaseOrderItemDisplayConfig.MATERIAL_PO_DETAIL) {
			return new MaterialDeliveryReceivingItemDisplayBusinessImpl().getDeliveryReceivingDisplayStr(poId, dnId,
					isModify, DeliveryOrReceiving);
		}
		return "";
	}

	public static String getWvDetailDispalyStr(int detailType, int wvId, int orgType, int orgOrderId,
			Boolean isModify) {
		if (detailType == InfoTypeConfig.INFO_TYPE_MATERIAL) {
			return new MaterialWarehouseVoucherItemDispalympl().getWarehouseDetailDisStr(wvId, orgType, orgOrderId,
					isModify);
		}
		if (detailType == InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM) {
			return new ProductItemWarehouseVoucherItemDisplayImpl().getWarehouseDetailDisStr(wvId, orgType, orgOrderId,
					isModify);
		}
		return "";
	}

}
