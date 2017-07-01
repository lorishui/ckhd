 package me.ckhd.opengame.app.utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import me.ckhd.opengame.app.dao.ChannelCarriersDao;
import me.ckhd.opengame.app.entity.ChannelCarriers;
import me.ckhd.opengame.common.utils.CacheUtils;
import me.ckhd.opengame.common.utils.SpringContextHolder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
public class ChannelCarriersUtils {
	
	private static ChannelCarriersDao channelCarriersDao = SpringContextHolder.getBean(ChannelCarriersDao.class);
	public static final String CACHE_CHANNEL_MAP = "CACHE_CHANNEL_MAP";
	
	public static final String CKCHANNELID_CHANNELID_MAP = "CKCHANNELID_CHANNELID_MAP";
	
	public static final String CHANNELID_CKCHANNELID_MAP = "CHANNELID_CKCHANNELID_MAP";  //通过CKAPPID获取运营商APPID
	
	public static List<ChannelCarriers> getChannelList(){
		@SuppressWarnings("unchecked")
		Map<String, List<ChannelCarriers>> channelMap = (Map<String, List<ChannelCarriers>>)CacheUtils.get(CACHE_CHANNEL_MAP);
		if (channelMap==null){
			channelMap = Maps.newHashMap();
			for (ChannelCarriers channel : channelCarriersDao.findAllList(new ChannelCarriers())){
				List<ChannelCarriers> channelCkList = channelMap.get("ALL");
				if (channelCkList != null){
					channelCkList.add(channel);
				}else{
					channelMap.put("ALL", Lists.newArrayList(channel));
				}
			}
			CacheUtils.put(CACHE_CHANNEL_MAP, channelMap);
		}
		List<ChannelCarriers> channelList = channelMap.get("ALL");
		if (channelList == null){
			channelList = Lists.newArrayList();
		}
		return channelList;
	}
	
	/**
	 * 运营商CKAPPID返回APPID
	 * 暂时只适用于联通一对一配置
	 * @param ckAppId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getAppIdByCkAppId(String ckChannelId){
		if(ckChannelId == null){
			return "";
		}
		Map<String, String> carrierChannelMap = (Map<String, String>)CacheUtils.get(CHANNELID_CKCHANNELID_MAP);
		if(carrierChannelMap == null){
			carrierChannelMap = new ConcurrentHashMap<String, String>();
			CacheUtils.put(CHANNELID_CKCHANNELID_MAP, carrierChannelMap);
		}
		String ckappid = carrierChannelMap.get(ckChannelId);
		if(ckappid != null){
			return ckappid;
		}
		for(ChannelCarriers channelCarrier:ChannelCarriersUtils.getChannelList()){
			if(ckChannelId.equals(channelCarrier.getChannelId())){
				carrierChannelMap.put(ckChannelId, channelCarrier.getCarriersChannelId());
				return channelCarrier.getCarriersChannelId();
			}
		}
		carrierChannelMap.put(ckChannelId, "");
		return "";
	}
}
