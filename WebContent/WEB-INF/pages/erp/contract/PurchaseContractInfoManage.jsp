<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.lpmas.framework.config.*"%>
<%@ page import="com.lpmas.framework.util.*"%>
<%@ page import="com.lpmas.framework.bean.StatusBean"%>
<%@ page import="com.lpmas.admin.bean.*"%>
<%@ page import="com.lpmas.admin.business.*"%>
<%@ page import="com.lpmas.framework.web.*"%>
<%@ page import="com.lpmas.constant.info.*"%>
<%@ page import="com.lpmas.erp.contract.bean.*"%>
<%@ page import="com.lpmas.srm.bean.*"%>
<%@ page import="com.lpmas.erp.contract.config.PurchaseContractConfig"%>
<%@ include file="../../include/header.jsp"%>
<%
	PurchaseContractInfoBean bean = (PurchaseContractInfoBean)request.getAttribute("PurchaseContractInfoBean");
	Map<Integer, SupplierInfoBean> supplierInfoMap = (Map<Integer, SupplierInfoBean>)request.getAttribute("SupplierInfoMap");
	List<SupplierInfoBean> supplierInfoList = (List<SupplierInfoBean>)request.getAttribute("SupplierInfoList");
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	int pcId = ParamKit.getIntParameter(request, "pcId", 0);
	request.setAttribute("readOnly", readOnly);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>合同管理</title>
<link href="<%=STATIC_URL%>/css/main.css" type="text/css"
	rel="stylesheet" />
<script type="text/javascript" src="../js/erp-common.js"></script>
<script type="text/javascript" src="<%=STATIC_URL%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=STATIC_URL%>/js/common.js"></script>
<script type="text/javascript" src="<%=STATIC_URL%>/js/ui.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.js"></script>
<link rel="stylesheet" href="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.css" type="text/css" media="screen" />
<script type="text/javascript"
	src="<%=STATIC_URL%>/js/My97DatePicker/WdatePicker.js"></script>
</head>
<body class="article_bg">
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a> 
		<ul class="art_nav">
			<li><a href="PurchaseContractInfoList.do">合同列表</a>&nbsp;>&nbsp;</li>
			<%if(bean.getPcId()==0){ %>
			<li>新建合同</li>
			<%}else{ %>
			<li>修改合同&nbsp;>&nbsp;</li>
			<li><%=bean.getPcName() %></li>
			<%} %>
		</ul>
	</div> 
	<form id="formData" name="formData" method="post" action="PurchaseContractInfoManage.do?pcId=<%=pcId%>" onsubmit="javascript:return checkForm('formData');">
	  <div class="modify_form">
	  <p>
  			<em class="int_label">合同名称：</em>
  			<input type="text"  name="pcName" id="pcName" value="<%=bean.getPcName()%>"/>
  		</p>
  		<p>
  			<em class="int_label">合同编号：</em>
  			<input type="text"  name="pcNumber" id="pcNumber" value="<%=bean.getPcNumber()%>"/>
  		</p>
  		<p>
  			<em class="int_label">合同类型：</em>
  			<select name="contractType" id="contractType">
  			<%for(StatusBean<Integer, String> statusBean : PurchaseContractConfig.PURCHASE_CONTRACT_TYPE_LIST){ %>
  			<option value="<%=statusBean.getStatus() %>" <%=(statusBean.getStatus()==(bean.getContractType()))?"selected":"" %>><%=statusBean.getValue() %></option>
  			<%} %>
			</select>
  		</p>
  		<p>
  			<em class="int_label">供应商：</em>
  			<select name="supplierId" id="supplierId">
				<%
					for (SupplierInfoBean SupplierBean :supplierInfoList) {
				%>
				<option value="<%=SupplierBean.getSupplierId()%>"
					<%=(SupplierBean.getSupplierId()==bean.getSupplierId()) ? "selected" : ""%>><%=SupplierBean.getSupplierName()%></option>
				<%
					}
				%>
				</select>
  		</p>
  		<p>
  			<em class="int_label">状态：</em>
  			<input type="checkbox" name="status" id="status" value="<%=Constants.STATUS_VALID %>" <%=(bean.getStatus()==Constants.STATUS_VALID)?"checked":"" %>/>
  		</p>
		</div>
	   <div class="div_center">
	  <input type="submit" name="submit" id="submit" class="modifysubbtn" value="提交" />
	  <a href="PurchaseContractInfoList.do"><input type="button" name="cancel" id="cancel" value="取消" ></a>
	  </div>
	</form>
</body>
<script>
$(document).ready(function() {
	var readOnly = '${readOnly}';
	if(readOnly=='true') {
		disablePageElement();
	}
});
</script>
</html>