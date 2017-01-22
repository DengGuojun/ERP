package com.lpmas.erp.warehouse.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.erp.factory.ErpDBFactory;
import com.lpmas.erp.warehouse.bean.WarehousePropertyBean;
import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;

public class WarehousePropertyDao {
	private static Logger log = LoggerFactory.getLogger(WarehousePropertyDao.class);

	public int insertWarehouseProperty(WarehousePropertyBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into warehouse_property ( warehouse_id, property_code, property_value, create_time, create_user) value( ?, ?, ?, now(), ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getWarehouseId());
			ps.setString(2, bean.getPropertyCode());
			ps.setString(3, bean.getPropertyValue());
			ps.setInt(4, bean.getCreateUser());

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

	public int updateWarehouseProperty(WarehousePropertyBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update warehouse_property set property_value = ?, modify_time = now(), modify_user = ? where warehouse_id = ? and property_code = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getPropertyValue());
			ps.setInt(2, bean.getModifyUser());

			ps.setInt(3, bean.getWarehouseId());
			ps.setString(4, bean.getPropertyCode());

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

	public WarehousePropertyBean getWarehousePropertyByKey(int warehouseId, String propertyCode) {
		WarehousePropertyBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_property where warehouse_id = ? and property_code = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, warehouseId);
			ps.setString(2, propertyCode);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new WarehousePropertyBean();
				bean = BeanKit.resultSet2Bean(rs, WarehousePropertyBean.class);
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

	public PageResultBean<WarehousePropertyBean> getWarehousePropertyPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		PageResultBean<WarehousePropertyBean> result = new PageResultBean<WarehousePropertyBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_property";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理

			String orderQuery = "order by property_code,warehouse_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, WarehousePropertyBean.class,
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

	public List<Integer> getWarehouseIdListByMap(Map<String, String> condMap) {
		List<Integer> result = new ArrayList<Integer>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		String condStr = "";
		for (Entry<String, String> entry : condMap.entrySet()) {
			condStr += "property_code = '" + entry.getKey() + "' and property_value = '" + entry.getValue() + "' and ";
		}
		condStr += " 1=1 ";
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select warehouse_id from warehouse_property where " + condStr;
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				result.add(rs.getInt(1));
			}

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