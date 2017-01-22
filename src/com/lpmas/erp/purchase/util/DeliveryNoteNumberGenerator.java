package com.lpmas.erp.purchase.util;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.business.DeliveryNoteInfoBusiness;

public class DeliveryNoteNumberGenerator {

	// 用于防止发货明细编码重复冲突的同步锁
	public static Lock lock = new ReentrantLock();
	public static String DELIVERY_PREFIX = "D";

	public static String generateDeliveryNoteNumber(PurchaseOrderInfoBean bean) {
		String poNumber = bean.getPoNumber();
		DeliveryNoteInfoBusiness deliveryNoteInfoBusiness = new DeliveryNoteInfoBusiness();
		int serialNumber = deliveryNoteInfoBusiness.getDeliveryInfoCountByPoId(bean.getPoId()) + 1;
		String result = poNumber + "-" + DELIVERY_PREFIX + serialNumber;
		return result;
	}

}
