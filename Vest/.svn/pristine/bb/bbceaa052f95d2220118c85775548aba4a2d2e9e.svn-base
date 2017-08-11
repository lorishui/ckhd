package me.ckhd.opengame.online.service;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.online.dao.AppDeviceInfoDao;
import me.ckhd.opengame.online.entity.AppDeviceInfo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AppDeviceInfoService extends CrudService<AppDeviceInfoDao, AppDeviceInfo>{
	
	@Transactional(readOnly = false)
	public int update(AppDeviceInfo appDeviceInfo){
		return this.dao.update(appDeviceInfo);
	}
	
	@Transactional(readOnly = false)
	public int insert(AppDeviceInfo appDeviceInfo){
		return this.dao.insert(appDeviceInfo);
	}
	
	@Transactional(readOnly = false)
	public int updateBuyFlow(AppDeviceInfo appDeviceInfo){
		return this.dao.updateBuyFlow(appDeviceInfo);
	}
	
	public boolean isExist(AppDeviceInfo appDeviceInfo){
		int n = this.dao.isExist(appDeviceInfo);
		if(n == 0){
			return false;
		}
		return true;
	}
}
