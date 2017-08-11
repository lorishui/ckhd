package me.ckhd.opengame.online.service;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.online.dao.RoleDao;
import me.ckhd.opengame.online.entity.RoleInfoOnline;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("RoleInfoServiceByOnline")
@Transactional(readOnly = true)
public class RoleService extends CrudService<RoleDao, RoleInfoOnline>{
	
	@Transactional(readOnly = false)
	public int update(RoleInfoOnline role){
		return this.dao.update(role);
	}
	
	@Transactional(readOnly = false)
	public int insert(RoleInfoOnline role){
		if(isExist(role)){
			return this.dao.update(role);
		}
		return this.dao.insert(role);
	}
	
	@Transactional(readOnly = false)
	public int insertEvent(RoleInfoOnline role){
		return this.dao.insertEvent(role);
	}
	
	public boolean isExist(RoleInfoOnline roleInfo){
		int n = this.dao.isExist(roleInfo);
		if(n == 0){
			return false;
		}
		return true;
	}
}
