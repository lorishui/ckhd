package me.ckhd.opengame.app.service;

import java.util.List;
import java.util.Map;

import me.ckhd.opengame.app.dao.CfgparamDao;
import me.ckhd.opengame.app.entity.Cfgparam;
import me.ckhd.opengame.common.service.CrudService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("CfgparamService")
public class CfgparamService extends CrudService<CfgparamDao, Cfgparam>{
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int bulkInsert(List<Map<String,Object>> list,Cfgparam cfgparam){
		this.dao.delete(cfgparam);
		int m = 0;
		for(Map<String,Object> map:list){
			Cfgparam cfg = new Cfgparam();
			cfg.setValue(map);
			cfg.preInsert();
			cfg.setRemarks(m+"");
			m += this.dao.insert(cfg);
		}
		return m;
	}
	
	public List<Cfgparam> getExcelData(Cfgparam cfg) {
		return this.dao.getExcelData(cfg);
	}
}
