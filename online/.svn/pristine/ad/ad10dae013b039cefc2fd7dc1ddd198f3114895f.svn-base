package me.ckhd.opengame.app.service;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.app.dao.CfgparamDao;
import me.ckhd.opengame.app.entity.Cfgparam;
import me.ckhd.opengame.common.utils.CacheUtils;
import me.ckhd.opengame.common.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 咪咕扩展SDK参数配置
 * 
 * @author qibiao
 *
 */
@Service("AndextendCfgService")
public class AndextendCfgService implements CfgService {

	public final static String KEY_NAME = "andextend";
	
	public Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CfgparamDao cfgparamDao;

	/**
	 * 当然使用哪个sdk，以及二次支付次数，一次支付的次数
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCfg(Cfgparam cfgparam) {
		Map<String, Object> result = null;
		try{
			if( StringUtils.isNoneBlank(cfgparam.getImsi())  ){
				log.info("black list imsi="+cfgparam.getImsi());
				result = new HashMap<String, Object>();
				result.put("resultCode", "-2");
				result.put("errMsg", "参数滤过！");
				return result;
			}
		}catch(Exception e){
			log.error("加载so文件出错！", e);
		}
		
//		if(cfgparam.getMmAppId() == null){
//			cfgparam.setMmAppId("");
//		}
		
//		String simNO = cfgparam.getSimNO();
//		if( simNO.length() > 10){
//			cfgparam.setProvince(simNO.substring(8, 10));
//		}
		
		String key = KEY_NAME + "_" + cfgparam.getCkAppId() + "_"
				+ cfgparam.getMmAppId() + "_" + cfgparam.getCkChannelId() + "_"
				+ cfgparam.getProvince() + "_" + cfgparam.getVersionName();
		result = (Map<String, Object>) CacheUtils.get(key);
		if (result == null) {
			Cfgparam vo = cfgparamDao.findAndextend(cfgparam);
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
