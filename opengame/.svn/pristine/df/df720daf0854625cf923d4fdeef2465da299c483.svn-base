<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default"/>
<title>redis清除页面</title>
<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.11.1/lib/jquery.js"></script>
<script src="${ctxStatic}/jquery-validation/1.11.1/lib/jquery.form.js" type="text/javascript"></script>
<script type="text/javascript">
	function saveReport() { 
		// jquery 表单提交 
		$("#form").ajaxSubmit(function(data) {
			if(data == ""){
				$("#data").html("<font color='red'>没有数据</font>");
			}else{
				$("#data").html("数据:<font color='red'>"+data+"</font>");
			}
		}); 
		return false; // 必须返回false，否则表单会自己再做一次提交操作，并且页面跳转 
	} 
</script>
</head>
<body>
	<form id="form" method="post" action="${ctx }/sys/redis/del" onsubmit="return saveReport();">
		<table>
			<tr>
				<td>key:</td><td><textarea name="key" rows="3" cols="4"></textarea></td>
			</tr>
			<tr>
				<td><td><input type="submit" value="清除" /></td>
			</tr>
		</table>
		<div id="data"></div>
	</form>
</body>
</html>