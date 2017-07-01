<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default"/>
<title>破解数查询</title>
<script type="text/javascript">
$(function(){
	var startDate = $("#startDate");
	var endDate = $("#endDate");
	if(startDate.val()==""){
		startDate.val(GetDateStr(0));
	}
	if(endDate.val()==""){
		endDate.val(GetDateStr(0));
	}
});

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
	<form:form id="searchForm" action="${ctx}/list" modelAttribute="appevent"  method="post" class="breadcrumb form-search">
		<div>
			<span class="help-inline"><font color="red">*</font> </span>
			<label>游戏：</label>
			<form:select id="ckappId" path="ckAppId" class="input-medium" >
				<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
		    </form:select>
		    <label>渠道：</label>
			<form:select id="ckChannelId" path="ckChannelId" class="input-medium">
				<form:option value="" label="(不选)"/>
				<form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
		    </form:select>
		   <label>时间：</label>
		    <input id="startDate" name="startDate" type="text" class="WdateTime"
		    value="${appevent.startDate}" 
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			到
		    <input id="endDate" name="endDate" type="text" class="WdateTime"
		    value="${appevent.endDate}" 
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>	
		</div>
	</form:form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
			<th onclick="$.sortTable.sort('contentTable',0)">游戏</th>
			<th onclick="$.sortTable.sort('contentTable',1)">渠道</th>
			<th onclick="$.sortTable.sort('contentTable',2,compareNumber)">数量</th>
			<th onclick="$.sortTable.sort('contentTable',3)">说明</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${data}" var="data">
			<tr>
				<td>${fns:getByCkAppName(appevent.ckAppId)}(${appevent.ckAppId })</td>
				<td>${fns:findChannelName(data.ckChannelId,'')}(${data.ckChannelId })</td>
				<td>${data.num}</td>
				<c:if test="${data.trust_sign eq 0 and data.num ge 100}">
					<td style="color:red">
					 破解（可能漏了采集签名MD5）
					</td>
				</c:if>
				<c:if test="${data.trust_sign eq 0 and data.num lt 100}">
					<td> 破解（可能漏了采集签名MD5）</td>
				</c:if>
				<c:if test="${data.trust_sign eq 1}">
					<td>正版</td>
				</c:if>
				<c:if test="${data.trust_sign eq 2 and data.num ge 100}">
					<td style="color:red">
					破解（可能是很旧的CKSDK的正版包）
					</td>
				</c:if>
				<c:if test="${data.trust_sign eq 2 and data.num lt 100}">
					<td>破解（可能是很旧的CKSDK的正版包）</td>
				</c:if>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>