<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>错误信息统计</title>
<meta name="decorator" content="default" />

<c:if test="${ param.ckAppId == null || param.ckAppId == '' }">
	<c:set var="cssGame" value=".game{display:none;}" />
</c:if>
<c:if test="${ param.ckChannelId == null || param.ckChannelId == '' }">
	<c:set var="cssChannel" value=".channel{display:none;}" />
</c:if>
<style type="text/css">
	.sort{cursor: pointer;}
	.nobr{white-space: nowrap;}
	.table form{display:inline;}
	${cssGame}
	${cssChannel}
</style>

<script src="/static/tableOrder/tableOrder.js" type="text/javascript"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$("#ckAppId").focus();
		$("#searchForm").validate({
			submitHandler : function(form) {
				form.submit();
			},
			errorContainer : "#messageBox"
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
		return [y, m, d].join('');
	}
	$(function(){
		var $doc = $(document);
			$doc.on('click', '[name=btnOccurDate]', function(event){
				var $this = $(event.currentTarget);
				var $form = $this.siblings('form');
				var $tr = $this.closest('tr');
				var $ntr = $tr.next();
				if( !$ntr.hasClass('sublist') ){
					$ntr = $('<tr class="sublist hide"><td colspan="100">加载中，请稍候...</td></tr>');
					$ntr.insertAfter($tr);
					$ntr.children('td').load($form.attr('action') + ' table#statErrorList', $form.serialize());
				}
				$ntr.toggleClass('hide');
			});
			$doc.on('click', '[name=btnViewContent]', function(event){
				var $this = $(event.currentTarget);
				var $span = $this.siblings('.content');
				var style = [
					'overflow:auto',
					'max-height:' + (document.documentElement.clientHeight * .6) + 'px'
				].join(';');
				$.jBox('html:<div style="' + style + '"><pre>' + $span.html() + '</pre></div>', {width: 'auto'});
			});
			
			$doc.on('click', '.sort', function(event){
				var $this = $(event.currentTarget);
				$.sortTable.sort($this.closest('table').attr('id'), $this.siblings().parent().children().index($this));
			});
	});
</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="param" action="${ctx}/stats/error/count" method="post" class="breadcrumb form-search">
		<div>
			<label>游戏：</label>
			<form:select id="ckAppId" path="ckAppId" class="input-medium">
				<form:option value="" label="(全部)" />
				<form:options items="${fns:getAPPCkListByChild()}" itemLabel="name" itemValue="ckappId" htmlEscape="false" />
			</form:select>
			<span><font color="red">*</font></span>

			<label>渠道：</label>
			<form:select id="channelId" path="ckChannelId" class="input-medium">
				<form:option value="" label="(全部)" />
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
			<input id="begDate" name="begDate" type="text" readonly="readonly" maxlength="20"
				value="${param.begDate}" class="WdateTime required"
				onclick="WdatePicker({dateFmt:'yyyyMMdd',isShowClear:false});" />
			<span><font color="red">*</font></span>

			<input id="endDate" name="endDate" type="text" readonly="readonly"
				maxlength="20" value="${param.endDate}"
				class="WdateTime required"
				onclick="WdatePicker({dateFmt:'yyyyMMdd',isShowClear:false});" />
			<span><font color="red">*</font> </span>

			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
	</form:form>
	<table id="statsOnline" class="table2excel table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th class="sort">日期</th>
				<th class="sort game">游戏</th>
				<th class="sort channel">渠道</th>
				<th class="sort">errorClassName</th>
				<th class="sort">errorLineNumber</th>
				<th class="sort">exceptionClassName</th>
				<th class="sort">总数</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${data}" var="stat" varStatus="status">
				<tr>
					<td>
						<form method="post" action="${ctx}/stats/error/list" target="_blank">
							<input type="hidden" name="ckAppId" value="${param.ckAppId}" />
							<input type="hidden" name="ckChannelId" value="${param.ckChannelId}" />
							<input type="hidden" name="begDate" value="${stat.occurDate}" />
							<input type="hidden" name="endDate" value="${stat.occurDate}" />
							<input type="hidden" name="errorClassName" value="${stat.errorClassName}" />
							<input type="hidden" name="errorLineNumber" value="${stat.errorLineNumber}" />
							<input type="hidden" name="exceptionClassName" value="${stat.exceptionClassName}" />
						</form>
						<a href="javascript:void(0);" name="btnOccurDate"/>${stat.occurDate}</a>
					</td>
					<td class="game">
						<c:if test="${ param.ckAppId != null && param.ckAppId != '' }">
							${fns:getByCkAppName(param.ckAppId)}(${param.ckAppId})
						</c:if>
					</td>
					<td class="channel">
						<c:if test="${ param.ckChannelId != null && param.ckChannelId != '' }">
							${fns:findChannelName(param.ckChannelId,"")}(${param.ckChannelId})
						</c:if>
					</td>
					<td>${stat.errorClassName}</td>
					<td>${stat.errorLineNumber}</td>
					<td>${stat.exceptionClassName}</td>
					<td>${stat.total}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>