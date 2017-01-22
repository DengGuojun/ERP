package com.lpmas.erp.purchase.bean;

import java.sql.Timestamp;

import com.lpmas.framework.annotation.FieldTag;

public class PurchaseOrderItemBean {
	@FieldTag(name = "明细ID")
	private int itemId = 0;
	@FieldTag(name = "采购订单ID")
	private int poId = 0;
	@FieldTag(name = "制品类型")
	private int wareType = 0;
	@FieldTag(name = "采购制品ID")
	private int wareId = 0;
	@FieldTag(name = "采购物品编号")
	private String wareNumber = "";
	@FieldTag(name = "币种")
	private String currency = "";
	@FieldTag(name = "单位")
	private String unit = "";
	@FieldTag(name = "采购数量")
	private double quatity = 0;
	@FieldTag(name = "采购单价")
	private double unitPrice = 0;
	@FieldTag(name = "采购总价")
	private double totalAmount = 0;
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

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getPoId() {
		return poId;
	}

	public void setPoId(int poId) {
		this.poId = poId;
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

	public String getWareNumber() {
		return wareNumber;
	}

	public void setWareNumber(String wareNumber) {
		this.wareNumber = wareNumber;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getQuatity() {
		return quatity;
	}

	public void setQuatity(double quatity) {
		this.quatity = quatity;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
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