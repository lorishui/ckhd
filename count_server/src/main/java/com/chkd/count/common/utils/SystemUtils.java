package com.chkd.count.common.utils;

public class SystemUtils {
	
	/**
	 * 获取Key加载信息
	 */
	public static boolean printKeyLoadMessage(){
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n======================================================================\r\n");
		sb.append("\r\n    欢迎使用 统计系统      \r\n ");
		sb.append("\r\n======================================================================\r\n");
		LogUtils.log.info(sb.toString());
		return true;
	}
	
}
