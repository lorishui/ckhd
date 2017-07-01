<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>配置文件详细数据显示</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<table>
	<c:if test="${exInfo ne null }">
		<c:if test="${exInfo.mmSdkName ne null }">
			<tr>
				<td>SDK名称(mmSdkName)</td><td>${exInfo.mmSdkName }</td>
			</tr>
		</c:if>
		<c:if test="${exInfo.ackType ne null }">
			<tr>
				<td>类型(ackType)</td><td>${exInfo.ackType }</td>
			</tr>
		</c:if>
		<c:if test="${exInfo.oneAckTimes ne null }">
			<tr>
				<td>一次确认支付次数(oneAckTimes)</td><td>${exInfo.oneAckTimes }</td>
			</tr>
		</c:if>
		<c:if test="${exInfo.twoAckTimes ne null }">
			<tr>
				<td>二次确认支付次数(twoAckTimes)</td><td>${exInfo.twoAckTimes }</td>
			</tr>
		</c:if>
	</c:if>
	</table>
</body>
</html>