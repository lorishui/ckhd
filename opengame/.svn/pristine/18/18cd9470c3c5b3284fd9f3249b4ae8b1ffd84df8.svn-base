<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>图形展示</title>
<meta name="decorator" content="default"/>
<script type="text/javascript" src="/static/echarts/echarts.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
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
			url: "${ctx }/app/channelcarriers/getList?carriersType=MM&channelId="+channelId, success: function(data){
				var code = "<option value=''>全部</option>";
				for(var i=0;i<data.length;i++){
					 code+='<option value="'+data[i].id+'" >'+data[i].name+'('+data[i].id+')</option>'; 
				}
				carriersChannelId.append(code);
			}
		});
	}
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
/*	height:25px;*/
	background-color: #D6D6D6;
	/* margin-left:25px; */
	/* box-shadow: 0px 1px 3px rgba(34, 25, 25, 0.2); */
	/* -moz-border-radius: 15px;
    -webkit-border-radius: 15px;
    border-radius: 8px 8px 0px 0px; */
    
}
/*.button{
	margin-left:25px; 
	padding: 1px 1px 1px 1px;
}
*/
.graph{
	margin-left: 25px;
	width: 1000px;
	height: 400px;
	border: 1px solid #969696;
/*	border-color: #0d0d0d;
	border-style: solid;*/
	border-radius: 8px 8px 0px 0px;
	
}
.button{
	padding: 1px 1px 1px 1px;
	width: 80px;
	margin-left: 10px;
	margin-top: 10px;
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
	<form:form id="searchForm"  modelAttribute="offlineMoneyCount" action="${ctx}/offlineMoneyCount/graphical" method="post" class="breadcrumb form-search">
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
			<label>时间：</label>
			<span><font color="red">*</font> </span>
			<input id="startTime" name="startTime" type="text" readonly="readonly" maxlength="20" class="WdateTime required"
				value="${offlineMoneyCount.startTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>到
			<input id="endTime" name="endTime" type="text" readonly="readonly" maxlength="20" class="WdateTime required"
				value="${offlineMoneyCount.endTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
		</div>
		<%-- <div>
			<label>统计维度选择：</label>
			<label for="groupApp"><input id="groupApp" ${offlineMoneyCount.groupApp == 1 ?'checked="checked"':'' } name="groupApp" type="checkbox" value="1" />游戏</label>
			<label for="groupChannel"><input id="groupChannel" ${offlineMoneyCount.groupChannel == 1 ?'checked="checked"':'' } name="groupChannel" type="checkbox" value="1" />渠道</label>
			<label for="groupProvince"><input id="groupProvince" ${offlineMoneyCount.groupProvince == 1 ?'checked="checked"':'' } name="groupProvince" type="checkbox" value="1" />省份</label>
			<label for="groupCarriesChannel"><input id="groupCarriesChannel" ${offlineMoneyCount.groupCarriesChannel == 1 ?'checked="checked"':'' } name="groupCarriesChannel" type="checkbox" value="1" />运营渠道</label>
		</div>
		<br/> --%>
		<div class="query_column_space">
			<label>统计时间精度选择：</label>
			<%-- <input type="radio" name="timeType" value="1" ${offlineMoneyCount.timeType == 1 or offlineMoneyCount.timeType == 0?'checked="checked"':'' }>月 --%>
			<input type="radio" name="timeType" value="2" ${offlineMoneyCount.timeType == 2 ?'checked="checked"':'' }>日
			<input type="radio" name="timeType" value="3" ${offlineMoneyCount.timeType == 3 ?'checked="checked"':''}>时
			&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		</div>
	</form:form>
	
	<div class="graph">
		<div class="position">MM金额统计</div>
	    <div class="bottom">
	    	<button value="成功金额" onclick="clickSuccess(this);" class="button${offlineMoneyCount.dataType eq 0 or offlineMoneyCount.dataType eq 1?' check':'' }">成功金额<br/><button value="总金额" onclick="clickTotal(this);" class="button${offlineMoneyCount.dataType eq 2?' check':'' }">总金额</button>
	    </div>
	    
	    <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
	    <div id="main" style="width:950px;height:330px;background-color: #f5f5f5;"></div>
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
	            yAxis: {},
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
	            yAxis: {},
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