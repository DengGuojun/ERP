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
	List<WarehouseVoucherInfoBean> list = (List<WarehouseVoucherInfoBean>)request.getAttribute("WarehouseVoucherList");
	PageBean PAGE_BEAN = (PageBean) request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>) request.getAttribute("CondList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>待上架入库单列表</title>
<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL %>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
<script type="text/javascript">
        document.domain='<%=DOMAIN%>'; 
</script>
</head>
<body class="article_bg">	
<p class="article_tit">待上架入库单列表</p>
<table width="100%" border="0" cellpadding="0" class="table_style">
    <tr>
      <th>选择</th>
      <th>入库单号</th>
      <th>操作</th>
    </tr>
    <%
    for(WarehouseVoucherInfoBean bean:list){%> 
    <tr>
    	  <td align="center"><input type="radio" name="wvId" value="<%=bean.getWvId()%>"></td>
      <td><%=bean.getWvNumber() %></td>
      <td>
      	<a target="blank" href="/erp/WarehouseVoucherInfoManage.do?wvId=<%=bean.getWvId()%>&wareType=<%=bean.getWareType()%>&readOnly=true">查看</a>
    	  </td>
    </tr>	
    <%} %>
  </table>
  <ul class="page_info">
<li class="page_left_btn">
  <input type="submit" name="button" id="button" class="modifysubbtn" value="上架" onclick="callbackTo()" />
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
	self.parent.<%=callbackFun %>(wvId);
	try{ self.parent.jQuery.fancybox.close(); }catch(e){console.log(e);}
}
</script>
</html>