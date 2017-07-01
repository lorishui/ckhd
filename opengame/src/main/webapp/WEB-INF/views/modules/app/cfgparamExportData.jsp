<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>配置文件数据导入</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	$(function(){
		$("#btnSubmit").click(function(){
			/* $("#form1").attr("action","${ctx}/app/download?ckAppId=1023&rqType=rqType");
			$("#form1").submit(); */
			 var options  = {
                url : '${ctx}/app/download',
                type:'post'
              };
              $("#form1").ajaxSubmit(options);
		});
	});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/app/list">配置文件列表</a></li>
		<li class="active"><a href="${ctx}/app/exportData">配置导入</a></li>
	</ul>
	<form id="form1" action="${ctx}/app/download">
		&nbsp;&nbsp;&nbsp;&nbsp;<label>游戏：</label>
		<select id="ckAppId" name="ckAppId" class="input-medium" >
			<c:forEach items="${ck}" var="ck">
				<option value="${ck.ckappId}">${ck.name}</option>
			</c:forEach>
	    </select>
	    <label>类型：</label>
		<select id="rqType" name="rqType" class="input-medium">
			<c:forEach items="${rq}" var="rq">
				<option value="${rq.value}">${rq.value}</option>
			</c:forEach>
	    </select>
	    &nbsp;&nbsp;&nbsp;&nbsp;<button id="btnSubmit" class="btn btn-primary" value="下载">下载</button>
    </form>
    
	<form action="${ctx}/app/upload" method="post" enctype="multipart/form-data">
		&nbsp;&nbsp;&nbsp;&nbsp;<input id="file" type="file" name="file"  accept=".xlsx"  />
		<input type="submit" value="上传">
	</form>
</body>
</html>