package me.ckhd.opengame.app.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import me.ckhd.opengame.app.dao.AppCarriersDao;
import me.ckhd.opengame.app.entity.AppCarriers;
import me.ckhd.opengame.app.entity.Paycode;
import me.ckhd.opengame.common.utils.CacheUtils;
import me.ckhd.opengame.common.utils.SpringContextHolder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
public class AppCarriersUtils {
	
	private static AppCarriersDao appCarriersDao = SpringContextHolder.getBean(AppCarriersDao.class);
	public static final String CACHE_APP_MAP = "appCarriersMap";
	
	public static final String APPID_CKAPPID_MAP = "APPID_CKAPPID_MAP";
	
	public static final String CKAPPID_APPID_MAP = "CKAPPID_APPID_MAP";  //通过CKAPPID获取运营商APPID
	
	public static List<AppCarriers> getAPPList(){
		@SuppressWarnings("unchecked")
		Map<String, List<AppCarriers>> appMap = (Map<String, List<AppCarriers>>)CacheUtils.get(CACHE_APP_MAP);
		if (appMap==null){
			appMap = Maps.newHashMap();
			for (AppCarriers app : appCarriersDao.findAllList(new AppCarriers())){
				List<AppCarriers> appckList = appMap.get("ALL");
				if (appckList != null){
					appckList.add(app);
				}else{
					appMap.put("ALL", Lists.newArrayList(app));
				}
			}
			CacheUtils.put(CACHE_APP_MAP, appMap);
		}
		List<AppCarriers> appList = appMap.get("ALL");
		if (appList == null){
			appList = Lists.newArrayList();
		}
		return appList;
	}
	
	/**
	 * 
	 * @param appId : eg. : MM_30000  ,eg. ANDGAME_60000
	 * @return  cpServerUrl
	 */
	
	public static String  getServerUrlByAppId(String appId){
		String  serverUrl =  (String)CacheUtils.get(appId);
		//缓存中不存在 
		if (serverUrl==null){
			for (AppCarriers app : getAPPList()){
				if (app.getAppId() != null && (app.getCarriersType()+"_"+app.getAppId()).equalsIgnoreCase(appId)){
					serverUrl = app.getCpServerUrl();
				}else{
					continue;
				}
			}
			CacheUtils.put(appId, serverUrl);
			return serverUrl;
		}else{//缓存中存在 直接返回
			return serverUrl;
		}
	}
	
	
	
	
	public static List<AppCarriers> getAPPListByType(String carriersType){
		List<AppCarriers> appList = new ArrayList<AppCarriers>();
		for (AppCarriers app : getAPPList()){
			if (app.getCarriersType().equalsIgnoreCase(carriersType)){
				appList.add(app);
			}else{
				continue;
			}
		}
		return appList;
	}
	
	public static String  getPaycodeName(String carriersType,String paycode,String defaultValue) {
		String paycodeName = (String) CacheUtils.get("PAYCODE_" + paycode);
		if(paycodeName == null){
			if (StringUtils.isNotBlank(paycode) ){
				Paycode paycodeBean = new Paycode();
				paycodeBean.setPaycode(paycode);
				paycodeBean.setCarriersType(carriersType);
				
			    paycodeName = appCarriersDao.getPaycodeName(paycodeBean);
				CacheUtils.put("PAYCODE_" + paycode, paycodeName);
				return paycodeName;
			}
		}else{
			return paycodeName;
		}
		
		return defaultValue;
	}
	
	/**
	 * 运营商APPID返回CKAPPID
	 * @param appid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getCkAppIdByAppId(String appid){
		if(appid == null){
			return "";
		}
		Map<String, String> appMap = (Map<String, String>)CacheUtils.get(APPID_CKAPPID_MAP);
		if(appMap == null){
			appMap = new ConcurrentHashMap<String, String>();
			CacheUtils.put(APPID_CKAPPID_MAP, appMap);
		}
		String ckappid = appMap.get(appid);
		if(ckappid != null){
			return ckappid;
		}
		for(AppCarriers appCarriers:AppCarriersUtils.getAPPList()){
			if(appid.equals(appCarriers.getAppId())){
				appMap.put(appid, appCarriers.getCkappId());
				return appCarriers.getCkappId();
			}
		}
		appMap.put(appid, "");
		return "";
	}
	
	
	/**
	 * 运营商CKAPPID返回APPID
	 * 暂时只适用于联通一对一配置
	 * @param ckAppId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getAppIdByCkAppId(String ckAppId){
		if(ckAppId == null){
			return "";
		}
		Map<String, String> ckAappMap = (Map<String, String>)CacheUtils.get(CKAPPID_APPID_MAP);
		if(ckAappMap == null){
			ckAappMap = new ConcurrentHashMap<String, String>();
			CacheUtils.put(CKAPPID_APPID_MAP, ckAappMap);
		}
		String ckappid = ckAappMap.get(ckAppId);
		if(ckappid != null){
			return ckappid;
		}
		for(AppCarriers appCarriers:AppCarriersUtils.getAPPList()){
			if(ckAppId.equals(appCarriers.getCkappId())){
				ckAappMap.put(ckAppId, appCarriers.getAppId());
				return appCarriers.getAppId();
			}
		}
		ckAappMap.put(ckAppId, "");
		return "";
	}
	
	/**
	 * appId like 'MM_300008853065'
	 * @param appId
	 * @return
	 */
	public static AppCarriers getAppCarriers(String appId){
		AppCarriers vo = (AppCarriers)CacheUtils.get("app_carriers_obj_" + appId);
		//缓存中不存在 
		if (vo==null){
			for (AppCarriers app : getAPPList()){
				if (app.getAppId() != null && (app.getCarriersType()+"_"+app.getAppId()).equalsIgnoreCase(appId)){
					vo = app;
				}else{
					continue;
				}
			}
			CacheUtils.put("app_carriers_obj_" + appId, vo);
			return vo;
		}else{//缓存中存在 直接返回
			return vo;
		}
	}
}
