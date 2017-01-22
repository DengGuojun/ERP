package com.lpmas.erp.inventory.bean;

import java.sql.Timestamp;
import java.util.List;

public class WarehouseInventoryReportBean extends WarehouseInventoryAggregateReportBean {

	private String batchNumber = "";
	private Timestamp productionDate = null;
	private double guaranteePeriod = 0;
	private Timestamp expirationDate = null;
	private String unit = "";
	private List<Integer> pendingShelfWvIdList;

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public double getGuaranteePeriod() {
		return guaranteePeriod;
	}

	public void setGuaranteePeriod(double guaranteePeriod) {
		this.guaranteePeriod = guaranteePeriod;
	}

	public Timestamp getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Timestamp productionDate) {
		this.productionDate = productionDate;
	}

	public Timestamp getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Timestamp expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public List<Integer> getPendingShelfWvIdList() {
		return pendingShelfWvIdList;
	}

	public void setPendingShelfWvIdList(List<Integer> pendingShelfWvIdList) {
		this.pendingShelfWvIdList = pendingShelfWvIdList;
	}

}
