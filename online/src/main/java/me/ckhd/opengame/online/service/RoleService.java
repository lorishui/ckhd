package me.ckhd.opengame.online.service;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.online.dao.RoleDao;
import me.ckhd.opengame.online.entity.RoleInfo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("RoleInfoService")
@Transactional(readOnly = true)
public class RoleService extends CrudService<RoleDao, RoleInfo>{
	
	@Transactional(readOnly = false)
	public int update(RoleInfo role){
		return this.dao.update(role);
	}
	
	@Transactional(readOnly = false)
	public int insert(RoleInfo role){
		if(isExist(role)){
			return this.dao.update(role);
		}
		return this.dao.insert(role);
	}
	
	@Transactional(readOnly = false)
	public int insertEvent(RoleInfo role){
		return this.dao.insertEvent(role);
	}
	
	public boolean isExist(RoleInfo roleInfo){
		int n = this.dao.isExist(roleInfo);
		if(n == 0){
			return false;
		}
		return true;
	}
}
