package me.ckhd.opengame.drds.service;

import java.util.List;
import java.util.Map;

import me.ckhd.opengame.drds.dao.EventDao;
import me.ckhd.opengame.drds.entity.EventEntity;
import me.ckhd.opengame.drds.entity.Stat;
import me.ckhd.opengame.evnet.entity.AppEventStat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("EventService")
public class EventService {

	@Autowired
	private EventDao eventDao;

	public void saveEventData(EventEntity event) {
		eventDao.saveEvent(event);
	}

	public void saveAcount(EventEntity event) {
		int accountCount = eventDao.checkAccountData(event);
		if (accountCount > 0) {
			eventDao.updateAccountData(event);
		} else {
			eventDao.saveAccountData(event);
		}
	}

	// -----------------------------TODO 事件统计-------------------------------
	/**
	 * 获取新增用户
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getNewAccout(AppEventStat appEventStat) throws Exception {
		return eventDao.getNewAccount(appEventStat);
	}

	/**
	 * 
	 * @param appEventStat
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getNewAccoutChannelId(AppEventStat appEventStat) throws Exception {
		return eventDao.getNewAccountChannelId(appEventStat);
	}
	
	public List<Map<String, Object>> getActiveUserNumChannelId(AppEventStat appEventStat) throws Exception {
		return eventDao.getActiveUserNumChannelId(appEventStat);
	}
	
	/**
	 * 获取活跃用户
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getActiveAccount(AppEventStat appEventStat) throws Exception {
		return eventDao.getMAU(appEventStat);
	}
	
	
	/**
	 * 获取留存数据
	 * @param appEventStat
	 * @return
	 */
	public List<Map<String, Object>> getStat(AppEventStat appEventStat){
		return eventDao.getStat(appEventStat);
	}
	
	
	/**
	 * 获取活跃付费用户数
	 * @param appEventStat
	 * @return
	 */
	public List<Map<String, Object>> getAPA(AppEventStat appEventStat){
		return eventDao.getAPA(appEventStat);
	}
	
	/**
	 * 获取付费率
	 * @param appEventStat
	 * @return
	 */
	public List<Map<String, Object>> getMPR(AppEventStat appEventStat){
		return eventDao.getMPR(appEventStat);
	}
	
	/**
	 * 获取参与次数统计
	 * @param appEventStat
	 * @return
	 */
	public List<Map<String, Object>> getDEC(AppEventStat appEventStat){
		return eventDao.getDEC(appEventStat);
	}
	
	
//----------------------------------------留存统计-------------------------------------------------	
	/**
	 * 保存昨日新增
	 */
	public void insertStat(){
		eventDao.insertStat();
	}
	
	/**
	 * 保存昨日新增
	 */
	public void updateStat(Stat stat){
		eventDao.updateSecondDay(stat);
	}
	
	/**
	 * 查询破解数
	 * @param appEventStat
	 * @return
	 */
	public List<Map<String, Object>> crackSDKNum(AppEventStat appEventStat) {
		return eventDao.crackSDKNum(appEventStat);
	}
	
	/**
	 * 生成统计新增的语句
	 * @param headSql
	 * @param sql
	 * @return
	 */
	public String generateSql(String headSql,String sql){
		return eventDao.generateSql(headSql,sql);
	}
	
	public String generateActSql(String headSql,String sql){
		return eventDao.generateActSql(headSql,sql);
	}
}
