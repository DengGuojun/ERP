<%@page import="com.lpmas.tms.config.TmsConfig"%>
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
<%@ page import="com.lpmas.tms.transporter.bean.*"%>
<%@ page import="com.lpmas.tms.transporter.business.*"%>
<%@ include file="../../include/header.jsp"%>
<%
	DeliveryNoteInfoBean deliveryInfoBean = (DeliveryNoteInfoBean)request.getAttribute("DeliveryInfoBean");
	PurchaseOrderInfoEntityBean orderInfoEntityBean = (PurchaseOrderInfoEntityBean)request.getAttribute("OrderInfoEntityBean");
	List<PurchaseOrderInfoBean> orderInfoList = (List<PurchaseOrderInfoBean>)request.getAttribute("OrderItemList");
	int dnId = ParamKit.getIntParameter(request, "dnId", 0);
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	int poId = orderInfoEntityBean.getPoId();
	TransporterInfoMediator transporterInfoMediator = (TransporterInfoMediator) request
			.getAttribute("TransporterInfoMediator");
	List<TransporterInfoBean> transporterInfoList = transporterInfoMediator
			.getTransporterInfoListByType(deliveryInfoBean.getTransporterType());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发货单明细</title>
<link href="<%=STATIC_URL%>/css/main.css" type="text/css"
	rel="stylesheet" />
<script type="text/javascript" src="../js/erp-common.js"></script>
<script type="text/javascript" src="<%=STATIC_URL%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=STATIC_URL%>/js/common.js"></script>
<script type="text/javascript" src="<%=STATIC_URL%>/js/ui.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.js"></script>
<link rel="stylesheet" href="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.css" type="text/css" media="screen" />
<script type="text/javascript" src="<%=TMS_URL%>/transporter/TransporterAjaxList.do"></script>
<script type="text/javascript" src="<%=STATIC_URL%>/js/My97DatePicker/WdatePicker.js"></script>
</head>
<body class="article_bg">
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a> 
		<ul class="art_nav">
			<li><a href="PurchaseOrderInfoList.do?wareType=<%=orderInfoEntityBean.getWareType()%>"><%=MapKit.getValueFromMap(orderInfoEntityBean.getWareType(), PurchaseOrderConfig.WARE_TYPE_MAP)%>订单列表</a>&nbsp;>&nbsp;</li>
			<li><%=orderInfoEntityBean.getPoNumber() %>&nbsp;>&nbsp;</li>
			<li><a href="DeliveryNoteInfoList.do?poId=<%=poId%>">收发货记录列表</a>&nbsp;>&nbsp;</li>
			<%if(dnId>0){ %>
			<li>发货单明细 <%=deliveryInfoBean.getDnNumber() %></li>
			<%}else{ %>
			<li>新建发货单</li>
			<%} %>
			
		</ul>
	</div>
	<form id="formData" name="formData" method="post" action="DeliveryNoteInfoManage.do?dnId=<%=dnId%>&poId=<%=poId%>" 
	<%if(dnId>0){ %>
	onsubmit="javascript:return checkForm('formData');"
	<%}else{ %>
	onsubmit="javascript:return submitCheck();"
	<%} %>>
	<p>基础信息</p>
		<div class="modify_form">
			<p>
				<em class="int_label">供应商：</em>
				<input size="50" disabled="disabled" type="text" value="<%=orderInfoEntityBean.getSupplier()%>" />
			</p>
			<p>
				<em class="int_label">供应商地址：</em>
				<input size="50" disabled="disabled" type="text" value="<%=orderInfoEntityBean.getCompleteConsignerAddress()%>" />
			</p>
			<p>
				<em class="int_label"><span>*</span>发货时间：</em>
				<input type="hidden" id="poCreateTime" value="<%=orderInfoEntityBean.getCreateTime()%>">
				<%if(dnId>0){ %>
				<input size="50" readOnly name="deliveryTime" onchange="checkTime(this)"  type="text" checkStr="发货时间;date;true;;100" value="<%=DateKit.formatTimestamp(deliveryInfoBean.getDeliveryTime(),DateKit.DEFAULT_DATE_TIME_FORMAT)%>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
				<%}else{ %>
				<input size="50" readOnly name="deliveryTime" onchange="checkTime(this)"  type="text" checkStr="发货时间;date;true;;100" value="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
				<%} %>
			</p>
			<p>
				<em class="int_label">联系电话：</em>
				<input size="50" disabled="disabled" type="text" value="<%=orderInfoEntityBean.getConsignerTelephone()%>" />
			</p>
			<p>
	    	<em class="int_label"><span>*</span>配送方类型：</em>    
	      	<select name="transporterType" id="transporterType" onchange="javascript:getTransporterNameList('transporterType','transporterId','<%=deliveryInfoBean==null?"":deliveryInfoBean.getTransporterType()%>');">
	      		<option></option>
	      		<%
					for (StatusBean<Integer, String> ttBean : TmsConfig.TRANSPORTER_TYPE_LIST) {
				%>
				<option value="<%=ttBean.getStatus()%>" <%=(deliveryInfoBean != null && ttBean.getStatus() == deliveryInfoBean.getTransporterType()) ? "selected" : ""%>><%=ttBean.getValue()%></option>
				<%
					}
				%>
	      	</select>
	    </p>
	    <p>
	    	<em class="int_label"><span>*</span>配送方：</em>    
	      	<select name="transporterId" id="transporterId">
			<option></option>
			<%
				for (TransporterInfoBean transporterInfoBean : transporterInfoList) {
			%>
			<option value="<%=transporterInfoBean.getTransporterId()%>" <%=(transporterInfoBean.getTransporterId() == deliveryInfoBean.getTransporterId()) ? "selected" : ""%>><%=transporterInfoBean.getTransporterName()%></option>
			<%
				}
			%>
	      	</select>
	    </p>
	    <p>
	    <em class="int_label">运单号：</em> 
	    <%if(dnId>0){ %>
	    <input type="hidden" name="dnNumber" value="<%=deliveryInfoBean.getDnNumber()%>">
	    <input type="hidden" name="receivingStatus" value="<%=deliveryInfoBean.getReceivingStatus()%>">
	    <input type="hidden" name="receivingTime" value="<%=deliveryInfoBean.getReceivingTime()%>">
	    <input type="text" checkStr="运单号;txt;false;;100" name="transportNumber" id="transportNumber" value="<%=deliveryInfoBean.getTransportNumber()%>">
		<%}else{ %>
		<input type="text" checkStr="运单号;txt;false;;100" name="transportNumber" id="transportNumber" value="">
		<%} %>
	    </p>
	    <input type="hidden" name="poId" value="<%=poId%>">
	    <input type="hidden" name="dnId" value="<%=dnId%>">
		</div>
		<% boolean isModify = false;
		if(PurchaseOrderStatusHelper.receivable(orderInfoEntityBean.getPurchaseOrderInfoBean())) {
			isModify = true;
		}%>
			<%if(orderInfoEntityBean.getWareType()==InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM){ %>
  			<%=DetailDisplayUtil.getDisplayStr(PurchaseOrderItemDisplayConfig.PRODUCT_PO_DETAIL,poId, dnId, isModify, PurchaseOrderItemDisplayConfig.DELIVERY_DETAIL) %>
  		<%}if(orderInfoEntityBean.getWareType()==InfoTypeConfig.INFO_TYPE_MATERIAL){ %>
  			<%=DetailDisplayUtil.getDisplayStr(PurchaseOrderItemDisplayConfig.MATERIAL_PO_DETAIL,poId, dnId, isModify, PurchaseOrderItemDisplayConfig.DELIVERY_DETAIL) %>
  		<%} %>
  		<%if(PurchaseOrderStatusHelper.receivable(orderInfoEntityBean.getPurchaseOrderInfoBean())) {%>
		<div class="div_center">
			<%String buttonString = "新建";
			if(dnId>0){buttonString="修改";} %>
			<input type="submit" name="modifyBtn" id="modifyBtn" value="<%=buttonString%>">
			<input type="button" name="cancel" id="cancel" value="返回" onclick="javascript:window.close()">
		</div>
      	<%}else{ %>
      	<div class="div_center">
      	<input type="button" name="cancel" id="cancel" value="返回" onclick="javascript:window.close()">
      	</div>
      	<%} %>
      	
</form>
<script type="text/javascript">
$(document).ready(function(){
	var readOnly = '<%=readOnly%>';
	if(readOnly=='true') {
		disablePageElement();
	}
});
function transproterSelection(){
	var transporterType = $("#transporterType").val();
	var transporterId = $("#transporterId").val();
	var params={  
	        'transporterTypeId':transporterType  
	    };  
	    $.ajax({
	        type: 'get',
	        url: "/erp/TransporterInfoSelect.do",
	        data: params,
	        dataType: 'json',
	        success: function(data){
		      	var sel2 = $("#transporterId");  
	      		sel2.empty();  
		      	if(data==null) {
	      		 	sel2.append("<option value = '-1' >"+"配送方信息为空"+"</option>");
	          	}else {
	          		var items=data;
		    	   		if(items!=null) {
		        	  		for(var i =0;i<items.length;i++) {
		          		var item=items[i];
		          			if(item.companyId == transporterId){
			          			sel2.append("<option value = '"+item.companyId+"' selected>"+item.companyName+"</option>");
			          		}else{
			          			sel2.append("<option value = '"+item.companyId+"' >"+item.companyName+"</option>");
			          		}
		            };
		        } else{
		       		sel2.empty();  
		          }
	          	}
		      	
	        },
	        error: function(){
	            return;
	        }
	    });
}
function submitCheck(){
	var table = $('#detail');
	var isOk = false;
	var quantiry = table.find('input[name^=deliveryQuantiry]');
	quantiry.each(function(){
		if(parseFloat($(this).val())!=0){
			isOk=true;
			return false;
		}
	});
	
	if(isOk){
		return checkForm('formData');
	}else{
		alert("新建发货单时不能所有发货数量都为0");
		return false;
	}
}
function checkTime(ele){
	var poCreateTime =  new Date($('#poCreateTime').val());
	var selectedTime =  new Date($(ele).val());
	if(selectedTime<=poCreateTime){
		alert("发货时间不能早于采购单建立时间!");
		$(ele).val("");
	}
}
</script>
</body>
</html>