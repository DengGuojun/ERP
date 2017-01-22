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
<%@ page import="com.lpmas.constant.info.*"  %>
<%@ include file="../../include/header.jsp" %>
<%
    String callbackFun = ParamKit.getParameter(request, "callbackFun", "callbackFun");
	int statusAction = (Integer)request.getAttribute("statusAction");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>库存操作</title>
<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL %>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
<script type="text/javascript">
        document.domain='<%=DOMAIN%>'; 
</script>
</head>
<body class="article_bg">	
<p class="article_tit"><%=WarehouseInventoryConfig.WAREHOUSE_INVENTORY_OPERATION_TYPE_MAP.get(statusAction) %></p>
<input type="text" name="quantity" maxlength="10" id="quantity" value="" onkeyup='this.value=this.value.replace(/[^\-?\d.]/g,"")'  placeholder="请输入<%=WarehouseInventoryConfig.WAREHOUSE_INVENTORY_OPERATION_TYPE_MAP.get(statusAction)  %>数量"/>
<input type="submit" name="button" id="button" class="modifysubbtn" value="确定" onclick="callbackTo()" />
</body>
<script>
function callbackTo(){
	var quantity = parseFloat($("#quantity").val()); 
	if (isNaN(quantity)){
		alert('请输入<%=WarehouseInventoryConfig.WAREHOUSE_INVENTORY_OPERATION_TYPE_MAP.get(statusAction)%>数量');
		return;
	}
	self.parent.<%=callbackFun %>(<%=statusAction%>,quantity);
	try{ self.parent.jQuery.fancybox.close(); }catch(e){console.log(e);}
}
</script>
</html>