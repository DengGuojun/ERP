package com.lpmas.erp.purchase.util;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.business.PurchaseOrderInfoBusiness;
import com.lpmas.framework.util.DateKit;

import freemarker.template.utility.StringUtil;

public final class PurchaseOrderNumberGenerator {
	// 用于防止采购订单编码重复冲突的同步锁
	public static Lock lock = new ReentrantLock();
	public static String PONUMBER_PREFIX = "PO";

	// 在调用生成编码前，线程必须先获得同步锁,否则在大并发下会出现采购订单编码重复
	public static String generatePurchaseOrderNumber(PurchaseOrderInfoBean bean) {
		String poNumber = PONUMBER_PREFIX + DateKit.getCurrentDateTime("yyyyMMdd");
		PurchaseOrderInfoBusiness business = new PurchaseOrderInfoBusiness();
		int serialNumber = business.getPurchaseOrderInfoCountForToday();
		serialNumber = serialNumber + 1;
		poNumber = poNumber + StringUtil.leftPad(serialNumber + "", 5, "0");// 当天序号不足5位的在前面补0
		return poNumber;
	}
}
