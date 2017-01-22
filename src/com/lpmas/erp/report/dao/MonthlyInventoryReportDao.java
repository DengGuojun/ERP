package com.lpmas.erp.report.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.erp.config.ErpMongoConfig;
import com.lpmas.erp.report.bean.MonthlyInventoryReportBean;
import com.lpmas.framework.nosql.mongodb.MongoDB;

public class MonthlyInventoryReportDao {

	public static Logger logger = LoggerFactory.getLogger(MonthlyInventoryReportDao.class);

	public List<MonthlyInventoryReportBean> getMonthlyInventoryReportListByMap(HashMap<String, Object> condMap) {
		MongoDB mongoDB = MongoDB.getInstance();
		try {
			return mongoDB.getRecordListResult(ErpMongoConfig.DB_NAME,
					ErpMongoConfig.COLLECTION_MONTHLY_INVENTORY_REPORT, condMap, MonthlyInventoryReportBean.class,
					null);
		} catch (Exception e) {
			logger.error("", e);
			return new ArrayList<MonthlyInventoryReportBean>(1);
		}
	}

	public MonthlyInventoryReportBean getMonthlyInventoryReportByKey(String _id) {
		MongoDB mongoDB = MongoDB.getInstance();
		try {
			return mongoDB.getRecordById(ErpMongoConfig.DB_NAME, ErpMongoConfig.COLLECTION_MONTHLY_INVENTORY_REPORT,
					_id, MonthlyInventoryReportBean.class);
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}
	
	public int deleteMonthlyInventoryReportByMap(Map<String, Object> condMap){
		MongoDB mongoDB = MongoDB.getInstance();
		return (int) mongoDB.delete(ErpMongoConfig.DB_NAME, ErpMongoConfig.COLLECTION_MONTHLY_INVENTORY_REPORT, condMap);
	}
	
	public int updateMonthlyInventoryReport(MonthlyInventoryReportBean bean){
		MongoDB mongoDB = MongoDB.getInstance();
		return (int) mongoDB.update(ErpMongoConfig.DB_NAME, ErpMongoConfig.COLLECTION_MONTHLY_INVENTORY_REPORT, bean);
	}

	public int insertMonthlyInventoryReport(MonthlyInventoryReportBean bean) {
		MongoDB mongoDB = MongoDB.getInstance();
		return mongoDB.insert(ErpMongoConfig.DB_NAME, ErpMongoConfig.COLLECTION_MONTHLY_INVENTORY_REPORT, bean);
	}

}
