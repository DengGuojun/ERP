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
import com.lpmas.erp.inventory.bean.WarehouseInventoryPrewarningBean;
import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;

public class WarehouseInventoryPrewarningDao {
	private static Logger log = LoggerFactory.getLogger(WarehouseInventoryPrewarningDao.class);

	public int insertWarehouseInventoryPrewarning(WarehouseInventoryPrewarningBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into warehouse_inventory_prewarning ( ware_type, ware_id, prewarning_type, prewarning_content, is_auto_modify, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getWareType());
			ps.setInt(2, bean.getWareId());
			ps.setInt(3, bean.getPrewarningType());
			ps.setString(4, bean.getPrewarningContent());
			ps.setInt(5, bean.getIsAutoModify());
			ps.setInt(6, bean.getStatus());
			ps.setInt(7, bean.getCreateUser());
			ps.setString(8, bean.getMemo());

			result = db.executePstmtUpdate();
		} catch (Exception e) {
			log.error("", e);
			result = -1;
		} finally {
			try {
				db.close();
			} catch (SQLException sqle) {
				log.error("", sqle);
			}
		}
		return result;
	}

	public int updateWarehouseInventoryPrewarning(WarehouseInventoryPrewarningBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update warehouse_inventory_prewarning set prewarning_content = ?, is_auto_modify = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where ware_type = ? and ware_id = ? and prewarning_type = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getPrewarningContent());
			ps.setInt(2, bean.getIsAutoModify());
			ps.setInt(3, bean.getStatus());
			ps.setInt(4, bean.getModifyUser());
			ps.setString(5, bean.getMemo());

			ps.setInt(6, bean.getWareType());
			ps.setInt(7, bean.getWareId());
			ps.setInt(8, bean.getPrewarningType());

			result = db.executePstmtUpdate();
		} catch (Exception e) {
			log.error("", e);
			result = -1;
		} finally {
			try {
				db.close();
			} catch (SQLException sqle) {
				log.error("", sqle);
			}
		}
		return result;
	}

	public WarehouseInventoryPrewarningBean getWarehouseInventoryPrewarningByKey(int wareType, int wareId,
			int prewarningType) {
		WarehouseInventoryPrewarningBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_inventory_prewarning where ware_type = ? and ware_id = ? and prewarning_type = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, wareType);
			ps.setInt(2, wareId);
			ps.setInt(3, prewarningType);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new WarehouseInventoryPrewarningBean();
				bean = BeanKit.resultSet2Bean(rs, WarehouseInventoryPrewarningBean.class);
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

	public PageResultBean<WarehouseInventoryPrewarningBean> getWarehouseInventoryPrewarningPageListByMap(
			HashMap<String, String> condMap, PageBean pageBean) {
		PageResultBean<WarehouseInventoryPrewarningBean> result = new PageResultBean<WarehouseInventoryPrewarningBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_inventory_prewarning";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			String wareType = condMap.get("wareType");
			if (StringKit.isValid(wareType)) {
				condList.add("ware_type = ?");
				paramList.add(wareType);
			}
			String prewarningType = condMap.get("prewarningType");
			if (StringKit.isValid(prewarningType)) {
				condList.add("prewarning_type = ?");
				paramList.add(prewarningType);
			}
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}

			String orderQuery = "order by prewarning_type,ware_id,ware_type desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList,
					WarehouseInventoryPrewarningBean.class, pageBean, db);
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

	public List<WarehouseInventoryPrewarningBean> getWarehouseInventoryPrewarningListByMap(
			HashMap<String, String> condMap) {
		List<WarehouseInventoryPrewarningBean> result = new ArrayList<WarehouseInventoryPrewarningBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_inventory_prewarning";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			String wareType = condMap.get("wareType");
			if (StringKit.isValid(wareType)) {
				condList.add("ware_type = ?");
				paramList.add(wareType);
			}
			String prewarningType = condMap.get("prewarningType");
			if (StringKit.isValid(prewarningType)) {
				condList.add("prewarning_type = ?");
				paramList.add(prewarningType);
			}
			String wareId = condMap.get("wareId");
			if (StringKit.isValid(wareId)) {
				condList.add("ware_id = ?");
				paramList.add(wareId);
			}
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}

			String orderQuery = "order by prewarning_type,ware_id,ware_type desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getRecordListResult(sql, orderQuery, condList, paramList,
					WarehouseInventoryPrewarningBean.class, db);
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

}
