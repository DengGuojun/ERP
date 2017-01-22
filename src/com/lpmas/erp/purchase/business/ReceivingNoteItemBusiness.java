package com.lpmas.erp.purchase.business;

import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.purchase.bean.ReceivingNoteItemBean;
import com.lpmas.erp.purchase.config.PurchaseOrderLogConfig;
import com.lpmas.erp.purchase.dao.ReceivingNoteItemDao;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;

public class ReceivingNoteItemBusiness {
	public int addReceivingNoteItem(ReceivingNoteItemBean bean) {
		ReceivingNoteItemDao dao = new ReceivingNoteItemDao();
		ReceivingNoteInfoBusiness receivingNoteInfoBusiness = new ReceivingNoteInfoBusiness();
		int result = dao.insertReceivingNoteItem(bean);
		if (result > 0) {
			bean.setRnItemId(result);
			ErpLogSender helper = new ErpLogSender();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER, receivingNoteInfoBusiness
					.getReceivingNoteInfoByKey(bean.getRnId()).getPoId(), bean.getRnId(), bean.getRnItemId(),
					PurchaseOrderLogConfig.LOG_RECEIVING_NOTE_ITEM);
		}
		return result;
	}

	public int updateReceivingNoteItem(ReceivingNoteItemBean bean) {
		ReceivingNoteItemDao dao = new ReceivingNoteItemDao();
		ReceivingNoteItemBean orginalBean = getReceivingNoteItemByKey(bean.getRnItemId());
		int result = dao.updateReceivingNoteItem(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			ReceivingNoteInfoBusiness receivingNoteInfoBusiness = new ReceivingNoteInfoBusiness();
			helper.sendUpdateLog(orginalBean, bean, InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER, receivingNoteInfoBusiness
					.getReceivingNoteInfoByKey(bean.getRnId()).getPoId(), bean.getRnId(), bean.getRnItemId(),
					PurchaseOrderLogConfig.LOG_RECEIVING_NOTE_ITEM);
		}
		return result;
	}

	public int removeReceivingNoteItem(ReceivingNoteItemBean bean) {
		ReceivingNoteItemDao dao = new ReceivingNoteItemDao();
		bean.setStatus(Constants.STATUS_NOT_VALID);
		int result = dao.updateReceivingNoteItem(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			ReceivingNoteInfoBusiness receivingNoteInfoBusiness = new ReceivingNoteInfoBusiness();
			helper.sendRemoveLog(bean, InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER, receivingNoteInfoBusiness
					.getReceivingNoteInfoByKey(bean.getRnId()).getPoId(), bean.getRnId(), bean.getRnItemId(),
					PurchaseOrderLogConfig.LOG_RECEIVING_NOTE_ITEM);
		}
		return result;
	}

	public ReceivingNoteItemBean getReceivingNoteItemByKey(int rnItemId) {
		ReceivingNoteItemDao dao = new ReceivingNoteItemDao();
		return dao.getReceivingNoteItemByKey(rnItemId);
	}

	public PageResultBean<ReceivingNoteItemBean> getReceivingNoteItemPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		ReceivingNoteItemDao dao = new ReceivingNoteItemDao();
		return dao.getReceivingNoteItemPageListByMap(condMap, pageBean);
	}

	public List<ReceivingNoteItemBean> getReceivingNoteItemListByRnId(int rnId) {
		ReceivingNoteItemDao dao = new ReceivingNoteItemDao();
		return dao.getReceivingNoteItemListByRnId(rnId);
	}

}