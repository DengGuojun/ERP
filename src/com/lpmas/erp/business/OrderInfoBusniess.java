package com.lpmas.erp.business;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.erp.bean.OrderInfoBean;
import com.lpmas.erp.inventory.bean.WarehouseTransferOrderInfoBean;
import com.lpmas.erp.inventory.business.WarehouseTransferOrderInfoBusiness;
import com.lpmas.erp.purchase.bean.DeliveryNoteInfoBean;
import com.lpmas.erp.purchase.business.DeliveryNoteInfoBusiness;

public class OrderInfoBusniess {
	public OrderInfoBean getOrderInfoByKey(int orderType, int orderId) {
		if (orderType == InfoTypeConfig.INFO_TYPE_WAREHOUSE_TRANSFER_ORDER) {
			return getOrderInfoByKeyForTransfer(orderId);
		} else if (orderType == InfoTypeConfig.INFO_TYPE_DELIVERY_NOTE) {
			return getOrderInfoByKeyForDeliveryNote(orderId);
		} else {
			return null;
		}
	}

	public OrderInfoBean getOrderInfoByKey(int orderType, String orderNumber) {
		if (orderType == InfoTypeConfig.INFO_TYPE_WAREHOUSE_TRANSFER_ORDER) {
			return getOrderInfoByNumberForTransfer(orderNumber);
		} else if (orderType == InfoTypeConfig.INFO_TYPE_DELIVERY_NOTE) {
			return getOrderInfoByNumberForDeliveryNote(orderNumber);
		} else {
			return null;
		}
	}

	private OrderInfoBean getOrderInfoByKeyForTransfer(int orderId) {
		WarehouseTransferOrderInfoBusiness warehouseTransferOrderInfoBusiness = new WarehouseTransferOrderInfoBusiness();
		WarehouseTransferOrderInfoBean warehouseTransferOrderInfoBean = warehouseTransferOrderInfoBusiness
				.getWarehouseTransferOrderInfoByKey(orderId);
		if (warehouseTransferOrderInfoBean == null)
			return null;
		return getOrderInfoByTransfer(warehouseTransferOrderInfoBean);
	}

	private OrderInfoBean getOrderInfoByKeyForDeliveryNote(int orderId) {
		DeliveryNoteInfoBusiness deliveryNoteInfoBusiness = new DeliveryNoteInfoBusiness();
		DeliveryNoteInfoBean deliveryNoteInfoBean = deliveryNoteInfoBusiness.getDeliveryNoteInfoByKey(orderId);
		if (deliveryNoteInfoBean == null)
			return null;
		return getOrderInfoByDeliveryNote(deliveryNoteInfoBean);
	}

	private OrderInfoBean getOrderInfoByNumberForTransfer(String orderNumber) {
		WarehouseTransferOrderInfoBusiness warehouseTransferOrderInfoBusiness = new WarehouseTransferOrderInfoBusiness();
		WarehouseTransferOrderInfoBean warehouseTransferOrderInfoBean = warehouseTransferOrderInfoBusiness
				.getWarehouseTransferOrderInfoByNumber(orderNumber);
		if (warehouseTransferOrderInfoBean == null)
			return null;
		return getOrderInfoByTransfer(warehouseTransferOrderInfoBean);
	}

	private OrderInfoBean getOrderInfoByNumberForDeliveryNote(String orderNumber) {
		DeliveryNoteInfoBusiness deliveryNoteInfoBusiness = new DeliveryNoteInfoBusiness();
		DeliveryNoteInfoBean deliveryNoteInfoBean = deliveryNoteInfoBusiness.getDeliveryNoteInfoByNumber(orderNumber);
		if (deliveryNoteInfoBean == null)
			return null;
		return getOrderInfoByDeliveryNote(deliveryNoteInfoBean);
	}

	private OrderInfoBean getOrderInfoByTransfer(WarehouseTransferOrderInfoBean warehouseTransferOrderInfoBean) {
		OrderInfoBean result = new OrderInfoBean();
		result.setOrderType(InfoTypeConfig.INFO_TYPE_WAREHOUSE_TRANSFER_ORDER);
		result.setOrderId(warehouseTransferOrderInfoBean.getToId());
		result.setOrderNumber(warehouseTransferOrderInfoBean.getToNumber());
		result.setWareType(warehouseTransferOrderInfoBean.getWareType());
		result.setApproveStatus(warehouseTransferOrderInfoBean.getApproveStatus());
		result.setOrderStatus(warehouseTransferOrderInfoBean.getToStatus());
		result.setSyncStatus(warehouseTransferOrderInfoBean.getSyncStatus());
		return result;
	}

	private OrderInfoBean getOrderInfoByDeliveryNote(DeliveryNoteInfoBean deliveryNoteInfoBean) {
		OrderInfoBean result = new OrderInfoBean();
		result.setOrderType(InfoTypeConfig.INFO_TYPE_DELIVERY_NOTE);
		result.setOrderId(deliveryNoteInfoBean.getDnId());
		result.setOrderNumber(deliveryNoteInfoBean.getDnNumber());
		return result;
	}
}
