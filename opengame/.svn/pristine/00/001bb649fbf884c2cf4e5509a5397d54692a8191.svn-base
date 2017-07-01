package me.ckhd.opengame.app.service;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.app.dao.CfgparamDao;
import me.ckhd.opengame.app.entity.Cfgparam;
import me.ckhd.opengame.common.utils.CacheUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("AndroidadCfgService")
public class AndroidadCfgService implements CfgService{

	@Autowired
	private CfgparamDao cfgparamDao;
	
	@Override
	public Map<String, Object> getCfg(Cfgparam cfgparam) {
		
		// 请求参数 ckappId，versionName，rqType
		
		String key = "androidad_" + cfgparam.getCkAppId() + "_"
				+ cfgparam.getVersionName()+"_"+cfgparam.getCkChannelId()+"_"+cfgparam.getCarriers()+"_"
				+ (cfgparam.getProvince()==null?"":cfgparam.getProvince()) + "_" + (cfgparam.getMmAppId()==null?"":cfgparam.getMmAppId());
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) CacheUtils.get(key);
		if (result == null) {
			Cfgparam vo = cfgparamDao.findAndroidad(cfgparam);
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
