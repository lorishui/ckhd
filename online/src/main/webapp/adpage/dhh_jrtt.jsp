<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0" />
<title>无敌大航海</title>
<style type="text/css">
*{
	margin:0px;
	padding:0px;
}
.main{
	margin:0 auto;
	max-width:720px;
	min-width:320px;
}
.btn-top{cursor: pointer;position:absolute;background:none;display:block;left: 0;right: 0;top: 59.8rem;margin: 0 auto;width: 16.5rem;height: 4.5rem;}
.btn-bottom{cursor: pointer;position:absolute;background:none;display:block;left: 0;right: 0;top: 176rem;margin: 0 auto;width: 28rem;height: 4.5rem;}
</style>
<%
String basePath = request.getScheme()+"://"+request.getServerName();
if(request.getServerPort() != 80){
	basePath +=":"+request.getServerPort();
}
basePath += "/" + request.getContextPath();
String item = request.getParameter("item");
if(item == null){
	item = "1";
}
String fileName = "dahanghai_jrtt1_cksdk_ckpay_201611111013.apk";
if("2".equals(item)){
	fileName = "dahanghai_jrtt2_cksdk_ckpay_201611111013.apk";
}else if("3".equals(item)){
	fileName = "dahanghai_jrtt3_cksdk_ckpay_201611111014.apk";
}else if("4".equals(item)){
	fileName = "dahanghai_jrtt4_cksdk_ckpay_201611111015.apk";
}else if("5".equals(item)){
	fileName = "dahanghai_jrtt5_cksdk_ckpay_201611111016.apk";
}
%>
<script type="text/javascript" src="http://cdn.mobee.im/page/js/jquery.min.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		htmlobj=$.ajax({url:"<%=basePath%>" + "/webaccess?ckappid=1064&channelid=170&source=1&item=<%=item%>",async:false});
	});
	function dl(){
		htmlobj=$.ajax({url:"<%=basePath%>" + "/webaccess?ckappid=1064&channelid=170&source=2&item=<%=item%>",async:false});
		window.location.href='http://cdn.mobee.im/app/1029/<%=fileName%>';
	}
</script>
</head>

<body>

<div class="main"><img src="http://cdn.mobee.im/page/img/dhh.png" border="0" width="100%" />
<button ></button>
<button  class="btn-top" id="download1" alt="无敌大航海" onclick="dl()"></button>
<button  class="btn-bottom" id="download2" alt="无敌大航海" onclick="dl()"></button>
</div>
</body>
</html>