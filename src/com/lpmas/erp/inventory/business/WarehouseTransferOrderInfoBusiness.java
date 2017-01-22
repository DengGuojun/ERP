package com.lpmas.erp.inventory.business;

import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.inventory.bean.WarehouseTransferOrderInfoBean;
import com.lpmas.erp.inventory.config.WarehouseTransferOrderLogConfig;
import com.lpmas.erp.inventory.dao.WarehouseTransferOrderInfoDao;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.DateKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;

public class WarehouseTransferOrderInfoBusiness {
	public int addWarehouseTransferOrderInfo(WarehouseTransferOrderInfoBean bean) {
		WarehouseTransferOrderInfoDao dao = new WarehouseTransferOrderInfoDao();
		int result = dao.insertWarehouseTransferOrderInfo(bean);
		if (result > 0) {
			bean.setToId(result);
			ErpLogSender helper = new ErpLogSender();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_WAREHOUSE_TRANSFER_ORDER, result,
					WarehouseTransferOrderLogConfig.LOG_WAREHOUSE_TRANSFER_ORDER_INFO);
		}
		return result;
	}

	public int updateWarehouseTransferOrderInfo(WarehouseTransferOrderInfoBean bean) {
		WarehouseTransferOrderInfoDao dao = new WarehouseTransferOrderInfoDao();
		WarehouseTransferOrderInfoBean orgBean = getWarehouseTransferOrderInfoByKey(bean.getToId());
		int result = dao.updateWarehouseTransferOrderInfo(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(orgBean, bean, InfoTypeConfig.INFO_TYPE_WAREHOUSE_TRANSFER_ORDER, bean.getToId(),
					WarehouseTransferOrderLogConfig.LOG_WAREHOUSE_TRANSFER_ORDER_INFO);
		}
		return result;
	}

	public WarehouseTransferOrderInfoBean getWarehouseTransferOrderInfoByKey(int toId) {
		WarehouseTransferOrderInfoDao dao = new WarehouseTransferOrderInfoDao();
		return dao.getWarehouseTransferOrderInfoByKey(toId);
	}

	public WarehouseTransferOrderInfoBean getWarehouseTransferOrderInfoByNumber(String toNumber) {
		WarehouseTransferOrderInfoDao dao = new WarehouseTransferOrderInfoDao();
		return dao.getWarehouseTransferOrderInfoByNumber(toNumber);
	}

	public int removeWarehouseTransferOrderInfo(WarehouseTransferOrderInfoBean bean, int userId) {
		bean.setModifyTime(DateKit.getCurrentTimestamp());
		bean.setModifyUser(userId);
		bean.setStatus(Constants.STATUS_NOT_VALID);
		return updateWarehouseTransferOrderInfo(bean);
	}

	public PageResultBean<WarehouseTransferOrderInfoBean> getWarehouseTransferOrderInfoPageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		WarehouseTransferOrderInfoDao dao = new WarehouseTransferOrderInfoDao();
		return dao.getWarehouseTransferOrderInfoPageListByMap(condMap, pageBean);
	}

	public int getWarehouseTransferOrderInfoCountForToday() {
		WarehouseTransferOrderInfoDao dao = new WarehouseTransferOrderInfoDao();
		return dao.getWarehouseTransferOrderInfoCountForToday();
	}

	public List<Integer> getWarehouseTransferOrderCreaterUserAllList() {
		WarehouseTransferOrderInfoDao dao = new WarehouseTransferOrderInfoDao();
		return dao.getWarehouseTransferOrderCreaterUserList(Constants.STATUS_VALID);
	}

	public ReturnMessageBean verifyWarehouseTransferInfo(WarehouseTransferOrderInfoBean bean) {
		ReturnMessageBean result = new ReturnMessageBean();
		if (bean.getSourceWarehouseId() <= 0) {
			result.setMessage("出库仓库必须填写");
		} else if (bean.getTargetWarehouseId() <= 0) {
			result.setMessage("入库仓库必须填写");
		} else if (!StringKit.isValid(bean.getDvInspectorName())) {
			result.setMessage("出货质检人必须填写");
		} else if (!StringKit.isValid(bean.getDvSenderName())) {
			result.setMessage("仓库出货人必须填写");
		} else if (!StringKit.isValid(bean.getWvInspectorName())) {
			result.setMessage("入货质检人必须填写");
		} else if (!StringKit.isValid(bean.getWvReceiverName())) {
			result.setMessage("入库收货人必须填写");
		} else if (!StringKit.isNull(bean.getMemo()) && bean.getMemo().length() > 1000) {
			result.setMessage("备注不能大于1000字");
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