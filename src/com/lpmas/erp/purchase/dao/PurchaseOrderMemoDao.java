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
import com.lpmas.erp.purchase.bean.PurchaseOrderMemoBean;
import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;

public class PurchaseOrderMemoDao {
	private static Logger log = LoggerFactory.getLogger(PurchaseOrderMemoDao.class);

	public int insertPurchaseOrderMemo(PurchaseOrderMemoBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into purchase_order_memo ( po_id, memo_content, create_time, create_user) value( ?, ?, now(), ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getPoId());
			ps.setString(2, bean.getMemoContent());
			ps.setInt(3, bean.getCreateUser());

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

	public int updatePurchaseOrderMemo(PurchaseOrderMemoBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update purchase_order_memo set po_id = ?, memo_content = ? where memo_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getPoId());
			ps.setString(2, bean.getMemoContent());

			ps.setInt(3, bean.getMemoId());

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

	public PurchaseOrderMemoBean getPurchaseOrderMemoByKey(int memoId) {
		PurchaseOrderMemoBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from purchase_order_memo where memo_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, memoId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new PurchaseOrderMemoBean();
				bean = BeanKit.resultSet2Bean(rs, PurchaseOrderMemoBean.class);
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

	public PageResultBean<PurchaseOrderMemoBean> getPurchaseOrderMemoPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		PageResultBean<PurchaseOrderMemoBean> result = new PageResultBean<PurchaseOrderMemoBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from purchase_order_memo";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			String poId = condMap.get("poId");
			if (StringKit.isValid(poId)) {
				condList.add("po_id = ?");
				paramList.add(poId);
			}

			String orderQuery = "order by memo_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, PurchaseOrderMemoBean.class,
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

	public List<PurchaseOrderMemoBean> getPurchaseOrderMemoListByPoId(int poId) {
		List<PurchaseOrderMemoBean> result = new ArrayList<PurchaseOrderMemoBean>();
		PurchaseOrderMemoBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from purchase_order_memo where po_id = ? order by memo_id desc";

			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, poId);

			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				bean = BeanKit.resultSet2Bean(rs, PurchaseOrderMemoBean.class);
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
