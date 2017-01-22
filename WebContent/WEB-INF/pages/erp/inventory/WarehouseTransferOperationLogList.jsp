<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map.Entry"%>
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
<%@ page import="com.lpmas.log.bean.*"%>
<%@ page import="com.lpmas.constant.info.*"%>
<%@ page import="com.lpmas.erp.inventory.config.*"%>
<%@ page import="com.lpmas.erp.config.*"%>
<%@ page import="com.lpmas.erp.inventory.bean.*"  %>
<%@ include file="../../include/header.jsp"%>
<%
	List<DataLogBean> logBeans = (List<DataLogBean>) request.getAttribute("DataLogList");
	PageBean PAGE_BEAN = (PageBean) request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>) request.getAttribute("CondList");
	AdminUserInfoClientCache adminCache = new AdminUserInfoClientCache();
	int toId = ParamKit.getIntParameter(request, "toId", 0);
	int wareType = ParamKit.getIntParameter(request, "wareType", 0);
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	WarehouseTransferOrderInfoBean infoBean = (WarehouseTransferOrderInfoBean)request.getAttribute("WarehouseTransferOrderInfoBean");
	String mode = ParamKit.getParameter(request, "mode", "general");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>日志管理</title>
<link href="<%=STATIC_URL%>/css/main.css" type="text/css"
	rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL%>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL%>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL%>/js/ui.js"></script>
<script type='text/javascript'
	src="<%=STATIC_URL%>/js/fancyBox/jquery.fancybox.js"></script>
<link rel="stylesheet"
	href="<%=STATIC_URL%>/js/fancyBox/jquery.fancybox.css" type="text/css"
	media="screen" />
<script type="text/javascript"
	src="<%=STATIC_URL%>/js/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript">
        document.domain='<%=DOMAIN%>'; 
	</script>
</head>

<body class="article_bg">
    <%if(mode.equals("po")){%>	
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a> 
		<ul class="art_nav">		    
			<li><a href="WarehouseTransferOrderInfoList.do?wareType=<%=wareType%>"><%=MapKit.getValueFromMap(infoBean.getWareType(), InventoryConsoleConfig.WARE_TYPE_MAP)%>调拨单列表</a>&nbsp;>&nbsp;</li>
			<li><%=infoBean.getToNumber() %>&nbsp;>&nbsp;</li>
			<li>调拨单日志</li>
		</ul>
	</div>
	<%}else{%>
	<p class="article_tit">调拨单日志</p>
	<%}%>
	<%if(mode.equals("po")){%>	
	<div class="article_tit">
		<p class="tab">
		<a href="WarehouseTransferOrderInfoManage.do?toId=<%=toId%>&readOnly=<%=readOnly%>&wareType=<%=wareType%>&mode=po">基础信息</a> 
		<a href="WarehouseTransferOperationLogList.do?toId=<%=toId%>&readOnly=<%=readOnly%>&wareType=<%=wareType%>&mode=po">调拨单日志</a>
		</p>
		<script>
		tabChange('.tab', 'a');
		</script>
	</div>
	<%}%>
	<%if(mode.equals("po")){%>		
	<form name="formSearch" method="post" action="WarehouseTransferOperationLogList.do?toId=<%=toId%>&readOnly=<%=readOnly%>&wareType=<%=wareType%>&mode=<%=mode%>">
	<%}else{ %>
	<form name="formSearch" method="post" action="WarehouseTransferOperationLogList.do?mode=<%=mode%>" onsubmit="addQueryStr(this)">
	<%} %>	
		<div class="search_form">
			<em class="em1">日志类型：</em> <select name="field1" id="field1">
				<option value=""></option>
				<%
					for (StatusBean<String, String> statusBean : WarehouseTransferOrderLogConfig.LOG_WAREHOUSE_TRANSFER_ORDER_LIST) {
				%>
				<option value="<%=statusBean.getStatus()%>"
					<%=(statusBean.getStatus().equals(ParamKit.getParameter(request, "field1", "")))
						? "selected"
						: ""%>>
					<%=statusBean.getValue()%>
				</option>
				<%
					}
				%>
			</select>
            <em class="em1">查询开始时间：</em> 
			   <input type="text" name="begDateTime" id="begDateTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', maxDate:'#F{$dp.$D(\'endDateTime\');}'})"
				value="<%=ParamKit.getParameter(request, "begDateTime", "")%>" size="20" checkStr="查询开始时间;date;false;;100" /> 
			<em class="em1">查询结束时间：</em>
			    <input type="text" name="endDateTime" id="endDateTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'begDateTime\');}'})"
				value="<%=ParamKit.getParameter(request, "endDateTime", "")%>" size="20" checkStr="查询开始时间;date;false;;100" /> 
				<input name="" type="submit" class="search_btn_sub" value="查询" />
				<%if(mode.equals("general")){ %>
				 <input name="" type="button" class="search_btn_sub" value="条件重置" onclick='location.href="WarehouseTransferOperationLogList.do"' />
				<%} %>
				<%if(mode.equals("po")){ %>
				 <input name="" type="button" class="search_btn_sub" value="条件重置" onclick='location.href="WarehouseTransferOperationLogList.do?toId=<%=toId%>&readOnly=<%=readOnly%>&wareType=<%=wareType%>&mode=po"' />
				<%} %>
		</div>
	</form>
	<table width="100%" border="0" cellpadding="0" class="table_style">
		<tr>
			<th>操作类型</th>
			<th>日志信息类型</th>
			<th>日志类型</th>
			<th>日志创建时间</th>
			<th>操作者</th>
			<th>点击查看</th>
		</tr>
		<%
			for (DataLogBean bean : logBeans) {
		%>
		<tr>
			<td><%=OperationConfig.OPERATION_MAP.get(bean.getOperationCode())%></td>
			<td><%=InfoTypeConfig.INFO_TYPE_MAP.get(bean.getInfoType())%></td>
			<td><%=WarehouseTransferOrderLogConfig.LOG_WAREHOUSE_TRANSFER_ORDER_MAP.get(bean.getField1()) %></td>
			<td><%=DateKit.formatTimestamp(bean.getCreateTime(), DateKit.DEFAULT_DATE_TIME_FORMAT)%></td>
			<td><%=adminCache.getAdminUserNameByKey(bean.getCreateUser())%></td>
			<%if(mode.equals("po")){%>	
			<td><a href="WarehouseTransferOperationLogManage.do?logId=<%=bean.getLogId()%>&toId=<%=toId%>&readOnly=<%=readOnly%>&wareType=<%=wareType%>&mode=<%=mode%>">查看详细</a></td>
		    <%}else{ %>
		    <td><a href="WarehouseTransferOperationLogManage.do?logId=<%=bean.getLogId()%>&toId=<%=toId%>&readOnly=<%=readOnly%>&wareType=<%=wareType%>&mode=<%=mode%>">查看详细</a></td>
		    <%} %>	
		</tr>
		<%
			}
		%>
	</table>

	<ul class="page_info">
		<%@ include file="../../include/page.jsp"%>
	</ul>
</body>
<script type='text/javascript'>
	var data;
	$(document).ready(function() {
		var showFilter = "${param.showFilter}";
		if (showFilter == "hide") {
			$('.search_form').hide();
		}
	});
</script>
</html>