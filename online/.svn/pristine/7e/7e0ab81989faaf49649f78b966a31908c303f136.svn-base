<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>游戏礼包管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var goodsData;
		var index=0;
		$(document).ready(function() {
			$("#btnSubmit").click(function(){
				var form =  $("#inputForm");
				var idData="";
				var numData="";
				for(var j=0;j<=index;j++){
					if($("#goodsId"+j).val() != undefined && $("#goodsId"+j).val() != '') idData+=$("#goodsId"+j).val()+",";
					if($("#goodsNumers"+j).val() != undefined && $("#goodsNumers"+j).val() != '') numData+=$("#goodsNumers"+j).val()+",";
				}
				$("#goodsId").attr("value",idData);
				$("#num").attr("value",numData);
				var value = $("#ckAppId").val();
				form.submit();
			});
			getGoods();
			$("#ckAppId").change(function(){			
				getGoods();
			});
			var data='${data}';
			if(data!=''){
				index='${fn:length(data)}';
			}
		});
		
		function addNode(n){
			var sub = $("#sub");
			var html = "<div class='control-group' id='"+index+"' >"+
				"<label class='control-label'>游戏物品id:</label>"+
				"<div class='controls'><select id='goodsId"+index+"'  class='input-medium'>";
			html+="<option value='0'>请选择</option>";
			for(var i=0;i<goodsData.length;i++){
				html+="<option value='"+goodsData[i].id+"'>"+goodsData[i].name+"</option>";
			}
			html+="</select> "+
				"</div><label class='control-label'>游戏物品数量:</label>"+
				"<div class='controls'>"+
				"<input id='goodsNumers"+index+"' type='text' />";
			if(n==1){
				html+=" <input id='del' name='"+index+"' type='button' onclick='addNode(0)' value='+'/></div></div>";	
			}else{
				html+=" <input id='del' name='"+index+"' type='button' onclick='delNode(this)' value='-'/></div></div>";
			}
			sub.append(html);
			index++;
		}
		
		function delNode(obj){
			var name = $(obj).attr("name");
			var clzz = "#"+name;
			$(clzz).remove();
		}
		
		function getGoods(){
			$.ajax({ url: "${ctx }/game/goods/getList?ckAppId="+$("#ckAppId").val(), success: function(data){
				goodsData = data;
				var id='${gift.id}';
				if(id == ''){
					for(var j=0;j<=index;j++){
						$("#"+j).remove();
					}
					addNode(1);	
				}
			}});
		}
		
		function addOld(){
			
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/game/gift/list">游戏礼包列表</a></li>
		<li class="active"><a href="${ctx}/game/gift/form?id=${gift.id}">游戏礼包<shiro:hasPermission name="game:gift:edit">${not empty gift.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="game:gift:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="gift" action="${ctx}/game/gift/save" method="post" class="form-horizontal">
		<input type="hidden" name="goodsId" id="goodsId"/>
		<input type="hidden" name="num" id="num"/>
		<c:if test="${gift.id ne null }">
			<input type="hidden" name="ckAppId" id="ckAppIdH" value="${gift.ckAppId }"/>
		</c:if>
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">APP:</label>
			<div class="controls">
				 <form:select id="ckAppId" path="ckAppId"  disabled="${not empty gift.id?true:false}"  class="input-medium">
					<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
				</form:select>  
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">礼包id:</label>
			<div class="controls">
				<input name="newId" htmlEscape="false" maxlength="100" class="required" value="${gift.id }"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">礼包名称:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div id="sub">
			<c:forEach var="data" items="${data }" varStatus="index">
			<div class="control-group" id="${index.index }">
				<label class="control-label">游戏物品id:</label>
				<div class="controls">
					<select id="goodsId${index.index }" style="width:100px" >
						<option value="0">请选择</option>
						<c:forEach var="goods" items="${goods}" >
							<c:if test="${data.goodsId eq goods.id }">
								<option value="${goods.id }" selected="selected">${goods.name }</option>
							</c:if>
							<c:if test="${data.goodsId ne goods.id }">
								<option value="${goods.id }" >${goods.name }</option>
							</c:if>
						</c:forEach> 
					</select>
				</div>
				<label class="control-label">游戏物品数量:</label>
				<div class="controls">
					<input id="goodsNumers${index.index }" type="text" value="${data.goodsNumber }"> 
					<c:if test="${index.index eq 0 }">
						<input id="del" name="${index.index }" type="button" onclick="addNode(0)" value="+">
					</c:if>
					<c:if test="${index.index ne 0 }">
						<input id="del" name="${index.index }" type="button" onclick="delNode(this)" value="-">
					</c:if>
				</div>
			</div>
		</c:forEach>
		<c:if test="${empty data}">
			<div class="control-group" id="0">
				<label class="control-label">游戏物品id:</label>
				<div class="controls">
					<select id="goodsId0" style="width:100px" >
						<option value="0">请选择</option>
						<c:forEach var="goods" items="${goods}" >
							<option value="${goods.id }" >${goods.name }</option>
						</c:forEach> 
					</select>
				</div>
				<label class="control-label">游戏物品数量:</label>
				<div class="controls">
					<input id="goodsNumers0" type="text" > 
				</div>
			</div>
		</c:if>
		</div>
		
		<div class="form-actions">
			<shiro:hasPermission name="game:gift:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>