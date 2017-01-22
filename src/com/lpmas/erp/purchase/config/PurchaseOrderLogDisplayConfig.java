package com.lpmas.erp.purchase.config;

import java.util.HashMap;
import java.util.Map;

public class PurchaseOrderLogDisplayConfig {
	public static Map<String, Map<?, ?>> CONFIG_VALUE_MAP = new HashMap<String, Map<?, ?>>();

	static {
		CONFIG_VALUE_MAP = new HashMap<String, Map<?, ?>>();
		CONFIG_VALUE_MAP.put("收货状态", PurchaseOrderConfig.RECEIVING_STATUS_MAP);
		CONFIG_VALUE_MAP.put("采购订单状态", PurchaseOrderConfig.PO_STATUS_MAP);
		CONFIG_VALUE_MAP.put("合同状态", PurchaseOrderConfig.PURCHASE_CONTRACT_STATUS_MAP);
		CONFIG_VALUE_MAP.put("发票状态", PurchaseOrderConfig.INVOICE_STATUS_MAP);
		CONFIG_VALUE_MAP.put("货款状态", PurchaseOrderConfig.PAYMENT_STATUS_MAP);
		CONFIG_VALUE_MAP.put("审批状态", PurchaseOrderConfig.REVIEW_STATUS_MAP);
		CONFIG_VALUE_MAP.put("制品类型", PurchaseOrderConfig.WARE_TYPE_MAP);
		CONFIG_VALUE_MAP.put("采购类型", PurchaseOrderConfig.PURCHASE_TYPE_MAP);
	}

}
