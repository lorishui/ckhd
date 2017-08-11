<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<title>test page</title>
	<meta charset="utf-8"/>
</head>
<body>
<%
response.sendRedirect(request.getContextPath() + "/yr/login");
%>

</body>
</html>