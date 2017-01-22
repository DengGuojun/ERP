package com.lpmas.erp.purchase.factory;

import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.business.PurchaseOrderStatus;
import com.lpmas.erp.purchase.business.impl.PurchaseOrderApprovedStatusImpl;
import com.lpmas.erp.purchase.business.impl.PurchaseOrderArchivedStatusImpl;
import com.lpmas.erp.purchase.business.impl.PurchaseOrderEditStatusImpl;
import com.lpmas.erp.purchase.business.impl.PurchaseOrderPlacedOrderStatusImpl;
import com.lpmas.erp.purchase.business.impl.PurchaseOrderReceivedStatusImpl;
import com.lpmas.erp.purchase.business.impl.PurchaseOrderWaitApproveStatusImpl;
import com.lpmas.erp.purchase.config.PurchaseOrderConfig;

public class PurchaseOrderStatusFactory {

	public PurchaseOrderStatus getPurchaseOrderStatus(PurchaseOrderInfoBean bean) {
		if (bean.getPoStatus().equals(PurchaseOrderConfig.PO_STATUS_EDIT)) {
			return new PurchaseOrderEditStatusImpl(bean);
		} else if (bean.getPoStatus().equals(PurchaseOrderConfig.PO_STATUS_WAIT_APPROVE)) {
			return new PurchaseOrderWaitApproveStatusImpl(bean);
		} else if (bean.getPoStatus().equals(PurchaseOrderConfig.PO_STATUS_APPROVED)) {
			return new PurchaseOrderApprovedStatusImpl(bean);
		} else if (bean.getPoStatus().equals(PurchaseOrderConfig.PO_STATUS_PLACED_ORDER)) {
			return new PurchaseOrderPlacedOrderStatusImpl(bean);
		} else if (bean.getPoStatus().equals(PurchaseOrderConfig.PO_STATUS_RECEIVED)) {
			return new PurchaseOrderReceivedStatusImpl(bean);
		} else if (bean.getPoStatus().equals(PurchaseOrderConfig.PO_STATUS_ARCHIVED)) {
			return new PurchaseOrderArchivedStatusImpl(bean);
		}
		return null;
	}
}
