<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*"  %>
<%@ page import="com.lpmas.framework.config.*"  %>
<%@ page import="com.lpmas.framework.bean.*"  %>
<%@ page import="com.lpmas.framework.page.*"  %>
<%@ page import="com.lpmas.framework.util.*"  %>
<%@ page import="com.lpmas.framework.web.*"  %>
<%@ page import="com.lpmas.admin.bean.*"  %>
<%@ page import="com.lpmas.admin.business.*"  %>
<%@ page import="com.lpmas.admin.config.*"  %>
<%@ page import="com.lpmas.erp.inventory.config.*"  %>
<%@ page import="com.lpmas.erp.inventory.bean.*"  %>
<%@ include file="../../include/header.jsp" %>
<%
	String callbackFun = ParamKit.getParameter(request, "callbackFun", "callbackFun");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>审批</title>
<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL %>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
</head>
<body class="article_bg">
	    <p>
	    		<span class="int_label">审批结果：</span>    
	      	<select name="reviewResult" id="reviewResult">
	      		<option value="<%=WarehouseTransferOrderConfig.TO_ACTION_APPROVE%>">审批通过</option>
	      		<option value="<%=WarehouseTransferOrderConfig.TO_ACTION_REJECT%>">审批不通过</option>
	      	</select>
	    </p>
	  <div class="div_center">
	  	<input type="submit" name="button" id="button" class="modifysubbtn" value="确认" onclick="callbackTo()" />
	  </div>
</body>
<script>
function callbackTo(){
	var result = $("#reviewResult").val();
	self.parent.<%=callbackFun %>(result);
	try{ self.parent.jQuery.fancybox.close(); }catch(e){console.log(e);}
}
</script>
</html>