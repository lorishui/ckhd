package me.ckhd.opengame.stats.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import me.ckhd.opengame.app.utils.AppCkUtils;
import me.ckhd.opengame.app.utils.ChannelUtils;
import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.stats.entity.StatMoney;
import me.ckhd.opengame.stats.entity.StatRelated;
import me.ckhd.opengame.stats.entity.StatRetention;
import me.ckhd.opengame.stats.service.StatMoneyService;
import me.ckhd.opengame.stats.service.StatRelateService;
import me.ckhd.opengame.stats.service.StatRetentionService;
import me.ckhd.opengame.sys.entity.Role;
import me.ckhd.opengame.sys.entity.User;
import me.ckhd.opengame.sys.utils.UserUtils;
import me.ckhd.opengame.util.ExcelUtils;
import me.ckhd.opengame.util.ExcelUtils.Column;
import me.ckhd.opengame.util.ExcelUtils.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@RequestMapping(value = "${adminPath}/stats")
@Controller
public class StatRelateController extends BaseController {
	
	@Autowired
	private StatRelateService statRelateService;
	@Autowired
	private StatMoneyService statMoneyService;
	@Autowired
	private StatRetentionService srdService;
	
	

	@RequestMapping("new")
	public String statNew(StatRelated statRelated,Model model){
		if ( !Verdict.isAllow(statRelated.getCkAppId())) {
			return "403";
		}
		StatRelated param = null;
		List<StatRelated> list = null;
		try {
			param = (StatRelated) statRelated.clone();
			if( StringUtils.isBlank(statRelated.getStartTime()) || StringUtils.isBlank(statRelated.getEndTime()) ){
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
				cal.add(Calendar.DAY_OF_MONTH, 0);
				statRelated.setEndTime(sdf.format(cal.getTime())+"23");
				param.setEndTime(sdf2.format(cal.getTime()));
				
				cal.add(Calendar.DAY_OF_MONTH, -3);
				statRelated.setStartTime(sdf.format(cal.getTime())+"00");
				param.setStartTime(sdf2.format(cal.getTime()));
			}else{
				statRelated.setStartTime(statRelated.getStartTime().replace("-", "")+"00");
				statRelated.setEndTime(statRelated.getEndTime().replace("-", "")+"23");
			}
			//处理条件
			statRelated.setGroupCkAppId(1);
			getSelectStatRelate(statRelated);
			statRelated.setTimeFt("%Y%m%d");
			list = statRelateService.statsNew(statRelated);
		} catch (CloneNotSupportedException e) {
			logger.error("", e);
		}
		model.addAttribute("data", list);
		model.addAttribute("statRelate",param);
		return "modules/stats/statNew";
	}
	
	/**
	 * 网游统计报表页面      以活跃统计页面为基础
	 * @param statRelated
	 * @param model
	 * @param session
	statRelated.setStartTime(statRelated.getStartTime().substring(0, 8));
	 * @return
	 */
	@RequestMapping("act")
	public String statAct(StatRelated statRelated,Model model,HttpSession session){
		if ( !Verdict.isAllow(statRelated.getCkAppId())) {
			return "403";
		}
		StatRelated param = null;
		List<StatRelated> newlist = null;
		List<StatRelated> actlist = null;
		List<StatMoney> moneylist = null;
		List<StatRetention> retentionlist = null;
		
		getPermission(statRelated,session);
		if(hasRole("dataFilter")){
			model.addAttribute("appPerm",1);
		}
		
		try {
			statRelated.setGroupCkAppId(1);
			getSelectStatRelate(statRelated);
			param = (StatRelated) statRelated.clone();
			if( StringUtils.isBlank(statRelated.getStartTime()) || StringUtils.isBlank(statRelated.getEndTime()) ){
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
				cal.add(Calendar.DAY_OF_MONTH, 0);
				statRelated.setEndTime(sdf.format(cal.getTime()));
				param.setEndTime(sdf2.format(cal.getTime()));
				
				cal.add(Calendar.DAY_OF_MONTH, 0);
				statRelated.setStartTime(sdf.format(cal.getTime()));
				param.setStartTime(sdf2.format(cal.getTime()));
				
				param.setGroupChildCkAppId(1);
				statRelated.setGroupChildCkAppId(1);
			}else{
				statRelated.setStartTime(statRelated.getStartTime().replace("-", ""));
				statRelated.setEndTime(statRelated.getEndTime().replace("-", ""));
			}
			//查询新增
			statRelated.setStartTime(statRelated.getStartTime()+"00");
			statRelated.setEndTime(statRelated.getEndTime()+"23");
			statRelated.setTimeFt("%Y%m%d");
			newlist = statRelateService.statsNew(statRelated);
			//查询活跃
			statRelated.setStartTime(statRelated.getStartTime().substring(0, 8));
			statRelated.setEndTime(statRelated.getEndTime().substring(0, 8));
			statRelated.setLength(8);
			statRelated.setAddStr("000000");
			actlist = statRelateService.statsAct(statRelated);
			
			combainData(actlist,newlist);
			
			//  拼装充值数据
			StatMoney statMoney = getStatMoney(statRelated);
			moneylist = statMoneyService.statsMoney(statMoney);
			combainMoneyData(actlist,moneylist);
			
			// 拼装留存数据
			StatRetention sr = getStatRetention(statRelated);
			retentionlist = srdService.statsData(sr);
			combainRetentionData(actlist,retentionlist);
			
			//拼装LTV数据
			StatRetention statLTV = getStatRetention(statRelated);
			retentionlist = srdService.statsLTVData(statLTV);
			combainLTVData(actlist,retentionlist);
			
			StatRelated totalData = getTotalData(actlist);
			model.addAttribute("total", totalData);
		} catch (CloneNotSupportedException e) {
			logger.error("", e);
		}
		model.addAttribute("data", actlist);
		model.addAttribute("statRelate",param);
		return "modules/stats/statAct";
	}
	

	/**
	 * 导出excel：网游统计报表页面      以活跃统计页面为基础
	 * @param statRelated
	 * @param model
	 * @param response
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("actExport")
	public String statActExport(StatRelated statRelated,Model model,HttpServletResponse response, HttpSession session) throws IOException{
		
		statAct(statRelated, model, session);
		
		@SuppressWarnings("unchecked")
		List<StatRelated> actlist = (List<StatRelated>) model.asMap().get("data");
		
		List<Column> cols = new ArrayList<Column>();
		cols.add(new Column().setIndex(cols.size()).setName("ckAppId").setAlias("游戏id"));
		cols.add(new Column().setIndex(cols.size()).setName("childCkAppId").setAlias("游戏子id").setHidden( 1 != statRelated.getGroupChildCkAppId() ));
		cols.add(new Column().setIndex(cols.size()).setName("ckChannelId").setAlias("渠道").setHidden( 1 != statRelated.getGroupChannel() ));
		cols.add(new Column().setIndex(cols.size()).setName("childChannelId").setAlias("子渠道号").setHidden( 1 != statRelated.getGroupChildChannel() ));
		cols.add(new Column().setIndex(cols.size()).setName("timeframes").setAlias("日期"));
		cols.add(new Column().setIndex(cols.size()).setName("newNum").setAlias("新增设备"));
		cols.add(new Column().setIndex(cols.size()).setName("actNum").setAlias("活跃"));
		cols.add(new Column().setIndex(cols.size()).setName("payPeopleNum").setAlias("付费人数"));
		cols.add(new Column().setIndex(cols.size()).setName("money").setAlias("充值金额"));
		cols.add(new Column().setIndex(cols.size()).setName("ffl").setAlias("付费率"));//新增人数里面的充值人数/新增人数
		cols.add(new Column().setIndex(cols.size()).setName("hyffl").setAlias("活跃付费率"));
		cols.add(new Column().setIndex(cols.size()).setName("ltv0").setAlias("新增ARPU"));//ltv0
		cols.add(new Column().setIndex(cols.size()).setName("hy_arpu").setAlias("活跃ARPU"));//arppu
		cols.add(new Column().setIndex(cols.size()).setName("ff_arpu").setAlias("付费ARPU"));//arpu
		cols.add(new Column().setIndex(cols.size()).setName("ltv3").setAlias("LTV3"));
		cols.add(new Column().setIndex(cols.size()).setName("ltv7").setAlias("LTV7"));
		cols.add(new Column().setIndex(cols.size()).setName("ltv14").setAlias("LTV14"));
		cols.add(new Column().setIndex(cols.size()).setName("ltv30").setAlias("LTV30"));
		cols.add(new Column().setIndex(cols.size()).setName("reten1").setAlias("次日留存"));
		cols.add(new Column().setIndex(cols.size()).setName("reten3").setAlias("三日留存"));
		cols.add(new Column().setIndex(cols.size()).setName("reten7").setAlias("七日留存"));
		cols.add(new Column().setIndex(cols.size()).setName("reten14").setAlias("十四日留存"));
		cols.add(new Column().setIndex(cols.size()).setName("reten30").setAlias("月留存"));

		List<Map<String, Object>> data = ExcelUtils.bean2maps(actlist, new ExcelUtils.IConvert<StatRelated>(){
			public void bean2map(StatRelated bean, Map<String, Object> map) {
				DecimalFormat df = new DecimalFormat("0.00");
				map.put("hy_arpu", df.format(bean.getActNum() == 0 ? 0 : bean.getSuccessMoney()/100.0/bean.getActNum()));
				map.put("ff_arpu", df.format(bean.getPaySuccessPeople() == 0 ? 0 : bean.getSuccessMoney()/100.0/bean.getPaySuccessPeople()));
				map.put("hyffl", (df.format(bean.getActNum() == 0 ? 0 : bean.getPaySuccessPeople()*100.0/bean.getActNum())) + "%");
				map.put("ffl", (df.format(bean.getNewNum() == 0 ? 0 : bean.getLtv0NewDevice()*100.0/bean.getNewNum())) + "%");
				
				map.put("money", df.format(bean.getMoney()/100.0));
				
				map.put("ltv0", df.format(bean.getLtv0()/100.0));
				map.put("ltv3", df.format(bean.getLtv3()/100.0));
				map.put("ltv7", df.format(bean.getLtv7()/100.0));
				map.put("ltv14", df.format(bean.getLtv14()/100.0));
				map.put("ltv30", df.format(bean.getLtv30()/100.0));
				
				map.put("reten1", df.format(bean.getNewNum() == 0 ? 0 : bean.getReten1()/100.0/bean.getNewNum()) + "%");
				map.put("reten3", df.format(bean.getNewNum() == 0 ? 0 : bean.getReten3()/100.0/bean.getNewNum()) + "%");
				map.put("reten7", df.format(bean.getNewNum() == 0 ? 0 : bean.getReten7()/100.0/bean.getNewNum()) + "%");
				map.put("reten14", df.format(bean.getNewNum() == 0 ? 0 : bean.getReten14()/100.0/bean.getNewNum()) + "%");
				map.put("reten30", df.format(bean.getNewNum() == 0 ? 0 : bean.getReten30()/100.0/bean.getNewNum()) + "%");
				
				map.put("ckAppId", AppCkUtils.getByCkAppName(bean.getCkAppId()) + "(" + bean.getCkAppId() + ")");
				map.put("childCkAppId", bean.getChildCkAppId() == null ? "" : AppCkUtils.getByChildAppName(bean.getCkAppId(), bean.getChildCkAppId()) + "(" + bean.getChildCkAppId() + ")");
				map.put("ckChannelId", bean.getCkChannelId() == null ? "" : ChannelUtils.findChannelName(bean.getCkChannelId(), "") + "(" + bean.getCkChannelId() + ")" );
			}
		});
		
		Config config = new Config().setDataRowNumber(1).setColumns(cols);
		
		InputStream is = null;
		OutputStream os = null;
		
		try {
			is = getClass().getClassLoader().getResourceAsStream("excel/onlineStatRepTemplate.xlsx");
			
        	response.reset();
        	response.setContentType("application/vnd.ms-excel; charset=utf-8");
        	response.setHeader("Content-Disposition","attachment;filename=onlineStatReport_" + DateUtils.formatDate(new Date(), "yyyyMMddHHmmss") + ".xlsx");
			
        	os = response.getOutputStream();
        	
        	ExcelUtils.write(is, os, config, data);
		}
		finally {
			ExcelUtils.close(is);
			ExcelUtils.close(os);
		}
		
		return null;
	}


	/**
	 * 新增
	 * @param newUsersCount
	 * @param model
	 * @return
	 */
	@RequestMapping("graphNew")
	public String graphicalNew(StatRelated statRelated,Model model){
		if (!Verdict.isAllow(statRelated.getCkAppId())) {
			return "403";
		}
		StringBuffer title = new StringBuffer();
		StringBuffer legend_data = new StringBuffer();
		String[] xAxis_data = null;
		StatRelated param = null;
		try{
			if(statRelated.getTimeStyle() == 0){
				statRelated.setTimeStyle(1);		//默认按小时
			}
			param = (StatRelated) statRelated.clone();
			if(StringUtils.isBlank(statRelated.getStartTime()) || StringUtils.isBlank(statRelated.getEndTime())){
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
				cal.add(Calendar.DAY_OF_MONTH, 0);
				statRelated.setEndTime(sdf.format(cal.getTime())+"23");
				param.setEndTime(sdf2.format(cal.getTime()));
				
				cal.add(Calendar.DAY_OF_MONTH, -3);
				statRelated.setStartTime(sdf.format(cal.getTime())+"00");
				param.setStartTime(sdf2.format(cal.getTime()));
			}else{
				statRelated.setStartTime(statRelated.getStartTime().replace("-", "")+"00");
				statRelated.setEndTime(statRelated.getEndTime().replace("-", "")+"23");
			}
			legend_data.append("['今日','昨日','上周同期']");
			//处理条件
			getSelectStatRelate(statRelated);
			xAxis_data = getAllGraph(statRelated, xAxis_data);
			//获取数据
			List<StatRelated> list = statRelateService.statsNew(statRelated);
			JSONObject today = getData(list, xAxis_data,1,"今日");
			statRelated.setStartTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(param.getStartTime()),-1),"yyyyMMdd")+"00");
			statRelated.setEndTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(param.getEndTime()),-1),"yyyyMMdd")+"23");
			list = this.statRelateService.statsNew(statRelated);
			JSONObject yesterday = getData(list, xAxis_data,1,"昨日");
			statRelated.setStartTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(param.getStartTime()),-7),"yyyyMMdd")+"00");
			statRelated.setEndTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(param.getEndTime()),-7),"yyyyMMdd")+"23");
			list = this.statRelateService.statsNew(statRelated);
			JSONObject lastweek = getData(list, xAxis_data,1,"上周同期");
			statRelated.setStartTime(param.getStartTime().replace("-", "")+"00");
			statRelated.setEndTime(param.getEndTime().replace("-", "")+"23");
			statRelated.setGroupBy("");
			list = this.statRelateService.statsNew(statRelated);
			param.setNewNum(list.get(0).getNewNum());
			JSONArray series = new JSONArray();
			series.add(today);
			series.add(yesterday);
			series.add(lastweek);
			model.addAttribute("series", series);
		}catch(Throwable e){
			logger.error("新增统计异常!", e);
		}
		model.addAttribute("title", title.toString());
		model.addAttribute("legend_data", legend_data.toString());
		model.addAttribute("xAxis_data", getX(xAxis_data));
		model.addAttribute("statRelate", param);
		return "modules/stats/statGraphNew";
	}
	
	/**
	 * 活跃
	 * @param statRelated
	 * @param model
	 * @return
	 */
	@RequestMapping("graphAct")
	public String graphicalAct(StatRelated statRelated,Model model){
		if (!Verdict.isAllow(statRelated.getCkAppId())) {
			return "403";
		}
		StringBuffer title = new StringBuffer();
		StringBuffer legend_data = new StringBuffer();
		String[] xAxis_data = null;
		StatRelated param = null;
		try{
			param = (StatRelated) statRelated.clone();
			if(param.getTimeStyle() == 0){
				param.setTimeStyle(1);		//默认按小时
			}
			if(StringUtils.isBlank(statRelated.getStartTime()) || StringUtils.isBlank(statRelated.getEndTime())){
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
				cal.add(Calendar.DAY_OF_MONTH, 0);
				statRelated.setEndTime(sdf.format(cal.getTime()));
				param.setEndTime(sdf2.format(cal.getTime()));
				
				cal.add(Calendar.DAY_OF_MONTH, -3);
				statRelated.setStartTime(sdf.format(cal.getTime()));
				param.setStartTime(sdf2.format(cal.getTime()));
			}else{
				statRelated.setStartTime(statRelated.getStartTime().replace("-", ""));
				statRelated.setEndTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(statRelated.getEndTime(), "yyyy-MM-dd"), 1),"yyyyMMdd"));
			}
			legend_data.append("['今日','昨日','上周同期']");
			//处理条件
			getSelectStatRelate(statRelated);
			xAxis_data = getAllGraph(param, xAxis_data);
			statRelated.setTimeFt(param.getTimeFt());
			statRelated.setTimeStyle(param.getTimeStyle());
			if( statRelated.getTimeStyle() == 1 ){
				statRelated.setLength(10);
				statRelated.setAddStr("0000");
			}
			if( statRelated.getTimeStyle() == 2 ){
				statRelated.setLength(8);
				statRelated.setAddStr("000000");
			}
			//获取数据
			statRelated.setStartTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(param.getStartTime()),0),"yyyyMMdd")+"00");
			statRelated.setEndTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(param.getEndTime()),0),"yyyyMMdd")+"23");
			fmtTime(statRelated);
			List<StatRelated> list = statRelateService.statsAct(statRelated);
			JSONObject today = getData(list, xAxis_data,2,"今日");
			
			statRelated.setStartTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(param.getStartTime()),-1),"yyyyMMdd")+"00");
			statRelated.setEndTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(param.getEndTime()),-1),"yyyyMMdd")+"23");
			fmtTime(statRelated);
			list = this.statRelateService.statsAct(statRelated);
			JSONObject yesterday = getData(list, xAxis_data,2,"昨日");
			
			statRelated.setStartTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(param.getStartTime()),-7),"yyyyMMdd")+"00");
			statRelated.setEndTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(param.getEndTime()),-7),"yyyyMMdd")+"23");
			fmtTime(statRelated);
			list = this.statRelateService.statsAct(statRelated);
			JSONObject lastweek = getData(list, xAxis_data,2,"上周同期");
			
			statRelated.setStartTime(param.getStartTime().replace("-", "")+"00");
			statRelated.setEndTime(param.getEndTime().replace("-", "")+"23");
			fmtTime(statRelated);
			statRelated.setGroupBy("");
			list = this.statRelateService.statsAct(statRelated);
			param.setActNum(list.get(0).getActNum());
			
			JSONArray series = new JSONArray();
			series.add(today);
			series.add(yesterday);
			series.add(lastweek);
			model.addAttribute("series", series);
		}catch(Throwable e){
			logger.error("活跃统计异常!", e);
		}
		model.addAttribute("title", title.toString());
		model.addAttribute("legend_data", legend_data.toString());
		model.addAttribute("xAxis_data", getX(xAxis_data));
		model.addAttribute("statRelate", param);
		return "modules/stats/statGraphAct";
	}
	
	private void getSelectStatRelate(StatRelated statRelated) {
		if(StringUtils.isNotBlank(statRelated.getChildCkAppId())){
			statRelated.setGroupChildCkAppId(1);
		}
		if(StringUtils.isNotBlank(statRelated.getCkChannelId())){
			statRelated.setGroupChannel(1);
		}
		if( 1 == statRelated.getGroupChildChannel() ){
			statRelated.setGroupBy(statRelated.getGroupBy()+",childChannelId");
			statRelated.setGroupChannel(1);
		}
		if( 1 == statRelated.getGroupChannel() ){
			statRelated.setGroupBy(statRelated.getGroupBy()+",ckChannelId");
		}
		if( 1 == statRelated.getGroupChildCkAppId() ){
			statRelated.setGroupBy(statRelated.getGroupBy()+",childCkAppId");
		}
		if( 1 == statRelated.getGroupCkAppId() ){
			statRelated.setGroupBy(statRelated.getGroupBy()+",ckAppId");
		}
		if( statRelated.getTimeStyle() > 0 ){
			if(statRelated.getTimeStyle() == 2){
				statRelated.setTimeFt("%Y%m%d");
			}
			if(statRelated.getTimeStyle() == 1){
				statRelated.setTimeFt("%Y%m%d%H");
			}
		}
	}
	
	
	
	
	private String[] getAllGraph(StatRelated statRelated, String[] xAxis_data) {
		//处理时间，groupby 
		String startTime = null;
		String endTime = null;
		if( statRelated.getStartTime().replace("-", "").replace(" ", "").length()>=10 ){
			startTime = statRelated.getStartTime().replace("-", "").replace(" ", "").substring(0, 10);
			endTime = statRelated.getEndTime().replace("-", "").replace(" ", "").substring(0, 10);
		}else{
			startTime = statRelated.getStartTime().replace("-", "").replace(" ", "")+"00";
			endTime = statRelated.getEndTime().replace("-", "").replace(" ", "")+"23";
		}
		if( statRelated.getTimeStyle() == 3 ){
			statRelated.setTimeFt("%Y/%m");
			xAxis_data = getXAxisByStartEnd(startTime, endTime, 3);	
		}
		if( statRelated.getTimeStyle() == 2 ){
			statRelated.setTimeFt("%Y/%m/%d");
			xAxis_data = getXAxisByStartEnd(startTime, endTime, 2);
		}
		if( statRelated.getTimeStyle() == 1 ){
			statRelated.setTimeFt("%Y/%m/%d %H");
			xAxis_data = getXAxisByStartEnd(startTime, endTime, 1);
		}
		return xAxis_data;
	}
	
	/**
	 * @param year
	 * @param month
	 * @param day
	 * @param type 1:小时,2:日,3:月
	 * @return
	 */
	private String[] getXAxisByStartEnd(String start,String end,int type){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
		Date date = null;
		Date date1 = null;
		String[] str = null;
		try {
			date = sdf.parse(start);
			date1 = sdf.parse(end);
		} catch (ParseException e) {
			
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		if( type==3 ){
			  int c = ( cal1.get(Calendar.YEAR) - cal.get(Calendar.YEAR)) * 12 
					  + cal1.get(Calendar.MONTH) - cal.get(Calendar.MONTH) + 1;
			  str = new String[c];
			  SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM");
			  if( c>0 ){
				  str[0] = sdf1.format(cal.getTime());
				  for(int i=1;i<c;i++){
					  cal.add(Calendar.MONTH, 1); 
					  str[i] = sdf1.format(cal.getTime());
				  }
			  }
		}else if( type==2 ){
		int c = (int)((cal1.getTimeInMillis() -  cal.getTimeInMillis())/(24*60*60*1000))+1;
			 SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
			 str = new String[c];
			  if( c>0 ){
				  str[0] = sdf1.format(cal.getTime());
				  for(int i=1;i<c;i++){
					  cal.add(Calendar.DAY_OF_YEAR, 1); 
					  str[i] = sdf1.format(cal.getTime());
				  }
			  }
		}else if( type==1 ){
			int c = (int)((cal1.getTimeInMillis() -  cal.getTimeInMillis())/60/60/1000)+1;
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH");
			str = new String[c];
			if( c>0 ){
				str[0] = sdf1.format(cal.getTime());
				for(int i=1;i<c;i++){
					cal.add(Calendar.HOUR_OF_DAY, 1); 
					str[i] = sdf1.format(cal.getTime());
				}
			}
		}
		return str;
	}
	
	/**
	 * 组装数据
	 * @param list
	 * @param time
	 * @param dataType 1:新增,2:活跃
	 * @return
	 */
	public JSONObject getData(List<StatRelated> list,String[] time,int dataType,String name){
		int[] data = new int[time.length];
		for( StatRelated nuser : list ){
			for(int i=0;i<time.length;i++){
				if(name.equals("今日")){
					if(time[i].equals(nuser.getTimeframes())){
						if(dataType == 1){
							data[i]=nuser.getNewNum();
						}
						if(dataType == 2){
							data[i]=nuser.getActNum();
						}
					}
				}else if(name.equals("昨日")){
					if( DateUtils.dateDifference(nuser.getTimeframes(),time[i],1)){
						if(dataType == 1){
							data[i]=nuser.getNewNum();
						}
						if(dataType == 2){
							data[i]=nuser.getActNum();
						}
					}
				}else if(name.equals("上周同期")){
					if( DateUtils.dateDifference(nuser.getTimeframes(),time[i],7) ){
						if(dataType == 1){
							data[i]=nuser.getNewNum();
						}
						if(dataType == 2){
							data[i]=nuser.getActNum();
						}
					}
				}
				if(name.equals("设备活跃")){
					if(time[i].equals(nuser.getTimeframes())){
						if(dataType == 1){
							data[i]=nuser.getActNum();
						}
					}
				}
			}
		}
		JSONObject json = new JSONObject();
		json.put("data", data);
		json.put("type", "line");
		json.put("name", name);
		return json;
	}
	
	private String getX(String[] str) {
		StringBuffer sb = new StringBuffer("[");
		if( str != null ){
			for(int i=0;i<str.length;i++){
				sb.append("'").append(str[i]).append("'").append(",");
			}
			sb.setLength(sb.length()-1);
		}
		sb.append("]");
		return sb.toString();
	}
	

	
	
	private void getPermission(StatRelated statRelated, HttpSession session) {
		//渠道权限
		String channelP = (String) session.getAttribute("channelP");
		if(StringUtils.isBlank(channelP)){
			Set<String> channelPermission = UserUtils.getChannelPermission();
			StringBuilder sb = new StringBuilder("");
			for (String cp : channelPermission) {
				sb.append(cp).append(",");
			}
			channelP = sb.toString();
			if(sb.length() > 0){
				channelP = sb.substring(0, sb.length()-1);
			}
			session.setAttribute("channelP", channelP);
		}
		statRelated.setPermissionChannel(channelP);
		
		//游戏权限
		String ckappP = (String) session.getAttribute("ckappP");
		if(StringUtils.isBlank(ckappP)){
			Set<String> ckappPermission = UserUtils.getGamePermission();
			StringBuilder sb = new StringBuilder("");
			for (String cp : ckappPermission) {
				sb.append(cp).append(",");
			}
			ckappP = sb.toString();
			if(ckappP.length() > 0){
				ckappP = sb.substring(0,sb.length()-1);
			}
			session.setAttribute("ckappP", ckappP);
		}
		statRelated.setPermissionCkAppId(ckappP);
	}
	
	/**
	 * 拼装留存数据
	 * @param actlist
	 * @param retentionlist
	 */
	private void combainRetentionData(List<StatRelated> actlist, List<StatRetention> retentionlist) {
		for (StatRelated act : actlist) {
			for (StatRetention reten : retentionlist) {
				String ckAppId = act.getCkAppId();
				String childCkAppId = act.getChildCkAppId();
				String ckChannelId = act.getCkChannelId();
				String childChannelId = act.getChildChannelId();
				if(act.getTimeframes().equals(reten.getRegTime())
						&& (ckAppId!=null?ckAppId.equals(reten.getCkAppId()):true)
						&& (childCkAppId!=null?childCkAppId.equals(reten.getChildCkAppId()):true)
						&& (ckChannelId!=null?ckChannelId.equals(reten.getCkChannelId()):true)
						&& (childChannelId!=null?childChannelId.equals(reten.getChildChannelId()):true)
							){
						act.setReten1(reten.getD1Num());
						act.setReten3(reten.getD3Num());
						act.setReten7(reten.getD7Num());
						act.setReten14(reten.getD14Num());
						act.setReten30(reten.getD30Num());
					}
			}
		}
	}

	/**
	 * 拼装LTV数据
	 * @param actlist
	 * @param retentionlist
	 */
	private void combainLTVData(List<StatRelated> actlist, List<StatRetention> retentionlist) {
		for (StatRelated act : actlist) {
			for (StatRetention reten : retentionlist) {
				String ckAppId = act.getCkAppId();
				String childCkAppId = act.getChildCkAppId();
				String ckChannelId = act.getCkChannelId();
				String childChannelId = act.getChildChannelId();
				if(act.getTimeframes().equals(reten.getRegTime())
						&& (ckAppId!=null?ckAppId.equals(reten.getCkAppId()):true)
						&& (childCkAppId!=null?childCkAppId.equals(reten.getChildCkAppId()):true)
						&& (ckChannelId!=null?ckChannelId.equals(reten.getCkChannelId()):true)
						&& (childChannelId!=null?childChannelId.equals(reten.getChildChannelId()):true)){
						act.setLtv0(reten.getD0Num());
						act.setLtv1(reten.getD1Num());
						act.setLtv3(reten.getD3Num());
						act.setLtv7(reten.getD7Num());
						act.setLtv14(reten.getD14Num());
						act.setLtv30(reten.getD30Num());
						act.setLtv0NewDevice(reten.getLtv0NewDevice());
					}
			}
		}
	}
	
	private StatRetention getStatRetention(StatRelated statRelated) {
		StatRetention sr = new StatRetention();
		sr.setStartTime(statRelated.getStartTime().substring(0, 8));
		sr.setEndTime(statRelated.getEndTime().substring(0, 8));
		sr.setPermissionCkAppId(statRelated.getPermissionCkAppId());
		sr.setPermissionChannel(statRelated.getPermissionChannel());
		sr.setCkAppId(statRelated.getCkAppId());
		sr.setGroupCkAppId(statRelated.getGroupCkAppId());
		sr.setChildCkAppId(statRelated.getChildCkAppId());
		sr.setGroupChildCkAppId(statRelated.getGroupChildCkAppId());
		sr.setCkChannelId(statRelated.getCkChannelId());
		sr.setGroupChannel(statRelated.getGroupChannel());
		sr.setChildChannelId(statRelated.getChildChannelId());
		sr.setGroupChildChannel(statRelated.getGroupChildChannel());
		if( 1 == sr.getGroupCkAppId() ){
			sr.setGroupBy(sr.getGroupBy()+",ckAppId");
		}
		if( 1 == sr.getGroupChildCkAppId() ){
			sr.setGroupBy(sr.getGroupBy()+",childCkAppId");
		}
		if( 1 == sr.getGroupChannel() ){
			sr.setGroupBy(sr.getGroupBy()+",ckChannelId");
		}
		if( 1 == sr.getGroupChildChannel() ){
			sr.setGroupBy(sr.getGroupBy()+",childChannelId");
		}
		return sr;
	}
	
	/**
	 * 拼装充值数据
	 * @param actlist
	 * @param moneylist
	 */
	private void combainMoneyData(List<StatRelated> actlist, List<StatMoney> moneylist) {
		for (StatRelated sr : actlist) {
			for (StatMoney sm : moneylist) {
				String ckAppId = sr.getCkAppId();
				String childCkAppId = sr.getChildCkAppId();
				String ckChannelId = sr.getCkChannelId();
				String childChannelId = sr.getChildChannelId();
				if(sr.getTimeframes().equals(sm.getTimeframes())
						&& (ckAppId!=null?ckAppId.equals(sm.getCkAppId()):true)
						&& (childCkAppId!=null?childCkAppId.equals(sm.getChildCkAppId()):true)
						&& (ckChannelId!=null?ckChannelId.equals(sm.getCkChannelId()):true)
						&& (childChannelId!=null?childChannelId.equals(sm.getChildChannelId()):true)
							){
						sr.setMoney(sm.getMoney());
						sr.setSuccessMoney(sm.getSuccessMoney());
						sr.setPayPeopleNum(sm.getPayPeopleNum());
						sr.setPaySuccessPeople(sm.getPaySuccessPeople());
						sr.setPayTimes(sm.getPayTimes());
						sr.setPaySuccessTimes(sm.getPaySuccessTimes());
					}
			}
		}
		
	}
	
	private StatMoney getStatMoney(StatRelated statRelated) {
		StatMoney statMoney = new StatMoney();
		statMoney.setStartTime(statRelated.getStartTime().substring(0, 8));
		statMoney.setEndTime(statRelated.getEndTime().substring(0, 8));
		statMoney.setLength(8);
		statMoney.setPermissionCkAppId(statRelated.getPermissionCkAppId());
		statMoney.setPermissionChannel(statRelated.getPermissionChannel());
		statMoney.setCkAppId(statRelated.getCkAppId());
		statMoney.setGroupCkAppId(statRelated.getGroupCkAppId());
		statMoney.setChildCkAppId(statRelated.getChildCkAppId());
		statMoney.setGroupChildCkAppId(statRelated.getGroupChildCkAppId());
		statMoney.setCkChannelId(statRelated.getCkChannelId());
		statMoney.setGroupChannel(statRelated.getGroupChannel());
		statMoney.setChildChannelId(statRelated.getChildChannelId());
		statMoney.setGroupChildChannel(statRelated.getGroupChildChannel());
		statMoney.setTimeFt("%Y%m%d");
		if( 1 == statMoney.getGroupCkAppId() ){
			statMoney.setGroupBy(statMoney.getGroupBy()+",ckAppId");
		}
		if( 1 == statMoney.getGroupChildCkAppId() ){
			statMoney.setGroupBy(statMoney.getGroupBy()+",childCkAppId");
		}
		if( 1 == statMoney.getGroupChannel() ){
			statMoney.setGroupBy(statMoney.getGroupBy()+",ckChannelId");
		}
		if( 1 == statMoney.getGroupChildChannel() ){
			statMoney.setGroupBy(statMoney.getGroupBy()+",childChannelId");
		}
		return statMoney;
	}
	
	/**
	 * 拼装新增与活跃数据
	 * @param actlist
	 * @param newlist
	 */
	private void combainData(List<StatRelated> actlist, List<StatRelated> newlist) {
		for (StatRelated statRelated : actlist) {
			for (StatRelated srd : newlist) {
				String ckAppId = srd.getCkAppId();
				String childCkAppId = srd.getChildCkAppId();
				String ckChannelId = srd.getCkChannelId();
				String childChannelId = srd.getChildChannelId();
				if(statRelated.getTimeframes().equals(srd.getTimeframes())
					&& (ckAppId!=null?ckAppId.equals(statRelated.getCkAppId()):true)
					&& (childCkAppId!=null?childCkAppId.equals(statRelated.getChildCkAppId()):true)
					&& (ckChannelId!=null?ckChannelId.equals(statRelated.getCkChannelId()):true)
					&& (childChannelId!=null?childChannelId.equals(statRelated.getChildChannelId()):true)
						){
					statRelated.setNewNum(srd.getNewNum());
				}
			}
		}
	}

	
	/**
	 * 判断是否去掉   2017010123   中的小时数 
	 * @param statRelated
	 */
	private void fmtTime(StatRelated statRelated) {
		if(statRelated.getTimeStyle() == 2){
			statRelated.setStartTime(statRelated.getStartTime().substring(0,8));
			statRelated.setEndTime(statRelated.getEndTime().substring(0,8));
		}
		
	}
	
	private boolean hasRole(String roleName) {
		User user = UserUtils.getUser();
		List<Role> roleList = user.getRoleList();
		for (Role role : roleList) {
			if(roleName.equals(role.getEnname())){
				return true;
			}
		}
		return false;
	}
	
	private StatRelated getTotalData(List<StatRelated> actlist) {
		StatRelated total = new StatRelated();
		for (StatRelated sr : actlist) {
			total.setNewNum(total.getNewNum()+sr.getNewNum());
			total.setActNum(total.getActNum()+sr.getActNum());
			total.setReten1(total.getReten1()+sr.getReten1());
			total.setReten7(total.getReten7()+sr.getReten7());
			total.setSuccessMoney(total.getSuccessMoney()+sr.getSuccessMoney());
			total.setPaySuccessPeople(total.getPaySuccessPeople()+sr.getPaySuccessPeople());
			total.setPaySuccessTimes(total.getPaySuccessTimes()+sr.getPaySuccessTimes());
		}
		return total;
	}
	
	
}
