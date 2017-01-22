package com.lpmas.erp.inventory.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lpmas.erp.purchase.util.DetailDisplayUtil;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;

/**
 * Servlet implementation class WarehouseVoucherDetailDisplay
 */
@WebServlet("/erp/WarehouseVoucherItemAjaxList.do")
public class WarehouseVoucherItemAjaxList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WarehouseVoucherItemAjaxList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int sourceType = ParamKit.getIntParameter(request, "sourceType", 0);
		int sourceOrderId = ParamKit.getIntParameter(request, "sourceOrderId", 0);
		int wareType = ParamKit.getIntParameter(request, "wareType", 0);
		int wvId = ParamKit.getIntParameter(request, "wvId", 0);
		Boolean isModify = !ParamKit.getBooleanParameter(request, "readOnly", true);
		
		/*response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter writer = response.getWriter();
		writer.write(DetailDisplayUtil.getWvDetailDispalyStr(wareType, wvId, sourceType, sourceOrderId, isModify));
		writer.flush();
		writer.close();*/
		
		HttpResponseKit.printMessage(response,DetailDisplayUtil.getWvDetailDispalyStr(wareType, wvId, sourceType, sourceOrderId, isModify));
	}

}
