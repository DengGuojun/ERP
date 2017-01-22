package com.lpmas.erp.inventory.business;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lpmas.erp.inventory.bean.WarehouseInventoryBean;
import com.lpmas.erp.inventory.dao.WarehouseInventoryDao;
import com.lpmas.framework.db.SqlKit;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.ListKit;

public class WarehouseInventoryBusiness {

	public WarehouseInventoryBean getWarehouseInventoryByKey(int wiId) {
		WarehouseInventoryDao dao = new WarehouseInventoryDao();
		return dao.getWarehouseInventoryByKey(wiId);
	}

	public PageResultBean<WarehouseInventoryBean> getWarehouseInventoryPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) throws Exception {
		WarehouseInventoryDao dao = new WarehouseInventoryDao();
		return dao.getWarehouseInventoryPageListByMap(condMap, pageBean);
	}

	public WarehouseInventoryBean getWarehouseInventoryBeanByCondition(int warehouseId, int wareType, int wareId,
			int inventoryType, String batchNumber, Timestamp productionDate) {
		WarehouseInventoryDao dao = new WarehouseInventoryDao();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("warehouseId", String.valueOf(warehouseId));
		condMap.put("wareType", String.valueOf(wareType));
		condMap.put("wareId", String.valueOf(wareId));
		condMap.put("inventoryType", String.valueOf(inventoryType));
		condMap.put("batchNumber", batchNumber);
		condMap.put("productionDate", String.valueOf(productionDate));
		List<WarehouseInventoryBean> list = dao.getWarehouseInventoryByMap(condMap);
		if (!list.isEmpty())
			return list.get(0);
		else
			return null;
	}

	public List<WarehouseInventoryBean> getWarehouseInventoryListByMap(HashMap<String, String> condMap) {
		WarehouseInventoryDao dao = new WarehouseInventoryDao();
		return dao.getWarehouseInventoryByMap(condMap);
	}

	public List<WarehouseInventoryBean> getWarehouseInventoryListByCondition(int wareType,
			List<Integer> warehouseIdList) {
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("warehouseIdList", SqlKit.getQueryStmt(warehouseIdList));
		condMap.put("wareType", String.valueOf(wareType));
		return getWarehouseInventoryListByMap(condMap);
	}

	public Set<Integer> getWareIdListFromWiList(List<WarehouseInventoryBean> wiList) {
		Map<Integer, WarehouseInventoryBean> wareIdMap = ListKit.list2Map(wiList, "wareId");
		return wareIdMap.keySet();
	}

	public List<Integer> getVaildWiIdListByCondition(int wareId, int wareType, List<WarehouseInventoryBean> wiList) {
		List<Integer> result = new ArrayList<Integer>();
		for (WarehouseInventoryBean bean : wiList) {
			if (bean.getWareId() == wareId && bean.getWareType() == wareType) {
				result.add(bean.getWiId());
			}
		}
		return result;
	}

	public List<WarehouseInventoryBean> getWareWarehouseInventoryListByInventoryType(int wareType, int wareId) {
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("wareType", String.valueOf(wareType));
		condMap.put("wareId", String.valueOf(wareId));
		return getWarehouseInventoryListByMap(condMap);
	}

	public List<WarehouseInventoryBean> getWarehouseInventoryAllList() {
		WarehouseInventoryDao dao = new WarehouseInventoryDao();
		return dao.getWarehouseInventoryAllList();
	}

}