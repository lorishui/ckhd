/*
 * 
 */
package me.ckhd.opengame.app.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import me.ckhd.opengame.app.dao.CfgZoneLevelDao;
import me.ckhd.opengame.app.entity.CfgZoneLevel;
import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.common.utils.CacheUtils;
import me.ckhd.opengame.common.utils.StringUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author qibiao
 *
 */
@Service
@Transactional(readOnly = true)
public class CfgZoneLevelService extends CrudService<CfgZoneLevelDao, CfgZoneLevel>{

	private static String KEY_PREFIX = "CfgZoneLevelCACHE_";
	// 锁
	private static ConcurrentHashMap<String, Lock> lockMap = new ConcurrentHashMap<String, Lock>();

	/**
	 * 配置保存
	 * @param province
	 */
	@Transactional(readOnly = false)
	public void save(CfgZoneLevel cfgZoneLevel) {
		if (StringUtils.isBlank(cfgZoneLevel.getId())) {
			cfgZoneLevel.preInsert();
			dao.insert(cfgZoneLevel);
		} else {
			dao.update(cfgZoneLevel);
		}
	}
	
	public boolean checkExist(CfgZoneLevel cfgZoneLevel) {

		int x = dao.checkExist(cfgZoneLevel);

		return x > 0 ? true : false;
	}

	public	void updateRedzone(CfgZoneLevel vo){
		dao.updateRedzone(vo);
	}
	
	public	void updateYellowzone(CfgZoneLevel vo){
		dao.updateYellowzone(vo);
	}
	
	/**
	 *  配置读取
	 */
	@SuppressWarnings("unchecked")
	public List<CfgZoneLevel> getCfg(CfgZoneLevel cfgZoneLevel) {
		String key = KEY_PREFIX + cfgZoneLevel.getBiztype() + "_" + cfgZoneLevel.getCkappid();
		// 按KEY及游戏缓存
		List<CfgZoneLevel> cacheCfgs = (List<CfgZoneLevel>) CacheUtils.get(key);

		if (cacheCfgs == null) {
			Lock lock = null;
			try {
				lockMap.putIfAbsent(key, new ReentrantLock());
				lock = lockMap.get(key);
				lock.lock();
				// 二次检查
				cacheCfgs = (List<CfgZoneLevel>) CacheUtils.get(key);
				if (cacheCfgs == null) {
					List<CfgZoneLevel> cfgs = dao.findCfg(cfgZoneLevel);
					CacheUtils.put(key, cfgs);
					cacheCfgs = cfgs;
				}
			} finally {
				if (lock != null) {
					lock.unlock();
				}
			}
		}
		
		return cacheCfgs;
	}
	
	/**
	 * @param cfgZoneLevel
	 * @return
	 */
	public String getCacheRedCfg(CfgZoneLevel cfgZoneLevel) {

		long start = System.currentTimeMillis();
		
		List<CfgZoneLevel> cfgs = this.getCfg(cfgZoneLevel);
		String gamecfg = null;
		String defaultCfg = null;
		String channelCfg = null;
		for (CfgZoneLevel vo : cfgs) {
			if (cfgZoneLevel.getCkappid().equals(vo.getCkappid())) {
				gamecfg = vo.getRedzonelist();
			}else if(cfgZoneLevel.getChannelid().equals(vo.getChannelid())){
				channelCfg = vo.getRedzonelist();
			}else if("#".equals(vo.getCkappid()) && "#".equals(vo.getChannelid())){
				defaultCfg = vo.getRedzonelist();
			}
		}
		if(channelCfg == null){
			channelCfg = "";
		}
		if(defaultCfg == null){
			defaultCfg = "";
		}
		String result;
		if (gamecfg != null){
			result = gamecfg + channelCfg;
		}else {
			result = defaultCfg + channelCfg;
		}
		logger.debug("getCacheRedCfg cost time :" + (System.currentTimeMillis() - start) + "ms");
		logger.debug("CacheRedCfg, Ckappid is " + cfgZoneLevel.getCkappid() + ", Channelid is " + cfgZoneLevel.getChannelid() + result);
		return result;
	}

	/**
	 * @param cfgZoneLevel
	 * @return
	 */
	public String getCacheYellowCfg(CfgZoneLevel cfgZoneLevel) {

		List<CfgZoneLevel> cfgs = this.getCfg(cfgZoneLevel);
		String gamecfg = null;
		String defaultCfg = null;
		String channelCfg = null;
		for (CfgZoneLevel vo : cfgs) {
			if (cfgZoneLevel.getCkappid().equals(vo.getCkappid())) {
				gamecfg = vo.getYellowzonelist();
			}else if(cfgZoneLevel.getChannelid().equals(vo.getChannelid())){
				channelCfg = vo.getYellowzonelist();
			}else if("#".equals(vo.getCkappid()) && "#".equals(vo.getChannelid())){
				defaultCfg = vo.getYellowzonelist();
			}
		}
		if(channelCfg == null){
			channelCfg = "";
		}
		if(defaultCfg == null){
			defaultCfg = "";
		}
		if (gamecfg != null){
			return gamecfg + channelCfg;
		}else {
			return defaultCfg + channelCfg;
		}
	}
}
