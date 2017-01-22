package com.lpmas.erp.inventory.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.util.StatusKit;

public class WarehouseTransferOrderConfig {

	// 调拨单编码前缀
	public static final String TO_NUMBER_PREFIX = "KCDB";

	// 审核状态
	public static final String APPROVE_STATUS_UNCOMMIT = "UNCOMMIT";
	public static final String APPROVE_STATUS_WAIT_APPROVE = "WAIT";
	public static final String APPROVE_STATUS_PASS = "PASS";
	public static final String APPROVE_STATUS_FAIL = "FAIL";
	public static List<StatusBean<String, String>> APPROVE_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> APPROVE_STATUS_MAP = new HashMap<String, String>();

	// 进度状态
	public static final String TO_STATUS_EDIT = "EDIT";
	public static final String TO_STATUS_WAIT_APPROVE = "WAIT_APPROVE";
	public static final String TO_STATUS_APPROVED = "APPROVED";
	public static final String TO_STATUS_IN_TRANSIT = "IN_TRANSIT";
	public static final String TO_STATUS_CLOSED = "CLOSED";
	public static List<StatusBean<String, String>> TO_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
	public static HashMap<String, String> TO_STATUS_MAP = new HashMap<String, String>();

	// 入库单操作
	public static final String TO_ACTION_COMMIT = "COMMIT";
	public static final String TO_ACTION_CANCEL_COMMIT = "CANCEL_COMMIT";
	public static final String TO_ACTION_APPROVE = "APPROVE";
	public static final String TO_ACTION_REJECT = "REJECT";
	public static final String TO_ACTION_CLOSE = "CLOSE";

	static {
		initReviewStatusList();
		initReviewStatusMap();

		initToStatusList();
		initToStatusMap();
	}

	private static void initReviewStatusList() {
		APPROVE_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
		APPROVE_STATUS_LIST.add(new StatusBean<String, String>(APPROVE_STATUS_UNCOMMIT, "未提交审批"));
		APPROVE_STATUS_LIST.add(new StatusBean<String, String>(APPROVE_STATUS_WAIT_APPROVE, "待审批"));
		APPROVE_STATUS_LIST.add(new StatusBean<String, String>(APPROVE_STATUS_FAIL, "审批不通过"));
		APPROVE_STATUS_LIST.add(new StatusBean<String, String>(APPROVE_STATUS_PASS, "审批通过"));
	}

	private static void initReviewStatusMap() {
		APPROVE_STATUS_MAP = StatusKit.toMap(APPROVE_STATUS_LIST);
	}

	private static void initToStatusList() {
		TO_STATUS_LIST = new ArrayList<StatusBean<String, String>>();
		TO_STATUS_LIST.add(new StatusBean<String, String>(TO_STATUS_EDIT, "编辑"));
		TO_STATUS_LIST.add(new StatusBean<String, String>(TO_STATUS_WAIT_APPROVE, "待审批"));
		TO_STATUS_LIST.add(new StatusBean<String, String>(TO_STATUS_APPROVED, "审批通过"));
		TO_STATUS_LIST.add(new StatusBean<String, String>(TO_STATUS_IN_TRANSIT, "在途"));
		TO_STATUS_LIST.add(new StatusBean<String, String>(TO_STATUS_CLOSED, "已关闭"));
	}

	private static void initToStatusMap() {
		TO_STATUS_MAP = StatusKit.toMap(TO_STATUS_LIST);
	}
}
