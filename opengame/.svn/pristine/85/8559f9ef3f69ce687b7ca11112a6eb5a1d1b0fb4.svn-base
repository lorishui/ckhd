<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default"/>
<title>Insert title here</title>
<script type="text/javascript">
	$(function(){
		$("#form").submit(function(){  
            $(this).ajaxSubmit({  
                type:"post",  //提交方式  
                dataType:"json", //数据类型  
                url:"${ctx }/app/appleUser/send", //请求url  
                success:function(data){ //提交成功的回调函数  
                    alert(data);  
                }  
            });  
            return false; //不刷新页面  
        }); 
	});
</script>
</head>
<body>
	<form:form id="form" action="${ctx }/app/appleUser/send" method="post" modelAttribute="apple">
		<table>
			<tr>
				<td>创酷appid：</td>
				<td>
					<form:select name="ckAppId" path="ckAppId" class="input-medium">
					<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
					</form:select> 
				</td>
			</tr>
			<tr>
				<td>推送的信息:</td>
				<td><textarea name="msg" ></textarea></td>
			</tr>
			<tr>
				<td>版本:</td>
				<td><input type="text" name="version" /></td>
			</tr>
			<tr>	
				<td><input type="submit" value="推送"/></td>
			</tr>
		</table>
	</form:form>
</body>
</html>