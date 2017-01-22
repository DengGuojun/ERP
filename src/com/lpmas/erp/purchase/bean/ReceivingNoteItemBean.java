package com.lpmas.erp.purchase.bean;

import java.sql.Timestamp;

import com.lpmas.framework.annotation.FieldTag;

public class ReceivingNoteItemBean {
	@FieldTag(name = "收货单明细ID")
	private int rnItemId = 0;
	@FieldTag(name = "收货单ID")
	private int rnId = 0;
	@FieldTag(name = "发货单项ID")
	private int dnItemId = 0;
	@FieldTag(name = "收货数量")
	private double receiveQuantity = 0;
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

	public int getRnItemId() {
		return rnItemId;
	}

	public void setRnItemId(int rnItemId) {
		this.rnItemId = rnItemId;
	}

	public int getRnId() {
		return rnId;
	}

	public void setRnId(int rnId) {
		this.rnId = rnId;
	}

	public int getDnItemId() {
		return dnItemId;
	}

	public void setDnItemId(int dnItemId) {
		this.dnItemId = dnItemId;
	}

	public double getReceiveQuantity() {
		return receiveQuantity;
	}

	public void setReceiveQuantity(double receiveQuantity) {
		this.receiveQuantity = receiveQuantity;
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