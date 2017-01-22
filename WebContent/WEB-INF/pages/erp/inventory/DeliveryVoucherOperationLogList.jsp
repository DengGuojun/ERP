<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
	int dvId = ParamKit.getIntParameter(request, "dvId", 0);
	int wareType = ParamKit.getIntParameter(request, "wareType", 0);
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	DeliveryVoucherInfoBean infoBean = (DeliveryVoucherInfoBean)request.getAttribute("DeliveryVoucherInfoBean");
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
			<li><a href="DeliveryVoucherInfoList.do?dvType=<%=infoBean.getDvType()%>&wareType=<%=wareType%>"><%=MapKit.getValueFromMap(infoBean.getWareType(), InventoryConsoleConfig.WARE_TYPE_MAP)%><%=MapKit.getValueFromMap(infoBean.getDvType(), DeliveryVoucherConfig.DELIVERY_VOUCHER_TYPE_MAP)%>单列表</a>&nbsp;>&nbsp;</li>
			<li><%=infoBean.getDvNumber() %>&nbsp;>&nbsp;</li>
			<li>出库单日志</li>
		</ul>
	</div>
	<%}else{%>
	<p class="article_tit">出库单日志</p>
	<%}%>
	<%if(mode.equals("po")){%>	
	<div class="article_tit">
		<p class="tab">
		<a href="DeliveryVoucherInfoManage.do?dvId=<%=dvId%>&readOnly=<%=readOnly%>&dvType=<%=infoBean.getDvType()%>&wareType=<%=wareType%>&mode=po">基础信息</a> 
		<a href="DeliveryVoucherOperationLogList.do?dvId=<%=dvId%>&readOnly=<%=readOnly%>&dvType=<%=infoBean.getDvType()%>&wareType=<%=wareType%>&mode=po">出库单日志</a>
		</p>
		<script>
		tabChange('.tab', 'a');
		</script>
	</div>
	<%}%>
	<%if(mode.equals("po")){%>		
	<form name="formSearch" method="post" action="DeliveryVoucherOperationLogList.do?dvId=<%=dvId%>&readOnly=<%=readOnly%>&dvType=<%=infoBean.getDvType()%>&wareType=<%=wareType%>&mode=<%=mode%>">
	<%}else{ %>
	<form name="formSearch" method="post" action="DeliveryVoucherOperationLogList.do?mode=<%=mode%>" onsubmit="addQueryStr(this)">
	<%} %>	
		<div class="search_form">
		    <%if(mode.equals("general")){%>	
			<em class="int_label">出库单号：</em>
			<input type="text" name="dvNumber" id="dvNumber" value="<%=ParamKit.getParameter(request, "dvNumber", "") %>" readOnly />
	      	<input type="hidden" name="dvId" id="sourceDvId" value="<%=dvId%>"/>
	      	<input type="button" name="selectSourceOrder" id="selectSourceOrder" value="选择" />	
			<%} %>
			<em class="em1">日志类型：</em> <select name="field1" id="field1">
				<option value=""></option>
				<%
					for (StatusBean<String, String> statusBean : DeliveryVoucherLogConfig.LOG_DELIVERY_VOUCHER_LIST) {
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
			</select> <em class="em1">查询开始时间：</em> <input type="text" name="begDateTime"
				id="begDateTime"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', maxDate:'#F{$dp.$D(\'endDateTime\');}'})"
				value="<%=ParamKit.getParameter(request, "begDateTime", "")%>"
				size="20" checkStr="查询开始时间;date;false;;100" /> <em class="em1">查询结束时间：</em>
			<input type="text" name="endDateTime" id="endDateTime"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'begDateTime\');}'})"
				value="<%=ParamKit.getParameter(request, "endDateTime", "")%>"
				size="20" checkStr="查询开始时间;date;false;;100" /> <input name=""
				type="submit" class="search_btn_sub" value="查询" />
				<%if(mode.equals("general")){ %>
				 <input name="" type="button" class="search_btn_sub" value="条件重置" onclick='location.href="DeliveryVoucherOperationLogList.do"' />
				<%} %>
				<%if(mode.equals("po")){ %>
				 <input name="" type="button" class="search_btn_sub" value="条件重置" onclick='location.href="DeliveryVoucherOperationLogList.do?dvId=<%=dvId%>&readOnly=<%=readOnly%>&dvType=<%=infoBean.getDvType()%>&wareType=<%=wareType%>&mode=po"' />
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
			<td><%=DeliveryVoucherLogConfig.LOG_DELIVERY_VOUCHER_MAP.get(bean.getField1()) %></td>
			<td><%=DateKit.formatTimestamp(bean.getCreateTime(), DateKit.DEFAULT_DATE_TIME_FORMAT)%></td>
			<td><%=adminCache.getAdminUserNameByKey(bean.getCreateUser())%></td>
			<%if(mode.equals("po")){%>	
			<td><a href="DeliveryVoucherOperationLogManage.do?logId=<%=bean.getLogId()%>&dvId=<%=dvId%>&readOnly=<%=readOnly%>&dvType=<%=infoBean.getDvType()%>&wareType=<%=wareType%>&mode=<%=mode%>">查看详细</a></td>
		    <%}else{ %>
		    <td><a href="DeliveryVoucherOperationLogManage.do?logId=<%=bean.getLogId()%>&dvId=<%=dvId%>&readOnly=<%=readOnly%>&wareType=<%=wareType%>&mode=<%=mode%>">查看详细</a></td>
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
		$("#selectSourceOrder").click(
				function() {
					var sourceDvId = $("#sourceDvId").val();
					var url = "DeliveryVoucherInfoSelect.do?&callbackFun=selectSourceOrder&mode=log&dvId=" + sourceDvId
					$.fancybox.open({
						href :  url,
						type : 'iframe',
						width : 560,
						minHeight : 500
				});
			});
		var showFilter = "${param.showFilter}";
		if (showFilter == "hide") {
			$('.search_form').hide();
		}
	});
	function selectSourceOrder(sourceDvId, dvNumber){
		if(sourceDvId != "" && dvNumber != ""){
			$("#sourceDvId").val(sourceDvId);
			$("#dvNumber").val(dvNumber);
		}
	}
	function addQueryStr(ele){
		var orgAction = $(ele).attr('action');
		var dvNumber = $("#dvNumber").val();
		$(ele).attr('action',orgAction+'&dvNumber='+dvNumber);
	}
</script>
</html>