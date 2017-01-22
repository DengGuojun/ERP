package com.lpmas.erp.inventory.bean;

import java.sql.Timestamp;

import com.lpmas.framework.nosql.mongodb.MongoDBDocumentBean;

public class WarehouseInventoryAggregateReportBean extends MongoDBDocumentBean<String> {

	private String _id;
	private int warehouseId = 0;
	private int wareId = 0;
	private int wareType = 0;
	private double pendingQuantity = 0;
	private double normalQuantity = 0;
	private double defectQuantity = 0;
	private double damageQuantity = 0;
	private int typeId = 0;
	private Timestamp modifyTime = null;
	private int modifyUser = 0;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public int getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(int warehouseId) {
		this.warehouseId = warehouseId;
	}

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

	public double getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(double pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}

	public double getNormalQuantity() {
		return normalQuantity;
	}

	public void setNormalQuantity(double normalQuantity) {
		this.normalQuantity = normalQuantity;
	}

	public double getDefectQuantity() {
		return defectQuantity;
	}

	public void setDefectQuantity(double defectQuantity) {
		this.defectQuantity = defectQuantity;
	}

	public double getDamageQuantity() {
		return damageQuantity;
	}

	public void setDamageQuantity(double damageQuantity) {
		this.damageQuantity = damageQuantity;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
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

}
