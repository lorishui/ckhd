package me.ckhd.opengame.api.task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.ckhd.opengame.api.entity.FlowOrderForward;

public class FlowForwardBoot implements Runnable{

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	// 是否已启动标识
	private final AtomicBoolean start = new AtomicBoolean(false);
	
	private boolean shutdown = false;
	
	// 待处理任务队列
	private final BlockingQueue<FlowOrderForward> queue = new LinkedBlockingQueue<FlowOrderForward>();
	
	// 任务线程池
	private final ExecutorService taskService = Executors.newFixedThreadPool(8);
	
	//
	
	public void add(FlowOrderForward flowOrderForward) {
		this.queue.add(flowOrderForward);
	}
	
	/**
	 * 开启任务
	 */
	public void startTask() {
		logger.info("FlowForwardBoot.startTask()");
		if (!start.get()) {
			start.set(true);
			// 总控会永久占用一个线程
			taskService.execute(this);
		}
	}
	
	@Override
	public void run() {
		for (;;) {
			if(shutdown){
				break;
			}
			try {
				// poll可以停止应用 2015-10-10
				FlowOrderForward flowOrderForward = queue.poll(5,TimeUnit.SECONDS);
				if(flowOrderForward == null){
					continue;
				}
				logger.debug("FlowForwardBoot start task");
				// 执行任务
				taskService.execute(new FlowSendOrder(flowOrderForward));
			} catch (InterruptedException e) {
				logger.error("FlowForwardBoot.run() InterruptedException", e);
			} catch (Throwable t) {
				logger.error("FlowForwardBoot.run() Throwable", t);
			}
		}
	}
	
	public void stopTask(){
		shutdown = true;
		taskService.shutdown();
	}
	
	// 单例
	private static class FlowForwardBootHolder {
		private static final FlowForwardBoot INSTANCE = new FlowForwardBoot();
	}

	public static FlowForwardBoot getInstance() {
		return FlowForwardBootHolder.INSTANCE;
	}
}
