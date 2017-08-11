package me.ckhd.opengame.app.service;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.app.dao.CfgparamDao;
import me.ckhd.opengame.app.entity.Cfgparam;
import me.ckhd.opengame.common.utils.CacheUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 开心 战机需求
 * @author qibiao
 *
 */
@Service("ProvinceLevelCfgService")
public class ProvinceLevelCfgService implements CfgService{

	@Autowired
	private CfgparamDao cfgparamDao;
	
	public CfgparamDao getCfgparamDao() {
		return cfgparamDao;
	}

	public void setCfgparamDao(CfgparamDao cfgparamDao) {
		this.cfgparamDao = cfgparamDao;
	}
	/**
	 * 获取当前省份级别
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCfg(Cfgparam cfgparam) {
		Map<String, Object> result = null;
		String key = "ProvinceLevel_" + cfgparam.getCkAppId() + "_" +
				cfgparam.getCkChannelId() + cfgparam.getVersionName()+ "_"
				+ cfgparam.getProvince();
		result = (Map<String, Object>) CacheUtils.get(key);
		if(result == null){
			Cfgparam vo = cfgparamDao.findProvinceLevel(cfgparam);
			if(vo != null){
				result = vo.getExInfoMap();
			}
			if(result == null){
				result = new HashMap<String, Object>();
				result.put("level", "0");
			}
			CacheUtils.put(key, result);
		}
		return 	result;
	}

}
