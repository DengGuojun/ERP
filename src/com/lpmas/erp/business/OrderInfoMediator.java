package com.lpmas.erp.business;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import com.lpmas.erp.bean.OrderInfoBean;
import com.lpmas.erp.client.cache.OrderInfoClientCache;
import com.lpmas.framework.util.MapKit;

public class OrderInfoMediator {
	private OrderInfoClientCache orderInfoClientCache;
	private Map<String, String> orderNumberMap;
	private static String MAP_KEY_PREFIX = "ORDER_INFO";

	public OrderInfoMediator() {
		orderInfoClientCache = new OrderInfoClientCache();
		orderNumberMap = new HashMap<String, String>();
	}

	public String getMapKey(int orderType, int orderId) {
		return MessageFormat.format("{0}_{1}_{2}", MAP_KEY_PREFIX, orderType, orderId);
	}

	public String getMapKey(int orderType, String orderNumber) {
		return MessageFormat.format("{0}_{1}_{2}", MAP_KEY_PREFIX, orderType, orderNumber);
	}

	public OrderInfoClientCache getOrderInfoClientCache() {
		return orderInfoClientCache;
	}

	public Map<String, String> getOrderNumberMap() {
		return orderNumberMap;
	}

	public String getWareNumberByKey(int orderType, int orderId) {
		return getValueFromMapByKey(orderNumberMap, orderType, orderId);
	}

	public String getWareNumberByNumber(int orderType, String orderNumber) {
		return getValueFromMapByNumber(orderNumberMap, orderType, orderNumber);
	}

	private void setMapVaule(String key, OrderInfoBean orderInfoBean) {
		orderNumberMap.put(key, orderInfoBean.getOrderNumber());
	}

	private String getValueFromMapByKey(Map<String, String> map, int orderType, int orderId) {
		String key = getMapKey(orderType, orderId);
		String result = map.get(key);
		if (result == null) {
			OrderInfoBean orderInfoBean = orderInfoClientCache.getOrderInfoByKey(orderType, orderId);
			if (orderInfoBean != null) {
				setMapVaule(key, orderInfoBean);
			}
		}
		return MapKit.getValueFromMap(key, map);
	}

	private String getValueFromMapByNumber(Map<String, String> map, int orderType, String orderNumber) {
		String key = getMapKey(orderType, orderNumber);
		String result = map.get(key);
		if (result == null) {
			OrderInfoBean orderInfoBean = orderInfoClientCache.getOrderInfoByKey(orderType, orderNumber);
			if (orderInfoBean != null) {
				setMapVaule(key, orderInfoBean);
			}
		}
		return MapKit.getValueFromMap(key, map);
	}
}
