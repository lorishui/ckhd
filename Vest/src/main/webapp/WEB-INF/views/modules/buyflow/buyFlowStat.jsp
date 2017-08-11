<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>网游买量统计</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
//@ sourceURL=list.js 
	$(document).ready(function() {
		showCol();
		$('#searchBox').bind('input', function () {//给文本框绑定input事件
			var data = $(this).val();
			var $rows = $('.rowss').show();
			if( data ) {
	            $rows.find('.buyflowName').not(':contains(' + data + ')').closest('.rowss').hide();
			}
			reCount()
		});
		 $('#searchBox').bind('blur', function () {
			 $("#searchName").show();
		     $("#searchBox").hide();
         });
	});
	
	function dayMediaExport(date,media){
		if(confirm("确认下载报表")){
			var $body = $('body');
	        var $form = $("<form>");
	            $form.attr("action","${ctx}/buyflow/stat/dayMediaExport");
	            var input1 = $('<input>'); 
	            input1.attr('value',date); 
	            input1.attr('name','statsDate'); 
	            $form.append(input1);
	            
	            var input2 = $('<input>'); 
	            input2.attr('value',media); 
	            input2.attr('name','media'); 
	            $form.append(input2);
	            $form.appendTo($body);
	            $form.submit();
	            $form.remove();
	        return false;
		}
	}
	
	function showSearchBox(){
		$("#searchName").hide();
		$("#searchBox").show().focus();
	}
	
	function showCol(){
		var a = $("#colButton").attr("checked")
		var cols =  $(".col");
		for(var i = 0, len = cols.length; i < len; i++){
            var cell = cols[i];
            if("checked" == a){
                cell.style.display = '';
            }else{
                cell.style.display = 'none';
            }
        }
	}
	
	function showRow(){
		var a = $("#rowButton").attr("checked")
        var rows =  $(".row");
        for(var i = 0, len = rows.length; i < len; i++){
        	if(0 == rows[i].innerText){
        		var row = rows[i];
        		if("checked" == a){
	        		row.parentNode.style.display = '';
        		}else{
        			row.parentNode.style.display = 'none';
        		}
        	}
        }
	}
	
	function reCount(){
		var clickNumList = $(".rowss:visible").find($(".clickNum"));
		var clickNum = 0;
		var deviceClickNumList = $(".rowss:visible").find($(".deviceClickNum"));
		var deviceClickNum = 0;
		var activeNumList = $(".rowss:visible").find($(".activeNum"));
		var activeNum = 0;
		var registerNumList = $(".rowss:visible").find($(".registerNum"));
		var registerNum = 0;
		var retain0List = $(".rowss:visible").find($(".retain0"));
		var retain0 = 0;
		var retain1List = $(".rowss:visible").find($(".retain1"));
		var retain1 = 0;
		var retain3List = $(".rowss:visible").find($(".retain3"));
		var retain3 = 0;
		var retain7List = $(".rowss:visible").find($(".retain7"));
		var retain7 = 0;
		var retain30List = $(".rowss:visible").find($(".retain30"));
		var retain30 = 0;
		var retainMoney0List = $(".rowss:visible").find($(".retainMoney0"));
		var retainMoney0 = 0;
		var retainMoney1List = $(".rowss:visible").find($(".retainMoney1"));
		var retainMoney1 = 0;
		var retainMoney7List = $(".rowss:visible").find($(".retainMoney7"));
		var retainMoney7 = 0;
		var retainMoney30List = $(".rowss:visible").find($(".retainMoney30"));
		var retainMoney30 = 0;
		var retainMoney60List = $(".rowss:visible").find($(".retainMoney60"));
		var retainMoney60 = 0;
		var totalMoneyList = $(".rowss:visible").find($(".totalMoney"));
		var totalMoney = 0;
		var activeDeviceNumList = $(".rowss:visible").find($(".activeDeviceNum"));
		var activeDeviceNum = 0;
		var totalDeviceNumList = $(".rowss:visible").find($(".totalDeviceNum"));
		var totalDeviceNum = 0;
		for (var i= 0; i < clickNumList.length; i++) {
			var clickNum = clickNum + parseInt(clickNumList[i].value);
			var deviceClickNum = deviceClickNum + parseInt(deviceClickNumList[i].value);
			var activeNum = activeNum + parseInt(activeNumList[i].value);
			var registerNum = registerNum + parseInt(registerNumList[i].value);
			var retain0 = retain0 + parseInt(retain0List[i].value);
			var retain1 = retain1 + parseInt(retain1List[i].value);
			var retain3 = retain3 + parseInt(retain3List[i].value);
			var retain7 = retain7 + parseInt(retain7List[i].value);
			var retain30 = retain30 + parseInt(retain30List[i].value);
			var retainMoney0 = retainMoney0 + parseInt(retainMoney0List[i].value);
			var retainMoney1 = retainMoney1 + parseInt(retainMoney1List[i].value);
			var retainMoney7 = retainMoney7 + parseInt(retainMoney7List[i].value);
			var retainMoney30 = retainMoney30 + parseInt(retainMoney30List[i].value);
			var retainMoney60 = retainMoney60 + parseInt(retainMoney60List[i].value);
			var totalMoney = totalMoney + parseInt(totalMoneyList[i].value);
			var activeDeviceNum = activeDeviceNum + parseInt(activeDeviceNumList[i].value);
			var totalDeviceNum = totalDeviceNum + parseInt(totalDeviceNumList[i].value);
		}
		var activeNumRate=0,registerNumRate=0;
		if(clickNum != 0){
			if(activeNum != 0){
			    activeNumRate = (activeNum*100/clickNum).toFixed(2) + "%";
			}
			if(registerNum != 0){
			    registerNumRate = (registerNum*100/activeNum).toFixed(2) + "%";
			}
		}
		$(".ttclickNum")[0].innerText = clickNum;
		$(".ttdeviceClickNum")[0].innerText = deviceClickNum;
		$(".ttactiveNum")[0].innerText = activeNum;
		$(".ttactiveNumRate")[0].innerText = activeNumRate;
		$(".ttregisterNum")[0].innerText = registerNum;
		$(".ttregisterNumRate")[0].innerText = registerNumRate;
		var re1=0,re3=0,re7=0,re30=0,ltv0=0,ltv1=0,ltv7=0,ltv30=0,ltv60=0;
		if(retain0 != 0){
			re1 = (retain1*100/retain0).toFixed(2) + "%";
			re3 = (retain3*100/retain0).toFixed(2) + "%";
			re7 = (retain7*100/retain0).toFixed(2) + "%";
			re30 = (retain30*100/retain0).toFixed(2) + "%";
			ltv0 = (retainMoney0*0.01/retain0).toFixed(2);
			ltv1 = (retainMoney1*0.01/retain0).toFixed(2);
			ltv7 = (retainMoney7*0.01/retain0).toFixed(2);
			ltv30 = (retainMoney30*0.01/retain0).toFixed(2);
			ltv60 = (retainMoney60*0.01/retain0).toFixed(2);
		}
		$(".ttretain1")[0].innerText = re1;
		$(".ttretain3")[0].innerText = re3;
		$(".ttretain7")[0].innerText = re7;
		$(".ttretain30")[0].innerText = re30;
		$(".ttltv0")[0].innerText = ltv0;
		$(".ttltv1")[0].innerText = ltv1;
		$(".ttltv7")[0].innerText = ltv7;
		$(".ttltv30")[0].innerText = ltv30;
		$(".ttltv60")[0].innerText = ltv60;
		$(".ttretainMoney60")[0].innerText = (retainMoney60*0.01).toFixed(2);
		$(".ttretainMoney0")[0].innerText = (retainMoney0*0.01).toFixed(2);
		$(".tttotalMoney")[0].innerText = (totalMoney*0.01).toFixed(2);
		$(".ttactiveDeviceNum")[0].innerText = activeDeviceNum;
		$(".tttotalDeviceNum")[0].innerText = totalDeviceNum;
		
	}
	
	function selectDateFn(val) {
		var startDate = $("#startDate");
		var endDate = $("#endDate");
		if (val == 0) {
			var day = GetDateStr(0);
			startDate.val(day);
			endDate.val(day);
		} else if (val == 1) {
			var day = GetDateStr(-1);
			startDate.val(day);
			endDate.val(day);
		} else if (val == 7) {
			startDate.val(GetDateStr(-6));
			endDate.val(GetDateStr(0));
		} else if (val == 30) {
			startDate.val(GetDateStr(-29));
			endDate.val(GetDateStr(0));
		}
		if (val >= 0) {
			$("#searchForm").submit();
		}
	}
	function GetDateStr(AddDayCount) {
		var dd = new Date();
		dd.setDate(dd.getDate() + AddDayCount);//获取AddDayCount天后的日期
		var y = dd.getFullYear();
		var m = dd.getMonth() + 1;//获取当前月份的日期
		var d = dd.getDate();
		m = m < 10 ? "0" + m : m;
		d = d < 10 ? "0" + d : d;
		return y + "-" + m + "-" + d;
	}
	
	function detail(name,index){
		var tb = $("#secTr");
		if(tb.length > 0){
			tb.remove();
			return true;
		}
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var groupVal = $("#group").val();
		var ckappId = $("#ckappId").val();
		$.ajax({
			url : "${ctx}/buyflow/stat/data",
			type : 'post',
			data : "startDate="+startDate+"&endDate="+endDate+"&activityId="+name+"&media="+name+"&group="+groupVal+"&ckappId="+ckappId,
			dataType:'json',
			error : function() {
				alert("fail")
			},
			success : function(data) {
				var node = $("#"+index);
				var thead = 
					"<tr>"
		                + "<th>日期</th>"
		                + "<th>"+ (groupVal == 1 ? "活动" : "渠道名称") + "</th>"
		                + "<th>点击总数</th>"
		                + "<th>排重点击</th>"
		                + "<th>激活设备数</th>"
		                + "<th>激活率</th>"
		                + "<th>注册设备数</th>"
		                + "<th>注册率</th> "
		                + "<th class='col'>次留</th>"
		                + "<th class='col'>3留</th>"
		                + "<th class='col'>7留</th>"
		                + "<th class='col'>30留</th>"
		                + "<th class='col'>LTV0</th>"
		                + "<th class='col'>LTV1</th>"
		                + "<th class='col'>LTV7</th>"
		                + "<th class='col'>LTV30</th>"
		                + "<th class='col'>LTV60</th>"
		                + "<th class='col'>60日留存付费</th>"
		                + "<th class='col'>新增用户付费金额</th>"
		                + "<th class='col'>总付费</th>"
		                + "<th class='col'>新增付费设备</th>"
		                + "<th class='col'>总付费设备</th>"
		            + "</tr>"
		        ;
		        var tbody = "";
		 		$.each(data,function(idx,item){
				 	if(idx<0){
				   		return true;
				 	}
				 	var itemName = "<td>"+item.name+"</td>";
				 	var itemDate = "<td>"+item.date+"</td>";
				 	if(groupVal == 0){
				 		itemName = "<td><a href='#' onclick=\"dayMediaDetail('"+item.date+"','"+item.name+"','trd"+idx+"')\">"+item.name+"</a></td>";
				 		itemDate = "<td><a href='#' onclick=\"dayMediaExport('"+item.date+"','"+item.name+"')\">"+item.date+"</a></td>";
				 	}
				 	tbody += 
				 	"<tr id='trd"+idx+"'>"
					    + itemDate
					    + itemName
					    + "<td class = 'row'>"+item.clickNum+"</td>"
					    + "<td>"+item.deviceClickNum+"</td>"
					    + "<td>"+item.activeNum+"</td>"
					    + "<td>"+item.activeRate+"</td>"
					    + "<td>"+item.registerNum+"</td>"
					    + "<td>"+item.registerRate+"</td>"
				  		+ "<td class='col'>"+item.reten1+"</td>"
				  		+ "<td class='col'>"+item.reten3+"</td>"
				  		+ "<td class='col'>"+item.reten7+"</td>"
				  		+ "<td class='col'>"+item.reten30+"</td>"
				  		+ "<td class='col'>"+item.ltv0+"</td>"
				  		+ "<td class='col'>"+item.ltv1+"</td>"
				  		+ "<td class='col'>"+item.ltv7+"</td>"
				  		+ "<td class='col'>"+item.ltv30+"</td>"
				  		+ "<td class='col'>"+item.ltv60+"</td>"
				  		+ "<td class='col'>"+item.retainTotalIncome+"</td>"
				  		+ "<td class='col'>"+item.retainIncome+"</td>"
				  		+ "<td class='col'>"+item.totalIncome+"</td>"
				  		+ "<td class='col'>"+item.payDevice+"</td>"
				  		+ "<td class='col'>"+item.totalDevice+"</td>"
				  	+"</tr>";
			  	});

                var html = 
                    "<tr id='secTr'>"
                        +  "<td colspan='24' style='border:none'>"
                            + "<div style='background:#ddddff;max-height:500px;overflow-y:auto;padding-top:5px;padding-left:3px;padding-right:3px'>"
                                + "<table class='table2excel table table-striped table-bordered table-condensed'>"
                                    + thead
                                    + tbody
                                + "</table>"
                            + "</div>"
                        +"</td>"
                    +"</tr>"
                ;
			  	node.after(html);
			  	showCol();
			  	showRow();
			}
		});
	}
	
	function mediaDetail(media,index){
		var tb = $("#secTr");
		if(tb.length > 0){
			tb.remove();
			return true;
		}
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var ckappId = $("#ckappId").val();
		$.ajax({
			url : "${ctx}/buyflow/stat/mediaData",
			type : 'post',
			data : "startDate="+startDate+"&endDate="+endDate+"&media="+media+"&ckappId="+ckappId,
			dataType:'json',
			error : function() {
				alert("fail")
			},
			success : function(data) {
				var group = "活动";
				var node = $("#"+index);
				var thead = 
					 "<tr>"
						+ "<th>"+group+"</th>"
						+ "<th>点击总数</th>"
						+ "<th>排重点击</th>"
						+ "<th>激活设备数</th>"
						+ "<th>激活率</th>"
						+ "<th>注册设备数</th>"
						+ "<th>注册率</th>"
						+ "<th class='col'>次留</th>"
                        + "<th class='col'>3留</th>"
                        + "<th class='col'>7留</th>"
                        + "<th class='col'>30留</th>"
                        + "<th class='col'>LTV0</th>"
                        + "<th class='col'>LTV1</th>"
                        + "<th class='col'>LTV7</th>"
                        + "<th class='col'>LTV30</th>"
                        + "<th class='col'>LTV60</th>"
                        + "<th class='col'>60日留存付费</th>"
                        + "<th class='col'>新增用户付费金额</th>"
                        + "<th class='col'>总付费</th>"
                        + "<th class='col'>新增付费设备</th>"
                        + "<th class='col'>总付费设备</th>"
	                +" </tr>";
	            var tbody = "";
		 		$.each(data,function(idx,item){
				 	if(idx<0){
				   		return true;//同countinue，返回false同break
				 	}
			  		tbody +=
				  		"<tr>"
					  		+ "<td>"+item.name+"</td>"
					  		+ "<td class = 'row'>"+item.clickNum+"</td>"
					  		+ "<td>"+item.deviceClickNum+"</td>"
					  		+ "<td>"+item.activeNum+"</td>"
					  		+ "<td>"+item.activeRate+"</td>"
					  		+ "<td>"+item.registerNum+"</td>"
					  		+ "<td>"+item.registerRate+"</td>"
					  		+ "<td class='col'>"+item.reten1+"</td>"
	                        + "<td class='col'>"+item.reten3+"</td>"
	                        + "<td class='col'>"+item.reten7+"</td>"
	                        + "<td class='col'>"+item.reten30+"</td>"
	                        + "<td class='col'>"+item.ltv0+"</td>"
	                        + "<td class='col'>"+item.ltv1+"</td>"
	                        + "<td class='col'>"+item.ltv7+"</td>"
	                        + "<td class='col'>"+item.ltv30+"</td>"
	                        + "<td class='col'>"+item.ltv60+"</td>"
	                        + "<td class='col'>"+item.retainTotalIncome+"</td>"
	                        + "<td class='col'>"+item.retainIncome+"</td>"
	                        + "<td class='col'>"+item.totalIncome+"</td>"
	                        + "<td class='col'>"+item.payDevice+"</td>"
	                        + "<td class='col'>"+item.totalDevice+"</td>"
				  		+ "</tr>"
			  	});
			  	html = "<tr id='secTr'>"
		                    +"<td colspan='24' style='border:none'>"
			                    +"<div style='background:#ddddff;max-height:500px;overflow-y:auto;padding-top:5px;padding-left:3px;padding-right:3px'>"
			                        + "<table class='table2excel table table-striped table-bordered table-condensed'>"
			                        +thead
			                        +tbody
			         + "</table></div></td></tr>"
			  	node.after(html);
			  	showCol();
                showRow();
			}
		});
	}
	function dayMediaDetail(date,media,index){
		var tb = $("#trdTr");
		if(tb.length > 0){
			tb.remove();
			return true;
		}
		var ckappId = $("#ckappId").val();
		$.ajax({
			url : "${ctx}/buyflow/stat/dayMediaData",
			type : 'post',
			data : "statsDate="+date+"&media="+media+"&ckappId="+ckappId,
			dataType:'json',
			error : function() {
				alert("fail")
			},
			success : function(data) {
				var group = "活动";
				var node = $("#"+index);
				var thead = 
					"<tr>"
						+ "<th>"+group+"</th>"
						+ "<th>点击总数</th>"
		                + "<th>排重点击</th>"
		                + "<th>激活设备数</th>"
		                + "<th>激活率</th>"
		                + "<th>注册设备数</th>"
		                + "<th>注册率</th>"
		                + "<th class='col'>次留</th>"
                        + "<th class='col'>3留</th>"
                        + "<th class='col'>7留</th>"
                        + "<th class='col'>30留</th>"
                        + "<th class='col'>LTV0</th>"
                        + "<th class='col'>LTV1</th>"
                        + "<th class='col'>LTV7</th>"
                        + "<th class='col'>LTV30</th>"
                        + "<th class='col'>LTV60</th>"
                        + "<th class='col'>60日留存付费</th>"
                        + "<th class='col'>新增用户付费金额</th>"
                        + "<th class='col'>总付费</th>"
                        + "<th class='col'>新增付费设备</th>"
                        + "<th class='col'>总付费设备</th>"
	                + "</tr>";
	            var tbody = "";
		 		$.each(data,function(idx,item){
				 	if(idx<0){
				   		return true;//同countinue，返回false同break
				 	}
			  		tbody += 
				  		"<tr>"
	                        + "<td>"+item.name+"</td>"
	                        + "<td class = 'row'>"+item.clickNum+"</td>"
	                        + "<td>"+item.deviceClickNum+"</td>"
	                        + "<td>"+item.activeNum+"</td>"
	                        + "<td>"+item.activeRate+"</td>"
	                        + "<td>"+item.registerNum+"</td>"
	                        + "<td>"+item.registerRate+"</td>"
	                        + "<td class='col'>"+item.reten1+"</td>"
	                        + "<td class='col'>"+item.reten3+"</td>"
	                        + "<td class='col'>"+item.reten7+"</td>"
	                        + "<td class='col'>"+item.reten30+"</td>"
	                        + "<td class='col'>"+item.ltv0+"</td>"
	                        + "<td class='col'>"+item.ltv1+"</td>"
	                        + "<td class='col'>"+item.ltv7+"</td>"
	                        + "<td class='col'>"+item.ltv30+"</td>"
	                        + "<td class='col'>"+item.ltv60+"</td>"
	                        + "<td class='col'>"+item.retainTotalIncome+"</td>"
	                        + "<td class='col'>"+item.retainIncome+"</td>"
	                        + "<td class='col'>"+item.totalIncome+"</td>"
	                        + "<td class='col'>"+item.payDevice+"</td>"
	                        + "<td class='col'>"+item.totalDevice+"</td>"
	                    + "</tr>"
			  	});
			  	html = 
			  		"<tr id='trdTr'>"
			  	      + "<td colspan='22' style='border:none'>"
			  	       + "<div style='background:#DDFFDD;max-height:500px;overflow-y:auto;padding-top:5px;padding-left:3px;padding-right:3px'>"
			  	         + "<table class='table2excel table table-striped table-bordered table-condensed' >"
			  	         +thead
			  	         +tbody
			  		+"</table></div></td></tr>";
			  	node.after(html);
			  	showCol();
                showRow();
			}
		});
	}
	
	function dayDetail(date,index){
		var tb = $("#secTr");
		if(tb.length > 0){
			tb.remove();
			return true;
		}
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var groupVal = $("#group").val();
		var ckappId = $("#ckappId").val();
		$.ajax({
			url : "${ctx}/buyflow/stat/dayData",
			type : 'post',
			data : "startDate="+startDate+"&endDate="+endDate+"&statsDate="+date+"&group="+groupVal+"&ckappId="+ckappId,
			dataType:'json',
			error : function() {
				alert("fail")
			},
			success : function(data) {
				var node = $("#"+index);
				var thead = 
					"<tr>"
	                    + "<th>"+(groupVal == 0 ? "渠道名称" : "活动")+"</th>"
	                    + "<th>点击总数</th>"
	                    + "<th>排重点击</th>"
	                    + "<th>激活设备数</th>"
	                    + "<th>激活率</th>"
	                    + "<th>注册设备数</th>"
	                    + "<th>注册率</th>"
	                    + "<th class='col'>次留</th>"
                        + "<th class='col'>3留</th>"
                        + "<th class='col'>7留</th>"
                        + "<th class='col'>30留</th>"
                        + "<th class='col'>LTV0</th>"
                        + "<th class='col'>LTV1</th>"
                        + "<th class='col'>LTV7</th>"
                        + "<th class='col'>LTV30</th>"
                        + "<th class='col'>LTV60</th>"
                        + "<th class='col'>60日留存付费</th>"
                        + "<th class='col'>新增用户付费金额</th>"
                        + "<th class='col'>总付费</th>"
                        + "<th class='col'>新增付费设备</th>"
                        + "<th class='col'>总付费设备</th>"
	                +" </tr>";
	            var tbody = "";
		 		$.each(data,function(idx,item){
				 	if(idx<0){
				   		return true;//同countinue，返回false同break
				 	}
				 	var itemName = "<td>"+item.name+"</td>";
				 	if(groupVal == 0){
				 		itemName = 
				 			"<td>"
						 		+ "<a href='#' onclick=\"dayMediaDetail('"+item.date+"','"+item.name+"','trd"+idx+"')\">"
						 	      + item.name
						 		+ "</a>"
					 		+ "</td>"
				 	}
			  		tbody += 
			  			"<tr id='trd"+idx+"'>"
							+ itemName
							+ "<td class = 'row'>"+item.clickNum+"</td>"
				            + "<td>"+item.deviceClickNum+"</td>"
				            + "<td>"+item.activeNum+"</td>"
				            + "<td>"+item.activeRate+"</td>"
				            + "<td>"+item.registerNum+"</td>"
				            + "<td>"+item.registerRate+"</td>"
				            + "<td class='col'>"+item.reten1+"</td>"
	                        + "<td class='col'>"+item.reten3+"</td>"
	                        + "<td class='col'>"+item.reten7+"</td>"
	                        + "<td class='col'>"+item.reten30+"</td>"
	                        + "<td class='col'>"+item.ltv0+"</td>"
	                        + "<td class='col'>"+item.ltv1+"</td>"
	                        + "<td class='col'>"+item.ltv7+"</td>"
	                        + "<td class='col'>"+item.ltv30+"</td>"
	                        + "<td class='col'>"+item.ltv60+"</td>"
	                        + "<td class='col'>"+item.retainTotalIncome+"</td>"
	                        + "<td class='col'>"+item.retainIncome+"</td>"
	                        + "<td class='col'>"+item.totalIncome+"</td>"
	                        + "<td class='col'>"+item.payDevice+"</td>"
	                        + "<td class='col'>"+item.totalDevice+"</td>"
		                 + "</tr>"
			  	});
			  	html = "<tr id='secTr'>"
			  	         + "<td colspan='24' style='border:none'>"
			  	           + "<div style='background:#ddddff;max-height:500px;overflow-y:auto;padding-top:5px;padding-left:3px;padding-right:3px'>"
			  	             +"<table class='table2excel table table-striped table-bordered table-condensed'>"
			  	         + thead
			  	         + tbody
			  		+"</table></div></td></tr>";
			  	node.after(html);
			  	showCol();
                showRow();
			}
		});
	}
	
</script>
</head>
<body>
	<div id="position">设备留存统计信息</div>
	<form:form id="searchForm" modelAttribute="buyFlowStat"
		action="${ctx}/buyflow/stat/list" method="post" class="breadcrumb form-search">
        
		<div>
			<label>统计时间精度选择：</label>
			<input type="radio" name="groupByDay" value="0" ${buyFlowStat.groupByDay == 0 ?'checked="checked"':'' }>汇总数据
			<input type="radio" name="groupByDay" value="1" ${buyFlowStat.groupByDay == 1 ?'checked="checked"':''}>按天显示数据
		
			<label>游戏：</label>
            <form:select id="ckappId" path="ckappId" class="input-medium">
                <form:options items="${fns:getAPPCkListByChild()}" itemLabel="name" itemValue="ckappId" htmlEscape="false" />
            </form:select>
			<label>维度：</label>
			<form:select id="group" path="group" class="input-medium">
				<form:option value="1" label="活动名称" />
				<form:option value="0" label="渠道" />
			</form:select>
			<shiro:hasPermission name="buyflow:stat:retain">
				&nbsp;&nbsp;&nbsp;
				<input type="checkbox" id="colButton" value="1" name="showRetain" ${buyFlowStat.showRetain == 1 ? 'checked="checked"' : '' } onclick="showCol()"><label>留存相关</label>
			</shiro:hasPermission>
			&nbsp;&nbsp;&nbsp;
			<input type="checkbox" id="rowButton" value="1" name="show0Data" ${buyFlowStat.show0Data == 1 ? 'checked="checked"' : '' } onclick="showRow()"><label>显示全0数据</label>
		</div>
		<br>
		<div>
			<label>时间范围：</label> <a href="#" onclick="javascript:selectDateFn(0);void(0)">今天</a> 
			<a href="#" onclick="javascript:selectDateFn(1);void(0)">昨天</a>
			<a href="#" onclick="javascript:selectDateFn(7);void(0)">近七天</a>
			<a href="#" onclick="javascript:selectDateFn(30);void(0)">近30天</a> 
			<span><font color="red">*</font> </span> 
			<label>统计日期：</label> 
			<span><font color="red">*</font> </span> 
			<input id="startDate" name="startDate" type="text" readonly="readonly" maxlength="20"
				value="${buyFlowStat.startDate}" class="WdateTime required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />&nbsp;&nbsp;
			<span><font color="red">*</font> </span> 
			<input id="endDate" name="endDate" type="text" readonly="readonly"
				maxlength="20" value="${buyFlowStat.endDate}"
				class="WdateTime required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		</div>
	</form:form>
	<table id="statsbuyFlow"
		class="table2excel table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th><div style="width:168px;">日期</div></th>
				<c:choose>
				   <c:when test="${buyFlowStat.groupByDay == 1}"> 
				    	<th class="sort-column childCkAppId">汇总</th>      
				   </c:when>
				   <c:otherwise>
				   		<c:choose>
				   			<c:when test="${buyFlowStat.group==0 }">
				   				<th>渠道</th>
				   			</c:when>
				   			<c:otherwise>
				   				<th id = "chan">
				   				     <font id='searchName' onclick="showSearchBox()">推广活动</font>
				   				     <input type="text" id = "searchBox" style="display:none;width:81px;margin:0;">
				   				</th>
				   			</c:otherwise>
				   		</c:choose>
				   </c:otherwise>
				</c:choose>
				<th>点击总数</th>
				<th>排重点击</th>
				<th>激活设备数</th>
				<th>激活率</th>
				<th>注册设备数</th>
				<th>注册率</th>
				<th class='col'>次留</th>
				<th class='col'>3留</th>
				<th class='col'>7留</th>
				<th class='col'>30留</th>
				<th class='col'>LTV0</th>
				<th class='col'>LTV1</th>
				<th class='col'>LTV7</th>
				<th class='col'>LTV30</th>
				<th class='col'>LTV60</th>
				<th class='col'>60日留存付费</th>
				<th class='col'>新增用户付费金额</th>
				<th class='col'>总付费</th>
				<th class='col'>新增付费设备</th>
				<th class='col'>总付费设备</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td colspan="2">汇总</td>
				<td class="ttclickNum">${total.clickNum}</td>
				<td class="ttdeviceClickNum">${total.deviceClickNum}</td>
				<td class="ttactiveNum">${total.activeNum}</td>
				<td class="ttactiveNumRate">
				    <c:choose>
				        <c:when test="${total.clickNum != 0}">
				            <fmt:formatNumber value="${total.activeNum / total.deviceClickNum * 100}" pattern="0.00"/>%
				        </c:when>
				        <c:otherwise>--</c:otherwise>
				    </c:choose>
				</td>
				<td class="ttregisterNum">${total.registerNum}</td>
				<td class="ttregisterNumRate">
				    <c:choose>
                        <c:when test="${total.activeNum != 0}">
                            <fmt:formatNumber value="${total.registerNum / total.activeNum * 100}" pattern="0.00"/>%
                        </c:when>
                        <c:otherwise>--</c:otherwise>
                    </c:choose>
				</td>
				<td class='col ttretain1'>
                    <c:choose>
                        <c:when test="${total.retain0 != 0}">
                              <fmt:formatNumber value="${total.retain1 / total.retain0 * 100}" pattern="0.00"/>%
                        </c:when>
                        <c:otherwise>--</c:otherwise>
                    </c:choose>
                </td>
				<td class='col ttretain3'>
				    <c:choose>
                        <c:when test="${total.retain0 != 0}">
                              <fmt:formatNumber value="${total.retain3 / total.retain0 * 100}" pattern="0.00"/>%
                        </c:when>
                        <c:otherwise>--</c:otherwise>
                    </c:choose>
                </td>
				<td class='col ttretain7'>
                    <c:choose>
                        <c:when test="${total.retain0 != 0}">
                              <fmt:formatNumber value="${total.retain7 / total.retain0 * 100}" pattern="0.00"/>%
                        </c:when>
                        <c:otherwise>--</c:otherwise>
                    </c:choose>
                </td>
                <td class='col ttretain30'>
                    <c:choose>
                        <c:when test="${total.retain0 != 0}">
                              <fmt:formatNumber value="${total.retain30 / total.retain0 * 100}" pattern="0.00"/>%
                        </c:when>
                        <c:otherwise>--</c:otherwise>
                    </c:choose>
                </td>
				<td class='col ttltv0'><fmt:formatNumber value="${total.ltv0}" pattern="0.00"/></td>
				<td class='col ttltv1'><fmt:formatNumber value="${total.ltv1}" pattern="0.00"/></td>
				<td class='col ttltv7'><fmt:formatNumber value="${total.ltv7}" pattern="0.00"/></td>
				<td class='col ttltv30'><fmt:formatNumber value="${total.ltv30}" pattern="0.00"/></td>
				<td class='col ttltv60'><fmt:formatNumber value="${total.ltv60}" pattern="0.00"/></td>
			    <td class='col ttretainMoney60'>
                   <fmt:formatNumber value="${total.retainMoney60*0.01}" pattern="0.00"/>
                </td>
                <td class='col ttretainMoney0'>
                   <fmt:formatNumber value="${total.retainMoney0*0.01}" pattern="0.00"/>
                </td>
                <td class='col tttotalMoney'>
                   <fmt:formatNumber value="${total.totalMoney*0.01}" pattern="0.00"/>
                </td>
				<td class='col ttactiveDeviceNum'>${total.activeDeviceNum }</td>
				<td class='col tttotalDeviceNum'>${total.totalDeviceNum }</td>
			</tr>
			<c:forEach items="${data}" var="stat" varStatus="status">
				<tr id="tr${status.index}" class = 'rowss'>
					<c:choose>
						<c:when test="${buyFlowStat.groupByDay == 0}">
							<td>
								<c:choose>
									<c:when test="${buyFlowStat.group == 0}">
										<a onclick="detail('${stat.media}','tr${status.index}')">${buyFlowStat.startDate}到${buyFlowStat.endDate}</a>
									</c:when>
									<c:otherwise>
										<a onclick="detail('${stat.activityId}','tr${status.index}')">${buyFlowStat.startDate}到${buyFlowStat.endDate}</a>
									</c:otherwise>
								</c:choose>
							</td>
						</c:when>
						<c:otherwise>
							<td>${stat.statsDate}</td>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${buyFlowStat.groupByDay == 0}">
							<c:choose>
							   <c:when test="${buyFlowStat.group == 0}"> 
							   		<td>
							   			<a href="#" onclick="mediaDetail('${stat.media}','tr${status.index }')">${fns:getDictLabel(stat.media,'adPush_media', defaultValue)}</a>
							   		</td>      
							   </c:when>
							   <c:otherwise>
							   		<td class='buyflowName'>${stat.buyFlowName}</td>
							   </c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<td><a href="#" onclick="dayDetail(${stat.statsDate},'tr${status.index}')">汇总</a></td>
						</c:otherwise>
					</c:choose>				
					<td>
					   ${stat.clickNum}
					   <input class="clickNum" type="hidden" value="${stat.clickNum}">
					</td>
					<td>
					   ${stat.deviceClickNum}
					   <input class="deviceClickNum" type="hidden" value="${stat.deviceClickNum}">
					</td>
					<td>
					   ${stat.activeNum}
					   <input class="activeNum" type="hidden" value="${stat.activeNum}">
					   </td>
					<td>
						<c:choose>
	                        <c:when test="${stat.clickNum != 0}">
	                              <fmt:formatNumber value="${stat.activeNum / stat.deviceClickNum * 100}" pattern="0.00"/>%
	                        </c:when>
	                        <c:otherwise>--</c:otherwise>
                        </c:choose>
					</td>
					<td>
					   ${stat.registerNum}
					   <input class="registerNum" type="hidden" value="${stat.registerNum}">
					</td>
					<td>
						<c:choose>
	                        <c:when test="${stat.activeNum != 0}">
	                              <fmt:formatNumber value="${stat.registerNum / stat.activeNum * 100}" pattern="0.00"/>%
	                        </c:when>
	                        <c:otherwise>--</c:otherwise>
                        </c:choose>
					</td>
	                <td class='col'>
	                    <c:choose>
	                        <c:when test="${stat.retain0 != 0}">
	                              <fmt:formatNumber value="${stat.retain1 / stat.retain0 * 100}" pattern="0.00"/>%
	                        </c:when>
	                        <c:otherwise>--</c:otherwise>
                        </c:choose>
                        <input class="retain0" type="hidden" value="${stat.retain0}">
                        <input class="retain1" type="hidden" value="${stat.retain1}">
	                </td>
	                <td class='col'>
	                    <c:choose>
	                        <c:when test="${stat.retain0 != 0}">
	                              <fmt:formatNumber value="${stat.retain3 / stat.retain0 * 100}" pattern="0.00"/>%
	                        </c:when>
	                        <c:otherwise>--</c:otherwise>
                        </c:choose>
                        <input class="retain3" type="hidden" value="${stat.retain3}">
	                </td>
	                <td class='col'>
	                    <c:choose>
	                        <c:when test="${stat.retain0 != 0}">
	                              <fmt:formatNumber value="${stat.retain7 / stat.retain0 * 100}" pattern="0.00"/>%
	                        </c:when>
	                        <c:otherwise>--</c:otherwise>
                        </c:choose>
                        <input class="retain7" type="hidden" value="${stat.retain7}">
	                </td>
	                <td class='col'>
	                    <c:choose>
	                        <c:when test="${stat.retain0 != 0}">
	                              <fmt:formatNumber value="${stat.retain30 / stat.retain0 * 100}" pattern="0.00"/>%
	                        </c:when>
	                        <c:otherwise>--</c:otherwise>
                        </c:choose>
                        <input class="retain30" type="hidden" value="${stat.retain30}">
	                </td>
	                <td class='col'>
	                   <fmt:formatNumber value="${stat.ltv0}" pattern="0.00"/>
                       <input class="retainMoney0" type="hidden" value="${stat.retainMoney0}">
	                </td>
	                <td class='col'>
	                   <fmt:formatNumber value="${stat.ltv1}" pattern="0.00"/>
                       <input class="retainMoney1" type="hidden" value="${stat.retainMoney1}">
	                </td>
	                <td class='col'>
	                   <fmt:formatNumber value="${stat.ltv7}" pattern="0.00"/>
                       <input class="retainMoney7" type="hidden" value="${stat.retainMoney7}">
	                </td>
	                <td class='col'>
	                   <fmt:formatNumber value="${stat.ltv30}" pattern="0.00"/>
                       <input class="retainMoney30" type="hidden" value="${stat.retainMoney30}">
	                </td>
	                <td class='col'>
	                   <fmt:formatNumber value="${stat.ltv60}" pattern="0.00"/>
                       <input class="retainMoney60" type="hidden" value="${stat.retainMoney60}">
	                </td>
	                <td class='col'>
	                   <fmt:formatNumber value="${stat.retainMoney60*0.01}" pattern="0.00"/>
	                </td>
	                <td class='col'>
	                   <fmt:formatNumber value="${stat.retainMoney0*0.01}" pattern="0.00"/>
	                </td>
	                <td class='col'>
	                   <fmt:formatNumber value="${stat.totalMoney*0.01}" pattern="0.00"/>
	                   <input class="totalMoney" type="hidden" value="${stat.totalMoney}">
	                </td>
	                <td class='col'>
	                   ${stat.activeDeviceNum }
	                   <input class="activeDeviceNum" type="hidden" value="${stat.activeDeviceNum}">
	                </td>
	                <td class='col'>
	                   ${stat.totalDeviceNum }
	                   <input class="totalDeviceNum" type="hidden" value="${stat.totalDeviceNum}" >
	                </td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>