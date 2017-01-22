package com.lpmas.erp.inventory.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.util.StatusKit;

public class DeliveryVoucherLogConfig {
	// 出库操作日志配置项
	public static String LOG_DELIVERY_VOUCHER_INFO = "DELIVERY_VOUCHER_INFO";
	public static String LOG_DELIVERY_VOUCHER_ITEM = "DELIVERY_VOUCHER_ITEM";
	public static List<StatusBean<String, String>> LOG_DELIVERY_VOUCHER_LIST = new ArrayList<StatusBean<String, String>>();
	public static Map<String, String> LOG_DELIVERY_VOUCHER_MAP = new HashMap<String, String>();

	static {

		initLogDeliveryVoucherList();
		initLogDeliveryVoucherMap();

	}

	public static void initLogDeliveryVoucherList() {
		LOG_DELIVERY_VOUCHER_LIST = new ArrayList<StatusBean<String, String>>();
		LOG_DELIVERY_VOUCHER_LIST.add(new StatusBean<String, String>(LOG_DELIVERY_VOUCHER_INFO, "出库单"));
		LOG_DELIVERY_VOUCHER_LIST.add(new StatusBean<String, String>(LOG_DELIVERY_VOUCHER_ITEM, "出库单明细"));
	}

	public static void initLogDeliveryVoucherMap() {
		LOG_DELIVERY_VOUCHER_MAP = new HashMap<String, String>();
		LOG_DELIVERY_VOUCHER_MAP = StatusKit.toMap(LOG_DELIVERY_VOUCHER_LIST);
	}

}
