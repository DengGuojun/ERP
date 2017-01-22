package com.lpmas.erp.config;

import com.lpmas.framework.util.PropertiesKit;

public class ErpDBConfig {

	public static String DB_LINK_ERP_W = PropertiesKit.getBundleProperties(ErpConsoleConfig.ERP_PROP_FILE_NAME,
			"DB_LINK_ERP_W");

	public static String DB_LINK_ERP_R = PropertiesKit.getBundleProperties(ErpConsoleConfig.ERP_PROP_FILE_NAME,
			"DB_LINK_ERP_R");
}
