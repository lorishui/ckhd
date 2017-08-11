<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>新手任务比率</title>
<meta name="decorator" content="default" />
<style type="text/css">
	.sort{cursor: pointer;}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		$("#ckAppId").focus();
		$("#searchForm").validate({
			submitHandler : function(form) {
				form.submit();
			},
			errorContainer : "#messageBox"
		});

		$('.sort').click(function(){
			var $this = $(this);
			$.sortTable.sort($this.closest('table').attr('id'), $this.siblings($this));
		});
	});
	function selectDateFn(val) {
		var begDate = $("#begDate");
		var endDate = $("#endDate");
		if (val == 0) {
			var day = GetDateStr(0);
			begDate.val(day);
			endDate.val(day);
		}
		else if (val == 1) {
			var day = GetDateStr(-1);
			begDate.val(day);
			endDate.val(day);
		}
		else if (val == 7) {
			begDate.val(GetDateStr(-6));
			endDate.val(GetDateStr(0));
		}
		else if (val == 30) {
			begDate.val(GetDateStr(-29));
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
</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="param" action="${ctx}/query/simple" method="post" class="breadcrumb form-search">
		<input type="hidden" name="qname" value="simpleDemo" />
		<input type="hidden" name="vname" value="query/simpleDemo" />
		<div>
			<label>游戏：</label>
			<form:select id="ckAppId" name="ckAppId" path="map[ckAppId]" class="input-medium">
				<!--form:option value="" label="(全部)" /-->
				<form:options items="${fns:getAPPCkListByChild()}" itemLabel="name" itemValue="ckappId" htmlEscape="false" />
			</form:select>
			<span><font color="red">*</font></span>

			<label>渠道：</label>
			<form:select id="ckChannelId" name="ckChannelId" path="map[ckChannelId]" class="input-medium">
				<!--form:option value="" label="(全部)" /-->
				<form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false" />
			</form:select>
			<span><font color="red">*</font></span>
		</div>
		<br>
		<div>
			<label>时间范围：</label> <a href="#" onclick="javascript:selectDateFn(0);void(0)">今天</a> 
			<a href="#" onclick="javascript:selectDateFn(1);void(0)">昨天</a>
			<a href="#" onclick="javascript:selectDateFn(7);void(0)">近七天</a>
			<a href="#" onclick="javascript:selectDateFn(30);void(0)">近30天</a> 
			
			<label>统计日期：</label>
			<input id="begDate" name="begTime" type="text" readonly="readonly" maxlength="20"
				value="${param.begTime}" class="WdateTime required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
			<span><font color="red">*</font></span>

			<input id="endDate" name="endTime" type="text" readonly="readonly"
				maxlength="20" value="${param.endTime}"
				class="WdateTime required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
			<span><font color="red">*</font> </span>

			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
	</form:form>
	<table id="statsOnline" class="table2excel table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th class="sort">日期</th>
				<th class="sort">游戏</th>
				<th class="sort">渠道</th>
				<th class="sort">新增角色数</th>
				<th class="sort">完成新手任务数</th>
				<th class="sort">比例</th>
			</tr>
		</thead>
		<tbody>
		<tbody>
			<c:forEach items="${data}" var="stat" varStatus="status">
				<tr>
					<td>${stat.cd}</td>
					<td>
						<c:set var="ckAppId" value="${ param['map[ckAppId]'] }" />
						<c:if test="${ ckAppId != null && ckAppId != '' }">
							${fns:getByCkAppName(ckAppId)}(${ckAppId})
						</c:if>
					</td>
					<td>
						<c:set var="ckChannelId" value="${ param['map[ckChannelId]'] }" />
						<c:if test="${ ckChannelId != null && ckChannelId != '' }">
							${fns:findChannelName(ckChannelId,"")}(${ckChannelId})
						</c:if>
					</td>
					<td>${stat.ctRole}</td>
					<td>${stat.ctEvent}</td>
					<td>${stat.ratio}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>