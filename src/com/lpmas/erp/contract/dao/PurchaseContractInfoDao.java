package com.lpmas.erp.contract.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.erp.contract.bean.PurchaseContractInfoBean;
import com.lpmas.erp.factory.ErpDBFactory;
import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;

public class PurchaseContractInfoDao {
	private static Logger log = LoggerFactory.getLogger(PurchaseContractInfoDao.class);

	public int insertPurchaseContractInfo(PurchaseContractInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into purchase_contract_info ( pc_name, pc_number, contract_type, supplier_id, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getPcName());
			ps.setString(2, bean.getPcNumber());
			ps.setInt(3, bean.getContractType());
			ps.setInt(4, bean.getSupplierId());
			ps.setInt(5, bean.getStatus());
			ps.setInt(6, bean.getCreateUser());
			ps.setString(7, bean.getMemo());

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

	public int updatePurchaseContractInfo(PurchaseContractInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update purchase_contract_info set pc_name = ?, pc_number = ?, contract_type = ?, supplier_id = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where pc_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getPcName());
			ps.setString(2, bean.getPcNumber());
			ps.setInt(3, bean.getContractType());
			ps.setInt(4, bean.getSupplierId());
			ps.setInt(5, bean.getStatus());
			ps.setInt(6, bean.getModifyUser());
			ps.setString(7, bean.getMemo());

			ps.setInt(8, bean.getPcId());

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

	public PurchaseContractInfoBean getPurchaseContractInfoByKey(int pcId) {
		PurchaseContractInfoBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from purchase_contract_info where pc_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, pcId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new PurchaseContractInfoBean();
				bean = BeanKit.resultSet2Bean(rs, PurchaseContractInfoBean.class);
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

	public PageResultBean<PurchaseContractInfoBean> getPurchaseContractInfoPageListByMap(
			HashMap<String, String> condMap, PageBean pageBean) {
		PageResultBean<PurchaseContractInfoBean> result = new PageResultBean<PurchaseContractInfoBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from purchase_contract_info";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			String pcName = condMap.get("pcName");
			if (StringKit.isValid(pcName)) {
				condList.add("pc_name like ?");
				paramList.add("%" + pcName + "%");
			}
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}

			String orderQuery = "order by pc_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, PurchaseContractInfoBean.class,
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

	public PageResultBean<PurchaseContractInfoBean> getPurchaseContractInfoPageListByFuzzyQueryParam(String param,
			PageBean pageBean) {
		PageResultBean<PurchaseContractInfoBean> result = new PageResultBean<PurchaseContractInfoBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from purchase_contract_info where concat(pc_name,pc_number) like ? and status =1";
			String orderQuery = "order by pc_id desc";
			List<String> paramList = new ArrayList<String>();
			paramList.add("%" + param + "%");
			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, paramList, PurchaseContractInfoBean.class, pageBean, db);
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
