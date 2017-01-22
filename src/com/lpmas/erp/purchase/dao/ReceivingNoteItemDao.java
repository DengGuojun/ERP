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
import com.lpmas.erp.purchase.bean.ReceivingNoteItemBean;
import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;

public class ReceivingNoteItemDao {
	private static Logger log = LoggerFactory.getLogger(ReceivingNoteItemDao.class);

	public int insertReceivingNoteItem(ReceivingNoteItemBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into receiving_note_item ( rn_id, dn_item_id, receive_quantity, status, create_time, create_user, memo) value( ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getRnId());
			ps.setInt(2, bean.getDnItemId());
			ps.setDouble(3, bean.getReceiveQuantity());
			ps.setInt(4, bean.getStatus());
			ps.setInt(5, bean.getCreateUser());
			ps.setString(6, bean.getMemo());

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

	public int updateReceivingNoteItem(ReceivingNoteItemBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update receiving_note_item set rn_id = ?, dn_item_id = ?, receive_quantity = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where rn_item_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getRnId());
			ps.setInt(2, bean.getDnItemId());
			ps.setDouble(3, bean.getReceiveQuantity());
			ps.setInt(4, bean.getStatus());
			ps.setInt(5, bean.getModifyUser());
			ps.setString(6, bean.getMemo());

			ps.setInt(7, bean.getRnItemId());

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

	public ReceivingNoteItemBean getReceivingNoteItemByKey(int rnItemId) {
		ReceivingNoteItemBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from receiving_note_item where rn_item_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, rnItemId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new ReceivingNoteItemBean();
				bean = BeanKit.resultSet2Bean(rs, ReceivingNoteItemBean.class);
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

	public PageResultBean<ReceivingNoteItemBean> getReceivingNoteItemPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		PageResultBean<ReceivingNoteItemBean> result = new PageResultBean<ReceivingNoteItemBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from receiving_note_item";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理

			String orderQuery = "order by rn_item_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, ReceivingNoteItemBean.class,
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

	public List<ReceivingNoteItemBean> getReceivingNoteItemListByRnId(int rnId) {
		List<ReceivingNoteItemBean> result = new ArrayList<ReceivingNoteItemBean>();
		ReceivingNoteItemBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from receiving_note_item where rn_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, rnId);

			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				bean = BeanKit.resultSet2Bean(rs, ReceivingNoteItemBean.class);
				result.add(bean);
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
