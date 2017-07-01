/*
 * 
 */
package me.ckhd.opengame.stats.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.mmapi.entity.MmAppOrder;
import me.ckhd.opengame.stats.entity.MmChannelProvince;
import me.ckhd.opengame.stats.entity.OrderQry;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author qibiao
 *
 */
@Repository
public interface MmOrderStatsDao extends CrudDao<MmAppOrder> {
	
	public List<Object> stats(MmAppOrder mmAppOrder);
	
	public List<Object> statsByPaycode(MmAppOrder mmAppOrder);
	
	public List<Object> statsByReturnStatus(MmAppOrder mmAppOrder);
	
	public List<Object> statsByChannel(MmAppOrder mmAppOrder);
	
	public List<Object> statsByChannelPaycode(MmAppOrder mmAppOrder);

	public List<Object> statsByProvince(MmAppOrder mmAppOrder);
	
	public List<Object> queryErrorOrders(MmAppOrder mmAppOrder);
	
	public List<Object> statsByErrorProvince(MmAppOrder mmAppOrder);
	
	public List<Object> normal_statsByReturnStatus(MmAppOrder mmAppOrder);
	
	public List<Map<String, Object>> statsMmOrder(MmAppOrder mmAppOrder);
	
	public List<Map<String, Object>> statsAndOrder(MmAppOrder mmAppOrder);
	
	public List<Object> statsForAccout(MmAppOrder mmAppOrder);

	public List<Map<String, Object>> mmStatsByChannel(OrderQry orderQry);
	
	public List<Map<String, Object>> andStatsByChannel(OrderQry orderQry);
	
	public Map<String, Object> statsZZOrderNum(MmAppOrder mmAppOrder);
	
	public List<Map<String, Object>> sealProvince(Map<String,Object> map);
	
	public List<Map<String, Object>> sealProvinceTotal(Map<String,Object> map);
	
	public List<Map<String, Object>> statsByCkApp(MmAppOrder mmAppOrder);

	public List<MmChannelProvince> findChannelProvincePage(MmChannelProvince mmChannelProvince);

	public List<Object> getSucc(MmChannelProvince mmChannelProvince);

	public List<Object> getSucc(@Param("ckappId")String ckappId, @Param("orderType")int orderType,
			@Param("appId")String appId,@Param("provinceId") String provinceId, 
			@Param("channelId")String channelId,@Param("filterRole") String filterRole,
			@Param("filterRate")int filterRate, @Param("startDate")String startDate, @Param("endDate")String endDate);
}
