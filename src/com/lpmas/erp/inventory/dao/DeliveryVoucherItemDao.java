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
import com.lpmas.erp.inventory.bean.DeliveryVoucherItemBean;
import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;

public class DeliveryVoucherItemDao {
	private static Logger log = LoggerFactory.getLogger(DeliveryVoucherItemDao.class);

	public int insertDeliveryVoucherItem(DeliveryVoucherItemBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into delivery_voucher_item ( dv_id, ware_type, ware_id, unit, inventory_type, stock_out_quantity, batch_number, production_date, guarantee_period, expiration_date, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getDvId());
			ps.setInt(2, bean.getWareType());
			ps.setInt(3, bean.getWareId());
			ps.setString(4, bean.getUnit());
			ps.setInt(5, bean.getInventoryType());
			ps.setDouble(6, bean.getStockOutQuantity());
			ps.setString(7, bean.getBatchNumber());
			ps.setTimestamp(8, bean.getProductionDate());
			ps.setDouble(9, bean.getGuaranteePeriod());
			ps.setTimestamp(10, bean.getExpirationDate());
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

	public int insertDeliveryVoucherItem(DBObject db, DeliveryVoucherItemBean bean) throws Exception {
		int result = -1;
		String sql = "insert into delivery_voucher_item ( dv_id, ware_type, ware_id, unit, inventory_type, stock_out_quantity, batch_number, production_date, guarantee_period, expiration_date, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
		try {
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getDvId());
			ps.setInt(2, bean.getWareType());
			ps.setInt(3, bean.getWareId());
			ps.setString(4, bean.getUnit());
			ps.setInt(5, bean.getInventoryType());
			ps.setDouble(6, bean.getStockOutQuantity());
			ps.setString(7, bean.getBatchNumber());
			ps.setTimestamp(8, bean.getProductionDate());
			ps.setDouble(9, bean.getGuaranteePeriod());
			ps.setTimestamp(10, bean.getExpirationDate());
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

	public int updateDeliveryVoucherItem(DeliveryVoucherItemBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update delivery_voucher_item set dv_id = ?, ware_type = ?, ware_id = ?, unit = ?, inventory_type = ?, stock_out_quantity = ?, batch_number = ?, production_date = ?, guarantee_period = ?, expiration_date = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where dv_item_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getDvId());
			ps.setInt(2, bean.getWareType());
			ps.setInt(3, bean.getWareId());
			ps.setString(4, bean.getUnit());
			ps.setInt(5, bean.getInventoryType());
			ps.setDouble(6, bean.getStockOutQuantity());
			ps.setString(7, bean.getBatchNumber());
			ps.setTimestamp(8, bean.getProductionDate());
			ps.setDouble(9, bean.getGuaranteePeriod());
			ps.setTimestamp(10, bean.getExpirationDate());
			ps.setInt(11, bean.getStatus());
			ps.setInt(12, bean.getModifyUser());
			ps.setString(13, bean.getMemo());

			ps.setInt(14, bean.getDvItemId());

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

	public DeliveryVoucherItemBean getDeliveryVoucherItemByKey(int dvItemId) {
		DeliveryVoucherItemBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from delivery_voucher_item where dv_item_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, dvItemId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new DeliveryVoucherItemBean();
				bean = BeanKit.resultSet2Bean(rs, DeliveryVoucherItemBean.class);
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

	public PageResultBean<DeliveryVoucherItemBean> getDeliveryVoucherItemPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		PageResultBean<DeliveryVoucherItemBean> result = new PageResultBean<DeliveryVoucherItemBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from delivery_voucher_item";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理

			String orderQuery = "order by dv_item_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, DeliveryVoucherItemBean.class,
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

	public List<DeliveryVoucherItemBean> getDeliveryVoucherItemListByMap(HashMap<String, String> condMap) {
		List<DeliveryVoucherItemBean> result = new ArrayList<DeliveryVoucherItemBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from delivery_voucher_item";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			String dvId = condMap.get("dvId");
			if (StringKit.isValid(dvId)) {
				condList.add("dv_id = ?");
				paramList.add(dvId);
			}
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}

			String orderQuery = "order by dv_item_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getRecordListResult(sql, orderQuery, condList, paramList, DeliveryVoucherItemBean.class,
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
		return result;
	}

}
