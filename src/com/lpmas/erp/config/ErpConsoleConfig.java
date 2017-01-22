package com.lpmas.erp.config;

import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.PropertiesKit;

public class ErpConsoleConfig {

	// appId
	public static final String APP_ID = "ERP";

	public static final String ERP_PROP_FILE_NAME = Constants.PROP_FILE_PATH + "/erp_config";

	public static final Integer DEFAULT_PAGE_NUM = 1;
	public static final Integer DEFAULT_PAGE_SIZE = 20;

	public static final String PAGE_PATH = Constants.PAGE_PATH +"erp/";
	
	public static final String TEMPLATE_PATH = PropertiesKit.getBundleProperties(ERP_PROP_FILE_NAME, "TEMPLATE_PATH");
	
	//国家代码
	public static final String REGION_COUNTRY_CODE_CHINA = "086";
	
	
}
