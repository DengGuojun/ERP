package com.lpmas.erp.purchase.business;

import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.purchase.bean.DeliveryNoteItemBean;
import com.lpmas.erp.purchase.config.PurchaseOrderLogConfig;
import com.lpmas.erp.purchase.dao.DeliveryNoteItemDao;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;

public class DeliveryNoteItemBusiness {
	public int addDeliveryNoteItem(DeliveryNoteItemBean bean) {
		DeliveryNoteItemDao dao = new DeliveryNoteItemDao();
		DeliveryNoteInfoBusiness deliveryNoteInfoBusiness = new DeliveryNoteInfoBusiness();
		int result = dao.insertDeliveryNoteItem(bean);
		if (result > 0) {
			bean.setDnItemId(result);
			ErpLogSender helper = new ErpLogSender();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER, deliveryNoteInfoBusiness
					.getDeliveryNoteInfoByKey(bean.getDnId()).getPoId(), bean.getDnId(), bean.getDnItemId(),
					PurchaseOrderLogConfig.LOG_DELIVERY_NOTE_ITEM);
		}
		return result;
	}

	public int updateDeliveryNoteItem(DeliveryNoteItemBean bean) {
		DeliveryNoteItemDao dao = new DeliveryNoteItemDao();
		DeliveryNoteItemBean orginalBean = getDeliveryNoteItemByKey(bean.getDnItemId());
		int result = dao.updateDeliveryNoteItem(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			DeliveryNoteInfoBusiness deliveryNoteInfoBusiness = new DeliveryNoteInfoBusiness();
			helper.sendUpdateLog(orginalBean, bean, InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER, deliveryNoteInfoBusiness
					.getDeliveryNoteInfoByKey(bean.getDnId()).getPoId(), bean.getDnId(), bean.getDnItemId(),
					PurchaseOrderLogConfig.LOG_DELIVERY_NOTE_ITEM);
		}
		return result;
	}

	public int removeDeliveryNoteItem(DeliveryNoteItemBean bean) {
		DeliveryNoteItemDao dao = new DeliveryNoteItemDao();
		bean.setStatus(Constants.STATUS_NOT_VALID);
		int result = dao.updateDeliveryNoteItem(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			DeliveryNoteInfoBusiness deliveryNoteInfoBusiness = new DeliveryNoteInfoBusiness();
			helper.sendRemoveLog(bean, InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER, deliveryNoteInfoBusiness
					.getDeliveryNoteInfoByKey(bean.getDnId()).getPoId(), bean.getDnId(), bean.getDnItemId(),
					PurchaseOrderLogConfig.LOG_DELIVERY_NOTE_ITEM);
		}
		return result;
	}

	public DeliveryNoteItemBean getDeliveryNoteItemByKey(int dnItemId) {
		DeliveryNoteItemDao dao = new DeliveryNoteItemDao();
		return dao.getDeliveryNoteItemByKey(dnItemId);
	}

	public PageResultBean<DeliveryNoteItemBean> getDeliveryNoteItemPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		DeliveryNoteItemDao dao = new DeliveryNoteItemDao();
		return dao.getDeliveryNoteItemPageListByMap(condMap, pageBean);
	}

	public List<DeliveryNoteItemBean> getDeliveryNoteItemListByDnId(int dnId) {
		DeliveryNoteItemDao dao = new DeliveryNoteItemDao();
		return dao.getDeliveryNoteItemListByDnId(dnId);
	}
}