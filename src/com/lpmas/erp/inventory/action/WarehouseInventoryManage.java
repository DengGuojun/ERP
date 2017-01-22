package com.lpmas.erp.inventory.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.erp.inventory.bean.WarehouseInventoryReportBean;
import com.lpmas.erp.inventory.bean.WarehouseVoucherInfoBean;
import com.lpmas.erp.inventory.business.WarehouseInventoryReportBusiness;
import com.lpmas.erp.inventory.business.WarehouseVoucherInfoBusiness;
import com.lpmas.erp.inventory.business.WarehouseVoucherStatusProcessor;
import com.lpmas.erp.inventory.config.InventoryResource;
import com.lpmas.erp.inventory.config.WarehouseInventoryConfig;
import com.lpmas.erp.inventory.handler.WarehouseInventoryHandler;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;

/**
 * Servlet implementation class WarehouseInventoryManage
 */
@WebServlet("/erp/WarehouseInventoryManage.do")
public class WarehouseInventoryManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(WarehouseInventoryManage.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WarehouseInventoryManage() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_INVENTORY, OperationConfig.UPDATE)) {
			return;
		}
		int statusAction = ParamKit.getIntParameter(request, "statusAction", 0);
		String reportId = ParamKit.getParameter(request, "reportId", "");
		double quantity = ParamKit.getDoubleParameter(request, "quantity", 0);
		if (statusAction == WarehouseInventoryConfig.WIO_TYPE_PUT_ON) {
			// 判断是否具有入库单操作权限
			if (!adminUserHelper.checkPermission(InventoryResource.WAREHOUSE_VOUCHER_INFO, OperationConfig.UPDATE)) {
				return;
			}
		}
		try {
			// 获得锁
			WarehouseVoucherStatusProcessor.statusLock.lock();

			boolean success = false;
			if (statusAction == WarehouseInventoryConfig.WIO_TYPE_PUT_ON) {
				// 库存上架，触发对应入库单的上架操作
				int wvId = ParamKit.getIntParameter(request, "wvId", 0);
				WarehouseVoucherInfoBusiness business = new WarehouseVoucherInfoBusiness();
				WarehouseVoucherInfoBean bean = business.getWarehouseVoucherInfoByKey(wvId);
				bean.setModifyUser(adminUserHelper.getAdminUserId());
				WarehouseVoucherStatusProcessor statusBusiness = new WarehouseVoucherStatusProcessor(bean);
				if (statusBusiness.closable()) {
					statusBusiness.close();
					success = true;
				}
			} else {
				if (quantity <= 0) {
					HttpResponseKit.alertMessage(response, "库存操作数量不合法", HttpResponseKit.ACTION_HISTORY_BACK);
					return;
				}
				WarehouseInventoryHandler handler = new WarehouseInventoryHandler();
				WarehouseInventoryReportBusiness business = new WarehouseInventoryReportBusiness();
				WarehouseInventoryReportBean bean = business.getWarehouseInventoryReportByKey(reportId);
				// 检验REPORTID正确性
				if (bean == null) {
					HttpResponseKit.alertMessage(response, "reportId不合法", HttpResponseKit.ACTION_HISTORY_BACK);
					return;
				}
				if (statusAction == WarehouseInventoryConfig.WIO_TYPE_DEFECT || statusAction == WarehouseInventoryConfig.WIO_TYPE_DAMAGE
						|| statusAction == WarehouseInventoryConfig.WIO_TYPE_LOSS) {
					// 库存报次、报损、盘亏时需要扣减正品库存
					if (bean.getNormalQuantity() < quantity) {
						HttpResponseKit.alertMessage(response, "输入的数量大于实库库存数量", HttpResponseKit.ACTION_HISTORY_BACK);
						return;
					}
					handler.subtractWarehouseInventory(bean, quantity, statusAction, adminUserHelper.getAdminUserId());
					success = true;
				} else if (statusAction == WarehouseInventoryConfig.WIO_TYPE_NORMAL || statusAction == WarehouseInventoryConfig.WIO_TYPE_OVERAGE) {
					if (statusAction == WarehouseInventoryConfig.WIO_TYPE_NORMAL && bean.getDefectQuantity() < quantity) {
						HttpResponseKit.alertMessage(response, "输入的数量大于残次品库存数量", HttpResponseKit.ACTION_HISTORY_BACK);
						return;
					}
					// 次转正、库存盘盈时需要增加正品库存
					handler.addWarehouseInventory(bean, quantity, statusAction, adminUserHelper.getAdminUserId());
					success = true;
				} else {
					HttpResponseKit.alertMessage(response, "库存操作代码错误", HttpResponseKit.ACTION_HISTORY_BACK);
					return;
				}
			}
			if (success) {
				HttpResponseKit.alertMessage(response, "处理成功", "/erp/WarehouseInventoryReportList.do");
			} else {
				HttpResponseKit.alertMessage(response, "操作失败！您的页面已过期，请刷新页面重新操作", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
		} catch (Exception e) {
			log.error("", e);
			HttpResponseKit.alertMessage(response, "操作失败！", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		} finally {
			// 释放锁
			WarehouseVoucherStatusProcessor.statusLock.unlock();
		}
	}
}
