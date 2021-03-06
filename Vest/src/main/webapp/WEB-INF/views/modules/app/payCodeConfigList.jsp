<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>网络计费点配置</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				var result = "${message}"; 
				if(result!="" && result!=null){
					$("#msg").css("visibility","visible");
			    	$("#msg").focus();
			    	$("#msg").blur(function(){
			    		$(this).css("visibility","hidden");
					});
				}
				var id= "${payCodeConfig.id}";
				if(id!=""){
					changeAPPID(true,true);
					$("#btnSave").val("保存");
					$("#btnSearch").val("返回");
					$("#btnSearch").attr("onclick", "return back()");
					$("#liAdd").show();
				}else{
					changeAPPID(true,true);
					$("#btnSave").val("保存");
					$("#btnSave").attr("onclick","return add()");
				}
			});
	function add() {
		$("#id").val("");
		$("#searchForm").attr("action", "${ctx}/app/payCodeConfig/save");
		$("#searchForm").submit();
		return false;
	}

	function back() {
		$("#searchForm").attr("action", "${ctx}/app/payCodeConfig/back");
		$("#searchForm").submit();
		return false;
	}
	function page(n, s) {
		if (n)
			$("#pageNo").val(n);
		if (s)
			$("#pageSize").val(s);
		$("#searchForm").attr("action", "${ctx}/app/payCodeConfig/search");
		$("#searchForm").submit();
		return false;
	}

	function save() {
		if (!validate()) {
			return false;
		}
		$("#searchForm").attr("action", "${ctx}/app/payCodeConfig/save");
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
			/* var channelId = $("#channelId").get(0).selectedIndex;
			if (channelId == 0) {
				alert("请选择渠道!");
				$("#channelId").focus();
				return false;
			} */
		}

		var version = $("#version").val();
		if (version == "") {
			alert('请填写版本号!');
			$("#version").focus();
			return false;
		}

		var productId = $("#productId").val();
		if (productId == "") {
			alert('请填写计费点编码!');
			$("#productId").focus();
			return false;
		}

		var productName = $("#productName").val();
		if (productName == "") {
			alert('请填写计费点名称!');
			$("#productName").focus();
			return false;
		}

		var price = $("#price").val();
		if (price == "") {
			alert('请填写计费价格!');
			$("#price").focus();
			return false;
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
	
	function changeAPPID(pageLoad,update) {
		 var data = $("#ckAppId").val();
		 var id = '${payCodeConfig.childCkAppId}';
		 $.ajax({
			 type: 'POST',
			  url:  "${ctx}/app/appck/getChildIdList",
			  data: "ckappId="+data,
			  datatype:"json",
			  success: function(data){
				var node = $("#childCkAppId");
				node.empty();
				var html = "<option value=\"\">全部</option>";
				for(var n=0;n<data.length;n++){
					html+="<option value='"+data[n].childId+"'>"+data[n].name+"_"+data[n].childId+"</option>";
				}
				node.append(html);
				node.val(id);
				var name = node.find("option:selected").text();
		 		if( id != null && id != '' ){
		 			$(".select2-chosen")[1].innerHTML = name;	
		 		}
			  }
			 });
	}
	function changeChildCkAppId(){
		var childCkAppId = "${payCodeConfig.childCkAppId}"
		var ckappId = "${payCodeConfig.ckAppId}";
		$("#ckAppId option").each(function() {
			if (ckappId != "") {
				if ($(this).val() == ckappId) {
					$(this).attr("selected", true);
					$(this).parent("select").prev()
					.children("a").children(".select2-chosen").html($(this).html());
				}
			}
		});
		$("#appid option").each(function() {
			if ($(this).val() == appid) {
				$(this).attr("selected", true);
				$(this).parent("select").prev()
				.children("a").children(".select2-chosen").html($(this).html());
			}
		});
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/app/payCodeConfig/search">支付信息列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="payCodeConfig"
		action="${ctx}/app/payCodeConfig/search" method="post"
		class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<input id="id" type="hidden" name="id" value="${payCodeConfig.id}">
		<input id="addckAppId" type="hidden" name="addckAppId"
			value="${payCodeConfig.ckAppId}">
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}"
			callback="page();" />
		<ul class="ul-form">
			<li><label class="control-label">游戏：</label> <form:select
					id="ckAppId" path="ckAppId" class="input-medium"
					onChange="changeAPPID()">
					<form:options items="${fns:getAPPCkListByChild()}" itemLabel="name"
						itemValue="ckappId" htmlEscape="false" />
				</form:select> 
				<select id="childCkAppId" name="childCkAppId"
					class="input-medium" style="width:300px">
				</select> <span class="help-inline"><font color="red">*</font></span></li>
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
			<li><label class="control-label">版本:</label> <form:input
					path="version" value="${payCodeConfig.version }" htmlEscape="false"
					maxlength="50" class="required" /> <span class="help-inline"><font
					color="red">*</font> </span></li>
			<li><label class="control-label">计费点编码:</label> <form:input
					path="productId" value="${payCodeConfig.productId }"
					htmlEscape="false" maxlength="50" class="required" /> <span
				class="help-inline"><font color="red">*</font> </span></li>
			<li class="clearfix"></li>
		</ul>
		<ul class="ul-form">
			<li><label class="control-label">计费点名称:</label> <form:input
					path="productName" value="${payCodeConfig.productName }"
					htmlEscape="false" maxlength="50" class="required" /> <span
				class="help-inline"><font color="red">*</font> </span></li>
			<li><label class="control-label">计费价格:</label> <form:input
					path="price"
					onblur="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))"
					value="${((payCodeConfig.price==null || payRulesConfig.cucc_money=='')?'0': payCodeConfig.price)}"
					htmlEscape="false" maxlength="50" class="required"
					data-constraints='@Digits(integer=5, fraction=0)' /> <span
				class="help-inline"><font color="red">*</font> </span></li>
			<li class="clearfix"></li>
		</ul>
		<ul class="ul-form">
			<li><label class="control-label">备注:</label> <form:input
					path="remarks" value="${payCodeConfig.remarks }" htmlEscape="false"
					maxlength="50" /></li>
			<li class="clearfix"></li>
		</ul>
		<ul class="ul-form" id="exInfo_1" name="exInfos">
			<li><label class="control-label">扩展信息:</label> <form:input
					path="exInfo_key" htmlEscape="false" maxlength="20" /> <span
				class="help-inline"><font color="black"
					style="font-weight: bold;">:</font></span> <form:input path="exInfo_value"
					htmlEscape="false" maxlength="255" /></li>
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
	<div tabindex="0" id="msg" style="visibility:hidden;width: 100%;text-align:center;background-color: #d5ecbf">
		<font size="5px" style="font-weight:bold;text-align:center; vertical-align:middle; ">${message}</font>
	</div>
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th class="sort-column ckAppId">游戏名称</th>
				<th class="sort-column appid">子游戏id</th>
				<th class="sort-column channelId">渠道名称</th>
				<th class="sort-column paytype">支付方式</th>
				<th class="sort-column version">版本号</th>
				<th>计费点编码</th>
				<th>计费点名称</th>
				<th>计费价格(单位分)</th>
				<th>扩展信息</th>
				<th>备注</th>
				<shiro:hasPermission name="app:payCodeConfig:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="payCodeConfig">
				<tr>
					<td>${payCodeConfig.appName}(${payCodeConfig.ckAppId})</td>
					<td>${payCodeConfig.childCkAppId}</td>
					<td style="width: 10%">${payCodeConfig.channelName}</td>
					<td style="width: 10%">${payCodeConfig.paytypeName}</td>
					<td>${payCodeConfig.version}</td>
					<td>${payCodeConfig.productId}</td>
					<td>${payCodeConfig.productName}</td>
					<td>${payCodeConfig.price}</td>
					<td>${payCodeConfig.exInfo}</td>
					<td>${payCodeConfig.remarks}</td>
					<shiro:hasPermission name="app:payCodeConfig:edit">
						<td>
							<a href="${ctx}/app/payCodeConfig/updateForm?id=${payCodeConfig.id}&ckAppId=${payCodeConfig.ckAppId}">修改</a>
							<a href="${ctx}/app/payCodeConfig/delete?id=${payCodeConfig.id}&ckAppId=${payCodeConfig.ckAppId}"
							onclick="return confirmx('确认要删除该配置吗？', this.href)">删除</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>