package me.ckhd.opengame.adpush.dao;

import java.util.List;
import java.util.Map;

import me.ckhd.opengame.adpush.entity.AdPush;
import me.ckhd.opengame.common.persistence.CrudDao;

import org.springframework.stereotype.Repository;

@Repository
public interface AdPushDao extends CrudDao<AdPush> {

	List<Map<String, String>> getAllGames();

	List<Map<String, String>> getAllMedia();

}
