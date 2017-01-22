<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"  %>
<%@ page import="com.lpmas.framework.config.*"  %>
<%@ page import="com.lpmas.framework.bean.*"  %>
<%@ page import="com.lpmas.framework.page.*"  %>
<%@ page import="com.lpmas.framework.util.*"  %>
<%@ page import="com.lpmas.framework.web.*"  %>
<%@ page import="com.lpmas.admin.bean.*"  %>
<%@page import="com.lpmas.erp.purchase.util.*"%>
<%@ page import="com.lpmas.admin.business.*"  %>
<%@ page import="com.lpmas.admin.config.*"  %>
<%@ page import="com.lpmas.erp.purchase.config.*"  %>
<%@ page import="com.lpmas.erp.config.*"  %>
<%@ page import="com.lpmas.erp.purchase.bean.*"  %>
<%@ page import="com.lpmas.srm.bean.*"  %>
<%@ page import="com.lpmas.erp.warehouse.bean.*"  %>
<%@ page import="com.lpmas.constant.info.*"  %>
<%@ include file="../../include/header.jsp" %>
<%
	AdminUserHelper adminUserHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	List<PurchaseOrderInfoBean> list = (List<PurchaseOrderInfoBean>)request.getAttribute("POList");
	PageBean PAGE_BEAN = (PageBean)request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>)request.getAttribute("CondList");
	int wareType = (Integer)request.getAttribute("WareType");
	List<SupplierInfoBean> supplierInfoList = (List<SupplierInfoBean>)request.getAttribute("SupplierInfoList");
	List<WarehouseInfoBean> warehouseInfoList = (List<WarehouseInfoBean>)request.getAttribute("WarehouseInfoList");
	Map<Integer,String> supplierInfoMap = (Map<Integer,String>)request.getAttribute("SupplierInfoMap");
	Map<Integer,String> warehouseInfoMap = (Map<Integer,String>)request.getAttribute("WarehouseInfoMap");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>采购订单管理</title>
<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL %>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.js"></script>
<link rel="stylesheet" href="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.css" type="text/css" media="screen" />
</head>
<body class="article_bg">
<p class="article_tit"><%=MapKit.getValueFromMap(wareType, PurchaseOrderConfig.WARE_TYPE_MAP)%>订单列表</p>
<form name="formSearch" method="post" action="PurchaseOrderInfoList.do?wareType=<%=wareType%>" onsubmit="javascript:disableDom.removeAttr('disabled')">
<div class="search_form">
			<table width="100%" border="0" cellpadding="0">
				<tr>
				<td><em class="em1">采购订单编号：</em> 
				<input type="text" name="poNumber" id="poNumber" value="<%=ParamKit.getParameter(request, "poNumber", "")%>" size="20" /> 
				</td>
				<td><em class="em1">采购类型：</em> 
				<select name="poType" id="poType">
				<option value=""></option>
				<%
					int poType = ParamKit.getIntParameter(request, "poType", 0);
					for (StatusBean<Integer, String> statusBean :PurchaseOrderConfig.PURCHASE_TYPE_LIST) {
				%>
				<option value="<%=statusBean.getStatus()%>"
					<%=(statusBean.getStatus() == poType) ? "selected" : ""%>><%=statusBean.getValue()%></option>
				<%
					}
				%>
				</select> 
				</td>
				<td><em class="em1">合同状态：</em> 
				<select name="contractStatus" id="contractStatus">
				<option value=""></option>
				<%
					String contractStatus = ParamKit.getParameter(request, "contractStatus", "");
					for (StatusBean<String, String> statusBean :PurchaseOrderConfig.PURCHASE_CONTRACT_STATUS_LIST) {
				%>
				<option value="<%=statusBean.getStatus()%>"
					<%=(statusBean.getStatus().equals(contractStatus)) ? "selected" : ""%>><%=statusBean.getValue()%></option>
				<%
					}
				%>
				</select>
				</td>
				<td>
				<em class="em1">发票状态：</em> 
				<select name="invoiceStatus" id="invoiceStatus">
				<option value=""></option>
				<%
					String invoiceStatus = ParamKit.getParameter(request, "invoiceStatus", "");
					for (StatusBean<String, String> statusBean :PurchaseOrderConfig.INVOICE_STATUS_LIST) {
				%>
				<option value="<%=statusBean.getStatus()%>"
					<%=(statusBean.getStatus().equals(invoiceStatus)) ? "selected" : ""%>><%=statusBean.getValue()%></option>
				<%
					}
				%>
				</select>
				</td>
				<td><em class="em1">货款状态：</em> 
				<select name="paymentStatus" id="paymentStatus">
				<option value=""></option>
				<%
					String paymentStatus = ParamKit.getParameter(request, "paymentStatus", "");
					for (StatusBean<String, String> statusBean :PurchaseOrderConfig.PAYMENT_STATUS_LIST) {
				%>
				<option value="<%=statusBean.getStatus()%>"
					<%=(statusBean.getStatus().equals(paymentStatus)) ? "selected" : ""%>><%=statusBean.getValue()%></option>
				<%
					}
				%>
				</select>
				</td>
				</tr>
				
				<tr>
				<td><em class="em1" style="margin-left: 15px">进度状态：</em> 
				<select name="poStatus" id="poStatus" style="margin-left: 14px">
				<option value=""></option>
				<%
					String poStatus = ParamKit.getParameter(request, "poStatus", "");
					for (StatusBean<String, String> statusBean :PurchaseOrderConfig.PO_STATUS_LIST) {
				%>
				<option value="<%=statusBean.getStatus()%>"
					<%=(statusBean.getStatus().equals(poStatus)) ? "selected" : ""%>><%=statusBean.getValue()%></option>
				<%
					}
				%>
				</select>
				</td>
				<td><em class="em1">审批状态：</em> 
				<select name="reviewStatus" id="reviewStatus">
				<option value=""></option>
				<%
					String reviewStatus = ParamKit.getParameter(request, "reviewStatus", "");
					for (StatusBean<String, String> statusBean :PurchaseOrderConfig.REVIEW_STATUS_LIST) {
				%>
				<option value="<%=statusBean.getStatus()%>"
					<%=(statusBean.getStatus().equals(reviewStatus)) ? "selected" : ""%>><%=statusBean.getValue()%></option>
				<%
					}
				%>
				</select>
				</td>
				<td><em class="em1" style="margin-left: 10px">供应商：</em> 
				<select name="supplierId" id="supplierId" style="margin-left: 6px">
				<option value=""></option>
				<%
					int supplierId = ParamKit.getIntParameter(request, "supplierId", 0);
					for (SupplierInfoBean bean :supplierInfoList) {
				%>
				<option value="<%=bean.getSupplierId()%>"
					<%=(bean.getSupplierId()==supplierId) ? "selected" : ""%>><%=bean.getSupplierName()%></option>
				<%
					}
				%>
				</select>
				</td>
				<%if(wareType == InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM){ %>
				<td><em class="em1">入库仓库：</em> 
				<select name="warehouseId" id="warehouseId">
				<option value=""></option>
				<%
					int warehouseId = ParamKit.getIntParameter(request, "warehouseId", 0);
					for (WarehouseInfoBean bean :warehouseInfoList) {
				%>
				<option value="<%=bean.getWarehouseId()%>"
					<%=(bean.getWarehouseId()==warehouseId) ? "selected" : ""%>><%=bean.getWarehouseName()%></option>
				<%
					}
				%>
				</select>
				</td>
				<%} %>
				<%if(adminUserHelper.hasPermission(PurchaseOrderResource.PURCHASE_ORDER_INFO, OperationConfig.SEARCH)){ %>
				<td><input name="" type="submit" class="search_btn_sub" value="查询" />&nbsp;&nbsp;
				<input name="" type="button" class="search_btn_sub" value="条件重置" onclick='location.href="PurchaseOrderInfoList.do?wareType=<%=wareType%>"' /></td>
				<%} %>
				</tr>
			</table>
			</div>
			<%String parentTab =ParamKit.getParameter(request, "screenType", "");
			String parentValue = ParamKit.getParameter(request, parentTab, "");
			if(StringKit.isValid(parentTab)&&!parentTab.equals("all")){%>
			<input name="<%=parentTab %>" type="hidden" class="search_btn_sub" value="<%=parentValue %>" />
			<%} %> 
			<input name="activeTab" type="hidden" class="search_btn_sub" value="<%=ParamKit.getParameter(request, "activeTab", "")%>" />
</form>
  <table width="100%" border="0" cellpadding="0" class="table_style">
    <tr>
      <th>选择</th>
      <th>采购订单编号</th>
      <th>采购类型</th>
      <th>供应商</th>
      <th>入库仓库</th>
      <th>审批状态</th>
      <th>进度</th>
      <th>合同</th>
      <th>发票</th>
      <th>货款</th>
      <th>操作</th>
    </tr>
    <%
    for(PurchaseOrderInfoBean bean:list){%> 
    <tr>
    	  <td><input type="radio" name="poId" id="radio_<%=bean.getPoId()%>" value="<%=bean.getPoId()%>" onclick="changeDisable()"></td>
      <td id="poNumber_<%=bean.getPoId() %>"><%=bean.getPoNumber() %></td>
      <td><%=MapKit.getValueFromMap(bean.getPurchaseType(), PurchaseOrderConfig.PURCHASE_TYPE_MAP) %></td>
      <td><%=MapKit.getValueFromMap(bean.getSupplierId(), supplierInfoMap) %></td>
      <td><%=MapKit.getValueFromMap(bean.getReceiverId(), warehouseInfoMap) == "" ? "--" : MapKit.getValueFromMap(bean.getReceiverId(), warehouseInfoMap) %></td>
      <td id="reviewStatus_<%=bean.getPoId()%>"><%=MapKit.getValueFromMap(bean.getApprovalStatus(), PurchaseOrderConfig.REVIEW_STATUS_MAP) %></td>
      <td><%=MapKit.getValueFromMap(bean.getPoStatus(), PurchaseOrderConfig.PO_STATUS_MAP)%></td>
      <td><%=MapKit.getValueFromMap(bean.getContractStatus(), PurchaseOrderConfig.PURCHASE_CONTRACT_STATUS_MAP)%></td>
      <td><%=MapKit.getValueFromMap(bean.getInvoiceStatus(), PurchaseOrderConfig.INVOICE_STATUS_MAP)%></td>
      <td><%=MapKit.getValueFromMap(bean.getPaymentStatus(), PurchaseOrderConfig.PAYMENT_STATUS_MAP)%></td>
      <td align="center">
      	<a href="/erp/PurchaseOrderInfoManage.do?poId=<%=bean.getPoId()%>&readOnly=true&wareType=<%=wareType%>">查看</a>
      	<%if(adminUserHelper.hasPermission(PurchaseOrderResource.PURCHASE_ORDER_INFO, OperationConfig.UPDATE)){ %>
	      	<%if(!PurchaseOrderStatusHelper.isLock(bean)) {%> 
	      	| <a href="/erp/PurchaseOrderInfoManage.do?poId=<%=bean.getPoId()%>&readOnly=false&wareType=<%=wareType%>">修改</a>
	      	<%} %>
	      	<%if (PurchaseOrderStatusHelper.committable(bean)){ %>
	      	| <a href="/erp/PurchaseOrderStatusManage.do?poId=<%=bean.getPoId()%>&statusAction=<%=PurchaseOrderConfig.PO_ACTION_COMMIT%>">提交审批</a>
	      	<%} %>
	      	<%if (PurchaseOrderStatusHelper.cancelCommittable(bean)){ %>
	      	| <a href="/erp/PurchaseOrderStatusManage.do?poId=<%=bean.getPoId()%>&statusAction=<%=PurchaseOrderConfig.PO_ACTION_CANCEL_COMMIT%>">取消审批</a>
	      	<%} %>
	      	<%if (PurchaseOrderStatusHelper.placeOrderable(bean)){ %>
	      	| <a href="/erp/PurchaseOrderStatusManage.do?poId=<%=bean.getPoId()%>&statusAction=<%=PurchaseOrderConfig.PO_ACTION_PLACE_ORDER%>">下单</a>
	      	<%} %>
	      	<%if (PurchaseOrderStatusHelper.cancelOrderable(bean)){ %>
	      	| <a href="/erp/PurchaseOrderStatusManage.do?poId=<%=bean.getPoId()%>&statusAction=<%=PurchaseOrderConfig.PO_ACTION_CANCEL_PLACE_ORDER%>">取消下单</a>
	      	<%} %>
	      	<%if (PurchaseOrderStatusHelper.receivable(bean)){ %>
	      	| <a href="/erp/PurchaseOrderStatusManage.do?poId=<%=bean.getPoId()%>&statusAction=<%=PurchaseOrderConfig.PO_ACTION_RECEIVE%>">收货</a>
	      	<%} %>
	      	<%if (PurchaseOrderStatusHelper.archivable(bean)){ %>
	      	| <a href="/erp/PurchaseOrderStatusManage.do?poId=<%=bean.getPoId()%>&statusAction=<%=PurchaseOrderConfig.PO_ACTION_ARCHIVE%>">归档</a>
	      	<%} %>
      	<%} %>
      </td>
    </tr>	
    <%} %>
  </table>
<ul class="page_info">
<li class="page_left_btn">
	<input type="hidden" name="wareType" id="wareType" value="<%=wareType %>"/>
	<%if(adminUserHelper.hasPermission(PurchaseOrderResource.PURCHASE_ORDER_INFO, OperationConfig.CREATE)){ %>
		<%if(wareType == InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM) {%>
	  	<input type="button" name="new" id="new" value="新建" >
		<%}else{ %>
	  	<input type="button" name="new" id="new" value="新建" onclick="javascript:location.href='PurchaseOrderInfoManage.do?wareType=<%=InfoTypeConfig.INFO_TYPE_MATERIAL%>'">
		<%} %> 
	<%} %>
  	<%if(adminUserHelper.hasPermission(PurchaseOrderResource.PURCHASE_ORDER_INFO, OperationConfig.REMOVE)){ %>
  	<input type="button" name="delete" id="delete" value="删除">
  	<%} %>
  	<%if(adminUserHelper.hasPermission(PurchaseOrderResource.PURCHASE_ORDER_INFO, OperationConfig.APPROVE)){ %>
  	<input type="button" name="review" id="review" disabled="disabled" value="审批">
  	<%} %>
</li>
<%@ include file="../../include/page.jsp" %>
</ul>
</body>
<script>
var disableDom = null;
$(document).ready(function() {
	$("#delete").click(function() {
		var poId = $('input:radio[name=poId]:checked').val();
		if (typeof(poId) == "undefined"){
		    alert("请选择需要删除的采购订单");
		    return;
		}
		var poNumber = $('#poNumber_'+poId).html();
		if(confirm("确定要删除采购订单"+poNumber+"吗?")){
			var url = "PurchaseOrderInfoRemove.do?poId="+ poId ;
			window.location.href= url
		 }
		
	});
	$("#review").click(function() {
		var poId = $('input:radio[name=poId]:checked').val();
		$.fancybox.open({
			href : 'PurchaseOrderReviewResultSelect.do?callbackFun=selectReviewResult&poId=' + poId,
			type : 'iframe',
			width : 560,
			minHeight : 150
		});
	});
});
function selectReviewResult(result){
	var poId = $('input:radio[name=poId]:checked').val();
	var url = "PurchaseOrderStatusManage.do?poId="+ poId +"&statusAction="+ result;
	window.location.href= url
}
function changeDisable(){
	var poId = $('input:radio[name=poId]:checked').val();
	var reviewStatus = $("#reviewStatus_"+poId).html();
	var waitApprove = "<%=PurchaseOrderConfig.REVIEW_STATUS_MAP.get(PurchaseOrderConfig.REVIEW_STATUS_WAIT_APPROVE)%>";
	if(reviewStatus == waitApprove ){				
		$('#review').removeAttr("disabled"); 
	}else{
		$('#review').attr("disabled","disabled");
	}
}
</script>
<%if(wareType == InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM){%>
<script type='text/javascript'>
$(document).ready(function() {
	$("#new").click(
		function() {
			$.fancybox.open({
				href : 'PurchaseTypeSelect.do?callbackFun=selectPurchaseType',
				type : 'iframe',
				width : 560,
				minHeight : 150
		});
	});
});
function selectPurchaseType(typeId) {
	var wareType = $("#wareType").val();
	var url = "PurchaseOrderInfoManage.do?wareType="+ wareType +"&purchaseType="+ typeId;
	window.location.href= url
}
</script>
<%}%> 
</html>