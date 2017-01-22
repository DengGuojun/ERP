<%@page import="com.lpmas.admin.client.AdminServiceClient"%>
<%@page import="com.lpmas.pdm.bean.MaterialInfoBean"%>
<%@page import="com.lpmas.pdm.bean.ProductItemBean"%>
<%@page import="com.lpmas.pdm.client.PdmServiceClient"%>
<%@page import="com.lpmas.constant.info.InfoTypeConfig"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.lpmas.erp.config.*"%>
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
<%@ page import="com.lpmas.erp.report.bean.*"%>
<%@ page import="com.lpmas.erp.inventory.bean.*"%>
<%@ page import="com.lpmas.erp.report.config.*"  %>
<%@ include file="../../include/header.jsp"%>
<%
    AdminUserHelper adminUserHelper = (AdminUserHelper) request.getAttribute("AdminUserHelper");
	List<WarePlannedPriceBean> WarePlannedPriceList = (List<WarePlannedPriceBean>)request.getAttribute("WarePlannedPriceList");
	PageBean PAGE_BEAN = (PageBean) request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>) request.getAttribute("CondList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>月度计划价格列表</title>
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
	<p class="article_tit">月度计划价格列表</p>
	<form name="formSearch" method="post" action="WarePlannedPriceList.do">
		<div class="search_form">
			<em class="em1">制品编码：</em> 
			<input type="text" name="wareNumber" id="wareNumber" value="<%=ParamKit.getParameter(request, "wareNumber", "").trim()%>" /> 
			<em class="em1">计划月份：</em> 
			<input type="text" name="planMonth" id="planMonth"
				 onclick="WdatePicker({dateFmt:'yyyyMM'})"
				 value="<%=ParamKit.getParameter(request, "planMonth", "").trim()%>"
				 size="20">
			<em class="em1">制品类型：</em> 
			<select name="wareType" id="wareType">
			<%int wareType = ParamKit.getIntParameter(request, "wareType", 0); %>
				<option value="0"></option>
				<option value="<%=InfoTypeConfig.INFO_TYPE_MATERIAL %>" <%=wareType==InfoTypeConfig.INFO_TYPE_MATERIAL? "selected":"" %>>物料</option>
				<option value="<%=InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM %>" <%=wareType==InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM? "selected":"" %>>商品项</option>
			</select>
		 <input name="" type="submit" class="search_btn_sub" value="查询" />
		</div>
	</form>
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			class="table_style">
			<tr>
				<th>制品编号</th>
				<th>制品类型</th>
				<th>制品名称</th>
				<th>计划月份</th>
				<th>计量单位</th>
				<th>计划价格</th>
				<th>修改时间</th>
				<th>修改人</th>
				<th>操作</th>
			</tr>
			<%
				PdmServiceClient client = new PdmServiceClient();
				AdminServiceClient adminServiceClient = new AdminServiceClient();
				String wareNumber = "";
				String wareName = "";
				String wareTypeName = "";
				String time = "";
				String user = "";
				MaterialInfoBean materialInfoBean = null;
				ProductItemBean productItemBean = null;
				AdminUserInfoBean userInfoBean = null;
				for (WarePlannedPriceBean bean : WarePlannedPriceList) {
					if(bean.getWareType()==InfoTypeConfig.INFO_TYPE_MATERIAL){
						materialInfoBean = client.getMaterialInfoByKey(bean.getWareId());
						if(materialInfoBean!=null){
							wareNumber = materialInfoBean.getMaterialNumber();
							wareName = materialInfoBean.getMaterialName();
							wareTypeName = "物料";
						}
					}else if(bean.getWareType()==InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM){
						productItemBean = client.getProductItemByKey(bean.getWareId());
						if(productItemBean!=null){
							wareNumber = productItemBean.getItemNumber();
							wareName = productItemBean.getItemName();
							wareTypeName = "商品项";
						}
					}
					
					if(bean.getModifyTime()==null&&bean.getCreateTime()!=null){
						time = DateKit.formatTimestamp(bean.getCreateTime(), DateKit.DEFAULT_DATE_TIME_FORMAT);
						userInfoBean = adminServiceClient.getAdminUserInfoByKey(bean.getCreateUser());
						if(userInfoBean!=null) user = userInfoBean.getAdminUserName();
					}else{
						time = DateKit.formatTimestamp(bean.getModifyTime(), DateKit.DEFAULT_DATE_TIME_FORMAT);
						userInfoBean = adminServiceClient.getAdminUserInfoByKey(bean.getModifyUser());
						if(userInfoBean!=null) user = userInfoBean.getAdminUserName();
					}
			%>
			<tr>
				<td><%=wareNumber%></td>
				<td><%=wareTypeName%></td>
				<td><%=wareName%></td>
				<td><%=DateKit.formatStr(bean.getPlanMonth(), "yyyyMM", "yyyy-MM")%></td>
				<td><%=bean.getUnit() %></td>
				<td><%=bean.getPrice()%></td>
				<td><%=time%></td>
				<td><%=user%></td>
				<td>
				<%if (adminUserHelper.hasPermission(ReportResource.ERP_REPORT_INFO,OperationConfig.UPDATE)) {%>
				<a href="WarePlannedPriceManage.do?wareType=<%=bean.getWareType()%>&wareId=<%=bean.getWareId()%>&planMonth=<%=bean.getPlanMonth()%>&unit=<%=bean.getUnit()%>">修改</a>
				<%}%>
				</td>
			</tr>
			<%}%>
		</table>
			
		<ul class="page_info">
		<li class="page_left_btn">
		    <%if (adminUserHelper.hasPermission(ReportResource.ERP_REPORT_INFO,OperationConfig.CREATE)) {%>
			<input type="button" name="new" id="new" value="新建" onclick="javascript:location.href='WarePlannedPriceManage.do'">
			<%}%>
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