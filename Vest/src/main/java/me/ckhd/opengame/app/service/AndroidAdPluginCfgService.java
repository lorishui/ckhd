package me.ckhd.opengame.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import me.ckhd.opengame.app.entity.Cfgparam;

@Service("AndroidAdPluginCfgService")
public class AndroidAdPluginCfgService extends CommonCfgService implements CfgService{

	@Override
	public Map<String, Object> getCfg(Cfgparam cfgparam) {
		return super.getCfg(cfgparam);
	}

	@Override
	public List<Cfgparam> findCfgByType(Cfgparam cfgparam) {
		cfgparam.setRqType("androidAdPlugin");
		return cfgparamDao.findCfgType(cfgparam);
	}

}
