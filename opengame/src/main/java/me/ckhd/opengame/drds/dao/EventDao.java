package me.ckhd.opengame.drds.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.drds.entity.EventEntity;
import me.ckhd.opengame.drds.entity.Stat;
import me.ckhd.opengame.evnet.entity.AppEventStat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


@Repository
public class EventDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	Logger logger = LoggerFactory.getLogger(EventDao.class);
	
	public void saveEvent(EventEntity event) {
		
		String sql = "insert into app_event(id,sid,sid_index,imei,iccid,imsi,android_id,ip,"
				+ "type,ckappid,appid,channelid,net_type,occur_time,version_name,"
				+ "phone_model,os_version,lang,sdk_version,exit_type,pay_sdk,pay_code,"
				+ "pay_number,insert_time,occur_date,sign_md5,trust_sign,provice_name,city_name,ad_plat_name,"
				+ "ad_type,level_id,level_success,level_time,equipment_id,equipment_get_type,"
				+ "equipment_remain_qty,coin,diamond,vit,exist_duration,view_state,pay_err_code,pay_err_msg,pay_order) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		event.preInsert();
		if (event.getOccurTime() > event.getCreateDate().getTime()) {
			// 发生时间比入库时间大，修正发生时间
			event.setOccurTime(event.getCreateDate().getTime());
		} else if(event.getCreateDate().getTime() - event.getOccurTime() > 8 * 24 * 3600 * 1000 ){
			// 超过8天不真实，修正发生时间
			event.setOccurTime(event.getCreateDate().getTime());
		}
		logger.info(String.format("开始插入事件信息[%s]", event.toString()));
		jdbcTemplate.update(sql,event.getId(),
				event.getSid(),event.getSidIndex(),
				event.getImei(),event.getIccid(),event.getImsi(),event.getAndroidid(),event.getIp(),
				event.getType(),event.getCkappid(),
				event.getAppid(),event.getChannelid(),
				event.getNetType(),new Date(event.getOccurTime()),
				event.getVersioName(),event.getPhoneModel(),
				event.getOsVersion(),event.getLang(),
				event.getSdkVersion(),event.getExitType(),
				event.getPaySdk(),event.getPayCode(),
				event.getPayNumber(),event.getCreateDate(),new Date(event.getOccurTime()),
				event.getSignMD5(),event.getTrustSign(),event.getProviceName(),event.getCityName(),event.getAdPlatName(),event.getAdType(),event.getLevelId(),
				event.getLevelSuccessInt(),event.getLevelTime(),event.getEquipmentId(),event.getEquipmentGetType(),event.getEquipmentRemainQty(),
				event.getCoin(),event.getDiamond(),event.getVit(),event.getExistDuration(),event.getViewState(),event.getPayErrCode(),event.getPayErrMsg(),event.getPayOrder());
		logger.info("事件信息插入完成");
	}
	
	public List<Map<String,Object>> getAllEventNum(){
//		String sql = "select imei,type,channelid,chappid,action_date,COUNT(*) as num from app_event GROUP BY imei,type,channelid,chappid,occur_date";
//		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return null;//list;
	}
	
	
	
	
	/**
	 * 保存用户信息
	 * @param event
	 */
	public void saveAccountData(EventEntity event){
		logger.info("开始插入用户数据");
		String sql = "INSERT INTO app_account "
				+ "(imei,iccid,imsi,android_id,ip,ckappid,appid,channelid,pay_sdk,version_name,occur_time,occur_date,last_login_date,sign_md5,trust_sign,insert_time,provice_name,city_name) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			jdbcTemplate.update(sql,
					event.getImei(),event.getIccid(),event.getImsi(),event.getAndroidid(),event.getIp(),event.getCkappid(),
					event.getAppid(),event.getChannelid(),event.getPaySdk(),event.getVersioName(),new Date(event.getOccurTime()),new Date(event.getOccurTime()),new Date(event.getOccurTime()), event.getSignMD5(), event.getTrustSign(), event.getCreateDate(), event.getProviceName(), event.getCityName());
		} catch (DuplicateKeyException e) {
			
		}
		logger.info("插入用户数据完成");
	}
	
	/**
	 * 检查是否有用户信息
	 * @param event
	 * @return
	 */
	public int checkAccountData(EventEntity event){
		logger.info("开始查询用户数据");
		String sql = "select count(0) from app_account where imei=? and ckappid=?";
		int num = jdbcTemplate.queryForObject(sql,new String[]{event.getImei(),event.getCkappid()},new RowMapper<Integer>(){

			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt(1);
			}
			
		});
		logger.info("查询用户数据完成");
		return num;
	}
	
	/**
	 * 更新用户信息
	 * @param event
	 */
	public void updateAccountData(EventEntity event){
		logger.info("开始更新用户数据");
		String sql = "update app_account Set last_login_date=? where imei=? and ckappid=?";
		jdbcTemplate.update(sql,new Date(event.getOccurTime()),event.getImei(),event.getCkappid());
		logger.info("更新用户数据完成");
	}
	
	
//----------------------------------- 事件统计--------------------------------------------	
	
	/**
	 * 获取新增用户数量
	 * 
	 * @param type
	 * @param whereStr
	 * @return
	 */
	public List<Map<String, Object>> getNewAccount(AppEventStat appEventStat) {

		StringBuilder sql = new StringBuilder("select DATE_FORMAT(occur_time,'%Y-%m-%d') as day,count(0) as num from app_account where ckappid=? and occur_time>=? and occur_time<=?");
		List<Object> params = new ArrayList<Object>();
		params.add(appEventStat.getCkAppId());
		params.add(appEventStat.getStartDate());
		params.add(appEventStat.getEndDate());
		
		if(StringUtils.isNotBlank(appEventStat.getAppId())){
			sql.append(" and appid=?");
			params.add(appEventStat.getAppId());
		}
		if(StringUtils.isNotBlank(appEventStat.getCkChannelId())){
			sql.append(" and channelid=?");
			params.add(appEventStat.getCkChannelId());
		}
		
		sql.append(" group by DATE_FORMAT(occur_time,'%Y-%m-%d')");

		List<Map<String, Object>> result = jdbcTemplate.queryForList(
				sql.toString(), params.toArray());
		return result;
	}
	
	/**
	 * @param appEventStat
	 * @return
	 */
	public List<Map<String, Object>> getNewAccountChannelId(AppEventStat appEventStat) {
		StringBuilder sql = new StringBuilder("select channelid,count(0) as allnum,COUNT(CASE WHEN trust_sign= 1 THEN 1 ELSE null END) as num from app_account where ckappid=? and occur_time>=? and occur_time<?");
		List<Object> params = new ArrayList<Object>();
		params.add(appEventStat.getCkAppId());
		params.add(appEventStat.getStartDate());
		params.add(nextDate(appEventStat.getStartDate()));
		
		if(StringUtils.isNotBlank(appEventStat.getAppId())){
			sql.append(" and appid=?");
			params.add(appEventStat.getAppId());
		}
		if(StringUtils.isNotBlank(appEventStat.getCkChannelId())){
			sql.append(" and channelid=?");
			params.add(appEventStat.getCkChannelId());
		}
		
		sql.append(" group by channelid");

		List<Map<String, Object>> result = jdbcTemplate.queryForList(
				sql.toString(), params.toArray());
		return result;
	}
	
	/**
	 * 
	 * @param appEventStat
	 * @return
	 */
	public List<Map<String, Object>> getActiveUserNumChannelId(AppEventStat appEventStat) {
		StringBuilder sql = new StringBuilder("SELECT channelid,count(distinct imei) as allnum from app_event where ckappid=? and occur_date =? and type=1 ");
		List<Object> params = new ArrayList<Object>();
		params.add(appEventStat.getCkAppId());
		params.add(appEventStat.getStartDate());
//		params.add(nextDate(appEventStat.getStartDate()));
		
		if(StringUtils.isNotBlank(appEventStat.getAppId())){
			sql.append(" and appid=?");
			params.add(appEventStat.getAppId());
		}
		if(StringUtils.isNotBlank(appEventStat.getCkChannelId())){
			sql.append(" and channelid=?");
			params.add(appEventStat.getCkChannelId());
		}
		
		sql.append(" group by channelid");

		List<Map<String, Object>> result = jdbcTemplate.queryForList(
				sql.toString(), params.toArray());
		
		// add

		StringBuilder sqlTrust = new StringBuilder("SELECT channelid,count(distinct imei) as num from app_event where ckappid=? and occur_date =? and type=1 and trust_sign=1 ");
		
		if(StringUtils.isNotBlank(appEventStat.getAppId())){
			sqlTrust.append(" and appid=?");
		}
		if(StringUtils.isNotBlank(appEventStat.getCkChannelId())){
			sqlTrust.append(" and channelid=?");
		}
		
		sqlTrust.append(" group by channelid");

		List<Map<String, Object>> trustResult = jdbcTemplate.queryForList(
				sqlTrust.toString(), params.toArray());
		// 渠道-活跃数
		Map<String, Long> activeMap = new HashMap<String, Long>();
		for(Map<String, Object> map: trustResult){
			String channelid = "-1";
			try{
				channelid = (String)map.get("channelid");
			}catch(Throwable t){
				// NOP
			}
			activeMap.put(channelid, ((Long)map.get("num")));
		}
		
		// result是所有的数量，追加上正版的数量，由于正版是子集，所以不会漏。
		for(Map<String, Object> map : result){
			String channelid = (String)map.get("channelid");
			if(channelid == null){
				channelid = "-1";
				map.put("channelid", "-1");
			}
			Long num = activeMap.get(channelid);
			if(num == null){
				num = 0L;
			}
			map.put("num", num);
		}
		
		return result;
	}
	/**
	 * 获取活跃用户 - 暂时只统计最后一天
	 * @param type
	 * @param whereStr
	 * @return
	 */
	public List<Map<String, Object>> getMAU(AppEventStat appEventStat){
		
		StringBuffer sql = new StringBuffer("select '" + appEventStat.getEndDate().substring(0,10) + "' as day,count(0) as num from(SELECT distinct imei from app_event where ckappid=? and occur_time >=? and occur_time <=? and type= 1");
		List<Object> params = new ArrayList<Object>();
		params.add(appEventStat.getCkAppId());
		params.add(appEventStat.getEndDate().substring(0,10) + " 00:00:00");
		params.add(appEventStat.getEndDate());
		
		if(StringUtils.isNotBlank(appEventStat.getAppId())){
			sql.append(" and appid=?");
			params.add(appEventStat.getAppId());
		}
		if(StringUtils.isNotBlank(appEventStat.getCkChannelId())){
			sql.append(" and channelid=?");
			params.add(appEventStat.getCkChannelId());
		}
		
		sql.append(") t");
		
		List<Map<String, Object>>  result = jdbcTemplate.queryForList(sql.toString(),params.toArray());
		return result;
	}
	
	/**
	 * 获取留存数据
	 * @return
	 */
	public List<Map<String,Object>> getStat(AppEventStat appEventStat){
		StringBuffer sql =new StringBuffer();
		String startDate = appEventStat.getStartDate().substring(0,8);
		String endDate = appEventStat.getEndDate().substring(0,8);
		sql.append("select channelid,ckappid,appid,stat_time,add_time,dru as num,second_day,third_day,seventh_day,thirtieth_day from ck_stat_remain where 1=1");
		String param = "";
		if(!StringUtils.isEmpty(appEventStat.getCkAppId())){
			sql.append(" and ckappid=?");
			param+=(appEventStat.getCkAppId()+",");
		}
		if(!StringUtils.isEmpty(appEventStat.getAppId())){
			sql.append(" and appid=?");
			param+=(appEventStat.getAppId()+",");
		}
		
		if(!StringUtils.isEmpty(appEventStat.getCkChannelId())){
			sql.append(" and channelid=?");
			param+=(appEventStat.getCkChannelId()+",");
		}
		sql.append(" and DATE_FORMAT(stat_time,'%Y%m%d') BETWEEN ? and ? order by stat_time, ckappid,channelid");
		param+=(startDate+",");
		param+=(endDate);
		String[] paramArray = param.split(",");
		List<Map<String, Object>>  result =jdbcTemplate.queryForList(sql.toString(),paramArray);
		return result;
	}
	
	
	/**
	 * 获取活跃付费用户数
	 * @param appEventStat
	 * @return
	 */
	public List<Map<String,Object>> getAPA(AppEventStat appEventStat){
		StringBuffer sql = new StringBuffer();
		String startDate = appEventStat.getStartDate().substring(0,8);
		String endDate = appEventStat.getEndDate().substring(0,8);
		boolean isSameDay=startDate.equals(endDate);
		String param = "";
		sql.append("SELECT date,channelid,count(*) num from( SELECT ");
		if(isSameDay){
			sql.append("DATE_FORMAT(occur_time,'%H:00') as date,");
		}else {
			sql.append("DATE_FORMAT(occur_time,'%Y-%m-%d') as date,");
		}
			
		sql.append("channelid,imei,COUNT(*) as num from app_event where 1=1");
		if(appEventStat.getCkAppId()!=null &&  !"".equals(appEventStat.getCkAppId())){
			sql.append(" and ckappid=? "); 
			param+=(appEventStat.getCkAppId()+",");
		}
		if(!StringUtils.isEmpty(appEventStat.getAppId())){
			sql.append(" and appid=?");
			param+=(appEventStat.getAppId()+",");
		}
		if(appEventStat.getCkChannelId()!=null && !"".equals(appEventStat.getCkChannelId())){
			sql.append(" and channelid=? "); 
			param+=(appEventStat.getCkChannelId()+",");
		}
		sql.append(" and DATE_FORMAT(occur_time,'%Y%m%d') BETWEEN ? and ? and type=4 group by channelid,imei, year(occur_time),month(occur_time),day(occur_time)");
		param+=(startDate+",");
		param+=(endDate);
		if(isSameDay){
			sql.append(",hour(occur_time)");
		}
		sql.append(" ) a group by channelid, year(date),month(date),day(date)");
		if(isSameDay){
			sql.append(",hour(date)");
		}
		sql.append("ORDER BY date");

		String[] paramArray = param.split(",");
		List<Map<String, Object>>  result = jdbcTemplate.queryForList(sql.toString(),paramArray);
		return result;
	}
	
	
	
	
	
	/**
	 * 获取付费率
	 * @param appEventStat
	 * @return
	 */
	public List<Map<String,Object>> getMPR(AppEventStat appEventStat){
		List<Map<String,Object>> mau = getMAU(appEventStat);
		List<Map<String,Object>> apa = getMAU(appEventStat);
		return getMPR(mau, apa);
	}
	
	/**
	 * 根据活跃用户和付费用户计算付费率
	 * @param mau
	 * @param apa
	 * @return
	 */
	public List<Map<String,Object>> getMPR(List<Map<String,Object>> mau,List<Map<String,Object>> apa){
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> mauMap :mau){
			Map<String,Object> resultMap = new HashMap<String, Object>();
			Double mauCount = Double.parseDouble(mauMap.get("num")==null?"0":mauMap.get("num").toString());
			String mauChannelid = mauMap.get("channelid").toString();
			String mauCkappid = mauMap.get("ckappid").toString();
			String mauDate = mauMap.get("date").toString();
			boolean flag = true;
			for(Map<String,Object> apaMap:apa){
				String apaChannelid = apaMap.get("channelid").toString();
				String apaCkappid = apaMap.get("ckappid").toString();
				String apaDate = apaMap.get("date").toString();
				if(mauDate.equals(apaDate) && mauChannelid.equals(apaChannelid) && mauCkappid.equals(apaCkappid)){
					Double apaCount = Double.parseDouble(mauMap.get("num")==null?"0":mauMap.get("num").toString());
					resultMap.put("num", mauCount/apaCount*100);
					resultMap.put("ckappid", mauCkappid);
					resultMap.put("channelid", mauChannelid);
					resultMap.put("date", mauDate);
					flag=false;
					break;
				}
			}
			if(flag){
				resultMap.put("num", 0);
				resultMap.put("ckappid", mauCkappid);
				resultMap.put("channelid", mauChannelid);
				resultMap.put("date", mauDate);
				result.add(resultMap);
			}
		}
		return result;
	}
	
	/**
	 * 获取参与次数统计
	 * @param appEventStat
	 * @return
	 */
	public List<Map<String,Object>> getDEC(AppEventStat appEventStat){
		StringBuffer sql = new StringBuffer();
		String startDate = appEventStat.getStartDate().substring(0,8);
		String endDate = appEventStat.getEndDate().substring(0,8);
		boolean isSameDay=startDate.equals(endDate);
		String param = "";
		sql.append("SELECT ");
		if(isSameDay){
			sql.append("DATE_FORMAT(occur_time,'%H:00') as date,");
		}else {
			sql.append("DATE_FORMAT(occur_time,'%Y-%m-%d') as date,");
		}
			
		sql.append("channelid,imei,ckappid,COUNT(*) as num from app_event where 1=1");
		if(appEventStat.getCkAppId()!=null &&  !"".equals(appEventStat.getCkAppId())){
			sql.append(" and ckappid=? "); 
			param+=(appEventStat.getCkAppId()+",");
		}
		if(!StringUtils.isEmpty(appEventStat.getAppId())){
			sql.append(" and appid=?");
			param+=(appEventStat.getAppId()+",");
		}
		if(appEventStat.getCkChannelId()!=null && !"".equals(appEventStat.getCkChannelId())){
			sql.append(" and channelid=? "); 
			param+=(appEventStat.getCkChannelId()+",");
		}
		sql.append(" and DATE_FORMAT(occur_time,'%Y%m%d') BETWEEN ? and ? and type=1 group by channelid,ckappid,imei, year(occur_time),month(occur_time),day(occur_time)");
		param+=(startDate+",");
		param+=(endDate);
		if(isSameDay){
			sql.append(",hour(occur_time)");
		}
		sql.append("ORDER BY date");
		String[] paramArray = param.split(",");
		List<Map<String, Object>>  result = jdbcTemplate.queryForList(sql.toString(),paramArray);
		return result;
	}
	
	
	
//-----------------------------------------------TODO 计算留存-----------------------------------------------	
	/**
	 * 插入昨日新增量
	 */
	public void insertStat(){
//		String selectSql= "SELECT channelid, ckappid, appid,count(*) dru, "
//				+ "DATE_FORMAT(DATE_SUB(SYSDATE(), interval 1 day),'%Y-%m-%d') stat_time ,SYSDATE() add_time FROM ck_app_account WHERE 1=1 "
//				+ "AND DATE_FORMAT(create_date,'%Y-%m-%d') BETWEEN DATE_FORMAT(DATE_SUB(SYSDATE(), interval 1 day),'%Y-%m-%d') AND DATE_FORMAT(DATE_SUB(SYSDATE(), interval 1 day),'%Y-%m-%d') "
//				+ "GROUP BY channelid,ckappid;";
//		String insertSql = "INSERT INTO ck_stat_remain (channelid,ckappid,appid,dru, stat_time, add_time)  values(?,?,?,?,?,?) ";
//		//查询昨日新增
//		List<Map<String,Object>> list  = jdbcTemplate.queryForList(selectSql);
//		//批量插入数据
//		try {
//			jdbcTemplate.batchUpdate(insertSql, new MyBatchInsertPreparedStatementSetter(list));
//		} catch (Exception e) {
//			
//		}
	}
	
	/**
	 * 更新留存率
	 */
	public void updateSecondDay(Stat stat){
//		String activeSql = "SELECT count(DISTINCT imei) num, ckappid, channelid FROM ck_app_event "
//				+ "WHERE (DATE_FORMAT(action_date,'%Y-%m-%d')=DATE_FORMAT(SYSDATE(),'%Y-%m-%d')) and type =1 GROUP BY channelid,ckappid";//获取活跃用户
//		List<Map<String, Object>> activeList = jdbcTemplate.queryForList(activeSql);
//		String newSql = "SELECT count(DISTINCT imei) num,ckappid,channelid,create_date add_time "
//				+ "FROM ck_app_account WHERE (DATE_FORMAT(create_date,'%Y-%m-%d') "
//				+ "BETWEEN DATE_FORMAT(date_sub(SYSDATE(), INTERVAL "+stat.getStartDate()+" DAY),'%Y-%m-%d') "
//				+ "AND DATE_FORMAT(date_sub(SYSDATE(), INTERVAL "+stat.getStartDate()+" DAY),'%Y-%m-%d')) GROUP BY channelid,ckappid ";//获取新增用户
//		List<Map<String, Object>> newList = jdbcTemplate.queryForList(newSql);
//		String updateSql = "UPDATE ck_stat_remain SET "+stat.getField()+"=? where channelid=? and ckappid=? and DATE_FORMAT(stat_time,'%Y-%m-%d')= DATE_FORMAT(date_sub(SYSDATE(), INTERVAL "+stat.getStartDate()+" DAY), '%Y-%m-%d')";
//		jdbcTemplate.batchUpdate(updateSql, new MyBatchUpdatePreparedStatementSetter(getUpdateList(newList, activeList)));
		
		// null do
	}

	
	
	public List<Map<String,Object>> getUpdateList(List<Map<String,Object>> newAccount,List<Map<String, Object>> activeAccount){
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> newMap:newAccount){
			Map<String,Object> map = new HashMap<String, Object>();
			String ckappid = newMap.get("ckappid").toString();
			String channelid = newMap.get("channelid").toString();
			double num = Double.parseDouble(newMap.get("num").toString());
			boolean flag = true;
			for(Map<String,Object> acMap:activeAccount){
				String acCkappid = acMap.get("ckappid").toString();
				String acChannelid = acMap.get("channelid").toString();
				double acNum = Double.parseDouble(acMap.get("num").toString());
				if(ckappid.equals(acCkappid) && channelid.equals(acChannelid)){
					map.put("num", (acNum/num)*100);
					map.put("ckappid", ckappid);
					map.put("channelid", channelid);
					result.add(map);
					flag=false;
					break;
				}
			}
			if(flag){
				map.put("num", 0);
				map.put("ckappid", ckappid);
				map.put("channelid", channelid);
				result.add(map);
			}
		}
		return result;
	}
	
	/**
	 * 批量更新
	 * @author leo
	 *
	 */
	class MyBatchUpdatePreparedStatementSetter implements BatchPreparedStatementSetter{
		  
		  private List<Map<String,Object>> list;
		  public MyBatchUpdatePreparedStatementSetter(List<Map<String,Object>> list) {
		    this.list = list;
		  }
		  
		  @Override
		  public void setValues(PreparedStatement ps, int i) throws SQLException {
			  Map<String,Object> map = list.get(i);
			  ps.setDouble(1,Double.parseDouble(map.get("num").toString()));
			  ps.setString(2, map.get("channelid").toString());
			  ps.setString(3, map.get("ckappid").toString());
		  }
		  @Override
		  public int getBatchSize() {
		    return list.size();
		  }
		}
	
	
	/**
	 * 批量存储
	 * @author leo
	 *
	 */
	class MyBatchInsertPreparedStatementSetter implements BatchPreparedStatementSetter{
		  
		  private List<Map<String,Object>> list;
		  
		  public MyBatchInsertPreparedStatementSetter(List<Map<String,Object>> list) {
		    this.list = list;
		  }
		  
		  @Override
		  public void setValues(PreparedStatement ps, int i) throws SQLException {
			  Map<String,Object> map = list.get(i);
			  ps.setString(1, map.get("channelid").toString());
			  ps.setString(2, map.get("ckappid").toString());
			  ps.setString(3, (map.get("appid")==null?null:map.get("appid").toString()));
			  ps.setInt(4, Integer.valueOf(map.get("dru").toString()));
			  ps.setString(5, map.get("stat_time").toString());
			  ps.setString(6, map.get("add_time").toString());
		  }
		  @Override
		  public int getBatchSize() {
		    return list.size();
		  }
		}
	/**
	 * 下一天
	 * @param date
	 * @return
	 */
	private String nextDate(String date){
		Date d = DateUtils.parseDate(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return DateUtils.formatDate(cal.getTime(),"yyyy-MM-dd");
	}
	
	/**
	 * 查询破解数
	 * @param appEventStat
	 * @return
	 */
	public List<Map<String, Object>> crackSDKNum(AppEventStat appEventStat) {
		StringBuilder sql = new StringBuilder("select channelid as ckChannelId,trust_sign,count(0) as num from app_account"
				+ " where ckappid= ? and occur_time>= ? and occur_time<= ?");
		if(StringUtils.isNotBlank(appEventStat.getCkChannelId())){
			sql.append(" and channelId=? ");
		}
		sql.append(" group by channelid, trust_sign  ORDER BY channelId,trust_sign");
		List<Object> params = new ArrayList<Object>();
		params.add(appEventStat.getCkAppId());
		params.add(appEventStat.getStartDate()+" 00:00:00");
		params.add(appEventStat.getEndDate()+" 23:59:59");
		if(StringUtils.isNotBlank(appEventStat.getCkChannelId())){
			params.add(appEventStat.getCkChannelId());
		}
		List<Map<String, Object>> result = jdbcTemplate.queryForList(
				sql.toString(), params.toArray());
		return result;
	}
	
	/**
	 * 生成统计新增的语句
	 * @param headSql
	 * @param sql
	 * @return
	 */
	public String generateSql(String headSql,String sql) {
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
		StringBuffer sb = new StringBuffer(); 
		String lineSeparator = System.getProperty("line.separator", "\n"); 
		for( int i=0;i<result.size();i++ ){
			if(i%10000==0){
				sb.append(headSql).append(lineSeparator);
			}
			sb.append("('").append(result.get(i).get("timeframe")).append("','")
			.append((result.get(i).get("ckappid")==null?"":result.get(i).get("ckappid"))).append("','")
			.append((result.get(i).get("channelid")==null?"":result.get(i).get("channelid"))).append("',")
			.append(result.get(i).get("newNum"))
			.append(")");
			if(i%10000==9999){
				sb.append(";").append(lineSeparator);
			}else{
				if( i == result.size()-1 ){
					sb.append(";");
				}else{
					sb.append(",").append(lineSeparator);
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 生成统计活跃的语句
	 * @param headSql
	 * @param sql
	 * @return
	 */
	public String generateActSql(String headSql,String sql) {
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
		StringBuffer sb = new StringBuffer(); 
		String lineSeparator = System.getProperty("line.separator", "\n"); 
		for( int i=0;i<result.size();i++ ){
			if(i%10000==0){
				sb.append(headSql).append(lineSeparator);
			}
			sb.append("('").append(result.get(i).get("timeframe")).append("','")
			.append((result.get(i).get("ckappid")==null?"":result.get(i).get("ckappid"))).append("','")
			.append((result.get(i).get("channelid")==null?"":result.get(i).get("channelid"))).append("',")
			.append(result.get(i).get("actNum"))
			.append(")");
			if(i%10000==9999){
				sb.append(";").append(lineSeparator);
			}else{
				if( i == result.size()-1 ){
					sb.append(";");
				}else{
					sb.append(",").append(lineSeparator);
				}
			}
		}
		return sb.toString();
	}
}