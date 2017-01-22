package com.lpmas.erp.warehouse.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lpmas.erp.warehouse.bean.WarehousePropertyBean;
import com.lpmas.erp.warehouse.dao.WarehousePropertyDao;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;

public class WarehousePropertyBusiness {
	public int addWarehouseProperty(WarehousePropertyBean bean) {
		WarehousePropertyDao dao = new WarehousePropertyDao();
		return dao.insertWarehouseProperty(bean);
	}

	public int updateWarehouseProperty(WarehousePropertyBean bean) {
		WarehousePropertyDao dao = new WarehousePropertyDao();
		return dao.updateWarehouseProperty(bean);
	}

	public WarehousePropertyBean getWarehousePropertyByKey(int warehouseId, String propertyCode) {
		WarehousePropertyDao dao = new WarehousePropertyDao();
		return dao.getWarehousePropertyByKey(warehouseId, propertyCode);
	}

	public PageResultBean<WarehousePropertyBean> getWarehousePropertyPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		WarehousePropertyDao dao = new WarehousePropertyDao();
		return dao.getWarehousePropertyPageListByMap(condMap, pageBean);
	}
	
	public List<Integer> getWarehouseIdListByMap(Map<String, String> condMap){
		WarehousePropertyDao dao = new WarehousePropertyDao();
		return dao.getWarehouseIdListByMap(condMap);
	}

}
