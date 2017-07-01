<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>移动封省相关信息查询</title>
<script type="text/javascript">
	$(function() {
		changeAPPID(true);
		var time = $("#time");
		if (time.val() == "") {
			time.val(GetDateStr(0));
		}
	});

	function GetDateStr(AddDayCount) {
		var dd = new Date();
		dd.setDate(dd.getDate() + AddDayCount);//获取AddDayCount天后的日期
		var y = dd.getFullYear();
		var m = dd.getMonth() + 1;//获取当前月份的日期
		var d = dd.getDate();
		m = m < 10 ? "0" + m : m;
		d = d < 10 ? "0" + d : d;
		return y + "-" + m + "-" + d;
	}

	function changeAPPID(pageLoad) {
		$.ajax({
			url : '${ctx}/stats/queryAppCarriers',
			type : 'get',
			data : 'ckAppId=' + $("#ckappId").val() + '&carriersType=MM',
			error : function() {
			},
			success : function(data) {
				$("#appId").empty();
				$("#appId").append("<option value='' selected>(全部)</option>");
				for ( var item in data) {
					$("#appId").append(
							"<option value='"+data[item].appId +"'>"
									+ data[item].appName + "("
									+ data[item].appId + ")</option>");
				}
				if (pageLoad) {
					$("#appId").val("${mm.appId}");
					$(".select2-chosen")[1].innerHTML = $(
							"#appId option:selected").text();
				} else {
					$("#appId").val("");
					$(".select2-chosen")[1].innerHTML = "(全部)";
				}
			}
		});
	}
</script>
</head>
<body>
	<c:set var="isHasTo" value="0" scope="session" />
	<c:set var="isHasYes" value="0" scope="session" />
	<form:form id="searchForm" action="${ctx}/stats/sealProvince"
		modelAttribute="mm" method="post" class="breadcrumb form-search">
		<div>
			<sys:message content="${message}" />
			<span class="help-inline"><font color="red">*</font> </span> <label>游戏：</label>
			<form:select id="ckappId" path="ckappId" class="input-medium"
				onChange="changeAPPID()">
				<form:options items="${fns:getAPPCkList()}" itemLabel="name"
					itemValue="ckappId" htmlEscape="false" />
			</form:select>
			<form:select id="appId" path="appId" class="input-medium"
				style="width:300px">
			</form:select>
			<label>时间：</label> <input id="time" name="actionTime" type="text"
				class="WdateTime" value="${mm.actionTime}"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
			<input id="btnSubmit" class="btn btn-primary" type="submit"
				value="查询" />
		</div>
	</form:form>
	<table id="mainInfoTable" data-tableName="Test Table 1"
		class="table2excel table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th onclick="$.sortTable.sort('mainInfoTable',0,compareNumber)">序号</th>
				<th onclick="$.sortTable.sort('mainInfoTable',1)">日期</th>
				<th onclick="$.sortTable.sort('mainInfoTable',2)">mmappid</th>
				<th onclick="$.sortTable.sort('mainInfoTable',3)">省份</th>
				<th onclick="$.sortTable.sort('mainInfoTable',4,compareNumber)">订单总数</th>
				<!-- <th onclick="$.sortTable.sort('mainInfoTable',5,compareNumber)">成功数量</th> -->
				<!-- <th onclick="$.sortTable.sort('mainInfoTable',6)">成功数量占比</th> -->
				<th onclick="$.sortTable.sort('mainInfoTable',5,compareRate)">成功率</th>
				<th onclick="$.sortTable.sort('mainInfoTable',6,compareNumber)">总金额</th>
				<!-- <th onclick="$.sortTable.sort('mainInfoTable',9)">总金额占比</th> -->
				<th onclick="$.sortTable.sort('mainInfoTable',7,compareNumber)">成功金额</th>
				<th onclick="$.sortTable.sort('mainInfoTable',8,compareRate)">成功金额占比</th>
				<!-- <th onclick="$.sortTable.sort('mainInfoTable',12,compareNumber)">错误码2080数量</th>
				<th onclick="$.sortTable.sort('mainInfoTable',13,compareNumber)">错误码2081数量</th> -->
				<th onclick="$.sortTable.sort('mainInfoTable',9)">封省状态</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${today}" begin="0" step="1" varStatus="status">
				<tr>
					<%-- <c:if test="${(today[status.index].num_2080 ne 0 or today[status.index].num_2081 ne 0) and today[status.index].successNum ne 0}">
				<c:if test="${today[status.index+1].num_2080 ne 0 or today[status.index+1].num_2081 ne 0}">
					<tr style="color:red">
				</c:if>
				<c:if test="${(today[status.index+1].num_2080 eq 0 and today[status.index+1].num_2081 eq 0) and today[status.index+1].successNum ne 0}">
					<tr style="color:red;">
				</c:if>
				<c:if test="${(today[status.index+1].num_2080 eq 0 and today[status.index+1].num_2081 eq 0) and today[status.index+1].successNum eq 0}">
					<tr>
					<div>
				</c:if>
			</c:if>
			<c:if test="${(today[status.index].num_2080 ne 0 or today[status.index].num_2081 ne 0) and today[status.index].successNum eq 0}">
				<c:if test="${today[status.index+1].num_2080 ne 0 or today[status.index+1].num_2081 ne 0}">
					<tr>
				</c:if>
				<c:if test="${(today[status.index+1].num_2080 eq 0 and today[status.index+1].num_2081 eq 0) and today[status.index+1].successNum ne 0}">
					<tr style="color:red;">
				</c:if>
				<c:if test="${(today[status.index+1].num_2080 eq 0 and today[status.index+1].num_2081 eq 0) and today[status.index+1].successNum eq 0}">
					<tr style="color:yellow">
				</c:if>
			</c:if>
			<c:if test="${(today[status.index].num_2080 eq 0 and today[status.index].num_2081 eq 0) and today[status.index].successNum ne 0}">
				<c:if test="${today[status.index+1].num_2080 ne 0 or today[status.index+1].num_2081 ne 0}">
					<tr style="color:green;">
					<div class="green">
				</c:if>
				<c:if test="${(today[status.index+1].num_2080 eq 0 and today[status.index+1].num_2081 eq 0) and today[status.index+1].successNum ne 0}">
					<tr>
					<div>
				</c:if>
				<c:if test="${(today[status.index+1].num_2080 eq 0 and today[status.index+1].num_2081 eq 0) and today[status.index+1].successNum eq 0}">
					<tr>
					<div>
				</c:if>
			</c:if>
			<c:if test="${(today[status.index].num_2080 eq 0 and today[status.index].num_2081 eq 0) and today[status.index].successNum eq 0}">
				<c:if test="${today[status.index+1].num_2080 ne 0 or today[status.index+1].num_2081 ne 0}">
					<tr style="color:yellow">
					<div class="yellow">
				</c:if>
				<c:if test="${(today[status.index+1].num_2080 eq 0 and today[status.index+1].num_2081 eq 0) and today[status.index+1].successNum ne 0}">
					<tr>
					<div>
				</c:if>
				<c:if test="${(today[status.index+1].num_2080 eq 0 and today[status.index+1].num_2081 eq 0) and today[status.index+1].successNum eq 0}">
					<tr style="color:#E0FFFF;">
					<div>
				</c:if>
			</c:if> --%>

					<td>${status.count}</td>
					<td>${today[status.index].timest}</td>
					<td>${fns:getChannelName(today[status.index].appid,'')}(${today[status.index].appid})</td>
					<td>${today[status.index].provinceName}</td>
					<td>${today[status.index].total}</td>
					<%-- <td>${today[status.index].successNum}</td>
				<td><c:forEach items="${total}" var="t">
						<c:if test="${t.timest eq today[status.index].timest}">
							<c:if test="${t.successNum ne 0 }">
							<fmt:formatNumber type="number" value="${(today[status.index].successNum*0.01)/(t.successNum*0.01)*100}"   maxFractionDigits="2" pattern="0.00"/>%
							</c:if>
							<c:if test="${t.successNum eq 0 }">
								0
							</c:if>
						</c:if>
					</c:forEach>
				</td> --%>
					<td><c:if test="${today[status.index].total ne 0 }">
							<fmt:formatNumber type="number"
								value="${(today[status.index].successNum*0.01)/(today[status.index].total*0.01)*100}"
								maxFractionDigits="2" pattern="0.00" />%
					</c:if> <c:if test="${today[status.index].total eq 0 }">
						0.00%
					</c:if></td>
					<td>${today[status.index].totalMoney/100}</td>
					<%-- <td>
					<c:forEach items="${total}" var="t" >
						<c:if test="${t.timest eq today[status.index].timest}">
							<c:if test="${t.totalMoney ne 0 }">
							<fmt:formatNumber type="number" value="${(today[status.index].totalMoney*0.01)/(t.totalMoney*0.01)*100}"   maxFractionDigits="2" pattern="0.00"/>%
							</c:if>
							<c:if test="${t.successNum eq 0 }">
								0.00%
							</c:if>
						</c:if>
					</c:forEach>
				</td> --%>
					<td>${today[status.index].successMoney/100}</td>
					<td><c:forEach items="${total}" var="t">
							<c:if test="${t.timest eq today[status.index].timest}">
								<c:if test="${t.successMoney ne 0 }">
									<fmt:formatNumber type="number"
										value="${(today[status.index].successMoney*0.01)/(t.successMoney*0.01)*100}"
										maxFractionDigits="2" pattern="0.00" />%
							</c:if>
								<c:if test="${t.successNum eq 0 }">
								0.00%
							</c:if>
							</c:if>
						</c:forEach></td>
					<%-- <td>${today[status.index].num_2080}</td>
				<td>${today[status.index].num_2081}</td> --%>
					<c:if test="${(today[status.index].num_2080 ne 0 or today[status.index].num_2081 ne 0) and today[status.index].successNum ne 0}">
						<c:set var="isHasTo" value="1" scope="session" />
						<c:if
							test="${today[status.index].successMaxTime < today[status.index].sealMaxTime }">
							<td><div class="red">新封省</div></td>
						</c:if>
						<c:if test="${ today[status.index].successMaxTime > today[status.index].sealMaxTime }">
							<td><div class="red">新解封</div></td>
						</c:if>
					</c:if> 
					<c:if test="${isHasTo ne 1}">
							<c:forEach items="${yesday}" var="y">
								<c:if
									test="${y.provinceId eq today[status.index].provinceId and y.appid eq today[status.index].appid}">
									<c:set var="isHasYes" value="1" scope="session" />
									<c:if
										test="${(today[status.index].num_2080 ne 0 or today[status.index].num_2081 ne 0) and today[status.index].successNum eq 0}">
										<c:if test="${y.num_2080 ne 0 or y.num_2081 ne 0}">
									<td>封省进行中</td>
								</c:if>
										<c:if
											test="${(y.num_2080 eq 0 and y.num_2081 eq 0) and y.successNum ne 0}">
											<td><div class="red">新封省</div></td>
										</c:if>
										<c:if
											test="${(y.num_2080 eq 0 and y.num_2081 eq 0) and y.successNum eq 0}">
									<td>封省进行中</td>
								</c:if>
									</c:if>
									<c:if
										test="${(today[status.index].num_2080 eq 0 and today[status.index].num_2081 eq 0) and today[status.index].successNum ne 0}">
										<c:if test="${(y.num_2080 ne 0 or y.num_2081 ne 0) and y.successNum ne 0}">
											<c:if test="${ y.successMaxTime > y.sealMaxTime }">
										<td>未封省</td>
									</c:if>
											<c:if test="${ y.successMaxTime < y.sealMaxTime }">
												<td><div class="red">新解封</div></td>
											</c:if>
										</c:if>
										<c:if test="${(y.num_2080 ne 0 or y.num_2081 ne 0) and y.successNum eq 0}">
											<td><div class="red">新解封</div></td>
										</c:if>
										<c:if
											test="${(y.num_2080 eq 0 and y.num_2081 eq 0) and y.successNum ne 0}">
									<td>未封省</td>
								</c:if>
										<c:if
											test="${(y.num_2080 eq 0 and y.num_2081 eq 0) and y.successNum eq 0}">
									<td>未封省</td>
								</c:if>
									</c:if>
									<c:if
										test="${(today[status.index].num_2080 eq 0 and today[status.index].num_2081 eq 0) and today[status.index].successNum eq 0}">
										<c:if test="${y.num_2080 ne 0 or y.num_2081 ne 0}">
											<c:if
												test="${y.successMaxTime ne null and y.successMaxTime > y.sealMaxTime }">
									<td>未封省</td>
									</c:if>
											<c:if
												test="${y.successMaxTime ne null and y.successMaxTime < y.sealMaxTime }">
									<td>封省进行中</td>
									</c:if>
											<c:if test="${y.successMaxTime eq null }">
											
									<td>封省进行中</td>
									</c:if>
										</c:if>
										<c:if
											test="${(y.num_2080 eq 0 and y.num_2081 eq 0) and y.successNum ne 0}">
									<td>未封省</td>
								</c:if>
										<c:if
											test="${(y.num_2080 eq 0 and y.num_2081 eq 0) and y.successNum eq 0}">
											<td><div class="yellow">没有足够数据,自行判断</div></td>
										</c:if>
									</c:if>
								</c:if>
							</c:forEach>
						</c:if> <c:if test="${isHasYes ne 1 and isHasTo ne 1}">
							<c:if test="${(today[status.index].num_2080 ne 0 or today[status.index].num_2081 ne 0) and today[status.index].successNum eq 0}">
							<td>封省进行中</td>
						</c:if>
							<c:if
								test="${(today[status.index].num_2080 eq 0 and today[status.index].num_2081 eq 0) and today[status.index].successNum ne 0}">
							<td>未封省</td>
						</c:if>
							<c:if
								test="${(today[status.index].num_2080 eq 0 and today[status.index].num_2081 eq 0) and today[status.index].successNum eq 0}">
								<td><div class="yellow">没有足够数据,自行判断</div></td>
							</c:if>
						</c:if>
						 <c:set var="isHasTo" value="0" scope="session" /> 
						<c:set var="isHasYes" value="0" scope="session" />
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<script type="text/javascript">
		$(function() {
			$('.red').parent().parent().css("color", "red");
			$('.yellow').parent().parent().css("color", "blue");
		});
	</script>
</body>
</html>