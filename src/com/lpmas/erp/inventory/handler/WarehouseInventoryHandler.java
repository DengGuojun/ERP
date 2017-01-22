package com.lpmas.erp.inventory.handler;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.config.ErpMongoConfig;
import com.lpmas.erp.factory.ErpDBFactory;
import com.lpmas.erp.inventory.bean.DeliveryVoucherInfoBean;
import com.lpmas.erp.inventory.bean.DeliveryVoucherItemBean;
import com.lpmas.erp.inventory.bean.WarehouseInventoryAggregateReportBean;
import com.lpmas.erp.inventory.bean.WarehouseInventoryBean;
import com.lpmas.erp.inventory.bean.WarehouseInventoryChangeBean;
import com.lpmas.erp.inventory.bean.WarehouseInventoryLogBean;
import com.lpmas.erp.inventory.bean.WarehouseInventoryReportBean;
import com.lpmas.erp.inventory.bean.WarehouseTransferOrderInfoBean;
import com.lpmas.erp.inventory.bean.WarehouseTransferOrderItemBean;
import com.lpmas.erp.inventory.bean.WarehouseVoucherInfoBean;
import com.lpmas.erp.inventory.bean.WarehouseVoucherItemBean;
import com.lpmas.erp.inventory.business.DeliveryVoucherItemBusiness;
import com.lpmas.erp.inventory.business.DeliveryVoucherNumberGenerator;
import com.lpmas.erp.inventory.business.WarehouesInventoryOperationLock;
import com.lpmas.erp.inventory.business.WarehouseInventoryAggregateReportBusiness;
import com.lpmas.erp.inventory.business.WarehouseInventoryBusiness;
import com.lpmas.erp.inventory.business.WarehouseInventoryReportBusiness;
import com.lpmas.erp.inventory.business.WarehouseTransferOrderInfoBusiness;
import com.lpmas.erp.inventory.business.WarehouseTransferOrderItemBusiness;
import com.lpmas.erp.inventory.business.WarehouseVoucherItemBusiness;
import com.lpmas.erp.inventory.config.DeliveryVoucherConfig;
import com.lpmas.erp.inventory.config.DeliveryVoucherLogConfig;
import com.lpmas.erp.inventory.config.SourceOrderTypeConfig;
import com.lpmas.erp.inventory.config.WarehouseInventoryConfig;
import com.lpmas.erp.inventory.config.WarehouseTransferOrderConfig;
import com.lpmas.erp.inventory.config.WarehouseTransferOrderLogConfig;
import com.lpmas.erp.inventory.config.WarehouseVoucherConfig;
import com.lpmas.erp.inventory.config.WarehouseVoucherLogConfig;
import com.lpmas.erp.inventory.dao.DeliveryVoucherInfoDao;
import com.lpmas.erp.inventory.dao.DeliveryVoucherItemDao;
import com.lpmas.erp.inventory.dao.WarehouseInventoryChangeDao;
import com.lpmas.erp.inventory.dao.WarehouseInventoryDao;
import com.lpmas.erp.inventory.dao.WarehouseInventoryLogDao;
import com.lpmas.erp.inventory.dao.WarehouseTransferOrderInfoDao;
import com.lpmas.erp.inventory.dao.WarehouseVoucherInfoDao;
import com.lpmas.erp.inventory.dao.WarehouseVoucherItemDao;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.DateKit;
import com.lpmas.framework.util.NumeralOperationKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.MaterialInfoBean;
import com.lpmas.pdm.bean.ProductInfoBean;
import com.lpmas.pdm.bean.ProductItemBean;
import com.lpmas.pdm.client.PdmServiceClient;

public class WarehouseInventoryHandler {

	private static Logger log = LoggerFactory.getLogger(WarehouseInventoryHandler.class);

	private int addWarehouseVoucherInfo(DBObject db, WarehouseVoucherInfoBean bean) throws Exception {
		WarehouseVoucherInfoDao dao = new WarehouseVoucherInfoDao();
		int result = dao.insertWarehouseVoucherInfo(db, bean);
		bean.setWvId(result);
		return result;
	}

	private int addDeliveryVoucherInfo(DBObject db, DeliveryVoucherInfoBean bean) throws Exception {
		DeliveryVoucherInfoDao dao = new DeliveryVoucherInfoDao();
		int result = dao.insertDeliveryVoucherInfo(db, bean);
		bean.setDvId(result);
		return result;
	}

	private int updateWarehouseVoucherInfo(DBObject db, WarehouseVoucherInfoBean bean) throws Exception {
		WarehouseVoucherInfoDao dao = new WarehouseVoucherInfoDao();
		return dao.updateWarehouseVoucherInfo(db, bean);
	}

	private int updateDeliveryVoucherInfo(DBObject db, DeliveryVoucherInfoBean bean) {
		DeliveryVoucherInfoDao dao = new DeliveryVoucherInfoDao();
		return dao.updateDeliveryVoucherInfo(db, bean);
	}

	private int addWarehouseInventory(DBObject db, WarehouseInventoryBean bean) throws Exception {
		WarehouseInventoryDao dao = new WarehouseInventoryDao();
		int result = dao.insertWarehouseInventory(db, bean);
		bean.setWiId(result);
		return result;
	}

	private int updateWarehouseInventory(DBObject db, WarehouseInventoryBean bean) throws Exception {
		WarehouseInventoryDao dao = new WarehouseInventoryDao();
		return dao.updateWarehouseInventory(db, bean);
	}

	private int updateWarehouseTransferOrderInfo(DBObject db, WarehouseTransferOrderInfoBean bean) throws Exception {
		WarehouseTransferOrderInfoDao dao = new WarehouseTransferOrderInfoDao();
		return dao.updateWarehouseTransferOrderInfo(db, bean);
	}

	private int addDeliveryVoucherItem(DBObject db, DeliveryVoucherItemBean bean) throws Exception {
		DeliveryVoucherItemDao dao = new DeliveryVoucherItemDao();
		int result = dao.insertDeliveryVoucherItem(db, bean);
		bean.setDvItemId(result);
		return result;
	}

	private int addWarehouseVoucherItem(DBObject db, WarehouseVoucherItemBean bean) throws Exception {
		WarehouseVoucherItemDao dao = new WarehouseVoucherItemDao();
		int result = dao.insertWarehouseVoucherItem(db, bean);
		bean.setWvItemId(result);
		return result;
	}

	private int addWarehouseInventoryChange(DBObject db, int wiId1, int wiId2, int changeType, double quantity, int createUser) throws Exception {
		WarehouseInventoryChangeBean bean = new WarehouseInventoryChangeBean();
		bean.setWiId1(wiId1);
		bean.setWiId2(wiId2);
		bean.setChangeType(changeType);
		bean.setQuantity(quantity);
		bean.setCreateUser(createUser);
		bean.setStatus(Constants.STATUS_VALID);
		WarehouseInventoryChangeDao dao = new WarehouseInventoryChangeDao();
		return dao.insertWarehouseInventoryChange(db, bean);
	}

	private int addWarehouseInventoryLog(DBObject db, int wiId, int sourceId, int changeType, double quantity, int createUser) throws Exception {
		WarehouseInventoryLogBean bean = new WarehouseInventoryLogBean();
		bean.setWiId(wiId);
		bean.setSourceId(sourceId);
		bean.setChangeType(changeType);
		bean.setQuantity(quantity);
		bean.setCreateUser(createUser);
		WarehouseInventoryLogDao dao = new WarehouseInventoryLogDao();
		return dao.insertWarehouseInventoryLog(db, bean);
	}

	private void sendDeliveryVoucherAddLog(DeliveryVoucherInfoBean deliveryVoucherInfoBean, List<DeliveryVoucherItemBean> dvItemList) {
		try {
			ErpLogSender helper = new ErpLogSender();
			helper.sendAddLog(deliveryVoucherInfoBean, InfoTypeConfig.INFO_TYPE_DELIVERY_VOUCHER, deliveryVoucherInfoBean.getDvId(),
					DeliveryVoucherLogConfig.LOG_DELIVERY_VOUCHER_INFO);
			dvItemList.forEach(dvItem -> helper.sendAddLog(dvItem, InfoTypeConfig.INFO_TYPE_DELIVERY_VOUCHER, dvItem.getDvId(), dvItem.getDvItemId(),
					0, DeliveryVoucherLogConfig.LOG_DELIVERY_VOUCHER_ITEM));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	private void sendWarehouseVoucherAddLog(WarehouseVoucherInfoBean warehouseVoucherInfoBean, List<WarehouseVoucherItemBean> wvItemList) {
		try {
			ErpLogSender helper = new ErpLogSender();
			helper.sendAddLog(warehouseVoucherInfoBean, InfoTypeConfig.INFO_TYPE_WAREHOUSE_VOUCHER, warehouseVoucherInfoBean.getWvId(),
					WarehouseVoucherLogConfig.LOG_WV_INFO);
			wvItemList.forEach(dvItem -> helper.sendAddLog(dvItem, InfoTypeConfig.INFO_TYPE_WAREHOUSE_VOUCHER, dvItem.getWvId(), dvItem.getWvItemId(),
					0, WarehouseVoucherLogConfig.LOG_WV_ITEM));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	private void sendWarehouseTransferOrderInfoUpdateLog(WarehouseTransferOrderInfoBean originalBean, WarehouseTransferOrderInfoBean currentBean) {
		try {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(originalBean, currentBean, InfoTypeConfig.INFO_TYPE_WAREHOUSE_TRANSFER_ORDER, currentBean.getToId(),
					WarehouseTransferOrderLogConfig.LOG_WAREHOUSE_TRANSFER_ORDER_INFO);
		} catch (Exception e) {
			log.error("", e);
		}
	}

	/**
	 * 根据入库信息，新建待上架库存记录
	 */
	public void createWarehouseInventory(WarehouseVoucherInfoBean warehouseVoucherInfoBean) throws Exception {
		WarehouesInventoryOperationLock.lock.lock(); // 获取lock，在事务完成后或发生异常后释放
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			db.beginTransition();// 事务开始

			WarehouseVoucherItemBusiness itemBusiness = new WarehouseVoucherItemBusiness();
			List<WarehouseVoucherItemBean> itemList = itemBusiness.getWarehouseVoucherItemListByWvId(warehouseVoucherInfoBean.getWvId());
			processCreateWarehouseInventory(db, warehouseVoucherInfoBean, itemList);
			db.commit();// 事务提交
		} catch (Exception e) {
			db.rollback();// 事务回滚
			log.error("", e);
			throw e;
		} finally {
			WarehouesInventoryOperationLock.lock.unlock();
			try {
				db.close();
			} catch (SQLException sqle) {
				log.error("", sqle);
			}
		}
	}

	private void processCreateWarehouseInventory(DBObject db, WarehouseVoucherInfoBean warehouseVoucherInfoBean,
			List<WarehouseVoucherItemBean> itemList) throws Exception {
		// 修改入库单状态
		updateWarehouseVoucherInfo(db, warehouseVoucherInfoBean);
		WarehouseInventoryReportBusiness reportBusiness = new WarehouseInventoryReportBusiness();
		for (WarehouseVoucherItemBean itemBean : itemList) {
			// 根据入库单的item，新增或者修改库存记录
			WarehouseInventoryReportBean reportBean = new WarehouseInventoryReportBean();
			reportBean.setWarehouseId(warehouseVoucherInfoBean.getWarehouseId());
			reportBean.setBatchNumber(itemBean.getBatchNumber());
			reportBean.setWareId(itemBean.getWareId());
			reportBean.setWareType(itemBean.getWareType());
			reportBean.setProductionDate(itemBean.getProductionDate());
			reportBean.setGuaranteePeriod(itemBean.getGuaranteePeriod());
			reportBean.setExpirationDate(itemBean.getExpirationDate());
			reportBean.setUnit(itemBean.getUnit());
			reportBean.setPendingQuantity(itemBean.getStockInQuantity());
			reportBean.set_id(ErpMongoConfig.getWarehouseInventoryReportUnqueId(reportBean));
			// 保存库存对象数据到MongoDB
			WarehouseInventoryReportBean originalBean = reportBusiness.getWarehouseInventoryReportByKey(reportBean.get_id());
			if (originalBean != null) {
				// 更新库存记录
				originalBean.setPendingQuantity(NumeralOperationKit.add(originalBean.getPendingQuantity(), reportBean.getPendingQuantity()));
				// 把入库单ID添加到为待上架的入库单列表
				List<Integer> pendingShelfWvIdList = originalBean.getPendingShelfWvIdList();
				if (!pendingShelfWvIdList.contains(warehouseVoucherInfoBean.getWvId())) {
					pendingShelfWvIdList.add(warehouseVoucherInfoBean.getWvId());
				}
				originalBean.setPendingShelfWvIdList(pendingShelfWvIdList);
				originalBean.setModifyTime(DateKit.getCurrentTimestamp());
				originalBean.setModifyUser(warehouseVoucherInfoBean.getModifyUser());
				reportBusiness.updateWarehouseInventoryReport(originalBean);
			} else {
				// 新建库存记录
				// 把入库单ID添加到为待上架的入库单列表
				List<Integer> pendingShelfWvIdList = new ArrayList<Integer>();
				pendingShelfWvIdList.add(warehouseVoucherInfoBean.getWvId());
				reportBean.setPendingShelfWvIdList(pendingShelfWvIdList);
				// 获取制品的类型
				int typeId = 0;
				PdmServiceClient pdmClient = new PdmServiceClient();
				if (reportBean.getWareType() == InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM) {
					ProductItemBean productItemBean = pdmClient.getProductItemByKey(reportBean.getWareId());
					ProductInfoBean productInfoBean = pdmClient.getProductInfoByKey(productItemBean.getProductId());
					typeId = productInfoBean.getTypeId2();
				} else if (reportBean.getWareType() == InfoTypeConfig.INFO_TYPE_MATERIAL) {
					MaterialInfoBean materialInfoBean = pdmClient.getMaterialInfoByKey(reportBean.getWareId());
					typeId = materialInfoBean.getTypeId1();
				}
				reportBean.setTypeId(typeId);
				reportBean.setModifyTime(DateKit.getCurrentTimestamp());
				reportBean.setModifyUser(warehouseVoucherInfoBean.getModifyUser());
				reportBusiness.insertWarehouseInventoryReport(reportBean);
			}

			// 更新聚合库存记录
			WarehouseInventoryAggregateReportBusiness aggregateBusiness = new WarehouseInventoryAggregateReportBusiness();
			WarehouseInventoryAggregateReportBean aggregateBean = new WarehouseInventoryAggregateReportBean();
			aggregateBean.setWarehouseId(reportBean.getWarehouseId());
			aggregateBean.setWareId(reportBean.getWareId());
			aggregateBean.setWareType(reportBean.getWareType());
			aggregateBean.set_id(ErpMongoConfig.getWarehouseInventoryAggregateReportUnqueId(aggregateBean));
			// 保存聚合库存对象数据到MongoDB
			WarehouseInventoryAggregateReportBean originalAggregateBean = aggregateBusiness
					.getWarehouseInventoryAggregateReportByKey(aggregateBean.get_id());
			if (originalAggregateBean != null) {
				// 更新库存记录
				originalAggregateBean
						.setPendingQuantity(NumeralOperationKit.add(originalAggregateBean.getPendingQuantity(), reportBean.getPendingQuantity()));
				originalAggregateBean.setModifyUser(reportBean.getModifyUser());
				originalAggregateBean.setModifyTime(reportBean.getModifyTime());
				aggregateBusiness.updateWarehouseInventoryAggregateReport(originalAggregateBean);
			} else {
				// 新建库存记录
				aggregateBean.setPendingQuantity(reportBean.getPendingQuantity());
				aggregateBean.setTypeId(reportBean.getTypeId());
				aggregateBean.setModifyUser(reportBean.getModifyUser());
				aggregateBean.setModifyTime(reportBean.getModifyTime());
				aggregateBusiness.insertWarehouseInventoryAggregateReport(aggregateBean);
			}
		}
	}

	/**
	 * 根据入库信息，上架库存
	 */
	public void putOnWarehouseInventory(WarehouseVoucherInfoBean warehouseVoucherInfoBean) throws Exception {
		WarehouesInventoryOperationLock.lock.lock(); // 获取lock，在事务完成后或发生异常后释放
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			db.beginTransition();// 事务开始
			// 修改入库单状态
			updateWarehouseVoucherInfo(db, warehouseVoucherInfoBean);
			// 修改关联单的状态
			Map<String, Object> processResult = processWarehouseVoucherBySource(db, warehouseVoucherInfoBean);

			WarehouseVoucherItemBusiness itemBusiness = new WarehouseVoucherItemBusiness();
			WarehouseInventoryReportBusiness reportBusiness = new WarehouseInventoryReportBusiness();
			List<WarehouseVoucherItemBean> itemList = itemBusiness.getWarehouseVoucherItemListByWvId(warehouseVoucherInfoBean.getWvId());
			for (WarehouseVoucherItemBean itemBean : itemList) {
				// 根据入库单的item，新增或者修改库存记录
				WarehouseInventoryBean normalInventoryBean = new WarehouseInventoryBean();
				WarehouseInventoryBean defectInventoryBean = new WarehouseInventoryBean();
				normalInventoryBean.setWarehouseId(warehouseVoucherInfoBean.getWarehouseId());
				normalInventoryBean.setWareType(itemBean.getWareType());
				normalInventoryBean.setWareId(itemBean.getWareId());
				normalInventoryBean.setInventoryType(WarehouseInventoryConfig.WIT_NORMAL);
				normalInventoryBean.setBatchNumber(itemBean.getBatchNumber());
				normalInventoryBean.setProductionDate(itemBean.getProductionDate());
				normalInventoryBean.setGuaranteePeriod(itemBean.getGuaranteePeriod());
				normalInventoryBean.setExpirationDate(itemBean.getExpirationDate());
				normalInventoryBean.setQuantity(NumeralOperationKit.subtract(itemBean.getStockInQuantity(), itemBean.getDefectQuantity()));
				normalInventoryBean.setUnit(itemBean.getUnit());
				normalInventoryBean.setStatus(Constants.STATUS_VALID);
				normalInventoryBean.setCreateUser(warehouseVoucherInfoBean.getModifyUser());
				// 校验数据库是否已存在同一批次的正品库存记录
				WarehouseInventoryBusiness inventoryBusiness = new WarehouseInventoryBusiness();
				WarehouseInventoryBean originalNormalInventoryBean = inventoryBusiness.getWarehouseInventoryBeanByCondition(
						normalInventoryBean.getWarehouseId(), normalInventoryBean.getWareType(), normalInventoryBean.getWareId(),
						normalInventoryBean.getInventoryType(), normalInventoryBean.getBatchNumber(), normalInventoryBean.getProductionDate());
				int wiId = 0;
				if (originalNormalInventoryBean == null) {
					// 新增正品库存
					wiId = addWarehouseInventory(db, normalInventoryBean);
				} else {
					// 如果存在同一批次的正品库存，则更新
					// 更新正品库存记录
					wiId = originalNormalInventoryBean.getWiId();
					originalNormalInventoryBean
							.setQuantity(NumeralOperationKit.add(originalNormalInventoryBean.getQuantity(), normalInventoryBean.getQuantity()));
					originalNormalInventoryBean.setModifyUser(normalInventoryBean.getCreateUser());
					updateWarehouseInventory(db, originalNormalInventoryBean);
				}
				// 新增正品库存转换操作记录
				addWarehouseInventoryChange(db, 0, wiId, WarehouseInventoryConfig.WIO_TYPE_PUT_ON, normalInventoryBean.getQuantity(),
						normalInventoryBean.getCreateUser());
				// 记录正品库存日志
				addWarehouseInventoryLog(db, wiId, warehouseVoucherInfoBean.getWvId(), WarehouseInventoryConfig.WIO_TYPE_PUT_ON,
						normalInventoryBean.getQuantity(), normalInventoryBean.getCreateUser());

				// 如果入库单项目存在残次品，则需要添加残次品库存
				if (itemBean.getDefectQuantity() > 0) {
					// 新增残次品库存记录
					BeanKit.copyBean(defectInventoryBean, normalInventoryBean);
					defectInventoryBean.setQuantity(itemBean.getDefectQuantity());
					defectInventoryBean.setInventoryType(WarehouseInventoryConfig.WIT_DEFECT);
					// 校验数据库是否已存在同一批次的残次品库存记录
					WarehouseInventoryBean originalDefectInventoryBean = inventoryBusiness.getWarehouseInventoryBeanByCondition(
							defectInventoryBean.getWarehouseId(), defectInventoryBean.getWareType(), defectInventoryBean.getWareId(),
							defectInventoryBean.getInventoryType(), defectInventoryBean.getBatchNumber(), defectInventoryBean.getProductionDate());
					int defectWiId = 0;
					if (originalDefectInventoryBean == null) {
						defectWiId = addWarehouseInventory(db, defectInventoryBean);
					} else {
						// 如果存在同一批次的残次库存，则更新
						defectWiId = originalDefectInventoryBean.getWiId();
						originalDefectInventoryBean
								.setQuantity(NumeralOperationKit.add(originalDefectInventoryBean.getQuantity(), defectInventoryBean.getQuantity()));
						originalDefectInventoryBean.setModifyUser(defectInventoryBean.getCreateUser());
						updateWarehouseInventory(db, originalDefectInventoryBean);
					}
					// 新增残次品库存转换操作记录
					addWarehouseInventoryChange(db, 0, defectWiId, WarehouseInventoryConfig.WIO_TYPE_PUT_ON, defectInventoryBean.getQuantity(),
							defectInventoryBean.getCreateUser());
					// 新增残次品库存日志
					addWarehouseInventoryLog(db, defectWiId, warehouseVoucherInfoBean.getWvId(), WarehouseInventoryConfig.WIO_TYPE_PUT_ON,
							defectInventoryBean.getQuantity(), defectInventoryBean.getCreateUser());
				}

				// 保存库存对象数据到MongoDB
				WarehouseInventoryReportBean reportBean = new WarehouseInventoryReportBean();
				reportBean.setWarehouseId(warehouseVoucherInfoBean.getWarehouseId());
				reportBean.setBatchNumber(itemBean.getBatchNumber());
				reportBean.setWareId(itemBean.getWareId());
				reportBean.setWareType(itemBean.getWareType());
				reportBean.setProductionDate(itemBean.getProductionDate());
				reportBean.setGuaranteePeriod(itemBean.getGuaranteePeriod());
				reportBean.set_id(ErpMongoConfig.getWarehouseInventoryReportUnqueId(reportBean));
				WarehouseInventoryReportBean originalBean = reportBusiness.getWarehouseInventoryReportByKey(reportBean.get_id());

				// 更新库存数量
				double putOnNomalQuantity = NumeralOperationKit.subtract(itemBean.getStockInQuantity(), itemBean.getDefectQuantity());
				double putOnDefectQuantity = itemBean.getDefectQuantity();
				originalBean.setNormalQuantity(NumeralOperationKit.add(originalBean.getNormalQuantity(), putOnNomalQuantity));
				originalBean.setPendingQuantity(
						NumeralOperationKit.subtract(originalBean.getPendingQuantity(), putOnNomalQuantity + putOnDefectQuantity));
				originalBean.setDefectQuantity(NumeralOperationKit.add(originalBean.getDefectQuantity(), putOnDefectQuantity));

				// 更新待上架入库单ID列表
				List<Integer> pendingShelfWvIdList = originalBean.getPendingShelfWvIdList();
				pendingShelfWvIdList.remove(Integer.valueOf(warehouseVoucherInfoBean.getWvId()));
				originalBean.setPendingShelfWvIdList(pendingShelfWvIdList);
				// 更新数据
				originalBean.setModifyTime(DateKit.getCurrentTimestamp());
				originalBean.setModifyUser(warehouseVoucherInfoBean.getModifyUser());
				reportBusiness.updateWarehouseInventoryReport(originalBean);

				// 更新聚合库存记录
				WarehouseInventoryAggregateReportBusiness aggregateBusiness = new WarehouseInventoryAggregateReportBusiness();
				WarehouseInventoryAggregateReportBean aggregateBean = new WarehouseInventoryAggregateReportBean();
				aggregateBean.setWarehouseId(reportBean.getWarehouseId());
				aggregateBean.setWareId(reportBean.getWareId());
				aggregateBean.setWareType(reportBean.getWareType());
				aggregateBean.set_id(ErpMongoConfig.getWarehouseInventoryAggregateReportUnqueId(aggregateBean));
				// 保存聚合库存对象数据到MongoDB
				WarehouseInventoryAggregateReportBean originalAggregateBean = aggregateBusiness
						.getWarehouseInventoryAggregateReportByKey(aggregateBean.get_id());
				// 更新库存记录
				originalAggregateBean.setNormalQuantity(NumeralOperationKit.add(originalAggregateBean.getNormalQuantity(), putOnNomalQuantity));
				originalAggregateBean.setPendingQuantity(
						NumeralOperationKit.subtract(originalAggregateBean.getPendingQuantity(), putOnNomalQuantity + putOnDefectQuantity));
				originalAggregateBean.setDefectQuantity(NumeralOperationKit.add(originalAggregateBean.getDefectQuantity(), putOnDefectQuantity));
				originalAggregateBean.setModifyUser(originalBean.getModifyUser());
				originalAggregateBean.setModifyTime(originalBean.getModifyTime());
				aggregateBusiness.updateWarehouseInventoryAggregateReport(originalAggregateBean);

			}
			db.commit();// 事务提交
			// 发送相关日志
			WarehouseTransferOrderInfoBean originalBean = (WarehouseTransferOrderInfoBean) processResult.get("originalBean");
			WarehouseTransferOrderInfoBean currentBean = (WarehouseTransferOrderInfoBean) processResult.get("currentBean");
			if (originalBean != null && currentBean != null) {
				sendWarehouseTransferOrderInfoUpdateLog(originalBean, currentBean);
			}
		} catch (Exception e) {
			db.rollback();// 事务回滚
			log.error("", e);
			throw e;
		} finally {
			WarehouesInventoryOperationLock.lock.unlock();
			try {
				db.close();
			} catch (SQLException sqle) {
				log.error("", sqle);
			}
		}
	}

	/**
	 * 扣减正品库存，适合报损、报次、盘亏操作
	 * 
	 */
	public void subtractWarehouseInventory(WarehouseInventoryReportBean reportBean, double quantity, int changeType, int changeUser)
			throws Exception {

		WarehouesInventoryOperationLock.lock.lock(); // 获取lock，在事务完成后或发生异常后释放
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			db.beginTransition();// 事务开始
			WarehouseInventoryBusiness inventoryBusiness = new WarehouseInventoryBusiness();
			WarehouseInventoryBean inventoryBean = inventoryBusiness.getWarehouseInventoryBeanByCondition(reportBean.getWarehouseId(),
					reportBean.getWareType(), reportBean.getWareId(), WarehouseInventoryConfig.WIT_NORMAL, reportBean.getBatchNumber(),
					reportBean.getProductionDate());

			// 扣减正品库存
			inventoryBean.setQuantity(NumeralOperationKit.subtract(inventoryBean.getQuantity(), quantity));
			inventoryBean.setModifyUser(changeUser);
			updateWarehouseInventory(db, inventoryBean);
			// 记录操作日志
			addWarehouseInventoryLog(db, inventoryBean.getWiId(), 0, changeType, -quantity, changeUser);

			// 如果是报次或者报损，需要增加次品或者损品库存
			if (changeType == WarehouseInventoryConfig.WIO_TYPE_DEFECT || changeType == WarehouseInventoryConfig.WIO_TYPE_DAMAGE) {
				int targetInventoryType = 0;
				if (changeType == WarehouseInventoryConfig.WIO_TYPE_DEFECT) {
					targetInventoryType = WarehouseInventoryConfig.WIT_DEFECT;
				} else {
					targetInventoryType = WarehouseInventoryConfig.WIT_DAMAGE;
				}
				WarehouseInventoryBean targetBean = inventoryBusiness.getWarehouseInventoryBeanByCondition(reportBean.getWarehouseId(),
						reportBean.getWareType(), reportBean.getWareId(), targetInventoryType, reportBean.getBatchNumber(),
						reportBean.getProductionDate());
				if (targetBean != null) {
					// 如果已经存在库存记录，则更新库存
					targetBean.setQuantity(NumeralOperationKit.add(targetBean.getQuantity(), quantity));
					targetBean.setModifyUser(changeUser);
					updateWarehouseInventory(db, targetBean);
				} else {
					// 如果没有库存记录，则新建库存
					targetBean = new WarehouseInventoryBean();
					targetBean.setWarehouseId(inventoryBean.getWarehouseId());
					targetBean.setWareType(inventoryBean.getWareType());
					targetBean.setWareId(inventoryBean.getWareId());
					targetBean.setBatchNumber(inventoryBean.getBatchNumber());
					targetBean.setProductionDate(inventoryBean.getProductionDate());
					targetBean.setInventoryType(targetInventoryType);
					targetBean.setGuaranteePeriod(inventoryBean.getGuaranteePeriod());
					targetBean.setExpirationDate(inventoryBean.getExpirationDate());
					targetBean.setUnit(inventoryBean.getUnit());
					targetBean.setQuantity(quantity);
					targetBean.setCreateUser(changeUser);
					targetBean.setStatus(Constants.STATUS_VALID);
					int wiId = addWarehouseInventory(db, targetBean);
					targetBean.setWiId(wiId);
				}
				// 记录操作日志
				addWarehouseInventoryLog(db, targetBean.getWiId(), inventoryBean.getWiId(), changeType, quantity, changeUser);
				// 新增库存转换操作记录
				addWarehouseInventoryChange(db, inventoryBean.getWiId(), targetBean.getWiId(), changeType, quantity, changeUser);
			} else {
				// 新增库存转换操作记录
				addWarehouseInventoryChange(db, inventoryBean.getWiId(), 0, changeType, quantity, changeUser);
			}

			// 更新Mongo数据
			reportBean.setNormalQuantity(NumeralOperationKit.subtract(reportBean.getNormalQuantity(), quantity));
			if (changeType == WarehouseInventoryConfig.WIO_TYPE_DEFECT) {
				reportBean.setDefectQuantity(NumeralOperationKit.add(reportBean.getDefectQuantity(), quantity));
			}
			if (changeType == WarehouseInventoryConfig.WIO_TYPE_DAMAGE) {
				reportBean.setDamageQuantity(NumeralOperationKit.add(reportBean.getDamageQuantity(), quantity));
			}
			WarehouseInventoryReportBusiness reportBusiness = new WarehouseInventoryReportBusiness();
			reportBean.setModifyTime(DateKit.getCurrentTimestamp());
			reportBean.setModifyUser(changeUser);
			reportBusiness.updateWarehouseInventoryReport(reportBean);

			// 更新聚合库存记录
			WarehouseInventoryAggregateReportBusiness aggregateBusiness = new WarehouseInventoryAggregateReportBusiness();
			WarehouseInventoryAggregateReportBean aggregateBean = new WarehouseInventoryAggregateReportBean();
			aggregateBean.setWarehouseId(reportBean.getWarehouseId());
			aggregateBean.setWareId(reportBean.getWareId());
			aggregateBean.setWareType(reportBean.getWareType());
			aggregateBean.set_id(ErpMongoConfig.getWarehouseInventoryAggregateReportUnqueId(aggregateBean));
			WarehouseInventoryAggregateReportBean originalAggregateBean = aggregateBusiness
					.getWarehouseInventoryAggregateReportByKey(aggregateBean.get_id());
			originalAggregateBean.setNormalQuantity(NumeralOperationKit.subtract(originalAggregateBean.getNormalQuantity(), quantity));
			if (changeType == WarehouseInventoryConfig.WIO_TYPE_DEFECT) {
				originalAggregateBean.setDefectQuantity(NumeralOperationKit.add(originalAggregateBean.getDefectQuantity(), quantity));
			}
			if (changeType == WarehouseInventoryConfig.WIO_TYPE_DAMAGE) {
				originalAggregateBean.setDamageQuantity(NumeralOperationKit.add(originalAggregateBean.getDamageQuantity(), quantity));
			}
			originalAggregateBean.setModifyUser(reportBean.getModifyUser());
			originalAggregateBean.setModifyTime(reportBean.getModifyTime());
			aggregateBusiness.updateWarehouseInventoryAggregateReport(originalAggregateBean);

			db.commit();// 事务提交
		} catch (Exception e) {
			db.rollback();// 事务回滚
			log.error("", e);
			throw e;
		} finally {
			WarehouesInventoryOperationLock.lock.unlock();
			try {
				db.close();
			} catch (SQLException sqle) {
				log.error("", sqle);
			}
		}
	}

	/**
	 * 增加正品库存，适合盘盈操作
	 * 
	 */
	public void addWarehouseInventory(WarehouseInventoryReportBean reportBean, double quantity, int changeType, int changeUser) throws Exception {

		WarehouesInventoryOperationLock.lock.lock(); // 获取lock，在事务完成后或发生异常后释放
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			db.beginTransition();// 事务开始
			WarehouseInventoryBusiness inventoryBusiness = new WarehouseInventoryBusiness();
			WarehouseInventoryBean defectBean = new WarehouseInventoryBean();
			// 如果是次转正，需要扣减次品库存
			if (changeType == WarehouseInventoryConfig.WIO_TYPE_NORMAL) {
				defectBean = inventoryBusiness.getWarehouseInventoryBeanByCondition(reportBean.getWarehouseId(), reportBean.getWareType(),
						reportBean.getWareId(), WarehouseInventoryConfig.WIT_DEFECT, reportBean.getBatchNumber(), reportBean.getProductionDate());
				defectBean.setQuantity(NumeralOperationKit.subtract(defectBean.getQuantity(), quantity));
				defectBean.setModifyUser(changeUser);
				updateWarehouseInventory(db, defectBean);
				// 记录操作日志
				addWarehouseInventoryLog(db, defectBean.getWiId(), 0, changeType, -quantity, changeUser);

			}

			WarehouseInventoryBean inventoryBean = inventoryBusiness.getWarehouseInventoryBeanByCondition(reportBean.getWarehouseId(),
					reportBean.getWareType(), reportBean.getWareId(), WarehouseInventoryConfig.WIT_NORMAL, reportBean.getBatchNumber(),
					reportBean.getProductionDate());
			if (inventoryBean != null) {
				// 如果已经存在正品库存记录，则更新
				inventoryBean.setQuantity(NumeralOperationKit.add(inventoryBean.getQuantity(), quantity));
				inventoryBean.setModifyUser(changeUser);
				updateWarehouseInventory(db, inventoryBean);
			} else {
				// 如果不存在正品库存记录，则新增
				inventoryBean = new WarehouseInventoryBean();
				inventoryBean.setWarehouseId(reportBean.getWarehouseId());
				inventoryBean.setWareType(reportBean.getWareType());
				inventoryBean.setWareId(reportBean.getWareId());
				inventoryBean.setBatchNumber(reportBean.getBatchNumber());
				inventoryBean.setProductionDate(reportBean.getProductionDate());
				inventoryBean.setInventoryType(WarehouseInventoryConfig.WIT_NORMAL);
				inventoryBean.setGuaranteePeriod(reportBean.getGuaranteePeriod());
				inventoryBean.setExpirationDate(reportBean.getExpirationDate());
				inventoryBean.setUnit(reportBean.getUnit());
				inventoryBean.setQuantity(quantity);
				inventoryBean.setCreateUser(changeUser);
				inventoryBean.setStatus(Constants.STATUS_VALID);
				int wiId = addWarehouseInventory(db, inventoryBean);
				inventoryBean.setWiId(wiId);
			}
			// 记录操作日志
			addWarehouseInventoryLog(db, inventoryBean.getWiId(), defectBean.getWiId(), changeType, quantity, changeUser);
			// 新增库存转换操作记录
			addWarehouseInventoryChange(db, defectBean.getWiId(), inventoryBean.getWiId(), changeType, quantity, changeUser);

			// 更新Mongo数据
			reportBean.setNormalQuantity(NumeralOperationKit.add(reportBean.getNormalQuantity(), quantity));
			if (changeType == WarehouseInventoryConfig.WIO_TYPE_NORMAL) {
				reportBean.setDefectQuantity(NumeralOperationKit.subtract(reportBean.getDefectQuantity(), quantity));
			}
			WarehouseInventoryReportBusiness reportBusiness = new WarehouseInventoryReportBusiness();
			reportBean.setModifyTime(DateKit.getCurrentTimestamp());
			reportBean.setModifyUser(changeUser);
			reportBusiness.updateWarehouseInventoryReport(reportBean);

			// 更新聚合库存记录
			WarehouseInventoryAggregateReportBusiness aggregateBusiness = new WarehouseInventoryAggregateReportBusiness();
			WarehouseInventoryAggregateReportBean aggregateBean = new WarehouseInventoryAggregateReportBean();
			aggregateBean.setWarehouseId(reportBean.getWarehouseId());
			aggregateBean.setWareId(reportBean.getWareId());
			aggregateBean.setWareType(reportBean.getWareType());
			aggregateBean.set_id(ErpMongoConfig.getWarehouseInventoryAggregateReportUnqueId(aggregateBean));
			WarehouseInventoryAggregateReportBean originalAggregateBean = aggregateBusiness
					.getWarehouseInventoryAggregateReportByKey(aggregateBean.get_id());
			originalAggregateBean.setNormalQuantity(NumeralOperationKit.add(originalAggregateBean.getNormalQuantity(), quantity));
			if (changeType == WarehouseInventoryConfig.WIO_TYPE_NORMAL) {
				originalAggregateBean.setDefectQuantity(NumeralOperationKit.subtract(originalAggregateBean.getDefectQuantity(), quantity));
			}
			originalAggregateBean.setModifyUser(reportBean.getModifyUser());
			originalAggregateBean.setModifyTime(reportBean.getModifyTime());
			aggregateBusiness.updateWarehouseInventoryAggregateReport(originalAggregateBean);

			db.commit();// 事务提交
		} catch (Exception e) {
			db.rollback();// 事务回滚
			log.error("", e);
			throw e;
		} finally {
			WarehouesInventoryOperationLock.lock.unlock();
			try {
				db.close();
			} catch (SQLException sqle) {
				log.error("", sqle);
			}
		}
	}

	/**
	 * 适用于实际出库的库存扣减
	 * 
	 * @param itemBean
	 * @throws Exception
	 */
	public void stockOutWarehouseInventory(DeliveryVoucherInfoBean deliveryVoucherInfoBean) throws Exception {

		WarehouesInventoryOperationLock.lock.lock(); // 获取lock，在事务完成后或发生异常后释放
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectW();
			db.beginTransition();// 事务开始

			// 找出明细
			DeliveryVoucherItemBusiness voucherItemBusiness = new DeliveryVoucherItemBusiness();
			List<DeliveryVoucherItemBean> voucherItemList = voucherItemBusiness.getDeliveryVoucherItemListByDvId(deliveryVoucherInfoBean.getDvId());
			processStockOutWarehouseInventory(db, deliveryVoucherInfoBean, voucherItemList);

			db.commit();// 事务提交
		} catch (Exception e) {
			db.rollback();// 事务回滚
			log.error("", e);
			throw e;
		} finally {
			WarehouesInventoryOperationLock.lock.unlock();
			try {
				db.close();
			} catch (SQLException sqle) {
				log.error("", sqle);
			}
		}
	}

	private void processStockOutWarehouseInventory(DBObject db, DeliveryVoucherInfoBean deliveryVoucherInfoBean,
			List<DeliveryVoucherItemBean> voucherItemList) throws Exception {
		int changeUser = deliveryVoucherInfoBean.getModifyUser();
		// 修改出库单状态
		if (updateDeliveryVoucherInfo(db, deliveryVoucherInfoBean) < 0) {
			throw new Exception("出库单更新失败");
		}
		WarehouseInventoryBusiness inventoryBusiness = new WarehouseInventoryBusiness();

		for (DeliveryVoucherItemBean itemBean : voucherItemList) {
			// 找出这个出库明细对应的库存记录
			WarehouseInventoryBean inventoryBean = inventoryBusiness.getWarehouseInventoryBeanByCondition(deliveryVoucherInfoBean.getWarehouseId(),
					itemBean.getWareType(), itemBean.getWareId(), itemBean.getInventoryType(), itemBean.getBatchNumber(),
					itemBean.getProductionDate());
			if (inventoryBean == null) {
				throw new Exception("找不到对应的库存");
			}

			// 检查库存数量于出库数量
			Double newStockInQty = NumeralOperationKit.subtract(inventoryBean.getQuantity(), itemBean.getStockOutQuantity());
			if (newStockInQty < 0f) {
				// 库存不够扣减
				throw new Exception("库存数量不足");
			}
			// 扣减库存
			inventoryBean.setQuantity(newStockInQty);
			inventoryBean.setModifyUser(changeUser);
			updateWarehouseInventory(db, inventoryBean);

			// 记录操作日志
			addWarehouseInventoryLog(db, inventoryBean.getWiId(), deliveryVoucherInfoBean.getDvId(), WarehouseInventoryConfig.WIO_TYPE_STOCK_OUT,
					itemBean.getStockOutQuantity(), changeUser);

			// 新增库存转换操作记录
			addWarehouseInventoryChange(db, inventoryBean.getWiId(), 0, WarehouseInventoryConfig.WIO_TYPE_STOCK_OUT, itemBean.getStockOutQuantity(),
					changeUser);

			// 获取REPORT BEAN
			WarehouseInventoryReportBusiness reportBusiness = new WarehouseInventoryReportBusiness();
			WarehouseInventoryAggregateReportBusiness aggregateBusiness = new WarehouseInventoryAggregateReportBusiness();

			WarehouseInventoryReportBean reportBean = new WarehouseInventoryReportBean();
			reportBean.setWarehouseId(deliveryVoucherInfoBean.getWarehouseId());
			reportBean.setBatchNumber(itemBean.getBatchNumber());
			reportBean.setWareId(itemBean.getWareId());
			reportBean.setWareType(itemBean.getWareType());
			reportBean.setProductionDate(itemBean.getProductionDate());
			reportBean.setGuaranteePeriod(itemBean.getGuaranteePeriod());
			reportBean.set_id(ErpMongoConfig.getWarehouseInventoryReportUnqueId(reportBean));
			WarehouseInventoryReportBean originalBean = reportBusiness.getWarehouseInventoryReportByKey(reportBean.get_id());

			WarehouseInventoryAggregateReportBean aggregateBean = new WarehouseInventoryAggregateReportBean();
			aggregateBean.setWarehouseId(reportBean.getWarehouseId());
			aggregateBean.setWareId(reportBean.getWareId());
			aggregateBean.setWareType(reportBean.getWareType());
			aggregateBean.set_id(ErpMongoConfig.getWarehouseInventoryAggregateReportUnqueId(aggregateBean));
			WarehouseInventoryAggregateReportBean originalAggregateBean = aggregateBusiness
					.getWarehouseInventoryAggregateReportByKey(aggregateBean.get_id());

			// 根据库存类型判断应该修改哪个数据
			switch (itemBean.getInventoryType()) {
			case WarehouseInventoryConfig.WIT_DAMAGE:
				originalBean.setDamageQuantity(NumeralOperationKit.subtract(originalBean.getDamageQuantity(), itemBean.getStockOutQuantity()));
				originalAggregateBean
						.setDamageQuantity(NumeralOperationKit.subtract(originalAggregateBean.getDamageQuantity(), itemBean.getStockOutQuantity()));
				break;
			case WarehouseInventoryConfig.WIT_DEFECT:
				originalBean.setDefectQuantity(NumeralOperationKit.subtract(originalBean.getDefectQuantity(), itemBean.getStockOutQuantity()));
				originalAggregateBean
						.setDefectQuantity(NumeralOperationKit.subtract(originalAggregateBean.getDefectQuantity(), itemBean.getStockOutQuantity()));
				break;
			case WarehouseInventoryConfig.WIT_NORMAL:
				originalBean.setNormalQuantity(NumeralOperationKit.subtract(originalBean.getNormalQuantity(), itemBean.getStockOutQuantity()));
				originalAggregateBean
						.setNormalQuantity(NumeralOperationKit.subtract(originalAggregateBean.getNormalQuantity(), itemBean.getStockOutQuantity()));
				break;
			default:
				throw new Exception("不正确的库存类型");
			}
			// 更新MONGO
			originalBean.setModifyTime(DateKit.getCurrentTimestamp());
			originalBean.setModifyUser(changeUser);
			reportBusiness.updateWarehouseInventoryReport(originalBean);

			originalAggregateBean.setModifyTime(originalBean.getModifyTime());
			originalAggregateBean.setModifyUser(changeUser);
			aggregateBusiness.updateWarehouseInventoryAggregateReport(originalAggregateBean);
		}
	}

	// 用于处理有关联原单的出库单
	private Map<String, Object> processDeliveryVoucherBySource(DBObject db, DeliveryVoucherInfoBean bean, List<DeliveryVoucherItemBean> dvItemList)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (bean.getSourceOrderType() == SourceOrderTypeConfig.SOURCE_ORDER_TYPE_WAREHOUSE_TRANSFER_ORDER) {
			// 关联了调拨单
			WarehouseTransferOrderInfoBusiness transferOrderInfoBusiness = new WarehouseTransferOrderInfoBusiness();
			WarehouseTransferOrderInfoBean transferOrderInfoBean = transferOrderInfoBusiness
					.getWarehouseTransferOrderInfoByKey(bean.getSourceOrderId());
			List<WarehouseVoucherItemBean> itemList = new ArrayList<WarehouseVoucherItemBean>(dvItemList.size());
			if (transferOrderInfoBean != null) {
				// 修改调拨单为在途
				WarehouseTransferOrderInfoBean originalBean = new WarehouseTransferOrderInfoBean();
				BeanKit.copyBean(originalBean, transferOrderInfoBean);
				resultMap.put("originalBean", originalBean);
				transferOrderInfoBean.setToStatus(WarehouseTransferOrderConfig.TO_STATUS_IN_TRANSIT);
				transferOrderInfoBean.setModifyTime(DateKit.getCurrentTimestamp());
				transferOrderInfoBean.setModifyUser(bean.getModifyUser());
				updateWarehouseTransferOrderInfo(db, transferOrderInfoBean);
				resultMap.put("currentBean", transferOrderInfoBean);
				// 新建入库单
				int result = -1;
				WarehouseVoucherInfoBean warehouseVoucherInfoBean = new WarehouseVoucherInfoBean();
				warehouseVoucherInfoBean.setWvNumber(WarehouseVoucherConfig.WVN_PREFIX_TRANSFER
						+ transferOrderInfoBean.getToNumber().replace(WarehouseTransferOrderConfig.TO_NUMBER_PREFIX, ""));
				warehouseVoucherInfoBean.setWvType(WarehouseVoucherConfig.WVT_TRANSFER);
				warehouseVoucherInfoBean.setSourceOrderType(SourceOrderTypeConfig.SOURCE_ORDER_TYPE_WAREHOUSE_TRANSFER_ORDER);
				warehouseVoucherInfoBean.setSourceOrderId(transferOrderInfoBean.getToId());
				warehouseVoucherInfoBean.setWareType(transferOrderInfoBean.getWareType());
				warehouseVoucherInfoBean.setStockInTime(DateKit.getCurrentTimestamp());
				warehouseVoucherInfoBean.setWarehouseId(transferOrderInfoBean.getTargetWarehouseId());
				warehouseVoucherInfoBean.setInspectorName(transferOrderInfoBean.getWvInspectorName());
				warehouseVoucherInfoBean.setReceiverName(transferOrderInfoBean.getWvReceiverName());
				warehouseVoucherInfoBean.setAccountClerkName(transferOrderInfoBean.getWvAccountClerkName());
				warehouseVoucherInfoBean.setApproveStatus(WarehouseVoucherConfig.REVIEW_STATUS_PASS);
				warehouseVoucherInfoBean.setWvStatus(WarehouseVoucherConfig.WV_STATUS_APPROVED);
				warehouseVoucherInfoBean.setSyncStatus(WarehouseVoucherConfig.SYNC_STATUS_NOT_SYNCHORIZED);
				warehouseVoucherInfoBean.setStatus(Constants.STATUS_VALID);
				warehouseVoucherInfoBean.setCreateTime(DateKit.getCurrentTimestamp());
				warehouseVoucherInfoBean.setCreateUser(bean.getModifyUser());
				result = addWarehouseVoucherInfo(db, warehouseVoucherInfoBean);
				resultMap.put("warehouseVoucherInfoBean", warehouseVoucherInfoBean);
				// 获取出库单明细
				WarehouseVoucherItemBean warehouseVoucherItemBean = null;
				for (DeliveryVoucherItemBean dvItemBean : dvItemList) {
					warehouseVoucherItemBean = new WarehouseVoucherItemBean();
					warehouseVoucherItemBean.setWvId(result);
					warehouseVoucherItemBean.setWareType(dvItemBean.getWareType());
					warehouseVoucherItemBean.setWareId(dvItemBean.getWareId());
					warehouseVoucherItemBean.setUnit(dvItemBean.getUnit());
					warehouseVoucherItemBean.setReceivableQuantity(dvItemBean.getStockOutQuantity());
					warehouseVoucherItemBean.setStockInQuantity(dvItemBean.getStockOutQuantity());
					warehouseVoucherItemBean.setBatchNumber(dvItemBean.getBatchNumber());
					warehouseVoucherItemBean.setProductionDate(dvItemBean.getProductionDate());
					warehouseVoucherItemBean.setGuaranteePeriod(dvItemBean.getGuaranteePeriod());
					warehouseVoucherItemBean.setExpirationDate(dvItemBean.getExpirationDate());
					warehouseVoucherItemBean.setCreateTime(DateKit.getCurrentTimestamp());
					warehouseVoucherItemBean.setCreateUser(bean.getModifyUser());
					warehouseVoucherItemBean.setStatus(Constants.STATUS_VALID);
					addWarehouseVoucherItem(db, warehouseVoucherItemBean);
					itemList.add(warehouseVoucherItemBean);
				}
				resultMap.put("wvItemList", itemList);
				// 执行入库单审批要做的动作
				processCreateWarehouseInventory(db, warehouseVoucherInfoBean, itemList);

			} else {
				throw new Exception("关联的调拨单不存在");
			}
		}
		return resultMap;
	}

	private Map<String, Object> processWarehouseVoucherBySource(DBObject db, WarehouseVoucherInfoBean bean) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (bean.getSourceOrderType() == SourceOrderTypeConfig.SOURCE_ORDER_TYPE_WAREHOUSE_TRANSFER_ORDER) {
			// 关联了调拨单
			WarehouseTransferOrderInfoBusiness transferOrderInfoBusiness = new WarehouseTransferOrderInfoBusiness();
			WarehouseTransferOrderInfoBean transferOrderInfoBean = transferOrderInfoBusiness
					.getWarehouseTransferOrderInfoByKey(bean.getSourceOrderId());
			if (transferOrderInfoBean != null) {
				WarehouseTransferOrderInfoBean originalBean = new WarehouseTransferOrderInfoBean();
				BeanKit.copyBean(originalBean, transferOrderInfoBean);
				resultMap.put("originalBean", originalBean);
				// 修改调拨单为在途
				transferOrderInfoBean.setToStatus(WarehouseTransferOrderConfig.TO_STATUS_CLOSED);
				transferOrderInfoBean.setModifyTime(DateKit.getCurrentTimestamp());
				transferOrderInfoBean.setModifyUser(bean.getModifyUser());
				updateWarehouseTransferOrderInfo(db, transferOrderInfoBean);
				resultMap.put("currentBean", transferOrderInfoBean);
			} else {
				throw new Exception("关联的调拨单不存在");
			}
		}
		return resultMap;
	}

	// 出库单关闭
	@SuppressWarnings("unchecked")
	public void closeDeliveryVoucherInfo(DeliveryVoucherInfoBean deliveryVoucherInfoBean) throws Exception {
		WarehouesInventoryOperationLock.lock.lock(); // 获取lock，在事务完成后或发生异常后释放
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;

		try {
			db = dbFactory.getDBObjectW();
			db.beginTransition();// 事务开始
			
			//更新出库单
			updateDeliveryVoucherInfo(db, deliveryVoucherInfoBean);
			// 找出明细
			DeliveryVoucherItemBusiness voucherItemBusiness = new DeliveryVoucherItemBusiness();
			List<DeliveryVoucherItemBean> voucherItemList = voucherItemBusiness.getDeliveryVoucherItemListByDvId(deliveryVoucherInfoBean.getDvId());
			if(voucherItemList.isEmpty()){
				throw new Exception("找不到出库单明细");
			}
			Map<String, Object> processResult = processDeliveryVoucherBySource(db, deliveryVoucherInfoBean, voucherItemList);
			db.commit();// 事务提交
			// 发送相关日志
			WarehouseVoucherInfoBean warehouseVoucherInfoBean = (WarehouseVoucherInfoBean) processResult.get("warehouseVoucherInfoBean");
			List<WarehouseVoucherItemBean> wvItemList = (List<WarehouseVoucherItemBean>) processResult.get("wvItemList");
			if (warehouseVoucherInfoBean != null && wvItemList != null) {
				sendWarehouseVoucherAddLog(warehouseVoucherInfoBean, wvItemList);
			}
			WarehouseTransferOrderInfoBean originalBean = (WarehouseTransferOrderInfoBean) processResult.get("originalBean");
			WarehouseTransferOrderInfoBean currentBean = (WarehouseTransferOrderInfoBean) processResult.get("currentBean");
			if (originalBean != null && currentBean != null) {
				sendWarehouseTransferOrderInfoUpdateLog(originalBean, currentBean);
			}
		} catch (Exception e) {
			db.rollback();// 事务回滚
			log.error("", e);
			throw e;
		} finally {
			WarehouesInventoryOperationLock.lock.unlock();
			try {
				db.close();
			} catch (SQLException sqle) {
				log.error("", sqle);
			}
		}
	}

	// 调拨单审批通过时使用
	public void approveWarehouseTransferInfo(WarehouseTransferOrderInfoBean bean) throws Exception {
		// 锁住发货单
		DeliveryVoucherNumberGenerator.lock.lock();
		DBFactory dbFactory = new ErpDBFactory();
		DBObject db = null;
		try {
			db = dbFactory.getDBObjectW();
			db.beginTransition();
			int result = -1;
			int userId = bean.getModifyUser();
			// 找出调拨单明细
			WarehouseTransferOrderItemBusiness transferOrderItemBusiness = new WarehouseTransferOrderItemBusiness();
			List<WarehouseTransferOrderItemBean> toItemList = transferOrderItemBusiness.getTransferOrderItemListByToId(bean.getToId());
			if (toItemList.isEmpty()) {
				throw new Exception("调拨单详情不存在");
			}

			// 调拨单设置成审批通过
			Timestamp currentTimeStamp = DateKit.getCurrentTimestamp();
			bean.setModifyTime(currentTimeStamp);
			bean.setModifyUser(userId);
			updateWarehouseTransferOrderInfo(db, bean);

			// 创建出库单
			DeliveryVoucherInfoBean deliveryVoucherInfoBean = new DeliveryVoucherInfoBean();
			deliveryVoucherInfoBean.setDvType(DeliveryVoucherConfig.DVT_TRANSFER);
			deliveryVoucherInfoBean.setDvNumber(
					DeliveryVoucherConfig.DV_NUMBER_PREFIX_TRANSFER + bean.getToNumber().replace(WarehouseTransferOrderConfig.TO_NUMBER_PREFIX, ""));
			deliveryVoucherInfoBean.setWareType(bean.getWareType());
			deliveryVoucherInfoBean.setSourceOrderType(SourceOrderTypeConfig.SOURCE_ORDER_TYPE_WAREHOUSE_TRANSFER_ORDER);
			deliveryVoucherInfoBean.setSourceOrderId(bean.getToId());
			deliveryVoucherInfoBean.setWarehouseId(bean.getSourceWarehouseId());
			deliveryVoucherInfoBean.setInspectorName(bean.getDvInspectorName());
			deliveryVoucherInfoBean.setSenderName(bean.getDvSenderName());
			deliveryVoucherInfoBean.setAccountClerkName(bean.getDvAccountClerkName());
			deliveryVoucherInfoBean.setStockOutTime(currentTimeStamp);
			deliveryVoucherInfoBean.setSyncStatus(DeliveryVoucherConfig.SYNC_STATUS_NOT_SYNCHORIZED);
			deliveryVoucherInfoBean.setDvStatus(DeliveryVoucherConfig.DV_STATUS_APPROVED);
			deliveryVoucherInfoBean.setApproveStatus(DeliveryVoucherConfig.REVIEW_STATUS_PASS);
			deliveryVoucherInfoBean.setStatus(Constants.STATUS_VALID);
			deliveryVoucherInfoBean.setCreateTime(currentTimeStamp);
			deliveryVoucherInfoBean.setCreateUser(userId);
			result = addDeliveryVoucherInfo(db, deliveryVoucherInfoBean);

			// 创建出库单明细
			DeliveryVoucherItemBean deliveryVoucherItemBean = null;
			List<DeliveryVoucherItemBean> dvItemList = new ArrayList<DeliveryVoucherItemBean>(toItemList.size());
			for (WarehouseTransferOrderItemBean toItemBean : toItemList) {
				deliveryVoucherItemBean = new DeliveryVoucherItemBean();
				deliveryVoucherItemBean.setDvId(result);
				deliveryVoucherItemBean.setWareType(toItemBean.getWareType());
				deliveryVoucherItemBean.setWareId(toItemBean.getWareId());
				deliveryVoucherItemBean.setUnit(toItemBean.getUnit());
				deliveryVoucherItemBean.setInventoryType(WarehouseInventoryConfig.WIT_NORMAL);
				deliveryVoucherItemBean.setStockOutQuantity(toItemBean.getQuanlity());
				deliveryVoucherItemBean.setBatchNumber(toItemBean.getBatchNumber());
				deliveryVoucherItemBean.setProductionDate(toItemBean.getProductionDate());
				deliveryVoucherItemBean.setExpirationDate(toItemBean.getExpirationDate());
				deliveryVoucherItemBean.setGuaranteePeriod(toItemBean.getGuaranteePeriod());
				deliveryVoucherItemBean.setStatus(Constants.STATUS_VALID);
				deliveryVoucherItemBean.setCreateTime(currentTimeStamp);
				deliveryVoucherItemBean.setCreateUser(userId);
				addDeliveryVoucherItem(db, deliveryVoucherItemBean);
				dvItemList.add(deliveryVoucherItemBean);
			}

			// 检验库存
			ReturnMessageBean messageBean = verifyWarehouseInventory(dvItemList, deliveryVoucherInfoBean);
			if (StringKit.isValid(messageBean.getMessage())) {
				throw new Exception(messageBean.getMessage());
			}

			// 出库
			processStockOutWarehouseInventory(db, deliveryVoucherInfoBean, dvItemList);

			db.commit();
			// 发送日志
			sendDeliveryVoucherAddLog(deliveryVoucherInfoBean, dvItemList);
		} catch (Exception e) {
			db.rollback();// 事务回滚
			log.error("", e);
			throw e;
		} finally {
			DeliveryVoucherNumberGenerator.lock.unlock();
			try {
				db.close();
			} catch (SQLException sqle) {
				log.error("", sqle);
			}
		}
	}

	public ReturnMessageBean verifyWarehouseInventory(DeliveryVoucherInfoBean deliveryVoucherInfoBean) {
		// 找出明细
		DeliveryVoucherItemBusiness voucherItemBusiness = new DeliveryVoucherItemBusiness();
		List<DeliveryVoucherItemBean> voucherItemList = voucherItemBusiness.getDeliveryVoucherItemListByDvId(deliveryVoucherInfoBean.getDvId());
		return verifyWarehouseInventory(voucherItemList, deliveryVoucherInfoBean);
	}

	public ReturnMessageBean verifyWarehouseInventory(List<DeliveryVoucherItemBean> voucherItemList,
			DeliveryVoucherInfoBean deliveryVoucherInfoBean) {
		ReturnMessageBean messageBean = new ReturnMessageBean();
		WarehouseInventoryBusiness inventoryBusiness = new WarehouseInventoryBusiness();
		for (DeliveryVoucherItemBean itemBean : voucherItemList) {
			// 找出这个出库明细对应的库存记录
			WarehouseInventoryBean inventoryBean = inventoryBusiness.getWarehouseInventoryBeanByCondition(deliveryVoucherInfoBean.getWarehouseId(),
					itemBean.getWareType(), itemBean.getWareId(), itemBean.getInventoryType(), itemBean.getBatchNumber(),
					itemBean.getProductionDate());
			if (inventoryBean == null) {
				messageBean.setMessage("找不到对应的库存");
				return messageBean;
			}

			// 检查库存数量于出库数量
			Double newStockInQty = NumeralOperationKit.subtract(inventoryBean.getQuantity(), itemBean.getStockOutQuantity());
			if (newStockInQty < 0f) {
				// 库存不够扣减
				messageBean.setMessage("库存数量不足");
				return messageBean;
			}
		}
		return messageBean;
	}
}
