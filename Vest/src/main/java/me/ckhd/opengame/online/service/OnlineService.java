package me.ckhd.opengame.online.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import me.ckhd.opengame.app.dao.ChannelDao;
import me.ckhd.opengame.app.dao.PayCodeConfigDao;
import me.ckhd.opengame.app.dao.PayInfoConfigDao;
import me.ckhd.opengame.app.entity.Channel;
import me.ckhd.opengame.app.entity.PayCodeConfig;
import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.CacheUtils;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.IdGen;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.dao.OfflinePayDao;
import me.ckhd.opengame.online.dao.OnlineLoginDao;
import me.ckhd.opengame.online.dao.OnlinePayDao;
import me.ckhd.opengame.online.entity.OfflinePay;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.sys.dao.DictDao;
import me.ckhd.opengame.sys.entity.Dict;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class OnlineService {

	@Autowired
	private OnlineLoginDao onlineLoginDao; //登陆数据持久层
	
	@Autowired
	private OnlinePayDao onlinePayDao;  //支onlinePayDao
	
	@Autowired
	private ChannelDao channelDao; //渠道数据持久层
	
	@Autowired
	private DictDao dictDao;  //字典数据持久层
	
	@Autowired
	private PayInfoConfigDao payInfoConfigDao;//游戏基本信息持久层
	
	@Autowired
	private PayCodeConfigDao payCodeConfigDao; //渠道计费列表持久层
	
	@Autowired
	private OfflinePayDao offlinePayDao; //单机
	
	public Channel getChannelById(String id){
		return channelDao.get(id);
	}
	
	public List<Channel> getChannelByName(String channelEngName){
		return channelDao.getChannelByEngName(channelEngName);
	}
	
	@Transactional(readOnly = false)
	public void saveUserInfo(OnlineUser userInfo){
		if (userInfo.getIsNewRecord()) {
			userInfo.setCreateDate(new Date());
			userInfo.setUpdateDate(userInfo.getCreateDate());
			onlineLoginDao.insert(userInfo);
		}else{
			userInfo.setUpdateDate(new Date());
			onlineLoginDao.update(userInfo);
		}
	}
	
	/**
	 * @Description 保存微信登录信息
	 * @param userInfo
	 */
	@Transactional(readOnly = false)
    public void saveUserInfoByWx(OnlineUser userInfo){
        userInfo.setToken(IdGen.uuid());
        userInfo.setUpdateDate(new Date());
        userInfo.setCreateDate(new Date());
        onlineLoginDao.insert(userInfo);
    }
	
	public OnlineUser getValidateUser(OnlineUser validateUser){
		return onlineLoginDao.getValidateUser(validateUser);
	}
	
	@Transactional(readOnly = false)
	public void savePayInfo(OnlinePay payInfo){
		if (StringUtils.isBlank(payInfo.getId())){
			payInfo.preInsert();
			onlinePayDao.insert(payInfo);
		}else{
			payInfo.preUpdate();
			onlinePayDao.update(payInfo);
		}
	}
	
	public OnlineUser get(OnlineUser user){
		return onlineLoginDao.get(user);
	}

	/**
	 * 没有用的
	 * @param user
	 * @return
	 */
	@Deprecated
	public int getCount(OnlineUser user){
		return onlineLoginDao.getCount(user);
	}
	
	public Dict getpayTypeDict(String value){
		Dict dict = new Dict();
		dict.setValue(value);
		dict.setType("paytype");
		List<Dict> dicts =  dictDao.findList(dict);
		if(dicts!=null && dicts.size()>0){
			return dicts.get(0);
		}
		return null;
	}
	
	public void updateOrderById(OnlinePay onlinePay){
		onlinePayDao.update(onlinePay);
	}
	
	public OnlinePay getOrderByOrderId(String orderId){
		OnlinePay onlinePay = new OnlinePay();
		onlinePay.setOrderId(orderId);
		return onlinePayDao.getOrderByOrderId(onlinePay);
	}
	
	//容大使用
	public OnlinePay getRdOrderByOrderId(String orderId){
		OnlinePay onlinePay = new OnlinePay();
		onlinePay.setOrderId(orderId);
		return onlinePayDao.getRdOrderByOrderId(onlinePay);
	}
	
	public OnlinePay getOrderByChannelOrderId(String channelOrderId, String channelId){
		OnlinePay onlinePay = new OnlinePay();
		onlinePay.setChannelOrderId(channelOrderId);
		onlinePay.setChannelId(channelId);
		return onlinePayDao.getOrderByChannelOrderId(onlinePay);
	}
	
	public OnlinePay getOrderByPrepayid(String orderId){
		OnlinePay onlinePay = new OnlinePay();
		onlinePay.setOrderId(orderId);
		return onlinePayDao.getOrderByPrepayid(onlinePay);
	}
	
	public OnlinePay getOrderByOutOrderId(String orderId){
		OnlinePay onlinePay = new OnlinePay();
		onlinePay.setOrderId(orderId);
		return onlinePayDao.getOrderByOrderId(onlinePay);
	}
	
	/**
	 * 百度使用
	 * @param orderId
	 * @return
	 */
	public OnlinePay getOrderById(String orderId){
		OnlinePay onlinePay = new OnlinePay();
		onlinePay.setOrderId(orderId);
		return onlinePayDao.getOrderByPrepayid(onlinePay);
	}
	
	/**
	 * TODO 获取渠道支付基本信息
	 * @param payInfoConfig
	 * @return
	 */
	public PayInfoConfig getPayInfo(PayInfoConfig payInfoConfig){
		// cacheKey是ehcache的key
		String cacheKey = "payInfoConfig";
		// indexKey是ehcache缓存的value(是map)取值的key
		String indexKey = "ckappid_"+ isNull(payInfoConfig.getCkAppId()) + ",paytype_" + isNull(payInfoConfig.getPaytype())+",channelId_"+isNull(payInfoConfig.getChannelId());
		PayInfoConfig payInfoConfigTemp = getCachePayInfoConfig(payInfoConfig, cacheKey, indexKey);
		if(payInfoConfigTemp == null){
			indexKey = "ckappid_"+isNull(payInfoConfig.getCkAppId())+ ",paytype_" + isNull(payInfoConfig.getPaytype())+",channelId_";
			payInfoConfigTemp = getCachePayInfoConfig(payInfoConfig, cacheKey, indexKey);
		}
		if(payInfoConfigTemp == null){
			indexKey = "ckappid_" + ",paytype_"+isNull(payInfoConfig.getPaytype())+",channelId_"+isNull(payInfoConfig.getChannelId());
			payInfoConfigTemp = getCachePayInfoConfig(payInfoConfig, cacheKey, indexKey);
		}
		if(payInfoConfigTemp == null){
			indexKey = "ckappid_" + ",paytype_"+isNull(payInfoConfig.getPaytype())+",channelId_";
			payInfoConfigTemp = getCachePayInfoConfig(payInfoConfig, cacheKey, indexKey);
		}
		return payInfoConfigTemp;
	}
	
	/**
	 * TODO 获取渠道支付基本信息 ---支付渠道专用
	 * @param payInfoConfig
	 * @return
	 */
	public PayInfoConfig getPayInfoByChild(PayInfoConfig payInfoConfig){
		// cacheKey是ehcache的key
		String cacheKey = "wx_payInfoConfig";
		// indexKey是ehcache缓存的value(是map)取值的key
		String indexKey = "ckappid_"+ isNull(payInfoConfig.getCkAppId())+",child_"+ isNull(payInfoConfig.getChildCkAppId()+"") +",channelId_"+isNull(payInfoConfig.getChannelId())+",paytype="+isNull(payInfoConfig.getPaytype());
		PayInfoConfig payInfoConfigTemp = getCachePayInfoConfigByChild(payInfoConfig, cacheKey, indexKey);
		if(payInfoConfigTemp == null){
			indexKey = "ckappid_"+ isNull(payInfoConfig.getCkAppId())+",child_"+ isNull(payInfoConfig.getChildCkAppId()+"") +",channelId_"+",paytype="+isNull(payInfoConfig.getPaytype());
			payInfoConfigTemp = getCachePayInfoConfigByChild(payInfoConfig, cacheKey, indexKey);
		}
		if(payInfoConfigTemp == null){
			indexKey = "ckappid_"+ isNull(payInfoConfig.getCkAppId())+",child_" +",channelId_"+isNull(payInfoConfig.getChannelId())+",paytype="+isNull(payInfoConfig.getPaytype());
			payInfoConfigTemp = getCachePayInfoConfigByChild(payInfoConfig, cacheKey, indexKey);
		}
		if(payInfoConfigTemp == null){
			indexKey = "ckappid_"+ isNull(payInfoConfig.getCkAppId())+",child_" +",channelId_"+",paytype="+isNull(payInfoConfig.getPaytype());
			payInfoConfigTemp = getCachePayInfoConfigByChild(payInfoConfig, cacheKey, indexKey);
		}
		if(payInfoConfigTemp == null){
			indexKey = "ckappid_"+ isNull(payInfoConfig.getCkAppId())+",child_1" +",channelId_"+",paytype="+isNull(payInfoConfig.getPaytype());
			payInfoConfigTemp = getCachePayInfoConfigByChild(payInfoConfig, cacheKey, indexKey);
		}
		return payInfoConfigTemp;
	}
	
	@SuppressWarnings("unchecked")
	private PayInfoConfig getCachePayInfoConfigByChild(PayInfoConfig payInfoConfig, String cacheKey, String indexKey){
		Map<String, Object> map = (Map<String, Object>) CacheUtils.get(cacheKey);
		if (map != null) {
			if(map.get(indexKey)!=null){
				return (PayInfoConfig)map.get(indexKey);
			}
		}
		if (lockMap.get(cacheKey) == null) {
			lockMap.putIfAbsent(cacheKey, new byte[0]);
		}

		synchronized (lockMap.get(cacheKey)) {
			map = (Map<String, Object>)CacheUtils.get(cacheKey);
			if (map != null) {
				if(map.get(indexKey)!=null){
					return (PayInfoConfig)map.get(indexKey);
				}
			}
			PayInfoConfig payInfo = new PayInfoConfig();
			payInfo.setPaytype(payInfoConfig.getPaytype());
			payInfo.setCkAppId(payInfoConfig.getCkAppId());
			List<PayInfoConfig> payInfos = payInfoConfigDao.findList(payInfo);
			if(payInfos == null){
				if(map == null){
					map = new ConcurrentHashMap<String, Object>();
				}
				String key = "ckappid_"+ isNull(payInfo.getCkAppId()) +",child_"+isNull(payInfo.getChildCkAppId()+"")+",channelId_"+isNull(payInfo.getChannelId())+",paytype="+isNull(payInfoConfig.getPaytype());
				map.put(key, new PayInfoConfig());
			} else {
				if(map == null){
					map = new ConcurrentHashMap<String, Object>();
				}
				for(PayInfoConfig payInfoconfigTemp:payInfos){
					String key = "ckappid_"+ isNull(payInfoconfigTemp.getCkAppId()) +",child_"+isNull(payInfoconfigTemp.getChildCkAppId()+"")+",channelId_"+isNull(payInfoconfigTemp.getChannelId())+",paytype="+isNull(payInfoConfig.getPaytype());
					map.put(key, payInfoconfigTemp);
				}
			}
			CacheUtils.put(cacheKey, map);
			return map.get(indexKey)==null?null:(PayInfoConfig)map.get(indexKey);
		}
	}
	
	/**
	 * TODO 获取渠道支付基本信息 ---苹果专用
	 * @param payInfoConfig
	 * @return
	 */
	public PayInfoConfig getPayInfoByChildApple(PayInfoConfig payInfoConfig){
		// cacheKey是ehcache的key
		String cacheKey = "wx_payInfoConfig";
		// indexKey是ehcache缓存的value(是map)取值的key
		String indexKey = "ckappid_"+ isNull(payInfoConfig.getCkAppId())+",child_"+ isNull(payInfoConfig.getChildCkAppId()+"") +",channelId_"+isNull(payInfoConfig.getChannelId());
		PayInfoConfig payInfoConfigTemp = getCachePayInfoConfigByChildApple(payInfoConfig, cacheKey, indexKey);
		if(payInfoConfigTemp == null){
			indexKey = "ckappid_"+ isNull(payInfoConfig.getCkAppId())+",child_"+ isNull(payInfoConfig.getChildCkAppId()+"") +",channelId_";
			payInfoConfigTemp = getCachePayInfoConfigByChildApple(payInfoConfig, cacheKey, indexKey);
		}
		if(payInfoConfigTemp == null){
			indexKey = "ckappid_"+ isNull(payInfoConfig.getCkAppId())+",child_" +",channelId_";
			payInfoConfigTemp = getCachePayInfoConfigByChildApple(payInfoConfig, cacheKey, indexKey);
		}
		return payInfoConfigTemp;
	}
	
	@SuppressWarnings("unchecked")
	private PayInfoConfig getCachePayInfoConfigByChildApple(PayInfoConfig payInfoConfig, String cacheKey, String indexKey){
		Map<String, Object> map = (Map<String, Object>) CacheUtils.get(cacheKey);
		if (map != null) {
			return map.get(indexKey)==null?null:(PayInfoConfig)map.get(indexKey);
		}
		if (lockMap.get(cacheKey) == null) {
			lockMap.putIfAbsent(cacheKey, new byte[0]);
		}

		synchronized (lockMap.get(cacheKey)) {
			map = (Map<String, Object>)CacheUtils.get(cacheKey);
			if (map != null) {
				return map.get(indexKey)==null?null:(PayInfoConfig)map.get(indexKey);
			}
			PayInfoConfig payInfo = new PayInfoConfig();
			payInfo.setPaytype(payInfoConfig.getPaytype());
			payInfo.setChannelId(payInfoConfig.getChannelId());
			List<PayInfoConfig> payInfos = payInfoConfigDao.findList(payInfo);
			if(payInfos == null){
				map = MapUtils.EMPTY_MAP;
			} else {
				map = new ConcurrentHashMap<String, Object>();
				for(PayInfoConfig payInfoconfigTemp:payInfos){
					String key = "ckappid_"+ isNull(payInfoconfigTemp.getCkAppId()) +",child_"+isNull(payInfoconfigTemp.getChildCkAppId()+"")+",channelId_"+isNull(payInfoconfigTemp.getChannelId());
					map.put(key, payInfoconfigTemp);
				}
			}
			CacheUtils.put(cacheKey, map);
			return map.get(indexKey)==null?null:(PayInfoConfig)map.get(indexKey);
		}
	}
	
	@SuppressWarnings("unchecked")
	private PayInfoConfig getCachePayInfoConfig(PayInfoConfig payInfoConfig, String cacheKey, String indexKey){
		Map<String, Object> map = (Map<String, Object>) CacheUtils.get(cacheKey);
		if (map != null) {
			return map.get(indexKey)==null?null:(PayInfoConfig)map.get(indexKey);
		}
		if (lockMap.get(cacheKey) == null) {
			lockMap.putIfAbsent(cacheKey, new byte[0]);
		}

		synchronized (lockMap.get(cacheKey)) {
			map = (Map<String, Object>)CacheUtils.get(cacheKey);
			if (map != null) {
				return map.get(indexKey)==null?null:(PayInfoConfig)map.get(indexKey);
			}
			List<PayInfoConfig> payInfos = payInfoConfigDao.findAllList(new PayInfoConfig());
			if(payInfos == null){
				map = MapUtils.EMPTY_MAP;
			} else {
				map = new ConcurrentHashMap<String, Object>();
				for(PayInfoConfig payInfoconfigTemp:payInfos){
					String key = "ckappid_"+ isNull(payInfoconfigTemp.getCkAppId()) + ",paytype_" + isNull(payInfoconfigTemp.getPaytype()) +",channelId_"+isNull(payInfoconfigTemp.getChannelId());
					map.put(key, payInfoconfigTemp);
				}
			}
			CacheUtils.put(cacheKey, map);
			return map.get(indexKey)==null?null:(PayInfoConfig)map.get(indexKey);
		}
	}
	
	private String isNull(String str){
		if("null".equals(str) || "".equals(str) || str==null){
			return "";
		}else{
			return str;
		}
		
	}
	// 缓存锁
	private static ConcurrentMap<String,byte[]> lockMap = new ConcurrentHashMap<String,byte[]>();
	
	
	/**
	 * TODO 获取登陆基本信息
	 * @param payInfoConfig
	 * @return
	 */
	public PayInfoConfig getLoginInfo(PayInfoConfig payInfoConfig){
		// cacheKey是ehcache的key
		String cacheKey = "loginPayInfoConfig";
		// indexKey是ehcache缓存的value(是map)取值的key
		String indexKey = "ckappid_"+ isNull(payInfoConfig.getCkAppId()) +",channelId_"+isNull(payInfoConfig.getChannelId())+",carrierAppid_"+isNull(payInfoConfig.getCarrierAppId());
		PayInfoConfig payInfoConfigTemp = getCacheLoginPayInfoConfig(payInfoConfig, cacheKey, indexKey);
		if(payInfoConfigTemp == null){
			indexKey = "ckappid_"+ isNull(payInfoConfig.getCkAppId())+",channelId_"+isNull(payInfoConfig.getChannelId())+",carrierAppid_";
			payInfoConfigTemp = getCacheLoginPayInfoConfig(payInfoConfig, cacheKey, indexKey);
		}
		if(payInfoConfigTemp == null){
			indexKey = "ckappid_"+ isNull(payInfoConfig.getCkAppId()) +",channelId_,carrierAppid_"+isNull(payInfoConfig.getCarrierAppId());
			payInfoConfigTemp = getCacheLoginPayInfoConfig(payInfoConfig, cacheKey, indexKey);
		}
		if(payInfoConfigTemp == null){
			indexKey = "ckappid_"+ isNull(payInfoConfig.getCkAppId()) +",channelId_,carrierAppid_";
			payInfoConfigTemp = getCacheLoginPayInfoConfig(payInfoConfig, cacheKey, indexKey);
		}
		return payInfoConfigTemp;
	}
	
	
	@SuppressWarnings("unchecked")
	private PayInfoConfig getCacheLoginPayInfoConfig(PayInfoConfig payInfoConfig, String cacheKey, String indexKey){
		Map<String, Object> map = (Map<String, Object>) CacheUtils.get(cacheKey);
		if (map != null) {
			return map.get(indexKey)==null?null:(PayInfoConfig)map.get(indexKey);
		}
		if (loginLockMap.get(cacheKey) == null) {
			loginLockMap.putIfAbsent(cacheKey, new byte[0]);
		}

		synchronized (loginLockMap.get(cacheKey)) {
			map = (Map<String, Object>)CacheUtils.get(cacheKey);
			if (map != null) {
				return map.get(indexKey)==null?null:(PayInfoConfig)map.get(indexKey);
			}
			List<PayInfoConfig> payInfos = payInfoConfigDao.findAllList(new PayInfoConfig());
			if(payInfos == null){
				map = MapUtils.EMPTY_MAP;
			} else {
				map = new ConcurrentHashMap<String, Object>();
				for(PayInfoConfig payInfoconfigTemp:payInfos){
					String key = "ckappid_"+ isNull(payInfoconfigTemp.getCkAppId()) +",channelId_"+isNull(payInfoconfigTemp.getChannelId())+",carrierAppid_"+isNull(payInfoconfigTemp.getCarrierAppId());
					map.put(key, payInfoconfigTemp);
				}
			}
			CacheUtils.put(cacheKey, map);
			return map.get(indexKey)==null?null:(PayInfoConfig)map.get(indexKey);
		}
	}
	// 缓存锁
	private static ConcurrentMap<String,byte[]> loginLockMap = new ConcurrentHashMap<String,byte[]>();
	
	
	/**
	 * TODO 获取渠道的计费列表
	 * @param payInfoConfig
	 * @param cacheKey
	 * @param indexKey
	 * @return
	 */
	public PayCodeConfig getPayCode(PayCodeConfig payCodeConfig){
		// cacheKey是ehcache的key
		String cacheKey = "payCodeInfo";
		// indexKey是ehcache缓存的value(是map)取值的key
		String indexKey = "ckappid_"+ isNull(payCodeConfig.getCkAppId())+",productId_"+isNull(payCodeConfig.getProductId()) +",channelId_"+isNull(payCodeConfig.getChannelId())+",paytype_"+isNull(payCodeConfig.getPaytype());
		PayCodeConfig paycode = getCachePayCode(payCodeConfig, cacheKey, indexKey);
		
		if(paycode == null){
			indexKey = "ckappid_"+ isNull(payCodeConfig.getCkAppId())+",productId_"+isNull(payCodeConfig.getProductId()) +",channelId_"+isNull(payCodeConfig.getChannelId())+",paytype_";
			paycode = getCachePayCode(payCodeConfig, cacheKey, indexKey);
		}
		
		if(paycode == null){
			indexKey = "ckappid_"+ isNull(payCodeConfig.getCkAppId()) +",productId_"+isNull(payCodeConfig.getProductId())+",channelId_,paytype_"+isNull(payCodeConfig.getPaytype());
			paycode = getCachePayCode(payCodeConfig, cacheKey, indexKey);
		}
		
		if(paycode == null){
			indexKey = "ckappid_"+ isNull(payCodeConfig.getCkAppId()) +",productId_"+isNull(payCodeConfig.getProductId())+",channelId_,paytype_";
			paycode = getCachePayCode(payCodeConfig, cacheKey, indexKey);
		}
		return paycode;
	}
	
	/**
	 * TODO 获取渠道的计费列表,加上子游戏Id的条件
	 * @param payInfoConfig
	 * @param cacheKey
	 * @param indexKey
	 * @return
	 */
	public PayCodeConfig getPayCodeByChild(PayCodeConfig payCodeConfig){
		// cacheKey是ehcache的key
		String cacheKey = "payCodeInfo_child";
		// indexKey是ehcache缓存的value(是map)取值的key
		String indexKey = "ckappid_"+ isNull(payCodeConfig.getCkAppId())+",childAppId_"+ isNull(payCodeConfig.getChildCkAppId())+",productId_"+isNull(payCodeConfig.getProductId()) +",channelId_"+isNull(payCodeConfig.getChannelId())+",paytype_"+isNull(payCodeConfig.getPaytype());
		PayCodeConfig paycode = getCachePayCodeByChild(payCodeConfig, cacheKey, indexKey);
		
		if(paycode == null){
			indexKey = "ckappid_"+ isNull(payCodeConfig.getCkAppId())+",childAppId_"+ isNull(payCodeConfig.getChildCkAppId())+",productId_"+isNull(payCodeConfig.getProductId()) +",channelId_"+isNull(payCodeConfig.getChannelId())+",paytype_";
			paycode = getCachePayCodeByChild(payCodeConfig, cacheKey, indexKey);
		}
		
		if(paycode == null){
			indexKey = "ckappid_"+ isNull(payCodeConfig.getCkAppId()) +",childAppId_"+ isNull(payCodeConfig.getChildCkAppId())+",productId_"+isNull(payCodeConfig.getProductId())+",channelId_,paytype_"+isNull(payCodeConfig.getPaytype());
			paycode = getCachePayCodeByChild(payCodeConfig, cacheKey, indexKey);
		}
		
		if(paycode == null){
			indexKey = "ckappid_"+ isNull(payCodeConfig.getCkAppId()) +",childAppId_"+ isNull(payCodeConfig.getChildCkAppId())+",productId_"+isNull(payCodeConfig.getProductId())+",channelId_,paytype_";
			paycode = getCachePayCodeByChild(payCodeConfig, cacheKey, indexKey);
		}
		return paycode;
	}
	
	/**
	 * 获取配置子游戏id的计费点类
	 * @param payCodeConfig
	 * @param cacheKey
	 * @param indexKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private PayCodeConfig getCachePayCodeByChild(PayCodeConfig payCodeConfig, String cacheKey,String indexKey){
		Map<String, Object> map = (Map<String, Object>) CacheUtils.get(cacheKey);
		if (map != null) {
			if(map.get(indexKey)!=null){
				return (PayCodeConfig)map.get(indexKey);
			}
		}
		if (lockCodeMap.get(cacheKey) == null) {
			lockCodeMap.putIfAbsent(cacheKey, new byte[0]);
		}

		synchronized (lockCodeMap.get(cacheKey)) {
			map = (Map<String, Object>)CacheUtils.get(cacheKey);
			if (map != null) {
				return map.get(indexKey)==null?null:(PayCodeConfig)map.get(indexKey);
			}
			PayCodeConfig payCode = new PayCodeConfig();
			payCode.setCkAppId(payCodeConfig.getCkAppId());
			//payCode.setChildCkAppId(payCodeConfig.getChildCkAppId());
			payCode.setPaytype(payCodeConfig.getPaytype());
			List<PayCodeConfig> payCodeConfigs = payCodeConfigDao.findList(payCode);
			if(payCodeConfigs == null){
				map = MapUtils.EMPTY_MAP;
			} else {
				map = new ConcurrentHashMap<String, Object>();
				if(payCodeConfigs!=null){
					for(PayCodeConfig codeConfig:payCodeConfigs){
						String key=("ckappid_"+isNull(codeConfig.getCkAppId())+",childAppId_"+ isNull(payCodeConfig.getChildCkAppId())+",productId_"+isNull(codeConfig.getProductId())+ ",channelId_"+isNull(codeConfig.getChannelId())+ ",paytype_"+isNull(codeConfig.getPaytype()));
						map.put(key,codeConfig);
					}
				}
			}
			CacheUtils.put(cacheKey, map);
			return map.get(indexKey)==null?null:(PayCodeConfig)map.get(indexKey);
		}
	}
	
	/**
	 * TODO 获取渠道的计费列表
	 * @param payInfoConfig
	 * @param cacheKey
	 * @param indexKey
	 * @return
	 */
	public List<PayCodeConfig> getPayCodeArray(PayCodeConfig payCodeConfig){
		// cacheKey是ehcache的key
		String cacheKey = "payCodeInfoArray_"+isNull(payCodeConfig.getCkAppId())+"_"+isNull(payCodeConfig.getChannelId());
		List<PayCodeConfig> paycode = getCachePayCodeArray(payCodeConfig, cacheKey);
		return paycode;
	}
	
	@SuppressWarnings("unchecked")
	private PayCodeConfig getCachePayCode(PayCodeConfig payCodeConfig, String cacheKey,String indexKey){
		Map<String, Object> map = (Map<String, Object>) CacheUtils.get(cacheKey);
		if (map != null) {
			return map.get(indexKey)==null?null:(PayCodeConfig)map.get(indexKey);
		}
		if (lockCodeMap.get(cacheKey) == null) {
			lockCodeMap.putIfAbsent(cacheKey, new byte[0]);
		}

		synchronized (lockCodeMap.get(cacheKey)) {
			map = (Map<String, Object>)CacheUtils.get(cacheKey);
			if (map != null) {
				return map.get(indexKey)==null?null:(PayCodeConfig)map.get(indexKey);
			}
			List<PayCodeConfig> payCodeConfigs = payCodeConfigDao.findList(new PayCodeConfig());
			if(payCodeConfigs == null){
				map = MapUtils.EMPTY_MAP;
			} else {
				map = new ConcurrentHashMap<String, Object>();
				if(payCodeConfigs!=null){
					for(PayCodeConfig codeConfig:payCodeConfigs){
						String key=("ckappid_"+isNull(codeConfig.getCkAppId())+",productId_"+isNull(codeConfig.getProductId())+ ",channelId_"+isNull(codeConfig.getChannelId())+ ",paytype_"+isNull(codeConfig.getPaytype()));
						map.put(key,codeConfig);
						
					}
				}
			}
			CacheUtils.put(cacheKey, map);
			return map.get(indexKey)==null?null:(PayCodeConfig)map.get(indexKey);
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<PayCodeConfig> getCachePayCodeArray(PayCodeConfig payCodeConfig, String cacheKey){
		Object list =  CacheUtils.get(cacheKey);
		if(list != null ){
			return (List<PayCodeConfig>)list;
		}
		List<PayCodeConfig> payCodeConfigs = payCodeConfigDao.findList(payCodeConfig);
		if( payCodeConfigs != null){
			CacheUtils.put(cacheKey, payCodeConfigs);
		}
		return payCodeConfigs;
	}
	
	// 缓存锁
	private static ConcurrentMap<String,byte[]> lockCodeMap = new ConcurrentHashMap<String,byte[]>();
	
	/**
	 * 数据库获取orderId
	 * @param map
	 * @return
	 */
	@Transactional(readOnly = false)
	public int getOrderId(String ckAppId){
		return  getOrderIdCache(ckAppId);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	public int getOrderIdCache(String ckAppId){
		Map<String, Object> map = (Map<String, Object>) CacheUtils.get("orderIdCache");
		if (map != null) {
			List<Integer> orders = map.get(ckAppId)==null?null:(ArrayList<Integer>)map.get(ckAppId);
			Integer orderId =1;
			if(orders!=null && orders.size()>0){
				orderId=orders.get(0);
				orders.remove(0);
				map.put(ckAppId, orders);
				CacheUtils.put("orderIdCache", map);
				return orderId;
			}
		}
		if (lockOrderIdMap.get("orderIdCache") == null) {
			lockOrderIdMap.putIfAbsent("orderIdCache", new byte[0]);
		}

		synchronized (lockOrderIdMap.get("orderIdCache")) {
			map = (Map<String, Object>)CacheUtils.get("orderIdCache");
			if (map != null) {
				List<Integer> orders = map.get(ckAppId)==null?null:(ArrayList<Integer>)map.get(ckAppId);
				Integer orderId =1;
				if(orders!=null && orders.size()>0){
					orderId=orders.get(0);
					orders.remove(0);
					map.put(ckAppId, orders);
					CacheUtils.put("orderIdCache", map);
					return orderId;
				}
			}
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("ckAppId", ckAppId);
			paramsMap.put("year", DateUtils.getYear());
			Integer orderId = getOrderId(paramsMap);
			map = new ConcurrentHashMap<String, Object>();
			List<Integer> orders = new ArrayList<Integer>();
			for (int i = orderId; i < orderId+10; i++) {
				orders.add(i);
			}
			map.put(ckAppId, orders);
			CacheUtils.put("orderIdCache", map);
			return getOrderIdCache(ckAppId);
		}
	}
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED)
	private Integer getOrderId(Map<String, Object> paramsMap){
		Integer result = onlinePayDao.getOrderId(paramsMap);
		if(result==null){
			paramsMap.put("num", 0);
			onlinePayDao.insertOrderId(paramsMap);
			result=0;
		}else{
			paramsMap.put("num", result+10);
			onlinePayDao.updateOrderId(paramsMap);
		}
		return result;
	}
	
	// 缓存锁
	private static ConcurrentMap<String,byte[]> lockOrderIdMap = new ConcurrentHashMap<String,byte[]>();

	
	/**
	 * 查询待转发的记录
	 * @return 等待转发的记录
	 */
	public List<OnlinePay> selectWaitSend(){
		return onlinePayDao.selectWaitSend();
	}
	/**
	 * 更新发送失败的记录 next_send_time =   status =  
	 * @param appOrderSender
	 * @return
	 */
	@Transactional(readOnly = false)
	public int updateSendFail(OnlinePay onlinePay){
		return onlinePayDao.updateSendFail(onlinePay);
	}
	/**
	 * 更新发送成功的记录 status = '0'
	 * @param appOrderSender
	 * @return
	 */
	@Transactional(readOnly = false)
	public int updateSendSucess(String id){
		return onlinePayDao.updateSendSucess(id);
	}
	
	/**
	 * 获取最大序号
	 * @return
	 */
	public int getMaxOrderIndex(){
		return onlinePayDao.getMaxOrderIndex();
	}
	
	/**
	 * 保存订单序号
	 * @param orderId
	 * @return
	 */
	@Transactional(readOnly = false)
	public int saveOrderIndex(String orderId){
		int maxId = getMaxOrderIndex();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", maxId);
		map.put("orderId", orderId);
		onlinePayDao.saveOrderIndex(map);
		return getOrderIndex(orderId);
	}
	
	/**
	 * 获取订单序号,已防止渠道透传参数少的情况
	 * @param id
	 * @return
	 */
	public int getOrderIndex(String orderId){
		return onlinePayDao.getOrderIndex(orderId);
	}
	
	/**
	 * 根据序号获取订单号
	 * @param id
	 * @return
	 */
	public int getOrderId(int index){
		return onlinePayDao.getOrderIdByIndex(index);
	}
	
	public Integer getCountByImsi(Map<String,String> map){
		return onlinePayDao.getCountByImsi(map);
	}
	
	/**
	 * 下单记录插入
	 * @param offlinePay
	 * @return
	 */
	public Integer addOfflinePay(OfflinePay offlinePay){
		offlinePay.preInsert();
		return offlinePayDao.insert(offlinePay);
	}
	
	public Integer getCountByImsiAndTime(Map<String,Object> map){
		return offlinePayDao.getCountByImsiAndTime(map);
	}
	
	public Integer getCountByIccidAndTime(Map<String,Object> map){
		return offlinePayDao.getCountByIccidAndTime(map);
	}
	
	public OnlinePay getOrderByChannelOrderIdOther(String channelOrderId){
		OnlinePay onlinePay = new OnlinePay();
		onlinePay.setChannelOrderId(channelOrderId);
		return onlinePayDao.getOrderByChannelOrderIdOther(onlinePay);
	}
}
