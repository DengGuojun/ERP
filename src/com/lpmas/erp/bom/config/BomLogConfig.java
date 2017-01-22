package com.lpmas.erp.bom.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.util.StatusKit;

public class BomLogConfig {
	// BOM日志配置项
	public static String LOG_BOM_INFO = "LOG_BOM_INFO";
	public static String LOG_BOM_ITEM = "LOG_BOM_ITEM";
	public static List<StatusBean<String, String>> LOG_BOM_LIST = new ArrayList<StatusBean<String, String>>();
	public static Map<String, String> LOG_BOM_MAP = new HashMap<String, String>();

	static {
		initLogBomList();
		initLogBomMap();
	}

	public static void initLogBomList() {
		LOG_BOM_LIST = new ArrayList<StatusBean<String, String>>();
		LOG_BOM_LIST.add(new StatusBean<String, String>(LOG_BOM_INFO, "BOM信息"));
		LOG_BOM_LIST.add(new StatusBean<String, String>(LOG_BOM_ITEM, "BOM明细"));
	}

	public static void initLogBomMap() {
		LOG_BOM_MAP = new HashMap<String, String>();
		LOG_BOM_MAP = StatusKit.toMap(LOG_BOM_LIST);
	}
}
