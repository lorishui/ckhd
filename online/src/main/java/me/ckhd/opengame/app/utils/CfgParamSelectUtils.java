/*
 * 
 */
package me.ckhd.opengame.app.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.ckhd.opengame.app.entity.Cfgparam;
import me.ckhd.opengame.app.entity.CfgparamVO;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 根据请求参数从已排序的配置结果集中选择
 * 
 * @author qibiao
 */
public class CfgParamSelectUtils {

	protected static Logger logger = LoggerFactory.getLogger(CfgParamSelectUtils.class);

	/**
	 * 注意：要求cfgs已经倒序，这样#优先级最低
	 * @param cfgparam
	 * @param cfgs
	 * @return
	 */
	public static Map<String,Object> select(Cfgparam cfgparam, List<CfgparamVO> cfgs) {
		try {
			if (StringUtils.isBlank(cfgparam.getCarriers())) {
				cfgparam.setCarriers("#");
			}
			if (StringUtils.isBlank(cfgparam.getMmAppId())) {
				cfgparam.setMmAppId("#");
			}
			// ckChannelId 不会空
			// versionName 不会空
			if (StringUtils.isBlank(cfgparam.getProvince())) {
				cfgparam.setProvince("#");
			}
			//加上childCkAppId
			if(StringUtils.isBlank(cfgparam.getChildCkAppId())){
				cfgparam.setChildCkAppId("#");
			}

			for (CfgparamVO cfg : cfgs) {
				//子游戏
				if( !cfgparam.getChildCkAppId().equals(cfg.getChildCkAppId()) && !"#".equals(cfg.getChildCkAppId()) ){
					// 不匹配则下一条
					continue;
				}
				
				// 3、渠道
				if (!existSub(cfg.getCkChannelId(), cfgparam.getCkChannelId())
						&& !cfg.getCkChannelId().equals("#")) {
					// 不匹配则下一条
					continue;
				}
				// 4、版本号
				if (!existSub(cfg.getVersionName(), cfgparam.getVersionName())
						&& !cfg.getVersionName().equals("#")) {
					// 不匹配则下一条
					continue;
				}
				// 5、省份
				if (!existSub(cfg.getProvince(), cfgparam.getProvince())
						&& !cfg.getProvince().equals("#")) {
					// 不匹配则下一条
					continue;
				}
				return cfg.getExInfoMap();
			}
		} catch (Throwable t) {
			logger.error("Here error", t);
		}

		return null;
	}

	/**
	 * @param source
	 *            逗号间隔
	 * @param key
	 * @return
	 */
	public static boolean existSub(String source, String key) {
		if (source == null || StringUtils.isBlank(key)) {
			return false;
		}
		String[] strs = source.split(",");
		Set<String> set = new HashSet<String>();
		for (String str : strs) {
			set.add(str);
		}
		return set.contains(key);
	}
	
	public static List<CfgparamVO> cover(List<Cfgparam> list){
		List<CfgparamVO> cfgs = new ArrayList<CfgparamVO>();
		for (Cfgparam cfg : list) {
			cfgs.add(new CfgparamVO(cfg));
		}
		return cfgs;
	}
}
