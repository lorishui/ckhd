package me.ckhd.opengame.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.ckhd.opengame.app.dao.AppVersionDao;
import me.ckhd.opengame.app.entity.AppVersion;
import me.ckhd.opengame.common.service.CrudService;

@Service
@Transactional(readOnly = true)
public class AppVersionService extends CrudService<AppVersionDao, AppVersion> {
	
	public List<AppVersion> getAppVersionAll(AppVersion appVersion){
		return dao.getAppVersionAll(appVersion);
	}
	public List<Map<String,String>> appVersionList(AppVersion appVersion){
		return dao.appVersionList(appVersion);
	}
	@Transactional(readOnly = false)
	public void update(AppVersion appVersion){
		dao.update(appVersion);
	}
}
