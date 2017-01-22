package com.lpmas.erp.inventory.business.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.inventory.bean.DeliveryVoucherInfoBean;
import com.lpmas.erp.inventory.business.DeliveryVoucherStatus;
import com.lpmas.erp.inventory.config.DeliveryVoucherConfig;
import com.lpmas.erp.inventory.config.DeliveryVoucherLogConfig;
import com.lpmas.erp.inventory.dao.DeliveryVoucherInfoDao;
import com.lpmas.erp.inventory.handler.WarehouseInventoryHandler;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;

public class DeliveryVoucherWaitApproveStatusImpl implements DeliveryVoucherStatus {

	private DeliveryVoucherInfoBean bean;
	private static Logger log = LoggerFactory.getLogger(DeliveryVoucherWaitApproveStatusImpl.class);

	public DeliveryVoucherWaitApproveStatusImpl(DeliveryVoucherInfoBean bean) {
		this.bean = bean;
	}

	@Override
	public int commit() {
		return Constants.STATUS_NOT_VALID;
	}

	@Override
	public int cancelCommit() {
		int result = Constants.STATUS_NOT_VALID;
		DeliveryVoucherInfoBean originalBean = new DeliveryVoucherInfoBean();
		BeanKit.copyBean(originalBean, bean);
		bean.setDvStatus(DeliveryVoucherConfig.DV_STATUS_EDIT);
		bean.setApproveStatus(DeliveryVoucherConfig.REVIEW_STATUS_UNCOMMIT);
		DeliveryVoucherInfoDao dao = new DeliveryVoucherInfoDao();
		if (dao.updateDeliveryVoucherInfo(bean) > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_DELIVERY_VOUCHER, bean.getDvId(),
					DeliveryVoucherLogConfig.LOG_DELIVERY_VOUCHER_INFO);
			result = Constants.STATUS_VALID;
		}
		return result;
	}

	@Override
	public int approve() {
		int result = Constants.STATUS_NOT_VALID;
		DeliveryVoucherInfoBean originalBean = new DeliveryVoucherInfoBean();
		BeanKit.copyBean(originalBean, bean);
		bean.setApproveStatus(DeliveryVoucherConfig.REVIEW_STATUS_PASS);
		bean.setDvStatus(DeliveryVoucherConfig.DV_STATUS_APPROVED);
		WarehouseInventoryHandler handler = new WarehouseInventoryHandler();
		// 检验库存
		ReturnMessageBean messageBean = handler.verifyWarehouseInventory(bean);
		if (StringKit.isValid(messageBean.getMessage())) {
			return result;
		}
		try {
			handler.stockOutWarehouseInventory(bean);
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
	public int reject() {
		int result = Constants.STATUS_NOT_VALID;
		DeliveryVoucherInfoBean originalBean = new DeliveryVoucherInfoBean();
		BeanKit.copyBean(originalBean, bean);
		bean.setDvStatus(DeliveryVoucherConfig.DV_STATUS_EDIT);
		bean.setApproveStatus(DeliveryVoucherConfig.REVIEW_STATUS_FAIL);
		DeliveryVoucherInfoDao dao = new DeliveryVoucherInfoDao();
		if (dao.updateDeliveryVoucherInfo(bean) > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_DELIVERY_VOUCHER, bean.getDvId(),
					DeliveryVoucherLogConfig.LOG_DELIVERY_VOUCHER_INFO);
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
