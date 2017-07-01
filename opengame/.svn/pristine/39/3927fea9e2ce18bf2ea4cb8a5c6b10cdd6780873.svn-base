package me.ckhd.opengame.stats.dao;

import java.util.List;
import java.util.Map;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.stats.entity.OfflineMoneyCount;

import org.springframework.stereotype.Repository;

@Repository
public interface OfflineMoneyCountDao extends CrudDao<OfflineMoneyCount> {
	public List<OfflineMoneyCount> stat(OfflineMoneyCount offlineMoneyCount);
	public List<OfflineMoneyCount> statHour(OfflineMoneyCount offlineMoneyCount);
	public List<OfflineMoneyCount> statMoeny(OfflineMoneyCount offlineMoneyCount);
	public int saveDatas(Map<String,String> sql);
	public OfflineMoneyCount statTotalMoeny(OfflineMoneyCount offlineMoneyCount);
	public OfflineMoneyCount statTotalMoenyByHour(OfflineMoneyCount offlineMoneyCount);
	/**
	 * 单机金额数据按时间统计金额
	 * @return
	 */
	public List<OfflineMoneyCount> statOfflineMoney(OfflineMoneyCount offlineMoneyCount);
	/**
	 * 单机金额数据按统计总金额
	 * @return
	 */
	public OfflineMoneyCount statTotalOfflineMoney(OfflineMoneyCount offlineMoneyCount);
}
