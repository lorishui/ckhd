package me.ckhd.opengame.app.service;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.app.dao.CfgparamDao;
import me.ckhd.opengame.app.entity.Cfgparam;
import me.ckhd.opengame.common.utils.CacheUtils;
import me.ckhd.opengame.sys.utils.DictUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service("AdCfgService")
public class AdCfgService implements CfgService{

	public final static String KEY_NAME = "ad";

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CfgparamDao cfgparamDao;
	
	@Autowired
	private AdAndroidSwitchCfgService adAndroidService;
	
	public Map<String, Object> getCfg(Cfgparam cfgparam) {

		// android渠道转android的配置
		//TODO 转到android ad 处理
//		String ckChannelId = cfgparam.getCkChannelId();
//		if("".equals(ckChannelId) || "".equals(ckChannelId)){
//			adAndroidService.getCfg(cfgparam);
//		}
		
		Map<String,Object> result = new HashMap<String,Object>();
		try {

			String key = "ad_" + cfgparam.getCkAppId() + "_"
					+ cfgparam.getMmAppId() + "_" + cfgparam.getCkChannelId() + "_"
					+ cfgparam.getProvince() + "_" + cfgparam.getVersionName();
			@SuppressWarnings("unchecked")
			Map<String, Object> resultMap = (Map<String, Object>) CacheUtils.get(key);
			if (resultMap == null) {
				Cfgparam vo = cfgparamDao.findAd(cfgparam);
				if( vo != null){
					resultMap = vo.getExInfoMap();
				}else{
					resultMap = new HashMap<String, Object>();
				}
				CacheUtils.put(key, resultMap);
			}
			
			Map<String, Object> curr = new HashMap<String, Object>();
			curr.putAll(resultMap);
			// 非信任签名MD5广告是否启用，默认启用
			boolean enableSignAd = true;
			try {
				if (curr.get("enableSignAd") != null) {
					enableSignAd = (Boolean) curr.get("enableSignAd");
				}
			} catch (Throwable t) {
				// null do
			}
			
			curr.put("enableSignAd", enableSignAd && !DictUtils.isTrustSignMd5(cfgparam.getSignMD5()));
			
			result.put("resultCode", 0);
			result.put("result", curr);
			
		} catch (Exception e) {
			logger.error("发生异常：",e);
			result.put("resultCode", -1);
			result.put("errMsg","获取初始化数据出错");
		}
		String returnStr = (result == null ? null : JSONObject.toJSONString(result));
		logger.info(String.format("返回客户端的初始化数据信息:[%s]", returnStr));
		return result; 
	
	}

	public CfgparamDao getCfgparamDao() {
		return cfgparamDao;
	}

	public void setCfgparamDao(CfgparamDao cfgparamDao) {
		this.cfgparamDao = cfgparamDao;
	}

	public AdAndroidSwitchCfgService getAdAndroidService() {
		return adAndroidService;
	}

	public void setAdAndroidService(AdAndroidSwitchCfgService adAndroidService) {
		this.adAndroidService = adAndroidService;
	}
	
}
