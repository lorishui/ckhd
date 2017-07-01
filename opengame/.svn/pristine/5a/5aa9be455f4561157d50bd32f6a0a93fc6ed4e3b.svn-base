<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>移动MM订单统计</title>
<meta name="decorator" content="default"/>
<script type="text/javascript" src="/static/tableOrder/tableOrder.js?a=1"></script>
<script type="text/javascript">
		$(document).ready(function() {
			changeAPPID(true);
			$("#ckappId").focus();
			$("#searchForm").validate({
				submitHandler: function(form){
					//loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox"
			});
		});
		function selectDateFn(val){
			var startDate = $("#startDate");
			var endDate = $("#endDate");
			if(val == 0){
				var day =  GetDateStr(0);
				startDate.val(day + " 00:00:00");
				endDate.val(day + " 23:59:59");
			}else if(val == 1){
				var day = GetDateStr(-1);
				startDate.val( day + " 00:00:00");
				endDate.val( day + " 23:59:59");
			}else if(val == 7){
				startDate.val( GetDateStr(-6) + " 00:00:00");
				endDate.val( GetDateStr(0) + " 23:59:59");
			}else if(val == 30){
				startDate.val( GetDateStr(-29) + " 00:00:00");
				endDate.val( GetDateStr(0) + " 23:59:59");
			}
			if(val >= 0){
				$("#searchForm").submit();
			}
		}
		function GetDateStr(AddDayCount) {
		    var dd = new Date();
		    dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
		    var y = dd.getFullYear();
		    var m = dd.getMonth()+1;//获取当前月份的日期
		    var d = dd.getDate();
		    m=m<10?"0"+m:m;
		    d=d<10?"0"+d:d;
		    return y+"-"+m+"-"+d;
		}
		function changeAPPID(pageLoad){
			$.ajax({
			    url:'${ctx}/stats/queryAppCarriers',
			    type:'get',
			    data:'ckAppId=' + $("#ckappId").val() + '&carriersType=MM',
			    error:function(){
			    },
			    success:function(data){
			    	$("#appId").empty();
			    	$("#appId").append("<option value='' selected>(全部)</option>");
			       for(var item in data){
			       	$("#appId").append("<option value='"+data[item].appId +"'>"+data[item].appName + "(" + data[item].appId +")</option>");
			       }
			       if(pageLoad){
			    	   $("#appId").val("${mmapporder.appId}");
					   $(".select2-chosen")[1].innerHTML = $("#appId option:selected").text();
			       }else{
			    	   $("#appId").val("");
				       $(".select2-chosen")[1].innerHTML = "(全部)";
			       }
			    }
			});
		}
</script>
<style type="text/css">
 thead th{ background-color:red}
</style>
</head>
<body>
	<div id="position">移动MM订单统计：支付信息</div>
	<form:form id="searchForm"  modelAttribute="mmapporder" action="${ctx}/stats/mmorder/" method="post" class="breadcrumb form-search">
		<div>
			<label>游戏：</label>
			 <span><font color="red">*</font> </span>
			<form:select id="ckappId" path="ckappId" class="input-medium" onChange="changeAPPID()">
				<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
		    </form:select>
			<form:select id="appId" path="appId" class="input-medium" style="width:300px">
		    </form:select>
			<label>省份：</label>
			<form:select id="provinceID" path="provinceID" class="input-mini" >
				<form:option value="" label="(全部)"/>
				<form:options items="${fns:getDictList('province')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		    </form:select>
			<label>渠道：</label>
			<form:select id="channelId" path="channelId" class="input-medium">
				<form:option value="" label="(全部)"/>
				<form:options items="${fns:getCarriersChannelList('MM')}" itemLabel="name" itemValue="id" htmlEscape="false"/>
		    </form:select>
		</div>
		
		<br/>
		<div>
			<label>订单类型：</label>
		     <span><font color="red">*</font> </span>
			<form:select id="orderType" path="orderType" class="input-mini" >
				<form:option value="1" label="正式订单" selected="selected"/>
				<form:option value="0" label="测试订单"/>
		    </form:select>
			<label>时间范围：</label>
			<a href="#" onclick="javascript:selectDateFn(0);void(0)">今天</a>
			<a href="#" onclick="javascript:selectDateFn(1);void(0)">昨天</a>
			<a href="#" onclick="javascript:selectDateFn(7);void(0)">近七天</a>
			<a href="#" onclick="javascript:selectDateFn(30);void(0)">近30天</a>
			<span><font color="red">*</font> </span>
			<input id="startDate" name="startDate" type="text" readonly="readonly" maxlength="20" class="WdateTime required"
				value="${mmapporder.startDate}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			&nbsp;--&nbsp;
			<span><font color="red">*</font> </span>
			<input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="WdateTime required"
				value="${mmapporder.endDate}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		</div>
		<div style="margin-top:8px;">
			&nbsp;&nbsp;&nbsp;<input type="button" class="btn btn-primary" onClick ="$('#mainInfoTable').tableExport({type:'excel',escape:'false'});" value="全局统计导出" />
			&nbsp;&nbsp;<input type="button" class="btn btn-primary" onClick ="$('#paycodeTable').tableExport({type:'excel',escape:'false'});" value="计费统计导出" />
			&nbsp;&nbsp;<input type="button" class="btn btn-primary" onClick ="$('#statusInfoTable').tableExport({type:'excel',escape:'false'});" value="错误码统计导出" />
			&nbsp;&nbsp;<input type="button" class="btn btn-primary" onClick ="$('#channelInfoTable').tableExport({type:'excel',escape:'false'});" value="渠道统计导出" />
			&nbsp;&nbsp;<input type="button" class="btn btn-primary" onClick ="$('#channelpaycodeInfoTable').tableExport({type:'excel',escape:'false'});" value="渠道付费点统计导出" />
			&nbsp;&nbsp;<input type="button" class="btn btn-primary" onClick ="$('#provinceInfoTable').tableExport({type:'excel',escape:'false'});" value="省份统计导出" />			
		</div>
	</form:form>
	<table id="mainInfoTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>日期</th>
				<th>订单总数</th>
				<th>成功数目</th>
				<th>失败数目</th>
				<th>成功数目比例</th>
				<th>总金额</th>
				<th>成功订单金额(移动MM)</th>
				<th>失败订单金额(移动MM)</th>
				<th>成功金额比例</th>
				<th>用户数</th>
				<th>付费有成功的用户数</th>
				<th>付费有失败的用户数</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${orderList}" var="order" varStatus="status">
			<tr>
				<td>${status.count}</td>
				<td>${order.mdate}</td>
				<td>${order.total_num}</td>
				<td>${order.succ_num}</td>
				<td>${order.fail_num}</td>
				<td><fmt:formatNumber type="percent" value="${order.succ_rate}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td><fmt:formatNumber type="number" value="${order.total_price}" maxFractionDigits="2" pattern="0.00"/></td>
				<td><fmt:formatNumber type="number" value="${order.succ_price}" maxFractionDigits="2" pattern="0.00"/></td>
				<td><fmt:formatNumber type="number" value="${order.fail_price}" maxFractionDigits="2" pattern="0.00"/></td>
				<td><fmt:formatNumber type="percent" value="${order.succ_price_rate}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td>${order.user_num}</td>
				<td>${order.succ_user_num}</td>
				<td>${order.error_user_num}</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
<table id="mainInfoTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>用户数</th>
				<th>新用户数</th>
				<th>老用户数</th>
				<th>成功付费用户数</th>
				<th>成功付费新用户数</th>
				<th>成功付费老用户数</th>
				<th>新用户成功付费金额</th>
				<th>老用户成功付费金额</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${acountList}" var="account" varStatus="status">
			<tr>
				<td>${status.count}</td>
				<td>${account.allCount}</td>
				<td>${account.allCount-account.oldCount}</td>
				<td>${account.oldCount}</td>
				<td>${account.succ_allCount}</td>
				<td>${account.succ_allCount-account.succ_oldCount}</td>
				<td>${account.succ_oldCount}</td>
				<td><fmt:formatNumber type="number" value="${(account.succ_allprice-account.succ_oldprice)*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
				<td><fmt:formatNumber type="number" value="${account.succ_oldprice*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
			</tr>
			</c:forEach>
		</tbody>
</table>
	<table id="paycodeTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
			<th onclick="$.sortTable.sort('paycodeTable',0,compareNumber)" style="cursor: pointer;">序号</th>
			<th onclick="$.sortTable.sort('paycodeTable',1)" style="cursor: pointer;">计费点</th>
			<th onclick="$.sortTable.sort('paycodeTable',2,compareNumber)" style="cursor: pointer;">数量</th>
			<th onclick="$.sortTable.sort('paycodeTable',2,compareNumber)" style="cursor: pointer;">数量比例</th>
			<th onclick="$.sortTable.sort('paycodeTable',4,compareNumber)" style="cursor: pointer;">成功数量</th>
			<th onclick="$.sortTable.sort('paycodeTable',4,compareNumber)" style="cursor: pointer;">成功数量占比</th>
			<th onclick="$.sortTable.sort('paycodeTable',6,compareRate)" style="cursor: pointer;">成功率</th>
			<th onclick="$.sortTable.sort('paycodeTable',7,compareNumber)" style="cursor: pointer;">订购总价(元)</th>
			<th onclick="$.sortTable.sort('paycodeTable',7,compareNumber)" style="cursor: pointer;">订购总价比例</th>
			<th onclick="$.sortTable.sort('paycodeTable',9,compareNumber)" style="cursor: pointer;">成功总价(元)</th>
			<th onclick="$.sortTable.sort('paycodeTable',9,compareNumber)" style="cursor: pointer;">成功金额占比</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${stats_paycode}" var="paycode" varStatus="status">
			<tr>
				<td>${status.count}</td>
				<td>${paycode.paycode_name}</td>
				<td>${paycode.total_num}</td>
				<td><fmt:formatNumber type="percent" value="${0.1*paycode.total_num/orderList[orderList.size()-1].total_num*10}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td>${paycode.total_num-paycode.fail_num }</td>
				<td><fmt:formatNumber type="percent" value="${orderList[orderList.size()-1].succ_num ne 0?0.1*(paycode.total_num-paycode.fail_num)/orderList[orderList.size()-1].succ_num*10:0}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td><fmt:formatNumber type="percent" value="${0.1*(paycode.total_num-paycode.fail_num)/paycode.total_num*10}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td><fmt:formatNumber type="number" value="${paycode.total_price*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
				<!-- 注意total_price是分 -->
				<td><fmt:formatNumber type="percent" value="${(paycode.total_price*0.01)/(orderList[orderList.size()-1].total_price)}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td><fmt:formatNumber type="number" value="${(paycode.total_price-paycode.fail_price)*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
				<td><fmt:formatNumber type="percent" value="${orderList[orderList.size()-1].succ_price ne '0.0000'?(paycode.total_price-paycode.fail_price)*0.01/orderList[orderList.size()-1].succ_price:0 }" maxFractionDigits="2" pattern="0.00%"/></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>

	<table id="channelInfoTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th onclick="$.sortTable.sort('channelInfoTable',0,compareNumber)" style="cursor: pointer;">序号</th>
				<th onclick="$.sortTable.sort('channelInfoTable',1)" style="cursor: pointer;">渠道</th>
				<th onclick="$.sortTable.sort('channelInfoTable',2,compareNumber)" style="cursor: pointer;">总付费金额(元)</th>
				<th onclick="$.sortTable.sort('channelInfoTable',3,compareNumber)" style="cursor: pointer;">成功金额(元)</th>
				<th onclick="$.sortTable.sort('channelInfoTable',4,compareNumber)" style="cursor: pointer;">失败金额(元)</th>
				<th onclick="$.sortTable.sort('channelInfoTable',5,compareRate)" style="cursor: pointer;">成功率</th>
			</tr>
	</thead>
	<tbody>
	<c:forEach items="${stats_channel}" var="stats" varStatus="status">
				<tr>
					<c:choose><c:when test="${(100*(0.01-(stats.channel_fail_price*0.01/stats.channel_total_price))) lt 0.2}">
					<td style="color:red">${status.count}</td>
					<td style="color:red">${fns:getChannelName(stats.channel_id,"")}(${stats.channel_id})</td>
					<td style="color:red"><fmt:formatNumber type="number" value="${0.01*stats.channel_total_price}" maxFractionDigits="2" pattern="0.00"/>
					</td>
					<td style="color:red">
					<fmt:formatNumber type="number" value="${0.01*(stats.channel_total_price-stats.channel_fail_price)}" maxFractionDigits="2" pattern="0.00"/>
					</td>
					<td style="color:red">
					<fmt:formatNumber type="number" value="${0.01*stats.channel_fail_price}" maxFractionDigits="2" pattern="0.00"/>
					</td>
					<td style="color:red">
					<fmt:formatNumber type="percent" value="${100*(0.01-(stats.channel_fail_price*0.01/stats.channel_total_price))}" maxFractionDigits="2" pattern="0.00%"/>
					</td>
					</c:when>
					<c:otherwise><td>${status.count}</td>
					<td>${fns:getChannelName(stats.channel_id,"")}(${stats.channel_id})</td>
					<td><fmt:formatNumber type="number" value="${0.01*stats.channel_total_price}" maxFractionDigits="2" pattern="0.00"/></td>
					<td><fmt:formatNumber type="number" value="${0.01*(stats.channel_total_price-stats.channel_fail_price)}" maxFractionDigits="2" pattern="0.00"/></td>
					<td><fmt:formatNumber type="number" value="${0.01*stats.channel_fail_price}" maxFractionDigits="2" pattern="0.00"/></td>
					<td><fmt:formatNumber type="percent" value="${100*(0.01-(stats.channel_fail_price*0.01/stats.channel_total_price))}" maxFractionDigits="2" pattern="0.00%"/></td>
					</c:otherwise></c:choose>
				</tr>
		</c:forEach>
		</tbody>
	</table>

	<table id="provinceInfoTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th onclick="$.sortTable.sort('provinceInfoTable',0,compareNumber)" style="cursor: pointer;">序号</th>
				<th onclick="$.sortTable.sort('provinceInfoTable',1)" style="cursor: pointer;">省份</th>
				<th onclick="$.sortTable.sort('provinceInfoTable',2,compareNumber)" style="cursor: pointer;">数量</th>
				<th onclick="$.sortTable.sort('provinceInfoTable',2,compareNumber)" style="cursor: pointer;">数量比例</th>
				<th onclick="$.sortTable.sort('provinceInfoTable',4,compareNumber)" style="cursor: pointer;">成功数量</th>
				<th onclick="$.sortTable.sort('provinceInfoTable',4,compareNumber)" style="cursor: pointer;">成功数量占比</th>
				<th onclick="$.sortTable.sort('provinceInfoTable',6,compareRate)" style="cursor: pointer;">成功率</th>
				<th onclick="$.sortTable.sort('provinceInfoTable',7,compareNumber)" style="cursor: pointer;">订购总价(元)</th>
				<th onclick="$.sortTable.sort('provinceInfoTable',7,compareNumber)" style="cursor: pointer;">订购总价比例</th>
				<th onclick="$.sortTable.sort('provinceInfoTable',9,compareNumber)" style="cursor: pointer;">成功总价(元)</th>
				<th onclick="$.sortTable.sort('provinceInfoTable',9,compareNumber)" style="cursor: pointer;">成功金额占比</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${stats_province}" var="province" varStatus="status">
			<tr>
				<c:choose><c:when test="${0.1*(province.total_num-province.fail_num)/province.total_num*10 eq 0}">
				<td style="color:red">${status.count}</td>
				<td style="color:red">${fns:getDictLabel(province.province_id, "province", province.province_id)}</td>
				<td style="color:red">${province.total_num}</td>
				<td style="color:red"><fmt:formatNumber type="percent" value="${0.1*province.total_num/orderList[orderList.size()-1].total_num*10}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td style="color:red">${province.total_num-province.fail_num }</td>
				<td style="color:red"><fmt:formatNumber type="percent" value="${orderList[orderList.size()-1].succ_num ne 0?0.1*(province.total_num-province.fail_num)/orderList[orderList.size()-1].succ_num*10:0}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td style="color:red"><fmt:formatNumber type="percent" value="${0.1*(province.total_num-province.fail_num)/province.total_num*10}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td style="color:red"><fmt:formatNumber type="number" value="${province.total_price*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
				<td style="color:red"><fmt:formatNumber type="percent" value="${(province.total_price*0.01)/(orderList[orderList.size()-1].total_price)}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td style="color:red"><fmt:formatNumber type="number" value="${(province.total_price-province.fail_price)*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
				<td style="color:red"><fmt:formatNumber type="percent" value="${orderList[orderList.size()-1].succ_price ne '0.0000'?(province.total_price-province.fail_price)*0.01/orderList[orderList.size()-1].succ_price:0 }" maxFractionDigits="2" pattern="0.00%"/></td>
				</c:when><c:otherwise>
				<td>${status.count}</td>
				<td>${fns:getDictLabel(province.province_id, "province", province.province_id)}</td>
				<td>${province.total_num}</td>
				<td><fmt:formatNumber type="percent" value="${0.1*province.total_num/orderList[orderList.size()-1].total_num*10}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td>${province.total_num-province.fail_num }</td>
				<td><fmt:formatNumber type="percent" value="${orderList[orderList.size()-1].succ_num ne 0?0.1*(province.total_num-province.fail_num)/orderList[orderList.size()-1].succ_num*10:0}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td><fmt:formatNumber type="percent" value="${0.1*(province.total_num-province.fail_num)/province.total_num*10}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td><fmt:formatNumber type="number" value="${province.total_price*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
				<td><fmt:formatNumber type="percent" value="${(province.total_price*0.01)/(orderList[orderList.size()-1].total_price)}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td><fmt:formatNumber type="number" value="${(province.total_price-province.fail_price)*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
				<td><fmt:formatNumber type="percent" value="${orderList[orderList.size()-1].succ_price ne '0.0000'?(province.total_price-province.fail_price)*0.01/orderList[orderList.size()-1].succ_price:0 }" maxFractionDigits="2" pattern="0.00%"/></td>
				</c:otherwise></c:choose>
			</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<table id="statusInfoTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th onclick="$.sortTable.sort('statusInfoTable',0,compareNumber)" style="cursor: pointer;">序号</th>
				<th onclick="$.sortTable.sort('statusInfoTable',1,compareNumber)" style="cursor: pointer;">错误码ID</th>
				<th onclick="$.sortTable.sort('statusInfoTable',2)" style="cursor: pointer;">错误码说明</th>
				<th onclick="$.sortTable.sort('statusInfoTable',3,compareNumber)" style="cursor: pointer;">数量</th>
				<th onclick="$.sortTable.sort('statusInfoTable',3,compareNumber)" style="cursor: pointer;">比例</th>
			</tr>
		</thead>
	<tbody>
	<c:forEach items="${normal_statsByReturnStatus}" var="stats" varStatus="status">
		<tr>
			<td>${status.count}</td>
			<td>${stats.returnStatus}</td>
			<td>${stats.error_name}</td>
			<td>${stats.error_num}</td>
			<td><fmt:formatNumber type="number" value="${stats.error_num/orderList[orderList.size()-1].fail_num}" maxFractionDigits="2" pattern="0.00%"/></td>
		</tr>
	</c:forEach>
	</table>
	
	<table id="ProvinceInfoTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th onclick="$.sortTable.sort('ProvinceInfoTable',0,compareNumber)" style="cursor: pointer;">序号</th>
					<th onclick="$.sortTable.sort('ProvinceInfoTable',1)" style="cursor: pointer;">省份</th>
					<th onclick="$.sortTable.sort('ProvinceInfoTable',2,compareNumber)" style="cursor: pointer;">错误码ID</th>
					<th onclick="$.sortTable.sort('ProvinceInfoTable',3)" style="cursor: pointer;">错误码说明</th>
					<th onclick="$.sortTable.sort('ProvinceInfoTable',4,compareNumber)" style="cursor: pointer;">数量</th>
					<th onclick="$.sortTable.sort('ProvinceInfoTable',5,compareNumber)" style="cursor: pointer;">比例</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${statsByErrorProvince}" var="stats" varStatus="status">
				<tr>
					<td>${status.count}</td>
					<td>${fns:getDictLabel(stats.province_id, "province", stats.province_id)}</td>
					<td>${stats.returnStatus}</td>
					<td>${fns:getDictLabels(stats.returnStatus,'mmErrorCode','')}</td>
					<td>${stats.error_num}</td>
					<td><fmt:formatNumber type="number" value="${stats.error_num/orderList[orderList.size()-1].fail_num}" maxFractionDigits="2" pattern="0.00%"/></td>
				</tr>
			</c:forEach>
	</table>

	<table id="statusInfoTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th onclick="$.sortTable.sort('statusInfoTable',0,compareNumber)" style="cursor: pointer;">序号</th>
				<th onclick="$.sortTable.sort('statusInfoTable',1,compareNumber)" style="cursor: pointer;">错误码ID</th>
				<th onclick="$.sortTable.sort('statusInfoTable',2)" style="cursor: pointer;">错误码说明</th>
				<th>渠道</th>
				<th onclick="$.sortTable.sort('statusInfoTable',3,compareNumber)" style="cursor: pointer;">数量</th>
				<th onclick="$.sortTable.sort('statusInfoTable',3,compareNumber)" style="cursor: pointer;">比例</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${stats_returnstatus}" var="stats" varStatus="status">
			<tr>
				<td>${status.count}</td>
				<td>${stats.returnStatus}</td>
				<td>${fns:getDictLabels(stats.returnStatus,'mmErrorCode','')}</td>
				<td>${fns:getChannelName(stats.channel_id,"")}-${stats.channel_id}</td>
				<td>${stats.error_num}</td>
				<td><fmt:formatNumber type="number" value="${stats.error_num/orderList[orderList.size()-1].fail_num}" maxFractionDigits="2" pattern="0.00%"/></td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>