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
import com.lpmas.erp.inventory.bean.WarehouseInventoryChangeBean;
import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;

public class WarehouseInventoryChangeDao {
	private static Logger log = LoggerFactory.getLogger(WarehouseInventoryChangeDao.class);

	public int insertWarehouseInventoryChange(DBObject db, WarehouseInventoryChangeBean bean) throws Exception{
		int result = -1;
		try {
			String sql = "insert into warehouse_inventory_change ( wi_id_1, wi_id_2, change_type, quantity, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getWiId1());
			ps.setInt(2, bean.getWiId2());
			ps.setInt(3, bean.getChangeType());
			ps.setDouble(4, bean.getQuantity());
			ps.setInt(5, bean.getStatus());
			ps.setInt(6, bean.getCreateUser());
			ps.setString(7, bean.getMemo());

			result = db.executePstmtInsert();
		} catch (Exception e) {
			log.error("", e);
			throw e;
		} 
		return result;
	}

	public int updateWarehouseInventoryChange(WarehouseInventoryChangeBean bean) throws Exception{
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update warehouse_inventory_change set wi_id_1 = ?, wi_id_2 = ?, change_type = ?, quantity = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where change_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getWiId1());
			ps.setInt(2, bean.getWiId2());
			ps.setInt(3, bean.getChangeType());
			ps.setDouble(4, bean.getQuantity());
			ps.setInt(5, bean.getStatus());
			ps.setInt(6, bean.getModifyUser());
			ps.setString(7, bean.getMemo());

			ps.setInt(8, bean.getChangeId());

			result = db.executePstmtUpdate();
		} catch (Exception e) {
			log.error("", e);
			result = -1;
			throw e;
		} finally {
			try {
				db.close();
			} catch (SQLException sqle) {
				log.error("", sqle);
			}
		}
		return result;
	}

	public WarehouseInventoryChangeBean getWarehouseInventoryChangeByKey(int changeId) {
		WarehouseInventoryChangeBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_inventory_change where change_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, changeId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new WarehouseInventoryChangeBean();
				bean = BeanKit.resultSet2Bean(rs, WarehouseInventoryChangeBean.class);
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

	public PageResultBean<WarehouseInventoryChangeBean> getWarehouseInventoryChangePageListByMap(
			HashMap<String, String> condMap, PageBean pageBean) {
		PageResultBean<WarehouseInventoryChangeBean> result = new PageResultBean<WarehouseInventoryChangeBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_inventory_change";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理

			String orderQuery = "order by change_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, WarehouseInventoryChangeBean.class,
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

}
