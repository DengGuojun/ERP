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
import com.lpmas.erp.inventory.bean.WarehouseTransferOrderItemBean;
import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;

public class WarehouseTransferOrderItemDao {
	private static Logger log = LoggerFactory.getLogger(WarehouseTransferOrderItemDao.class);

	public int insertWarehouseTransferOrderItem(WarehouseTransferOrderItemBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into warehouse_transfer_order_item ( to_id, ware_type, ware_id, unit, quanlity, batch_number, production_date, guarantee_period, expiration_date, to_item_status, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getToId());
			ps.setInt(2, bean.getWareType());
			ps.setInt(3, bean.getWareId());
			ps.setString(4, bean.getUnit());
			ps.setDouble(5, bean.getQuanlity());
			ps.setString(6, bean.getBatchNumber());
			ps.setTimestamp(7, bean.getProductionDate());
			ps.setDouble(8, bean.getGuaranteePeriod());
			ps.setTimestamp(9, bean.getExpirationDate());
			ps.setString(10, bean.getToItemStatus());
			ps.setInt(11, bean.getStatus());
			ps.setInt(12, bean.getCreateUser());
			ps.setString(13, bean.getMemo());

			result = db.executePstmtInsert();
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

	public int updateWarehouseTransferOrderItem(WarehouseTransferOrderItemBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update warehouse_transfer_order_item set to_id = ?, ware_type = ?, ware_id = ?, unit = ?, quanlity = ?, batch_number = ?, production_date = ?, guarantee_period = ?, expiration_date = ?, to_item_status = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where to_item_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getToId());
			ps.setInt(2, bean.getWareType());
			ps.setInt(3, bean.getWareId());
			ps.setString(4, bean.getUnit());
			ps.setDouble(5, bean.getQuanlity());
			ps.setString(6, bean.getBatchNumber());
			ps.setTimestamp(7, bean.getProductionDate());
			ps.setDouble(8, bean.getGuaranteePeriod());
			ps.setTimestamp(9, bean.getExpirationDate());
			ps.setString(10, bean.getToItemStatus());
			ps.setInt(11, bean.getStatus());
			ps.setInt(12, bean.getModifyUser());
			ps.setString(13, bean.getMemo());

			ps.setInt(14, bean.getToItemId());

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

	public WarehouseTransferOrderItemBean getWarehouseTransferOrderItemByKey(int toItemId) {
		WarehouseTransferOrderItemBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_transfer_order_item where to_item_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, toItemId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new WarehouseTransferOrderItemBean();
				bean = BeanKit.resultSet2Bean(rs, WarehouseTransferOrderItemBean.class);
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

	public List<WarehouseTransferOrderItemBean> getTransferOrderItemListByToId(int toId) {
		WarehouseTransferOrderItemBean bean = null;
		List<WarehouseTransferOrderItemBean> result = new ArrayList<WarehouseTransferOrderItemBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_transfer_order_item where to_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, toId);

			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				bean = BeanKit.resultSet2Bean(rs, WarehouseTransferOrderItemBean.class);
				result.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			log.error("", e);
			result = new ArrayList<WarehouseTransferOrderItemBean>(1);
		} finally {
			try {
				db.close();
			} catch (SQLException sqle) {
				log.error("", sqle);
			}
		}
		return result;
	}

	public PageResultBean<WarehouseTransferOrderItemBean> getWarehouseTransferOrderItemPageListByMap(
			HashMap<String, String> condMap, PageBean pageBean) {
		PageResultBean<WarehouseTransferOrderItemBean> result = new PageResultBean<WarehouseTransferOrderItemBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_transfer_order_item";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理

			String orderQuery = "order by to_item_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList,
					WarehouseTransferOrderItemBean.class, pageBean, db);
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

	public List<WarehouseTransferOrderItemBean> getWarehouseTransferOrderItemListByMap(
			HashMap<String, String> condMap) {
		List<WarehouseTransferOrderItemBean> result = new ArrayList<WarehouseTransferOrderItemBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_transfer_order_item";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			String dvId = condMap.get("toId");
			if (StringKit.isValid(dvId)) {
				condList.add("to_id = ?");
				paramList.add(dvId);
			}
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}

			String orderQuery = "order by to_item_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getRecordListResult(sql, orderQuery, condList, paramList,
					WarehouseTransferOrderItemBean.class, db);
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
