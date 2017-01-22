package com.lpmas.erp.inventory.config;

import java.util.HashMap;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.config.ErpConsoleConfig;

public class InventoryConsoleConfig {

	public static final String INVENTORY_PAGE_PATH = ErpConsoleConfig.PAGE_PATH + "inventory/";

	public static HashMap<Integer, String> WARE_TYPE_MAP = new HashMap<Integer, String>();

	static {
		initWareTypeMap();
	}

	private static void initWareTypeMap() {
		WARE_TYPE_MAP.put(InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM, "商品");
		WARE_TYPE_MAP.put(InfoTypeConfig.INFO_TYPE_MATERIAL, "物料");
	}

}
