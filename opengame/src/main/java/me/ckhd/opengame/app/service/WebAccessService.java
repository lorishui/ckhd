/*
 * 
 */
package me.ckhd.opengame.app.service;

import java.util.List;
import java.util.Map;

import me.ckhd.opengame.app.dao.WebAccessDao;
import me.ckhd.opengame.app.entity.WebAccess;
import me.ckhd.opengame.common.service.CrudService;

import org.springframework.stereotype.Service;

/**
 * @author qibiao
 */
@Service
public class WebAccessService extends CrudService<WebAccessDao, WebAccess> {
	
	public List<Map<String, Object>> statsWebAccessNum(WebAccess webAccess){
		return dao.statsWebAccessNum(webAccess);
	}
	
}
