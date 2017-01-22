package com.lpmas.erp.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.erp.bean.OrderInfoBean;
import com.lpmas.erp.business.OrderInfoBusniess;
import com.lpmas.erp.config.ErpCacheConfig;
import com.lpmas.erp.config.ErpConsoleConfig;
import com.lpmas.framework.cache.RemoteCache;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.JsonKit;

public class OrderInfoCache {
	private static Logger log = LoggerFactory.getLogger(OrderInfoCache.class);

	public OrderInfoBean getOrderInfoByKey(int orderType, int orderId) {
		OrderInfoBean bean = null;
		String key = ErpCacheConfig.getOrderInfoCacheKey(orderType, orderId);

		RemoteCache remoteCache = RemoteCache.getInstance();
		Object obj = remoteCache.get(ErpConsoleConfig.APP_ID, key);
		if (obj != null) {
			log.debug("get OrderInfoBean from remote cache");
			bean = JsonKit.toBean((String) obj, OrderInfoBean.class);
		} else {
			log.debug("set OrderInfoBean to remote cache");
			OrderInfoBusniess orderInfoBusniess = new OrderInfoBusniess();
			bean = orderInfoBusniess.getOrderInfoByKey(orderType, orderId);
			if (bean != null) {
				remoteCache.set(ErpConsoleConfig.APP_ID, key, JsonKit.toJson(bean), Constants.CACHE_TIME_2_HOUR);
			}
		}
		return bean;
	}

	public OrderInfoBean getOrderInfoByKey(int orderType, String orderNumber) {
		OrderInfoBean bean = null;
		String key = ErpCacheConfig.getOrderInfoCacheKey(orderType, orderNumber);

		RemoteCache remoteCache = RemoteCache.getInstance();
		Object obj = remoteCache.get(ErpConsoleConfig.APP_ID, key);
		if (obj != null) {
			log.debug("get OrderInfoBean from remote cache");
			bean = JsonKit.toBean((String) obj, OrderInfoBean.class);
		} else {
			log.debug("set OrderInfoBean to remote cache");
			OrderInfoBusniess orderInfoBusniess = new OrderInfoBusniess();
			bean = orderInfoBusniess.getOrderInfoByKey(orderType, orderNumber);
			if (bean != null) {
				remoteCache.set(ErpConsoleConfig.APP_ID, key, JsonKit.toJson(bean), Constants.CACHE_TIME_2_HOUR);
			}
		}
		return bean;
	}

	// 刷新缓存
	public Boolean refresWareInfoCacheByKey(int orderType, int orderId) {
		String key = ErpCacheConfig.getOrderInfoCacheKey(orderType, orderId);
		RemoteCache remoteCache = RemoteCache.getInstance();
		return remoteCache.delete(ErpConsoleConfig.APP_ID, key);
	}

	// 刷新缓存
	public Boolean refresWareInfoCacheByKey(int orderType, String orderNumber) {
		String key = ErpCacheConfig.getOrderInfoCacheKey(orderType, orderNumber);
		RemoteCache remoteCache = RemoteCache.getInstance();
		return remoteCache.delete(ErpConsoleConfig.APP_ID, key);
	}
}
