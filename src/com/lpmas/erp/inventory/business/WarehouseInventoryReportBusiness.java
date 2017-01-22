package com.lpmas.erp.inventory.business;

import java.util.HashMap;

import com.lpmas.erp.inventory.bean.WarehouseInventoryReportBean;
import com.lpmas.erp.inventory.dao.WarehouseInventoryReportDao;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;

public class WarehouseInventoryReportBusiness {

	public int insertWarehouseInventoryReport(WarehouseInventoryReportBean bean) {
		WarehouseInventoryReportDao dao = new WarehouseInventoryReportDao();
		return dao.insertWarehouseInventoryReport(bean);
	}

	public long updateWarehouseInventoryReport(WarehouseInventoryReportBean bean) {
		WarehouseInventoryReportDao dao = new WarehouseInventoryReportDao();
		return dao.updateWarehouseInventoryReport(bean);
	}

	public WarehouseInventoryReportBean getWarehouseInventoryReportByKey(String id) throws Exception {
		WarehouseInventoryReportDao dao = new WarehouseInventoryReportDao();
		return dao.getWarehouseInventoryReportByKey(id);
	}

	public PageResultBean<WarehouseInventoryReportBean> getWarehouseInventoryReportPageListByMap(
			HashMap<String, String> condMap, HashMap<String, HashMap<String, String>> scopeMap, PageBean pageBean)
					throws Exception {
		WarehouseInventoryReportDao dao = new WarehouseInventoryReportDao();
		return dao.getWarehouseInventoryReportPageListByMap(condMap, scopeMap, pageBean);
	}
}
