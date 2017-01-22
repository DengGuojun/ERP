package com.lpmas.erp.report.business;

import java.util.ArrayList;
import java.util.List;

import com.lpmas.erp.inventory.bean.DeliveryVoucherInfoBean;
import com.lpmas.erp.inventory.bean.WarePlannedPriceBean;
import com.lpmas.erp.inventory.bean.WarehouseInventoryBean;
import com.lpmas.erp.inventory.bean.WarehouseInventoryLogBean;
import com.lpmas.erp.inventory.bean.WarehouseVoucherInfoBean;
import com.lpmas.erp.inventory.business.DeliveryVoucherInfoBusiness;
import com.lpmas.erp.inventory.business.WarePlannedPriceBusiness;
import com.lpmas.erp.inventory.business.WarehouseInventoryLogBusiness;
import com.lpmas.erp.inventory.business.WarehouseVoucherInfoBusiness;
import com.lpmas.erp.inventory.config.WarehouseInventoryConfig;
import com.lpmas.erp.report.config.ReportConsoleConfig;
import com.lpmas.framework.excel.ExcelConfig;
import com.lpmas.framework.excel.ExcelWriteBean;
import com.lpmas.framework.util.DateKit;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.NumeralOperationKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.pdm.bean.UnitInfoBean;
import com.lpmas.pdm.client.PdmServiceClient;

public class WarehouseInAndOutDetailReportBusiness {
	public ExcelWriteBean createWarehouseInAndOutReport(int wareType, int wareId, String openDateTime, String endDateTime, String itemName,
			String itemUnit, List<WarehouseInventoryBean> inventoryList, String createUserName, boolean isContainHeader) throws Exception {
		// 日期转换
		String queryOpenDateTime = DateKit.formatStr(openDateTime, DateKit.DEFAULT_DATE_FORMAT, DateKit.DEFAULT_DATE_TIME_FORMAT);
		String queryEndDateTime = ReportUtil.getLastSecondOfDay(DateKit.str2Date(endDateTime, DateKit.DEFAULT_DATE_FORMAT));
		// 对象准备
		PdmServiceClient pdmServiceClient = new PdmServiceClient();
		UnitInfoBean unitInfoBean = null;
		String itemUnitName = "";
		String memo = "";
		String sourceNumber = "";
		ExcelWriteBean excelWriteBean = new ExcelWriteBean();
		WarehouseInventoryLogBusiness warehouseInventoryLogBusiness = new WarehouseInventoryLogBusiness();
		WarePlannedPriceBusiness warePlannedPriceBusiness = new WarePlannedPriceBusiness();
		WarehouseVoucherInfoBusiness warehouseVoucherInfoBusiness = new WarehouseVoucherInfoBusiness();
		DeliveryVoucherInfoBusiness deliveryVoucherInfoBusiness = new DeliveryVoucherInfoBusiness();
		WarehouseVoucherInfoBean warehouseVoucherInfoBean = null;
		DeliveryVoucherInfoBean deliveryVoucherInfoBean = null;
		WarePlannedPriceBean warePlannedPriceBean = null;
		double plannedPrice = 0;
		double addTotal = 0;
		double subtractTotal = 0;

		List<Integer> wiIdList = new ArrayList<Integer>(inventoryList.size());
		for (WarehouseInventoryBean inventoryBean : inventoryList) {
			wiIdList.add(inventoryBean.getWiId());
		}
		// 查出这些库存记录的变化情况
		List<Integer> addChangeTypeList = new ArrayList<Integer>(2);
		addChangeTypeList.add(WarehouseInventoryConfig.WIO_TYPE_PUT_ON);
		addChangeTypeList.add(WarehouseInventoryConfig.WIO_TYPE_UPDATE);

		List<Integer> subtractChangeTypeList = new ArrayList<Integer>(1);
		subtractChangeTypeList.add(WarehouseInventoryConfig.WIO_TYPE_STOCK_OUT);
		List<WarehouseInventoryLogBean> logList = warehouseInventoryLogBusiness.getWarehouseInventoryLogListByCondition(wiIdList, queryOpenDateTime,
				queryEndDateTime);

		// 开始写EXCEL
		excelWriteBean.setFileType(ExcelConfig.FT_XLSX);
		excelWriteBean.setFileName(openDateTime + "~" + endDateTime + itemName + ReportConsoleConfig.IN_AND_OUT_DETAIL_REPORT_NAME_PATTERN
				+ DateKit.getCurrentDateTime("yyyyMMdd"));
		excelWriteBean.setSheetName(itemName);
		List<List<Object>> contentList = new ArrayList<List<Object>>();
		List<Object> rowContentList = null;
		if (isContainHeader) {
			// 头部
			// 处理制品名，制表人
			List<String> headerList = new ArrayList<String>();
			headerList.add("品名:");
			headerList.add(itemName);
			for (int i = 0; i < 11; i++) {
				headerList.add("");
			}
			headerList.add("制表人:");
			headerList.add(createUserName);
			excelWriteBean.setHeaderList(headerList);

			// 补充头部
			rowContentList = new ArrayList<Object>();
			ReportUtil.putEmptyCell(rowContentList, 3);
			rowContentList.add("本月入库");
			ReportUtil.putEmptyCell(rowContentList, 3);
			rowContentList.add("本月出库");
			ReportUtil.putEmptyCell(rowContentList, 3);
			rowContentList.add("期末库存");
			ReportUtil.putEmptyCell(rowContentList, 3);
			contentList.add(rowContentList);
			// 栏位名称
			rowContentList = new ArrayList<Object>();
			rowContentList.add("日期");
			rowContentList.add("摘要");
			rowContentList.add("单位");
			rowContentList.add("入库单号");
			rowContentList.add("数量");
			rowContentList.add("单价");
			rowContentList.add("金额");
			rowContentList.add("出库单号");
			rowContentList.add("数量");
			rowContentList.add("单价");
			rowContentList.add("金额");
			rowContentList.add("数量");
			rowContentList.add("单价");
			rowContentList.add("金额");
			rowContentList.add("备注");
			contentList.add(rowContentList);
		}
		// 插入期初的库存
		double addInventory = 0;
		double subtractInventory = 0;
		addInventory = warehouseInventoryLogBusiness.getWarehouseInventoryLogSumResultBeanByCondition(wiIdList, addChangeTypeList, queryOpenDateTime)
				.getSumQuantity();
		subtractInventory = warehouseInventoryLogBusiness
				.getWarehouseInventoryLogSumResultBeanByCondition(wiIdList, subtractChangeTypeList, queryOpenDateTime).getSumQuantity();
		double beginInventory = NumeralOperationKit.subtract(addInventory, subtractInventory);
		rowContentList = new ArrayList<Object>();
		rowContentList.add(openDateTime);
		ReportUtil.putEmptyCell(rowContentList, 10);
		rowContentList.add(beginInventory);
		ReportUtil.putEmptyCell(rowContentList, 3);
		contentList.add(rowContentList);

		// 每行数据
		for (WarehouseInventoryLogBean logBean : logList) {
			// 修正结果正负性
			logBean.setQuantity(Math.abs(logBean.getQuantity()));

			rowContentList = new ArrayList<Object>();
			// 日期摘单位
			rowContentList.add(DateKit.formatTimestamp(logBean.getCreateTime(), DateKit.DEFAULT_DATE_FORMAT));
			rowContentList.add("");
			// 处理单位
			itemUnit = ReportUtil.getItemUnit(pdmServiceClient, wareType, wareId);
			unitInfoBean = pdmServiceClient.getUnitInfoByCode(itemUnit);
			if (unitInfoBean != null && unitInfoBean.getUnitCode().equals(itemUnit))
				itemUnitName = unitInfoBean.getUnitName();
			else
				itemUnitName = "单位名称缺失";
			rowContentList.add(itemUnitName);

			// 获取计划价格
			warePlannedPriceBean = warePlannedPriceBusiness.getWarePlannedPriceByKey(wareType, wareId,
					DateKit.formatTimestamp(logBean.getCreateTime(), ReportConsoleConfig.MONTHLY_REPORT_DATE_FORMAT), itemUnit);
			if (warePlannedPriceBean != null)
				plannedPrice = warePlannedPriceBean.getPrice();
			else
				plannedPrice = 0;

			// 处理单号和备注
			sourceNumber = "";
			if (logBean.getChangeType() == WarehouseInventoryConfig.WIO_TYPE_UPDATE
					|| logBean.getChangeType() == WarehouseInventoryConfig.WIO_TYPE_PUT_ON) {
				// 查找入库单
				warehouseVoucherInfoBean = warehouseVoucherInfoBusiness.getWarehouseVoucherInfoByKey(logBean.getSourceId());
				if (warehouseVoucherInfoBean != null) {
					sourceNumber = warehouseVoucherInfoBean.getWvNumber();
					if (StringKit.isValid(warehouseVoucherInfoBean.getMemo()))
						memo = warehouseVoucherInfoBean.getMemo();
					else
						memo = MapKit.getValueFromMap(logBean.getChangeType(), WarehouseInventoryConfig.WAREHOUSE_INVENTORY_OPERATION_TYPE_MAP);
				} else
					sourceNumber = "入库单号不存在";
			} else if (logBean.getChangeType() == WarehouseInventoryConfig.WIO_TYPE_STOCK_OUT) {
				// 查找出库单
				deliveryVoucherInfoBean = deliveryVoucherInfoBusiness.getDeliveryVoucherInfoByKey(logBean.getSourceId());
				if (deliveryVoucherInfoBean != null) {
					sourceNumber = deliveryVoucherInfoBean.getDvNumber();
					if (StringKit.isValid(deliveryVoucherInfoBean.getMemo()))
						memo = deliveryVoucherInfoBean.getMemo();
					else
						memo = MapKit.getValueFromMap(logBean.getChangeType(), WarehouseInventoryConfig.WAREHOUSE_INVENTORY_OPERATION_TYPE_MAP);
				} else
					sourceNumber = "出库单号不存在";
			} else {
				memo = MapKit.getValueFromMap(logBean.getChangeType(), WarehouseInventoryConfig.WAREHOUSE_INVENTORY_OPERATION_TYPE_MAP);
			}

			// 处理入库或者出库信息
			boolean isAddInventory = true;
			if (subtractChangeTypeList.contains(logBean.getChangeType())) {
				subtractTotal = NumeralOperationKit.add(subtractTotal, logBean.getQuantity());
				ReportUtil.putEmptyCell(rowContentList, 4);
				isAddInventory = false;
			}
			rowContentList.add(sourceNumber);
			rowContentList.add(logBean.getQuantity());
			rowContentList.add(plannedPrice);
			rowContentList.add(NumeralOperationKit.multiply(plannedPrice, logBean.getQuantity()));
			if (addChangeTypeList.contains(logBean.getChangeType())) {
				ReportUtil.putEmptyCell(rowContentList, 4);
				addTotal = NumeralOperationKit.add(addTotal, logBean.getQuantity());
			}
			if (!addChangeTypeList.contains(logBean.getChangeType()) && !subtractChangeTypeList.contains(logBean.getChangeType())) {
				continue;
			}

			// 处理期末库存
			if (isAddInventory) {
				beginInventory = NumeralOperationKit.add(beginInventory, logBean.getQuantity());
			} else {
				beginInventory = NumeralOperationKit.subtract(beginInventory, logBean.getQuantity());
			}
			rowContentList.add(beginInventory);
			rowContentList.add(plannedPrice);
			rowContentList.add(NumeralOperationKit.multiply(plannedPrice, beginInventory));
			// 处理备注
			rowContentList.add(memo);
			// 插入一行
			contentList.add(rowContentList);
		}
		// 处理TOTAL
		rowContentList = new ArrayList<Object>();
		rowContentList.add("TOTAL:");
		ReportUtil.putEmptyCell(rowContentList, 3);
		rowContentList.add(addTotal);
		ReportUtil.putEmptyCell(rowContentList, 3);
		rowContentList.add(subtractTotal);
		contentList.add(rowContentList);
		ReportUtil.putEmptyCell(rowContentList, 6);

		excelWriteBean.setContentList(contentList);
		return excelWriteBean;
	}
}
