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
import me.ckhd.opengame.stats.service.StatRoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@RequestMapping(value = "${adminPath}/stats/role")
@Controller
public class StatRoleController extends BaseController {
	
	@Autowired
	private StatRoleService statRoleService;
	

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
				statRelated.setStartTime(DateUtils.getDate("yyyyMMdd")+"00");
				statRelated.setEndTime(DateUtils.getDate("yyyyMMdd")+"23");
				param.setStartTime(DateUtils.getDate("yyyy-MM-dd"));
				param.setEndTime(DateUtils.getDate("yyyy-MM-dd"));
			}else{
				statRelated.setStartTime(statRelated.getStartTime().replace("-", "")+"00");
				statRelated.setEndTime(statRelated.getEndTime().replace("-", "")+"23");
			}
			//处理条件
			statRelated.setGroupCkAppId(1);
			getSelectStatRelate(statRelated);
			statRelated.setTimeFt("%Y%m%d");
			list = statRoleService.statsNew(statRelated);
		} catch (CloneNotSupportedException e) {
			logger.error("", e);
		}
		model.addAttribute("data", list);
		model.addAttribute("statRelate",param);
		return "modules/stats/statNewRole";
	}
	
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
		if( statRelated.getTimeStyle() > -1 ){
			if(statRelated.getTimeStyle() == 2){
				statRelated.setTimeFt("%Y%m%d");
			}
			if(statRelated.getTimeStyle() == 1){
				statRelated.setTimeFt("%Y%m%d%H");
			}
		}
	}
	
	
	/**
	 * 新增
	 * @param newUsersCount
	 * @param model
	 * @return
	 */
	@RequestMapping("graphNew")
	public String graphicalAct(StatRelated statRelated,Model model){
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
				statRelated.setStartTime(DateUtils.getDate("yyyyMMdd")+"00");
				statRelated.setEndTime(DateUtils.getDate("yyyyMMdd")+"23");
				param.setStartTime(DateUtils.getDate("yyyy-MM-dd"));
				param.setEndTime(DateUtils.getDate("yyyy-MM-dd"));
			}else{
				statRelated.setStartTime(statRelated.getStartTime().replace("-", "")+"00");
				statRelated.setEndTime(statRelated.getEndTime().replace("-", "")+"23");
			}
			legend_data.append("['新增角色']");
//			legend_data.append("['今日','昨日','上周同期']");
//			legend_data.append("['今日']");
			//处理条件
			getSelectStatRelate(statRelated);
			xAxis_data = getAllGraph(statRelated, xAxis_data);
			//获取数据
			List<StatRelated> list = statRoleService.statsNew(statRelated);
			JSONObject today = getData(list, xAxis_data,1,"新增角色");
//			statRelated.setStartTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(statRelated.getStartTime()),-1),"yyyyMMdd"));
//			statRelated.setEndTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(statRelated.getEndTime()),-1),"yyyyMMdd"));
//			list = this.statRelateService.statsNew(statRelated);
//			JSONObject yesterday = getData(list, xAxis_data,2,"昨日");
//			statRelated.setStartTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(statRelated.getStartTime()),-7),"yyyyMMdd"));
//			statRelated.setEndTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(statRelated.getEndTime()),-7),"yyyyMMdd"));
//			list = this.statRelateService.statsNew(statRelated);
//			JSONObject lastweek = getData(list, xAxis_data,2,"上周同期");
			JSONArray series = new JSONArray();
			series.add(today);
//			sercies.add(yesterday);
//			sercies.add(lastweek);
			model.addAttribute("series", series);
		}catch(Throwable e){
			logger.error("新增统计异常!", e);
		}
		model.addAttribute("title", title.toString());
		model.addAttribute("legend_data", legend_data.toString());
		model.addAttribute("xAxis_data", getX(xAxis_data));
		model.addAttribute("statRelate", param);
		return "modules/stats/statGraphNewRole";
	}
	
	private String[] getAllGraph(StatRelated statRelated, String[] xAxis_data) {
		//处理时间，groupby 
		String startTime = statRelated.getStartTime().replace("-", "").replace(" ", "").substring(0, 10);
		String endTime = statRelated.getEndTime().replace("-", "").replace(" ", "").substring(0, 10);
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
			int c = (int)((cal1.getTimeInMillis() -  cal.getTimeInMillis())/(24*60*60*1000));
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
			int c = (int)((cal1.getTimeInMillis() -  cal.getTimeInMillis())/60/60/1000);
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
//				if(name.equals("今日")){
//					if(time[i].equals(nuser.getTimeframes())){
//						if(dataType == 1){
//							data[i]=nuser.getNewNum();
//						}
//					/*	if(dataType == 2){
//							data[i]=nuser.getActNum();
//						}*/
//					}
//				}else if(name.equals("昨日")){
//					if( DateUtils.dateDifference(nuser.getTimeframes(),time[i],1)){
//						if(dataType == 1){
//							data[i]=nuser.getNewNum();
//						}
//					/*	if(dataType == 2){
//							data[i]=nuser.getActNum();
//						}*/
//					}
//				}else if(name.equals("上周同期")){
//					if( DateUtils.dateDifference(nuser.getTimeframes(),time[i],7) ){
//						if(dataType == 1){
//							data[i]=nuser.getNewNum();
//						}
//					/*	if(dataType == 2){
//							data[i]=nuser.getActNum();
//						}*/
//					}
//				}
				if(name.equals("新增角色")){
					if(time[i].equals(nuser.getTimeframes())){
						if(dataType == 1){
							data[i]=nuser.getNewNum();
						}
					/*	if(dataType == 2){
							data[i]=nuser.getActNum();
						}*/
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
	
}
