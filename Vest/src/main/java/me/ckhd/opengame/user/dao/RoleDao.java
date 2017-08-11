package me.ckhd.opengame.user.dao;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.user.entity.RoleInfo;

import org.springframework.stereotype.Repository;

@Repository(value="RoleInfoDao")
public interface RoleDao extends CrudDao<RoleInfo>{
	public Integer isExist(RoleInfo roleInfo);
}
