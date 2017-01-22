package com.lpmas.erp.inventory.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.util.StatusKit;

public class WarehouseVoucherLogConfig {

	// 入库操作日志配置项
	public static String LOG_WV_INFO = "WAREHOUCE_VOUCHER_INFO";
	public static String LOG_WV_ITEM = "WAREHOUCE_VOUCHER_ITEM";
	public static List<StatusBean<String, String>> LOG_WAREHOUCE_VOUCHER_LIST = new ArrayList<StatusBean<String, String>>();
	public static Map<String, String> LOG_WAREHOUCE_VOUCHER_MAP = new HashMap<String, String>();

	static {

		initLogWarehouseVoucherList();
		initLogWarehouseVoucherMap();

	}

	public static void initLogWarehouseVoucherList() {
		LOG_WAREHOUCE_VOUCHER_LIST = new ArrayList<StatusBean<String, String>>();
		LOG_WAREHOUCE_VOUCHER_LIST.add(new StatusBean<String, String>(LOG_WV_INFO, "入库单"));
		LOG_WAREHOUCE_VOUCHER_LIST.add(new StatusBean<String, String>(LOG_WV_ITEM, "入库单明细"));
	}

	public static void initLogWarehouseVoucherMap() {
		LOG_WAREHOUCE_VOUCHER_MAP = new HashMap<String, String>();
		LOG_WAREHOUCE_VOUCHER_MAP = StatusKit.toMap(LOG_WAREHOUCE_VOUCHER_LIST);
	}
}
