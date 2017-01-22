package com.lpmas.erp.report.bean;

import java.sql.Timestamp;

import com.lpmas.framework.nosql.mongodb.MongoDBDocumentBean;

public class MonthlyInventoryReportBean extends MongoDBDocumentBean<String> {

	private int wareId = 0;
	private int wareType = 0;
	private int warehouseId = 0;
	private int inventoryType = 0;
	private double quantity = 0;
	private String unit = "";
	private String reportMonth = "";// yyyyMM
	private Timestamp createTime = null;

	public int getWareId() {
		return wareId;
	}

	public void setWareId(int wareId) {
		this.wareId = wareId;
	}

	public int getWareType() {
		return wareType;
	}

	public void setWareType(int wareType) {
		this.wareType = wareType;
	}

	public int getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(int warehouseId) {
		this.warehouseId = warehouseId;
	}

	public int getInventoryType() {
		return inventoryType;
	}

	public void setInventoryType(int inventoryType) {
		this.inventoryType = inventoryType;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getReportMonth() {
		return reportMonth;
	}

	public void setReportMonth(String reportMonth) {
		this.reportMonth = reportMonth;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

}
