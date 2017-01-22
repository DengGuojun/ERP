package com.lpmas.erp.inventory.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lpmas.constant.info.InfoTypeConfig;
import com.lpmas.framework.bean.StatusBean;
import com.lpmas.framework.util.StatusKit;

public class WarehouseInventoryConfig {

	//制品类型
	public static List<StatusBean<Integer, String>> WARE_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
	public static HashMap<Integer, String> WARE_TYPE_MAP = new HashMap<Integer, String>();
	
	//库存操作类型
	public static final int WIO_TYPE_UPDATE = 1;
	public static final int WIO_TYPE_PUT_ON = 2;
	public static final int WIO_TYPE_OVERAGE = 3;
	public static final int WIO_TYPE_NORMAL = 4;
	public static final int WIO_TYPE_STOCK_OUT = -2;
	public static final int WIO_TYPE_LOSS = -3;
	public static final int WIO_TYPE_DEFECT = -4;
	public static final int WIO_TYPE_DAMAGE = -5;
	public static List<StatusBean<Integer, String>> WAREHOUSE_INVENTORY_OPERATION_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
	public static HashMap<Integer, String> WAREHOUSE_INVENTORY_OPERATION_TYPE_MAP = new HashMap<Integer, String>();
	
	//库存类型
	public static final int WIT_NORMAL = 1;
	public static final int WIT_DEFECT = 2;
	public static final int WIT_DAMAGE = 3;
	public static List<StatusBean<Integer, String>> WAREHOUSE_INVENTORY_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
	public static HashMap<Integer, String> WAREHOUSE_INVENTORY_TYPE_MAP = new HashMap<Integer, String>();
	
	static {
		initWareTypeList();
		initWareTypeMap();
		
		initWarehouseInventoryOperationTypeList();
		initWarehouseInventoryOperationTypeMap();
		
		initWarehouseInventoryTypeList();
		initWarehouseInventoryTypeMap();
	}
	
	private static void initWareTypeList() {
		WARE_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
		WARE_TYPE_LIST.add(new StatusBean<Integer, String>(InfoTypeConfig.INFO_TYPE_MATERIAL, "物料"));
		WARE_TYPE_LIST.add(new StatusBean<Integer, String>(InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM, "商品项"));
	}

	private static void initWareTypeMap() {
		WARE_TYPE_MAP = StatusKit.toMap(WARE_TYPE_LIST);
	}
	
	private static void initWarehouseInventoryOperationTypeList() {
		WAREHOUSE_INVENTORY_OPERATION_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
		WAREHOUSE_INVENTORY_OPERATION_TYPE_LIST.add(new StatusBean<Integer, String>(WIO_TYPE_UPDATE, "全量更新"));
		WAREHOUSE_INVENTORY_OPERATION_TYPE_LIST.add(new StatusBean<Integer, String>(WIO_TYPE_PUT_ON, "上架"));
		WAREHOUSE_INVENTORY_OPERATION_TYPE_LIST
				.add(new StatusBean<Integer, String>(WIO_TYPE_OVERAGE, "盘盈"));
		WAREHOUSE_INVENTORY_OPERATION_TYPE_LIST.add(new StatusBean<Integer, String>(WIO_TYPE_NORMAL, "次转正"));
		WAREHOUSE_INVENTORY_OPERATION_TYPE_LIST.add(new StatusBean<Integer, String>(WIO_TYPE_STOCK_OUT, "出库"));
		WAREHOUSE_INVENTORY_OPERATION_TYPE_LIST.add(new StatusBean<Integer, String>(WIO_TYPE_LOSS, "盘亏"));
		WAREHOUSE_INVENTORY_OPERATION_TYPE_LIST.add(new StatusBean<Integer, String>(WIO_TYPE_DEFECT, "报次"));
		WAREHOUSE_INVENTORY_OPERATION_TYPE_LIST.add(new StatusBean<Integer, String>(WIO_TYPE_DAMAGE, "报损"));
	}

	private static void initWarehouseInventoryOperationTypeMap() {
		WAREHOUSE_INVENTORY_OPERATION_TYPE_MAP = StatusKit.toMap(WAREHOUSE_INVENTORY_OPERATION_TYPE_LIST);
	}
	
	private static void initWarehouseInventoryTypeList() {
		WAREHOUSE_INVENTORY_TYPE_LIST = new ArrayList<StatusBean<Integer, String>>();
		WAREHOUSE_INVENTORY_TYPE_LIST.add(new StatusBean<Integer, String>(WIT_NORMAL, "正品"));
		WAREHOUSE_INVENTORY_TYPE_LIST.add(new StatusBean<Integer, String>(WIT_DEFECT, "次品"));
		WAREHOUSE_INVENTORY_TYPE_LIST.add(new StatusBean<Integer, String>(WIT_DAMAGE, "损坏"));
	}

	private static void initWarehouseInventoryTypeMap() {
		WAREHOUSE_INVENTORY_TYPE_MAP = StatusKit.toMap(WAREHOUSE_INVENTORY_TYPE_LIST);
	}
}
