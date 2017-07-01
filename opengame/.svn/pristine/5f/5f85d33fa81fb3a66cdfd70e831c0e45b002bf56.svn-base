package me.ckhd.opengame.api.task;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.ckhd.opengame.api.entity.AppOrderForward;
import me.ckhd.opengame.api.service.AppOrderForwardService;
import me.ckhd.opengame.common.config.Global;
import me.ckhd.opengame.common.utils.SpringContextHolder;

/**
 * OrderForwardBoot 中启动
 * 注意，多机器只启动一台
 * @author qibiao
 */
public class SendOrderScheduleBoot implements Runnable{

	protected static Logger logger = LoggerFactory.getLogger(SendOrderScheduleBoot.class);
	
	private final ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(1);
	
	private AppOrderForwardService appOrderForwardService;

	private SendOrderScheduleBoot(){
		
	}
	
	private static class SendOrderScheduleBootHolder{
		private static final SendOrderScheduleBoot INSTANCE = new SendOrderScheduleBoot();
	}
	
	public static SendOrderScheduleBoot getInstance() {
		return SendOrderScheduleBootHolder.INSTANCE;
	}
	
	public void service(){
		if(isStart()){
			appOrderForwardService = SpringContextHolder.getBean(AppOrderForwardService.class);
			int periodTime = 10;
			try{
				periodTime = Integer.parseInt(Global.getConfig("send.order.schedule.periodTime"));
			}catch(Throwable t){
				logger.warn("get send.order.schedule.periodTime error,Use default periodTime = 10");
			}
			scheduledService.scheduleAtFixedRate(this, 1, periodTime, TimeUnit.SECONDS);
		}
	}
	
	@Override
	public void run() {
		List<AppOrderForward> appOrderForwards = appOrderForwardService.selectWaitSend(Calendar.getInstance().getTime());
		if(appOrderForwards.size() > 0){
			OrderForwardBoot.getInstance().add(appOrderForwards);
		}
	}
	
	public boolean isStart(){
		String localHost;
		try {
			localHost = InetAddress.getLocalHost().getHostAddress();
			if(localHost.endsWith(Global.getConfig("send.order.schedule.serverIp"))){
				return true;
			}
		} catch (UnknownHostException e) {
			logger.error("SendOrderScheduleBoot.isStart() UnknownHostException", e);
		} catch (Throwable t) {
		}
		return false;
	}
	
	public void stopSchedule(){
		scheduledService.shutdown();
	}
}
