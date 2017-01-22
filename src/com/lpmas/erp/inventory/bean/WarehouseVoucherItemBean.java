package com.lpmas.erp.inventory.bean;

import java.sql.Timestamp;

import com.lpmas.framework.annotation.FieldTag;

public class WarehouseVoucherItemBean {
	@FieldTag(name = "明细ID")
	private int wvItemId = 0;
	@FieldTag(name = "入库单ID")
	private int wvId = 0;
	@FieldTag(name = "制品类型")
	private int wareType = 0;
	@FieldTag(name = "制品ID")
	private int wareId = 0;
	@FieldTag(name = "单位")
	private String unit = "";
	@FieldTag(name = "应收数量")
	private double receivableQuantity = 0;
	@FieldTag(name = "实收数量")
	private double stockInQuantity = 0;
	@FieldTag(name = "残次品数量")
	private double defectQuantity = 0;
	@FieldTag(name = "批次号")
	private String batchNumber = "";
	@FieldTag(name = "生产日期")
	private Timestamp productionDate = null;
	@FieldTag(name = "保质期，单位：天")
	private double guaranteePeriod = 0;
	@FieldTag(name = "有效日期")
	private Timestamp expirationDate = null;
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

	public int getWvItemId() {
		return wvItemId;
	}

	public void setWvItemId(int wvItemId) {
		this.wvItemId = wvItemId;
	}

	public int getWvId() {
		return wvId;
	}

	public void setWvId(int wvId) {
		this.wvId = wvId;
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

	public double getReceivableQuantity() {
		return receivableQuantity;
	}

	public void setReceivableQuantity(double receivableQuantity) {
		this.receivableQuantity = receivableQuantity;
	}

	public double getStockInQuantity() {
		return stockInQuantity;
	}

	public void setStockInQuantity(double stockInQuantity) {
		this.stockInQuantity = stockInQuantity;
	}

	public double getDefectQuantity() {
		return defectQuantity;
	}

	public void setDefectQuantity(double defectQuantity) {
		this.defectQuantity = defectQuantity;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Timestamp getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Timestamp productionDate) {
		this.productionDate = productionDate;
	}

	public double getGuaranteePeriod() {
		return guaranteePeriod;
	}

	public void setGuaranteePeriod(double guaranteePeriod) {
		this.guaranteePeriod = guaranteePeriod;
	}

	public Timestamp getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Timestamp expirationDate) {
		this.expirationDate = expirationDate;
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