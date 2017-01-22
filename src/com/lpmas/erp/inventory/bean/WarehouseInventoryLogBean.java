package com.lpmas.erp.inventory.bean;

import java.sql.Timestamp;
import com.lpmas.framework.annotation.FieldTag;

public class WarehouseInventoryLogBean {
	@FieldTag(name = "日志ID")
	private int logId = 0;
	@FieldTag(name = "库存ID")
	private int wiId = 0;
	@FieldTag(name = "变化类型")
	private int changeType = 0;
	@FieldTag(name = "原单号")
	private int sourceId = 0;
	@FieldTag(name = "数量")
	private double quantity = 0;
	@FieldTag(name = "创建时间")
	private Timestamp createTime = null;
	@FieldTag(name = "创建用户")
	private int createUser = 0;

	public int getLogId() {
		return logId;
	}

	public void setLogId(int logId) {
		this.logId = logId;
	}

	public int getWiId() {
		return wiId;
	}

	public void setWiId(int wiId) {
		this.wiId = wiId;
	}

	public int getChangeType() {
		return changeType;
	}

	public void setChangeType(int changeType) {
		this.changeType = changeType;
	}

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
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
}