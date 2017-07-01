package me.ckhd.opengame.app.service;

import me.ckhd.opengame.app.dao.AppCarriersDao;
import me.ckhd.opengame.app.entity.AppCarriers;
import me.ckhd.opengame.app.entity.Paycode;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.service.CrudService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AppCarriersService extends  CrudService<AppCarriersDao, AppCarriers>{

	 
	//add some service method here 4  extend .
	
	
	public Page<Paycode> findPaycodePage(Page<Paycode> page, Paycode paycode) {
		paycode.setPage(page);
		page.setList(dao.findPaycodeList(paycode));
		return page;
	}
	@Transactional(readOnly = false)
	public void deletePaycode(Paycode paycode) {
		dao.deletePaycode(paycode);
	}
	@Transactional(readOnly = false)
	public void savePaycode(Paycode paycode) {
		if (paycode.getIsNewRecord()){
			paycode.preInsert();
			dao.insertPaycode(paycode);
		}else{
			paycode.preUpdate();
			dao.updatePaycode(paycode);
		}
	}
	public Paycode getPaycode(String id) {
		return dao.getPaycode(id);
	}
	
	
	public boolean getByPaycodeAndCarriersType(Paycode paycode) {
		return dao.getByPaycodeAndCarriersType(paycode);
	}
	
}
