package me.ckhd.opengame.sys.utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.ckhd.opengame.common.utils.CacheUtils;
import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.ipip.IPUtils;
import me.ckhd.opengame.sys.dao.DictDao;
import me.ckhd.opengame.sys.entity.Dict;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
public class DictUtils {
	
	private static DictDao dictDao = SpringContextHolder.getBean(DictDao.class);

	public static final String CACHE_DICT_MAP = "dictMap";
	
	public static final String CACHE_OPENGAME_TRUST_SIGNMD5_SET = "OPENGAME_TRUST_SIGNMD5_SET";
	
	public static final String CACHE_OPENGAME_ICCID_WHITE_LIST_SET = "OPENGAME_ICCID_WHITE_LIST_SET";
	
	public static final String CACHE_OPENGAME_IMSI_WHITE_LIST_SET = "OPENGAME_IMSI_WHITE_LIST_SET";
	
	public static final String CACHE_OPENGAME_CONTROL_AREA_SET = "CACHE_OPENGAME_CONTROL_AREA_SET";
	
	public static final String CACHE_OPENGAME_GREEN_AREA_SET = "OPENGAME_GREEN_AREA_SET";
	
	public static final String CACHE_OPENGAME_GREEN_AREA_AD_SET = "OPENGAME_GREEN_AREA_AD_SET";
	
	public static final String CACHE_OPENGAME_YELLOW_AREA_SET = "OPENGAME_YELLOW_AREA_SET";
	
	public static String getDictLabel(String value, String type, String defaultValue){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)){
			for (Dict dict : getDictList(type)){
				if (type.equals(dict.getType()) && value.equals(dict.getValue())){
					return dict.getLabel();
				}
			}
		}
		return defaultValue;
	}
	
	public static String getDictLabels(String values, String type, String defaultValue){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(values)){
			List<String> valueList = Lists.newArrayList();
			for (String value : StringUtils.split(values, ",")){
				valueList.add(getDictLabel(value, type, defaultValue));
			}
			return StringUtils.join(valueList, ",");
		}
		return defaultValue;
	}

	public static String getDictValue(String label, String type, String defaultLabel){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)){
			for (Dict dict : getDictList(type)){
				if (type.equals(dict.getType()) && label.equals(dict.getLabel())){
					return dict.getValue();
				}
			}
		}
		return defaultLabel;
	}
	
	public static List<Dict> getDictList(String type){
		@SuppressWarnings("unchecked")
		Map<String, List<Dict>> dictMap = (Map<String, List<Dict>>)CacheUtils.get(CACHE_DICT_MAP);
		if (dictMap==null){
			dictMap = Maps.newHashMap();
			for (Dict dict : dictDao.findAllList(new Dict())){
				List<Dict> dictList = dictMap.get(dict.getType());
				if (dictList != null){
					dictList.add(dict);
				}else{
					dictMap.put(dict.getType(), Lists.newArrayList(dict));
				}
			}
			CacheUtils.put(CACHE_DICT_MAP, dictMap);
		}
		List<Dict> dictList = dictMap.get(type);
		if (dictList == null){
			dictList = Lists.newArrayList();
		}
		return dictList;
	}
	
	public static List<Dict> getDictProvince(String type){
		@SuppressWarnings("unchecked")
		Map<String, List<Dict>> dictMap = (Map<String, List<Dict>>)CacheUtils.get(CACHE_DICT_MAP);
		if (dictMap==null){
			dictMap = Maps.newHashMap();
			for (Dict dict : dictDao.findAllList(new Dict())){
				List<Dict> dictList = dictMap.get(dict.getType());
				if (dictList != null){
					dictList.add(dict);
				}else{
					dictMap.put(dict.getType(), Lists.newArrayList(dict));
				}
			}
			CacheUtils.put(CACHE_DICT_MAP, dictMap);
		}
		List<Dict> dictList = dictMap.get(type);
		if (dictList == null){
			dictList = Lists.newArrayList();
		}
		if(dictList != null && dictList.size()>0){
			for(int i=0; i<dictList.size();i++){
				if(!"default".equals(dictList.get(i).getValue()) && !"未知省份".equals(dictList.get(i).getLabel())){
					if(i==dictList.size()-1){
						Dict dic = new Dict();
						dic.setValue("default");
						dic.setLabel("未知省份");
						dictList.add(dic);
						break;
					}
				}
			}
		}
		return dictList;
	}
	/**
	 * 根据支付value查找支付名称
	 * @author yong
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static String findLabel(String value, String defaultValue){
		String label = (String) CacheUtils.get("VALUE_" + value);
		if(label == null){
			if (StringUtils.isNotBlank(value) ){
				label = dictDao.getLabel(value);
				CacheUtils.put("VALUE_" + value, label);
				return label;
			}
		}else{
			return label;
		}
		
		return defaultValue;
	}
	
	@SuppressWarnings("unchecked")
	public static boolean isTrustSignMd5(String signMd5){
		if(StringUtils.isBlank(signMd5)){
			return false;
		}
		signMd5 = signMd5.trim().toUpperCase();
		Set<String> signMd5set = (Set<String>) CacheUtils.get(CACHE_OPENGAME_TRUST_SIGNMD5_SET);
		if(signMd5set == null){
			Dict dict = new Dict();
			// trust_sign信任的签名
			dict.setType("trust_sign");
			List<Dict> dicts = dictDao.findList(dict);
			signMd5set = new HashSet<String>();
			for(Dict vo: dicts){
				signMd5set.add(vo.getLabel());
			}
			CacheUtils.put(CACHE_OPENGAME_TRUST_SIGNMD5_SET, signMd5set);
		}
		return signMd5set.contains(signMd5);
	}
	
	/**
	 * 是不是在ICCID的白名单中
	 * @param iccid
	 * @return
	 */
	public static boolean isIccidWhiteList(String iccid){
		@SuppressWarnings("unchecked")
		Set<String> iccidSet = (Set<String>) CacheUtils.get(CACHE_OPENGAME_ICCID_WHITE_LIST_SET);
		if(iccidSet == null){
			Dict dict = new Dict();
			// trust_sign信任的签名
			dict.setType("iccid_white_list");
			List<Dict> dicts = dictDao.findList(dict);
			iccidSet = new HashSet<String>();
			for(Dict vo: dicts){
				iccidSet.add(vo.getLabel());
			}
			CacheUtils.put(CACHE_OPENGAME_ICCID_WHITE_LIST_SET, iccidSet);
		}
		return iccidSet.contains(iccid);
	}
	
	/**
	 * 是不是在ICCID的白名单中
	 * @param iccid
	 * @return
	 */
	public static boolean isImsiWhiteList(String imsi){
		if(StringUtils.isNotBlank(imsi)){
			@SuppressWarnings("unchecked")
			Set<String> imsiSet = (Set<String>) CacheUtils.get(CACHE_OPENGAME_IMSI_WHITE_LIST_SET);
			if(imsiSet == null){
				Dict dict = new Dict();
				// trust_sign信任的签名
				dict.setType("imsi_white_list");
				List<Dict> dicts = dictDao.findList(dict);
				imsiSet = new HashSet<String>();
				for(Dict vo: dicts){
					imsiSet.add(vo.getLabel());
				}
				CacheUtils.put(CACHE_OPENGAME_IMSI_WHITE_LIST_SET, imsiSet);
			}
			return imsiSet.contains(imsi);
		}else{
			return false;
		}
	}
	
	/**
	 * 是不是管控区域
	 * @param ip
	 * @return
	 */
	public static boolean isControlArea(String ip){
		String address = IPUtils.getAddress(ip);
		boolean is = false;
		if(address != null){
			@SuppressWarnings("unchecked")
			Set<String> area = (Set<String>) CacheUtils.get(CACHE_OPENGAME_CONTROL_AREA_SET);
			if(area == null){
				Dict dict = new Dict();
				// trust_sign信任的签名
				dict.setType("control_area");
				List<Dict> dicts = dictDao.findList(dict);
				area = new HashSet<String>();
				for(Dict vo: dicts){
					area.add(vo.getLabel());
					if(address.indexOf(vo.getLabel()) != -1){
						is = true;
					}
				}
				CacheUtils.put(CACHE_OPENGAME_CONTROL_AREA_SET, area);
			}else{
				Iterator<String> it = area.iterator();
				while(it.hasNext()){
					if(address.indexOf(it.next()) != -1){
						is = true;
					}
				}
			}
		}
		return is;
	}
	
	public static int getFilterRate(String ckappid){
		String rate = getDictValue(ckappid, "filter_rate", "80");
		return Integer.parseInt(rate);
	}
	
	public static boolean getCellinfoSwitch(){
		String rate = getDictValue("switch", "cellinfo_switch", "false");
		return Boolean.parseBoolean(rate);
	}
	
	public static boolean getNet4IpSwitch(){
		String rate = getDictValue("switch", "net4ip_switch", "false");
		return Boolean.parseBoolean(rate);
	}
	
	/**
	 * 绿区
	 * @param imei
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isGreenArea(String imei){
		if(StringUtils.isBlank(imei)){
			return false;
		}
		imei = imei.trim().toUpperCase();
		Set<String> greenAreaSet = (Set<String>) CacheUtils.get(CACHE_OPENGAME_GREEN_AREA_SET);
		if(greenAreaSet == null){
			Dict dict = new Dict();
			// trust_sign信任的签名
			dict.setType("green_area");
			List<Dict> dicts = dictDao.findList(dict);
			greenAreaSet = new HashSet<String>();
			for(Dict vo: dicts){
				greenAreaSet.add(vo.getLabel());
			}
			CacheUtils.put(CACHE_OPENGAME_GREEN_AREA_SET, greenAreaSet);
		}
		return greenAreaSet.contains(imei);
	}
	
	@SuppressWarnings("unchecked")
	public static boolean isGreenAreaAd(String imei){
		if(StringUtils.isBlank(imei)){
			return false;
		}
		imei = imei.trim().toUpperCase();
		Set<String> greenAreaAdSet = (Set<String>) CacheUtils.get(CACHE_OPENGAME_GREEN_AREA_AD_SET);
		if(greenAreaAdSet == null){
			Dict dict = new Dict();
			// trust_sign信任的签名
			dict.setType("green_area_ad");
			List<Dict> dicts = dictDao.findList(dict);
			greenAreaAdSet = new HashSet<String>();
			for(Dict vo: dicts){
				greenAreaAdSet.add(vo.getLabel());
			}
			CacheUtils.put(CACHE_OPENGAME_GREEN_AREA_AD_SET, greenAreaAdSet);
		}
		return greenAreaAdSet.contains(imei);
	}
	
	/**
	 * 黄区
	 * @param imei
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isYellowArea(String imei){
		if(StringUtils.isBlank(imei)){
			return false;
		}
		imei = imei.trim().toUpperCase();
		Set<String> yellowAreaSet = (Set<String>) CacheUtils.get(CACHE_OPENGAME_YELLOW_AREA_SET);
		if(yellowAreaSet == null){
			Dict dict = new Dict();

			dict.setType("yellow_area");
			List<Dict> dicts = dictDao.findList(dict);
			yellowAreaSet = new HashSet<String>();
			for(Dict vo: dicts){
				yellowAreaSet.add(vo.getLabel());
			}
			CacheUtils.put(CACHE_OPENGAME_YELLOW_AREA_SET, yellowAreaSet);
		}
		return yellowAreaSet.contains(imei);
	}
	
	/**
	 * @param phoneModel
	 * @param type = green or type = yellow
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isTypePhoneModel(String phoneModel, String type){
		if(StringUtils.isBlank(phoneModel)){
			return false;
		}

		Set<String> set = (Set<String>) CacheUtils.get(type.toUpperCase());
		if(set == null){
			Dict dict = new Dict();
			// trust_sign信任的签名
			dict.setType(type);
			List<Dict> dicts = dictDao.findList(dict);
			set = new HashSet<String>();
			for(Dict vo: dicts){
				set.add(vo.getLabel());
			}
			CacheUtils.put(type.toUpperCase(), set);
		}
		return set.contains(phoneModel);
	}
	
	public static boolean isGreenPhoneModel(String phoneModel){
		return isTypePhoneModel(phoneModel, "phone_model_green_type");
	}
	
	public static boolean isYellowPhoneModel(String phoneModel){
		return isTypePhoneModel(phoneModel, "phone_model_yellow_type");
	}
}