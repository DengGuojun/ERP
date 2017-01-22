package com.lpmas.erp.inventory.business;

import java.util.HashMap;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.inventory.bean.WarePlannedPriceBean;
import com.lpmas.erp.inventory.dao.WarePlannedPriceDao;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.page.PageBean;
import com.lpmas.framework.page.PageResultBean;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.pdm.bean.MaterialInfoBean;
import com.lpmas.pdm.bean.ProductItemBean;
import com.lpmas.pdm.client.PdmServiceClient;

public class WarePlannedPriceBusiness {
	public int addWarePlannedPrice(WarePlannedPriceBean bean) {
		WarePlannedPriceDao dao = new WarePlannedPriceDao();
		return dao.insertWarePlannedPrice(bean);
	}

	public int updateWarePlannedPrice(WarePlannedPriceBean bean) {
		WarePlannedPriceDao dao = new WarePlannedPriceDao();
		return dao.updateWarePlannedPrice(bean);
	}

	public WarePlannedPriceBean getWarePlannedPriceByKey(int wareType, int wareId, String planMonth, String unit) {
		WarePlannedPriceDao dao = new WarePlannedPriceDao();
		return dao.getWarePlannedPriceByKey(wareType, wareId, planMonth, unit);
	}

	public PageResultBean<WarePlannedPriceBean> getWarePlannedPricePageListByMap(HashMap<String, String> condMap,
			PageBean pageBean) {
		WarePlannedPriceDao dao = new WarePlannedPriceDao();
		return dao.getWarePlannedPricePageListByMap(condMap, pageBean);
	}

	public ReturnMessageBean verifyWarePlannedPriceBean(WarePlannedPriceBean bean) {
		ReturnMessageBean result = new ReturnMessageBean();
		if (bean.getPrice() <= 0) {
			result.setMessage("计划价格必须填写");
			return result;
		}

		PdmServiceClient client = new PdmServiceClient();
		MaterialInfoBean materialInfoBean = null;
		ProductItemBean productItemBean = null;
		String wareUnit = "";
		if (bean.getWareType() == InfoTypeConfig.INFO_TYPE_MATERIAL) {
			materialInfoBean = client.getMaterialInfoByKey(bean.getWareId());
			if (materialInfoBean == null || materialInfoBean.getMaterialId() != bean.getWareId()) {
				result.setMessage("该制品不存在");
				return result;
			}
			wareUnit = materialInfoBean.getUnit();
		} else if (bean.getWareType() == InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM) {
			productItemBean = client.getProductItemByKey(bean.getWareId());
			if (productItemBean == null || productItemBean.getItemId() != bean.getWareId()) {
				result.setMessage("该制品不存在");
				return result;
			}
			wareUnit = productItemBean.getUnit();
		} else {
			result.setMessage("制品类型非法");
			return result;
		}

		if (!wareUnit.equals(bean.getUnit())) {
			result.setMessage("计量单位非法");
			return result;
		}

		result.setCode(Constants.STATUS_VALID);
		return result;
	}

}