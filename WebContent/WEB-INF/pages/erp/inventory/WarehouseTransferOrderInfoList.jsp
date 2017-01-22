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
<%@page import="com.lpmas.admin.client.cache.AdminUserInfoClientCache"%>
<%@ page import="com.lpmas.erp.config.*"  %>
<%@ page import="com.lpmas.erp.purchase.config.*"  %>
<%@ page import="com.lpmas.erp.inventory.config.*"  %>
<%@ page import="com.lpmas.erp.inventory.bean.*"  %>
<%@ page import="com.lpmas.erp.inventory.business.*"  %>
<%@ page import="com.lpmas.constant.info.*"  %>
<%@ page import="com.lpmas.erp.warehouse.bean.*"  %>
<%@ include file="../../include/header.jsp"%>
<%
	AdminUserHelper adminUserHelper = (AdminUserHelper) request.getAttribute("AdminUserHelper");
	List<WarehouseTransferOrderInfoBean> list = (List<WarehouseTransferOrderInfoBean>) request.getAttribute("WarehouseTransferOrderInfoList");
	PageBean PAGE_BEAN = (PageBean) request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>) request.getAttribute("CondList");
	int wareType = (Integer) request.getAttribute("WareType");
	AdminUserInfoClientCache adminCache = new AdminUserInfoClientCache();
	List<WarehouseInfoBean> warehouseInfoList = (List<WarehouseInfoBean>) request.getAttribute("WarehouseInfoList");
	Map<String,String> warehouseInfoMap = (Map<String,String>) request.getAttribute("WarehouseInfoMap");
	List<Integer> warehouseTransferOrderCreaterUserList = (List<Integer>) request.getAttribute("WarehouseTransferOrderCreaterUserList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=MapKit.getValueFromMap(wareType, InventoryConsoleConfig.WARE_TYPE_MAP) %>调拨单列表</title>
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
</head>
<body class="article_bg">
	<p class="article_tit"><%=MapKit.getValueFromMap(wareType, InventoryConsoleConfig.WARE_TYPE_MAP) %>调拨单列表</p>
    <form name="formSearch" method="post" action="WarehouseTransferOrderInfoList.do?wareType=<%=wareType%>" onsubmit="javascript:disableDom.removeAttr('disabled')">
		<div class="search_form">
					<p>
					<em class="em1">调拨单编号：</em> 
					<input type="text" name="toNumber" id="toNumber" value="<%=ParamKit.getParameter(request, "toNumber", "")%>"size="20" />
					</p>
					<p>
					<em class="em1">出库仓库：</em> 
						<select name="sourceWarehouseId" id="sourceWarehouseId">
							<option value=""></option>
							<%int sourceWarehouseId = ParamKit.getIntParameter(request, "sourceWarehouseId", 0);
								for (WarehouseInfoBean bean : warehouseInfoList) {%>
							<option value="<%=bean.getWarehouseId()%>"
								<%=(bean.getWarehouseId() == sourceWarehouseId) ? "selected" : ""%>><%=bean.getWarehouseName()%></option>
							<%}%>
						</select>
					</p>
					<p>
					<em class="em1">入库仓库：</em> 
						<select name="targetWarehouseId" id="targetWarehouseId">
							<option value=""></option>
							<%int targetWarehouseId = ParamKit.getIntParameter(request, "targetWarehouseId", 0);
								for (WarehouseInfoBean bean : warehouseInfoList) {%>
							<option value="<%=bean.getWarehouseId()%>"
								<%=(bean.getWarehouseId() == targetWarehouseId) ? "selected" : ""%>><%=bean.getWarehouseName()%></option>
							<%}%>
						</select>
					</p>
					<p>
					<em class="em1">创建人:</em> 
						<select name="createUser" id="createUser">
							<option value=""></option>
							<%int createUser = ParamKit.getIntParameter(request, "createUser", 0);
								for (Integer user : warehouseTransferOrderCreaterUserList) {%>
							<option value="<%=user%>"
								<%=(user == createUser) ? "selected" : ""%>><%=adminCache.getAdminUserNameByKey(user)%>
							</option>
							<%}%>
						</select>
					</p>
					<p>
					<em class="em1">审批状态：</em> 
					<select name="reviewStatus" id="reviewStatus">
							<option value=""></option>
							<%String reviewStatus = ParamKit.getParameter(request, "reviewStatus", "");
								for (StatusBean<String, String> statusBean : WarehouseTransferOrderConfig.APPROVE_STATUS_LIST) {%>
							<option value="<%=statusBean.getStatus()%>"
								<%=(statusBean.getStatus().equals(reviewStatus)) ? "selected" : ""%>><%=statusBean.getValue()%></option>
							<%}%>
					</select>
					</p>
					<p>
					<em class="em1">进度：</em> 
					<select name="toStatus" id="toStatus">
							<option value=""></option>
							<%String toStatus = ParamKit.getParameter(request, "toStatus", "");
								for (StatusBean<String, String> statusBean : WarehouseTransferOrderConfig.TO_STATUS_LIST) {%>
							<option value="<%=statusBean.getStatus()%>"
								<%=(statusBean.getStatus().equals(toStatus)) ? "selected" : ""%>><%=statusBean.getValue()%></option>
							<%}%>
					</select>
					</p>
					<input name="status" type="hidden" value="<%=Constants.STATUS_VALID%>">
					<input name="" type="submit" class="search_btn_sub"value="查询" />&nbsp;&nbsp; 
					<input name="" type="button" class="search_btn_sub" value="条件重置" onclick='location.href="WarehouseTransferOrderInfoList.do?wareType=<%=wareType%>"' />
					<%if (adminUserHelper.hasPermission(InventoryResource.WAREHOUSE_TRANSFER_ORDER_INFO,OperationConfig.CREATE)) {%>
					   <input type="button" class="search_btn_sub" name="new" id="new" value="新建" onclick="javascript:location.href='WarehouseTransferOrderInfoManage.do?wareType=<%=wareType%>'">
					<%}%> 
		</div>
	</form>
	<table width="100%" border="0" cellpadding="0" class="table_style">
		<tr>
			<th>选择</th>
			<th>调拨单号</th>
			<th>调拨单创建时间</th>
			<th>出库仓库</th>
			<th>入库仓库</th>
			<th>审批状态</th>
			<th>进度</th>
			<th>操作</th>
		</tr>
		<%
			for (WarehouseTransferOrderInfoBean bean : list) {
		%>
		<tr>
			<td><input type="radio" name="toId" id="radio_<%=bean.getToId()%>" value="<%=bean.getToId()%>" onclick="changeDisable()"></td>
			<td id="toNumber_<%=bean.getToId()%>"><%=bean.getToNumber()%></td>
			<td><%=DateKit.formatTimestamp(bean.getCreateTime(), "yyyy-MM-dd")%></td>
			<td><%=MapKit.getValueFromMap(bean.getSourceWarehouseId(), warehouseInfoMap)%></td>
			<td><%=MapKit.getValueFromMap(bean.getTargetWarehouseId(), warehouseInfoMap)%></td>
			<td id="reviewStatus_<%=bean.getToId()%>"><%=MapKit.getValueFromMap(bean.getApproveStatus(), WarehouseTransferOrderConfig.APPROVE_STATUS_MAP)%></td>
			<td><%=MapKit.getValueFromMap(bean.getToStatus(), WarehouseTransferOrderConfig.TO_STATUS_MAP)%></td>
			<td align="center">
			   <a href="/erp/WarehouseTransferOrderInfoManage.do?toId=<%=bean.getToId()%>&wareType=<%=wareType%>&readOnly=true">查看</a>
				<%if (adminUserHelper.hasPermission(InventoryResource.WAREHOUSE_TRANSFER_ORDER_INFO,OperationConfig.UPDATE)) {%>
				<%if (!WarehouseTransferOrderHelper.isLock(bean)) {%>
				 | <a href="/erp/WarehouseTransferOrderInfoManage.do?toId=<%=bean.getToId()%>&wareType=<%=wareType%>&readOnly=false">修改</a>
				<%}%> 
				<%if (WarehouseTransferOrderHelper.committable(bean)) {%>
				 | <a href="/erp/WarehouseTransferStatusManage.do?toId=<%=bean.getToId()%>&statusAction=<%=WarehouseTransferOrderConfig.TO_ACTION_COMMIT%>">提交审批</a>
				<%}%> 
				<%if (WarehouseTransferOrderHelper.cancelCommittable(bean)) {%>
				 | <a href="/erp/WarehouseTransferStatusManage.do?toId=<%=bean.getToId()%>&statusAction=<%=WarehouseTransferOrderConfig.TO_ACTION_CANCEL_COMMIT%>">取消审批</a>
				<%}%>
				<%if (WarehouseTransferOrderHelper.closable(bean)) {%>
				 | <a href="/erp/WarehouseTransferStatusManage.do?toId=<%=bean.getToId()%>&statusAction=<%=WarehouseTransferOrderConfig.TO_ACTION_CLOSE%>">关闭</a>
				<%}%>
				<%}%>
            </td>
		</tr>
		<%
			}
		%>
	</table>
	<ul class="page_info">
		<li class="page_left_btn">
		  <input type="hidden" name="wareType" id="wareType" value="<%=wareType%>" /> 
		  <%if (adminUserHelper.hasPermission(InventoryResource.WAREHOUSE_TRANSFER_ORDER_INFO,OperationConfig.CREATE)) {%>
		    <input type="button" name="new" id="new" value="新建" onclick="javascript:location.href='WarehouseTransferOrderInfoManage.do?wareType=<%=wareType%>'">
		  <%}%> 
		  <%if (adminUserHelper.hasPermission(InventoryResource.WAREHOUSE_TRANSFER_ORDER_INFO,OperationConfig.REMOVE)) {%>
			<input type="button" name="delete" id="delete" value="删除"> 
		  <%}%>
		  <%if (adminUserHelper.hasPermission(InventoryResource.WAREHOUSE_TRANSFER_ORDER_INFO,OperationConfig.APPROVE)) {%>
			<input type="button" name="review" id="review" disabled="disabled"value="审批"> 
		  <%}%>
        </li>
		<%@ include file="../../include/page.jsp"%>
	</ul>
</body>
<script>
var disableDom = null;
$(document).ready(function() {
	$("#delete").click(function() {
		var toId = $('input:radio[name=toId]:checked').val();
		if (typeof(toId) == "undefined"){
		    alert("请选择需要删除的调拨单");
		    return;
		}
		var toNumber = $('#toNumber_'+toId).html();
		if(confirm("确定要删除调拨单"+toNumber+"吗?")){
			var url = "WarehouseTransferOrderInfoRemove.do?toId="+ toId ;
			window.location.href= url
		 }
		
	});	
	$("#review").click(function() {
		var toId = $('input:radio[name=toId]:checked').val();				
		$.fancybox.open({
			href : 'WarehouseTransferOrderReviewResultSelect.do?callbackFun=selectReviewResult&toId=' + toId,
			type : 'iframe',
			width : 560,
			minHeight : 150
		});		
	});
});
function selectReviewResult(result){
	var toId = $('input:radio[name=toId]:checked').val();
	var url = "WarehouseTransferStatusManage.do?toId="+ toId +"&statusAction="+ result;
	window.location.href= url
}
function changeDisable(){
	var toId = $('input:radio[name=toId]:checked').val();
	var reviewStatus = $("#reviewStatus_"+toId).html();
	var waitApprove = "<%=WarehouseTransferOrderConfig.APPROVE_STATUS_MAP.get(WarehouseTransferOrderConfig.APPROVE_STATUS_WAIT_APPROVE)%>";
	if(reviewStatus == waitApprove ){				
		$('#review').removeAttr("disabled"); 
	}else{
	   $('#review').attr("disabled","disabled"); 
	}
}

</script>
</html>