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
<%@ page import="com.lpmas.erp.contract.bean.*"  %>
<%@ include file="../../include/header.jsp" %>
<%
	String callbackFun = ParamKit.getParameter(request, "callbackFun", "callbackFun");
	int contractId = (Integer)request.getAttribute("ContractId");
	List<PurchaseContractInfoEntityBean> list = (List<PurchaseContractInfoEntityBean>) request.getAttribute("PurchaseContractInfoList");
	PageBean PAGE_BEAN = (PageBean)request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>)request.getAttribute("CondList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加合同</title>
<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL %>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
<script type="text/javascript">
        document.domain='<%=DOMAIN%>'; 
</script>
</head>
<body class="article_bg">
<form name="formSearch" method="post" action="PurchaseContractInfoSelect.do">
  <div class="search_form">
    <input type="text" name="queryParam" id="queryParam" value="<%=ParamKit.getParameter(request, "queryParam", "") %>"  placeholder="请输入合同编号/合同名称" size="20"/>
    <input type="hidden" name="contractId" value="<%=contractId %>" />
    <input type="hidden" name="callbackFun" value ="<%=callbackFun %>" />
    <input name="" type="submit" class="search_btn_sub" value="筛选"/>
  </div>
</form> 
  <table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_style">
    <tr>
      <th>选择</th>
      <th>合同编号</th>
      <th>供应商</th>
      <th>合同名称</th>
    </tr>
    <%
    for(PurchaseContractInfoEntityBean bean:list){%> 
    <tr>
      <td align="center"><input type="radio" name="pcId" value="<%=bean.getPcId()%>" <%=contractId ==  bean.getPcId() ? "checked" : ""%>></td>
      <td><%=bean.getPcNumber()%></td>
      <td><%=bean.getSupplierName() %></td>
      <td id="pcName_<%=bean.getPcId()%>"><%=bean.getPcName() %></td>
    </tr>	
    <%} %>
  </table>
<ul class="page_info">
<li class="page_left_btn">
  <input type="submit" name="button" id="button" class="modifysubbtn" value="选择" onclick="callbackTo()" />
</li>
<%@ include file="../../include/page.jsp" %>
</ul>
</body>
<input type="button" class="btn_fixed" value="选择" onclick="callbackTo()" />
<script>
function callbackTo(){
	var pcId = $('input:radio[name=pcId]:checked').val();
	var pcName = $("#pcName_"+pcId).html()
	self.parent.<%=callbackFun %>(pcId, pcName);
	try{ self.parent.jQuery.fancybox.close(); }catch(e){console.log(e);}
}
</script>
</html>