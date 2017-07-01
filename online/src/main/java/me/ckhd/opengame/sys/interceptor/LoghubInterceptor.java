/**
 * Copyright &copy; 2015-2018 <a href="http://www.ckhd.me/">创酷互动</a> All rights reserved.
 */
package me.ckhd.opengame.sys.interceptor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.common.service.BaseService;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.util.LoghubUtils;

import org.slf4j.Logger;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

/**
 * 用于记录日志到阿里的日志服务
 * @author wizard
 * @since 2017/06/30
 */
public class LoghubInterceptor extends BaseService implements HandlerInterceptor {

	private static final ThreadLocal<Long> begTimeMillis = new NamedThreadLocal<Long>(LoghubInterceptor.class.getName());
	private static final ThreadLocal<Long> endTimeMillis = new NamedThreadLocal<Long>(LoghubInterceptor.class.getName());
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (getLogger().isDebugEnabled()){
	        begTimeMillis.set(System.currentTimeMillis());	
		}
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (getLogger().isDebugEnabled()){
			endTimeMillis.set(System.currentTimeMillis());
		}
	}

	public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,  final Object handler, final Exception ex) throws Exception {
		if (getLogger().isDebugEnabled()){
			
			final Map<String, Object> logs = new HashMap<String, Object>();
			logs.put("remoteAddress", StringUtils.getRemoteAddr(request));
			//logs.put("userAgent", request.getHeader("user-agent"));
			logs.put("uri", request.getRequestURI());
			logs.put("params", request.getParameterMap());
			logs.put("method", request.getMethod());
			logs.put("handler", getHandler(handler));
			logs.put("postTimeMillis", endTimeMillis.get() - begTimeMillis.get());
			logs.put("totalTimeMillis", System.currentTimeMillis() - begTimeMillis.get());
			//logs.put("maxMemory", Runtime.getRuntime().maxMemory()/1024/1024);
			//logs.put("totalMemory", Runtime.getRuntime().totalMemory()/1024/1024);
			//logs.put("freeMemory", Runtime.getRuntime().freeMemory()/1024/1024);
			
			if( ex != null ) {
				logs.put("exception", getStackTrace(ex));
			}
			
			new Thread(new Runnable(){
				public void run() {
					getLogger().debug(new Gson().toJson(logs));
				}
			}).start();
			
			begTimeMillis.remove();
			endTimeMillis.remove();
		}
	}
	
	private Logger getLogger(){ return LoghubUtils.getHttpRequestLogger(); }
	
	private String getHandler(Object handler) {
		if( handler != null ) {
			if( handler.getClass().isAssignableFrom(HandlerMethod.class) ) {
				HandlerMethod hm = (HandlerMethod) handler;
				return hm.getMethod().getDeclaringClass().getName() + "." + hm.getMethod().getName();
			}
			return handler.toString();
		}
		return null;
	}

	private String getStackTrace(Throwable e) {
		StringWriter stringWriter = new StringWriter();
		if( e != null ) {
			e.printStackTrace(new PrintWriter(stringWriter));
		}
		return stringWriter.toString();
	}
}