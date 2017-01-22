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
	List<DeliveryVoucherInfoBean> list = (List<DeliveryVoucherInfoBean>) request.getAttribute("DeliveryVoucherList");
	PageBean PAGE_BEAN = (PageBean) request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>) request.getAttribute("CondList");
	int wareType = (Integer) request.getAttribute("WareType");
	int dvType = (Integer) request.getAttribute("dvType");
	AdminUserInfoClientCache adminCache = new AdminUserInfoClientCache();
	List<WarehouseInfoBean> warehouseInfoList = (List<WarehouseInfoBean>) request.getAttribute("WarehouseInfoList");
	List<Integer> warehouseVoucherCreaterUserList = (List<Integer>) request.getAttribute("warehouseVoucherCreaterUserList");
	Map<Integer,String> warehouseInfoMap = (Map<Integer,String>)request.getAttribute("WarehouseInfoMap");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>出库单列表</title>
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
	<p class="article_tit"><%=MapKit.getValueFromMap(wareType, InventoryConsoleConfig.WARE_TYPE_MAP)%>出库单列表</p>
    <form name="formSearch" method="post" action="DeliveryVoucherInfoList.do?wareType=<%=wareType%>" onsubmit="javascript:disableDom.removeAttr('disabled')">
		<div class="search_form">
					<em class="em1">出库单编号：</em> 
					<input type="text" name="dvNumber" id="wvNumber" value="<%=ParamKit.getParameter(request, "dvNumber", "")%>"size="20" />
					<em class="em1">审批状态：</em> 
					<select name="reviewStatus" id="reviewStatus">
							<option value=""></option>
							<%
								String reviewStatus = ParamKit.getParameter(request, "reviewStatus", "");
								for (StatusBean<String, String> statusBean : DeliveryVoucherConfig.REVIEW_STATUS_LIST) {
							%>
							<option value="<%=statusBean.getStatus()%>"
								<%=(statusBean.getStatus().equals(reviewStatus)) ? "selected" : ""%>><%=statusBean.getValue()%></option>
							<%
								}
							%>
					</select>
					<em class="em1">进度状态：</em> 
					<select name="poStatus" id="poStatus">
							<option value=""></option>
							<%
								String poStatus = ParamKit.getParameter(request, "poStatus", "");
								for (StatusBean<String, String> statusBean : DeliveryVoucherConfig.DV_STATUS_LIST) {
							%>
							<option value="<%=statusBean.getStatus()%>"
								<%=(statusBean.getStatus().equals(poStatus)) ? "selected" : ""%>><%=statusBean.getValue()%></option>
							<%
								}
							%>
					</select>
					<em class="em1">创建人:</em> 
					<select name="createUser" id="createUser">
							<option value=""></option>
							<%
								int createUser = ParamKit.getIntParameter(request, "createUser", 0);
								for (Integer user : warehouseVoucherCreaterUserList) {
							%>
							<option value="<%=user%>"
								<%=(user == createUser) ? "selected" : ""%>><%=adminCache.getAdminUserNameByKey(user)%>
							</option>
							<%
								}
							%>
					</select>
					<em class="em1">出库类型：</em> 
					<select name="dvType">
							<option value="0"></option>
							<%for (StatusBean<Integer, String> statusBean : DeliveryVoucherConfig.DELIVERY_VOUCHER_TYPE_LIST) {%>
							<option value="<%=statusBean.getStatus()%>"
								<%=(statusBean.getStatus()==dvType) ? "selected" : ""%>><%=statusBean.getValue()%></option>
							<%}%>
					</select>
					<em class="em1">出库仓库：</em> 
					<select name="warehouseId" id="warehouseId">
							<option value=""></option>
							<%
								int warehouseId = ParamKit.getIntParameter(request, "warehouseId", 0);
								for (WarehouseInfoBean bean : warehouseInfoList) {
							%>
							<option value="<%=bean.getWarehouseId()%>"
								<%=(bean.getWarehouseId() == warehouseId) ? "selected" : ""%>><%=bean.getWarehouseName()%></option>
							<%
								}
							%>
					</select>
					<em class="em1">源单号：</em> 
					<input type="text" name="sourceOrderNumber" id="sourceOrderNumber" value="<%=ParamKit.getParameter(request, "sourceOrderNumber", "")%>" size="20" />
					<%
						if (adminUserHelper.hasPermission(InventoryResource.DELIVERY_VOUCHER_INFO,OperationConfig.SEARCH)) {
					%>
					<input name="" type="submit" class="search_btn_sub"value="查询" />&nbsp;&nbsp; 
					    <input name="" type="button" class="search_btn_sub" value="条件重置" onclick='location.href="DeliveryVoucherInfoList.do?wareType=<%=wareType%>"' />
					
					<%
						}
					%>
		</div>
	</form>
	<table width="100%" border="0" cellpadding="0" class="table_style">
		<tr>
			<th>选择</th>
			<th>出库单号</th>
			<th>出库时间</th>
			<th>出库仓库</th>
			<th>审批状态</th>
			<th>进度</th>
			<th>操作</th>
		</tr>
		<%
			for (DeliveryVoucherInfoBean bean : list) {
		%>
		<tr>
			<td><input type="radio" name="dvId" id="radio_<%=bean.getDvId()%>" value="<%=bean.getDvId()%>" onclick="changeDisable()"></td>
			<td id="dvNumber_<%=bean.getDvId()%>"><%=bean.getDvNumber()%></td>
			<td><%=DateKit.formatTimestamp(bean.getStockOutTime(), "yyyy-MM-dd")%></td>
			<td><%=MapKit.getValueFromMap(bean.getWarehouseId(), warehouseInfoMap)%></td>
			<td id="reviewStatus_<%=bean.getDvId()%>"><%=MapKit.getValueFromMap(bean.getApproveStatus(), DeliveryVoucherConfig.REVIEW_STATUS_MAP)%></td>
			<td><%=MapKit.getValueFromMap(bean.getDvStatus(), DeliveryVoucherConfig.DV_STATUS_MAP)%></td>
			<td align="center">
			   <a href="/erp/DeliveryVoucherInfoManage.do?dvId=<%=bean.getDvId()%>&wareType=<%=wareType%>&readOnly=true">查看</a>
				<%if (adminUserHelper.hasPermission(InventoryResource.DELIVERY_VOUCHER_INFO,OperationConfig.UPDATE)) {%>
				<%if (!DeliveryVoucherStatusHelper.isLock(bean)) {%>
				 | <a href="/erp/DeliveryVoucherInfoManage.do?dvId=<%=bean.getDvId()%>&wareType=<%=wareType%>&readOnly=false">修改</a>
				<%}%> 
				<%if (DeliveryVoucherStatusHelper.committable(bean)) {%>
				 | <a href="/erp/DeliveryVoucherStatusManage.do?dvId=<%=bean.getDvId()%>&statusAction=<%=DeliveryVoucherConfig.DV_ACTION_COMMIT%>">提交审批</a>
				<%}%> 
				<%if (DeliveryVoucherStatusHelper.cancelCommittable(bean)) {%>
				 | <a href="/erp/DeliveryVoucherStatusManage.do?dvId=<%=bean.getDvId()%>&statusAction=<%=DeliveryVoucherConfig.DV_ACTION_CANCEL_COMMIT%>">取消审批</a>
				<%}%>
				<%if (DeliveryVoucherStatusHelper.closable(bean)) {%>
				 | <a href="/erp/DeliveryVoucherStatusManage.do?dvId=<%=bean.getDvId()%>&statusAction=<%=DeliveryVoucherConfig.DV_ACTION_CLOSE%>">关闭</a>
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
		  <%if (adminUserHelper.hasPermission(InventoryResource.DELIVERY_VOUCHER_INFO,OperationConfig.CREATE)) {%>
            <label>出库类型：</label> 
			<select id="dvType">
					<%for (StatusBean<Integer, String> statusBean : DeliveryVoucherConfig.DELIVERY_VOUCHER_TYPE_LIST) {%>
						<%if(statusBean.getStatus()!=DeliveryVoucherConfig.DVT_TRANSFER){ %>
							<option value="<%=statusBean.getStatus()%>"<%=(statusBean.getStatus()==dvType) ? "selected" : ""%>><%=statusBean.getValue()%></option>
						<%} %>
					<%}%>
			</select>
		    <input type="button" name="new" id="new" value="新建" onclick="javascript:var dvType=$('#dvType').val();location.href='DeliveryVoucherInfoManage.do?wareType=<%=wareType%>&dvType='+dvType">
		  <%}%> 
		  <%
 	        if (adminUserHelper.hasPermission(InventoryResource.DELIVERY_VOUCHER_INFO,OperationConfig.REMOVE)) {
          %>
			<input type="button" name="delete" id="delete" value="删除"> 
		  <%
 	          }
          %><%
		    if (adminUserHelper.hasPermission(InventoryResource.DELIVERY_VOUCHER_INFO,OperationConfig.APPROVE)) {
		  %>
			<input type="button" name="review" id="review" disabled="disabled"value="审批"> 
		  <%
 	         }
          %></li>
		<%@ include file="../../include/page.jsp"%>
	</ul>
</body>
<script>
var disableDom = null;
$(document).ready(function() {
	$("#delete").click(function() {
		var dvId = $('input:radio[name=dvId]:checked').val();
		if (typeof(dvId) == "undefined"){
		    alert("请选择需要删除的出库单");
		    return;
		}
		var dvNumber = $('#dvNumber_'+dvId).html();
		if(confirm("确定要删除出库单"+dvNumber+"吗?")){
			var url = "DeliveryVoucherInfoRemove.do?dvId="+ dvId ;
			window.location.href= url
		 }
		
	});	
	$("#review").click(function() {
		var dvId = $('input:radio[name=dvId]:checked').val();				
		$.fancybox.open({
			href : 'DeliveryVoucherReviewResultSelect.do?callbackFun=selectReviewResult&dvId=' + dvId,
			type : 'iframe',
			width : 560,
			minHeight : 150
		});		
	});
});
function selectReviewResult(result){
	var dvId = $('input:radio[name=dvId]:checked').val();
	var url = "DeliveryVoucherStatusManage.do?dvId="+ dvId +"&statusAction="+ result;
	window.location.href= url
}
function changeDisable(){
	var dvId = $('input:radio[name=dvId]:checked').val();
	var reviewStatus = $("#reviewStatus_"+dvId).html();
	var waitApprove = "<%=DeliveryVoucherConfig.REVIEW_STATUS_MAP.get(DeliveryVoucherConfig.REVIEW_STATUS_WAIT_APPROVE)%>";
	if(reviewStatus == waitApprove ){				
		$('#review').removeAttr("disabled"); 
	}else{
	   $('#review').attr("disabled","disabled"); 
	}
}

</script>
</html>