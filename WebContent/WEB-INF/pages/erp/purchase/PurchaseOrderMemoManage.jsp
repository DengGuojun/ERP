<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"  %>
<%@ page import="com.lpmas.framework.config.*"  %>
<%@ page import="com.lpmas.admin.bean.*"  %>
<%@ page import="com.lpmas.admin.business.*"  %>
<%@ page import="com.lpmas.erp.purchase.bean.*"  %>
<%@page import="com.lpmas.framework.util.*"%>
<%@page import="com.lpmas.framework.web.*"%>
<%@ page import="com.lpmas.erp.purchase.config.*"%>
<%@ page import="com.lpmas.framework.page.*"  %>
<%@ include file="../../include/header.jsp" %>
<% 
	List<PurchaseOrderMemoBean> list = (List<PurchaseOrderMemoBean>)request.getAttribute("MemoList");
	Map<Integer, AdminUserInfoBean> adminUserInfoMap = (Map<Integer, AdminUserInfoBean>)request.getAttribute("AdminUserInfoMap");
	PurchaseOrderInfoBean infoBean = (PurchaseOrderInfoBean)request.getAttribute("PurchaseOrderInfoBean");
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	request.setAttribute("readOnly", readOnly);
	int wareType = ParamKit.getIntParameter(request, "wareType", 0);
	PageBean PAGE_BEAN = (PageBean)request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>)request.getAttribute("CondList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>备注信息管理</title>
	<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="../js/erp-common.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/common.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/ui.js"></script>
</head>
<body class="article_bg">
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a> 
		<ul class="art_nav">
			<li><a href="PurchaseOrderInfoList.do?wareType=<%=infoBean.getWareType()%>"><%=MapKit.getValueFromMap(wareType, PurchaseOrderConfig.WARE_TYPE_MAP)%>订单列表</a>&nbsp;>&nbsp;</li>
			<li><%=infoBean.getPoNumber() %>&nbsp;>&nbsp;</li>
			<li>修改采购订单备注</li>
		</ul>
	</div>
	<div class="article_tit">
		<p class="tab">
		<a href="PurchaseOrderInfoManage.do?poId=<%=infoBean.getPoId()%>&readOnly=<%=readOnly%>&wareType=<%=infoBean.getWareType()%>&mode=po">基本信息</a> 
		<a href="DeliveryNoteInfoList.do?poId=<%=infoBean.getPoId()%>&readOnly=<%=readOnly%>&wareType=<%=infoBean.getWareType()%>&mode=po">收发货记录</a>
		<a href="PurchaseOrderMemoManage.do?poId=<%=infoBean.getPoId()%>&readOnly=<%=readOnly%>&wareType=<%=infoBean.getWareType()%>&mode=po">备注信息</a>
		<a href="PurchaseOrderOperationLogList.do?poId=<%=infoBean.getPoId()%>&readOnly=<%=readOnly%>&wareType=<%=wareType%>&mode=po">操作日志</a>
		</p>
		<script>
		tabChange('.tab', 'a');
		</script>
	</div>
	<table width="100%" border="0" cellpadding="0" class="table_style">
	    <tr>
	      <th width="20%">创建日期</th>
	      <th width="20%">创建人</th>
	      <th width="60%">内容</th>
	    </tr>
	    <%
	    for(PurchaseOrderMemoBean bean:list){%> 
	    <tr>
	      <td><%=bean.getCreateTime()%></td>
	      <td><%=adminUserInfoMap.get(bean.getCreateUser())%></td>
	      <td class="memo_content"><%=bean.getMemoContent()%></td>
	    </tr>	
	    <%} %>
  	</table>
  	<%@ include file="../../include/page.jsp" %>
  	
  	<%if(readOnly.equals("false")) {%>
	<form id="formData" name="formData" method="post" action="PurchaseOrderMemoManage.do" onsubmit="javascript:return checkForm('formData');">
	  <input type="hidden" name="poId" id="poId" value="<%=infoBean.getPoId() %>"/>
	  <div class="modify_form">
	  	<p>添加备注信息:</p>
	    <p>
	      <textarea name="memoContent" id="memoContent" checkStr="备注信息;text;true;;200" cols=60 rows=5 ></textarea><em><span>*</span></em>
	    </p>
	  </div>
	  <div class="div_center">
	  <input type="submit" name="submit" id="submit" class="modifysubbtn" value="提交" />
	  </div>
	</form>
	<%} %>
</body>
<script>
$(document).ready(function() {
	var readOnly = '${readOnly}';
	if(readOnly=='true') {
		disablePageElement();
	}
	$('td.memo_content').each(function(){
		$(this).html($(this).html().replace(/\n/g,'<br/>'));
	})
});
</script>
</html>