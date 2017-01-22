package com.lpmas.erp.purchase.bean;

import java.sql.Timestamp;

import com.lpmas.framework.annotation.FieldTag;

public class PurchaseOrderMemoBean {
	@FieldTag(name = "备注ID")
	private int memoId = 0;
	@FieldTag(name = "采购单ID")
	private int poId = 0;
	@FieldTag(name = "备注内容")
	private String memoContent = "";
	@FieldTag(name = "创建时间")
	private Timestamp createTime = null;
	@FieldTag(name = "创建用户")
	private int createUser = 0;

	public int getMemoId() {
		return memoId;
	}

	public void setMemoId(int memoId) {
		this.memoId = memoId;
	}

	public int getPoId() {
		return poId;
	}

	public void setPoId(int poId) {
		this.poId = poId;
	}

	public String getMemoContent() {
		return memoContent;
	}

	public void setMemoContent(String memoContent) {
		this.memoContent = memoContent;
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