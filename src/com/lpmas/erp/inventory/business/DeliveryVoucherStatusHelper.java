package com.lpmas.erp.inventory.business;

import com.lpmas.erp.inventory.bean.DeliveryVoucherInfoBean;
import com.lpmas.erp.inventory.config.DeliveryVoucherConfig;

public class DeliveryVoucherStatusHelper {

	public static boolean committable(DeliveryVoucherInfoBean bean) {
		DeliveryVoucherStatusProcessor business = new DeliveryVoucherStatusProcessor(bean);
		return business.committable();
	}

	public static boolean cancelCommittable(DeliveryVoucherInfoBean bean) {
		DeliveryVoucherStatusProcessor business = new DeliveryVoucherStatusProcessor(bean);
		return business.cancelCommittable();
	}

	public static boolean approvable(DeliveryVoucherInfoBean bean) {
		DeliveryVoucherStatusProcessor business = new DeliveryVoucherStatusProcessor(bean);
		return business.approvable();
	}

	public static boolean rejectable(DeliveryVoucherInfoBean bean) {
		DeliveryVoucherStatusProcessor business = new DeliveryVoucherStatusProcessor(bean);
		return business.rejectable();
	}

	public static boolean closable(DeliveryVoucherInfoBean bean) {
		DeliveryVoucherStatusProcessor business = new DeliveryVoucherStatusProcessor(bean);
		return business.closable();
	}

	public static boolean isLock(DeliveryVoucherInfoBean bean) {
		if (bean.getDvStatus().equals(DeliveryVoucherConfig.DV_STATUS_WAIT_APPROVE)
				|| bean.getDvStatus().equals(DeliveryVoucherConfig.DV_STATUS_APPROVED)
				|| bean.getDvStatus().equals(DeliveryVoucherConfig.DV_STATUS_CLOSED)) {
			return true;
		}
		return false;
	}

}
