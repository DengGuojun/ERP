<%@page import="com.lpmas.tms.config.TmsConfig"%>
<%@page import="com.lpmas.erp.config.*"%>
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
<%@ page import="com.lpmas.constant.info.*"%>
<%@ page import="com.lpmas.erp.purchase.bean.*"%>
<%@ page import="com.lpmas.erp.purchase.config.*"%>
<%@ page import="com.lpmas.tms.transporter.business.*"%>
<%@ page import="com.lpmas.erp.purchase.util.*"  %>
<%@ include file="../../include/header.jsp"%>
<%
	AdminUserHelper adminUserHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	List<DeliveryNoteInfoBean> infoList = (List<DeliveryNoteInfoBean>)request.getAttribute("DeliveryNoteInfoList");
	PageBean PAGE_BEAN = (PageBean) request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>) request.getAttribute("CondList");
	int poId = ParamKit.getIntParameter(request, "poId", 0);
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	int wareType = ParamKit.getIntParameter(request, "wareType", 0);
	TransporterInfoMediator transporterInfoMediator = (TransporterInfoMediator) request
			.getAttribute("TransporterInfoMediator");
	PurchaseOrderInfoBean poInfoBean = (PurchaseOrderInfoBean)request.getAttribute("PurchaseOrderInfoBean");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发货单据列表</title>
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
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a> 
		<ul class="art_nav">
			<li><a href="PurchaseOrderInfoList.do?wareType=<%=poInfoBean.getWareType()%>"><%=MapKit.getValueFromMap(wareType, PurchaseOrderConfig.WARE_TYPE_MAP)%>订单列表</a>&nbsp;>&nbsp;</li>
			<li><%=poInfoBean.getPoNumber() %>&nbsp;>&nbsp;</li>
			<li>发货单据列表</li>
		</ul>
	</div>
	<%if(poId > 0 ){%>
	<div class="article_tit">
		<p class="tab">
		<a href="PurchaseOrderInfoManage.do?poId=<%=poId%>&readOnly=<%=readOnly%>&wareType=<%=wareType%>&mode=po">基本信息</a> 
		<a href="DeliveryNoteInfoList.do?poId=<%=poId%>&readOnly=<%=readOnly%>&wareType=<%=wareType%>&mode=po">收发货记录</a>
		<a href="PurchaseOrderMemoManage.do?poId=<%=poId%>&readOnly=<%=readOnly%>&wareType=<%=wareType%>&mode=po">备注信息</a>
		<a href="PurchaseOrderOperationLogList.do?poId=<%=poId%>&readOnly=<%=readOnly%>&wareType=<%=wareType%>&mode=po">操作日志</a>
		</p>
		<script>
		tabChange('.tab', 'a');
		</script>
	</div>
	<%}%>
	<form name="formSearch" method="post" action="DataLogBeanList.do">
		
	</form>
		<table width="100%" border="0"  cellpadding="0"
			class="table_style">
			<tr>
				<th>建立时间</th>
				<th>发货单号</th>
				<th>运单号</th>
				<th>配送方</th>
				<th>发货时间</th>
				<th>收货时间</th>
				<th>收货状态</th>
				<th>操作</th>
			</tr>
			<%
				for (DeliveryNoteInfoBean bean : infoList) {
			%>
			<tr>
				<td><%=DateKit.formatDate(bean.getCreateTime(), "yyyy-MM-dd HH:mm")%></td>
				<td><%=bean.getDnNumber()%></td>
				<td><%=bean.getTransportNumber()%></td>
				<td><%=transporterInfoMediator.getTransporterNameByKey(bean.getTransporterType(), bean.getTransporterId())%></td>
				<td><%=DateKit.formatTimestamp(bean.getDeliveryTime(),DateKit.DEFAULT_DATE_TIME_FORMAT)%></td>
				<td><%=DateKit.formatTimestamp(bean.getReceivingTime(),DateKit.DEFAULT_DATE_TIME_FORMAT) == null ? "" : DateKit.formatTimestamp(bean.getReceivingTime(),DateKit.DEFAULT_DATE_TIME_FORMAT) %></td>
				<td><%=PurchaseOrderConfig.RECEIVING_STATUS_MAP.get(bean.getReceivingStatus()) %></td>
				<td><a target="_Blank" href="DeliveryNoteInfoManage.do?dnId=<%=bean.getDnId()%>&poId=<%=poId%>&readOnly=<%=readOnly%>">发货明细</a> | 
				<a target="_Blank" href="ReceivingNoteInfoManage.do?dnId=<%=bean.getDnId()%>&readOnly=<%=readOnly%>">收货明细</a></td>
			</tr>
			<%
				}
			%>
		</table>
			
<ul class="page_info">
		<%if(PurchaseOrderStatusHelper.receivable(poInfoBean)&&!readOnly.equalsIgnoreCase("true")) {%>
		<li class="page_left_btn">
			<%if(adminUserHelper.hasPermission(PurchaseOrderResource.PURCHASE_ORDER_INFO, OperationConfig.UPDATE)){ %>
			<input type="button" name="new" id="new" value="新建" >
			<%} %>
		</li>
		<%} %>
</body>
<script type='text/javascript'>
 $(document).ready(function() {
	$("#new").click(
		function() {
			window.open("DeliveryNoteInfoManage.do?&readOnly="+<%=readOnly%>+"&poId="+<%=poId%>);
	}
		);
}); 
function selectPurchaseType(transporterTypeId,transporterId,transportNumber) {
	var url = "/erp/DeliveryNoteInfoList.do?transporterTypeId="+transporterTypeId+"&transporterId="+transporterId+"&transportNumber="+transportNumber+"&poId="+<%=poId%>;
	$.post(url,"",function(data){
		location.reload(true);
		//alert(data);
	});
}
</script>
</html>