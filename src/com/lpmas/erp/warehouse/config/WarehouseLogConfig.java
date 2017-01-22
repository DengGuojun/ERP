package com.lpmas.erp.warehouse.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.util.StatusKit;

public class WarehouseLogConfig {
	// 仓库操作日志配置项
	public static String LOG_WAREHOUSE_INFO = "WAREHOUSE_INFO";
	public static List<StatusBean<String, String>> LOG_WAREHOUSE_LIST = new ArrayList<StatusBean<String, String>>();
	public static Map<String, String> LOG_WAREHOUSE_MAP = new HashMap<String, String>();

	static {

		initLogWareHouseList();
		initLogWareHouseMap();

	}

	public static void initLogWareHouseList() {
		LOG_WAREHOUSE_LIST = new ArrayList<StatusBean<String, String>>();
		LOG_WAREHOUSE_LIST.add(new StatusBean<String, String>(LOG_WAREHOUSE_INFO, "仓库信息"));
	}

	public static void initLogWareHouseMap() {
		LOG_WAREHOUSE_MAP = new HashMap<String, String>();
		LOG_WAREHOUSE_MAP = StatusKit.toMap(LOG_WAREHOUSE_LIST);
	}

}
