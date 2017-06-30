 package me.ckhd.opengame.app.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import me.ckhd.opengame.app.dao.ChannelDao;
import me.ckhd.opengame.app.entity.Channel;
import me.ckhd.opengame.common.utils.CacheUtils;
import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.sys.entity.Role;
import me.ckhd.opengame.sys.entity.User;
import me.ckhd.opengame.sys.utils.UserUtils;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
public class ChannelUtils {
	
	private static ChannelDao channelDao = SpringContextHolder.getBean(ChannelDao.class);
	public static final String CACHE_CHANNEL_MAP = "channelMap";
	/**
	 * 根据用户权限控制渠道展示
	 * @return
	 */
	public static List<Channel> getChannelList(){
		@SuppressWarnings("unchecked")
		Map<String, List<Channel>> channelMap = (Map<String, List<Channel>>)CacheUtils.get(CACHE_CHANNEL_MAP);
		if (channelMap==null){
			channelMap = Maps.newHashMap();
			for (Channel channel : channelDao.findAllList(new Channel())){
				List<Channel> channelList = channelMap.get("ALL");
				if (channelList != null){
					channelList.add(channel);
				}else{
					channelMap.put("ALL", Lists.newArrayList(channel));
				}
			}
			CacheUtils.put(CACHE_CHANNEL_MAP, channelMap);
		}
		List<Channel> channelList = channelMap.get("ALL");
		User user = UserUtils.getUser();
		// user为空不显示列表
		if (channelList == null || user == null){
			channelList = Lists.newArrayList();
		}
		String channelId="";
		String[] channelIds = null;
		List<String> userList = new ArrayList<String>();
		for(Role role : user.getRoleList()){
			if(channelId != null && !"".equals(channelId)){
				channelId += ",";
			}
			channelId += role.getChannelIds();
		}
		channelIds = channelId.split(",");
		Collections.addAll(userList, channelIds);
		List<Channel> returnList = new ArrayList<Channel>();
		for(Channel ca :channelList){
				if(userList.contains(ca.getId())){
					returnList.add(ca);
				}
		}
		if(returnList != null &&returnList.size()>0){
			return returnList;
		}else{
			return channelList;
		}
		
	}
	
	public static String getChannelName(String channelId, String defaultValue){
		String channelName = (String) CacheUtils.get("CHANNEL_ID_" + channelId);
		if(channelName == null){
			if (StringUtils.isNotBlank(channelId) ){
			    channelName = channelDao.getNameByCarriersChannelId(channelId);
				CacheUtils.put("CHANNEL_ID_" + channelId, channelName);
				return channelName;
			}
		}else{
			return channelName;
		}
		
		return defaultValue;
	}
	
	
	public static String findChannelName(String id, String defaultValue){
		String channelName = (String) CacheUtils.get("ID_" + id);
		if(channelName == null){
			if (StringUtils.isNotBlank(id) ){
			    channelName = channelDao.findChannelName(id);
				CacheUtils.put("ID_" + id, channelName);
				return channelName;
			}
		}else{
			return channelName;
		}
		
		return defaultValue;
	}
	
	
	public static List<Channel> getCarriersChannelList(String carriersType){
		@SuppressWarnings("unchecked")
		Map<String, List<Channel>> channelMap = (Map<String, List<Channel>>)CacheUtils.get(carriersType+CACHE_CHANNEL_MAP);
		if (channelMap==null){
			channelMap = Maps.newHashMap();
			for (Channel channel : channelDao.findChannelByCarriersType(carriersType)){
				//
				channel.setName(channel.getName() + "(" + channel.getId() + ")");
				
				List<Channel> channelList = channelMap.get("ALL");
				if (channelList != null){
					channelList.add(channel);
				}else{
					channelMap.put("ALL", Lists.newArrayList(channel));
				}
			}
			CacheUtils.put(carriersType+CACHE_CHANNEL_MAP, channelMap);
		}
		List<Channel> channelList = channelMap.get("ALL");
		if (channelList == null){
			channelList = Lists.newArrayList();
		}
		return channelList;
	}
	
	public static List<Channel> getChannel(){
		@SuppressWarnings("unchecked")
		Map<String, List<Channel>> channelMap = (Map<String, List<Channel>>)CacheUtils.get(CACHE_CHANNEL_MAP);
		if (channelMap==null){
			channelMap = Maps.newHashMap();
			for (Channel channel : channelDao.findAllList(new Channel())){
				List<Channel> channelList = channelMap.get("ALL");
				if (channelList != null){
					channelList.add(channel);
				}else{
					channelMap.put("ALL", Lists.newArrayList(channel));
				}
			}
			CacheUtils.put(CACHE_CHANNEL_MAP, channelMap);
		}
		List<Channel> channelList = channelMap.get("ALL");
		if (channelList == null){
			channelList = Lists.newArrayList();
		}
		return channelList;
	}
}
