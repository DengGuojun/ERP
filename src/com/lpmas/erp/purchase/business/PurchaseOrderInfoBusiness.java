package com.lpmas.erp.purchase.business;

import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.purchase.bean.DeliveryNoteInfoBean;
import com.lpmas.erp.purchase.bean.DeliveryNoteItemBean;
import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.bean.PurchaseOrderItemBean;
import com.lpmas.erp.purchase.bean.ReceivingNoteInfoBean;
import com.lpmas.erp.purchase.bean.ReceivingNoteItemBean;
import com.lpmas.erp.purchase.config.PurchaseOrderConfig;
import com.lpmas.erp.purchase.config.PurchaseOrderLogConfig;
import com.lpmas.erp.purchase.dao.PurchaseOrderInfoDao;
import com.lpmas.erp.purchase.util.PurchaseOrderStatusHelper;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;

public class PurchaseOrderInfoBusiness {
	public int addPurchaseOrderInfo(PurchaseOrderInfoBean bean) {
		PurchaseOrderInfoDao dao = new PurchaseOrderInfoDao();
		int result = dao.insertPurchaseOrderInfo(bean);
		if (result > 0) {
			bean.setPoId(result);
			ErpLogSender helper = new ErpLogSender();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER, bean.getPoId(),
					PurchaseOrderLogConfig.LOG_PURCHASE_ORDER_INFO);
		}
		return result;
	}

	public int updatePurchaseOrderInfo(PurchaseOrderInfoBean bean) {
		PurchaseOrderInfoDao dao = new PurchaseOrderInfoDao();
		PurchaseOrderInfoBean orginalBean = getPurchaseOrderInfoByKey(bean.getPoId());
		int result = dao.updatePurchaseOrderInfo(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(orginalBean, bean, InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER, bean.getPoId(),
					PurchaseOrderLogConfig.LOG_PURCHASE_ORDER_INFO);
		}
		return result;
	}

	public PurchaseOrderInfoBean getPurchaseOrderInfoByNumber(String Number) {
		PurchaseOrderInfoDao dao = new PurchaseOrderInfoDao();
		return dao.getPurchaseOrderInfoByNumber(Number);
	}

	public PurchaseOrderInfoBean getPurchaseOrderInfoByKey(int poId) {
		PurchaseOrderInfoDao dao = new PurchaseOrderInfoDao();
		return dao.getPurchaseOrderInfoByKey(poId);
	}

	public PageResultBean<PurchaseOrderInfoBean> getPurchaseOrderInfoPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		PurchaseOrderInfoDao dao = new PurchaseOrderInfoDao();
		return dao.getPurchaseOrderInfoPageListByMap(condMap, pageBean);
	}

	public ReturnMessageBean verifyPurchaseOrderInfo(PurchaseOrderInfoBean bean) {
		ReturnMessageBean result = new ReturnMessageBean();
		if (bean.getWareType() <= 0) {
			result.setMessage("订单类型必须填写");
		} else if (bean.getSupplierId() <= 0) {
			result.setMessage("供应商信息必须填写");
		} else if (bean.getReceiverId() < 0) {
			result.setMessage("收货信息必须填写");
		} else if (bean.getPurchaseType() <= 0) {
			result.setMessage("采购类型必须填写");
		} else if (!StringKit.isValid(bean.getInvoiceStatus())) {
			result.setMessage("发票状态必须填写");
		} else if (!StringKit.isValid(bean.getContractStatus())) {
			result.setMessage("合同状态必须填写");
		} else if (bean.getContractStatus().equals(PurchaseOrderConfig.PCS_SIGNED) && bean.getContractId() <= 0) {
			result.setMessage("合同信息必须填写");
		}
		if (bean.getPoId() > 0) {
			PurchaseOrderInfoBean dbBean = getPurchaseOrderInfoByKey(bean.getPoId());
			if (PurchaseOrderStatusHelper.isLock(dbBean)) {
				result.setMessage("采购订单已被锁定，不能修改");
			}
			if (!dbBean.getPoStatus().equals(bean.getPoStatus()) || dbBean.getStatus() == Constants.STATUS_NOT_VALID) {
				result.setMessage("操作失败！您的页面已过期，请刷新页面重新操作");
			}
		}
		return result;
	}

	public int getPurchaseOrderInfoCountForToday() {
		PurchaseOrderInfoDao dao = new PurchaseOrderInfoDao();
		return dao.getPurchaseOrderInfoCountForToday();
	}

	public int removePurchaseOrderInfo(PurchaseOrderInfoBean bean) {
		PurchaseOrderInfoDao dao = new PurchaseOrderInfoDao();
		bean.setStatus(Constants.STATUS_NOT_VALID);
		int result = dao.updatePurchaseOrderInfo(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendRemoveLog(bean, InfoTypeConfig.INFO_TYPE_PURCHASE_ORDER, bean.getPoId(),
					PurchaseOrderLogConfig.LOG_PURCHASE_ORDER_INFO);
			// 删除关联的PO Item
			PurchaseOrderItemBusiness poItemBusiness = new PurchaseOrderItemBusiness();
			List<PurchaseOrderItemBean> poItemList = poItemBusiness.getPurchaseOrderItemListByPoId(bean.getPoId());
			for (PurchaseOrderItemBean poItemBean : poItemList) {
				poItemBean.setModifyUser(bean.getModifyUser());
				poItemBusiness.removePurchaseOrderItem(poItemBean);
			}
			// 删除关联的发货单及发货单Item
			DeliveryNoteInfoBusiness dnInfoBusiness = new DeliveryNoteInfoBusiness();
			List<DeliveryNoteInfoBean> deliveryNoteList = dnInfoBusiness.getDeliveryInfoListByPoId(bean.getPoId());
			for (DeliveryNoteInfoBean dnInfoBean : deliveryNoteList) {
				dnInfoBean.setModifyUser(bean.getModifyUser());
				dnInfoBusiness.removeDeliveryNoteInfo(dnInfoBean);
				DeliveryNoteItemBusiness dnItemBusiness = new DeliveryNoteItemBusiness();
				List<DeliveryNoteItemBean> dnItemList = dnItemBusiness
						.getDeliveryNoteItemListByDnId(dnInfoBean.getDnId());
				for (DeliveryNoteItemBean dnItemBean : dnItemList) {
					dnItemBean.setModifyUser(bean.getModifyUser());
					dnItemBusiness.removeDeliveryNoteItem(dnItemBean);
				}
			}
			// 删除关联的收货单及收货单Item
			ReceivingNoteInfoBusiness rnInfoBusiness = new ReceivingNoteInfoBusiness();
			List<ReceivingNoteInfoBean> receivingNoteList = rnInfoBusiness
					.getReceivingNoteInfoListByPoId(bean.getPoId());
			for (ReceivingNoteInfoBean rnInfoBean : receivingNoteList) {
				rnInfoBean.setModifyUser(bean.getModifyUser());
				rnInfoBusiness.removeReceivingNoteInfo(rnInfoBean);
				ReceivingNoteItemBusiness rnItemBusiness = new ReceivingNoteItemBusiness();
				List<ReceivingNoteItemBean> rnItemList = rnItemBusiness
						.getReceivingNoteItemListByRnId(rnInfoBean.getRnId());
				for (ReceivingNoteItemBean rnItemBean : rnItemList) {
					rnItemBean.setModifyUser(bean.getModifyUser());
					rnItemBusiness.removeReceivingNoteItem(rnItemBean);
				}
			}

		}
		return result;
	}

	public boolean checkHasPurchaseOrderInfoByWarehouseId(int warehouseId) {
		PurchaseOrderInfoDao dao = new PurchaseOrderInfoDao();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("receiverId", String.valueOf(warehouseId));
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		condMap.put("receiverType", String.valueOf(PurchaseOrderConfig.RECEIVER_TYPE_WAREHOUSE));
		return dao.checkHasPurchaseOrderInfoByMap(condMap);
	}

}