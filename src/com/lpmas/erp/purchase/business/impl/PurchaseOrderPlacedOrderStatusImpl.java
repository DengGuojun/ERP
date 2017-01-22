package com.lpmas.erp.purchase.business.impl;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.business.PurchaseOrderStatus;
import com.lpmas.erp.purchase.config.PurchaseOrderConfig;
import com.lpmas.erp.purchase.config.PurchaseOrderLogConfig;
import com.lpmas.erp.purchase.dao.PurchaseOrderInfoDao;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.BeanKit;

public class PurchaseOrderPlacedOrderStatusImpl implements PurchaseOrderStatus {

	private PurchaseOrderInfoBean bean;

	public PurchaseOrderPlacedOrderStatusImpl(PurchaseOrderInfoBean bean) {
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
		int result = Constants.STATUS_NOT_VALID;
		PurchaseOrderInfoBean originalBean = new PurchaseOrderInfoBean();
		BeanKit.copyBean(originalBean, bean);
		bean.setPoStatus(PurchaseOrderConfig.PO_STATUS_APPROVED);
		PurchaseOrderInfoDao dao = new PurchaseOrderInfoDao();
		if (dao.updatePurchaseOrderInfo(bean) > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER, bean.getPoId(),
					PurchaseOrderLogConfig.LOG_PURCHASE_ORDER_INFO);
			result = Constants.STATUS_VALID;
		}
		return result;
	}

	@Override
	public int receive() {
		int result = Constants.STATUS_NOT_VALID;
		PurchaseOrderInfoBean originalBean = new PurchaseOrderInfoBean();
		BeanKit.copyBean(originalBean, bean);
		bean.setPoStatus(PurchaseOrderConfig.PO_STATUS_RECEIVED);
		PurchaseOrderInfoDao dao = new PurchaseOrderInfoDao();
		if (dao.updatePurchaseOrderInfo(bean) > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(originalBean, bean, InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER, bean.getPoId(),
					PurchaseOrderLogConfig.LOG_PURCHASE_ORDER_INFO);
			result = Constants.STATUS_VALID;
		}
		return result;
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
		return true;
	}

	@Override
	public boolean receivable() {
		return true;
	}

	@Override
	public boolean archivable() {
		return false;
	}

}
