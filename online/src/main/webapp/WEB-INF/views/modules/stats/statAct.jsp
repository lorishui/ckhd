<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>网游统计报表</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
(function($){
    //插件
    $.extend($,{
        //命名空间
        sortTable:{
            sort:function(tableId,Idx,fn){
                var table = document.getElementById(tableId);
                var tbody = table.tBodies[1];
                var tr = tbody.rows; 
        
                var trValue = new Array();
                for (var i=0; i<tr.length; i++ ) {
                    trValue[i] = tr[i];  //将表格中各行的信息存储在新建的数组中
                }
        
                if (tbody.sortCol == Idx) {
                    trValue.reverse(); //如果该列已经进行排序过了，则直接对其反序排列
                } else {
                    //trValue.sort(compareTrs(Idx));  //进行排序
                    trValue.sort(function(tr1, tr2){
                        var value1 = tr1.cells[Idx].innerHTML;
                        var value2 = tr2.cells[Idx].innerHTML;
                        if(fn){
                            return fn(value1,value2);
                        }else{
                            return value1.localeCompare(value2);
                        }
                    });
                }
        
                var fragment = document.createDocumentFragment();  //新建一个代码片段，用于保存排序后的结果
                for (var i=0; i<trValue.length; i++ ) {
                    fragment.appendChild(trValue[i]);
                }
        
                tbody.appendChild(fragment); //将排序的结果替换掉之前的值
                tbody.sortCol = Idx;
                
            }
        }
    });       
})(jQuery);
</script>
<script type="text/javascript">
	$(document).ready(function() {
		changeAPPID(true);
		$("#ckAppId").focus();
		$("#searchForm").validate({
			submitHandler : function(form) {
				$("#searchForm").attr("action", "${ctx}/stats/act");
				form.submit();
			},
			errorContainer : "#messageBox"
		});
	});
	function changeAPPID(pageLoad) {
		var id = '${statRelate.childCkAppId}';
		$.ajax({
			url : "${ctx}/app/appck/getChildIdList",
			type : 'get',
			data : 'ckappId=' + $("#ckAppId").val(),
			error : function() {
			},
			success : function(data) {
				var node = $("#childCkAppId");
				node.empty();
				var html = "<option value=\"\">全部</option>";
				for (var n = 0; n < data.length; n++) {
					html += "<option value='"+data[n].childId+"'>"
							+ data[n].name + "_" + data[n].childId
							+ "</option>";
				}
				node.append(html);
				node.val(id);
				var name = node.find("option:selected").text();
				if (id != null && id != '') {
					$(".select2-chosen")[1].innerHTML = name;
				}
			}
		});
	}

	function selectDateFn(val) {
		var startDate = $("#startDate");
		var endDate = $("#endDate");
		if (val == 0) {
			var day = GetDateStr(0);
			startDate.val(day);
			endDate.val(day);
		} else if (val == 1) {
			var day = GetDateStr(-1);
			startDate.val(day);
			endDate.val(day);
		} else if (val == 7) {
			startDate.val(GetDateStr(-6));
			endDate.val(GetDateStr(0));
		} else if (val == 30) {
			startDate.val(GetDateStr(-29));
			endDate.val(GetDateStr(0));
		}
		if (val >= 0) {
			$("#searchForm").submit();
		}
	}
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
	function exportExcel(){
		var $body = $('body');
		var $form = $('#searchForm').clone();
			$form.attr("action","${ctx}/stats/actExport");
			$form.appendTo($body);
			$form.submit();
			$form.remove();
		return false;
	}
</script>
</head>
<body>
	<div id="position">网游统计报表</div>
	<form:form id="searchForm" modelAttribute="statRelate"
		action="${ctx}/stats/act" method="post" class="breadcrumb form-search">

		<div>
			<label>游戏：</label>
			<form:select id="ckAppId" path="ckAppId" class="input-medium"
				onChange="changeAPPID()">
				<form:option value="" label="(全部)" />
				<form:options items="${fns:getAPPCkListByChild()}" itemLabel="name"
					itemValue="ckappId" htmlEscape="false" />
			</form:select>
			<span><font color="red">*</font> </span>
			<c:if test="${appPerm != 1}">
				<form:select id="childCkAppId" path="childCkAppId"
					class="input-medium" style="width:250px">
				</form:select>
			</c:if>
			<label>渠道：</label>
			<form:select id="channelId" path="ckChannelId" class="input-medium">
				<form:option value="" label="(全部)" />
				<form:options items="${fns:getChannelList()}" itemLabel="name"
					itemValue="id" htmlEscape="false" />
			</form:select>
		</div>
		<br>
		<div>
			<label>时间范围：</label> <a href="#" onclick="javascript:selectDateFn(0);void(0)">今天</a> 
			<a href="#" onclick="javascript:selectDateFn(1);void(0)">昨天</a>
			<a href="#" onclick="javascript:selectDateFn(7);void(0)">近七天</a>
			<a href="#" onclick="javascript:selectDateFn(30);void(0)">近30天</a> 
			<span><font color="red">*</font> </span> 
			<label>统计日期：</label> 
			<span><font color="red">*</font> </span> 
			<input id="startDate" name="startTime" type="text" readonly="readonly" maxlength="20"
				value="${statRelate.startTime}" class="WdateTime required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />&nbsp;&nbsp;
			<span><font color="red">*</font> </span> 
			<input id="endDate" name="endTime" type="text" readonly="readonly"
				maxlength="20" value="${statRelate.endTime}"
				class="WdateTime required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
		</div>
		<br>
			<div class="query_column_space">
				<label>统计精度选择：</label>
		  <c:if test="${appPerm != 1}">
				<input type="checkbox" name="groupChildCkAppId" value="1" ${statRelate.groupChildCkAppId == 1 ?'checked="checked"':'' }>统计各子游戏
		  </c:if>
				<input type="checkbox" name="groupChannel" value="1" ${statRelate.groupChannel == 1 ?'checked="checked"':'' }>统计各渠道
		  <c:if test="${appPerm != 1}">
				<input type="checkbox" name="groupChildChannel" value="1" ${statRelate.groupChildChannel == 1 ?'checked="checked"':''}>统计各子渠道
		  </c:if>
				&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
				<input id="exptSubmit" type="button" class="btn btn-primary" onclick="return exportExcel();" value="导出报表">
			</div>
	</form:form>
	<table id="statsOnline"
		class="table2excel table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th onclick="$.sortTable.sort('statsOnline',0,compareNumber)" style="cursor: pointer;">序号</th>
				<th onclick="$.sortTable.sort('statsOnline',1,compareNumber)" style="cursor: pointer;">日期</th>
				<th onclick="$.sortTable.sort('statsOnline',2,compareNumber)" style="cursor: pointer;">游戏</th>
				<c:choose>
				   <c:when test="${statRelate.groupChildCkAppId == 1}"> 
				    <th onclick="$.sortTable.sort('statsOnline',3,compareNumber)" style="cursor: pointer;">子游戏id</th>      
				   </c:when>
				</c:choose>
				<c:choose>
				   <c:when test="${statRelate.groupChannel == 1}"> 
				    <th onclick="$.sortTable.sort('statsOnline',${3+statRelate.groupChildCkAppId},compareNumber)" style="cursor: pointer;">渠道</th>      
				   </c:when>
				</c:choose>
				<c:choose>
				   <c:when test="${statRelate.groupChildChannel == 1}"> 
				    <th onclick="$.sortTable.sort('statsOnline',${3+statRelate.groupChildCkAppId+statRelate.groupChannel},compareNumber)" style="cursor: pointer;">子渠道号</th>     
				   </c:when>
				</c:choose>
				<th onclick="$.sortTable.sort('statsOnline',${3+statRelate.groupChildCkAppId+statRelate.groupChannel+statRelate.groupChildChannel},compareNumber)" style="cursor: pointer;">新增数</th>
				<th onclick="$.sortTable.sort('statsOnline',${4+statRelate.groupChildCkAppId+statRelate.groupChannel+statRelate.groupChildChannel},compareNumber)" style="cursor: pointer;">活跃数</th>
				<th onclick="$.sortTable.sort('statsOnline',${5+statRelate.groupChildCkAppId+statRelate.groupChannel+statRelate.groupChildChannel},compareNumber)" style="cursor: pointer;">活跃新增比</th>
				<th onclick="$.sortTable.sort('statsOnline',${6+statRelate.groupChildCkAppId+statRelate.groupChannel+statRelate.groupChildChannel},compareRate)" style="cursor: pointer;">次留</th>
				<th onclick="$.sortTable.sort('statsOnline',${7+statRelate.groupChildCkAppId+statRelate.groupChannel+statRelate.groupChildChannel},compareRate)" style="cursor: pointer;">7留</th>
				<th onclick="$.sortTable.sort('statsOnline',${8+statRelate.groupChildCkAppId+statRelate.groupChannel+statRelate.groupChildChannel},compareNumber)" style="cursor: pointer;">成功金额</th>
				<th onclick="$.sortTable.sort('statsOnline',${9+statRelate.groupChildCkAppId+statRelate.groupChannel+statRelate.groupChildChannel},compareNumber)" style="cursor: pointer;">成功充值人数</th>
				<th onclick="$.sortTable.sort('statsOnline',${10+statRelate.groupChildCkAppId+statRelate.groupChannel+statRelate.groupChildChannel},compareNumber)" style="cursor: pointer;">成功充值次数</th>
				<th onclick="$.sortTable.sort('statsOnline',${11+statRelate.groupChildCkAppId+statRelate.groupChannel+statRelate.groupChildChannel},compareNumber)" style="cursor: pointer;">ARPU</th>
				<th onclick="$.sortTable.sort('statsOnline',${12+statRelate.groupChildCkAppId+statRelate.groupChannel+statRelate.groupChildChannel},compareNumber)" style="cursor: pointer;">ARPPU</th>
				<th onclick="$.sortTable.sort('statsOnline',${13+statRelate.groupChildCkAppId+statRelate.groupChannel+statRelate.groupChildChannel},compareRate)" style="cursor: pointer;">付费率</th>
			</tr>
		</thead>
		<tbody>
		    <tr>
               <td colspan="${3+statRelate.groupChildCkAppId+statRelate.groupChannel+statRelate.groupChildChannel}" style="text-align:center;">汇总</td>
               <td>${total.newNum}</td>
               <td>${total.actNum}</td>
               <c:choose>
                   <c:when test="${total.newNum != 0}">
                       <td><fmt:formatNumber value="${total.actNum/total.newNum }" pattern="0.00"/></td>
                   </c:when>
                   <c:otherwise><td></td></c:otherwise>
               </c:choose>
               <td>
                   <c:if test="${total.newNum != 0 }">
                       <fmt:formatNumber value="${total.reten1/(total.newNum*0.01+0.000001)}" pattern="0.00"/>%
                   </c:if>
               </td>
               <td>
                   <c:if test="${total.newNum != 0 }">
                       <fmt:formatNumber value="${total.reten7/(total.newNum*0.01+0.000001)}" pattern="0.00"/>%
                   </c:if>
               </td>
               <td>${total.successMoney/100}</td>
               <td>${total.paySuccessPeople}</td>
               <td>${total.paySuccessTimes}</td>
               <td>
                   <fmt:formatNumber value="${(total.successMoney/100)/(total.actNum+0.000001)}" pattern="0.00"/>
               </td>
               <td>
                   <fmt:formatNumber value="${(total.successMoney/100)/(total.paySuccessPeople+0.000001)}" pattern="0.00"/>
               </td>
               <td>
                   <c:if test="${total.actNum != 0 }">
                       <fmt:formatNumber value="${total.paySuccessPeople/(total.actNum*0.01+0.000001)}" pattern="0.00"/>%
                   </c:if>
               </td>
           </tr>
        </tbody>
		<tbody>
			<c:forEach items="${data}" var="stat" varStatus="status">
				<tr>
					<td>${status.count}</td>
					<td>${stat.timeframes}</td>
					<td>${fns:getByCkAppName(stat.ckAppId)}(${stat.ckAppId})</td>
					<c:choose>
					   <c:when test="${statRelate.groupChildCkAppId == 1}"> 
					    <td>${fns:getByChildAppName(stat.ckAppId,stat.childCkAppId)}(${stat.childCkAppId}) </td>      
					   </c:when>
					</c:choose>
					<c:choose>
					   <c:when test="${statRelate.groupChannel == 1}"> 
					    <td>${fns:findChannelName(stat.ckChannelId,"")}(${stat.ckChannelId})</td>     
					   </c:when>
					</c:choose>
					<c:choose>
					   <c:when test="${statRelate.groupChildChannel == 1}"> 
					    <td>${stat.childChannelId}</td>     
					   </c:when>
					</c:choose>
					<td>${stat.newNum}</td>
					<td>${stat.actNum}</td>
					<c:choose>
						<c:when test="${stat.newNum != 0}">
							<td><fmt:formatNumber value="${stat.actNum/stat.newNum }" pattern="0.00"/></td>
						</c:when>
						<c:otherwise><td></td></c:otherwise>
					</c:choose>
					<td>
						<c:if test="${stat.newNum != 0 }">
							<fmt:formatNumber type="percent" minFractionDigits="2" value="${stat.reten1/(stat.newNum+0.000001)}"/>
						</c:if>
					</td>
					<td>
						<c:if test="${stat.newNum != 0 }">
							<fmt:formatNumber type="percent" minFractionDigits="2" value="${stat.reten7/(stat.newNum+0.000001)}"/>
						</c:if>
					</td>
					<td>${stat.successMoney/100}</td>
					<td>${stat.paySuccessPeople}</td>
					<td>${stat.paySuccessTimes}</td>
					<td>
						<fmt:formatNumber value="${(stat.successMoney/100)/(stat.actNum+0.000001)}" pattern="0.00"/>
					</td>
					<td>
						<fmt:formatNumber value="${(stat.successMoney/100)/(stat.paySuccessPeople+0.000001)}" pattern="0.00"/>
					</td>
					<td>
						<c:if test="${stat.actNum != 0 }">
							<fmt:formatNumber type="percent" minFractionDigits="2" value="${stat.paySuccessPeople/(stat.actNum+0.000001)}"/>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>