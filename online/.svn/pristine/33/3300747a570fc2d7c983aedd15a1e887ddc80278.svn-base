package me.ckhd.opengame.app.dao;

import java.util.List;

import me.ckhd.opengame.app.entity.PayRules;
import me.ckhd.opengame.common.persistence.CrudDao;

import org.springframework.stereotype.Repository;

@Repository
public interface PayRulesDao extends CrudDao<PayRules> {
	
	public List<PayRules> checkByTime(PayRules payRules);
	
	public PayRules getRulesAndConfig(PayRules payRules);
	
	public void saveInternetPay(PayRules payRules);
	
	public List<PayRules> getRulesAndConfigByTime(PayRules payRules);
	
	public List<PayRules> getAllRulesAndConfigByTime(PayRules payRules);

}
