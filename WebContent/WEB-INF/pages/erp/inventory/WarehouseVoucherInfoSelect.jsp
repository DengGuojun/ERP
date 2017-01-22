<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"  %>
<%@ page import="com.lpmas.framework.config.*"  %>
<%@ page import="com.lpmas.framework.bean.*"  %>
<%@ page import="com.lpmas.framework.page.*"  %>
<%@ page import="com.lpmas.framework.util.*"  %>
<%@ page import="com.lpmas.framework.web.*"  %>
<%@ page import="com.lpmas.admin.bean.*"  %>
<%@ page import="com.lpmas.admin.business.*"  %>
<%@ page import="com.lpmas.admin.config.*"  %>
<%@page import="com.lpmas.admin.client.cache.AdminUserInfoClientCache"%>
<%@ page import="com.lpmas.erp.config.*"  %>
<%@ page import="com.lpmas.erp.inventory.config.*"  %>
<%@ page import="com.lpmas.erp.inventory.bean.*"  %>
<%@ page import="com.lpmas.erp.inventory.business.*"  %>
<%@ page import="com.lpmas.constant.info.*"  %>
<%@ page import="com.lpmas.erp.warehouse.bean.*"  %>
<%@ include file="../../include/header.jsp" %>
<%
    String callbackFun = ParamKit.getParameter(request, "callbackFun", "callbackFun");
	AdminUserHelper adminUserHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	List<WarehouseVoucherInfoBean> list = (List<WarehouseVoucherInfoBean>)request.getAttribute("WarehouseVoucherList");
	PageBean PAGE_BEAN = (PageBean)request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>)request.getAttribute("CondList");
	int wvId = (Integer)request.getAttribute("wvId");
	String mode = ParamKit.getParameter(request, "mode", "general");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择入库订单</title>
<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL %>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
<script type="text/javascript">
        document.domain='<%=DOMAIN%>'; 
</script>
</head>
<body class="article_bg">
<form name="formSearch" method="post" action="WarehouseVoucherInfoSelect.do">
  <div class="search_form">
    <input type="text" name="wvNumber" id="wvNumber" value="<%=ParamKit.getParameter(request, "wvNumber", "") %>"  placeholder="请输入入库订单编号" size="20"/>
    <input type="hidden" name="wvId" value="<%=wvId %>" />
    <input type="hidden" name="mode" value="<%=mode %>" />
    <input type="hidden" name="wareType" value="<%=ParamKit.getParameter(request, "wareType", "") %>" />
    <input type="hidden" name="callbackFun" value ="<%=callbackFun %>" />
    <input name="" type="submit" class="search_btn_sub" value="筛选"/>
  </div>
</form> 
<table width="100%" border="0" cellpadding="0" class="table_style">
    <tr>
      <th>选择</th>
      <th>入库单号</th>
      <th>制品类型</th>
      <th>入库类型</th>
      <%if(mode.equals("log")){%>	
      <th>有效状态</th>
      <%} %>
    </tr>
    <%
    for(WarehouseVoucherInfoBean bean:list){%> 
    <tr>
    
    	  <td align="center">
      	  <input type="radio" name="wvId" value="<%=bean.getWvId()%>" <%=wvId == bean.getWvId() ? "checked" : ""%>>   	  
          </td>
          <td id="wvNumber_<%=bean.getWvId() %>"><%=bean.getWvNumber() %></td>
          <td><%=MapKit.getValueFromMap(bean.getWareType(), InventoryConsoleConfig.WARE_TYPE_MAP) %></td>
          <td><%=MapKit.getValueFromMap(bean.getWvType(), WarehouseVoucherConfig.WAREHOUSE_VOUCHER_TYPE_MAP) %></td>
          <%if(mode.equals("log")){%>	
          <td><%=Constants.STATUS_MAP.get(bean.getStatus())%></td>
          <%} %>
    </tr>	
    <%} %>
  </table>
  <ul class="page_info">
<li class="page_left_btn">
  <input type="submit" name="button" id="button" class="modifysubbtn" value="选择" onclick="callbackTo()" />
</li>
<%@ include file="../../include/page.jsp"%>
</ul>
</body>
<script>
function callbackTo(){
	var wvId = $('input:radio[name=wvId]:checked').val();
	if (typeof(wvId) == 'undefined'){
		alert("请选择入库单");
		return;
	}
	var wvNumber = $("#wvNumber_"+wvId).html();
	self.parent.<%=callbackFun %>(wvId, wvNumber);
	try{ self.parent.jQuery.fancybox.close(); }catch(e){console.log(e);}
}
</script>
</html>