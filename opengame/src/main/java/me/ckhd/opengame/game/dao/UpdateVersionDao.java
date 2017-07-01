package me.ckhd.opengame.game.dao;

import java.util.Map;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.game.entity.UpdateVersion;

import org.springframework.stereotype.Repository;

@Repository
public interface UpdateVersionDao extends CrudDao<UpdateVersion>{
	public Map<String,Object> getDownUrl(UpdateVersion update);
}
