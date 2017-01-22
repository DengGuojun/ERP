package com.lpmas.erp.purchase.business;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.factory.PurchaseOrderStatusFactory;

public class PurchaseOrderStatusProcessor {

	// 当前采购订单状态
	private PurchaseOrderStatus poStatus;
	public static Lock statusLock = new ReentrantLock();

	public PurchaseOrderStatusProcessor(PurchaseOrderInfoBean poInfoBean) {
		// 初始化当前订单状态
		PurchaseOrderStatusFactory factory = new PurchaseOrderStatusFactory();
		this.poStatus = factory.getPurchaseOrderStatus(poInfoBean);
	}

	public int commit() {
		return poStatus.commit();
	}

	public int cancelCommit() {
		return poStatus.cancelCommit();
	}

	public int approve() {
		return poStatus.approve();
	}

	public int reject() {
		return poStatus.reject();
	}

	public int placeOrder() {
		return poStatus.placeOrder();
	}

	public int cancelOrder() {
		return poStatus.cancelOrder();
	}

	public int receive() {
		return poStatus.receive();
	}

	public int archive() {
		return poStatus.archive();
	}

	public boolean committable() {
		return poStatus.committable();
	}

	public boolean cancelCommittable() {
		return poStatus.cancelCommittable();
	}

	public boolean approvable() {
		return poStatus.approvable();
	}

	public boolean rejectable() {
		return poStatus.rejectable();
	}

	public boolean placeOrderable() {
		return poStatus.placeOrderable();
	}

	public boolean cancelOrderable() {
		return poStatus.cancelOrderable();
	}

	public boolean receivable() {
		return poStatus.receivable();
	}

	public boolean archivable() {
		return poStatus.archivable();
	}

}
