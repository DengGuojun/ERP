<%@page import="com.lpmas.erp.warehouse.bean.WarehouseInfoBean"%>
<%@page import="com.lpmas.admin.client.AdminServiceClient"%>
<%@page import="com.lpmas.pdm.bean.MaterialInfoBean"%>
<%@page import="com.lpmas.pdm.bean.ProductItemBean"%>
<%@page import="com.lpmas.pdm.client.PdmServiceClient"%>
<%@page import="com.lpmas.constant.info.InfoTypeConfig"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.lpmas.erp.config.*"%>
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
<%@ page import="com.lpmas.erp.report.bean.*"%>
<%@ page import="com.lpmas.erp.report.config.*"  %>
<%@ include file="../../include/header.jsp"%>
<%
    AdminUserHelper adminUserHelper = (AdminUserHelper) request.getAttribute("AdminUserHelper");
	List<List<Object>> contentList = (List<List<Object>>)request.getAttribute("reprotContent");
	List<String> headerList = (List<String>)request.getAttribute("headerList");
	
	String openDateTime = ParamKit.getParameter(request, "openDateTime", "");
	String endDateTime = ParamKit.getParameter(request, "endDateTime", "");
	int wareType = ParamKit.getIntParameter(request, "wareType", 0);
	int wareId = ParamKit.getIntParameter(request, "wareId", 0);
	String wareNumber = "";
	String wareName = "";
	
	if(wareType>0&&wareId>0){
		PdmServiceClient pdmServiceClient = new PdmServiceClient();
		if(wareType==InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM){
			ProductItemBean productItemBean = pdmServiceClient.getProductItemByKey(wareId);
			if(productItemBean!=null){
				wareNumber = productItemBean.getItemNumber();
				wareName = productItemBean.getItemName();
			}
		}else{
			MaterialInfoBean materialInfoBean = pdmServiceClient.getMaterialInfoByKey(wareId);
			if(materialInfoBean!=null){
				wareNumber = materialInfoBean.getMaterialNumber();
				wareName = materialInfoBean.getMaterialName();
			}
		}
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>出入库报表导出</title>
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
	<p class="article_tit">出入库报表导出</p>
	<form name="formSearch" method="post" action="" id="form">
		<div class="search_form">
			<p>
				<em class="em1">制品类型：</em> 
				<select name="wareType" id="wareType">
					<option value="<%=InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM %>" <%=wareType==InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM ? "selected":"" %>>商品项</option>
				<option value="<%=InfoTypeConfig.INFO_TYPE_MATERIAL %>" <%=wareType==InfoTypeConfig.INFO_TYPE_MATERIAL ? "selected":"" %>>物料</option>
				</select>
			</p>
			<p>
	  			<em class="em1">制品编号：</em>
	  			<input type="text"  name="wareNumber" id="wareNumber"value="<%=wareNumber%>"checkStr="制品编号;txt;true;;200" size="20" readOnly onclick="selectItem()"/>
	  		</p>
	  		<p>
	  			<em class="em1">制品名称：</em>
	  			<em id="wareIdDispaly"><%=wareName%></em>
	  			<input type="hidden"  name="wareId" id="wareId" value="<%=wareId%>"checkStr="制品名称;txt;true;;200" />
	  		</p>
	  		<p>
		  		<em class="em1">开始时间：</em> 
				<input type="text" name="openDateTime" id="openDateTime"onclick=" WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endDateTime\');}'})" size="20" value="<%=openDateTime%>"/>
	  		</p>
			<p>
				<em class="em1">结束时间：</em> 
				<input type="text" name="endDateTime" id="endDateTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'openDateTime\');}'})" size="20" value="<%=endDateTime%>"/>
			</p>
			<input name="" type="button" class="search_btn_sub" value="查询" onclick="submitForm('false')" />
			<input name="" type="button" class="search_btn_sub" value="导出" onclick="submitForm('true')" />
		</div>
	</form>
	
	<%if(contentList!=null){ %>
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			class="table_style">
			<tr>
				<%for(String header : headerList){ %>
					<th><%=header %></th>
				<%} %>
			</tr>
			<tr>
				<th></th>
				<th></th>
				<th></th>
				<th></th>
				<th>本期入库</th>
				<th></th>
				<th></th>
				<th></th>
				<th>本期出库</th>
				<th></th>
				<th></th>
				<th>期末库存</th>
				<th></th>
				<th></th>
				<th></th>
			</tr>
			<tr>
				<th>日期</th>
				<th>摘要</th>
				<th>单位</th>
				<th>入库单号</th>
				<th>数量</th>
				<th>单价</th>
				<th>金额</th>
				<th>出库单号</th>
				<th>数量</th>
				<th>单价</th>
				<th>金额</th>
				<th>数量</th>
				<th>单价</th>
				<th>金额</th>
				<th>备注</th>
			</tr>
			<%for(List<Object> row : contentList){ %>
				<tr>
					<%for(Object cell : row){ %>
						<td><%=cell %></td>
					<%} %>
				</tr>
			<%} %>
		</table>
	<%} %>
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
	
	$('#wareNumber').val(itemObj.itemNumber);
	$('#wareIdDispaly').html(itemObj.itemName);
	$('#wareId').val(itemObj.itemId);
	
}
function submitForm(isExport){
	var form = $('#form');
	form.attr("action","WarehouseInAndOutDetailReport.do?isExport="+isExport);
	form.submit();
}
</script>
</html>