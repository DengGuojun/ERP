<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<%@ include file="../../include/header.jsp" %>
<% 
	WarehouseVoucherInfoEntityBean bean = (WarehouseVoucherInfoEntityBean)request.getAttribute("WarehouseVoucherInfoEntityBean");
	AdminUserHelper adminUserHelper = (AdminUserHelper)request.getAttribute("AdminUserHelper");
	AdminUserInfoClientCache adminCache = new AdminUserInfoClientCache();
	int wvId = bean.getWvId();
	int wvType = bean.getWvType();
	boolean hasSourceSupplier = (Boolean)request.getAttribute("HasSourceSupplier");
	int wareType = ParamKit.getIntParameter(request, "wareType", 0);
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	boolean isReadOnly = readOnly.equals("true")? true:false;
	request.setAttribute("readOnly", readOnly);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>入库管理</title>
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
			<li><a href="WarehouseVoucherInfoList.do?wareType=<%=bean.getWareType()%>&wvType=<%=bean.getWvType()%>"><%=MapKit.getValueFromMap(bean.getWareType(), InventoryConsoleConfig.WARE_TYPE_MAP)%><%=MapKit.getValueFromMap(bean.getWvType(), WarehouseVoucherConfig.WAREHOUSE_VOUCHER_TYPE_MAP)%>单列表</a>&nbsp;>&nbsp;</li>
			<% if(wvId > 0) {%>
			<li><%=bean.getWvNumber() %>&nbsp;>&nbsp;</li>
			<li>修改入库单</li>
			<%}else{ %>
			<li>新建入库单</li>
			<%}%>
		</ul>
	</div>
	<% if(wvId > 0) {%>
	<div class="article_tit">
		<p class="tab">
		<a href="WarehouseVoucherInfoManage.do?wvId=<%=wvId%>&readOnly=<%=readOnly%>&wvType=<%=wvType%>&wareType=<%=wareType%>&mode=po">基础信息</a> 
		<a href="WarehouseVoucherOperationLogList.do?wvId=<%=wvId%>&readOnly=<%=readOnly%>&wvType=<%=wvType%>&wareType=<%=wareType%>&mode=po">入库单日志</a>
		</p>
		<script>
		tabChange('.tab', 'a');
		</script>
	</div>
	<%}%>
	<form style="width: 100%" id="formData" name="formData" method="post" action="WarehouseVoucherInfoManage.do" onsubmit="javascript:return formOnSubmit();">
	  <input type="hidden" name="wvId" id="wvId" value="<%=bean.getWvId() %>"/>
	  <input type="hidden" name="wvNumber" id="wvNumber" value="<%=bean.getWvNumber() %>"/>
	  <input type="hidden" name="wareType" id="wareType" value="<%=bean.getWareType() %>"/>
	  <input type="hidden" name="wvType" id="wvType" value="<%=bean.getWvType() %>"/>
	  <input type="hidden" name="approveStatus" id="approveStatus" value="<%=bean.getApproveStatus() %>"/>
	  <input type="hidden" name="syncStatus" id="syncStatus" value="<%=bean.getSyncStatus() %>"/>
	  <input type="hidden" name="wvStatus" id="wvStatus" value="<%=bean.getWvStatus() %>"/>
	  <input type="hidden" name="status" id="status" value="<%=bean.getStatus() %>"/>
	  <p>基础信息</p>
	  <div class="modify_form">
	  	<p>
	  		<em class="int_label">入库货品类型：</em>
	  		<em><%=MapKit.getValueFromMap(bean.getWareType(), InventoryConsoleConfig.WARE_TYPE_MAP)%></em>
	  	</p>
	  	<p>
	  		<em class="int_label">入库类型：</em>
	  		<em><%=MapKit.getValueFromMap(bean.getWvType(), WarehouseVoucherConfig.WAREHOUSE_VOUCHER_TYPE_MAP) %></em>
	  	</p>
	  	<%if(wvType==WarehouseVoucherConfig.WVT_PURCHASE){ %>
		  	<p>
		  		<em class="int_label"><span>*</span>源单类型：</em>
		  		<select name="sourceOrderType" id="sourceOrderType" >
		      		<%for(StatusBean<Integer, String> statusBean : SourceOrderTypeConfig.SOURCE_ORDER_TYPE_LIST){ %><option value="<%=statusBean.getStatus() %>" <%=(statusBean.getStatus() == bean.getSourceOrderType())?"selected":"" %>><%=statusBean.getValue() %></option><%} %>
		      	</select>
		  	</p>
		  	<p>
		  		<em class="int_label"><span>*</span>源单号：</em>
		  		<input type="text" name="sourceOrderNumber" id="sourceOrderNumber" value="<%=bean.getSourceOrderNumber()%>"  readOnly checkStr="源单号;txt;true;;100"/>
		      	<input type="hidden" name="sourceOrderId" id="sourceOrderId" value="<%=bean.getSourceOrderId()%>"/>
		      	<%if(!isReadOnly){ %>
		      	<input type="button" name="selectSourceOrder" id="selectSourceOrder" value="添加" />
		      	<%} %>
		  	</p>
	  	<%} %>
	  	<p>
	  		<em class="int_label"><span>*</span>入库仓库：</em>
	  		<input type="text" name="warehouseName" id="warehouseName" value="<%=bean.getWarehouseName()%>"  readOnly size="50" checkStr="入库仓库;txt;true;;100"/>
	      	<input type="hidden" name="warehouseId" id="warehouseId" value="<%=bean.getWarehouseId()%>"/>
	      	<%if(!isReadOnly){ %>
	      	<input type="button" name="selectWarehouse" id="selectWarehouse" value="添加" />
	      	<%} %>
	  	</p>
	  	<p>
	  		<em class="int_label"><span>*</span>入库日期：</em>
	  		<input id="stockInTime" name="stockInTime"  value="<%=DateKit.formatTimestamp(bean.getStockInTime() , "yyyy-MM-dd")%>" readOnly  type="text" size="50" checkStr="入库日期;date;true;;100" value="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
	  	</p>
	  	<p>
  			<em class="int_label"><span>*</span>入库质检人：</em>
	  		<input type="text" id="inspectorName" name="inspectorName" value="<%=bean.getInspectorName()%>" size="50" checkStr="入库质检人;txt;true;;100"/>
	  	</p>
	  	<p>
  			<em class="int_label"><span>*</span>仓库收货人：</em>
	  		<input type="text" id="receiverName" name="receiverName" value="<%=bean.getReceiverName()%>" size="50" checkStr="仓库收货人;txt;true;;100"/>
	  	</p>
	  	<p>
  			<em class="int_label"><span>*</span>供应商：</em>
	  		<input type="text" id="supplierName" name="supplierName" value="<%=bean.getSupplierName() %>" size="50"  readOnly checkStr="供应商;txt;true;;100"/>
	  		<input type="hidden" id="supplierId" name="supplierId" value="<%=bean.getSupplierId() %>" />
	  		<%if(!isReadOnly){ %>
	  		<input type="button" name="selectSupplier" id="selectSupplier" value="添加" />
	  		<%} %>
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
	  <a href="WarehouseVoucherInfoList.do?wareType=<%=bean.getWareType()%>&wvType=<%=bean.getWvType()%>"><input type="button" name="cancel" id="cancel" value="取消" ></a>
	  </div>
	</form>
</body>
<script>
$(document).ready(function() {
	$("#selectSourceOrder").click(
		function() {
			var sourceOrderId = $("#sourceOrderId").val();
			var wareType = $("#wareType").val();
			var url = "";
			var sourceOrderType = $("#sourceOrderType").val();
			var purchaseOrder = "<%=SourceOrderTypeConfig.SOURCE_ORDER_TYPE_PURCHASE_ORDER%>";
			if(sourceOrderType == purchaseOrder){
				url = "PurchaseOrderInfoSelect.do?&callbackFun=selectSourceOrder&wareType="+ wareType+ "&poId=" + sourceOrderId
			}
			$.fancybox.open({
				href :  url,
				type : 'iframe',
				width : 560,
				minHeight : 500
		});
	});
	
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
	
	$("#selectSupplier").click(
		function() {
			var supplierId = $("#supplierId").val();
			var supplierName = $("#supplierName").val();
			$.fancybox.open({
				href : '<%=SRM_URL%>/srm/SupplierInfoSelect.do?callbackFun=selectSupplier&supplierId='+ supplierId,
				type : 'iframe',
				width : 560,
				minHeight : 500
		});
	});
	
	var hasSourceSupplier = <%=hasSourceSupplier%>;
	if(hasSourceSupplier ==true){
		$("#selectSupplier").hide();
	}
	
	var readOnly = '${readOnly}';
	if(readOnly=='true') {
		disablePageElement();
	}
	detailInit();
	formatDouble();
});
function detailInit(){
	var sourceOrderType = $("#sourceOrderType").val();
	var sourceOrderId =$("#sourceOrderId").val()
	if(typeof(sourceOrderType)=='undefined'||sourceOrderType.trim()==''){
		sourceOrderType = '0';
	}
	if(typeof(sourceOrderId)=='undefined'||sourceOrderId.trim()==''){
		sourceOrderId = '0';
	}
	getDetailStr(sourceOrderType,sourceOrderId);
}
function selectSourceOrder(sourceOrderId, sourceOrderNumber, supplierId, supplierName){
	if(sourceOrderId != "" && sourceOrderNumber != ""){
		$("#sourceOrderId").val(sourceOrderId);
		$("#sourceOrderNumber").val(sourceOrderNumber)
	}
	if(supplierId != "" && supplierName != ""){
		$("#supplierId").val(supplierId);
		$("#supplierName").val(supplierName);
		$("#selectSupplier").hide();
	}else{
		$("#selectSupplier").show();
	}
	getDetailStr($("#sourceOrderType").val(),sourceOrderId);
}
function selectWarehouse(warehouseJsonStr,echoParameter){
	var warehouseJsonBean = jQuery.parseJSON(warehouseJsonStr);
	if(warehouseJsonBean.warehouseId != ""){
		$("#warehouseId").val(warehouseJsonBean.warehouseId);
		$("#warehouseName").val(warehouseJsonBean.warehouseName)
	}
}
function selectSupplier(supplierInfoJsonStr,echoParameter){
	var supplierInfoBean = jQuery.parseJSON(supplierInfoJsonStr);
	if(supplierInfoBean.supplierId != ""){
		$("#supplierName").val(supplierInfoBean.supplierName);
		$("#supplierId").val(supplierInfoBean.supplierId);
	}
	
}
function unitSelectionDo(ele){
	//判断是新建的还是修改的
	var currentRow = $(ele).parent().parent();
	var itemId = currentRow.find('input[name=itemId]').val();
	var selectedItem = currentRow.find('input[name=selectedItem]').val();
	var unitCode = currentRow.find('#unit').val();
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
		href : '<%=PDM_URL%>/pdm/UnitInfoSelect.do?callbackFun=unitSelectionCallBack&itemId=' + id+'&itemName='+itemName+'&unitCode='+unitCode,
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
function getDetailStr(sourceType,sourceOrderId){
	$('#detailDiv').empty();
	var wareType = '<%=wareType%>';
	var wvId = '<%=wvId%>';
	var beanSourceId = '<%=bean.getSourceOrderId()%>';
	var beanSourceType = '<%=bean.getSourceOrderType()%>';
	//判断现在选的原单是不是这个入库单本身记录的原单
	if(beanSourceId!=sourceOrderId&&beanSourceType=='<%=SourceOrderTypeConfig.SOURCE_ORDER_TYPE_PURCHASE_ORDER%>'){
		//不等于的话WVID强制为0，这样后台会认为从新原单取数据，而不是从数据库取
		wvId='0';
	}
	var url = '/erp/WarehouseVoucherItemAjaxList.do?wareType='+wareType+'&sourceType='+sourceType+'&sourceOrderId='+sourceOrderId+'&readOnly=<%=readOnly%>'+'&wvId='+wvId;
	$.ajax({
		  type: 'POST',
		  url: url,
		  success: function(res){
			  $('#detailDiv').html(res);
			  formatDouble();
		  },
		  dataType: 'text'
	});
	
}
function addRow(){
	var selectedItem = $('tr').find('input[name=selectedItem]');
	var itemId = $('tr').find('input[name=itemId]');
	var selectItemStr="";
	<%if(wareType==InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM) { %>
	var actionUrl = "<%=PDM_URL%>/pdm/ProductItemSelect.do";
	<%}if(wareType==InfoTypeConfig.INFO_TYPE_MATERIAL) { %>
	var actionUrl = "<%=PDM_URL%>/pdm/MaterialInfoSelect.do";
	<%} %>
	selectedItem.each(function(){
		selectItemStr+=$(this).val()+",";
	});
	itemId.each(function(){
		selectItemStr+=$(this).val()+",";
	});
	selectItemStr = selectItemStr.substring(0,selectItemStr.length-1);
	$.fancybox.open({
		href : actionUrl+"?selectedItem="+selectItemStr+'&openOrPop=pop',
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
			var currentTr=table.addRow([
				  			'<input type="checkbox" name="select">',
				  			itemContent.itemNumber,
				  			itemContent.itemName,
				  			itemContent.itemSpecification,
				  			'<input type="hidden" name="unit" id="unit"><input type="text" size="10" maxlength="10" readOnly onclick="unitSelectionDo(this)" checkStr="采购计量单位;txt;true;;100" id="unitName" value="">',
				  			'<input type="text" size="10" maxlength="10" onkeyup="this.value=this.value.replace(&#47[^&#92-?&#92d.]&#47g,&quot&quot)" checkStr="应收数量;num;true;;100" name="receivableQuantity" id="receivableQuantity" value="" onchange="itemModifyOnChange(this)">',
							'<input type="text" size="10" maxlength="10" onkeyup="this.value=this.value.replace(&#47[^&#92-?&#92d.]&#47g,&quot&quot)" checkStr="实收数量;num;true;;100" name="stockInQuantity" id="stockInQuantity" value="" onchange="itemModifyOnChange(this)">',
							'<span id="discrepancy" name="discrepancy">0.00</span></td>',
							'<input type="text" size="10" maxlength="10" onkeyup="this.value=this.value.replace(&#47[^&#92-?&#92d.]&#47g,&quot&quot)" checkStr="残次品;num;true;;100" name="defectQuantity" id="defectQuantity" value="" onchange="itemModifyOnChange(this)" >',
							'<input type="text" size="10" checkStr="生产日期;txt;true;;100" name="batchNumber" id="batchNumber" value="" onchange="itemModifyOnChange(this)">',
							'<input type="text" size="10" onkeyup="this.value=this.value.replace(&#47[^&#92-?&#92d.]&#47g,&quot&quot)" checkStr="生产日期;date;true;;100" name="productionDate" id="productionDate" value="" onchange="itemModifyOnChange(this)" readOnly>',
							'<input type="text" size="10" maxlength="10" onkeyup="this.value=this.value.replace(&#47[^&#92-?&#92d.]&#47g,&quot&quot)" checkStr="保质期;num;true;;100" name="guaranteePeriod" id="guaranteePeriod" value="" onchange="itemModifyOnChange(this)" >',
							'<input type="text" size="10" onkeyup="this.value=this.value.replace(&#47[^&#92-?&#92d.]&#47g,&quot&quot)" checkStr="保质期至;date;true;;100" name="expirationDate" id="expirationDate" value=""} onchange="itemModifyOnChange(this)" readOnly>',
				  			'<input type="text" size="20" checkStr="备注;txt;false;;500" id="memo" onchange="itemModifyOnChange(this)" value="">',
				  			'<input type="hidden" name="selectedItem" value="'+itemContent.itemId+'">',
				  			'<input type="hidden" checkStr="入库明细值;txt;true;;1000" id="selectedItemValue" name="selectedItemValue_'+itemContent.itemId+'"  value="">',
				  			]);
			//检查是否保质期管理
			if(typeof(guaranteePeriod)!="undefined"&&itemContent.itemGuaranteePeriod!=-1){
				//如果有，就读取保质期和允许修改，保质期至允许修改
				currentTr.find('#expirationDate').attr("onclick","WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'2008-01-01',maxDate:'2020-01-01'})");
				//尝试获取PDM的保质期
				//获取到就使用，获取不到填0
				currentTr.find('#guaranteePeriod').val(itemContent.itemGuaranteePeriod);
			}else{
				//如果没有，保质期和保质期至都不允许修改
				currentTr.find('#guaranteePeriod').attr('readOnly','readOnly');
				currentTr.find('#guaranteePeriod').removeAttr('checkStr');
				currentTr.find('#guaranteePeriod').attr('checkStr','保质期;num;false;;100');
				
				currentTr.find('#expirationDate').removeAttr('checkStr');
				currentTr.find('#expirationDate').attr('checkStr','保质期至;date;false;;100');
			}
			currentTr.find('#productionDate').attr("onclick","WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'2008-01-01',maxDate:'2020-01-01'})");
			}
		}
	}
}
function discrepancyCaculation(receivableQuantity,stockInQuantity,currentRow){
	if(receivableQuantity.trim()==''||stockInQuantity.trim()==''){
		return;
	}
	var discrepancy = (parseFloat(receivableQuantity)-parseFloat(stockInQuantity)).toFixed(2);
	currentRow.find('#discrepancy').html(discrepancy);
	formatDouble();
}
function formatDouble(){
	 //更正明细条目回显小数位
	 var discrepancys = $('span[name=discrepancy]');
	 discrepancys.each(function(){
		 var temp = $(this).text();
		 temp = parseFloat(temp).toFixed(2);
		 $(this).text(temp);
	 });
}
function itemModifyOnChange(ele){
	var currentRow = $(ele).parent().parent();
	var tds = currentRow.find('td');
	var typename = $(tds[1]).html();
	var typecode = $(tds[2]).html();
	var unit = currentRow.find('#unit').val();
	var defectQuantity = currentRow.find('#defectQuantity').val();
	var receivableQuantity = currentRow.find('#receivableQuantity').val();
	var stockInQuantity = currentRow.find('#stockInQuantity').val();
	var productionDate = currentRow.find('#productionDate').val();
	var guaranteePeriod = currentRow.find('#guaranteePeriod').val();
	var expirationDate = currentRow.find('#expirationDate').val();
	var batchNumber = currentRow.find('#batchNumber').val();
	var memo = currentRow.find('#memo').val();
	var wareType = '<%=wareType%>';
	var itemValueStr = "unit="+unit+",receivableQuantity="+receivableQuantity+",stockInQuantity="+stockInQuantity+",productionDate="
						+productionDate+",guaranteePeriod="+guaranteePeriod+",expirationDate="+expirationDate
						+",memo="+memo+",typecode="+typecode+",wareType="+wareType+",defectQuantity="+defectQuantity+",batchNumber="+batchNumber;
	var itemValue = currentRow.find('#selectedItemValue').val(itemValueStr);
	
	//计算差值
	discrepancyCaculation(receivableQuantity,stockInQuantity,currentRow);
}
function formOnSubmit(){
	var trs = $("#detail").find('tr');
	var success = true;
	trs.each(function(){
		var receivableQuantity = $(this).find('#receivableQuantity').val();//应收
		var stockInQuantity = $(this).find('#stockInQuantity').val();//实收
		var defectQuantity = $(this).find('#defectQuantity').val();//残次品
		var productionDate = $(this).find('#productionDate').val();//生产日期
		var expirationDate = $(this).find('#expirationDate').val();//保质期至
		var guaranteePeriod = $(this).find('#guaranteePeriod').val();//保质期
		if(typeof(receivableQuantity)!="undefined" && typeof(stockInQuantity)!="undefined" && typeof(defectQuantity)!="undefined" 
			&& typeof(productionDate)!="undefined" && typeof(expirationDate)!="undefined" && typeof(guaranteePeriod)!="undefined"){
			//三个数都必须大于0
			if(parseFloat(receivableQuantity)<0){
				success = false;
				alert("应收数量不能小于0");
			}
			if(parseFloat(stockInQuantity)<0){
				success = false;
				alert("实收数量不能小于0");
			}
			if(parseFloat(defectQuantity)<0){
				success = false;
				alert("残次品数量不能小于0");
			}
			//残次品不能大于实收
			var deiierent = parseFloat(stockInQuantity)-parseFloat(defectQuantity);
			if(deiierent<0){
				var itemName = $(this).find('span').html();
				success = false;
				alert(itemName+" 的[残次品]不能大于[实收数量]");
			}
			//保质期必须大于0
			var period = parseFloat(guaranteePeriod);
			if(period<=0){
				var itemName = $(this).find('span').html();
				success = false;
				alert(itemName+"保质期必须大于0");
			}
			//如果保质期至不为空，验证生产日期要早于保质期至
			if(expirationDate.trim()!=""){
				var expirationDateStamp = Date.parse(new Date(expirationDate));
				var productionDateStamp = Date.parse(new Date(productionDate));
				if(productionDateStamp>expirationDateStamp){
					var itemName = $(this).find('span').html();
					success = false;
					alert(itemName+" 的[生产日期]不能晚于[有效期至]");
				}
			}
		}else{
			return true;
		}
	});
	if(!success){
		return success;
	}
	
	if(checkForm('formData')){
		//验证通过后，日期选择框补0
		var stockInTime = $("#stockInTime").val();
		if(stockInTime != "" && stockInTime.indexOf("00:00:00") == -1){
			$("#stockInTime").val(stockInTime  + " 00:00:00");
		}
		return true;
	}else{
		return false;
	}
}

</script>
</html>