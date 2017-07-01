<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>游戏流水统计</title>
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
	
</script>
<style type="text/css">
 thead th{ background-color:red}
</style>
</head>
<body>
	<div id="position">游戏收入概况展示</div>
	<form:form id="searchForm"  modelAttribute="orderQry" action="${ctx}/stats/SuccessMoney/states" method="post" class="breadcrumb form-search">
		<div>
			<label>游戏：</label>
			<form:select id="ckAppId" path="ckAppId" class="input-medium">
				<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
			</form:select> <span class="help-inline"><font color="red">*</font></span>
			<label>时间：</label>
			<input id="startDate" name="startDate" type="text" readonly="readonly" maxlength="20" class="WdateTime required"
				value="${orderQry.startDate}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>到
			<input id="startDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="WdateTime required"
				value="${orderQry.endDate}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
			<label>计费方式：</label>
			<select name="carriesType" style="width: 100px">
				<option value="1">MM</option>
				<option value="2" ${carriesType eq 2?'selected = "selected"':''  }>和游戏 </option>
			</select>
				&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" style="width:100px" value="查    询"/>&nbsp;&nbsp;
		</div>
	</form:form>
	<table id="onlineStats" class="table2excel table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th onclick="$.sortTable.sort('onlineStats',0,compareNumber)" style="cursor: pointer;">序号</th>
				<th>日期</th>
				<th onclick="$.sortTable.sort('onlineStats',2)" style="cursor: pointer;">游戏名称  </th>
				<th onclick="$.sortTable.sort('onlineStats',3,compareNumber)" style="cursor: pointer;">总收入(元)</th>
			</tr>
		 </thead>	
		 <tbody>
		 <c:set var="total" value="0" />
	     <c:forEach items="${data}" var="statsAndMm" varStatus="status">
			<tr>
				<td>${status.count}</td>
				<td>${statsAndMm.mdate}</td>
				<td>${fns:getByCkAppName(statsAndMm.ckAppId)}(${statsAndMm.ckAppId})</td>
				<td><fmt:formatNumber type="NUMBER" value="${statsAndMm.succ_price}" maxFractionDigits="2" pattern="0.00"/></td>
			</tr>
			<c:set var="total" value="${total+statsAndMm.succ_price}" />
		 </c:forEach>
		 <c:if test="${total > 0 }">
		 	<tr>
				<td colspan="3">总计</td>
				<td><fmt:formatNumber type="NUMBER" value="${total}" maxFractionDigits="2" pattern="0.00"/></td>
			</tr>
		 </c:if>
		 </tbody>	
	</table>
</body>
</html>