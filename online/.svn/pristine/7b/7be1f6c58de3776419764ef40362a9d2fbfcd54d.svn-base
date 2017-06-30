<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>页面访问/下载统计</title>
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
	<div id="position">页面访问/下载统计</div>
	<form:form id="searchForm"  modelAttribute="webAccess" action="${ctx}/stats/webAccessStats/" method="post" class="breadcrumb form-search">
		<div>
			<label>游戏：</label>
			<span><font color="red">*</font> </span>
			<form:select id="ckappid" path="ckappid" class="input-medium">
				<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
		    </form:select>
		    <label>渠道：</label>
			<form:select id="channelid" path="channelid" class="input-medium">
				<form:option value="" label="(全部)"/>
				<form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
		    </form:select>
		    <label>来源：</label>
			<form:select id="source" path="source" class="input-medium">
				<form:option value="" label="(全部)"/>
				<form:option value="1" label="访问"/>
				<form:option value="2" label="下载"/>
		    </form:select>
		</div><br/>
		<div>
			<label>子项：</label>
			<form:select id="item" path="item" class="input-medium">
				<form:option value="" label="(全部)"/>
				<form:option value="1" label="子项1"/>
				<form:option value="2" label="子项2"/>
				<form:option value="3" label="子项3"/>
				<form:option value="4" label="子项4"/>
				<form:option value="5" label="子项5"/>
		    </form:select>
			<label>IP去重：</label>
			<form:select id="ipfilter" path="ipfilter" class="input-medium">
				<form:option value="" label="否"/>
				<form:option value="1" label="是"/>
		    </form:select>
			<label>时间：</label>
			<a href="#" onclick="javascript:selectDateFn(0);void(0)">今天</a>
			<a href="#" onclick="javascript:selectDateFn(1);void(0)">昨天</a>
			<a href="#" onclick="javascript:selectDateFn(2);void(0)">前天</a>
			<a href="#" onclick="javascript:selectDateFn(7);void(0)">7天前</a>
			<span><font color="red">*</font> </span>
			<input id="startDate" name="startDate" type="text" readonly="readonly" maxlength="20" class="WdateTime required"
				value="${webAccess.startDate}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" style="width:100px" value="查    询"/>&nbsp;&nbsp;
		</div>
	</form:form>

<table id="mainInfoTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>渠道号</th>
				<th>来源</th>
				<th>子项</th>
				<th>数量</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${result}" var="vo" varStatus="status">
			<tr>
				<td>${status.count}</td>
				<td>${fns:findChannelName(vo.channelid,"")}(${vo.channelid})</td>
				<td><c:choose>
				<c:when test="${vo.source eq '1'}">访问</c:when>
				<c:otherwise>下载</c:otherwise>
				</c:choose>
				</td>
				<td>${vo.item}</td>
				<td>${vo.num}</td>
			</tr>
		</c:forEach>
</tbody>
</table>
</body>
</html>