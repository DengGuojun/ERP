<%@page import="com.lpmas.erp.warehouse.bean.WarehouseInfoBean"%>
<%@page import="com.lpmas.admin.client.AdminServiceClient"%>
<%@page import="com.lpmas.pdm.bean.MaterialInfoBean"%>
<%@page import="com.lpmas.pdm.bean.ProductItemBean"%>
<%@page import="com.lpmas.pdm.client.PdmServiceClient"%>
<%@page import="com.lpmas.constant.info.InfoTypeConfig"%>
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
<%@ page import="com.lpmas.erp.report.bean.*"%>
<%@ page import="com.lpmas.erp.report.config.*"  %>
<%@ include file="../../include/header.jsp"%>
<%
    AdminUserHelper adminUserHelper = (AdminUserHelper) request.getAttribute("AdminUserHelper");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>库存月度数据手动汇总</title>
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
	<p class="article_tit">库存月度数据手动汇总</p>
	<form name="formSearch" method="post" action="MonthlyInventoryReportMenualSummary.do">
		<div class="modify_form">
		<p>
		<em class="int_label"><span>*</span>汇总月份：</em> 
			<input type="text" name="reportMonth" id="reportMonth"
				 onclick="WdatePicker({dateFmt:'yyyyMM'})"
				 value=""
				 size="20">
		</p>
		 <div class="div_center">
			  <input name="" type="submit" class="search_btn_sub" value="汇总" />
			</div>
		</div>
	</form>
</body>
</html>