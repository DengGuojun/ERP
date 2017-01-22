package com.lpmas.erp.bom.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.bom.bean.BomItemBean;
import com.lpmas.erp.bom.config.BomLogConfig;
import com.lpmas.erp.bom.dao.BomItemDao;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;

public class BomItemBusiness {
	public int addBomItem(BomItemBean bean) {
		BomItemDao dao = new BomItemDao();
		int result = dao.insertBomItem(bean);
		if (result > 0) {
			bean.setBomItemId(result);
			ErpLogSender helper = new ErpLogSender();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_BOM, bean.getBomId(), bean.getBomItemId(), 0, BomLogConfig.LOG_BOM_ITEM);
		}
		return result;
	}

	public int updateBomItem(BomItemBean bean) {
		BomItemDao dao = new BomItemDao();
		BomItemBean orginalBean = getBomItemByKey(bean.getBomItemId());
		int result = dao.updateBomItem(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(orginalBean, bean, InfoTypeConfig.INFO_TYPE_BOM, bean.getBomId(), bean.getBomItemId(), 0, BomLogConfig.LOG_BOM_ITEM);
		}
		return result;
	}

	public BomItemBean getBomItemByKey(int bomItemId) {
		BomItemDao dao = new BomItemDao();
		return dao.getBomItemByKey(bomItemId);
	}

	public PageResultBean<BomItemBean> getBomItemPageListByMap(HashMap<String, String> condMap, PageBean pageBean) {
		BomItemDao dao = new BomItemDao();
		return dao.getBomItemPageListByMap(condMap, pageBean);
	}

	public List<BomItemBean> getBomItemListByMap(HashMap<String, String> condMap) {
		BomItemDao dao = new BomItemDao();
		return dao.getBomItemListByMap(condMap);
	}

	public List<BomItemBean> getBomItemListByBomId(int bomId) {
		BomItemDao dao = new BomItemDao();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("bomId", String.valueOf(bomId));
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		return dao.getBomItemListByMap(condMap);
	}

	public List<BomItemBean> getBomItemAllListByBomId(int bomId) {
		BomItemDao dao = new BomItemDao();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("bomId", String.valueOf(bomId));
		return dao.getBomItemListByMap(condMap);
	}

	public Boolean isVaildArray(String[] array) {
		Boolean result = false;
		for (String s : array) {
			if (!s.trim().equals(""))
				result = true;
		}
		return result;
	}

	private String getWareIdAndTypeKey(int wareId, int wareType) {
		return String.valueOf(wareId) + "_" + String.valueOf(wareType);
	}

	public int processBomItem(String[] selectedItemValue, int bomId, int userId) {
		int result = 0;
		// 插入到采购明细表
		Map<String, BomItemBean> existBomItemMap = new HashMap<String, BomItemBean>();
		// 获取这个BOM所包括的ITEM,并转化成Map
		List<BomItemBean> bomItemList = getBomItemAllListByBomId(bomId);
		List<BomItemBean> newBomItemList = new ArrayList<BomItemBean>();
		for (BomItemBean bomItemBean : bomItemList) {
			existBomItemMap.put(getWareIdAndTypeKey(bomItemBean.getWareId(), bomItemBean.getWareType()), bomItemBean);
		}
		// 获取新增的ITEM的value
		if (selectedItemValue != null) {
			for (String itemValue : selectedItemValue) {
				if (StringKit.isValid(itemValue.trim())) {
					BomItemBean itemBean = new BomItemBean();
					Map<String, String> itemValueMap = MapKit.string2Map(itemValue.trim(), ",", "=");
					int wareId = Integer.valueOf(itemValueMap.get("wareId"));
					int usageType = Integer.valueOf(itemValueMap.get("usageType"));
					int wareType = Integer.valueOf(itemValueMap.get("wareType"));
					if (existBomItemMap.containsKey(getWareIdAndTypeKey(wareId, wareType))) {
						itemBean = existBomItemMap.get(getWareIdAndTypeKey(wareId, wareType));
						itemBean.setModifyUser(userId);
						existBomItemMap.remove(getWareIdAndTypeKey(wareId, wareType));
					} else {
						if (StringKit.isValid(itemValueMap.get("itmeId"))) {
							itemBean.setModifyUser(userId);
						} else {
							itemBean.setCreateUser(userId);
						}
					}
					itemBean.setUnit(itemValueMap.get("unit"));
					itemBean.setQuantity(Double.valueOf(itemValueMap.get("quatity")));
					itemBean.setWareType(wareType);
					itemBean.setWareId(wareId);
					itemBean.setUsageType(usageType);
					itemBean.setBomId(bomId);
					itemBean.setStatus(Constants.STATUS_VALID);
					newBomItemList.add(itemBean);
				}
			}
		}
		for (BomItemBean bomItemBean : newBomItemList) {
			if (bomItemBean.getModifyUser() == userId) {
				result = updateBomItem(bomItemBean);
			} else {
				result = addBomItem(bomItemBean);
			}
		}
		// 逻辑删除
		if (!existBomItemMap.isEmpty()) {
			for (Entry<String, BomItemBean> entry : existBomItemMap.entrySet()) {
				BomItemBean temp = entry.getValue();
				temp.setStatus(Constants.STATUS_NOT_VALID);
				temp.setModifyUser(userId);
				updateBomItem(temp);
			}
		}
		return result;
	}
}