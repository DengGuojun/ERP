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
<%@ page import="com.lpmas.erp.purchase.config.*"  %>
<%@ page import="com.lpmas.erp.purchase.bean.*"  %>
<%@ include file="../../include/header.jsp" %>
<%
	String callbackFun = ParamKit.getParameter(request, "callbackFun", "callbackFun");
	int poId = (Integer)request.getAttribute("PoId");
	List<PurchaseOrderInfoEntityBean> list = (List<PurchaseOrderInfoEntityBean>) request.getAttribute("PurchaseOrderInfoEntityList");
	PageBean PAGE_BEAN = (PageBean)request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>)request.getAttribute("CondList");
	String mode = ParamKit.getParameter(request, "mode", "general");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择采购订单</title>
<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL %>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
<script type="text/javascript">
        document.domain='<%=DOMAIN%>'; 
</script>
</head>
<body class="article_bg">
<form name="formSearch" method="post" action="PurchaseOrderInfoSelect.do">
  <div class="search_form">
    <input type="text" name="poNumber" id="poNumber" value="<%=ParamKit.getParameter(request, "poNumber", "") %>"  placeholder="请输入采购订单编号" size="20"/>
    <input type="hidden" name="poId" value="<%=poId %>" />
    <input type="hidden" name="mode" value="<%=mode %>" />
    <input type="hidden" name="wareType" value="<%=ParamKit.getParameter(request, "wareType", "") %>" />
    <input type="hidden" name="callbackFun" value ="<%=callbackFun %>" />
    <input name="" type="submit" class="search_btn_sub" value="筛选"/>
  </div>
</form> 
  <table width="100%" border="0"cellpadding="0" class="table_style">
    <tr>
      <th>选择</th>
      <th>采购订单编号</th>
      <th>供应商</th>
      <%if(mode.equals("log")){%>	
      <th>有效状态</th>
      <%}else{ %>
      <th>操作</th>
      <%} %>
    </tr>
    <%
    for(PurchaseOrderInfoEntityBean bean:list){%> 
    <tr>
      <td align="center">
      	<input type="radio" name="poId" value="<%=bean.getPoId()%>" <%=poId == bean.getPoId() ? "checked" : ""%>>
      	<input type="hidden" id="supplierId_<%=bean.getPoId() %>"  value="<%=bean.getSupplierId()%>" />
      </td>
      <td id="poNumber_<%=bean.getPoId() %>"><%=bean.getPoNumber()%></td>
      <td id="supplierName_<%=bean.getPoId() %>"><%=bean.getSupplier()%></td>
       <%if(mode.equals("log")){%>	
       <td><%=Constants.STATUS_MAP.get(bean.getStatus())%></td>
       <%}else{ %>
       <td align="center"><a href="/erp/PurchaseOrderInfoManage.do?poId=<%=bean.getPoId()%>&readOnly=true&wareType=<%=bean.getWareType()%>" target="_Blank">查看</a></td>
       <%} %>
    </tr>	
    <%} %>
  </table>
<ul class="page_info">
<li class="page_left_btn">
  <input type="submit" name="button" id="button" class="modifysubbtn" value="选择" onclick="callbackTo()" />
</li>
<%@ include file="../../include/page.jsp" %>
</ul>
<input type="button"  class="btn_fixed" value="选择" onclick="callbackTo()" />
</body>
<script>
function callbackTo(){
	var poId = $('input:radio[name=poId]:checked').val();
	var poNumber = $("#poNumber_"+poId).html();
	var supplierId = $("#supplierId_"+poId).val();
	var supplierName = $("#supplierName_"+poId).html();
	self.parent.<%=callbackFun %>(poId, poNumber, supplierId, supplierName);
	try{ self.parent.jQuery.fancybox.close(); }catch(e){console.log(e);}
}
</script>
</html>