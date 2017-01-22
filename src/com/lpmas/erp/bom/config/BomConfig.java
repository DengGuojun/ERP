package com.lpmas.erp.bom.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.StatusKit;

public class BomConfig {
	// 激活状态
	public static List<StatusBean<Integer, String>> ACTIVE_STATUS_LIST = new ArrayList<StatusBean<Integer, String>>();
	public static HashMap<Integer, String> ACTIVE_STATUS_MAP = new HashMap<Integer, String>();
	// BOM类型
	public static final int PRODUCTION = 1;
	public static final int SALES = 2;
	public static final int VIRTUAL = -1;
	public static List<StatusBean<Integer, String>> BOM_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
	public static HashMap<Integer, String> BOM_TYPE_MAP = new HashMap<Integer, String>();
	// BOM item使用类型
	public static final int PRODUCT = 1;
	public static final int PACKING = 11;
	public static final int COMPLIMENTARY = 21;
	public static List<StatusBean<Integer, String>> BOM_USAGE_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
	public static HashMap<Integer, String> BOM_USAGE_TYPE_MAP = new HashMap<Integer, String>();

	static {
		initActiveStatusList();
		initActiveStatusMap();

		initBomTypeList();
		initBomTypeMap();

		initBomUsageTypeList();
		initBomUsageTypeMap();

	}

	public static void initActiveStatusList() {
		ACTIVE_STATUS_LIST = new ArrayList<StatusBean<Integer, String>>();
		ACTIVE_STATUS_LIST.add(new StatusBean<Integer, String>(Constants.STATUS_VALID, "激活"));
		ACTIVE_STATUS_LIST.add(new StatusBean<Integer, String>(Constants.STATUS_NOT_VALID, "未激活"));
	}

	public static void initActiveStatusMap() {
		ACTIVE_STATUS_MAP = StatusKit.toMap(ACTIVE_STATUS_LIST);
	}

	public static void initBomTypeList() {
		BOM_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
		BOM_TYPE_LIST.add(new StatusBean<Integer, String>(PRODUCTION, "生产"));
		BOM_TYPE_LIST.add(new StatusBean<Integer, String>(SALES, "销售"));
		BOM_TYPE_LIST.add(new StatusBean<Integer, String>(VIRTUAL, "虚拟"));
	}

	public static void initBomTypeMap() {
		BOM_TYPE_MAP = StatusKit.toMap(BOM_TYPE_LIST);
	}

	public static void initBomUsageTypeList() {
		BOM_USAGE_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
		BOM_USAGE_TYPE_LIST.add(new StatusBean<Integer, String>(PRODUCT, "产品,本体"));
		BOM_USAGE_TYPE_LIST.add(new StatusBean<Integer, String>(PACKING, "包装"));
		BOM_USAGE_TYPE_LIST.add(new StatusBean<Integer, String>(COMPLIMENTARY, "附件,随包附件"));
	}

	public static void initBomUsageTypeMap() {
		BOM_USAGE_TYPE_MAP = StatusKit.toMap(BOM_USAGE_TYPE_LIST);
	}
}
