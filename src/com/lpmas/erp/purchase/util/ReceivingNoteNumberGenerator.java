package com.lpmas.erp.purchase.util;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lpmas.erp.purchase.bean.DeliveryNoteInfoBean;
import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.business.ReceivingNoteInfoBusiness;

public class ReceivingNoteNumberGenerator {

	// 用于防止收货明细编码重复冲突的同步锁
	public static Lock lock = new ReentrantLock();
	public static String RECEIVING_PREFIX = "R";

	public static String generateReceivingNoteNumber(PurchaseOrderInfoBean bean) {
		// 自行生成收货单号
		String poNumber = bean.getPoNumber();
		ReceivingNoteInfoBusiness receivingNoteInfoBusiness = new ReceivingNoteInfoBusiness();
		int serialNumber = receivingNoteInfoBusiness.getReceivingNoteInfoCountByPoId(bean.getPoId()) + 1;
		String result = poNumber + "-" + RECEIVING_PREFIX + serialNumber;
		return result;
	}

	public static String generateReceivingNoteNumber(DeliveryNoteInfoBean bean) {
		return bean.getDnNumber().replace(DeliveryNoteNumberGenerator.DELIVERY_PREFIX, RECEIVING_PREFIX);
	}

}
