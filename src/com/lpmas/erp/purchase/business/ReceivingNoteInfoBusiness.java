package com.lpmas.erp.purchase.business;

import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.purchase.bean.ReceivingNoteInfoBean;
import com.lpmas.erp.purchase.config.PurchaseOrderLogConfig;
import com.lpmas.erp.purchase.dao.ReceivingNoteInfoDao;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;

public class ReceivingNoteInfoBusiness {
	public int addReceivingNoteInfo(ReceivingNoteInfoBean bean) {
		ReceivingNoteInfoDao dao = new ReceivingNoteInfoDao();
		int result = dao.insertReceivingNoteInfo(bean);
		if (result > 0) {
			bean.setRnId(result);
			ErpLogSender helper = new ErpLogSender();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER, bean.getPoId(), bean.getRnId(),
					bean.getDnId(), PurchaseOrderLogConfig.LOG_RECEIVING_NOTE_INFO);
		}
		return result;
	}

	public int updateReceivingNoteInfo(ReceivingNoteInfoBean bean) {
		ReceivingNoteInfoDao dao = new ReceivingNoteInfoDao();
		ReceivingNoteInfoBean orginalBean = getReceivingNoteInfoByKey(bean.getRnId());
		int result = dao.updateReceivingNoteInfo(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(orginalBean, bean, InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER, bean.getPoId(),
					bean.getRnId(), bean.getDnId(), PurchaseOrderLogConfig.LOG_RECEIVING_NOTE_INFO);
		}
		return result;
	}

	public int removeReceivingNoteInfo(ReceivingNoteInfoBean bean) {
		ReceivingNoteInfoDao dao = new ReceivingNoteInfoDao();
		bean.setStatus(Constants.STATUS_NOT_VALID);
		int result = dao.updateReceivingNoteInfo(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendRemoveLog(bean, InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER, bean.getPoId(), bean.getDnId(),
					bean.getRnId(), PurchaseOrderLogConfig.LOG_RECEIVING_NOTE_INFO);
		}
		return result;
	}

	public ReceivingNoteInfoBean getReceivingNoteInfoByKey(int rnId) {
		ReceivingNoteInfoDao dao = new ReceivingNoteInfoDao();
		return dao.getReceivingNoteInfoByKey(rnId);
	}

	public PageResultBean<ReceivingNoteInfoBean> getReceivingNoteInfoPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		ReceivingNoteInfoDao dao = new ReceivingNoteInfoDao();
		return dao.getReceivingNoteInfoPageListByMap(condMap, pageBean);
	}

	public ReceivingNoteInfoBean getReceivingNoteInfoByDnId(int dnId) {
		ReceivingNoteInfoDao dao = new ReceivingNoteInfoDao();
		return dao.getReceivingNoteInfoByDnId(dnId);
	}

	public int getReceivingNoteInfoCountByPoId(int poId) {
		ReceivingNoteInfoDao dao = new ReceivingNoteInfoDao();
		return dao.getReceivingNoteInfoCountByPoId(poId);
	}

	public List<ReceivingNoteInfoBean> getReceivingNoteInfoListByPoId(int poId) {
		ReceivingNoteInfoDao dao = new ReceivingNoteInfoDao();
		return dao.getReceivingNoteInfoListByPoId(poId);
	}

}