package com.lpmas.erp.inventory.business.impl;

import com.lpmas.erp.inventory.bean.DeliveryVoucherInfoBean;
import com.lpmas.erp.inventory.business.DeliveryVoucherStatus;
import com.lpmas.framework.config.Constants;

public class DeliveryVoucherClosedStatusImpl implements DeliveryVoucherStatus {

	@SuppressWarnings("unused")
	private DeliveryVoucherInfoBean bean;

	public DeliveryVoucherClosedStatusImpl(DeliveryVoucherInfoBean bean) {
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
	public int close() {
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
	public boolean closable() {
		return false;
	}

}
