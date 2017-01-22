package com.lpmas.erp.inventory.business;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lpmas.erp.inventory.bean.DeliveryVoucherInfoBean;
import com.lpmas.erp.inventory.factory.DeliveryVoucherStatusFactory;

public class DeliveryVoucherStatusProcessor {

	// 当前出库单状态
	private DeliveryVoucherStatus dvStatus;
	public static Lock statusLock = new ReentrantLock();

	public DeliveryVoucherStatusProcessor(DeliveryVoucherInfoBean bean) {
		// 初始化当前出库单状态
		DeliveryVoucherStatusFactory factory = new DeliveryVoucherStatusFactory();
		this.dvStatus = factory.getDeliveryVoucherStatus(bean);
	}

	public int commit() {
		return dvStatus.commit();
	}

	public int cancelCommit() {
		return dvStatus.cancelCommit();
	}

	public int approve() {
		return dvStatus.approve();
	}

	public int reject() {
		return dvStatus.reject();
	}

	public int close() {
		return dvStatus.close();
	}

	public boolean committable() {
		return dvStatus.committable();
	}

	public boolean cancelCommittable() {
		return dvStatus.cancelCommittable();
	}

	public boolean approvable() {
		return dvStatus.approvable();
	}

	public boolean rejectable() {
		return dvStatus.rejectable();
	}

	public boolean closable() {
		return dvStatus.closable();
	}

}
