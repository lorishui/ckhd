package me.ckhd.opengame.stats.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import me.ckhd.opengame.andapi.entity.AndAPPOrder;
import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.stats.entity.AndChannelProvince;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface AndOrderStatsDao extends  CrudDao<AndAPPOrder>{

	List<Map<String, Object>> stats(AndAPPOrder andapporder);

	List<Map<String, Object>> statsPaycode(AndAPPOrder andapporder);
	
	List<Map<String, Object>> statsByReturnStatus(AndAPPOrder andapporder);
	
	List<Map<String, Object>> queryErrorOrders(AndAPPOrder andapporder);

	List<Map<String, Object>> statsForAccout(AndAPPOrder andapporder);
	
	List<AndAPPOrder> findVersionId(AndAPPOrder andapporder);

	List<Map<String, Object>> statsByErrorProvince(AndAPPOrder andapporder);

	List<Map<String, Object>> statsByProvince(AndAPPOrder andapporder);

	List<Map<String, Object>> statsByChannel(AndAPPOrder andapporder);

	List<Map<String, Object>> statsByChannelPaycode(AndAPPOrder andapporder);
	
	public List<Map<String, Object>> sealProvince(Map<String,Object> map);
	
	public List<Map<String, Object>> sealProvinceTotal(Map<String,Object> map);
	
	public List<Map<String, Object>> statsByCkApp(AndAPPOrder andapporder);

	List<AndChannelProvince> findChannelProvincePage(AndChannelProvince andChannelProvince);

	List<Object> getSucc(@Param("ckappId")String ckappId, @Param("contentId")String contentId, @Param("versionId")String versionId, 
			@Param("provinceId")String provinceId,@Param("channelId") String channelId, @Param("filterRole")String filterRole, 
			@Param("filterRate")int filterRate,@Param("startDate") Date startDate,@Param("endDate") Date endDate);
}
