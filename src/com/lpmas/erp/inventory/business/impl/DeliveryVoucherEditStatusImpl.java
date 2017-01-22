package com.lpmas.erp.inventory.business.impl;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.inventory.bean.DeliveryVoucherInfoBean;
import com.lpmas.erp.inventory.business.DeliveryVoucherStatus;
import com.lpmas.erp.inventory.config.DeliveryVoucherConfig;
import com.lpmas.erp.inventory.config.DeliveryVoucherLogConfig;
import com.lpmas.erp.inventory.dao.DeliveryVoucherInfoDao;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.BeanKit;

public class DeliveryVoucherEditStatusImpl implements DeliveryVoucherStatus {

	private DeliveryVoucherInfoBean bean;

	public DeliveryVoucherEditStatusImpl(DeliveryVoucherInfoBean bean) {
		this.bean = bean;
	}

	@Override
	public int commit() {
		int result = Constants.STATUS_NOT_VALID;
		DeliveryVoucherInfoBean originalBean = new DeliveryVoucherInfoBean();
		BeanKit.copyBean(originalBean, bean);
		bean.setDvStatus(DeliveryVoucherConfig.DV_STATUS_WAIT_APPROVE);
		bean.setApproveStatus(DeliveryVoucherConfig.REVIEW_STATUS_WAIT_APPROVE);
		DeliveryVoucherInfoDao dao = new DeliveryVoucherInfoDao();
		if ( dao.updateDeliveryVoucherInfo(bean) > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_DELIVERY_VOUCHER, bean.getDvId(),
					DeliveryVoucherLogConfig.LOG_DELIVERY_VOUCHER_INFO);
			result = Constants.STATUS_VALID;
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
