package com.lpmas.erp.purchase.util;

import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.business.PurchaseOrderStatusProcessor;
import com.lpmas.erp.purchase.config.PurchaseOrderConfig;

public final class PurchaseOrderStatusHelper {

	public static boolean committable(PurchaseOrderInfoBean bean) {
		PurchaseOrderStatusProcessor business = new PurchaseOrderStatusProcessor(bean);
		return business.committable();
	}

	public static boolean cancelCommittable(PurchaseOrderInfoBean bean) {
		PurchaseOrderStatusProcessor business = new PurchaseOrderStatusProcessor(bean);
		return business.cancelCommittable();
	}

	public static boolean approvable(PurchaseOrderInfoBean bean) {
		PurchaseOrderStatusProcessor business = new PurchaseOrderStatusProcessor(bean);
		return business.approvable();
	}

	public static boolean rejectable(PurchaseOrderInfoBean bean) {
		PurchaseOrderStatusProcessor business = new PurchaseOrderStatusProcessor(bean);
		return business.rejectable();
	}

	public static boolean placeOrderable(PurchaseOrderInfoBean bean) {
		PurchaseOrderStatusProcessor business = new PurchaseOrderStatusProcessor(bean);
		return business.placeOrderable();
	}

	public static boolean cancelOrderable(PurchaseOrderInfoBean bean) {
		PurchaseOrderStatusProcessor business = new PurchaseOrderStatusProcessor(bean);
		return business.cancelOrderable();
	}

	public static boolean receivable(PurchaseOrderInfoBean bean) {
		PurchaseOrderStatusProcessor business = new PurchaseOrderStatusProcessor(bean);
		return business.receivable();
	}

	public static boolean archivable(PurchaseOrderInfoBean bean) {
		PurchaseOrderStatusProcessor business = new PurchaseOrderStatusProcessor(bean);
		return business.archivable();
	}
	
	public static boolean isFinished(PurchaseOrderInfoBean bean){
		if(bean.getPoStatus().equals(PurchaseOrderConfig.PO_STATUS_ARCHIVED)){
			return true;
		}
		return false;
	}

	public static boolean isLock(PurchaseOrderInfoBean bean) {
		if (bean.getPoStatus().equals(PurchaseOrderConfig.PO_STATUS_WAIT_APPROVE)
				|| bean.getPoStatus().equals(PurchaseOrderConfig.PO_STATUS_ARCHIVED)) {
			return true;
		}
		return false;
	}

	public static boolean isPoItemLock(PurchaseOrderInfoBean bean) {
		if (bean.getPoStatus().equals(PurchaseOrderConfig.PO_STATUS_APPROVED)
				|| bean.getPoStatus().equals(PurchaseOrderConfig.PO_STATUS_PLACED_ORDER)
				|| bean.getPoStatus().equals(PurchaseOrderConfig.PO_STATUS_RECEIVED)) {
			return true;
		}
		return false;
	}
}
