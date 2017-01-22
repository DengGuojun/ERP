package com.lpmas.erp.inventory.bean;

import java.sql.Timestamp;
import com.lpmas.framework.annotation.FieldTag;

public class WarehouseVoucherInfoBean {
	@FieldTag(name = "入库单ID")
	private int wvId = 0;
	@FieldTag(name = "入库单编号")
	private String wvNumber = "";
	@FieldTag(name = "入库类型")
	private int wvType = 0;
	@FieldTag(name = "源单类型")
	private int sourceOrderType = 0;
	@FieldTag(name = "源单ID")
	private int sourceOrderId = 0;
	@FieldTag(name = "供应商ID")
	private int supplierId = 0;
	@FieldTag(name = "制品类型")
	private int wareType = 0;
	@FieldTag(name = "入库日期")
	private Timestamp stockInTime = null;
	@FieldTag(name = "仓库ID")
	private int warehouseId = 0;
	@FieldTag(name = "入库质检人")
	private String inspectorName = "";
	@FieldTag(name = "仓库收货人")
	private String receiverName = "";
	@FieldTag(name = "记账人")
	private String accountClerkName = "";
	@FieldTag(name = "审核状态")
	private String approveStatus = "";
	@FieldTag(name = "同步状态")
	private String syncStatus = "";
	@FieldTag(name = "入库单状态")
	private String wvStatus = "";
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

	public int getWvId() {
		return wvId;
	}

	public void setWvId(int wvId) {
		this.wvId = wvId;
	}

	public String getWvNumber() {
		return wvNumber;
	}

	public void setWvNumber(String wvNumber) {
		this.wvNumber = wvNumber;
	}

	public int getWvType() {
		return wvType;
	}

	public void setWvType(int wvType) {
		this.wvType = wvType;
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

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public int getWareType() {
		return wareType;
	}

	public void setWareType(int wareType) {
		this.wareType = wareType;
	}

	public Timestamp getStockInTime() {
		return stockInTime;
	}

	public void setStockInTime(Timestamp stockInTime) {
		this.stockInTime = stockInTime;
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

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
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

	public String getWvStatus() {
		return wvStatus;
	}

	public void setWvStatus(String wvStatus) {
		this.wvStatus = wvStatus;
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