<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>网络订单统计信息</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			changeAPPID(true);
			$("#ckAppId").focus();
			$("#searchForm").validate({
				submitHandler: function(form){
					$("#searchForm").attr("action","${ctx}/stats/appOnlinePay/list");
					form.submit();
					merge();
				},
				errorContainer: "#messageBox"
			});
		});
		function changeAPPID(pageLoad){
			var id = '${onlinePay.childCkAppId}';
			$.ajax({
				url:  "${ctx}/app/appck/getChildIdList",
			    type:'get',
			    data:'ckappId=' + $("#ckAppId").val(),
			    error:function(){
			    },
			    success:function(data){
			    	var node = $("#childCkAppId");
			 		node.empty();
			 		var html = "<option value=\"\">全部</option>";
			 		for(var n=0;n<data.length;n++){
			 			html+="<option value='"+data[n].childId+"'>"+data[n].name+"_"+data[n].childId+"</option>";
			 		}
			 		node.append(html);
			 		node.val(id);
					var name = node.find("option:selected").text();
			 		if( id != null && id != '' ){
			 			$(".select2-chosen")[1].innerHTML = name;	
			 		}
			    }
			});
		}
		
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
		
	</script>
</head>
<body>
	<div id="position">网络订单统计信息</div>
	<form:form id="searchForm" modelAttribute="onlinePay" action="${ctx}/stats/appOnlinePay/list" method="post" class="breadcrumb form-search">
		
		<div>
			<label>游戏：</label>
			<form:select id="ckAppId" path="ckAppId" class="input-medium" onChange="changeAPPID()">
				<form:option value="" label="(全部)"/>
				<form:options items="${fns:getAPPCkListByChild()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
		    </form:select>
		     <span><font color="red">*</font> </span>
			<form:select id="childCkAppId" path="childCkAppId" class="input-medium" style="width:250px">
		    </form:select>
		    <label class="control-label">支付方式：</label>
		    <form:select path="payType" class="input-large">
		   		<form:option value="" label="(全部)"/>
				<form:options items="${fns:getDictList('paytype')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select> <span class="help-inline"><font color="red">*</font></span>
			<label>渠道：</label>
			<form:select id="channelId" path="channelId" class="input-medium">
				<form:option value="" label="(全部)"/>
				<form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
		    </form:select>
	   	</div> 
	   	<br>
	   	<div>
	   		<label>时间范围：</label>
			<a href="#" onclick="javascript:selectDateFn(0);void(0)">今天</a>
			<a href="#" onclick="javascript:selectDateFn(1);void(0)">昨天</a>
			<a href="#" onclick="javascript:selectDateFn(7);void(0)">近七天</a>
			<a href="#" onclick="javascript:selectDateFn(30);void(0)">近30天</a>
			<span><font color="red">*</font> </span>
	   		<label>统计日期：</label>
			<span><font color="red">*</font> </span>
		    <input id="startDate" name="startDate" type="text" readonly="readonly" maxlength="20" value="${onlinePay.startDate}"
		    class="WdateTime required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			&nbsp;&nbsp;
			<span><font color="red">*</font> </span>
			<input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" value="${onlinePay.endDate}"
			class="WdateTime required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			<label>订单类型：</label>
		     <span><font color="red">*</font> </span>
			<form:select id="isTest" path="isTest" class="input-mini" >
				<form:option value="1" label="测试订单" selected="selected"/>
				<form:option value="0" label="正式订单"/>
		    </form:select>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input id="btnSubmit" class="btn btn-primary" type="submit" style="width:100px" value="查  询"/>&nbsp;&nbsp;
	  </div>
	  <br>
	</form:form>
	<table id="statsOnline" class="table2excel table table-striped table-bordered table-condensed">
		<thead>
			<tr><th colspan="14">按日期统计</th></tr>
			<tr>
				<th>序号</th>
				<th>游戏</th>
				<th>日期</th>
				<th>订单总数</th>
				<th>创建订单失败数</th>
				<th>未支付数</th>
				<th>订单申请成功数</th>
				<th>订单申请失败数</th>
				<th>支付成功数</th>
				<th>支付失败数</th>
				<th>成功数比例</th>
				<th>总金额</th>
				<th>成功金额</th>
				<th>成功金额比例</th>
			</tr>
		 </thead>	
		 <tbody>
		<c:forEach items="${onlinePayList}" var="onlinePay" varStatus="status">
				<tr>
					<td>${status.count}</td>
					<td>${fns:getByCkAppNameByChild(onlinePay.ckAppId,onlinePay.childCkAppId)}(${onlinePay.ckAppId})</td>
					<td>${onlinePay.mdate}</td>
					<td>${onlinePay.total_num}</td>
					<td>${onlinePay.fail_order}</td>
					<td>${onlinePay.non_payment}</td>
					<td>${onlinePay.fail_payment}</td>
					<td>${onlinePay.succ_payment}</td>
					<td>${onlinePay.succ_num}</td>
					<td>${onlinePay.fail_num}</td>
					<td><fmt:formatNumber type="percent" value="${onlinePay.succ_rate}" maxFractionDigits="2" pattern="0.00%"/></td>
				    <td><fmt:formatNumber type="number" value="${onlinePay.actualAmount}" maxFractionDigits="2" pattern="0.00"/></td>
					 <td><fmt:formatNumber type="number" value="${onlinePay.succ_actual}" maxFractionDigits="2" pattern="0.00"/></td>
					<td><fmt:formatNumber type="percent" value="${onlinePay.succ_actual_rate}" maxFractionDigits="2" pattern="0.00%"/></td>
				</tr>
			</c:forEach>
			<c:forEach items="${total}" var="total" varStatus="status">
				<tr>
					<td>${status.count}</td>
					<td colspan="2">总计</td>
					<td>${total.total_num}</td>
					<c:set var="total_num" value="${total.total_num}"></c:set>
					<td>${total.fail_order}</td>
					<td>${total.non_payment}</td>
					<td>${total.fail_payment}</td>
					<td>${total.succ_payment}</td>
					<td>${total.succ_num}</td>
					<c:set var="succ_num" value="${total.succ_num}"></c:set>
					<td>${total.fail_num}</td>
					<td><fmt:formatNumber type="percent" value="${total.succ_rate}" maxFractionDigits="2" pattern="0.00%"/></td>
				    <td><fmt:formatNumber type="number" value="${total.actualAmount}" maxFractionDigits="2" pattern="0.00"/></td>
				    <c:set var="actualAmount" value="${total.actualAmount}"></c:set>
					<td><fmt:formatNumber type="number" value="${total.succ_actual}" maxFractionDigits="2" pattern="0.00"/></td>
					<c:set var="succ_actual" value="${total.succ_actual}"></c:set>
					<td><fmt:formatNumber type="percent" value="${total.succ_actual_rate}" maxFractionDigits="2" pattern="0.00%"/></td>
				</tr>
			</c:forEach>
		 </tbody>	
	</table>
	
	<table id="paycodeTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr><th colspan="12">按计费点统计</th></tr>
			<tr>
				<th onclick="$.sortTable.sort('paycodeTable',0,compareNumber)" style="cursor: pointer;">序号</th>
				<th>游戏</th>
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
			<c:forEach items="${onlinePaycodeList}" var="onlinePaycode" varStatus="status">
				<tr>
					<td>${status.count}</td>
					<td>${fns:getByCkAppNameByChild(onlinePaycode.ckAppId,onlinePaycode.childCkAppId)}(${onlinePaycode.ckAppId})</td>
					<td>${onlinePaycode.paycode_name}</td>
					<td>${onlinePaycode.total_num}</td>
					<td><fmt:formatNumber type="percent" value="${(0.000001+onlinePaycode.total_num)/total_num}" maxFractionDigits="2" pattern="0.00%"/></td>
					<td>${onlinePaycode.succ_num}</td>
					<td><fmt:formatNumber type="percent" value="${onlinePayList[onlinePayList.size()-1].succ_num ne 0?(0.000001+onlinePaycode.succ_num)/succ_num:0}" maxFractionDigits="2" pattern="0.00%"/></td>
					<td><fmt:formatNumber type="percent" value="${(0.000001+onlinePaycode.succ_num)/onlinePaycode.total_num}" maxFractionDigits="2" pattern="0.00%"/></td>
					<td><c:choose><c:when test="${not empty onlinePaycode.actualAmount}">
						<fmt:formatNumber type="number" value="${onlinePaycode.actualAmount}" maxFractionDigits="2" pattern="0.00"/>
						</c:when><c:otherwise>0.00</c:otherwise>
					</c:choose></td>
					<td><c:choose><c:when test="${ onlinePaycode.actualAmount ne 0}">
						<fmt:formatNumber type="percent" value="${(onlinePaycode.actualAmount)/(0.000001+total[total.size()-1].actualAmount)} " maxFractionDigits="2" pattern="0.00%"/>
					</c:when><c:otherwise>0.00%</c:otherwise></c:choose>
					</td>
					<td><fmt:formatNumber type="number" value="${onlinePaycode.succ_actual}" maxFractionDigits="2" pattern="0.00"/></td>
					<td><fmt:formatNumber type="percent" value="${onlinePayList[onlinePayList.size()-1].succ_actual ne '0.0000'?(0.000001+onlinePaycode.succ_actual)/succ_actual:0}" maxFractionDigits="2" pattern="0.00%"/></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<table id="channelInfoTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr><th colspan="7">按渠道统计</th></tr>
			<tr>
				<th onclick="$.sortTable.sort('channelInfoTable',0,compareNumber)" style="cursor: pointer;">序号</th>
				<td>游戏</td>
				<th onclick="$.sortTable.sort('channelInfoTable',1)" style="cursor: pointer;">渠道</th>
				<th onclick="$.sortTable.sort('channelInfoTable',2,compareNumber)" style="cursor: pointer;">总付费金额(元)</th>
				<th onclick="$.sortTable.sort('channelInfoTable',3,compareNumber)" style="cursor: pointer;">成功金额(元)</th>
				<th onclick="$.sortTable.sort('channelInfoTable',4,compareNumber)" style="cursor: pointer;">失败金额(元)</th>
				<th onclick="$.sortTable.sort('channelInfoTable',5,compareRate)" style="cursor: pointer;">成功率</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${onlineChannelList}" var="stats" varStatus="status">
				<tr>
					<td>${status.count}</td>
					<td>${fns:getByCkAppNameByChild(stats.ckAppId,stats.childCkAppId)}(${stats.ckAppId})</td>
					<td>${fns:findChannelName(stats.channelId,"")}(${stats.channelId})</td>
					<td><c:choose><c:when test="${not empty stats.actualAmount}">
					<fmt:formatNumber type="number" value="${stats.actualAmount}" maxFractionDigits="2" pattern="0.00"/></c:when>
					<c:otherwise>0.00</c:otherwise></c:choose></td>
					<td><fmt:formatNumber type="number" value="${stats.succ_actual}" maxFractionDigits="2" pattern="0.00"/></td>
					<td><c:choose><c:when test="${not empty stats.fail_actual}">
					<fmt:formatNumber type="number" value="${stats.fail_actual}" maxFractionDigits="2" pattern="0.00"/></c:when><c:otherwise>0.00</c:otherwise></c:choose></td>
					<td><c:choose><c:when test="${not empty stats.succ_actual && not empty stats.actualAmount}">
					<fmt:formatNumber type="percent" value="${(0.000001+stats.succ_actual)/stats.actualAmount}" maxFractionDigits="2" pattern="0.00%"/>
					</c:when><c:otherwise>0.00%</c:otherwise></c:choose></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<table id="channelpaycodeInfoTable" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr><th colspan="13">按渠道和计费点统计</th></tr>
		<tr>
			<th onclick="$.sortTable.sort('channelpaycodeInfoTable',0,compareNumber)" style="cursor: pointer;">序号</th>
			<th>游戏</th>
			<th onclick="$.sortTable.sort('channelpaycodeInfoTable',1)" style="cursor: pointer;">渠道</th>
			<th onclick="$.sortTable.sort('channelpaycodeInfoTable',2)" style="cursor: pointer;">计费点</th>
			<th onclick="$.sortTable.sort('channelpaycodeInfoTable',3,compareNumber)" style="cursor: pointer;">数量</th>
			<th onclick="$.sortTable.sort('channelpaycodeInfoTable',3,compareNumber)" style="cursor: pointer;">数量比例</th>
			<th onclick="$.sortTable.sort('channelpaycodeInfoTable',5)" style="cursor: pointer;">成功数量</th>
			<th onclick="$.sortTable.sort('channelpaycodeInfoTable',5,compareNumber)" style="cursor: pointer;">成功数量占比</th>
			<th onclick="$.sortTable.sort('channelpaycodeInfoTable',7,compareRate)" style="cursor: pointer;">成功率</th>
			<th onclick="$.sortTable.sort('channelpaycodeInfoTable',8,compareNumber)" style="cursor: pointer;">订购总价(元)</th>
			<th onclick="$.sortTable.sort('channelpaycodeInfoTable',8,compareNumber)" style="cursor: pointer;">	订购总价比例</th>
			<th onclick="$.sortTable.sort('channelpaycodeInfoTable',10,compareNumber)" style="cursor: pointer;">成功总价(元)</th>
			<th onclick="$.sortTable.sort('channelpaycodeInfoTable',10,compareNumber)" style="cursor: pointer;">成功金额占比</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${onlineChannelPaycodeList}" var="stats" varStatus="status">
			<tr>
				<td>${status.count}</td>
				<td>${fns:getByCkAppNameByChild(stats.ckAppId,stats.childCkAppId)}(${stats.ckAppId})</td>
				<td>${fns:findChannelName(stats.channelId,"")}(${stats.channelId})</td>
				<td><c:choose>
				<c:when test="${not empty stats.productid}">${stats.paycode_name}(${stats.productid})</c:when>
				<c:otherwise>${stats.paycode_name}</c:otherwise>
				</c:choose>
				</td>
				<td>${stats.total_num}</td>
				<td><fmt:formatNumber type="percent" value="${(0.000001+stats.total_num)/total_num}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td>${stats.succ_num}</td>
				<td><fmt:formatNumber type="percent" value="${onlinePayList[onlinePayList.size()-1].succ_num ne 0?(0.000001+stats.succ_num)/onlinePayList[onlinePayList.size()-1].succ_num:0}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td><fmt:formatNumber type="percent" value="${(0.000001+stats.succ_num)/(stats.total_num)}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td><c:choose><c:when test="${not empty stats.actualAmount}">
					<fmt:formatNumber type="number" value="${stats.actualAmount}" maxFractionDigits="2" pattern="0.00"/></c:when>
					<c:otherwise>0.00</c:otherwise></c:choose></td>
				<td><c:choose><c:when test="${not empty actualAmount && not empty onlinePayList[onlinePayList.size()-1].actualAmount}">
					<fmt:formatNumber type="percent" value="${(0.000001+stats.actualAmount)/actualAmount}" maxFractionDigits="2" pattern="0.00%"/></c:when>
					<c:otherwise>0.00%</c:otherwise></c:choose>
				</td>
				<td><fmt:formatNumber type="number" value="${stats.succ_actual}" maxFractionDigits="2" pattern="0.00"/></td>
				<td><fmt:formatNumber type="percent" value="${onlinePayList[onlinePayList.size()-1].succ_actual ne '0.0000'?(0.000001+stats.succ_actual)/succ_actual:0}" maxFractionDigits="2" pattern="0.00%"/></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</body>
</html>