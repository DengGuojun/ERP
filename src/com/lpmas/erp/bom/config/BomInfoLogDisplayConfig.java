package com.lpmas.erp.bom.config;

import java.util.HashMap;
import java.util.Map;

public class BomInfoLogDisplayConfig {
	public static Map<String, Map<?, ?>> CONFIG_VALUE_MAP = new HashMap<String, Map<?, ?>>();
	static {
		CONFIG_VALUE_MAP = new HashMap<String, Map<?, ?>>();
		CONFIG_VALUE_MAP.put("激活状态", BomConfig.ACTIVE_STATUS_MAP);
		CONFIG_VALUE_MAP.put("BOM类型", BomConfig.BOM_TYPE_MAP);
		CONFIG_VALUE_MAP.put("使用类型", BomConfig.BOM_USAGE_TYPE_MAP);
	}
}
