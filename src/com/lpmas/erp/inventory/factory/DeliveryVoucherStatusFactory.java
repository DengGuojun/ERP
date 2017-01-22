package com.lpmas.erp.inventory.factory;

import com.lpmas.erp.inventory.bean.DeliveryVoucherInfoBean;
import com.lpmas.erp.inventory.business.DeliveryVoucherStatus;
import com.lpmas.erp.inventory.business.impl.DeliveryVoucherApprovedStatusImpl;
import com.lpmas.erp.inventory.business.impl.DeliveryVoucherClosedStatusImpl;
import com.lpmas.erp.inventory.business.impl.DeliveryVoucherEditStatusImpl;
import com.lpmas.erp.inventory.business.impl.DeliveryVoucherWaitApproveStatusImpl;
import com.lpmas.erp.inventory.config.DeliveryVoucherConfig;

public class DeliveryVoucherStatusFactory {

	public DeliveryVoucherStatus getDeliveryVoucherStatus(DeliveryVoucherInfoBean bean) {
		if (bean.getDvStatus().equals(DeliveryVoucherConfig.DV_STATUS_EDIT)) {
			return new DeliveryVoucherEditStatusImpl(bean);
		} else if (bean.getDvStatus().equals(DeliveryVoucherConfig.DV_STATUS_WAIT_APPROVE)) {
			return new DeliveryVoucherWaitApproveStatusImpl(bean);
		} else if (bean.getDvStatus().equals(DeliveryVoucherConfig.DV_STATUS_APPROVED)) {
			return new DeliveryVoucherApprovedStatusImpl(bean);
		} else if (bean.getDvStatus().equals(DeliveryVoucherConfig.DV_STATUS_CLOSED)) {
			return new DeliveryVoucherClosedStatusImpl(bean);
		}
		return null;
	}

}
