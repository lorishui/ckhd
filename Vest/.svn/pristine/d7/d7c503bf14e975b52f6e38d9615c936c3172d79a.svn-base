package me.ckhd.opengame.app.service;

import java.util.Map;

import me.ckhd.opengame.app.dao.AppPayListDao;
import me.ckhd.opengame.app.entity.AppPayList;
import me.ckhd.opengame.common.service.CrudService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AppPayListService extends  CrudService<AppPayListDao, AppPayList>{
	public Map<String,Object> findOne(AppPayList app){
		return dao.findOne(app);
	}
}
