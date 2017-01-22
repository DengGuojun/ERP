package com.lpmas.erp.inventory.business;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.inventory.business.impl.MaterialDeliveryVoucherItemDisplayImpl;
import com.lpmas.erp.inventory.business.impl.ProductItemDeliveryVoucherItemDisplayImpl;

public class DeliveryVoucherItemDisplayUtil {

	public static String getDeliveryVoucherItemDisplayStr(int wareType, int dvId, int orgType, int orgOrderId, Boolean isModify) {
		if (wareType == InfoTypeConfig.INFO_TYPE_MATERIAL) {
			return new MaterialDeliveryVoucherItemDisplayImpl().getDeliveryVoucherDetailDisStr(dvId, orgType, orgOrderId,
					isModify);
		}else if (wareType == InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM) {
			return new ProductItemDeliveryVoucherItemDisplayImpl().getDeliveryVoucherDetailDisStr(dvId, orgType,
					orgOrderId, isModify);
		}
		return "";
	}

}
