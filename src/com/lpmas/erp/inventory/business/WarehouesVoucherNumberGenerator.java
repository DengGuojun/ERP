package com.lpmas.erp.inventory.business;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lpmas.erp.inventory.bean.WarehouseVoucherInfoBean;
import com.lpmas.erp.inventory.config.WarehouseVoucherConfig;
import com.lpmas.framework.util.DateKit;

import freemarker.template.utility.StringUtil;

public final class WarehouesVoucherNumberGenerator {
	// 用于防止入库单编码重复冲突的同步锁
	public static Lock lock = new ReentrantLock();

	// 在调用生成编码前，线程必须先获得同步锁,否则在大并发下会出现入库单编码重复
	public static String generateWarehouseVoucherNumber(WarehouseVoucherInfoBean bean) {
		String wvNumber = WarehouseVoucherConfig.WAREHOUSE_VOUCHER_NUMBER_PREFIX_MAP.get(bean.getWvType())
				+ DateKit.getCurrentDateTime("yyyyMMdd");
		WarehouseVoucherInfoBusiness business = new WarehouseVoucherInfoBusiness();
		int serialNumber = business.getWarehouseVoucherInfoCountForToday(bean.getWvType());
		serialNumber = serialNumber + 1;
		wvNumber = wvNumber + StringUtil.leftPad(serialNumber + "", 5, "0");// 当天序号不足5位的在前面补0
		return wvNumber;
	}
}
