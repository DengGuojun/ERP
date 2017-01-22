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
import com.lpmas.erp.inventory.bean.WarehouseTransferOrderInfoBean;
import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;

public class WarehouseTransferOrderInfoDao {
	private static Logger log = LoggerFactory.getLogger(WarehouseTransferOrderInfoDao.class);

	public int insertWarehouseTransferOrderInfo(WarehouseTransferOrderInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into warehouse_transfer_order_info ( to_number, source_warehouse_id, target_warehouse_id, ware_type, dv_inspector_name, dv_sender_name, dv_account_clerk_name, wv_inspector_name, wv_receiver_name, wv_account_clerk_name, approve_status, sync_status, to_status, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getToNumber());
			ps.setInt(2, bean.getSourceWarehouseId());
			ps.setInt(3, bean.getTargetWarehouseId());
			ps.setInt(4, bean.getWareType());
			ps.setString(5, bean.getDvInspectorName());
			ps.setString(6, bean.getDvSenderName());
			ps.setString(7, bean.getDvAccountClerkName());
			ps.setString(8, bean.getWvInspectorName());
			ps.setString(9, bean.getWvReceiverName());
			ps.setString(10, bean.getWvAccountClerkName());
			ps.setString(11, bean.getApproveStatus());
			ps.setString(12, bean.getSyncStatus());
			ps.setString(13, bean.getToStatus());
			ps.setInt(14, bean.getStatus());
			ps.setInt(15, bean.getCreateUser());
			ps.setString(16, bean.getMemo());

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

	public int insertWarehouseTransferOrderInfo(DBObject db, WarehouseTransferOrderInfoBean bean) throws Exception {
		int result = -1;
		try {
			String sql = "insert into warehouse_transfer_order_info ( to_number, source_warehouse_id, target_warehouse_id, ware_type, dv_inspector_name, dv_sender_name, dv_account_clerk_name, wv_inspector_name, wv_receiver_name, wv_account_clerk_name, approve_status, sync_status, to_status, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getToNumber());
			ps.setInt(2, bean.getSourceWarehouseId());
			ps.setInt(3, bean.getTargetWarehouseId());
			ps.setInt(4, bean.getWareType());
			ps.setString(5, bean.getDvInspectorName());
			ps.setString(6, bean.getDvSenderName());
			ps.setString(7, bean.getDvAccountClerkName());
			ps.setString(8, bean.getWvInspectorName());
			ps.setString(9, bean.getWvReceiverName());
			ps.setString(10, bean.getWvAccountClerkName());
			ps.setString(11, bean.getApproveStatus());
			ps.setString(12, bean.getSyncStatus());
			ps.setString(13, bean.getToStatus());
			ps.setInt(14, bean.getStatus());
			ps.setInt(15, bean.getCreateUser());
			ps.setString(16, bean.getMemo());

			result = db.executePstmtInsert();
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return result;
	}

	public int updateWarehouseTransferOrderInfo(WarehouseTransferOrderInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update warehouse_transfer_order_info set to_number = ?, source_warehouse_id = ?, target_warehouse_id = ?, ware_type = ?, dv_inspector_name = ?, dv_sender_name = ?, dv_account_clerk_name = ?, wv_inspector_name = ?, wv_receiver_name = ?, wv_account_clerk_name = ?, approve_status = ?, sync_status = ?, to_status = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where to_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getToNumber());
			ps.setInt(2, bean.getSourceWarehouseId());
			ps.setInt(3, bean.getTargetWarehouseId());
			ps.setInt(4, bean.getWareType());
			ps.setString(5, bean.getDvInspectorName());
			ps.setString(6, bean.getDvSenderName());
			ps.setString(7, bean.getDvAccountClerkName());
			ps.setString(8, bean.getWvInspectorName());
			ps.setString(9, bean.getWvReceiverName());
			ps.setString(10, bean.getWvAccountClerkName());
			ps.setString(11, bean.getApproveStatus());
			ps.setString(12, bean.getSyncStatus());
			ps.setString(13, bean.getToStatus());
			ps.setInt(14, bean.getStatus());
			ps.setInt(15, bean.getModifyUser());
			ps.setString(16, bean.getMemo());

			ps.setInt(17, bean.getToId());

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

	public int updateWarehouseTransferOrderInfo(DBObject db, WarehouseTransferOrderInfoBean bean) throws Exception {
		int result = -1;
		try {
			String sql = "update warehouse_transfer_order_info set to_number = ?, source_warehouse_id = ?, target_warehouse_id = ?, ware_type = ?, dv_inspector_name = ?, dv_sender_name = ?, dv_account_clerk_name = ?, wv_inspector_name = ?, wv_receiver_name = ?, wv_account_clerk_name = ?, approve_status = ?, sync_status = ?, to_status = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where to_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getToNumber());
			ps.setInt(2, bean.getSourceWarehouseId());
			ps.setInt(3, bean.getTargetWarehouseId());
			ps.setInt(4, bean.getWareType());
			ps.setString(5, bean.getDvInspectorName());
			ps.setString(6, bean.getDvSenderName());
			ps.setString(7, bean.getDvAccountClerkName());
			ps.setString(8, bean.getWvInspectorName());
			ps.setString(9, bean.getWvReceiverName());
			ps.setString(10, bean.getWvAccountClerkName());
			ps.setString(11, bean.getApproveStatus());
			ps.setString(12, bean.getSyncStatus());
			ps.setString(13, bean.getToStatus());
			ps.setInt(14, bean.getStatus());
			ps.setInt(15, bean.getModifyUser());
			ps.setString(16, bean.getMemo());

			ps.setInt(17, bean.getToId());

			result = db.executePstmtUpdate();
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return result;
	}

	public WarehouseTransferOrderInfoBean getWarehouseTransferOrderInfoByKey(int toId) {
		WarehouseTransferOrderInfoBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_transfer_order_info where to_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, toId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new WarehouseTransferOrderInfoBean();
				bean = BeanKit.resultSet2Bean(rs, WarehouseTransferOrderInfoBean.class);
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

	public WarehouseTransferOrderInfoBean getWarehouseTransferOrderInfoByNumber(String toNumber) {
		WarehouseTransferOrderInfoBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_transfer_order_info where to_number = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, toNumber);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new WarehouseTransferOrderInfoBean();
				bean = BeanKit.resultSet2Bean(rs, WarehouseTransferOrderInfoBean.class);
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

	public int getWarehouseTransferOrderInfoCountForToday() {
		int result = 0;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select count(1) from warehouse_transfer_order_info where create_time >= curdate()";
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				result = rs.getInt(1);
			}
			rs.close();
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

	public PageResultBean<WarehouseTransferOrderInfoBean> getWarehouseTransferOrderInfoPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		PageResultBean<WarehouseTransferOrderInfoBean> result = new PageResultBean<WarehouseTransferOrderInfoBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_transfer_order_info";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			String wareType = condMap.get("wareType");
			if (StringKit.isValid(wareType)) {
				condList.add("ware_type = ?");
				paramList.add(wareType);
			}
			String wvNumber = condMap.get("toNumber");
			if (StringKit.isValid(wvNumber)) {
				condList.add("to_number like ?");
				paramList.add("%" + wvNumber + "%");
			}
			String reviewStatus = condMap.get("reviewStatus");
			if (StringKit.isValid(reviewStatus)) {
				condList.add("approve_status = ?");
				paramList.add(reviewStatus);
			}
			String poStatus = condMap.get("toStatus");
			if (StringKit.isValid(poStatus)) {
				condList.add("to_status = ?");
				paramList.add(poStatus);
			}
			String sourceWarehouseId = condMap.get("sourceWarehouseId");
			if (StringKit.isValid(sourceWarehouseId)) {
				condList.add("source_warehouse_id = ?");
				paramList.add(sourceWarehouseId);
			}
			String targetWarehouseId = condMap.get("targetWarehouseId");
			if (StringKit.isValid(targetWarehouseId)) {
				condList.add("target_warehouse_id = ?");
				paramList.add(targetWarehouseId);
			}
			String createUser = condMap.get("createUser");
			if (StringKit.isValid(createUser)) {
				condList.add("create_user = ?");
				paramList.add(createUser);
			}
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}
			String orderQuery = "order by to_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, WarehouseTransferOrderInfoBean.class, pageBean, db);
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

	public List<Integer> getWarehouseTransferOrderCreaterUserList(int status) {
		List<Integer> result = new ArrayList<Integer>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectR();
			String sql = "select create_user from warehouse_transfer_order_info where status = ? group by create_user";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, status);
			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				result.add(rs.getInt("create_user"));
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
