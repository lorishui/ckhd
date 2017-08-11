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

	@Transactional(readOnly = false)
	public List<TencentCallbackData> getListData(){
		List<TencentCallbackData> list = this.dao.getListData();
		for(TencentCallbackData ten:list){
			ten.setStatus(2);
			ten.setSendNum(ten.getSendNum());
			this.dao.updateByOrderNull(ten);
		}
		return list;
	}
	
	@Transactional(readOnly = false)
	public int update(TencentCallbackData ten){
		return this.dao.update(ten);
	}
	
	@Transactional(readOnly = false)
	public int updateByOrderNull(TencentCallbackData ten){
		return this.dao.updateByOrderNull(ten);
	}
}
