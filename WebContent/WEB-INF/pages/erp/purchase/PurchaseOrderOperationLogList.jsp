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
<%@ page import="com.lpmas.erp.purchase.config.*"%>
<%@ page import="com.lpmas.erp.config.*"%>
<%@ page import="com.lpmas.erp.purchase.bean.*"  %>

<%
	List<DataLogBean> logBeans = (List<DataLogBean>) request.getAttribute("DataLogList");
	PageBean PAGE_BEAN = (PageBean) request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>) request.getAttribute("CondList");
	AdminUserInfoClientCache adminCache = new AdminUserInfoClientCache();
	int poId = ParamKit.getIntParameter(request, "poId", 0);
	int wareType = ParamKit.getIntParameter(request, "wareType", 0);
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	String mode = ParamKit.getParameter(request, "mode", "general");
	PurchaseOrderInfoBean infoBean = (PurchaseOrderInfoBean)request.getAttribute("PurchaseOrderInfoBean");
%>

<%@ include file="../../include/header.jsp"%>
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
			<li><a href="PurchaseOrderInfoList.do?wareType=<%=wareType%>"><%=MapKit.getValueFromMap(wareType, PurchaseOrderConfig.WARE_TYPE_MAP)%>订单列表</a>&nbsp;>&nbsp;</li>
			<li><%=infoBean.getPoNumber() %>&nbsp;>&nbsp;</li>			
			<li>采购单日志</li>
		</ul>
	</div>
	<%}else{%>
	<p class="article_tit">采购单日志</p>
	<%}%>
	<%if(mode.equals("po")){%>	
	<div class="article_tit">
		<p class="tab">
		<a href="PurchaseOrderInfoManage.do?poId=<%=poId%>&readOnly=<%=readOnly%>&wareType=<%=wareType%>&mode=po">基本信息</a> 
		<a href="DeliveryNoteInfoList.do?poId=<%=poId%>&readOnly=<%=readOnly%>&wareType=<%=wareType%>&mode=po">收发货记录</a>
		<a href="PurchaseOrderMemoManage.do?poId=<%=poId%>&readOnly=<%=readOnly%>&wareType=<%=wareType%>&mode=po">备注信息</a>
		<a href="PurchaseOrderOperationLogList.do?poId=<%=poId%>&readOnly=<%=readOnly%>&wareType=<%=wareType%>&mode=po">操作日志</a>
		</p>
		<script>
		tabChange('.tab', 'a');
		</script>
	</div>	
	<%} %>
	<%if(mode.equals("po")){%>	
	<form name="formSearch" method="post" action="PurchaseOrderOperationLogList.do?poId=<%=poId %>&readOnly=<%=readOnly%>&wareType=<%=wareType%>&mode=<%=mode %>">
	<%}else{ %>
	<form name="formSearch" method="post" action="PurchaseOrderOperationLogList.do?readOnly=<%=readOnly%>&wareType=<%=wareType%>&mode=<%=mode %>" onsubmit="addQueryStr(this)">
	<%} %>
		<div class="search_form">
			<%if(mode.equals("general")){%>	
			<em class="int_label">采购单号：</em>
	  		<input type="text" name="sourceOrderNumber" id="sourceOrderNumber" value="<%=ParamKit.getParameter(request, "sourceOrderNumber", "") %>"  readOnly />
	      	<input type="hidden" name="poId" id="sourceOrderId" value="<%=poId%>"/>
	      	<input type="button" name="selectSourceOrder" id="selectSourceOrder" value="选择" />	
			<%} %>
			<em class="em1">日志类型：</em> <select name="field1" id="field1">
				<option value=""></option>
				<%
					for (StatusBean<String, String> statusBean : PurchaseOrderLogConfig.PO_LOG_TYPE_LIST) {
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
				size="20" checkStr="查询结束时间;date;false;;100" />
				 <input name="" type="submit" class="search_btn_sub" value="查询" />
				<%if(mode.equals("general")){ %>
				 <input name="" type="button" class="search_btn_sub" value="条件重置" onclick='location.href="PurchaseOrderOperationLogList.do"' />
				<%} %>
				<%if(mode.equals("po")){ %>
				 <input name="" type="button" class="search_btn_sub" value="条件重置" onclick='location.href="PurchaseOrderOperationLogList.do?poId=<%=poId%>&readOnly=<%=readOnly%>&wareType=<%=wareType%>&mode=po"' />
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
			<td><%=PurchaseOrderLogConfig.PO_LOG_TYPE_MAP.get(bean.getField1()) %></td>
			<td><%=DateKit.formatTimestamp(bean.getCreateTime(), DateKit.DEFAULT_DATE_TIME_FORMAT)%></td>
			<td><%=adminCache.getAdminUserNameByKey(bean.getCreateUser())%></td>
			<td><a href="PurchaseOrderOperationLogManage.do?logId=<%=bean.getLogId()%>&poId=<%=poId%>&readOnly=<%=readOnly%>&wareType=<%=wareType%>&mode=<%=mode %>">查看详细</a></td>
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
					var sourceOrderId = $("#sourceOrderId").val();
					var url = "PurchaseOrderInfoSelect.do?&callbackFun=selectSourceOrder&mode=log&poId=" + sourceOrderId
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
	function selectSourceOrder(sourceOrderId, sourceOrderNumber, supplierId, supplierName){
		if(sourceOrderId != "" && sourceOrderNumber != ""){
			$("#sourceOrderId").val(sourceOrderId);
			$("#sourceOrderNumber").val(sourceOrderNumber);
		}
	}
	function addQueryStr(ele){
		var orgAction = $(ele).attr('action');
		var sourceOrderNumber = $("#sourceOrderNumber").val();
		$(ele).attr('action',orgAction+'&sourceOrderNumber='+sourceOrderNumber);
	} 
</script>
</html>