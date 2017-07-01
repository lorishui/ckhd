<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>ZZ</title>
<meta name="decorator" content="default"/>
<script type="text/javascript" src="/static/tableOrder/tableOrder.js?a=1"></script>
<script type="text/javascript">
	$(document).ready(function() {
		changeAPPID(true);
		$("#ckappId").focus();
		$("#searchForm").validate({
			submitHandler: function(form){
				form.submit();
			},
			errorContainer: "#messageBox"
		});
	});
	function selectDateFn(val){
		var startDate = $("#startDate");
		if(val == 0){
			var day =  GetDateStr(0);
			startDate.val(day);
		}else if(val == 1){
			var day = GetDateStr(-1);
			startDate.val( day);
		}else if(val == 2){
			startDate.val( GetDateStr(-2));
		}else if(val == 7){
			startDate.val( GetDateStr(-7));
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
	<div id="position">ZZ订单数</div>
	<form:form id="searchForm"  modelAttribute="mmapporder" action="${ctx}/stats/zzorderstats/" method="post" class="breadcrumb form-search">
		<div>
			<label>游戏：</label>
			<span><font color="red">*</font> </span>
			<form:select id="ckappId" path="ckappId" class="input-medium" onChange="changeAPPID()">
				<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
		    </form:select>
			<form:select id="appId" path="appId" class="input-medium" style="width:300px">
		    </form:select>
			<label>时间：</label>
			<a href="#" onclick="javascript:selectDateFn(0);void(0)">今天</a>
			<a href="#" onclick="javascript:selectDateFn(1);void(0)">昨天</a>
			<a href="#" onclick="javascript:selectDateFn(2);void(0)">前天</a>
			<a href="#" onclick="javascript:selectDateFn(7);void(0)">7天前</a>
			<span><font color="red">*</font> </span>
			<input id="startDate" name="startDate" type="text" readonly="readonly" maxlength="20" class="WdateTime required"
				value="${mmapporder.startDate}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" style="width:100px" value="查    询"/>&nbsp;&nbsp;
		</div>
	</form:form>
	<div>
		<c:if test="${not empty result}">统计结果:<span style="color:red">${result.zzNum}</span></c:if>
	</div>
</body>
</html>