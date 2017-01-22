package com.lpmas.erp.contract.business;

import java.util.HashMap;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.contract.bean.PurchaseContractInfoBean;
import com.lpmas.erp.contract.config.PurchaseContractLogConfig;
import com.lpmas.erp.contract.dao.PurchaseContractInfoDao;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;

public class PurchaseContractInfoBusiness {
	public int addPurchaseContractInfo(PurchaseContractInfoBean bean) {
		PurchaseContractInfoDao dao = new PurchaseContractInfoDao();
		int result = dao.insertPurchaseContractInfo(bean);
		if (result > 0) {
			bean.setPcId(result);
			ErpLogSender helper = new ErpLogSender();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_PURCHASE_CONTRACT, bean.getPcId(),
					PurchaseContractLogConfig.LOG_PURCHASE_CONTRACT_INFO);
		}
		return result;
	}

	public int updatePurchaseContractInfo(PurchaseContractInfoBean bean) {
		PurchaseContractInfoDao dao = new PurchaseContractInfoDao();
		PurchaseContractInfoBean orginalBean = getPurchaseContractInfoByKey(bean.getPcId());
		int result = dao.updatePurchaseContractInfo(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(orginalBean, bean, InfoTypeConfig.INFO_TYPE_PURCHASE_CONTRACT, bean.getPcId(),
					PurchaseContractLogConfig.LOG_PURCHASE_CONTRACT_INFO);
		}
		return result;
	}

	public PurchaseContractInfoBean getPurchaseContractInfoByKey(int pcId) {
		PurchaseContractInfoDao dao = new PurchaseContractInfoDao();
		return dao.getPurchaseContractInfoByKey(pcId);
	}

	public PageResultBean<PurchaseContractInfoBean> getPurchaseContractInfoPageListByMap(
			HashMap<String, String> condMap, PageBean pageBean) {
		PurchaseContractInfoDao dao = new PurchaseContractInfoDao();
		return dao.getPurchaseContractInfoPageListByMap(condMap, pageBean);
	}

	public PageResultBean<PurchaseContractInfoBean> getPurchaseContractInfoPageListByFuzzyQueryParam(String param,
			PageBean pageBean) {
		PurchaseContractInfoDao dao = new PurchaseContractInfoDao();
		return dao.getPurchaseContractInfoPageListByFuzzyQueryParam(param, pageBean);
	}

}