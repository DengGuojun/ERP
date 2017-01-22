package com.lpmas.erp.purchase.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lpmas.admin.business.AdminUserHelper;
import com.lpmas.admin.config.OperationConfig;
import com.lpmas.erp.purchase.bean.DeliveryNoteInfoBean;
import com.lpmas.erp.purchase.bean.DeliveryNoteItemBean;
import com.lpmas.erp.purchase.bean.PurchaseOrderInfoBean;
import com.lpmas.erp.purchase.bean.ReceivingNoteInfoBean;
import com.lpmas.erp.purchase.bean.ReceivingNoteItemBean;
import com.lpmas.erp.purchase.business.DeliveryNoteInfoBusiness;
import com.lpmas.erp.purchase.business.DeliveryNoteItemBusiness;
import com.lpmas.erp.purchase.business.PurchaseOrderInfoBusiness;
import com.lpmas.erp.purchase.business.PurchaseOrderStatusProcessor;
import com.lpmas.erp.purchase.business.ReceivingNoteInfoBusiness;
import com.lpmas.erp.purchase.business.ReceivingNoteItemBusiness;
import com.lpmas.erp.purchase.config.PurchaseOrderConfig;
import com.lpmas.erp.purchase.config.PurchaseOrderConsoleConfig;
import com.lpmas.erp.purchase.config.PurchaseOrderResource;
import com.lpmas.erp.purchase.util.ReceivingNoteNumberGenerator;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.DateKit;
import com.lpmas.framework.util.ListKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;

/**
 * Servlet implementation class ReceivingNoteInfoManage
 */
@WebServlet("/erp/ReceivingNoteInfoManage.do")
public class ReceivingNoteInfoManage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReceivingNoteInfoManage() {
		super();
		// TODO Auto-generated constructor stub
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
		if (dnId == 0) {
			HttpResponseKit.alertMessage(response, "发货单号缺失", HttpResponseKit.ACTION_WINDOW_CLOSE);
			return;
		}
		// 根据DN ID 查出对应的发货单和发货单明细
		DeliveryNoteInfoBusiness deliveryNoteInfoBusiness = new DeliveryNoteInfoBusiness();
		DeliveryNoteItemBusiness deliveryNoteItemBusiness = new DeliveryNoteItemBusiness();
		List<DeliveryNoteItemBean> deliveryNoteItemList = deliveryNoteItemBusiness.getDeliveryNoteItemListByDnId(dnId);
		ReceivingNoteItemBusiness receivingNoteItemBusiness = new ReceivingNoteItemBusiness();
		// 发货单
		DeliveryNoteInfoBean deliveryNoteInfoBean = deliveryNoteInfoBusiness.getDeliveryNoteInfoByKey(dnId);

		// 查出对应的采购单
		PurchaseOrderInfoBusiness purchaseOrderInfoBusiness = new PurchaseOrderInfoBusiness();
		PurchaseOrderInfoBean purchaseOrderInfoBean = purchaseOrderInfoBusiness
				.getPurchaseOrderInfoByKey(deliveryNoteInfoBean.getPoId());
		if (purchaseOrderInfoBean.getStatus() == Constants.STATUS_NOT_VALID) {
			HttpResponseKit.alertMessage(response, "采购订单已被删除", HttpResponseKit.ACTION_WINDOW_CLOSE);
			return;
		}
		// 根据DNID查询对应的收货单
		ReceivingNoteInfoBusiness receivingNoteInfoBusiness = new ReceivingNoteInfoBusiness();
		ReceivingNoteInfoBean receivingNoteInfoBean = receivingNoteInfoBusiness.getReceivingNoteInfoByDnId(dnId);
		if (receivingNoteInfoBean == null) {
			receivingNoteInfoBean = new ReceivingNoteInfoBean();
		} else {
			// 如果当前不是READONLY
			if (!readOnly) {
				// 当存在时，检查明细
				ReceivingNoteItemBean receivingNoteItemBean = null;
				List<ReceivingNoteItemBean> receivingNoteItemList = receivingNoteItemBusiness
						.getReceivingNoteItemListByRnId(receivingNoteInfoBean.getRnId());
				Map<Integer, ReceivingNoteItemBean> receivingNoteItemMap = ListKit.list2Map(receivingNoteItemList,
						"dnItemId");
				for (DeliveryNoteItemBean bean : deliveryNoteItemList) {
					// 不存在时插入
					if (!receivingNoteItemMap.containsKey(bean.getDnItemId())) {
						receivingNoteItemBean = new ReceivingNoteItemBean();
						receivingNoteItemBean.setRnId(receivingNoteInfoBean.getRnId());
						receivingNoteItemBean.setDnItemId(bean.getDnItemId());
						receivingNoteItemBean.setStatus(Constants.STATUS_VALID);
						receivingNoteItemBean.setCreateUser(adminUserHelper.getAdminUserId());
						receivingNoteItemBusiness.addReceivingNoteItem(receivingNoteItemBean);
					}
				}
			}
		}
		// 把信息放在页面上
		request.setAttribute("DeliveryNoteInfoBean", deliveryNoteInfoBean);
		request.setAttribute("PurchaseOrderInfoBean", purchaseOrderInfoBean);
		request.setAttribute("ReceivingNoteInfoBean", receivingNoteInfoBean);

		// 请求转发
		RequestDispatcher rd = request.getRequestDispatcher(
				PurchaseOrderConsoleConfig.PURCHASE_ORDER_PAGE_PATH + "ReceivingNoteInfoManage.jsp");
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
		Timestamp receivingTime = null;
		// 获取页面请求参数
		int dnId = ParamKit.getIntParameter(request, "dnId", 0);
		if (dnId == 0) {
			HttpResponseKit.alertMessage(response, "采购单号缺失", HttpResponseKit.ACTION_WINDOW_CLOSE);
			return;
		}
		String receivingTimeStr = ParamKit.getParameter(request, "receivingTime", "");
		if (!StringKit.isValid(receivingTimeStr)) {
			HttpResponseKit.alertMessage(response, "收货日期缺失", HttpResponseKit.ACTION_HISTORY_BACK);
			return;
		} else {
			try {
				receivingTime = DateKit.str2Timestamp(receivingTimeStr, DateKit.DEFAULT_DATE_TIME_FORMAT);
			} catch (Exception e) {
				HttpResponseKit.alertMessage(response, "收货日期格式错误", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
		}

		int result = 0;
		try {
			PurchaseOrderStatusProcessor.statusLock.lock();

			// 修改对应发货单中的收货时间
			DeliveryNoteInfoBusiness deliveryNoteInfoBusiness = new DeliveryNoteInfoBusiness();
			DeliveryNoteInfoBean deliveryNoteInfoBean = deliveryNoteInfoBusiness.getDeliveryNoteInfoByKey(dnId);
			// 验证收货时间是否早于发货时间
			if (!deliveryNoteInfoBean.getDeliveryTime().before(receivingTime)) {
				HttpResponseKit.alertMessage(response, "收货时间不得早于发货时间", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			PurchaseOrderInfoBusiness purchaseOrderInfoBusiness = new PurchaseOrderInfoBusiness();
			PurchaseOrderInfoBean purchaseOrderInfoBean = purchaseOrderInfoBusiness
					.getPurchaseOrderInfoByKey(deliveryNoteInfoBean.getPoId());
			if (purchaseOrderInfoBean.getStatus() == Constants.STATUS_NOT_VALID) {
				HttpResponseKit.alertMessage(response, "采购订单已被删除", HttpResponseKit.ACTION_WINDOW_CLOSE);
				return;
			}
			PurchaseOrderStatusProcessor poStatusBusiness = new PurchaseOrderStatusProcessor(purchaseOrderInfoBean);
			if (!poStatusBusiness.receivable()) {
				HttpResponseKit.alertMessage(response, "采购订单必须在等待收货的状态才能新建收货单", HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}
			deliveryNoteInfoBean.setReceivingTime(receivingTime);
			deliveryNoteInfoBean.setModifyUser(adminUserHelper.getAdminUserId());
			result = deliveryNoteInfoBusiness.updateDeliveryNoteInfo(deliveryNoteInfoBean);

			DeliveryNoteItemBusiness deliveryNoteItemBusiness = new DeliveryNoteItemBusiness();
			List<DeliveryNoteItemBean> deliveryNoteItemList = deliveryNoteItemBusiness
					.getDeliveryNoteItemListByDnId(dnId);
			ReceivingNoteItemBusiness receivingNoteItemBusiness = new ReceivingNoteItemBusiness();
			ReceivingNoteItemBean receivingNoteItemBean = null;
			ReceivingNoteInfoBusiness receivingNoteInfoBusiness = new ReceivingNoteInfoBusiness();
			ReceivingNoteInfoBean receivingNoteInfoBean = receivingNoteInfoBusiness.getReceivingNoteInfoByDnId(dnId);
			if (receivingNoteInfoBean == null) {
				int rnId = 0;
				// 对应的收货单不存在就创建一个
				try {
					// 申请收货单号
					ReceivingNoteNumberGenerator.lock.lock();
					String receivingNumber = ReceivingNoteNumberGenerator
							.generateReceivingNoteNumber(deliveryNoteInfoBean);

					// 设值
					receivingNoteInfoBean = new ReceivingNoteInfoBean();
					receivingNoteInfoBean.setRnNumber(receivingNumber);
					receivingNoteInfoBean.setDnId(dnId);
					receivingNoteInfoBean.setPoId(deliveryNoteInfoBean.getPoId());
					receivingNoteInfoBean.setReceivingStatus(receivingNoteInfoBean.getReceivingStatus());
					receivingNoteInfoBean.setStatus(Constants.STATUS_VALID);
					receivingNoteInfoBean.setCreateUser(adminUserHelper.getAdminUserId());
					receivingNoteInfoBean.setReceivingTime(receivingTime);
					receivingNoteInfoBean.setReceivingStatus(PurchaseOrderConfig.RECEIVING_STATUS_RECEIVED);
					rnId = receivingNoteInfoBusiness.addReceivingNoteInfo(receivingNoteInfoBean);

					if (rnId > 0) {
						// 成功后更新发货单
						deliveryNoteInfoBean.setReceivingStatus(PurchaseOrderConfig.RECEIVING_STATUS_RECEIVED);
						deliveryNoteInfoBusiness.updateDeliveryNoteInfo(deliveryNoteInfoBean);
						result = 0;
						// 插入对应的收货明细
						for (DeliveryNoteItemBean bean : deliveryNoteItemList) {
							receivingNoteItemBean = new ReceivingNoteItemBean();
							receivingNoteItemBean.setRnId(rnId);
							receivingNoteItemBean.setDnItemId(bean.getDnItemId());
							receivingNoteItemBean.setStatus(Constants.STATUS_VALID);
							Double receiveQuantity = ParamKit.getDoubleParameter(request,
									"receiveQuantity_" + bean.getDnItemId(), 0);
							String memo = ParamKit.getParameter(request, "memo_" + bean.getDnItemId(), "");
							receivingNoteItemBean.setReceiveQuantity(Double.valueOf(receiveQuantity));
							receivingNoteItemBean.setMemo(memo);
							receivingNoteItemBean.setCreateUser(adminUserHelper.getAdminUserId());
							receivingNoteItemBusiness.addReceivingNoteItem(receivingNoteItemBean);
							if (result < 0) {
								HttpResponseKit.alertMessage(response, "收货明细建失败", HttpResponseKit.ACTION_WINDOW_CLOSE);
								return;
							} else {
								PrintWriter printWriter = response.getWriter();
								String reflashFather = "<script>window.opener.location.reload()</script>";
								printWriter.write(reflashFather);
								printWriter.close();
								HttpResponseKit.alertMessage(response, "操作成功!", HttpResponseKit.ACTION_WINDOW_CLOSE);
							}
						}
					} else {
						HttpResponseKit.alertMessage(response, "收货单创建失败", HttpResponseKit.ACTION_WINDOW_CLOSE);
						return;
					}
				} catch (Exception e) {
					HttpResponseKit.alertMessage(response, "收货单创建失败", HttpResponseKit.ACTION_WINDOW_CLOSE);
					return;
				} finally {
					ReceivingNoteNumberGenerator.lock.unlock();
				}
			} else {
				// 存在时修改这个采购单以及其明细
				// 修改BEAN对应的值
				receivingNoteInfoBean.setReceivingTime(receivingTime);
				receivingNoteInfoBean.setModifyUser(adminUserHelper.getAdminUserId());
				// 保存这个BEAN
				result = receivingNoteInfoBusiness.updateReceivingNoteInfo(receivingNoteInfoBean);
				if (result < 0) {
					HttpResponseKit.alertMessage(response, "操作失败!", HttpResponseKit.ACTION_HISTORY_BACK);
					return;
				} else {
					// 修改收货单明细信息
					List<ReceivingNoteItemBean> receivingNoteItemList = receivingNoteItemBusiness
							.getReceivingNoteItemListByRnId(receivingNoteInfoBean.getRnId());
					for (ReceivingNoteItemBean bean : receivingNoteItemList) {
						try {
							Double receiveQuantity = ParamKit.getDoubleParameter(request,
									"receiveQuantity_" + bean.getDnItemId(), 0);
							String memo = ParamKit.getParameter(request, "memo_" + bean.getDnItemId(), "");
							bean.setReceiveQuantity(Double.valueOf(receiveQuantity));
							bean.setMemo(memo);
							bean.setModifyUser(adminUserHelper.getAdminUserId());
						} catch (Exception e) {
							continue;
						}
						result = receivingNoteItemBusiness.updateReceivingNoteItem(bean);
					}
					if (result < 0) {
						HttpResponseKit.alertMessage(response, "操作失败!", HttpResponseKit.ACTION_HISTORY_BACK);
					} else {
						PrintWriter printWriter = response.getWriter();
						String reflashFather = "<script>window.opener.location.reload()</script>";
						printWriter.write(reflashFather);
						printWriter.close();
						HttpResponseKit.alertMessage(response, "操作成功!", HttpResponseKit.ACTION_WINDOW_CLOSE);
					}
				}
			}
		} finally {
			PurchaseOrderStatusProcessor.statusLock.unlock();
		}
	}

}
