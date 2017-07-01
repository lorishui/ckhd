package me.ckhd.opengame.adpush.service;

import java.util.Date;
import java.util.List;

import me.ckhd.opengame.adpush.dao.AdPushCostDao;
import me.ckhd.opengame.adpush.entity.AdPushCost;
import me.ckhd.opengame.common.service.CrudService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdPushCostService extends CrudService<AdPushCostDao, AdPushCost> {
	@Autowired
	private AdPushCostDao adPushCostDao;

	public List<AdPushCost> findListByAdPushDetailId(String adPushDetailId) {
		return adPushCostDao.findListByAdPushDetailId(adPushDetailId);
	}
	

	public AdPushCost findByDateAndAdPushDetailId(Date date,
			String adPushDetailId) {
		return adPushCostDao.findByDateAndAdPushDetailId(date,adPushDetailId);
	}
}
