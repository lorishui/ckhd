package me.ckhd.opengame.app.service;

import java.util.List;

import me.ckhd.opengame.app.entity.Cfgparam;

import org.springframework.stereotype.Service;

@Service("AdAndroidSwitchCfgService")
public class AdAndroidSwitchCfgService extends CommonCfgService{

	@Override
	public List<Cfgparam> findCfgByType(Cfgparam cfgparam) {
		cfgparam.setRqType("adAndroidSwitch");
		return cfgparamDao.findCfgType(cfgparam);
	}
}
