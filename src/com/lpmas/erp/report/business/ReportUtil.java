package com.lpmas.erp.report.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.util.DateKit;
import com.lpmas.framework.util.ReflectKit;
import com.lpmas.pdm.bean.MaterialInfoBean;
import com.lpmas.pdm.bean.ProductItemBean;
import com.lpmas.pdm.client.PdmServiceClient;

public class ReportUtil {

	public static void putEmptyCell(List<Object> rowContentList, int count) {
		for (int i = 0; i < count; i++) {
			rowContentList.add("");
		}
	}

	public static String getLastSecondOfDay(Date day) {
		Date nextDay = DateKit.addTime(DateKit.REGEX_DATE, 1, day);
		String nextDayStr = DateKit.formatDate(nextDay, DateKit.DEFAULT_DATE_FORMAT);
		long time = DateKit.str2Timestamp(nextDayStr, DateKit.DEFAULT_DATE_FORMAT).getTime();
		return DateKit.formatDate(new Date(time - 1000), DateKit.DEFAULT_DATE_TIME_FORMAT);
	}

	public static String getLastSecondOfMonth(Date day) {
		Date nextMonth = DateKit.addTime(DateKit.REGEX_MONTH, 1, day);
		String nextMonthStr = DateKit.formatDate(nextMonth, DateKit.DEFAULT_DATE_FORMAT);
		long time = DateKit.str2Timestamp(nextMonthStr, DateKit.DEFAULT_DATE_FORMAT).getTime();
		return DateKit.formatDate(new Date(time - 1000), DateKit.DEFAULT_DATE_TIME_FORMAT);
	}

	@SuppressWarnings("unchecked")
	public static <E, T> List<E> subList(List<T> list, String fieldName) {
		List<E> result = new ArrayList<E>();
		
		if (null == list || list.size() <= 0) {
			return result;
		}

		for (T bean : list) {
			E e = (E) ReflectKit.getPropertyValue(bean, fieldName);
			result.add(e);
		}
		return result;
	}
	
	public static String getItemUnit(PdmServiceClient client, int wareType, int wareId) {
		if (wareType == InfoTypeConfig.INFO_TYPE_MATERIAL) {
			MaterialInfoBean materialInfoBean = client.getMaterialInfoByKey(wareId);
			if (materialInfoBean != null) {
				return materialInfoBean.getUnit();
			}
		} else if (wareType == InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM) {
			ProductItemBean productItemBean = client.getProductItemByKey(wareId);
			if (productItemBean != null) {
				return productItemBean.getUnit();
			}
		}
		return "";
	}

}
