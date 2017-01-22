package com.lpmas.erp.inventory.business;

import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.inventory.bean.WarehouseVoucherInfoBean;
import com.lpmas.erp.inventory.bean.WarehouseVoucherItemBean;
import com.lpmas.erp.inventory.config.WarehouseVoucherConfig;
import com.lpmas.erp.inventory.config.WarehouseVoucherLogConfig;
import com.lpmas.erp.inventory.dao.WarehouseVoucherInfoDao;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.ReturnMessageBean;

public class WarehouseVoucherInfoBusiness {
	public int addWarehouseVoucherInfo(WarehouseVoucherInfoBean bean) {
		WarehouseVoucherInfoDao dao = new WarehouseVoucherInfoDao();
		int result = dao.insertWarehouseVoucherInfo(bean);
		if (result > 0) {
			bean.setWvId(result);
			ErpLogSender helper = new ErpLogSender();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_WAREHOUSE_VOUCHER, result,
					WarehouseVoucherLogConfig.LOG_WV_INFO);
		}
		return result;
	}

	public int updateWarehouseVoucherInfo(WarehouseVoucherInfoBean bean) {
		WarehouseVoucherInfoDao dao = new WarehouseVoucherInfoDao();
		WarehouseVoucherInfoBean orgBean = getWarehouseVoucherInfoByKey(bean.getWvId());
		int result = dao.updateWarehouseVoucherInfo(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(orgBean, bean, InfoTypeConfig.INFO_TYPE_WAREHOUSE_VOUCHER, bean.getWvId(),
					WarehouseVoucherLogConfig.LOG_WV_INFO);
		}
		return result;
	}

	public WarehouseVoucherInfoBean getWarehouseVoucherInfoByKey(int wvId) {
		WarehouseVoucherInfoDao dao = new WarehouseVoucherInfoDao();
		return dao.getWarehouseVoucherInfoByKey(wvId);
	}

	public WarehouseVoucherInfoBean getWarehouseVoucherInfoByNumber(String wvNumber) {
		WarehouseVoucherInfoDao dao = new WarehouseVoucherInfoDao();
		return dao.getWarehouseVoucherInfoByNumber(wvNumber);
	}

	public ReturnMessageBean verifyWarehouseVoucherInfo(WarehouseVoucherInfoBean bean) {
		ReturnMessageBean result = new ReturnMessageBean();
		if ((bean.getWvType() == WarehouseVoucherConfig.WVT_PURCHASE) && bean.getSourceOrderId() <= 0) {
			result.setMessage("源单号必须填写");
		} else if (bean.getWarehouseId() <= 0) {
			result.setMessage("入库仓库必须填写");
		} else if (bean.getStockInTime() == null) {
			result.setMessage("入库日期必须填写");
		} else if (!StringKit.isValid(bean.getInspectorName())) {
			result.setMessage("入库质检人必须填写");
		} else if (!StringKit.isValid(bean.getReceiverName())) {
			result.setMessage("仓库收货人必须填写");
		} else if (!StringKit.isNull(bean.getMemo()) && bean.getMemo().length() > 1000) {
			result.setMessage("备注不能大于1000字");
		}
		return result;
	}

	public PageResultBean<WarehouseVoucherInfoBean> getWarehouseVoucherInfoPageListByMap(
			HashMap<String, String> condMap, PageBean pageBean) {
		WarehouseVoucherInfoDao dao = new WarehouseVoucherInfoDao();
		return dao.getWarehouseVoucherInfoPageListByMap(condMap, pageBean);
	}

	public int getWarehouseVoucherInfoCountForToday(int wvType) {
		WarehouseVoucherInfoDao dao = new WarehouseVoucherInfoDao();
		return dao.getWarehhouseVoucherInfoCountForToday(wvType);
	}

	public List<Integer> getWarehouseVoucherCreaterUserAllList() {
		WarehouseVoucherInfoDao dao = new WarehouseVoucherInfoDao();
		return dao.getWarehouseVoucherCreaterUserList(Constants.STATUS_VALID);
	}

	public List<WarehouseVoucherInfoBean> getWarehouseVoucherInfoListByOrderId(int sourceOrderId) {
		WarehouseVoucherInfoDao dao = new WarehouseVoucherInfoDao();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		condMap.put("sourceOrderId", String.valueOf(sourceOrderId));
		return dao.getWarehouseVoucherInfoListByMap(condMap);
	}

	public List<WarehouseVoucherInfoBean> getWarehouseVoucherInfoListByWvIdList(List<Integer> wvIdList,
			PageBean pageBean) {
		WarehouseVoucherInfoDao dao = new WarehouseVoucherInfoDao();
		return dao.getWarehouseVoucherInfoListByWvIdList(wvIdList, pageBean);
	}

	public boolean checkHasWarehouseVoucherInfoByWarehouseId(int warehouseId) {
		WarehouseVoucherInfoDao dao = new WarehouseVoucherInfoDao();
		HashMap<String, String> condMap = new HashMap<String, String>();
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		condMap.put("warehouseId", String.valueOf(warehouseId));
		return dao.checkHasWarehouseVoucherInfoByMap(condMap);
	}

	public int removeWarehouseVoucherInfo(WarehouseVoucherInfoBean bean) {
		WarehouseVoucherInfoDao dao = new WarehouseVoucherInfoDao();
		bean.setStatus(Constants.STATUS_NOT_VALID);
		int result = dao.updateWarehouseVoucherInfo(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendRemoveLog(bean, InfoTypeConfig.INFO_TYPE_WAREHOUSE_VOUCHER, bean.getWvId(),
					WarehouseVoucherLogConfig.LOG_WV_INFO);
			// 删除关联的Item
			WarehouseVoucherItemBusiness warehouseVoucherItemBusiness = new WarehouseVoucherItemBusiness();
			List<WarehouseVoucherItemBean> warehouseVoucherItemList = warehouseVoucherItemBusiness
					.getWarehouseVoucherItemListByWvId(bean.getWvId());
			for (WarehouseVoucherItemBean warehouseVoucherItemBean : warehouseVoucherItemList) {
				warehouseVoucherItemBean.setModifyUser(bean.getModifyUser());
				warehouseVoucherItemBusiness.removeWarehouseVoucherItem(warehouseVoucherItemBean);
			}
		}
		return result;
	}

}