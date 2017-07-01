package me.ckhd.opengame.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class LoghubUtils {

	private static Logger log4HttpRequest = LoggerFactory.getLogger(LoghubUtils.class.getName() + ".httpRequest");
	
	private static Logger log4BackgroundTasklogger = LoggerFactory.getLogger(LoghubUtils.class.getName() + ".backgroundTask");
	
	public static Logger getHttpRequestLogger() { return log4HttpRequest; }
	
	public static Logger getBackgroundTasklogger(){ return log4BackgroundTasklogger; }
	
	
	public static <T>T executeBackgroundTask(IExecute<T> exe) {
		long timeMillis = System.currentTimeMillis();
		List<String> message = new ArrayList<String>();
		try {
			return exe.execute(message);
		}
		finally {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("message", StringUtils.join(message, "\n"));
			map.put("totalTimeMillis", System.currentTimeMillis() - timeMillis);
			exe.log(new Gson().toJson(map));
		}
	}

	public interface IExecute<T> {
		public T execute(List<String> message);
		public void log(String message);
	}
}
