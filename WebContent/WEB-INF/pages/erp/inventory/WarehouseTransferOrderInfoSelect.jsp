<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"  %>
<%@ page import="com.lpmas.framework.config.*"  %>
<%@ page import="com.lpmas.framework.bean.*"  %>
<%@ page import="com.lpmas.framework.page.*"  %>
<%@ page import="com.lpmas.framework.util.*"  %>
<%@ page import="com.lpmas.framework.web.*"  %>
<%@ page import="com.lpmas.erp.inventory.bean.*"  %>
<%@ page import="com.lpmas.erp.inventory.config.*"  %>
<%@ page import="com.lpmas.erp.warehouse.bean.*"  %>
<%
	String callbackFun = ParamKit.getParameter(request, "callbackFun", "callbackFun");
	int toId = (Integer)request.getAttribute("toId");
	List<WarehouseTransferOrderInfoBean> list = (List<WarehouseTransferOrderInfoBean>) request.getAttribute("TransferOrderInfoList");
	Map<Integer,WarehouseInfoBean> warehouseInfoMap = (Map<Integer,WarehouseInfoBean>) request.getAttribute("WarehouseInfoMap");
	PageBean PAGE_BEAN = (PageBean)request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>)request.getAttribute("CondList");
%>
<%@ include file="../../include/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加调拨单</title>
<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL %>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
<script type="text/javascript">
        document.domain='<%=DOMAIN%>'; 
</script>
</head>
<body class="article_bg">
<form name="formSearch" method="post" action="WarehouseTransferOrderInfoSelect.do">
<div class="search_form">
    <input type="text" name="toNumber" id="toNumber" value="<%=ParamKit.getParameter(request, "toNumber", "") %>"  placeholder="请输入调拨单号" size="70"/>
    <input type="hidden" name="toId" value="<%=toId %>" />
    <input type="hidden" name="callbackFun" value="<%=callbackFun %>" />
    <input name="" type="submit" class="search_btn_sub" value="筛选"/>
  </div>
  <table width="100%" border="0" cellpadding="0" class="table_style">
    <tr>
      <th>选择</th>
      <th>调拨单号</th>
      <th>调拨类型</th>
      <th>出库仓库</th>
	  <th>入库仓库</th>
    </tr>
    <%
    for(WarehouseTransferOrderInfoBean bean:list){%> 
    <tr>
      <td align="center"><input type="radio" name="toId" value="<%=bean.getToId()%>" <%=toId==bean.getToId() ? "checked" : ""%>></td>
      <td><%=bean.getToNumber()%></td>
      <td><%=MapKit.getValueFromMap(bean.getWareType(), InventoryConsoleConfig.WARE_TYPE_MAP)%></td>
      <td><%=warehouseInfoMap.get(bean.getSourceWarehouseId()).getWarehouseName()%></td>
	  <td><%=warehouseInfoMap.get(bean.getTargetWarehouseId()).getWarehouseName()%></td>
    </tr>	
    <input type="hidden" id="transferOrderJsonStr_<%=bean.getToId()%>" value='<%=JsonKit.toJson(bean)%>'>
    <input type="hidden" id="sourceWarehouseJsonStr_<%=bean.getToId()%>" value='<%=JsonKit.toJson(warehouseInfoMap.get(bean.getSourceWarehouseId()))%>'>
    <input type="hidden" id="targetWarehouseJsonStr_<%=bean.getToId()%>" value='<%=JsonKit.toJson(warehouseInfoMap.get(bean.getTargetWarehouseId()))%>'>
    <%} %>
  </table>
</form>
<ul class="page_info">
<li class="page_left_btn">
  <input type="submit" name="button" id="button" class="modifysubbtn" value="选择" onclick="callbackTo()" />
</li>
<%@ include file="../../include/page.jsp" %>
</ul>
<input type="button" class="btn_fixed" value="选择" onclick="callbackTo()" />
</body>
<script>
function callbackTo(){
	var toId = $('input:radio[name=toId]:checked').val();
	if (typeof(toId) == 'undefined'){
		alert("请选择调拨单");
		return;
	}
	var transferOrderJsonStr = $("#transferOrderJsonStr_"+toId).val();
	var sourceWarehouseJsonStr = $("#sourceWarehouseJsonStr_"+toId).val();
	var targetWarehouseJsonStr = $("#targetWarehouseJsonStr_"+toId).val();
	
	self.parent.<%=callbackFun %>(transferOrderJsonStr,sourceWarehouseJsonStr,targetWarehouseJsonStr);
	try{ self.parent.jQuery.fancybox.close(); }catch(e){console.log(e);}
}
</script>
</html>