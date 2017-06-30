<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>订单统计</title>
<meta name="decorator" content="default"/>
<script type="text/javascript" src="/static/tableOrder/tableOrder.js?a=1"></script>
<script type="text/javascript">
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
		function selectDateFn(val){
			var startDate = $("#startDate");
			var endDate = $("#endDate");
			if(val == 0){
				var day =  GetDateStr(0);
				startDate.val(day);
				endDate.val(day);
			}else if(val == 1){
				var day = GetDateStr(-1);
				startDate.val( day);
				endDate.val( day);
			}else if(val == 7){
				startDate.val( GetDateStr(-6));
				endDate.val( GetDateStr(0));
			}else if(val == 30){
				startDate.val( GetDateStr(-29));
				endDate.val( GetDateStr(0));
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
		function changeAPPID(pageLoad){
			$.ajax({
			    url:'${ctx}/stats/queryAppCarriers',
			    type:'get',
			    data:'ckAppId=' + $("#ckappId").val() + '&carriersType=MM',
			    error:function(){
			    },
			    success:function(data){
			    	$("#appId").empty();
			    	$("#appId").append("<option value='' selected>(全部)</option>");
			       for(var item in data){
			       	$("#appId").append("<option value='"+data[item].appId +"'>"+data[item].appName + "(" + data[item].appId +")</option>");
			       }
			       if(pageLoad){
			    	   $("#appId").val("${mmapporder.appId}");
					   $(".select2-chosen")[1].innerHTML = $("#appId option:selected").text();
			       }else{
			    	   $("#appId").val("");
				       $(".select2-chosen")[1].innerHTML = "(全部)";
			       }
			    }
			});
		}
</script>
<style type="text/css">
 thead th{ background-color:red}
</style>
</head>
<body>

<div id="position">订单统计</div>

<form:form id="searchForm"  modelAttribute="apporder" action="${ctx}/statsforclient/order/" method="post" class="breadcrumb form-search">
		<div>
			<label>游戏：</label>
			 <span><font color="red">*</font> </span>
			<form:select id="ckappId" path="ckappId" class="input-medium" onChange="changeAPPID()">
				<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
		    </form:select>
			<form:select id="appId" path="appId" class="input-medium" style="width:300px">
		    </form:select>
		</div>
		<br/>
		<div>
			<label>时间范围：</label>
			<a href="#" onclick="javascript:selectDateFn(0);void(0)">今天</a>
			<a href="#" onclick="javascript:selectDateFn(1);void(0)">昨天</a>
			<a href="#" onclick="javascript:selectDateFn(7);void(0)">近七天</a>
			<a href="#" onclick="javascript:selectDateFn(30);void(0)">近30天</a>
			<span><font color="red">*</font> </span>
			<input id="startDate" name="startDate" type="text" readonly="readonly" maxlength="20" class="WdateTime required"
				value="${apporder.startDate}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			&nbsp;--&nbsp;
			<span><font color="red">*</font> </span>
			<input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="WdateTime required"
				value="${apporder.endDate}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		</div>
</form:form>

<table id="mainInfoTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
			<th>序号</th>
			<th>日期</th>
			<th>付费用户数</th>
			<th>总金额</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${orderList}" var="order" varStatus="status">
			<tr>
				<td>${status.count}</td>
				<td>${order.mdate}</td>
				<td><fmt:formatNumber type="number" value="${(order.mm_user_num==null?0:order.mm_user_num) + (order.and_user_num==null?0:order.and_user_num)}" maxFractionDigits="2" pattern="0"/></td>
				<td><fmt:formatNumber type="number" value="${order.total_price==null?0:order.total_price}" maxFractionDigits="2" pattern="0.00"/></td>
			</tr>
			</c:forEach>
		</tbody>
</table>
</body>
</html>