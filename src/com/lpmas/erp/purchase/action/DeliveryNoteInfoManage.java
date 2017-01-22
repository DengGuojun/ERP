package com.lpmas.erp.purchase.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.erp.purchase.bean.DeliveryNoteInfoBean;
import com.lpmas.erp.purchase.bean.DeliveryNoteItemBean;
import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.bean.PurchaseOrderInfoEntityBean;
import com.lpmas.erp.purchase.bean.PurchaseOrderItemBean;
import com.lpmas.erp.purchase.business.DeliveryNoteInfoBusiness;
import com.lpmas.erp.purchase.business.DeliveryNoteItemBusiness;
import com.lpmas.erp.purchase.business.PurchaseOrderInfoBusiness;
import com.lpmas.erp.purchase.business.PurchaseOrderItemBusiness;
import com.lpmas.erp.purchase.business.PurchaseOrderStatusProcessor;
import com.lpmas.erp.purchase.config.PurchaseOrderConfig;
import com.lpmas.erp.purchase.config.PurchaseOrderConsoleConfig;
import com.lpmas.erp.purchase.config.PurchaseOrderResource;
import com.lpmas.erp.purchase.util.DeliveryNoteNumberGenerator;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.DateKit;
import com.lpmas.framework.util.ListKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.srm.bean.SupplierInfoBean;
import com.lpmas.srm.client.SrmServiceClient;
import com.lpmas.tms.transporter.business.TransporterInfoMediator;

/**
 * Servlet implementation class DeliveryNoteInfoManage
 */
@WebServlet("/erp/DeliveryNoteInfoManage.do")
public class DeliveryNoteInfoManage extends HttpServlet {

	private static Logger log = LoggerFactory.getLogger(DeliveryNoteInfoManage.class);

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeliveryNoteInfoManage() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		boolean readOnly = ParamKit.getBooleanParameter(request, "readOnly", false);
		if (!readOnly && !adminUserHelper.checkPermission(PurchaseOrderResource.PURCHASE_ORDER_INFO,
				OperationConfig.UPDATE)) {
			return;
		}
		if (readOnly && !adminUserHelper.checkPermission(PurchaseOrderResource.PURCHASE_ORDER_INFO,
				OperationConfig.SEARCH)) {
			return;
		}

		// 获取页面请求参数
		int dnId = ParamKit.getIntParameter(request, "dnId", 0);
		int poId = ParamKit.getIntParameter(request, "poId", 0);
		if (poId == 0) {
			HttpResponseKit.alertMessage(response, "采购单号缺失", HttpResponseKit.ACTION_WINDOW_CLOSE);
			return;
		}

		// 查出订单信息
		PurchaseOrderInfoBusiness purchaseOrderInfoBusiness = new PurchaseOrderInfoBusiness();
		PurchaseOrderInfoBean orderInfoBean = purchaseOrderInfoBusiness.getPurchaseOrderInfoByKey(poId);
		if (orderInfoBean.getStatus() == Constants.STATUS_NOT_VALID) {
			HttpResponseKit.alertMessage(response, "采购订单已被删除", HttpResponseKit.ACTION_WINDOW_CLOSE);
			return;
		}
		// 获取订单关联对象信息
		SrmServiceClient srmServiceClient = new SrmServiceClient();
		SupplierInfoBean supplierInfoBean = srmServiceClient.getSupplierInfoByKey(orderInfoBean.getSupplierId());
		PurchaseOrderInfoEntityBean purchaseOrderInfoEntityBean = new PurchaseOrderInfoEntityBean(orderInfoBean, null,
				supplierInfoBean, null, null);
		// 根据订单信息查出订单明细
		PurchaseOrderItemBusiness purchaseOrderItemBusiness = new PurchaseOrderItemBusiness();
		List<PurchaseOrderItemBean> OrderItemList = purchaseOrderItemBusiness.getPurchaseOrderItemListByPoId(poId);
		DeliveryNoteInfoBean deliveryInfoBean = new DeliveryNoteInfoBean();
		if (dnId > 0) {
			// 查出发货单的INFO
			DeliveryNoteInfoBusiness deliveryNoteInfoBusiness = new DeliveryNoteInfoBusiness();
			deliveryInfoBean = deliveryNoteInfoBusiness.getDeliveryNoteInfoByKey(dnId);
			// 查出发货单明细
			DeliveryNoteItemBusiness deliveryNoteItemBusiness = new DeliveryNoteItemBusiness();
			List<DeliveryNoteItemBean> deliveryNoteItemList = deliveryNoteItemBusiness
					.getDeliveryNoteItemListByDnId(dnId);
			// 转换成MAP
			Map<Integer, DeliveryNoteItemBean> deliveryNoteItemMap = ListKit.list2Map(deliveryNoteItemList, "wareId");

			// 放入request
			request.setAttribute("DeliveryNoteItemList", deliveryNoteItemList);
			request.setAttribute("DeliveryNoteItemMap", deliveryNoteItemMap);
		}
		request.setAttribute("DeliveryInfoBean", deliveryInfoBean);
		request.setAttribute("OrderInfoEntityBean", purchaseOrderInfoEntityBean);
		request.setAttribute("OrderItemList", OrderItemList);
		
		TransporterInfoMediator transporterInfoMediator = new TransporterInfoMediator();
		request.setAttribute("TransporterInfoMediator", transporterInfoMediator);

		// 请求转发
		RequestDispatcher rd = request.getRequestDispatcher(
				PurchaseOrderConsoleConfig.PURCHASE_ORDER_PAGE_PATH + "DeliveryNoteInfoManage.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		if (!adminUserHelper.checkPermission(PurchaseOrderResource.PURCHASE_ORDER_INFO, OperationConfig.UPDATE)) {
			return;
		}
		Timestamp deliveryTime = null;
		try {
			DeliveryNoteNumberGenerator.lock.lock();
			PurchaseOrderStatusProcessor.statusLock.lock();
			// 获取页面请求参数
			int dnId = ParamKit.getIntParameter(request, "dnId", 0);
			int poId = ParamKit.getIntParameter(request, "poId", 0);
			if (poId == 0) {
				HttpResponseKit.alertMessage(response, "采购单号缺失!", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			String deliveryTimeStr = ParamKit.getParameter(request, "deliveryTime", "");
			if (!StringKit.isValid(deliveryTimeStr)) {
				HttpResponseKit.alertMessage(response, "发货日期缺失!", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			} else {
				try {
					deliveryTime = DateKit.str2Timestamp(deliveryTimeStr, DateKit.DEFAULT_DATE_TIME_FORMAT);
				} catch (Exception e) {
					HttpResponseKit.alertMessage(response, "发货日期格式错误!", HttpResponseKit.ACTION_HISTORY_BACK);
					return;
				}
			}
			int transporterType = ParamKit.getIntParameter(request, "transporterType", 0);
			if (transporterType == 0) {
				HttpResponseKit.alertMessage(response, "配送方类型缺失", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			int transporterId = ParamKit.getIntParameter(request, "transporterId", 0);
			if (transporterId == 0) {
				HttpResponseKit.alertMessage(response, "配送方缺失", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			// 查出对应的采购单
			PurchaseOrderInfoBusiness purchaseOrderInfoBusiness = new PurchaseOrderInfoBusiness();
			PurchaseOrderInfoBean purchaseOrderInfoBean = purchaseOrderInfoBusiness.getPurchaseOrderInfoByKey(poId);
			if (purchaseOrderInfoBean.getStatus() == Constants.STATUS_NOT_VALID) {
				HttpResponseKit.alertMessage(response, "采购订单已被删除", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			// 判断发货日期是否早于采购单建立日期
			if (!purchaseOrderInfoBean.getCreateTime().before(deliveryTime)) {
				HttpResponseKit.alertMessage(response, "发货时间不得早于采购单建立时间", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			PurchaseOrderStatusProcessor poStatusBusiness = new PurchaseOrderStatusProcessor(purchaseOrderInfoBean);
			if (!poStatusBusiness.receivable()) {
				HttpResponseKit.alertMessage(response, "采购订单必须在等待收货的状态才能新建发货单", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			// 根据PO单查出对应的采购明细
			PurchaseOrderItemBusiness purchaseOrderItemBusiness = new PurchaseOrderItemBusiness();
			List<PurchaseOrderItemBean> purchaseOrderItemList = purchaseOrderItemBusiness
					.getPurchaseOrderItemListByPoId(poId);

			DeliveryNoteInfoBean bean = BeanKit.request2Bean(request, DeliveryNoteInfoBean.class);
			DeliveryNoteInfoBusiness deliveryNoteInfoBusiness = new DeliveryNoteInfoBusiness();
			int result = 0;
			if (dnId == 0) {
				// 新建采购单
				String dnNumber = DeliveryNoteNumberGenerator.generateDeliveryNoteNumber(purchaseOrderInfoBean);
				// 设值
				bean.setDnNumber(dnNumber);
				bean.setCreateUser(adminUserHelper.getAdminUserId());
				bean.setReceivingStatus(PurchaseOrderConfig.RECEIVING_STATUS_WAIT_RECEIVE);
				bean.setStatus(Constants.STATUS_VALID);
				result = deliveryNoteInfoBusiness.addDeliveryNoteInfo(bean);
				dnId = result;
			} else {
				bean.setModifyUser(adminUserHelper.getAdminUserId());
				bean.setStatus(Constants.STATUS_VALID);
				result = deliveryNoteInfoBusiness.updateDeliveryNoteInfo(bean);
			}

			// 新增或者修改INFOBEAN成功时
			if (result > 0) {
				// 根据当前采购明细的WAREID获得请求参数
				// 并组装成Map
				Double deliveryQuantiry = null;
				String memo = null;
				DeliveryNoteItemBean dnBean = null;
				Map<PurchaseOrderItemBean, DeliveryNoteItemBean> qualifyDnItemMap = new HashMap<PurchaseOrderItemBean, DeliveryNoteItemBean>();
				for (PurchaseOrderItemBean poItemBean : purchaseOrderItemList) {
					deliveryQuantiry = ParamKit.getDoubleParameter(request,
							"deliveryQuantiry_" + poItemBean.getWareId(), 0);
					memo = ParamKit.getParameter(request, "memo_" + poItemBean.getWareId(), "");
					dnBean = new DeliveryNoteItemBean();
					dnBean.setDnId(dnId);
					dnBean.setWareId(poItemBean.getWareId());
					dnBean.setWareType(poItemBean.getWareType());
					dnBean.setWareNumber(poItemBean.getWareNumber());
					dnBean.setUnit(poItemBean.getUnit());
					dnBean.setDeliveryQuantiry(deliveryQuantiry);
					dnBean.setStatus(Constants.STATUS_VALID);
					dnBean.setMemo(memo);
					qualifyDnItemMap.put(poItemBean, dnBean);
				}
				// 查询是否存在这个ITEM
				// 存在就更新，不存在
				DeliveryNoteItemBusiness deliveryNoteItemBusiness = new DeliveryNoteItemBusiness();
				// 查出存在在DNITEM并且转换成MAP,KEY是WAREID
				List<DeliveryNoteItemBean> existDnItemList = deliveryNoteItemBusiness
						.getDeliveryNoteItemListByDnId(dnId);
				Map<Integer, DeliveryNoteItemBean> existDnItemMap = ListKit.list2Map(existDnItemList, "wareId");

				// 遍历插入或修改
				DeliveryNoteItemBean existNoteItemBean = null;
				for (Entry<PurchaseOrderItemBean, DeliveryNoteItemBean> entry : qualifyDnItemMap.entrySet()) {
					DeliveryNoteItemBean tempNoteItemBean = entry.getValue();
					int wareId = tempNoteItemBean.getWareId();
					existNoteItemBean = existDnItemMap.get(wareId);
					if (existNoteItemBean != null) {
						// 存在就更新
						existNoteItemBean.setDeliveryQuantiry(tempNoteItemBean.getDeliveryQuantiry());
						existNoteItemBean.setMemo(tempNoteItemBean.getMemo());
						existNoteItemBean.setModifyUser(adminUserHelper.getAdminUserId());
						result = deliveryNoteItemBusiness.updateDeliveryNoteItem(existNoteItemBean);
					} else {
						// 不存在就新增
						// 当且仅当收货数量大于0才新增
						if (tempNoteItemBean.getDeliveryQuantiry() > new Double("0")) {
							tempNoteItemBean.setCreateUser(adminUserHelper.getAdminUserId());
							result = deliveryNoteItemBusiness.addDeliveryNoteItem(tempNoteItemBean);
						}
					}
				}
				if (result > 0) {
					PrintWriter writer = response.getWriter();
					String reflashParent = "<script>window.opener.location.reload(true)</script>";
					writer.write(reflashParent);
					writer.close();
					HttpResponseKit.alertMessage(response, "操作成功!", HttpResponseKit.ACTION_WINDOW_CLOSE);
					return;
				} else {
					HttpResponseKit.alertMessage(response, "操作失败!", HttpResponseKit.ACTION_WINDOW_CLOSE);
					return;
				}
			} else {
				// 新增或者修改INFOBEAN失败时
				HttpResponseKit.alertMessage(response, "操作失败!", HttpResponseKit.ACTION_WINDOW_CLOSE);
				return;
			}
		} catch (Exception e) {
			log.error("", e);
		} finally {
			DeliveryNoteNumberGenerator.lock.unlock();
			PurchaseOrderStatusProcessor.statusLock.unlock();
		}
	}

}
