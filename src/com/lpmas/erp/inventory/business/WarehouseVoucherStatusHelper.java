package com.lpmas.erp.inventory.business;

import com.lpmas.erp.inventory.bean.WarehouseVoucherInfoBean;
import com.lpmas.erp.inventory.config.WarehouseVoucherConfig;

public final class WarehouseVoucherStatusHelper {

	public static boolean committable(WarehouseVoucherInfoBean bean) {
		WarehouseVoucherStatusProcessor business = new WarehouseVoucherStatusProcessor(bean);
		return business.committable();
	}

	public static boolean cancelCommittable(WarehouseVoucherInfoBean bean) {
		WarehouseVoucherStatusProcessor business = new WarehouseVoucherStatusProcessor(bean);
		return business.cancelCommittable();
	}

	public static boolean approvable(WarehouseVoucherInfoBean bean) {
		WarehouseVoucherStatusProcessor business = new WarehouseVoucherStatusProcessor(bean);
		return business.approvable();
	}

	public static boolean rejectable(WarehouseVoucherInfoBean bean) {
		WarehouseVoucherStatusProcessor business = new WarehouseVoucherStatusProcessor(bean);
		return business.rejectable();
	}

	public static boolean closable(WarehouseVoucherInfoBean bean) {
		WarehouseVoucherStatusProcessor business = new WarehouseVoucherStatusProcessor(bean);
		return business.closable();
	}

	public static boolean isLock(WarehouseVoucherInfoBean bean) {
		if (bean.getWvStatus().equals(WarehouseVoucherConfig.WV_STATUS_WAIT_APPROVE)
				|| bean.getWvStatus().equals(WarehouseVoucherConfig.WV_STATUS_APPROVED)
				|| bean.getWvStatus().equals(WarehouseVoucherConfig.WV_STATUS_CLOSED)) {
			return true;
		}
		return false;
	}
}
