package com.chkd.count.common.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.mysql.jdbc.StringUtils;

/**
 * 队列操作
 * @author ASUS
 *
 */
public class QueueUtils {
	
	public static BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	
	/**
	 * 获取队列内容并删除
	 * @return
	 */
	public static String poll(){
		String data;
		try {
			data = queue.poll(1, TimeUnit.MINUTES);
			return data;
		} catch (InterruptedException e) {
			LogUtils.log.info("Queue poll error:",e);
		}
		return null;
	}
	
	/**
	 * 加入队列
	 * @param data
	 * @return
	 */
	public static boolean offer(String data){
		LogUtils.log.info("统计任务的数据:"+data);
		if(!StringUtils.isNullOrEmpty(data)){
			LogUtils.log.info("Queue offer num:"+queue.size());
			return queue.offer(data);
		}
		return false;
	}
}
