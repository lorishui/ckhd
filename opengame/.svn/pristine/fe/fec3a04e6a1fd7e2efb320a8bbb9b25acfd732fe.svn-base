/*
 * www.ckhd.me
 */
package me.ckhd.opengame.online.sendOrder.task;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import me.ckhd.opengame.online.entity.OnlinePay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 开启订单转发
 * 
 * @author qibiao
 */
public class OrderSenderBoot implements Runnable {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	// 是否已启动标识
	private final AtomicBoolean start = new AtomicBoolean(false);

	// 待处理任务队列
	private final BlockingQueue<OnlinePay> queue = new LinkedBlockingQueue<OnlinePay>();

	// 任务线程池
	private final ExecutorService taskService = Executors.newFixedThreadPool(16);

	private boolean shutdown = false;
	
	private OrderSenderBoot() {

	}

	// 单例
	private static class OrderSenderBootHolder {
		private static final OrderSenderBoot INSTANCE = new OrderSenderBoot();
	}

	public static OrderSenderBoot getInstance() {
		return OrderSenderBootHolder.INSTANCE;
	}

	/**
	 * 任务放入队列
	 * 
	 * @param obj
	 */
	public void add(OnlinePay onlinePay) {
		this.queue.add(onlinePay);
	}
	
	public void add(List<OnlinePay> onlinePays) {
		this.queue.addAll(onlinePays);
	}

	/**
	 * 开启任务
	 */
	public void startTask() {
		logger.info("OrderSenderBoot.startTask()");
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
				OnlinePay onlinePay = queue.poll(5,TimeUnit.SECONDS);
				if(onlinePay == null){
					continue;
				}
				logger.debug("OrderSenderBoot start task");
				// 执行任务
				taskService.execute(new SendOrder(onlinePay));
			} catch (InterruptedException e) {
				logger.error("OrderSenderBoot.run() InterruptedException", e);
			} catch (Throwable t) {
				logger.error("OrderSenderBoot.run() Throwable", t);
			}
		}
	}
	
	public void stopTask(){
		shutdown = true;
		taskService.shutdown();
		SendOrderScheduleBoot.getInstance().stopSchedule();
	}
}