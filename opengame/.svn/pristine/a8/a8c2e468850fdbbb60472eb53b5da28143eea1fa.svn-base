package me.ckhd.opengame.adpush.service;

import java.util.List;
import java.util.Map;

import me.ckhd.opengame.adpush.dao.AdPushDao;
import me.ckhd.opengame.adpush.dao.AdPushDetailDao;
import me.ckhd.opengame.adpush.entity.AdPush;
import me.ckhd.opengame.adpush.entity.AdPushDetail;
import me.ckhd.opengame.common.service.CrudService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdPushService extends CrudService<AdPushDao, AdPush> {
	@Autowired
	private AdPushDao adPushDao;
	@Autowired
	private AdPushDetailDao adPushDetailDao;

	public List<Map<String, String>> getAllGames() {
		return adPushDao.getAllGames();
	}

	public List<Map<String, String>> getAllMedia() {
		return adPushDao.getAllMedia();
	}
	
	@Override
	public void delete(AdPush entity) {
		super.delete(entity);
		adPushDetailDao.deleteByAdPushId(entity.getId());
	}
}
