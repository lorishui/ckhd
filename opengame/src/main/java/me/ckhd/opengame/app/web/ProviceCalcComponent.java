/*
 * 
 */
package me.ckhd.opengame.app.web;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.app.entity.BaseStation;
import me.ckhd.opengame.app.entity.CfgZoneLevel;
import me.ckhd.opengame.app.entity.Cfgparam;
import me.ckhd.opengame.app.service.BaseStationService;
import me.ckhd.opengame.app.service.CfgZoneLevelService;
import me.ckhd.opengame.app.utils.IccidUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.ipip.IPUtils;
import me.ckhd.opengame.sys.utils.DictUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @author qibiao
 *
 */
@Component
public class ProviceCalcComponent {

	private Logger logger = LoggerFactory.getLogger(CfgparamController.class); 

	// 严格省份：广东19，上海09，北京01，海南21，江苏10，黑龙江08
	public static Set<String> STRICT_PROVICES = new HashSet<String>();

	// 计数
	public static ConcurrentMap<String, AtomicLong> statsMap = new ConcurrentHashMap<String, AtomicLong>();
	
	public static String OLD_VERSION = "OLD_VERSION";
	
	public static String NEW_VERSION_NODATA = "NEW_VERSION_NODATA";
	
	public static String NEW_VERSION_NOHIT = "NEW_VERSION_NOHIT";
	
	public static String NEW_VERSION_HIT = "NEW_VERSION_HIT";
	
	static {
		STRICT_PROVICES.add("19");
		STRICT_PROVICES.add("09");
		STRICT_PROVICES.add("01");
		STRICT_PROVICES.add("21");
		STRICT_PROVICES.add("10");
		STRICT_PROVICES.add("08");
		
		statsMap.put(OLD_VERSION, new AtomicLong(0));
		statsMap.put(NEW_VERSION_NODATA, new AtomicLong(0));
		statsMap.put(NEW_VERSION_NOHIT, new AtomicLong(0));
		statsMap.put(NEW_VERSION_HIT, new AtomicLong(0));
	}
	
	/**
	 * 计算省份级别代码
	 * @param cfgparam
	 * @param request
	 */
	public String calcProvice(Cfgparam cfgparam, String biztype, HttpServletRequest request){
		
		String simNO = cfgparam.getSimNO();
		String carriers = cfgparam.getCarriers();
		logger.debug("进入calcProvice方法");
		// 基站版本
		boolean cellinfoVersionSwitch = DictUtils.getCellinfoSwitch() && (cfgparam.getMcc() != 460);
		// 省份，基站
		if(cfgparam.getMcc() == -100 || cellinfoVersionSwitch){
			logger.debug("进入calcProvice方法分支1");
			// 统计
			if(cfgparam.getMcc() == -100){
				statsMap.get(OLD_VERSION).incrementAndGet();
			} else {
				statsMap.get(NEW_VERSION_NODATA).incrementAndGet();
			}
			
			// -100是旧版本,或者没有数据的新版本开启了iccid，ip的规则
			String provinceCode = null;
			String ipProvince = IPUtils.getCityCode(StringUtils.getRemoteAddr(request));
			if(StringUtils.isNotBlank(simNO) && simNO.trim().length() >=10 && StringUtils.isNotBlank(carriers)){
				// ICCID省份
				provinceCode = IccidUtils.getProvinceCodeBySimNO(simNO.trim(), carriers);
				provinceCode = IccidUtils.getCmccProvinceCode(provinceCode,carriers);
			}
			if(provinceCode == null || "99".equals(provinceCode)){
				// IP省份
				provinceCode = ipProvince;
			}
			logger.debug("进入calcProvice方法分支1-2，provinceCode=" + provinceCode);
			if(provinceCode == null || STRICT_PROVICES.contains(provinceCode) || STRICT_PROVICES.contains(ipProvince)
					|| ("10".equals(cfgparam.getCkChannelId()) && ("13".equals(provinceCode) || "13".equals(ipProvince)))){
				// 红区
				cfgparam.setProvince("90");
			} else {
				// 白区
				cfgparam.setProvince("70");
			}
		}else if(cfgparam.getMcc() != 460){
			logger.debug("进入calcProvice方法分支2");
			statsMap.get(NEW_VERSION_NODATA).incrementAndGet();
			
			// 新版本数据没有获取到,红区
			cfgparam.setProvince("90");
		}else{
			// 根据数据查询数据，解析红白区
			try {
				logger.debug("进入calcProvice方法分支3");
				// 方法调用，返回红黄白区
				CfgZoneLevel cfgZoneLevel = new CfgZoneLevel();
				cfgZoneLevel.setCkappid(cfgparam.getCkAppId());
				cfgZoneLevel.setChannelid(cfgparam.getCkChannelId());
				cfgZoneLevel.setBiztype(biztype);
				
				BaseStation baseStation = new BaseStation();
				baseStation.setMnc(cfgparam.getMnc());
				baseStation.setLac(cfgparam.getLac());
				baseStation.setCi(cfgparam.getCi());
				
				String zoneCode = baseStationService.getBaseStation(baseStation);
				logger.debug("进入calcProvice方法分支3，zoneCode=" + zoneCode);
				if(StringUtils.isBlank(zoneCode) || "10000".equals(zoneCode)){
					
					// 统计
					statsMap.get(NEW_VERSION_NOHIT).incrementAndGet();
					
					cfgparam.setProvince("90");
				} else {
					
					// 统计
					statsMap.get(NEW_VERSION_HIT).incrementAndGet();
					
					String redZoneList = cfgZoneLevelService.getCacheRedCfg(cfgZoneLevel);
					logger.debug("进入calcProvice方法分支3，redZoneList=" + redZoneList);
					if(redZoneList.indexOf("," + zoneCode + ",") >= 0){
						cfgparam.setProvince("90");
					} else {
						String yellowZoneList = cfgZoneLevelService.getCacheYellowCfg(cfgZoneLevel);
						logger.debug("进入calcProvice方法分支3，yellowZoneList=" + yellowZoneList);
						if(yellowZoneList.indexOf("," + zoneCode + ",") >= 0){
							cfgparam.setProvince("80");
						}else{
							cfgparam.setProvince("70");
						}
					}
				}
				
//				boolean isRedZone = baseStationService.isHit(cfgZoneLevelService.getCacheRedCfg(cfgZoneLevel), baseStation);
//				boolean isYellowZone = false;
//				if(isRedZone){
//					cfgparam.setProvince("90");
//				}else{
//					isYellowZone = baseStationService.isHit(cfgZoneLevelService.getCacheYellowCfg(cfgZoneLevel), baseStation);
//					if(isYellowZone){
//						cfgparam.setProvince("80");
//					}
//				}
//				if(!isRedZone && !isYellowZone){
//					cfgparam.setProvince("70");
//				}
			} catch (Exception e) {
				cfgparam.setProvince("90");
				logger.error("cellinfo process error, please check it!!!", e);
			}
		}
		return cfgparam.getProvince();
	}

	@Autowired
	private CfgZoneLevelService cfgZoneLevelService;
	
	@Autowired
	private BaseStationService baseStationService;

	public CfgZoneLevelService getCfgZoneLevelService() {
		return cfgZoneLevelService;
	}

	public void setCfgZoneLevelService(CfgZoneLevelService cfgZoneLevelService) {
		this.cfgZoneLevelService = cfgZoneLevelService;
	}

	public BaseStationService getBaseStationService() {
		return baseStationService;
	}

	public void setBaseStationService(BaseStationService baseStationService) {
		this.baseStationService = baseStationService;
	}
	
}
