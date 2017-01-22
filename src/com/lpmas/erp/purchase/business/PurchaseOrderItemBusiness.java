package com.lpmas.erp.purchase.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.purchase.bean.PurchaseOrderItemBean;
import com.lpmas.erp.purchase.config.PurchaseOrderLogConfig;
import com.lpmas.erp.purchase.dao.PurchaseOrderItemDao;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;

public class PurchaseOrderItemBusiness {
	public int addPurchaseOrderItem(PurchaseOrderItemBean bean) {
		PurchaseOrderItemDao dao = new PurchaseOrderItemDao();
		int result = dao.insertPurchaseOrderItem(bean);
		if (result > 0) {
			bean.setItemId(result);
			ErpLogSender helper = new ErpLogSender();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER, bean.getPoId(), bean.getItemId(), 0,
					PurchaseOrderLogConfig.LOG_PURCHASE_ORDER_ITEM);
		}
		return result;
	}

	public int updatePurchaseOrderItem(PurchaseOrderItemBean bean) {
		PurchaseOrderItemDao dao = new PurchaseOrderItemDao();
		PurchaseOrderItemBean orginalBean = getPurchaseOrderItemByKey(bean.getItemId());
		int result = dao.updatePurchaseOrderItem(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(orginalBean, bean, InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER, bean.getPoId(),
					bean.getItemId(), 0, PurchaseOrderLogConfig.LOG_PURCHASE_ORDER_ITEM);
		}
		return result;
	}

	public PurchaseOrderItemBean getPurchaseOrderItemByKey(int itemId) {
		PurchaseOrderItemDao dao = new PurchaseOrderItemDao();
		return dao.getPurchaseOrderItemByKey(itemId);
	}

	public PageResultBean<PurchaseOrderItemBean> getPurchaseOrderItemPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		PurchaseOrderItemDao dao = new PurchaseOrderItemDao();
		return dao.getPurchaseOrderItemPageListByMap(condMap, pageBean);
	}

	public List<PurchaseOrderItemBean> getPurchaseOrderItemListByPoId(int poId) {
		PurchaseOrderItemDao dao = new PurchaseOrderItemDao();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("poId", String.valueOf(poId));
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		return dao.getPurchaseOrderItemListByMap(condMap);
	}

	public int removePurchaseOrderItem(PurchaseOrderItemBean bean) {
		PurchaseOrderItemDao dao = new PurchaseOrderItemDao();
		bean.setStatus(Constants.STATUS_NOT_VALID);
		int result = dao.updatePurchaseOrderItem(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendRemoveLog(bean, InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER, bean.getPoId(), bean.getItemId(), 0,
					PurchaseOrderLogConfig.LOG_PURCHASE_ORDER_ITEM);
		}
		return result;
	}

	public ReturnMessageBean verifyOrderInfo(String[] itemValueArr) {
		ReturnMessageBean result = new ReturnMessageBean();
		StringBuffer sb = new StringBuffer();
		if (!StringKit.isValid(itemValueArr[0].trim())) {
			sb.append("计量单位必须填写!");
		}
		if (!StringKit.isValid(itemValueArr[1].trim())) {
			sb.append("单位价格必须填写!");
		}
		if (!StringKit.isValid(itemValueArr[2].trim())) {
			sb.append("数量必须填写!");
		}
		return result;
	}

	public ReturnMessageBean verifyOrderInfo(Map<String, String> itemValueMap) {
		ReturnMessageBean result = new ReturnMessageBean();
		StringBuffer sb = new StringBuffer();
		if (!StringKit.isValid(itemValueMap.get("unit").trim())) {
			sb.append("计量单位必须填写!");
		}
		if (!StringKit.isValid(itemValueMap.get("unitPrice").trim())) {
			sb.append("单位价格必须填写!");
		}
		if (!StringKit.isValid(itemValueMap.get("quatity").trim())) {
			sb.append("数量必须填写!");
		}
		return result;
	}

	public Boolean isVaildArray(String[] array) {
		Boolean result = false;
		for (String s : array) {
			if (!s.trim().equals(""))
				result = true;
		}
		return result;
	}

}