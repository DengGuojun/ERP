package com.lpmas.erp.purchase.business;

public interface PurchaseOrderStatus {
	
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
	 * 下单
	 */
	public int placeOrder();
	
	/**
	 * 取消下单
	 */
	public int cancelOrder();
	
	/**
	 * 收货
	 */
	public int receive();
	
	/**
	 * 归档
	 */
	public int archive();

	public boolean committable();
	
	public boolean cancelCommittable();
	
	public boolean approvable();
	
	public boolean rejectable();
	
	public boolean placeOrderable();
	
	public boolean cancelOrderable();
	
	public boolean receivable();
	
	public boolean archivable();
	
	
	
}
