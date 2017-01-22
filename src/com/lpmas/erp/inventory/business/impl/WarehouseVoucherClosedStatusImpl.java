package com.lpmas.erp.inventory.business.impl;

import com.lpmas.erp.inventory.bean.WarehouseVoucherInfoBean;
import com.lpmas.erp.inventory.business.WarehouseVoucherStatus;
import com.lpmas.framework.config.Constants;

public class WarehouseVoucherClosedStatusImpl implements WarehouseVoucherStatus {

	@SuppressWarnings("unused")
	private WarehouseVoucherInfoBean bean;

	public WarehouseVoucherClosedStatusImpl(WarehouseVoucherInfoBean bean) {
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
