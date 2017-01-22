(function(global,$){	
	$.fn.addRow=function(options){
		if((typeof(options) == "undefined")||options=="") return false;
		
		var tr = $('<tr></tr>');
		for(var i=0;i<options.length;i++){
			if(options[i]=="null"||options[i]==""){
				tr.append($('<td></td>'));
			}else{
				var td=$('<td>'+options[i]+'</td>');
				tr.append(td);
			}
		}
		$(this).append(tr);
		return tr;
	}
})(window,$);