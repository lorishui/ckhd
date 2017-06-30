<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>APP管理</title>
<meta name="decorator" content="default" />

<script type="text/javascript"
	src="/static/jquery-ztree/3.5.12/js/jquery.ztree.all-3.5.min.js"></script>
<link rel="stylesheet"
	href="/static/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.min.css"
	type="text/css">
<script type="text/javascript">
<!--
	var setting = {
		check : {
			enable : true
		},
		data : {
			simpleData : {
				enable : true
			}
		}
	};

	var zNodes =<%=request.getAttribute("zTree")%>;

	$(document).ready(function() {
		$.fn.zTree.init($("#zTree"), setting, zNodes);
		var zTree = $.fn.zTree.getZTreeObj("zTree");
		type = {
			"Y" : "ps",
			"N" : "ps"
		};
		zTree.setting.check.chkboxType = type;
	});

	function saveCfg() {
		var zTree = $.fn.zTree.getZTreeObj("zTree");
		var nodes = zTree.getCheckedNodes(true);
		var v = ",";

		for (var i = 0; i < nodes.length; i++) {
			v += nodes[i].id + ",";
		}
		if(v === ','){
			v = "";
		}
		$("#zonelist").val(v);
		$("#form1").submit();
	}
//-->
</script>
</head>
<body>
	<h3>
	<%
	String type = request.getParameter("type");
	if("1".equals(type)){
		out.println("红区配置：");
	}else{
		out.println("黄区配置：");
	}
	%></h3>
	<a href="#" onclick="saveCfg();return false">保存配置</a>
	<span style="color:red">${message}</span>
	<form id="form1" name="form1" action="${ctx}/app/zonetree/cfg" method="post">
		<input id="id" name="id" type="hidden" value="<%=request.getParameter("id") %>" />
		<input id="type" name="type" type="hidden" value="<%=request.getParameter("type") %>" />
		<input id="zonelist" name="zonelist" type="hidden" />
	</form>
	<div class="content_wrap">
		<div id="redzone" class="zTreeDemoBackground left">
			<ul id="zTree" class="ztree"></ul>
		</div>
	</div>

</body>
</html>