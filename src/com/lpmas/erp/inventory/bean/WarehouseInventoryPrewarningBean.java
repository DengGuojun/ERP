package com.lpmas.erp.inventory.bean;

import java.sql.Timestamp;

public class WarehouseInventoryPrewarningBean {
	private int wareType = 0;
	private int wareId = 0;
	private int prewarningType = 0;
	private String prewarningContent = "";
	private int isAutoModify = 0;
	private int status = 0;
	private Timestamp createTime = null;
	private int createUser = 0;
	private Timestamp modifyTime = null;
	private int modifyUser = 0;
	private String memo = "";

	public int getWareType() {
		return wareType;
	}

	public void setWareType(int wareType) {
		this.wareType = wareType;
	}

	public int getWareId() {
		return wareId;
	}

	public void setWareId(int wareId) {
		this.wareId = wareId;
	}

	public int getPrewarningType() {
		return prewarningType;
	}

	public void setPrewarningType(int prewarningType) {
		this.prewarningType = prewarningType;
	}

	public String getPrewarningContent() {
		return prewarningContent;
	}

	public void setPrewarningContent(String prewarningContent) {
		this.prewarningContent = prewarningContent;
	}

	public int getIsAutoModify() {
		return isAutoModify;
	}

	public void setIsAutoModify(int isAutoModify) {
		this.isAutoModify = isAutoModify;
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