package com.lpmas.erp.inventory.dao;

import java.util.HashMap;
import java.util.Map;

import com.lpmas.erp.config.ErpMongoConfig;
import com.lpmas.erp.inventory.bean.WarehouseInventoryReportBean;
import com.lpmas.framework.nosql.mongodb.MongoDB;
import com.lpmas.framework.nosql.mongodb.MongoDBConfig;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.DateKit;
import com.lpmas.framework.util.StringKit;
import com.mongodb.BasicDBObject;

public class WarehouseInventoryReportDao {

	public int insertWarehouseInventoryReport(WarehouseInventoryReportBean bean) {
		return MongoDB.getInstance().insert(ErpMongoConfig.DB_NAME,
				ErpMongoConfig.COLLECTION_WAREHOUSE_INVENTORY_REPORT, bean);
	}

	public long updateWarehouseInventoryReport(WarehouseInventoryReportBean bean) {
		return MongoDB.getInstance().update(ErpMongoConfig.DB_NAME,
				ErpMongoConfig.COLLECTION_WAREHOUSE_INVENTORY_REPORT, bean);
	}

	public WarehouseInventoryReportBean getWarehouseInventoryReportByKey(String id) throws Exception {
		return MongoDB.getInstance().getRecordById(ErpMongoConfig.DB_NAME,
				ErpMongoConfig.COLLECTION_WAREHOUSE_INVENTORY_REPORT, id, WarehouseInventoryReportBean.class);
	}

	public PageResultBean<WarehouseInventoryReportBean> getWarehouseInventoryReportPageListByMap(
			HashMap<String, String> condMap, HashMap<String, HashMap<String, String>> scopeMap, PageBean pageBean)
					throws Exception {
		// 条件处理
		HashMap<String, Object> queryMap = new HashMap<String, Object>();
		String warehouseId = condMap.get("warehouseId");
		if (StringKit.isValid(warehouseId)) {
			queryMap.put("warehouseId", Integer.valueOf(warehouseId));
		}
		String wareType = condMap.get("wareType");
		if (StringKit.isValid(wareType)) {
			queryMap.put("wareType", Integer.valueOf(wareType));
		}
		String batchNumber = condMap.get("batchNumber");
		if (StringKit.isValid(batchNumber)) {
			queryMap.put("batchNumber", String.valueOf(batchNumber));
		}
		String productionDate = condMap.get("productionDate");
		if (StringKit.isValid(productionDate)) {
			queryMap.put("productionDate",
					DateKit.str2Timestamp(productionDate, DateKit.DEFAULT_DATE_FORMAT).getTime());
		}
		// 范围条件处理
		if (scopeMap.get("productionDate") != null) {
			BasicDBObject scopeCond = new BasicDBObject();
			if (scopeMap.get("productionDate").get("gt_dateTime") != null) {
				long gtTime = DateKit
						.str2Timestamp(scopeMap.get("productionDate").get("gt_dateTime"), DateKit.DEFAULT_DATE_FORMAT)
						.getTime();
				scopeCond.append("$gte", gtTime);
				queryMap.put("productionDate", scopeCond);
			}
			if (scopeMap.get("productionDate").get("lt_dateTime") != null) {
				long ltTime = DateKit
						.str2Timestamp(scopeMap.get("productionDate").get("lt_dateTime"), DateKit.DEFAULT_DATE_FORMAT)
						.getTime();
				scopeCond.append("$lte", ltTime);
				queryMap.put("productionDate", scopeCond);
			}
		}
		Map<String, Object> orderBy = new HashMap<String, Object>();
		orderBy.put("warehouseId", MongoDBConfig.SORT_ORDER_ASC);
		return MongoDB.getInstance().getPageListResult(ErpMongoConfig.DB_NAME,
				ErpMongoConfig.COLLECTION_WAREHOUSE_INVENTORY_REPORT, queryMap, WarehouseInventoryReportBean.class,
				pageBean, orderBy);
	}

}
