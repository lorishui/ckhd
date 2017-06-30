<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>支付通道配置管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			changeAPPID(true);
			$("#ckappId").focus();
			$("#inputForm").validate({
				rules: {},
				messages: {},
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
		
		
		function save(){
			var mmAppId = $("#mmAppId").val();
		//	var andgameAppId = $("#andgameAppId").val();
			$("#appIds").val(mmAppId);
			return true;
		}
		
		function showSelectAppIdsDiv(carriers){
			console.info(carriers.value)
		}
		
		function changeAPPID(pageLoad){
			$.ajax({
			    url:'${ctx}/app/appcarriers/queryAppCarriers',
			    type:'get',
			    data:'ckAppId=' + $("#ckappId").val() + '&carriersType=MM',
			    error:function(){
			    },
			    success:function(data){
			    	$("#mmAppId").empty();
			    	$("#mmAppId").append("<option value='' selected>(全部)</option>");
			       for(var item in data){
			       	$("#mmAppId").append("<option value='"+data[item].appId +"'>"+data[item].appName + "(" + data[item].appId +")</option>");
			       }
			       if(pageLoad){
			    	   $("#mmAppId").val("${paymentWay.mmAppId}");
					   $("#s2id_mmAppId .select2-chosen").html($("#mmAppId option:selected").text());
			       }else{
			    	   $("#mmAppId").val("");
				       $("#s2id_mmAppId .select2-chosen").html("(全部)");
			       }
			    }
			});
			
			
			
/*			$.ajax({
			    url:'${ctx}/app/appcarriers/queryAppCarriers',
			    type:'get',
			    data:'ckAppId=' + $("#ckappId").val() + '&carriersType=ANDGAME',
			    error:function(){
			    },
			    success:function(data){
			    	$("#andgameAppId").empty();
			    	$("#andgameAppId").append("<option value='' selected>(全部)</option>");
			       for(var item in data){
			       	$("#andgameAppId").append("<option value='"+data[item].appId +"'>"+data[item].appName + "(" + data[item].appId +")</option>");
			       }
			       if(pageLoad){
			    	   $("#andgameAppId").val("${paymentWay.andgameAppId}");
					   $("#s2id_andgameAppId .select2-chosen").html($("#andgameAppId option:selected").text());
			       }else{
			    	   $("#andgameAppId").val("");
				       $("#s2id_andgameAppId .select2-chosen").html("(全部)");
			       }
			    }
			});
			 */
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/app/paymentway/list">支付通道配置列表</a></li>
		<li class="active"><a href="${ctx}/app/paymentway/form?id=${paymentWay.id}">支付通道配置<shiro:hasPermission name="app:paymentway:edit">${not empty paymentWay.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="app:paymentway:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="paymentWay" action="${ctx}/app/paymentway/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden id="appIds" path="appIds"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">创酷app:</label>
			<div class="controls">
				<form:select path="ckappId"  disabled="${not empty paymentWay.id?true:false}" id="ckappId"  onChange="changeAPPID()" class="input-large">
					<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
				</form:select>  
			</div>
		</div>	
		
		<div class="control-group">
			<label class="control-label">运营商:</label>
			<div class="controls">
				<form:select  path="carriers"  disabled="${not empty paymentWay.id?true:false}"  onchange="showSelectAppIdsDiv(this)"  class="input-large">
					<form:option value="CMCC" label="移动"/>
					<!--  
					<form:option value="CUCC" label="联通"/>
					<form:option value="CTCC" label="电信"/>
					-->
				</form:select>  
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<div id="CMCC" class="control-group">
			<label class="control-label">MM:</label>
			<div class="controls">
				<form:select id="mmAppId" path="mmAppId" disabled="${not empty paymentWay.id?true:false}"  class="required input-xlarge">
				</form:select> <span class="help-inline"><font color="red">*</font> </span>
			</div>	 
		</div>
		
		
		<div class="control-group">
			<label class="control-label">版本:</label>
			<div class="controls">
				<form:input path="version" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">渠道ID:</label>
			<div class="controls">
				<form:select id="channelId" path="channelId" class="input-medium">
					<form:option value="" label="(全部)"/>
					<form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
		   		</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">省份代码:</label>
			<div class="controls">
				<form:select id="provinceCode" path="provinceCode" class="input-medium">
					<form:option value="" label="(全部)"/>
					<form:options items="${fns:getDictList('province_iccid')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		   		</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">支付通道标识:</label>
			<div class="controls">
				<form:select path="payWaySign"    class="input-medium">
					<form:options items="${fns:getDictList('sdkType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">是否启用:</label>
			<div class="controls">
				<form:select path="delFlag">
					<form:option value="0" label="是"/>
					<form:option value="1" label="否"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> “是”代表此配置生效，“否”则表示不生效</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="1" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group" style="margin-left: 200px">
			<shiro:hasPermission name="app:paymentway:edit"><input id="btnSubmit" class="btn btn-primary" onclick="return save();" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>