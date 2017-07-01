package me.ckhd.opengame.app.service;

import me.ckhd.opengame.app.dao.AppEventDao;
import me.ckhd.opengame.app.entity.AppEvent;
import me.ckhd.opengame.common.service.CrudService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppEventService extends CrudService<AppEventDao, AppEvent>{
	
	@Autowired
	private AppEventDao eventDao;
	
	@Transactional(readOnly = false)
	public void saveEvent(AppEvent event) {
		event.preInsert();
		eventDao.insert(event);
	}
}