<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.lpmas.framework.config.*"%>
<%@ page import="com.lpmas.framework.util.*"%>
<%@ page import="com.lpmas.framework.bean.StatusBean"%>
<%@ page import="com.lpmas.admin.bean.*"%>
<%@ page import="com.lpmas.admin.business.*"%>
<%@ page import="com.lpmas.framework.web.*"%>
<%@ page import="com.lpmas.constant.info.*"%>
<%@ page import="com.lpmas.erp.warehouse.bean.*"%>
<%@ page import="com.lpmas.erp.warehouse.config.*"%>
<%@ include file="../../include/header.jsp"%>
<%
	WarehouseInfoBean bean = (WarehouseInfoBean)request.getAttribute("WarehouseInfoBean");
    WarehousePropertyBean warehousePropertyBean = (WarehousePropertyBean)request.getAttribute("WarehousePropertyBean");
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	int warehouseId = ParamKit.getIntParameter(request, "warehouseId", 0);
	String countryName = (String)request.getAttribute("CountryName");
	request.setAttribute("readOnly", readOnly);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>仓库管理</title>
<link href="<%=STATIC_URL%>/css/main.css" type="text/css"
	rel="stylesheet" />
<script type="text/javascript" src="../js/erp-common.js"></script>
<script type="text/javascript" src="<%=STATIC_URL%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=STATIC_URL%>/js/common.js"></script>
<script type="text/javascript" src="<%=STATIC_URL%>/js/ui.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.js"></script>
<link rel="stylesheet" href="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.css" type="text/css" media="screen" />
<script type="text/javascript"
	src="<%=STATIC_URL%>/js/My97DatePicker/WdatePicker.js"></script>
</head>
<body class="article_bg">
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a> 
		<ul class="art_nav">
			<li><a href="WarehouseInfoList.do">仓库列表</a>&nbsp;>&nbsp;</li>
			<%if(bean.getWarehouseId()==0){ %>
			<li>新建仓库</li>
			<%}else{ %>
			<li>修改仓库&nbsp;>&nbsp;</li>
			<li><%=bean.getWarehouseName() %></li>
			<%} %>
		</ul>
	</div> 
	<form id="formData" name="formData" method="post" action="WarehouseInfoManage.do?warehouseId=<%=warehouseId %>" onsubmit="javascript:return checkForm('formData');">
	  <div class="modify_form">
	  	<p>
  			<em class="int_label"><span>*</span>仓库名称：</em>
  			<input type="text"  name="warehouseName" id="warehouseName" size=50" maxLength="50" value="<%=bean.getWarehouseName()%>"checkStr="仓库名称;txt;true;;200"/>
  		</p>
  		<p>
  			<em class="int_label"><span>*</span>仓库编号：</em>
  			<input type="text"  name="warehouseNumber" id="warehouseNumber" size="50" maxLength="50" value="<%=bean.getWarehouseNumber()%>"checkStr="仓库编号;txt;true;;200"/>
  		</p>
  		<p>
  			<em class="int_label"><span>*</span>仓库类型：</em>
  			<select name="warehouseType" id="warehouseType">
	      		<%for(StatusBean<String, String> statusBean : WarehouseConfig.WAREHOUSE_TYPE_LIST){ %><option value="<%=statusBean.getStatus() %>" <%=(statusBean.getStatus().equals(bean.getWarehouseType()))?"selected":"" %>><%=statusBean.getValue() %></option><%} %>
	      	</select>
  		</p>
  		<p>
  			<em class="int_label"><span>*</span>国家：</em>
  			<input readOnly  name="country" id="country" value="<%=countryName%>"/>
  		</p>
  		<p>
  			<em class="int_label"><span>*</span>省份：</em>
  			<select id="province" name="province" onchange="$('#region').empty();getCityNameList('province','city','')" checkStr="省份;txt;true;;200"></select>
     	   <input type="hidden" id="provinceDummy" value="<%=bean.getProvince()%>"/>
  		</p>
  		<p>
  			<em class="int_label"><span>*</span>城市：</em>
  			<select id="city" name="city" onchange="getRegionNameList('city','region','')" checkStr="城市;txt;true;;200"></select>
     	  <input type="hidden" id="cityDummy" value="<%=bean.getCity()%>"/>
  		</p>
  		<p>
  			<em class="int_label"><span>*</span>地区：</em>
  			<select id="region" name="region" checkStr="地区;txt;true;;200"></select>
  		</p>
  		<p>
  			<em class="int_label"><span>*</span>地址：</em>
  			<input type="text"  name="address" id="address" size="100" maxLength="100" value="<%=bean.getAddress()%>" checkStr="地址;txt;true;;100"/>
  		</p>
  		<p>
  			<em class="int_label">邮编：</em>
  			<input type="text"  name="zipCode" id="zipCode" size="6" maxLength="6" value="<%=bean.getZipCode()%>" checkStr="邮编;digit;false;;6" onkeyup="value=this.value.replace(/\D+/g,'')"/>
  		</p>
  		<p>
  			<em class="int_label"><span>*</span>联系人：</em>
  			<input type="text"  name="contactName" id="contactName" size="20" maxLength="20" value="<%=bean.getContactName()%>" checkStr="联系人;txt;true;;20"/>
  		</p>
  		<p>
  			<em class="int_label"><span>*</span>电话：</em>
  			<input type="text"  name="telephone" id="telephone" size="20" maxLength="20" value="<%=bean.getTelephone()%>" checkStr="电话;digit;false;;20" onkeyup="value=this.value.replace(/\D+/g,'')"/>
  		</p>
  		<p>
  			<em class="int_label"><span>*</span>手机：</em>
  			<input type="text"  name="mobile" id="mobile" size="20" maxLength="20" value="<%=bean.getMobile()%>" checkStr="手机;digit;false;;20" onkeyup="value=this.value.replace(/\D+/g,'')"/>
  		</p>
        <p>
  			<em class="int_label">是否向客户发货：</em>
  			<input type="checkbox" name="deliveryToCustomer" id="deliveryToCustomer" value="<%=Constants.STATUS_VALID %>" <%=(warehousePropertyBean.getPropertyValue().equals(String.valueOf(Constants.STATUS_VALID)))?"checked":"" %>/>
  		</p>
  		<p>
  			<em class="int_label">状态：</em>
  			<input type="checkbox" name="status" id="status" value="<%=Constants.STATUS_VALID %>" <%=(bean.getStatus()==Constants.STATUS_VALID)?"checked":"" %>/>
  		</p>
		</div>
	   <div class="div_center">
	  <input type="submit" name="submit" id="submit" class="modifysubbtn" value="提交" />
	  <input type="button" name="cancel" id="cancel" value="取消" onclick="javascript:history.back()">
	  </div>
	</form>
</body>
<script>
$(document).ready(function() {
	var readOnly = '${readOnly}';
	if(readOnly=='true') {
		disablePageElement();
	}
	//获区省市区
		var url='<%=REGION_URL%>/region/RegionAjaxList.do';
		$.getScript(url,function(data){
			getProvinceNameList('country','province','<%=bean.getProvince()%>');
			getCityNameList('provinceDummy','city','<%=bean.getCity()%>');
			getRegionNameList('cityDummy','region','<%=bean.getRegion()%>');
		});
});
</script>
</html>