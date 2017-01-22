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
import com.lpmas.erp.inventory.bean.WarehouseVoucherItemBean;
import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;

public class WarehouseVoucherItemDao {
	private static Logger log = LoggerFactory.getLogger(WarehouseVoucherItemDao.class);

	public int insertWarehouseVoucherItem(WarehouseVoucherItemBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into warehouse_voucher_item ( wv_id, ware_type, ware_id, unit, receivable_quantity, stock_in_quantity, defect_quantity, batch_number, production_date, guarantee_period, expiration_date, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getWvId());
			ps.setInt(2, bean.getWareType());
			ps.setInt(3, bean.getWareId());
			ps.setString(4, bean.getUnit());
			ps.setDouble(5, bean.getReceivableQuantity());
			ps.setDouble(6, bean.getStockInQuantity());
			ps.setDouble(7, bean.getDefectQuantity());
			ps.setString(8, bean.getBatchNumber());
			ps.setTimestamp(9, bean.getProductionDate());
			ps.setDouble(10, bean.getGuaranteePeriod());
			ps.setTimestamp(11, bean.getExpirationDate());
			ps.setInt(12, bean.getStatus());
			ps.setInt(13, bean.getCreateUser());
			ps.setString(14, bean.getMemo());

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

	public int insertWarehouseVoucherItem(DBObject db, WarehouseVoucherItemBean bean) throws Exception {
		int result = -1;
		String sql = "insert into warehouse_voucher_item ( wv_id, ware_type, ware_id, unit, receivable_quantity, stock_in_quantity, defect_quantity, batch_number, production_date, guarantee_period, expiration_date, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
		try {
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getWvId());
			ps.setInt(2, bean.getWareType());
			ps.setInt(3, bean.getWareId());
			ps.setString(4, bean.getUnit());
			ps.setDouble(5, bean.getReceivableQuantity());
			ps.setDouble(6, bean.getStockInQuantity());
			ps.setDouble(7, bean.getDefectQuantity());
			ps.setString(8, bean.getBatchNumber());
			ps.setTimestamp(9, bean.getProductionDate());
			ps.setDouble(10, bean.getGuaranteePeriod());
			ps.setTimestamp(11, bean.getExpirationDate());
			ps.setInt(12, bean.getStatus());
			ps.setInt(13, bean.getCreateUser());
			ps.setString(14, bean.getMemo());

			result = db.executePstmtInsert();
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return result;
	}

	public int updateWarehouseVoucherItem(WarehouseVoucherItemBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update warehouse_voucher_item set wv_id = ?, ware_type = ?, ware_id = ?, unit = ?, receivable_quantity = ?, stock_in_quantity = ?, defect_quantity = ?, batch_number = ?, production_date = ?, guarantee_period = ?, expiration_date = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where wv_item_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getWvId());
			ps.setInt(2, bean.getWareType());
			ps.setInt(3, bean.getWareId());
			ps.setString(4, bean.getUnit());
			ps.setDouble(5, bean.getReceivableQuantity());
			ps.setDouble(6, bean.getStockInQuantity());
			ps.setDouble(7, bean.getDefectQuantity());
			ps.setString(8, bean.getBatchNumber());
			ps.setTimestamp(9, bean.getProductionDate());
			ps.setDouble(10, bean.getGuaranteePeriod());
			ps.setTimestamp(11, bean.getExpirationDate());
			ps.setInt(12, bean.getStatus());
			ps.setInt(13, bean.getModifyUser());
			ps.setString(14, bean.getMemo());

			ps.setInt(15, bean.getWvItemId());

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

	public WarehouseVoucherItemBean getWarehouseVoucherItemByKey(int wvItmeId) {
		WarehouseVoucherItemBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_voucher_item where wv_item_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, wvItmeId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new WarehouseVoucherItemBean();
				bean = BeanKit.resultSet2Bean(rs, WarehouseVoucherItemBean.class);
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

	public List<WarehouseVoucherItemBean> getWarehouseVoucherItemListByMap(HashMap<String, String> condMap) {
		List<WarehouseVoucherItemBean> result = new ArrayList<WarehouseVoucherItemBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_voucher_item ";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			String wvId = condMap.get("wvId");
			if (StringKit.isValid(wvId)) {
				condList.add("wv_id = ?");
				paramList.add(wvId);
			}
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}
			String orderQuery = "order by ware_id desc";

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getRecordListResult(sql, orderQuery, condList, paramList,
					WarehouseVoucherItemBean.class, db);
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

	public PageResultBean<WarehouseVoucherItemBean> getWarehouseVoucherItemPageListByMap(
			HashMap<String, String> condMap, PageBean pageBean) {
		PageResultBean<WarehouseVoucherItemBean> result = new PageResultBean<WarehouseVoucherItemBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_voucher_item";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理

			String orderQuery = "order by wv_itme_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, WarehouseVoucherItemBean.class,
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
