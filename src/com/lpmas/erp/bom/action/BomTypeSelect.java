package com.lpmas.erp.bom.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lpmas.erp.bom.config.BomConfig;
import com.lpmas.erp.bom.config.BomConsoleConfig;

@WebServlet("/erp/BomTypeSelect.do")
public class BomTypeSelect extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BomTypeSelect() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setAttribute("bomTypeList", BomConfig.BOM_TYPE_LIST);
		RequestDispatcher rd = request.getRequestDispatcher(BomConsoleConfig.BOM_PAGE_PATH + "BomTypeSelect.jsp");
		rd.forward(request, response);
	}

}
