<%@ page
	import="org.slf4j.Logger,org.slf4j.LoggerFactory,java.lang.management.ManagementFactory,java.lang.management.ThreadMXBean"%>
<%@ page session="false"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<head>
<title>Cluster App Console</title>
</head>
<body>
	Server Info:
	<%
	out.println(request.getLocalAddr() + " : " + request.getLocalPort() + "<br/>");

	out.print("<br/><b>监控信息</b><br/>");
%>
	<%="最大内存: "+(Runtime.getRuntime().maxMemory()/1024/1024)
		+"m。 已分配内存: "+(Runtime.getRuntime().totalMemory()/1024/1024)
		+"m。<br/> 已分配内存中的剩余空间: "+(Runtime.getRuntime().freeMemory()/1024/1024)
		+"m。 最大可用内存: "+((Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()
				+Runtime.getRuntime().freeMemory())/1024/1024)+"m" %>
	<%
	ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
	out.println("<br/><b>线程信息</b>");
	out.println("<br/>线程数:"+threadMXBean.getThreadCount());
	out.println("<br/>死锁线程数:"+threadMXBean.findDeadlockedThreads());
for(long id :threadMXBean.getAllThreadIds()){
	out.println("<br/>线程名称:"+threadMXBean.getThreadInfo(id).getThreadName());
	out.println("<br/>线程BlockedTime:"+threadMXBean.getThreadInfo(id).getBlockedTime());
	
}
	%>
</body>
</html>