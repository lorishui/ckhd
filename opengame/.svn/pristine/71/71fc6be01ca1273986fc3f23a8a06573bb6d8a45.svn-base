<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>图形展示</title>
<meta name="decorator" content="default"/>
<script type="text/javascript" src="/static/echarts/echarts.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	var width = document.body.clientWidth;
	$(".graph").css("width",width-85);
	$(".position").css("width",width-90);
	$("#main").css("width",width-100);
	$("#searchForm").validate({
		submitHandler: function(form){
			//loading('正在提交，请稍等...');
			
			form.submit();
		},
		errorContainer: "#messageBox"
	});
	$("#channelId").change(function(){
		var channelId = $('#channelId option:selected').val();
		getCarriersChannelId(channelId);
	});
	var channelId = '${offlineMoneyCount.ckChannelId}';
	if(channelId != ''){
		getCarriersChannelId(channelId);
	}
});

function getCarriersChannelId(channelId){
	var carriersChannelId = $("#carriersChannelId");
	carriersChannelId.html("");
	 if(channelId != ''){
		$.ajax({ 
			url: "${ctx }/app/channelcarriers/getList?carriersType=ANDGAME&channelId="+channelId, success: function(data){
				var code = "<option value=''>全部</option>";
				for(var i=0;i<data.length;i++){
					 code+='<option value="'+data[i].id+'" >'+data[i].name+'('+data[i].id+')</option>'; 
				}
				console.log(code);
				carriersChannelId.append(code);
				var channelId = '${offlineMoneyCount.carriersChannelId}';
				if( channelId != '' ){
					 $("#carriersChannelId").val(channelId);
					 $(".select2-chosen")[2].innerHTML = $("#carriersChannelId option:selected").text();
				}
			}
		});
	}
}

function selectDateFn(val){
	var startTime = $("#startTime");
	var endTime = $("#endTime");
	if(val == 0){
		var day = GetDateStr(0);
		startTime.val(day);
		endTime.val(day);
	}else if(val == 1){
		var day = GetDateStr(-1);
		startTime.val( day);
		endTime.val(day);
	}else if(val == 2){
		startTime.val( GetDateStr(-2));
		endTime.val(GetDateStr(-2));
	}
	if(val >= 0){
		$("#searchForm").submit();
		return false;
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
<style type="text/css">
body{ 
	background-color:#f5f5f5;
}
.query_column_space{
	padding-top:3px;
	padding-bottom:3px;
}
.position{
/*	padding-top:5px;*/
	padding-left:5px;
/*	width:1100px;*/
	height:20px;
	background-color: #D6D6D6;
	/* margin-left:25px; */
	/* box-shadow: 0px 1px 3px rgba(34, 25, 25, 0.2); */
	/* -moz-border-radius: 15px;
    -webkit-border-radius: 15px;
    border-radius: 8px 8px 0px 0px; */
    
}
.button{
	/* margin-left:25px; */
	padding: 5px 5px 5px 5px;
}
.graph{
	margin-left: 25px;
	/* width: 1115px; */
	height: 410px;
	border: 1px solid #969696;
	border-radius: 8px 8px 0px 0px;
	
	margin-left: 25px;
	
}
.button{
	padding: 1px 1px 1px 1px;
	width: 120px;
	margin-left: 20px;
	margin-top: 20px;
}
.check{
	background-color: #6fa8dc;
	
}
.button:hover {
    background: #f47c20;
}
</style>
</head>
<body> 
	<form:form id="searchForm"  modelAttribute="offlineMoneyCount" action="${ctx}/offlineMoneyCount/graphAndByManyDay" method="post" class="breadcrumb form-search">
		<input id="dataType" type="hidden" name="dataType" value="${offlineMoneyCount.dataType }" />
		<div class="query_column_space">
			<label>游戏：</label>
			<form:select id="ckappId" path="ckAppId" class="input-medium" onChange="changeAPPID()">
				<form:option value="" label="(全部)"/>
				<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
		    </form:select>
		    <label>渠道：</label>
			<form:select id="channelId" path="ckChannelId" class="input-medium">
				<form:option value="" label="(全部)"/>
				<form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
		    </form:select>
		   <form:select id="carriersChannelId" path="carriersChannelId" class="input-medium" style="width:300px">
		    	<form:option value="" label="(全部)"/>
		    </form:select>
		</div>
		<div class="query_column_space">
			<label>省份：</label>
			<form:select id="province" path="province" class="input-mini" >
				<form:option value="" label="(全部)"/>
				<form:options items="${fns:getDictList('province')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		    </form:select>
			<label>统计日期：</label>
			<span><font color="red">*</font> </span>
			<input id="startTime" name="startTime" type="text" readonly="readonly" maxlength="20" class="WdateTime required"
				value="${offlineMoneyCount.startTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>到
			<input id="endTime" name="endTime" type="text" readonly="readonly" maxlength="20" class="WdateTime required"
				value="${offlineMoneyCount.endTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
		</div>
		<div class="query_column_space">
			<label>时间范围：&nbsp;</label>
			<a href="#" onclick="javascript:selectDateFn(0);void(0)">今天</a>
			<a href="#" onclick="javascript:selectDateFn(1);void(0)">昨天</a>
			<a href="#" onclick="javascript:selectDateFn(2);void(0)">前天</a>
			<label>统计时间精度选择：</label>
			<%-- <input type="radio" name="timeType" value="1" ${offlineMoneyCount.timeType == 1 or offlineMoneyCount.timeType == 0?'checked="checked"':'' }>月 --%>
			<input type="radio" name="timeType" value="2" ${offlineMoneyCount.timeType == 2 ?'checked="checked"':'' }>日
			<input type="radio" name="timeType" value="3" ${offlineMoneyCount.timeType == 3 ?'checked="checked"':''}>时
			&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		</div>
	</form:form>
	
	<div class="graph">
		<div class="position">和游戏金额统计</div>
	    <div class="bottom">
	    	<button style="" value="成功金额" onclick="clickSuccess(this);" class="button${offlineMoneyCount.dataType eq 0 or offlineMoneyCount.dataType eq 1?' check':'' }">
	    		<label>
	    			<font size="1">成功金额</font><br/>
	    			<font size="5">${total.successMoney/100}</font><br/>
	    		</label>
	    		<br/>
	    		<label style="width:100px;text-align:right">
	    			<font size="1" >
		    			<fmt:formatNumber type="percent" value="${totalHour.successMoney/yesterDayTotal.successMoney-1}" maxFractionDigits="2" pattern="0.00%"/>
			    		</font>
	    		</label>
	    	</button>
	    	<button value="总金额" onclick="clickTotal(this);" class="button${offlineMoneyCount.dataType eq 2?' check':'' }">
		    	<label>
		    		<font size="1">总金额</font><br/>
		    		<font size="5">${total.totalMoney/100}</font><br/>
	    		</label>
	    		<br/>
	    		<label style="width:100px;text-align:right">
	    			<font size="1" >
		    			<fmt:formatNumber type="percent" value="${totalHour.totalMoney/yesterDayTotal.totalMoney-1}" maxFractionDigits="2" pattern="0.00%"/>
			    		</font>
	    		</label>
	    	</button>
	    </div>
	    <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
	    <div id="main" style="height:290px;background-color: #f5f5f5;"></div>
	</div>
	    <script type="text/javascript">
	        // 基于准备好的dom，初始化echarts实例
	        var myChart = echarts.init(document.getElementById('main'));
	        var legend_data = ${legend_data};
	        var xAxis_data = ${xAxis_data};
	        var sercies = ${sercies};
			var serciesSuccess = ${serciesSuccess};
	        // 指定图表的配置项和数据
	        var option = {
	           /*  title: {
	            	show:true,
	            	textAlign:'left',
	                text: '${title}'
	            }, */
	            tooltip:{
	                trigger: 'axis'
	            },
	            legend:{
	            	orient:'horizontal',
	            	align:'left',
	            	itemGap:20,
	                data: legend_data
	            },
	            toolbox: {
	            	orient:'vertical',
	                // y: 'bottom',
	                feature: {
	                    magicType: {
	                        type: ['bar', 'line']
	                    },
	                    dataView: {},
	                    saveAsImage: {
	                        pixelRatio: 2
	                    }
	                }
	            },
	            xAxis: {
	                data: xAxis_data
	            },
	            yAxis: {
	            	name:'金额(元)'
	            },
	            backgroundColor:'#f5f5f5',
	            series: sercies
	        };
	        
	        // 指定图表的配置项和数据
	        var option1 = {
	           /*  title: {
	            	show:true,
	            	textAlign:'left',
	                text: '${title}'
	            }, */
	            tooltip:{
	                trigger: 'axis'
	            },
	            legend:{
	            	orient:'horizontal',
	            	align:'left',
	            	itemGap:20,
	                data: legend_data
	            },
	            toolbox: {
	            	orient:'vertical',
	                feature: {
	                    magicType: {
	                        type: ['bar', 'line']
	                    },
	                    dataView: {readOnly: false},
	                    saveAsImage: {
	                         pixelRatio: 2 
	                    }
	                }
	            },
	            xAxis: {
	                data: xAxis_data
	            },
	            yAxis: {
	            	name:'金额(元)'
	            },
	            backgroundColor:'#f5f5f5',
	            series: serciesSuccess
	        };
			
	        var dataType = '${offlineMoneyCount.dataType }'
	        if(dataType == 2){
	        	 // 使用刚指定的配置项和数据显示图表。
		        myChart.setOption(option);
	        }else{
	        	// 使用刚指定的配置项和数据显示图表。
		        myChart.setOption(option1);	
	        }
	        
	        function clickTotal(node){
	        	 // 使用刚指定的配置项和数据显示图表。
		        myChart.setOption(option,true);	
		        setClass(node);
		        $("#dataType").attr("value",2);
	        }
	        
	        function clickSuccess(node){
	        	 // 使用刚指定的配置项和数据显示图表。
		        myChart.setOption(option1,true);	
		        setClass(node);
		        $("#dataType").attr("value",1);
	        }
	        
	        function setClass(node){
	        	var b = $(node);
	        	var name = b.attr("class");
	        	var a = $(".check");
	        	if(name == 'button'){
	        		b.attr("class","button check");
	        		a.attr("class","button");
	        	}
	        	
	        }
	    </script>
</body>
</html>