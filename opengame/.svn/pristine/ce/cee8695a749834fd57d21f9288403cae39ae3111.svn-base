package me.ckhd.opengame.app.dao;

import java.util.List;
import java.util.Map;

import me.ckhd.opengame.app.entity.Cfgparam;
import me.ckhd.opengame.common.persistence.CrudDao;

import org.springframework.stereotype.Repository;

@Repository
public interface CfgparamDao extends  CrudDao<Cfgparam>{

	public Cfgparam findAd(Cfgparam entity);
	
	@Deprecated
	public Cfgparam findMmpaycode(Cfgparam entity);
	
	public Cfgparam findMmextend(Cfgparam entity);

	public Cfgparam findAndextend(Cfgparam entity);
	
	public Cfgparam findWoextend(Cfgparam entity);
	
	public Cfgparam findEgameextend(Cfgparam entity);
	
	public Cfgparam findIosad(Cfgparam entity);
	
	public Cfgparam findAndroidad(Cfgparam entity);
	
	public Cfgparam findGamePersonal(Cfgparam entity);
	
	public Cfgparam findIOSGamePersonal(Cfgparam entity);
	
	public int bulkInsert(List<Map<String,Object>> list);

	public List<Cfgparam> getExcelData(Cfgparam cfg);
	
	public Cfgparam findProvinceLevel(Cfgparam entity);
	
	public List<Cfgparam> findSdk(Cfgparam entity);
	
	public List<Cfgparam> findCfgType(Cfgparam entity);

	@Deprecated
	public Cfgparam findBlackWhiteCfg(Cfgparam entity);
	
}
