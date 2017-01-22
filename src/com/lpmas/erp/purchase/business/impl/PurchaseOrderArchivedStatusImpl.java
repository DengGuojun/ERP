package com.lpmas.erp.purchase.business.impl;

import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.business.PurchaseOrderStatus;
import com.lpmas.framework.config.Constants;

public class PurchaseOrderArchivedStatusImpl implements PurchaseOrderStatus {

	@SuppressWarnings("unused")
	private PurchaseOrderInfoBean bean;

	public PurchaseOrderArchivedStatusImpl(PurchaseOrderInfoBean bean) {
		this.bean = bean;
	}

	@Override
	public int commit() {
		return Constants.STATUS_NOT_VALID;
	}

	@Override
	public int cancelCommit() {
		return Constants.STATUS_NOT_VALID;
	}

	@Override
	public int approve() {
		return Constants.STATUS_NOT_VALID;
	}

	@Override
	public int reject() {
		return Constants.STATUS_NOT_VALID;
	}

	@Override
	public int placeOrder() {
		return Constants.STATUS_NOT_VALID;
	}

	@Override
	public int cancelOrder() {
		return Constants.STATUS_NOT_VALID;
	}

	@Override
	public int receive() {
		return Constants.STATUS_NOT_VALID;
	}

	@Override
	public int archive() {
		return Constants.STATUS_NOT_VALID;
	}

	@Override
	public boolean committable() {
		return false;
	}

	@Override
	public boolean cancelCommittable() {
		return false;
	}

	@Override
	public boolean approvable() {
		return false;
	}

	@Override
	public boolean rejectable() {
		return false;
	}

	@Override
	public boolean placeOrderable() {
		return false;
	}

	@Override
	public boolean cancelOrderable() {
		return false;
	}

	@Override
	public boolean receivable() {
		return false;
	}

	@Override
	public boolean archivable() {
		return false;
	}

}
