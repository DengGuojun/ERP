package com.lpmas.erp.inventory.business.impl;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.inventory.bean.WarehouseVoucherInfoBean;
import com.lpmas.erp.inventory.business.WarehouseVoucherStatus;
import com.lpmas.erp.inventory.config.WarehouseVoucherConfig;
import com.lpmas.erp.inventory.config.WarehouseVoucherLogConfig;
import com.lpmas.erp.inventory.dao.WarehouseVoucherInfoDao;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.BeanKit;

public class WarehouseVoucherEditStatusImpl implements WarehouseVoucherStatus {

	private WarehouseVoucherInfoBean bean;

	public WarehouseVoucherEditStatusImpl(WarehouseVoucherInfoBean bean) {
		this.bean = bean;
	}

	@Override
	public int commit() {
		WarehouseVoucherInfoBean originalBean = new WarehouseVoucherInfoBean();
		BeanKit.copyBean(originalBean, bean);
		bean.setWvStatus(WarehouseVoucherConfig.WV_STATUS_WAIT_APPROVE);
		bean.setApproveStatus(WarehouseVoucherConfig.REVIEW_STATUS_WAIT_APPROVE);
		WarehouseVoucherInfoDao dao = new WarehouseVoucherInfoDao();
		int result = dao.updateWarehouseVoucherInfo(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_WAREHOUSE_VOUCHER, bean.getWvId(),
					WarehouseVoucherLogConfig.LOG_WV_INFO);
		}
		return result;
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
		return true;
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
