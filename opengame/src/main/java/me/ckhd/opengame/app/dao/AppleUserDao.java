package me.ckhd.opengame.app.dao;

import org.springframework.stereotype.Repository;

import me.ckhd.opengame.app.entity.AppleUser;
import me.ckhd.opengame.common.persistence.CrudDao;

@Repository
public interface AppleUserDao extends  CrudDao<AppleUser>{
	public int exist(AppleUser apple);
	public Integer getBadge(AppleUser apple);
}
