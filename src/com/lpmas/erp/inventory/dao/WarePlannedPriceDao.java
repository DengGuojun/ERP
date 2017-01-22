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
import com.lpmas.erp.inventory.bean.WarePlannedPriceBean;
import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;

public class WarePlannedPriceDao {
	private static Logger log = LoggerFactory.getLogger(WarePlannedPriceDao.class);

	public int insertWarePlannedPrice(WarePlannedPriceBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into ware_planned_price ( ware_type, ware_id, plan_month, unit, price, create_time, create_user) value( ?, ?, ?, ?, ?, now(), ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bean.getWareType());
			ps.setInt(2, bean.getWareId());
			ps.setString(3, bean.getPlanMonth());
			ps.setString(4, bean.getUnit());
			ps.setDouble(5, bean.getPrice());
			ps.setInt(6, bean.getCreateUser());

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

	public int updateWarePlannedPrice(WarePlannedPriceBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update ware_planned_price set price = ?, modify_time = now(), modify_user = ? where ware_type = ? and ware_id = ? and plan_month = ? and unit = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setDouble(1, bean.getPrice());
			ps.setInt(2, bean.getModifyUser());

			ps.setInt(3, bean.getWareType());
			ps.setInt(4, bean.getWareId());
			ps.setString(5, bean.getPlanMonth());
			ps.setString(6, bean.getUnit());

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

	public WarePlannedPriceBean getWarePlannedPriceByKey(int wareType, int wareId, String planMonth, String unit) {
		WarePlannedPriceBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from ware_planned_price where ware_type = ? and ware_id = ? and plan_month = ? and unit = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, wareType);
			ps.setInt(2, wareId);
			ps.setString(3, planMonth);
			ps.setString(4, unit);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new WarePlannedPriceBean();
				bean = BeanKit.resultSet2Bean(rs, WarePlannedPriceBean.class);
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

	public PageResultBean<WarePlannedPriceBean> getWarePlannedPricePageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		PageResultBean<WarePlannedPriceBean> result = new PageResultBean<WarePlannedPriceBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from ware_planned_price";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			String wareId = condMap.get("wareId");
			if (StringKit.isValid(wareId)) {
				condList.add("ware_id =  ?");
				paramList.add(wareId);
			}
			String wareType = condMap.get("wareType");
			if (StringKit.isValid(wareType)) {
				condList.add("ware_type =  ?");
				paramList.add(wareType);
			}
			String unit = condMap.get("unit");
			if (StringKit.isValid(unit)) {
				condList.add("unit = ?");
				paramList.add(unit);
			}
			String planMonth = condMap.get("planMonth");
			if (StringKit.isValid(planMonth)) {
				condList.add("plan_month = ?");
				paramList.add(planMonth);
			}

			String orderQuery = "order by unit,plan_month,ware_id,ware_type desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, WarePlannedPriceBean.class,
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
