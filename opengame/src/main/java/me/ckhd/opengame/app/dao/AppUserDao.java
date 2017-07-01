package me.ckhd.opengame.app.dao;

import org.springframework.stereotype.Repository;

import me.ckhd.opengame.app.entity.AppUser;
import me.ckhd.opengame.common.persistence.CrudDao;



@Repository
public interface AppUserDao extends CrudDao<AppUser> {

	/** 根据imsi查询 ：验证是否新增用户**/
	boolean  isExistsImsi(String imsi) ;
	/** 根据imei查询 ：验证是否新增用户**/
	boolean  isExistsImei(String imei) ;
}
