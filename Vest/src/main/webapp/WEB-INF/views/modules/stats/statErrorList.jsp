<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>错误信息列表</title>
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
	${cssGame}
	${cssChannel}
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
			$.sortTable.sort($this.closest('table').attr('id'), $this.siblings().index($this));
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
			$doc.on('click', '[name=btnViewContent]', function(event){
				var $this = $(event.currentTarget);
				var $span = $this.siblings('.content');
				var style = [
					'overflow:auto',
					'max-height:' + (document.documentElement.clientHeight * .6) + 'px'
				].join(';');
				$.jBox('html:<div style="' + style + '"><pre>' + $span.html() + '</pre></div>', {width: 'auto'});
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


	<table id="statErrorList" class="table2excel table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th class="sort">游戏</th>
				<th class="sort">子游戏</th>
				<th class="sort">渠道</th>
				<th class="sort">子渠道</th>

				<th class="sort">会话</th>
				<th class="sort">设备号</th>
				<th class="sort">应用版本号</th>
				<th class="sort">网络类型</th>

				<th class="sort">游戏角色</th>
				<th class="sort">区服</th>
				
				<th class="sort">系统语言</th>
				<th class="sort">操作系统版本</th>
				<th class="sort">设备型号</th>
				<th class="sort">CPU信息</th>
				<th class="sort">内存信息</th>

				<th class="sort">创酷SDK版本</th>
				<th class="sort">账号SDK版本</th>
				<th class="sort">支付SDK版本</th>

				<th class="sort">IP地址</th>
				<th class="sort">国家</th>
				<th class="sort">省份</th>
				<th class="sort">城市</th>

				<th class="sort">启动时间</th>
				<th class="sort">启动时长</th>
				<th class="sort">最后的状态</th>
				<th class="sort">最后的操作</th>

				<th class="sort">错误的类名</th>
				<th class="sort">错误的行数</th>
				<th class="sort">异常的类名</th>

				<th class="sort">日志主题</th>
				<th class="sort">日志内容</th>

				<th class="sort">发生时间</th>
				<th class="sort">入库时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${data}" var="stat" varStatus="status">
				<tr>
					<td>
						<c:if test="${ stat.ckAppId != null && stat.ckAppId != '' }">
							${fns:getByCkAppName(stat.ckAppId)}(${stat.ckAppId})
						</c:if>
					</td>
					<td>
						<c:if test="${ stat.ckAppId != null && stat.ckAppId != '' && stat.ckAppChildId != null && stat.ckAppChildId != '' }">
							${fns:getByChildAppName(stat.ckAppId,stat.ckAppChildId)}(${stat.ckAppChildId})
						</c:if>
					</td>
					<td>
						<c:if test="${ stat.channelId != null && stat.channelId != '' }">
							${fns:findChannelName(stat.channelId,"")}(${stat.channelId})
						</c:if>
					</td>
					<td>${stat.channelChildId}</td>

					<td>${stat.sid}</td>
					<td>${stat.deviceId}</td>
					<td>${stat.versionName}</td>
					<td>${stat.netType}</td>

					<td>${stat.cpRoleId}</td>
					<td>${stat.cpServerId}</td>

					<td>${stat.osLang}</td>
					<td>${stat.osVersion}</td>
					<td>${stat.deviceModel}</td>
					<td>${stat.cpuInfo}</td>
					<td>${stat.memInfo}</td>

					<td>${stat.sdkVersionCk}</td>
					<td>${stat.sdkVersionAccount}</td>
					<td>${stat.sdkVersionPay}</td>

					<td>${stat.ip}</td>
					<td>${stat.country}</td>
					<td>${stat.province}</td>
					<td>${stat.city}</td>

					<td class="nobr"><fmt:formatDate value="${stat.firstStartupTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td>${stat.runtimeMillisecond}</td>
					<td>${stat.lastState}</td>
					<td>${stat.lastAction}</td>

					<td>${stat.errorClassName}</td>
					<td>${stat.errorLineNumber}</td>
					<td>${stat.exceptionClassName}</td>

					<td>${stat.topic}</td>
					<td><a href="javascript:void(0);" name="btnViewContent">查看</a><span class="hide content">${stat.content}</span></td>

					<td class="nobr"><fmt:formatDate value="${stat.occurTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td class="nobr"><fmt:formatDate value="${stat.insertTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>