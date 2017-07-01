package me.ckhd.opengame.app.service;

import java.util.List;

import me.ckhd.opengame.app.entity.Cfgparam;

import org.springframework.stereotype.Service;

/**
 * SDK参数
 * @author qibiao
 *
 */
@Service("SdkCfgService")
public class SdkCfgService extends CommonCfgService {

	@Override
	public List<Cfgparam> findCfgByType(Cfgparam cfgparam) {
		cfgparam.setRqType("sdk");
		return cfgparamDao.findCfgType(cfgparam);
	}

}