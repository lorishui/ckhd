package me.ckhd.opengame.stats.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.stats.entity.StatRelated;
import me.ckhd.opengame.stats.entity.StatRetention;
import me.ckhd.opengame.stats.service.StatRelateService;
import me.ckhd.opengame.stats.service.StatRetentionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "${adminPath}/stats")
@Controller
public class StatRetentionController extends BaseController {
	
	@Autowired
	private StatRetentionService srdService;
	@Autowired
	private	StatRelateService deviceService;
	
	
	@RequestMapping("retention")
	public String statRetention(StatRetention statRetention,Model model ){
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
		String currentDate = "";
		try {
			StatRelated statRelated =  getStatRelated(statLTV);
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
			List<StatRelated> actList = deviceService.getTotalActNum(statRelated);
			list = srdService.statsLTVData(statLTV);
			clearData(list);
			combainData(list,deviceList);
			combainActData(list, actList);
			
			currentDate = sdf.format(new Date());
		} catch (Exception e) {
			logger.error("", e);
		}
		model.addAttribute("currentDate", currentDate);
		model.addAttribute("data", list);
		model.addAttribute("statLTV",param);
		return "modules/stats/statLTV";
	}
	
	


	private void clearData(List<StatRetention> list) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		for (int i = 0; i < list.size(); i++) {
			StatRetention sr = list.get(i);
			try {
				Date date = sdf.parse(sr.getRegTime());
				Date now = sdf.parse(sdf.format(new Date()));
				long t = (now.getTime()-date.getTime()) / (1000 * 3600 * 24);
				if(t < 0){
					list.get(i).setD0Num(-1);
				}
				if(t <= 1){
					list.get(i).setD1Num(-1);
				}
				if(t <= 2){
					list.get(i).setD2Num(-1);
				}
				if(t <= 3){
					list.get(i).setD3Num(-1);
				}
				if(t <= 4){
					list.get(i).setD4Num(-1);
				}
				if(t <= 5){
					list.get(i).setD5Num(-1);
				}
				if(t <= 6){
					list.get(i).setD6Num(-1);
				}
				if(t <= 7){
					list.get(i).setD7Num(-1);
				}
				if(t <= 14){
					list.get(i).setD14Num(-1);
				}
				if(t <= 30){
					list.get(i).setD30Num(-1);
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
	 * 组装留存数量与活跃总数
	 * @param list
	 * @param actList
	 */
	private void combainActData(List<StatRetention> list, List<StatRelated> actList) {
		for (StatRelated statRelated : actList) {
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
					srd.setActNum(statRelated.getActNum());
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
