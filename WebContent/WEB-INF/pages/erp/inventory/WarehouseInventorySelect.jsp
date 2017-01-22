<%@page import="java.math.BigDecimal"%>
<%@page import="com.lpmas.pdm.bean.ProductItemBean"%>
<%@page import="com.lpmas.pdm.bean.MaterialInfoBean"%>
<%@page import="com.caucho.server.admin.ListJmxQueryReply.Bean"%>
<%@page import="com.lpmas.constant.info.InfoTypeConfig"%>
<%@page import="com.lpmas.pdm.client.PdmServiceClient"%>
<%@page import="com.lpmas.pdm.bean.*"%>
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
	List<WarehouseInventoryBean> list = (List<WarehouseInventoryBean>)request.getAttribute("WarehouseInventoryList");
	Map<Integer, MaterialTypeBean> materialTypeMap = (Map<Integer, MaterialTypeBean>)request.getAttribute("MaterialTypeMap");
	Map<Integer, MaterialInfoBean> materialInfoMap = (Map<Integer, MaterialInfoBean>)request.getAttribute("MaterialInfoMap");
	Map<Integer, ProductTypeBean> productTypeMap = (Map<Integer, ProductTypeBean>)request.getAttribute("ProductTypeMap");
	Map<Integer, ProductItemBean> productItemMap = (Map<Integer, ProductItemBean>)request.getAttribute("ProductItemMap");
	Map<String, String> unitMap = (Map<String, String>)request.getAttribute("UnitMap");
	PageBean PAGE_BEAN = (PageBean) request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>) request.getAttribute("CondList");
	AdminUserHelper adminUserHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	String callbackFun = ParamKit.getParameter(request, "callbackFun", "callbackFun");
	String openOrPop = ParamKit.getParameter(request, "openOrPop", "open");
	int wareType = ParamKit.getIntParameter(request, "wareType", 0);
	int warehouseId = ParamKit.getIntParameter(request, "warehouseId", 0);
	boolean isMultiple = ParamKit.getBooleanParameter(request, "isMultiple",true);
	String selectedItem = ParamKit.getParameter(request, "selectedItem", "");
	List<String> selectedItemList = new ArrayList<String>();
	if(!selectedItem.trim().equals("")){
		selectedItemList = ListKit.string2List(selectedItem, ",");
	}
	String mode = (String)request.getAttribute("mode");
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
<script type="text/javascript"
	src="<%=STATIC_URL%>/js/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="../../js/erp-common.js"></script>
	<script>
document.domain='<%=DOMAIN%>';
</script>
</head>


<body class="article_bg">
	<p class="article_tit">库存列表</p>
	<form name="formSearch" method="post" action="WarehouseInventorySelect.do?wareType=<%=wareType%>&warehouseId=<%=warehouseId%>&callbackFun=<%=callbackFun%>&openOrPop=<%=openOrPop%>&selectedItem=<%=selectedItem%>">
		<div class="search_form">
			<em class="em1">批次号：</em> 
			<input type="text" name="batchNumber" id="batchNumber" value="<%=ParamKit.getParameter(request, "batchNumber", "") %>" placeholder="批次号" size="40">
			<input type="hidden" id="mode" name="mode" value="<%=mode%>">
			<%if(!StringKit.isValid(mode)){ %>
			<em class="em1">库存类型：</em> 
			<select name="inventoryType" id="inventoryType">
							<option value=""></option>
							<%
								int inventoryType = ParamKit.getIntParameter(request, "inventoryType", 0);
								for (StatusBean<Integer, String> statusBean : WarehouseInventoryConfig.WAREHOUSE_INVENTORY_TYPE_LIST) {
							%>
							<option value="<%=statusBean.getStatus()%>"
								<%=(statusBean.getStatus().equals(inventoryType)) ? "selected" : ""%>><%=statusBean.getValue()%></option>
							<%
								}
							%>
					</select>
			<%} %>
		 <input name="" type="submit" class="search_btn_sub" value="查询" />
		</div>
	</form>
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			class="table_style">
			<tr>
				<%if(isMultiple){%>
				<th><input type="checkbox" id="selectAll" onchange="checkAll(this)">选择</th>
				<%}else{%>
				<th>选择</th>
				<%} %>
				<th>制品类型</th>
				<th>制品名称</th>
				<th>批次号</th>
				<th>库存类型</th>
				<th>库存数量</th>
				<th>有效期至</th>
			</tr>
			<%
			 	Map<String,Object> itemValueMap = new HashMap<String,Object>();
				for (WarehouseInventoryBean bean : list) {
					if(wareType==InfoTypeConfig.INFO_TYPE_MATERIAL){
						MaterialInfoBean material = materialInfoMap.get(bean.getWareId());
						itemValueMap.put("itemId", String.valueOf(material.getMaterialId()));
				    	itemValueMap.put("itemTypeName", materialTypeMap.get(bean.getWareId()).getTypeName());
				    	itemValueMap.put("itemNumber", material.getMaterialNumber());
				    	itemValueMap.put("itemName", material.getMaterialName());
				    	itemValueMap.put("itemGuaranteePeriod", material.getGuaranteePeriod());
				    	itemValueMap.put("itemSpecification", material.getSpecification());
				    	itemValueMap.put("itemTypeId",String.valueOf(materialTypeMap.get(material.getMaterialId()).getTypeId()));
					}else if(wareType==InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM){
						ProductItemBean productItem = productItemMap.get(bean.getWareId());
						itemValueMap.put("itemId", String.valueOf(productItem.getItemId()));
				    	itemValueMap.put("itemTypeName", productTypeMap.get(bean.getWareId()).getTypeName());
				    	itemValueMap.put("itemNumber", productItem.getItemNumber());
				    	itemValueMap.put("itemName", productItem.getItemName());
				    	itemValueMap.put("itemGuaranteePeriod", productItem.getGuaranteePeriod());
				    	itemValueMap.put("itemSpecification", productItem.getSpecification());
				    	itemValueMap.put("itemTypeId",String.valueOf(productTypeMap.get(bean.getWareId()).getTypeId()));
					}
					itemValueMap.put("productionDate",DateKit.formatTimestamp(bean.getProductionDate(), DateKit.DEFAULT_DATE_FORMAT));
					itemValueMap.put("expirationDate",DateKit.formatTimestamp(bean.getExpirationDate(), DateKit.DEFAULT_DATE_FORMAT));
					itemValueMap.put("unitNmae", unitMap.get(bean.getUnit()));
					itemValueMap.put("inventoryTypeName",WarehouseInventoryConfig.WAREHOUSE_INVENTORY_TYPE_MAP.get(bean.getInventoryType()));
					itemValueMap.put("itemInventory", bean);
					itemValueMap.put("wareType",wareType);
			%>
			<tr>
			 <%if(isMultiple){%>
			<%if(selectedItemList.contains(String.valueOf(bean.getWiId()))){ %>
   			<td><input type="checkbox" name="select" selected="selected" onclick="alert('此该库存您已经选择,请勿重复选择!');return false;"/></td>
      		<%}else{ %>
      		<td><input type="checkbox" name="select" /></td>
      		<%} %>
			<%}else{%>
					<td><input type="radio" name="radio" /></td>
			<%} %>
				<td><%=WarehouseInventoryPrewarningConfig.WARE_TYPE_MAP.get(bean.getWareType())%></td>
				<td><%=itemValueMap.get("itemName")%></td>
				<td><%=bean.getBatchNumber() %></td>
				<td><%=WarehouseInventoryConfig.WAREHOUSE_INVENTORY_TYPE_MAP.get(bean.getInventoryType()) %></td>
				<td><%=new BigDecimal(bean.getQuantity())%></td>
				<td><%=bean.getExpirationDate()!=null?DateKit.formatTimestamp(bean.getExpirationDate(), DateKit.DEFAULT_DATE_FORMAT):"--"%></td>
				<td><input type="hidden" id="itemValueJson" value='<%=JsonKit.toJson(itemValueMap) %>'></td>
			</tr>
			<%
			itemValueMap.clear();
				}
			%>
		</table>
			
		<ul class="page_info">
		<li class="page_left_btn">
			<input type="button" name="new" id="new" value="选择" onclick="excuteCallBack()">
		</li>
		<%@ include file="../../include/page.jsp"%>
	</ul>
	<input type="button" class="btn_fixed" value="选择" onclick="excuteCallBack()">
</body>
<script type='text/javascript'>
function excuteCallBack(){
	<%if(isMultiple){%>
	var checkboxs = $('input[name=select]:checked');
	var itemValue = new Array();
	if(checkboxs.length>0){
		checkboxs.each(function(){
			itemValue.push($(this).parent().parent().find('#itemValueJson').val());
		});
	<%}else{%>
	var radio = $('input:radio[name=radio]:checked');
	var itemValue = new Array();
	if(radio.length==1){
		itemValue.push(radio.parent().parent().find('#itemValueJson').val());
	<%} %>
		<%
		if(openOrPop.equals("open")){%>
		window.opener.<%=callbackFun%>(itemValue);
		self.close();
		<%}else if(openOrPop.equals("pop")){%>
		self.parent.<%=callbackFun%>(itemValue);
		try{ self.parent.jQuery.fancybox.close(); }catch(e){console.log(e);}
		<%}%>
	}else{
		alert("请选择要出库的制品!");
	}
}
</script>
</html>