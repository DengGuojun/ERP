package com.lpmas.erp.inventory.factory;

import com.lpmas.erp.inventory.bean.WarehouseTransferOrderInfoBean;
import com.lpmas.erp.inventory.business.WarehouseTransferOrderStatus;
import com.lpmas.erp.inventory.business.impl.WarehouseTransferOrderApprovedStatusImpl;
import com.lpmas.erp.inventory.business.impl.WarehouseTransferOrderClosedStatusImpl;
import com.lpmas.erp.inventory.business.impl.WarehouseTransferOrderEditStatusImpl;
import com.lpmas.erp.inventory.business.impl.WarehouseTransferOrderInTransitStatusImpl;
import com.lpmas.erp.inventory.business.impl.WarehouseTransferOrderWaitApproveStatusImpl;
import com.lpmas.erp.inventory.config.WarehouseTransferOrderConfig;

public class WarehouseTransferOrderStatusFactory {

	public WarehouseTransferOrderStatus getWarehouseTransferOrderStatus(WarehouseTransferOrderInfoBean bean) {
		if (bean.getToStatus().equals(WarehouseTransferOrderConfig.TO_STATUS_EDIT)) {
			return new WarehouseTransferOrderEditStatusImpl(bean);
		} else if (bean.getToStatus().equals(WarehouseTransferOrderConfig.TO_STATUS_WAIT_APPROVE)) {
			return new WarehouseTransferOrderWaitApproveStatusImpl(bean);
		} else if (bean.getToStatus().equals(WarehouseTransferOrderConfig.TO_STATUS_APPROVED)) {
			return new WarehouseTransferOrderApprovedStatusImpl(bean);
		} else if (bean.getToStatus().equals(WarehouseTransferOrderConfig.TO_STATUS_IN_TRANSIT)) {
			return new WarehouseTransferOrderInTransitStatusImpl(bean);
		} else if (bean.getToStatus().equals(WarehouseTransferOrderConfig.TO_STATUS_CLOSED)) {
			return new WarehouseTransferOrderClosedStatusImpl(bean);
		}
		return null;
	}

}
