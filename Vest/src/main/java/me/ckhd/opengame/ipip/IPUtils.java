package me.ckhd.opengame.ipip;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import me.ckhd.opengame.common.utils.Constant;
import me.ckhd.opengame.sys.utils.DictUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.WebUtils;

public class IPUtils{
	static Logger log = LoggerFactory.getLogger(IPUtils.class);
	static final String datUrl = "https://user.ipip.net/download.php?token=9b789f12f71029ee788156fca458e41647be3810";
	public static Timer timer = new Timer(); 
	public static boolean success = false; 
	
	public static void init() {
		log.info("ip init");
		Calendar curr = Calendar.getInstance();

		// 最近的周二00:05:12
		Calendar tueCal = Calendar.getInstance();
		tueCal.setTime(curr.getTime());
		tueCal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		tueCal.set(Calendar.HOUR_OF_DAY, 0);
		tueCal.set(Calendar.MINUTE, 5);
		tueCal.set(Calendar.SECOND, 12);
		tueCal.set(Calendar.MILLISECOND, 0);

		long diff = tueCal.getTimeInMillis() - curr.getTimeInMillis();
		if (diff < 0) {
			tueCal.set(Calendar.WEEK_OF_MONTH,
					tueCal.get(Calendar.WEEK_OF_MONTH) + 1);
		}
		
		timer.schedule(new TimerTask() {
			public void run() {
				start();
		   	}
		}, tueCal.getTime() , 7*24*60*60*1000);
	}
	
	public static void start(){
		int times = 0;
		while(times < 3){
			try {
				String serverUrl = DictUtils.getDictValue("url", "ip_down_url", datUrl);
				log.info("ip serverUrl="+serverUrl);
				String url = System.getProperty(WebUtils.DEFAULT_WEB_APP_ROOT_KEY);
				log.info("ip url="+url);
				boolean isMv = HttpUtils.get(serverUrl, url, 100000, 100000);
				log.info("ip isMv="+isMv);
				if(isMv){
//					IP.load(url+File.separator+Constant.IP_FILE_NAME);
					return;
				} else {
					log.warn("get IPFile error");
				}
			} catch (Throwable t) {
				log.error("IPUtils run error!", t);
			} finally {
				times ++;
				try {
					Thread.sleep(1 * 1000);
				} catch (Throwable t2) {
					log.error("sleep error!", t2);
				}
			}
		}
	}
	
	/**
	 * 开始启动
	 */
	public static void startUp(){
		try {
			String url = System.getProperty(WebUtils.DEFAULT_WEB_APP_ROOT_KEY)+File.separator+Constant.IP_FILE_NAME;
			log.info("ip url="+url);
			if(url !=  null ){
				IP.enableFileWatch = true;
				IP.load(url);
				success = true;
				log.info("ip success "+success);
			}
		} catch (IOException e) {
			log.error("IPUtils start error!", e);
		}
	}
	
	public static void stopTask(){
		if(timer != null){
			timer.cancel();
		}
	}
	
	public static String get(String ip){
		if(success && StringUtils.isNotBlank(ip)){
			StringBuffer sb = new StringBuffer();
			String[] ipArr = IP.find(ip);
			log.info("ip:"+ip+"  地址:"+Arrays.toString(ipArr));
			if(ipArr != null ){
//				sb.append(ipArr[0]);
				if(ipArr[1].trim().length() > 0){
					sb.append(ipArr[1]+"省");
				}
				if(ipArr[2].trim().length() > 0){
					sb.append(ipArr[2]+"市");
				}
				if( sb.length() >0 ){
					return sb.toString();
				}
			}
			return "未知ip";
		}
		return "未加载ip库成功";
	}

	public static String getCode(String ip){
		if(success && StringUtils.isNotBlank(ip)){
			String[] ipArr = IP.find(ip);
			log.info("ip:"+ip+"  地址:"+Arrays.toString(ipArr));
			String area = null;
			if(ipArr != null ){
				if(ipArr[1].trim().length() > 0){
					area = DictUtils.getDictValue(ipArr[1], "province_iccid", null);
				}
			}
			return area;
		}
		return null;
	}
	
	public static String getCityCode(String ip){
		if(success && StringUtils.isNotBlank(ip)){
			String[] ipArr = IP.find(ip);
			log.info("ip:"+ip+"  地址:"+Arrays.toString(ipArr));
			String area = null;
			if(ipArr != null ){
				if(ipArr[2].trim().length() > 0){
					area = DictUtils.getDictValue(ipArr[2], "province_iccid", null);
				}
				if(area == null){
					area = DictUtils.getDictValue(ipArr[1], "province_iccid", null);
				}
			}
			return area;
		}
		return null;
	}
	
	public static String getAddress(String ip){
		if(success && StringUtils.isNotBlank(ip)){
			String[] ipArr = IP.find(ip);
			log.info("ip:"+ip+"  地址:"+Arrays.toString(ipArr));
			String area = null;
			if(ipArr != null ){
				if(ipArr[1].trim().length() > 0){
					area += ipArr[1]+"省";
				}
				if(ipArr[2].trim().length() > 0){
					area += ipArr[1]+"市";
				}
			}
			return area;
		}
		return null;
	}
}
