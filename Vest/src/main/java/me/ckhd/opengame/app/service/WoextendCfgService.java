package me.ckhd.opengame.app.service;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.app.dao.CfgparamDao;
import me.ckhd.opengame.app.entity.Cfgparam;
import me.ckhd.opengame.common.utils.CacheUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MM扩展SDK参数配置
 * 
 * @author qibiao
 *
 */
@Service("WoextendCfgService")
public class WoextendCfgService implements CfgService {

	public final static String KEY_NAME = "woextend";
	
	public Logger log = LoggerFactory.getLogger(WoextendCfgService.class);

	@Autowired
	private CfgparamDao cfgparamDao;

	/**
	 * 当然使用哪个sdk，以及二次支付次数，一次支付的次数
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCfg(Cfgparam cfgparam) {

		// 黑名单，以后弄一个文件好了随时更新。
		
		// 请求参数，ckAppId（必填），mmAppId（必填），ckChannelId（必填），versionName（必填），省份（），
		
		// 日时间段 配置项

		// 配置数据，ckAppId（必填），mmAppId（必填 -
		// 这个是指包的mmappid），ckChannelId（有全匹配值，多选），versionName（有全匹配值，多选）==> 配置数据

		// 返回数据 sdkName，oneAckTimes，twoAckTimes

		// key:a_b_c_d

		// SQL:select * from app_cfgparam where ckAppId=? and rqType='1' and
		// mmAppId=? and (ckChannelId like ? or ckChannelId='#') and
		// (versionName like ? or versionName='#') order by
		// ckChannelId,versionName;

		Map<String, Object> result = null;
		
		String key = KEY_NAME + cfgparam.getCkAppId() + "_"
				+ cfgparam.getMmAppId() + "_" + cfgparam.getCkChannelId() + "_"
				+ cfgparam.getProvince() + "_" + cfgparam.getVersionName();
		result = (Map<String, Object>) CacheUtils.get(key);
		if (result == null) {
			Cfgparam vo = cfgparamDao.findWoextend(cfgparam);
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
