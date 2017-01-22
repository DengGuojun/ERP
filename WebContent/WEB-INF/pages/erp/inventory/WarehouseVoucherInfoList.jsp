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
	List<WarehouseVoucherInfoBean> list = (List<WarehouseVoucherInfoBean>) request.getAttribute("WarehouseVoucherList");
	PageBean PAGE_BEAN = (PageBean) request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>) request.getAttribute("CondList");
	int wareType = (Integer) request.getAttribute("WareType");
	int wvType = (Integer) request.getAttribute("WvType");
	AdminUserInfoClientCache adminCache = new AdminUserInfoClientCache();
	List<WarehouseInfoBean> warehouseInfoList = (List<WarehouseInfoBean>) request.getAttribute("WarehouseInfoList");
	List<Integer> warehouseVoucherCreaterUserList = (List<Integer>) request.getAttribute("warehouseVoucherCreaterUserList");
	Map<Integer,String> supplierInfoMap = (Map<Integer,String>)request.getAttribute("SupplierInfoMap");
	Map<Integer,String> warehouseInfoMap = (Map<Integer,String>)request.getAttribute("WarehouseInfoMap");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>入库管理</title>
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
	<p class="article_tit"><%=MapKit.getValueFromMap(wareType, InventoryConsoleConfig.WARE_TYPE_MAP)%>入库单列表</p>
	<form name="formSearch" method="post" action="WarehouseVoucherInfoList.do?wareType=<%=wareType%>" onsubmit="javascript:disableDom.removeAttr('disabled')">
		<div class="search_form">
						<em class="em1">入库单编号：</em> 
						<input type="text"name="wvNumber" id="wvNumber" value="<%=ParamKit.getParameter(request, "wvNumber", "")%>"size="20" />
						<em class="em1">审批状态：</em> 
						<select name="reviewStatus" id="reviewStatus">
							<option value=""></option>
							<%
								String reviewStatus = ParamKit.getParameter(request, "reviewStatus", "");
								for (StatusBean<String, String> statusBean : WarehouseVoucherConfig.REVIEW_STATUS_LIST) {
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
								for (StatusBean<String, String> statusBean : WarehouseVoucherConfig.WV_STATUS_LIST) {
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
						<em class="em1">入库仓库：</em> 
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
						<em class="em1">入库类型：</em>
						<select name="wvType">
						<option value="0"></option>
						<%for(StatusBean<Integer, String> bean : WarehouseVoucherConfig.WAREHOUSE_VOUCHER_TYPE_LIST){ %>
						<option value="<%=bean.getStatus()%>" <%=bean.getStatus()==wvType ? "selected":"" %>><%=bean.getValue() %></option>
						<%} %>
						</select>
						<em class="em1">源单号：</em> 
						<input type="text" name="sourceOrderNumber" id="sourceOrderNumber" value="<%=ParamKit.getParameter(request, "sourceOrderNumber", "")%>" size="20" />
					<%if (adminUserHelper.hasPermission(InventoryResource.WAREHOUSE_VOUCHER_INFO,OperationConfig.SEARCH)) {%>
						<input name="" type="submit" class="search_btn_sub" value="查询" />&nbsp;&nbsp; 
						<input name="" type="button" class="search_btn_sub" value="条件重置" onclick='location.href="WarehouseVoucherInfoList.do?wareType=<%=wareType%>"' />
					<%}%>
		</div>
	</form>
	<table width="100%" border="0" cellpadding="0" class="table_style">
		<tr>
			<th>选择</th>
			<th>入库单号</th>
			<th>入库时间</th>
			<th>供应商</th>
            <th>入库仓库</th>
			<th>审批状态</th>
			<th>进度</th>
			<!-- <th>同步NC</th> -->
			<th>操作</th>
		</tr>
		<%
			for (WarehouseVoucherInfoBean bean : list) {
		%>
		<tr>
			<td>
				<input type="radio" name="wvId" id="radio_<%=bean.getWvId()%>" value="<%=bean.getWvId()%>"onclick="changeDisable()">
			</td>
			<td id="wvNumber_<%=bean.getWvId()%>"><%=bean.getWvNumber()%></td>
			<td><%=DateKit.formatTimestamp(bean.getStockInTime(), "yyyy-MM-dd")%></td>
			<td><%=MapKit.getValueFromMap(bean.getSupplierId(), supplierInfoMap) == "" ? "--" : MapKit.getValueFromMap(bean.getSupplierId(), supplierInfoMap)%></td>
            <td><%=MapKit.getValueFromMap(bean.getWarehouseId(), warehouseInfoMap)%></td>
			<td id="reviewStatus_<%=bean.getWvId()%>"><%=MapKit.getValueFromMap(bean.getApproveStatus(), WarehouseVoucherConfig.REVIEW_STATUS_MAP)%></td>
			<td><%=MapKit.getValueFromMap(bean.getWvStatus(), WarehouseVoucherConfig.WV_STATUS_MAP)%></td>
			<%-- <td><%=MapKit.getValueFromMap(bean.getSyncStatus(), WarehouseVoucherConfig.SYNC_STATUS_MAP)%></td> --%>
			<td align="center"><a href="/erp/WarehouseVoucherInfoManage.do?wvId=<%=bean.getWvId()%>&wareType=<%=wareType%>&readOnly=true">查看</a>
				<%if (adminUserHelper.hasPermission(InventoryResource.WAREHOUSE_VOUCHER_INFO, OperationConfig.UPDATE)) {%>
					<%if (!WarehouseVoucherStatusHelper.isLock(bean)) {%> 
					| <a href="/erp/WarehouseVoucherInfoManage.do?wvId=<%=bean.getWvId()%>&wareType=<%=wareType%>&readOnly=false">修改</a>
					<%}%> 
					<%if (WarehouseVoucherStatusHelper.committable(bean)) {%> 
					| <a href="/erp/WarehouseVoucherStatusManage.do?wvId=<%=bean.getWvId()%>&statusAction=<%=WarehouseVoucherConfig.WV_ACTION_COMMIT%>">提交审批</a>
					<%}%> 
					<%if (WarehouseVoucherStatusHelper.cancelCommittable(bean)) {%> 
					| <a href="/erp/WarehouseVoucherStatusManage.do?wvId=<%=bean.getWvId()%>&statusAction=<%=WarehouseVoucherConfig.WV_ACTION_CANCEL_COMMIT%>">取消审批</a>
					<%}%> 
					<%if (WarehouseVoucherStatusHelper.closable(bean)) {%> 
					| <a href="/erp/WarehouseVoucherStatusManage.do?wvId=<%=bean.getWvId()%>&statusAction=<%=WarehouseVoucherConfig.WV_ACTION_CLOSE%>">上架</a>
					<%}
					%> 
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
			<%if (adminUserHelper.hasPermission(InventoryResource.WAREHOUSE_VOUCHER_INFO, OperationConfig.CREATE)) { %>
				<label>入库类型：</label>
				<select id="wvType">
				<%for(StatusBean<Integer, String> bean : WarehouseVoucherConfig.WAREHOUSE_VOUCHER_TYPE_LIST){ %>
					<%if(bean.getStatus()!=WarehouseVoucherConfig.WVT_TRANSFER){ %>
						<option value="<%=bean.getStatus()%>" <%=bean.getStatus()==wvType ? "selected":"" %>><%=bean.getValue() %></option>
					<%} %>
				<%} %>
				</select>
				<input type="button" name="new" id="new" value="新建" onclick="javascript:var wvType = $('#wvType').val();location.href='WarehouseVoucherInfoManage.do?wareType=<%=wareType%>&wvType='+wvType">
			<%}%> 
			<%if (adminUserHelper.hasPermission(InventoryResource.WAREHOUSE_VOUCHER_INFO, OperationConfig.REMOVE)) {%>
				<input type="button" name="delete" id="delete" value="删除"> 
			<%}%>
			<%if (adminUserHelper.hasPermission(InventoryResource.WAREHOUSE_VOUCHER_INFO, OperationConfig.APPROVE)) { %>
			<input type="button" name="review" id="review" disabled="disabled" value="审批"> 
			<%}%>
		</li>
		<%@ include file="../../include/page.jsp"%>
	</ul>
</body>
<script>
var disableDom = null;
$(document).ready(function() {
	$("#delete").click(function() {
		var wvId = $('input:radio[name=wvId]:checked').val();
		if (typeof(wvId) == "undefined"){
		    alert("请选择需要删除的入库单");
		    return;
		}
		var wvNumber = $('#wvNumber_'+wvId).html();
		if(confirm("确定要删除入库单"+wvNumber+"吗?")){
			var url = "WarehouseVoucherInfoRemove.do?wvId="+ wvId ;
			window.location.href= url
		 }
		
	});	
	$("#review").click(function() {
		var wvId = $('input:radio[name=wvId]:checked').val();				
		$.fancybox.open({
			href : 'WarehouseVoucherReviewResultSelect.do?callbackFun=selectReviewResult&wvId=' + wvId,
			type : 'iframe',
			width : 560,
			minHeight : 150
		});		
	});
});
function selectReviewResult(result){
	var wvId = $('input:radio[name=wvId]:checked').val();
	var url = "WarehouseVoucherStatusManage.do?wvId="+ wvId +"&statusAction="+ result;
	window.location.href= url
}
function changeDisable(){
	var wvId = $('input:radio[name=wvId]:checked').val();
	var reviewStatus = $("#reviewStatus_"+wvId).html();
	var waitApprove = "<%=WarehouseVoucherConfig.REVIEW_STATUS_MAP.get(WarehouseVoucherConfig.REVIEW_STATUS_WAIT_APPROVE)%>";
	if(reviewStatus == waitApprove ){				
		$('#review').removeAttr("disabled"); 
	}else{
	   $('#review').attr("disabled","disabled"); 
	}
}

</script>
</html>