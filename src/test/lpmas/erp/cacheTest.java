package test.lpmas.erp;

import org.junit.Test;

import com.lpmas.erp.cache.WarehouseInfoCache;
import com.lpmas.erp.warehouse.bean.WarehouseInfoBean;

public class cacheTest {
	@Test
	public void testWarehouseTransferOrderInfoCache() {
		WarehouseInfoCache WarehouseInfoCache = new WarehouseInfoCache();
		WarehouseInfoBean result = WarehouseInfoCache.getWarehouseInfoByKey(5);
		System.out.println(result.getWarehouseName());
	}
}
