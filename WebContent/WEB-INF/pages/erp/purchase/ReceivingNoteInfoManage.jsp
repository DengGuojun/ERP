<%@page import="com.lpmas.admin.client.cache.AdminUserInfoClientCache"%>
<%@page import="com.lpmas.log.bean.DataLogBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.lpmas.framework.config.*"%>
<%@ page import="com.lpmas.framework.util.*"%>
<%@ page import="com.lpmas.framework.bean.StatusBean"%>
<%@ page import="com.lpmas.admin.bean.*"%>
<%@ page import="com.lpmas.admin.business.*"%>
<%@ page import="com.lpmas.framework.web.*"%>
<%@ page import="com.lpmas.constant.info.*"%>
<%@ page import="com.lpmas.erp.purchase.bean.*"%>
<%@ page import="com.lpmas.erp.purchase.config.*"  %>
<%@ page import="com.lpmas.erp.purchase.util.*"  %>
<%@ include file="../../include/header.jsp"%>
<%
	DeliveryNoteInfoBean deliveryInfoBean = (DeliveryNoteInfoBean)request.getAttribute("DeliveryNoteInfoBean");
	PurchaseOrderInfoBean orderInfoBean = (PurchaseOrderInfoBean)request.getAttribute("PurchaseOrderInfoBean");
	ReceivingNoteInfoBean receivingNoteInfoBean = (ReceivingNoteInfoBean)request.getAttribute("ReceivingNoteInfoBean");
	int dnId = ParamKit.getIntParameter(request, "dnId", 0);
	int poId = orderInfoBean.getPoId();
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	request.setAttribute("readOnly", readOnly);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>收货单明细</title>
<link href="<%=STATIC_URL%>/css/main.css" type="text/css"
	rel="stylesheet" />
<script type="text/javascript" src="../js/erp-common.js"></script>
<script type="text/javascript" src="<%=STATIC_URL%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=STATIC_URL%>/js/common.js"></script>
<script type="text/javascript" src="<%=STATIC_URL%>/js/ui.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.js"></script>
<link rel="stylesheet" href="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.css" type="text/css" media="screen" />
<script type="text/javascript"
	src="<%=STATIC_URL%>/js/My97DatePicker/WdatePicker.js"></script>
</head>
<body class="article_bg">
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a> 
		<ul class="art_nav">
			<li><a href="PurchaseOrderInfoList.do?wareType=<%=orderInfoBean.getWareType()%>"><%=MapKit.getValueFromMap(orderInfoBean.getWareType(), PurchaseOrderConfig.WARE_TYPE_MAP)%>订单列表</a>&nbsp;>&nbsp;</li>
			<li><%=orderInfoBean.getPoNumber() %>&nbsp;>&nbsp;</li>
			<li><a href="DeliveryNoteInfoList.do?poId=<%=poId%>">收发货记录列表</a>&nbsp;>&nbsp;</li>
			<%if(receivingNoteInfoBean.getRnId()>0){ %>
			<li>收货单明细 <%=receivingNoteInfoBean.getRnNumber() %></li>
			<%}else{ %>
			<li>新建收货单</li>
			<%} %>
			
		</ul>
	</div>
	<form id="formData" name="formData" method="post" action="ReceivingNoteInfoManage.do?dnId=<%=dnId%>" onsubmit="javascript:return checkForm('formData');">
	<p>基础信息</p>
		<div class="modify_form">
			<p>
				<em class="int_label">收货人：</em>
				<input size="50" disabled type="text" value="<%=orderInfoBean.getReceiverName()%>" />
			</p>
			<p>
				<em class="int_label">收货地址：</em>
				<input size="50" disabled type="text" value="<%=orderInfoBean.getCompleteReceiverAddress()%>" />
			</p>
			<p>
				<em class="int_label"><span>*</span>收货时间：</em>
				<input type="hidden" id="deliveryTime" value="<%=deliveryInfoBean.getDeliveryTime()%>">
				<%if(receivingNoteInfoBean.getReceivingTime()!=null){ %>
				<input size="50" readOnly name="receivingTime" onchange="checkTime(this)" checkStr="收货时间;date;true;;100"  type="text" value="<%=DateKit.formatTimestamp(receivingNoteInfoBean.getReceivingTime(),DateKit.DEFAULT_DATE_TIME_FORMAT)%>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
				<%}else{ %>
				<input size="50" readOnly name="receivingTime" onchange="checkTime(this)" checkStr="收货时间;date;true;;100"  type="text" value="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
				<%} %>
			</p>
			<p>
				<em class="int_label">联系电话：</em>
				<input size="50" disabled type="text" value="<%=orderInfoBean.getReceiverTelephone()%>" />
			</p>
			<input type="hidden" name="rnNumber" value="<%=receivingNoteInfoBean.getRnNumber()%>">
		</div>
		<% boolean isModify = false;
			if(PurchaseOrderStatusHelper.receivable(orderInfoBean)) {
				isModify = true;
			}%>
		<%if(orderInfoBean.getWareType()==InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM){ %>
	  		<%=DetailDisplayUtil.getDisplayStr(PurchaseOrderItemDisplayConfig.PRODUCT_PO_DETAIL,poId, dnId, isModify,PurchaseOrderItemDisplayConfig.RECEVING_DETAIL) %>
	  	<%}if(orderInfoBean.getWareType()==InfoTypeConfig.INFO_TYPE_MATERIAL){ %>
	  		<%=DetailDisplayUtil.getDisplayStr(PurchaseOrderItemDisplayConfig.MATERIAL_PO_DETAIL,poId, dnId, isModify,PurchaseOrderItemDisplayConfig.RECEVING_DETAIL) %>
	  	<%} %>
	  	<%if(PurchaseOrderStatusHelper.receivable(orderInfoBean)) {%>
		<div class="div_center">
			<%String buttonString = "新建";
			if(receivingNoteInfoBean.getRnId()>0){buttonString="修改";} %>
			<input type="submit" name="modifyBtn" id="modifyBtn" value="<%=buttonString%>">
			<input type="button" name="cancel" id="cancel" value="返回" onclick="javascript:window.close()">
		</div>
		<%}else{ %>
      	<div class="div_center">
      	<input type="button" name="cancel" id="cancel" value="返回" onclick="javascript:window.close()">
      	</div>
      	<%} %>
</form>
</body>
<script>
$(document).ready(function(){
	var readOnly = '<%=readOnly%>';
	if(readOnly=='true') {
		disablePageElement();
	}
});
function checkTime(ele){
	var deliveryTime =  new Date($('#deliveryTime').val());
	var selectedTime =  new Date($(ele).val());
	if(selectedTime<=deliveryTime){
		alert("收货时间不能早于发货时间!");
		$(ele).val("");
	}
}
</script>
</html>