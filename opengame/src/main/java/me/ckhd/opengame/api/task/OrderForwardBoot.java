/*
 * www.ckhd.me
 */
package me.ckhd.opengame.api.task;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import me.ckhd.opengame.api.entity.AppOrderForward;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 开启订单转发
 * 
 * @author qibiao
 */
public class OrderForwardBoot implements Runnable {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	// 是否已启动标识
	private final AtomicBoolean start = new AtomicBoolean(false);

	// 待处理任务队列
	private final BlockingQueue<AppOrderForward> queue = new LinkedBlockingQueue<AppOrderForward>();

	// 任务线程池
	private final ExecutorService taskService = Executors.newFixedThreadPool(16);

	private boolean shutdown = false;
	
	private OrderForwardBoot() {

	}

	// 单例
	private static class OrderForwardBootHolder {
		private static final OrderForwardBoot INSTANCE = new OrderForwardBoot();
	}

	public static OrderForwardBoot getInstance() {
		return OrderForwardBootHolder.INSTANCE;
	}

	/**
	 * 任务放入队列
	 * 
	 * @param obj
	 */
	public void add(AppOrderForward appOrderForward) {
		this.queue.add(appOrderForward);
	}
	
	public void add(List<AppOrderForward> appOrderForwards) {
		this.queue.addAll(appOrderForwards);
	}

	/**
	 * 开启任务
	 */
	public void startTask() {
		logger.info("OrderForwardBoot.startTask()");
		if (!start.get()) {
			start.set(true);
			// 总控会永久占用一个线程
			taskService.execute(this);
			SendOrderScheduleBoot.getInstance().service();
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
				AppOrderForward appOrderForward = queue.poll(5,TimeUnit.SECONDS);
				if(appOrderForward == null){
					continue;
				}
				logger.debug("OrderForwardBoot start task");
				// 执行任务
				taskService.execute(new SendOrder(appOrderForward));
			} catch (InterruptedException e) {
				logger.error("OrderForwardBoot.run() InterruptedException", e);
			} catch (Throwable t) {
				logger.error("OrderForwardBoot.run() Throwable", t);
			}
		}
	}
	
	public void stopTask(){
		shutdown = true;
		taskService.shutdown();
		SendOrderScheduleBoot.getInstance().stopSchedule();
	}
}