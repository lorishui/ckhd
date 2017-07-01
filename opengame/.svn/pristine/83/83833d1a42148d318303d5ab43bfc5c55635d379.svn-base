<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>和游戏失败订单流水</title>
<meta name="decorator" content="default"/>
<script type="text/javascript">
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/stats/anderrororders");
			$("#searchForm").submit();
			return false;
		}
		function exp(){
			$("#searchForm").attr("action","${ctx}/stats/exportAnderrororders");
			$("#searchForm").submit();
			return false;
		}
		$(document).ready(function() {
			changeAPPID(true);
			$("#ckappId").focus();
			$("#searchForm").validate({
				submitHandler: function(form){
					//loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox"
				 
			});
		});
		function exportStats(flag){
			//$('#tableID').tableExport({type:'pdf',escape:'false'});
			if(flag === 1){
				$("#mainInfoTable").tableExport({type:'excel',escape:'false'});
			}else if (flag === 2){
				$("#paycodeTable").tableExport({type:'excel',escape:'false'});
			}else if (flag === 3) {
				$("#errorcodeTable").tableExport({type:'excel',escape:'false'});
			}else{
				$("#errorOrdersTable").tableExport({type:'excel',escape:'false'});
			}
		}
		function selectDateFn(val){
			var startDate = $("#beginDate");
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
				$("#searchForm").attr("action","${ctx}/stats/anderrororders");
				$("#searchForm").submit();
				return false;
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
		function changeAPPID(pageLoad){
			$.ajax({
			    url:'${ctx}/stats/queryAppCarriers',
			    type:'get',
			    data:'ckAppId=' + $("#ckappId").val() + '&carriersType=ANDGAME',
			    error:function(){
			    },
			    success:function(data){
			    	$("#contentId").empty();
			    	$("#contentId").append("<option value='' selected>(全部)</option>");
			       for(var item in data){
			       	$("#contentId").append("<option value='"+data[item].appId +"'>"+data[item].appName + "(" + data[item].appId +")</option>");
			       }
			       if(pageLoad){
			    	   $("#contentId").val("${andapporder.contentId}");
					   $(".select2-chosen")[1].innerHTML = $("#contentId option:selected").text();
			       }else{
			    	   $("#contentId").val("");
				       $(".select2-chosen")[1].innerHTML = "(全部)";
			       }
			    }
			});
		}
</script>
</head>
<body>
<div id="position">和游戏失败订单信息</div>
<form:form id="searchForm"  modelAttribute="andapporder" action="${ctx}/stats/anderrororders" method="post" class="breadcrumb form-search">
		<div>
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
			<sys:message content="${message}"/>
			<span class="help-inline"><font color="red">*</font> </span>
			<label>游戏：</label>
			<form:select id="ckappId" path="ckappId" class="input-medium" onChange="changeAPPID()">
				<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
		    </form:select>
		    <form:select id="contentId" path="contentId" class="input-medium" style="width:300px">
		    </form:select>
		    <label>省份：</label>
			<form:select id="province" path="provinceId" class="input-medium" >
				<form:option value="" label="(全部)"/>
				<form:options items="${fns:getDictList('province_andgame')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		    </form:select>
		    <label>渠道：</label>
			<form:select id="channelId" path="channelId" class="input-medium">
				<form:option value="" label="(全部)" />
				<form:options items="${fns:getCarriersChannelList('ANDGAME')}" itemLabel="name" itemValue="id" htmlEscape="false"/>
		    </form:select>
			</div>
		    <br />
		    <div>
		   <span class="help-inline"><font color="red">*</font> </span>
			<label>时间范围：&nbsp;</label>
			<a href="#" onclick="javascript:selectDateFn(0);void(0)">今天</a>
			<a href="#" onclick="javascript:selectDateFn(1);void(0)">昨天</a>
			<a href="#" onclick="javascript:selectDateFn(7);void(0)">近七天</a>
			<a href="#" onclick="javascript:selectDateFn(30);void(0)">近30天</a>
			<input id="beginDate" name="beginDate" type="text" readonly="readonly"  maxlength="20" class="WdateTime required"
				value="<fmt:formatDate value="${andapporder.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" 
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			<label>--</label>
			<input id="endDate" name="endDate" type="text" readonly="readonly"  maxlength="20" class="WdateTime required"
				value="<fmt:formatDate value="${andapporder.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" 
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				&nbsp;&nbsp;
			<input id="btnSubmit" class="btn btn-primary" type="submit" onclick="return page();" value="查询"/>&nbsp;&nbsp;
			<input id="eptSubmit" type="button" class="btn btn-primary" onClick ="return exp();" value="失败流水导出" />	
		</div>
		
</form:form>

<table id="errorOrdersTable" class="table2excel table table-striped table-bordered table-condensed">
	<thead>
		<tr>
		<th onclick="$.sortTable.sort('errorOrdersTable',0,compareNumber)" >序号</th>
		<th onclick="$.sortTable.sort('errorOrdersTable',1)" >省份</th>
		<th onclick="$.sortTable.sort('errorOrdersTable',2)" >渠道</th>
		<th onclick="$.sortTable.sort('errorOrdersTable',3)"  >计费编码说明</th>
		<th onclick="$.sortTable.sort('errorOrdersTable',4)"  >错误码</th>
		<th onclick="$.sortTable.sort('errorOrdersTable',5)"  >错误码说明</th>
		<th onclick="$.sortTable.sort('errorOrdersTable',6,compareNumber)" class="sort-column price" >总价(元)</th>
		<th onclick="$.sortTable.sort('errorOrdersTable',7)" class="sort-column create_date">发生时间</th>
		</tr>
	 </thead>	
	 <tbody>
		<c:forEach items="${page.list}" var="errorOrders" varStatus="status">
			<tr>	
				<td>${status.count}</td>
				<td>${fns:getDictLabel(errorOrders.provinceId, "province_andgame", errorOrders.provinceId)}</td>
				<td>${fns:getChannelName(errorOrders.channelId,"")}(${errorOrders.channelId})</td>
				<td>${fns:getPaycodeName('ANDGAME',errorOrders.consumeCode,'')}(${errorOrders.consumeCode})</td>
				<td>${errorOrders.status}</td>
				<td>${fns:getDictLabels(errorOrders.status,'andErrorCode','')}</td>
				<td>${errorOrders.price/100}</td>
				<td>
				<fmt:formatDate type="both" value="${errorOrders.createDate}"
					 pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
	 </tbody>	
</table>
<div class="pagination">${page}</div>
</body>
</html>