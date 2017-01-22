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
import com.lpmas.erp.purchase.bean.DeliveryNoteItemBean;
import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;

public class DeliveryNoteItemDao {
	private static Logger log = LoggerFactory.getLogger(DeliveryNoteItemDao.class);

	public int insertDeliveryNoteItem(DeliveryNoteItemBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into delivery_note_item ( dn_id, ware_type, ware_id, ware_number, unit, delivery_quantiry, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getDnId());
			ps.setInt(2, bean.getWareType());
			ps.setInt(3, bean.getWareId());
			ps.setString(4, bean.getWareNumber());
			ps.setString(5, bean.getUnit());
			ps.setDouble(6, bean.getDeliveryQuantiry());
			ps.setInt(7, bean.getStatus());
			ps.setInt(8, bean.getCreateUser());
			ps.setString(9, bean.getMemo());

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

	public int updateDeliveryNoteItem(DeliveryNoteItemBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update delivery_note_item set dn_id = ?, ware_type = ?, ware_id = ?, ware_number = ?, unit = ?, delivery_quantiry = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where dn_item_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getDnId());
			ps.setInt(2, bean.getWareType());
			ps.setInt(3, bean.getWareId());
			ps.setString(4, bean.getWareNumber());
			ps.setString(5, bean.getUnit());
			ps.setDouble(6, bean.getDeliveryQuantiry());
			ps.setInt(7, bean.getStatus());
			ps.setInt(8, bean.getModifyUser());
			ps.setString(9, bean.getMemo());

			ps.setInt(10, bean.getDnItemId());

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

	public DeliveryNoteItemBean getDeliveryNoteItemByKey(int dnItemId) {
		DeliveryNoteItemBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from delivery_note_item where dn_item_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, dnItemId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new DeliveryNoteItemBean();
				bean = BeanKit.resultSet2Bean(rs, DeliveryNoteItemBean.class);
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

	public PageResultBean<DeliveryNoteItemBean> getDeliveryNoteItemPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		PageResultBean<DeliveryNoteItemBean> result = new PageResultBean<DeliveryNoteItemBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from delivery_note_item";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理

			String orderQuery = "order by dn_item_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, DeliveryNoteItemBean.class,
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

	public List<DeliveryNoteItemBean> getDeliveryNoteItemListByDnId(int dnId) {
		List<DeliveryNoteItemBean> result = new ArrayList<DeliveryNoteItemBean>();
		DeliveryNoteItemBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from delivery_note_item where dn_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, dnId);

			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				bean = BeanKit.resultSet2Bean(rs, DeliveryNoteItemBean.class);
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
}
