package com.lpmas.erp.inventory.business;

public interface DeliveryVoucherStatus {
	
	/**
	 * 提交审核
	 */
	public int commit();
	
	/**
	 * 取消审核
	 */
	public int cancelCommit();

	/**
	 * 审核成功
	 */
	public int approve();

	/**
	 * 审核失败
	 */
	public int reject();
	
	/**
	 * 关闭
	 */
	public int close();

	public boolean committable();
	
	public boolean cancelCommittable();
	
	public boolean approvable();
	
	public boolean rejectable();
	
	public boolean closable();

}
