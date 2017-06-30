<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>渠道订单统计信息</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		 $(document).ready(function() {
			changeAPPID(true);
			$("#ckAppId").focus();
			$("#searchForm").validate({
				submitHandler: function(form){
					$("#searchForm").attr("action","${ctx}/stats/appOnlinePay/channel");
					form.submit();
					merge();
				},
				errorContainer: "#messageBox"
			});
		});
		function changeAPPID(pageLoad){
			$.ajax({
			    url:'${ctx}/stats/queryAppEvent',
			    type:'get',
			    data:'ckAppId=' + $("#ckAppId").val(),
			    error:function(){
			    },
			    success:function(data){
			    	$("#appId").empty();
			    	$("#appId").append("<option value='' selected>(全部)</option>");
			       for(var item in data){
			       	$("#appId").append("<option value='"+data[item].appId +"'>"+data[item].appName + "(" + data[item].appId +")</option>");
			       }
			       if(pageLoad){
			    	   $("#appId").val("${onlinePay.appId}");
					   $(".select2-chosen")[1].innerHTML = $("#appId option:selected").text();
			       }else{
			    	   $("#appId").val("");
				       $(".select2-chosen")[1].innerHTML = "(全部)";
			       }
			    }
			});
		}
		function selectDateFn(val){
			var startDate = $("#startDate");
			var endDate = $("#endDate");
			if(val == 0){
				var day =  GetDateStr(0);
				startDate.val(day + " 00:00:00");
				endDate.val(day + " 23:59:59");
			}else if(val == 1){
				var day = GetDateStr(-1);
				startDate.val( day + " 00:00:00");
				endDate.val( day + " 23:59:59");
			}else if(val == 7){
				startDate.val( GetDateStr(-6) + " 00:00:00");
				endDate.val( GetDateStr(0) + " 23:59:59");
			}else if(val == 30){
				startDate.val( GetDateStr(-29) + " 00:00:00");
				endDate.val( GetDateStr(0) + " 23:59:59");
			}
			if(val >= 0){
				$("#searchForm").submit();
			}
		}
		function GetDateStr(AddDayCount) {
		    var dd = new Date();
		    dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
		    var y = dd.getFullYear();
		    var m = dd.getMonth()+1;//获取当前月份的日期
		    var d = dd.getDate();
		    m=m<10?"0"+m:m;
		    d=d<10?"0"+d:d;
		    return y+"-"+m+"-"+d;
	    }
		
	</script>
</head>
<body>
	<div id="position">渠道订单统计信息</div>
	<form:form id="searchForm" modelAttribute="onlinePay" action="${ctx}/stats/appOnlinePay/channel" 
	method="post" class="breadcrumb form-search">
		<div>
			<label>游戏：</label>
			<form:select id="ckAppId" path="ckAppId" class="input-medium" onChange="changeAPPID()">
				<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
		    </form:select>
		     <span><font color="red">*</font> </span>
			<form:select id="appId" path="appId" class="input-medium" style="width:250px">
		    </form:select>
		    <label class="control-label">支付方式：</label>
		    <form:select path="payType" class="input-large">
				<form:options items="${fns:getDictList('paytpye_channel')}"
					itemLabel="label" itemValue="value" htmlEscape="false" />
			</form:select> <span class="help-inline"><font color="red">*</font></span>
			<label>渠道：</label>
			<form:select id="channelId" path="channelId" class="input-medium">
				<form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
		    </form:select>
	   	</div> 
	   	<br>
	   	<div>
	   		<label>时间范围：</label>
			<a href="#" onclick="javascript:selectDateFn(0);void(0)">今天</a>
			<a href="#" onclick="javascript:selectDateFn(1);void(0)">昨天</a>
			<a href="#" onclick="javascript:selectDateFn(7);void(0)">近七天</a>
			<a href="#" onclick="javascript:selectDateFn(30);void(0)">近30天</a>
			<span><font color="red">*</font> </span>
	   		<label>统计日期：</label>
			<span><font color="red">*</font> </span>
		    <input id="startDate" name="startDate" type="text" readonly="readonly" maxlength="20" value="${onlinePay.startDate}"
		    class="WdateTime required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			&nbsp;&nbsp;
			<span><font color="red">*</font> </span>
			<input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" value="${onlinePay.endDate}"
			class="WdateTime required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input id="btnSubmit" class="btn btn-primary" type="submit" style="width:100px" value="查  询"/>&nbsp;&nbsp;
	  </div>
	  <br>
	</form:form>
	<table id="statsOnline" class="table2excel table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>日期</th>
				<th>支付成功数</th>
				<th>成功金额</th>
			</tr>
		 </thead>	
		 <tbody>
		<c:forEach items="${onlinePayList}" var="onlinePay" varStatus="status">
				<tr>
					<td>${status.count}</td>
					<td>${onlinePay.mdate}</td>
					<td>${onlinePay.succ_num}</td>
					<td><fmt:formatNumber type="number" value="${onlinePay.succ_actual}" maxFractionDigits="2" pattern="0.00"/></td>
				</tr>
			</c:forEach>
		 </tbody>	
	</table>
</body>
</html>