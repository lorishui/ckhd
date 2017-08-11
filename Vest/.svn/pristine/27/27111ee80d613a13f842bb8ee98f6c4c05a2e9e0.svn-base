/**
 * Copyright &copy; 2015-2018 <a href="http://www.ckhd.me/">创酷互动</a> All rights reserved.
 */
package me.ckhd.opengame.sys.dao;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.sys.entity.Role;

import org.springframework.stereotype.Repository;

/**
 * 角色DAO接口
 * @author ThinkGem
 * @version 2013-12-05
 */
@Repository
public interface RoleDao extends CrudDao<Role> {

	public Role getByName(Role role);
	
	public Role getByEnname(Role role);

	/**
	 * 维护角色与菜单权限关系
	 * @param role
	 * @return
	 */
	public int deleteRoleResource(Role role);

	public int insertRoleResource(Role role);
	
 

}
