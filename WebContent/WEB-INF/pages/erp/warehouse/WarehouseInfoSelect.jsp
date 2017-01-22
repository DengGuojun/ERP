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
<%@ page import="com.lpmas.erp.warehouse.config.*"  %>
<%@ page import="com.lpmas.erp.warehouse.bean.*"  %>
<%@ include file="../../include/header.jsp" %>
<%
	String selectedItem = ParamKit.getParameter(request, "selectedItem", "");
	List<String> selectedItemList = new ArrayList<String>();
	if(!selectedItem.trim().equals("")){
		selectedItemList = ListKit.string2List(selectedItem, ",");
	}
	String callbackFun = ParamKit.getParameter(request, "callbackFun", "callbackFun");
	String echoParameter = ParamKit.getParameter(request, "echoParameter", "");
	boolean isMultiple = ParamKit.getBooleanParameter(request, "isMultiple",false);
	int warehouseId = (Integer)request.getAttribute("WarehouseId");
	List<WarehouseInfoBean> list = (List<WarehouseInfoBean>) request.getAttribute("WarehouseInfoList");
	PageBean PAGE_BEAN = (PageBean)request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>)request.getAttribute("CondList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加仓库</title>
<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL %>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
<script type='text/javascript' src="/js/erp-common.js"></script>
<script type="text/javascript">
       document.domain='<%=DOMAIN%>'; 
</script>
</head>
<body class="article_bg">
<form name="formSearch" method="post" action="WarehouseInfoSelect.do">
  <div class="search_form">
    <input type="text" name="queryParam" id="queryParam" value="<%=ParamKit.getParameter(request, "queryParam", "") %>"  placeholder="请输入仓库编号/仓库名称/仓库类型" size="40"/>
    <input type="hidden" name="warehouseId" value="<%=warehouseId %>" />
    <input type="hidden" name="callbackFun" value="<%=callbackFun %>" />
    <input type="hidden" name="echoParameter" value="<%=echoParameter %>" />
    <input name="" type="submit" class="search_btn_sub" value="筛选"/>
  </div>
  <table width="100%" border="0" cellpadding="0" class="table_style">
    <tr>
      <tr>
			<%if(isMultiple){%>
			<th><input type="checkbox" id="selectAll" onchange="checkAll(this)">选择</th>
			<%}else{%>
			<th>选择</th>
			<%} %>
      <th>仓库编号</th>
      <th>仓库类型</th>
      <th>仓库名称</th>
    </tr>
    <%
    for(WarehouseInfoBean bean:list){%> 
    <tr>
      <%if(isMultiple){%>
			<%if(selectedItemList.contains((bean.getWarehouseId()+"").trim())){ %>
   			<td><input type="checkbox" name="select" checked readonly/></td>
      		<%}else{ %>
      		<td><input type="checkbox" name="select" /></td>
      		<%} %>
	  <%}else{%>
			<td align="center"><input type="radio" name="warehouseId" value="<%=bean.getWarehouseId()%>" <%=warehouseId==bean.getWarehouseId() ? "checked" : ""%>></td>
	  <%} %>
      <td><%=bean.getWarehouseNumber()%></td>
      <td><%=MapKit.getValueFromMap(bean.getWarehouseType(), WarehouseConfig.WAREHOUSE_TYPE_MAP)%></td>
      <td id="warehouseName_<%=bean.getWarehouseId()%>"><%=bean.getWarehouseName()%></td>
      <td><input type="hidden" id="warehouseJsonStr_<%=bean.getWarehouseId()%>" value='<%=JsonKit.toJson(bean)%>'></td>
    </tr>	
    <%} %>
  </table>
</form>
<ul class="page_info">
<li class="page_left_btn">
   	  <%if(isMultiple){%>
   	  <input type="submit" name="button" id="button" class="modifysubbtn" value="选择" onclick="mutilCallBackTo()" />
	  <%}else{%>
	  <input type="submit" name="button" id="button" class="modifysubbtn" value="选择" onclick="callbackTo()" />
	  <%} %>
</li>
<%@ include file="../../include/page.jsp" %>
</ul>
	<%if(isMultiple){%>
   	  <input type="button" class="btn_fixed" value="选择" onclick="mutilCallBackTo()" />
	  <%}else{%>
	  <input type="button" class="btn_fixed" value="选择" onclick="callbackTo()" />
	  <%} %>
</body>
<script>
function callbackTo(){
	var warehouseId = $('input:radio[name=warehouseId]:checked').val();
	if (typeof(warehouseId) == 'undefined'){
		alert("请选择仓库");
		return;
	}
	var warehouseJsonStr = $("#warehouseJsonStr_"+warehouseId).val();
	var echoParameter = '<%=ParamKit.getParameter(request, "echoParameter", "")%>';
	self.parent.<%=callbackFun %>(warehouseJsonStr,echoParameter);
	try{ self.parent.jQuery.fancybox.close(); }catch(e){console.log(e);}
}
function mutilCallBackTo(){
	var checkboxs = $('input[name=select]:checked');
	var itemValue = new Array();
	if(checkboxs.length>0){
		checkboxs.each(function(){
			itemValue.push($(this).parent().parent().find('#warehouseId').val());
		});
		self.parent.<%=callbackFun%>(itemValue);
		try{ self.parent.jQuery.fancybox.close(); }catch(e){console.log(e);}
	}
	else{
		alert("请选择仓库");	
	}
}
</script>
</html>