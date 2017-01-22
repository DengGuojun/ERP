package com.lpmas.erp.report.bean;

public class WarehouseInventoryLogSumResultBean {

	private int changeType = 0;
	private double sumQuantity = 0;

	public int getChangeType() {
		return changeType;
	}

	public void setChangeType(int changeType) {
		this.changeType = changeType;
	}

	public double getSumQuantity() {
		return sumQuantity;
	}

	public void setSumQuantity(double sumQuantity) {
		this.sumQuantity = sumQuantity;
	}

}
