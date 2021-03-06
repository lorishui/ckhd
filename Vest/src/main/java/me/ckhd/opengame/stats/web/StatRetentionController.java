package me.ckhd.opengame.stats.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.stats.entity.StatRelated;
import me.ckhd.opengame.stats.entity.StatRetention;
import me.ckhd.opengame.stats.service.StatRelateService;
import me.ckhd.opengame.stats.service.StatRetentionService;
import me.ckhd.opengame.sys.utils.UserUtils;

@RequestMapping(value = "${adminPath}/stats")
@Controller
public class StatRetentionController extends BaseController {
	
	@Autowired
	private StatRetentionService srdService;
	@Autowired
	private	StatRelateService deviceService;
	
	
	@RequestMapping("retention")
	public String statRetention(StatRetention statRetention,Model model,HttpSession session){
		if ( !Verdict.isAllow(statRetention.getCkAppId())) {
			return "403";
		}
		if(statRetention.getCkAppId() == null){
			statRetention.setCkAppId("2000");
		}
		StatRetention param = null;
		List<StatRetention> list = null;
		String currentDate = "";
		try {
			//20170731，添加权限限制
			Set<String> channelPermission = UserUtils.getChannelPermission();
			if( !channelPermission.isEmpty() ) {
				statRetention.setPermissionChannel(StringUtils.join(channelPermission, ","));
			}
			Set<String> gamePermission = UserUtils.getGamePermission();
			if( !gamePermission.isEmpty() ) {
				statRetention.setPermissionCkAppId(StringUtils.join(gamePermission, ","));
			}
			Set<String> gameChildPermission = UserUtils.getGameChildPermission();
			if( !gameChildPermission.isEmpty() ) {
				statRetention.setPermissionCkAppChildId(new ArrayList<String>(gameChildPermission));
			}
			
			StatRelated statRelated =  getStatRelated(statRetention);
			param = (StatRetention) statRetention.clone();
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			if( StringUtils.isBlank(statRetention.getStartTime()) || StringUtils.isBlank(statRetention.getEndTime()) ){
				cal.add(Calendar.DAY_OF_MONTH, 0);
				statRetention.setEndTime(sdf.format(cal.getTime()));
				param.setEndTime(sdf2.format(cal.getTime()));

				cal.add(Calendar.DAY_OF_MONTH, -7);
				statRetention.setStartTime(sdf.format(cal.getTime()));
				param.setStartTime(sdf2.format(cal.getTime()));
			}else{
				statRetention.setStartTime(statRetention.getStartTime().replace("-", ""));
				statRetention.setEndTime(statRetention.getEndTime().replace("-", ""));
			}
			statRelated.setEndTime(statRetention.getEndTime()+"23");
			statRelated.setStartTime(statRetention.getStartTime()+"00");
			getSelectStatRelate(statRelated);
			getSelectStatRetention(statRetention);
			statRelated.setPermissionChannel(statRetention.getPermissionChannel());
			statRelated.setPermissionCkAppId(statRetention.getPermissionCkAppId());
			statRelated.setPermissionCkAppChildId(statRetention.getPermissionCkAppChildId());
			list = srdService.statsData(statRetention);
			List<StatRelated> deviceList = deviceService.getTotalNum(statRelated);
			combainData(list,deviceList);
			
			currentDate = sdf.format(DateUtils.addDays(new Date(), -1));
		} catch (Exception e) {
			logger.error("", e);
		}
		model.addAttribute("currentDate", currentDate);
		model.addAttribute("data", list);
		model.addAttribute("statRetention",param);
		return "modules/stats/statRetentionData";
	}
	
	
	@RequestMapping("LTV")
	public String statLTV(StatRetention statLTV,Model model ){
		if ( !Verdict.isAllow(statLTV.getCkAppId())) {
			return "403";
		}
		if(statLTV.getCkAppId() == null){
			statLTV.setCkAppId("2000");
		}
		StatRetention param = null;
		List<StatRetention> list = null;
		List<StatRelated> actList = null;
		String currentDate = "";
		try {
			//20170731，添加权限限制
			Set<String> channelPermission = UserUtils.getChannelPermission();
			if( !channelPermission.isEmpty() ) {
				statLTV.setPermissionChannel(StringUtils.join(channelPermission, ","));
			}
			Set<String> gamePermission = UserUtils.getGamePermission();
			if( !gamePermission.isEmpty() ) {
				statLTV.setPermissionCkAppId(StringUtils.join(gamePermission, ","));
			}
			Set<String> gameChildPermission = UserUtils.getGameChildPermission();
			if( !gameChildPermission.isEmpty() ) {
				statLTV.setPermissionCkAppChildId(new ArrayList<String>(gameChildPermission));
			}
			
			StatRelated statRelated =  getStatRelated(statLTV);
			statRelated.setPermissionChannel(statLTV.getPermissionChannel());
			statRelated.setPermissionCkAppId(statLTV.getPermissionCkAppId());
			statRelated.setPermissionCkAppChildId(statLTV.getPermissionCkAppChildId());
			param = (StatRetention) statLTV.clone();
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			if( StringUtils.isBlank(statLTV.getStartTime()) || StringUtils.isBlank(statLTV.getEndTime()) ){
				cal.add(Calendar.DAY_OF_MONTH, 0);
				statLTV.setEndTime(sdf.format(cal.getTime()));
				param.setEndTime(sdf2.format(cal.getTime()));
				
				cal.add(Calendar.DAY_OF_MONTH, -7);
				statLTV.setStartTime(sdf.format(cal.getTime()));
				param.setStartTime(sdf2.format(cal.getTime()));
			}else{
				statLTV.setStartTime(statLTV.getStartTime().replace("-", ""));
				statLTV.setEndTime(statLTV.getEndTime().replace("-", ""));
			}
			statRelated.setEndTime(statLTV.getEndTime()+"23");
			statRelated.setStartTime(statLTV.getStartTime()+"00");
			getSelectStatRelate(statRelated);
			getSelectStatRetention(statLTV);
			List<StatRelated> deviceList = deviceService.getTotalNum(statRelated);
			statRelated.setStartTime(statRelated.getStartTime().substring(0, 8));
			statRelated.setEndTime(statRelated.getEndTime().substring(0, 8));
			statRelated.setLength(8);
			actList = deviceService.getTotalActNum(statRelated);
			list = srdService.statsLTVData(statLTV);
			//以actList为主导
			combainActData(list, actList);
			combainActAndNew(actList,deviceList);
			clearData(actList);
			
			currentDate = sdf.format(new Date());
		} catch (Exception e) {
			logger.error("", e);
		}
		model.addAttribute("currentDate", currentDate);
		model.addAttribute("data", actList);
		model.addAttribute("statLTV",param);
		return "modules/stats/statLTV";
	}
	
	


	private void clearData(List<StatRelated> list) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		for (int i = 0; i < list.size(); i++) {
			StatRelated sr = list.get(i);
			try {
				Date date = sdf.parse(sr.getTimeframes());
				Date now = sdf.parse(sdf.format(new Date()));
				long t = (now.getTime()-date.getTime()) / (1000 * 3600 * 24);
				if(t < 0){
					list.get(i).setLtv0(-1);
				}
				if(t <= 1){
					list.get(i).setLtv1(-1);
				}
				if(t <= 2){
					list.get(i).setLtv2(-1);
				}
				if(t <= 3){
					list.get(i).setLtv3(-1);
				}
				if(t <= 4){
					list.get(i).setLtv4(-1);
				}
				if(t <= 5){
					list.get(i).setLtv5(-1);
				}
				if(t <= 6){
					list.get(i).setLtv6(-1);
				}
				if(t <= 7){
					list.get(i).setLtv7(-1);
				}
				if(t <= 14){
					list.get(i).setLtv14(-1);
				}
				if(t <= 30){
					list.get(i).setLtv30(-1);
				}
				if(t <= 60){
					list.get(i).setLtv60(-1);
				}
				if(t <= 90){
					list.get(i).setLtv90(-1);
				}
			} catch (ParseException e) {
				logger.error("日期转换错误");
			}
			
		}
		
	}


	/**
	 * 组装查询group by条件
	 * @param statRelated
	 */
	private void getSelectStatRetention(StatRetention statRetention) {
		if( 1 == statRetention.getGroupCkAppId() ){
			statRetention.setGroupBy(statRetention.getGroupBy()+",ckAppId");
		}
		if( 1 == statRetention.getGroupChildCkAppId() ){
			statRetention.setGroupBy(statRetention.getGroupBy()+",childCkAppId");
		}
		if( 1 == statRetention.getGroupChannel() ){
			statRetention.setGroupBy(statRetention.getGroupBy()+",ckChannelId");
		}
		if( 1 == statRetention.getGroupChildChannel() ){
			statRetention.setGroupBy(statRetention.getGroupBy()+",childChannelId");
		}
	}
	
	/**
	 * 组装查询group by条件
	 * @param statRelated
	 */
	private void getSelectStatRelate(StatRelated statRelated) {
		if( 1 == statRelated.getGroupCkAppId() ){
			statRelated.setGroupBy(statRelated.getGroupBy()+",ckAppId");
		}
		if( 1 == statRelated.getGroupChildCkAppId() ){
			statRelated.setGroupBy(statRelated.getGroupBy()+",childCkAppId");
		}
		if( 1 == statRelated.getGroupChannel() ){
			statRelated.setGroupBy(statRelated.getGroupBy()+",ckChannelId");
		}
		if( 1 == statRelated.getGroupChildChannel() ){
			statRelated.setGroupBy(statRelated.getGroupBy()+",childChannelId");
		}
		statRelated.setTimeFt("%Y%m%d");
	}
	
	/**
	 * 组装留存数量与新增总数
	 * @param list
	 * @param deviceList
	 */
	private void combainData(List<StatRetention> list, List<StatRelated> deviceList) {
		for (StatRelated statRelated : deviceList) {
			for (StatRetention srd : list) {
				String ckAppId = srd.getCkAppId();
				String childCkAppId = srd.getChildCkAppId();
				String ckChannelId = srd.getCkChannelId();
				String childChannelId = srd.getChildChannelId();
				if(statRelated.getTimeframes().equals(srd.getRegTime())
					&& (ckAppId!=null?ckAppId.equals(statRelated.getCkAppId()):true)
					&& (childCkAppId!=null?childCkAppId.equals(statRelated.getChildCkAppId()):true)
					&& (ckChannelId!=null?ckChannelId.equals(statRelated.getCkChannelId()):true)
					&& (childChannelId!=null?childChannelId.equals(statRelated.getChildChannelId()):true)
						){
					srd.setTotalNum(statRelated.getNewNum());
				}
			}
		}
	}
	
	/**
	 * 组装活跃数与新增总数
	 * @param list
	 * @param deviceList
	 */
	private void combainActAndNew(List<StatRelated> actlist, List<StatRelated> deviceList) {
		for (StatRelated statRelated : deviceList) {
			for (StatRelated sr : actlist) {
				String ckAppId = sr.getCkAppId();
				String childCkAppId = sr.getChildCkAppId();
				String ckChannelId = sr.getCkChannelId();
				String childChannelId = sr.getChildChannelId();
				if(statRelated.getTimeframes().equals(sr.getTimeframes())
					&& (ckAppId!=null?ckAppId.equals(statRelated.getCkAppId()):true)
					&& (childCkAppId!=null?childCkAppId.equals(statRelated.getChildCkAppId()):true)
					&& (ckChannelId!=null?ckChannelId.equals(statRelated.getCkChannelId()):true)
					&& (childChannelId!=null?childChannelId.equals(statRelated.getChildChannelId()):true)
						){
					sr.setNewNum(statRelated.getNewNum());
				}
			}
		}
	}
	
	/**
	 * 组装留存数量与活跃总数
	 * @param list
	 * @param actList
	 */
	private void combainActData(List<StatRetention> list, List<StatRelated> actList) {
		for (StatRelated statRelated : actList) {
			statRelated.setLtv0(0);
			statRelated.setLtv1(0);
			statRelated.setLtv2(0);
			statRelated.setLtv3(0);
			statRelated.setLtv4(0);
			statRelated.setLtv5(0);
			statRelated.setLtv6(0);
			statRelated.setLtv7(0);
			statRelated.setLtv14(0);
			statRelated.setLtv30(0);
			statRelated.setLtv60(0);
			statRelated.setLtv90(0);
			for (StatRetention srd : list) {
				String ckAppId = srd.getCkAppId();
				String childCkAppId = srd.getChildCkAppId();
				String ckChannelId = srd.getCkChannelId();
				String childChannelId = srd.getChildChannelId();
				if(statRelated.getTimeframes().equals(srd.getRegTime())
					&& (ckAppId!=null?ckAppId.equals(statRelated.getCkAppId()):true)
					&& (childCkAppId!=null?childCkAppId.equals(statRelated.getChildCkAppId()):true)
					&& (ckChannelId!=null?ckChannelId.equals(statRelated.getCkChannelId()):true)
					&& (childChannelId!=null?childChannelId.equals(statRelated.getChildChannelId()):true)
						){
					statRelated.setLtv0(srd.getD0Num());
					statRelated.setLtv1(srd.getD1Num());
					statRelated.setLtv2(srd.getD2Num());
					statRelated.setLtv3(srd.getD3Num());
					statRelated.setLtv4(srd.getD4Num());
					statRelated.setLtv5(srd.getD5Num());
					statRelated.setLtv6(srd.getD6Num());
					statRelated.setLtv7(srd.getD7Num());
					statRelated.setLtv14(srd.getD14Num());
					statRelated.setLtv30(srd.getD30Num());
					statRelated.setLtv60(srd.getD60Num());
					statRelated.setLtv90(srd.getD90Num());
				}
			}
		}
	}
	
	private StatRelated getStatRelated(StatRetention srd){
		if(srd.getGroupChildChannel() == 1){
			srd.setGroupChannel(1);
		}
		if(StringUtils.isNotBlank(srd.getChildCkAppId())){
			srd.setGroupChildCkAppId(1);
		}
		if(StringUtils.isNotBlank(srd.getCkChannelId())){
			srd.setGroupChannel(1);
		}
		
		StatRelated statRelated = new StatRelated();
		if(StringUtils.isNotBlank(srd.getCkAppId())){
			statRelated.setCkAppId(srd.getCkAppId());
		}
		if(StringUtils.isNotBlank(srd.getChildCkAppId())){
			statRelated.setChildCkAppId(srd.getChildCkAppId());
		}
		if(StringUtils.isNotBlank(srd.getCkChannelId())){
			statRelated.setCkChannelId(srd.getCkChannelId());
		}
		if(StringUtils.isNotBlank(srd.getChildChannelId())){
			statRelated.setChildChannelId(srd.getChildChannelId());
		}
		statRelated.setGroupCkAppId(srd.getGroupCkAppId());
		statRelated.setGroupChildCkAppId(srd.getGroupChildCkAppId());
		statRelated.setGroupChannel(srd.getGroupChannel());
		statRelated.setGroupChildChannel(srd.getGroupChildChannel());
		return statRelated;
	}
	



	
}
