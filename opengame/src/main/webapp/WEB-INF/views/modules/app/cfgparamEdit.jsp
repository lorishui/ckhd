<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>配置文件修改</title>
<meta name="decorator" content="default"/>
<script type="text/javascript">
	$(function(){
		$("#btn").click(function(){
			
			var versionName = $("#versionName").val();
			 if(versionName == ''){
				 alert("版本号不能为空!");
				 return;
			 }
			 var carries = $('input[name="carriesA"]:checkbox:checked').val();
			 if(carries == undefined){
				 alert("运营商不能为空!");
				 return;
			 }
			 var channel = $('input[name="channel"]:checkbox:checked').val();
			 if(channel == undefined){
				 alert("渠道不能为空!");
				 return;
			 }
			 var mmapp = $('input[name="mmapp"]:checkbox:checked').val();
			 if(mmapp == undefined){
				 alert("mmappid不能为空!");
				 return;
			 }
			 var pro = $('input[name="pro"]:checkbox:checked').val();
			 if(pro == undefined){
				 alert("省份不能为空!");
				 return;
			 }
			 
	         var json = {};
	         $("#json textarea").each(function(){
	        	 json[$(this).attr("id")]=$(this).attr("value");
	         });
	         $("#jsonStr").attr("value",JSON.stringify(json));
	         var options  = {};
	         $("#inputForm").submit();
		});
		
		$.ajax({ url: "${ctx }/sys/dict/getList?type=province_iccid", success: function(data){
	    	var html = $("#provice");
	    	html.empty();
	    	var code = "";
			if('${cfg.province}' == '#'){
				$('input[name="pro"]:checkbox').attr("checked", true);
	    	}
	    	var pArr = '${cfg.province}'.split(',');
	    	for(var i=0;i<data.length;i++){
	    		var isChecked = 0;
	    		for(var j=0;j<pArr.length;j++){
	    			if(pArr[j] == data[i].value){
		    			if(i%6==5){
	    					isChecked = 1;
	    					code+='<input name="pro" type="checkbox" checked="checked" value="'+data[i].value+'" /><label style="width:65px">'+data[i].label+"("+data[i].value+")"+"</label><br\>";		
			    		}else{
		    				isChecked = 1;
		    				code+='<input name="pro" type="checkbox" checked="checked" value="'+data[i].value+'" /><label style="width:65px">'+data[i].label+"("+data[i].value+")"+'</label>';	
			    		}	
	    			}
	    		}
	    		if(isChecked != 1){
	    			if(i%6==5){
	    				code+='<input name="pro" type="checkbox" value="'+data[i].value+'" /><label style="width:65px">'+data[i].label+"("+data[i].value+")"+"</label><br\>";		
		    		}else{
		    			code+='<input name="pro" type="checkbox" value="'+data[i].value+'" /><label style="width:65px">'+data[i].label+"("+data[i].value+")"+'</label>';	
		    		}
	    		}
	    	}
	    	html.append(code);
	    }});
		
		$.ajax({ url: "${ctx }/app/channel/getList", success: function(data){
	    	var html = $("#ckChannelId");
	    	html.empty();
	    	var code = "";
	    	if('${cfg.ckChannelId}' == '#'){
	    		$('input[name="channel"]:checkbox').attr("checked", true);
	    	}
	    	var cArr = '${cfg.ckChannelId}'.split(',');
	    	for(var i=0;i<data.length;i++){
	    		var isChecked = 0;
	    		for(var j=0;j<cArr.length;j++){
	    			if(cArr[j]==data[i].id){
			    		if(i%4==3){
			    			isChecked = 1;
			    			code+='<input name="channel" type="checkbox" checked="checked" value="'+data[i].id+'" /><label style="width:250px">'+data[i].name+"("+data[i].id+")"+'</label><br/>';
			    		}else{
			    			isChecked = 1;
			    			code+='<input name="channel" type="checkbox" checked="checked" value="'+data[i].id+'" /><label style="width:250px">'+data[i].name+"("+data[i].id+")"+'</label>';	
			    		}
	    			}
	    		}
	    		if(isChecked != 1){
	    			if(i%6==5){
		    			code+='<input name="channel" type="checkbox" value="'+data[i].id+'" /><label style="width:100px">'+data[i].name+"("+data[i].id+")"+'</label><br/>';
		    		}else{
		    			code+='<input name="channel" type="checkbox" value="'+data[i].id+'" /><label style="width:100px">'+data[i].name+"("+data[i].id+")"+'</label>';	
		    		}
	    		}
	    	}
	    	html.append(code);
	    }});
		
		$.ajax({ url: "${ctx }/app/appcarriers/getList?ckAppId=${cfg.ckAppId}", success: function(data){
			var mm = $("#mmappid");
			mm.empty();
			var code = "";
			var cArr = '${cfg.mmAppId}'.split(',');
			for(var i=0;i<data.length;i++){
				var isChecked = 0;
				for(var j=0;j<cArr.length;j++){
					if(cArr[j] == data[i].id){
						isChecked = 1;
						code+='<input name="mmapp" type="checkbox" checked="checked" value="'+data[i].id+'" id="m'+data[i].id+'" /><label style="width:250px" for="m'+data[i].id+'">'+data[i].name+"("+data[i].id+")"+"</label>";
						if(i%3==2){
							code+="<br/>";	
						}
					}
				}
				if(isChecked != 1){
					code+='<input name="mmapp" type="checkbox" value="'+data[i].id+'" id="m'+data[i].id+'" /><label style="width:250px" for="m'+data[i].id+'">'+data[i].name+"("+data[i].id+")"+"</label>";
					if(i%3==2){
						code+="<br/>";	
					}
				}
			}
			mm.append(code);
		}});
		
		$("#cAll").change(function(){
			var c = $("#cC").prop("checked");
			if($("#cAll").prop("checked")){
				$('input[name="channel"]:checkbox').attr("checked", true);
			}else{
				$('input[name="channel"]:checkbox').attr("checked", false);
			}
			$("#cC").attr("checked",c);
		});
		
		$("#pAll").change(function(){
			var p = $("#pP").prop("checked");
			if($("#pAll").prop("checked")){
				$('input[name="pro"]:checkbox').attr("checked", true);
			}else{
				$('input[name="pro"]:checkbox').attr("checked", false);
			}
			$("#pP").attr("checked", p);
		});
		
		$("#cChange").change(function(){
			var c = $("#cC").prop("checked");
			var arr = $('input[name="channel"]:checkbox');
			for(var i=1;i<arr.length;i++){
				var arrs = $('input[name="channel"]:checkbox:eq('+i+')');
				if(arrs.prop("checked")){
					arrs.attr("checked", false);
				}else{
					arrs.attr("checked", true);
				}
			}
			$("#cC").attr("checked",c);
		});
		
		$("#pChange").change(function(){
			var p = $("#pP").prop("checked");
			var arr = $('input[name="pro"]:checkbox');
			for(var i=1;i<arr.length;i++){
				var arrs = $('input[name="pro"]:checkbox:eq('+i+')');
				if(arrs.attr("checked")){
					arrs.attr("checked", false);
				}else{
					arrs.attr("checked", true);
				}
			}
			$("#pP").attr("checked", p);
		});
	});
	
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/app/list">配置文件列表</a></li>
		<li><a href="${ctx}/app/insert">配置添加</a></li>
		<li class="active"><a>配置修改</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="cfg" action="${ctx }/app/save"  method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="ckAppId"/>
		<input type="hidden" name="type" value="${type }"/>
		<input id="jsonStr" type="hidden" name="jsonStr" value="${cfg.exInfo }"/>
		<div class="control-group">
			<label class="control-label">游戏:</label>
			<div class="controls">
				<input  readonly="readonly" class="required" value="${fns:getByCkAppName(cfg.ckAppId) }(${cfg.ckAppId })"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">类型:</label>
			<div class="controls">
				<input id="rqType" name="rqType" type="rqType" readonly="readonly" class="required" value="${cfg.rqType }"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">运营商:</label>
			<div class="controls">
				<input name="carriesA" type="checkbox" ${cfg.carriers eq '#' ?'checked="checked"':'' } value="#" id="ccarAll"/><label for="ccarAll" style="width:20px">#</label>
				<input name="carriesA" type="checkbox" ${fn:contains(cfg.carriers,'CMCC') ?'checked="checked"':'' } value="CMCC" id="CMCC"/><label for="CMCC" style="width:72px">CMCC(移动)</label>
				<input name="carriesA" type="checkbox" ${fn:contains(cfg.carriers,'CTCC') ?'checked="checked"':'' } value="CTCC" id="CTCC"/><label for="CTCC" style="width:70px">CTCC(电信)</label>
				<input name="carriesA" type="checkbox" ${fn:contains(cfg.carriers,'CUCC') ?'checked="checked"':'' } value="CUCC" id="CUCC"/><label for="CUCC" style="width:70px">CUCC(联通)</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">mmappid:</label>
			<div class="controls">
				<input name="mmapp" id="mM" type="checkbox" ${cfg.mmAppId eq '#' ?'checked="checked"':'' } value="#" /><label style="width:65px" for="mM">#</label><br/>
				<div id="mmappid">
				</div>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">渠道:</label>
			<div class="controls">
				<input name="channel" id="cC" type="checkbox" ${cfg.ckChannelId eq '#' ?'checked="checked"':'' } value="#" /><label style="width:96px" for="cC">#</label>
				<input id="cAll" type="checkbox" /><label  style="width:96px" for="cAll">全选</label>
				<input id="cChange" type="checkbox" /><label  style="width:96px" for="cChange">反选</label><br/>
				<div id="ckChannelId" >
				</div>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">版本号:</label>
			<div class="controls">
				<input id="versionName" name="versionName" type="versionName" class="required" value="${cfg.versionName }"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">省份:</label>
			<div class="controls">
				<input name="pro" id="pP" type="checkbox" value="#" ${cfg.province eq '#' ?'checked="checked"':'' } /><label style="width:61px" for="pP">#</label>
				<input id="pAll" type="checkbox" /><label  style="width:61px" for="pAll">全选</label>
				<input id="pChange" type="checkbox" /><label  style="width:61px" for="pChange">反选</label><br/>
				<div id="provice">
				</div>
			</div>
		</div>
		<c:if test="${map ne null }">
			<c:forEach items="${map }" var="data">
			<div class="control-group" >
				<label class="control-label">${data.label}:</label>
				<div class="controls">
					<div id="json">
						<textarea id="${data.key }"  style="width:500px" rows="10" >${data.value }</textarea><label><font color="red">${data.desc }</font></a></label>
					</div>
				</div>
			</div>
			</c:forEach>
		</c:if>
	</form:form>
	<div class="form-actions">
		<button id="btn" class="btn btn-primary" value="保 存" >保 存</button>
	</div>
</body>
</html>