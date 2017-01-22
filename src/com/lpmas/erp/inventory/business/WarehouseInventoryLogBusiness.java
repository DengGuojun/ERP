package com.lpmas.erp.inventory.business;

import java.util.HashMap;
import java.util.List;

import com.lpmas.erp.inventory.bean.WarehouseInventoryLogBean;
import com.lpmas.erp.inventory.dao.WarehouseInventoryLogDao;
import com.lpmas.erp.report.bean.WarehouseInventoryLogSumResultBean;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;

public class WarehouseInventoryLogBusiness {

	public WarehouseInventoryLogBean getWarehouseInventoryLogByKey(int logId) {
		WarehouseInventoryLogDao dao = new WarehouseInventoryLogDao();
		return dao.getWarehouseInventoryLogByKey(logId);
	}

	public PageResultBean<WarehouseInventoryLogBean> getWarehouseInventoryLogPageListByMap(
			HashMap<String, String> condMap, PageBean pageBean) {
		WarehouseInventoryLogDao dao = new WarehouseInventoryLogDao();
		return dao.getWarehouseInventoryLogPageListByMap(condMap, pageBean);
	}

	public List<WarehouseInventoryLogBean> getWarehouseInventoryLogListByMap(HashMap<String, String> condMap) {
		WarehouseInventoryLogDao dao = new WarehouseInventoryLogDao();
		return dao.getWarehouseInventoryLogListByMap(condMap);
	}

	public List<WarehouseInventoryLogSumResultBean> getWarehouseInventoryLogSumResultListByCondition(int wiId,
			String endDayTime) {
		WarehouseInventoryLogDao dao = new WarehouseInventoryLogDao();
		return dao.getWarehouseInventoryLogSumResultListByCondition(wiId, endDayTime);
	}

	public List<WarehouseInventoryLogSumResultBean> getWarehouseInventoryLogSumResultListByCondition(int wiId,
			String openDayTime, String endDayTime) {
		WarehouseInventoryLogDao dao = new WarehouseInventoryLogDao();
		return dao.getWarehouseInventoryLogSumResultListByCondition(wiId, openDayTime, endDayTime);
	}

	public WarehouseInventoryLogSumResultBean getWarehouseInventoryLogSumResultBeanByCondition(List<Integer> wiIdList,
			List<Integer> changeTypeList, String startTime, String endTime) {
		WarehouseInventoryLogDao dao = new WarehouseInventoryLogDao();
		return dao.getWarehouseInventoryLogSumResultBeanByCondition(wiIdList, changeTypeList, startTime, endTime);
	}

	public WarehouseInventoryLogSumResultBean getWarehouseInventoryLogSumResultBeanByCondition(List<Integer> wiIdList,
			List<Integer> changeTypeList, String lastDayTime) {
		WarehouseInventoryLogDao dao = new WarehouseInventoryLogDao();
		return dao.getWarehouseInventoryLogSumResultBeanByCondition(wiIdList, changeTypeList, lastDayTime);
	}

	public List<WarehouseInventoryLogBean> getWarehouseInventoryLogListByCondition(List<Integer> wiIdList,
			String startTime, String endTime) {
		WarehouseInventoryLogDao dao = new WarehouseInventoryLogDao();
		return dao.getWarehouseInventoryLogListByCondition(wiIdList, startTime, endTime);
	}
}