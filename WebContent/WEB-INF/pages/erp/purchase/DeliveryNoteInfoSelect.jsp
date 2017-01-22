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
<%@ include file="../../include/header.jsp"%>
<%
    String callbackFun = ParamKit.getParameter(request, "callbackFun", "callbackFun");
	List<DeliveryNoteInfoBean> list = (List<DeliveryNoteInfoBean>)request.getAttribute("DeliveryNoteInfoList");
    Map<Integer, PurchaseOrderInfoBean> purchaseOrderInfoMap = (Map<Integer, PurchaseOrderInfoBean>)request.getAttribute("PurchaseOrderInfoMap");
	PageBean PAGE_BEAN = (PageBean) request.getAttribute("PageResult");
	List<String[]> COND_LIST = (List<String[]>) request.getAttribute("CondList");
	int dnId = (Integer)request.getAttribute("dnId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发货单选择</title>
<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
<script type='text/javascript' src="<%=STATIC_URL %>/js/jquery.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/common.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/ui.js"></script>
<script type="text/javascript">
        document.domain='<%=DOMAIN%>'; 
</script>
</head>
<body class="article_bg">
<form name="formSearch" method="post" action="DeliveryNoteInfoSelect.do">
 <div class="search_form">
    <input type="text" name="dnNumber" id="dnNumber" value="<%=ParamKit.getParameter(request, "dnNumber", "") %>"  placeholder="请输入发货单号" size="70"/>
    <input type="hidden" name="dnId" value="<%=dnId %>" />
    <input type="hidden" name="callbackFun" value="<%=callbackFun %>" />
    <input name="" type="submit" class="search_btn_sub" value="筛选"/>
  </div>
  <table width="100%" border="0" cellpadding="0" class="table_style">
    <tr>
      <th>选择</th>
      <th>发货单号</th>
      <th>采购类型</th>
      <th>发货时间</th>
    </tr>
    <%
    for(DeliveryNoteInfoBean bean:list){%> 
    <tr>
      <td align="center"><input type="radio" name="dnId" value="<%=bean.getDnId()%>" <%=dnId==bean.getDnId() ? "checked" : ""%>></td>
      <td><%=bean.getDnNumber()%></td>
      <td><%=MapKit.getValueFromMap(purchaseOrderInfoMap.get(bean.getPoId()).getPurchaseType(), PurchaseOrderConfig.PURCHASE_TYPE_MAP) %></td>
      <td><%=DateKit.formatTimestamp(bean.getDeliveryTime(),DateKit.DEFAULT_DATE_TIME_FORMAT)%></td>
    </tr>	
    <input type="hidden" id="deliveryNoteJsonStr_<%=bean.getDnId()%>" value='<%=JsonKit.toJson(bean)%>'>
    <input type="hidden" id="purchaseOrderJsonStr_<%=bean.getDnId()%>" value='<%=JsonKit.toJson(purchaseOrderInfoMap.get(bean.getPoId()))%>'>
    <%} %>
  </table>
</form>
<ul class="page_info">
<li class="page_left_btn">
  <input type="submit" name="button" id="button" class="modifysubbtn" value="选择" onclick="callbackTo()" />
</li>
<%@ include file="../../include/page.jsp" %>
</ul>
<input type="button" class="btn_fixed" value="选择" onclick="callbackTo()" />
</body>
<script>
function callbackTo(){
	var dnId = $('input:radio[name=dnId]:checked').val();
	if (typeof(dnId) == 'undefined'){
		alert("请选择出货单");
		return;
	}
	var deliveryNoteJsonStr = $("#deliveryNoteJsonStr_"+dnId).val();
	var purchaseOrderJsonStr = $("#purchaseOrderJsonStr_"+dnId).val();
	
	self.parent.<%=callbackFun %>(deliveryNoteJsonStr, purchaseOrderJsonStr);
	try{ self.parent.jQuery.fancybox.close(); }catch(e){console.log(e);}
}
</script>
</html>