package com.lpmas.erp.purchase.business;

import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.purchase.bean.DeliveryNoteInfoBean;
import com.lpmas.erp.purchase.config.PurchaseOrderLogConfig;
import com.lpmas.erp.purchase.dao.DeliveryNoteInfoDao;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;

public class DeliveryNoteInfoBusiness {
	public int addDeliveryNoteInfo(DeliveryNoteInfoBean bean) {
		DeliveryNoteInfoDao dao = new DeliveryNoteInfoDao();
		int result = dao.insertDeliveryNoteInfo(bean);
		if (result > 0) {
			bean.setDnId(result);
			ErpLogSender helper = new ErpLogSender();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER, bean.getPoId(), bean.getDnId(), 0,
					PurchaseOrderLogConfig.LOG_DELIVERY_NOTE_INFO);
		}
		return result;
	}

	public int updateDeliveryNoteInfo(DeliveryNoteInfoBean bean) {
		DeliveryNoteInfoDao dao = new DeliveryNoteInfoDao();
		DeliveryNoteInfoBean orginalBean = dao.getDeliveryNoteInfoByKey(bean.getDnId());
		int result = dao.updateDeliveryNoteInfo(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(orginalBean, bean, InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER, bean.getPoId(), bean.getDnId(), 0,
					PurchaseOrderLogConfig.LOG_DELIVERY_NOTE_INFO);
		}
		return result;
	}

	public int removeDeliveryNoteInfo(DeliveryNoteInfoBean bean) {
		DeliveryNoteInfoDao dao = new DeliveryNoteInfoDao();
		bean.setStatus(Constants.STATUS_NOT_VALID);
		int result = dao.updateDeliveryNoteInfo(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendRemoveLog(bean, InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER, bean.getPoId(), bean.getDnId(), 0,
					PurchaseOrderLogConfig.LOG_DELIVERY_NOTE_INFO);
		}
		return result;
	}

	public DeliveryNoteInfoBean getDeliveryNoteInfoByKey(int dnId) {
		DeliveryNoteInfoDao dao = new DeliveryNoteInfoDao();
		return dao.getDeliveryNoteInfoByKey(dnId);
	}

	public DeliveryNoteInfoBean getDeliveryNoteInfoByNumber(String dnNumber) {
		DeliveryNoteInfoDao dao = new DeliveryNoteInfoDao();
		return dao.getDeliveryNoteInfoByNumber(dnNumber);
	}

	public PageResultBean<DeliveryNoteInfoBean> getDeliveryNoteInfoPageListByMap(HashMap<String, String> condMap, PageBean pageBean) {
		DeliveryNoteInfoDao dao = new DeliveryNoteInfoDao();
		return dao.getDeliveryNoteInfoPageListByMap(condMap, pageBean);
	}

	public int getDeliveryInfoCountByPoId(int poId) {
		DeliveryNoteInfoDao dao = new DeliveryNoteInfoDao();
		return dao.getDeliveryNoteInfoCountByPoId(poId);
	}

	public List<DeliveryNoteInfoBean> getDeliveryInfoListByPoId(int poId) {
		DeliveryNoteInfoDao dao = new DeliveryNoteInfoDao();
		return dao.getDeliveryNoteInfoListByPoId(poId);
	}

}