package com.lpmas.erp.inventory.business.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.inventory.bean.WarehouseTransferOrderInfoBean;
import com.lpmas.erp.inventory.business.WarehouseTransferOrderStatus;
import com.lpmas.erp.inventory.config.WarehouseTransferOrderConfig;
import com.lpmas.erp.inventory.config.WarehouseTransferOrderLogConfig;
import com.lpmas.erp.inventory.dao.WarehouseTransferOrderInfoDao;
import com.lpmas.erp.inventory.handler.WarehouseInventoryHandler;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.BeanKit;

public class WarehouseTransferOrderWaitApproveStatusImpl implements WarehouseTransferOrderStatus {

	private WarehouseTransferOrderInfoBean bean;
	private Logger logger = LoggerFactory.getLogger(WarehouseTransferOrderWaitApproveStatusImpl.class);

	public WarehouseTransferOrderWaitApproveStatusImpl(WarehouseTransferOrderInfoBean bean) {
		this.bean = bean;
	}

	@Override
	public int commit() {
		return Constants.STATUS_NOT_VALID;
	}

	@Override
	public int cancelCommit() {
		int result = Constants.STATUS_NOT_VALID;
		WarehouseTransferOrderInfoBean originalBean = new WarehouseTransferOrderInfoBean();
		BeanKit.copyBean(originalBean, bean);
		bean.setToStatus(WarehouseTransferOrderConfig.TO_STATUS_EDIT);
		bean.setApproveStatus(WarehouseTransferOrderConfig.APPROVE_STATUS_UNCOMMIT);
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
	public int approve() {
		int result = Constants.STATUS_NOT_VALID;
		WarehouseTransferOrderInfoBean originalBean = new WarehouseTransferOrderInfoBean();
		BeanKit.copyBean(originalBean, bean);
		bean.setApproveStatus(WarehouseTransferOrderConfig.APPROVE_STATUS_PASS);
		bean.setToStatus(WarehouseTransferOrderConfig.TO_STATUS_APPROVED);
		WarehouseInventoryHandler handler = new WarehouseInventoryHandler();
		try {
			handler.approveWarehouseTransferInfo(bean);
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_WAREHOUSE_TRANSFER_ORDER, bean.getToId(),
					WarehouseTransferOrderLogConfig.LOG_WAREHOUSE_TRANSFER_ORDER_INFO);
			result = Constants.STATUS_VALID;
		} catch (Exception e) {
			logger.error("", e);
		}
		return result;
	}

	@Override
	public int reject() {
		int result = Constants.STATUS_NOT_VALID;
		WarehouseTransferOrderInfoBean originalBean = new WarehouseTransferOrderInfoBean();
		BeanKit.copyBean(originalBean, bean);
		bean.setToStatus(WarehouseTransferOrderConfig.TO_STATUS_EDIT);
		bean.setApproveStatus(WarehouseTransferOrderConfig.APPROVE_STATUS_FAIL);
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
	public int close() {
		return Constants.STATUS_NOT_VALID;
	}

	@Override
	public boolean committable() {
		return false;
	}

	@Override
	public boolean cancelCommittable() {
		return true;
	}

	@Override
	public boolean approvable() {
		return true;
	}

	@Override
	public boolean rejectable() {
		return true;
	}

	@Override
	public boolean closable() {
		return false;
	}

}
