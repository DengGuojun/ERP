package com.lpmas.erp.inventory.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.util.StatusKit;

public class SourceOrderTypeConfig {

	// 源单类型
	public static final int SOURCE_ORDER_TYPE_PURCHASE_ORDER = InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER;
	public static final int SOURCE_ORDER_TYPE_RECEIVING_NOTE = InfoTypeConfig.INFO_TYPE_RECEIVING_NOTE;
	public static final int SOURCE_ORDER_TYPE_WAREHOUSE_TRANSFER_ORDER = InfoTypeConfig.INFO_TYPE_WAREHOUSE_TRANSFER_ORDER;
	public static List<StatusBean<Integer, String>> SOURCE_ORDER_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
	public static HashMap<Integer, String> SOURCE_ORDER_TYPE_MAP = new HashMap<Integer, String>();

	static {
		initSourceOrderTypeList();
		initSourceOrderTypeMap();
	}

	private static void initSourceOrderTypeList() {
		SOURCE_ORDER_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
		SOURCE_ORDER_TYPE_LIST.add(new StatusBean<Integer, String>(SOURCE_ORDER_TYPE_PURCHASE_ORDER, "采购订单"));
		// SOURCE_ORDER_TYPE_LIST.add(new StatusBean<Integer,
		// String>(SOURCE_ORDER_TYPE_RECEIVING_NOTE, "收货单"));
	}

	private static void initSourceOrderTypeMap() {
		SOURCE_ORDER_TYPE_MAP = StatusKit.toMap(SOURCE_ORDER_TYPE_LIST);
	}
}
