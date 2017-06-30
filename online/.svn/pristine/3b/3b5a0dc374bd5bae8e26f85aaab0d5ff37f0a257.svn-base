<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
<title>新玩家领Q币活动</title>
</head>
<body>
	<%
		String resultCode = (String)request.getAttribute("resultCode");
		if (resultCode == null) {
	%>
	<form action="${ctx}/app/qqActivityForm" method="post">
		<table>
			<tr>
				<td>请填写您的QQ号！</td>
			</tr>
			<tr>
				<td>QQ号码：<input type="text" name="qq"></td>
			</tr>
			<tr>
				<td style="color: red">*请核对您的QQ号码，确认后将无法修改</td>
			</tr>
			<tr>
				<td><input type="submit" value="确认" >
				<input type="hidden" name="ckAppId" value="1001"></td>
			</tr>
		</table>
	</form>
	<%
		} else {
	%>
	<span style="color:red">您的QQ号码提交成功！</span>
	<%
		}
	%>
</body>
</html>