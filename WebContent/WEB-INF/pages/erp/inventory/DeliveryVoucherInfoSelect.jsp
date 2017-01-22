<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.lpmas.framework.config.*"%>
<%@ page import="com.lpmas.framework.bean.*"%>
<%@ page import="com.lpmas.framework.page.*"%>
<%@ page import="com.lpmas.framework.util.*"%>
<%@ page import="com.lpmas.framework.web.*"%>
<%@ page import="com.lpmas.admin.bean.*"%>
<%@ page import="com.lpmas.admin.business.*"%>
<%@ page import="com.lpmas.admin.config.*"%>
<%@page import="com.lpmas.admin.client.cache.AdminUserInfoClientCache"%>
<%@ page import="com.lpmas.erp.config.*"  %>
<%@ page import="com.lpmas.erp.purchase.config.*"  %>
<%@ page import="com.lpmas.erp.inventory.config.*"  %>
<%@ page import="com.lpmas.erp.inventory.bean.*"  %>
<%@ page import="com.lpmas.erp.inventory.business.*"  %>
<%@ page import="com.lpmas.constant.info.*"  %>
<%@ page import="com.lpmas.erp.warehouse.bean.*"  %>
<%@ include file="../../include/header.jsp" %>
<%
    String callbackFun = ParamKit.getParameter(request, "callbackFun", "callbackFun");
	AdminUserHelper adminUserHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	List<DeliveryVoucherInfoBean> list = (List<DeliveryVoucherInfoBean>)request.getAttribute("DeliveryVoucherList");
	PageBean PAGE_BEAN = (PageBean)request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>)request.getAttribute("CondList");
	int dvId = (Integer)request.getAttribute("dvId");
	String mode = ParamKit.getParameter(request, "mode", "general");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择出库订单</title>
<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL %>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
<script type="text/javascript">
        document.domain='<%=DOMAIN%>'; 
</script>
</head>
<body class="article_bg">
<form name="formSearch" method="post" action="DeliveryVoucherInfoSelect.do">
  <div class="search_form">
    <input type="text" name="dvNumber" id="dvNumber" value="<%=ParamKit.getParameter(request, "dvNumber", "") %>"  placeholder="请输入出库订单编号" size="20"/>
    <input type="hidden" name="dvId" value="<%=dvId %>" />
    <input type="hidden" name="mode" value="<%=mode %>" />
    <input type="hidden" name="wareType" value="<%=ParamKit.getParameter(request, "wareType", "") %>" />
    <input type="hidden" name="callbackFun" value ="<%=callbackFun %>" />
    <input name="" type="submit" class="search_btn_sub" value="筛选"/>
  </div>
</form> 
<table width="100%" border="0" cellpadding="0" class="table_style">
    <tr>
      <th>选择</th>
      <th>出库单号</th>
      <th>制品类型</th>
      <th>出库类型</th>
      <%if(mode.equals("log")){%>	
      <th>有效状态</th>
      <%} %>
    </tr>
    <%
    for(DeliveryVoucherInfoBean bean:list){%> 
    <tr>
    
    	  <td align="center">
      	  <input type="radio" name="dvId" value="<%=bean.getDvId()%>" <%=dvId == bean.getDvId() ? "checked" : ""%>>   	  
          </td>
          <td id="dvNumber_<%=bean.getDvId() %>"><%=bean.getDvNumber() %></td>
          <td><%=MapKit.getValueFromMap(bean.getWareType(), InventoryConsoleConfig.WARE_TYPE_MAP) %></td>
          <td><%=MapKit.getValueFromMap(bean.getDvType(), DeliveryVoucherConfig.DELIVERY_VOUCHER_TYPE_MAP) %></td>
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
	var dvId = $('input:radio[name=dvId]:checked').val();
	if (typeof(dvId) == 'undefined'){
		alert("请选择出库单");
		return;
	}
	var dvNumber = $("#dvNumber_"+dvId).html();
	self.parent.<%=callbackFun %>(dvId, dvNumber);
	try{ self.parent.jQuery.fancybox.close(); }catch(e){console.log(e);}
}
</script>
</html>