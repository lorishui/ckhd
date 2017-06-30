<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default"/>
<title>qq活动导出数据</title>
<script type="text/javascript">
	Date.prototype.format = function(format){ 
		var args = { "M+" : this.getMonth() + 1,
				"d+" : this.getDate(), 
				"h+" : this.getHours(), 
				"m+" : this.getMinutes(), 
				"s+" : this.getSeconds(), 
				"q+" : Math.floor((this.getMonth() + 3) / 3),  //quarter
				"S" : this.getMilliseconds() }; 
		if(/(y+)/.test(format)) format = format.replace(RegExp.$1,(this.getFullYear() + "").substr(4 - RegExp.$1.length)); 
		for(var i in args) { 
			var n = args[i]; 
			if(new RegExp("("+ i +")").test(format)) format = format.replace(RegExp.$1,RegExp.$1.length == 1 ? n : ("00" + n).substr(("" + n).length)); 
		} 
		return format;
	};
	
	$(function(){
		$("#time").val(new Date().format("yyyy-MM-dd"));
		$("#time2").val(new Date().format("yyyy-MM-dd"));
	});
</script>
</head>
<body>
	<form:form action="${ctx }/app/down" method="post" modelAttribute="qqAct">
		<table>
			<tr>
				<td>创酷appid：
					<form:select name="ckAppId" path="ckAppId" class="input-medium">
					<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
					</form:select> 
				</td>
				<td>开始时间：<input id="time" name="startTime" type="text" class="WdateTime"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/></td>
				<td>结束时间：<input id="time2" name="endTime" type="text" class="WdateTime" 
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/></td>
				<td><input type="submit" value="导出"/></td>
			</tr>
		</table>
	</form:form>
</body>
</html>