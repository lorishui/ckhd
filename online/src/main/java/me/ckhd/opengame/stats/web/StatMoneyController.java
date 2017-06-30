package me.ckhd.opengame.stats.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.stats.entity.StatMoney;
import me.ckhd.opengame.stats.service.StatMoneyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus.Series;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@RequestMapping(value = "${adminPath}/stats")
@Controller
public class StatMoneyController extends BaseController {
	
	@Autowired
	private StatMoneyService statMoneyService;
	

	@RequestMapping("money")
	public String statMoney(StatMoney statMoney,Model model){
		if ( !Verdict.isAllow(statMoney.getCkAppId())) {
			return "403";
		}
		StatMoney param = null;
		List<StatMoney> list = null;
		try {
			param = (StatMoney) statMoney.clone();
			if( StringUtils.isBlank(statMoney.getStartTime()) || StringUtils.isBlank(statMoney.getEndTime()) ){
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
				cal.add(Calendar.DAY_OF_MONTH, 0);
				statMoney.setEndTime(sdf.format(cal.getTime()));
				param.setEndTime(sdf2.format(cal.getTime()));
				
				cal.add(Calendar.DAY_OF_MONTH, -7);
				statMoney.setStartTime(sdf.format(cal.getTime()));
				param.setStartTime(sdf2.format(cal.getTime()));
			}else{
				statMoney.setStartTime(statMoney.getStartTime().replace("-", ""));
				statMoney.setEndTime(statMoney.getEndTime().replace("-", ""));
			}
			//处理条件
			statMoney.setLength(8);
			statMoney.setGroupCkAppId(1);
			getSelectStatRelate(statMoney);
			statMoney.setTimeFt("%Y%m%d");
			list = statMoneyService.statsMoney(statMoney);
		} catch (CloneNotSupportedException e) {
			logger.error("", e);
		}
		model.addAttribute("data", list);
		model.addAttribute("statMoney",param);
		return "modules/stats/statMoney";
	}
	
	private void getSelectStatRelate(StatMoney statMoney) {
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
		if( statMoney.getTimeStyle() > 0 ){
			if(statMoney.getTimeStyle() == 2){
				statMoney.setTimeFt("%Y%m%d");
			}
			if(statMoney.getTimeStyle() == 1){
				statMoney.setTimeFt("%Y%m%d%H");
			}
		}
	}
	
	
	/**
	 * 充值金额图形
	 * @param statMoney
	 * @param model
	 * @return
	 */
	@RequestMapping("graphMoney")
	public String graphicalMoney(StatMoney statMoney,Model model){
		if (!Verdict.isAllow(statMoney.getCkAppId())) {
			return "403";
		}
		StringBuffer title = new StringBuffer();
		StringBuffer legend_data = new StringBuffer();
		String[] xAxis_data = null;
		StatMoney param = null;
		try{
			if(statMoney.getTimeStyle() == 0){
				statMoney.setTimeStyle(1);		//默认按小时
			}
			param = (StatMoney) statMoney.clone();
			if(StringUtils.isBlank(statMoney.getStartTime()) || StringUtils.isBlank(statMoney.getEndTime())){
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
				cal.add(Calendar.DAY_OF_MONTH, 0);
				statMoney.setEndTime(sdf.format(cal.getTime())+"23");
				param.setEndTime(sdf2.format(cal.getTime()));
				
				cal.add(Calendar.DAY_OF_MONTH, -3);
				statMoney.setStartTime(sdf.format(cal.getTime())+"00");
				param.setStartTime(sdf2.format(cal.getTime()));
			}else{
				statMoney.setStartTime(statMoney.getStartTime().replace("-", "")+"00");
				statMoney.setEndTime(statMoney.getEndTime().replace("-", "")+"23");
			}
			legend_data.append("['今日','昨日','上周同期']");
			//处理条件
			getSelectStatRelate(statMoney);
			xAxis_data = getAllGraph(statMoney, xAxis_data);
			//获取数据
			List<StatMoney> list = statMoneyService.statsSuccessMoney(statMoney);
			JSONObject today = getData(list, xAxis_data,1,"今日");
			JSONObject TotalToday = getData(list, xAxis_data,2,"今日");
			statMoney.setStartTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(param.getStartTime()),-1),"yyyyMMdd")+"00");
			statMoney.setEndTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(param.getEndTime()),-1),"yyyyMMdd")+"23");
			list = this.statMoneyService.statsSuccessMoney(statMoney);
			JSONObject yesterday = getData(list, xAxis_data,1,"昨日");
			JSONObject Totalyesterday = getData(list, xAxis_data,2,"昨日");
			statMoney.setStartTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(param.getStartTime()),-7),"yyyyMMdd")+"00");
			statMoney.setEndTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(param.getEndTime()),-7),"yyyyMMdd")+"23");
			list = this.statMoneyService.statsSuccessMoney(statMoney);
			JSONObject lastweek = getData(list, xAxis_data,1,"上周同期");
			JSONObject Totallastweek = getData(list, xAxis_data,2,"上周同期");
			statMoney.setStartTime(param.getStartTime().replace("-", "")+"00");
			statMoney.setEndTime(param.getEndTime().replace("-", "")+"23");
			statMoney.setGroupBy("");
			list = this.statMoneyService.statsSuccessMoney(statMoney);
			if(list.get(0) != null){
				param.setSuccessMoney(list.get(0).getSuccessMoney()/100);
			}else{
				param.setSuccessMoney(0);
			}
			param.setMoney(list.get(0).getMoney()/100);
			JSONArray series = new JSONArray();
			series.add(today);
			series.add(yesterday);
			series.add(lastweek);
			model.addAttribute("series",series);
			JSONArray totalSeries = new JSONArray();
			totalSeries.add(TotalToday);
			totalSeries.add(Totalyesterday);
			totalSeries.add(Totallastweek);
			model.addAttribute("total", totalSeries);
		}catch(Throwable e){
			logger.error("新增统计异常!", e);
		}
		model.addAttribute("title", title.toString());
		model.addAttribute("legend_data", legend_data.toString());
		model.addAttribute("xAxis_data", getX(xAxis_data));
		model.addAttribute("statMoney", param);
		return "modules/stats/statGraphMoney";
	}
	
	/**
	 * 充值金额自动刷新页面
	 * @param statMoney
	 * @param model
	 * @return
	 */
	@RequestMapping("graphMoneyDynamic")
	public String graphicalMoneyDynamic(StatMoney statMoney,Model model){
		if (!Verdict.isAllow(statMoney.getCkAppId())) {
			return "403";
		}
		StringBuffer title = new StringBuffer();
		StringBuffer legend_data = new StringBuffer();
		String[] xAxis_data = null;
		StatMoney param = null;
		try{
			statMoney.setTimeStyle(1);		//默认按小时
			param = (StatMoney) statMoney.clone();
			statMoney.setStartTime(DateUtils.getDate("yyyyMMdd")+"00");
			statMoney.setEndTime(DateUtils.getDate("yyyyMMdd")+"23");
			param.setStartTime(DateUtils.getDate("yyyy-MM-dd"));
			param.setEndTime(DateUtils.getDate("yyyy-MM-dd"));
			legend_data.append("['成功金额','总金额']");
			//处理条件
			getSelectStatRelate(statMoney);
			xAxis_data = getAllGraph(statMoney, xAxis_data);
			//获取数据
			List<StatMoney> list = statMoneyService.statsSuccessMoney(statMoney);
			JSONObject today = getData(list, xAxis_data,1,"今日");
			today.put("name","成功金额");
			JSONObject TotalToday = getData(list, xAxis_data,2,"今日");
			TotalToday.put("name","总金额");
			//获取总金额与成功金额总数
			statMoney.setStartTime(param.getStartTime().replace("-", "")+"00");
			statMoney.setEndTime(param.getEndTime().replace("-", "")+"23");
			statMoney.setGroupBy("");
			list = this.statMoneyService.statsSuccessMoney(statMoney);
			if(list.get(0) != null){
				param.setSuccessMoney(list.get(0).getSuccessMoney()/100);
				param.setMoney(list.get(0).getMoney()/100);
			}else{
				param.setSuccessMoney(0);
				param.setMoney(0);
			}
			JSONArray series = new JSONArray();
			series.add(today);
			series.add(TotalToday);
			model.addAttribute("series",series);
		}catch(Throwable e){
			logger.error("新增统计异常!", e);
		}
		model.addAttribute("title", title.toString());
		model.addAttribute("legend_data", legend_data.toString());
		model.addAttribute("xAxis_data", getX(xAxis_data));
		model.addAttribute("statMoney", param);
		return "modules/stats/statGraphMoneyDynamic";
	}
	
	/**
	 * 充值金额自动刷新页面   获取数据
	 * @param statMoney
	 * @param model
	 * @return
	 */
	@RequestMapping("graphMoneyDynamic/data")
	@ResponseBody
	public String graphicalMoneyDynamicData(@RequestBody String code, HttpServletRequest request,HttpServletResponse response){
		StatMoney statMoney = new StatMoney();
		Map<String, Object> codeMap = MyJsonUtils.jsonStr2Map(code);
		statMoney.setCkAppId(codeMap.get("ckAppId").toString());
		statMoney.setChildCkAppId(codeMap.get("childCkAppId").toString());
		statMoney.setCkChannelId(codeMap.get("ckChannelId").toString());
		
		StringBuffer title = new StringBuffer();
		StringBuffer legend_data = new StringBuffer();
		String[] xAxis_data = null;
		StatMoney param = null;
		JSONObject data = new JSONObject();
		try{
			statMoney.setTimeStyle(1);		//默认按小时
			param = (StatMoney) statMoney.clone();
			statMoney.setStartTime(DateUtils.getDate("yyyyMMdd")+"00");
			statMoney.setEndTime(DateUtils.getDate("yyyyMMdd")+"23");
			param.setStartTime(DateUtils.getDate("yyyy-MM-dd"));
			param.setEndTime(DateUtils.getDate("yyyy-MM-dd"));
			legend_data.append("['成功金额','总金额']");
			//处理条件
			getSelectStatRelate(statMoney);
			xAxis_data = getAllGraph(statMoney, xAxis_data);
			//获取数据
			List<StatMoney> list = statMoneyService.statsSuccessMoney(statMoney);
			JSONObject today = getData(list, xAxis_data,1,"今日");
			today.put("name","成功金额");
			JSONObject TotalToday = getData(list, xAxis_data,2,"今日");
			TotalToday.put("name","总金额");
			//获取总金额与成功金额总数
			statMoney.setStartTime(param.getStartTime().replace("-", "")+"00");
			statMoney.setEndTime(param.getEndTime().replace("-", "")+"23");
			statMoney.setGroupBy("");
			list = this.statMoneyService.statsSuccessMoney(statMoney);
			if(list.get(0) != null){
				param.setSuccessMoney(list.get(0).getSuccessMoney()/100);
			}else{
				param.setSuccessMoney(0);
			}
			JSONArray series = new JSONArray();
			series.add(today);
			series.add(TotalToday);
			
			data.put("series", series);
			data.put("legend_data", legend_data.toString());
			data.put("xAxis_data", getX(xAxis_data));
			data.put("title", title.toString());
		}catch(Throwable e){
			logger.error("新增统计异常!", e);
		}
		return data.toJSONString();
	}
	
	private String[] getAllGraph(StatMoney statMoney, String[] xAxis_data) {
		//处理时间，groupby 
		String startTime = null;
		String endTime = null;
		if( statMoney.getStartTime().replace("-", "").replace(" ", "").length()>=10 ){
			startTime = statMoney.getStartTime().replace("-", "").replace(" ", "").substring(0, 10);
			endTime = statMoney.getEndTime().replace("-", "").replace(" ", "").substring(0, 10);
		}else{
			startTime = statMoney.getStartTime().replace("-", "").replace(" ", "")+"00";
			endTime = statMoney.getEndTime().replace("-", "").replace(" ", "")+"23";
		}
		if( statMoney.getTimeStyle() == 3 ){
			statMoney.setTimeFt("%Y/%m");
			xAxis_data = getXAxisByStartEnd(startTime, endTime, 3);	
		}
		if( statMoney.getTimeStyle() == 2 ){
			statMoney.setTimeFt("%Y/%m/%d");
			xAxis_data = getXAxisByStartEnd(startTime, endTime, 2);
		}
		if( statMoney.getTimeStyle() == 1 ){
			statMoney.setTimeFt("%Y/%m/%d %H");
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
	 * @param dataType 1:成功,2:总金额
	 * @return
	 */
	public JSONObject getData(List<StatMoney> list,String[] time,int dataType,String name){
		int[] data = new int[time.length];
		for( StatMoney nuser : list ){
			for(int i=0;i<time.length;i++){
				if(name.equals("今日")){
					if(time[i].equals(nuser.getTimeframes())){
						if(dataType == 1){
							data[i]=nuser.getSuccessMoney()/100;
						}
						if(dataType == 2){
							data[i]=nuser.getMoney()/100;
						}
					}
				}else if(name.equals("昨日")){
					if( DateUtils.dateDifference(nuser.getTimeframes(),time[i],1)){
						if(dataType == 1){
							data[i]=nuser.getSuccessMoney()/100;
						}
						if(dataType == 2){
							data[i]=nuser.getMoney()/100;
						}
					}
				}else if(name.equals("上周同期")){
					if( DateUtils.dateDifference(nuser.getTimeframes(),time[i],7) ){
						if(dataType == 1){
							data[i]=nuser.getSuccessMoney()/100;
						}
						if(dataType == 2){
							data[i]=nuser.getMoney()/100;
						}
					}
				}
//				if(name.equals("设备活跃")){
//					if(time[i].equals(nuser.getTimeframes())){
//						if(dataType == 1){
//							data[i]=nuser.getActNum();
//						}
//					}
//				}
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
