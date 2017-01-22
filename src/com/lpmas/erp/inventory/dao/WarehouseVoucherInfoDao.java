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
import com.lpmas.erp.inventory.bean.WarehouseVoucherInfoBean;
import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.db.SqlKit;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;

public class WarehouseVoucherInfoDao {
	private static Logger log = LoggerFactory.getLogger(WarehouseVoucherInfoDao.class);

	public int insertWarehouseVoucherInfo(WarehouseVoucherInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into warehouse_voucher_info ( wv_number, wv_type, source_order_type, source_order_id, supplier_id, ware_type, stock_in_time, warehouse_id, inspector_name, receiver_name, account_clerk_name, approve_status, sync_status, wv_status, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getWvNumber());
			ps.setInt(2, bean.getWvType());
			ps.setInt(3, bean.getSourceOrderType());
			ps.setInt(4, bean.getSourceOrderId());
			ps.setInt(5, bean.getSupplierId());
			ps.setInt(6, bean.getWareType());
			ps.setTimestamp(7, bean.getStockInTime());
			ps.setInt(8, bean.getWarehouseId());
			ps.setString(9, bean.getInspectorName());
			ps.setString(10, bean.getReceiverName());
			ps.setString(11, bean.getAccountClerkName());
			ps.setString(12, bean.getApproveStatus());
			ps.setString(13, bean.getSyncStatus());
			ps.setString(14, bean.getWvStatus());
			ps.setInt(15, bean.getStatus());
			ps.setInt(16, bean.getCreateUser());
			ps.setString(17, bean.getMemo());

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

	public int insertWarehouseVoucherInfo(DBObject db, WarehouseVoucherInfoBean bean) throws Exception {
		int result = -1;
		String sql = "insert into warehouse_voucher_info ( wv_number, wv_type, source_order_type, source_order_id, supplier_id, ware_type, stock_in_time, warehouse_id, inspector_name, receiver_name, account_clerk_name, approve_status, sync_status, wv_status, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
		try {
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getWvNumber());
			ps.setInt(2, bean.getWvType());
			ps.setInt(3, bean.getSourceOrderType());
			ps.setInt(4, bean.getSourceOrderId());
			ps.setInt(5, bean.getSupplierId());
			ps.setInt(6, bean.getWareType());
			ps.setTimestamp(7, bean.getStockInTime());
			ps.setInt(8, bean.getWarehouseId());
			ps.setString(9, bean.getInspectorName());
			ps.setString(10, bean.getReceiverName());
			ps.setString(11, bean.getAccountClerkName());
			ps.setString(12, bean.getApproveStatus());
			ps.setString(13, bean.getSyncStatus());
			ps.setString(14, bean.getWvStatus());
			ps.setInt(15, bean.getStatus());
			ps.setInt(16, bean.getCreateUser());
			ps.setString(17, bean.getMemo());

			result = db.executePstmtInsert();
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return result;
	}

	public int updateWarehouseVoucherInfo(WarehouseVoucherInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update warehouse_voucher_info set wv_number = ?, wv_type = ?, source_order_type = ?, source_order_id = ?, supplier_id = ?, ware_type = ?, stock_in_time = ?, warehouse_id = ?, inspector_name = ?, receiver_name = ?, account_clerk_name = ?, approve_status = ?, sync_status = ?, wv_status = ?,  status = ?, modify_time = now(), modify_user = ?, memo = ? where wv_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getWvNumber());
			ps.setInt(2, bean.getWvType());
			ps.setInt(3, bean.getSourceOrderType());
			ps.setInt(4, bean.getSourceOrderId());
			ps.setInt(5, bean.getSupplierId());
			ps.setInt(6, bean.getWareType());
			ps.setTimestamp(7, bean.getStockInTime());
			ps.setInt(8, bean.getWarehouseId());
			ps.setString(9, bean.getInspectorName());
			ps.setString(10, bean.getReceiverName());
			ps.setString(11, bean.getAccountClerkName());
			ps.setString(12, bean.getApproveStatus());
			ps.setString(13, bean.getSyncStatus());
			ps.setString(14, bean.getWvStatus());
			ps.setInt(15, bean.getStatus());
			ps.setInt(16, bean.getModifyUser());
			ps.setString(17, bean.getMemo());

			ps.setInt(18, bean.getWvId());

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

	public int updateWarehouseVoucherInfo(DBObject db, WarehouseVoucherInfoBean bean) {
		int result = -1;
		try {
			String sql = "update warehouse_voucher_info set wv_number = ?, wv_type = ?, source_order_type = ?, source_order_id = ?, supplier_id = ?, ware_type = ?, stock_in_time = ?, warehouse_id = ?, inspector_name = ?, receiver_name = ?, account_clerk_name = ?, approve_status = ?, sync_status = ?, wv_status = ?,  status = ?, modify_time = now(), modify_user = ?, memo = ? where wv_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getWvNumber());
			ps.setInt(2, bean.getWvType());
			ps.setInt(3, bean.getSourceOrderType());
			ps.setInt(4, bean.getSourceOrderId());
			ps.setInt(5, bean.getSupplierId());
			ps.setInt(6, bean.getWareType());
			ps.setTimestamp(7, bean.getStockInTime());
			ps.setInt(8, bean.getWarehouseId());
			ps.setString(9, bean.getInspectorName());
			ps.setString(10, bean.getReceiverName());
			ps.setString(11, bean.getAccountClerkName());
			ps.setString(12, bean.getApproveStatus());
			ps.setString(13, bean.getSyncStatus());
			ps.setString(14, bean.getWvStatus());
			ps.setInt(15, bean.getStatus());
			ps.setInt(16, bean.getModifyUser());
			ps.setString(17, bean.getMemo());

			ps.setInt(18, bean.getWvId());

			result = db.executePstmtUpdate();
		} catch (Exception e) {
			log.error("", e);
			result = -1;
		}
		return result;
	}

	public WarehouseVoucherInfoBean getWarehouseVoucherInfoByKey(int wvId) {
		WarehouseVoucherInfoBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_voucher_info where wv_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, wvId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new WarehouseVoucherInfoBean();
				bean = BeanKit.resultSet2Bean(rs, WarehouseVoucherInfoBean.class);
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

	public WarehouseVoucherInfoBean getWarehouseVoucherInfoByNumber(String wvNumber) {
		WarehouseVoucherInfoBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_voucher_info where wv_number = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, wvNumber);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new WarehouseVoucherInfoBean();
				bean = BeanKit.resultSet2Bean(rs, WarehouseVoucherInfoBean.class);
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

	public PageResultBean<WarehouseVoucherInfoBean> getWarehouseVoucherInfoPageListByMap(
			HashMap<String, String> condMap, PageBean pageBean) {
		PageResultBean<WarehouseVoucherInfoBean> result = new PageResultBean<WarehouseVoucherInfoBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_voucher_info";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			condtionProcess(condList, paramList, condMap);

			String orderQuery = "order by wv_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, WarehouseVoucherInfoBean.class,
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

	public int getWarehhouseVoucherInfoCountForToday(int wvType) {
		int result = 0;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_voucher_info where create_time > curdate() and wv_type = ?";
			List<String> paramList = new ArrayList<String>();
			paramList.add(String.valueOf(wvType));
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

	public List<Integer> getWarehouseVoucherCreaterUserList(int status) {
		List<Integer> result = new ArrayList<Integer>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectR();
			String sql = "select create_user from warehouse_voucher_info where status = ? group by create_user";
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

	public List<WarehouseVoucherInfoBean> getWarehouseVoucherInfoListByMap(HashMap<String, String> condMap) {
		List<WarehouseVoucherInfoBean> list = new ArrayList<WarehouseVoucherInfoBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_voucher_info";
			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			condtionProcess(condList, paramList, condMap);

			String orderQuery = "order by wv_id desc";

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			list = dbExecutor.getRecordListResult(sql, orderQuery, condList, paramList, WarehouseVoucherInfoBean.class,
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

	public boolean checkHasWarehouseVoucherInfoByMap(Map<String, String> condMap) {
		int flage = 1;
		if (condMap.isEmpty())
			return false;

		boolean result = false;
		String condSql = MapKit.map2String(condMap, "=", " and ");
		condSql += " 1=1 ";

		String sql = "select " + flage + " from warehouse_voucher_info where " + condSql + " limit 1";
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

	private void condtionProcess(List<String> condList, List<String> paramList, Map<String, String> condMap) {
		String wareType = condMap.get("wareType");
		if (StringKit.isValid(wareType)) {
			condList.add("ware_type = ?");
			paramList.add(wareType);
		}
		String wvType = condMap.get("wvType");
		if (StringKit.isValid(wvType)) {
			condList.add("wv_type = ?");
			paramList.add(wvType);
		}
		String wvNumber = condMap.get("wvNumber");
		if (StringKit.isValid(wvNumber)) {
			condList.add("wv_number = ?");
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
			condList.add("wv_status = ?");
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
	}

	public List<WarehouseVoucherInfoBean> getWarehouseVoucherInfoListByWvIdList(List<Integer> wvIdList,
			PageBean pageBean) {
		List<WarehouseVoucherInfoBean> list = new ArrayList<WarehouseVoucherInfoBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			List<String> paramList = new ArrayList<String>();
			String inSql = SqlKit.getInQueryPreparedStmt(wvIdList.size());
			String sql = "select * from warehouse_voucher_info where wv_id in (" + inSql + ") order by wv_id desc";

			for (int i = 0; i < wvIdList.size(); i++) {
				paramList.add(String.valueOf(wvIdList.get(i)));
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			list = dbExecutor.getRecordListResult(sql, paramList, WarehouseVoucherInfoBean.class, pageBean, db);
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
