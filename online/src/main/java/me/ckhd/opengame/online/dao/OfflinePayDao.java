package me.ckhd.opengame.online.dao;

import java.util.Map;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.online.entity.OfflinePay;

import org.springframework.stereotype.Repository;

@Repository
public interface OfflinePayDao extends CrudDao<OfflinePay>{
	public Integer getCountByImsiAndTime(Map<String,Object> map);
	public Integer getCountByIccidAndTime(Map<String,Object> map);
}
