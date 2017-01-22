package com.lpmas.erp.inventory.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.util.StatusKit;

public class WarehouseVoucherConfig {

	// 入库类型
	public static final int WVT_TRANSFER = 1;// 调拨入库
	public static final int WVT_PURCHASE = 2;// 采购入库
	public static final int WVT_VIRTUAL = 3;// 虚拟入库
	public static final int WVT_INVENTORY_PROFIT = 4;// 盘盈入库
	public static final int WVT_DEPOSIT = 5;// 代储入库
	public static final int WVT_OTHER = 99;// 其他入库
	public static List<StatusBean<Integer, String>> WAREHOUSE_VOUCHER_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
	public static HashMap<Integer, String> WAREHOUSE_VOUCHER_TYPE_MAP = new HashMap<Integer, String>();

	// 入库单编码前缀
	public static final String WVN_PREFIX_TRANSFER = "DBRK";
	public static final String WVN_PREFIX_PURCHASE = "CGRK";
	public static final String WVN_PREFIX_VIRTUAL = "XNRK";
	public static final String WVN_PREFIX_INVENTORY_PROFIT = "PYRK";
	public static final String WVN_PREFIX_DEPOSIT = "DCRK";
	public static final String WVN_PREFIX_OTHER = "QTRK";
	public static HashMap<Integer, String> WAREHOUSE_VOUCHER_NUMBER_PREFIX_MAP = new HashMap<Integer, String>();

	// 审核状态
	public static final String REVIEW_STATUS_UNCOMMIT = "UNCOMMIT";
	public static final String REVIEW_STATUS_WAIT_APPROVE = "WAIT";
	public static final String REVIEW_STATUS_PASS = "PASS";
	public static final String REVIEW_STATUS_FAIL = "FAIL";
	public static List<StatusBean<String, String>> REVIEW_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> REVIEW_STATUS_MAP = new HashMap<String, String>();

	// 同步状态
	public static final String SYNC_STATUS_NOT_SYNCHORIZED = "NOT_SYNCHORIZED";
	public static final String SYNC_STATUS_SYNCHORIZED = "SYNCHORIZED";
	public static final String SYNC_STATUS_FAIL = "FAIL";
	public static final String SYNC_STATUS_SUCCESS = "SUCCESS";
	public static List<StatusBean<String, String>> SYNC_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> SYNC_STATUS_MAP = new HashMap<String, String>();

	// 进度状态
	public static final String WV_STATUS_EDIT = "EDIT";
	public static final String WV_STATUS_WAIT_APPROVE = "WAIT_APPROVE";
	public static final String WV_STATUS_APPROVED = "APPROVED";
	public static final String WV_STATUS_CLOSED = "CLOSED";
	public static List<StatusBean<String, String>> WV_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> WV_STATUS_MAP = new HashMap<String, String>();

	// 入库单操作
	public static final String WV_ACTION_COMMIT = "COMMIT";
	public static final String WV_ACTION_CANCEL_COMMIT = "CANCEL_COMMIT";
	public static final String WV_ACTION_APPROVE = "APPROVE";
	public static final String WV_ACTION_REJECT = "REJECT";
	public static final String WV_ACTION_CLOSE = "CLOSE";

	static {
		initWarehouseVoucherTypeList();
		initWarehouseVoucherTypeMap();

		initWarehouseVoucherNumberPrefixMap();

		initReviewStatusList();
		initReviewStatusMap();

		initSyncStatusList();
		initSyncStatusMap();

		initWvStatusList();
		initWvStatusMap();
	}

	private static void initWarehouseVoucherTypeList() {
		WAREHOUSE_VOUCHER_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
		WAREHOUSE_VOUCHER_TYPE_LIST.add(new StatusBean<Integer, String>(WVT_TRANSFER, "调拨入库"));
		WAREHOUSE_VOUCHER_TYPE_LIST.add(new StatusBean<Integer, String>(WVT_PURCHASE, "采购入库"));
		WAREHOUSE_VOUCHER_TYPE_LIST.add(new StatusBean<Integer, String>(WVT_VIRTUAL, "虚拟入库"));
		WAREHOUSE_VOUCHER_TYPE_LIST
				.add(new StatusBean<Integer, String>(WVT_INVENTORY_PROFIT, "盘盈入库"));
		WAREHOUSE_VOUCHER_TYPE_LIST.add(new StatusBean<Integer, String>(WVT_DEPOSIT, "代储入库"));
		WAREHOUSE_VOUCHER_TYPE_LIST.add(new StatusBean<Integer, String>(WVT_OTHER, "其他入库"));
	}

	private static void initWarehouseVoucherTypeMap() {
		WAREHOUSE_VOUCHER_TYPE_MAP = StatusKit.toMap(WAREHOUSE_VOUCHER_TYPE_LIST);
	}

	private static void initWarehouseVoucherNumberPrefixMap() {
		WAREHOUSE_VOUCHER_NUMBER_PREFIX_MAP.put(WVT_TRANSFER,
				WVN_PREFIX_TRANSFER);
		WAREHOUSE_VOUCHER_NUMBER_PREFIX_MAP.put(WVT_PURCHASE,
				WVN_PREFIX_PURCHASE);
		WAREHOUSE_VOUCHER_NUMBER_PREFIX_MAP
				.put(WVT_VIRTUAL, WVN_PREFIX_VIRTUAL);
		WAREHOUSE_VOUCHER_NUMBER_PREFIX_MAP.put(WVT_INVENTORY_PROFIT,
				WVN_PREFIX_INVENTORY_PROFIT);
		WAREHOUSE_VOUCHER_NUMBER_PREFIX_MAP
				.put(WVT_DEPOSIT, WVN_PREFIX_DEPOSIT);
		WAREHOUSE_VOUCHER_NUMBER_PREFIX_MAP.put(WVT_OTHER, WVN_PREFIX_OTHER);
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

	private static void initWvStatusList() {
		WV_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
		WV_STATUS_LIST.add(new StatusBean<String, String>(WV_STATUS_EDIT, "编辑"));
		WV_STATUS_LIST.add(new StatusBean<String, String>(WV_STATUS_WAIT_APPROVE, "待审批"));
		WV_STATUS_LIST.add(new StatusBean<String, String>(WV_STATUS_APPROVED, "审批通过"));
		WV_STATUS_LIST.add(new StatusBean<String, String>(WV_STATUS_CLOSED, "已关闭"));
	}

	private static void initWvStatusMap() {
		WV_STATUS_MAP = StatusKit.toMap(WV_STATUS_LIST);
	}

}
