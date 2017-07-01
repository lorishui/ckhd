package me.ckhd.opengame.stats.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import me.ckhd.opengame.andapi.entity.AndAPPOrder;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.stats.dao.AndOrderStatsDao;
import me.ckhd.opengame.stats.entity.AndChannelProvince;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AndOrderStatsService extends  CrudService<AndOrderStatsDao, AndAPPOrder>{
	/**
	 * @return stats map 
	 */
	public  List<Map<String,Object>> stats(AndAPPOrder andapporder) {
		List<Map<String,Object>> data = new ArrayList<Map<String, Object>>();
		data = dao.stats(andapporder);
		/*	for(String key : data.keySet()){
			System.out.println("key:"+key+",value:"+data.get(key));
		}*/
		return data;
	}
	
	
	/**
	 * @return stats map 
	 */
	public List<Map<String,Object>> statsPaycode(AndAPPOrder andapporder) {
		List<Map<String,Object>> data = new ArrayList<Map<String, Object>>();
		data = dao.statsPaycode(andapporder);
		
		/*for (int i = 0; i < data.size(); i++) {
			for(String key : data.get(i).keySet()){
				System.out.println("key:"+key+",value:"+data.get(i).get(key));
			}
		}*/
		return data;
	}
	
	
	public List<Map<String,Object>> statsByReturnStatus(AndAPPOrder andapporder) {
		return dao.statsByReturnStatus(andapporder);
	}
	

	public List<Map<String,Object>> queryErrorOrders(AndAPPOrder andapporder) {
		return dao.queryErrorOrders(andapporder);
	}
	
	
	public List<Map<String,Object>> statsForAccout(AndAPPOrder andapporder){
		return dao.statsForAccout(andapporder);
	}
	
	public List<AndAPPOrder> findVersionId(AndAPPOrder andapporder){
		return dao.findVersionId(andapporder);
	}
	
	public List<Map<String,Object>> statsByErrorProvince(AndAPPOrder andapporder){
		return dao.statsByErrorProvince(andapporder);
	}
	
	public List<Map<String,Object>> statsByProvince(AndAPPOrder andapporder){
		return dao.statsByProvince(andapporder);
	}


	public List<Map<String,Object>> statsByChannel(AndAPPOrder andapporder) {
		return dao.statsByChannel(andapporder);
	}

	/**
	 * 按渠道计费点数据量太大，不需要统计 2016-07-13
	 * @param andapporder
	 * @return
	 */
	@Deprecated
	public List<Map<String,Object>> statsByChannelPaycode(AndAPPOrder andapporder) {
		return dao.statsByChannelPaycode(andapporder);
	}
	
	public List<Map<String, Object>> sealProvince(Map<String,Object> map){
		return dao.sealProvince(map);
	}
	
	public List<Map<String, Object>> sealProvinceTotal(Map<String,Object> map){
		return dao.sealProvinceTotal(map);
	}
	
	public List<Map<String, Object>> statsByCkApp(AndAPPOrder andAPPOrder){
		return dao.statsByCkApp(andAPPOrder);
	}


	public Page<AndChannelProvince> findChannelProvincePage(Page<AndChannelProvince> page, AndChannelProvince andChannelProvince) {
		andChannelProvince.setPage(page);
		page.setList(dao.findChannelProvincePage(andChannelProvince));
		return page;
	}


	public List<Object> getSucc(String ckappId, String contentId, String versionId, String provinceId, String channelId, String filterRole, int filterRate, Date startDate,
			Date endDate) {
		return dao.getSucc(ckappId, contentId,  versionId,  provinceId,  channelId,  filterRole,  filterRate,  startDate,endDate);
	}
}