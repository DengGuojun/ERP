<%@page import="java.math.BigDecimal"%>
<%@page import="com.lpmas.erp.inventory.business.*"%>
<%@page import="com.lpmas.constant.info.InfoTypeConfig"%>
<%@page import="com.lpmas.pdm.client.PdmServiceClient"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.lpmas.erp.inventory.config.*"%>
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
<%@ page import="com.lpmas.erp.inventory.bean.*"%>
<%@ include file="../../include/header.jsp"%>
<%
	List<WarehouseInventoryReportBean> reportList = (List<WarehouseInventoryReportBean>)request.getAttribute("WarehouseInventoryReportList");
	Map<String, String> warehouseMap = (Map<String, String>)request.getAttribute("WarehouseMap");
	Map<String, Double> inventoryPrewarningMap = (Map<String, Double>)request.getAttribute("InventoryPrewarningMap");
	PageBean PAGE_BEAN = (PageBean) request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>) request.getAttribute("CondList");
	AdminUserHelper adminUserHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>库存列表</title>
<link href="<%=STATIC_URL%>/css/main.css" type="text/css"
	rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL%>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL%>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL%>/js/ui.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.js"></script>
<link rel="stylesheet" href="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.css" type="text/css" media="screen" />
<script type="text/javascript" src="<%=STATIC_URL%>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
        document.domain='<%=DOMAIN%>'; 
</script>
</head>

<body class="article_bg">
	<p class="article_tit">库存列表</p>
	<form name="formSearch" method="post" action="WarehouseInventoryReportList.do">
  	<div class="search_form">
  			<em class="em1">仓库：</em>
  			<input type="text" name="warehouseName" id="warehouseName" value="<%=ParamKit.getParameter(request, "warehouseName", "") %>"  readOnly size="20" />
	      	<input type="hidden" name="warehouseId" id="warehouseId" value="<%=ParamKit.getParameter(request, "warehouseId", "") %>"/>
	      	<input type="button" name="selectWarehouse" id="selectWarehouse" value="选择" />
			<em class="em1">制品类型：</em> 
			<select name="wareType" id="wareType">
				<option value="0"></option>
				<%for (StatusBean<Integer, String> statusBean : WarehouseInventoryConfig.WARE_TYPE_LIST) {%>
				<option value="<%=statusBean.getStatus()%>"
					<%=(statusBean.getStatus() == ParamKit.getIntParameter(request, "wareType", 0)) ? "selected" : ""%>><%=statusBean.getValue()%></option>
				<%}%>
			</select>
			<em class="em1">生产日期：</em> 
			<input type="text" name="productionDate"id="productionDate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'2008-01-01',maxDate:'2020-01-01'})"
				value="<%=ParamKit.getParameter(request, "productionDate", "")%>" size="20" checkStr="查询日期;date;false;;100" />
			<em class="em1">批次号：</em>
			<input type="text"name="batchNumber" id="batchNumber" value="<%=ParamKit.getParameter(request, "batchNumber", "")%>"size="20" />
			<em class="em1">开始日期：</em>
			<input type="text" name="gt_dateTime"id="gt_dateTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'2008-01-01',maxDate:$('#endDateTime').val()})"
				value="<%=ParamKit.getParameter(request, "gt_dateTime", "")%>" size="20" checkStr="查询开始日期;date;false;;100" />
			<em class="em1">结束日期：</em>
			<input type="text" name="lt_dateTime" id="lt_dateTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:$('#begDateTime').val(),maxDate:'2020-01-01'})"
				value="<%=ParamKit.getParameter(request, "lt_dateTime", "")%>" size="20" checkStr="查询结束日期;date;false;;100" />
		<input name="" type="submit" class="search_btn_sub" value="查询"/>
   		<input name="" type="button" class="search_btn_sub" value="条件重置" onclick="location.href='WarehouseInventoryReportList.do'" />
   	</div>
   	</form> 
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			class="table_style"  style="word-break:break-all; word-wrap:break-all;">
			<tr>
				<th>选择</th>
				<th>仓库名称</th>
				<th>制品类型</th>
				<th>制品名称</th>
				<th>实库库存</th>
				<th>待上架库存</th>
				<th>残次品</th>
				<th>已报损</th>
				<th>批次号</th>
				<th>生产日期</th>
				<th>保质期(天)</th>
				<th>保质期至</th>
				<th>剩余保质期(天)</th>
				<th>保质期危急值</th>
				<th>预警状态</th>
			</tr>
			<%
				PdmServiceClient client = new PdmServiceClient();
				WarehouseInventoryPrewarningProcessor helper = new WarehouseInventoryPrewarningProcessor();
				String wareName="";
				for (WarehouseInventoryReportBean bean : reportList) {
			%>
			<tr>
				<td>
					<input type="radio" name="reportId" id="radio_<%=bean.get_id()%>" value="<%=bean.get_id()%>" >
				</td>
				<td><%=warehouseMap.get(bean.get_id()) %></td>
				<td><%=WarehouseInventoryConfig.WARE_TYPE_MAP.get(bean.getWareType())%></td>
				<%
				if(bean.getWareType()==InfoTypeConfig.INFO_TYPE_MATERIAL){
					wareName = client.getMaterialInfoByKey(bean.getWareId()).getMaterialName();
				}else{
					wareName = client.getProductItemByKey(bean.getWareId()).getItemName();
				}%>
				<td><%=wareName%></td>
				<td id="normalQuantity_<%=bean.get_id()%>"><%=new BigDecimal(bean.getNormalQuantity()) %></td>
				<td id="pendingQuantity_<%=bean.get_id()%>"><%=new BigDecimal(bean.getPendingQuantity())%></td>
				<td id="defectQuantity_<%=bean.get_id()%>"><%=new BigDecimal(bean.getDefectQuantity())%></td>
				<td><%=bean.getDamageQuantity()%></td>
				<td><%=bean.getBatchNumber()%></td>
				<td><%=DateKit.formatTimestamp(bean.getProductionDate(), DateKit.DEFAULT_DATE_FORMAT)%></td>
				<td><%=bean.getGuaranteePeriod() == -1f ? "--":bean.getGuaranteePeriod()%></td>
				<td><%=bean.getExpirationDate() !=null ?  DateKit.formatTimestamp(bean.getExpirationDate(),DateKit.DEFAULT_DATE_FORMAT) : "--"%></td>
				<%
				String warning ="";
				Double remainDays = null;
				warning =helper.getPrewarningStrByWiReportBean((WarehouseInventoryAggregateReportBean)bean,WarehouseInventoryPrewarningConfig.PREWARNING_TYPE_EXPIRATION);
				remainDays = helper.calculateRemainGuaranteePeriod(bean.getExpirationDate());
				Double inventoryWarning = inventoryPrewarningMap.get(bean.get_id());
				%>
				<td><%=remainDays ==null ? "--": remainDays.intValue()%></td>
				<td><%=inventoryWarning !=null ?  inventoryWarning : "--" %></td>
				<td><%=warning %></td>
			</tr>
			<%
				}
			%>
		</table>
		<ul class="page_info">
		<li class="page_left_btn">
		<%if(adminUserHelper.hasPermission(InventoryResource.WAREHOUSE_INVENTORY, OperationConfig.UPDATE)){ %>
			<input type="button" name="putOn" id="putOn" value="上架">
			<input type="button" name="defect" id="defect" value="报次">
			<input type="button" name="damage" id="damage" value="报损">
			<input type="button" name="normal" id="normal" value="次转正">
			<!-- <input type="button" name="loss" id="loss" value="盘亏">
			<input type="button" name="overage" id="overage" value="盘盈"> -->
		<%} %>
		</li>
		<%@ include file="../../include/page.jsp"%>
	</ul>
</body>
<script type='text/javascript'>
$(document).ready(function() {
	$("#putOn").click(function() {
		var reportId = $('input:radio[name=reportId]:checked').val();	
		if (typeof(reportId) == 'undefined'){
			alert("请选择需要上架的库存");
			return;
		}
		var pendingQuantity = $("#pendingQuantity_"+reportId).html();
		if(pendingQuantity == 0){
			alert("此批次的产品已全部上架");
			return;
		}
		$.fancybox.open({
			href : 'WarehouseInventoryPutOnSelect.do?callbackFun=putOnWarehouseInventory&reportId=' + reportId,
			type : 'iframe',
			width : 650,
			minHeight : 650
		});
	});
	
	$("#defect").click(function() {
		var reportId = $('input:radio[name=reportId]:checked').val();	
		if (typeof(reportId) == 'undefined'){
			alert("请选择需要报次的库存");
			return;
		}
		var normalQuantity = $("#normalQuantity_"+reportId).html();
		if(normalQuantity == 0){
			alert("此批次的实库库存数量为0");
			return;
		}
		$.fancybox.open({
			href : 'WarehouseInventoryChangeSelect.do?callbackFun=warehouseInventoryManage&statusAction=<%=WarehouseInventoryConfig.WIO_TYPE_DEFECT%>&reportId=' + reportId,
			type : 'iframe',
			width : 560,
			minHeight : 150
		});
	});
	
	$("#damage").click(function() {
		var reportId = $('input:radio[name=reportId]:checked').val();	
		if (typeof(reportId) == 'undefined'){
			alert("请选择需要报损的库存");
			return;
		}
		var normalQuantity = $("#normalQuantity_"+reportId).html();
		if(normalQuantity == 0){
			alert("此批次的实库库存数量为0");
			return;
		}
		$.fancybox.open({
			href : 'WarehouseInventoryChangeSelect.do?callbackFun=warehouseInventoryManage&statusAction=<%=WarehouseInventoryConfig.WIO_TYPE_DAMAGE%>&reportId=' + reportId,
			type : 'iframe',
			width : 560,
			minHeight : 150
		});
	});
	
	$("#normal").click(function() {
		var reportId = $('input:radio[name=reportId]:checked').val();	
		if (typeof(reportId) == 'undefined'){
			alert("请选择需要次转正的库存");
			return;
		}
		var defectQuantity = $("#defectQuantity_"+reportId).html();
		if(defectQuantity == 0){
			alert("此批次的残次品库存数量为0");
			return;
		}
		$.fancybox.open({
			href : 'WarehouseInventoryChangeSelect.do?callbackFun=warehouseInventoryManage&statusAction=<%=WarehouseInventoryConfig.WIO_TYPE_NORMAL%>&reportId=' + reportId,
			type : 'iframe',
			width : 560,
			minHeight : 150
		});
	});
	
	$("#loss").click(function() {
		var reportId = $('input:radio[name=reportId]:checked').val();	
		if (typeof(reportId) == 'undefined'){
			alert("请选择需要盘亏的库存");
			return;
		}
		var normalQuantity = $("#normalQuantity_"+reportId).html();
		if(normalQuantity == 0){
			alert("此批次的实库库存数量为0");
			return;
		}
		$.fancybox.open({
			href : 'WarehouseInventoryChangeSelect.do?callbackFun=warehouseInventoryManage&statusAction=<%=WarehouseInventoryConfig.WIO_TYPE_LOSS%>&reportId=' + reportId,
			type : 'iframe',
			width : 560,
			minHeight : 150
		});
	});
	
	$("#overage").click(function() {
		var reportId = $('input:radio[name=reportId]:checked').val();	
		if (typeof(reportId) == 'undefined'){
			alert("请选择需要盘盈的库存");
			return;
		}
		$.fancybox.open({
			href : 'WarehouseInventoryChangeSelect.do?callbackFun=warehouseInventoryManage&statusAction=<%=WarehouseInventoryConfig.WIO_TYPE_OVERAGE%>&reportId=' + reportId,
			type : 'iframe',
			width : 560,
			minHeight : 150
		});
	});
	
	$("#selectWarehouse").click(
			function() {
				var warehouseId = $("#warehouseId").val();
				$.fancybox.open({
					href :  'WarehouseInfoSelect.do?callbackFun=selectWarehouse&warehouseId='+warehouseId,
					type : 'iframe',
					width : 560,
					minHeight : 500
			});
		});
});

function putOnWarehouseInventory(wvId) {
	var url = 'WarehouseInventoryManage.do?statusAction=<%=WarehouseInventoryConfig.WIO_TYPE_PUT_ON%>&wvId='+ wvId;
	window.location.href= url
}

function warehouseInventoryManage(statusAction,quantity){
	var reportId = $('input:radio[name=reportId]:checked').val();
	var url = 'WarehouseInventoryManage.do?statusAction='+statusAction+'&quantity='+ quantity + '&reportId='+reportId;
	window.location.href= url
}

function selectWarehouse(warehouseId, warehouseName){
	if(warehouseId != "" && warehouseName != ""){
		$("#warehouseId").val(warehouseId);
		$("#warehouseName").val(warehouseName)
	}
}
</script>
</html>