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

public class PurchaseOrderWaitApproveStatusImpl implements PurchaseOrderStatus {

	private PurchaseOrderInfoBean bean;

	public PurchaseOrderWaitApproveStatusImpl(PurchaseOrderInfoBean bean) {
		this.bean = bean;
	}

	@Override
	public int commit() {
		return Constants.STATUS_NOT_VALID;
	}

	@Override
	public int cancelCommit() {
		int result = Constants.STATUS_NOT_VALID;
		PurchaseOrderInfoBean originalBean = new PurchaseOrderInfoBean();
		BeanKit.copyBean(originalBean, bean);
		bean.setPoStatus(PurchaseOrderConfig.PO_STATUS_EDIT);
		bean.setApprovalStatus(PurchaseOrderConfig.REVIEW_STATUS_UNCOMMIT);
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
	public int approve() {
		int result = Constants.STATUS_NOT_VALID;
		PurchaseOrderInfoBean orgBean = new PurchaseOrderInfoBean();
		BeanKit.copyBean(orgBean, bean);
		bean.setPoStatus(PurchaseOrderConfig.PO_STATUS_APPROVED);
		bean.setApprovalStatus(PurchaseOrderConfig.REVIEW_STATUS_PASS);
		PurchaseOrderInfoDao dao = new PurchaseOrderInfoDao();
		if (dao.updatePurchaseOrderInfo(bean) > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(orgBean, bean, InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER, bean.getPoId(),
					PurchaseOrderLogConfig.LOG_PURCHASE_ORDER_INFO);
			result = Constants.STATUS_VALID;
		}
		return result;
	}

	@Override
	public int reject() {
		int result = Constants.STATUS_NOT_VALID;
		PurchaseOrderInfoBean orginalBean = new PurchaseOrderInfoBean();
		BeanKit.copyBean(orginalBean, bean);
		bean.setPoStatus(PurchaseOrderConfig.PO_STATUS_EDIT);
		bean.setApprovalStatus(PurchaseOrderConfig.REVIEW_STATUS_FAIL);
		PurchaseOrderInfoDao dao = new PurchaseOrderInfoDao();
		if (dao.updatePurchaseOrderInfo(bean) > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(orginalBean, bean, InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER, bean.getPoId(),
					PurchaseOrderLogConfig.LOG_PURCHASE_ORDER_INFO);
			result = Constants.STATUS_VALID;
		}
		return result;
	}

	@Override
	public int placeOrder() {
		return Constants.STATUS_NOT_VALID;
	}

	@Override
	public int cancelOrder() {
		return Constants.STATUS_NOT_VALID;
	}

	@Override
	public int receive() {
		return Constants.STATUS_NOT_VALID;
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
	public boolean placeOrderable() {
		return false;
	}

	@Override
	public boolean cancelOrderable() {
		return false;
	}

	@Override
	public boolean receivable() {
		return false;
	}

	@Override
	public boolean archivable() {
		return false;
	}

}
