package me.ckhd.opengame.game.service;

import java.util.Map;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.game.dao.UpdateVersionDao;
import me.ckhd.opengame.game.entity.UpdateVersion;

import org.springframework.stereotype.Service;

@Service
public class UpdateVersionService extends CrudService<UpdateVersionDao, UpdateVersion>{
	public Map<String,Object> getDownUrl(UpdateVersion update){
		return dao.getDownUrl(update);
	}
}
