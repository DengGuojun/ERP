package com.lpmas.erp.purchase.bean;

import java.sql.Timestamp;

import com.lpmas.framework.annotation.FieldTag;

public class PurchaseOrderInfoBean {
	@FieldTag(name = "采购订单ID")
	private int poId = 0;
	@FieldTag(name = "采购订单编号")
	private String poNumber = "";
	@FieldTag(name = "制品类型")
	private int wareType = 0;
	@FieldTag(name = "采购计划ID")
	private int planId = 0;
	@FieldTag(name = "采购类型")
	private int purchaseType = 0;
	@FieldTag(name = "合同ID")
	private int contractId = 0;
	@FieldTag(name = "供应商ID")
	private int supplierId = 0;
	@FieldTag(name = "发货人")
	private String consignerName = "";
	@FieldTag(name = "发货人国家")
	private String consignerCountry = "";
	@FieldTag(name = "发货人省份")
	private String consignerProvince = "";
	@FieldTag(name = "发货人城市")
	private String consignerCity = "";
	@FieldTag(name = "发货人地区")
	private String consignerRegion = "";
	@FieldTag(name = "发货人地址")
	private String consignerAddress = "";
	@FieldTag(name = "发货人电话")
	private String consignerTelephone = "";
	@FieldTag(name = "发货人手机")
	private String consignerMobile = "";
	@FieldTag(name = "收货方")
	private int receiverType = 0;
	@FieldTag(name = "收货方ID")
	private int receiverId = 0;
	@FieldTag(name = "收货人")
	private String receiverName = "";
	@FieldTag(name = "收货人国家")
	private String receiverCountry = "";
	@FieldTag(name = "收货人省份")
	private String receiverProvince = "";
	@FieldTag(name = "收货人城市")
	private String receiverCity = "";
	@FieldTag(name = "收货人地区")
	private String receiverRegion = "";
	@FieldTag(name = "收货人地址")
	private String receiverAddress = "";
	@FieldTag(name = "收货人电话")
	private String receiverTelephone = "";
	@FieldTag(name = "收货人手机")
	private String receiverMobile = "";
	@FieldTag(name = "币种")
	private String currency = "";
	@FieldTag(name = "采购订单总价")
	private double poAmount = 0;
	@FieldTag(name = "合同状态")
	private String contractStatus = "";
	@FieldTag(name = "发票状态")
	private String invoiceStatus = "";
	@FieldTag(name = "货款状态")
	private String paymentStatus = "";
	@FieldTag(name = "审批状态")
	private String approvalStatus = "";
	@FieldTag(name = "采购订单状态")
	private String poStatus = "";
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

	public int getPoId() {
		return poId;
	}

	public void setPoId(int poId) {
		this.poId = poId;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public int getWareType() {
		return wareType;
	}

	public void setWareType(int wareType) {
		this.wareType = wareType;
	}

	public int getPlanId() {
		return planId;
	}

	public void setPlanId(int planId) {
		this.planId = planId;
	}

	public int getPurchaseType() {
		return purchaseType;
	}

	public void setPurchaseType(int purchaseType) {
		this.purchaseType = purchaseType;
	}

	public int getContractId() {
		return contractId;
	}

	public void setContractId(int contractId) {
		this.contractId = contractId;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public String getConsignerName() {
		return consignerName;
	}

	public void setConsignerName(String consignerName) {
		this.consignerName = consignerName;
	}

	public String getConsignerCountry() {
		return consignerCountry;
	}

	public void setConsignerCountry(String consignerCountry) {
		this.consignerCountry = consignerCountry;
	}

	public String getConsignerProvince() {
		return consignerProvince;
	}

	public void setConsignerProvince(String consignerProvince) {
		this.consignerProvince = consignerProvince;
	}

	public String getConsignerCity() {
		return consignerCity;
	}

	public void setConsignerCity(String consignerCity) {
		this.consignerCity = consignerCity;
	}

	public String getConsignerRegion() {
		return consignerRegion;
	}

	public void setConsignerRegion(String consignerRegion) {
		this.consignerRegion = consignerRegion;
	}

	public String getConsignerAddress() {
		return consignerAddress;
	}

	public void setConsignerAddress(String consignerAddress) {
		this.consignerAddress = consignerAddress;
	}

	public String getConsignerTelephone() {
		return consignerTelephone;
	}

	public void setConsignerTelephone(String consignerTelephone) {
		this.consignerTelephone = consignerTelephone;
	}

	public String getConsignerMobile() {
		return consignerMobile;
	}

	public void setConsignerMobile(String consignerMobile) {
		this.consignerMobile = consignerMobile;
	}

	public int getReceiverType() {
		return receiverType;
	}

	public void setReceiverType(int receiverType) {
		this.receiverType = receiverType;
	}

	public int getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverCountry() {
		return receiverCountry;
	}

	public void setReceiverCountry(String receiverCountry) {
		this.receiverCountry = receiverCountry;
	}

	public String getReceiverProvince() {
		return receiverProvince;
	}

	public void setReceiverProvince(String receiverProvince) {
		this.receiverProvince = receiverProvince;
	}

	public String getReceiverCity() {
		return receiverCity;
	}

	public void setReceiverCity(String receiverCity) {
		this.receiverCity = receiverCity;
	}

	public String getReceiverRegion() {
		return receiverRegion;
	}

	public void setReceiverRegion(String receiverRegion) {
		this.receiverRegion = receiverRegion;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getReceiverTelephone() {
		return receiverTelephone;
	}

	public void setReceiverTelephone(String receiverTelephone) {
		this.receiverTelephone = receiverTelephone;
	}

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getPoAmount() {
		return poAmount;
	}

	public void setPoAmount(double poAmount) {
		this.poAmount = poAmount;
	}

	public String getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}

	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getPoStatus() {
		return poStatus;
	}

	public void setPoStatus(String poStatus) {
		this.poStatus = poStatus;
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

	public String getCompleteConsignerAddress() {
		return this.getConsignerCountry() + this.getConsignerProvince() + this.getConsignerCity()
				+ this.getConsignerRegion() + this.getConsignerAddress();
	}

	public String getCompleteReceiverAddress() {
		return this.getReceiverCountry() + this.getReceiverProvince() + this.getReceiverCity()
				+ this.getReceiverRegion() + this.getReceiverAddress();
	}
}