/**
 * Copyright &copy; 2015-2018 <a href="http://www.ckhd.me/">创酷互动</a> All rights reserved.
 */
package me.ckhd.opengame.sys.dao;

import java.util.List;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.sys.entity.Resource;

import org.springframework.stereotype.Repository;

/**
 * 菜单DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@Repository
public interface ResourceDao extends CrudDao<Resource> {

	public List<Resource> findByParentIdsLike(Resource resource);

	public List<Resource> findByUserId(Resource resource);
	
	public int updateParentIds(Resource resource);
	
	public int updateSort(Resource resource);
	
}
