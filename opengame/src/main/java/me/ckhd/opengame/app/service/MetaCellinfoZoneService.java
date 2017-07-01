package me.ckhd.opengame.app.service;

import java.util.List;

import me.ckhd.opengame.app.dao.MetaCellinfoZoneDao;
import me.ckhd.opengame.app.entity.MetaCellinfoZone;
import me.ckhd.opengame.common.service.CrudService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetaCellinfoZoneService extends CrudService<MetaCellinfoZoneDao, MetaCellinfoZone>{

	@Autowired
	public MetaCellinfoZoneDao metaCellinfoZoneDao;
	
	// cache
	public List<MetaCellinfoZone> findAllList() {
		// 两级
		return metaCellinfoZoneDao.findAllList();
		
	}
	
}
