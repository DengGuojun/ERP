package com.lpmas.erp.inventory.business.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.lpmas.erp.config.ErpConsoleConfig;
import com.lpmas.erp.inventory.bean.DeliveryVoucherItemBean;
import com.lpmas.erp.inventory.bean.WarehouseInventoryBean;
import com.lpmas.erp.inventory.business.DeliveryVoucherInfoBusiness;
import com.lpmas.erp.inventory.business.DeliveryVoucherItemBusiness;
import com.lpmas.erp.inventory.business.DeliveryVoucherItemDisplayBusiness;
import com.lpmas.erp.inventory.business.WarehouseInventoryBusiness;
import com.lpmas.erp.inventory.config.WarehouseInventoryConfig;
import com.lpmas.framework.util.FreemarkerKit;
import com.lpmas.framework.util.ListKit;
import com.lpmas.pdm.bean.MaterialInfoBean;
import com.lpmas.pdm.client.PdmServiceClient;

public class MaterialDeliveryVoucherItemDisplayImpl extends DeliveryVoucherItemDisplayBusiness {

	@Override
	public String getDeliveryVoucherDetailDisStr(int dvId, int sourceType, int sourceOrderId, Boolean isModify) {
		String templeteName = "materialDvDetailDisplay.html";

		FreemarkerKit freemarkerKit = new FreemarkerKit();
		HashMap<String, Object> contentMap = freemarkerKit.getContentMap();
		DeliveryVoucherItemBusiness business = new DeliveryVoucherItemBusiness();
		// 放入基本参数
		contentMap.put("DvId", dvId);
		contentMap.put("sourceType", sourceType);
		contentMap.put("sourceOrderId", sourceOrderId);

		List<Integer> wareIdList = new ArrayList<Integer>();
		Map<String, String> unitNameMap = new HashMap<String, String>();
		Map<String, String> wiIdMap = new HashMap<String, String>();
		Map<String, Double> inventoryQtyMap = new HashMap<String, Double>();
		PdmServiceClient client = new PdmServiceClient();
		// 判断有无原单，任意一个不为NULL判定为有原单
		Boolean fromSource = false;
		Boolean hasSource = false;
		if (sourceType != 0 && sourceOrderId != 0) {
			// 有原单
			hasSource = true;
		} else {
			// 无原单
			hasSource = false;
		}
		contentMap.put("hasSource", hasSource);
		if (dvId == 0 && hasSource) {
			// 新建，或者是强制从别的源单取数据
			// 判断源单类型
			/*
			 * 暂不实现由原单的情况，全部是无源带情况
			 */
		} else {
			// 从出库明细中获取数据
			WarehouseInventoryBusiness inventoryBusiness = new WarehouseInventoryBusiness();
			DeliveryVoucherInfoBusiness deliveryVoucherInfoBusiness = new DeliveryVoucherInfoBusiness();
			List<DeliveryVoucherItemBean> deliveryVoucherItemList = business.getDeliveryVoucherItemListByDvId(dvId);
			for (DeliveryVoucherItemBean bean : deliveryVoucherItemList) {
				// 获取单位
				unitNameMap.put(String.valueOf(bean.getWareId()),
						client.getUnitInfoByCode(bean.getUnit()).getUnitName());
				int warehouseId = deliveryVoucherInfoBusiness.getDeliveryVoucherInfoByKey(bean.getDvId())
						.getWarehouseId();
				WarehouseInventoryBean inventoryBean = inventoryBusiness.getWarehouseInventoryBeanByCondition(
						warehouseId, bean.getWareType(), bean.getWareId(), bean.getInventoryType(),
						bean.getBatchNumber(), bean.getProductionDate());
				wiIdMap.put(String.valueOf(bean.getDvItemId()), String.valueOf(inventoryBean.getWiId()));
				inventoryQtyMap.put(String.valueOf(bean.getDvItemId()), inventoryBean.getQuantity());
			}
			Map<Integer, DeliveryVoucherItemBean> deliveryVoucherItemMap = ListKit.list2Map(deliveryVoucherItemList,
					"wareId");
			if (!deliveryVoucherItemList.isEmpty()) {
				wareIdList = ListKit.string2List(ListKit.list2String(deliveryVoucherItemList, "wareId", ","), ",",
						Integer.class);
			}
			contentMap.put("ItemList", deliveryVoucherItemList);
			contentMap.put("ItemMap", deliveryVoucherItemMap);
		}
		// 查CLIENT
		// 获取制品信息
		Map<String, MaterialInfoBean> itemDetailMap = new HashMap<String, MaterialInfoBean>();
		for (Integer id : wareIdList) {
			MaterialInfoBean materialInfoBean = client.getMaterialInfoByKey(id);
			itemDetailMap.put(String.valueOf(id), materialInfoBean);
		}
		// 获取库存类型
		// 转化成整形的KEY
		Map<String, String> inventoryTypeMap = new HashMap<String, String>();
		for (Entry<Integer, String> entry : WarehouseInventoryConfig.WAREHOUSE_INVENTORY_TYPE_MAP.entrySet()) {
			inventoryTypeMap.put(String.valueOf(entry.getKey()), entry.getValue());
		}

		// 把数据存在contentMap中
		contentMap.put("inventoryQtyMap", inventoryQtyMap);
		contentMap.put("wiIdMap", wiIdMap);
		contentMap.put("inventoryTypeMap", inventoryTypeMap);
		contentMap.put("fromSource", fromSource);
		contentMap.put("ItemDetailMap", itemDetailMap);
		contentMap.put("isModify", isModify);
		contentMap.put("UnitNameMap", unitNameMap);

		return freemarkerKit.mergeTemplate(contentMap, ErpConsoleConfig.TEMPLATE_PATH + "/inventory", templeteName);
	}

}
