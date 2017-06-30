<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>角色管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<script type="text/javascript">
		$(function(){
			$("#all").toggle(function(){
				$("input[name='ckappIds']").each(function(){
	   				$(this).attr("checked",true);
	  			});
	  			$("#all").val("取消");
			},function(){
				$("input[name='ckappIds']").each(function(){
	   				$(this).attr("checked",false);
	  			});
	  			$("#all").val("全选");
			});
			
			$("#channelAll").toggle(function(){
				$("input[name='channelIds']").each(function(){
	   				$(this).attr("checked",true);
	  			});
	  			$("#channelAll").val("取消");
			},function(){
				$("input[name='channelIds']").each(function(){
	   				$(this).attr("checked",false);
	  			});
	  			$("#channelAll").val("全选");
			});
		});
		$(document).ready(function(){
			$("#name").focus();
			$("#inputForm").validate({
				rules: {
					name: {remote: "${ctx}/sys/role/checkName?oldName=" + encodeURIComponent("${role.name}")},
					enname: {remote: "${ctx}/sys/role/checkEnname?oldEnname=" + encodeURIComponent("${role.enname}")}
				},
				messages: {
					name: {remote: "角色名已存在"},
					enname: {remote: "英文名已存在"}
				},
				submitHandler: function(form){
					var ids = [], nodes = tree.getCheckedNodes(true);
					for(var i=0; i<nodes.length; i++) {
						ids.push(nodes[i].id);
					}
					$("#resourceIds").val(ids);
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});

			var setting = {check:{enable:true,nocheckInherit:true},view:{selectedMulti:false},
					data:{simpleData:{enable:true}},callback:{beforeClick:function(id, node){
						tree.checkNode(node, !node.checked, true, true);
						return false;
					}}};
			
			// 用户-资源
			var zNodes=[
					<c:forEach items="${resourceList}" var="menu">{id:"${menu.id}", pId:"${not empty menu.parent.id?menu.parent.id:0}", name:"${not empty menu.parent.id?menu.name:'权限列表'}"},
		            </c:forEach>];
			// 初始化树结构
			var tree = $.fn.zTree.init($("#menuTree"), setting, zNodes);
			// 不选择父节点
			tree.setting.check.chkboxType = { "Y" : "ps", "N" : "s" };
			// 默认选择节点
			var ids = "${role.resourceIds}".split(",");
			for(var i=0; i<ids.length; i++) {
				var node = tree.getNodeByParam("id", ids[i]);
				try{tree.checkNode(node, true, false);}catch(e){}
			}
			// 默认展开全部节点
			tree.expandAll(true);
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/role/">角色列表</a></li>
		<li class="active"><a href="${ctx}/sys/role/form?id=${role.id}">角色<shiro:hasPermission name="sys:role:edit">${not empty role.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:role:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="role" action="${ctx}/sys/role/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		 
		<div class="control-group">
			<label class="control-label">角色名称:</label>
			<div class="controls">
				<input id="oldName" name="oldName" type="hidden" value="${role.name}">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">英文名称:</label>
			<div class="controls">
				<input id="oldEnname" name="oldEnname" type="hidden" value="${role.enname}">
				<form:input path="enname" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> 英文名</span>
			</div>
		</div>
		 
		<div class="control-group">
			<label class="control-label">是否可用:</label>
			<div class="controls">
				<form:select path="isabled">
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline">“是”代表此数据可用，“否”则表示此数据不可用</span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">可访问游戏:</label>
			<div class="controls">
				<input type="button" value="全 选" id="all" name="all"/>
				<c:forEach items="${appCkList}" var="appCk" varStatus="status">
					<c:choose>
						<c:when test="${ckAppIdsList.contains(appCk.ckappId)}">
							<input type="checkbox" name="ckappIds" checked value="${appCk.ckappId}"/>${appCk.name}(${appCk.ckappId})
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="ckappIds" value="${appCk.ckappId}"/>${appCk.name}(${appCk.ckappId})
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</div>
		</div>
		 <div class="control-group">
			<label class="control-label">可访问渠道:</label>
			<div class="controls">
				<input type="button" value="全 选" id="channelAll" name="channelall"/>
				<c:forEach items="${channelList}" var="channel" varStatus="status">
				<c:choose>
					<c:when test="${channelIdsList.contains(channel.id)}">
						<input type="checkbox" name=channelIds checked value="${channel.id}"/>${channel.name}(${channel.id})
					</c:when>
					<c:otherwise>
						<input type="checkbox" name=channelIds value="${channel.id}"/>${channel.name}(${channel.id})
					</c:otherwise>
					</c:choose>
				</c:forEach>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">角色授权:</label>
			<div class="controls">
				<div id="menuTree" class="ztree" style="margin-top:3px;float:left;"></div>
				<form:hidden path="resourceIds"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<div class="form-actions">
			<c:if test="${fns:getUser().admin}">
				<shiro:hasPermission name="sys:role:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			</c:if>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>