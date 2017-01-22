package com.lpmas.erp.inventory.business;

import java.util.HashMap;

import com.lpmas.erp.inventory.bean.WarehouseInventoryChangeBean;
import com.lpmas.erp.inventory.dao.WarehouseInventoryChangeDao;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;

public class WarehouseInventoryChangeBusiness {
	
	public WarehouseInventoryChangeBean getWarehouseInventoryChangeByKey(int changeId) {
		WarehouseInventoryChangeDao dao = new WarehouseInventoryChangeDao();
		return dao.getWarehouseInventoryChangeByKey(changeId);
	}

	public PageResultBean<WarehouseInventoryChangeBean> getWarehouseInventoryChangePageListByMap(
			HashMap<String, String> condMap, PageBean pageBean) {
		WarehouseInventoryChangeDao dao = new WarehouseInventoryChangeDao();
		return dao.getWarehouseInventoryChangePageListByMap(condMap, pageBean);
	}

}