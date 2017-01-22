package com.lpmas.erp.bom.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.erp.bom.bean.BomItemBean;
import com.lpmas.erp.factory.ErpDBFactory;
import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;

public class BomItemDao {
	private static Logger log = LoggerFactory.getLogger(BomItemDao.class);

	public int insertBomItem(BomItemBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into bom_item ( bom_id, usage_type, ware_type, ware_id, unit, quantity, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getBomId());
			ps.setInt(2, bean.getUsageType());
			ps.setInt(3, bean.getWareType());
			ps.setInt(4, bean.getWareId());
			ps.setString(5, bean.getUnit());
			ps.setDouble(6, bean.getQuantity());
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

	public int updateBomItem(BomItemBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update bom_item set bom_id = ?, usage_type = ?, ware_type = ?, ware_id = ?, unit = ?, quantity = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where bom_item_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getBomId());
			ps.setInt(2, bean.getUsageType());
			ps.setInt(3, bean.getWareType());
			ps.setInt(4, bean.getWareId());
			ps.setString(5, bean.getUnit());
			ps.setDouble(6, bean.getQuantity());
			ps.setInt(7, bean.getStatus());
			ps.setInt(8, bean.getModifyUser());
			ps.setString(9, bean.getMemo());

			ps.setInt(10, bean.getBomItemId());

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

	public BomItemBean getBomItemByKey(int bomItemId) {
		BomItemBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from bom_item where bom_item_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bomItemId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new BomItemBean();
				bean = BeanKit.resultSet2Bean(rs, BomItemBean.class);
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

	public PageResultBean<BomItemBean> getBomItemPageListByMap(HashMap<String, String> condMap, PageBean pageBean) {
		PageResultBean<BomItemBean> result = new PageResultBean<BomItemBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from bom_item";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理

			String orderQuery = "order by bom_item_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, BomItemBean.class, pageBean, db);
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

	public List<BomItemBean> getBomItemListByMap(HashMap<String, String> condMap) {
		List<BomItemBean> result = new ArrayList<BomItemBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from bom_item";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			String bomId = condMap.get("bomId");
			if (StringKit.isValid(bomId)) {
				condList.add("bom_id = ?");
				paramList.add(bomId);
			}
			String wareType = condMap.get("wareType");
			if (StringKit.isValid(wareType)) {
				condList.add("ware_type = ?");
				paramList.add(wareType);
			}
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}
			String orderQuery = "order by bom_item_id desc";

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getRecordListResult(sql, orderQuery, condList, paramList, BomItemBean.class, db);
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
