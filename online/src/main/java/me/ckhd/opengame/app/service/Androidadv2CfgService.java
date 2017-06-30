package me.ckhd.opengame.app.service;

import java.util.List;
import java.util.Map;

import me.ckhd.opengame.app.entity.Cfgparam;

import org.springframework.stereotype.Service;

@Service("Androidadv2CfgService")
public class Androidadv2CfgService extends CommonCfgService implements CfgService{

	@Override
	public Map<String, Object> getCfg(Cfgparam cfgparam) {
		cfgparam.setRqType("androidadv2");
		return super.getCfg(cfgparam);
	}

	@Override
	public List<Cfgparam> findCfgByType(Cfgparam cfgparam) {
		cfgparam.setRqType("androidadv2");
		return cfgparamDao.findCfgType(cfgparam);
	}
	
}
