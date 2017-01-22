package com.lpmas.erp.purchase.bean;

import java.sql.Timestamp;

import com.lpmas.erp.contract.bean.PurchaseContractInfoBean;
import com.lpmas.erp.purchase.config.PurchaseOrderConfig;
import com.lpmas.srm.bean.SupplierInfoBean;
import com.lpmas.erp.warehouse.bean.WarehouseInfoBean;

public class PurchaseOrderInfoEntityBean {

	private PurchaseOrderInfoBean purchaseOrderInfoBean;
	private PurchaseContractInfoBean purchaseContractInfoBean;
	private SupplierInfoBean supplierInfoBean;
	private SupplierInfoBean receiverInfoBean;// 当采购物品为物料时起效
	private WarehouseInfoBean warehouseInfoBean;// 当采购物品为商品，且采购类型为一般采购时起效

	public PurchaseOrderInfoEntityBean(PurchaseOrderInfoBean purchaseOrderInfoBean) {
		super();
		this.purchaseOrderInfoBean = purchaseOrderInfoBean;
	}

	public PurchaseOrderInfoEntityBean(PurchaseOrderInfoBean purchaseOrderInfoBean, PurchaseContractInfoBean purchaseContractInfoBean,
			SupplierInfoBean supplierInfoBean, SupplierInfoBean receiverInfoBean, WarehouseInfoBean warehouseInfoBean) {
		super();
		this.purchaseOrderInfoBean = purchaseOrderInfoBean;
		this.purchaseContractInfoBean = purchaseContractInfoBean;
		this.supplierInfoBean = supplierInfoBean;
		this.receiverInfoBean = receiverInfoBean;
		this.warehouseInfoBean = warehouseInfoBean;
	}

	public PurchaseOrderInfoBean getPurchaseOrderInfoBean() {
		return purchaseOrderInfoBean;
	}

	public void setPurchaseOrderInfoBean(PurchaseOrderInfoBean purchaseOrderInfoBean) {
		this.purchaseOrderInfoBean = purchaseOrderInfoBean;
	}

	public PurchaseContractInfoBean getPurchaseContractInfoBean() {
		return purchaseContractInfoBean;
	}

	public void setPurchaseContractInfoBean(PurchaseContractInfoBean purchaseContractInfoBean) {
		this.purchaseContractInfoBean = purchaseContractInfoBean;
	}

	public SupplierInfoBean getReceiverInfoBean() {
		return receiverInfoBean;
	}

	public void setReceiverInfoBean(SupplierInfoBean receiverInfoBean) {
		this.receiverInfoBean = receiverInfoBean;
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

	public int getPoId() {
		return purchaseOrderInfoBean.getPoId();
	}

	public String getPoNumber() {
		return purchaseOrderInfoBean.getPoNumber();
	}

	public int getWareType() {
		return purchaseOrderInfoBean.getWareType();
	}

	public int getPlanId() {
		return purchaseOrderInfoBean.getPlanId();
	}

	public int getPurchaseType() {
		return purchaseOrderInfoBean.getPurchaseType();
	}

	public int getContractId() {
		return purchaseOrderInfoBean.getContractId();
	}

	public int getSupplierId() {
		return purchaseOrderInfoBean.getSupplierId();
	}

	public String getConsignerName() {
		return purchaseOrderInfoBean.getConsignerName();
	}

	public String getConsignerCountry() {
		return purchaseOrderInfoBean.getConsignerCountry();
	}

	public String getConsignerProvince() {
		return purchaseOrderInfoBean.getConsignerProvince();
	}

	public String getConsignerCity() {
		return purchaseOrderInfoBean.getConsignerCity();
	}

	public String getConsignerRegion() {
		return purchaseOrderInfoBean.getConsignerRegion();
	}

	public String getConsignerAddress() {
		return purchaseOrderInfoBean.getConsignerAddress();
	}

	public String getConsignerTelephone() {
		return purchaseOrderInfoBean.getConsignerTelephone();
	}

	public String getConsignerMobile() {
		return purchaseOrderInfoBean.getConsignerMobile();
	}

	public int getReceiverType() {
		return purchaseOrderInfoBean.getReceiverType();
	}

	public int getReceiverId() {
		return purchaseOrderInfoBean.getReceiverId();
	}

	public String getReceiverName() {
		return purchaseOrderInfoBean.getReceiverName();
	}

	public String getReceiverCountry() {
		return purchaseOrderInfoBean.getReceiverCountry();
	}

	public String getReceiverProvince() {
		return purchaseOrderInfoBean.getReceiverProvince();
	}

	public String getReceiverCity() {
		return purchaseOrderInfoBean.getReceiverCity();
	}

	public String getReceiverRegion() {
		return purchaseOrderInfoBean.getReceiverRegion();
	}

	public String getReceiverAddress() {
		return purchaseOrderInfoBean.getReceiverAddress();
	}

	public String getReceiverTelephone() {
		return purchaseOrderInfoBean.getReceiverTelephone();
	}

	public String getReceiverMobile() {
		return purchaseOrderInfoBean.getReceiverMobile();
	}

	public String getCurrency() {
		return purchaseOrderInfoBean.getCurrency();
	}

	public double getPoAmount() {
		return purchaseOrderInfoBean.getPoAmount();
	}

	public String getContractStatus() {
		return purchaseOrderInfoBean.getContractStatus();
	}

	public String getInvoiceStatus() {
		return purchaseOrderInfoBean.getInvoiceStatus();
	}

	public String getPaymentStatus() {
		return purchaseOrderInfoBean.getPaymentStatus();
	}

	public String getPoStatus() {
		return purchaseOrderInfoBean.getPoStatus();
	}
	
	public String getApprovalStatus(){
		return purchaseOrderInfoBean.getApprovalStatus();
	}

	public int getStatus() {
		return purchaseOrderInfoBean.getStatus();
	}

	public Timestamp getCreateTime() {
		return purchaseOrderInfoBean.getCreateTime();
	}

	public int getCreateUser() {
		return purchaseOrderInfoBean.getCreateUser();
	}

	public Timestamp getModifyTime() {
		return purchaseOrderInfoBean.getModifyTime();
	}

	public int getModifyUser() {
		return purchaseOrderInfoBean.getModifyUser();
	}

	public String getMemo() {
		return purchaseOrderInfoBean.getMemo();
	}

	public String getCompleteConsignerAddress() {
		return purchaseOrderInfoBean.getCompleteConsignerAddress();
	}

	public String getCompleteReceiverAddress() {
		return purchaseOrderInfoBean.getCompleteReceiverAddress();
	}

	public String getPcName() {
		String pcName = "";
		if (purchaseContractInfoBean != null) {
			pcName = purchaseContractInfoBean.getPcName();
		}
		return pcName;
	}

	public String getSupplier() {
		String supplier = "";
		if (supplierInfoBean != null) {
			supplier = supplierInfoBean.getSupplierName();
		}
		return supplier;
	}

	public String getReceiver() {
		String receiver = "";
		if (purchaseOrderInfoBean.getReceiverType() == PurchaseOrderConfig.RECEIVER_TYPE_WAREHOUSE
				&& warehouseInfoBean != null) {
			receiver = warehouseInfoBean.getWarehouseName();
		} else if (purchaseOrderInfoBean.getReceiverType() == PurchaseOrderConfig.RECEIVER_TYPE_FACTORY
				&& receiverInfoBean != null) {
			receiver = receiverInfoBean.getSupplierName();
		}
		return receiver;
	}

}
