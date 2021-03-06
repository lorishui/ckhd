<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>网络订单流水信息</title>
<meta name="decorator" content="default"/>
<script type="text/javascript">
	$(function(){
		changeAPPID(true);
	});
	function page(n,s){
		if(n) $("#pageNo").val(n);
		if(s) $("#pageSize").val(s);
		$("#searchForm").attr("action","${ctx}/stats/appOnlinePay/form");
		$("#searchForm").submit();
		return false;
	}
	function exportExcel(){
		$("#searchForm").attr("action","${ctx}/stats/appOnlinePay/exprotOnlinePay");
		$("#searchForm").submit();
		return false;
	}
	
	function changeAPPID(pageLoad){
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
		 		var id = '${onlinePay.childCkAppId}';
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
			$("#searchForm").attr("action","${ctx}/stats/appOnlinePay/form");
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
	
	function resendOrder(orderId,id){
		$.ajax({
            url : "${ctx}/stats/appOnlinePay/resend",
            type : 'get',
            data : 'orderId=' + orderId,
            dataType:'text',
            error : function() {
            	alert('error')
            },
            success : function(data) {
                if('success' == data){
                	alert("订单成功加入重发任务");
                	$("#"+id).css("color","#f00");
                }else{
                	alert("订单重发失败");
                }
            }
        });
	}
</script>
</head>
<body>
	<div id="position">网络订单流水信息</div>
	<form:form id="searchForm" modelAttribute="onlinePay" action="" method="post" class="breadcrumb form-search">
		<div>
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
			<label>游戏：</label>
			<form:select id="ckAppId" path="ckAppId" class="input-medium" onChange="changeAPPID()">
				<form:options items="${fns:getAPPCkListByChild()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
		    </form:select>
		     <span><font color="red">*</font> </span>
			<form:select id="childCkAppId" path="childCkAppId" class="input-medium" style="width:150px">
		    </form:select>
		    <label class="control-label">支付方式：</label>
		    <form:select path="payType" class="input-large" style="width:140px">
		   		<form:option value="" label="(全部)"/>
				<form:options items="${fns:getDictList('paytype')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select> <span class="help-inline"><font color="red">*</font></span>
			<label>渠道：</label>
			<form:select id="channelId" path="channelId" class="input-medium" style="width:110px">
				<form:option value="" label="(全部)"/>
				<form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
		    </form:select>
		   	<label>区ID：</label>
		   		<input type="text" name="zoneId" value="${onlinePay.zoneId }" style="width:60px"/>
	   		<label>服ID：</label>
				<input type="text" name="serverId" value="${onlinePay.serverId }" style="width:60px"/>
	   	</div> 
	   	<br>
	   	<div>
	   		<!-- <label>时间范围：</label>
			<a href="#" onclick="javascript:selectDateFn(0);void(0)">今天</a>
			<a href="#" onclick="javascript:selectDateFn(1);void(0)">昨天</a>
			<a href="#" onclick="javascript:selectDateFn(7);void(0)">近七天</a>
			<a href="#" onclick="javascript:selectDateFn(30);void(0)">近30天</a>
			<span><font color="red">*</font> </span> -->
	   		<label>统计日期：</label>
			<span><font color="red">*</font> </span>
		    <input id="startDate" name="startDate" type="text" readonly="readonly" maxlength="20" value="${onlinePay.startDate}"
		    class="WdateTime required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			&nbsp;&nbsp;
			<span><font color="red">*</font> </span>
			<input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" value="${onlinePay.endDate}"
			class="WdateTime required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	   		<label>订单状态：</label>
	   		<form:select id="orderStatus" path="orderStatus" class="input-mini" style="width:140px">
	   			<form:option value="" label="全部" selected="selected"/>
				<form:option value="-1" label="创建订单失败"/>
				<form:option value="0" label="未支付"/>
				<form:option value="1" label="订单申请成功"/>
				<form:option value="2" label="订单申请失败"/>
				<form:option value="3" label="支付成功"/>
				<form:option value="4" label="支付失败"/>
		    </form:select>
	   		<label>发货状态：</label>
	   		<form:select id="sendStatus" path="sendStatus" class="input-mini" style="width:140px">
	   			<form:option value="" label="全部" selected="selected"/>
				<form:option value="1" label="下发中"/>
				<form:option value="2" label="下发成功"/>
				<form:option value="3" label="下发失败"/>
				<form:option value="4" label="发货成功"/>
				<form:option value="5" label="发货失败"/>
		    </form:select>
	  </div>
	  <br>
	  	<div>
	   		<label>订单号/外部订单号：</label>
	   		<input type="text" name="orderId" value="${onlinePay.orderId }"/>
	   		<label>用户id：</label>
			<input type="text" name="userId" value="${onlinePay.userId }" style="width:100px"/>
	   		<label>角色id：</label>
			<input type="text" name="roleId" value="${onlinePay.roleId }" style="width:100px"/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input id="btnSubmit" class="btn btn-primary" type="submit" style="width:100px" onclick="return page();" value="查  询"/>&nbsp;&nbsp;
	  		<input id="exptSubmit" type="button" class="btn btn-primary" onClick ="return exportExcel();" value="导出报表"/>
	  </div>
	</form:form>
	<table id="onlineStats" class="table2excel table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>游 戏</th>
				<th>子游戏id</th>
				<th>渠 道</th>
				<th>区ID</th>
				<th>服ID</th>
				<th>用户ID</th>
				<th>角色ID</th>
				<th>预订单号</th>
				<th>订单号</th>
				<th>渠道订单号</th>
				<th>支付方式</th>
				<th>价格</th>
				<th>订单状态</th>
				<th>下发状态</th>
				<th>错误信息</th>
				<th>支付金额</th>
				<th>下单时间</th>
				<th>更新时间</th>
				<th>是否网游</th>
			</tr>
		 </thead>	
		 <tbody>
	     <c:forEach items="${page.list}" var="onlinePay" varStatus="status">
			<tr>
				<td>${status.count}</td>
				<td>${fns:getByCkAppNameByChild(onlinePay.ckAppId,onlinePay.childCkAppId)}(${onlinePay.ckAppId})</td>
				<td>${onlinePay.childCkAppId }</td>
				<td>${fns:findChannelName(onlinePay.channelId,"")}(${onlinePay.channelId})</td>
				<td>${onlinePay.zoneId}</td>
				<td>${onlinePay.serverId}</td>
				<td>${onlinePay.userId}</td>
				<td>${onlinePay.roleId}</td>
				<td>${not empty onlinePay.prepayid ? onlinePay.prepayid : "----"}</td>
				<td>${not empty onlinePay.orderId ? onlinePay.orderId : "----"}</td>
				<td>${not empty onlinePay.channelOrderId ? onlinePay.channelOrderId : "----"}</td>
				<td>${fns:findLabel(onlinePay.payType,"")}(${onlinePay.payType})</td>
				<td>${onlinePay.prices*0.01}</td>
			    <td><c:choose><c:when test="${onlinePay.orderStatus=='-1'}">创建订单失败</c:when>
				    <c:when test="${onlinePay.orderStatus=='0'}">未支付</c:when>
				    <c:when test="${onlinePay.orderStatus=='1'}">订单申请成功</c:when>
				    <c:when test="${onlinePay.orderStatus=='2'}">订单申请失败</c:when>
				    <c:when test="${onlinePay.orderStatus=='3'}">支付成功</c:when>
				    <c:when test="${onlinePay.orderStatus=='4'}">支付失败</c:when>
				    <c:otherwise>----</c:otherwise></c:choose>
				</td>
				<c:choose><c:when test="${not empty onlinePay.sendStatus}">
					<td><c:choose><c:when test="${onlinePay.sendStatus=='1'}">下发中</c:when>
					    <c:when test="${onlinePay.sendStatus=='2'}">下发成功</c:when>
					    <c:when test="${onlinePay.sendStatus=='3'}">下发失败</c:when>
					    <c:when test="${onlinePay.sendStatus=='4'}">发货成功</c:when>
					    <c:otherwise>发货失败</c:otherwise>
					    </c:choose>
					    <shiro:hasPermission name="stats:appOnlinePay:resend">
					       <c:if test="${onlinePay.orderStatus=='3' && (onlinePay.sendStatus=='5' || (onlinePay.sendStatus=='3'&&onlinePay.sendNum>3)) }">
					           <a href="javascript:void(0)" onclick="resendOrder('${onlinePay.orderId}','${onlinePay.id }')" id='${onlinePay.id }'>
					                                           重新发货
					           </a>
					       </c:if>
					    </shiro:hasPermission>
					</td>
					</c:when>
					<c:otherwise>
						<td align="center">----</td>
					</c:otherwise>
				</c:choose>
				<td>${onlinePay.errMsg }</td>
				<td><fmt:formatNumber type="number" value="${onlinePay.actualAmount*0.01}" maxFractionDigits="2" pattern="0.00"/></td>
				<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${onlinePay.createDate}" /></td>
				<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${not empty onlinePay.updateDate?onlinePay.updateDate:onlinePay.createDate}" /></td>
				<td><c:choose><c:when test="${onlinePay.gameOnline==0}">非网游</c:when>
				<c:otherwise>网游</c:otherwise></c:choose></td>
			</tr>
		 </c:forEach>
		 </tbody>	
	</table>
	<div class="pagination">${page}</div>
</body>
</html>