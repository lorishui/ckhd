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
	
	setInterval(function () {
		var ckAppId = $("#ckAppId").val();
		var childCkAppId = $("#childCkAppId").val();
		var ckChannelId = $("#channelId").val();
		 $.ajax({
          url : "${ctx}/stats/graphMoneyDynamic/data",
          type : "POST",
          contentType: "application/json;charset=utf-8",
          data : JSON.stringify({'ckAppId':ckAppId,'childCkAppId':childCkAppId,'ckChannelId':ckChannelId}),
          dataType : "json",
          success : function(data) {
        	  var myChart = echarts.init(document.getElementById('main'));
              var legend_data = data.legend_data.substring(2,data.legend_data.length-2).split("','");
              var xAxis_data = data.xAxis_data.substring(2,data.xAxis_data.length-2).split("','");
              var series = data.series;
        	  option = {
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
   		    
   	        myChart.setOption(option);
          },
          error:function(msg){
            $(".notice").html('Error:'+msg);
          }
        })
	}, 30000);
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
	<form:form id="searchForm"  modelAttribute="statMoney" action="${ctx}/stats/graphMoneyDynamic" method="post" class="breadcrumb form-search">
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
		    </form:select>&nbsp;&nbsp;&nbsp;&nbsp;
		    <input id="btnSubmit" class="btn btn-primary" type="submit" style="width:100px" value="查  询"/>
		</div>
	</form:form>
	
	<div class="graph">
		<div class="position"><font size="3">充值金额统计</font></div>
	   <button style="" value="成功金额" class="button">
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
	    	<button value="总金额" class="button">
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
	    
        myChart.setOption(option);
        	
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