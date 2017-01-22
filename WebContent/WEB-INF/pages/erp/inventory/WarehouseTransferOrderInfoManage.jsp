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
<%@ page import="com.lpmas.pdm.bean.*"  %>
<%@ include file="../../include/header.jsp" %>
<% 
    WarehouseTransferOrderInfoBean bean = (WarehouseTransferOrderInfoBean)request.getAttribute("WarehouseTransferOrderInfoBean");
	AdminUserHelper adminUserHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	AdminUserInfoClientCache adminCache = new AdminUserInfoClientCache();
	int toId = bean.getToId();
	int wareType = ParamKit.getIntParameter(request, "wareType", 0);
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	boolean isReadOnly = readOnly.equals("true")? true:false;
	request.setAttribute("readOnly", readOnly);
	WarehouseInfoBean sourceWarehouseInfoBean = (WarehouseInfoBean)request.getAttribute("SourceWarehouseInfoBean");
	WarehouseInfoBean targetWarehouseInfoBean = (WarehouseInfoBean)request.getAttribute("TargetWarehouseInfoBean");
	List<WarehouseTransferOrderItemEntityBean> warehouseTransferOrderItemEntityList = (List<WarehouseTransferOrderItemEntityBean>)request.getAttribute("WarehouseTransferOrderItemEntityList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>调拨管理</title>
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
			<li><a href="WarehouseTransferOrderInfoList.do?wareType=<%=bean.getWareType()%>"><%=MapKit.getValueFromMap(wareType, InventoryConsoleConfig.WARE_TYPE_MAP)%>调拨单列表</a>&nbsp;>&nbsp;</li>
			<% if(toId > 0) {%>
			<li><%=bean.getToNumber() %>&nbsp;>&nbsp;</li>
			<li>修改调拨单</li>
			<%}else{ %>
			<li>新建调拨单</li>
			<%}%>
		</ul>
	</div>
	<% if(toId > 0) {%>
	<div class="article_tit">
		<p class="tab">
		<a href="WarehouseTransferOrderInfoManage.do?toId=<%=toId%>&readOnly=<%=readOnly%>&wareType=<%=wareType%>&mode=po">基础信息</a> 
		<a href="WarehouseTransferOperationLogList.do?toId=<%=toId%>&readOnly=<%=readOnly%>&wareType=<%=wareType%>&mode=po">调拨单日志</a>
		</p>
		<script>
		tabChange('.tab', 'a');
		</script>
	</div>
	<%}%>
    <form style="width: 100%" id="formData" name="formData" method="post" action="WarehouseTransferOrderInfoManage.do" onsubmit="javascript:return formOnSubmit();">
	  <input type="hidden" name="toId" id="toId" value="<%=bean.getToId() %>"/>
	  <input type="hidden" name="toNumber" id="toNumber" value="<%=bean.getToNumber() %>"/>
	  <input type="hidden" name="wareType" id="wareType" value="<%=bean.getWareType() %>"/>
	  <input type="hidden" name="approveStatus" id="approveStatus" value="<%=bean.getApproveStatus() %>"/>
	  <input type="hidden" name="syncStatus" id="syncStatus" value="<%=bean.getSyncStatus() %>"/>
	  <input type="hidden" name="toStatus" id="toStatus" value="<%=bean.getToStatus() %>"/>
	  <input type="hidden" name="status" id="status" value="<%=bean.getStatus() %>"/>
	  <div class="article_subtit">基础信息</div>
	  <div class="modify_form">
  		  <p>
	  		<em class="int_label">制品类型：</em>
	  		<em><%=MapKit.getValueFromMap(bean.getWareType(), InventoryConsoleConfig.WARE_TYPE_MAP)%></em>
	  	   </p>
		  <div class="u-wrap">
	  		<div class="u-wrap_half bor">
	  			<p>
			  	<%if(toId > 0){ %>
			  	   <em class="int_label"><span>*</span>调拨出库仓库：</em>
			  	   <em><%=sourceWarehouseInfoBean.getWarehouseName()%></em>
			       <input type="hidden" name="sourceWarehouseId" id="sourceWarehouseId" value="<%=bean.getSourceWarehouseId() %>"/>	  		
			    <%}else{ %>
			       <em class="int_label"><span>*</span>调拨出库仓库：</em>
			  	   <input type="text" name="sourceWarehouseName" id="sourceWarehouseName" value="<%=sourceWarehouseInfoBean.getWarehouseName()%>"  readOnly size="50" checkStr="调拨出库仓库;txt;true;;100"/>
			       <input type="hidden" name="sourceWarehouseId" id="sourceWarehouseId" value="<%=bean.getSourceWarehouseId()%>"/>
			       <%if(!isReadOnly){ %>
			       <input type="button" name="selectSourceWarehouse" id="selectSourceWarehouse" value="添加" />
			       <%} %>
			    <%} %>
			  	  </p>
			  	   <p>
		  			<em class="int_label"><span>*</span>调拨出库质检人：</em>
			  		<input type="text" id="dvInspectorName" name="dvInspectorName" value="<%=bean.getDvInspectorName() %>" size="50" checkStr="调拨出货质检人;txt;true;;100"/>
			  	   </p>
			  	   <p>
		  			<em class="int_label"><span>*</span>仓库出货人：</em>
			  		<input type="text" id="dvSenderName" name="dvSenderName" value="<%=bean.getDvSenderName() %>" size="50" checkStr="仓库出货人;txt;true;;100"/>
			  	   </p>
			  	   <p>
			  	    <em class="int_label"><span>*</span>出货记账人：</em>
			  		<input type="text" id="dvAccountClerkName" name="dvAccountClerkName"  value="<%=bean.getDvAccountClerkName()%>" size="50" checkStr="出货记账人;txt;true;;100"/>
			  	   </p>
			  </div>
			  <div class="u-wrap_half">
	  			<p>
		        <%if(toId > 0){ %>
			  	   <em class="int_label"><span>*</span>调拨入库仓库：</em>
			  	   <em><%=targetWarehouseInfoBean.getWarehouseName()%></em>
			       <input type="hidden" name="targetWarehouseId" id="targetWarehouseId" value="<%=bean.getTargetWarehouseId() %>"/>	  		
			    <%}else{ %>
			       <em class="int_label"><span>*</span>调拨入库仓库：</em>
			  	   <input type="text" name="targetWarehouseName" id="targetWarehouseName" value="<%=targetWarehouseInfoBean.getWarehouseName()%>"  readOnly size="50" checkStr="调拨入库仓库;txt;true;;100"/>
			       <input type="hidden" name="targetWarehouseId" id="targetWarehouseId" value="<%=bean.getTargetWarehouseId()%>"/>
			       <%if(!isReadOnly){ %>
			       <input type="button" name="selectTargetWarehouse" id="selectTargetWarehouse" value="添加" />
			       <%} %>
			    <%} %>
			     </p>
			  	  <p>
		  			<em class="int_label"><span>*</span>调拨入库质检人：</em>
			  		<input type="text" id="wvInspectorName" name="wvInspectorName" value="<%=bean.getWvInspectorName() %>" size="50" checkStr="调拨入货质检人;txt;true;;100"/>
			  	 </p>
			  	  <p>
		  			<em class="int_label"><span>*</span>入库收货人：</em>
			  		<input type="text" id="wvReceiverName" name="wvReceiverName" value="<%=bean.getWvReceiverName() %>" size="50" checkStr="调拨入库收货人;txt;true;;100"/>
			  	  </p>
			  	  <p>
			  	     <em class="int_label"><span>*</span>入库记账人：</em>
			  		<input type="text" id="wvAccountClerkName" name="wvAccountClerkName"  value="<%=bean.getWvAccountClerkName()%> "size="50" checkStr="入库记账人;txt;true;;100"/>
			  	 </p>
	  		</div>
			</div>
  	       <p>
  			<em class="int_label">创建人：</em>
  			<em><%=adminCache.getAdminUserNameByKey(bean.getCreateUser())%></em>
	  	   </p>
	  	   <p>
  			<em class="int_label">创建时间：</em>
  			<em><%=DateKit.getCurrentDate() %></em>
	  	   </p>
	     <p>
	      <em class="int_label">备注：</em>
	      <textarea  name="memo" id="memo" cols="50" rows="3" checkStr="备注;txt;false;;1000"><%=bean.getMemo() %></textarea>
	     </p>
	  </div>
	  <div class="article_subtit">调拨明细</div>
	  <div id="detailDisplay">
	<table id="detail" width="100%" border="0" cellpadding="0" class="table_style">
		<tr>
			<th><input type="checkbox" id="selectAll" onchange="checkAll(this)">选择</th>
			<th><%=MapKit.getValueFromMap(wareType, InventoryConsoleConfig.WARE_TYPE_MAP)%>编码</th>
			<th><%=MapKit.getValueFromMap(wareType, InventoryConsoleConfig.WARE_TYPE_MAP)%>名称</th>
			<th>型号规格</th>
			<th>采购计量单位</th>
			<th>调拨数量</th>
			<th>当前可用库存</th>
			<th>批次号</th>
			<th>库存类型</th>
			<th>生产日期</th>
			<th>保质期(天)</th>
			<th>保质期至</th>
			<th width="10%">备注</th>
		</tr>
            <%for(WarehouseTransferOrderItemEntityBean warehouseTransferOrderItemEntityBean : warehouseTransferOrderItemEntityList){ %>
				<tr>
					<td><input type="checkbox" name="select"></td>
					<td><span><%=warehouseTransferOrderItemEntityBean.getWareNumber()%></span></td>
					<td><span><%=warehouseTransferOrderItemEntityBean.getWareName()%></span></td>
					<td><span><%=warehouseTransferOrderItemEntityBean.getSpecification()%></span></td>
					<td><input type="hidden" name="unit_<%=warehouseTransferOrderItemEntityBean.getToItemId() %>" id="unit" value="<%=warehouseTransferOrderItemEntityBean.getUnit() %>"><span><%=warehouseTransferOrderItemEntityBean.getUnitName()%></span></span></td>
					<td><input type="text" onkeyup='this.value=this.value.replace(/[^\-?\d.]/g,"")' size="10" maxlength="10" checkStr="调拨数量;num;true;;100" name="quantity_<%=warehouseTransferOrderItemEntityBean.getToItemId() %>" id="quantity" value="<%=warehouseTransferOrderItemEntityBean.getQuanlity() %>" onchange="itemModifyOnChange(this)"></td>
					<td><span id="inventoryQty"><%=warehouseTransferOrderItemEntityBean.getQuantity()%></span></td>
					<td><input type="hidden" name="batchNumber_<%=warehouseTransferOrderItemEntityBean.getToItemId() %>" id="batchNumber" value="<%=warehouseTransferOrderItemEntityBean.getBatchNumber() %>"><span><%=warehouseTransferOrderItemEntityBean.getBatchNumber() %></span></td>
					<td><input type="hidden" id="inventoryType" value="<%=WarehouseInventoryConfig.WIT_NORMAL %>"><span><%=WarehouseInventoryConfig.WAREHOUSE_INVENTORY_TYPE_MAP.get(WarehouseInventoryConfig.WIT_NORMAL) %></span></td>
					<td><input type="hidden" name="productionDate_<%=warehouseTransferOrderItemEntityBean.getToItemId() %>" id="productionDate" value="<%=DateKit.formatTimestamp(warehouseTransferOrderItemEntityBean.getProductionDate(), DateKit.DEFAULT_DATE_FORMAT)%>"><span><%=DateKit.formatTimestamp(warehouseTransferOrderItemEntityBean.getProductionDate(), DateKit.DEFAULT_DATE_FORMAT)%></span></td>
					<td><input type="hidden"  name="guaranteePeriod_<%=warehouseTransferOrderItemEntityBean.getToItemId() %>" id="guaranteePeriod" value="<%=warehouseTransferOrderItemEntityBean.getGuaranteePeriod() %>"><span><%=warehouseTransferOrderItemEntityBean.getGuaranteePeriod()==-1?"--":warehouseTransferOrderItemEntityBean.getGuaranteePeriod()%></span></td>
					<td><input type="hidden" name="expirationDate_<%=warehouseTransferOrderItemEntityBean.getToItemId() %>" id="expirationDate" value="<%=DateKit.formatTimestamp(warehouseTransferOrderItemEntityBean.getExpirationDate(), DateKit.DEFAULT_DATE_FORMAT)%>"><span><%=DateKit.formatTimestamp(warehouseTransferOrderItemEntityBean.getExpirationDate(), DateKit.DEFAULT_DATE_FORMAT) == null ? "--": DateKit.formatTimestamp(warehouseTransferOrderItemEntityBean.getExpirationDate(), DateKit.DEFAULT_DATE_FORMAT)%></span></td>
					<td><input type="text" checkStr="备注;txt;false;;100" name="memo_<%=warehouseTransferOrderItemEntityBean.getToItemId()%>" size="10" maxlength="20" id="memo" value="<%=warehouseTransferOrderItemEntityBean.getMemo()%>" onchange="itemModifyOnChange(this)"></td>
					<td><input type="hidden" name="itemId" value="<%=warehouseTransferOrderItemEntityBean.getToItemId() %>"></td>
					<td><input type="hidden" name="wiId" value="<%=warehouseTransferOrderItemEntityBean.getWiId()%>"></td>
					<td><input type="hidden" id="wareId" name="wareId" value="<%=warehouseTransferOrderItemEntityBean.getWareId() %>"></td>
					<td><input type="hidden" name="selectedItemValue" id="selectedItemValue" value="<%=warehouseTransferOrderItemEntityBean.getItemValue() %>"></td>
				</tr>
			<%} %>	
	</table>
	<%if(!isReadOnly){ %>
	<input type="button" name="button" id="add" class="modifysubbtn" onclick="addRow()" value="添加" />
	<input type="button" name="button" id="delete" class="modifysubbtn" onclick="delRow()" value="删除" />
	<%}%>
</div>
	  <div class="div_center">
	  <input type="submit" name="submit" id="submit" class="modifysubbtn" value="提交" />
	  <a href="WarehouseTransferOrderInfoList.do?wareType=<%=bean.getWareType()%>"><input type="button" name="cancel" id="cancel" value="取消" ></a>
	  </div>
	</form>
</body>
<script>
$(document).ready(function() {
	$("#selectSourceWarehouse").click(
		function() {
			var warehouseId = $("#sourceWarehouseId").val();
			$.fancybox.open({
				href :  'WarehouseInfoSelect.do?callbackFun=selectWarehouse&warehouseId='+warehouseId,
				type : 'iframe',
				width : 560,
				minHeight : 500
		});
	});
	$("#selectTargetWarehouse").click(
			function() {
				var warehouseId = $("#targetWarehouseId").val();
				$.fancybox.open({
					href :  'WarehouseInfoSelect.do?callbackFun=selectTargetWarehouse&warehouseId='+warehouseId,
					type : 'iframe',
					width : 560,
					minHeight : 500
			});
		});
	var readOnly = '${readOnly}';
	if(readOnly=='true') {
		disablePageElement();
	}
});

function selectWarehouse(warehouseJsonStr,echoParameter){
	var warehouseJsonBean = jQuery.parseJSON(warehouseJsonStr);
	if(warehouseJsonBean.warehouseId != ""){
		$("#sourceWarehouseId").val(warehouseJsonBean.warehouseId);
		$("#sourceWarehouseName").val(warehouseJsonBean.warehouseName);
		clearSelectedItem();
	}
}
function selectTargetWarehouse(warehouseJsonStr,echoParameter){
	var warehouseJsonBean = jQuery.parseJSON(warehouseJsonStr);
	if(warehouseJsonBean.warehouseId != ""){
		$("#targetWarehouseId").val(warehouseJsonBean.warehouseId);
		$("#targetWarehouseName").val(warehouseJsonBean.warehouseName);
		clearSelectedItem();
	}
}

function formOnSubmit(){	
	//验证出库数量是否少于库存数量
	var trs = $('#detail tr');
	if(typeof(trs)=="undefined"||trs.length<=1){
	   alert("必须填写调拨单明细");
	   return false;
	}else{
	   for(var i=1;i<trs.length;i++){
		   var currentTr = $(trs[i]);
		   var inventoryQty = parseFloat(currentTr.find('#inventoryQty').text());
		   var stockOutQty = parseFloat(currentTr.find('#quantity').val());
		   if(inventoryQty<stockOutQty){
			   //当前库存少于出库数量
			   alert("调拨数量大于库存");
			   return false;
		   }
	   }
	}	
}
function addRow(){
	var warehouseId = $('#sourceWarehouseId').val();
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
		href : actionUrl+"?selectedItem="+selectItemStr+'&openOrPop=pop&warehouseId='+warehouseId+"&wareType="+wareType+"&mode=transfer",
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
							'<input type="text" size="10" maxlength="10" onkeyup="this.value=this.value.replace(&#47[^&#92-?&#92d.]&#47g,&quot&quot)" checkStr="调拨数量;num;true;;100" name="quantity" id="quantity" value="" onchange="itemModifyOnChange(this)">',
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
				  			'<input type="hidden" checkStr="调拨明细值;txt;true;;1000" id="selectedItemValue" name="selectedItemValue"  value="">'
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
	var wareId = currentRow.find('#wareId').val();
	var unit = currentRow.find('#unit').val();
	var quantity = currentRow.find('#quantity').val();
	var productionDate = currentRow.find('#productionDate').val();
	var guaranteePeriod = currentRow.find('#guaranteePeriod').val();
	var expirationDate = currentRow.find('#expirationDate').val();
	var batchNumber = currentRow.find('#batchNumber').val();
	var inventoryType = currentRow.find('#inventoryType').val();
	var memo = currentRow.find('#memo').val();
	var wareType = '<%=wareType%>';
	var itemValueStr = "wareId="+wareId+",unit="+unit+",quantity="+quantity+",productionDate="
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