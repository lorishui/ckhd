<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>和游戏订单统计</title>
<meta name="decorator" content="default"/>
<script type="text/javascript">
		$(document).ready(function() {
			changeVersionId(true);
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
		function exportStats(flag){
			//$('#tableID').tableExport({type:'pdf',escape:'false'});
			if(flag === 1){
				$("#mainInfoTable").tableExport({type:'excel',escape:'false'});
			}else if (flag === 2){
				$("#paycodeTable").tableExport({type:'excel',escape:'false'});
			}else if (flag === 3) {
				$("#errorcodeTable").tableExport({type:'excel',escape:'false'});
			}else{
				$("#errorOrdersTable").tableExport({type:'excel',escape:'false'});
			}
		}
		function selectDateFn(val){
			var startDate = $("#beginDate");
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
			    data:'ckAppId=' + $("#ckappId").val() + '&carriersType=ANDGAME',
			    error:function(){
			    },
			    success:function(data){
			    	$("#contentId").empty();
			    	$("#contentId").append("<option value='' selected>(全部)</option>");
			       for(var item in data){
			       	$("#contentId").append("<option value='"+data[item].appId +"'>"+data[item].appName + "(" + data[item].appId +")</option>");
			       }
			       if(pageLoad){
			    	   $("#contentId").val("${andapporder.contentId}");
					   $(".select2-chosen")[1].innerHTML = $("#contentId option:selected").text();
			       }else{
			    	   $("#contentId").val("");
				       $(".select2-chosen")[1].innerHTML = "(全部)";
			       }
			    }
			});
		}
		
		function changeVersionId(load){
			$.ajax({
			    url:'${ctx}/stats/queryAppVersionId',
			    type:'get',
			    data:'ckAppId=' + $("#ckappId").val(),
			    error:function(){
			    },
			    success:function(data){
			    	$("#versionId").empty();
			    	$("#versionId").append("<option value='' selected>(全部)</option>");
			       for(var item in data){
			       	$("#versionId").append("<option value='"+data[item].versionId +"'>"+data[item].versionId+"</option>");
			       }
			       if(load){
			    	   $("#versionId").val("${andapporder.versionId}");
					   $(".select2-chosen")[2].innerHTML = $("#versionId option:selected").text();
			       }else{
			    	   $("#versionId").val("");
				       $(".select2-chosen")[2].innerHTML = "(全部)";
			       }
			    }
			});
		}
</script>
</head>
<body>

<div id="position">移动和游戏订单统计：支付信息</div>

<form:form id="searchForm"  modelAttribute="andapporder" action="${ctx}/stats/andorder" method="post" class="breadcrumb form-search">
		<div>
			<sys:message content="${message}"/>
			<span class="help-inline"><font color="red">*</font> </span>
			<label>游戏：</label>
			<form:select id="ckappId" path="ckappId" class="input-medium" onChange="changeAPPID(),changeVersionId()">
				<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
		    </form:select>
		    <form:select id="contentId" path="contentId" class="input-medium" style="width:300px">
		    </form:select>
		    <label>和游戏版本号：</label>
			<form:select id="versionId" path="versionId" class="input-medium">
		    </form:select>
		 </div>
		 <br />
		 <div>
		    <label>省份：</label>
			<form:select id="province" path="provinceId" class="input-medium" >
				<form:option value="" label="(全部)"/>
				<form:options items="${fns:getDictList('province_andgame')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		    </form:select>
		    <label>渠道：</label>
			<form:select id="channelId" path="channelId" class="input-medium">
				<form:option value="" label="(全部)"/>
				<form:options items="${fns:getCarriersChannelList('ANDGAME')}" itemLabel="name" itemValue="id" htmlEscape="false"/>
		    </form:select>
	    </div>
		    <br />
	    <div>
		   <span class="help-inline"><font color="red">*</font> </span>
			<label>时间范围：&nbsp;</label>
			<a href="#" onclick="javascript:selectDateFn(0);void(0)">今天</a>
			<a href="#" onclick="javascript:selectDateFn(1);void(0)">昨天</a>
			<a href="#" onclick="javascript:selectDateFn(7);void(0)">近七天</a>
			<a href="#" onclick="javascript:selectDateFn(30);void(0)">近30天</a>
			<input id="beginDate" name="beginDate" type="text" readonly="readonly"  maxlength="20" class="WdateTime required"
				value="<fmt:formatDate value="${andapporder.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" 
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			<label>--</label>
			<input id="endDate" name="endDate" type="text" readonly="readonly"  maxlength="20" class="WdateTime required"
				value="<fmt:formatDate value="${andapporder.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" 
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				&nbsp;&nbsp;
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>	
		</div>
		<div style="margin-top:8px;">
			<input id="btnExport1" class="btn btn-primary"  onclick="javascript:exportStats(1)" type="button" value="全局统计导出"/>
			&nbsp;&nbsp;
			<input id="btnExport2" class="btn btn-primary" onclick="javascript:exportStats(2)" type="button" value="计费点统计导出"/>
			&nbsp;&nbsp;
			<input id="btnExport3" class="btn btn-primary" onclick="javascript:exportStats(3)" type="button" value="错误码统计导出"/>
		</div>
</form:form>

<table id="mainInfoTable"  data-tableName="Test Table 1"  class="table2excel table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>日期</th>
				<th>订单总数</th>
				<th>成功数目</th>
				<th>失败数目</th>
				<th>成功数目比例</th>
				<th>总金额</th>
				<th>成功金额(和游戏)</th>
				<th>失败金额(和游戏)</th>
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
				<td>
				<fmt:formatNumber type="percent" value="${order.succ_rate}"
					maxFractionDigits="2" pattern="0.00%"/>
				</td>
				<td>
					<fmt:formatNumber type="number" value="${order.total_price}"
					maxFractionDigits="2" pattern="0.00"/>
				</td>
				
				<td> 
				<fmt:formatNumber type="number" value="${order.succ_price}"
					maxFractionDigits="2" pattern="0.00"/>
				</td>
				<td><fmt:formatNumber type="number" value="${order.fail_price}"
					maxFractionDigits="2" pattern="0.00"/></td>
				<td>
					<fmt:formatNumber type="percent" value="${order.succ_price_rate}"
					maxFractionDigits="2" pattern="0.00%"/>
				</td>
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

<table id="paycodeTable" class="table2excel table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th onclick="$.sortTable.sort('paycodeTable',0,compareNumber)" class="sort-column">序号</th>
			<th onclick="$.sortTable.sort('paycodeTable',1)" class="sort-column">计费编码说明</th>
			<th onclick="$.sortTable.sort('paycodeTable',2,compareNumber)" class="sort-column">数量</th>
			<th onclick="$.sortTable.sort('paycodeTable',2,compareNumber)" class="sort-column">数量比例</th>
			<th onclick="$.sortTable.sort('paycodeTable',4,compareNumber)" class="sort-column">成功数量</th>
			<th onclick="$.sortTable.sort('paycodeTable',4,compareNumber)" class="sort-column">成功数量占比</th>
			<th onclick="$.sortTable.sort('paycodeTable',6,compareRate)" class="sort-column">成功率</th>
			<th onclick="$.sortTable.sort('paycodeTable',7,compareNumber)" class="sort-column">订购总价(元)</th>
			<th onclick="$.sortTable.sort('paycodeTable',7,compareNumber)" class="sort-column">订购总价占比</th>
			<th onclick="$.sortTable.sort('paycodeTable',9,compareNumber)" class="sort-column">成功总价(元)</th>
			<th onclick="$.sortTable.sort('paycodeTable',9,compareNumber)" class="sort-column">成功金额占比</th>
		</tr>
	</thead>	
	<tbody>
		<c:forEach items="${paycodeList}" var="paycode" varStatus="status">
			<tr>	
				<td>${status.count} </td>
				<td>${paycode.name}</td>
				<td>${paycode.num}</td>
				<td>
					<fmt:formatNumber type="percent" value="${ orderList[orderList.size()-1].total_num ne 0 ? paycode.num/orderList[orderList.size()-1].total_num:0}"
					maxFractionDigits="2" pattern="0.00%"/>
				</td>
			
				<td>${paycode.success_num}</td>
				<td>
					<fmt:formatNumber type="percent" value="${orderList[orderList.size()-1].succ_num ne 0 ?paycode.success_num/orderList[orderList.size()-1].succ_num:0}"
					maxFractionDigits="2" pattern="0.00%"/> 
				</td>
				<td>
					<fmt:formatNumber type="percent" value="${paycode.num ne 0 ? paycode.success_num/paycode.num:0}"
					maxFractionDigits="2" pattern="0.00%"/>
				</td>
				<td>
					<fmt:formatNumber type="number" value="${paycode.price*0.01}"
					maxFractionDigits="2" pattern="0.00"/>
				</td>
				<td>
					<fmt:formatNumber type="percent"  value="${orderList[orderList.size()-1].total_price ne 0 ? paycode.price*0.01/orderList[orderList.size()-1].total_price:0}"
			   maxFractionDigits="2" pattern="0.00%"/>
				</td>
				<td>
					<fmt:formatNumber type="number" value= "${paycode.success_price*0.01}" 
					maxFractionDigits="2" pattern="0.00"/>
				</td>
				<td>
					<fmt:formatNumber type="percent" value= "${orderList[orderList.size()-1].succ_price ne '0.00' ? (0.01*paycode.success_price/orderList[orderList.size()-1].succ_price):0}" 
					maxFractionDigits="2" pattern="0.00%"/>
				</td>
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
				<c:choose><c:when test="${(100*(0.01-((stats.total_price-stats.succ_price)*0.01/stats.total_price))) lt 0.2}">
				<td style="color:red">${status.count}</td>
				<td style="color:red">${fns:getChannelName(stats.channelId,"")}(${stats.channelId})</td>
				<td style="color:red"><fmt:formatNumber type="number" value="${0.01*stats.total_price}" maxFractionDigits="2" pattern="0.00"/>
				</td>
				<td style="color:red">
				<fmt:formatNumber type="number" value="${0.01*(stats.succ_price)}" maxFractionDigits="2" pattern="0.00"/>
				</td>
				<td style="color:red">
				<fmt:formatNumber type="number" value="${0.01*(stats.total_price-stats.succ_price)}" maxFractionDigits="2" pattern="0.00"/>
				</td>
				<td style="color:red">
				<fmt:formatNumber type="percent" value="${100*(0.01-((stats.total_price-stats.succ_price)*0.01/stats.total_price))}" maxFractionDigits="2" pattern="0.00%"/>
				</td>
				</c:when>
				<c:otherwise><td>${status.count}</td>
				<td>${fns:getChannelName(stats.channelId,"")}(${stats.channelId})</td>
				<td><fmt:formatNumber type="number" value="${0.01*stats.total_price}" maxFractionDigits="2" pattern="0.00"/></td>
				<td><fmt:formatNumber type="number" value="${0.01*(stats.succ_price)}" maxFractionDigits="2" pattern="0.00"/></td>
				<td><fmt:formatNumber type="number" value="${0.01*(stats.total_price-stats.succ_price)}" maxFractionDigits="2" pattern="0.00"/></td>
				<td><fmt:formatNumber type="percent" value="${100*(0.01-((stats.total_price-stats.succ_price)*0.01/stats.total_price))}" maxFractionDigits="2" pattern="0.00%"/></td>
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
				<c:choose><c:when test="${ province.total_num eq 0 or province.succ_num eq 0  or 0.1*(province.succ_num)/province.total_num*10 eq 0}">
				<td style="color:red">${status.count}</td>
				<td style="color:red">${fns:getDictLabel(province.provinceId, "province_andgame", province.provinceId)}</td>
				<td style="color:red">${province.total_num}</td>
				<td style="color:red"><fmt:formatNumber type="percent" value="${0.1*province.total_num/orderList[orderList.size()-1].total_num*10}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td style="color:red">${province.succ_num }</td>
				<td style="color:red"><fmt:formatNumber type="percent" value="${orderList[orderList.size()-1].succ_num ne 0?0.1*(province.succ_num)/orderList[orderList.size()-1].succ_num*10:0}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td style="color:red"><fmt:formatNumber type="percent" value="${0.1*(province.succ_num)/province.total_num*10}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td style="color:red"><fmt:formatNumber type="number" value="${province.total_price*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
				<td style="color:red"><fmt:formatNumber type="percent" value="${(province.total_price*0.01)/(orderList[orderList.size()-1].total_price)}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td style="color:red"><fmt:formatNumber type="number" value="${(province.succ_price)*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
				<td style="color:red"><fmt:formatNumber type="percent" value="${orderList[orderList.size()-1].succ_price ne '0.00'?(province.succ_price)*0.01/orderList[orderList.size()-1].succ_price:0 }" maxFractionDigits="2" pattern="0.00%"/></td>
				</c:when><c:otherwise>
				<td>${status.count}</td>
				<td>${fns:getDictLabel(province.provinceId, "province_andgame", province.provinceId)}</td>
				<td>${province.total_num}</td>
				<td><fmt:formatNumber type="percent" value="${0.1*province.total_num/orderList[orderList.size()-1].total_num*10}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td>${province.succ_num }</td>
				<td><fmt:formatNumber type="percent" value="${orderList[orderList.size()-1].succ_num ne 0?0.1*(province.succ_num)/orderList[orderList.size()-1].succ_num*10:0}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td><fmt:formatNumber type="percent" value="${0.1*(province.succ_num)/province.total_num*10}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td><fmt:formatNumber type="number" value="${province.total_price*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
				<td><fmt:formatNumber type="percent" value="${(province.total_price*0.01)/(orderList[orderList.size()-1].total_price)}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td><fmt:formatNumber type="number" value="${(province.succ_price)*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
				<td><fmt:formatNumber type="percent" value="${orderList[orderList.size()-1].succ_price ne '0.00'?(province.succ_price)*0.01/orderList[orderList.size()-1].succ_price:0 }" maxFractionDigits="2" pattern="0.00%"/></td>
				</c:otherwise></c:choose>
			</tr>
			</c:forEach>
		</tbody>
	</table>
	
<table id="errorcodeTable" class="table2excel table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th onclick="$.sortTable.sort('errorcodeTable',0,compareNumber)" class="sort-column">序号</th>
			<th onclick="$.sortTable.sort('errorcodeTable',1)" class="sort-column">错误码</th>
			<th onclick="$.sortTable.sort('errorcodeTable',2)" class="sort-column">错误码说明</th>
			<th onclick="$.sortTable.sort('errorcodeTable',3,compareNumber)" class="sort-column">数量</th>
			<th onclick="$.sortTable.sort('errorcodeTable',3,compareNumber)" class="sort-column">比例</th>
		</tr>
	 </thead>	
	 <tbody>
		<c:forEach items="${returnstatuList}" var="errorcode" varStatus="status">
			<tr>	
				<td>${status.count} </td>
				<td>${errorcode.returnStatus}</td>
				<td>${errorcode.error_name}</td>
				<td>${errorcode.error_num}</td>
				<td>
				<fmt:formatNumber type="percent" value="${errorcode.error_num/orderList[orderList.size()-1].fail_num}"
					maxFractionDigits="2" pattern="0.00%"/>
				</td>
				
			</tr>
		</c:forEach>
	 </tbody>	
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
					<td>${ null == stats.provinceName?'':stats.provinceName}</td>
					<td>${stats.status}</td>
					<td>${stats.sName}</td>
					<td>${stats.errorNum}</td>
					<td><fmt:formatNumber type="number" value="${stats.errorNum/orderList[orderList.size()-1].fail_num}" maxFractionDigits="2" pattern="0.00%"/></td>
				</tr>
			</c:forEach>
	</table>

</body>
</html>