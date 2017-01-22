package com.lpmas.erp.bom.bean;

import java.sql.Timestamp;

import com.lpmas.framework.annotation.FieldTag;

public class BomInfoBean {
	@FieldTag(name = "BomID")
	private int bomId = 0;
	@FieldTag(name = "Bom名称")
	private String bomName = "";
	@FieldTag(name = "Bom编号")
	private String bomNumber = "";
	@FieldTag(name = "Bom类型")
	private int bomType = 0;
	@FieldTag(name = "是否激活")
	private int isActive = 0;
	@FieldTag(name = "起始有效时间")
	private Timestamp useStartTime = null;
	@FieldTag(name = "终止有效时间")
	private Timestamp useEndTime = null;
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

	public int getBomId() {
		return bomId;
	}

	public void setBomId(int bomId) {
		this.bomId = bomId;
	}

	public String getBomName() {
		return bomName;
	}

	public void setBomName(String bomName) {
		this.bomName = bomName;
	}

	public String getBomNumber() {
		return bomNumber;
	}

	public void setBomNumber(String bomNumber) {
		this.bomNumber = bomNumber;
	}

	public int getBomType() {
		return bomType;
	}

	public void setBomType(int bomType) {
		this.bomType = bomType;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public Timestamp getUseStartTime() {
		return useStartTime;
	}

	public void setUseStartTime(Timestamp useStartTime) {
		this.useStartTime = useStartTime;
	}

	public Timestamp getUseEndTime() {
		return useEndTime;
	}

	public void setUseEndTime(Timestamp useEndTime) {
		this.useEndTime = useEndTime;
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
