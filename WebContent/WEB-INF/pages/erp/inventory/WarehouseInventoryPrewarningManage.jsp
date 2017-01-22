<%@page import="com.lpmas.framework.util.ListKit"%>
<%@page import="com.lpmas.framework.util.JsonKit"%>
<%@page import="com.lpmas.constant.info.InfoTypeConfig"%>
<%@page import="com.lpmas.framework.web.ParamKit"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"  %>
<%@ page import="com.lpmas.framework.config.*"  %>
<%@ page import="com.lpmas.framework.bean.StatusBean" %>
<%@ page import="com.lpmas.admin.bean.*"  %>
<%@ page import="com.lpmas.admin.business.*"  %>
<%@ page import="com.lpmas.erp.inventory.config.*"  %>
<%@ page import="com.lpmas.erp.inventory.bean.*"  %>
<%@ include file="../../include/header.jsp" %>
<% 
	WarehouseInventoryPrewarningBean bean = (WarehouseInventoryPrewarningBean)request.getAttribute("PrewarningBean");
	String wareName = (String)request.getAttribute("WareName");
	AdminUserHelper adminHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	boolean isCreateAction = (bean.getWareId() == 0 || bean.getPrewarningType() == 0 || bean.getWareType() == 0);
	request.setAttribute("readOnly", readOnly);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>预警管理</title>
	<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="../js/erp-common.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/common.js"></script>
	<script type='text/javascript' src="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.js"></script>
	<link rel="stylesheet" href="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.css" type="text/css" media="screen" />
</head>
<script type="text/javascript">
document.domain='<%=DOMAIN%>';
</script>
<body class="article_bg">
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a> 
		<ul class="art_nav">
			<li><a href="WarehouseInventoryPrewarningList.do">预警列表</a>&nbsp;>&nbsp;</li>
			<% if(isCreateAction) {%>
			<li>新建预警信息</li>
			<%}else{ %>
			<li><%=wareName %>&nbsp;>&nbsp;</li>
			<li>修改预警信息</li>
			<%}%>
		</ul>
	</div>
	<form id="formData" name="formData" method="post" action="WarehouseInventoryPrewarningManage.do" onsubmit="javascript:return checkForm('formData');">
	  <%if(isCreateAction){ %>
	  <input type="hidden" name="isCreateAction" id="isCreateAction" value="true"/>
	  <%}else{ %>
	  <input type="hidden" name="isCreateAction" id="isCreateAction" value="false"/>
	  <%} %>
	  <div class="modify_form">
	  	<p>
	  	 <%if(isCreateAction){ %>
	  	 <em class="int_label"><span>*</span>制品类型：</em>
	  		<select name="wareType"
				id="wareType">
				<option></option>
				<%
					int wareType = bean.getWareType();
					for (StatusBean<Integer, String> statusBean : WarehouseInventoryPrewarningConfig.WARE_TYPE_LIST) {
				%>
				<option value="<%=statusBean.getStatus()%>"
					<%=(statusBean.getStatus() == wareType) ? "selected" : ""%>><%=statusBean.getValue()%></option>
				<%
					}
				%>
			</select>
	  	 <%}else{ %>
	  	 <em class="int_label">制品类型：</em>
	  	 <% int wareType =ParamKit.getIntParameter(request, "wareType", 0);
	  	 	String wareTypeName = WarehouseInventoryPrewarningConfig.WARE_TYPE_MAP.get(wareType);%>
	  	 <input type="hidden" name="wareType" value="<%=wareType%>">
	  	 <em><%=wareTypeName %></em>
	  	 <%} %>
	  		
	  	</p>
	    <p>
	    <%if(isCreateAction){ %>
	    <em class="int_label"><span>*</span>制品选择：</em>
	      <input type="hidden" name="wareId" id="wareId" value="<%=bean.getWareId() %>"/>
	      <input type="text"  name="wareName" id="wareName" size="30" maxlength="100" value="<%=wareName%>"/>
	      <input type="button" id="wareSelection" value="选择">
	    <%}else{ %>
	    <em class="int_label">制品选择：</em>
	    <input type="hidden" name="wareId" id="wareId" value="<%=bean.getWareId() %>"/>
	    <em><%=wareName %></em>
	    <%} %>
	      
	    </p>
	    <p>
	    <%if(isCreateAction){ %>
	    <em class="int_label"><span>*</span>预警类型：</em>    
	      	<select name="prewarningType"
				id="prewarningType" onchange="prewarningTypeOnChange($('#prewarningType').val())">
				<%
					int prewarningType = bean.getPrewarningType();
					for (StatusBean<Integer, String> statusBean : WarehouseInventoryPrewarningConfig.PREWARNING_TYPE_LIST) {
				%>
				<option value="<%=statusBean.getStatus()%>"
					<%=(statusBean.getStatus() == prewarningType) ? "selected" : ""%>><%=statusBean.getValue()%></option>
				<%
					}
				%>
			</select>
	    <%}else{ %>
	    <em class="int_label">预警类型：</em>  
	    <input type="hidden" name="prewarningType" id="prewarningType" value="<%=bean.getPrewarningType() %>"/>
	    <em><%=WarehouseInventoryPrewarningConfig.PREWARNING_TYPE_MAP.get(bean.getPrewarningType()) %></em>
	    <%} %>
	      
	    </p>
	    <p id="prewarning">
	      <em class="int_label"><span>*</span>预警内容：</em>
	      <%if(isCreateAction){ %>
	      <%}else{ %>
	      		<%List<WarehouseInventoryPrewarningContentBean> contentList = JsonKit.toList(bean.getPrewarningContent(), WarehouseInventoryPrewarningContentBean.class);
	      		Map<String,String> contentMap = ListKit.list2Map(contentList, "key","value");
	      		if(bean.getPrewarningType()==WarehouseInventoryPrewarningConfig.PREWARNING_TYPE_EXPIRATION){ %>
	      		<label>保质期预警值:</label><input onkeyup='this.value=this.value.replace(/[^\-?\d.]/g,"")' name="guaranteePeriod" id="guaranteePeriod" value="<%=contentMap.get(WarehouseInventoryPrewarningConfig.GUARANTEE_PERIOD) %>" checkStr="保质期预警值;num;true;;20">
	      		<%}else{ %>
	      		<label>最大库存警值:</label><input onkeyup='this.value=this.value.replace(/[^\-?\d.]/g,"")' name="maxInventory" id="maxInventory" value="<%=contentMap.get(WarehouseInventoryPrewarningConfig.MAX_INVENTORY) %>" checkStr="最大库存警值;num;true;;20">&nbsp;&nbsp;
	      		<label>最小库存警值:</label><input onkeyup='this.value=this.value.replace(/[^\-?\d.]/g,"")' name="minInventory" id="minInventory" value="<%=contentMap.get(WarehouseInventoryPrewarningConfig.MIN_INVENTORY) %>" checkStr="最小库存警值;num;true;;20">
	      		<%} %>
	      <%} %>
	    </p>
	    <p>
	      <em class="int_label">是否自动修改：</em>
	      <input type="checkbox" name="isAutoModify" id="isAutoModify" value="<%=Constants.STATUS_VALID %>" <%=(bean.getIsAutoModify()==Constants.STATUS_VALID)?"checked":"" %>/>
	    </p>
	    <p>
	      <em class="int_label">有效状态：</em>
	      <input type="checkbox" name="status" id="status" value="<%=Constants.STATUS_VALID %>" <%=(bean.getStatus()==Constants.STATUS_VALID)?"checked":"" %>/>
	    </p>
	    <p class="p_top_border">
	      <em class="int_label">备注：</em>
	      <textarea  name="memo" id="memo" cols="60" rows="3" checkStr="备注;txt;false;;1000"><%=bean.getMemo() %></textarea>
	    </p>
	  </div>
	  <div class="div_center">
	  <input type="submit" name="submit" id="submit" class="modifysubbtn" value="提交" />
	  <input type="button" name="cancel" id="cancel" value="取消" onclick="javascript:history.back()">
	  </div>
	</form>
</body>
<script>
$(document).ready(function() {
	var readOnly = '${readOnly}';
	if(readOnly=='true') {
		disablePageElement();
	}
	 $("#prewarningType").trigger('change');
	
	$("#wareSelection").click(
			function() {				
				var url = "";
				var wareType = $('#wareType').val().trim();
				if(wareType==""){
					alert("请先选择制品类型");
					return;
				}else{
					if(wareType=='<%=InfoTypeConfig.INFO_TYPE_MATERIAL%>'){
						url = "/pdm/MaterialInfoSelect.do";
					}else{
						url = "/pdm/ProductItemSelect.do";
					}
				}
				$.fancybox.open({
					href : '<%=PDM_URL%>'+url+'?callbackFun=selectWareInfo&openOrPop=pop&isMultiple=false',
					type : 'iframe',
					width : 560,
					minHeight : 150
			});
		});
});
function selectWareInfo(selectWareInfo){
	console.log(selectWareInfo);
	var itemContent = jQuery.parseJSON(selectWareInfo[0]);
	var wareId = $('#wareId');
	var wareName = $('#wareName');
	
	wareId.val(itemContent.itemId);
	wareName.val(itemContent.itemName);
	
}
function prewarningTypeOnChange(prewarningType){
	var prewarning = $('#prewarning');
	prewarning.find('input').remove();
	prewarning.find('label').remove();
	if(prewarningType=='<%=WarehouseInventoryPrewarningConfig.PREWARNING_TYPE_EXPIRATION%>'){
		//保质期预警
		prewarning.append($('<label>保质期预警值:</label><input onkeyup="this.value=this.value.replace(/[^\\-?\\d.]/g,&quot&quot)" name="guaranteePeriod" id="guaranteePeriod" value="" checkStr="保质期预警值;txt;true;;20">'));
	}else if(prewarningType=='<%=WarehouseInventoryPrewarningConfig.PREWARNING_TYPE_INVENTORY%>'){
		//库存预警
		prewarning.append($('<label>最大库存警值:</label><input onkeyup="this.value=this.value.replace(/[^\\-?\\d.]/g,&quot&quot)" name="maxInventory" id="maxInventory" value="" checkStr="最大库存警值;txt;true;;20">&nbsp;&nbsp;'));
		prewarning.append($('<label>最小库存警值:</label><input onkeyup="this.value=this.value.replace(/[^\\-?\\d.]/g,&quot&quot)" name="minInventory" id="minInventory" value="" checkStr="最小库存警值;txt;true;;20">'));
	}
}
</script>
</html>