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
		function page(n, s) {
			if (n)
				$("#pageNo").val(n);
			if (s)
				$("#pageSize").val(s);
			$("#searchForm").attr("action", "${ctx}/stats/andChannelProvince");
			$("#searchForm").submit();
			return false;
		}
		function exportExcel() {
			$("#searchForm").attr("action","${ctx}/stats/exportAndChannelProvince");
			$("#searchForm").submit();
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
		function orderCol(a){
			var orderDire = getCookie("orderDire");
			if(orderDire == null){
				orderDire = "Asc";
			}
			if("Desc" == orderDire){
				setCookie("orderDire","Asc");
			}else{
				setCookie("orderDire","Desc");
			}
			if(a == '1'){
				$("#searchForm").attr("action", "${ctx}/stats/andChannelProvince?orderByTP=1&orderDire="+orderDire);
				$("#searchForm").submit();
			}else{
				$("#searchForm").attr("action", "${ctx}/stats/andChannelProvince?orderBySP=1&orderDire="+orderDire);
				$("#searchForm").submit();
			}
		}
		function setCookie(name,value){
			var Days = 30;
			var exp = new Date();
			exp.setTime(exp.getTime() + Days*24*60*60*1000);
			document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
		}
		function getCookie(name){
			var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
			if(arr=document.cookie.match(reg))
			return unescape(arr[2]);
			else
			return null;
		}
</script>
</head>
<body>

<div id="position">移动和游戏订单统计：渠道省份</div>

<form:form id="searchForm"  modelAttribute="andChannelProvince" action="" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();" />
		<div>
			<sys:message content="${message}"/>
			<span class="help-inline"><font color="red">*</font> </span>
			<label>游戏：</label>
			<form:select id="ckappId" path="ckappId" class="input-medium" onChange="changeAPPID(),changeVersionId()">
				<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
		    </form:select>&nbsp;&nbsp;&nbsp;
		    <form:select id="contentId" path="contentId" class="input-medium" style="width:200px">
		    </form:select>
		    <label>和游戏版本号：</label>
				<form:select id="versionId" path="versionId" class="input-medium">
		    </form:select>
		    <label>渠道：</label>
				<form:select id="channelId" path="channelId" class="input-medium">
					<form:option value="" label="(全部)"/>
					<form:options items="${fns:getCarriersChannelList('ANDGAME')}" itemLabel="name" itemValue="id" htmlEscape="false"/>
		    </form:select>
		 </div>
		 <br>
		 <div>
		 	 <span class="help-inline"><font color="red">*</font> </span>
		    <label>省份：</label>
			<form:select id="province" path="provinceId" class="input-medium" >
				<form:option value="" label="(全部)"/>
				<form:options items="${fns:getDictList('province_andgame')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		    </form:select>
		    <span class="help-inline"><font color="red">*</font> </span>
			<label>时间范围：&nbsp;</label>
			<a href="#" onclick="javascript:selectDateFn(0);void(0)">今天</a>
			<a href="#" onclick="javascript:selectDateFn(1);void(0)">昨天</a>
			<a href="#" onclick="javascript:selectDateFn(7);void(0)">近七天</a>
			<a href="#" onclick="javascript:selectDateFn(30);void(0)">近30天</a>
			<input id="beginDate" name="startDate" type="text" readonly="readonly"  maxlength="20" class="WdateTime required"
				value="<fmt:formatDate value="${andChannelProvince.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" 
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			<label>--</label>
			<input id="endDate" name="endDate" type="text" readonly="readonly"  maxlength="20" class="WdateTime required"
				value="<fmt:formatDate value="${andChannelProvince.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" 
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				&nbsp;&nbsp;
			<input id="btnSubmit" class="btn btn-primary" type="submit" onClick="return page();" value="查询"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" class="btn btn-primary" onClick ="return exportExcel();" value="统计结果导出" />	
	    </div>
</form:form>


<table id="provinceInfoTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th onclick="$.sortTable.sort('provinceInfoTable',0,compareNumber)" style="cursor: pointer;">渠道号</th>
				<th onclick="$.sortTable.sort('provinceInfoTable',0,compareNumber)" style="cursor: pointer;">渠道名称</th>
				<th onclick="$.sortTable.sort('provinceInfoTable',2)" style="cursor: pointer;">省份</th>
				<th onclick="$.sortTable.sort('provinceInfoTable',3,compareNumber)" style="cursor: pointer;">数量</th>
				<th onclick="$.sortTable.sort('provinceInfoTable',3,compareNumber)" style="cursor: pointer;">数量比例</th>
				<th onclick="$.sortTable.sort('provinceInfoTable',5,compareNumber)" style="cursor: pointer;">成功数量</th>
				<th onclick="$.sortTable.sort('provinceInfoTable',5,compareNumber)" style="cursor: pointer;">成功数量占比</th>
				<th onclick="$.sortTable.sort('provinceInfoTable',7,compareRate)" style="cursor: pointer;">成功率</th>
				<th onclick="orderCol(1)" style="cursor: pointer;">订购总价(元)</th>
				<th onclick="orderCol(1)" style="cursor: pointer;">订购总价比例</th>
				<th onclick="orderCol(2)" style="cursor: pointer;">成功总价(元)</th>
				<th onclick="orderCol(2)" style="cursor: pointer;">成功金额占比</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="province" varStatus="status">
			<tr>
				<c:choose><c:when test="${province.totalNum eq 0 or province.succNum eq 0  or 0.1*(province.succNum)/province.totalNum*10 <0.2}">
				<td style="color:red">${province.channelId}</td>
				<td style="color:red">${fns:getChannelName(province.channelId,"")}</td>
				<td style="color:red">${fns:getDictLabel(province.provinceId, "province_andgame", province.provinceId)}</td>
				<td style="color:red">${province.totalNum}</td>
				<td style="color:red"><fmt:formatNumber type="percent" value="${0.1*province.totalNum/succ[succ.size()-1].total_num*10}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td style="color:red">${province.succNum }</td>
				<td style="color:red"><fmt:formatNumber type="percent" value="${succ[succ.size()-1].succ_num ne 0?0.1*(province.succNum)/succ[succ.size()-1].succ_num*10:0}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td style="color:red"><fmt:formatNumber type="percent" value="${0.1*(province.succNum)/province.totalNum*10}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td style="color:red"><fmt:formatNumber type="number" value="${province.totalPrice*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
				<td style="color:red"><fmt:formatNumber type="percent" value="${(province.totalPrice+0.000001)/(succ[succ.size()-1].total_price)}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td style="color:red"><fmt:formatNumber type="number" value="${(province.succPrice)*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
				<td style="color:red"><fmt:formatNumber type="percent" value="${succ[succ.size()-1].succ_price ne '0'?(province.succPrice+0.000001)/(succ[succ.size()-1].succ_price):0 }" maxFractionDigits="2" pattern="0.00%"/></td>
				</c:when><c:otherwise>
				<td>${province.channelId}</td>
				<td>${fns:getChannelName(province.channelId,"")}</td>
				<td>${fns:getDictLabel(province.provinceId, "province_andgame", province.provinceId)}</td>
				<td>${province.totalNum}</td>
				<td><fmt:formatNumber type="percent" value="${0.1*province.totalNum/succ[succ.size()-1].total_num*10}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td>${province.succNum }</td>
				<td><fmt:formatNumber type="percent" value="${succ[succ.size()-1].succ_num ne 0?0.1*(province.succNum)/succ[succ.size()-1].succ_num*10:0}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td><fmt:formatNumber type="percent" value="${0.1*(province.succNum)/province.totalNum*10}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td><fmt:formatNumber type="number" value="${province.totalPrice*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
				<td><fmt:formatNumber type="percent" value="${(province.totalPrice+0.000001)/(succ[succ.size()-1].total_price)}" maxFractionDigits="2" pattern="0.00%"/></td>
				<td><fmt:formatNumber type="number" value="${(province.succPrice)*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
				<td><fmt:formatNumber type="percent" value="${succ[succ.size()-1].succ_price ne 0?(province.succPrice+0.000001)/(succ[succ.size()-1].succ_price):0 }" maxFractionDigits="2" pattern="0.00%"/></td>
				</c:otherwise></c:choose>
			</tr>
			</c:forEach>
		</tbody>
	</table>
<div class="pagination">${page}</div>
</body>
</html>