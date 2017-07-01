<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>支付通道切换设置管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		var result = "${isAdd}";
		if (result == "true") {
			var i = 0;
			$("#dataTbody").children("tr").eq(0).children("td").each(function() {
				$(this).css("background-color", "#FFFFBB");
				i++;
			});
		}
		chanageCarrier("");
	});

	function chanageCarrier(_carrier) {
		carrier = _carrier;
		if(carrier=="" || carrier==null){
			carrier = $("#carriers option:selected").val();
		}
		
		$.ajax({
			type : 'POST',
			url : "${ctx}/app/payrules/getProvinceConfig",
			data : {
				payRulesId : $("#pr_id").val(),
				carrier : carrier,
			},
			error : function(data) {
			},
			success : function(data) {
				$("#provinceChange").empty();
				$("[name=province_val]").each(function(){
					var id = $(this).val();
					var val = $("#lb"+id).val();
					if(data=="" || data.indexOf(id)==-1){
						$("#provinceChange").append(
							"<input type='checkbox' name='ckb_input_province' id='"+id+"' value='"+val+"'/><label>"+val+"</label>"	
						);
					}
				});
				if(_carrier!=""){
					$("#carriers option").each(function(){
						if($(this).val()==_carrier){
							$(this).attr("selected","true");
							$(".select2-chosen").html($(this).html());
							return;
						}
					});
				}
			}
		});
	}

	//保存按钮
	function savePayRule() {
		var configid = "";
		var provinceStr = "";
		var provinces = "";
		var provinceId = "";
		var carrierTemp = $("#carriers  option:selected").val();
		/* $("#provinceTable tr").each(
				function() {
					var id = $(this).attr("id");
					var carrier =$("#carriersvalue"+id).val();
					if (carrierTemp == carrier) {
						$("#id").val($(this).attr("id"));
						var provinceids = $(this).children("td").eq(1)
								.children("[name=provinceids]").val();
						provinces += provinceids;
					}
				}); */

		$("[name = ckb_input_province]:checkbox").each(function() {
			if ($(this).is(":checked")) {
				if (provinces != "") {
					provinces += ",";
				}
				provinces += $(this).attr("id");
			}
		});

		if (provinces == "") {
			alert("请选择省份!");
			return;
		}
		$("#provinceIds").val(provinces);
		$("#inputForm").attr("action", "${ctx}/app/payrules/saveConfig");
		$("#inputForm").submit();
	}

	function repageForaJax(data, message,id) {
		var carriersavalue=$("#carriersvalue"+id).val();
		var newChange = $(data).find("#provinceChange").html();
		var newTBody = $(data).find("#dataTbody").html();
		$("#dataTbody").html(newTBody);
    	$("#provinceMessage").html(message);
    	$("#provinceMessage").parent().css("visibility","visible");
    	$("#provinceMessage").parent().focus();
    	$("#provinceMessage").parent().blur(function(){
    		$(this).css("visibility","hidden");
		});
		chanageCarrier(carriersavalue);
	}

	//更新数据
	function update(obj, type) {
		var objId = obj.id;
		switch (type) {
		case 0:
		case 4:
			updateProvinces(objId, type)
			break;
		}
	}

	function updateProvinces(objId, type) {
		var index = $("#" + objId).parent().children(":checkbox").length;
		var provinces = "";
		var id = $("#" + objId).parent().parent().attr("id");
		
		if (index == 1 && type==0) {
			delProvince(id)
		} else {
			if(type==4){
				var nextTd = $("#" + objId).parent().next().children().each(
					function() {
						if ($(this).is(":checked")
								&& $(this).val() != $("#" + objId).val()) {
							if (provinces != "") {
								provinces += ",";
							}
							provinces += $(this).val();
						}
					}
				);
			}
			
			$("#" + objId).parent().children(":checkbox").each(
				function() {
					if ($(this).is(":checked")
							&& $(this).val() != $("#" + objId).val()) {
						if (provinces != "") {
							provinces += ",";
						}
						provinces += $(this).val();
					}
				});
			updateConfig(id, provinces, type);
		}
	}
	//更新money
	function updateOther(id) {
		var money = $("#money" + id).val();
		var totalmoney = $("#total" + id).val();
		$.ajax({
			type : 'POST',
			url : "${ctx}/app/payrules/updateOther",
			data : {
				id : id,
				pr_id : $("#pr_id").val(),
				money : money,
				totalmoney :totalmoney
			},
			dataType : "html",
			success : function(data) {
				var message = "省份配置修改成功!"
				repageForaJax(data, message,id);
			},
			error : function() {
				var message = "省份配置修改失败!";
				repageForaJax(data, message,id);
			}
		});
	}

	function updateConfig(id, data, type) {
		$.ajax({
			type : 'POST',
			url : "${ctx}/app/payrules/updateConfig",
			data : {
				id : id,
				pr_id : $("#pr_id").val(),
				data : data,
				type : type
			},
			dataType : "html",
			success : function(data) {
				var message = "修改省份成功!";
				repageForaJax(data, message,id);
			},
			error : function() {
				var message = "修改省份失败!";
				repageForaJax(data, message,id);
			}
		});
	}

	function delProvince(id) {
		$.ajax({
			type : 'POST',
			url : "${ctx}/app/payrules/delConfig",
			data : {
				id : id,
				pr_id : $("#pr_id").val()
			},
			dataType : "html",
			success : function(data) {
				var message = "省份配置删除成功!";
				repageForaJax(data, message,id);
			},
			error : function() {
				var message = "省份配置删除失败!";
				repageForaJax(data, message,id);
			}
		});
	}

	//保存网络支付
	function saveIntenerPay() {
		$.ajax({
			type : 'POST',
			url : "${ctx}/app/payrules/saveIntenerPay",
			data : {
				pr_id : $("#pr_id").val(),
				internetPay : $("#internetPay").val()
			},
			dataType : "html",
			success : function(data) {
				$("#message").html("保存网络支付成功!");
				$("#message").focus();
				$("#message").blur(function() {
					$(this).html("");
				});
			},
			error : function() {
				$("#message").html("保存网络支付失败!");
				$("#message").focus();
				$("#message").blur(function() {
					$(this).html("");
				});
			}
		});
	}

	function checkAll(obj) {
		var flag = false;
		if ($(obj).attr("checked")) {
			flag = true;
		} else {
			flag = false;
		}
		$(obj).parent().parent().children(".controls ").children(":checkbox")
				.attr("checked", flag);
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><H4>支付通道切换省份设置</H4></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="payRules" method="post"
		class="form-horizontal">
		<input type="hidden" id="pr_id" name="pr_id" value="${payRules.id }">
		<input type="hidden" id="id" name="id">
		<input type="hidden" id="startTime" name="startTime"
			value="${payRules.startTime}">
		<input type="hidden" id="endTime" name="endTime"
			value="${payRules.endTime }">
		<input type="hidden" id="provinceIds" name="provinceIds" value="">
		<div class="control-group" style="margin-top: -25px">
			<label class="control-label">时间段:</label>
			<div class="controls">
				<c:choose>
					<c:when test="${payRules.isLenght}">
						<span class="help-inline">${payRules.startTime}至今</span>
					</c:when>
					<c:otherwise>
						<span class="help-inline">${payRules.startTime} 至
							${payRules.endTime }</span>
					</c:otherwise>
				</c:choose>

			</div>
		</div>
		<div class="control-group">
			<label class="control-label">网络支付方式:</label>
			<div class="controls">
				<form:input path="internetPay" id="internetPay" htmlEscape="false"
					maxlength="50" class="input-medium" />
				<span tabindex="0" id="message" class="help-inline"
					style="color: red; border: 2px;"></span>
			</div>
		</div>
		<ul class="nav nav-tabs">
			<li class="active"
				style="margin-left: 80px; margin-bottom: 10px; margin-top: -5px">
				<shiro:hasPermission name="app:payrules:edit">
					<input id="btnSaveIn" class="btn btn-primary" type="button"
						onclick="saveIntenerPay()" value="保存网络支付方式" />
				</shiro:hasPermission>
			</li>
		</ul>
		<br />
		<ul class="nav nav-tabs">
			<li class="active" style="margin-top: -25px"><H5>省份选择</H5></li>
		</ul>
		<br />
		<div class="control-group" style="margin-top: -25px">
			<label class="control-label">支付通道标识:</label>
			<div class="controls">
				<form:select path="carriers" class="input-medium" onChange="chanageCarrier('')">
					<form:options items="${fns:getDictList('sdkType')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
		</div>
		<br />
		<div class="control-group" style="margin-top: -25px">

			<label class="control-label">省份选择<input type="checkbox"
				onclick="checkAll(this)" id="provinceAll">:
			</label>
			<c:forEach items="${fns:getDictProvince('province_iccid')}" var="province" varStatus="s">
					<input type="hidden" name="province_val" value="${province.value}">
					<input type="hidden" id="lb${province.value}" value="${province.label}">
			</c:forEach>
			<div class="controls" id="provinceChange">
				
			</div>
		</div>
		<ul class="nav nav-tabs">
			<li class="active" style="margin-left: 80px"><shiro:hasPermission
					name="app:payrules:edit">
					<input id="btnSave" class="btn btn-primary" type="button"
						onclick="savePayRule()" value="保存省份选择" />
				</shiro:hasPermission></li>
		</ul>
	</form:form>

	<form:form action="" id="dataForm" method="post"
		class="form-horizontal">
		<div tabindex="0"
			style="visibility: hidden; width: 100%; text-align: center; background-color: #d5ecbf">
			<font size="5px"
				style="font-weight: bold; text-align: center; vertical-align: middle;"
				id="provinceMessage">提示</font>
		</div>
		<input type="hidden" id="pr_id" value="${payRules.id}">
		<table id="provinceTable"
			class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>支付类型</th>
					<th>未选省份</th>
					<th>省份</th>
					<th>单次支付金额(单位:分)</th>
					<th>每日最高限额(单位:分)</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody id="dataTbody">
				<c:forEach items="${payRules.configs}" var="payRulesConfig">
					<tr id="${payRulesConfig.id}">
					<input type="hidden" name="carriersvalue" id="carriersvalue${payRulesConfig.id}" value="${payRulesConfig.carriers}"/>
						<td id="carriers${payRulesConfig.id}">
							<label>${payRulesConfig.carriersName}</label>
						</td>
						<td style="width: 40%">
							<c:forEach items="${fns:getDictList('province_iccid')}"
								var="province" varStatus="s">
								<c:if test="${fn:startsWith(payRulesConfig.carriers, 'CMCC')}">
									<c:if test="${payRules.cmcc_provinceIds.indexOf(province.value)<0}">
										<input type="checkbox"
											name="ckb_not_province" id="ckb_not_${payRulesConfig.id}${province.value}"
											value="${province.value}">
										<label>${province.label}</label>
									</c:if>
								</c:if>
								<c:if test="${fn:startsWith(payRulesConfig.carriers, 'CUCC')}">
									<c:if test="${payRules.cucc_provinceIds.indexOf(province.value)<0}">
										<input type="checkbox"
											name="ckb_not_province" id="ckb_not_${payRulesConfig.id}${province.value}"
											value="${province.value}">
										<label>${province.label}</label>
									</c:if>
								</c:if>
								<c:if test="${fn:startsWith(payRulesConfig.carriers, 'CTCC')}">
									<c:if test="${payRules.ctcc_provinceIds.indexOf(province.value)<0}">
										<input type="checkbox"
											name="ckb_not_province" id="ckb_not_${payRulesConfig.id}${province.value}"
											value="${province.value}">
										<label>${province.label}</label>
									</c:if>
								</c:if>
							</c:forEach>
							<br />
							<input class="btn btn-primary" value="保存省份" type="button" id="btn_not_${payRulesConfig.id}" onclick="update(this, 4)">
						</td>
						<td style="width: 30%">
						<input type="hidden" name="provinceids" value="${payRulesConfig.provinceids}"/>
							<c:forEach items="${fns:getDictList('province_iccid')}"
								var="province" varStatus="s">
								<c:if test="${payRulesConfig.provinceids.indexOf(province.value)>-1}">
									<input type="checkbox" onClick="update(this,0)"
										name="ckb_province" id="ckb${payRulesConfig.id}${province.value}" checked=checked
										value="${province.value}">
									<label>${province.label}</label>
								</c:if>
							</c:forEach></td>
						<td><input id="money${payRulesConfig.id}"
							onblur="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))"
							value="${((payRulesConfig.money==null || payRulesConfig.money=='')?'0': payRulesConfig.money)}"
							maxlength="50" class="input-medium" /></td>
						<td><input id="total${payRulesConfig.id}"
							onblur="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))"
							value="${((payRulesConfig.totalmoney==null || payRulesConfig.totalmoney=='')?'0': payRulesConfig.totalmoney)}"
							maxlength="50" class="input-medium" /></td>
						<shiro:hasPermission name="app:payrules:edit">
							<td><a href="javascript:updateOther('${payRulesConfig.id}')">保存</a>
								<a href="javascript:delProvince('${payRulesConfig.id}')"
								onclick="return confirmx('确认要删除该支付通道切换配置吗？', this.href)">删除</a></td>
						</shiro:hasPermission>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</form:form>
</body>
</html>