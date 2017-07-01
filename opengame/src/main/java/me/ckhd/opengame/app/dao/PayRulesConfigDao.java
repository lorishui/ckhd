package me.ckhd.opengame.app.dao;

import java.util.List;

import me.ckhd.opengame.app.entity.PayRulesConfig;
import me.ckhd.opengame.common.persistence.CrudDao;

import org.springframework.stereotype.Repository;

@Repository
public interface PayRulesConfigDao extends CrudDao<PayRulesConfig> {
	
	public List<PayRulesConfig> getPayRulesConfigByPayRulesId(PayRulesConfig payRulesConfig);
	
}
