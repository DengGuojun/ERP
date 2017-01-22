package com.lpmas.erp.inventory.business.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.inventory.bean.WarehouseVoucherInfoBean;
import com.lpmas.erp.inventory.business.WarehouseVoucherStatus;
import com.lpmas.erp.inventory.config.WarehouseVoucherConfig;
import com.lpmas.erp.inventory.config.WarehouseVoucherLogConfig;
import com.lpmas.erp.inventory.handler.WarehouseInventoryHandler;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.BeanKit;

public class WarehouseVoucherApprovedStatusImpl implements WarehouseVoucherStatus {

	private static Logger log = LoggerFactory.getLogger(WarehouseVoucherApprovedStatusImpl.class);

	private WarehouseVoucherInfoBean bean;

	public WarehouseVoucherApprovedStatusImpl(WarehouseVoucherInfoBean bean) {
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
		WarehouseVoucherInfoBean originalBean = new WarehouseVoucherInfoBean();
		BeanKit.copyBean(originalBean, bean);
		bean.setWvStatus(WarehouseVoucherConfig.WV_STATUS_CLOSED);
		WarehouseInventoryHandler handler = new WarehouseInventoryHandler();
		try {
			handler.putOnWarehouseInventory(bean);
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_WAREHOUSE_VOUCHER, bean.getWvId(),
					WarehouseVoucherLogConfig.LOG_WV_INFO);
			result = Constants.STATUS_VALID;
		} catch (Exception e) {
			log.error("审批操作失败，库存记录创建失败", e);
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
