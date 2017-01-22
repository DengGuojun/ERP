package com.lpmas.erp.client.cache;

import com.lpmas.erp.bean.OrderInfoBean;
import com.lpmas.erp.client.ErpServiceClient;
import com.lpmas.erp.config.ErpCacheConfig;
import com.lpmas.framework.cache.LocalCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;
import com.opensymphony.oscache.base.NeedsRefreshException;

public class OrderInfoClientCache {
	public OrderInfoBean getOrderInfoByKey(int orderType, int orderId) {
		OrderInfoBean result = null;
		String key = ErpCacheConfig.getOrderInfoCacheKey(orderType, orderId);
		LocalCache localCache = LocalCache.getInstance();
		Object object = null;
		try {
			object = localCache.get(key);
			result = JsonKit.toBean((String) object, OrderInfoBean.class);
		} catch (NeedsRefreshException nre) {
			boolean updated = false;
			ErpServiceClient client = new ErpServiceClient();
			result = client.getOrderInfoByKey(orderType, orderId);
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

	public OrderInfoBean getOrderInfoByKey(int orderType, String orderNumber) {
		OrderInfoBean result = null;
		String key = ErpCacheConfig.getOrderInfoCacheKey(orderType, orderNumber);
		LocalCache localCache = LocalCache.getInstance();
		Object object = null;
		try {
			object = localCache.get(key);
			result = JsonKit.toBean((String) object, OrderInfoBean.class);
		} catch (NeedsRefreshException e) {
			boolean updated = false;
			ErpServiceClient client = new ErpServiceClient();
			result = client.getOrderInfoByKey(orderType, orderNumber);
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
}
