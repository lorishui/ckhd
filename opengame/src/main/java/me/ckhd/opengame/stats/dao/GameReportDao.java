package me.ckhd.opengame.stats.dao;

import org.springframework.stereotype.Repository;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.stats.entity.GameReport;

@Repository
public interface GameReportDao extends CrudDao<GameReport>{

	public GameReport findData(GameReport entity);
	
	public int countTask(GameReport entity);
	
	public void updateSucc(GameReport entity);
	
	public void updateFail(GameReport entity);
	
}
