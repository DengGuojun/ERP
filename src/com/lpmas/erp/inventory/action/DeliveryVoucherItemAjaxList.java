package com.lpmas.erp.inventory.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lpmas.erp.inventory.business.DeliveryVoucherItemDisplayUtil;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;

/**
 * Servlet implementation class DeliveryVoucherItemAjaxList
 */
@WebServlet("/erp/DeliveryVoucherItemAjaxList.do")
public class DeliveryVoucherItemAjaxList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeliveryVoucherItemAjaxList() {
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
		int sourceType = ParamKit.getIntParameter(request, "sourceType", 0);
		int sourceOrderId = ParamKit.getIntParameter(request, "sourceOrderId", 0);
		int wareType = ParamKit.getIntParameter(request, "wareType", 0);
		int dvId = ParamKit.getIntParameter(request, "dvId", 0);
		Boolean isModify = !ParamKit.getBooleanParameter(request, "readOnly", true);

		/*response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter writer = response.getWriter();
		writer.write(DeliveryVoucherItemDisplayUtil.getDeliveryVoucherItemDisplayStr(wareType, dvId, sourceType, sourceOrderId, isModify));
		writer.flush();
		writer.close();*/

		HttpResponseKit.printMessage(response,
				DeliveryVoucherItemDisplayUtil.getDeliveryVoucherItemDisplayStr(wareType, dvId, sourceType, sourceOrderId, isModify));
	}

}
