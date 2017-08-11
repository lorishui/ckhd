<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>新增图形展示</title>
<meta name="decorator" content="default"/>
<script type="text/javascript" src="/static/echarts/echarts.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	changeAPPID(true);
	$("#ckAppId").focus();
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
	var channelId = '${newUserCount.ckChannelId}';
	if(channelId != ''){
		getCarriersChannelId(channelId);
	}
});
	function changeAPPID(pageLoad) {
		var id = '${statMoney.childCkAppId}';
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
	border-radius: 8px;
	
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
	<form:form id="searchForm"  modelAttribute="statMoney" action="${ctx}/stats/graphMoney" method="post" class="breadcrumb form-search">
		<div class="query_column_space">
			<label>游戏：</label>
			<form:select id="ckAppId" path="ckAppId" class="input-medium"
				onChange="changeAPPID()">
				<form:option value="" label="(全部)" />
				<form:options items="${fns:getAPPCkListByChild()}" itemLabel="name"
					itemValue="ckappId" htmlEscape="false" />
			</form:select>
			<span><font color="red">*</font> </span>
			<form:select id="childCkAppId" path="childCkAppId"
				class="input-medium" style="width:250px">
			</form:select>
		    <label>渠道：</label>
			<form:select id="channelId" path="ckChannelId" class="input-medium">
				<form:option value="" label="(全部)"/>
				<form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
		    </form:select>
		</div>
		<div class="query_column_space">
			<label>时间：</label>
			<span><font color="red">*</font> </span>
			<input id="startTime" name="startTime" type="text" readonly="readonly" maxlength="20" class="WdateTime required"
				value="${statMoney.startTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>到
			<input id="endTime" name="endTime" type="text" readonly="readonly" maxlength="20" class="WdateTime required"
				value="${statMoney.endTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
		</div>
		<div class="query_column_space">
			<label>统计时间精度选择：</label>
			<%-- <input type="radio" name="timeStyle" value="1" ${statMoney.timeStyle == 1 or statMoney.timeStyle == 0?'checked="checked"':'' }>月 --%>
			<input type="radio" name="timeStyle" value="2" ${statMoney.timeStyle == 2 ?'checked="checked"':'' }>日
			<input type="radio" name="timeStyle" value="1" ${statMoney.timeStyle == 1 ?'checked="checked"':''}>时
			&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		</div>
	</form:form>
	
	<div class="graph">
		<div class="position"><font size="3">充值金额统计</font></div>
	   <button style="" value="成功金额" onclick="clickSuccess(this);" class="button${statMoney.dataType eq 0 or statMoney.dataType eq 1?' check':'' }">
	    		<label>
	    			<font size="1">成功金额</font><br/>
	    			<font size="5">${statMoney.successMoney}</font><br/>
	    		</label>
	    		<%-- <br/>
	    		<label style="width:100px;text-align:right">
	    			<font size="1" >
		    			<fmt:formatNumber type="percent" value="${totalHour.successMoney/yesterDayTotal.successMoney-1}" maxFractionDigits="2" pattern="0.00%"/>
			    		</font>
	    		</label> --%>
	    	</button>
	    	<button value="总金额" onclick="clickTotal(this);" class="button${statMoney.dataType eq 2?' check':'' }">
		    	<label>
		    		<font size="1">总金额</font><br/>
		    		<font size="5">${statMoney.money}</font><br/>
		    	</label>
		    	<%-- <br/>
		    	<label style="width:100px;text-align:right">
		    		<font size="1" >
		    		<fmt:formatNumber type="percent" value="${totalHour.totalMoney/yesterDayTotal.totalMoney-1}" maxFractionDigits="2" pattern="0.00%"/>
		    		</font>
		    	</label> --%>
	    	</button>
	    <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
	    <div id="main" style="height:350px;background-color: #f5f5f5;"></div>
	</div>
	    <script type="text/javascript">
	 // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));
        var legend_data = ${legend_data};
        var xAxis_data = ${xAxis_data};
        var series = ${series};
		var total = ${total};
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
            series: series
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
            series: total
        };
		
        var dataType = '${statMoney.dataType }'
        if(dataType == 2){
        	 // 使用刚指定的配置项和数据显示图表。
	        myChart.setOption(option1);
        }else{
        	// 使用刚指定的配置项和数据显示图表。
	        myChart.setOption(option);	
        }
        
        function clickTotal(node){
        	 // 使用刚指定的配置项和数据显示图表。
	        myChart.setOption(option1,true);	
	        setClass(node);
	        $("#dataType").attr("value",2);
        }
        
        function clickSuccess(node){
        	 // 使用刚指定的配置项和数据显示图表。
	        myChart.setOption(option,true);	
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