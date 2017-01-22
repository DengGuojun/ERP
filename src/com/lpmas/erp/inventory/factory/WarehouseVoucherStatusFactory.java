package com.lpmas.erp.inventory.factory;

import com.lpmas.erp.inventory.bean.WarehouseVoucherInfoBean;
import com.lpmas.erp.inventory.business.WarehouseVoucherStatus;
import com.lpmas.erp.inventory.business.impl.WarehouseVoucherApprovedStatusImpl;
import com.lpmas.erp.inventory.business.impl.WarehouseVoucherClosedStatusImpl;
import com.lpmas.erp.inventory.business.impl.WarehouseVoucherEditStatusImpl;
import com.lpmas.erp.inventory.business.impl.WarehouseVoucherWaitApproveStatusImpl;
import com.lpmas.erp.inventory.config.WarehouseVoucherConfig;

public class WarehouseVoucherStatusFactory {

	public WarehouseVoucherStatus getWarehouseVoucherStatus(WarehouseVoucherInfoBean bean) {
		if (bean.getWvStatus().equals(WarehouseVoucherConfig.WV_STATUS_EDIT)) {
			return new WarehouseVoucherEditStatusImpl(bean);
		} else if (bean.getWvStatus().equals(WarehouseVoucherConfig.WV_STATUS_WAIT_APPROVE)) {
			return new WarehouseVoucherWaitApproveStatusImpl(bean);
		} else if (bean.getWvStatus().equals(WarehouseVoucherConfig.WV_STATUS_APPROVED)) {
			return new WarehouseVoucherApprovedStatusImpl(bean);
		} else if (bean.getWvStatus().equals(WarehouseVoucherConfig.WV_STATUS_CLOSED)) {
			return new WarehouseVoucherClosedStatusImpl(bean);
		} 
		return null;
	}
}
