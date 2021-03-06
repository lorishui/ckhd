package me.ckhd.opengame.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ckhd.opengame.app.dao.CfgparamDao;
import me.ckhd.opengame.app.entity.Cfgparam;
import me.ckhd.opengame.common.utils.CacheUtils;
import me.ckhd.opengame.common.utils.KeyPrefix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service("CkPlatformPaySwitchCfgService")
public class CkPlatformPaySwitchCfgService extends CommonCfgService implements CfgService{

	public final static String KEY_NAME = "ckPlatformPaySwitch";

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CfgparamDao cfgparamDao;
	
	public CfgparamDao getCfgparamDao() {
		return cfgparamDao;
	}

	public void setCfgparamDao(CfgparamDao cfgparamDao) {
		this.cfgparamDao = cfgparamDao;
	}

	@Override
	public List<Cfgparam> findCfgByType(Cfgparam cfgparam) {
		cfgparam.setRqType("ckPlatformPaySwitch");
		return cfgparamDao.findCfgType(cfgparam);
	}
	
	@Override
	public Map<String, Object> getCfg(Cfgparam cfgparam) {

		String key = KeyPrefix.CFG_PARAM_KEY+KEY_NAME + cfgparam.getCkAppId() + "_"
				+ cfgparam.getVersionName()+ "_" + cfgparam.getCkChannelId();
		//获取数据
		@SuppressWarnings("unchecked")
		Map<String,Object> result =  (Map<String, Object>) CacheUtils.get(key);
		if (result == null) {
			result = super.getCfg(cfgparam);
			if (result == null) {
				result = new HashMap<String, Object>();
				result.put("resultCode", "-1");
				result.put("errMsg", "未配置参数");
			} 
			if ( !result.containsKey("resultCode") ) {
				result.put("resultCode", "0");
				result.put("errMsg", "");
			}
			CacheUtils.put(key, result);
		}
		String returnStr = (result == null ? null : JSONObject.toJSONString(result));
		logger.info(String.format("返回客户端的初始化数据信息:[%s]", returnStr));
		return result; 
	}
	
}
