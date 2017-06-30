package me.ckhd.opengame.sys.utils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import me.ckhd.opengame.common.utils.CacheUtils;

public class DBDataCacheUtils {

	private static String CHANNEL_CARRIERS_AND_CHANNELID = "CHANNEL_CARRIERS_AND_CHANNELID";
	private final static ReentrantLock CHANNEL_CARRIERS_AND_CHANNELID_LOCK = new ReentrantLock();

	private static String CHANNEL_CARRIERS_MM_CHANNELID = "CHANNEL_CARRIERS_MM_CHANNELID";
	private final static ReentrantLock CHANNEL_CARRIERS_MM_CHANNELID_LOCK = new ReentrantLock();

	@SuppressWarnings("rawtypes")
	public static Object getCacheObject(String key, Lock lock,
			String methodName, Class[] clzz, Object[] objs) {

		Object cache = CacheUtils.get(key);
		if (cache == null) {
			lock.lock();
			try {
				cache = CacheUtils.get(key);
				if (cache != null) {
					return cache;
				}
				try {
					Method m = DBDataCacheUtils.class.getMethod(methodName,
							clzz);
					Object rst = m.invoke(null, objs);
					CacheUtils.put(key, rst);
					return rst;
				} catch (Throwable t) {
					//
				}

			} finally {
				lock.unlock();
			}
		}
		return cache;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> getAndChannelCKChannelMap() {
		return (Map<String, String>) getCacheObject(
				CHANNEL_CARRIERS_AND_CHANNELID,
				CHANNEL_CARRIERS_AND_CHANNELID_LOCK, "initChannelCKChannelMap",
				new Class[] { String.class }, new String[] { "ANDGAME" });
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> getMMChannelCKChannelMap() {
		return (Map<String, String>) getCacheObject(
				CHANNEL_CARRIERS_MM_CHANNELID,
				CHANNEL_CARRIERS_MM_CHANNELID_LOCK, "initChannelCKChannelMap",
				new Class[] { String.class }, new String[] { "MM" });
	}

}