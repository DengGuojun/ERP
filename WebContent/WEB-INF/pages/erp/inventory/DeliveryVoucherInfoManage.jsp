<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.lpmas.constant.info.InfoTypeConfig"%>
<%@page import="com.lpmas.erp.config.*"%>
<%@page import="com.lpmas.framework.web.ParamKit"%>
<%@ page import="java.util.*"  %>
<%@ page import="com.lpmas.framework.config.*"  %>
<%@ page import="com.lpmas.framework.util.*"  %>
<%@ page import="com.lpmas.framework.bean.StatusBean" %>
<%@ page import="com.lpmas.admin.bean.*"  %>
<%@ page import="com.lpmas.admin.business.*"  %>
<%@page import="com.lpmas.admin.client.cache.AdminUserInfoClientCache"%>
<%@ page import="com.lpmas.erp.inventory.bean.*"  %>
<%@ page import="com.lpmas.erp.inventory.config.*"  %>
<%@ page import="com.lpmas.erp.warehouse.bean.*"  %>
<%@ include file="../../include/header.jsp" %>
<% 
    DeliveryVoucherInfoBean bean = (DeliveryVoucherInfoBean)request.getAttribute("DeliveryVoucherInfoBean");
	AdminUserHelper adminUserHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	AdminUserInfoClientCache adminCache = new AdminUserInfoClientCache();
	int dvId = bean.getDvId();
	int dvType = bean.getDvType();
	int wareType = ParamKit.getIntParameter(request, "wareType", 0);
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	boolean isReadOnly = readOnly.equals("true")? true:false;
	request.setAttribute("readOnly", readOnly);
	WarehouseInfoBean warehouseInfoBean = (WarehouseInfoBean)request.getAttribute("warehouseInfoBean");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>出库管理</title>
	<link href="<%=STATIC_URL %>/css/main.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="<%=STATIC_URL %>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=STATIC_URL %>/js/common.js"></script>
	<script type='text/javascript' src="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.js"></script>
	<link rel="stylesheet" href="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.css" type="text/css" media="screen" />
	<script type="text/javascript" src="../js/erp-common.js"></script>
	<script type="text/javascript" src="../js/rows-jq.js"></script>
	<script type='text/javascript' src="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.js"></script>
	<script type="text/javascript"
	src="<%=STATIC_URL%>/js/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript">
        document.domain='<%=DOMAIN%>'; 
	</script>
</head>
<body class="article_bg" style="overflow-y:scroll">
<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a> 
		<ul class="art_nav">
			<li><a href="DeliveryVoucherInfoList.do?wareType=<%=bean.getWareType()%>&dvType=<%=bean.getDvType()%>"><%=MapKit.getValueFromMap(wareType, InventoryConsoleConfig.WARE_TYPE_MAP)%><%=MapKit.getValueFromMap(dvType, DeliveryVoucherConfig.DELIVERY_VOUCHER_TYPE_MAP)%>单列表</a>&nbsp;>&nbsp;</li>
			<% if(dvId > 0) {%>
			<li><%=bean.getDvNumber() %>&nbsp;>&nbsp;</li>
			<li>修改出库单</li>
			<%}else{ %>
			<li>新建出库单</li>
			<%}%>
		</ul>
	</div>
	<% if(dvId > 0) {%>
	<div class="article_tit">
		<p class="tab">
		<a href="DeliveryVoucherInfoManage.do?dvId=<%=dvId%>&readOnly=<%=readOnly%>&dvType=<%=dvType%>&wareType=<%=wareType%>&mode=po">基础信息</a> 
		<a href="DeliveryVoucherOperationLogList.do?dvId=<%=dvId%>&readOnly=<%=readOnly%>&dvType=<%=dvType%>&wareType=<%=wareType%>&mode=po">出库单日志</a>
		</p>
		<script>
		tabChange('.tab', 'a');
		</script>
	</div>
	<%}%>
    <form style="width: 100%" id="formData" name="formData" method="post" action="DeliveryVoucherInfoManage.do" onsubmit="javascript:return formOnSubmit();">
	  <input type="hidden" name="dvId" id="dvId" value="<%=bean.getDvId() %>"/>
	  <input type="hidden" name="dvNumber" id="dvNumber" value="<%=bean.getDvNumber() %>"/>
	  <input type="hidden" name="wareType" id="wareType" value="<%=bean.getWareType() %>"/>
	  <input type="hidden" name="dvType" id="dvType" value="<%=bean.getDvType() %>"/>
	  <input type="hidden" name="approveStatus" id="approveStatus" value="<%=bean.getApproveStatus() %>"/>
	  <input type="hidden" name="syncStatus" id="syncStatus" value="<%=bean.getSyncStatus() %>"/>
	  <input type="hidden" name="dvStatus" id="dvStatus" value="<%=bean.getDvStatus() %>"/>
	  <input type="hidden" name="status" id="status" value="<%=bean.getStatus() %>"/>
	  <input type="hidden" name="sourceOrderType" id="sourceOrderType" value="<%=bean.getSourceOrderType() %>"/>
	  <input type="hidden" name="sourceOrderId" id="sourceOrderId" value="<%=bean.getSourceOrderId() %>"/>
	  <p>基础信息</p>
	  <div class="modify_form">
	  	<p>
	  		<em class="int_label">出库货品类型：</em>
	  		<em><%=MapKit.getValueFromMap(bean.getWareType(), InventoryConsoleConfig.WARE_TYPE_MAP)%></em>
	  	</p>
	  	<p>
	  		<em class="int_label">出库类型：</em>
	  		<em><%=MapKit.getValueFromMap(bean.getDvType(), DeliveryVoucherConfig.DELIVERY_VOUCHER_TYPE_MAP) %></em>
	  	</p>
	  	<p>
	  	<%if(dvId > 0){ %>
	  	   <em class="int_label"><span>*</span>出库仓库：</em>
	  	   <em><%=warehouseInfoBean.getWarehouseName()%></em>
	       <input type="hidden" name="warehouseId" id="warehouseId" value="<%=bean.getWarehouseId()%>"/>	  		
	    <%}else{ %>
	       <em class="int_label"><span>*</span>出库仓库：</em>
	  	   <input type="text" name="warehouseName" id="warehouseName" value="<%=warehouseInfoBean.getWarehouseName()%>"  readOnly size="50" checkStr="出库仓库;txt;true;;100"/>
	       <input type="hidden" name="warehouseId" id="warehouseId" value="<%=bean.getWarehouseId()%>"/>
	       <%if(!isReadOnly){ %>
	       <input type="button" name="selectWarehouse" id="selectWarehouse" value="添加" />
	       <%} %>
	    <%} %>
	  	</p>
	  	<p>
	  		<em class="int_label"><span>*</span>出库日期：</em>
	  		<input id="stockOutTime" name="stockOutTime"  value="<%=DateKit.formatTimestamp(bean.getStockOutTime(), "yyyy-MM-dd")%>" readOnly  type="text" size="50" checkStr="出库日期;date;true;;100" value="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
	  	</p>
	  	<p>
  			<em class="int_label"><span>*</span>出货质检人：</em>
	  		<input type="text" id="inspectorName" name="inspectorName" value="<%=bean.getInspectorName()%>" size="50" checkStr="出货质检人;txt;true;;100"/>
	  	</p>
	  	<p>
  			<em class="int_label"><span>*</span>仓库出货人：</em>
	  		<input type="text" id="senderName" name="senderName" value="<%=bean.getSenderName()%>" size="50" checkStr="仓库出货人;txt;true;;100"/>
	  	</p>
	  <%-- 	<p>
  			<em class="int_label">记账人：</em>
  			<em><%=bean.getAccountClerkName() %></em>
	  		<input type="hidden" id="accountClerkName" name="accountClerkName"  value="<%=bean.getAccountClerkName()%>"/>
	  	</p> --%>
	  	<p>
  			<em class="int_label">创建人：</em>
  			<em><%=adminCache.getAdminUserNameByKey(bean.getCreateUser())%></em>
	  	</p>
	  	<p>
  			<em class="int_label">创建时间：</em>
  			<em><%=DateKit.getCurrentDate() %></em>
	  	</p>
	    <p class="p_top_border">
	      <em class="int_label">备注：</em>
	      <textarea  name="memo" id="memo" cols="60" rows="3" checkStr="备注;txt;false;;1000"><%=bean.getMemo() %></textarea>
	    </p>
	  </div>
	   <div id="detailDiv" style="width: 100%">
	  </div>
	  <div class="div_center">
	  <input type="submit" name="submit" id="submit" class="modifysubbtn" value="提交" />
	  <a href="DeliveryVoucherInfoList.do?wareType=<%=bean.getWareType()%>&dvType=<%=bean.getDvType()%>"><input type="button" name="cancel" id="cancel" value="取消" ></a>
	  </div>
	</form>
</body>
<script>
$(document).ready(function() {
	$("#selectWarehouse").click(
		function() {
			var warehouseId = $("#warehouseId").val();
			$.fancybox.open({
				href :  'WarehouseInfoSelect.do?callbackFun=selectWarehouse&warehouseId='+warehouseId,
				type : 'iframe',
				width : 560,
				minHeight : 500
		});
	});
	var readOnly = '${readOnly}';
	if(readOnly=='true') {
		disablePageElement();
	}
	detailInit();
});

function selectWarehouse(warehouseJsonStr,echoParameter){
	var warehouseJsonBean = jQuery.parseJSON(warehouseJsonStr);
	if(warehouseJsonBean.warehouseId != ""){
		$("#warehouseId").val(warehouseJsonBean.warehouseId);
		$("#warehouseName").val(warehouseJsonBean.warehouseName);
		clearSelectedItem();
	}
}

function formOnSubmit(){	
	//验证出库数量是否少于库存数量
	var trs = $('#detail tr');
	if(typeof(trs)=="undefined"||trs.length<=1){
	   alert("必须填写出库明细");
	   return false;
	}else{
	   for(var i=1;i<trs.length;i++){
		   var currentTr = $(trs[i]);
		   var inventoryQty = parseFloat(currentTr.find('#inventoryQty').text());
		   var stockOutQty = parseFloat(currentTr.find('#stockOutQuantity').val());
		   if(inventoryQty<stockOutQty){
			   //当前库存少于出库数量
			   alert("出库数量大于库存");
			   return false;
		   }
	   }
	}
	
	if(checkForm('formData')){
		//验证通过后，日期选择框补0
		var stockOutTime = $("#stockOutTime").val();
		if(stockOutTime != "" && stockOutTime.indexOf("00:00:00") == -1){
			$("#stockOutTime").val(stockOutTime  + " 00:00:00");
		}
		return true;
	}else{
		return false;
	}
}
function detailInit(){
	var sourceOrderType = $("#sourceOrderType").val();
	var sourceOrderId =$("#sourceOrderId").val()
	if(sourceOrderType.trim()==''){
		sourceOrderType = '0';
	}
	if(sourceOrderId.trim()==''){
		sourceOrderId = '0';
	}
	getDetailStr(sourceOrderType,sourceOrderId);
}
function getDetailStr(sourceType,sourceOrderId){
	$('#detailDiv').empty();
	var wareType = '<%=wareType%>';
	var dvId = '<%=dvId%>';
	var beanSourceId = '<%=bean.getSourceOrderId()%>';
	//判断现在选的原单是不是这个入库单本身记录的原单
	if(beanSourceId!=sourceOrderId){
		//不等于的话DVID强制为0，这样后台会认为从新原单取数据，而不是从数据库取
		dvId='0';
	}
	var url = '/erp/DeliveryVoucherItemAjaxList.do?wareType='+wareType+'&sourceType='+sourceType+'&sourceOrderId='+sourceOrderId+'&readOnly=<%=readOnly%>'+'&dvId='+dvId;
	$.ajax({
		  type: 'POST',
		  url: url,
		  success: function(res){
			  $('#detailDiv').html(res);
		  },
		  dataType: 'text'
	});
	
}
function addRow(){
	var warehouseId = $('#warehouseId').val();
	//校验仓库有无选
	if(typeof(warehouseId)=="undefined"||warehouseId.trim()==""||warehouseId.trim()=='0'){
		alert("请先选择仓库");
		return;
	}
	var selectedItem = $('tr').find('input[name=selectedItem]');
	var wiId = $('tr').find('input[name=wiId]');
	var selectItemStr="";
	var actionUrl = "/erp/WarehouseInventorySelect.do";
	var wareType = '<%=wareType%>';
	selectedItem.each(function(){
		selectItemStr+=$(this).val()+",";
	});
	wiId.each(function(){
		selectItemStr+=$(this).val()+",";
	});
	selectItemStr = selectItemStr.substring(0,selectItemStr.length-1);
	$.fancybox.open({
		href : actionUrl+"?selectedItem="+selectItemStr+'&openOrPop=pop&warehouseId='+warehouseId+"&wareType="+wareType,
		type : 'iframe',
		width : 650,
		minHeight : 500
});
}
function delRow(){
	var checkBoxs = $('input[name=select]');
	checkBoxs.each(function(e){
		if($(this).is(':checked')){
			$(this).parent().parent().remove();
		}
	});
	$('#selectAll').removeAttr("checked");
}
function callbackFun(selectItems){
	var trs = $('#detail').find('tr');
	//把空白的删除掉
	trs.each(function(){
		var tds = $(this).find('td');
		var itemType = $(tds[1]).html();
		var wareNumber = $(tds[2]).html();
		var itemName = $(tds[3]).html();
		if(itemType==""||wareNumber==""||itemName==""){
			$(this).remove();
		} 
	});
	var table = $('#detail');
	//插入行
	for(var i=0;i<selectItems.length;i++){
		if(selectItems[i]!=""){
			var itemContent = jQuery.parseJSON(selectItems[i]);
			if(itemContent.wareType=='<%=wareType%>'){
			var itemInventory = itemContent.itemInventory;
			var selectedItem = itemInventory.wiId;
			//检查保质期，修正显示
			var guaranteePeriod = itemInventory.guaranteePeriod;
			var expirationDate = itemContent.expirationDate;
			if(guaranteePeriod==-1){
				//无保质期管理
				guaranteePeriod = "--";
				expirationDate = "--";
			}
			var currentTr=table.addRow([
				  			'<input type="checkbox" name="select">',
				  			itemContent.itemNumber,
				  			itemContent.itemName,
				  			itemContent.itemSpecification,
				  			'<input type="hidden" name="unit" id="unit" value="'+itemInventory.unit+'"><span>'+itemContent.unitNmae+'</span>',
							'<input type="text" size="10" maxlength="10" onkeyup="this.value=this.value.replace(&#47[^&#92-?&#92d.]&#47g,&quot&quot)" checkStr="出库数量;num;true;;100" name="stockOutQuantity" id="stockOutQuantity" value="" onchange="itemModifyOnChange(this)">',
							'<span id="inventoryQty">'+itemInventory.quantity+'</span>',
							'<input type="hidden" id="batchNumber" value="'+itemInventory.batchNumber+'"><span>'+itemInventory.batchNumber+'</span>',
							'<input type="hidden" id="inventoryType" value="'+itemInventory.inventoryType+'"><span>'+itemContent.inventoryTypeName+'</span>',
							'<input type="hidden" id="productionDate" value="'+itemContent.productionDate+'"><span>'+itemContent.productionDate+'</span>',
							'<input type="hidden" id="guaranteePeriod" value="'+guaranteePeriod+'"><span>'+guaranteePeriod+'</span>',
							'<input type="hidden" id="expirationDate" value="'+expirationDate+'"><span>'+expirationDate+'</span>',
				  			'<input type="text" size="10" maxlength="20" checkStr="备注;txt;false;;500" id="memo" name="memo" value="" onchange="itemModifyOnChange(this)">',
				  			'<input type="hidden" name="selectedItem" value="'+selectedItem+'">',
				  			'<input type="hidden" id="inventoryType" name="inventoryType_'+selectedItem+'"  value="">',
				  			'<input type="hidden" id="wareId" name="wareId_'+selectedItem+'"  value="'+itemInventory.wareId+'">',
				  			'<input type="hidden" checkStr="出库明细值;txt;true;;1000" id="selectedItemValue" name="selectedItemValue_'+selectedItem+'"  value="">'
				  			]);
			//每新增一行，强行刷新一次selectedItemValue
			itemModifyOnChange(currentTr.find('#unit')[0]);
			}
		}
	}
}
function unitSelectionDo(ele){
	//判断是新建的还是修改的
	var currentRow = $(ele).parent().parent();
	var itemId = currentRow.find('input[name=itemId]').val();
	var selectedItem = currentRow.find('input[name=selectedItem]').val();
	var id ="";
	var itemName="";
	if(typeof(itemId)!="undefined") {
		id=itemId;
		itemName="itemId";
	}
	if(typeof(selectedItem)!="undefined"){
		id=selectedItem;
		itemName="selectedItem";
	} 
	
	$.fancybox.open({
		href : '<%=PDM_URL%>/pdm/UnitInfoSelect.do?callbackFun=unitSelectionCallBack&itemId=' + id+'&itemName='+itemName,
		type : 'iframe',
		width : 560,
		minHeight : 150
});
}
function unitSelectionCallBack(unitCode,unitName,itemId,itemName){
	var input = $('input[name='+itemName+']');
	input.each(function(){
		if($(this).val()==itemId){
			var parent = $(this).parent().parent();
			parent.find('#unitName').val(unitName);
			parent.find('#unit').val(unitCode);
			itemModifyOnChange(parent.find('#unitName')[0]);
			return false;
		}
	});
}
function itemModifyOnChange(ele){
	var currentRow = $(ele).parent().parent();
	var tds = currentRow.find('td');
	var typename = $(tds[1]).html();
	var typecode = $(tds[2]).html();
	var unit = currentRow.find('#unit').val();
	var stockOutQuantity = currentRow.find('#stockOutQuantity').val();
	var productionDate = currentRow.find('#productionDate').val();
	var guaranteePeriod = currentRow.find('#guaranteePeriod').val();
	var expirationDate = currentRow.find('#expirationDate').val();
	var batchNumber = currentRow.find('#batchNumber').val();
	var inventoryType = currentRow.find('#inventoryType').val();
	var memo = currentRow.find('#memo').val();
	var wareType = '<%=wareType%>';
	var itemValueStr = "unit="+unit+",stockOutQuantity="+stockOutQuantity+",productionDate="
						+productionDate+",guaranteePeriod="+guaranteePeriod+",expirationDate="+expirationDate
						+",memo="+memo+",typecode="+typecode+",wareType="+wareType+",batchNumber="+batchNumber
						+",inventoryType="+inventoryType;
	var itemValue = currentRow.find('#selectedItemValue').val(itemValueStr);
}
function clearSelectedItem(){
	var trs = $('#detail tr');
	if(trs.length>1){
		for(var i=1;i<trs.length;i++){
			$(trs[i]).remove();
		}
	}
}
</script>
</html>