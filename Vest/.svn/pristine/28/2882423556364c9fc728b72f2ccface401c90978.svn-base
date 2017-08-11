package me.ckhd.opengame.online.sendOrder.task;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import me.ckhd.opengame.common.config.Global;
import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.service.OnlineService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OrderSenderBoot 中启动
 * 注意，多机器只启动一台
 * @author qibiao
 */
public class SendOrderScheduleBoot implements Runnable{

	protected static Logger logger = LoggerFactory.getLogger(SendOrderScheduleBoot.class);
	
	private final ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(1);
	
	private OnlineService onlineService;

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
			onlineService = SpringContextHolder.getBean(OnlineService.class);
			int periodTime = 10;
			try{
				periodTime = Integer.parseInt(Global.getConfig("send.order.schedule.periodTime"));
			}catch(Throwable t){
				logger.warn("get send.order.schedule.periodTime error,Use default periodTime = 10");
			}
			scheduledService.scheduleAtFixedRate(this, 1, periodTime, TimeUnit.SECONDS);
		}
	}
	
	public void run() {
		List<OnlinePay> appOrderSenders = onlineService.selectWaitSend();
		if(appOrderSenders.size() > 0){
			OrderSenderBoot.getInstance().add(appOrderSenders);
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
