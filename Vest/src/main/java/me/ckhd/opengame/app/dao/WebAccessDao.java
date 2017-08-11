/*
 * 
 */
package me.ckhd.opengame.app.dao;

import java.util.List;
import java.util.Map;

import me.ckhd.opengame.app.entity.WebAccess;
import me.ckhd.opengame.common.persistence.CrudDao;

import org.springframework.stereotype.Repository;

/**
 * @author qibiao
 */
@Repository
public interface WebAccessDao extends CrudDao<WebAccess> {

	public List<Map<String, Object>> statsWebAccessNum(WebAccess webAccess);
	
}
