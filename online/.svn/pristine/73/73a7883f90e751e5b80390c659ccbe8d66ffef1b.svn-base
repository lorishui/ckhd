/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package me.ckhd.opengame.sys.dao;

import java.util.List;
import java.util.Map;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.sys.entity.Dict;

import org.springframework.stereotype.Repository;

 

/**
 * 字典DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@Repository
public interface DictDao extends CrudDao<Dict> {

	public List<String> findTypeList(Dict dict);
	
	public String getLabel(String value);
	
	public List<Map<String,String>> getList(Dict dict);
}
