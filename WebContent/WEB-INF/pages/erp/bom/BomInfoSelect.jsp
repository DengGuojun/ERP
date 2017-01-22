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
<%@ page import="com.lpmas.erp.bom.bean.*"%>
<%@ page import="com.lpmas.erp.bom.config.*"%>
<%@ page import="com.lpmas.constant.info.*"%>
<%@ include file="../../include/header.jsp"%>
<%
	List<BomInfoBean> bomInfoList = (List<BomInfoBean>)request.getAttribute("BomInfoList");
	PageBean PAGE_BEAN = (PageBean) request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>) request.getAttribute("CondList");
	String callbackFun = ParamKit.getParameter(request, "callbackFun", "callbackFun");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>BOM</title>
<link href="<%=STATIC_URL%>/css/main.css" type="text/css"
	rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL%>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL%>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL%>/js/ui.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.js"></script>
<link rel="stylesheet" href="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.css" type="text/css" media="screen" />
<script type="text/javascript"
	src="<%=STATIC_URL%>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
        document.domain='<%=DOMAIN%>'; 
</script>
</head>
<body class="article_bg">
	<p class="article_tit">BOM</p>
	<form name="formSearch" method="post" action="BomInfoList.do">
		<div class="search_form">
			<em class="em1">BOM编码：</em> 
			<input type="text" name="bomNumber" id="bomNumber" value="<%=ParamKit.getParameter(request, "bomNumber", "")%>" /> 
			<em class="em1">BOM名称：</em> 
			<input type="text" name="bomName" id="bomName" value="<%=ParamKit.getParameter(request, "bomName", "")%>" /> 		
		 <input name="" type="submit" class="search_btn_sub" value="查询" />
		</div>
	</form>
		<table width="100%" border="0" cellpadding="0"
			class="table_style">
			<tr>
			    <th>选择</th>
				<th>BOM ID</th>
				<th>BOM编码</th>
				<th>BOM名称 </th>
				<th>BOM类型</th>
				<th>状态</th>
			</tr>
			<%
				for (BomInfoBean bean : bomInfoList) {
			%>
			<tr>
			    <td><input type="radio" name="radio" /></td>
				<td><%=bean.getBomId() %></td>
				<td><%=bean.getBomNumber() %></td>
				<td><%=bean.getBomName() %></td>
				<td><%=MapKit.getValueFromMap(bean.getBomType(), BomConfig.BOM_TYPE_MAP)%></td>
				<td><%=MapKit.getValueFromMap(bean.getIsActive(), BomConfig.ACTIVE_STATUS_MAP) %></td>
				 <td><input type="hidden" id="bomId" value='<%=bean.getBomId()%>' ></td>
			</tr>
			<%
				}
			%>
		</table>
			
		<ul class="page_info">
		<li class="page_left_btn">
		<input type="button" name="button" id="button" class="modifysubbtn" onclick="excuteCallBack()" value="添加" /></li>
		<%@ include file="../../include/page.jsp"%>
	</ul>
</body>
<script type='text/javascript'>
function excuteCallBack(){
	var radio = $('input:radio[name=radio]:checked');
	var itemValue = new Array();
	if(radio.length==1){
		itemValue.push(radio.parent().parent().find('#bomId').val());
		window.opener.<%=callbackFun%>(itemValue);
		self.close();
	}else{
		alert("请选择BOM!");
	}
}
</script>
</html>