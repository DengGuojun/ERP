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
	List<WarehouseInventoryAggregateReportBean> reportList = (List<WarehouseInventoryAggregateReportBean>)request.getAttribute("WarehouseInventoryReportList");
	Map<String, String> warehouseMap = (Map<String, String>)request.getAttribute("WarehouseMap");
	PageBean PAGE_BEAN = (PageBean) request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>) request.getAttribute("CondList");
	AdminUserHelper adminUserHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	int wareType = (Integer)request.getAttribute("WareType");
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
	<p class="article_tit"><%=MapKit.getValueFromMap(wareType, WarehouseInventoryConfig.WARE_TYPE_MAP)%>库存列表</p>
	<form name="formSearch" method="post" action="WarehouseInventoryAggregateReportList.do?wareType=<%=wareType%>">
  	<div class="search_form">
  	    <em class="em1"><%=MapKit.getValueFromMap(wareType, WarehouseInventoryConfig.WARE_TYPE_MAP)%>编码：</em> 
  	            <%if(wareType == InfoTypeConfig.INFO_TYPE_MATERIAL){ %>
				<input type="text" name="materialNumber" id="materialNumber" value="<%=ParamKit.getParameter(request, "materialNumber", "")%>" size="40" /> 
				<%}else{ %>
				<input type="text" name="productNumber" id="productNumber" value="<%=ParamKit.getParameter(request, "productNumber", "")%>" size="40" /> 
				<%} %>
		<em class="em1"><%=MapKit.getValueFromMap(wareType, WarehouseInventoryConfig.WARE_TYPE_MAP)%>类型：</em> 
		        <input type="hidden" name="typeId" id="typeId" value="" /> 
		        <%if(wareType == InfoTypeConfig.INFO_TYPE_MATERIAL){ %>
				<input type="text" name="materialType" id="materialType" value="<%=ParamKit.getParameter(request, "materialType", "")%>" size="20" readOnly/> 
				<input type="button" name="selectMaterial" id="selectMaterial" value="选择" />	
				<%}else{ %>
				<input type="text" name="productType" id="productType" value="<%=ParamKit.getParameter(request, "productType", "")%>" size="20" readOnly/> 
				<input type="button" name="selectProduct" id="selectProduct" value="选择" />	
				<%} %>
		<em class="em1"></em>		
   	    <input name="" type="submit" class="search_btn_sub" value="查询"/>
   		<input name="" type="button" class="search_btn_sub" value="条件重置" onclick="location.href='WarehouseInventoryAggregateReportList.do?wareType=<%=wareType%>'" />
   	</div>
   	</form> 
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			class="table_style"  style="word-break:break-all; word-wrap:break-all;">
			<tr>
				<th>仓库名称</th>
				<th><%=MapKit.getValueFromMap(wareType, WarehouseInventoryConfig.WARE_TYPE_MAP)%>名称</th>
				<th>实库库存</th>
				<th>待上架库存</th>
				<th>残次品</th>
				<th>已报损</th>
				<th>危急值</th>
				<th>预警状态</th>
				<th>上一次库存更新时间</th>
			</tr>
			<%
			    PdmServiceClient client = new PdmServiceClient();
				WarehouseInventoryPrewarningProcessor helper = new WarehouseInventoryPrewarningProcessor();
				String wareName="";
				for (WarehouseInventoryAggregateReportBean bean : reportList) {
			%>
			<tr>
				<td><%=warehouseMap.get(bean.get_id()) %></td>
				<%
				if(bean.getWareType()==InfoTypeConfig.INFO_TYPE_MATERIAL){
					wareName = client.getMaterialInfoByKey(bean.getWareId()).getMaterialName();
				}else{
					wareName = client.getProductItemByKey(bean.getWareId()).getItemName();
				}%>
				<td><%=wareName%></td>
				<td><%=new BigDecimal(bean.getNormalQuantity()) %></td>
				<td><%=new BigDecimal(bean.getPendingQuantity())%></td>
				<td><%=new BigDecimal(bean.getDefectQuantity())%></td>
				<td><%=new BigDecimal(bean.getDamageQuantity())%></td>
				<%String warningValue ="";
				String warning ="";
				warning =helper.getPrewarningStrByWiReportBean(bean,WarehouseInventoryPrewarningConfig.PREWARNING_TYPE_INVENTORY);
				warningValue=helper.getInventoryPrewarningStr(bean);
				%>
				<td><%=warningValue %></td>
				<td><%=warning %></td>
				<td><%=bean.getModifyTime() !=null ?  DateKit.formatTimestamp(bean.getModifyTime(),DateKit.DEFAULT_DATE_TIME_FORMAT) : "--"%></td>
			</tr>
			<%
				}
			%>
		</table>
		<ul class="page_info">
		<li class="page_left_btn">
		</li>
		<%@ include file="../../include/page.jsp"%>
	</ul>
</body>
<script type='text/javascript'>
	$(document).ready(function() {
		$("#selectMaterial").click(
				function() {
					var url = "<%=PDM_URL%>/pdm/MaterialTypeSelect.do?callbackFun=selectMaterialType"
					$.fancybox.open({
						href :  url,
						type : 'iframe',
						width : 560,
						minHeight : 150
				});
			});
		$("#selectProduct").click(
				function() {
					var url = "<%=PDM_URL%>/pdm/ProductTypeSelect.do?callbackFun=selectProductType"
					$.fancybox.open({
						href :  url,
						type : 'iframe',
						width : 560,
						minHeight : 150
				});
			});
	});
	function selectMaterialType(typeId,typeName){
		if(typeId != ""){
			$("#typeId").val(typeId);
			$("#materialType").val(typeName);
		}
	}
	function selectProductType(typeId1,typeId2,typeName){
		if(typeId2 != ""){
			$("#typeId").val(typeId2);
			$("#productType").val(typeName);
		}
	}
</script>
</html>