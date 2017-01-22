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
import com.lpmas.erp.inventory.bean.WarehouseInventoryLogBean;
import com.lpmas.erp.report.bean.WarehouseInventoryLogSumResultBean;
import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.db.SqlKit;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;

public class WarehouseInventoryLogDao {
	private static Logger log = LoggerFactory.getLogger(WarehouseInventoryLogDao.class);

	public int insertWarehouseInventoryLog(DBObject db, WarehouseInventoryLogBean bean) throws Exception {
		int result = -1;
		try {
			String sql = "insert into warehouse_inventory_log (wi_id, change_type, source_id, quantity, create_time, create_user) value( ?, ?, ?, ?, now(), ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getWiId());
			ps.setInt(2, bean.getChangeType());
			ps.setInt(3, bean.getSourceId());
			ps.setDouble(4, bean.getQuantity());
			ps.setInt(5, bean.getCreateUser());

			result = db.executePstmtInsert();
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return result;
	}

	public int updateWarehouseInventoryLog(WarehouseInventoryLogBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update warehouse_inventory_log set wi_id = ?, change_type = ?, source_id = ?, quantity = ? where log_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getWiId());
			ps.setInt(2, bean.getChangeType());
			ps.setInt(3, bean.getSourceId());
			ps.setDouble(4, bean.getQuantity());

			ps.setInt(5, bean.getLogId());

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

	public WarehouseInventoryLogBean getWarehouseInventoryLogByKey(int logId) {
		WarehouseInventoryLogBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_inventory_log where log_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, logId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new WarehouseInventoryLogBean();
				bean = BeanKit.resultSet2Bean(rs, WarehouseInventoryLogBean.class);
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

	public List<WarehouseInventoryLogSumResultBean> getWarehouseInventoryLogSumResultListByCondition(int wiId,
			String endDayTime) {
		List<WarehouseInventoryLogSumResultBean> result = new ArrayList<WarehouseInventoryLogSumResultBean>(1);
		WarehouseInventoryLogSumResultBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select change_type,sum(quantity) as sum_quantity from warehouse_inventory_log where wi_id = ? and create_time <= ? group by change_type";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, wiId);
			ps.setString(2, endDayTime);

			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				bean = BeanKit.resultSet2Bean(rs, WarehouseInventoryLogSumResultBean.class);
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

	public List<WarehouseInventoryLogSumResultBean> getWarehouseInventoryLogSumResultListByCondition(int wiId,
			String openDayTime, String endDayTime) {
		List<WarehouseInventoryLogSumResultBean> result = new ArrayList<WarehouseInventoryLogSumResultBean>(1);
		WarehouseInventoryLogSumResultBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select change_type,sum(quantity) as sum_quantity from warehouse_inventory_log where wi_id = ? and create_time >= ? and create_time <= ? group by change_type";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, wiId);
			ps.setString(2, openDayTime);
			ps.setString(3, endDayTime);

			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				bean = BeanKit.resultSet2Bean(rs, WarehouseInventoryLogSumResultBean.class);
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

	public WarehouseInventoryLogSumResultBean getWarehouseInventoryLogSumResultBeanByCondition(List<Integer> wiIdList,
			List<Integer> changeTypeList, String startTime, String endTime) {
		WarehouseInventoryLogSumResultBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;

		if (changeTypeList.isEmpty())
			return null;
		String inSql = "";
		inSql = "(" + SqlKit.getQueryStmt(changeTypeList) + ")";
		
		if (wiIdList.isEmpty())
			return null;
		String wiInSql = "";
		wiInSql = "(" + SqlKit.getQueryStmt(wiIdList) + ")";
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select sum(quantity) as sum_quantity from warehouse_inventory_log where wi_id in "+wiInSql+" and create_time >= ? and create_time <= ? and change_type in "
					+ inSql;
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, startTime);
			ps.setString(2, endTime);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = BeanKit.resultSet2Bean(rs, WarehouseInventoryLogSumResultBean.class);
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

	public WarehouseInventoryLogSumResultBean getWarehouseInventoryLogSumResultBeanByCondition(List<Integer> wiIdList,
			List<Integer> changeTypeList, String lastTime) {
		WarehouseInventoryLogSumResultBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;

		if (changeTypeList.isEmpty())
			return null;
		String inSql = "";
		inSql = "(" + SqlKit.getQueryStmt(changeTypeList) + ")";

		if (wiIdList.isEmpty())
			return null;
		String wiInSql = "";
		wiInSql = "(" + SqlKit.getQueryStmt(wiIdList) + ")";

		try {
			db = dbFactory.getDBObjectR();
			String sql = "select sum(quantity) as sum_quantity from warehouse_inventory_log where wi_id in " + wiInSql
					+ " and create_time <= ? and change_type in " + inSql;
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, lastTime);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = BeanKit.resultSet2Bean(rs, WarehouseInventoryLogSumResultBean.class);
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

	public List<WarehouseInventoryLogBean> getWarehouseInventoryLogListByCondition(List<Integer> wiIdList,
			String startTime, String endTime) {
		WarehouseInventoryLogBean bean = null;
		List<WarehouseInventoryLogBean> result = new ArrayList<WarehouseInventoryLogBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;

		if (wiIdList.isEmpty())
			return result;
		String wiInSql = "";
		wiInSql = "(" + SqlKit.getQueryStmt(wiIdList) + ")";

		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_inventory_log where wi_id in " + wiInSql
					+ " and create_time >= ? and create_time <= ? order by create_time";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, startTime);
			ps.setString(2, endTime);

			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				bean = BeanKit.resultSet2Bean(rs, WarehouseInventoryLogBean.class);
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

	public PageResultBean<WarehouseInventoryLogBean> getWarehouseInventoryLogPageListByMap(
			HashMap<String, String> condMap, PageBean pageBean) {
		PageResultBean<WarehouseInventoryLogBean> result = new PageResultBean<WarehouseInventoryLogBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_inventory_log";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理

			String orderQuery = "order by log_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, WarehouseInventoryLogBean.class,
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

	public List<WarehouseInventoryLogBean> getWarehouseInventoryLogListByMap(HashMap<String, String> condMap) {
		List<WarehouseInventoryLogBean> result = new ArrayList<WarehouseInventoryLogBean>();

		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_inventory_log";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			String createTime = condMap.get("createTime");
			if (StringKit.isValid(createTime)) {
				condList.add("create_time <= ?");
				paramList.add(createTime);
			}

			String orderQuery = "order by log_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getRecordListResult(sql, orderQuery, condList, paramList,
					WarehouseInventoryLogBean.class, db);
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
