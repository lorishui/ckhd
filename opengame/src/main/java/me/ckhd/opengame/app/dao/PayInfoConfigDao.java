package me.ckhd.opengame.app.dao;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.persistence.CrudDao;

import org.springframework.stereotype.Repository;

@Repository
public interface PayInfoConfigDao extends CrudDao<PayInfoConfig> {
	public int checkOnly(PayInfoConfig payInfoConfig);
}
