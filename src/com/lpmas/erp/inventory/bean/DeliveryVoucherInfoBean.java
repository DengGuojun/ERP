package com.lpmas.erp.inventory.bean;

import java.sql.Timestamp;

import com.lpmas.framework.annotation.FieldTag;

public class DeliveryVoucherInfoBean {
	@FieldTag(name = "出库单ID")
	private int dvId = 0;
	@FieldTag(name = "出库单编号")
	private String dvNumber = "";
	@FieldTag(name = "出库类型")
	private int dvType = 0;
	@FieldTag(name = "源单类型")
	private int sourceOrderType = 0;
	@FieldTag(name = "源单ID")
	private int sourceOrderId = 0;
	@FieldTag(name = "制品类型")
	private int wareType = 0;
	@FieldTag(name = "出库日期")
	private Timestamp stockOutTime = null;
	@FieldTag(name = "仓库ID")
	private int warehouseId = 0;
	@FieldTag(name = "出货质检人")
	private String inspectorName = "";
	@FieldTag(name = "仓库出货人")
	private String senderName = "";
	@FieldTag(name = "记账人")
	private String accountClerkName = "";
	@FieldTag(name = "审核状态")
	private String approveStatus = "";
	@FieldTag(name = "同步状态")
	private String syncStatus = "";
	@FieldTag(name = "出库单状态")
	private String dvStatus = "";
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

	public int getDvId() {
		return dvId;
	}

	public void setDvId(int dvId) {
		this.dvId = dvId;
	}

	public String getDvNumber() {
		return dvNumber;
	}

	public void setDvNumber(String dvNumber) {
		this.dvNumber = dvNumber;
	}

	public int getDvType() {
		return dvType;
	}

	public void setDvType(int dvType) {
		this.dvType = dvType;
	}

	public int getSourceOrderType() {
		return sourceOrderType;
	}

	public void setSourceOrderType(int sourceOrderType) {
		this.sourceOrderType = sourceOrderType;
	}

	public int getSourceOrderId() {
		return sourceOrderId;
	}

	public void setSourceOrderId(int sourceOrderId) {
		this.sourceOrderId = sourceOrderId;
	}

	public int getWareType() {
		return wareType;
	}

	public void setWareType(int wareType) {
		this.wareType = wareType;
	}

	public Timestamp getStockOutTime() {
		return stockOutTime;
	}

	public void setStockOutTime(Timestamp stockOutTime) {
		this.stockOutTime = stockOutTime;
	}

	public int getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(int warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getInspectorName() {
		return inspectorName;
	}

	public void setInspectorName(String inspectorName) {
		this.inspectorName = inspectorName;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getAccountClerkName() {
		return accountClerkName;
	}

	public void setAccountClerkName(String accountClerkName) {
		this.accountClerkName = accountClerkName;
	}

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(String syncStatus) {
		this.syncStatus = syncStatus;
	}

	public String getDvStatus() {
		return dvStatus;
	}

	public void setDvStatus(String dvStatus) {
		this.dvStatus = dvStatus;
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
