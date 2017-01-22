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
import com.lpmas.erp.purchase.bean.ReceivingNoteInfoBean;
import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;

public class ReceivingNoteInfoDao {
	private static Logger log = LoggerFactory.getLogger(ReceivingNoteInfoDao.class);

	public int insertReceivingNoteInfo(ReceivingNoteInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into receiving_note_info ( rn_number, dn_id, po_id, receiving_time, receiving_status, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getRnNumber());
			ps.setInt(2, bean.getDnId());
			ps.setInt(3, bean.getPoId());
			ps.setTimestamp(4, bean.getReceivingTime());
			ps.setString(5, bean.getReceivingStatus());
			ps.setInt(6, bean.getStatus());
			ps.setInt(7, bean.getCreateUser());
			ps.setString(8, bean.getMemo());

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

	public int updateReceivingNoteInfo(ReceivingNoteInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update receiving_note_info set rn_number = ?, dn_id = ?, po_id = ?, receiving_time = ?, receiving_status = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where rn_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getRnNumber());
			ps.setInt(2, bean.getDnId());
			ps.setInt(3, bean.getPoId());
			ps.setTimestamp(4, bean.getReceivingTime());
			ps.setString(5, bean.getReceivingStatus());
			ps.setInt(6, bean.getStatus());
			ps.setInt(7, bean.getModifyUser());
			ps.setString(8, bean.getMemo());

			ps.setInt(9, bean.getRnId());

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

	public ReceivingNoteInfoBean getReceivingNoteInfoByKey(int rnId) {
		ReceivingNoteInfoBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from receiving_note_info where rn_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, rnId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new ReceivingNoteInfoBean();
				bean = BeanKit.resultSet2Bean(rs, ReceivingNoteInfoBean.class);
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

	public PageResultBean<ReceivingNoteInfoBean> getReceivingNoteInfoPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		PageResultBean<ReceivingNoteInfoBean> result = new PageResultBean<ReceivingNoteInfoBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from receiving_note_info";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理

			String orderQuery = "order by rn_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, ReceivingNoteInfoBean.class,
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

	public ReceivingNoteInfoBean getReceivingNoteInfoByDnId(int dnId) {
		ReceivingNoteInfoBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from receiving_note_info where dn_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, dnId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new ReceivingNoteInfoBean();
				bean = BeanKit.resultSet2Bean(rs, ReceivingNoteInfoBean.class);
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

	public List<ReceivingNoteInfoBean> getReceivingNoteInfoListByPoId(int poId) {
		List<ReceivingNoteInfoBean> result = new ArrayList<ReceivingNoteInfoBean>();
		ReceivingNoteInfoBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from receiving_note_info where po_id=? order by rn_id desc";

			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, poId);

			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				bean = BeanKit.resultSet2Bean(rs, ReceivingNoteInfoBean.class);
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

	public int getReceivingNoteInfoCountByPoId(int poId) {
		int result = 0;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectR();
			String sql = "select count(1) as numbers from receiving_note_info where po_id=?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, poId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				result = rs.getInt("numbers");
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
