package com.lpmas.erp.purchase.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.erp.factory.ErpDBFactory;
import com.lpmas.erp.purchase.bean.PurchaseOrderItemBean;
import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;

public class PurchaseOrderItemDao {
	private static Logger log = LoggerFactory.getLogger(PurchaseOrderItemDao.class);

	public int insertPurchaseOrderItem(PurchaseOrderItemBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into purchase_order_item ( po_id, ware_type, ware_id, ware_number, currency, unit, quatity, unit_price, total_amount, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getPoId());
			ps.setInt(2, bean.getWareType());
			ps.setInt(3, bean.getWareId());
			ps.setString(4, bean.getWareNumber());
			ps.setString(5, bean.getCurrency());
			ps.setString(6, bean.getUnit());
			ps.setDouble(7, bean.getQuatity());
			ps.setDouble(8, bean.getUnitPrice());
			ps.setDouble(9, bean.getTotalAmount());
			ps.setInt(10, bean.getStatus());
			ps.setInt(11, bean.getCreateUser());
			ps.setString(12, bean.getMemo());

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

	public int updatePurchaseOrderItem(PurchaseOrderItemBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update purchase_order_item set po_id = ?, ware_type = ?, ware_id = ?, ware_number = ?, currency = ?, unit = ?, quatity = ?, unit_price = ?, total_amount = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where item_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getPoId());
			ps.setInt(2, bean.getWareType());
			ps.setInt(3, bean.getWareId());
			ps.setString(4, bean.getWareNumber());
			ps.setString(5, bean.getCurrency());
			ps.setString(6, bean.getUnit());
			ps.setDouble(7, bean.getQuatity());
			ps.setDouble(8, bean.getUnitPrice());
			ps.setDouble(9, bean.getTotalAmount());
			ps.setInt(10, bean.getStatus());
			ps.setInt(11, bean.getModifyUser());
			ps.setString(12, bean.getMemo());

			ps.setInt(13, bean.getItemId());

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

	public PurchaseOrderItemBean getPurchaseOrderItemByKey(int itemId) {
		PurchaseOrderItemBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from purchase_order_item where item_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, itemId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new PurchaseOrderItemBean();
				bean = BeanKit.resultSet2Bean(rs, PurchaseOrderItemBean.class);
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

	public PageResultBean<PurchaseOrderItemBean> getPurchaseOrderItemPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		PageResultBean<PurchaseOrderItemBean> result = new PageResultBean<PurchaseOrderItemBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from purchase_order_item";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理

			String orderQuery = "order by item_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, PurchaseOrderItemBean.class,
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

	public List<PurchaseOrderItemBean> getPurchaseOrderItemListByMap(HashMap<String, String> condMap) {
		List<PurchaseOrderItemBean> result = new ArrayList<PurchaseOrderItemBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from purchase_order_item";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			String poId = condMap.get("poId");
			if (StringKit.isValid(poId)) {
				condList.add("po_id = ?");
				paramList.add(poId);
			}
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}
			String orderQuery = "order by item_id desc";

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getRecordListResult(sql, orderQuery, condList, paramList, PurchaseOrderItemBean.class,
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
