package me.ckhd.opengame.online.task;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import me.ckhd.opengame.app.entity.APPCk;
import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.app.utils.AppCkUtils;
import me.ckhd.opengame.common.config.Global;
import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.TencentCallbackData;
import me.ckhd.opengame.online.handle.common.tencent.OtherRequest;
import me.ckhd.opengame.online.service.OnlinePayService;
import me.ckhd.opengame.online.service.OnlineService;
import me.ckhd.opengame.online.service.TencentCallbackDataService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

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
			int periodTime = 3;
			try{
				periodTime = Integer.parseInt(Global.getConfig("send.order.schedule.periodTime"));
			}catch(Throwable t){
				logger.warn("get send.order.schedule.periodTime error,Use default periodTime = 3");
			}
			scheduledService.scheduleAtFixedRate(this, 10, periodTime, TimeUnit.SECONDS);
		}
	}
	
	public void run() {
		try{
			if( list == null || list.size() == 0 ){
				list = tencentCallbackDataService.getListData();
			}
			if(list!=null){
				for(TencentCallbackData ten : list){
					JSONObject json = JSONObject.parseObject(ten.getData());
					OnlinePay onlinePay = new OnlinePay();
					onlinePay.setOrderId(ten.getOrderId());
					onlinePay = onlinePayService.get(onlinePay);
					json.put("operate", 1);
					json.put("sendNum", ten.getSendNum());
					json.put("isResend", 1);
					if(onlinePay!=null && StringUtils.isNotBlank(onlinePay.getCkAppId())){
						APPCk appCk = AppCkUtils.getAppCkByIdAndChild(onlinePay.getCkAppId(),onlinePay.getChildCkAppId());;
						if( appCk != null ){
							onlinePay.setSercetKey(appCk.getSecretKey());
						}
						PayInfoConfig payInfo = new PayInfoConfig();
						payInfo.setAddCkAppId(onlinePay.getCkAppId());
						payInfo.setChannelId(onlinePay.getChannelId());
						payInfo.setPaytype("141");
						payInfo = onlineService.getPayInfo(payInfo);
						onlinePay.setPayInfoConfig(payInfo);
						OtherRequest other = new OtherRequest(onlinePay,json);
						ExecutorServiceUtils.execute(other);
					}else{
						ten.setStatus(1);
						tencentCallbackDataService.updateByOrderNull(ten);
					}
				}
			}
			list = null;
		}catch(Throwable e){
			logger.error("腾讯巡查出现错误！",e);
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
