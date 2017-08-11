<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>配置文件添加</title>
<meta name="decorator" content="default"/>
<script type="text/javascript">
	$(function(){
		$("#btn").click(function(){
			 var ckAppId = $("#ckAppId").val();
			 if(ckAppId == ''){
				 alert("游戏不能为空!");
				 return;
			 }
			 var versionName = $("#versionName").val();
			 if(versionName == ''){
				 alert("版本号不能为空!");
				 return;
			 }
			 var channel = $('input[name="channel"]:checkbox:checked').val();
			 if(channel == undefined){
				 alert("渠道不能为空!");
				 return;
			 }
			 var pro = $('input[name="pro"]:checkbox:checked').val();
			 if(pro == undefined){
				 alert("省份不能为空!");
				 return;
			 }
	         var json = {};
	         $("#json input").each(function(){
	        	 json[$(this).attr("id")]=$(this).attr("value");
	         });
	         $("#json textarea").each(function(){
	        	 json[$(this).attr("id")]=$(this).attr("value");
	         });
	         $("#json select").each(function(){
	        	 json[$(this).attr("id")]=$(this).attr("value");
	         });
	         $("#jsonStr").attr("value",JSON.stringify(json));
	         var options  = {};
	         $("#inputForm").submit();
		});
		
		$("#ckAppId").change(function(data){
			var ckAppId = $("#ckAppId").val();
			var rqType = $("#rqType").val();
			$.ajax({
				 type: 'POST',
				  url:  "${ctx}/app/appck/getChildIdList",
				  data: "ckappId="+ckAppId,
				  datatype:"json",
				  success: function(data){
					 		var node = $("#childCkAppId");
					 		node.empty();
					 		var html = "<option value=\"#\">全部</option>";
					 		var html = "";
					 		for(var n=0;n<data.length;n++){
					 			html+="<option value='"+data[n].childId+"'>"+data[n].name+"_"+data[n].childId+"</option>";
					 		}
					 		node.append(html);
					 		var id = '${cfg.childCkAppId}';
					 		node.val(id);
							var name = node.find("option:selected").text();
					 		if( id != null && id != '' ){
					 			$(".select2-chosen")[1].innerHTML = name;	
					 		}
						  }
				 });
			if( ckAppId != '' && rqType != '' ){
				$.ajax({ url: "${ctx }/app/getFieldList?ckAppId="+ckAppId+"&rqType="+rqType, success: function(data){
			    	var html = $("#data");
			    	html.empty();
			    	var code = "";
			    	var classType = "";
			    	for(var i=0;i<data.length;i++){
			    		classType += data[i].value+"-"+data[i].classType+",";
			    		code += "<div class='control-group' >"+"<div id='json'>";
						code += "<label class='control-label'>"+data[i].label+"("+data[i].value+"):</label>";
						code += "<div class='controls'>";
						if(data[i].classType == 1 || data[i].classType == 5){
							code += "<input id='"+data[i].value+"' name='"+data[i].value+"' type='text'  />";
						}
						if(data[i].classType == 3 || data[i].classType == 4){
							code += "<textarea id='"+data[i].value+"'  style='width:500px' rows='10' ></textarea>";
						}
						if(data[i].classType == 2){
							code += "<select name='"+data[i].value+"' id='data.value'>";
							code += "<option value='true'>true</option>";
							code += "<option value='false'>false</option></select>";
						}
						code += "<font color='red'>"+data[i].description+"</font></div></div></div>";
			    	}
			    	html.append(code);
			    	if(classType.length > 1){
			    		classType = classType.substring(0, classType.length-1);
			    	}
			    	$("#classType").val(classType);
			    }});
			}else{
				var html = $("#data");
		    	html.empty();
			}
		});
		
		var ckappid = $("#ckAppId").val();
		 $.ajax({
			 type: 'POST',
			  url:  "${ctx}/app/appck/getChildIdList",
			  data: "ckappId="+ckappid,
			  datatype:"json",
			  success: function(data){
				 		var node = $("#childCkAppId");
				 		node.empty();
				 		var html = "<option value=\"#\">全部</option>";
				 		var html = "";
				 		for(var n=0;n<data.length;n++){
				 			html+="<option value='"+data[n].childId+"'>"+data[n].name+"_"+data[n].childId+"</option>";
				 		}
				 		node.append(html);
				 		var id = '${cfg.childCkAppId}';
				 		node.val(id);
						var name = node.find("option:selected").text();
				 		if( id != null && id != '' ){
				 			$(".select2-chosen")[1].innerHTML = name;	
				 		}
					  }
			 });
		
		$("#rqType").change(function(data){
			var ckAppId = $("#ckAppId").val();
			var rqType = $("#rqType").val();
			if( ckAppId != '' && rqType != '' ){
				$.ajax({ url: "${ctx }/app/getFieldList?ckAppId="+ckAppId+"&rqType="+rqType, success: function(data){
			    	var html = $("#data");
			    	html.empty();
			    	var code = "";
			    	var classType = "";
			    	//console.log(data);
			    	for(var i=0;i<data.length;i++){
			    		classType += data[i].value+"-"+data[i].classType+",";
			    		code += "<div class='control-group' >"+"<div id='json'>";
						code += "<label class='control-label'>"+data[i].label+"("+data[i].value+"):</label>";
						code += "<div class='controls'>";
						if(data[i].classType == 1 || data[i].classType == 5){
							code += "<input id='"+data[i].value+"' name='"+data[i].value+"' type='text'  />";
						}
						if(data[i].classType == 3 || data[i].classType == 4){
							code += "<textarea id='"+data[i].value+"'  style='width:500px' rows='10' ></textarea>";
						}
						if(data[i].classType == 2){
							code += "<select name='"+data[i].value+"' id='"+data[i].value+"'>";
							code += "<option value='true'>true</option>";
							code += "<option value='false'>false</option></select>";
						}
						code += "<font color='red'>"+data[i].description+"</font></div></div></div></div>";
			    	}
			    	html.append(code);
			    	//console.log(classType);
			    	if(classType.length > 1){
			    		classType = classType.substring(0, classType.length-1);
			    	}
			    	//console.log(classType);
			    	$("#classType").val(classType);
			    }});
			}else{
				var html = $("#data");
		    	html.empty();
			}
		});
		
		$.ajax({ url: "${ctx }/sys/dict/getList?type=province_iccid", success: function(data){
	    	var html = $("#provice");
	    	html.empty();
	    	var code = "";
	    	//console.log(data);
	    	for(var i=0;i<data.length;i++){
	    		if(i%6==5){
	    			code+='<input name="pro" type="checkbox" value="'+data[i].value+'" id="p'+data[i].value+'" /><label style="width:65px" for="p'+data[i].value+'">'+data[i].label+"("+data[i].value+")"+"</label><br\>";	
	    		}else{
	    			code+='<input name="pro" type="checkbox" value="'+data[i].value+'" id="p'+data[i].value+'"/><label style="width:65px" for="p'+data[i].value+'">'+data[i].label+"("+data[i].value+")"+'</label>';	
	    		}
	    	}
	    	html.append(code);
	    }});
		
		$("#cAll").change(function(){
			var c = $("#cC").prop("checked");
			console.log($("#cAll"));
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
	
	
	function getChannel(){
		var key = encodeURI($("#key").val());
		$.ajax({ url: "${ctx }/app/channel/getList",data:"key="+key,type:'POST', success: function(data){
	    	var html = $("#ckChannelId");
	    	html.empty();
	    	var code = "";
	    	for(var i=0;i<data.length;i++){
	    		code+='<input name="channel" type="checkbox" value="'+data[i].id+'" id="c'+data[i].id+'" /><label style="width:250px"  for="c'+data[i].id+'">'+data[i].name+"("+data[i].id+")"+'</label>';
    			if(i%4==3){
	    			code+="<br/>";
	    		}
	    	}
	    	html.append(code);
	    }});
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/app/list">配置文件列表</a></li>
		<li class="active"><a>配置添加</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="cfg" action="${ctx }/app/add"  method="post" class="form-horizontal">
		<input id="jsonStr" name="data" type="hidden" value=""/>
		<div class="control-group">
			<label class="control-label">游戏:</label>
			<div class="controls">
				 <form:select id="ckAppId" path="ckAppId" class="input-medium" >
				 	<form:option value="" label="(不选)"/>
					<form:options items="${fns:getAPPCkListByChild()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
				</form:select>  
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">子游戏id:</label>
			<div class="controls">
				 <select id="childCkAppId" name="childCkAppId" class="input-medium">
					<option value="#">全部</option>
				</select>  
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">类型:</label>
			<div class="controls">
				<%-- <input id="rqType"  type="rqType" readonly="readonly" class="required" value="${fns:getDictLabel(cfg.rqType,'cfgtype','') }"/> --%>
				<form:select path="rqType" class="input-medium" itemValue="${rqType }" >
				 	<form:option value="" label="(不选)"/> 
					<form:options items="${fns:getDictList('cfgtype')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">渠道:</label>
			<div class="controls" >
				<input id="key" type="text" /><input type="button" onclick="getChannel()" value="过滤" /><br/>
				<input name="channel" id="cC" type="checkbox"  value="#" /><label style="width:96px" for="cC">#</label>
				<input id="cAll" type="checkbox" /><label  style="width:96px" for="cAll">全选</label>
				<input id="cChange" type="checkbox" /><label  style="width:96px" for="cChange">反选</label><br/>
				<div id="ckChannelId">
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
			<div class="controls"  >
				<input name="pro" id="pP" type="checkbox" value="#" /><label style="width:61px" for="pP">#</label>
				<input id="pAll" type="checkbox" /><label  style="width:61px" for="pAll">全选</label>
				<input id="pChange" type="checkbox" /><label  style="width:61px" for="pChange">反选</label><br/>
				<div id="provice">
				</div>
				<%-- <input id="province" name="province" type="province" class="required" value="${cfg.province }"/> --%>
				<%-- <textarea id="provinces" name="province" type="province"  class="required" cols="8" rows="3" value="${cfg.ckChannelId }"></textarea> --%>
			</div>
		</div>
		<div id="data">
		
		</div>
		<%-- <c:if test="${map ne null }">
			<c:forEach items="${map }" var="data">
			<div class="control-group" >
				<div id="json">
					<label class="control-label">${data.label }(${data.value }):</label>
					<div class="controls">
						<c:if test="${data.classType == 1 || data.classType == 5}">
							<c:set var="classType" value="${classType}-${data.value },${data.classType}"></c:set>
							<input id="${data.value }" name="${data.value }" type="text"  />
						</c:if>
						<c:if test="${data.classType == 3 || data.classType == 4 }">
							<c:set var="classType" value="${classType}-${data.value },${data.classType}"></c:set>
							<textarea id="${data.value }"  style="width:500px" rows="10" ></textarea>
						</c:if>
						<c:if test="${data.classType == 2 }">
							<select name="${data.value }" id="${data.value }">
								<option value="true">true</option>
								<option value="false">false</option>
							</select>
							<c:set var="classType" value="${classType}-${data.value },${data.classType}"></c:set>
						</c:if>
						<font color="red">*${data.description }</font>
					</div>
				</div>
			</div>
			</c:forEach>
		</c:if> --%>
		<input type="hidden" name="classType" id="classType" value=""/>
	</form:form>
	<div class="form-actions">
		<button id="btn" class="btn btn-primary" value="保 存" >保 存</button>
	</div>
</body>
</html>