/*
 * 
 */
package me.ckhd.opengame.app.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.app.dao.BaseStationDao;
import me.ckhd.opengame.app.entity.BaseStation;
import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.common.utils.KeyPrefix;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.user.utils.RedisClientTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BaseStationService extends
		CrudService<BaseStationDao, BaseStation> {

	protected static Logger logger = LoggerFactory
			.getLogger(BaseStationService.class);
	//
	public static Map<String, String> zoneMap = new HashMap<String, String>();

	static {
		String path = Thread.currentThread().getContextClassLoader()
				.getResource("").getPath();
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					path + "/zone.txt"), "UTF-8"));
			String str = null;
			while ((str = br.readLine()) != null) {
				if (!"".equals(str.trim())) {
					int index = str.indexOf(',');
					zoneMap.put(str.substring(0, index),
							str.substring(index + 1));
				}
			}

			logger.debug("BaseStationService debug", zoneMap);
		} catch (Exception e) {
			logger.error("BaseStationService error", e);
		}
	}

	@Autowired
	private RedisClientTemplate redis;

	/**
	 * 算法来源 http://www.cellocation.com/doc-v2/
	 * 
	 * @param baseStation
	 * @return
	 */
	public BaseStation findCellinfoCity(BaseStation baseStation) {

//	    if (mnc == 2 || mnc == 7) mnc = 0;  // mnc=0/2/7 均为移动基站，数据库中统一使用mnc=0查询
//	    if ((mnc == 0 || mnc == 1) && (lac < 40960 || lac == 65534) && ci > 65536 || mnc == 11)
//	        // 4G LTE基站
//	        // 第1次查询，使用指定的mnc，lac，ci
//	        if (result = sql_query("select * from cellinfo_v2 where mnc=%d and lac=%d and ci=%d", mnc, lac, ci)) return result;
//	        // 第2次查询，不指定mnc
//	        if (result = sql_query("select * from cellinfo_v2 where (mnc=0 or mnc=1 or mnc=11) and lac=%d and ci=%d", lac, ci)) return result;
//	        // 第3次查询，不指定lac
//	        if (result = sql_query("select * from cellinfo_v2 where mnc=%d and ci=%d", mnc, ci)) return result;
//	        // 第4次查询，使用EnodeB ID查询，忽略ECI中的低8位
//	        ci_min = ci & 0xffffff00;
//	        ci_max = ci_min + 256;
//	        return sql_query("select * from cellinfo_v2 where mnc=%d and lac=%d and ci>=%d and ci<%d", mnc, lac, ci_min, ci_max);
//	    }
//	    if ((mnc == 0 || mnc == 1) && lac >= 40960 && ci > 65536) {
//	        // 3G UMTS基站
//	        // 第1次查询，使用指定的mnc, lac, ci
//	        if (result = sql_query("select * from cellinfo_v2 where mnc=%d and lac=%d and ci=%d", mnc, lac, ci)) return result;
//	        // 第2次查询，不指定mnc
//	        if (result = sql_query("select * from cellinfo_v2 where (mnc=0 or mnc=1) and lac=%d and ci=%d", lac, ci)) return result;
//	        // 第3次查询，使用（mnc，lac，ci mod 65536）
//	        if (result = sql_query("select * from cellinfo_v2 where mnc=%d and lac=%d and ci=%d", mnc, lac, ci % 65536)) return result;
//	        // 第4次查询，不指定lac
//	        return sql_query("select * from cellinfo_v2 where mnc=%d and ci=%d", mnc, ci);
//	    }
//	    // 2G GSM基站或CDMA基站，使用指定的mnc，lac，ci（sid，nid，bid）
//	    return sql_query("select * from cellinfo_v2 where mnc=%d and lac=%d and ci=%d", mnc, lac, ci);

		
		int mnc = baseStation.getMnc();
		int lac = baseStation.getLac();
		int ci = baseStation.getCi();
		if (mnc == 2 || mnc == 7) {
			mnc = 0;
		}
		if ((mnc == 0 || mnc == 1) && (lac < 40960 || lac == 65534) && ci > 65536 || mnc == 11) {
			//
			int ciMin = ci & 0xffffff00;
			baseStation.setCiMin(ciMin);
			baseStation.setCiMax(ciMin + 256);
			return dao.find4GLTE(baseStation);
		}

		if ((mnc == 0 || mnc == 1) && lac >= 40960 && ci > 65536) {
			baseStation.setCiMod65536(ci % 65536);
			return dao.find3GUMTS(baseStation);
		}

		return dao.find2GGSMOrCDMA(baseStation);
	}

	/**
	 * code
	 * 
	 * @param baseStation
	 * @return
	 */
	public String getBaseStation(BaseStation baseStation) {
		
		long start = System.currentTimeMillis(); 
		
		String key = KeyPrefix.BASE_STATION_KEY + baseStation.getMnc() + "_"
				+ baseStation.getLac() + "_" + baseStation.getCi();
		String value = redis.get(key);

		if (value == null) {
			BaseStation vo = findCellinfoCity(baseStation);
			if (vo == null) {
				value = "10000";
			} else {
				value = zoneMap.get(vo.getCity());
				if (value == null) {
					value = zoneMap.get(vo.getProvince());
				}
				if (value == null) {
					value = "10000";
				}
			}
//			redis.set(key, value);
			redis.set(key, value, 8*60*60);
		}
		
		logger.debug("getBaseStation cost time :" + (System.currentTimeMillis() - start) + "ms");
		return value;
	}

	@Deprecated
	public boolean isHit(String zonelist, BaseStation vo) {

		String zoneCode = getBaseStation(vo);
		// 严格
		if (StringUtils.isBlank(zoneCode) || "10000".equals(zoneCode)) {
			return true;
		}

		return zonelist.indexOf("," + zoneCode + ",") >= 0;
	}
}