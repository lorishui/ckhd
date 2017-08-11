package me.ckhd.opengame.app.service;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.app.dao.CfgparamDao;
import me.ckhd.opengame.app.entity.Cfgparam;
import me.ckhd.opengame.common.utils.CacheUtils;
import me.ckhd.opengame.common.utils.KeyPrefix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("IOSGamePersonalCfgService")
public class IOSGamePersonalService implements CfgService{

	public final static String KEY_NAME = "ios_game_personal";

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CfgparamDao cfgparamDao;
	
	public CfgparamDao getCfgparamDao() {
		return cfgparamDao;
	}

	public void setCfgparamDao(CfgparamDao cfgparamDao) {
		this.cfgparamDao = cfgparamDao;
	}
	
	public Map<String, Object> getCfg(Cfgparam cfgparam) {
		String key = KeyPrefix.CFG_PARAM_KEY+KEY_NAME + cfgparam.getCkAppId() + "_"
				+ cfgparam.getVersionName()+ "_" + cfgparam.getCkChannelId();
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) CacheUtils.get(key);
		//获取数据
		if (result == null) {
			try{
				Cfgparam vo = cfgparamDao.findIOSGamePersonal(cfgparam);
				if( vo != null){
					result = vo.getExInfoMap();
				}
				if (result == null) {
					result = new HashMap<String, Object>();
					result.put("resultCode", "-1");
					result.put("errMsg", "未配置参数");
				} else {
					result.put("resultCode", "0");
					result.put("errMsg", "");
				}
				CacheUtils.put(key, result);
			}catch(Exception e){
				logger.error("error:",e);
			}
		}
		return result; 
	}
	
}
