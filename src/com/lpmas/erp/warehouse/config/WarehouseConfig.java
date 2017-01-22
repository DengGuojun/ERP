package com.lpmas.erp.warehouse.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.util.StatusKit;

public class WarehouseConfig {

	// 仓库类型
	public static final String WAREHOUSE_TYPE_PHYSICAL = "PHYSICAL";
	public static final String WAREHOUSE_TYPE_VIRTUAL = "VIRTUAL";
	public static List<StatusBean<String, String>> WAREHOUSE_TYPE_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> WAREHOUSE_TYPE_MAP = new HashMap<String, String>();

	static {
		initWarehouseTypeList();
		initWarehouseTypeMap();
	}

	private static void initWarehouseTypeList() {
		WAREHOUSE_TYPE_LIST = new ArrayList<StatusBean<String, String>>();
		WAREHOUSE_TYPE_LIST.add(new StatusBean<String, String>(WAREHOUSE_TYPE_PHYSICAL, "实体仓库"));
		WAREHOUSE_TYPE_LIST.add(new StatusBean<String, String>(WAREHOUSE_TYPE_VIRTUAL, "虚拟仓库"));
	}

	private static void initWarehouseTypeMap() {
		WAREHOUSE_TYPE_MAP = StatusKit.toMap(WAREHOUSE_TYPE_LIST);
	}

}
