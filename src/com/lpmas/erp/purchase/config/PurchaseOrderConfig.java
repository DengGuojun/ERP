package com.lpmas.erp.purchase.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.util.StatusKit;

public class PurchaseOrderConfig {

	public static HashMap<Integer, String> WARE_TYPE_MAP = new HashMap<Integer, String>();

	// 采购类型
	public static final int PURCHASE_TYPE_ORDINARY = 1;
	public static final int PURCHASE_TYPE_DIRECT_SHIPMENT = 2;
	public static List<StatusBean<Integer, String>> PURCHASE_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
	public static HashMap<Integer, String> PURCHASE_TYPE_MAP = new HashMap<Integer, String>();

	// 贷款状态
	public static final String PAYMENT_STATUS_NON_PAYMENT = "NON_PAYMENT";
	public static final String PAYMENT_STATUS_DOWN_PAYMENT = "DOWN_PAYMENT";
	public static final String PAYMENT_STATUS_FULL_PAYMENT = "FULL_PAYMENT";
	public static List<StatusBean<String, String>> PAYMENT_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> PAYMENT_STATUS_MAP = new HashMap<String, String>();

	// 发票状态
	public static final String INVOICE_STATUS_LATER = "LATER";
	public static final String INVOICE_STATUS_FULL = "FULL";
	public static List<StatusBean<String, String>> INVOICE_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> INVOICE_STATUS_MAP = new HashMap<String, String>();

	// 合同状态
	public static final String PCS_LATER = "LATER";
	public static final String PCS_SIGNED = "SIGNED";
	public static final String PCS_NON_CONTRACT = "NON_CONTRACT";
	public static List<StatusBean<String, String>> PURCHASE_CONTRACT_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> PURCHASE_CONTRACT_STATUS_MAP = new HashMap<String, String>();

	// 审核状态
	public static final String REVIEW_STATUS_UNCOMMIT = "UNCOMMIT";
	public static final String REVIEW_STATUS_WAIT_APPROVE = "WAIT";
	public static final String REVIEW_STATUS_PASS = "PASS";
	public static final String REVIEW_STATUS_FAIL = "FAIL";
	public static List<StatusBean<String, String>> REVIEW_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> REVIEW_STATUS_MAP = new HashMap<String, String>();

	// 收货方类型
	public static final int RECEIVER_TYPE_WAREHOUSE = 1;
	public static final int RECEIVER_TYPE_FACTORY = 2;
	public static final int RECEIVER_TYPE_CUSTOMER = 3;

	// 进度状态
	public static final String PO_STATUS_EDIT = "EDIT";
	public static final String PO_STATUS_WAIT_APPROVE = "WAIT_APPROVE";
	public static final String PO_STATUS_APPROVED = "APPROVED";
	public static final String PO_STATUS_PLACED_ORDER = "PLACED_ORDER";
	public static final String PO_STATUS_RECEIVED = "RECEIVED";
	public static final String PO_STATUS_ARCHIVED = "ARCHIVED";
	public static List<StatusBean<String, String>> PO_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> PO_STATUS_MAP = new HashMap<String, String>();

	// 收货状态
	public static final String RECEIVING_STATUS_WAIT_RECEIVE = "WAIT_RECEIVE";
	public static final String RECEIVING_STATUS_RECEIVED = "RECEIVED";
	public static List<StatusBean<String, String>> RECEIVING_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> RECEIVING_STATUS_MAP = new HashMap<String, String>();

	// 订单操作
	public static final String PO_ACTION_COMMIT = "COMMIT";
	public static final String PO_ACTION_CANCEL_COMMIT = "CANCEL_COMMIT";
	public static final String PO_ACTION_APPROVE = "APPROVE";
	public static final String PO_ACTION_REJECT = "REJECT";
	public static final String PO_ACTION_PLACE_ORDER = "PLACE_ORDER";
	public static final String PO_ACTION_CANCEL_PLACE_ORDER = "CANCEL_PLACE_ORDER";
	public static final String PO_ACTION_RECEIVE = "RECEIVE";
	public static final String PO_ACTION_ARCHIVE = "ARCHIVE";
	
	public static final String REGION_COUNTRY_CODE_CHINA = "086";

	static {
		initWareTypeMap();

		initPurchaseTypeList();
		initPurchaseTypeMap();

		initPaymentStatusList();
		initPaymentStatusMap();

		initInvoiceStatusList();
		initInvoiceStatusMap();

		initContractStatusList();
		initContractStatusMap();

		initReviewStatusList();
		initReviewStatusMap();

		initPoStatusList();
		initPoStatusMap();

		initReceivingStatusList();
		initReceivingStatusMap();
	}

	private static void initWareTypeMap() {
		WARE_TYPE_MAP.put(InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM, "商品采购");
		WARE_TYPE_MAP.put(InfoTypeConfig.INFO_TYPE_MATERIAL, "物料采购");
	}

	private static void initPurchaseTypeList() {
		PURCHASE_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
		PURCHASE_TYPE_LIST.add(new StatusBean<Integer, String>(PURCHASE_TYPE_ORDINARY, "一般采购"));
		PURCHASE_TYPE_LIST.add(new StatusBean<Integer, String>(PURCHASE_TYPE_DIRECT_SHIPMENT, "直运采购"));
	}

	private static void initPurchaseTypeMap() {
		PURCHASE_TYPE_MAP = StatusKit.toMap(PURCHASE_TYPE_LIST);
	}

	private static void initPaymentStatusList() {
		PAYMENT_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
		PAYMENT_STATUS_LIST.add(new StatusBean<String, String>(PAYMENT_STATUS_NON_PAYMENT, "未付款"));
		PAYMENT_STATUS_LIST.add(new StatusBean<String, String>(PAYMENT_STATUS_DOWN_PAYMENT, "已付订金"));
		PAYMENT_STATUS_LIST.add(new StatusBean<String, String>(PAYMENT_STATUS_FULL_PAYMENT, "已结账"));
	}

	private static void initPaymentStatusMap() {
		PAYMENT_STATUS_MAP = StatusKit.toMap(PAYMENT_STATUS_LIST);
	}

	private static void initInvoiceStatusList() {
		INVOICE_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
		INVOICE_STATUS_LIST.add(new StatusBean<String, String>(INVOICE_STATUS_LATER, "待后补"));
		INVOICE_STATUS_LIST.add(new StatusBean<String, String>(INVOICE_STATUS_FULL, "齐全"));
	}

	private static void initInvoiceStatusMap() {
		INVOICE_STATUS_MAP = StatusKit.toMap(INVOICE_STATUS_LIST);
	}

	private static void initContractStatusList() {
		PURCHASE_CONTRACT_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
		PURCHASE_CONTRACT_STATUS_LIST.add(new StatusBean<String, String>(PCS_LATER, "待后补"));
		PURCHASE_CONTRACT_STATUS_LIST.add(new StatusBean<String, String>(PCS_SIGNED, "已签署"));
		PURCHASE_CONTRACT_STATUS_LIST.add(new StatusBean<String, String>(PCS_NON_CONTRACT, "无需合同"));
	}

	private static void initContractStatusMap() {
		PURCHASE_CONTRACT_STATUS_MAP = StatusKit.toMap(PURCHASE_CONTRACT_STATUS_LIST);
	}

	private static void initReviewStatusList() {
		REVIEW_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
		REVIEW_STATUS_LIST.add(new StatusBean<String, String>(REVIEW_STATUS_UNCOMMIT, "未提交审批"));
		REVIEW_STATUS_LIST.add(new StatusBean<String, String>(REVIEW_STATUS_WAIT_APPROVE, "待审批"));
		REVIEW_STATUS_LIST.add(new StatusBean<String, String>(REVIEW_STATUS_PASS, "审批通过"));
		REVIEW_STATUS_LIST.add(new StatusBean<String, String>(REVIEW_STATUS_FAIL, "审批不通过"));
	}

	private static void initReviewStatusMap() {
		REVIEW_STATUS_MAP = StatusKit.toMap(REVIEW_STATUS_LIST);
	}

	private static void initPoStatusList() {
		PO_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
		PO_STATUS_LIST.add(new StatusBean<String, String>(PO_STATUS_EDIT, "编辑"));
		PO_STATUS_LIST.add(new StatusBean<String, String>(PO_STATUS_WAIT_APPROVE, "待审批"));
		PO_STATUS_LIST.add(new StatusBean<String, String>(PO_STATUS_APPROVED, "审批通过"));
		PO_STATUS_LIST.add(new StatusBean<String, String>(PO_STATUS_PLACED_ORDER, "已下单"));
		PO_STATUS_LIST.add(new StatusBean<String, String>(PO_STATUS_RECEIVED, "已收货"));
		PO_STATUS_LIST.add(new StatusBean<String, String>(PO_STATUS_ARCHIVED, "已归档"));
	}

	private static void initPoStatusMap() {
		PO_STATUS_MAP = StatusKit.toMap(PO_STATUS_LIST);
	}

	private static void initReceivingStatusList() {
		RECEIVING_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
		RECEIVING_STATUS_LIST.add(new StatusBean<String, String>(RECEIVING_STATUS_WAIT_RECEIVE, "待收货"));
		RECEIVING_STATUS_LIST.add(new StatusBean<String, String>(RECEIVING_STATUS_RECEIVED, "已收货"));
	}

	private static void initReceivingStatusMap() {
		RECEIVING_STATUS_MAP = StatusKit.toMap(RECEIVING_STATUS_LIST);
	}

}
