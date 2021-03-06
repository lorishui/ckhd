package me.ckhd.opengame.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;

import me.ckhd.opengame.app.dao.CfgparamDao;
import me.ckhd.opengame.app.entity.Cfgparam;
import me.ckhd.opengame.app.entity.CfgparamVO;
import me.ckhd.opengame.app.utils.CfgParamSelectUtils;
import me.ckhd.opengame.common.utils.CacheUtils;

public abstract class CommonCfgService implements CfgService{

	protected Logger logger = LoggerFactory.getLogger(getClass());

	// 锁
	private ConcurrentHashMap<String, Lock> lockMap = new ConcurrentHashMap<String, Lock>();

	@Autowired
	protected CfgparamDao cfgparamDao;

	public CfgparamDao getCfgparamDao() {
		return cfgparamDao;
	}

	public void setCfgparamDao(CfgparamDao cfgparamDao) {
		this.cfgparamDao = cfgparamDao;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getCfg(Cfgparam cfgparam) {
		String childCkAppId = StringUtils.isNullOrEmpty(cfgparam.getChildCkAppId())?"#":cfgparam.getChildCkAppId();
		String key = cfgparam.getRqType() + "_" + cfgparam.getCkAppId()+"_"+childCkAppId;
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("resultCode", 0);
		try {
			// 按KEY及游戏缓存
			List<CfgparamVO> cacheCfgs = (List<CfgparamVO>) CacheUtils.get(key);

			if (cacheCfgs == null) {
				Lock lock = null;
				try {
					lockMap.putIfAbsent(key, new ReentrantLock());
					lock = lockMap.get(key);
					lock.lock();
					// 二次检查
					cacheCfgs = (List<CfgparamVO>) CacheUtils.get(key);
					if (cacheCfgs == null) {
						List<CfgparamVO> cfgs = CfgParamSelectUtils
								.cover(this.findCfgByType(cfgparam));
						CacheUtils.put(key, cfgs);
						cacheCfgs = cfgs;
					}
				} finally {
					if (lock != null) {
						lock.unlock();
					}
				}
			}

			Map<String, Object> map = CfgParamSelectUtils.select(cfgparam,
					cacheCfgs);
			if (map != null) {
				result.putAll(map);
			} else {
				result.put("resultCode", -1);
				result.put("errMsg", "no cfg");
			}
		} catch (Exception e) {
			logger.error("发生异常：", e);
			result.put("resultCode", -1);
			result.put("errMsg", "get data error");
		}
		if (logger.isInfoEnabled()) {
			String returnStr = (result == null ? null : JSONObject
					.toJSONString(result));
			logger.info(String.format("返回客户端的初始化数据信息:[%s]", returnStr));
		}
		return result;
	}
	
	public abstract List<Cfgparam> findCfgByType(Cfgparam cfgparam);
}