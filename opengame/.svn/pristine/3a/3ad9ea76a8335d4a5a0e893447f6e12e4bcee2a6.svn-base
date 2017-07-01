<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>游戏事件统计</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(function(){
			changeAPPID(true);
			_w_table_rowspan("#statsEventTable",1);
		});
		function query(){
			var startDate = new Date($("#startDate").val());
			var endDate = new Date($("#endDate").val());
			
			if(endDate.getTime() - startDate.getTime() > 6 * 24 * 3600 * 1000){
				$("#message").html("日期不能超过7天");
				$("#message").focus();
				$("#message").blur(function() {
					$(this).html("");
				});
				return false;
			}
			changeAPPID(true);
			_w_table_rowspan("#statsEventTable",1);
			$("#searchForm").attr("action","${ctx}/stats/appEventStat/list");
			$("#searchForm").submit();
			return false;
		} 
		
		function exp(){
			changeAPPID(true);
			$("#searchForm").attr("action","${ctx}/stats/appEventStat/exprotEventStat");
			$("#searchForm").submit();
			return false;
		}
		function changeAPPID(pageLoad){
			$.ajax({
			    url:'${ctx}/stats/queryAppEvent',
			    type:'get',
			    data:'ckAppId=' + $("#ckAppId").val(),
			    error:function(){
			    },
			    success:function(data){
			    	$("#appId").empty();
			    	$("#appId").append("<option value='' selected>(全部)</option>");
			       for(var item in data){
			       	$("#appId").append("<option value='"+data[item].appId +"'>"+data[item].appName + "(" + data[item].appId +")</option>");
			       }
			       if(pageLoad){
			    	   $("#appId").val("${appEventStat.appId}");
					   $(".select2-chosen")[1].innerHTML = $("#appId option:selected").text();
			       }else{
			    	   $("#appId").val("");
				       $(".select2-chosen")[1].innerHTML = "(全部)";
			       }
			    }
			});
		}
		function selectDateFn(val){
			var startDate = $("#startDate");
			var endDate = $("#endDate");
			if(val == 0){
				var day =  GetDateStr(0);
				startDate.val(day);
				endDate.val(day);
			}else if(val == 1){
				var day = GetDateStr(-1);
				startDate.val( day);
				endDate.val( day);
			}else if(val == 7){
				startDate.val( GetDateStr(-6));
				endDate.val( GetDateStr(0));
			}else if(val == 30){
				startDate.val( GetDateStr(-29));
				endDate.val( GetDateStr(0));
			}
			if(val >= 0){
				$("#searchForm").attr("action","${ctx}/stats/appEventStat/list");
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
		
		function _w_table_rowspan(_w_table_id,_w_table_colnum){
			var _w_table_firsttd = "";
			var _w_table_currenttd = "";
			var _w_table_SpanNum = 0;
			var _w_table_Obj = $(_w_table_id + " tr td:nth-child(" + _w_table_colnum + ")");
		    _w_table_Obj.each(function(i){
			if(i==0){
				 _w_table_firsttd = $(this);
			     _w_table_SpanNum = 1;
			}else{
				_w_table_currenttd = $(this);
			    if(_w_table_firsttd.text()==_w_table_currenttd.text()){
			        _w_table_SpanNum++;
			        _w_table_currenttd.hide(); //remove();
			        _w_table_firsttd.attr("rowSpan",_w_table_SpanNum);
		        }else{
		        	_w_table_firsttd = $(this);
			        _w_table_SpanNum = 1;
			    }
			 }
			}); 
		}
	</script>
</head>
<body>
	<div id="position">游戏事件统计</div>
	<form:form id="searchForm"  modelAttribute="appEventStat" action="${ctx}/stats/appEventStat/list" method="post" class="breadcrumb form-search">
	<br>
		<div>
			<label>游戏：</label>
			<form:select id="ckAppId" path="ckAppId" class="input-medium" onChange="changeAPPID()">
				<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
		    </form:select>
		     <span><font color="red">*</font> </span>
			<form:select id="appId" path="appId" class="input-medium" style="width:300px">
		    </form:select>
			<label>渠道：</label>
			<form:select id="ckChannelId" path="ckChannelId" class="input-medium">
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
			<!-- a href="#" onclick="javascript:selectDateFn(30);void(0)">近30天</a-->
			<span><font color="red">*</font> </span>
	   		<label>统计日期：</label>
			<span><font color="red">*</font> </span>
		    <input id="startDate" name="startDate" type="text" readonly="readonly" maxlength="20" value="${appEventStat.startDate}"
		    class="WdateTime required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			&nbsp;&nbsp;
			<span><font color="red">*</font> </span>
			<input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" value="${appEventStat.endDate}"
			class="WdateTime required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input id="btnSubmit" class="btn btn-primary" type="submit" style="width:100px" onClick = "return query();" value="查  询"/>&nbsp;&nbsp;
			<!--input id="eptSubmit" type="button" class="btn btn-primary" style="width:100px" onClick = "return exp();" value="导出报表" /-->
			<br/><span tabindex="0" id="message" class="help-inline" style="color: red; border: 2px;"></span>
	  </div>
	  <br>
	</form:form>
	<table id="statsEventTable" class="table2excel table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th onclick="$.sortTable.sort('statsEventTable',0,compareNumber)" class="sort-column">序号</th>
				<th onclick="$.sortTable.sort('statsEventTable',1,compareNumber)" class="sort-column">日期</th>
				<th onclick="$.sortTable.sort('statsEventTable',2,compareNumber)" class="sort-column">新增用户数</th>
				<th onclick="$.sortTable.sort('statsEventTable',3,compareNumber)" class="sort-column">活跃用户数</th> 
			</tr>
		 </thead>	
		 <tbody>
		 <c:forEach items="${statsList}" var="statsEvent" varStatus="status">
				<tr>
					<td>${status.count} </td>
					<td>${statsEvent.statsTime}</td>
					<td><c:choose><c:when test="${not empty statsEvent.add}">
						${statsEvent.add}
					</c:when><c:otherwise>0</c:otherwise></c:choose></td>
					<td><c:choose><c:when test="${not empty statsEvent.active}">
						${statsEvent.active}
					</c:when><c:otherwise>0</c:otherwise></c:choose></td>
				</tr>
			</c:forEach>
		 </tbody>	
	</table>
</body>
</html>