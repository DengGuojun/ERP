package com.lpmas.erp.warehouse.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.util.StatusKit;

public class WarehousePropertyConfig {

	public static final String PROPERTY_DELIVERY_TO_CUSTOMER = "DELIVERY_TO_CUSTOMER";// 是否向客户发货

	public static List<StatusBean<String, String>> WAREHOUSE_PROPERTY_CONFIG_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> WAREHOUSE_PROPERTY_CONFIG_MAP = new HashMap<String, String>();

	static {
		initWarehousePropertyConfigList();
		initWarehousePropertyConfigMap();
	}

	private static void initWarehousePropertyConfigList() {
		WAREHOUSE_PROPERTY_CONFIG_LIST = new ArrayList<StatusBean<String, String>>();
		WAREHOUSE_PROPERTY_CONFIG_LIST
				.add(new StatusBean<String, String>(PROPERTY_DELIVERY_TO_CUSTOMER, "deliveryToCustomer"));
	}

	private static void initWarehousePropertyConfigMap() {
		WAREHOUSE_PROPERTY_CONFIG_MAP = StatusKit.toMap(WAREHOUSE_PROPERTY_CONFIG_LIST);
	}

}
