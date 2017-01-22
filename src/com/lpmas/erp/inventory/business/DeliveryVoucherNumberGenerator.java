package com.lpmas.erp.inventory.business;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lpmas.erp.inventory.bean.DeliveryVoucherInfoBean;
import com.lpmas.erp.inventory.config.DeliveryVoucherConfig;
import com.lpmas.framework.util.DateKit;
import com.lpmas.framework.util.NumberKit;

public class DeliveryVoucherNumberGenerator {

	// 用于防止出库单编码重复冲突的同步锁
	public static Lock lock = new ReentrantLock();

	// 在调用生成编码前，线程必须先获得同步锁,否则在大并发下会出现出库单编码重复
	public static String generateDeliveryVoucherNumber(DeliveryVoucherInfoBean bean) {
		String dvNumber = DeliveryVoucherConfig.DELIVERY_VOUCHER_NUMBER_PREFIX_MAP.get(bean.getDvType())
				+ DateKit.getCurrentDateTime("yyyyMMdd");
		DeliveryVoucherInfoBusiness business = new DeliveryVoucherInfoBusiness();
		int serialNumber = business.getDeliveryVoucherInfoCountForToday(bean.getDvType());
		serialNumber = serialNumber + 1;
		dvNumber = dvNumber + NumberKit.fillPreZero(serialNumber, 5);// 当天序号不足5位的在前面补0
		return dvNumber;
	}
}
