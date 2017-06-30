package me.ckhd.opengame.app.dao;

import java.util.Map;

import me.ckhd.opengame.app.entity.AppPayList;
import me.ckhd.opengame.common.persistence.CrudDao;

import org.springframework.stereotype.Repository;

@Repository
public interface AppPayListDao extends  CrudDao<AppPayList>{
	public Map<String,Object> findOne(AppPayList app);
}
