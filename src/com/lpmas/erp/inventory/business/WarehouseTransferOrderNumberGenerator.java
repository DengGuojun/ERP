package com.lpmas.erp.inventory.business;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lpmas.erp.inventory.config.WarehouseTransferOrderConfig;
import com.lpmas.framework.util.DateKit;
import com.lpmas.framework.util.NumberKit;

public class WarehouseTransferOrderNumberGenerator {

	// 用于防止调拨单编码重复冲突的同步锁
	public static Lock lock = new ReentrantLock();

	// 在调用生成编码前，线程必须先获得同步锁,否则在大并发下会出现调拨单编码重复
	public static String generateDeliveryVoucherNumber() {
		String toNumber = WarehouseTransferOrderConfig.TO_NUMBER_PREFIX + DateKit.getCurrentDateTime("yyyyMMdd");
		WarehouseTransferOrderInfoBusiness business = new WarehouseTransferOrderInfoBusiness();
		int serialNumber = business.getWarehouseTransferOrderInfoCountForToday();
		serialNumber = serialNumber + 1;
		toNumber = toNumber + NumberKit.fillPreZero(serialNumber, 5);// 当天序号不足5位的在前面补0
		return toNumber;
	}
}
