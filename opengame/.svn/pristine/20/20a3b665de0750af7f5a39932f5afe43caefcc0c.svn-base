package me.ckhd.opengame.stats.dao;

import java.util.List;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.stats.entity.NewUsersCount;

import org.springframework.stereotype.Repository;

@Repository
public interface NewUsersCountDao extends CrudDao<NewUsersCount> {
	public List<NewUsersCount> statNew(NewUsersCount newUsersCount);
	public List<NewUsersCount> statAct(NewUsersCount newUsersCount);
}