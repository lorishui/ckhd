package me.ckhd.opengame.stats.service;

import java.util.List;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.stats.dao.NewUsersCountDao;
import me.ckhd.opengame.stats.entity.NewUsersCount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class NewUsersCountService extends  CrudService<NewUsersCountDao, NewUsersCount>{
	
	@Autowired
	private NewUsersCountDao newUsersCountDao;
	
	public List<NewUsersCount> statNew(NewUsersCount newUsersCount){
		return newUsersCountDao.statNew(newUsersCount);
	}
	
	public List<NewUsersCount> statAct(NewUsersCount newUsersCount){
		return newUsersCountDao.statAct(newUsersCount);
	}
}
