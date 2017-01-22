package com.lpmas.erp.inventory.bean;

import java.sql.Timestamp;

import com.lpmas.erp.inventory.config.SourceOrderTypeConfig;
import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.warehouse.bean.WarehouseInfoBean;
import com.lpmas.srm.bean.SupplierInfoBean;

public class WarehouseVoucherInfoEntityBean {

	private WarehouseVoucherInfoBean wareshoueVoucherInfoBean;
	private PurchaseOrderInfoBean purchaseOrderInfoBean;
	private SupplierInfoBean supplierInfoBean;
	private WarehouseInfoBean warehouseInfoBean;

	public WarehouseVoucherInfoEntityBean(WarehouseVoucherInfoBean wareshoueVoucherInfoBean,
			PurchaseOrderInfoBean purchaseOrderInfoBean, SupplierInfoBean supplierInfoBean,
			WarehouseInfoBean warehouseInfoBean) {
		super();
		this.wareshoueVoucherInfoBean = wareshoueVoucherInfoBean;
		this.purchaseOrderInfoBean = purchaseOrderInfoBean;
		this.supplierInfoBean = supplierInfoBean;
		this.warehouseInfoBean = warehouseInfoBean;
	}

	public WarehouseVoucherInfoBean getWareshoueVoucherInfoBean() {
		return wareshoueVoucherInfoBean;
	}

	public void setWareshoueVoucherInfoBean(WarehouseVoucherInfoBean wareshoueVoucherInfoBean) {
		this.wareshoueVoucherInfoBean = wareshoueVoucherInfoBean;
	}

	public PurchaseOrderInfoBean getPurchaseOrderInfoBean() {
		return purchaseOrderInfoBean;
	}

	public void setPurchaseOrderInfoBean(PurchaseOrderInfoBean purchaseOrderInfoBean) {
		this.purchaseOrderInfoBean = purchaseOrderInfoBean;
	}

	public SupplierInfoBean getSupplierInfoBean() {
		return supplierInfoBean;
	}

	public void setSupplierInfoBean(SupplierInfoBean supplierInfoBean) {
		this.supplierInfoBean = supplierInfoBean;
	}

	public WarehouseInfoBean getWarehouseInfoBean() {
		return warehouseInfoBean;
	}

	public void setWarehouseInfoBean(WarehouseInfoBean warehouseInfoBean) {
		this.warehouseInfoBean = warehouseInfoBean;
	}

	public int getWvId() {
		return wareshoueVoucherInfoBean.getWvId();
	}

	public String getWvNumber() {
		return wareshoueVoucherInfoBean.getWvNumber();
	}

	public int getWvType() {
		return wareshoueVoucherInfoBean.getWvType();
	}

	public int getSourceOrderType() {
		return wareshoueVoucherInfoBean.getSourceOrderType();
	}

	public int getSourceOrderId() {
		return wareshoueVoucherInfoBean.getSourceOrderId();
	}

	public int getSupplierId() {
		return wareshoueVoucherInfoBean.getSupplierId();
	}

	public int getWareType() {
		return wareshoueVoucherInfoBean.getWareType();
	}

	public Timestamp getStockInTime() {
		return wareshoueVoucherInfoBean.getStockInTime();
	}

	public int getWarehouseId() {
		return wareshoueVoucherInfoBean.getWarehouseId();
	}

	public String getInspectorName() {
		return wareshoueVoucherInfoBean.getInspectorName();
	}

	public String getReceiverName() {
		return wareshoueVoucherInfoBean.getReceiverName();
	}

	public String getAccountClerkName() {
		return wareshoueVoucherInfoBean.getAccountClerkName();
	}

	public String getApproveStatus() {
		return wareshoueVoucherInfoBean.getApproveStatus();
	}

	public String getSyncStatus() {
		return wareshoueVoucherInfoBean.getSyncStatus();
	}

	public String getWvStatus() {
		return wareshoueVoucherInfoBean.getWvStatus();
	}

	public int getStatus() {
		return wareshoueVoucherInfoBean.getStatus();
	}

	public Timestamp getCreateTime() {
		return wareshoueVoucherInfoBean.getCreateTime();
	}

	public int getCreateUser() {
		return wareshoueVoucherInfoBean.getCreateUser();
	}

	public Timestamp getModifyTime() {
		return wareshoueVoucherInfoBean.getModifyTime();
	}

	public int getModifyUser() {
		return wareshoueVoucherInfoBean.getModifyUser();
	}

	public String getMemo() {
		return wareshoueVoucherInfoBean.getMemo();
	}

	public String getSourceOrderNumber() {
		String sourceOrderNumber = "";
		if (wareshoueVoucherInfoBean
				.getSourceOrderType() == SourceOrderTypeConfig.SOURCE_ORDER_TYPE_PURCHASE_ORDER) {
			if (purchaseOrderInfoBean != null) {
				sourceOrderNumber = purchaseOrderInfoBean.getPoNumber();
			}
		}
		return sourceOrderNumber;
	}

	public String getSupplierName() {
		String supplierName = "";
		if (supplierInfoBean != null) {
			supplierName = supplierInfoBean.getSupplierName();
		}
		return supplierName;
	}

	public String getWarehouseName() {
		String warehouseName = "";
		if (warehouseInfoBean != null) {
			warehouseName = warehouseInfoBean.getWarehouseName();
		}
		return warehouseName;
	}

}
