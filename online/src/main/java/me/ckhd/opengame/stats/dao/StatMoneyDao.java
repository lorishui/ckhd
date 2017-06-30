package me.ckhd.opengame.stats.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.stats.entity.StatMoney;

@Repository
public interface StatMoneyDao extends CrudDao<StatMoney> {

	List<StatMoney> statsSuccessMoney(StatMoney statMoney);

	List<StatMoney> statsMoney(StatMoney statMoney);

}
