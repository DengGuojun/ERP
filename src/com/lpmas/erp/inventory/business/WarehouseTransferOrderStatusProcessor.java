package com.lpmas.erp.inventory.business;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lpmas.erp.inventory.bean.WarehouseTransferOrderInfoBean;
import com.lpmas.erp.inventory.factory.WarehouseTransferOrderStatusFactory;

public class WarehouseTransferOrderStatusProcessor {

	// 当前调拨单状态
	private WarehouseTransferOrderStatus toStatus;
	public static Lock statusLock = new ReentrantLock();

	public WarehouseTransferOrderStatusProcessor(WarehouseTransferOrderInfoBean bean) {
		// 初始化当前调拨单状态
		WarehouseTransferOrderStatusFactory factory = new WarehouseTransferOrderStatusFactory();
		this.toStatus = factory.getWarehouseTransferOrderStatus(bean);
	}

	public int commit() {
		return toStatus.commit();
	}

	public int cancelCommit() {
		return toStatus.cancelCommit();
	}

	public int approve() {
		return toStatus.approve();
	}

	public int reject() {
		return toStatus.reject();
	}

	public int close() {
		return toStatus.close();
	}

	public boolean committable() {
		return toStatus.committable();
	}

	public boolean cancelCommittable() {
		return toStatus.cancelCommittable();
	}

	public boolean approvable() {
		return toStatus.approvable();
	}

	public boolean rejectable() {
		return toStatus.rejectable();
	}

	public boolean closable() {
		return toStatus.closable();
	}

}
