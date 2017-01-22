<%@page import="com.lpmas.pdm.config.PdmConfig"%>
<%@page import="com.lpmas.framework.web.ParamKit"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"  %>
<%@ page import="com.lpmas.framework.config.*"  %>
<%@ page import="com.lpmas.framework.bean.StatusBean" %>
<%@ page import="com.lpmas.framework.util.*"  %>
<%@ page import="com.lpmas.constant.info.*"  %>
<%@ page import="com.lpmas.admin.bean.*"  %>
<%@ page import="com.lpmas.admin.business.*"  %>
<%@ page import="com.lpmas.erp.purchase.bean.*"  %>
<%@ page import="com.lpmas.erp.purchase.config.*"  %>
<%@ page import="com.lpmas.erp.purchase.util.*"  %>
<%@ page import="com.lpmas.srm.bean.*"  %>
<%@ include file="../../include/header.jsp" %>
<% 
	PurchaseOrderInfoEntityBean bean = (PurchaseOrderInfoEntityBean)request.getAttribute("PurchaseOrderInfoEntity");
	int poId = bean.getPoId();
	AdminUserHelper adminHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	int wareType = ParamKit.getIntParameter(request, "wareType", 0);
	String countryName = (String)request.getAttribute("CountryName");
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	Boolean isModifyAble = readOnly.equalsIgnoreCase("true")? false:true;
	request.setAttribute("readOnly", readOnly);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>采购订单管理</title>
	<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="<%=STATIC_URL %>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/common.js"></script>
	<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
	<script type='text/javascript' src="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.js"></script>
	<script type="text/javascript" src="../js/erp-common.js"></script>
	<script type="text/javascript" src="../js/rows-jq.js"></script>
	<link rel="stylesheet" href="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.css" type="text/css" media="screen" />
	<script type="text/javascript">
        document.domain='<%=DOMAIN%>'; 
	</script>
</head>
<body class="article_bg">
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a> 
		<ul class="art_nav">
			<li><a href="PurchaseOrderInfoList.do?wareType=<%=bean.getWareType()%>"><%=MapKit.getValueFromMap(wareType, PurchaseOrderConfig.WARE_TYPE_MAP)%>订单列表</a>&nbsp;>&nbsp;</li>
			<% if(poId > 0) {%>
			<li><%=bean.getPoNumber() %>&nbsp;>&nbsp;</li>
			<li>修改采购订单</li>
			<%}else{ %>
			<li>新建采购订单</li>
			<%}%>
		</ul>
	</div>
	<%if(poId > 0 ){%>
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
	<%}%>
	<form id="formData" name="formData" method="post" action="PurchaseOrderInfoManage.do" onsubmit="javascript:return formOnSubmit();">
	  <input type="hidden" name="poNumber" id="poNumber" value="<%=bean.getPoNumber() %>"/>
	  <input type="hidden" name="poId" id="poId" value="<%=bean.getPoId() %>"/>
	  <input type="hidden" name="wareType" id="wareType" value="<%=bean.getWareType() %>"/>
	  <input type="hidden" name="purchaseType" id="purchaseType" value="<%=bean.getPurchaseType() %>"/>
	  <input type="hidden" name="receiverType" id="receiverType" value="<%=bean.getReceiverType() %>"/>
	  <input type="hidden" name="currency" id="currency" value="<%=bean.getCurrency() %>"/>
	  <input type="hidden" name="poStatus" id="poStatus" value="<%=bean.getPoStatus() %>"/>
	  <input type="hidden" name="approvalStatus" id="approvalStatus" value="<%=bean.getApprovalStatus()%>"/>
	  <p>基础信息</p>
	  <div class="modify_form">
	  	<p>
	  		<em class="int_label">订单类型：</em>
	  		<em><%=MapKit.getValueFromMap(bean.getWareType(), PurchaseOrderConfig.WARE_TYPE_MAP)%></em>
	  	</p>
	  	<p>
	  		<em class="int_label">采购类型：</em>
	  		<em><%=MapKit.getValueFromMap(bean.getPurchaseType(), PurchaseOrderConfig.PURCHASE_TYPE_MAP) %></em>
	  	</p>
	  	<p>
	  		<em class="int_label"><span>*</span>货款状态：</em>
	  		<select name="paymentStatus" id="paymentStatus">
	      		<%for(StatusBean<String, String> statusBean : PurchaseOrderConfig.PAYMENT_STATUS_LIST){ %><option value="<%=statusBean.getStatus() %>" <%=(statusBean.getStatus().equals(bean.getPaymentStatus()))?"selected":"" %>><%=statusBean.getValue() %></option><%} %>
	      	</select>
	  	</p>
	  	<p>
	  		<em class="int_label"><span>*</span>发票状态：</em>
	  		<select name="invoiceStatus" id="invoiceStatus">
	      		<%for(StatusBean<String, String> statusBean : PurchaseOrderConfig.INVOICE_STATUS_LIST){ %><option value="<%=statusBean.getStatus() %>" <%=(statusBean.getStatus().equals(bean.getInvoiceStatus()))?"selected":"" %>><%=statusBean.getValue() %></option><%} %>
	      	</select>
	  	</p>
	  	<p>
  			<em class="int_label"><span>*</span>合同状态：</em>
	  		<select name="contractStatus" id="contractStatus" onchange="checkContractStatus()">
	      		<%for(StatusBean<String, String> statusBean : PurchaseOrderConfig.PURCHASE_CONTRACT_STATUS_LIST){ %><option value="<%=statusBean.getStatus() %>" <%=(statusBean.getStatus().equals(bean.getContractStatus()))?"selected":"" %>><%=statusBean.getValue() %></option><%} %>
	      	</select>
	      	<em id="contractInput" style="display:none">
	      		<input type="text" name="contractName" id="contractName" value="<%=bean.getPcName() %>" readOnly size="50"/>
	      		<input type="hidden" name="contractId" id="contractId" value="<%=bean.getContractId() %>"/>
	      		<%if(isModifyAble){ %>
	      		<input type="button" name="selectContract" id="selectContract" value="添加" />
	      		<%} %>
	      	</em>
	  	</p>
	  	<input type="hidden" name="status" id="status" value="<%=Constants.STATUS_VALID %>" />
	  	<!-- 
	  	<p>
	      	<em class="int_label">有效状态：</em>
	      	<input type="checkbox" name="status" id="status" value="<%=Constants.STATUS_VALID %>" <%=(bean.getStatus()==Constants.STATUS_VALID)?"checked":"" %>/>
	    </p>
	     -->
	  </div>
	  <p>供应商信息</p>
	  <div id="supplier_info" class="modify_form">
	  	<p>
	  		<em class="int_label"><span>*</span>供应商：</em>
	  		<input type="text" name="supplierName" id="supplierName" value="<%=bean.getSupplier()%>" placeholder="请添加供应商" readOnly size="50" checkStr="供应商;txt;true;;100"/>
	      	<input type="hidden" name="supplierId" id="supplierId" value="<%=bean.getSupplierId()%>"/>
	      	<%if(isModifyAble){ %>
	      	<input type="button" name="selectSupplier" id="selectSupplier" value="添加" />
	      	<%} %>
	  	</p>
	  	<p>
	  		<em class="int_label">供应商地址：</em>
	  		<em id="consignerAddressDisplay"><%=bean.getCompleteConsignerAddress() %></em>
	  		<em id="modifyConsignerAddress" <%=bean.getCompleteConsignerAddress().equals("") ? "style='display:none'" : ""%>>
	  		<%if(isModifyAble){ %>
	  		<input type="button" name="selectSupplierAddress" id="selectSupplierAddress" value="修改" />
	  		<%} %>
	  		</em>
	  		<input type="hidden" name="consignerAddressId" id="consignerAddressId" value="<%=bean.getPoId() == 0 ? -1 : ""%>"/>
	  		<input type="hidden" name="consignerName" id="consignerName" value="<%=bean.getConsignerName()%>"/>
	  		<input type="hidden" name="consignerCountry" id="consignerCountry" value="<%=bean.getConsignerCountry()%>"/>
	  		<input type="hidden" name="consignerProvince" id="consignerProvince" value="<%=bean.getConsignerProvince()%>"/>
	  		<input type="hidden" name="consignerCity" id="consignerCity" value="<%=bean.getConsignerCity()%>"/>
	  		<input type="hidden" name="consignerRegion" id="consignerRegion" value="<%=bean.getConsignerRegion()%>"/>
	  		<input type="hidden" name="consignerAddress" id="consignerAddress" value="<%=bean.getConsignerAddress()%>"/>
	  		<input type="hidden" name="consignerTelephone" id="consignerTelephone" value="<%=bean.getConsignerTelephone()%>"/>
	  		<input type="hidden" name="consignerMobile" id="consignerMobile" value="<%=bean.getConsignerMobile()%>"/>
	  	</p>
	  	<p>
	  		<em class="int_label">联系人：</em>
	  		<em id="consignerNameDisplay"><%=bean.getConsignerName() %></em>
	  	</p>
	  	<p>
	  		<em class="int_label">联系电话：</em>
	  		<em id="consignerTelephoneDisplay"><%=bean.getConsignerTelephone() %></em>
	  	</p>
	  </div>	
	  <%if(bean.getReceiverType() == PurchaseOrderConfig.RECEIVER_TYPE_WAREHOUSE) { %>
	  <p>仓库信息</p>
	  <div id="recevier_info" class="modify_form">
	  	<p>
	  		<em class="int_label"><span>*</span>入库仓库：</em>
	  		<input type="text" name="receiverOrgName" id="receiverOrgName" value="<%=bean.getReceiver()%>" placeholder="请添加仓库" readOnly size="50" checkStr="仓库;text;true;;100"/>
	      	<input type="hidden" name="receiverId" id="receiverId" value="<%=bean.getReceiverId()%>"/>
	      	<%if(isModifyAble){ %>
	      	<input type="button" name="selectReceiver" id="selectReceiver" value="添加" />
	      	<%} %>
	      	<input type="hidden" name="receiverSelectionUrl" id="receiverSelectionUrl" value="WarehouseInfoSelect.do?callbackFun=selectWarehouse&warehouseId="/>
	  	</p>	
	  	<p>
	  		<em class="int_label">仓库地址：</em>
	  		<em id="receiverAddressDisplay"><%=bean.getCompleteReceiverAddress() %></em>
	  		<input type="hidden" name="receiverAddressId" id="receiverAddressId" value=""/>
	  		<input type="hidden" name="receiverName" id="receiverName" value="<%=bean.getReceiverName()%>"/>
	  		<input type="hidden" name="receiverCountry" id="receiverCountry" value="<%=bean.getReceiverCountry()%>"/>
	  		<input type="hidden" name="receiverProvince" id="receiverProvince" value="<%=bean.getReceiverProvince()%>"/>
	  		<input type="hidden" name="receiverCity" id="receiverCity" value="<%=bean.getReceiverCity()%>"/>
	  		<input type="hidden" name="receiverRegion" id="receiverRegion" value="<%=bean.getReceiverRegion()%>"/>
	  		<input type="hidden" name="receiverAddress" id="receiverAddress" value="<%=bean.getReceiverAddress()%>"/>
	  		<input type="hidden" name="receiverTelephone" id="receiverTelephone" value="<%=bean.getReceiverTelephone()%>"/>
	  		<input type="hidden" name="receiverMobile" id="receiverMobile" value="<%=bean.getReceiverMobile()%>"/>
	  	</p>
	  	<p style="display:none">
	  		<em class="int_label">联系人：</em>
	  		<em id="receiverNameDisplay"><%=bean.getReceiverName()%></em>
	  	</p>
	  	<p>
	  		<em class="int_label">联系电话：</em>
	  		<em id="receiverTelephoneDisplay"><%=bean.getReceiverTelephone()%></em>
	  	</p>
	  </div>	
	  <%} %>
	  <%if(bean.getReceiverType() == PurchaseOrderConfig.RECEIVER_TYPE_CUSTOMER){ %>
	  <p>收货信息</p>
	  <div id="recevier_info" class="modify_form">
	  	<p>
  			<em class="int_label"><span>*</span>收货人名称：</em>
  			<input type="text" name="receiverName" id="receiverName" value="<%=bean.getReceiverName()%>" size="50" checkStr="收货人名称;text;true;;100"/>
  			<input type="hidden" name="receiverId" id="receiverId" value="0"/>
  		</p>
		<input type="hidden" name="receiverCountry" id="receiverCountry" value="<%=countryName %>" size="50"/>
		<p>
	      <em class="int_label"><span>*</span>省:</em>    
     	  <select id="province" name="receiverProvince" onchange="$('#region').empty();getCityNameList('province','city','<%=bean.getReceiverCity()%>')" checkStr="省;txt;true;;200"></select>
     	  <input type="hidden" id="provinceDummy" value="<%=bean.getReceiverProvince()%>"/>
	    </p>
	    <p>
	      <em class="int_label"><span>*</span>市:</em>    
     	  <select id="city" name="receiverCity" onchange="getRegionNameList('city','region','<%=bean.getReceiverRegion()%>')" checkStr="市;txt;true;;200"></select>
     	  <input type="hidden" id="cityDummy" value="<%=bean.getReceiverCity()%>"/>
	    </p>
	    <p>
	      <em class="int_label"><span>*</span>区:</em>    
     	  <select id="region" name="receiverRegion" checkStr="区;txt;true;;200"></select>
	    </p>
  		<p>
  			<em class="int_label"><span>*</span>地址：</em>
  			<input type="text" name="receiverAddress" id="receiverAddress" value="<%=bean.getReceiverAddress()%>" size="50" checkStr="地址;text;true;;100"/>
  		</p>
  		<p>
  			<em class="int_label"><span>*</span>电话：</em>
  			<input type="text" name="receiverTelephone" id="receiverTelephone" value="<%=bean.getReceiverTelephone()%>" size="50"  checkStr="电话;digit;true;;50"/>
  		</p>
  		<p>
  			<em class="int_label"><span>*</span>手机：</em>
  			<input type="text" name="receiverMobile" id="receiverMobile" value="<%=bean.getReceiverMobile()%>"  size="50" checkStr="手机;digit;true;;50"/>
  		</p>
	  </div>
	  <%} %>
	  <%if(bean.getReceiverType() == PurchaseOrderConfig.RECEIVER_TYPE_FACTORY){ %>
	  <p>收货信息</p>
	  <div id="recevier_info" class="modify_form">
	  	<p>
	  		<em class="int_label"><span>*</span>收货人：</em>
	  		<input type="text" name="receiverOrgName" id="receiverOrgName" value="<%=bean.getReceiver() %>" placeholder="请添加收货方" readOnly checkStr="收货方;text;true;;100"/>
	      	<input type="hidden" name="receiverId" id="receiverId" value="<%=bean.getReceiverId()%>"/>
	      	<%if(isModifyAble){ %>
	      	<input type="button" name="selectReceiver" id="selectReceiver" value="添加" />
	      	<%} %>
	      	<input type="hidden" name="receiverSelectionUrl" id="receiverSelectionUrl" value="<%=SRM_URL%>/srm/SupplierInfoSelect.do?callbackFun=selectReceiver&supplierId="/>
	  	</p>	
	  	<p>
	  		<em class="int_label">收货人地址：</em>
	  		<em id="receiverAddressDisplay"><%=bean.getCompleteReceiverAddress() %></em>
	  		<em id="modifyReceiverAddress" <%=bean.getCompleteReceiverAddress().equals("") ? "style='display:none'" : ""%>>
	  		<%if(isModifyAble){ %>
	  		<input type="button" name="selectReceiverAddress" id="selectReceiverAddress" value="修改" />
	  		<%} %>
	  		</em>
	  		<input type="hidden" name="receiverAddressId" id="receiverAddressId" value="<%=bean.getPoId() == 0 ? -1 : ""%>"/>
	  		<input type="hidden" name="receiverName" id="receiverName" value="<%=bean.getReceiverName()%>"/>
	  		<input type="hidden" name="receiverCountry" id="receiverCountry" value="<%=bean.getReceiverCountry()%>"/>
	  		<input type="hidden" name="receiverProvince" id="receiverProvince" value="<%=bean.getReceiverProvince()%>"/>
	  		<input type="hidden" name="receiverCity" id="receiverCity" value="<%=bean.getReceiverCity()%>"/>
	  		<input type="hidden" name="receiverRegion" id="receiverRegion" value="<%=bean.getReceiverRegion()%>"/>
	  		<input type="hidden" name="receiverAddress" id="receiverAddress" value="<%=bean.getReceiverAddress()%>"/>
	  		<input type="hidden" name="receiverTelephone" id="receiverTelephone" value="<%=bean.getReceiverTelephone()%>"/>
	  		<input type="hidden" name="receiverMobile" id="receiverMobile" value="<%=bean.getReceiverMobile()%>"/>
	  	</p>
	  	<p>
	  		<em class="int_label">联系人：</em>
	  		<em id="receiverNameDisplay"><%=bean.getReceiverName()%></em>
	  	</p>
	  	<p>
	  		<em class="int_label">联系电话：</em>
	  		<em id="receiverTelephoneDisplay"><%=bean.getReceiverTelephone() %></em>
	  	</p>
	  </div>	
	  <%} %>
	  <%
  		boolean isPoItemModify = true;
	  	if(PurchaseOrderStatusHelper.isPoItemLock(bean.getPurchaseOrderInfoBean())) {
			isPoItemModify = false;
	  	}%>
 		<%if(wareType==InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM) { %>
  			<%=DetailDisplayUtil.getDisplayStr(PurchaseOrderItemDisplayConfig.PRODUCT_PO_DETAIL, poId, isPoItemModify&&isModifyAble) %>
  		<%}if(wareType==InfoTypeConfig.INFO_TYPE_MATERIAL) { %>
  			<%=DetailDisplayUtil.getDisplayStr(PurchaseOrderItemDisplayConfig.MATERIAL_PO_DETAIL, poId, isPoItemModify&&isModifyAble) %>
  		<%} %>
	  <div class="div_center">
	  <input type="submit" name="submit" id="submit" class="modifysubbtn" value="提交" />
	  <a href="PurchaseOrderInfoList.do?wareType=<%=bean.getWareType()%>"><input type="button" name="cancel" id="cancel" value="取消" ></a>
	  </div>
	</form>
</body>
<script>
$(document).ready(function() {
	var signedStatus = "<%=PurchaseOrderConfig.PCS_SIGNED%>";
	var contractStatus = $("#contractStatus").val();
	if(contractStatus == signedStatus){
		$("#contractInput").show();
	}
	var poStatus = $("#poStatus").val();
	var receivedStatus = "<%=PurchaseOrderConfig.PO_STATUS_RECEIVED%>";
	if(poStatus == receivedStatus){
		$("#supplier_info input").attr("readonly", "true");
		$("#supplier_info input[type=button]").attr("disabled", "disabled");
		$("#recevier_info input").attr("readonly", "true");
		$("#recevier_info input[type=button]").attr("disabled", "disabled");
	}
	
	$("#selectContract").click(
		function() {
			var contractId = $("#contractId").val();
			var contractName = $("#contractName").val();
			$.fancybox.open({
				href : 'PurchaseContractInfoSelect.do?callbackFun=selectContract&contractId='+ contractId,
				type : 'iframe',
				width : 560,
				minHeight : 500
		});
	});
	
	$("#selectSupplier").click(
		function() {
			var supplierId = $("#supplierId").val();
			var supplierName = $("#supplierName").val();
			$.fancybox.open({
				href : '<%=SRM_URL%>/srm/SupplierInfoSelect.do?callbackFun=selectSupplier&supplierId='+ supplierId,
				type : 'iframe',
				width : 560,
				minHeight : 500
		});
	});
	
	$("#selectSupplierAddress").click(
		function() {
			var supplierId = $("#supplierId").val();
			var consignerAddressId = $("#consignerAddressId").val();
			$.fancybox.open({
				href : '<%=SRM_URL%>/srm/SupplierAddressSelect.do?callbackFun=selectSupplierAddress&supplierId=' + supplierId + '&supplierAddressId=' + consignerAddressId,
				type : 'iframe',
				width : 560,
				minHeight : 500
		});
	});
	
	$("#selectReceiver").click(
		function() {
			var receiverSelectionUrl = $("#receiverSelectionUrl").val();
			var receiverId = $("#receiverId").val();
			var receiverOrgName = $("#receiverOrgName").val();
			$.fancybox.open({
				href : receiverSelectionUrl + receiverId,
				type : 'iframe',
				width : 560,
				minHeight : 500
		});
	});
	
	$("#selectReceiverAddress").click(
		function() {
			var receiverId = $("#receiverId").val();
			var receiverAddressId = $("#receiverAddressId").val();
			$.fancybox.open({
				href : '<%=SRM_URL%>/srm/SupplierAddressSelect.do?callbackFun=selectReceiverAddress&supplierId=' + receiverId + '&supplierAddressId=' + receiverAddressId,
				type : 'iframe',
				width : 560,
				minHeight : 500
		});
	});
	
	//获取省市区
	<%if(bean.getReceiverType() == PurchaseOrderConfig.RECEIVER_TYPE_CUSTOMER){ %>
		var url='<%=REGION_URL%>/region/RegionAjaxList.do';
		$.getScript(url,function(data){
			getProvinceNameList('receiverCountry','province','<%=bean.getReceiverProvince()%>');
			getCityNameList('provinceDummy','city','<%=bean.getReceiverCity()%>');
			getRegionNameList('cityDummy','region','<%=bean.getReceiverRegion()%>');
		});
	<%}%>
	
	var readOnly = '${readOnly}';
	if(readOnly=='true') {
		disablePageElement();
	}

	//修复金额回显小数位
	formatDouble();
});

function checkContractStatus(){
	var signedStatus = "<%=PurchaseOrderConfig.PCS_SIGNED%>";
	var contractStatus = $("#contractStatus").val();
	if(contractStatus == signedStatus){
		$("#contractInput").show();
		$("#contractName").attr("checkStr","合同;text;true;;100")
	}else {
		$("#contractInput").hide();
		$("#contractName").attr("checkStr","")
	}
}

function selectContract(contractId, contractName){
	if(contractId != "" && contractName != ""){
		$("#contractName").val(contractName);
		$("#contractId").val(contractId)
	}	
}

function selectSupplier(supplierInfoJsonStr,echoParameter){
	var supplierInfoBean = jQuery.parseJSON(supplierInfoJsonStr);
	if(supplierInfoBean.supplierId != ""){
		$("#supplierName").val(supplierInfoBean.supplierName);
		$("#supplierId").val(supplierInfoBean.supplierId);
		$("#consignerAddressDisplay").html(supplierInfoBean.country+supplierInfoBean.province+supplierInfoBean.city+supplierInfoBean.region+supplierInfoBean.address);
		$("#consignerNameDisplay").html(supplierInfoBean.supplierName);
		$("#consignerTelephoneDisplay").html(supplierInfoBean.telephone);
		$("#modifyConsignerAddress").show();
		$("#consignerName").val(supplierInfoBean.contactName);
		$("#consignerCountry").val(supplierInfoBean.country);
		$("#consignerProvince").val(supplierInfoBean.province);
		$("#consignerCity").val(supplierInfoBean.city);
		$("#consignerRegion").val(supplierInfoBean.region);
		$("#consignerAddress").val(supplierInfoBean.address);
		$("#consignerTelephone").val(supplierInfoBean.telephone);
		$("#consignerMobile").val(supplierInfoBean.mobile);
	}
}

function selectSupplierAddress(supplierAddressJsonStr,echoParameter){
	var supplierAddressBean = jQuery.parseJSON(supplierAddressJsonStr);
	if(supplierAddressBean.addressId != ""){
		$("#consignerAddressId").val(supplierAddressBean.addressId);
		$("#consignerAddressDisplay").html(supplierAddressBean.country+supplierAddressBean.province+supplierAddressBean.city+supplierAddressBean.region+supplierAddressBean.address);
		$("#consignerNameDisplay").html(supplierAddressBean.contactName);
		$("#consignerTelephoneDisplay").html(supplierAddressBean.telephone);
		$("#consignerName").val(supplierAddressBean.contactName);
		$("#consignerCountry").val(supplierAddressBean.country);
		$("#consignerProvince").val(supplierAddressBean.province);
		$("#consignerCity").val(supplierAddressBean.city);
		$("#consignerRegion").val(supplierAddressBean.region);
		$("#consignerAddress").val(supplierAddressBean.address);
		$("#consignerTelephone").val(supplierAddressBean.telephone);
		$("#consignerMobile").val(supplierAddressBean.mobile);
	}
}

function selectWarehouse(warehouseJsonStr,echoParameter){
	 var warehouseJsonBean = jQuery.parseJSON(warehouseJsonStr);
	if(warehouseJsonBean.warehouseId != ""){
		$("#receiverOrgName").val(warehouseJsonBean.warehouseName);
		$("#receiverId").val(warehouseJsonBean.warehouseId);
		$("#receiverAddressDisplay").html(warehouseJsonBean.country+warehouseJsonBean.province+warehouseJsonBean.city+warehouseJsonBean.region+warehouseJsonBean.address);
		$("#receiverNameDisplay").html(warehouseJsonBean.contactName);
		$("#receiverTelephoneDisplay").html(warehouseJsonBean.telephone);
		$("#modifyReceiverAddress").show();
		$("#receiverName").val(warehouseJsonBean.contactName);
		$("#receiverCountry").val(warehouseJsonBean.country);
		$("#receiverProvince").val(warehouseJsonBean.province);
		$("#receiverCity").val(warehouseJsonBean.city);
		$("#receiverRegion").val(warehouseJsonBean.region);
		$("#receiverAddress").val(warehouseJsonBean.address);
		$("#receiverTelephone").val(warehouseJsonBean.telephone);
	}
}

function selectReceiver(supplierInfoJsonStr,echoParameter){
	var supplierInfoBean = jQuery.parseJSON(supplierInfoJsonStr);
	if(supplierInfoBean.supplierId != ""){
		$("#receiverOrgName").val(supplierInfoBean.supplierName);
		$("#receiverId").val(supplierInfoBean.supplierId);
		$("#receiverAddressDisplay").html(supplierInfoBean.country+supplierInfoBean.province+supplierInfoBean.city+supplierInfoBean.region+supplierInfoBean.address);
		$("#receiverNameDisplay").html(supplierInfoBean.supplierName);
		$("#receiverTelephoneDisplay").html(supplierInfoBean.telephone);
		$("#modifyReceiverAddress").show();
		$("#receiverName").val(supplierInfoBean.contactName);
		$("#receiverCountry").val(supplierInfoBean.country);
		$("#receiverProvince").val(supplierInfoBean.province);
		$("#receiverCity").val(supplierInfoBean.city);
		$("#receiverRegion").val(supplierInfoBean.region);
		$("#receiverAddress").val(supplierInfoBean.address);
		$("#receiverTelephone").val(supplierInfoBean.telephone);
		$("#receiverMobile").val(supplierInfoBean.mobile);
	}
}

function selectReceiverAddress(supplierAddressJsonStr,echoParameter){
	var supplierAddressBean = jQuery.parseJSON(supplierAddressJsonStr);
	if(supplierAddressBean.addressId != ""){
		$("#receiverAddressId").val(supplierAddressBean.addressId);
		$("#receiverAddressDisplay").html(supplierAddressBean.country+supplierAddressBean.province+supplierAddressBean.city+supplierAddressBean.region+supplierAddressBean.address);
		$("#receiverNameDisplay").html(supplierAddressBean.contactName);
		$("#receiverTelephoneDisplay").html(supplierAddressBean.telephone);
		$("#receiverName").val(supplierAddressBean.contactName);
		$("#receiverCountry").val(supplierAddressBean.country);
		$("#receiverProvince").val(supplierAddressBean.province);
		$("#receiverCity").val(supplierAddressBean.city);
		$("#receiverRegion").val(supplierAddressBean.region);
		$("#receiverAddress").val(supplierAddressBean.address);
		$("#receiverTelephone").val(supplierAddressBean.telephone);
		$("#receiverMobile").val(supplierAddressBean.mobile);
	}
}
function callbackFun(selectItems){
	var trs = $('#detail').find('tr');
	//把空白的删除掉
	trs.each(function(){
		var tds = $(this).find('td');
		var itemType = $(tds[1]).html();
		var wareNumber = $(tds[2]).html();
		var itemName = $(tds[3]).html();
		if(itemType==""||wareNumber==""||itemName==""){
			$(this).remove();
		} 
	});
	var table = $('#detail');
	//插入行
	for(var i=0;i<selectItems.length;i++){
		if(selectItems[i]!=""){
			var itemContent = jQuery.parseJSON(selectItems[i]);
			if(itemContent.wareType=='<%=wareType%>'){
				table.addRow([
				  			'<input type="checkbox" name="select">',
				  			itemContent.itemTypeName,
				  			itemContent.itemNumber,
				  			itemContent.itemName,
				  			itemContent.itemSpecification,
				  			'<input type="hidden" name="unit" id="unit"><input type="text" id="unitName" size="10" maxlength="10" readOnly onclick="unitSelectionDo(this)" checkStr="采购计量单位;txt;true;;100" onchange="itemModifyOnChange(this)" value="">',
				  			'<input type="text" size="10" maxlength="10"  checkStr="采购单价;num;true;;100"  id="unitPrice" onchange="itemModifyOnChange(this)" onkeyup="this.value=this.value.replace(/[^\\-?\\d.]/g,&quot&quot)" value="">',
				  			'<input type="text" size="10" maxlength="10"  checkStr="采购数量;num;true;;100"  id="quatity" onchange="itemModifyOnChange(this)" onkeyup="this.value=this.value.replace(/[^\\-?\\d.]/g,&quot&quot)" value="">',
				  			'<span id="total" name="total">0.00</span>',
				  			'<input type="text" checkStr="备注;txt;false;;500" id="memo" onchange="itemModifyOnChange(this)" value="">',
				  			'<input type="hidden" name="selectedItem" value="'+itemContent.itemId+'">',
				  			'<input type="hidden" checkStr="采购明细值;txt;true;;1000" id="selectedItemValue" name="selectedItemValue_'+itemContent.itemId+'"  value="">',
				  			
				  		]);
			}
		}
	}
}

function itemModifyOnChange(ele){
	var currentRow = $(ele).parent().parent();
	var tds = currentRow.find('td');
	var typename = $(tds[1]).html();
	var typecode = $(tds[2]).html();
	var unit = currentRow.find('#unit').val();
	var unitPrice = currentRow.find('#unitPrice').val();
	var quatity = currentRow.find('#quatity').val();
	var memo = currentRow.find('#memo').val();
	var wareType = '<%=wareType%>';
	var total = currentRow.find('#total');
	var itemValueStr = "unit="+unit+",unitPrice="+unitPrice+",quatity="+quatity+",memo="+memo+",typecode="+typecode+",wareType="+wareType;
	var itemValue = currentRow.find('#selectedItemValue').val(itemValueStr);
	
	if(unitPrice!=""&&quatity!=""){
		//计算总价保留两位小数
		total.html((unitPrice*quatity).toFixed(2));
	}
	
	poAmountOnChange();
	
}
function poAmountOnChange(){
	//更改总价显示
	var poAmount = $('#poAmount');
	var poAmountValue = parseFloat('0');
	var totals = $('span[name=total]');
	totals.each(function(){
		poAmountValue+=parseFloat($(this).text());
	});
	poAmount.text(poAmountValue.toFixed(2));
}
function unitSelectionDo(ele){
	//判断是新建的还是修改的
	var currentRow = $(ele).parent().parent();
	var itemId = currentRow.find('input[name=itemId]').val();
	var selectedItem = currentRow.find('input[name=selectedItem]').val();
	var unitCode = currentRow.find('#unit').val();
	var id ="";
	var itemName="";
	if(itemId!="") {
		id=itemId;
		itemName="itemId";
	}
	if(selectedItem!=""){
		id=selectedItem;
		itemName="selectedItem";
	} 
	
	$.fancybox.open({
		href : '<%=PDM_URL%>/pdm/UnitInfoSelect.do?callbackFun=unitSelectionCallBack&itemId=' + id+'&itemName='+itemName+'&unitCode='+unitCode,
		type : 'iframe',
		width : 560,
		minHeight : 150
});
}

function unitSelectionCallBack(unitCode,unitName,itemId,itemName){
	var input = $('input[name='+itemName+']');
	input.each(function(){
		if($(this).val()==itemId){
			var parent = $(this).parent().parent();
			parent.find('#unitName').val(unitName);
			parent.find('#unit').val(unitCode);
			return false;
		}
	});
	
}
function addRow(){
		var selectedItem = $('tr').find('input[name=selectedItem]');
  		var selectItemStr="";
  		var poId = '<%=poId%>';
  		<%if(wareType==InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM) { %>
  		var actionUrl = "<%=PDM_URL%>/pdm/ProductItemSelect.do";
  		<%}if(wareType==InfoTypeConfig.INFO_TYPE_MATERIAL) { %>
  		var actionUrl = "<%=PDM_URL%>/pdm/MaterialInfoSelect.do";
  		<%} %>
  		selectedItem.each(function(){
  			selectItemStr+=$(this).val()+",";
  		});
  		selectItemStr = selectItemStr.substring(0,selectItemStr.length-1);
  		$.fancybox.open({
  			href : actionUrl+"?selectedItem="+selectItemStr+"&poId="+poId+'&openOrPop=pop',
  			type : 'iframe',
  			width : 650,
  			minHeight : 500
  	}); 
}
function delRow(){
	var checkBoxs = $('input[name=select]');
	checkBoxs.each(function(e){
		if($(this).is(':checked')){
			$(this).parent().parent().remove();
		}
	});
	$('#selectAll').removeAttr("checked");
	poAmountOnChange();
}
  	
 function formOnSubmit(){
	 //先判断是新建还是修改
	 var poId = '<%=poId%>';
	 if(poId=='0'){
		 //新建
		 //规则是必须有新建的采购ITEM
		 var items = $('#detail').find('input[name=selectedItem]');
		 if(items.length<=0){
			 alert('新建采购单必须填写采购明细!');
			 return false;
		 }
	 }else{
		 //修改
		 //规则是如果没有新增，原来的ITEM就不能完全删除
		 var orgitems = $('#detail').find('input[name=selectedItem]');
		 var newitems = $('#detail').find('input[name=itemId]');
		 //不能两个都等于0
		 if(orgitems.length<=0&&newitems.length<=0){
			 alert('不能再没有新建明细的情况下,完全删除原有明细!');
			 return false;
		 }
	 }
	 //验证是不是有相同的WAREID
	 //var existItem = $('input[name=itemId]');
	 var newItem = $('input[name=selectedItem]');
	 var tempArray = new Array();
	 /* if(existItem.length>0){
		 existItem.each(function(){
			 if($(this).val().trim()!="")
			 tempArray.push($(this).val());
		 });
	 } */
	 if(newItem.length>0){
		 newItem.each(function(){
			 if($(this).val().trim()!="")
			 tempArray.push($(this).val());
		 });
	 }
	 //数组排序
	 tempArray=tempArray.sort();
	 for(var i = 0;i<tempArray.length;i++){
		 if((i+1)<tempArray.length){
			 if(tempArray[i]==tempArray[i+1]){
				 alert("不能选择重复的制品!请删除后再提交");
				 return false;
			 }
		 }
	 }
	 
	 //先恢复table中的DISABLED
	 var disabledDom = $('#detail').find('input[disabled]');
	 disabledDom.removeAttr('disabled');
	//经过所有检验后，执行原有的表单验证
	if(checkForm('formData')){
		//验证通过
		return true;
	}else{
		//验证不通过要把DISABLED恢复
		disabledDom.attr('disabled');
		return false;
	}
 }
 
 function formatDouble(){
	 //更正明细条目回显小数位
	 var totals = $('span[name=total]');
	 totals.each(function(){
		 var temp = $(this).text();
		 temp = parseFloat(temp).toFixed(2);
		 $(this).text(temp);
	 });
	 //更正总价回显小数位
	 var poAmount = $('#poAmount').text();
	 poAmount = parseFloat(poAmount).toFixed(2);
	 $('#poAmount').text(poAmount);
 }
</script>
</html>