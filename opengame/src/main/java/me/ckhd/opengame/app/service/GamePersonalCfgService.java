package me.ckhd.opengame.app.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.ckhd.opengame.app.dao.CfgparamDao;
import me.ckhd.opengame.app.entity.Cfgparam;
import me.ckhd.opengame.common.utils.CacheUtils;

@Service("GamePersonalCfgService")
public class GamePersonalCfgService implements CfgService{

	@Autowired
	private CfgparamDao cfgparamDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getCfg(Cfgparam cfgparam) {

		String key = "gamePersonalcfg_" + cfgparam.getCkAppId() + "_" + cfgparam.getCarriers() + "_" + cfgparam.getMmAppId() + "_"
				+ cfgparam.getCkChannelId() + "_" + cfgparam.getProvince() + "_"
				+ "_" + cfgparam.getVersionName();
		
		Map<String, Object> result = (Map<String, Object>) CacheUtils.get(key);
		if (result == null) {
			Cfgparam vo = cfgparamDao.findGamePersonal(cfgparam);
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
		}
		return result;
		
	}
	
}
