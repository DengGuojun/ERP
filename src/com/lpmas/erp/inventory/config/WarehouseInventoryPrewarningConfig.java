package com.lpmas.erp.inventory.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.StatusKit;

public class WarehouseInventoryPrewarningConfig {

	public static List<StatusBean<Integer, String>> WARE_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
	public static HashMap<Integer, String> WARE_TYPE_MAP = new HashMap<Integer, String>();

	public static final int PREWARNING_TYPE_EXPIRATION = 1;
	public static final int PREWARNING_TYPE_INVENTORY = 2;
	public static List<StatusBean<Integer, String>> PREWARNING_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
	public static HashMap<Integer, String> PREWARNING_TYPE_MAP = new HashMap<Integer, String>();

	public static final int IS_AUTO_MODIFY_YES = Constants.STATUS_VALID;
	public static final int IS_AUTO_MODIFY_NO = Constants.STATUS_NOT_VALID;
	public static List<StatusBean<Integer, String>> IS_AUTO_MODIFY_LIST = new ArrayList<StatusBean<Integer, String>>();
	public static HashMap<Integer, String> IS_AUTO_MODIFY_MAP = new HashMap<Integer, String>();

	public static final String GUARANTEE_PERIOD = "guaranteePeriod";
	public static final String MAX_INVENTORY = "maxInventory";
	public static final String MIN_INVENTORY = "minInventory";
	public static HashMap<String, Integer> PREWARNING_CONTENT_MAP = new HashMap<String, Integer>();

	static {
		initWareTypeList();
		initWareTypeMap();

		initPrewarningTypeList();
		initPrewarningTypeMap();

		initIsAutoModifyList();
		initIsAutoModifyMap();

		initPrewarningContentMap();
	}

	private static void initWareTypeList() {
		WARE_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
		WARE_TYPE_LIST.add(new StatusBean<Integer, String>(InfoTypeConfig.INFO_TYPE_MATERIAL, "物料"));
		WARE_TYPE_LIST.add(new StatusBean<Integer, String>(InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM, "商品项"));
	}

	private static void initWareTypeMap() {
		WARE_TYPE_MAP = StatusKit.toMap(WARE_TYPE_LIST);
	}

	private static void initPrewarningTypeList() {
		PREWARNING_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
		PREWARNING_TYPE_LIST.add(new StatusBean<Integer, String>(PREWARNING_TYPE_EXPIRATION, "保质期预警"));
		PREWARNING_TYPE_LIST.add(new StatusBean<Integer, String>(PREWARNING_TYPE_INVENTORY, "库存预警"));
	}

	private static void initPrewarningTypeMap() {
		PREWARNING_TYPE_MAP = StatusKit.toMap(PREWARNING_TYPE_LIST);
	}

	private static void initIsAutoModifyList() {
		IS_AUTO_MODIFY_LIST = new ArrayList<StatusBean<Integer, String>>();
		IS_AUTO_MODIFY_LIST.add(new StatusBean<Integer, String>(IS_AUTO_MODIFY_YES, "自动修改"));
		IS_AUTO_MODIFY_LIST.add(new StatusBean<Integer, String>(IS_AUTO_MODIFY_NO, "非自动修改"));
	}

	private static void initIsAutoModifyMap() {
		IS_AUTO_MODIFY_MAP = StatusKit.toMap(IS_AUTO_MODIFY_LIST);
	}

	private static void initPrewarningContentMap() {
		PREWARNING_CONTENT_MAP = new HashMap<String, Integer>();
		PREWARNING_CONTENT_MAP.put(GUARANTEE_PERIOD, PREWARNING_TYPE_EXPIRATION);
		PREWARNING_CONTENT_MAP.put(MAX_INVENTORY, PREWARNING_TYPE_INVENTORY);
		PREWARNING_CONTENT_MAP.put(MIN_INVENTORY, PREWARNING_TYPE_INVENTORY);
	}

}
