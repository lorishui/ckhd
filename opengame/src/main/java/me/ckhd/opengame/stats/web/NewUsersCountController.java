package me.ckhd.opengame.stats.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.stats.entity.NewUsersCount;
import me.ckhd.opengame.stats.service.NewUsersCountService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("ck/newUserCount")
public class NewUsersCountController {
	
	Logger log = LoggerFactory.getLogger(NewUsersCountController.class);
	
	@Autowired
	private NewUsersCountService newUserCountService;
	
	@RequestMapping("graph")
	public String graphical(NewUsersCount newUsersCount,Model model){
		if (!Verdict.isAllow(newUsersCount.getCkAppId())) {
			return "403";
		}
		StringBuffer title = new StringBuffer();
		StringBuffer legend_data = new StringBuffer();
		String[] xAxis_data = null;
		String startTime="";
		String endTime="";
		if(StringUtils.isNotBlank(newUsersCount.getStartTime()) && StringUtils.isNotBlank(newUsersCount.getEndTime())){
			startTime = newUsersCount.getStartTime();
			endTime = newUsersCount.getEndTime();
			newUsersCount.setStartTime(startTime.replace("-", ""));
			newUsersCount.setEndTime(endTime.replace("-", ""));
		}else{
			endTime = DateUtils.getDate("yyyy-MM-dd");
			startTime =  DateUtils.formatDate(DateUtils.beforeNumDate(7),"yyyy-MM-dd");
			newUsersCount.setTimeType(2);
			newUsersCount.setStartTime(startTime.replace("-", ""));
			newUsersCount.setEndTime(endTime.replace("-", ""));
		}
		legend_data.append("['今日','昨日','上周同期']");
		xAxis_data = getAllGraph(newUsersCount, xAxis_data);
		//获取数据
		List<NewUsersCount> list = this.newUserCountService.statNew(newUsersCount);
		JSONObject today = getData(list, xAxis_data,1,"今日");
		newUsersCount.setStartTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(startTime),-1),"yyyyMMdd"));
		newUsersCount.setEndTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(endTime),-1),"yyyyMMdd"));
		list = this.newUserCountService.statNew(newUsersCount);
		JSONObject yesterday = getData(list, xAxis_data,1,"昨日");
		newUsersCount.setStartTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(startTime),-7),"yyyyMMdd"));
		newUsersCount.setEndTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(endTime),-7),"yyyyMMdd"));
		list = this.newUserCountService.statNew(newUsersCount);
		JSONObject lastweek = getData(list, xAxis_data,1,"上周同期");
		JSONArray sercies = new JSONArray();
		sercies.add(today);
		sercies.add(yesterday);
		sercies.add(lastweek);
		model.addAttribute("title", title.toString());
		model.addAttribute("legend_data", legend_data.toString());
		model.addAttribute("xAxis_data", getX(xAxis_data));
		model.addAttribute("sercies", sercies);
		newUsersCount.setStartTime(startTime);
		newUsersCount.setEndTime(endTime);
		model.addAttribute("newUsersCount", newUsersCount);
		return "modules/stats/NewUsersCount";
	}
	
	private String[] getAllGraph(NewUsersCount newUsersCount, String[] xAxis_data) {
		//处理时间，groupby 
		String startTime = newUsersCount.getStartTime().replace("-", "").replace(" ", "");
		String endTime = newUsersCount.getEndTime().replace("-", "").replace(" ", "");
		if( newUsersCount.getTimeType() == 1 ){
			newUsersCount.setTimeFt("%Y/%m");
			xAxis_data = getXAxisByStartEnd(startTime, endTime, 1);	
		}
		if( newUsersCount.getTimeType() == 2 ){
			newUsersCount.setTimeFt("%Y/%m/%d");
			xAxis_data = getXAxisByStartEnd(startTime, endTime, 2);
		}
		if( newUsersCount.getTimeType() == 3 ){
			newUsersCount.setTimeFt("%Y/%m/%d %H");
			xAxis_data = getXAxisByStartEnd(startTime, endTime, 3);
		}
		return xAxis_data;
	}
	
	/**
	 * @param year
	 * @param month
	 * @param day
	 * @param type 1:月,2:日,3:小时
	 * @return
	 */
	private String[] getXAxisByStartEnd(String start,String end,int type){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
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
		if( type==1 ){
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
			int c = (int)(cal1.getTimeInMillis() -  cal.getTimeInMillis())/(24*60*60*1000)+1;
			 SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
			 str = new String[c];
			  if( c>0 ){
				  str[0] = sdf1.format(cal.getTime());
				  for(int i=1;i<c;i++){
					  cal.add(Calendar.DAY_OF_YEAR, 1); 
					  str[i] = sdf1.format(cal.getTime());
				  }
			  }
		}else if( type==3 ){
			int c = (int)((cal1.getTimeInMillis() -  cal.getTimeInMillis())/60/60/1000)+24;
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
	public JSONObject getData(List<NewUsersCount> list,String[] time,int dataType,String name){
		int[] data = new int[time.length];
		for( NewUsersCount nuser : list ){
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
		for(int i=0;i<str.length;i++){
			sb.append("'").append(str[i]).append("'").append(",");
		}
		sb.setLength(sb.length()-1);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * 活跃
	 * @param newUsersCount
	 * @param model
	 * @return
	 */
	@RequestMapping("graphAct")
	public String graphicalAct(NewUsersCount newUsersCount,Model model){
		if (!Verdict.isAllow(newUsersCount.getCkAppId())) {
			return "403";
		}
		StringBuffer title = new StringBuffer();
		StringBuffer legend_data = new StringBuffer();
		String[] xAxis_data = null;
		String startTime="";
		String endTime="";
		if(StringUtils.isNotBlank(newUsersCount.getStartTime()) && StringUtils.isNotBlank(newUsersCount.getEndTime())){
			startTime = newUsersCount.getStartTime();
			endTime = newUsersCount.getEndTime();
			newUsersCount.setStartTime(startTime.replace("-", ""));
			newUsersCount.setEndTime(endTime.replace("-", ""));
		}else{
			endTime = DateUtils.getDate("yyyy-MM-dd");
			startTime =  DateUtils.formatDate(DateUtils.beforeNumDate(7),"yyyy-MM-dd");
			newUsersCount.setTimeType(2);
			newUsersCount.setStartTime(startTime.replace("-", ""));
			newUsersCount.setEndTime(endTime.replace("-", ""));
		}
		legend_data.append("['今日','昨日','上周同期']");
		xAxis_data = getAllGraph(newUsersCount, xAxis_data);
		//获取数据
		List<NewUsersCount> list = this.newUserCountService.statAct(newUsersCount);
		JSONObject today = getData(list, xAxis_data,2,"今日");
		newUsersCount.setStartTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(startTime),-1),"yyyyMMdd"));
		newUsersCount.setEndTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(endTime),-1),"yyyyMMdd"));
		list = this.newUserCountService.statAct(newUsersCount);
		JSONObject yesterday = getData(list, xAxis_data,2,"昨日");
		newUsersCount.setStartTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(startTime),-7),"yyyyMMdd"));
		newUsersCount.setEndTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(endTime),-7),"yyyyMMdd"));
		list = this.newUserCountService.statAct(newUsersCount);
		JSONObject lastweek = getData(list, xAxis_data,2,"上周同期");
		JSONArray sercies = new JSONArray();
		sercies.add(today);
		sercies.add(yesterday);
		sercies.add(lastweek);
		model.addAttribute("title", title.toString());
		model.addAttribute("legend_data", legend_data.toString());
		model.addAttribute("xAxis_data", getX(xAxis_data));
		model.addAttribute("sercies", sercies);
		newUsersCount.setStartTime(startTime);
		newUsersCount.setEndTime(endTime);
		model.addAttribute("newUsersCount", newUsersCount);
		return "modules/stats/ActUsersCount";
	}
}
