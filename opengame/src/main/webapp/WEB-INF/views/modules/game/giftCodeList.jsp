<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>礼包码查看</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(function(){
			getGiftId();
		});
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/game/giftCode/list");
			$("#searchForm").submit();
	    	return false;
	    }
		function exportData(){
			var batch = $("#batch").val();
			var ckAppId = $("#ckAppId").val();
			if(batch != '' && ckAppId != '' ){
				$("#searchForm").attr("action","${ctx}/game/giftCode/download");
				$("#searchForm").submit();	
			}else{
				alert("游戏或者批次没填！");
			}
	    	return false;
	    }
		
		function getGiftId(){
			var ckAppid = $("#ckAppId").val();
			var giftId ='${giftCode.giftId}';
			 $.ajax({
				 type: 'POST',
				  url:  "${ctx}/game/gift/getList",
				  data: "ckAppId="+ckAppid,
				  datatype:"json",
				  success: function(data){
					 		var node = $("#giftId");
					 		node.empty();
					 		var html = "<option value=\"\">全部</option>";
					 		for(var n=0;n<data.length;n++){
					 			if( giftId != null && data[n].id==giftId){
					 				html+="<option value='"+data[n].id+"' selected >"+data[n].name+"_"+data[n].id+"</option>";
					 			}else{
					 				html+="<option value='"+data[n].id+"'>"+data[n].name+"_"+data[n].id+"</option>";
					 			}
					 		}
					 		node.append(html);
					 		node.val(giftId);
					 		var name = node.find("option:selected").text();
					 		if( giftId != null && giftId != '' ){
					 			$(".select2-chosen")[1].innerHTML = name;	
					 		}
						  }
				 });
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/game/giftCode/list">礼包码列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="giftCode" action="${ctx}/game/giftCode/list" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label>创酷appid：</label>
				<form:select path="ckAppId" class="input-medium" id="ckAppId" onchange="getGiftId()">
						<form:option value="" label="(全部)"/>
						<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
				</form:select> </li>
			<%-- <form:input path="ckappId" htmlEscape="false" maxlength="50" class="input-medium"/> --%></li>
			<li><label>礼包类型：</label>
				<select name="giftId" class="input-medium"  id="giftId">
					<option value="" >全部</option>
				</select> </li>
				<li><label>礼包类型：</label>
				<select name="giftId" class="input-medium"  id="giftId">
					<option value="" >全部</option>
				</select> </li>
			<li class="clearfix"></li>
			<li><label>渠道：</label>
				<form:select id="channelId" path="ckChannelId" class="input-medium">
					<form:option value="" label="(全部)" />
					<form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
		   	</form:select></li>
			<li><label>批次：</label><form:input id="batch" path="batch" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li class="clearfix"></li>
			<li><label>imei：</label><form:input path="imei" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li><label>code：</label><form:input path="code" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/></li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="导出" onclick="return exportData();"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th class="sort-column a.ckAppId">创酷appid</th>
				<th>礼包名称</th>
				<th>渠道</th>
				<th>礼包码</th>
				<th>使用者</th>
				<th>使用时间</th>
				<th>有效时间起</th>
				<th>有效时间止</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="giftCode">
			<tr>
				<td>${fns:getByCkAppName(giftCode.ckAppId)}(${giftCode.ckAppId })</td>
				<td>${giftCode.name}</td>
				<td>${fns:findChannelName(giftCode.ckChannelId,'')}</td>
				<td>${giftCode.code }</td>
				<td>${giftCode.imei}</td>
				<td>${giftCode.updateTime}</td>
				<td>${giftCode.startTime}</td>
				<td>${giftCode.endTime}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>