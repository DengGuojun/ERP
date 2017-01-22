package com.lpmas.erp.report.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.erp.config.ErpMongoConfig;
import com.lpmas.erp.inventory.bean.WarePlannedPriceBean;
import com.lpmas.erp.inventory.bean.WarehouseInventoryBean;
import com.lpmas.erp.inventory.business.WarePlannedPriceBusiness;
import com.lpmas.erp.inventory.business.WarehouseInventoryBusiness;
import com.lpmas.erp.inventory.business.WarehouseInventoryLogBusiness;
import com.lpmas.erp.inventory.config.WarehouseInventoryConfig;
import com.lpmas.erp.report.bean.MonthlyInventoryReportBean;
import com.lpmas.erp.report.bean.WarehouseInventoryLogSumResultBean;
import com.lpmas.erp.report.config.ReportConsoleConfig;
import com.lpmas.erp.report.dao.MonthlyInventoryReportDao;
import com.lpmas.erp.warehouse.bean.WarehouseInfoBean;
import com.lpmas.framework.excel.ExcelConfig;
import com.lpmas.framework.excel.ExcelWriteBean;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.DateKit;
import com.lpmas.framework.util.JsonKit;
import com.lpmas.framework.util.NumeralOperationKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.pdm.business.WareInfoMediator;
import com.mongodb.BasicDBObject;

public class MonthlyInventoryReportBusniess {
	private static Logger logger = LoggerFactory.getLogger(MonthlyInventoryReportBusniess.class);

	public int addMonthlyInventoryReport(MonthlyInventoryReportBean bean) {
		MonthlyInventoryReportDao dao = new MonthlyInventoryReportDao();
		return dao.insertMonthlyInventoryReport(bean);
	}

	public int updateMonthlyInventoryReport(MonthlyInventoryReportBean bean) {
		MonthlyInventoryReportDao dao = new MonthlyInventoryReportDao();
		return dao.updateMonthlyInventoryReport(bean);
	}

	public List<MonthlyInventoryReportBean> getMonthlyInventoryReportListByMap(HashMap<String, Object> condMap) {
		MonthlyInventoryReportDao dao = new MonthlyInventoryReportDao();
		return dao.getMonthlyInventoryReportListByMap(condMap);
	}

	public int deleteMonthlyInventoryReportByMap(Map<String, Object> condMap) {
		MonthlyInventoryReportDao dao = new MonthlyInventoryReportDao();
		return dao.deleteMonthlyInventoryReportByMap(condMap);
	}

	public MonthlyInventoryReportBean getMonthlyInventoryReportByKey(String _id) {
		MonthlyInventoryReportDao dao = new MonthlyInventoryReportDao();
		return dao.getMonthlyInventoryReportByKey(_id);
	}

	public MonthlyInventoryReportBean getSumResultByInventoryType(List<MonthlyInventoryReportBean> reportList,
			Set<Integer> effectInventoryType) {
		MonthlyInventoryReportBean result = new MonthlyInventoryReportBean();
		for (MonthlyInventoryReportBean bean : reportList) {
			if (effectInventoryType.contains(bean.getInventoryType())) {
				result.setQuantity(NumeralOperationKit.add(result.getQuantity(), bean.getQuantity()));
			}
		}
		return result;
	}

	public boolean summaryMonthlyInvenoryByDate(String reportMonth) {
		try {
			// 验证一下查询月份
			DateKit.str2Date(reportMonth, ReportConsoleConfig.MONTHLY_REPORT_DATE_FORMAT);
			String currentMonth = DateKit.getCurrentDateTime(ReportConsoleConfig.MONTHLY_REPORT_DATE_FORMAT);
			if (!DateKit.compareTime(reportMonth, currentMonth, ReportConsoleConfig.MONTHLY_REPORT_DATE_FORMAT)) {
				return false;
			}

			// 删除之前的汇总数据
			Map<String, Object> condMap = new HashMap<String, Object>();
			condMap.put("reportMonth", reportMonth);
			deleteMonthlyInventoryReportByMap(condMap);

			// 这个日期之前的数据
			String endDayTime = ReportUtil.getLastSecondOfMonth(
					DateKit.str2Date(reportMonth, ReportConsoleConfig.MONTHLY_REPORT_DATE_FORMAT));

			// 对象准备
			WarehouseInventoryLogBusiness inventoryLogBusiness = new WarehouseInventoryLogBusiness();
			List<WarehouseInventoryLogSumResultBean> sumReusltList = null;
			MonthlyInventoryReportBean reportBean = null;
			MonthlyInventoryReportBean exsitBean = null;
			boolean result = true;
			int stepResult = -1;

			// 查询所有库存记录
			WarehouseInventoryBusiness inventoryBusiness = new WarehouseInventoryBusiness();

			int pageSize = 50;
			int pageNum = 1;
			// 循环读取数据
			PageBean pageBean = new PageBean(pageNum, pageSize);
			PageResultBean<WarehouseInventoryBean> resultBean = inventoryBusiness
					.getWarehouseInventoryPageListByMap(new HashMap<String, String>(), pageBean);

			while (!resultBean.getRecordList().isEmpty()) {
				// 遍历库存
				String key = "";
				for (WarehouseInventoryBean bean : resultBean.getRecordList()) {
					// BEAN初始化
					key = ErpMongoConfig.getMonthlyInventoryReportKey(bean.getWareType(), bean.getWareId(),
							bean.getInventoryType(), bean.getWarehouseId(), reportMonth);
					reportBean = new MonthlyInventoryReportBean();
					reportBean.setWareType(bean.getWareType());
					reportBean.setWareId(bean.getWareId());
					reportBean.setInventoryType(bean.getInventoryType());
					reportBean.setWarehouseId(bean.getWarehouseId());
					reportBean.setReportMonth(reportMonth);
					reportBean.setUnit(bean.getUnit());
					reportBean.set_id(key);

					sumReusltList = inventoryLogBusiness
							.getWarehouseInventoryLogSumResultListByCondition(bean.getWiId(), endDayTime);

					// 遍历聚合结果
					for (WarehouseInventoryLogSumResultBean sumResultBean : sumReusltList) {
						// 根据不同库存类型增减
						if (reportBean.getInventoryType() == WarehouseInventoryConfig.WIT_NORMAL) {
							if (sumResultBean.getChangeType() == WarehouseInventoryConfig.WIO_TYPE_STOCK_OUT) {
								reportBean.setQuantity(NumeralOperationKit.add(reportBean.getQuantity(),
										-sumResultBean.getSumQuantity()));
							} else if (sumResultBean.getChangeType() == WarehouseInventoryConfig.WIO_TYPE_UPDATE
									|| sumResultBean.getChangeType() == WarehouseInventoryConfig.WIO_TYPE_PUT_ON
									|| sumResultBean.getChangeType() == WarehouseInventoryConfig.WIO_TYPE_OVERAGE
									|| sumResultBean.getChangeType() == WarehouseInventoryConfig.WIO_TYPE_NORMAL
									|| sumResultBean.getChangeType() == WarehouseInventoryConfig.WIO_TYPE_LOSS
									|| sumResultBean.getChangeType() == WarehouseInventoryConfig.WIO_TYPE_DEFECT
									|| sumResultBean.getChangeType() == WarehouseInventoryConfig.WIO_TYPE_DAMAGE) {
								reportBean.setQuantity(NumeralOperationKit.add(reportBean.getQuantity(),
										sumResultBean.getSumQuantity()));
							}
						} else if (reportBean.getInventoryType() == WarehouseInventoryConfig.WIT_DAMAGE) {
							if (sumResultBean.getChangeType() == WarehouseInventoryConfig.WIO_TYPE_STOCK_OUT) {
								reportBean.setQuantity(NumeralOperationKit.add(reportBean.getQuantity(),
										-sumResultBean.getSumQuantity()));
							} else if (sumResultBean.getChangeType() == WarehouseInventoryConfig.WIO_TYPE_DAMAGE) {
								reportBean.setQuantity(NumeralOperationKit.add(reportBean.getQuantity(),
										sumResultBean.getSumQuantity()));
							}
						} else if (reportBean.getInventoryType() == WarehouseInventoryConfig.WIT_DEFECT) {
							if (sumResultBean.getChangeType() == WarehouseInventoryConfig.WIO_TYPE_STOCK_OUT) {
								reportBean.setQuantity(NumeralOperationKit.add(reportBean.getQuantity(),
										-sumResultBean.getSumQuantity()));
							} else if (sumResultBean.getChangeType() == WarehouseInventoryConfig.WIO_TYPE_PUT_ON
									|| sumResultBean.getChangeType() == WarehouseInventoryConfig.WIO_TYPE_DEFECT
									|| sumResultBean.getChangeType() == WarehouseInventoryConfig.WIO_TYPE_NORMAL
									|| sumResultBean.getChangeType() == WarehouseInventoryConfig.WIO_TYPE_UPDATE) {
								reportBean.setQuantity(NumeralOperationKit.add(reportBean.getQuantity(),
										sumResultBean.getSumQuantity()));
							}
						}
					}

					// 判断是否存在
					exsitBean = getMonthlyInventoryReportByKey(reportBean.get_id());
					reportBean.setCreateTime(DateKit.getCurrentTimestamp());
					if (exsitBean != null) {
						reportBean.setQuantity(
								NumeralOperationKit.add(reportBean.getQuantity(), exsitBean.getQuantity()));
						stepResult = updateMonthlyInventoryReport(reportBean);
					} else {
						stepResult = addMonthlyInventoryReport(reportBean);
					}
					if (stepResult <= 0) {
						result = result && false;
						logger.error("数据汇总失败:{}", JsonKit.toJson(bean));
					}
				}

				pageNum++;
				pageBean = new PageBean(pageNum, pageSize);
				resultBean = inventoryBusiness.getWarehouseInventoryPageListByMap(new HashMap<String, String>(),
						pageBean);
			}

			return result;

		} catch (Exception e) {
			logger.error("", e);
			return false;
		}
	}

	public ExcelWriteBean createMonthlyReport(int wareType, List<WarehouseInfoBean> warehouseInfoList,
			String queryOpenMonth, String queryEndMonth, String reportMonth, String createUserName,
			boolean isContainHeader) throws Exception {
		HashMap<String, Object> condMap = new HashMap<String, Object>();
		condMap.put("wareType", wareType);
		WarehouseInventoryLogBusiness warehouseInventoryLogBusiness = new WarehouseInventoryLogBusiness();
		WarePlannedPriceBusiness warePlannedPriceBusiness = new WarePlannedPriceBusiness();
		WarePlannedPriceBean openPlannedPriceBean = null;
		WarePlannedPriceBean endPlannedPriceBean = null;
		List<MonthlyInventoryReportBean> openInventoryList = null;
		MonthlyInventoryReportBean openInventoryBean = null;
		List<MonthlyInventoryReportBean> endInventoryList = null;
		MonthlyInventoryReportBean endInventoryBean = null;
		List<Integer> changeTypeList = null;
		WarehouseInventoryLogSumResultBean sumResultBean = null;
		List<Integer> wiIdList = null;
		ExcelWriteBean excelWriteBean = new ExcelWriteBean();
		List<Integer> warehouseIdList = ReportUtil.subList(warehouseInfoList, "warehouseId");
		WareInfoMediator mediator = new WareInfoMediator();
		// 遍历仓库获取库存信息
		condMap.put("warehouseId", new BasicDBObject("$in", warehouseIdList.toArray()));

		// 检查数据完整性
		WarehouseInventoryBusiness warehouseInventoryBusiness = new WarehouseInventoryBusiness();
		List<WarehouseInventoryBean> wiList = warehouseInventoryBusiness.getWarehouseInventoryListByCondition(wareType,
				warehouseIdList);

		// 获取该仓库所有的WAREID
		Set<Integer> wareIdList = warehouseInventoryBusiness.getWareIdListFromWiList(wiList);

		// 开始写EXCEL
		excelWriteBean.setFileType(ExcelConfig.FT_XLSX);
		excelWriteBean.setFileName(ReportConsoleConfig.MONTHLY_REPORT_NAME_PATTERN + reportMonth);
		excelWriteBean.setSheetName(ReportConsoleConfig.MONTHLY_REPORT_NAME_PATTERN);
		List<List<Object>> contentList = new ArrayList<List<Object>>();
		List<Object> rowContentList = null;

		if (isContainHeader) {
			// 表头信息
			List<String> headerList = new ArrayList<String>();
			excelWriteBean.setHeaderList(headerList);

			// 补充表头
			rowContentList = new ArrayList<Object>();
			ReportUtil.putEmptyCell(rowContentList, 3);
			rowContentList.add("期初库存");
			ReportUtil.putEmptyCell(rowContentList, 2);
			rowContentList.add("本期入库");
			ReportUtil.putEmptyCell(rowContentList, 2);
			rowContentList.add("本期出库");
			ReportUtil.putEmptyCell(rowContentList, 2);
			rowContentList.add("期末库存");
			contentList.add(rowContentList);
			// 处理栏位
			rowContentList = new ArrayList<Object>();
			rowContentList.add("品名");
			rowContentList.add("存放地");
			rowContentList.add("单位");
			rowContentList.add("数量");
			rowContentList.add("单价");
			rowContentList.add("金额");
			rowContentList.add("数量");
			rowContentList.add("单价");
			rowContentList.add("金额");
			rowContentList.add("数量");
			rowContentList.add("单价");
			rowContentList.add("金额");
			rowContentList.add("数量");
			rowContentList.add("单价");
			rowContentList.add("金额");
			rowContentList.add("备注");
			contentList.add(rowContentList);
		}
		String itemName = "";
		String itemUnit = "";
		String itemUnitName = "";
		double openQutity = 0;
		double addQutity = 0;
		double subtractQutity = 0;
		double endQutity = 0;

		for (Integer wareId : wareIdList) {
			// 对应制品期初库存列表
			condMap.put("reportMonth", queryOpenMonth);
			condMap.put("wareId", wareId);
			openInventoryList = getMonthlyInventoryReportListByMap(condMap);
			openInventoryBean = getSumResultByInventoryType(openInventoryList,
					ReportConsoleConfig.EFFECT_INVENTORY_TYPE_SET);

			// 对应制品期末库存列表
			condMap.put("reportMonth", queryEndMonth);
			condMap.put("wareId", wareId);
			endInventoryList = getMonthlyInventoryReportListByMap(condMap);
			endInventoryBean = getSumResultByInventoryType(endInventoryList,
					ReportConsoleConfig.EFFECT_INVENTORY_TYPE_SET);

			// 再次验证数据
			if (openInventoryList.isEmpty() || endInventoryList.isEmpty()) {
				throw new Exception("数据不足无法导出!");
			}

			// 开始组装一行数据
			rowContentList = new ArrayList<Object>();
			// 处理制品信息
			itemName = mediator.getWareNameByKey(wareType, wareId);
			if (!StringKit.isValid(itemName))
				itemName = "制品名称缺失";
			rowContentList.add(itemName);
			// 处理仓库名称
			rowContentList.add("共" + warehouseInfoList.size() + "个仓库");
			// 处理单位
			itemUnitName = mediator.getWareUnitNameByKey(wareType, wareId);
			if (!StringKit.isValid(itemUnitName))
				itemUnitName = "单位名称缺失";
			rowContentList.add(itemUnitName);
			// 处理计划单价
			openPlannedPriceBean = warePlannedPriceBusiness.getWarePlannedPriceByKey(wareType, wareId, queryOpenMonth,
					itemUnit);
			endPlannedPriceBean = warePlannedPriceBusiness.getWarePlannedPriceByKey(wareType, wareId, queryEndMonth,
					itemUnit);
			if (openPlannedPriceBean == null)
				openPlannedPriceBean = new WarePlannedPriceBean();
			if (endPlannedPriceBean == null)
				endPlannedPriceBean = new WarePlannedPriceBean();
			// 期初数量，单价，总额
			rowContentList.add(openInventoryBean.getQuantity());
			rowContentList.add(openPlannedPriceBean.getPrice());
			rowContentList.add(
					NumeralOperationKit.multiply(openInventoryBean.getQuantity(), openPlannedPriceBean.getPrice()));
			openQutity = NumeralOperationKit.add(openQutity, openInventoryBean.getQuantity());
			// 处理本期入库
			changeTypeList = new ArrayList<Integer>(2);
			changeTypeList.add(WarehouseInventoryConfig.WIO_TYPE_PUT_ON);
			changeTypeList.add(WarehouseInventoryConfig.WIO_TYPE_UPDATE);
			wiIdList = warehouseInventoryBusiness.getVaildWiIdListByCondition(wareId, wareType, wiList);
			sumResultBean = warehouseInventoryLogBusiness.getWarehouseInventoryLogSumResultBeanByCondition(wiIdList,
					changeTypeList,
					DateKit.formatStr(queryEndMonth, ReportConsoleConfig.MONTHLY_REPORT_DATE_FORMAT,
							DateKit.DEFAULT_DATE_TIME_FORMAT),
					ReportUtil.getLastSecondOfMonth(
							DateKit.str2Date(queryEndMonth, ReportConsoleConfig.MONTHLY_REPORT_DATE_FORMAT)));
			if (sumResultBean == null) {
				throw new Exception("数据缺失无法导出!");
			}
			// 修正结果正负性
			sumResultBean.setSumQuantity(Math.abs(sumResultBean.getSumQuantity()));
			rowContentList.add(sumResultBean.getSumQuantity());
			rowContentList.add(endPlannedPriceBean.getPrice());
			rowContentList
					.add(NumeralOperationKit.multiply(sumResultBean.getSumQuantity(), endPlannedPriceBean.getPrice()));
			addQutity = NumeralOperationKit.add(addQutity, sumResultBean.getSumQuantity());
			// 处理本期出库
			changeTypeList = new ArrayList<Integer>(1);
			changeTypeList.add(WarehouseInventoryConfig.WIO_TYPE_STOCK_OUT);
			wiIdList = warehouseInventoryBusiness.getVaildWiIdListByCondition(wareId, wareType, wiList);
			sumResultBean = warehouseInventoryLogBusiness.getWarehouseInventoryLogSumResultBeanByCondition(wiIdList,
					changeTypeList,
					DateKit.formatStr(queryEndMonth, ReportConsoleConfig.MONTHLY_REPORT_DATE_FORMAT,
							DateKit.DEFAULT_DATE_TIME_FORMAT),
					ReportUtil.getLastSecondOfMonth(
							DateKit.str2Date(queryEndMonth, ReportConsoleConfig.MONTHLY_REPORT_DATE_FORMAT)));
			if (sumResultBean == null) {
				throw new Exception("数据缺失无法导出!");
			}
			// 修正结果正负性
			sumResultBean.setSumQuantity(Math.abs(sumResultBean.getSumQuantity()));
			rowContentList.add(sumResultBean.getSumQuantity());
			rowContentList.add(endPlannedPriceBean.getPrice());
			rowContentList
					.add(NumeralOperationKit.multiply(sumResultBean.getSumQuantity(), endPlannedPriceBean.getPrice()));
			subtractQutity = NumeralOperationKit.add(subtractQutity, sumResultBean.getSumQuantity());
			// 期末数量，单价，总额
			rowContentList.add(endInventoryBean.getQuantity());
			rowContentList.add(endPlannedPriceBean.getPrice());
			rowContentList
					.add(NumeralOperationKit.multiply(endInventoryBean.getQuantity(), endPlannedPriceBean.getPrice()));
			endQutity = NumeralOperationKit.add(endQutity, endInventoryBean.getQuantity());
			// 行备注
			rowContentList.add("");
			// 插一行数据
			contentList.add(rowContentList);
		}
		// 插入总数行
		rowContentList = new ArrayList<Object>();
		ReportUtil.putEmptyCell(rowContentList, 2);
		rowContentList.add("期初总数:");
		rowContentList.add(openQutity);
		ReportUtil.putEmptyCell(rowContentList, 1);
		rowContentList.add("本期入库:");
		rowContentList.add(addQutity);
		ReportUtil.putEmptyCell(rowContentList, 1);
		rowContentList.add("本期出库:");
		rowContentList.add(subtractQutity);
		ReportUtil.putEmptyCell(rowContentList, 1);
		rowContentList.add("期末总数:");
		rowContentList.add(endQutity);
		ReportUtil.putEmptyCell(rowContentList, 3);
		contentList.add(rowContentList);
		// 表尾信息
		rowContentList = new ArrayList<Object>();
		rowContentList.add("制表人:");
		rowContentList.add(createUserName);
		rowContentList.add("日期:");
		rowContentList.add(DateKit.formatTimestamp(DateKit.getCurrentTimestamp(), DateKit.DEFAULT_DATE_FORMAT));
		rowContentList.add("部门负责人");
		ReportUtil.putEmptyCell(rowContentList, 1);
		rowContentList.add("日期:");
		ReportUtil.putEmptyCell(rowContentList, 9);
		contentList.add(rowContentList);

		excelWriteBean.setContentList(contentList);
		return excelWriteBean;
	}
	
	public List<List<Object>> getWarehouseInfoExcelList(List<WarehouseInfoBean> warehouseInfoList,int rowSize){
		List<List<Object>> result = new ArrayList<List<Object>>();
		List<Object> row = new ArrayList<Object>();
		row.add("关联仓库信息:");
		ReportUtil.putEmptyCell(row, rowSize-row.size());
		result.add(row);
		for(WarehouseInfoBean bean : warehouseInfoList){
			row = new ArrayList<Object>();
			row.add(bean.getWarehouseName());
			row.add(bean.getCompleteAddress());
			row.add(StringKit.isValid(bean.getTelephone())? bean.getTelephone():"没有提供");
			ReportUtil.putEmptyCell(row, rowSize-row.size());
			result.add(row);
		}
		return result;
	}
}
