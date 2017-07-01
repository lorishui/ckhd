package me.ckhd.opengame.countly.task;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import me.ckhd.opengame.common.config.Global;
import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.countly.entity.OrderForwardCountly;
import me.ckhd.opengame.countly.service.OrderForwardCountlyService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OrderForwardBoot 中启动
 * 注意，多机器只启动一台
 * @author qibiao
 */
public class SendOrder2CountlyScheduleBoot implements Runnable{

	protected static Logger logger = LoggerFactory.getLogger(SendOrder2CountlyScheduleBoot.class);
	
	private final ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(1);
	
	private OrderForwardCountlyService orderForwardCountlyService;

	private SendOrder2CountlyScheduleBoot(){
		
	}
	
	private static class SendOrderScheduleBootHolder{
		private static final SendOrder2CountlyScheduleBoot INSTANCE = new SendOrder2CountlyScheduleBoot();
	}
	
	public static SendOrder2CountlyScheduleBoot getInstance() {
		return SendOrderScheduleBootHolder.INSTANCE;
	}
	
	public void service(){
		//if(isStart()){
			orderForwardCountlyService = SpringContextHolder.getBean(OrderForwardCountlyService.class);
			int periodTime = 10;
			try{
				periodTime = Integer.parseInt(Global.getConfig("send.order.schedule.periodTime"));
			}catch(Throwable t){
				logger.warn("get send.order.schedule.periodTime error,Use default periodTime = 10");
			}
			scheduledService.scheduleAtFixedRate(this, 1, periodTime, TimeUnit.SECONDS);
		//}
	}
	
	@Override
	public void run() {
		List<OrderForwardCountly> orderForwardCountlies = orderForwardCountlyService.selectWaitSend(Calendar.getInstance().getTime());
		if(orderForwardCountlies.size() > 0){
			OrderForwardCountlyBoot.getInstance().addAll(orderForwardCountlies);
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
