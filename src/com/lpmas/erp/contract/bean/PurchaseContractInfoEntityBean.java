package com.lpmas.erp.contract.bean;

import java.sql.Timestamp;

import com.lpmas.srm.bean.SupplierInfoBean;

public class PurchaseContractInfoEntityBean {
	
	private PurchaseContractInfoBean purchaseContractInfoBean;
	private SupplierInfoBean supplierInfoBean;
	
	public PurchaseContractInfoEntityBean(PurchaseContractInfoBean purchaseContractInfoBean, SupplierInfoBean supplierInfoBean) {
		super();
		this.purchaseContractInfoBean = purchaseContractInfoBean;
		this.supplierInfoBean = supplierInfoBean;
	}

	public PurchaseContractInfoBean getPurchaseContractInfoBean() {
		return purchaseContractInfoBean;
	}

	public void setPurchaseContractInfoBean(PurchaseContractInfoBean purchaseContractInfoBean) {
		this.purchaseContractInfoBean = purchaseContractInfoBean;
	}
	
	public SupplierInfoBean getSupplierInfoBean() {
		return supplierInfoBean;
	}

	public void setSupplierInfoBean(SupplierInfoBean supplierInfoBean) {
		this.supplierInfoBean = supplierInfoBean;
	}
	
	
	public int getPcId() {
		return purchaseContractInfoBean.getPcId();
	}

	public String getPcName() {
		return purchaseContractInfoBean.getPcName();
	}

	public String getPcNumber() {
		return purchaseContractInfoBean.getPcNumber();
	}

	public int getContractType() {
		return purchaseContractInfoBean.getContractType();
	}

	public int getSupplierId() {
		return purchaseContractInfoBean.getSupplierId();
	}

	public int getStatus() {
		return purchaseContractInfoBean.getStatus();
	}

	public Timestamp getCreateTime() {
		return purchaseContractInfoBean.getCreateTime();
	}

	public int getCreateUser() {
		return purchaseContractInfoBean.getCreateUser();
	}

	public Timestamp getModifyTime() {
		return purchaseContractInfoBean.getModifyTime();
	}

	public int getModifyUser() {
		return purchaseContractInfoBean.getModifyUser();
	}

	public String getMemo() {
		return purchaseContractInfoBean.getMemo();
	}
	
	public String getSupplierName(){
		return supplierInfoBean.getSupplierName();
	}


}
