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

public class PurchaseOrderReceivedStatusImpl implements PurchaseOrderStatus {

	private PurchaseOrderInfoBean bean;

	public PurchaseOrderReceivedStatusImpl(PurchaseOrderInfoBean bean) {
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
		return Constants.STATUS_NOT_VALID;
	}

	@Override
	public int receive() {
		return Constants.STATUS_NOT_VALID;
	}

	@Override
	public int archive() {
		int result = Constants.STATUS_NOT_VALID;
		PurchaseOrderInfoBean originalBean = new PurchaseOrderInfoBean();
		BeanKit.copyBean(originalBean, bean);
		bean.setPoStatus(PurchaseOrderConfig.PO_STATUS_ARCHIVED);
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
		return false;
	}

	@Override
	public boolean receivable() {
		return false;
	}

	@Override
	public boolean archivable() {
		boolean contractValid = bean.getContractStatus()
				.equals(PurchaseOrderConfig.PCS_SIGNED)
				|| bean.getContractStatus().equals(PurchaseOrderConfig.PCS_NON_CONTRACT);
		boolean invoiceValid = bean.getInvoiceStatus().equals(PurchaseOrderConfig.INVOICE_STATUS_FULL);
		boolean paymentValid = bean.getPaymentStatus().equals(PurchaseOrderConfig.PAYMENT_STATUS_FULL_PAYMENT);
		if (contractValid && invoiceValid && paymentValid) {
			return true;
		}
		return false;
	}

}
