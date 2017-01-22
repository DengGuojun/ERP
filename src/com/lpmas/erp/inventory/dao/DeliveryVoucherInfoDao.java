package com.lpmas.erp.inventory.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.erp.factory.ErpDBFactory;
import com.lpmas.erp.inventory.bean.DeliveryVoucherInfoBean;
import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;

public class DeliveryVoucherInfoDao {

	private static Logger log = LoggerFactory.getLogger(DeliveryVoucherInfoDao.class);

	public int insertDeliveryVoucherInfo(DeliveryVoucherInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into delivery_voucher_info ( dv_number, dv_type, source_order_type, source_order_id, ware_type, stock_out_time, warehouse_id, inspector_name, sender_name, account_clerk_name, approve_status, sync_status, dv_status, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getDvNumber());
			ps.setInt(2, bean.getDvType());
			ps.setInt(3, bean.getSourceOrderType());
			ps.setInt(4, bean.getSourceOrderId());
			ps.setInt(5, bean.getWareType());
			ps.setTimestamp(6, bean.getStockOutTime());
			ps.setInt(7, bean.getWarehouseId());
			ps.setString(8, bean.getInspectorName());
			ps.setString(9, bean.getSenderName());
			ps.setString(10, bean.getAccountClerkName());
			ps.setString(11, bean.getApproveStatus());
			ps.setString(12, bean.getSyncStatus());
			ps.setString(13, bean.getDvStatus());
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

	public int insertDeliveryVoucherInfo(DBObject db, DeliveryVoucherInfoBean bean) throws Exception {
		int result = -1;
		String sql = "insert into delivery_voucher_info ( dv_number, dv_type, source_order_type, source_order_id, ware_type, stock_out_time, warehouse_id, inspector_name, sender_name, account_clerk_name, approve_status, sync_status, dv_status, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
		try {
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getDvNumber());
			ps.setInt(2, bean.getDvType());
			ps.setInt(3, bean.getSourceOrderType());
			ps.setInt(4, bean.getSourceOrderId());
			ps.setInt(5, bean.getWareType());
			ps.setTimestamp(6, bean.getStockOutTime());
			ps.setInt(7, bean.getWarehouseId());
			ps.setString(8, bean.getInspectorName());
			ps.setString(9, bean.getSenderName());
			ps.setString(10, bean.getAccountClerkName());
			ps.setString(11, bean.getApproveStatus());
			ps.setString(12, bean.getSyncStatus());
			ps.setString(13, bean.getDvStatus());
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

	public int updateDeliveryVoucherInfo(DeliveryVoucherInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update delivery_voucher_info set dv_number = ?, dv_type = ?, source_order_type = ?, source_order_id = ?, ware_type = ?, stock_out_time = ?, warehouse_id = ?, inspector_name = ?, sender_name = ?, account_clerk_name = ?, approve_status = ?, sync_status = ?, dv_status = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where dv_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getDvNumber());
			ps.setInt(2, bean.getDvType());
			ps.setInt(3, bean.getSourceOrderType());
			ps.setInt(4, bean.getSourceOrderId());
			ps.setInt(5, bean.getWareType());
			ps.setTimestamp(6, bean.getStockOutTime());
			ps.setInt(7, bean.getWarehouseId());
			ps.setString(8, bean.getInspectorName());
			ps.setString(9, bean.getSenderName());
			ps.setString(10, bean.getAccountClerkName());
			ps.setString(11, bean.getApproveStatus());
			ps.setString(12, bean.getSyncStatus());
			ps.setString(13, bean.getDvStatus());
			ps.setInt(14, bean.getStatus());
			ps.setInt(15, bean.getModifyUser());
			ps.setString(16, bean.getMemo());

			ps.setInt(17, bean.getDvId());

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

	public int updateDeliveryVoucherInfo(DBObject db, DeliveryVoucherInfoBean bean) {
		int result = -1;
		try {
			String sql = "update delivery_voucher_info set dv_number = ?, dv_type = ?, source_order_type = ?, source_order_id = ?, ware_type = ?, stock_out_time = ?, warehouse_id = ?, inspector_name = ?, sender_name = ?, account_clerk_name = ?, approve_status = ?, sync_status = ?, dv_status = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where dv_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getDvNumber());
			ps.setInt(2, bean.getDvType());
			ps.setInt(3, bean.getSourceOrderType());
			ps.setInt(4, bean.getSourceOrderId());
			ps.setInt(5, bean.getWareType());
			ps.setTimestamp(6, bean.getStockOutTime());
			ps.setInt(7, bean.getWarehouseId());
			ps.setString(8, bean.getInspectorName());
			ps.setString(9, bean.getSenderName());
			ps.setString(10, bean.getAccountClerkName());
			ps.setString(11, bean.getApproveStatus());
			ps.setString(12, bean.getSyncStatus());
			ps.setString(13, bean.getDvStatus());
			ps.setInt(14, bean.getStatus());
			ps.setInt(15, bean.getModifyUser());
			ps.setString(16, bean.getMemo());

			ps.setInt(17, bean.getDvId());

			result = db.executePstmtUpdate();
		} catch (Exception e) {
			log.error("", e);
			result = -1;
		}
		return result;
	}

	public DeliveryVoucherInfoBean getDeliveryVoucherInfoByKey(int dvId) {
		DeliveryVoucherInfoBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from delivery_voucher_info where dv_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, dvId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new DeliveryVoucherInfoBean();
				bean = BeanKit.resultSet2Bean(rs, DeliveryVoucherInfoBean.class);
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

	public PageResultBean<DeliveryVoucherInfoBean> getDeliveryVoucherInfoPageListByMap(HashMap<String, String> condMap, PageBean pageBean) {
		PageResultBean<DeliveryVoucherInfoBean> result = new PageResultBean<DeliveryVoucherInfoBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from delivery_voucher_info";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			String wareType = condMap.get("wareType");
			if (StringKit.isValid(wareType)) {
				condList.add("ware_type = ?");
				paramList.add(wareType);
			}
			String wvType = condMap.get("dvType");
			if (StringKit.isValid(wvType)) {
				condList.add("dv_type = ?");
				paramList.add(wvType);
			}
			String wvNumber = condMap.get("dvNumber");
			if (StringKit.isValid(wvNumber)) {
				condList.add("dv_number = ?");
				paramList.add(wvNumber);
			}
			String sourceOrderId = condMap.get("sourceOrderId");
			if (StringKit.isValid(sourceOrderId)) {
				condList.add("source_order_id = ?");
				paramList.add(sourceOrderId);
			}
			String reviewStatus = condMap.get("reviewStatus");
			if (StringKit.isValid(reviewStatus)) {
				condList.add("approve_status = ?");
				paramList.add(reviewStatus);
			}
			String ncStatus = condMap.get("ncStatus");
			if (StringKit.isValid(ncStatus)) {
				condList.add("sync_status = ?");
				paramList.add(ncStatus);
			}
			String poStatus = condMap.get("poStatus");
			if (StringKit.isValid(poStatus)) {
				condList.add("dv_status = ?");
				paramList.add(poStatus);
			}
			String warehouseId = condMap.get("warehouseId");
			if (StringKit.isValid(warehouseId)) {
				condList.add("warehouse_id = ?");
				paramList.add(warehouseId);
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

			String orderQuery = "order by dv_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, DeliveryVoucherInfoBean.class, pageBean, db);
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

	public int getDeliveryVoucherInfoCountForToday(int dvType) {
		int result = 0;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from delivery_voucher_info where create_time > curdate() and dv_type = ?";
			List<String> paramList = new ArrayList<String>();
			paramList.add(String.valueOf(dvType));
			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getTotalRecordResult(sql, paramList, db);
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

	public List<DeliveryVoucherInfoBean> getDeliveryVoucherInfoListByMap(HashMap<String, String> condMap) {
		List<DeliveryVoucherInfoBean> list = new ArrayList<DeliveryVoucherInfoBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from delivery_voucher_info";
			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}
			String warehouseId = condMap.get("warehouseId");
			if (StringKit.isValid(warehouseId)) {
				condList.add("warehouse_id = ?");
				paramList.add(warehouseId);
			}
			String orderQuery = "order by dv_id desc";

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			list = dbExecutor.getRecordListResult(sql, orderQuery, condList, paramList, DeliveryVoucherInfoBean.class, db);
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

	public boolean checkHasDeliveryVoucherInfoByMap(Map<String, String> condMap) {
		int flage = 1;
		if (condMap.isEmpty())
			return false;

		boolean result = false;
		String condSql = MapKit.map2String(condMap, "=", " and ");
		condSql += " 1=1 ";

		String sql = "select " + flage + " from delivery_voucher_info where " + condSql + " limit 1";
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectR();
			ResultSet resultSet = db.executeQuery(sql);
			if (resultSet.next()) {
				int has = resultSet.getInt(0);
				if (has == flage) {
					result = true;
				}
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
