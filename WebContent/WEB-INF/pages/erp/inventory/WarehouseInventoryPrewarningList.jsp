<%@page import="com.lpmas.constant.info.InfoTypeConfig"%>
<%@page import="com.lpmas.pdm.client.PdmServiceClient"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.lpmas.erp.inventory.config.*"%>
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
<%@ page import="com.lpmas.erp.inventory.bean.*"%>
<%@ include file="../../include/header.jsp"%>
<%
	List<WarehouseInventoryPrewarningBean> prewarningList = (List<WarehouseInventoryPrewarningBean>)request.getAttribute("WarehouseInventoryPrewarningList");
	PageBean PAGE_BEAN = (PageBean) request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>) request.getAttribute("CondList");
	AdminUserHelper adminUserHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>预警列表</title>
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
	<p class="article_tit">预警列表</p>
	<form name="formSearch" method="post" action="WarehouseInventoryPrewarningList.do">
		<div class="search_form">
			<em class="em1">制品类型：</em> 
			<select name="wareType"
				id="wareType">
				<option value="0"></option>
				<%
					int wareType = ParamKit.getIntParameter(request, "wareType", 0);
					for (StatusBean<Integer, String> statusBean : WarehouseInventoryPrewarningConfig.WARE_TYPE_LIST) {
				%>
				<option value="<%=statusBean.getStatus()%>"
					<%=(statusBean.getStatus() == wareType) ? "selected" : ""%>><%=statusBean.getValue()%></option>
				<%
					}
				%>
			</select>
			<em class="em1">预警类型：</em> 
			<select name="prewarningType"
				id="prewarningType">
				<option value="0"></option>
				<%
					int prewarningType = ParamKit.getIntParameter(request, "prewarningType", 0);
					for (StatusBean<Integer, String> statusBean : WarehouseInventoryPrewarningConfig.PREWARNING_TYPE_LIST) {
				%>
				<option value="<%=statusBean.getStatus()%>"
					<%=(statusBean.getStatus() == prewarningType) ? "selected" : ""%>><%=statusBean.getValue()%></option>
				<%
					}
				%>
			</select>
			
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
		 <input name="" type="button" class="search_btn_sub" value="条件重置" onclick="location.href='WarehouseInventoryPrewarningList.do'" />
		</div>
	</form>
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			class="table_style">
			<tr>
				<th>制品类型</th>
				<th>制品名称</th>
				<th>预警类型</th>
				<th>是否自动修改</th>
				<th>状态</th>
				<th>操作</th>
			</tr>
			<%
				PdmServiceClient client = new PdmServiceClient();
				String wareName="";
				for (WarehouseInventoryPrewarningBean bean : prewarningList) {
			%>
			<tr>
				<td><%=WarehouseInventoryPrewarningConfig.WARE_TYPE_MAP.get(bean.getWareType())%></td>
				<%
				if(bean.getWareType()==InfoTypeConfig.INFO_TYPE_MATERIAL){
					wareName = client.getMaterialInfoByKey(bean.getWareId()).getMaterialName();
				}else{
					wareName = client.getProductItemByKey(bean.getWareId()).getItemName();
				}%>
				<td><%=wareName%></td>
				<td><%=WarehouseInventoryPrewarningConfig.PREWARNING_TYPE_MAP.get(bean.getPrewarningType()) %></td>
				<td><%=WarehouseInventoryPrewarningConfig.IS_AUTO_MODIFY_MAP.get(bean.getIsAutoModify())%></td>
				<td><%=Constants.STATUS_MAP.get(bean.getStatus())%></td>
				<td><a href="WarehouseInventoryPrewarningManage.do?wareType=<%=bean.getWareType()%>&wareId=<%=bean.getWareId()%>&prewarningType=<%=bean.getPrewarningType() %>&readOnly=true">查看</a>
				<%if(adminUserHelper.hasPermission(InventoryResource.WAREHOUSE_INVENTORY_PREWARNING, OperationConfig.UPDATE)){ %>
				&nbsp;|&nbsp; <a href="WarehouseInventoryPrewarningManage.do?wareType=<%=bean.getWareType()%>&wareId=<%=bean.getWareId()%>&prewarningType=<%=bean.getPrewarningType() %>&readOnly=false">修改</a>
				<%} %>
				</td>
			</tr>
			<%
				}
			%>
		</table>
			
		<ul class="page_info">
		<li class="page_left_btn">
		<%if(adminUserHelper.hasPermission(InventoryResource.WAREHOUSE_INVENTORY_PREWARNING, OperationConfig.CREATE)){ %>
			<input type="button" name="new" id="new" value="新建" onclick="window.location.href= 'WarehouseInventoryPrewarningManage.do'">
		<%} %>
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