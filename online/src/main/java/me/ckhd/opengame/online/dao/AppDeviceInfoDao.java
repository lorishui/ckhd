package me.ckhd.opengame.online.dao;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.online.entity.AppDeviceInfo;

import org.springframework.stereotype.Repository;

@Repository
public interface AppDeviceInfoDao extends CrudDao<AppDeviceInfo>{
	
	public Integer isExist(AppDeviceInfo appDeviceInfo);
	
	public int updateBuyFlow(AppDeviceInfo appDeviceInfo);
	
}
