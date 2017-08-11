package me.ckhd.opengame.user.service;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.user.dao.RoleDao;
import me.ckhd.opengame.user.entity.RoleInfo;

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
		return this.dao.insert(role);
	}
	
	public boolean isExist(RoleInfo roleInfo){
		int n = this.dao.isExist(roleInfo);
		if(n == 0){
			return false;
		}
		return true;
	}
}
