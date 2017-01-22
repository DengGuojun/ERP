package com.lpmas.erp.client.cache;

import java.util.ArrayList;
import java.util.List;

import com.lpmas.erp.client.ErpServiceClient;
import com.lpmas.erp.config.ErpClientCacheConfig;
import com.lpmas.erp.warehouse.bean.WarehouseInfoBean;
import com.lpmas.framework.cache.LocalCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.opensymphony.oscache.base.NeedsRefreshException;

public class WarehouseInfoCache {

	public WarehouseInfoBean getWarehouseInfoByKey(int warehouseId) {
		WarehouseInfoBean result = null;
		String key = ErpClientCacheConfig.getwarehouseInfoCacheKeyByKey(warehouseId);
		LocalCache localCache = LocalCache.getInstance();
		Object obj = null;
		try {
			obj = localCache.get(key);
			result = JsonKit.toBean((String) obj, WarehouseInfoBean.class);
		} catch (NeedsRefreshException e) {
			boolean updated = false;
			ErpServiceClient client = new ErpServiceClient();
			result = client.getWarehouseInfoByKey(warehouseId);
			if (result != null) {
				localCache.set(key, JsonKit.toJson(result), Constants.CACHE_TIME_2_HOUR);
				updated = true;
			}
			if (!updated) {
				localCache.cancelUpdate(key);
			}
		}
		return result;
	}

	public WarehouseInfoBean getWarehouseInfoByNumber(String warehouseNumber) {
		WarehouseInfoBean result = null;
		String key = ErpClientCacheConfig.getwarehouseInfoCacheKeyByNumber(warehouseNumber);
		LocalCache localCache = LocalCache.getInstance();
		Object obj = null;
		try {
			obj = localCache.get(key);
			result = JsonKit.toBean((String) obj, WarehouseInfoBean.class);
		} catch (NeedsRefreshException e) {
			boolean updated = false;
			ErpServiceClient client = new ErpServiceClient();
			result = client.getWarehouseInfoByNumber(warehouseNumber);
			if (result != null) {
				localCache.set(key, JsonKit.toJson(result), Constants.CACHE_TIME_2_HOUR);
				updated = true;
			}
			if (!updated) {
				localCache.cancelUpdate(key);
			}
		}
		return result;
	}

	public String getWarehouseNameByKey(int warehouseId) {
		String result = "";
		WarehouseInfoBean warehouseInfoBean = getWarehouseInfoByKey(warehouseId);
		if (warehouseInfoBean != null) {
			result = warehouseInfoBean.getWarehouseName();
		}
		return result;
	}

	public String getWarehouseNameByNumber(String warehouseNumber) {
		String result = "";
		WarehouseInfoBean warehouseInfoBean = getWarehouseInfoByNumber(warehouseNumber);
		if (warehouseInfoBean != null) {
			result = warehouseInfoBean.getWarehouseName();
		}
		return result;
	}

	public List<WarehouseInfoBean> getWarehouseListByTypeAndIsDeleveryToCumstomer(String warehouseType, boolean isDeliveryToCustomer) {
		String key = ErpClientCacheConfig.getWarehouseListCacheKeyByTypeAndIsDeleveryToCumstomer(warehouseType, isDeliveryToCustomer);
		LocalCache localCache = LocalCache.getInstance();
		Object object = null;
		List<WarehouseInfoBean> result = null;
		try {
			object = localCache.get(key);
			result = JsonKit.toList((String) object, WarehouseInfoBean.class);
		} catch (NeedsRefreshException e) {
			boolean isUpdated = false;
			ErpServiceClient client = new ErpServiceClient();
			result = client.getWarehouseListByTypeAndIsDeleveryToCumstomer(warehouseType, isDeliveryToCustomer);
			if (result != null && !result.isEmpty()) {
				localCache.set(key, JsonKit.toJson(result), Constants.CACHE_TIME_2_HOUR);
				isUpdated = true;
			}
			if (!isUpdated) {
				result = new ArrayList<WarehouseInfoBean>(1);
				localCache.cancelUpdate(key);
			}
		}

		return result;
	}

}
