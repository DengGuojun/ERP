package com.lpmas.erp.bom.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.erp.bom.bean.BomInfoBean;
import com.lpmas.erp.factory.ErpDBFactory;
import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;

public class BomInfoDao {
	private static Logger log = LoggerFactory.getLogger(BomInfoDao.class);

	public int insertBomInfo(BomInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into bom_info ( bom_name, bom_number, bom_type, is_active, use_start_time, use_end_time, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getBomName());
			ps.setString(2, bean.getBomNumber());
			ps.setInt(3, bean.getBomType());
			ps.setInt(4, bean.getIsActive());
			ps.setTimestamp(5, bean.getUseStartTime());
			ps.setTimestamp(6, bean.getUseEndTime());
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

	public int updateBomInfo(BomInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update bom_info set bom_name = ?, bom_number = ?, bom_type = ?, is_active = ?, use_start_time = ?, use_end_time = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where bom_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getBomName());
			ps.setString(2, bean.getBomNumber());
			ps.setInt(3, bean.getBomType());
			ps.setInt(4, bean.getIsActive());
			ps.setTimestamp(5, bean.getUseStartTime());
			ps.setTimestamp(6, bean.getUseEndTime());
			ps.setInt(7, bean.getStatus());
			ps.setInt(8, bean.getModifyUser());
			ps.setString(9, bean.getMemo());

			ps.setInt(10, bean.getBomId());

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

	public BomInfoBean getBomInfoByKey(int bomId) {
		BomInfoBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from bom_info where bom_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, bomId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new BomInfoBean();
				bean = BeanKit.resultSet2Bean(rs, BomInfoBean.class);
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

	public PageResultBean<BomInfoBean> getBomInfoPageListByMap(HashMap<String, String> condMap, PageBean pageBean) {
		PageResultBean<BomInfoBean> result = new PageResultBean<BomInfoBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from bom_info";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			String bomName = condMap.get("bomName");
			if (StringKit.isValid(bomName)) {
				condList.add("bom_name like ?");
				paramList.add("%" + bomName + "%");
			}
			String bomNumber = condMap.get("bomNumber");
			if (StringKit.isValid(bomNumber)) {
				condList.add("bom_number = ?");
				paramList.add(bomNumber);
			}
			String isActive = condMap.get("isActive");
			if (StringKit.isValid(isActive)) {
				condList.add("is_active = ?");
				paramList.add(isActive);
			}
			String bomType = condMap.get("bomType");
			if (StringKit.isValid(bomType)) {
				condList.add("bom_type = ?");
				paramList.add(bomType);
			}
			String useStartTime = condMap.get("useStartTime");
			if (StringKit.isValid(useStartTime)) {
				condList.add("use_start_time >= ?");
				paramList.add(useStartTime);
			}
			String useEndTime = condMap.get("useEndTime");
			if (StringKit.isValid(useEndTime)) {
				condList.add("use_end_time <= ?");
				paramList.add(useEndTime);
			}
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}
			
			String orderQuery = "order by bom_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, BomInfoBean.class, pageBean, db);
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
