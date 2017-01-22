package com.lpmas.erp.purchase.business;

import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.bean.PurchaseOrderMemoBean;
import com.lpmas.erp.purchase.config.PurchaseOrderLogConfig;
import com.lpmas.erp.purchase.dao.PurchaseOrderMemoDao;
import com.lpmas.erp.purchase.util.PurchaseOrderStatusHelper;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;

public class PurchaseOrderMemoBusiness {
	public int addPurchaseOrderMemo(PurchaseOrderMemoBean bean) {
		PurchaseOrderMemoDao dao = new PurchaseOrderMemoDao();
		int result = dao.insertPurchaseOrderMemo(bean);
		if (result > 0) {
			bean.setMemoId(result);
			ErpLogSender helper = new ErpLogSender();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER, bean.getPoId(), bean.getMemoId(), 0,
					PurchaseOrderLogConfig.LOG_PURCHASE_ORDER_MEMO);
		}
		return result;
	}

	public int updatePurchaseOrderMemo(PurchaseOrderMemoBean bean) {
		PurchaseOrderMemoDao dao = new PurchaseOrderMemoDao();
		PurchaseOrderMemoBean orginalBean = getPurchaseOrderMemoByKey(bean.getMemoId());
		int result = dao.updatePurchaseOrderMemo(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(orginalBean, bean, InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER, bean.getPoId(),
					bean.getMemoId(), 0, PurchaseOrderLogConfig.LOG_PURCHASE_ORDER_MEMO);
		}
		return result;
	}

	public PurchaseOrderMemoBean getPurchaseOrderMemoByKey(int memoId) {
		PurchaseOrderMemoDao dao = new PurchaseOrderMemoDao();
		return dao.getPurchaseOrderMemoByKey(memoId);
	}

	public PageResultBean<PurchaseOrderMemoBean> getPurchaseOrderMemoPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		PurchaseOrderMemoDao dao = new PurchaseOrderMemoDao();
		return dao.getPurchaseOrderMemoPageListByMap(condMap, pageBean);
	}

	public List<PurchaseOrderMemoBean> getPurchaseOrderMemoListByPoId(int poId) {
		PurchaseOrderMemoDao dao = new PurchaseOrderMemoDao();
		return dao.getPurchaseOrderMemoListByPoId(poId);
	}

	public ReturnMessageBean verifyPurchaseOrderMemo(PurchaseOrderMemoBean bean) {
		ReturnMessageBean result = new ReturnMessageBean();
		if (!StringKit.isValid(bean.getMemoContent())) {
			result.setMessage("备注信息必须填写");
		}
		PurchaseOrderInfoBusiness infoBusiness = new PurchaseOrderInfoBusiness();
		PurchaseOrderInfoBean poInfoBean = infoBusiness.getPurchaseOrderInfoByKey(bean.getPoId());
		if (PurchaseOrderStatusHelper.isLock(poInfoBean)) {
			result.setMessage("采购订单被锁定，不能修改");
		}
		if (poInfoBean.getStatus() == Constants.STATUS_NOT_VALID) {
			result.setMessage("采购订单已被删除");
		}
		return result;
	}

}