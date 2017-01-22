package com.lpmas.erp.purchase.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.util.ListKit;
import com.lpmas.framework.util.StatusKit;

public class PurchaseOrderLogConfig {

	// 发货单操作日志配置项
	public static String LOG_DELIVERY_NOTE_INFO = "DELIVERY_NOTE_INFO";
	public static String LOG_DELIVERY_NOTE_ITEM = "DELIVERY_NOTE_ITEM";
	public static List<StatusBean<String, String>> LOG_DELIVERY_LIST = new ArrayList<StatusBean<String, String>>();
	public static Map<String, String> LOG_DELIVERY_MAP = new HashMap<String, String>();

	// 发货单操作日志配置项
	public static String LOG_RECEIVING_NOTE_INFO = "RECEIVING_NOTE_INFO";
	public static String LOG_RECEIVING_NOTE_ITEM = "RECEIVING_NOTE_ITEM";
	public static List<StatusBean<String, String>> LOG_RECEIVING_LIST = new ArrayList<StatusBean<String, String>>();
	public static Map<String, String> LOG_RECEIVING_MAP = new HashMap<String, String>();

	// 采购操作日志配置项
	public static String LOG_PURCHASE_ORDER_INFO = "PURCHASE_ORDER_INFO";
	public static String LOG_PURCHASE_ORDER_ITEM = "PURCHASE_ORDER_ITEM";
	public static String LOG_PURCHASE_ORDER_MEMO = "PURCHASE_ORDER_MEMO";
	public static List<StatusBean<String, String>> LOG_PO_LIST = new ArrayList<StatusBean<String, String>>();
	public static Map<String, String> LOG_PO_MAP = new HashMap<String, String>();

	// 总的日志类型配置列表
	public static List<StatusBean<String, String>> PO_LOG_TYPE_LIST = new ArrayList<StatusBean<String, String>>();
	public static Map<String, String> PO_LOG_TYPE_MAP = new HashMap<String, String>();

	static {

		initLogDeliveryList();
		initLogDeliveryMap();

		initLogReceivingList();
		initLogReceivingMap();

		initLogPoList();
		initLogPoMap();

		initPoLogTypeList();
		initPoLogTypeMap();

	}

	public static void initLogDeliveryList() {
		LOG_DELIVERY_LIST = new ArrayList<StatusBean<String, String>>();
		LOG_DELIVERY_LIST.add(new StatusBean<String, String>(LOG_DELIVERY_NOTE_INFO, "发货单"));
		LOG_DELIVERY_LIST.add(new StatusBean<String, String>(LOG_DELIVERY_NOTE_ITEM, "发货单明细"));
	}

	public static void initLogDeliveryMap() {
		LOG_RECEIVING_MAP = new HashMap<String, String>();
		LOG_DELIVERY_MAP = StatusKit.toMap(LOG_DELIVERY_LIST);
	}

	public static void initLogReceivingList() {
		LOG_RECEIVING_LIST = new ArrayList<StatusBean<String, String>>();
		LOG_RECEIVING_LIST.add(new StatusBean<String, String>(LOG_RECEIVING_NOTE_INFO, "收货单"));
		LOG_RECEIVING_LIST.add(new StatusBean<String, String>(LOG_RECEIVING_NOTE_ITEM, "收货单明细"));
	}

	public static void initLogReceivingMap() {
		LOG_RECEIVING_MAP = new HashMap<String, String>();
		LOG_RECEIVING_MAP = StatusKit.toMap(LOG_RECEIVING_LIST);
	}

	public static void initLogPoList() {
		LOG_PO_LIST = new ArrayList<StatusBean<String, String>>();
		LOG_PO_LIST.add(new StatusBean<String, String>(LOG_PURCHASE_ORDER_INFO, "采购单"));
		LOG_PO_LIST.add(new StatusBean<String, String>(LOG_PURCHASE_ORDER_ITEM, "采购单明细"));
		LOG_PO_LIST.add(new StatusBean<String, String>(LOG_PURCHASE_ORDER_MEMO, "采购单备注"));
	}

	public static void initLogPoMap() {
		LOG_PO_MAP = new HashMap<String, String>();
		LOG_PO_MAP = StatusKit.toMap(LOG_PO_LIST);
	}

	public static void initPoLogTypeList() {
		PO_LOG_TYPE_LIST = new ArrayList<StatusBean<String, String>>();
		PO_LOG_TYPE_LIST = ListKit.combineList(LOG_DELIVERY_LIST, LOG_RECEIVING_LIST);
		PO_LOG_TYPE_LIST = ListKit.combineList(PO_LOG_TYPE_LIST, LOG_PO_LIST);
	}

	public static void initPoLogTypeMap() {
		PO_LOG_TYPE_MAP = StatusKit.toMap(PO_LOG_TYPE_LIST);
	}

}
