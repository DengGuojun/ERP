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
import com.lpmas.erp.purchase.bean.DeliveryNoteInfoBean;
import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;

public class DeliveryNoteInfoDao {
	private static Logger log = LoggerFactory.getLogger(DeliveryNoteInfoDao.class);

	public int insertDeliveryNoteInfo(DeliveryNoteInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into delivery_note_info ( dn_number, po_id, delivery_time, receiving_time, transporter_type, transporter_id, transport_number, receiving_status, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getDnNumber());
			ps.setInt(2, bean.getPoId());
			ps.setTimestamp(3, bean.getDeliveryTime());
			ps.setTimestamp(4, bean.getReceivingTime());
			ps.setInt(5, bean.getTransporterType());
			ps.setInt(6, bean.getTransporterId());
			ps.setString(7, bean.getTransportNumber());
			ps.setString(8, bean.getReceivingStatus());
			ps.setInt(9, bean.getStatus());
			ps.setInt(10, bean.getCreateUser());
			ps.setString(11, bean.getMemo());

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

	public int updateDeliveryNoteInfo(DeliveryNoteInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update delivery_note_info set dn_number = ?, po_id = ?, delivery_time = ?, receiving_time = ?, transporter_type = ?, transporter_id = ?, transport_number = ?, receiving_status = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where dn_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getDnNumber());
			ps.setInt(2, bean.getPoId());
			ps.setTimestamp(3, bean.getDeliveryTime());
			ps.setTimestamp(4, bean.getReceivingTime());
			ps.setInt(5, bean.getTransporterType());
			ps.setInt(6, bean.getTransporterId());
			ps.setString(7, bean.getTransportNumber());
			ps.setString(8, bean.getReceivingStatus());
			ps.setInt(9, bean.getStatus());
			ps.setInt(10, bean.getModifyUser());
			ps.setString(11, bean.getMemo());

			ps.setInt(12, bean.getDnId());

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

	public DeliveryNoteInfoBean getDeliveryNoteInfoByKey(int dnId) {
		DeliveryNoteInfoBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from delivery_note_info where dn_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, dnId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new DeliveryNoteInfoBean();
				bean = BeanKit.resultSet2Bean(rs, DeliveryNoteInfoBean.class);
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

	public DeliveryNoteInfoBean getDeliveryNoteInfoByNumber(String dnNumber) {
		DeliveryNoteInfoBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from delivery_note_info where dn_number = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, dnNumber);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new DeliveryNoteInfoBean();
				bean = BeanKit.resultSet2Bean(rs, DeliveryNoteInfoBean.class);
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

	public PageResultBean<DeliveryNoteInfoBean> getDeliveryNoteInfoPageListByMap(HashMap<String, String> condMap, PageBean pageBean) {
		PageResultBean<DeliveryNoteInfoBean> result = new PageResultBean<DeliveryNoteInfoBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from delivery_note_info";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			String poId = condMap.get("poId");
			if (StringKit.isValid(poId)) {
				condList.add("po_id = ?");
				paramList.add(poId);
			}
			String dnNumber = condMap.get("dnNumber");
			if (StringKit.isValid(dnNumber)) {
				condList.add("dn_number like ?");
				paramList.add("%" + dnNumber + "%");
			}
			// 只显示状态为1的
			condList.add("status = ?");
			paramList.add("1");

			String orderQuery = "order by dn_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, DeliveryNoteInfoBean.class, pageBean, db);
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

	public List<DeliveryNoteInfoBean> getDeliveryNoteInfoListByPoId(int poId) {
		List<DeliveryNoteInfoBean> result = new ArrayList<DeliveryNoteInfoBean>();
		DeliveryNoteInfoBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from delivery_note_info where po_id=? order by dn_id desc";

			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, poId);

			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				bean = BeanKit.resultSet2Bean(rs, DeliveryNoteInfoBean.class);
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

	public int getDeliveryNoteInfoCountByPoId(int poId) {
		int result = 0;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectR();
			String sql = "select count(1) as numbers from delivery_note_info where po_id=?";
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
