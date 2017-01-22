package com.lpmas.erp.inventory.business;

import com.lpmas.erp.inventory.bean.WarehouseTransferOrderInfoBean;
import com.lpmas.erp.inventory.config.WarehouseTransferOrderConfig;

public class WarehouseTransferOrderHelper {

	public static boolean committable(WarehouseTransferOrderInfoBean bean) {
		WarehouseTransferOrderStatusProcessor business = new WarehouseTransferOrderStatusProcessor(bean);
		return business.committable();
	}

	public static boolean cancelCommittable(WarehouseTransferOrderInfoBean bean) {
		WarehouseTransferOrderStatusProcessor business = new WarehouseTransferOrderStatusProcessor(bean);
		return business.cancelCommittable();
	}

	public static boolean approvable(WarehouseTransferOrderInfoBean bean) {
		WarehouseTransferOrderStatusProcessor business = new WarehouseTransferOrderStatusProcessor(bean);
		return business.approvable();
	}

	public static boolean rejectable(WarehouseTransferOrderInfoBean bean) {
		WarehouseTransferOrderStatusProcessor business = new WarehouseTransferOrderStatusProcessor(bean);
		return business.rejectable();
	}

	public static boolean closable(WarehouseTransferOrderInfoBean bean) {
		WarehouseTransferOrderStatusProcessor business = new WarehouseTransferOrderStatusProcessor(bean);
		return business.closable();
	}

	public static boolean isLock(WarehouseTransferOrderInfoBean bean) {
		if (bean.getToStatus().equals(WarehouseTransferOrderConfig.TO_STATUS_WAIT_APPROVE)
				|| bean.getToStatus().equals(WarehouseTransferOrderConfig.TO_STATUS_APPROVED)
				|| bean.getToStatus().equals(WarehouseTransferOrderConfig.TO_STATUS_CLOSED)
				|| bean.getToStatus().equals(WarehouseTransferOrderConfig.TO_STATUS_IN_TRANSIT)) {
			return true;
		}
		return false;
	}
}
