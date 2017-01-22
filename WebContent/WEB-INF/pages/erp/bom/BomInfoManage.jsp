<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.lpmas.framework.config.*"%>
<%@ page import="com.lpmas.framework.util.*"%>
<%@ page import="com.lpmas.framework.bean.StatusBean"%>
<%@ page import="com.lpmas.admin.bean.*"%>
<%@ page import="com.lpmas.admin.business.*"%>
<%@ page import="com.lpmas.framework.web.*"%>
<%@ page import="com.lpmas.constant.info.*"%>
<%@ page import="com.lpmas.erp.bom.bean.*"%>
<%@ page import="com.lpmas.erp.bom.config.*"%>
<%@ page import="com.lpmas.pdm.bean.*"%>
<%@ page import="com.lpmas.constant.info.InfoTypeConfig" %>
<%@ include file="../../include/header.jsp"%>
<%
    BomInfoBean bean = (BomInfoBean)request.getAttribute("BomInfoBean");
	String readOnly = ParamKit.getParameter(request, "readOnly","false").trim();
	boolean isReadOnly = readOnly.equals("true")? true:false;
	int bomId = ParamKit.getIntParameter(request, "bomId", 0);
	request.setAttribute("readOnly", readOnly);
	List<BomItemEntityBean> bomItemEntityProductList = (List<BomItemEntityBean>)request.getAttribute("BomItemEntityProductList");
	List<BomItemEntityBean> bomItemEntityMaterialList = (List<BomItemEntityBean>)request.getAttribute("BomItemEntityMaterialList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>BOM管理</title>
<link href="<%=STATIC_URL%>/css/main.css" type="text/css"
	rel="stylesheet" />
<script type="text/javascript" src="<%=STATIC_URL%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=STATIC_URL%>/js/common.js"></script>
<script type="text/javascript" src="<%=STATIC_URL%>/js/ui.js"></script>
<script type="text/javascript" src="../js/erp-common.js"></script>
<script type="text/javascript" src="../js/rows-jq.js"></script>
<script type='text/javascript' src="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.js"></script>
<link rel="stylesheet" href="<%=STATIC_URL %>/js/fancyBox/jquery.fancybox.css" type="text/css" media="screen" />
<script type="text/javascript"
	src="<%=STATIC_URL%>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
        document.domain='<%=DOMAIN%>'; 
</script>
</head>
<body class="article_bg">
	<div class="article_tit">
		<a href="javascript:history.back()" ><img src="<%=STATIC_URL %>/images/back_forward.jpg"/></a> 
		<ul class="art_nav">
			<li><a href="BomInfoList.do">BOM列表</a>&nbsp;>&nbsp;</li>
			<%if(bean.getBomId()==0){ %>
			<li>新建BOM</li>
			<%}else{ %>
			<li>修改BOM&nbsp;>&nbsp;</li>
			<li><%=bean.getBomName() %></li>
			<%} %>
		</ul>
	</div> 
	<form id="formData" name="formData" method="post" action="BomInfoManage.do?bomId=<%=bomId%>" onsubmit="javascript:return formOnSubmit();">
	<input type="hidden" name="bomId" id="bomId" value="<%=bean.getBomId() %>"/>
	 <input type="hidden"  name="status" id="status" value="<%=bean.getStatus()%>"/>
	  <div class="article_subtit">基础信息</div>
	  <div class="modify_form">
	  <p>
  			<em class="int_label">BOM类型：</em>
  			<input type="hidden"  name="bomType" id="bomType" value="<%=bean.getBomType()%>"/>
  			<%=MapKit.getValueFromMap(bean.getBomType(), BomConfig.BOM_TYPE_MAP)%>
  		</p>
  		<p>
  			<em class="int_label">BOM编码：</em>
  			<input type="text"  name="bomNumber" id="bomNumber" value="<%=bean.getBomNumber()%>"/>
  		</p>
  		<p>
  			<em class="int_label"><span>*</span>BOM名称：</em>
  			<input type="text"  name="bomName" id="bomName" value="<%=bean.getBomName()%>" checkStr="BOM名称;txt;true;;100"/>
  		</p>
  		<p>
  			<em class="int_label"><span>*</span>开始时间：</em>
            <input type="text" name="useStartTime" id="useStartTime"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'useEndTime\');}'})"
				value="<%=DateKit.formatTimestamp(bean.getUseStartTime(), DateKit.DEFAULT_DATE_FORMAT) == null ? "" : DateKit.formatTimestamp(bean.getUseStartTime(), DateKit.DEFAULT_DATE_FORMAT)%>"
				size="20" checkStr="开始时间;date;true;;100" /> 
  		</p>
  		<p>
  			<em class="int_label"><span>*</span>结束时间：</em>
            <input type="text" name="useEndTime" id="useEndTime"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'useStartTime\');}'})"
				value="<%=DateKit.formatTimestamp(bean.getUseEndTime(), DateKit.DEFAULT_DATE_FORMAT) == null ? "" : DateKit.formatTimestamp(bean.getUseEndTime(), DateKit.DEFAULT_DATE_FORMAT)%>"
				size="20" checkStr="结束时间;date;true;;100" />
  		</p>
  		<p>
  			<em class="int_label">激活状态：</em>
  			<input type="checkbox" name="isActive" id="isActive" value="<%=Constants.STATUS_VALID %>" <%=(bean.getIsActive()==Constants.STATUS_VALID)?"checked":"" %>/>
  		</p>
		</div>
		<div class="article_subtit">BOM明细--商品项</div>
	  <div id="product_item">
	  <table id="detailProduct" width="100%" border="0" cellpadding="0" class="table_style">
		<tr>
			<th width="5%"><input type="checkbox" id="selectAll" onchange="checkAll(this)">选择</th>
			<th width="20%">商品项编码</th>
			<th width="25%">商品项名称</th>
			<th width="20%">类型</th>
			<th width="10%">规格</th>
			<th width="10%">单位</th>
			<th width="10%">用量</th>
		</tr>
		<%for(BomItemEntityBean bomItemEntityBean : bomItemEntityProductList){ %>
			<tr>
				<td><input type="checkbox" name="select"></td>
				<td><%=bomItemEntityBean.getWareNumber()%></td>
				<td><%=bomItemEntityBean.getWareName()%></td>
				<td><select id="usageType" name="usageType" onchange="itemModifyOnChange(this)">
				<%for (StatusBean<Integer, String> typeBean : BomConfig.BOM_USAGE_TYPE_LIST){%>
				<option value="<%=typeBean.getStatus()%>"<%=bomItemEntityBean.getUsageType() == (Integer) typeBean.getStatus()? "selected": ""%>><%=typeBean.getValue()%></option>
				<%}%></select></td>
				<td><%=bomItemEntityBean.getSpecification()%></td>
				<td><input type="hidden" id="unit" value="<%=bomItemEntityBean.getUnit()%>"><%=bomItemEntityBean.getUnitName() %></td>
				<td><input type="text" size="10" maxlength="10" onkeyup='this.value=this.value.replace(/[^\-?\d.]/g,"")' checkStr="用量;num;true;;100" name="quatity_<%=bomItemEntityBean.getBomItemId() %>" id="quatity" value="<%=bomItemEntityBean.getQuantity() %>" onchange="itemModifyOnChange(this)"></td>
				<td><input type="hidden" name="itemId" value="<%=bomItemEntityBean.getBomItemId() %>"></td>
				<td><input type="hidden" id="wareType" name="wareType_<%=bomItemEntityBean.getBomItemId() %>" value="<%=bomItemEntityBean.getWareType() %>"></td>
				<td><input type="hidden" id="wareId" name="selectedProductItem" value="<%=bomItemEntityBean.getWareId() %>"></td>
				<td><input type="hidden" id="selectedItemValue" name="selectedItemValue"  value="<%=bomItemEntityBean.getItemValue()%>"></td>
			</tr>
		<%} %>
	</table>
	<%if(!isReadOnly){ %>
	<input type="button" name="button" id="add" class="modifysubbtn" onclick="addRow(<%=InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM %>)" value="添加" />
	<input type="button" name="button" id="delete" class="modifysubbtn" onclick="delRow(<%=InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM %>)" value="删除" />
	<%} %>	
	  </div>	
	  <div class="article_subtit">BOM明细--物料</div>
	  <div id="material_info">
	    <table id="detailMaterial" width="100%" border="0" cellpadding="0" class="table_style">
		<tr>
			<th width="5%"><input type="checkbox" id="selectAllMaterial" onchange="checkAllMaterial(this)">选择</th>
			<th width="20%">物料编码</th>
			<th width="25%">物料名称</th>
			<th width="20%">类型</th>
			<th width="10%">规格</th>
			<th width="10%">单位</th>
			<th width="10%">用量</th>
		</tr>
		<%for(BomItemEntityBean bomItemEntityBean : bomItemEntityMaterialList){ %>
			<tr>
				<td><input type="checkbox" name="selectMaterial"></td>
				<td><%=bomItemEntityBean.getWareNumber()%></td>
				<td><%=bomItemEntityBean.getWareName()%></td>
				<td><select id="usageType" name="usageType" onchange="itemModifyOnChange(this)">
				<%for (StatusBean<Integer, String> typeBean : BomConfig.BOM_USAGE_TYPE_LIST){%>
				<option value="<%=typeBean.getStatus()%>"<%=bomItemEntityBean.getUsageType() == (Integer) typeBean.getStatus()? "selected": ""%>><%=typeBean.getValue()%></option>
				<%}%></select></td>
				<td><%=bomItemEntityBean.getSpecification()%></td>
				<td><input type="hidden" id="unit" value="<%=bomItemEntityBean.getUnit()%>"><%=bomItemEntityBean.getUnitName() %></td>
				<td><input type="text" size="10" maxlength="10" onkeyup='this.value=this.value.replace(/[^\-?\d.]/g,"")' checkStr="用量;num;true;;100" name="quatity_<%=bomItemEntityBean.getBomItemId() %>" id="quatity" value="<%=bomItemEntityBean.getQuantity() %>" onchange="itemModifyOnChange(this)"></td>
				<td><input type="hidden" name="itemId" value="<%=bomItemEntityBean.getBomItemId() %>"></td>
				<td><input type="hidden" id="wareType" name="wareType_<%=bomItemEntityBean.getBomItemId() %>" value="<%=bomItemEntityBean.getWareType() %>"></td>
				<td><input type="hidden" id="wareId" name="selectedMaterial" value="<%=bomItemEntityBean.getWareId() %>"></td>
				<td><input type="hidden" id="selectedItemValue" name="selectedItemValue"  value="<%=bomItemEntityBean.getItemValue()%>"></td>
			</tr>
		<%} %>
	</table>
	<%if(!isReadOnly){ %>
	<input type="button" name="button" id="add" class="modifysubbtn" onclick="addRow(<%=InfoTypeConfig.INFO_TYPE_MATERIAL %>)" value="添加" />
	<input type="button" name="button" id="delete" class="modifysubbtn" onclick="delRow(<%=InfoTypeConfig.INFO_TYPE_MATERIAL %>)" value="删除" />
	<%} %>
	  </div>
	   <div class="div_center">
	  <input type="submit" name="submit" id="submit" class="modifysubbtn" value="提交" />
	  <a href="BomInfoList.do"><input type="button" name="cancel" id="cancel" value="取消" ></a>
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
function formOnSubmit(){
	//先判断是新建还是修改
	 var bomId = '<%=bomId%>';
	 if(bomId=='0'){
		 //新建
		 //规则是必须有新建的采购ITEM
		 var items = $('#detailProduct').find('input[name=selectedProductItem]');
		 var items2 = $('#detailMaterial').find('input[name=selectedMaterial]');
		 if(items.length<=0 && items2.length<=0){
			 alert('新建BOM必须填写BOM明细!');
			 return false;
		 }
	 }else{
		 //修改
		 //规则是如果没有新增，原来的ITEM就不能完全删除
		 var orgitems = $('#detailProduct').find('input[name=selectedProductItem]');
		 var newitems = $('#detailProduct').find('input[name=itemId]');
		 var orgitems2 = $('#detailMaterial').find('input[name=selectedMaterial]');
		 var newitems2 = $('#detailMaterial').find('input[name=itemId]');
		 //不能两个都等于0
		 if(orgitems.length<=0&&newitems.length<=0&&orgitems2.length<=0&&newitems2.length<=0){
			 alert('不能再没有新建明细的情况下,完全删除原有明细!');
			 return false;
		 }
	 }
	 //验证是不是有相同的WAREID
	 var newItem = $('input[name=selectedProductItem]');
	 var tempArray = new Array();
	 if(newItem.length>0){
		 newItem.each(function(){
			 if($(this).val().trim()!="")
			 tempArray.push($(this).val());
		 });
	 }
	 //数组排序
	 tempArray=tempArray.sort();
	 for(var i = 0;i<tempArray.length;i++){
		 if((i+1)<tempArray.length){
			 if(tempArray[i]==tempArray[i+1]){
				 alert("不能选择重复的商品项!请删除后再提交");
				 return false;
			 }
		 }
	 }
	 var newItem2 = $('input[name=selectedMaterial]');
	 var tempArray2 = new Array();
	 if(newItem2.length>0){
		 newItem2.each(function(){
			 if($(this).val().trim()!="")
			 tempArray2.push($(this).val());
		 });
	 }
	 //数组排序
	 tempArray2=tempArray2.sort();
	 for(var i = 0;i<tempArray2.length;i++){
		 if((i+1)<tempArray2.length){
			 if(tempArray2[i]==tempArray2[i+1]){
				 alert("不能选择重复的物料!请删除后再提交");
				 return false;
			 }
		 }
	 }
	//先恢复table中的DISABLED
	 var disabledDom = $('#detailProduct').find('input[disabled]');
	 disabledDom.removeAttr('disabled');
	 var disabledDom2 = $('#detailMaterial').find('input[disabled]');
	 disabledDom2.removeAttr('disabled');
	if(checkForm('formData')){
		//验证通过后，日期选择框补0
		var useStartTime = $("#useStartTime").val();
		if(useStartTime != "" && useStartTime.indexOf("00:00:00") == -1){
			$("#useStartTime").val(useStartTime  + " 00:00:00");
		}
		var useEndTime = $("#useEndTime").val();
		if(useEndTime != "" && useEndTime.indexOf("23:59:59") == -1){
			$("#useEndTime").val(useEndTime  + " 23:59:59");
		}
		return true;
	}else{
		//验证不通过要把DISABLED恢复
		disabledDom.attr('disabled');
		disabledDom2.attr('disabled');
		return false;
	}
}
function checkAllMaterial(ele){
	if($(ele).is(':checked')){
		$('input[name=selectMaterial][selected!=selected]').attr("checked",'checked');
	}else{
		$('input[name=selectMaterial]').removeAttr("checked");
	}
}
function addRow(type){
	var selectedItem;
	var actionUrl;
	if(type == '<%=InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM %>'){
		selectedItem = $('tr').find('input[name=selectedProductItem]');
		actionUrl = "<%=PDM_URL%>/pdm/ProductItemSelect.do";
	}else if(type == '<%=InfoTypeConfig.INFO_TYPE_MATERIAL %>'){
		selectedItem = $('tr').find('input[name=selectedMaterial]');
		actionUrl = "<%=PDM_URL%>/pdm/MaterialInfoSelect.do";
	}
		var selectItemStr="";
		var bomId = '<%=bomId%>';		
		selectedItem.each(function(){
			selectItemStr+=$(this).val()+",";
		});
		selectItemStr = selectItemStr.substring(0,selectItemStr.length-1);
		$.fancybox.open({
			href : actionUrl+"?selectedItem="+selectItemStr+"&bomId="+bomId+'&openOrPop=pop',
			type : 'iframe',
			width : 650,
			minHeight : 500
	}); 
}
function delRow(type){	
	if(type == '<%=InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM %>'){
		var checkBoxs = $('input[name=select]');
		checkBoxs.each(function(e){
			if($(this).is(':checked')){
				$(this).parent().parent().remove();
			}
		});
		$('#selectAll').removeAttr("checked");
	}else if(type == '<%=InfoTypeConfig.INFO_TYPE_MATERIAL %>'){
		var checkBoxs = $('input[name=selectMaterial]');
		checkBoxs.each(function(e){
			if($(this).is(':checked')){
				$(this).parent().parent().remove();
			}
		});
		$('#selectAllMaterial').removeAttr("checked");
	}	
}
function callbackFun(selectItems){	
	var table;
	//插入行
	for(var i=0;i<selectItems.length;i++){
		if(selectItems[i]!=""){
			var itemContent = jQuery.parseJSON(selectItems[i]);
			if(itemContent.wareType=='<%=InfoTypeConfig.INFO_TYPE_PRODUCT_ITEM %>'){
				table = $('#detailProduct');
				var trs = $('#detailProduct').find('tr');
				table.addRow([
					  			'<input type="checkbox" name="select">',
					  			itemContent.itemNumber,
					  			itemContent.itemName,
					  			'<select id="usageType" name="usageType"><%for (StatusBean<Integer, String> typeBean : BomConfig.BOM_USAGE_TYPE_LIST){%><option value="<%=typeBean.getStatus()%>"><%=typeBean.getValue()%></option><%}%></select>',
					  			itemContent.itemSpecification,
					  			'<input type="hidden" name="unit" id="unit" value="'+itemContent.itemUnit+'"><span>'+itemContent.itemUnitName+'</span>',
					  			'<input type="text" size="10" maxlength="10"  checkStr="用量;num;true;;100"  id="quatity" onchange="itemModifyOnChange(this)" onkeyup="this.value=this.value.replace(/[^\\-?\\d.]/g,&quot&quot)" value="">',
					  			'<input type="hidden" id="wareId" name="selectedProductItem" value="'+itemContent.itemId+'">',
					  			'<input type="hidden" id="wareType" name="wareType" value="'+itemContent.wareType+'">',
					  			'<input type="hidden" checkStr="BOM明细值;txt;true;;1000" id="selectedItemValue" name="selectedItemValue"  value="">',
					  			
					  		]);
			}
			if(itemContent.wareType=='<%=InfoTypeConfig.INFO_TYPE_MATERIAL %>'){
			   table = $('#detailMaterial');	
			   var trs = $('#detailMaterial').find('tr');
				table.addRow([
					  			'<input type="checkbox" name="selectMaterial">',
					  			itemContent.itemNumber,
					  			itemContent.itemName,
					  			'<select id="usageType" name="usageType"><%for (StatusBean<Integer, String> typeBean : BomConfig.BOM_USAGE_TYPE_LIST){%><option value="<%=typeBean.getStatus()%>"><%=typeBean.getValue()%></option><%}%></select>',
					  			itemContent.itemSpecification,
					  			'<input type="hidden" name="unit" id="unit" value="'+itemContent.itemUnit+'"><span>'+itemContent.itemUnitName+'</span>',
					  			'<input type="text" size="10" maxlength="10"  checkStr="用量;num;true;;100"  id="quatity" onchange="itemModifyOnChange(this)" onkeyup="this.value=this.value.replace(/[^\\-?\\d.]/g,&quot&quot)" value="">',
					  			'<input type="hidden" id="wareId" name="selectedMaterial" value="'+itemContent.itemId+'">',
					  			'<input type="hidden" id="wareType" name="wareType" value="'+itemContent.wareType+'">',
					  			'<input type="hidden" checkStr="BOM明细值;txt;true;;1000" id="selectedItemValue" name="selectedItemValue"  value="">',
					  			
					  		]);
			}
		}
	}
}
function itemModifyOnChange(ele){
	var currentRow = $(ele).parent().parent();
	var tds = currentRow.find('td');
	var typename = $(tds[1]).html();
	var typecode = $(tds[2]).html();
	var usageType = currentRow.find('#usageType').val();
	var unit = currentRow.find('#unit').val();
	var quatity = currentRow.find('#quatity').val();
	var wareId = currentRow.find('#wareId').val();
	var wareType = currentRow.find('#wareType').val();
	var itemValueStr = "wareId="+wareId+",wareType="+wareType+",unit="+unit+",usageType="+usageType+",quatity="+quatity+",typecode="+typecode;
	var itemValue = currentRow.find('#selectedItemValue').val(itemValueStr);	
}
</script>
</html>