<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>网游设备新增统计信息</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		
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
		$.ajax({
			url : "${ctx}/buyflow/stat/data",
			type : 'post',
			data : "startDate="+startDate+"&endDate="+endDate+"&activityId="+name+"&media="+name+"&group="+groupVal,
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
				 	if(groupVal == 0){
				 		itemName = "<td><a href='#' onclick=\"dayMediaDetail('"+item.date+"','"+item.name+"','trd"+idx+"')\">"+item.name+"</a></td>"
				 	}
				 	tbody += 
				 	"<tr id='trd"+idx+"'>"
					    + "<td><a href='#' onclick=\"dayMediaExport('"+item.date+"','"+item.name+"')\">"+item.date+"</a></td>"
					    +itemName
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
                        +  "<td colspan='8' style='border:none'>"
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
		$.ajax({
			url : "${ctx}/buyflow/stat/mediaData",
			type : 'post',
			data : "startDate="+startDate+"&endDate="+endDate+"&media="+media,
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
		                    +"<td colspan='8' style='border:none'>"
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
		$.ajax({
			url : "${ctx}/buyflow/stat/dayMediaData",
			type : 'post',
			data : "statsDate="+date+"&media="+media,
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
		$.ajax({
			url : "${ctx}/buyflow/stat/dayData",
			type : 'post',
			data : "startDate="+startDate+"&endDate="+endDate+"&statsDate="+date+"&group="+groupVal,
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
			  	         + "<td colspan='8' style='border:none'>"
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
		
			<label>维度：</label>
			<form:select id="group" path="group" class="input-medium">
				<form:option value="0" label="渠道" />
				<form:option value="1" label="活动名称" />
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
				<th>日期</th>
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
				   				<th>推广活动</th>
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
			<!-- 	<th>次留</th>
				<th>3留</th>
				<th>7留</th>
				<th>30留</th>
				<th>LTV0</th>
				<th>LTV1</th>
				<th>LTV7</th>
				<th>LTV30</th>
				<th>LTV60</th> -->
			</tr>
		</thead>
		<tbody>
			<tr>
				<td colspan="2">汇总</td>
				<td>${total.clickNum}</td>
				<td>${total.deviceClickNum}</td>
				<td>${total.activeNum}</td>
				<td>
					<c:if test="${total.clickNum != 0}">
						<fmt:formatNumber value="${total.activeNum / total.clickNum * 100}" pattern="0.00"/>%
					</c:if>
				</td>
				<td>${total.registerNum}</td>
				<td>
					<c:if test="${total.activeNum != 0}">
						<fmt:formatNumber value="${total.registerNum / total.activeNum * 100}" pattern="0.00"/>%
					</c:if>
				</td>
				
<!-- 				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td> -->
			</tr>
			<c:forEach items="${data}" var="stat" varStatus="status">
				<tr id="tr${status.index}">
					<c:choose>
						<c:when test="${buyFlowStat.groupByDay == 0}">
							<td>
								<c:choose>
									<c:when test="${buyFlowStat.group == 0}">
										<span onclick="detail('${stat.media}','tr${status.index}')">${buyFlowStat.startDate}到${buyFlowStat.endDate}</span>
									</c:when>
									<c:otherwise>
										<span onclick="detail('${stat.activityId}','tr${status.index}')">${buyFlowStat.startDate}到${buyFlowStat.endDate}</span>
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
							   		<td>${stat.buyFlowName}</td>
							   </c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<td><a href="#" onclick="dayDetail(${stat.statsDate},'tr${status.index}')">汇总</a></td>
						</c:otherwise>
					</c:choose>				
					<td>${stat.clickNum}</td>
					<td>${stat.deviceClickNum}</td>
					<td>${stat.activeNum}</td>
					<td>
						<c:if test="${stat.clickNum != 0 }">
							<fmt:formatNumber value="${stat.activeNum / stat.clickNum * 100}" pattern="0.00"/>%
						</c:if>
					</td>
					<td>${stat.registerNum}</td>
					<td>
						<c:if test="${stat.activeNum != 0 }">
							<fmt:formatNumber value="${stat.registerNum / stat.activeNum * 100}" pattern="0.00"/>%
						</c:if>
					</td>
	<!-- 				<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td> -->
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>