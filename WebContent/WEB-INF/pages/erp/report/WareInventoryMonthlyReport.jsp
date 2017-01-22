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
	List<List<Object>> warehouseInfoExcelList = (List<List<Object>>)request.getAttribute("warehouseInfoExcelList");
	
	int wareType = ParamKit.getIntParameter(request, "wareType", 0);
	//String warehouseIds = ParamKit.getParameter(request, "warehouseIds", "").trim();
	//String warehouseIds = ParamKit.getAttribute(request, "warehouseIds").trim();
	boolean isShowWarehouseInfo = (boolean)request.getAttribute("isShowWarehouseInfo");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>库存月度报表导出</title>
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
	<p class="article_tit">库存月度报表导出</p>
	<form name="formSearch" method="post" action="" id="form">
		<div class="search_form">
		<p>
		<em class="em1">导出月份：</em> 
			<input type="text" name="reportMonth" id="reportMonth"
				 onclick="WdatePicker({dateFmt:'yyyyMM'})"
				 value="<%=ParamKit.getParameter(request, "reportMonth", "") %>"
				 size="20">
		</p>
		<p>
		<em class="em1">制品类型：</em> 
			<select name="wareType" id="wareType">
				<option value="<%=InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM %>" <%=wareType==InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM ? "selected":"" %>>商品项</option>
				<option value="<%=InfoTypeConfig.INFO_TYPE_MATERIAL %>" <%=wareType==InfoTypeConfig.INFO_TYPE_MATERIAL ? "selected":"" %>>物料</option>
			</select>
		</p>
		
	    <%-- <input type="hidden" name="warehouseIds" id="warehouseIds" value="<%=warehouseIds%>"/> --%>
	    <!-- <input type="button" class="search_btn_sub" name="selectWarehouses" id="selectWarehouses" value="选择查询仓库" /> -->
	    <input name="" type="button" class="search_btn_sub" value="查询" onclick="submitForm('false')" />
		<input name="" type="button" class="search_btn_sub" value="导出" onclick="submitForm('true')" />
		</div>
	</form>
	
	<%if(contentList!=null){ %>
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			class="table_style">
			<tr>
				<th></th>
				<th></th>
				<th></th>
				<th>期初库存</th>
				<th></th>
				<th></th>
				<th>本期入库</th>
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
				<th>品名</th>
				<th>存放地</th>
				<th>单位</th>
				<th>数量</th>
				<th>单价</th>
				<th>金额</th>
				<th>数量</th>
				<th>单价</th>
				<th>金额</th>
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
		
		<%if(isShowWarehouseInfo){ %>
		<!-- 关联仓库信息 -->
		<p class="article_tit">关联仓库信息</p>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_style">
			<tr>
				<th>仓库名</th>
				<th>地址</th>
				<th>联系电话</th>
			</tr>
			<%for(int i =1;i<warehouseInfoExcelList.size();i++){ %>
				<tr>
					<%for(Object cell : warehouseInfoExcelList.get(i)){ %>
						<%if(StringKit.isValid(cell.toString())){ %>
							<td><%=cell.toString()%></td>
						<%} %>
					<%} %>
				</tr>
			<%} %>
		<%} %>
		</table>
	<%} %>
</body>
<script>
$(document).ready(function() {
	$("#selectWarehouses").click(
		function() {
			var selectedItem = $("#warehouseIds").val();
			$.fancybox.open({
				href :  'WarehouseInfoSelect.do?callbackFun=selectWarehouse&isMultiple=true&selectedItem='+selectedItem,
				type : 'iframe',
				width : 560,
				minHeight : 500
		});
	});
});

function selectWarehouse(warehouseIds){
	if(typeof(warehouseIds) != 'undefined'&&warehouseIds.toString().trim() != ""){
		$("#warehouseIds").val(warehouseIds.toString());
		submitForm('false');
	}
}
function submitForm(isExport){
	var form = $('#form');
	form.attr("action","WareInventoryMonthlyReport.do?isExport="+isExport);
	form.submit();
}
</script>
</html>