package me.ckhd.opengame.app.dao;

import java.util.List;
import java.util.Map;

import me.ckhd.opengame.app.entity.QQActivity;
import me.ckhd.opengame.common.persistence.CrudDao;

import org.springframework.stereotype.Repository;

@Repository
public interface QQActivityDao extends  CrudDao<QQActivity>{

	public List<Map<String,Object>> getQQDataByTimeAndCkAppId(Map<String,Object> map);
	public Integer existImsi(Map<String,Object> map);
	public Integer existQQ(Map<String,Object> map);
}
