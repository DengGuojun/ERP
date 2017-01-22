package com.lpmas.erp.inventory.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.sync.SyncStatusConfig;
import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.util.StatusKit;

public class DeliveryVoucherConfig {

	// 出库类型
	public static final int DVT_TRANSFER = 1;// 调拨出库
	public static final int DVT_SALE = 2;// 销售出库
	public static final int DVT_VIRTUAL = 3;// 虚拟出库
	public static final int DVT_INVENTORY_LOSS = 4;// 盘亏出库
	public static final int DVT_PICK = 5;// 领用出库
	public static final int DVT_OTHER = 99;// 其他出库
	public static List<StatusBean<Integer, String>> DELIVERY_VOUCHER_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
	public static HashMap<Integer, String> DELIVERY_VOUCHER_TYPE_MAP = new HashMap<Integer, String>();

	// 审核状态
	public static final String REVIEW_STATUS_UNCOMMIT = "UNCOMMIT";
	public static final String REVIEW_STATUS_WAIT_APPROVE = "WAIT";
	public static final String REVIEW_STATUS_PASS = "PASS";
	public static final String REVIEW_STATUS_FAIL = "FAIL";
	public static List<StatusBean<String, String>> REVIEW_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> REVIEW_STATUS_MAP = new HashMap<String, String>();

	// 进度状态
	public static final String DV_STATUS_EDIT = "EDIT";
	public static final String DV_STATUS_WAIT_APPROVE = "WAIT_APPROVE";
	public static final String DV_STATUS_APPROVED = "APPROVED";
	public static final String DV_STATUS_CLOSED = "CLOSED";
	public static List<StatusBean<String, String>> DV_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> DV_STATUS_MAP = new HashMap<String, String>();

	// 同步状态
	public static final String SYNC_STATUS_NOT_SYNCHORIZED = SyncStatusConfig.SYNCS_NONE;
	public static final String SYNC_STATUS_SYNCHORIZED = SyncStatusConfig.SYNCS_SENT;
	public static final String SYNC_STATUS_FAIL = SyncStatusConfig.SYNCS_FAIL;
	public static final String SYNC_STATUS_SUCCESS = SyncStatusConfig.SYNCS_SUCCESS;
	public static List<StatusBean<String, String>> SYNC_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> SYNC_STATUS_MAP = new HashMap<String, String>();

	// 出库单操作
	public static final String DV_ACTION_COMMIT = "COMMIT";
	public static final String DV_ACTION_CANCEL_COMMIT = "CANCEL_COMMIT";
	public static final String DV_ACTION_APPROVE = "APPROVE";
	public static final String DV_ACTION_REJECT = "REJECT";
	public static final String DV_ACTION_CLOSE = "CLOSE";

	// 出库单编码前缀
	public static final String DV_NUMBER_PREFIX_TRANSFER = "DBCK";
	public static final String DV_NUMBER_PREFIX_SALE = "XSCK";
	public static final String DV_NUMBER_PREFIX_VIRTUAL = "XNCK";
	public static final String DV_NUMBER_PREFIX_INVENTORY_LOSS = "PKRCK";
	public static final String DV_NUMBER_PREFIX_PICK = "LYCK";
	public static final String DV_NUMBER_PREFIX_OTHER = "QTCK";
	public static HashMap<Integer, String> DELIVERY_VOUCHER_NUMBER_PREFIX_MAP = new HashMap<Integer, String>();

	static {
		initDeliveryVoucherTypeList();
		initDeliveryVoucherTypeMap();

		initReviewStatusList();
		initReviewStatusMap();

		initWvStatusList();
		initWvStatusMap();

		initSyncStatusList();
		initSyncStatusMap();

		initDeliveryVoucherNumberPrefixMap();
	}

	private static void initDeliveryVoucherTypeList() {
		DELIVERY_VOUCHER_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
		DELIVERY_VOUCHER_TYPE_LIST.add(new StatusBean<Integer, String>(DVT_TRANSFER, "调拨出库"));
		DELIVERY_VOUCHER_TYPE_LIST.add(new StatusBean<Integer, String>(DVT_SALE, "销售出库"));
		DELIVERY_VOUCHER_TYPE_LIST.add(new StatusBean<Integer, String>(DVT_VIRTUAL, "虚拟出库"));
		DELIVERY_VOUCHER_TYPE_LIST.add(new StatusBean<Integer, String>(DVT_INVENTORY_LOSS, "盘亏出库"));
		DELIVERY_VOUCHER_TYPE_LIST.add(new StatusBean<Integer, String>(DVT_PICK, "领用出库"));
		DELIVERY_VOUCHER_TYPE_LIST.add(new StatusBean<Integer, String>(DVT_OTHER, "其他出库"));
	}

	private static void initDeliveryVoucherTypeMap() {
		DELIVERY_VOUCHER_TYPE_MAP = StatusKit.toMap(DELIVERY_VOUCHER_TYPE_LIST);
	}

	private static void initReviewStatusList() {
		REVIEW_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
		REVIEW_STATUS_LIST.add(new StatusBean<String, String>(REVIEW_STATUS_UNCOMMIT, "未提交审批"));
		REVIEW_STATUS_LIST.add(new StatusBean<String, String>(REVIEW_STATUS_WAIT_APPROVE, "待审批"));
		REVIEW_STATUS_LIST.add(new StatusBean<String, String>(REVIEW_STATUS_FAIL, "审批不通过"));
		REVIEW_STATUS_LIST.add(new StatusBean<String, String>(REVIEW_STATUS_PASS, "审批通过"));
	}

	private static void initReviewStatusMap() {
		REVIEW_STATUS_MAP = StatusKit.toMap(REVIEW_STATUS_LIST);
	}

	private static void initWvStatusList() {
		DV_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
		DV_STATUS_LIST.add(new StatusBean<String, String>(DV_STATUS_EDIT, "编辑"));
		DV_STATUS_LIST.add(new StatusBean<String, String>(DV_STATUS_WAIT_APPROVE, "待审批"));
		DV_STATUS_LIST.add(new StatusBean<String, String>(DV_STATUS_APPROVED, "审批通过"));
		DV_STATUS_LIST.add(new StatusBean<String, String>(DV_STATUS_CLOSED, "已关闭"));
	}

	private static void initWvStatusMap() {
		DV_STATUS_MAP = StatusKit.toMap(DV_STATUS_LIST);
	}

	private static void initSyncStatusList() {
		SYNC_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
		SYNC_STATUS_LIST.add(new StatusBean<String, String>(SYNC_STATUS_NOT_SYNCHORIZED, "未同步"));
		SYNC_STATUS_LIST.add(new StatusBean<String, String>(SYNC_STATUS_SYNCHORIZED, "已同步"));
		SYNC_STATUS_LIST.add(new StatusBean<String, String>(SYNC_STATUS_FAIL, "同步失败"));
		SYNC_STATUS_LIST.add(new StatusBean<String, String>(SYNC_STATUS_SUCCESS, "同步成功"));
	}

	private static void initSyncStatusMap() {
		SYNC_STATUS_MAP = StatusKit.toMap(SYNC_STATUS_LIST);
	}

	private static void initDeliveryVoucherNumberPrefixMap() {
		DELIVERY_VOUCHER_NUMBER_PREFIX_MAP.put(DVT_TRANSFER, DV_NUMBER_PREFIX_TRANSFER);
		DELIVERY_VOUCHER_NUMBER_PREFIX_MAP.put(DVT_SALE, DV_NUMBER_PREFIX_SALE);
		DELIVERY_VOUCHER_NUMBER_PREFIX_MAP.put(DVT_VIRTUAL, DV_NUMBER_PREFIX_VIRTUAL);
		DELIVERY_VOUCHER_NUMBER_PREFIX_MAP.put(DVT_INVENTORY_LOSS, DV_NUMBER_PREFIX_INVENTORY_LOSS);
		DELIVERY_VOUCHER_NUMBER_PREFIX_MAP.put(DVT_PICK, DV_NUMBER_PREFIX_PICK);
		DELIVERY_VOUCHER_NUMBER_PREFIX_MAP.put(DVT_OTHER, DV_NUMBER_PREFIX_OTHER);
	}
}
