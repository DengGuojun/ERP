package com.lpmas.erp.inventory.bean;

import java.sql.Timestamp;

import com.lpmas.framework.annotation.FieldTag;

public class WarehouseTransferOrderInfoBean {
	@FieldTag(name = "调拨单ID")
	private int toId = 0;
	@FieldTag(name = "调拨单编号")
	private String toNumber = "";
	@FieldTag(name = "调出仓库ID")
	private int sourceWarehouseId = 0;
	@FieldTag(name = "调入仓库ID")
	private int targetWarehouseId = 0;
	@FieldTag(name = "制品类型")
	private int wareType = 0;
	@FieldTag(name = "出库质检人")
	private String dvInspectorName = "";
	@FieldTag(name = "仓库出货人")
	private String dvSenderName = "";
	@FieldTag(name = "出货记账人")
	private String dvAccountClerkName = "";
	@FieldTag(name = "入库质检人")
	private String wvInspectorName = "";
	@FieldTag(name = "仓库收货人")
	private String wvReceiverName = "";
	@FieldTag(name = "入库记账人")
	private String wvAccountClerkName = "";
	@FieldTag(name = "审核状态")
	private String approveStatus = "";
	@FieldTag(name = "同步状态")
	private String syncStatus = "";
	@FieldTag(name = "调拨单状态")
	private String toStatus = "";
	@FieldTag(name = "状态")
	private int status = 0;
	@FieldTag(name = "创建时间")
	private Timestamp createTime = null;
	@FieldTag(name = "创建用户")
	private int createUser = 0;
	@FieldTag(name = "修改时间")
	private Timestamp modifyTime = null;
	@FieldTag(name = "修改用户")
	private int modifyUser = 0;
	@FieldTag(name = "备注")
	private String memo = "";

	public int getToId() {
		return toId;
	}

	public void setToId(int toId) {
		this.toId = toId;
	}

	public String getToNumber() {
		return toNumber;
	}

	public void setToNumber(String toNumber) {
		this.toNumber = toNumber;
	}

	public int getSourceWarehouseId() {
		return sourceWarehouseId;
	}

	public void setSourceWarehouseId(int sourceWarehouseId) {
		this.sourceWarehouseId = sourceWarehouseId;
	}

	public int getTargetWarehouseId() {
		return targetWarehouseId;
	}

	public void setTargetWarehouseId(int targetWarehouseId) {
		this.targetWarehouseId = targetWarehouseId;
	}

	public int getWareType() {
		return wareType;
	}

	public void setWareType(int wareType) {
		this.wareType = wareType;
	}

	public String getDvInspectorName() {
		return dvInspectorName;
	}

	public void setDvInspectorName(String dvInspectorName) {
		this.dvInspectorName = dvInspectorName;
	}

	public String getDvSenderName() {
		return dvSenderName;
	}

	public void setDvSenderName(String dvSenderName) {
		this.dvSenderName = dvSenderName;
	}

	public String getDvAccountClerkName() {
		return dvAccountClerkName;
	}

	public void setDvAccountClerkName(String dvAccountClerkName) {
		this.dvAccountClerkName = dvAccountClerkName;
	}

	public String getWvInspectorName() {
		return wvInspectorName;
	}

	public void setWvInspectorName(String wvInspectorName) {
		this.wvInspectorName = wvInspectorName;
	}

	public String getWvReceiverName() {
		return wvReceiverName;
	}

	public void setWvReceiverName(String wvReceiverName) {
		this.wvReceiverName = wvReceiverName;
	}

	public String getWvAccountClerkName() {
		return wvAccountClerkName;
	}

	public void setWvAccountClerkName(String wvAccountClerkName) {
		this.wvAccountClerkName = wvAccountClerkName;
	}

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(String syncStatus) {
		this.syncStatus = syncStatus;
	}

	public String getToStatus() {
		return toStatus;
	}

	public void setToStatus(String toStatus) {
		this.toStatus = toStatus;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public int getCreateUser() {
		return createUser;
	}

	public void setCreateUser(int createUser) {
		this.createUser = createUser;
	}

	public Timestamp getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Timestamp modifyTime) {
		this.modifyTime = modifyTime;
	}

	public int getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(int modifyUser) {
		this.modifyUser = modifyUser;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}