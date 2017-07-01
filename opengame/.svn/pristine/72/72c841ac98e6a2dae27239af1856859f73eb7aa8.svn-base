package me.ckhd.opengame.app.service;

import me.ckhd.opengame.app.dao.AppleUserDao;
import me.ckhd.opengame.app.entity.AppleUser;
import me.ckhd.opengame.common.service.CrudService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AppleUserService extends  CrudService<AppleUserDao, AppleUser>{
	
	public int exist(AppleUser apple){
		return this.dao.exist(apple);
	}
	
	@Transactional(readOnly = false)
	public int update(AppleUser apple) {
		return this.dao.update(apple);
	}
	
	public Integer getBadge(AppleUser apple){
		return this.dao.getBadge(apple);
	}
}
