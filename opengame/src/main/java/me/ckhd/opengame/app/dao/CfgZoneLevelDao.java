package me.ckhd.opengame.app.dao;

import java.util.List;

import me.ckhd.opengame.app.entity.CfgZoneLevel;
import me.ckhd.opengame.common.persistence.CrudDao;

import org.springframework.stereotype.Repository;

@Repository
public interface CfgZoneLevelDao extends CrudDao<CfgZoneLevel>{

	public int checkExist(CfgZoneLevel vo);
	
	public	void updateRedzone(CfgZoneLevel vo);
	
	public	void updateYellowzone(CfgZoneLevel vo);
	
	public	List<CfgZoneLevel> findCfg(CfgZoneLevel vo);
	
}
