package me.ckhd.opengame.online.service;

import java.util.List;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.online.dao.TencentCallbackDataDao;
import me.ckhd.opengame.online.entity.TencentCallbackData;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("TencentCallbackDataService")
@Transactional(readOnly=true)
public class TencentCallbackDataService extends CrudService<TencentCallbackDataDao, TencentCallbackData>{

	public List<TencentCallbackData> getListData(){
		return this.dao.getListData();
	}
	
	@Transactional(readOnly = false)
	public int update(TencentCallbackData ten){
		return this.dao.update(ten);
	}
}
