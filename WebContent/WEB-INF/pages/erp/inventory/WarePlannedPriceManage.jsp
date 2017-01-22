<%@page import="com.lpmas.pdm.client.PdmServiceClient"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.lpmas.framework.config.*"%>
<%@ page import="com.lpmas.framework.util.*"%>
<%@ page import="com.lpmas.framework.bean.StatusBean"%>
<%@ page import="com.lpmas.admin.bean.*"%>
<%@ page import="com.lpmas.admin.business.*"%>
<%@ page import="com.lpmas.framework.web.*"%>
<%@ page import="com.lpmas.constant.info.*"%>
<%@ page import="com.lpmas.erp.report.bean.*"%>
<%@ page import="com.lpmas.erp.inventory.bean.*"%>
<%@ page import="com.lpmas.erp.report.config.*"%>
<%@ include file="../../include/header.jsp"%>
<%
	WarePlannedPriceBean bean = (WarePlannedPriceBean)request.getAttribute("WarePlannedPriceBean");
	boolean isModify = (boolean)request.getAttribute("isModify");
	String wareNumber = (String)request.getAttribute("wareNumber");
	String wareName = (String)request.getAttribute("wareName");
	String wareTypeName = (String)request.getAttribute("wareTypeName");
	String unitName = (String)request.getAttribute("unitName");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>月度计划价格管理</title>
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
<script type="text/javascript">
document.domain='<%=DOMAIN%>';
</script>
</head>
<body class="article_bg">
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a> 
		<ul class="art_nav">
			<li><a href="WarePlannedPriceList.do">月度计划价格列表</a>&nbsp;>&nbsp;</li>
			<%if(!isModify){ %>
			<li>新建月度计划价格</li>
			<%}else{ %>
			<li>修改月度计划价格</li>
			<%} %>
		</ul>
	</div> 
	<form id="formData" name="formData" method="post" action="WarePlannedPriceManage.do" onsubmit="javascript:return checkForm('formData');">
	  <div class="modify_form">
  		<p>
  			<em class="int_label"><span>*</span>制品类型：</em>
  			<select name="wareType" id="wareType">
			<%int wareType = bean.getWareType(); %>
				<option value="<%=InfoTypeConfig.INFO_TYPE_MATERIAL %>" <%=wareType==InfoTypeConfig.INFO_TYPE_MATERIAL? "selected":"" %>>物料</option>
				<option value="<%=InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM %>" <%=wareType==InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM? "selected":"" %>>商品项</option>
			</select>
  		</p>
  		<p>
  			<em class="int_label"><span>*</span>制品编号：</em>
  			<em id="wareNumberDisplay"><%=wareNumber %></em>
  			<input type="button" value="选择制品" onclick="selectItem()">
  			<input type="hidden"  name="wareNumber" id="wareNumber"value="<%=wareNumber%>"checkStr="制品编号;txt;true;;200"/>
  		</p>
  		<p>
  			<em class="int_label"><span>*</span>制品名称：</em>
  			<em id="wareIdDispaly"><%=wareName %></em>
  			<input type="hidden"  name="wareId" id="wareId" value="<%=bean.getWareId()%>"checkStr="制品名称;txt;true;;200"/>
  		</p>
  		<p>
  			<em class="int_label"><span>*</span>计划月份：</em>
  			<input type="text" name="planMonth" id="planMonth" onclick="WdatePicker({dateFmt:'yyyyMM'})" value="<%=ParamKit.getParameter(request, "planMonth", "").trim()%>" size="20">
  		</p>
  		<p>
  			<em class="int_label"><span>*</span>计量单位：</em>
  			<em id="unitDispaly"><%=bean.getUnit() %></em>
  			<input type="hidden"  name="unit" id="unit"value="<%=bean.getUnit()%>"checkStr="计量单位;txt;true;;200"/>
  		</p>
  		<p>
  			<em class="int_label"><span>*</span>计划价格：</em>
     	  	<input type="text"  name="price" id="price" size="20" maxLength="20" value="<%=bean.getPrice()%>" checkStr="计划价格;num;false;;20" onkeyup="value=this.value.replace(/\D+/g,'')"/>
  		</p>
		</div>
	   <div class="div_center">
	  <input type="submit" name="submit" id="submit" class="modifysubbtn" value="提交" />
	  <input type="button" name="cancel" id="cancel" value="取消" onclick="javascript:history.back()">
	  </div>
	</form>
</body>
<script>
$(document).ready(function() {
	var readOnly = '${readOnly}';
	if(readOnly=='true') {
		disablePageElement();
	}
});

function selectItem(){
	var selectedItem = $('tr').find('input[name=selectedItem]');
	var selectItemStr="";
	var wareType = $('#wareType').val();
	var selectedItem = $('#wareId').val();
	var actionUrl = '';
	if(wareType==<%=InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM%>) { 
	actionUrl = "<%=PDM_URL%>/pdm/ProductItemSelect.do";
	}
	else if(wareType==<%=InfoTypeConfig.INFO_TYPE_MATERIAL%>) { 
	var actionUrl = "<%=PDM_URL%>/pdm/MaterialInfoSelect.do";
	}else{
		alert('制品类型不正确');
		return;
	}
	$.fancybox.open({
		href : actionUrl+"?selectedItem="+selectItemStr+"&isMultiple=false&openOrPop=pop",
		type : 'iframe',
		width : 650,
		minHeight : 500
}); 		
}

function callbackFun(selectItems){
	console.log(selectItems[0]);
	var itemObj = jQuery.parseJSON(selectItems[0]);
	
	$('#wareNumberDisplay').html(itemObj.itemNumber);
	$('#wareNumber').val(itemObj.itemNumber);
	$('#wareIdDispaly').html(itemObj.itemName);
	$('#wareId').val(itemObj.itemId);
	$('#unitDispaly').html(itemObj.itemUnit);
	$('#unit').val(itemObj.itemUnit);
	
}
</script>
</html>