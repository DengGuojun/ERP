package com.lpmas.erp.inventory.business;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class WarehouesInventoryOperationLock {
	// 用于控制库存操作的同步锁
	public static Lock lock = new ReentrantLock();
}
