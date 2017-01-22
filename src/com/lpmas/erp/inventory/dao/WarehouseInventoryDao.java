package com.lpmas.erp.inventory.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.erp.factory.ErpDBFactory;
import com.lpmas.erp.inventory.bean.WarehouseInventoryBean;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;

public class WarehouseInventoryDao {
	private static Logger log = LoggerFactory.getLogger(WarehouseInventoryDao.class);

	public int insertWarehouseInventory(DBObject db, WarehouseInventoryBean bean) throws Exception {
		int result = -1;
		try {
			String sql = "insert into warehouse_inventory ( warehouse_id, ware_type, ware_id, inventory_type, batch_number, production_date, guarantee_period, expiration_date, quantity, unit, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getWarehouseId());
			ps.setInt(2, bean.getWareType());
			ps.setInt(3, bean.getWareId());
			ps.setInt(4, bean.getInventoryType());
			ps.setString(5, bean.getBatchNumber());
			ps.setTimestamp(6, bean.getProductionDate());
			ps.setDouble(7, bean.getGuaranteePeriod());
			ps.setTimestamp(8, bean.getExpirationDate());
			ps.setDouble(9, bean.getQuantity());
			ps.setString(10, bean.getUnit());
			ps.setInt(11, bean.getStatus());
			ps.setInt(12, bean.getCreateUser());
			ps.setString(13, bean.getMemo());
			result = db.executePstmtInsert();
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return result;
	}

	public int updateWarehouseInventory(DBObject db, WarehouseInventoryBean bean) throws Exception {
		int result = -1;
		try {
			String sql = "update warehouse_inventory set warehouse_id = ?, ware_type = ?, ware_id = ?, inventory_type = ?, batch_number = ?, production_date = ?, guarantee_period = ?, expiration_date = ?, quantity = ?, unit = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where wi_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getWarehouseId());
			ps.setInt(2, bean.getWareType());
			ps.setInt(3, bean.getWareId());
			ps.setInt(4, bean.getInventoryType());
			ps.setString(5, bean.getBatchNumber());
			ps.setTimestamp(6, bean.getProductionDate());
			ps.setDouble(7, bean.getGuaranteePeriod());
			ps.setTimestamp(8, bean.getExpirationDate());
			ps.setDouble(9, bean.getQuantity());
			ps.setString(10, bean.getUnit());
			ps.setInt(11, bean.getStatus());
			ps.setInt(12, bean.getModifyUser());
			ps.setString(13, bean.getMemo());

			ps.setInt(14, bean.getWiId());

			result = db.executePstmtUpdate();
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return result;
	}

	public WarehouseInventoryBean getWarehouseInventoryByKey(int wiId) {
		WarehouseInventoryBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_inventory where wi_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, wiId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new WarehouseInventoryBean();
				bean = BeanKit.resultSet2Bean(rs, WarehouseInventoryBean.class);
			}
			rs.close();
		} catch (Exception e) {
			log.error("", e);
			bean = null;
		} finally {
			try {
				db.close();
			} catch (SQLException sqle) {
				log.error("", sqle);
			}
		}
		return bean;
	}

	public List<WarehouseInventoryBean> getWarehouseInventoryAllList() {
		List<WarehouseInventoryBean> result = new ArrayList<WarehouseInventoryBean>(1);
		WarehouseInventoryBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_inventory where status = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, Constants.STATUS_VALID);

			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				bean = BeanKit.resultSet2Bean(rs, WarehouseInventoryBean.class);
				result.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			log.error("", e);
			bean = null;
		} finally {
			try {
				db.close();
			} catch (SQLException sqle) {
				log.error("", sqle);
			}
		}
		return result;
	}

	public PageResultBean<WarehouseInventoryBean> getWarehouseInventoryPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		PageResultBean<WarehouseInventoryBean> result = new PageResultBean<WarehouseInventoryBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_inventory";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			String warehouseId = condMap.get("warehouseId");
			if (StringKit.isValid(warehouseId)) {
				condList.add("warehouse_id = ?");
				paramList.add(warehouseId);
			}
			String wareType = condMap.get("wareType");
			if (StringKit.isValid(wareType)) {
				condList.add("ware_type = ?");
				paramList.add(wareType);
			}
			String wareId = condMap.get("wareId");
			if (StringKit.isValid(wareId)) {
				condList.add("ware_id = ?");
				paramList.add(wareId);
			}
			String inventoryType = condMap.get("inventoryType");
			if (StringKit.isValid(inventoryType)) {
				condList.add("inventory_type = ?");
				paramList.add(inventoryType);
			}
			String batchNumber = condMap.get("batchNumber");
			if (StringKit.isValid(batchNumber)) {
				condList.add("batch_number = ?");
				paramList.add(batchNumber);
			}
			String productionDate = condMap.get("productionDate");
			if (StringKit.isValid(productionDate)) {
				condList.add("production_date = ?");
				paramList.add(productionDate);
			}
			String gt_quantity = condMap.get("gt_quantity");
			if (StringKit.isValid(gt_quantity)) {
				condList.add("quantity > ?");
				paramList.add(gt_quantity);
			}
			String lt_quantity = condMap.get("lt_quantity");
			if (StringKit.isValid(lt_quantity)) {
				condList.add("quantity < ?");
				paramList.add(lt_quantity);
			}

			String orderQuery = "order by wi_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, WarehouseInventoryBean.class,
					pageBean, db);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			try {
				db.close();
			} catch (SQLException sqle) {
				log.error("", sqle);
			}
		}
		return result;
	}

	public List<WarehouseInventoryBean> getWarehouseInventoryByMap(HashMap<String, String> condMap) {
		List<WarehouseInventoryBean> list = new ArrayList<WarehouseInventoryBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_inventory";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			String warehouseId = condMap.get("warehouseId");
			if (StringKit.isValid(warehouseId)) {
				condList.add("warehouse_id = ?");
				paramList.add(warehouseId);
			}
			String warehouseIdList = condMap.get("warehouseIdList");
			if (StringKit.isValid(warehouseIdList)) {
				condList.add("warehouse_id in (" + warehouseIdList + ")");
			}
			String wareType = condMap.get("wareType");
			if (StringKit.isValid(wareType)) {
				condList.add("ware_type = ?");
				paramList.add(wareType);
			}
			String wareId = condMap.get("wareId");
			if (StringKit.isValid(wareId)) {
				condList.add("ware_id = ?");
				paramList.add(wareId);
			}
			String inventoryType = condMap.get("inventoryType");
			if (StringKit.isValid(inventoryType)) {
				condList.add("inventory_type = ?");
				paramList.add(inventoryType);
			}
			String batchNumber = condMap.get("batchNumber");
			if (StringKit.isValid(batchNumber)) {
				condList.add("batch_number = ?");
				paramList.add(batchNumber);
			}
			String productionDate = condMap.get("productionDate");
			if (StringKit.isValid(productionDate)) {
				condList.add("production_date = ?");
				paramList.add(productionDate);
			}
			String orderQuery = "order by wi_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}
			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			list = dbExecutor.getRecordListResult(sql, orderQuery, condList, paramList, WarehouseInventoryBean.class,
					db);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			try {
				db.close();
			} catch (SQLException sqle) {
				log.error("", sqle);
			}
		}
		return list;
	}

}
