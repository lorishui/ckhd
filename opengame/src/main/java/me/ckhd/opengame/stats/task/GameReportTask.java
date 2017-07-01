/*
 * 创酷互动® 2016
 */
package me.ckhd.opengame.stats.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import me.ckhd.opengame.stats.entity.GameReport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qibiao
 * @date 2016-06-13
 * 报表任务
 */
public class GameReportTask implements Runnable {

	protected static Logger logger = LoggerFactory
			.getLogger(GameReportTask.class);
	
	private LinkedBlockingQueue<GameReport> queue = new LinkedBlockingQueue<GameReport>();

	// 任务线程池
	private final ExecutorService taskService = Executors.newFixedThreadPool(8);

	private final ReentrantLock lock = new ReentrantLock();

	private final AtomicBoolean start = new AtomicBoolean(false);
	
	private final AtomicBoolean shutdown = new AtomicBoolean(false);

	public void addTask(GameReport qryCnd) {
		queue.add(qryCnd);
	}

	public void startTask(){
		lock.lock();
	     try {
	    	 if(!start.get()){
	 			start.set(true);
	 			taskService.execute(this);
	 		}
	     } finally {
	       lock.unlock();
	     }
	}
	
	@Override
	public void run() {
		for (;;) {
			try {
				if (shutdown.get()) {
					break;
				}
				GameReport qryCnd = queue.poll(5, TimeUnit.SECONDS);

				if (qryCnd == null) {
					continue;
				}
				
				taskService.execute(new GeneralControlRunnable(qryCnd));
			} catch (Throwable t) {
				logger.error("GameReportTask error",t);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					//
				}
			}
		}
	}

	public void stop(){
		shutdown.set(true);
		taskService.shutdown();
		GeneralControlRunnable.stop();
	}
	
	// 单例
	private static class GameReportTaskHolder {
		private static final GameReportTask INSTANCE = new GameReportTask();
	}

	public static GameReportTask getInstance() {
		return GameReportTaskHolder.INSTANCE;
	}
	
}