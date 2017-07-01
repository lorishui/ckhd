package me.ckhd.opengame.stats.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.mmapi.entity.MmAppOrder;
import me.ckhd.opengame.stats.dao.MmOrderStatsDao;
import me.ckhd.opengame.stats.entity.MmChannelProvince;
import me.ckhd.opengame.stats.entity.OrderQry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MmOrderStatsService extends  CrudService<MmOrderStatsDao, MmAppOrder>{

	@Autowired
	private MmOrderStatsDao mmOrderStatsDao;

	/**
	 * @param mmAppOrder
	 * @return
	 */
	public List<Object> stats(MmAppOrder mmAppOrder) {
		
		return mmOrderStatsDao.stats(mmAppOrder);
	}
	
	/**
	 * @param mmAppOrder
	 * @return
	 */
	public List<Object> statsByPaycode(MmAppOrder mmAppOrder){
		
		return mmOrderStatsDao.statsByPaycode(mmAppOrder);
	}
	
	public List<Object> statsByReturnStatus(MmAppOrder mmAppOrder) {
		return mmOrderStatsDao.statsByReturnStatus(mmAppOrder);
	}
	 
	public List<Object> statsByChannel(MmAppOrder mmAppOrder) {
		return mmOrderStatsDao.statsByChannel(mmAppOrder);
	}
	
	/**
	 * 按渠道计费点统计不需要 2016-07-13
	 * @param mmAppOrder
	 * @return
	 */
	@Deprecated
	public List<Object> statsByChannelPaycode(MmAppOrder mmAppOrder){
		return mmOrderStatsDao.statsByChannelPaycode(mmAppOrder);
	}
	
	public List<Object> statsByProvince(MmAppOrder mmAppOrder){
		return mmOrderStatsDao.statsByProvince(mmAppOrder);
	}
	
	public List<Object> queryErrorOrders(MmAppOrder mmAppOrder){
		return mmOrderStatsDao.queryErrorOrders(mmAppOrder);
	}
	
	public List<Object> statsByErrorProvince(MmAppOrder mmAppOrder){
		return mmOrderStatsDao.statsByErrorProvince(mmAppOrder);
	}
	
	public List<Object> normal_statsByReturnStatus(MmAppOrder mmAppOrder){
		return mmOrderStatsDao.normal_statsByReturnStatus(mmAppOrder);
	}
	
	public List<Map<String, Object>> statsMmOrder(MmAppOrder mmAppOrder){
		return mmOrderStatsDao.statsMmOrder(mmAppOrder);
	}
	
	public List<Map<String, Object>> statsAndOrder(MmAppOrder mmAppOrder){
		return mmOrderStatsDao.statsAndOrder(mmAppOrder);
	}

	public List<Object> statsForAccout(MmAppOrder mmAppOrder){
		return mmOrderStatsDao.statsForAccout(mmAppOrder);
	}
	
	public List<Map<String, Object>> mmStatsByChannel(OrderQry orderQry){
		return mmOrderStatsDao.mmStatsByChannel(orderQry);
	}
	
	public List<Map<String, Object>> andStatsByChannel(OrderQry orderQry){
		return mmOrderStatsDao.andStatsByChannel(orderQry);
	}

	public Map<String, Object> statsZZOrderNum(MmAppOrder mmAppOrder){
		return mmOrderStatsDao.statsZZOrderNum(mmAppOrder);
	}

	public List<Map<String, Object>> sealProvince(Map<String,Object> map){
		return mmOrderStatsDao.sealProvince(map);
	}
	
	public List<Map<String, Object>> sealProvinceTotal(Map<String,Object> map){
		return mmOrderStatsDao.sealProvinceTotal(map);
	}
	
	public List<Map<String, Object>> statsByCkApp(MmAppOrder mmAppOrder){
		return dao.statsByCkApp(mmAppOrder);
	}

	public Page<MmChannelProvince> findChannelProvincePage(Page<MmChannelProvince> page, MmChannelProvince mmChannelProvince) {
		mmChannelProvince.setPage(page);
		List<MmChannelProvince> list = dao.findChannelProvincePage(mmChannelProvince);
		page.setList(list);
		return page;
	}


	public List<Object> getSucc(MmChannelProvince mmChannelProvince) {
		return dao.getSucc(mmChannelProvince);
	}

	public List<Object> getSucc(String ckappId, int orderType, String appId, String provinceId, String channelId, String filterRole,int filterRate, String startDate, String endDate) {
		return dao.getSucc(ckappId,orderType,appId,provinceId,channelId,filterRole,filterRate,startDate,endDate);
	}
}