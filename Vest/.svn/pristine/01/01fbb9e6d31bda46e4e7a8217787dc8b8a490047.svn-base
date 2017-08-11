package me.ckhd.opengame.app.dao;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import me.ckhd.opengame.app.entity.AppVersion;
import me.ckhd.opengame.common.persistence.CrudDao;

@Repository
public interface AppVersionDao extends CrudDao<AppVersion>{
	/**
	 * 根据所传参数获取AppVersion信息
	 * @param appVersion
	 * @return
	 */
	public List<AppVersion> getAppVersionAll(AppVersion appVersion);
	
	public List<Map<String,String>> appVersionList(AppVersion appVersion);
}
