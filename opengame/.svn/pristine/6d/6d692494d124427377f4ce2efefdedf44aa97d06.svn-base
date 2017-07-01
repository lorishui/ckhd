 package me.ckhd.opengame.app.utils;

import java.util.List;
import java.util.Map;

import me.ckhd.opengame.app.dao.ProvinceDao;
import me.ckhd.opengame.app.entity.Province;
import me.ckhd.opengame.common.utils.CacheUtils;
import me.ckhd.opengame.common.utils.SpringContextHolder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
public class ProvinceUtils {
	
	private static ProvinceDao provinceDao = SpringContextHolder.getBean(ProvinceDao.class);
	public static final String CACHE_PROVINCE_MAP = "provinceMap";
	
	public static List<Province> getProvinceList(){
		@SuppressWarnings("unchecked")
		Map<String, List<Province>> provinceMap = (Map<String, List<Province>>)CacheUtils.get(CACHE_PROVINCE_MAP);
		if (provinceMap==null){
			provinceMap = Maps.newHashMap();
			for (Province province : provinceDao.findAllList(new Province())){
				List<Province> provinceList = provinceMap.get("ALL");
				if (provinceList != null){
					provinceList.add(province);
				}else{
					provinceMap.put("ALL", Lists.newArrayList(province));
				}
			}
			CacheUtils.put(CACHE_PROVINCE_MAP, provinceMap);
		}
		List<Province> provinceList = provinceMap.get("ALL");
		if (provinceList == null){
			provinceList = Lists.newArrayList();
		}
		return provinceList;
	}
	
}
