package com.lpmas.erp.purchase.bean;

import java.sql.Timestamp;

import com.lpmas.framework.annotation.FieldTag;

public class ReceivingNoteInfoBean {
	@FieldTag(name = "收货单ID")
	private int rnId = 0;
	@FieldTag(name = "收货单编号")
	private String rnNumber = "";
	@FieldTag(name = "发货单ID")
	private int dnId = 0;
	@FieldTag(name = "采购单ID")
	private int poId = 0;
	@FieldTag(name = "收货时间")
	private Timestamp receivingTime = null;
	@FieldTag(name = "收货状态")
	private String receivingStatus = "";
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

	public int getRnId() {
		return rnId;
	}

	public void setRnId(int rnId) {
		this.rnId = rnId;
	}

	public String getRnNumber() {
		return rnNumber;
	}

	public void setRnNumber(String rnNumber) {
		this.rnNumber = rnNumber;
	}

	public int getDnId() {
		return dnId;
	}

	public void setDnId(int dnId) {
		this.dnId = dnId;
	}

	public int getPoId() {
		return poId;
	}

	public void setPoId(int poId) {
		this.poId = poId;
	}

	public Timestamp getReceivingTime() {
		return receivingTime;
	}

	public void setReceivingTime(Timestamp receivingTime) {
		this.receivingTime = receivingTime;
	}

	public String getReceivingStatus() {
		return receivingStatus;
	}

	public void setReceivingStatus(String receivingStatus) {
		this.receivingStatus = receivingStatus;
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