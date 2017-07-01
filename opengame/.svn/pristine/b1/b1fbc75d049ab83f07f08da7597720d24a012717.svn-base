<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>游戏报表</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	
</script>
</head>
<body>
	<table id="stats"
		class="table2excel table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th colspan="32" style="color:red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${fns:getByCkAppName(report.ckAppId)}(${report.ckAppId})(${report.date})统计情况-已去除破解数（总的新增活跃不含苹果的新增活跃）</th>
			</tr>
			<tr>
				<th onclick="$.sortTable.sort('stats',0)">渠道</th>
				<th onclick="$.sortTable.sort('stats',1,compareNumber)">新增</th>
				<th onclick="$.sortTable.sort('stats',2,compareNumber)">活跃</th>

				<th onclick="$.sortTable.sort('stats',3,compareNumber)">MM成功金额</th>
				<th onclick="$.sortTable.sort('stats',4,compareNumber)">咪咕成功金额</th>
				<th onclick="$.sortTable.sort('stats',5,compareNumber)">总成功金额</th>
				
				<th onclick="$.sortTable.sort('stats',6,compareNumber)">新增价值</th>
				<th onclick="$.sortTable.sort('stats',7,compareNumber)">活跃价值</th>
				<th onclick="$.sortTable.sort('stats',8,compareNumber)">付费apru</th>
				
				<th onclick="$.sortTable.sort('stats',9,compareNumber)">每活跃用户MM订单</th>
				<th onclick="$.sortTable.sort('stats',10,compareNumber)">每活跃用户成功MM订单</th>
				<th onclick="$.sortTable.sort('stats',11,compareNumber)">每活跃用户咪咕订单</th>
				<th onclick="$.sortTable.sort('stats',12,compareNumber)">每活跃用户成功咪咕订单</th>
				<th onclick="$.sortTable.sort('stats',13,compareNumber)">每活跃用户订单</th>
				<th onclick="$.sortTable.sort('stats',14,compareNumber)">每活跃用户成功订单</th>
				
				<th onclick="$.sortTable.sort('stats',15,compareNumber)">MM新增价值</th>
				<th onclick="$.sortTable.sort('stats',16,compareNumber)">MM活跃价值</th>
				<th onclick="$.sortTable.sort('stats',17,compareNumber)">咪咕新增价值</th>
				<th onclick="$.sortTable.sort('stats',18,compareNumber)">咪咕活跃价值</th>
				
				<th onclick="$.sortTable.sort('stats',19,compareNumber)">MM订单数</th>
				<th onclick="$.sortTable.sort('stats',20,compareNumber)">咪咕订单数</th>
				<th onclick="$.sortTable.sort('stats',21,compareNumber)">订单总数</th>
				
				<th onclick="$.sortTable.sort('stats',22,compareNumber)">MM成功订单数</th>
				<th onclick="$.sortTable.sort('stats',23,compareNumber)">咪咕成功订单数</th>
				<th onclick="$.sortTable.sort('stats',24,compareNumber)">成功订单总数</th>
				<th onclick="$.sortTable.sort('stats',25,compareNumber)">MM用户数</th>
				<th onclick="$.sortTable.sort('stats',26,compareNumber)">咪咕用户数</th>
				<th onclick="$.sortTable.sort('stats',27,compareNumber)">MM成功用户数</th>
				<th onclick="$.sortTable.sort('stats',28,compareNumber)">咪咕成功用户数</th>
				<th onclick="$.sortTable.sort('stats',29,compareNumber)">成功用户总数</th>
				
				<th onclick="$.sortTable.sort('stats',30,compareNumber)">新增（含破解）</th>
				<th onclick="$.sortTable.sort('stats',31,compareNumber)">活跃（含破解）</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${report.dataList}" var="item" varStatus="status">
				<tr>
				
					<td>${fns:findChannelName(item.channelId,item.channelId)}(${item.channelId})</td>
					<td>${item.newUserNum }</td>
					<td>${item.activeUserNum }</td>
					
					<td><fmt:formatNumber type="number" value="${item.mmSuccAmt*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
					<td><fmt:formatNumber type="number" value="${item.andSuccAmt*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
					<td><fmt:formatNumber type="number" value="${(item.mmSuccAmt + item.andSuccAmt)*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
					
					<td><c:choose><c:when test="${item.newUserNum gt 0}"><fmt:formatNumber type="number" value="${(item.mmSuccAmt + item.andSuccAmt)*0.01/item.newUserNum}" maxFractionDigits="4" pattern="0.0000"/></c:when><c:otherwise>N/A</c:otherwise></c:choose></td>
					<td><c:choose><c:when test="${item.activeUserNum gt 0}"><fmt:formatNumber type="number" value="${(item.mmSuccAmt + item.andSuccAmt)*0.01/item.activeUserNum}" maxFractionDigits="4" pattern="0.0000"/></c:when><c:otherwise>N/A</c:otherwise></c:choose></td>
					<td><c:choose><c:when test="${(item.mmUserSuccNum + item.andUserSuccNum) gt 0}"><fmt:formatNumber type="number" value="${(item.mmSuccAmt + item.andSuccAmt)*0.01/(item.mmUserSuccNum + item.andUserSuccNum) }" maxFractionDigits="4" pattern="0.0000"/></c:when><c:otherwise>N/A</c:otherwise></c:choose></td>
					
					<td><c:choose><c:when test="${item.activeUserNum gt 0}"><fmt:formatNumber type="number" value="${item.mmOrderNum/item.activeUserNum}" maxFractionDigits="4" pattern="0.0000"/></c:when><c:otherwise>N/A</c:otherwise></c:choose></td>
					<td><c:choose><c:when test="${item.activeUserNum gt 0}"><fmt:formatNumber type="number" value="${item.mmOrderSuccNum/item.activeUserNum}" maxFractionDigits="4" pattern="0.0000"/></c:when><c:otherwise>N/A</c:otherwise></c:choose></td>
					<td><c:choose><c:when test="${item.activeUserNum gt 0}"><fmt:formatNumber type="number" value="${item.andOrderNum/item.activeUserNum}" maxFractionDigits="4" pattern="0.0000"/></c:when><c:otherwise>N/A</c:otherwise></c:choose></td>
					<td><c:choose><c:when test="${item.activeUserNum gt 0}"><fmt:formatNumber type="number" value="${item.andOrderSuccNum/item.activeUserNum}" maxFractionDigits="4" pattern="0.0000"/></c:when><c:otherwise>N/A</c:otherwise></c:choose></td>
					<td><c:choose><c:when test="${item.activeUserNum gt 0}"><fmt:formatNumber type="number" value="${(item.mmOrderNum + item.andOrderNum)/item.activeUserNum}" maxFractionDigits="4" pattern="0.0000"/></c:when><c:otherwise>N/A</c:otherwise></c:choose></td>
					<td><c:choose><c:when test="${item.activeUserNum gt 0}"><fmt:formatNumber type="number" value="${(item.mmOrderSuccNum + item.andOrderSuccNum)/item.activeUserNum}" maxFractionDigits="4" pattern="0.0000"/></c:when><c:otherwise>N/A</c:otherwise></c:choose></td>

					<td><c:choose><c:when test="${item.newUserNum gt 0}"><fmt:formatNumber type="number" value="${item.mmSuccAmt*0.01/item.newUserNum}" maxFractionDigits="4" pattern="0.0000"/></c:when><c:otherwise>N/A</c:otherwise></c:choose></td>
					<td><c:choose><c:when test="${item.activeUserNum gt 0}"><fmt:formatNumber type="number" value="${item.mmSuccAmt*0.01/item.activeUserNum}" maxFractionDigits="4" pattern="0.0000"/></c:when><c:otherwise>N/A</c:otherwise></c:choose></td>
					<td><c:choose><c:when test="${item.newUserNum gt 0}"><fmt:formatNumber type="number" value="${item.andSuccAmt*0.01/item.newUserNum}" maxFractionDigits="4" pattern="0.0000"/></c:when><c:otherwise>N/A</c:otherwise></c:choose></td>
					<td><c:choose><c:when test="${item.activeUserNum gt 0}"><fmt:formatNumber type="number" value="${item.andSuccAmt*0.01/item.activeUserNum}" maxFractionDigits="4" pattern="0.0000"/></c:when><c:otherwise>N/A</c:otherwise></c:choose></td>
					
					<td>${item.mmOrderNum }</td>
					<td>${item.andOrderNum }</td>
					<td>${item.mmOrderNum + item.andOrderNum }</td>
					
					<td>${item.mmOrderSuccNum }</td>
					<td>${item.andOrderSuccNum }</td>
					<td>${item.mmOrderSuccNum + item.andOrderSuccNum }</td>
					<td>${item.mmUserNum }</td>
					<td>${item.andUserNum }</td>
					<td>${item.mmUserSuccNum }</td>
					<td>${item.andUserSuccNum }</td>
					<td>${item.mmUserSuccNum + item.andUserSuccNum }</td>
					
					<td>${item.newUserNum2 }(${item.newUserNum2 - item.newUserNum})</td>
					<td>${item.activeUserNum2 }(${item.activeUserNum2 - item.activeUserNum})</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

</body>
</html>