package com.lpmas.erp.inventory.bean;

import java.sql.Timestamp;

import com.lpmas.pdm.business.WareInfoMediator;

public class WarehouseTransferOrderItemEntityBean {
	private WarehouseTransferOrderItemBean warehouseTransferOrderItemBean;
	private WarehouseInventoryBean warehouseInventoryBean;
	WareInfoMediator wareInfoMediator = new WareInfoMediator();

	public WarehouseTransferOrderItemEntityBean(WarehouseTransferOrderItemBean warehouseTransferOrderItemBean,
			WarehouseInventoryBean warehouseInventoryBean) {
		super();
		this.warehouseTransferOrderItemBean = warehouseTransferOrderItemBean;
		this.warehouseInventoryBean = warehouseInventoryBean;
	}

	public WarehouseTransferOrderItemBean getWarehouseTransferOrderItemBean() {
		return warehouseTransferOrderItemBean;
	}

	public void setWarehouseTransferOrderItemBean(WarehouseTransferOrderItemBean warehouseTransferOrderItemBean) {
		this.warehouseTransferOrderItemBean = warehouseTransferOrderItemBean;
	}

	public WarehouseInventoryBean getWarehouseInventoryBean() {
		return warehouseInventoryBean;
	}

	public void setWarehouseInventoryBean(WarehouseInventoryBean warehouseInventoryBean) {
		this.warehouseInventoryBean = warehouseInventoryBean;
	}

	public String getWareName() {
		return wareInfoMediator.getWareNameByKey(warehouseTransferOrderItemBean.getWareType(), warehouseTransferOrderItemBean.getWareId());
	}

	public String getWareNumber() {
		return wareInfoMediator.getWareNumberByKey(warehouseTransferOrderItemBean.getWareType(), warehouseTransferOrderItemBean.getWareId());
	}

	public String getSpecification() {
		return wareInfoMediator.getWareSpecificationByKey(warehouseTransferOrderItemBean.getWareType(), warehouseTransferOrderItemBean.getWareId());
	}

	public String getUnitName() {
		return wareInfoMediator.getWareUnitNameByKey(warehouseTransferOrderItemBean.getWareType(), warehouseTransferOrderItemBean.getWareId());
	}

	public int getWiId() {
		return warehouseInventoryBean.getWiId();
	}

	public double getQuantity() {
		return warehouseInventoryBean.getQuantity();
	}

	public int getToItemId() {
		return warehouseTransferOrderItemBean.getToItemId();
	}

	public int getToId() {
		return warehouseTransferOrderItemBean.getToId();
	}

	public int getWareType() {
		return warehouseTransferOrderItemBean.getWareType();
	}

	public int getWareId() {
		return warehouseTransferOrderItemBean.getWareId();
	}

	public String getUnit() {
		return warehouseTransferOrderItemBean.getUnit();
	}

	public double getQuanlity() {
		return warehouseTransferOrderItemBean.getQuanlity();
	}

	public String getBatchNumber() {
		return warehouseTransferOrderItemBean.getBatchNumber();
	}

	public Timestamp getProductionDate() {
		return warehouseTransferOrderItemBean.getProductionDate();
	}

	public double getGuaranteePeriod() {
		return warehouseTransferOrderItemBean.getGuaranteePeriod();
	}

	public Timestamp getExpirationDate() {
		return warehouseTransferOrderItemBean.getExpirationDate();
	}

	public String getMemo() {
		return warehouseTransferOrderItemBean.getMemo();
	}

	public String getItemValue() {
		return "itemId=" + String.valueOf(warehouseTransferOrderItemBean.getToItemId()) + ",wareId="
				+ String.valueOf(warehouseTransferOrderItemBean.getWareId()) + ",wareType="
				+ String.valueOf(warehouseTransferOrderItemBean.getWareType()) + ",unit=" + warehouseTransferOrderItemBean.getUnit() + ",quatity="
				+ String.valueOf(warehouseTransferOrderItemBean.getQuanlity()) + ",productionDate="
				+ String.valueOf(warehouseTransferOrderItemBean.getProductionDate()) + ",guaranteePeriod="
				+ String.valueOf(warehouseTransferOrderItemBean.getGuaranteePeriod()) + ",expirationDate="
				+ String.valueOf(warehouseTransferOrderItemBean.getExpirationDate()) + ",memo=" + warehouseTransferOrderItemBean.getMemo()
				+ ",batchNumber=" + warehouseTransferOrderItemBean.getBatchNumber();
	}
}
