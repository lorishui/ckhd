<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>游戏收入概况展示</title>
<meta name="decorator" content="default"/>
<script type="text/javascript" src="/static/tableOrder/tableOrder.js?a=1"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#ckappId").focus();
		$("#searchForm").validate({
			submitHandler: function(form){
				form.submit();
			},
			errorContainer: "#messageBox"
		});
	});
	function selectDateFn(val){
		var startDate = $("#startDate");
		if(val == 0){
			var day =  GetDateStr(0);
			startDate.val(day);
		}else if(val == 1){
			var day = GetDateStr(-1);
			startDate.val( day);
		}else if(val == 2){
			startDate.val( GetDateStr(-2));
		}else if(val == 7){
			startDate.val( GetDateStr(-7));
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
<style type="text/css">
 thead th{ background-color:red}
</style>
</head>
<body>
	<div id="position">游戏收入概况展示</div>
	<form:form id="searchForm"  modelAttribute="mmapporder" action="${ctx}/stats/andmmorder/" method="post" class="breadcrumb form-search">
		<div>
			<label>游戏：</label>
			<form:select id="ckappId" path="ckappId" class="input-medium">
				<form:option value="" label="(全部)"/>
				<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
			</form:select> <span class="help-inline"><font color="red">*</font></span>
			<label>时间范围：</label>
			<a href="#" onclick="javascript:selectDateFn(0);void(0)">今天</a>
			<a href="#" onclick="javascript:selectDateFn(1);void(0)">昨天</a>
			<a href="#" onclick="javascript:selectDateFn(2);void(0)">前天</a>
			<a href="#" onclick="javascript:selectDateFn(7);void(0)">7天前</a>
			<span><font color="red">*</font> </span>
			<input id="startDate" name="startDate" type="text" readonly="readonly" maxlength="20" class="WdateTime required"
				value="${mmapporder.startDate}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" style="width:100px" value="查    询"/>&nbsp;&nbsp;
		</div>
	</form:form>
	<table id="onlineStats" class="table2excel table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th colspan="13">收入情况(日期：<font size="2" color="blue">${mmapporder.startDate}</font>)</th>
			</tr>
			<tr>
				<th onclick="$.sortTable.sort('onlineStats',0,compareNumber)" style="cursor: pointer;">序号</th>
				<th onclick="$.sortTable.sort('onlineStats',1)" style="cursor: pointer;">游戏名称  </th>
				<th onclick="$.sortTable.sort('onlineStats',2,compareNumber)" style="cursor: pointer;">MM收入(元)</th>
				<th onclick="$.sortTable.sort('onlineStats',3,compareNumber)" style="cursor: pointer;">和游戏收入(元)</th>
				<th onclick="$.sortTable.sort('onlineStats',4,compareNumber)" style="cursor: pointer;">总收入(元)</th>
				<th onclick="$.sortTable.sort('onlineStats',5,compareNumber)" style="cursor: pointer;">用户数</th>
				<th onclick="$.sortTable.sort('onlineStats',6,compareNumber)" style="cursor: pointer;">付费arpu</th>
				<th onclick="$.sortTable.sort('onlineStats',7,compareNumber)" style="cursor: pointer;">成功付费用户数</th>
				<th onclick="$.sortTable.sort('onlineStats',8,compareNumber)" style="cursor: pointer;">成功付费arpu</th>
				<th onclick="$.sortTable.sort('onlineStats',9,compareNumber)" style="cursor: pointer;">上周同期MM收入(元)</th>
				<th onclick="$.sortTable.sort('onlineStats',10,compareNumber)" style="cursor: pointer;">上周同期和游戏收入(元)</th>
				<th onclick="$.sortTable.sort('onlineStats',11,compareNumber)" style="cursor: pointer;">上周同期总收入(元)</th>
				<th onclick="$.sortTable.sort('onlineStats',12,compareNumber)" style="cursor: pointer;">总收入同比</th>
			</tr>
		 </thead>	
		 <tbody>
	     <c:forEach items="${result}" var="statsAndMm" varStatus="status">
			<tr>
				<td>${status.count}</td>
				<td><c:choose><c:when test="${statsAndMm.ckAppId eq '游戏总计'}">${statsAndMm.ckAppId}</c:when>
				<c:otherwise>${fns:getByCkAppName(statsAndMm.ckAppId)}(${statsAndMm.ckAppId})
				</c:otherwise></c:choose></td>
				<td><fmt:formatNumber type="number" value="${statsAndMm.mmPrice*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
				<td><fmt:formatNumber type="number" value="${statsAndMm.andPrice*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
				<td><fmt:formatNumber type="number" value="${(statsAndMm.mmPrice+statsAndMm.andPrice)*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
				<td>${statsAndMm.mmUserNum+statsAndMm.andUserNum}</td>
				<td><c:choose><c:when test="${statsAndMm.mmUserNum+statsAndMm.andUserNum eq 0}">---</c:when><c:otherwise>
				<fmt:formatNumber type="number" value="${(statsAndMm.mmPrice+statsAndMm.andPrice)*0.01/(statsAndMm.mmUserNum+statsAndMm.andUserNum)}" maxFractionDigits="2" pattern="0.00"/>
				</c:otherwise></c:choose></td>
				<td>${statsAndMm.mmSuccUserNum+statsAndMm.andSuccUserNum}</td>
				<td><c:choose><c:when test="${statsAndMm.mmSuccUserNum+statsAndMm.andSuccUserNum eq 0}">---</c:when><c:otherwise>
				<fmt:formatNumber type="number" value="${(statsAndMm.mmPrice+statsAndMm.andPrice)*0.01/(statsAndMm.mmSuccUserNum+statsAndMm.andSuccUserNum)}" maxFractionDigits="2" pattern="0.00"/>
				</c:otherwise></c:choose></td>
				<td><fmt:formatNumber type="number" value="${statsAndMm.lastWeekMmPrice*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
				<td><fmt:formatNumber type="number" value="${statsAndMm.lastWeekAndPrice*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
				<td><fmt:formatNumber type="number" value="${(statsAndMm.lastWeekMmPrice+statsAndMm.lastWeekAndPrice)*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
				<td><c:choose><c:when test="${(statsAndMm.lastWeekMmPrice+statsAndMm.lastWeekAndPrice) eq 0}">---</c:when>
				<c:when test="${(statsAndMm.lastWeekMmPrice+statsAndMm.lastWeekAndPrice) lt (statsAndMm.mmPrice+statsAndMm.andPrice)}"><font size="3" color="red">
				<fmt:formatNumber type="percent" value="${(statsAndMm.mmPrice+statsAndMm.andPrice)/(statsAndMm.lastWeekMmPrice+statsAndMm.lastWeekAndPrice)-1}" maxFractionDigits="2" pattern="0.00%"/>↑</font></c:when>
				<c:otherwise><font size="3" color="red"><fmt:formatNumber type="percent" value="${(statsAndMm.mmPrice+statsAndMm.andPrice)/(statsAndMm.lastWeekMmPrice+statsAndMm.lastWeekAndPrice)-1}" maxFractionDigits="2" pattern="0.00%"/>
				↓</font></c:otherwise></c:choose>
				</td>
		 </c:forEach>
		 </tbody>	
	</table>
	<div style="color:red">说明:用户数是MM用户数+咪咕用户数；成功付费用户数是有成功付费的用户数目。</div>
</body>
</html>