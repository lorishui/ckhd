/*
 * www.ckhd.me
 */
package me.ckhd.opengame.app.service;

import java.util.Map;

import me.ckhd.opengame.app.entity.Cfgparam;
/**
 * 参数配置服务
 * @author qibiao
 */
public interface CfgService {

	/**
	 * 获取参数配置
	 * @return
	 */
	public Map<String,Object> getCfg(Cfgparam cfgparam);
	
}
