package com.lpmas.erp.inventory.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lpmas.erp.config.ErpMongoConfig;
import com.lpmas.erp.inventory.bean.WarehouseInventoryAggregateReportBean;
import com.lpmas.framework.nosql.mongodb.MongoDB;
import com.lpmas.framework.nosql.mongodb.MongoDBConfig;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;

public class WarehouseInventoryAggregateReportDao {

	public int insertWarehouseInventoryAggregateReport(WarehouseInventoryAggregateReportBean bean) {
		return MongoDB.getInstance().insert(ErpMongoConfig.DB_NAME,
				ErpMongoConfig.COLLECTION_WAREHOUSE_INVENTORY_AGGREGATE_REPORT, bean);
	}

	public long updateWarehouseInventoryAggregateReport(WarehouseInventoryAggregateReportBean bean) {
		return MongoDB.getInstance().update(ErpMongoConfig.DB_NAME,
				ErpMongoConfig.COLLECTION_WAREHOUSE_INVENTORY_AGGREGATE_REPORT, bean);
	}

	public WarehouseInventoryAggregateReportBean getWarehouseInventoryAggregateReportByKey(String id) throws Exception {
		return MongoDB.getInstance().getRecordById(ErpMongoConfig.DB_NAME,
				ErpMongoConfig.COLLECTION_WAREHOUSE_INVENTORY_AGGREGATE_REPORT, id,
				WarehouseInventoryAggregateReportBean.class);
	}
	
	public List<WarehouseInventoryAggregateReportBean> getWarehouseInventoryAggregateReportListByMap(Map<String, Object> condMap) throws Exception{
		Map<String, Object> orderBy = new HashMap<String, Object>();
		orderBy.put("warehouseId", MongoDBConfig.SORT_ORDER_ASC);
		return MongoDB.getInstance().getRecordListResult(ErpMongoConfig.DB_NAME,
				ErpMongoConfig.COLLECTION_WAREHOUSE_INVENTORY_AGGREGATE_REPORT, condMap, WarehouseInventoryAggregateReportBean.class, orderBy);
	}

	public PageResultBean<WarehouseInventoryAggregateReportBean> getWarehouseInventoryAggregateReportPageListByMap(
			HashMap<String, Object> condMap, PageBean pageBean) throws Exception {
		Map<String, Object> orderBy = new HashMap<String, Object>();
		orderBy.put("warehouseId", MongoDBConfig.SORT_ORDER_ASC);
		return MongoDB.getInstance().getPageListResult(ErpMongoConfig.DB_NAME,
				ErpMongoConfig.COLLECTION_WAREHOUSE_INVENTORY_AGGREGATE_REPORT, condMap,
				WarehouseInventoryAggregateReportBean.class, pageBean, orderBy);
	}

}
