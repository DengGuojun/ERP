package com.lpmas.erp.bom.business;

import java.util.HashMap;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.bom.bean.BomInfoBean;
import com.lpmas.erp.bom.config.BomLogConfig;
import com.lpmas.erp.bom.dao.BomInfoDao;
import com.lpmas.erp.util.ErpLogSender;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;

public class BomInfoBusiness {
	public int addBomInfo(BomInfoBean bean) {
		BomInfoDao dao = new BomInfoDao();
		int result = dao.insertBomInfo(bean);
		if (result > 0) {
			bean.setBomId(result);
			ErpLogSender helper = new ErpLogSender();
			helper.sendAddLog(bean, InfoTypeConfig.INFO_TYPE_BOM, bean.getBomId(), BomLogConfig.LOG_BOM_INFO);
		}
		return result;
	}

	public int updateBomInfo(BomInfoBean bean) {
		BomInfoDao dao = new BomInfoDao();
		BomInfoBean orginalBean = getBomInfoByKey(bean.getBomId());
		int result = dao.updateBomInfo(bean);
		if (result > 0) {
			ErpLogSender helper = new ErpLogSender();
			helper.sendUpdateLog(orginalBean, bean, InfoTypeConfig.INFO_TYPE_BOM, bean.getBomId(), BomLogConfig.LOG_BOM_INFO);
		}
		return result;
	}

	public BomInfoBean getBomInfoByKey(int bomId) {
		BomInfoDao dao = new BomInfoDao();
		return dao.getBomInfoByKey(bomId);
	}

	public PageResultBean<BomInfoBean> getBomInfoPageListByMap(HashMap<String, String> condMap, PageBean pageBean) {
		BomInfoDao dao = new BomInfoDao();
		condMap.put("status", String.valueOf(Constants.STATUS_VALID));
		return dao.getBomInfoPageListByMap(condMap, pageBean);
	}

}