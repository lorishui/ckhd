<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>支付通道切换管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	function page(n, s) {
		if(ckdate()){
			if (n)
				$("#pageNo").val(n);
			if (s)
				$("#pageSize").val(s);
			$("#searchForm").attr("action", "${ctx}/app/payrules/search");
			$("#searchForm").submit();
		}
		
		return false;
	}

	function saveForm() {
		if(validata() && ckdate()){
			$("#searchForm").attr("action", "${ctx}/app/payrules/save?type=1");//1:更新
			$("#searchForm").submit();
		}
		return false;
	}

	function validata() {
		var selectIndex = $("#ckappId").get(0).selectedIndex;
		if (selectIndex < 0) {
			alert("游戏名称不能为空!");
			return false;
		}

		var endtime = $('#endTime').val();
		var starttime = $('#startTime').val();
		if((endtime==null || endtime=="") && (starttime==null || starttime =="")){
			alert("时间段不能为空!");
			return false;
		}
		return true;
	}
	
	function loadIfram(id){
		
		var lastClientId = $("#clientId").val();
		
		$("#"+lastClientId).children().each(function() {
			$(this).css("background-color", "");
		});
		
		$("#"+id).children().each(function() {
			$(this).css("background-color", "#FFFFBB");
		});
		
		$("#clientId").val(id);
		$("#myFrame").attr("src","${ctx}/app/payrules/provinceConfig?id="+id);
	}
	
	function ckdate() {
		var endtime = $('#endTime').val();
		var starttime = $('#startTime').val();
		var start = new Date(starttime.replace("-", "/").replace("-", "/"));
		var end = new Date(endtime.replace("-", "/").replace("-", "/"));
		if (end < start) {
			alert("结束日期不能小于开始日期！");
			return false;
		} else {
			return true;
		}
	}
	
	$(document).ready(function() {
		
		var result = "${message}"; 
		if(result!="" && result!=null){
			$("#msg").css("visibility","visible");
	    	$("#msg").focus();
	    	$("#msg").blur(function(){
	    		$(this).css("visibility","hidden");
			});
		}
		var id= "${payRules.id}";
		var isSave = "${payRules.isSave}";
		if(id!=""){
			changeAPPID(true,true);
			$("#btnSubmit").val("保存");
			$("#btnSubmit").attr("onclick","return saveForm()");
			$("#btnBack").val("返回");
			$("#btnBack").attr("onclick","return back()");
			$("#btnBack").show();
			$("#btnAdd").show();
			$("#btnSearch").hide();
		}else{
			changeAPPID(true,true,isSave);
			$("#btnSave").val("保存");
			$("#btnSave").attr("onclick","return add()");
		}
		
	});
	
	function add(){
		$("#searchForm").attr("action", "${ctx}/app/payrules/save?type=0");//0:新增
		$("#searchForm").submit();
		return false;
	}
	
	function back(){
		$("#searchForm").attr("action", "${ctx}/app/payrules/back");
		$("#searchForm").submit();
		return false;
	}
	
	function changeAPPID(pageLoad,update,isSave) {
		$.ajax({
			url : '${ctx}/stats/queryAppCarriers',
			type : 'get',
			data : 'ckAppId=' + $("#ckappId").val() + '&carriersType=MM',
			error : function() {
			},
			success : function(data) {
				$("#appid").empty();
				$("#appid").append("<option value='' selected>(全部)</option>");
				for ( var item in data) {
					$("#appid").append(
							"<option value='"+data[item].appId +"'>"
									+ data[item].appName + "("
									+ data[item].appId + ")</option>");
				}
				if (pageLoad) {
					$("#appid").val("${mmapporder.appId}");
					$(".select2-chosen")[1].innerHTML = $("#appId option:selected").text();
					if(update){
						chanageCarrierAppId(isSave);
					}
				} else {
					$("#appid").val("");
					$(".select2-chosen")[1].innerHTML = "(全部)";
				}
			}
		});
	}
	function chanageCarrierAppId(isSave){
		var appid = "${payRules.appid}"
		var ckappId = "${payRules.ckappId}";
		$("#ckappId option").each(function() {
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
		<li class="active"><a href="${ctx}/app/payrules/list">支付通道切换列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="payRules"
		action="${ctx}/app/payrules/search" method="post"
		class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<input type="hidden" id="id" name="id" value="${payRules.id}">
		<input type="hidden" id="addCkAppId" name="addCkAppId" value="${payRules.ckappId}">
		<input type="hidden" id="addAppId" name="addAppId" value="${payRules.appid}">
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}"
			callback="page();" />
		<ul class="ul-form">
			<li><label>游戏名称<span class="help-inline"><font color="red">*</font> </span>：</label>
			<form:select id="ckappId" path="ckappId" disabled="${not empty payRules.id?true:false}" class="input-medium" onChange="changeAPPID()">
				<form:options items="${fns:getAPPCkList()}" itemLabel="name"
					itemValue="ckappId" htmlEscape="false"  />
			</form:select>
			 <form:select id="appid" path="appid"
					class="input-medium" style="width:300px">
			</form:select>
			<span>说明：1、渠道+版本+MMAPPID；2、渠道空+版本+MMAPPID；</span>
			</li>
			<li class="clearfix"></li>
			<li><label>渠道名称：</label> <form:select id="channelId"
					path="channelId"
					class="input-medium">
					<form:option value="" label="(全部)"/>
					<form:options items="${fns:getChannelList()}" itemLabel="name"
						itemValue="id" htmlEscape="false" />
				</form:select></li>
			<li><label>包版本：</label>
			<form:input path="version" name="version" id="version" htmlEscape="false" maxlength="50"
					class="input-medium" /></li>
			<li class="btns">
			<input id="btnSearch" class="btn btn-primary"
				type="submit" value="查询" onclick="return page();" /><span>&nbsp;&nbsp;3、渠道+版本+MMAPPID空；4、渠道空+版本+MMAPPID空；</span></li>
			<li class="clearfix"></li>
			<li><label>时间段：</label> 
				<form:input path="startTime"
					name="startTime" htmlEscape="false" maxlength="50"
					class="WdateTime required valid"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false});" />--
					<form:input
					path="endTime" name="endTime" htmlEscape="false" maxlength="50"
					class="WdateTime required valid"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false});" />
				</li>
			<li class="btns">
				<input id="btnAdd" style="display: none" class="btn btn-primary"
				type="submit" value="新增" onclick="return add();" />
				<input id="btnSubmit" class="btn btn-primary"
				type="submit" value="保存" onclick="return add();" />
				<input id="btnBack" style="display: none" class="btn btn-primary"
				type="submit" value="返回" />
				<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5、渠道+版本+MMAPPID全部空</span>
				</li>
				
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<div tabindex="0" id="msg" style="visibility:hidden;width: 100%;text-align:center;background-color: #d5ecbf">
		<font size="5px" style="font-weight:bold;text-align:center; vertical-align:middle; ">${message}</font>
	</div>
	<input id="clientId" type="hidden" value=""/>
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>创酷游戏名称</th>
				<th>游戏名称</th>
				<th>渠道名称</th>
				<th>包版本</th>
				<th>时间段</th>
				<th>状态</th>
				<shiro:hasPermission name="app:payrules:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="payrules">
				<tr id="${payrules.id}">
					<td>${payrules.ckAppName}(${payrules.ckappId})</td>
					<td>
						<c:if test="${payrules.appName!=null}">
							${payrules.appName}(${payrules.appid })
						</c:if>
					</td>
					<td>
						<c:if test="${payrules.channelName!=null}">
							${payrules.channelName}(${payrules.channelId})
						</c:if>
					</td>
					<td>${payrules.version}</td>
					<td>
					<c:choose>
						<c:when test="${payrules.isLenght}">
							<a onclick="loadIfram('${payrules.id}')">从${payrules.startTime}至今</a>
						</c:when>
						<c:otherwise>
							<a onclick="loadIfram('${payrules.id}')">从${payrules.startTime}至${payrules.endTime}</a>
						</c:otherwise>
					</c:choose>
					<td>
						<c:choose>
							<c:when test="${payrules.isLose}">
								生效
							</c:when>
							<c:otherwise>
								<font color="red">失效</font>
							</c:otherwise>
						</c:choose>
					</td>
					<shiro:hasPermission name="app:payrules:edit">
						<td><%-- <a href="${ctx}/app/payrules/getPayway?id=${payrules.id}&ckappId=1002&channelId=10&version=45&simNO=89860081191435323993&carriers=CMCC">获取json串</a>
 							<a href="${ctx}/sys/dict/saveTemp">存储province</a>--%>
 							<a href="${ctx}/app/payrules/copy?id=${payrules.id}">复制</a>
							<a href="${ctx}/app/payrules/getAllPayway?id=${payrules.id}">获取JSON</a>
							<a href="${ctx}/app/payrules/updateForm?id=${payrules.id}">修改</a>
 								<a href="${ctx}/app/payrules/delete?id=${payrules.id}&ckappId=${payrules.ckappId}"
							onclick="return confirmx('确认要删除该时间段的支付通道切换配置吗？', this.href)">删除</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	
	<iframe id="myFrame" marginwidth="0" marginheight="0" frameborder="0" scrolling="no"  width="100%" height="1700px" />
</body>
</html>