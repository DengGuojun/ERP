package com.lpmas.erp.bom.bean;

import java.sql.Timestamp;

import com.lpmas.framework.annotation.FieldTag;

public class BomItemBean {
	@FieldTag(name = "Bom明细ID")
	private int bomItemId = 0;
	@FieldTag(name = "BomID")
	private int bomId = 0;
	@FieldTag(name = "使用类型")
	private int usageType = 0;
	@FieldTag(name = "制品类型")
	private int wareType = 0;
	@FieldTag(name = "制品ID")
	private int wareId = 0;
	@FieldTag(name = "单位")
	private String unit = "";
	@FieldTag(name = "数量")
	private double quantity = 0;
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

	public int getBomItemId() {
		return bomItemId;
	}

	public void setBomItemId(int bomItemId) {
		this.bomItemId = bomItemId;
	}

	public int getBomId() {
		return bomId;
	}

	public void setBomId(int bomId) {
		this.bomId = bomId;
	}

	public int getUsageType() {
		return usageType;
	}

	public void setUsageType(int usageType) {
		this.usageType = usageType;
	}

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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
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
