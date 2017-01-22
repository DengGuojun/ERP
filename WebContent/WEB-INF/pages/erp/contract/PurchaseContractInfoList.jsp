<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.lpmas.erp.contract.config.PurchaseContractConfig"%>
<%@page import="com.lpmas.admin.client.cache.AdminUserInfoClientCache"%>
<%@page import="com.lpmas.system.bean.SysApplicationInfoBean"%>
<%@page import="com.lpmas.log.bean.DataLogBean"%>
<%@ page import="java.util.*"%>
<%@ page import="com.lpmas.framework.config.*"%>
<%@ page import="com.lpmas.framework.bean.*"%>
<%@ page import="com.lpmas.framework.page.*"%>
<%@ page import="com.lpmas.framework.util.*"%>
<%@ page import="com.lpmas.framework.web.*"%>
<%@ page import="com.lpmas.admin.bean.*"%>
<%@ page import="com.lpmas.admin.business.*"%>
<%@ page import="com.lpmas.admin.config.*"%>
<%@ page import="com.lpmas.erp.contract.bean.*"%>
<%@ page import="com.lpmas.srm.bean.*"%>
<%@ page import="com.lpmas.constant.info.*"%>
<%@ include file="../../include/header.jsp"%>
<%
	List<PurchaseContractInfoBean> pcList = (List<PurchaseContractInfoBean>)request.getAttribute("PurchaseContractInfoList");
	Map<Integer, SupplierInfoBean> supplierInfoMap = (Map<Integer, SupplierInfoBean>)request.getAttribute("SupplierInfoMap");
	PageBean PAGE_BEAN = (PageBean) request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>) request.getAttribute("CondList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>采购合同列表</title>
<link href="<%=STATIC_URL%>/css/main.css" type="text/css"
	rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL%>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL%>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL%>/js/ui.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.js"></script>
<link rel="stylesheet" href="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.css" type="text/css" media="screen" />
<script type="text/javascript"
	src="<%=STATIC_URL%>/js/My97DatePicker/WdatePicker.js"></script>
</head>

<body class="article_bg">
	<p class="article_tit">采购合同列表</p>
	<form name="formSearch" method="post" action="PurchaseContractInfoList.do">
		<div class="search_form">
			<em class="em1">合同名称：</em> 
			<input type="text" name="pcName" id="pcName" value="<%=ParamKit.getParameter(request, "pcName", "")%>" /> 
			
			<em class="em1">有效状态：</em> <select name="status"
				id="status">
				<%
					int status = ParamKit.getIntParameter(request, "status", Constants.STATUS_VALID);
					for (StatusBean<Integer, String> statusBean : Constants.STATUS_LIST) {
				%>
				<option value="<%=statusBean.getStatus()%>"
					<%=(statusBean.getStatus() == status) ? "selected" : ""%>><%=statusBean.getValue()%></option>
				<%
					}
				%>
			</select>
		 <input name="" type="submit" class="search_btn_sub" value="查询" />
		</div>
	</form>
		<table width="100%" border="0" cellpadding="0"
			class="table_style">
			<tr>
				<th>合同ID</th>
				<th>合同名称</th>
				<th>合同编号</th>
				<th>合同类型</th>
				<th>供应商</th>
				<th>操作</th>
			</tr>
			<%
				for (PurchaseContractInfoBean bean : pcList) {
			%>
			<tr>
				<td><%=bean.getPcId()%></td>
				<td><%=bean.getPcName()%></td>
				<td><%=bean.getPcNumber()%></td>
				<td><%=PurchaseContractConfig.PURCHASE_CONTRACT_TYPE_MAP.get(bean.getContractType())%></td>
				<td><%=supplierInfoMap.get(bean.getSupplierId()) != null ? supplierInfoMap.get(bean.getSupplierId()).getSupplierName() : ""%></td>
				<td><a href="PurchaseContractInfoManage.do?pcId=<%=bean.getPcId()%>&readOnly=true">查看</a>&nbsp;|
				&nbsp;<a href="PurchaseContractInfoManage.do?pcId=<%=bean.getPcId()%>&readOnly=false">修改</a></td>
			</tr>
			<%
				}
			%>
		</table>
			
		<ul class="page_info">
		<li class="page_left_btn">
			<input type="button" name="new" id="new" value="新建" onclick="javascript:location.href='PurchaseContractInfoManage.do'">
		</li>
		<%@ include file="../../include/page.jsp"%>
	</ul>
</body>
<script type='text/javascript'>
var data;
$(document).ready(function() {
	var showFilter="${param.showFilter}";
	if(showFilter=="hide"){
		$('.search_form').hide();
	}
});
</script>
</html>