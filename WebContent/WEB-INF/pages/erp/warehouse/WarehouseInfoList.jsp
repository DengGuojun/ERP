<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.lpmas.erp.config.*"%>
<%@page import="com.lpmas.admin.client.cache.AdminUserInfoClientCache"%>
<%@page import="com.lpmas.system.bean.SysApplicationInfoBean"%>
<%@page import="com.lpmas.log.bean.DataLogBean"%>
<%@ page import="java.util.*"%>
<%@ page import="com.lpmas.framework.config.*"%>
<%@ page import="com.lpmas.framework.bean.*"%>
<%@ page import="com.lpmas.framework.page.*"%>
<%@ page import="com.lpmas.framework.util.*"%>
<%@ page import="com.lpmas.framework.web.*"%>
<%@ page import="com.lpmas.admin.bean.*"%>
<%@ page import="com.lpmas.admin.business.*"%>
<%@ page import="com.lpmas.admin.config.*"%>
<%@ page import="com.lpmas.erp.warehouse.bean.*"%>
<%@ page import="com.lpmas.erp.warehouse.config.*"  %>
<%@ include file="../../include/header.jsp"%>
<%
    AdminUserHelper adminUserHelper = (AdminUserHelper) request.getAttribute("AdminUserHelper");
	List<WarehouseInfoBean> wareHouseList = (List<WarehouseInfoBean>)request.getAttribute("WarehouseInfoList");
	PageBean PAGE_BEAN = (PageBean) request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>) request.getAttribute("CondList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>仓库列表</title>
<link href="<%=STATIC_URL%>/css/main.css" type="text/css"
	rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL%>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL%>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL%>/js/ui.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.js"></script>
<link rel="stylesheet" href="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.css" type="text/css" media="screen" />
<script type="text/javascript"
	src="<%=STATIC_URL%>/js/My97DatePicker/WdatePicker.js"></script>
</head>

<body class="article_bg">
	<p class="article_tit">仓库列表</p>
	<form name="formSearch" method="post" action="WarehouseInfoList.do">
		<div class="search_form">
			<em class="em1">仓库名称：</em> 
			<input type="text" name="warehouseName" id="warehouseName" value="<%=ParamKit.getParameter(request, "warehouseName", "")%>" /> 
			
			<em class="em1">有效状态：</em> <select name="status"
				id="status">
				<%
					int status = ParamKit.getIntParameter(request, "status", Constants.STATUS_VALID);
					for (StatusBean<Integer, String> statusBean : Constants.STATUS_LIST) {
				%>
				<option value="<%=statusBean.getStatus()%>"
					<%=(statusBean.getStatus() == status) ? "selected" : ""%>><%=statusBean.getValue()%></option>
				<%
					}
				%>
			</select>
		 <input name="" type="submit" class="search_btn_sub" value="查询" />
		</div>
	</form>
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			class="table_style">
			<tr>
				<th>仓库名称</th>
				<th>仓库编号</th>
				<th>仓库类型</th>
				<th>国家</th>
				<th>省份</th>
				<th>城市</th>
				<th>地区</th>
				<th>地址</th>
				<th>邮编</th>
				<th>电话</th>
				<th>操作</th>
			</tr>
			<%
				for (WarehouseInfoBean bean : wareHouseList) {
			%>
			<tr>
				<td><%=bean.getWarehouseName()%></td>
				<td><%=bean.getWarehouseNumber()%></td>
				<td>实体仓库</td>
				<td><%=bean.getCountry()%></td>
				<td><%=bean.getProvince()%></td>
				<td><%=bean.getCity()%></td>
				<td><%=bean.getRegion()%></td>
				<td><%=bean.getAddress()%></td>
				<td><%=bean.getZipCode()%></td>
				<td><%=bean.getTelephone()%></td>
				<td><a href="WarehouseInfoManage.do?warehouseId=<%=bean.getWarehouseId()%>&readOnly=true">查看</a>&nbsp;|
				&nbsp;<%if (adminUserHelper.hasPermission(WarehouseResource.WAREHOUSE_INFO,OperationConfig.UPDATE)) {%>
				<a href="WarehouseInfoManage.do?warehouseId=<%=bean.getWarehouseId()%>&readOnly=false">修改</a><%}%></td>
			</tr>
			<%
				}
			%>
		</table>
			
		<ul class="page_info">
		<li class="page_left_btn">
		    <%if (adminUserHelper.hasPermission(WarehouseResource.WAREHOUSE_INFO,OperationConfig.CREATE)) {%>
			<input type="button" name="new" id="new" value="新建" onclick="javascript:location.href='WarehouseInfoManage.do'">
			<%}%>
		</li>
		<%@ include file="../../include/page.jsp"%>
	</ul>
</body>
<script type='text/javascript'>
var data;
$(document).ready(function() {
	var showFilter="${param.showFilter}";
	if(showFilter=="hide"){
		$('.search_form').hide();
	}
});
</script>
</html>