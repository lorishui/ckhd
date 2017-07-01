package me.ckhd.opengame.stats.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.stats.dao.OfflineMoneyCountDao;
import me.ckhd.opengame.stats.entity.OfflineMoneyCount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("OfflineMoneyCountSerivce")
@Transactional(readOnly = true)
public class OfflineMoneyCountSerivce extends  CrudService<OfflineMoneyCountDao, OfflineMoneyCount>{
	
	@Autowired
	private OfflineMoneyCountDao offlineMoneyCountDao;
	
	public List<OfflineMoneyCount> stat(OfflineMoneyCount offlineMoneyCount){
		return offlineMoneyCountDao.stat(offlineMoneyCount);
	}
	
	public List<OfflineMoneyCount> statHour(OfflineMoneyCount offlineMoneyCount){
		return offlineMoneyCountDao.statHour(offlineMoneyCount);
	}
	
	public List<OfflineMoneyCount> statMoeny(OfflineMoneyCount offlineMoneyCount){
		return offlineMoneyCountDao.statMoeny(offlineMoneyCount);
	}
	
	public int saveDatas(String sql){
		Map<String,String> map = new HashMap<String, String>();
		map.put("sql", sql);
		return dao.saveDatas(map);
	}
	
	public OfflineMoneyCount statTotalMoeny(OfflineMoneyCount offlineMoneyCount){
		return offlineMoneyCountDao.statTotalMoeny(offlineMoneyCount);
	}
	
	public OfflineMoneyCount statTotalMoenyByHour(OfflineMoneyCount offlineMoneyCount){
		return offlineMoneyCountDao.statTotalMoenyByHour(offlineMoneyCount);
	}
	
	/**
	 * 单机金额数据按时间统计金额
	 * @return
	 */
	public List<OfflineMoneyCount> statOfflineMoney(OfflineMoneyCount offlineMoneyCount){
		return offlineMoneyCountDao.statOfflineMoney(offlineMoneyCount);
	}
	/**
	 * 单机金额数据按统计总金额
	 * @return
	 */
	public OfflineMoneyCount statTotalOfflineMoney(OfflineMoneyCount offlineMoneyCount){
		return offlineMoneyCountDao.statTotalOfflineMoney(offlineMoneyCount);
	}
}
