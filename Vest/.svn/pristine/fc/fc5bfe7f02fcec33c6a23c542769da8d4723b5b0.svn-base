<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>游戏渠道基本信息配置</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				var id = "${payInfoConfig.id}";
				if (id != "") {
					changeAPPID(true, true);
					var channelId = "${payInfoConfig.channelId}";
					var str = '${payInfoConfig.exInfo}';
					if (str != "" && str != "null") {
						var exInfo = JSON.parse(str);
						var i = 0;
						$.each(exInfo, function(key, value) {
							if (i == 0) {
								var exInfoTemp = $("#exInfo_1");
								exInfoTemp.children("li:first-child").children(
										"#exInfo_key").val(key);
								exInfoTemp.children("li:first-child").children(
										"#exInfo_value").val(value);
							} else {
								addExInfo(key, value);
							}
							i++;
						});
					}
					$("#btnSave").val("保存");
					$("#btnSearch").val("返回");
					$("#btnSearch").attr("onclick", "return back()");
					$("#liAdd").show();
				} else {
					changeAPPID(true);
				}
			});

	function add() {
		$("#id").val("");
		$("#searchForm").attr("action", "${ctx}/app/payInfoConfig/save");
		$("#searchForm").submit();
		return false;
	}

	function back() {
		$("#searchForm").attr("action", "${ctx}/app/payInfoConfig/back");
		$("#searchForm").submit();
		return false;
	}

	function page(n, s) {
		if (n)
			$("#pageNo").val(n);
		if (s)
			$("#pageSize").val(s);
		$("#searchForm").attr("action", "${ctx}/app/payInfoConfig/list");
		$("#searchForm").submit();
		return false;
	}

	function save() {
		if (!validate()) {
			return false;
		}
		$("#searchForm").attr("action", "${ctx}/app/payInfoConfig/save");
		$("#searchForm").submit();
		return false;
	}

	//验证数据
	function validate() {
		var ckAppId = $("#ckAppId").get(0).selectedIndex;
		if (ckAppId < 0) {
			alert("请选择游戏!");
			$("#ckAppId").focus();
			return false;
		}
		var paytype = $("#paytype").find("option:selected").val();
		if (paytype == "") {
			alert("请选择支付方式!");
			$("#channelId").focus();
			return false;
		}
		if (paytype == "141") {
			var channelId = $("#channelId").get(0).selectedIndex;
			if (channelId == 0) {
				alert("请选择渠道!");
				$("#channelId").focus();
				return false;
			}
		}
		return true;
	}

	function addExInfo(key, value) {
		var index = $("[name = exInfos]").length;
		var exInfo = $("#exInfo_" + index);
		var id = "exInfo_" + (index + 1);
		var clonedEx = $("#exInfo_" + index).clone(true).attr("id", id);
		clonedEx.children("li:first-child").children(".control-label").html("");
		clonedEx.children("li:first-child").children("#exInfo_key").val(key);
		clonedEx.children("li:first-child").children("#exInfo_value")
				.val(value);
		clonedEx.children("[name=add]").children("input").val("-").attr(
				"onclick", "return removeExInfo(" + id + ");");
		exInfo.after(clonedEx);
		return false;
	}

	function removeExInfo(obj) {
		obj.remove()
		return false;
	}

	function changeAPPID(pageLoad, update) {
		$.ajax({
			url : '${ctx}/app/appck/getChildIdList',
			type : 'get',
			data : 'ckappId=' + $("#ckAppId").val(),
			error : function() {
			},
			success : function(data) {
				$("#childCkAppId").empty();
				$("#childCkAppId").append(
						"<option value='' selected>(全部)</option>");
				for ( var item in data) {
					$("#childCkAppId").append(
							"<option value='"+data[item].childId +"'>"
									+ data[item].name + "("
									+ data[item].childId + ")</option>");
				}
				if (pageLoad) {
					$("#childCkAppId").val("${payInfoConfig.childCkAppId}");
					$(".select2-chosen")[1].innerHTML = $(
							"#childCkAppId option:selected").text();
					if (update) {
						chanagechildCkAppId();
					}
				} else {
					$("#childCkAppId").val("");
					$(".select2-chosen")[1].innerHTML = "(全部)";
				}
			}
		});
	}
	function chanagechildCkAppId() {
		var childCkAppId = "${payInfoConfig.childCkAppId}"
		var ckappId = "${payInfoConfig.ckAppId}";
		$("#ckAppId option").each(
				function() {
					if (ckappId != "") {
						if ($(this).val() == ckappId) {
							$(this).attr("selected", true);
							$(this).parent("select").prev().children("a")
									.children(".select2-chosen").html(
											$(this).html());
							$(this).parent("select").attr("disabled", true);
						}
					}
				});
		$("#childCkAppId option").each(
				function() {
					if ($(this).val() == childCkAppId) {
						$(this).attr("selected", true);
						$(this).parent("select").prev().children("a").children(
								".select2-chosen").html($(this).html());
						$(this).parent("select").attr("disabled", true);
					}
				});
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/app/payInfoConfig/list">游戏渠道基本信息配置</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="payInfoConfig"
		action="${ctx}/app/payInfoConfig/list" method="post"
		class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<input id="id" type="hidden" name="id" value="${payInfoConfig.id}">
		<input id="addCkAppId" type="hidden" name="addCkAppId"
			value="${payInfoConfig.ckAppId}">
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}"
			callback="page();" />
		<ul class="ul-form">
			<li><label class="control-label">游戏：</label> <form:select
					id="ckAppId" path="ckAppId" class="input-medium"
					onChange="changeAPPID()">
					<form:options items="${fns:getAPPCkListByChild()}" itemLabel="name"
						itemValue="ckappId" htmlEscape="false" />
				</form:select> <form:select id="childCkAppId" path="childCkAppId"
					class="input-medium" style="width:300px">
				</form:select> <span class="help-inline"><font color="red">*</font></span></li>
			<li class="clearfix"></li>
		</ul>
		<ul class="ul-form">
			<li><label class="control-label">支付方式:</label> <form:select
					path="paytype" class="input-large">
					<form:options items="${fns:getDictList('paytype')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select> <span class="help-inline"><font color="red">*</font></span></li>
			<li><label class="control-label">渠道:</label> <form:select
					path="channelId" class="input-large">
					<form:option value="" label="(全部)" />
					<form:options items="${fns:getChannelList()}" itemLabel="name"
						itemValue="id" htmlEscape="false" />
				</form:select></li>
		</ul>
		<ul class="ul-form">
			<li><label class="control-label">APPID:</label> <form:input
					path="appid" value="${payInfoConfig.appid }" htmlEscape="false" /></li>
			<li><label class="control-label">APPKEY:</label> <form:input
					path="appkey" value="${payInfoConfig.appkey }" htmlEscape="false" /></li>
		</ul>
		<ul class="ul-form">
			<li><label class="control-label">回调地址:</label> <form:input
					path="notifyUrl" value="${payInfoConfig.notifyUrl }"
					htmlEscape="false" maxlength="255" /></li>
			<li><label class="control-label">备注:</label> <form:input
					path="remarks" value="${payInfoConfig.remarks }" htmlEscape="false"
					maxlength="50" /></li>
			<li class="clearfix"></li>
		</ul>
		<ul class="ul-form" id="exInfo_1" name="exInfos">
			<li><label class="control-label">扩展信息:</label> <form:input
					path="exInfo_key" htmlEscape="false" maxlength="255" /> <span
				class="help-inline"><font color="black"
					style="font-weight: bold;">:</font></span> <form:input path="exInfo_value"
					htmlEscape="false" /></li>
			<li class="btns" name="add"><input class="btn btn-primary"
				type="submit" value="+" onclick="return addExInfo();" /></li>
			<li class="clearfix"></li>
		</ul>
		<ul class="ul-form">
			<li class="btns" id="liAdd" style="display: none"><input
				id="btnAdd" class="btn btn-primary" type="submit" value="新增"
				onclick="return add();" /></li>
			<li class="btns"><input id="btnSave" class="btn btn-primary"
				type="submit" value="添加" onclick="return save();" /></li>
			<li class="btns"><input id="btnSearch" class="btn btn-primary"
				type="submit" value="查询" onclick="return page();" /></li>
		</ul>
	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed" style="word-break:break-all; word-wrap:break-word;" >
		<thead>
			<tr>
				<th class="sort-column ckAppId">游戏名称</th>
				<th class="sort-column childCkAppId">子游戏id</th>
				<th class="sort-column channelId">渠道名称</th>
				<th class="sort-column paytype">支付方式</th>
				<th class="sort-column appid">APPID</th>
				<th class="sort-column appkey">APPKEY</th>
				<th>回调地址</th>
				<th>扩展信息</th>
				<th>备注</th>
				<shiro:hasPermission name="app:payInfoConfig:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="payInfoConfig">
				<tr>
					<td style="width: 10%">${payInfoConfig.ckappname}(${payInfoConfig.ckAppId})</td>
					<td style="width: 10%">${payInfoConfig.childCkAppId }</td>
					<td style="width: 10%">${payInfoConfig.channelName}</td>
					<td style="width: 10%">${payInfoConfig.payTypeName}</td>
					<td style="width: 10%">${payInfoConfig.appid}</td>
					<td style="width: 10%">${payInfoConfig.appkey}</td>
					<td style="width: 10%">${payInfoConfig.notifyUrl}</td>
					<td style="width: 10%">${payInfoConfig.exInfo}</td>
					<td>${payInfoConfig.remarks}</td>
					<shiro:hasPermission name="app:payInfoConfig:edit">
						<td><a
							href="${ctx}/app/payInfoConfig/updateForm?id=${payInfoConfig.id}">修改</a>
							<a href="${ctx}/app/payInfoConfig/delete?id=${payInfoConfig.id}"
							onclick="return confirmx('确认要删除该配置吗？', this.href)">删除</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>