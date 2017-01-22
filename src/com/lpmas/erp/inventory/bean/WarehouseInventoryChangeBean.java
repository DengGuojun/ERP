package com.lpmas.erp.inventory.bean;

import java.sql.Timestamp;
import com.lpmas.framework.annotation.FieldTag;

public class WarehouseInventoryChangeBean {
	@FieldTag(name = "转换单ID")
	private int changeId = 0;
	@FieldTag(name = "库存ID1")
	private int wiId1 = 0;
	@FieldTag(name = "库存ID2")
	private int wiId2 = 0;
	@FieldTag(name = "转换类型")
	private int changeType = 0;
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

	public int getChangeId() {
		return changeId;
	}

	public void setChangeId(int changeId) {
		this.changeId = changeId;
	}

	public int getWiId1() {
		return wiId1;
	}

	public void setWiId1(int wiId1) {
		this.wiId1 = wiId1;
	}

	public int getWiId2() {
		return wiId2;
	}

	public void setWiId2(int wiId2) {
		this.wiId2 = wiId2;
	}

	public int getChangeType() {
		return changeType;
	}

	public void setChangeType(int changeType) {
		this.changeType = changeType;
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