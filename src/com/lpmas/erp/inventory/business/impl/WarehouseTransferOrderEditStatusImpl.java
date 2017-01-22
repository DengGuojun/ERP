package com.lpmas.erp.inventory.business.impl;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.inventory.bean.WarehouseTransferOrderInfoBean;
import com.lpmas.erp.inventory.business.WarehouseTransferOrderStatus;
import com.lpmas.erp.inventory.config.WarehouseTransferOrderConfig;
import com.lpmas.erp.inventory.config.WarehouseTransferOrderLogConfig;
import com.lpmas.erp.inventory.dao.WarehouseTransferOrderInfoDao;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.BeanKit;

public class WarehouseTransferOrderEditStatusImpl implements WarehouseTransferOrderStatus {

	private WarehouseTransferOrderInfoBean bean;

	public WarehouseTransferOrderEditStatusImpl(WarehouseTransferOrderInfoBean bean) {
		this.bean = bean;
	}

	@Override
	public int commit() {
		int result = Constants.STATUS_NOT_VALID;
		WarehouseTransferOrderInfoBean originalBean = new WarehouseTransferOrderInfoBean();
		BeanKit.copyBean(originalBean, bean);
		bean.setToStatus(WarehouseTransferOrderConfig.TO_STATUS_WAIT_APPROVE);
		bean.setApproveStatus(WarehouseTransferOrderConfig.APPROVE_STATUS_WAIT_APPROVE);
		WarehouseTransferOrderInfoDao dao = new WarehouseTransferOrderInfoDao();
		if (dao.updateWarehouseTransferOrderInfo(bean) > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_WAREHOUSE_TRANSFER_ORDER, bean.getToId(),
					WarehouseTransferOrderLogConfig.LOG_WAREHOUSE_TRANSFER_ORDER_INFO);
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
