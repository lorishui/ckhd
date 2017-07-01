/*
 * www.ckhd.me
 */
package me.ckhd.opengame.countly.task;

import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import me.ckhd.opengame.andapi.entity.AndAPPOrder;
import me.ckhd.opengame.api.entity.AppOrderForward;
import me.ckhd.opengame.api.task.SendOrder;
import me.ckhd.opengame.common.config.Global;
import me.ckhd.opengame.common.persistence.DataEntity;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.countly.entity.OrderForwardCountly;
import me.ckhd.opengame.mmapi.entity.MmAppOrder;
import me.ckhd.opengame.online.util.OrderStatus;

import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 开启订单转发
 * 
 * @author qibiao
 */
public class OrderForwardCountlyBoot implements Runnable {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	// 是否已启动标识
	private final AtomicBoolean start = new AtomicBoolean(false);

	// 待处理任务队列
	private final BlockingQueue<DataEntity> queue = new LinkedBlockingQueue<DataEntity>();

	// 任务线程池
	private final ExecutorService taskService = Executors.newFixedThreadPool(16);

	private boolean shutdown = false;
	
	private Date lastForWardTime=new Date();
	
	private OrderForwardCountlyBoot() {

	}

	// 单例
	private static class OrderForwardBootHolder {
		private static final OrderForwardCountlyBoot INSTANCE = new OrderForwardCountlyBoot();
	}

	public static OrderForwardCountlyBoot getInstance() {
		return OrderForwardBootHolder.INSTANCE;
	}

	/**
	 * 任务放入队列
	 * 
	 * @param obj
	 */
	public void add(DataEntity dataEntity) {	
		this.queue.add(getOrderForWardCountly(dataEntity));
	}
	
	public OrderForwardCountly getOrderForWardCountly(DataEntity dataEntity){
		OrderForwardCountly orderForwardCountly=null;
		if(dataEntity instanceof OrderForwardCountly){
			orderForwardCountly=(OrderForwardCountly) dataEntity;
		}else{
			orderForwardCountly = new OrderForwardCountly(dataEntity);
		}
		return orderForwardCountly;
	}
	
	public void addAll(List<OrderForwardCountly> orderForwardCountlies){
		this.queue.addAll(orderForwardCountlies);
	}

	/**
	 * 开启任务
	 */
	public void startTask() {
		logger.info("OrderForwardCountlyBoot.startTask()");
		if (!start.get()) {
			start.set(true);
			// 总控会永久占用一个线程
			taskService.execute(this);
			SendOrder2CountlyScheduleBoot.getInstance().service();
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
				OrderForwardCountly appOrderForward = (OrderForwardCountly)queue.poll(5,TimeUnit.SECONDS);
				if(appOrderForward == null){
					continue;
				}
				logger.debug("OrderForwardCountlyBoot start task");
				// 执行任务
				taskService.execute(new Send2CoutlyOrder(appOrderForward));
			} catch (InterruptedException e) {
				logger.error("OrderForwardBoot.run() InterruptedException", e);
			} catch (Throwable t) {
				logger.error("OrderForwardBoot.run() Throwable", t);
			}
//			if(shutdown){
//				break;
//			}
//			try {
//				//当队列大于50时,将全部获取并一起处理
//				long waitTime =Long.parseLong(Global.getConfig("COUNTLY_ORDER_WAITTIME"));
//				int queueTotal = Integer.parseInt(Global.getConfig("COUNTLY_ORDER_TOTAL"));
//				//if(queue.size()>queueTotal || DateUtils.getDistanceOfTwoDate(lastForWardTime, new Date(), 1000*60*60)>=waitTime){
//					for(int i= 0 ; i<queue.size();i++){
//						//从队列中获取数据
//						OrderForwardCountly appOrderForward =getOrderForWardCountly(queue.poll(5,TimeUnit.SECONDS));
//						// poll可以停止应用 2015-10-10
//						if(appOrderForward == null){
//							continue;
//						}
//						logger.debug("OrderForwardCountlyBoot start task");
//						// 执行任务
//						taskService.execute(new Send2CoutlyOrder(appOrderForward));
//					}
//					lastForWardTime=new Date();
//				//}
//			} catch (InterruptedException e) {
//				logger.error("OrderForwardCountlyBoot.run() InterruptedException", e);
//			} catch (Throwable t) {
//				logger.error("OrderForwardCountlyBoot.run() Throwable", t);
//			}
		}
	}
	private OrderForwardCountly getOrderForwardCountly(T t) {
		// TODO Auto-generated method stub
		return null;
	}

	public void stopTask(){
		shutdown = true;
		taskService.shutdown();
		SendOrder2CountlyScheduleBoot.getInstance().stopSchedule();
	}
}