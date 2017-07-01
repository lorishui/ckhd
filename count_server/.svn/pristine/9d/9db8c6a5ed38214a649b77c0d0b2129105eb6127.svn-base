package com.chkd.count.count.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.chkd.count.common.utils.LogUtils;
import com.chkd.count.common.utils.QueueUtils;

/**
 * 轻量级任务处理
 * @author ASUS
 *
 */
public class LightWeightTaskApi {
	
	public static Lock lock = new ReentrantLock();
	
	public static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);
	
	private static LightWeightTaskApi lightWeightTaskApi = null;
	
	private LightWeightTaskApi(){
		
	}
	
	public static LightWeightTaskApi getInstance(){
		LogUtils.log.info("轻量级统计开始");
		if( lightWeightTaskApi == null ){
			lock.lock();
			if(lightWeightTaskApi==null){
				lightWeightTaskApi = new LightWeightTaskApi();
			}
			lock.unlock();
		}
		return lightWeightTaskApi;
	}
	
	class LightWeight implements Runnable{
		private String data;
	
		public String getData() {
			return data;
		}

		public void setData(String data) {
			this.data = data;
		}

		public void run() {
			CountApi.invoke(data);
		}
		
	}
	
	class test extends Thread{
		
	}
	/**
	 * 执行进程
	 * @author ASUS
	 *
	 */
	class Count implements Runnable{

		public void run() {
			try{
				LogUtils.log.info("开始执行统计监听任务");
				while(true){
					String data = null;
					while( (data = QueueUtils.poll()) != null ){
						LogUtils.log.info("队列任务内容:"+data);
						LightWeight light = new LightWeight();	
						light.setData(data);
						executor.execute(light);
					}
					
				}
			}catch(Throwable e){
				LogUtils.log.error("执行轻量级统计报错!",e);
			}
		}
		
	}
	
	/**
	 * 执行统计
	 */
	public void count() {
		LogUtils.log.info("开始执行队列监听");
		Count count = new Count();
		executor.execute(count);
	}
	
	public void shutdown(){
		executor.shutdown();
	}
	
}
