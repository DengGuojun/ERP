package com.lpmas.erp.inventory.business.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.inventory.bean.DeliveryVoucherInfoBean;
import com.lpmas.erp.inventory.business.DeliveryVoucherStatus;
import com.lpmas.erp.inventory.config.DeliveryVoucherConfig;
import com.lpmas.erp.inventory.config.DeliveryVoucherLogConfig;
import com.lpmas.erp.inventory.handler.WarehouseInventoryHandler;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.BeanKit;

public class DeliveryVoucherApprovedStatusImpl implements DeliveryVoucherStatus {

	private DeliveryVoucherInfoBean bean;
	private static Logger log = LoggerFactory.getLogger(DeliveryVoucherApprovedStatusImpl.class);

	public DeliveryVoucherApprovedStatusImpl(DeliveryVoucherInfoBean bean) {
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
		int result = Constants.STATUS_NOT_VALID;
		DeliveryVoucherInfoBean originalBean = new DeliveryVoucherInfoBean();
		BeanKit.copyBean(originalBean, bean);
		bean.setDvStatus(DeliveryVoucherConfig.DV_STATUS_CLOSED);
		WarehouseInventoryHandler handler = new WarehouseInventoryHandler();
		try {
			handler.closeDeliveryVoucherInfo(bean);
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_DELIVERY_VOUCHER, bean.getDvId(),
					DeliveryVoucherLogConfig.LOG_DELIVERY_VOUCHER_INFO);
			result = Constants.STATUS_VALID;
		} catch (Exception e) {
			log.error("审批操作失败，库存记录更新失败", e);
		}
		return result;
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
		return true;
	}

}
