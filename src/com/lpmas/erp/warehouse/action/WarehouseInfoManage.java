package com.lpmas.erp.warehouse.action;

import java.io.IOException;
import java.util.List;

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
import com.lpmas.erp.config.ErpConsoleConfig;
import com.lpmas.erp.warehouse.bean.WarehouseInfoBean;
import com.lpmas.erp.warehouse.bean.WarehousePropertyBean;
import com.lpmas.erp.warehouse.business.WarehouseInfoBusiness;
import com.lpmas.erp.warehouse.business.WarehousePropertyBusiness;
import com.lpmas.erp.warehouse.config.WarehouseConsoleConfig;
import com.lpmas.erp.warehouse.config.WarehousePropertyConfig;
import com.lpmas.erp.warehouse.config.WarehouseResource;
import com.lpmas.framework.config.Constants;
import com.lpmas.framework.util.BeanKit;
import com.lpmas.framework.util.StringKit;
import com.lpmas.framework.web.HttpResponseKit;
import com.lpmas.framework.web.ParamKit;
import com.lpmas.framework.web.ReturnMessageBean;
import com.lpmas.region.bean.CountryInfoBean;
import com.lpmas.region.client.RegionServiceClient;

/**
 * Servlet implementation class WarehouseInfoListManage
 */
@WebServlet("/erp/WarehouseInfoManage.do")
public class WarehouseInfoManage extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(WarehouseInfoManage.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WarehouseInfoManage() {
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
		int warehouseId = ParamKit.getIntParameter(request, "warehouseId", 0);
		WarehouseInfoBusiness warehouseInfoBusiness = new WarehouseInfoBusiness();
		WarehouseInfoBean warehouseInfoBean = null;
		WarehousePropertyBusiness warehousePropertyBusiness = new WarehousePropertyBusiness();
		WarehousePropertyBean warehousePropertyBean = null;
		if (warehouseId > 0) {
			if (!readOnly
					&& !adminUserHelper.checkPermission(WarehouseResource.WAREHOUSE_INFO, OperationConfig.UPDATE)) {
				return;
			}
			if (readOnly
					&& !adminUserHelper.checkPermission(WarehouseResource.WAREHOUSE_INFO, OperationConfig.SEARCH)) {
				return;
			}
			warehouseInfoBean = warehouseInfoBusiness.getWarehouseInfoByKey(warehouseId);
			warehousePropertyBean = warehousePropertyBusiness.getWarehousePropertyByKey(warehouseId,
					WarehousePropertyConfig.PROPERTY_DELIVERY_TO_CUSTOMER);
			if (warehousePropertyBean == null) {
				warehousePropertyBean = new WarehousePropertyBean();
				warehousePropertyBean.setPropertyValue(String.valueOf(Constants.STATUS_NOT_VALID));
			}
		} else {
			if (!adminUserHelper.checkPermission(WarehouseResource.WAREHOUSE_INFO, OperationConfig.CREATE)) {
				return;
			}
			warehouseInfoBean = new WarehouseInfoBean();
			warehouseInfoBean.setStatus(Constants.STATUS_VALID);
			warehousePropertyBean = new WarehousePropertyBean();
			warehousePropertyBean.setPropertyValue(String.valueOf(Constants.STATUS_NOT_VALID));
		}

		// 获取国家ID
		RegionServiceClient client = new RegionServiceClient();
		List<CountryInfoBean> countryList = client.getCountryInfoAllList();
		CountryInfoBean countryBeanChina = new CountryInfoBean();
		for (CountryInfoBean country : countryList) {
			if (country.getCountryCode().equals(ErpConsoleConfig.REGION_COUNTRY_CODE_CHINA)) {
				countryBeanChina = country;
				break;
			}
		}
		request.setAttribute("CountryName", countryBeanChina.getCountryName());
		request.setAttribute("WarehouseInfoBean", warehouseInfoBean);
		request.setAttribute("WarehousePropertyBean", warehousePropertyBean);
		request.setAttribute("AdminUserHelper", adminUserHelper);
		// 请求转发
		RequestDispatcher rd = request
				.getRequestDispatcher(WarehouseConsoleConfig.WAREHOUSE_PAGE_PATH + "WarehouseInfoManage.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int warehouseId = ParamKit.getIntParameter(request, "warehouseId", 0);
		AdminUserHelper adminUserHelper = new AdminUserHelper(request, response);
		try {
			WarehouseInfoBusiness warehouseInfoBusiness = new WarehouseInfoBusiness();
			WarehouseInfoBean warehouseInfoBean = BeanKit.request2Bean(request, WarehouseInfoBean.class);
			ReturnMessageBean messageBean = warehouseInfoBusiness.verifyWarehouseInfo(warehouseInfoBean);
			if (StringKit.isValid(messageBean.getMessage())) {
				HttpResponseKit.alertMessage(response, messageBean.getMessage(), HttpResponseKit.ACTION_HISTORY_BACK);
				return;
			}

			int result = 0;
			boolean propertyResult = true;
			if (warehouseId > 0) {
				// 修改
				if (!adminUserHelper.checkPermission(WarehouseResource.WAREHOUSE_INFO, OperationConfig.UPDATE)) {
					return;
				}
				warehouseInfoBean.setModifyUser(adminUserHelper.getAdminUserId());
				result = warehouseInfoBusiness.updateWarehouseInfo(warehouseInfoBean);
			} else {
				// 新增
				if (!adminUserHelper.checkPermission(WarehouseResource.WAREHOUSE_INFO, OperationConfig.CREATE)) {
					return;
				}
				warehouseInfoBean.setCreateUser(adminUserHelper.getAdminUserId());
				result = warehouseInfoBusiness.addWarehouseInfo(warehouseInfoBean);
				warehouseInfoBean.setWarehouseId(result);
			}
			if (result > 0) {
				// 遍历其属性
				for (String warehouseProperty : WarehousePropertyConfig.WAREHOUSE_PROPERTY_CONFIG_MAP.keySet()) {
					int resultProperty = 0;
					String propertyValue = ParamKit.getParameter(request,
							WarehousePropertyConfig.WAREHOUSE_PROPERTY_CONFIG_MAP.get(warehouseProperty), "");
					WarehousePropertyBusiness warehousePropertyBusiness = new WarehousePropertyBusiness();
					WarehousePropertyBean warehousePropertyBean = warehousePropertyBusiness
							.getWarehousePropertyByKey(warehouseInfoBean.getWarehouseId(), warehouseProperty);
					if (!StringKit.isValid(propertyValue)) {
						propertyValue = String.valueOf(Constants.STATUS_NOT_VALID);
					}
					if (warehousePropertyBean == null) {
						warehousePropertyBean = new WarehousePropertyBean();
						warehousePropertyBean.setWarehouseId(warehouseInfoBean.getWarehouseId());
						warehousePropertyBean.setPropertyCode(warehouseProperty);
						warehousePropertyBean.setPropertyValue(propertyValue);
						warehousePropertyBean.setCreateUser(adminUserHelper.getAdminUserId());
						resultProperty = warehousePropertyBusiness.addWarehouseProperty(warehousePropertyBean);
					} else {
						warehousePropertyBean.setPropertyValue(propertyValue);
						warehousePropertyBean.setModifyUser(adminUserHelper.getAdminUserId());
						resultProperty = warehousePropertyBusiness.updateWarehouseProperty(warehousePropertyBean);
					}
					if (resultProperty < 0) {
						propertyResult = false;
					}
				}
			}

			if (result > 0 && propertyResult) {
				HttpResponseKit.alertMessage(response, "操作成功", "/erp/WarehouseInfoList.do");
			} else {
				HttpResponseKit.alertMessage(response, "操作失败", HttpResponseKit.ACTION_HISTORY_BACK);
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

}
