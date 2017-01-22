package com.lpmas.erp.purchase.bean;

import java.sql.Timestamp;

import com.lpmas.framework.annotation.FieldTag;

public class DeliveryNoteInfoBean {
	@FieldTag(name = "发货单ID")
	private int dnId = 0;
	@FieldTag(name = "发货单号")
	private String dnNumber = "";
	@FieldTag(name = "采购单ID")
	private int poId = 0;
	@FieldTag(name = "发货时间")
	private Timestamp deliveryTime = null;
	@FieldTag(name = "收货时间")
	private Timestamp receivingTime = null;
	@FieldTag(name = "配送方类型")
	private int transporterType = 0;
	@FieldTag(name = "配送方ID")
	private int transporterId = 0;
	@FieldTag(name = "配送单号")
	private String transportNumber = "";
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

	public int getDnId() {
		return dnId;
	}

	public void setDnId(int dnId) {
		this.dnId = dnId;
	}

	public String getDnNumber() {
		return dnNumber;
	}

	public void setDnNumber(String dnNumber) {
		this.dnNumber = dnNumber;
	}

	public int getPoId() {
		return poId;
	}

	public void setPoId(int poId) {
		this.poId = poId;
	}

	public Timestamp getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Timestamp deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Timestamp getReceivingTime() {
		return receivingTime;
	}

	public void setReceivingTime(Timestamp receivingTime) {
		this.receivingTime = receivingTime;
	}

	public int getTransporterType() {
		return transporterType;
	}

	public void setTransporterType(int transporterType) {
		this.transporterType = transporterType;
	}

	public int getTransporterId() {
		return transporterId;
	}

	public void setTransporterId(int transporterId) {
		this.transporterId = transporterId;
	}

	public String getTransportNumber() {
		return transportNumber;
	}

	public void setTransportNumber(String transportNumber) {
		this.transportNumber = transportNumber;
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