package com.lpmas.erp.inventory.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lpmas.erp.inventory.bean.WarehouseInventoryAggregateReportBean;
import com.lpmas.erp.inventory.dao.WarehouseInventoryAggregateReportDao;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.StringKit;

public class WarehouseInventoryAggregateReportBusiness {

	public int insertWarehouseInventoryAggregateReport(WarehouseInventoryAggregateReportBean bean) {
		WarehouseInventoryAggregateReportDao dao = new WarehouseInventoryAggregateReportDao();
		return dao.insertWarehouseInventoryAggregateReport(bean);
	}

	public long updateWarehouseInventoryAggregateReport(WarehouseInventoryAggregateReportBean bean) {
		WarehouseInventoryAggregateReportDao dao = new WarehouseInventoryAggregateReportDao();
		return dao.updateWarehouseInventoryAggregateReport(bean);
	}

	public WarehouseInventoryAggregateReportBean getWarehouseInventoryAggregateReportByKey(String id) throws Exception {
		WarehouseInventoryAggregateReportDao dao = new WarehouseInventoryAggregateReportDao();
		return dao.getWarehouseInventoryAggregateReportByKey(id);
	}
	
	public List<WarehouseInventoryAggregateReportBean> getWarehouseInventoryAggregateReportListByMap(Map<String, Object> condMap) throws Exception{
		WarehouseInventoryAggregateReportDao dao = new WarehouseInventoryAggregateReportDao();
		return dao.getWarehouseInventoryAggregateReportListByMap(condMap);
	}

	public PageResultBean<WarehouseInventoryAggregateReportBean> getWarehouseInventoryAggregateReportPageListByMap(
			HashMap<String, String> condMap, HashMap<String, HashMap<String, String>> scopeMap, PageBean pageBean)
					throws Exception {
		WarehouseInventoryAggregateReportDao dao = new WarehouseInventoryAggregateReportDao();
		// 条件处理
		HashMap<String, Object> queryMap = new HashMap<String, Object>();
		String wareType = condMap.get("wareType");
		if (StringKit.isValid(wareType)) {
			queryMap.put("wareType", Integer.valueOf(wareType));
		}
		String wareId = condMap.get("wareId");
		if (StringKit.isValid(wareId)) {
			queryMap.put("wareId", Integer.valueOf(wareId));
		}
		String typeId = condMap.get("typeId");
		if (StringKit.isValid(typeId)) {
			queryMap.put("typeId", Integer.valueOf(typeId));
		}
		return dao.getWarehouseInventoryAggregateReportPageListByMap(queryMap, pageBean);
	}
}
