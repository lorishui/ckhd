package me.ckhd.opengame.app.service;

import java.util.List;

import me.ckhd.opengame.app.entity.Cfgparam;

import org.springframework.stereotype.Service;

@Service("IOSInitCfgService")
public class IOSInitCfgService extends CommonCfgService{
	
	@Override
	public List<Cfgparam> findCfgByType(Cfgparam cfgparam) {
		cfgparam.setRqType("iOSInit");
		return cfgparamDao.findCfgType(cfgparam);
	}
	
}
