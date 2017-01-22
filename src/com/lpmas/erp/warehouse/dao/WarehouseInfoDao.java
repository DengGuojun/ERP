package com.lpmas.erp.warehouse.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.erp.factory.ErpDBFactory;
import com.lpmas.erp.warehouse.bean.WarehouseInfoBean;
import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.db.SqlKit;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;

public class WarehouseInfoDao {
	private static Logger log = LoggerFactory.getLogger(WarehouseInfoDao.class);

	public int insertWarehouseInfo(WarehouseInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into warehouse_info ( warehouse_name, warehouse_number, warehouse_type, country, province, city, region, address, contact_name, zip_code, telephone, mobile, warehouse_status, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getWarehouseName());
			ps.setString(2, bean.getWarehouseNumber());
			ps.setString(3, bean.getWarehouseType());
			ps.setString(4, bean.getCountry());
			ps.setString(5, bean.getProvince());
			ps.setString(6, bean.getCity());
			ps.setString(7, bean.getRegion());
			ps.setString(8, bean.getAddress());
			ps.setString(9, bean.getContactName());
			ps.setString(10, bean.getZipCode());
			ps.setString(11, bean.getTelephone());
			ps.setString(12, bean.getMobile());
			ps.setString(13, bean.getWarehouseStatus());
			ps.setInt(14, bean.getStatus());
			ps.setInt(15, bean.getCreateUser());
			ps.setString(16, bean.getMemo());

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

	public int updateWarehouseInfo(WarehouseInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update warehouse_info set warehouse_name = ?, warehouse_number = ?, warehouse_type = ?, country = ?, province = ?, city = ?, region = ?, address = ?, contact_name = ?, zip_code = ?, telephone = ?, mobile = ?, warehouse_status = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where warehouse_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getWarehouseName());
			ps.setString(2, bean.getWarehouseNumber());
			ps.setString(3, bean.getWarehouseType());
			ps.setString(4, bean.getCountry());
			ps.setString(5, bean.getProvince());
			ps.setString(6, bean.getCity());
			ps.setString(7, bean.getRegion());
			ps.setString(8, bean.getAddress());
			ps.setString(9, bean.getContactName());
			ps.setString(10, bean.getZipCode());
			ps.setString(11, bean.getTelephone());
			ps.setString(12, bean.getMobile());
			ps.setString(13, bean.getWarehouseStatus());
			ps.setInt(14, bean.getStatus());
			ps.setInt(15, bean.getModifyUser());
			ps.setString(16, bean.getMemo());

			ps.setInt(17, bean.getWarehouseId());

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

	public WarehouseInfoBean getWarehouseInfoByKey(int warehouseId) {
		WarehouseInfoBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_info where warehouse_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, warehouseId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new WarehouseInfoBean();
				bean = BeanKit.resultSet2Bean(rs, WarehouseInfoBean.class);
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

	public WarehouseInfoBean getWarehouseInfoByNumber(String warehouseNumber) {
		WarehouseInfoBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_info where warehouse_number = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, warehouseNumber);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new WarehouseInfoBean();
				bean = BeanKit.resultSet2Bean(rs, WarehouseInfoBean.class);
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

	public List<WarehouseInfoBean> getWarehouseListConditon(String warehouseType, List<Integer> warehouseIdList) {
		List<WarehouseInfoBean> result = new ArrayList<WarehouseInfoBean>(1);
		WarehouseInfoBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_info where warehouse_type = ? and warehouse_id in (" + SqlKit.getQueryStmt(warehouseIdList) + ")";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, warehouseType);

			ResultSet rs = db.executePstmtQuery();
			while (rs.next()) {
				bean = BeanKit.resultSet2Bean(rs, WarehouseInfoBean.class);
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

	public PageResultBean<WarehouseInfoBean> getWarehouseInfoPageListByMap(HashMap<String, String> condMap, PageBean pageBean) {
		PageResultBean<WarehouseInfoBean> result = new PageResultBean<WarehouseInfoBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_info";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			processCond(condList, paramList, condMap);

			String orderQuery = "order by warehouse_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, WarehouseInfoBean.class, pageBean, db);
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

	public PageResultBean<WarehouseInfoBean> getWarehouseInfoPageListByFuzzyQueryParam(String param, PageBean pageBean) {
		PageResultBean<WarehouseInfoBean> result = new PageResultBean<WarehouseInfoBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_info where concat(warehouse_name,warehouse_number,warehouse_type) like ? and status =1";
			String orderQuery = "order by warehouse_id desc";
			List<String> paramList = new ArrayList<String>();
			paramList.add("%" + param + "%");
			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, paramList, WarehouseInfoBean.class, pageBean, db);
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

	public List<WarehouseInfoBean> getWarehouseInfoListByMap(HashMap<String, String> condMap) {
		List<WarehouseInfoBean> result = new ArrayList<WarehouseInfoBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from warehouse_info";
			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			processCond(condList, paramList, condMap);

			String orderQuery = "order by warehouse_id desc";

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getRecordListResult(sql, orderQuery, condList, paramList, WarehouseInfoBean.class, db);
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

	private void processCond(List<String> condList, List<String> paramList, HashMap<String, String> condMap) {
		String warehouseName = condMap.get("warehouseName");
		if (StringKit.isValid(warehouseName)) {
			condList.add("warehouse_name like ?");
			paramList.add("%" + warehouseName + "%");
		}
		String status = condMap.get("status");
		if (StringKit.isValid(status)) {
			condList.add("status = ?");
			paramList.add(status);
		}
		String warehouseIds = condMap.get("warehouseIds");
		if (StringKit.isValid(warehouseIds)) {
			condList.add("warehouse_id in (" + warehouseIds + ")");
		}
	}

}
