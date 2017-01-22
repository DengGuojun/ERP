package com.lpmas.erp.contract.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.util.StatusKit;

public class PurchaseContractConfig {

	public static final int PCT_FREAMWORK = 1;
	public static final int PCT_PURCHASE = 2;
	public static List<StatusBean<Integer, String>> PURCHASE_CONTRACT_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
	public static HashMap<Integer, String> PURCHASE_CONTRACT_TYPE_MAP = new HashMap<Integer, String>();

	static{
		initPurchaseContractTypeList();
		initPurchaseContractTypeMap();
	}
	
	public static void initPurchaseContractTypeList() {
		PURCHASE_CONTRACT_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
		PURCHASE_CONTRACT_TYPE_LIST.add(new StatusBean<Integer, String>(PCT_FREAMWORK, "框架合同"));
		PURCHASE_CONTRACT_TYPE_LIST.add(new StatusBean<Integer, String>(PCT_PURCHASE, "购销合同"));
	}

	public static void initPurchaseContractTypeMap() {
		PURCHASE_CONTRACT_TYPE_MAP = StatusKit.toMap(PURCHASE_CONTRACT_TYPE_LIST);
	}
}
