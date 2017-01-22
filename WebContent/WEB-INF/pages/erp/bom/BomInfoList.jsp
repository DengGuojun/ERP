<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.lpmas.framework.config.*"%>
<%@ page import="com.lpmas.framework.bean.*"%>
<%@ page import="com.lpmas.framework.page.*"%>
<%@ page import="com.lpmas.framework.util.*"%>
<%@ page import="com.lpmas.framework.web.*"%>
<%@ page import="com.lpmas.admin.bean.*"%>
<%@ page import="com.lpmas.admin.business.*"%>
<%@ page import="com.lpmas.admin.config.*"%>
<%@ page import="com.lpmas.erp.bom.bean.*"%>
<%@ page import="com.lpmas.erp.bom.config.*"%>
<%@ page import="com.lpmas.constant.info.*"%>
<%@ include file="../../include/header.jsp"%>
<%
    AdminUserHelper adminUserHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	List<BomInfoBean> bomInfoList = (List<BomInfoBean>)request.getAttribute("BomInfoList");
	PageBean PAGE_BEAN = (PageBean) request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>) request.getAttribute("CondList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>BOM</title>
<link href="<%=STATIC_URL%>/css/main.css" type="text/css"
	rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL%>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL%>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL%>/js/ui.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.js"></script>
<link rel="stylesheet" href="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.css" type="text/css" media="screen" />
<script type="text/javascript"
	src="<%=STATIC_URL%>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
        document.domain='<%=DOMAIN%>'; 
</script>
</head>
<body class="article_bg">
	<p class="article_tit">BOM</p>
	<form name="formSearch" method="post" action="BomInfoList.do">
		<div class="search_form">
			<p>
			<em class="em1">BOM编码：</em> 
			<input type="text" name="bomNumber" id="bomNumber" value="<%=ParamKit.getParameter(request, "bomNumber", "")%>" /> 
			</p>
			<p>
			<em class="em1">BOM名称：</em> 
			<input type="text" name="bomName" id="bomName" value="<%=ParamKit.getParameter(request, "bomName", "")%>" /> 
			</p>
			<p>
			<em class="em1">BOM类型：</em> <select id="bomType" name="bomType">
				<option value=0>请选择</option>
				<%for (StatusBean<Integer, String> typeBean : BomConfig.BOM_TYPE_LIST) {%>
				<option value="<%=typeBean.getStatus()%>"
					<%=ParamKit.getIntParameter(request, "bomType", 0) == (Integer) typeBean.getStatus()? "selected": ""%>><%=typeBean.getValue()%></option>
				<%}%>
			</select>
			</p>
			<p>
			<em class="em1">BOM状态：</em> <select name="isActive" id="isActive">
				<%int isActive = ParamKit.getIntParameter(request, "isActive", Constants.STATUS_VALID);
					for (StatusBean<Integer, String> statusBean : BomConfig.ACTIVE_STATUS_LIST) {%>
				<option value="<%=statusBean.getStatus()%>"
					<%=(statusBean.getStatus() == isActive) ? "selected" : ""%>><%=statusBean.getValue()%></option>
				<%}%>
			</select>
			</p>
			<p>
			<em class="em1">开始时间：</em> <input type="text" name="useStartTime"
				id="useStartTime"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'useEndTime\');}'})"
				value="<%=ParamKit.getParameter(request, "useStartTime", "")%>"
				size="20" checkStr="开始时间;date;false;;100" />
			</p>
		    <p>
		    <em class="em1">结束时间：</em> <input type="text" name="useEndTime" id="useEndTime"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'useStartTime\');}'})"
				value="<%=ParamKit.getParameter(request, "useEndTime", "")%>"
				size="20" checkStr="结束时间;date;false;;100" />
		    </p>
		<%if(adminUserHelper.hasPermission(BomResource.BOM_INFO, OperationConfig.SEARCH)){ %>
		 <input name="" type="submit" class="search_btn_sub" value="查询" />
		<%} %>
		<%if(adminUserHelper.hasPermission(BomResource.BOM_INFO, OperationConfig.CREATE)){ %>
			<input type="button" class="search_btn_sub" name="new" id="new" value="新建" onclick="create()">
		<%} %>
		</div>
	</form>
		<table width="100%" border="0" cellpadding="0"
			class="table_style">
			<tr>
				<th>BOM ID</th>
				<th>BOM编码</th>
				<th>BOM名称 </th>
				<th>BOM类型</th>
				<th>激活状态</th>
				<th>开始时间</th>
				<th>结束时间</th>
				<th>操作</th>
			</tr>
			<%
				for (BomInfoBean bean : bomInfoList) {
			%>
			<tr>
				<td><%=bean.getBomId() %></td>
				<td><%=bean.getBomNumber() %></td>
				<td><%=bean.getBomName() %></td>
				<td><%=MapKit.getValueFromMap(bean.getBomType(), BomConfig.BOM_TYPE_MAP)%></td>
				<td><%=MapKit.getValueFromMap(bean.getIsActive(), BomConfig.ACTIVE_STATUS_MAP) %></td>
				<td><%=DateKit.formatTimestamp(bean.getUseStartTime(), DateKit.DEFAULT_DATE_FORMAT) %></td>
				<td><%=DateKit.formatTimestamp(bean.getUseEndTime(), DateKit.DEFAULT_DATE_FORMAT) %></td>
				<td><a href="BomInfoManage.do?bomId=<%=bean.getBomId()%>&readOnly=true">查看</a>&nbsp;
				<%if(adminUserHelper.hasPermission(BomResource.BOM_INFO, OperationConfig.UPDATE)){ %>
				|&nbsp;<a href="BomInfoManage.do?bomId=<%=bean.getBomId()%>&readOnly=false">修改</a></td>
				<%} %>
			</tr>
			<%
				}
			%>
		</table>
			
		<ul class="page_info">
		<li class="page_left_btn">
		<%if(adminUserHelper.hasPermission(BomResource.BOM_INFO, OperationConfig.CREATE)){ %>
			<input type="button" name="new" id="new" value="新建" onclick="create()">
		<%} %>
		</li>
		<%@ include file="../../include/page.jsp"%>
	</ul>
</body>
<script type="text/javascript">	
function create(){
	$.fancybox.open({
			href : 'BomTypeSelect.do?callbackFun=selectBomType',
			type : 'iframe',
			width : 560,
			minHeight : 150
	});
}
function selectBomType(value) {
	var url = "BomInfoManage.do?bomType="+ value;
	window.location.href= url
}
</script>
</html>