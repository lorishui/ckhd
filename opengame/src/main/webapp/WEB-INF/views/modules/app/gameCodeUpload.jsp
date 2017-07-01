<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>消耗管理</title>
<meta name="decorator" content="default"/>
<script type="text/javascript">
	$(document).ready(function() {
		$("#btnImport").click(function(){
			$.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true}, 
				bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"});
		});
	});
	function page(n, s) {
		if (n)
			$("#pageNo").val(n);
		if (s)
			$("#pageSize").val(s);
		$("#searchForm").attr("action", "${ctx}/game/gamecode/importweb");
		$("#searchForm").submit();
		return false;
	}
 	function changeCkAppId(){
 		var path = "${ctx}"
		var ckApp = document.getElementById("ckAppId");
		var index = ckApp.selectedIndex;
		var ckAppId = ckApp.options[index].value;
		var remark = document.getElementById("remark").value;
		document.getElementById("importForm").setAttribute("action","${ctx}/game/gamecode/import?ckAppId="+ckAppId+"&remark="+encodeURI(encodeURI(remark)))
		var a = $("#importForm").attr("action")
	} 
</script>
</head>
<body>
	<div id="importBox" class="hide">
		<form id="importForm" action="" method="post" enctype="multipart/form-data"
			class="form-search" style="padding-left:20px;text-align:center;" onsubmit="loading('正在导入，请稍等...');"><br/>
			<input id="uploadFile" name="file" type="file" style="width:330px"/><br/><br/>　　
			<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>
			<a href="${ctx}/game/gamecode/import/template">下载模板</a>
		</form>
	</div>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/game/gamecode/importweb">礼包码上传</a></li>
		<li><a href="${ctx}/game/gamecode/validweb">礼包码管理</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="gameCode"
		action="" method="post"
		class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}"
			callback="page();" />
			<label>游戏名称:</label>
				<form:select path="ckAppId" class="input-medium" onchange="changeCkAppId()">
					<form:option value="" label="(全部)" />
					<c:forEach var="app" items="${allCkApp}" varStatus="status">
						<form:option value="${app.appId}" label="${app.name}" />
					</c:forEach>
				</form:select>
			<input id="btnSubmit" class="btn btn-primary"
				type="submit" value="查询" onclick="return page();" />
			<label>礼包码总数:</label>${data[0].total}&nbsp;&nbsp;&nbsp;
			<label>未发放数:</label>${data[0].total - data[0].used}
	</form:form>
	<label>备注:</label>
		<input id = "remark" required="required" onchange="changeCkAppId()"/>
	<input id="btnImport" class="btn btn-primary" type="button" value="导入"/><font color="#FF0000">  导入前请先选择游戏并填写备注</font>
	<sys:message content="${message}"/>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th class="sort-column login_name">创酷APP</th><th>礼包码</th><th class="sort-column name">使用状态</th></thead>
		<tbody>
		<c:forEach items="${page.list}" var="gc">
			<tr>
				<td>${gc.ckAppId}</td>
				<td>${gc.code}</td>
				<td>${gc.status eq '0'?"未发放":"已发"}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>