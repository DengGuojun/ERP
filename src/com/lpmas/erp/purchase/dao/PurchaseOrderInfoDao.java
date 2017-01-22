package com.lpmas.erp.purchase.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.erp.factory.ErpDBFactory;
import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.config.PurchaseOrderConfig;
import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;

public class PurchaseOrderInfoDao {
	private static Logger log = LoggerFactory.getLogger(PurchaseOrderInfoDao.class);

	public int insertPurchaseOrderInfo(PurchaseOrderInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "insert into purchase_order_info ( po_number, ware_type, plan_id, purchase_type, contract_id, supplier_id, consigner_name, consigner_country, consigner_province, consigner_city, consigner_region, consigner_address, consigner_telephone, consigner_mobile, receiver_type, receiver_id, receiver_name, receiver_country, receiver_province, receiver_city, receiver_region, receiver_address, receiver_telephone, receiver_mobile, currency, po_amount, contract_status, invoice_status, payment_status, approval_status, po_status, status, create_time, create_user, memo) value( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getPoNumber());
			ps.setInt(2, bean.getWareType());
			ps.setInt(3, bean.getPlanId());
			ps.setInt(4, bean.getPurchaseType());
			ps.setInt(5, bean.getContractId());
			ps.setInt(6, bean.getSupplierId());
			ps.setString(7, bean.getConsignerName());
			ps.setString(8, bean.getConsignerCountry());
			ps.setString(9, bean.getConsignerProvince());
			ps.setString(10, bean.getConsignerCity());
			ps.setString(11, bean.getConsignerRegion());
			ps.setString(12, bean.getConsignerAddress());
			ps.setString(13, bean.getConsignerTelephone());
			ps.setString(14, bean.getConsignerMobile());
			ps.setInt(15, bean.getReceiverType());
			ps.setInt(16, bean.getReceiverId());
			ps.setString(17, bean.getReceiverName());
			ps.setString(18, bean.getReceiverCountry());
			ps.setString(19, bean.getReceiverProvince());
			ps.setString(20, bean.getReceiverCity());
			ps.setString(21, bean.getReceiverRegion());
			ps.setString(22, bean.getReceiverAddress());
			ps.setString(23, bean.getReceiverTelephone());
			ps.setString(24, bean.getReceiverMobile());
			ps.setString(25, bean.getCurrency());
			ps.setDouble(26, bean.getPoAmount());
			ps.setString(27, bean.getContractStatus());
			ps.setString(28, bean.getInvoiceStatus());
			ps.setString(29, bean.getPaymentStatus());
			ps.setString(30, bean.getApprovalStatus());
			ps.setString(31, bean.getPoStatus());
			ps.setInt(32, bean.getStatus());
			ps.setInt(33, bean.getCreateUser());
			ps.setString(34, bean.getMemo());

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

	public int updatePurchaseOrderInfo(PurchaseOrderInfoBean bean) {
		int result = -1;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			String sql = "update purchase_order_info set po_number = ?, ware_type = ?, plan_id = ?, purchase_type = ?, contract_id = ?, supplier_id = ?, consigner_name = ?, consigner_country = ?, consigner_province = ?, consigner_city = ?, consigner_region = ?, consigner_address = ?, consigner_telephone = ?, consigner_mobile = ?, receiver_type = ?, receiver_id = ?, receiver_name = ?, receiver_country = ?, receiver_province = ?, receiver_city = ?, receiver_region = ?, receiver_address = ?, receiver_telephone = ?, receiver_mobile = ?, currency = ?, po_amount = ?, contract_status = ?, invoice_status = ?, payment_status = ?, approval_status = ?, po_status = ?, status = ?, modify_time = now(), modify_user = ?, memo = ? where po_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, bean.getPoNumber());
			ps.setInt(2, bean.getWareType());
			ps.setInt(3, bean.getPlanId());
			ps.setInt(4, bean.getPurchaseType());
			ps.setInt(5, bean.getContractId());
			ps.setInt(6, bean.getSupplierId());
			ps.setString(7, bean.getConsignerName());
			ps.setString(8, bean.getConsignerCountry());
			ps.setString(9, bean.getConsignerProvince());
			ps.setString(10, bean.getConsignerCity());
			ps.setString(11, bean.getConsignerRegion());
			ps.setString(12, bean.getConsignerAddress());
			ps.setString(13, bean.getConsignerTelephone());
			ps.setString(14, bean.getConsignerMobile());
			ps.setInt(15, bean.getReceiverType());
			ps.setInt(16, bean.getReceiverId());
			ps.setString(17, bean.getReceiverName());
			ps.setString(18, bean.getReceiverCountry());
			ps.setString(19, bean.getReceiverProvince());
			ps.setString(20, bean.getReceiverCity());
			ps.setString(21, bean.getReceiverRegion());
			ps.setString(22, bean.getReceiverAddress());
			ps.setString(23, bean.getReceiverTelephone());
			ps.setString(24, bean.getReceiverMobile());
			ps.setString(25, bean.getCurrency());
			ps.setDouble(26, bean.getPoAmount());
			ps.setString(27, bean.getContractStatus());
			ps.setString(28, bean.getInvoiceStatus());
			ps.setString(29, bean.getPaymentStatus());
			ps.setString(30, bean.getApprovalStatus());
			ps.setString(31, bean.getPoStatus());
			ps.setInt(32, bean.getStatus());
			ps.setInt(33, bean.getModifyUser());
			ps.setString(34, bean.getMemo());
			ps.setInt(35, bean.getPoId());

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

	public PurchaseOrderInfoBean getPurchaseOrderInfoByKey(int poId) {
		PurchaseOrderInfoBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from purchase_order_info where po_id = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setInt(1, poId);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new PurchaseOrderInfoBean();
				bean = BeanKit.resultSet2Bean(rs, PurchaseOrderInfoBean.class);
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

	public PurchaseOrderInfoBean getPurchaseOrderInfoByNumber(String Number) {
		PurchaseOrderInfoBean bean = null;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from purchase_order_info where po_number = ?";
			PreparedStatement ps = db.getPreparedStatement(sql);
			ps.setString(1, Number);

			ResultSet rs = db.executePstmtQuery();
			if (rs.next()) {
				bean = new PurchaseOrderInfoBean();
				bean = BeanKit.resultSet2Bean(rs, PurchaseOrderInfoBean.class);
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

	public PageResultBean<PurchaseOrderInfoBean> getPurchaseOrderInfoPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		PageResultBean<PurchaseOrderInfoBean> result = new PageResultBean<PurchaseOrderInfoBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from purchase_order_info";

			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			// 条件处理
			// 制品类型
			String wareType = condMap.get("wareType");
			if (StringKit.isValid(wareType)) {
				condList.add("ware_type = ?");
				paramList.add(wareType);
			}
			// 采购单号
			String poNumber = condMap.get("poNumber");
			if (StringKit.isValid(poNumber)) {
				condList.add("po_number like ?");
				paramList.add("%" + poNumber + "%");
			}
			// 采购类型
			String poType = condMap.get("poType");
			if (StringKit.isValid(poType)) {
				condList.add("purchase_type = ?");
				paramList.add(poType);
			}
			// 合同状态
			String contractStatus = condMap.get("contractStatus");
			if (StringKit.isValid(contractStatus)) {
				condList.add("contract_status = ?");
				paramList.add(contractStatus);
			}
			// 发票状态
			String invoiceStatus = condMap.get("invoiceStatus");
			if (StringKit.isValid(invoiceStatus)) {
				condList.add("invoice_status = ?");
				paramList.add(invoiceStatus);
			}
			// 货款状态
			String paymentStatus = condMap.get("paymentStatus");
			if (StringKit.isValid(paymentStatus)) {
				condList.add("payment_status = ?");
				paramList.add(paymentStatus);
			}
			// 采购状态
			String poStatus = condMap.get("poStatus");
			if (StringKit.isValid(poStatus)) {
				condList.add("po_status = ?");
				paramList.add(poStatus);
			}
			// 审核状态
			String reviewStatus = condMap.get("reviewStatus");
			if (StringKit.isValid(reviewStatus)) {
				condList.add("approval_status = ?");
				paramList.add(reviewStatus);
			}
			// 供应商
			String supplierId = condMap.get("supplierId");
			if (StringKit.isValid(supplierId)) {
				condList.add("supplier_id = ?");
				paramList.add(supplierId);
			}
			// 仓库
			String warehouseId = condMap.get("warehouseId");
			if (StringKit.isValid(warehouseId)) {
				condList.add("receiver_id = ? and receiver_type=" + PurchaseOrderConfig.RECEIVER_TYPE_WAREHOUSE);
				paramList.add(warehouseId);
			}
			// 状态为1的
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}

			String orderQuery = "order by po_id desc";
			String orderBy = condMap.get("orderBy");
			if (StringKit.isValid(orderBy)) {
				orderQuery = " order by " + orderBy;
			}

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getPageResult(sql, orderQuery, condList, paramList, PurchaseOrderInfoBean.class,
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

	public int getPurchaseOrderInfoCountForToday() {
		int result = 0;
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from purchase_order_info where create_time > curdate() ";
			List<String> paramList = new ArrayList<String>();
			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getTotalRecordResult(sql, paramList, db);
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

	public List<PurchaseOrderInfoBean> getPurchaseOrderInfoListByMap(Map<String, String> condMap) {
		List<PurchaseOrderInfoBean> result = new ArrayList<PurchaseOrderInfoBean>();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectR();
			String sql = "select * from purchase_order_info";
			List<String> condList = new ArrayList<String>();
			List<String> paramList = new ArrayList<String>();
			String receiverId = condMap.get("receiverId");
			if (StringKit.isValid(receiverId)) {
				condList.add("receiver_id = ?");
				paramList.add(receiverId);
			}
			String receiverType = condMap.get("receiverType");
			if (StringKit.isValid(receiverType)) {
				condList.add("receiver_type = ?");
				paramList.add(receiverType);
			}
			String status = condMap.get("status");
			if (StringKit.isValid(status)) {
				condList.add("status = ?");
				paramList.add(status);
			}
			String orderQuery = "order by po_id desc";

			DBExecutor dbExecutor = dbFactory.getDBExecutor();
			result = dbExecutor.getRecordListResult(sql, orderQuery, condList, paramList, PurchaseOrderInfoBean.class,
					db);
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

	public boolean checkHasPurchaseOrderInfoByMap(Map<String, String> condMap) {
		int flage = 1;
		if (condMap.isEmpty())
			return false;

		boolean result = false;
		String condSql = MapKit.map2String(condMap, "=", " and ");
		condSql += " 1=1 ";

		String sql = "select " + flage + " from purchase_order_info where " + condSql + " limit 1";
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectR();
			ResultSet resultSet = db.executeQuery(sql);
			if (resultSet.next()) {
				int has = resultSet.getInt(0);
				if (has == flage) {
					result = true;
				}
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
