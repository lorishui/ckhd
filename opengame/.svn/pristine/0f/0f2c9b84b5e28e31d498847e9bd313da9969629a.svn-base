package me.ckhd.opengame.blacklist;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import me.ckhd.opengame.common.utils.MacUtils;
import me.ckhd.opengame.sys.utils.DictUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlackListUtils {
	private static Logger log = LoggerFactory.getLogger(BlackListUtils.class);
	private MyBlackList blackList = new MyBlackList();
	
	private AtomicBoolean start = new AtomicBoolean(false);
	
	private Lock lock = new ReentrantLock();
	
	private BlackListUtils(){
		
	}
	
	public static BlackListUtils getInstance(){
		return BlackListUtilsHolder.INSTANCE;
	}

	public void init() {
		if(MacUtils.getOSName().startsWith("win")){
			return;
		}
		try {
			int n = blackList.init();
			if (n == 1) {
				log.info("BlackList init success!");
				return;
			}
			log.info("BlackList init error!");
		} catch (Throwable t) {
			//
			log.error("BlackList init error:", t);
		}
		// error exit
//		System.exit(1);
	}

	public boolean isBlackList(String imsi) {
		// log.info("BlackList imsi="+imsi);
		// log.info("BlackList isInit="+isInit);
		
//		if (DictUtils.isImsiWhiteList(imsi)) {
//			return false;
//		}
//		if(MacUtils.getOSName().startsWith("win")){
//			return false;
//		}
//		if(!start.get()){
//			try{
//				lock.lock();
//				if(!start.get()){
//					init();
//					start.set(true);
//				}
//			}finally{
//				lock.unlock();
//			}
//		}
//		int n = blackList.isInBlackList(imsi);
//
//		if (n == 1) {
//			return true;
//		}
		return false;
	}
	
	private static class BlackListUtilsHolder{
		private static final BlackListUtils INSTANCE = new BlackListUtils();
	}
}
