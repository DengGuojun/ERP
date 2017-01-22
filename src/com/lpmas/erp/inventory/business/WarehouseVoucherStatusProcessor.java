package com.lpmas.erp.inventory.business;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lpmas.erp.inventory.bean.WarehouseVoucherInfoBean;
import com.lpmas.erp.inventory.factory.WarehouseVoucherStatusFactory;

public class WarehouseVoucherStatusProcessor {

	// 当前入库单状态
	private WarehouseVoucherStatus wvStatus;
	public static Lock statusLock = new ReentrantLock();

	public WarehouseVoucherStatusProcessor(WarehouseVoucherInfoBean bean) {
		// 初始化当前入库单状态
		WarehouseVoucherStatusFactory factory = new WarehouseVoucherStatusFactory();
		this.wvStatus = factory.getWarehouseVoucherStatus(bean);
	}

	public int commit() {
		return wvStatus.commit();
	}

	public int cancelCommit() {
		return wvStatus.cancelCommit();
	}

	public int approve() {
		return wvStatus.approve();
	}

	public int reject() {
		return wvStatus.reject();
	}

	public int close() {
		return wvStatus.close();
	}

	public boolean committable() {
		return wvStatus.committable();
	}

	public boolean cancelCommittable() {
		return wvStatus.cancelCommittable();
	}

	public boolean approvable() {
		return wvStatus.approvable();
	}

	public boolean rejectable() {
		return wvStatus.rejectable();
	}

	public boolean closable() {
		return wvStatus.closable();
	}
}
