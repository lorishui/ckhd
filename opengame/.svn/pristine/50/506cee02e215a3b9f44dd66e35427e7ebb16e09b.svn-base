package me.ckhd.opengame.online.task;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import me.ckhd.opengame.app.entity.APPCk;
import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.app.utils.AppCkUtils;
import me.ckhd.opengame.common.config.Global;
import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.TencentCallbackData;
import me.ckhd.opengame.online.request.tencent.OtherRequest;
import me.ckhd.opengame.online.service.OnlinePayService;
import me.ckhd.opengame.online.service.OnlineService;
import me.ckhd.opengame.online.service.TencentCallbackDataService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * tencent_callback_data 中启动
 * 注意，多机器只启动一台
 * @author liupei
 */
public class TencentSendOrderScheduleBoot implements Runnable{

	protected static Logger logger = LoggerFactory.getLogger(TencentSendOrderScheduleBoot.class);
	
	private final ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(1);
	
	private TencentCallbackDataService tencentCallbackDataService = null;
	
	private OnlinePayService onlinePayService = null;
	
	private OnlineService onlineService = null;
	
	private List<TencentCallbackData> list = null;

	private TencentSendOrderScheduleBoot(){
	}
	
	private static class TencentSendOrderScheduleBootHolder{
		private static final TencentSendOrderScheduleBoot INSTANCE = new TencentSendOrderScheduleBoot();
	}
	
	public static TencentSendOrderScheduleBoot getInstance() {
		return TencentSendOrderScheduleBootHolder.INSTANCE;
	}
	
	public void init(){
		if(isStart()){
			tencentCallbackDataService = SpringContextHolder.getBean(TencentCallbackDataService.class);
			onlinePayService = SpringContextHolder.getBean(OnlinePayService.class);
			onlineService = SpringContextHolder.getBean(OnlineService.class);
			int periodTime = 10;
			try{
				periodTime = Integer.parseInt(Global.getConfig("send.order.schedule.periodTime"));
			}catch(Throwable t){
				logger.warn("get send.order.schedule.periodTime error,Use default periodTime = 3");
			}
			scheduledService.scheduleAtFixedRate(this, 10, periodTime, TimeUnit.SECONDS);
		}
	}
	
	@Override
	public void run() {
		if( list == null ){
			list = tencentCallbackDataService.getListData();
		}
		if(list!=null){
			for(TencentCallbackData ten : list){
				Map<String, Object> map = MyJsonUtils.jsonStr2Map(ten.getData());
				OnlinePay onlinePay = new OnlinePay();
				onlinePay.setOrderId(ten.getOrderId());
				onlinePay = onlinePayService.get(onlinePay);
				map.put("operate", "5");
				map.put("sendNum", ten.getSendNum());
				map.put("isResend", 1);
				onlinePay.setPayMap(map);
				APPCk appCk = AppCkUtils.getAppCkById(onlinePay.getCkAppId());
				if( appCk != null ){
					onlinePay.setSercetKey(appCk.getSecretKey());
				}
				PayInfoConfig payInfo = new PayInfoConfig();
				payInfo.setAddCkAppId(onlinePay.getCkAppId());
				payInfo.setChannelId(onlinePay.getChannelId());
				payInfo.setPaytype("141");
				payInfo = onlineService.getPayInfo(payInfo);
				onlinePay.setPayInfoConfig(payInfo);
				OtherRequest other = new OtherRequest(onlinePay);
				ten.setStatus(2);
				tencentCallbackDataService.update(ten);
				ExecutorServiceUtils.execute(other);
			}
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
		ExecutorServiceUtils.stopTask();
	}
}
