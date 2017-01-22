package com.lpmas.erp.inventory.business;

import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.inventory.bean.DeliveryVoucherInfoBean;
import com.lpmas.erp.inventory.bean.DeliveryVoucherItemBean;
import com.lpmas.erp.inventory.config.DeliveryVoucherLogConfig;
import com.lpmas.erp.inventory.dao.DeliveryVoucherInfoDao;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;

public class DeliveryVoucherInfoBusiness {

	public int addDeliveryVoucherInfo(DeliveryVoucherInfoBean bean) {
		DeliveryVoucherInfoDao dao = new DeliveryVoucherInfoDao();
		int result = dao.insertDeliveryVoucherInfo(bean);
		if (result > 0) {
			bean.setDvId(result);
			ErpLogSender helper = new ErpLogSender();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_DELIVERY_VOUCHER, result,
					DeliveryVoucherLogConfig.LOG_DELIVERY_VOUCHER_INFO);
		}
		return result;
	}

	public int updateDeliveryVoucherInfo(DeliveryVoucherInfoBean bean) {
		DeliveryVoucherInfoDao dao = new DeliveryVoucherInfoDao();
		DeliveryVoucherInfoBean orgBean = getDeliveryVoucherInfoByKey(bean.getDvId());
		int result = dao.updateDeliveryVoucherInfo(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(orgBean, bean, InfoTypeConfig.INFO_TYPE_DELIVERY_VOUCHER, bean.getDvId(),
					DeliveryVoucherLogConfig.LOG_DELIVERY_VOUCHER_INFO);
		}
		return result;
	}

	public DeliveryVoucherInfoBean getDeliveryVoucherInfoByKey(int dvId) {
		DeliveryVoucherInfoDao dao = new DeliveryVoucherInfoDao();
		return dao.getDeliveryVoucherInfoByKey(dvId);
	}

	public PageResultBean<DeliveryVoucherInfoBean> getDeliveryVoucherInfoPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		DeliveryVoucherInfoDao dao = new DeliveryVoucherInfoDao();
		return dao.getDeliveryVoucherInfoPageListByMap(condMap, pageBean);
	}

	public int getDeliveryVoucherInfoCountForToday(int dvType) {
		DeliveryVoucherInfoDao dao = new DeliveryVoucherInfoDao();
		return dao.getDeliveryVoucherInfoCountForToday(dvType);
	}

	public int removeDeliveryVoucherInfo(DeliveryVoucherInfoBean bean) {
		DeliveryVoucherInfoDao dao = new DeliveryVoucherInfoDao();
		bean.setStatus(Constants.STATUS_NOT_VALID);
		int result = dao.updateDeliveryVoucherInfo(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendRemoveLog(bean, InfoTypeConfig.INFO_TYPE_DELIVERY_VOUCHER, bean.getDvId(),
					DeliveryVoucherLogConfig.LOG_DELIVERY_VOUCHER_INFO);
			// 删除关联的Item
			DeliveryVoucherItemBusiness deliveryVoucherItemBusiness = new DeliveryVoucherItemBusiness();
			List<DeliveryVoucherItemBean> deliveryVoucherItemList = deliveryVoucherItemBusiness
					.getDeliveryVoucherItemListByDvId(bean.getDvId());
			for (DeliveryVoucherItemBean deliveryVoucherItemBean : deliveryVoucherItemList) {
				deliveryVoucherItemBean.setModifyUser(bean.getModifyUser());
				deliveryVoucherItemBusiness.removeDeliveryVoucherItem(deliveryVoucherItemBean);
			}
		}
		return result;
	}

	public ReturnMessageBean verifyDeliveryVoucherInfo(DeliveryVoucherInfoBean bean) {
		ReturnMessageBean result = new ReturnMessageBean();
		if (bean.getWarehouseId() <= 0) {
			result.setMessage("出库仓库必须填写");
		} else if (bean.getStockOutTime() == null) {
			result.setMessage("出库日期必须填写");
		} else if (!StringKit.isValid(bean.getInspectorName())) {
			result.setMessage("出货质检人必须填写");
		} else if (!StringKit.isValid(bean.getSenderName())) {
			result.setMessage("仓库出货人必须填写");
		} else if (!StringKit.isNull(bean.getMemo()) && bean.getMemo().length() > 1000) {
			result.setMessage("备注不能大于1000字");
		}
		return result;
	}

	public boolean checkHasDeliveryVoucherInfoByWarehouseId(int warehouseId) {
		DeliveryVoucherInfoDao dao = new DeliveryVoucherInfoDao();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		condMap.put("warehouseId", String.valueOf(warehouseId));
		return dao.checkHasDeliveryVoucherInfoByMap(condMap);
	}

}
