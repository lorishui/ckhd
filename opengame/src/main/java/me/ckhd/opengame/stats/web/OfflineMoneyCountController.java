package me.ckhd.opengame.stats.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ckhd.opengame.app.utils.AppCkUtils;
import me.ckhd.opengame.app.utils.ChannelUtils;
import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.stats.entity.OfflineMoneyCount;
import me.ckhd.opengame.stats.service.OfflineMoneyCountSerivce;
import me.ckhd.opengame.sys.utils.DictUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("ck/offlineMoneyCount")
public class OfflineMoneyCountController {
	Logger log = LoggerFactory.getLogger(OfflineMoneyCountController.class);
	
	@Autowired
	private OfflineMoneyCountSerivce offlineMoneyCountService;
	
	@RequestMapping("graph1")
	public String graph1(OfflineMoneyCount offlineMoneyCount,Model model){
		return "modules/stats/offlineMoneyCount1";
	}
	
	/*@RequestMapping("graph")
	public String graph(OfflineMoneyCount offlineMoneyCount,Model model){
		if (!Verdict.isAllow(offlineMoneyCount.getCkAppId())) {
			return "403";
		}
		StringBuffer title = new StringBuffer();
		StringBuffer legend_data = new StringBuffer();
		String xAxis_data = "";
		JSONArray sercies = new JSONArray();
		if( StringUtils.isNotBlank(offlineMoneyCount.getTimeArea()) ){
			xAxis_data = getAllGraph(offlineMoneyCount, title, xAxis_data);
			
			//获取数据
			List<OfflineMoneyCount> list = this.offlineMoneyCountService.stat(offlineMoneyCount);
			Map<String,Object> map = getService(list, offlineMoneyCount, xAxis_data);
			sercies = (JSONArray) map.get("sercies");
			legend_data.append(map.get("legend_data").toString());
		}
		model.addAttribute("title", title.toString());
		model.addAttribute("legend_data", legend_data.toString());
		model.addAttribute("xAxis_data", xAxis_data);
		model.addAttribute("sercies", sercies);
		model.addAttribute("offlineMoneyCount", offlineMoneyCount);
		return "modules/stats/offlineMoneyCount";
	}*/
	
	/**
	 * 按小时统计
	 * @param offlineMoneyCount
	 * @param model
	 * @return
	 */
	@RequestMapping("graphHour")
	public String graphHour(OfflineMoneyCount offlineMoneyCount,Model model){
		if (!Verdict.isAllow(offlineMoneyCount.getCkAppId())) {
			return "403";
		}
		StringBuffer title = new StringBuffer();
		StringBuffer legend_data = new StringBuffer();
		String xAxis_data = "[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23]";
		JSONArray sercies = new JSONArray();
		if( StringUtils.isBlank(offlineMoneyCount.getTimeArea()) ){
			offlineMoneyCount.setTimeArea(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
			offlineMoneyCount.setTimeType(3);
		}
		if( StringUtils.isNotBlank(offlineMoneyCount.getTimeArea()) ){
			//处理时间，groupby 
			if(StringUtils.isNotBlank(offlineMoneyCount.getCkAppId())){
				title.append(AppCkUtils.getByCkAppName(offlineMoneyCount.getCkAppId()));
			}else{
				title.append("全部游戏");
			}
			if(StringUtils.isNotBlank(offlineMoneyCount.getCkChannelId())){
				title.append(ChannelUtils.findChannelName(offlineMoneyCount.getCkChannelId(), ""));
			}
			if(StringUtils.isNotBlank(offlineMoneyCount.getCarriersChannelId())){
				title.append("_").append(offlineMoneyCount.getCarriersChannelId());
			}
			if(StringUtils.isNotBlank(offlineMoneyCount.getProvince())){
				title.append(DictUtils.getDictLabel(offlineMoneyCount.getProvince(), "province", ""));
			}
			String time = offlineMoneyCount.getTimeArea().replace("-", "").replace(" ", "");
			title.append(time.substring(0, 4)).append("年");
			title.append(time.substring(4, 6)).append("月");
			title.append(time.substring(6, 8)).append("日");
			offlineMoneyCount.setStartTime(time.substring(0, 8)+"00");
			offlineMoneyCount.setEndTime(time.substring(0, 8)+"23");
			
			//获取数据
			List<OfflineMoneyCount> list = this.offlineMoneyCountService.statHour(offlineMoneyCount);
			Map<String,Object> map = getServiceByTotalAndSuccess(list, offlineMoneyCount, xAxis_data,3,"");
			sercies = (JSONArray) map.get("sercies");
			legend_data.append(map.get("legend_data").toString());
			if(offlineMoneyCount.getTimeType()==4){
				//上周数据
				String lastTime = getLastWeek(offlineMoneyCount.getTimeArea()).replace("-", "").replace(" ", "");
				offlineMoneyCount.setStartTime(lastTime+"00");
				offlineMoneyCount.setEndTime(lastTime+"23");
				list = this.offlineMoneyCountService.statHour(offlineMoneyCount);
				map = getServiceByTotalAndSuccess(list, offlineMoneyCount, xAxis_data,3,"上周_");
				JSONArray j1 = (JSONArray) map.get("sercies");
				legend_data.setLength(legend_data.length()-1);
				legend_data.append(",").append((map.get("legend_data").toString().substring(1)));
				sercies.addAll(j1);
			}
		}
		model.addAttribute("title", title.toString());
		model.addAttribute("legend_data", legend_data.toString());
		model.addAttribute("xAxis_data", xAxis_data);
		model.addAttribute("sercies", sercies);
		model.addAttribute("offlineMoneyCount", offlineMoneyCount);
		return "modules/stats/HourMoneyCount";
	}
	
	/**
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @param type 1:年,2:月
	 * @return
	 */
	/*private String getXAxisStr(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			log.error(this.getClass()+" getDateFormat error!", e);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int num = cal.getActualMaximum(Calendar.DATE);
		StringBuffer sb = new StringBuffer();
		for(int i=1;i<=num;i++){
			sb.append(i).append(",");
		}
		sb.setLength(sb.length()-1);
		return sb.toString();
	}*/
	
	/*[{
        name: '销量',
        type: 'line',
        data: [5, 20, 36, 10, 10, 20]
    	},{
    		name: '支出',
            type: 'bar',
            data: [15, 0, 6, 30, 30, 20]
    	}]*/
	/**
	 * 组装数据
	 * @param list
	 * @param offlineMoneyCount
	 * @param xAxis_data
	 * @return
	 */
	/*private Map<String,Object> getService(List<OfflineMoneyCount> list,OfflineMoneyCount offlineMoneyCount,String xAxis_data){
		String[] str = xAxis_data.split(",");
		Map<String,Object> data = new HashMap<String, Object>();
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> nameMap = new HashMap<String, Object>();
		String time = "";
		if(offlineMoneyCount.getTimeType() == 1){
			time = offlineMoneyCount.getStartTime().substring(0, 6);
		}
		if(offlineMoneyCount.getTimeType() == 2){
			time = offlineMoneyCount.getStartTime().substring(0, 8);
		}
		if(offlineMoneyCount.getTimeType() == 3){
			time = offlineMoneyCount.getStartTime().substring(0, 10);
		}
		int n = Integer.parseInt(time);
		for( OfflineMoneyCount off : list ){
			String key = off.getCkAppId();
			String name = AppCkUtils.getByCkAppName(off.getCkAppId());
			if(offlineMoneyCount.getGroupChannel() == 1){
				key += "_"+off.getCkChannelId();
				name += "_" + ChannelUtils.findChannelName(off.getCkChannelId(), "");
			}
			if(offlineMoneyCount.getGroupCarriesChannel() == 1){
				key += "_"+off.getCarriersChannelId();
				name += "_" +off.getCarriersChannelId();
			}
			if(offlineMoneyCount.getGroupProvince() == 1){
				key += "_"+off.getProvince();
				name += "_" +DictUtils.getDictLabel(off.getProvince(), "province", "");
			}
			if(!map.containsKey(key)){
				map.put(key, new int[str.length]);
				nameMap.put(key, name);
			}
			int m = Integer.parseInt(off.getTimeframe());
			((int[])map.get(key))[m-n] = off.getTotalMoney();
		}
		JSONArray jsonA = new JSONArray();
		Object[] obj = map.keySet().toArray();
		StringBuffer sb = new StringBuffer("[");
		for(int i=0;i<obj.length;i++){
			JSONObject json = new JSONObject();
			json.put("data", (int[])map.get(obj[i].toString()));
			json.put("type", "line");
			json.put("name", nameMap.get(obj[i]));
			sb.append("'").append(nameMap.get(obj[i])).append("'").append(",");
			jsonA.add(json);
		}
		sb.setLength(sb.length()>1?sb.length()-1:sb.length());
		sb.append("]");
		data.put("sercies", jsonA);
		data.put("legend_data", sb.toString());		
		return data;
	}*/
	
	/**
	 *  组装数据 successMoney totalMoney
	 * @param list
	 * @param offlineMoneyCount
	 * @param xAxis_data
	 * @param type
	 * @return
	 */
	private Map<String,Object> getServiceByTotalAndSuccess(List<OfflineMoneyCount> list,
			OfflineMoneyCount offlineMoneyCount,String xAxis_data,int type,String prifix){
		String[] str = xAxis_data.split(",");
		Map<String,Object> data = new HashMap<String, Object>();
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> mapSuccess = new HashMap<String, Object>();
		Map<String,Object> nameMap = new HashMap<String, Object>();
		String time = "";
		if(type == 1){
			time = offlineMoneyCount.getStartTime().substring(0, 6);
		}
		if(type == 2){
			time = offlineMoneyCount.getStartTime().substring(0, 8);
		}
		if(type == 3){
			time = offlineMoneyCount.getStartTime().substring(0, 10);
		}
		int n = Integer.parseInt(time);
		for( OfflineMoneyCount off : list ){
			String key = prifix;
			String name = prifix;
			if(offlineMoneyCount.getGroupApp() == 1){
				key += "_"+off.getCkAppId();
				name += AppCkUtils.getByCkAppName(off.getCkAppId());
			}
			if(offlineMoneyCount.getGroupChannel() == 1){
				key += "_"+off.getCkChannelId();
				name += "_" + ChannelUtils.findChannelName(off.getCkChannelId(), "");
			}
			if(offlineMoneyCount.getGroupCarriesChannel() == 1){
				key += "_"+off.getCarriersChannelId();
				name += "_" +off.getCarriersChannelId();
			}
			if(offlineMoneyCount.getGroupProvince() == 1){
				key += "_"+off.getProvince();
				name += "_" +DictUtils.getDictLabel(off.getProvince(), "province", "");
			}
			if(!map.containsKey(key)){
				map.put(key, new int[str.length]);
				mapSuccess.put(key, new int[str.length]);
				nameMap.put(key, name);
			}
			int m = Integer.parseInt(off.getTimeframe());
			((int[])map.get(key))[m-n] = off.getTotalMoney();
			((int[])mapSuccess.get(key))[m-n] = off.getSuccessMoney();
		}
		JSONArray jsonA = new JSONArray();
		Object[] obj = map.keySet().toArray();
		StringBuffer sb = new StringBuffer("[");
		for(int i=0;i<obj.length;i++){
			JSONObject json = new JSONObject();
			json.put("data", (int[])map.get(obj[i].toString()));
			json.put("type", "line");
			json.put("yAxisIndex", 0);
			json.put("name", nameMap.get(obj[i])+"总金额");
			sb.append("'").append(nameMap.get(obj[i])).append("总金额").append("'").append(",");
			jsonA.add(json);
			JSONObject j1 = new JSONObject();
			j1.put("data", (int[])mapSuccess.get(obj[i].toString()));
			j1.put("type", "line");
			j1.put("name", nameMap.get(obj[i])+"成功金额");
			sb.append("'").append(nameMap.get(obj[i])).append("成功金额").append("'").append(",");
			getYRight(j1);
			jsonA.add(j1);
		}
		sb.setLength(sb.length()>1?sb.length()-1:sb.length());
		sb.append("]");
		data.put("sercies", jsonA);
		data.put("legend_data", sb.toString());		
		return data;
	}
	
	
	/**
	 *  组装数据 successMoney totalMoney
	 * @param list
	 * @param offlineMoneyCount
	 * @param xAxis_data
	 * @param type
	 * @return
	 */
	/*private Map<String,Object> getServiceBySuccess(List<OfflineMoneyCount> list,
			OfflineMoneyCount offlineMoneyCount,String xAxis_data){
		String[] str = xAxis_data.split(",");
		Map<String,Object> data = new HashMap<String, Object>();
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> nameMap = new HashMap<String, Object>();
		String time = "";
		int n = Integer.parseInt(time);
		for( OfflineMoneyCount off : list ){
			String key = "";
			String name = "";
			if(offlineMoneyCount.getGroupApp() == 1){
				key += "_"+off.getCkAppId();
				name += AppCkUtils.getByCkAppName(off.getCkAppId());
			}
			if(offlineMoneyCount.getGroupChannel() == 1){
				key += "_"+off.getCkChannelId();
				name += "_" + ChannelUtils.findChannelName(off.getCkChannelId(), "");
			}
			if(offlineMoneyCount.getGroupCarriesChannel() == 1){
				key += "_"+off.getCarriersChannelId();
				name += "_" +off.getCarriersChannelId();
			}
			if(offlineMoneyCount.getGroupProvince() == 1){
				key += "_"+off.getProvince();
				name += "_" +DictUtils.getDictLabel(off.getProvince(), "province", "");
			}
			if(!map.containsKey(key)){
				map.put(key, new int[str.length]);

				nameMap.put(key, name);
			}
			int m = Integer.parseInt(off.getTimeframe());
			((int[])map.get(key))[m-n] = off.getSuccessMoney();
		}
		JSONArray jsonA = new JSONArray();
		Object[] obj = map.keySet().toArray();
		StringBuffer sb = new StringBuffer("[");
		for(int i=0;i<obj.length;i++){
			JSONObject json = new JSONObject();
			json.put("data", (int[])map.get(obj[i].toString()));
			json.put("type", "line");
			json.put("name", nameMap.get(obj[i])+"总金额");
			sb.append("'").append(nameMap.get(obj[i])).append("总金额").append("'").append(",");
			jsonA.add(json);
		}
		sb.setLength(sb.length()>1?sb.length()-1:sb.length());
		sb.append("]");
		data.put("sercies", jsonA);
		data.put("legend_data", sb.toString());		
		return data;
	}*/
	
	private void getYRight(JSONObject json){
		json.put("yAxisIndex", 0);
		/*smooth: true,
	   	 lineStyle:{
	   		 normal:{
	   			 type:'dotted'
	   		 } 
	   	 },*/
		json.put("smooth", true);
		JSONObject js = new JSONObject();
		JSONObject j = new JSONObject();
		j.put("type", "dotted");
		js.put("normal", j);
		json.put("lineStyle", js);
	}
	/*private String getLastDay(String time){
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = sdf.parse(time);
			Calendar cld = Calendar.getInstance();
			cld.setTime(date);
			int num = cld.getMaximum(Calendar.DAY_OF_MONTH);
			if( num < 10){
				sb.append(0).append(num);
			}else{
				sb.append(num);
			}
		} catch (ParseException e) {
			log.error("getLastDay is ERROR!",e);
		}		
		return sb.toString();
	}*/
	
	private String getLastWeek(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cld = null;
		try {
			Date date = sdf.parse(time);
			cld = Calendar.getInstance();
			cld.setTime(date);
			cld.add(Calendar.WEEK_OF_YEAR, -1);
		} catch (ParseException e) {
			log.error("getLastDay is ERROR!",e);
		}		
		return sdf.format(cld.getTime());
	}
	
	@RequestMapping("graphical")
	public String graphical(OfflineMoneyCount offlineMoneyCount,Model model){
		if (!Verdict.isAllow(offlineMoneyCount.getCkAppId())) {
			return "403";
		}
		StringBuffer title = new StringBuffer();
		StringBuffer legend_data = new StringBuffer();
		String[] xAxis_data = null;
		String startTime="";
		String endTime="";
		offlineMoneyCount.setcType("mm");
		if(StringUtils.isNotBlank(offlineMoneyCount.getStartTime()) && StringUtils.isNotBlank(offlineMoneyCount.getEndTime())){
			startTime = offlineMoneyCount.getStartTime();
			endTime = offlineMoneyCount.getEndTime();
			offlineMoneyCount.setStartTime(startTime.replace("-", ""));
			offlineMoneyCount.setEndTime(endTime.replace("-", ""));
		}else{
			endTime = DateUtils.getDate("yyyy-MM-dd");
			startTime =  DateUtils.formatDate(DateUtils.beforeNumDate(7),"yyyy-MM-dd");
			offlineMoneyCount.setTimeType(2);
			offlineMoneyCount.setStartTime(startTime.replace("-", ""));
			offlineMoneyCount.setEndTime(endTime.replace("-", ""));
		}
		legend_data.append("['今日','昨日','上周同期']");
		xAxis_data = getAllGraph(offlineMoneyCount, xAxis_data);
		//获取数据
		List<OfflineMoneyCount> list = this.offlineMoneyCountService.statMoeny(offlineMoneyCount);
		JSONObject today = getData(list, xAxis_data,1,"今日");
		JSONObject todaySuccess = getData(list, xAxis_data,2,"今日");
		offlineMoneyCount.setStartTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(startTime),-1),"yyyyMMdd"));
		offlineMoneyCount.setEndTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(endTime),-1),"yyyyMMdd"));
		list = this.offlineMoneyCountService.statMoeny(offlineMoneyCount);
		JSONObject yesterday = getData(list, xAxis_data,1,"昨日");
		JSONObject yesterdaySuccess = getData(list, xAxis_data,2,"昨日");
		offlineMoneyCount.setStartTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(startTime),-7),"yyyyMMdd"));
		offlineMoneyCount.setEndTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(endTime),-7),"yyyyMMdd"));
		list = this.offlineMoneyCountService.statMoeny(offlineMoneyCount);
		JSONObject lastweek = getData(list, xAxis_data,1,"上周同期");
		JSONObject lastweekSuccess = getData(list, xAxis_data,2,"上周同期");
		JSONArray sercies = new JSONArray();
		JSONArray serciesSuccess = new JSONArray();
		sercies.add(today);
		sercies.add(yesterday);
		sercies.add(lastweek);
		serciesSuccess.add(todaySuccess);
		serciesSuccess.add(yesterdaySuccess);
		serciesSuccess.add(lastweekSuccess);
		model.addAttribute("title", title.toString());
		model.addAttribute("legend_data", legend_data.toString());
		model.addAttribute("xAxis_data", getX(xAxis_data));
		model.addAttribute("sercies", sercies);
		model.addAttribute("serciesSuccess", serciesSuccess);
		offlineMoneyCount.setStartTime(startTime);
		offlineMoneyCount.setEndTime(endTime);
		return "modules/stats/MMAndMoneyCount";
	}

	private String[] getAllGraph(OfflineMoneyCount offlineMoneyCount, String[] xAxis_data) {
		//处理时间，groupby 
		String startTime = offlineMoneyCount.getStartTime().replace("-", "").replace(" ", "");
		String endTime = offlineMoneyCount.getEndTime().replace("-", "").replace(" ", "");
		if( offlineMoneyCount.getTimeType() == 1 ){
			offlineMoneyCount.setTimeFt("%Y/%m");
			xAxis_data = getXAxisByStartEnd(startTime, endTime, 1);	
		}
		if( offlineMoneyCount.getTimeType() == 2 ){
			offlineMoneyCount.setTimeFt("%Y/%m/%d");
			xAxis_data = getXAxisByStartEnd(startTime, endTime, 2);
		}
		if( offlineMoneyCount.getTimeType() == 3 ){
			offlineMoneyCount.setTimeFt("%Y/%m/%d %H");
			xAxis_data = getXAxisByStartEnd(startTime, endTime, 3);
		}
		return xAxis_data;
	}
	
	/**
	 * 
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
			int c = (int)((cal1.getTimeInMillis() -  cal.getTimeInMillis())/(24*60*60*1000)+1);
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
	 * @param dataType 1:总金额,2:成功金额
	 * @return
	 */
	public JSONObject getData(List<OfflineMoneyCount> list,String[] time,int dataType,String name){
		double[] data = new double[time.length];
		for( OfflineMoneyCount off : list ){
			for(int i=0;i<time.length;i++){
				if(name.equals("今日")){
					if(time[i].equals(off.getTimeframes())){
						if(dataType==1){
							data[i]=off.getTotalMoney()/100.00d;
						}else if(dataType==2){
							data[i]=off.getSuccessMoney()/100.00d;
						}
					}
				}else if(name.equals("昨日")){
					if( DateUtils.dateDifference(off.getTimeframes(),time[i],1)){
						if(dataType==1){
							data[i]=off.getTotalMoney()/100.00d;
						}else if(dataType==2){
							data[i]=off.getSuccessMoney()/100.00d;
						}
					}
				}else if(name.equals("上周同期")){
					if( DateUtils.dateDifference(off.getTimeframes(),time[i],7) ){
						if(dataType==1){
							data[i]=off.getTotalMoney()/100.00d;
						}else if(dataType==2){
							data[i]=off.getSuccessMoney()/100.00d;
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
	
	@RequestMapping("andGraphical")
	public String andGraphical(OfflineMoneyCount offlineMoneyCount,Model model){
		if (!Verdict.isAllow(offlineMoneyCount.getCkAppId())) {
			return "403";
		}
		StringBuffer title = new StringBuffer();
		StringBuffer legend_data = new StringBuffer();
		String[] xAxis_data = null;
		String startTime="";
		String endTime="";
		offlineMoneyCount.setcType("and");
		if(StringUtils.isNotBlank(offlineMoneyCount.getStartTime()) && StringUtils.isNotBlank(offlineMoneyCount.getEndTime())){
			startTime = offlineMoneyCount.getStartTime();
			endTime = offlineMoneyCount.getEndTime();
			offlineMoneyCount.setStartTime(startTime.replace("-", ""));
			offlineMoneyCount.setEndTime(endTime.replace("-", ""));
		}else{
			endTime = DateUtils.getDate("yyyy-MM-dd");
			startTime =  DateUtils.formatDate(DateUtils.beforeNumDate(7),"yyyy-MM-dd");
			offlineMoneyCount.setTimeType(2);
			offlineMoneyCount.setStartTime(startTime.replace("-", ""));
			offlineMoneyCount.setEndTime(endTime.replace("-", ""));
		}
		legend_data.append("['今日','昨日','上周同期']");
		xAxis_data = getAllGraph(offlineMoneyCount, xAxis_data);
		//获取数据
		List<OfflineMoneyCount> list = this.offlineMoneyCountService.statMoeny(offlineMoneyCount);
		JSONObject today = getData(list, xAxis_data,1,"今日");
		JSONObject todaySuccess = getData(list, xAxis_data,2,"今日");
		offlineMoneyCount.setStartTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(startTime),-1),"yyyyMMdd"));
		offlineMoneyCount.setEndTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(endTime),-1),"yyyyMMdd"));
		list = this.offlineMoneyCountService.statMoeny(offlineMoneyCount);
		JSONObject yesterday = getData(list, xAxis_data,1,"昨日");
		JSONObject yesterdaySuccess = getData(list, xAxis_data,2,"昨日");
		offlineMoneyCount.setStartTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(startTime),-7),"yyyyMMdd"));
		offlineMoneyCount.setEndTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(endTime),-7),"yyyyMMdd"));
		list = this.offlineMoneyCountService.statMoeny(offlineMoneyCount);
		JSONObject lastweek = getData(list, xAxis_data,1,"上周同期");
		JSONObject lastweekSuccess = getData(list, xAxis_data,2,"上周同期");
		JSONArray sercies = new JSONArray();
		JSONArray serciesSuccess = new JSONArray();
		sercies.add(today);
		sercies.add(yesterday);
		sercies.add(lastweek);
		serciesSuccess.add(todaySuccess);
		serciesSuccess.add(yesterdaySuccess);
		serciesSuccess.add(lastweekSuccess);
		model.addAttribute("title", title.toString());
		model.addAttribute("legend_data", legend_data.toString());
		model.addAttribute("xAxis_data", getX(xAxis_data));
		model.addAttribute("sercies", sercies);
		model.addAttribute("serciesSuccess", serciesSuccess);
		offlineMoneyCount.setStartTime(startTime);
		offlineMoneyCount.setEndTime(endTime);
		return "modules/stats/AndMoneyCount";
	}
	
	/**
	 * MM统计-按天查看
	 * @param offlineMoneyCount
	 * @param model
	 * @return
	 */
	@RequestMapping("graphMMByDay")
	public String graphMMByDay(OfflineMoneyCount offlineMoneyCount,Model model){
		if (!Verdict.isAllow(offlineMoneyCount.getCkAppId())) {
			return "403";
		}
		StringBuffer title = new StringBuffer();
		StringBuffer legend_data = new StringBuffer();
		String[] xAxis_data = null;
		String startTime="";
		String endTime="";
		offlineMoneyCount.setcType("mm");
		if( StringUtils.isNotBlank(offlineMoneyCount.getStartTime()) ){
			startTime = offlineMoneyCount.getStartTime();
			endTime = offlineMoneyCount.getStartTime();
			offlineMoneyCount.setStartTime(startTime.replace("-", ""));
			offlineMoneyCount.setEndTime(startTime.replace("-", ""));
		}else{
			startTime = DateUtils.getDate("yyyy-MM-dd");
			endTime = DateUtils.getDate("yyyy-MM-dd");
//			startTime =  DateUtils.formatDate(DateUtils.beforeNumDate(7),"yyyy-MM-dd");
			offlineMoneyCount.setTimeType(3);
			offlineMoneyCount.setStartTime(endTime.replace("-", ""));
			offlineMoneyCount.setEndTime(endTime.replace("-", ""));
		}
		legend_data.append("['今日','昨日','上周同期']");
		xAxis_data = getAllGraph(offlineMoneyCount, xAxis_data);
		//获取数据
		List<OfflineMoneyCount> list = this.offlineMoneyCountService.statMoeny(offlineMoneyCount);
		OfflineMoneyCount total = this.offlineMoneyCountService.statTotalMoeny(offlineMoneyCount);
		OfflineMoneyCount todayOff = null;
		try {
			todayOff = offlineMoneyCount.cloneObject();
			todayOff.setEndTime(getLastHourDayTime(todayOff.getStartTime()));
			todayOff.setStartTime(todayOff.getEndTime().substring(0, 8)+"00");
		} catch (CloneNotSupportedException e) {
			log.error("OfflineMoneyCount clone failure", e);
		}
		OfflineMoneyCount totalHour = this.offlineMoneyCountService.statTotalMoenyByHour(todayOff);
		JSONObject today = getData(list, xAxis_data,1,"今日");
		JSONObject todaySuccess = getData(list, xAxis_data,2,"今日");
		OfflineMoneyCount yester = null;
		try {
			yester = offlineMoneyCount.cloneObject();
			yester.setEndTime(getLastWeekDayTime(yester.getStartTime()));
			yester.setStartTime(yester.getEndTime().substring(0, 8)+"00");
		} catch (CloneNotSupportedException e) {
			log.error("OfflineMoneyCount clone failure", e);
		}
		offlineMoneyCount.setStartTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(startTime),-1),"yyyyMMdd"));
		offlineMoneyCount.setEndTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(endTime),-1),"yyyyMMdd"));
		list = this.offlineMoneyCountService.statMoeny(offlineMoneyCount);
		OfflineMoneyCount yesterDayTotal = this.offlineMoneyCountService.statTotalMoenyByHour(yester);
		JSONObject yesterday = getData(list, xAxis_data,1,"昨日");
		JSONObject yesterdaySuccess = getData(list, xAxis_data,2,"昨日");
		offlineMoneyCount.setStartTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(startTime),-7),"yyyyMMdd"));
		offlineMoneyCount.setEndTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(endTime),-7),"yyyyMMdd"));
		list = this.offlineMoneyCountService.statMoeny(offlineMoneyCount);
		JSONObject lastweek = getData(list, xAxis_data,1,"上周同期");
		JSONObject lastweekSuccess = getData(list, xAxis_data,2,"上周同期");
		JSONArray sercies = new JSONArray();
		JSONArray serciesSuccess = new JSONArray();
		sercies.add(today);
		sercies.add(yesterday);
		sercies.add(lastweek);
		serciesSuccess.add(todaySuccess);
		serciesSuccess.add(yesterdaySuccess);
		serciesSuccess.add(lastweekSuccess);
		model.addAttribute("title", title.toString());
		model.addAttribute("legend_data", legend_data.toString());
		model.addAttribute("xAxis_data", getX(xAxis_data));
		model.addAttribute("sercies", sercies);
		model.addAttribute("serciesSuccess", serciesSuccess);
		model.addAttribute("total", total==null?new OfflineMoneyCount():total);
		model.addAttribute("totalHour", totalHour==null?new OfflineMoneyCount():totalHour);
		model.addAttribute("yesterDayTotal", yesterDayTotal==null?new OfflineMoneyCount():yesterDayTotal);
		offlineMoneyCount.setStartTime(startTime);
		return "modules/stats/MMByDayMoneyCount";
	}
	
	private String getLastWeekDayTime(String dateStr){//yyyyMMdd
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date start = null;
		try {
			start = sdf.parse(dateStr);
		} catch (ParseException e) {
			log.error("获取昨天时刻数据错误！", e);
		}
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(start);
		if( cal.getTimeInMillis() - cal2.getTimeInMillis() > 24*60*60*1000 ){
			cal2.add(Calendar.DAY_OF_YEAR, -7);
			return sdf.format(cal2.getTime())+"23";
		}else{
			if(cal.get(Calendar.HOUR_OF_DAY) > 0){
				cal.add(Calendar.HOUR_OF_DAY, -1);
			}
			cal.add(Calendar.DAY_OF_YEAR, -7);
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHH");
			return sdf2.format(cal.getTime());
		}
	}
	
	private String getLastHourDayTime(String dateStr){//yyyyMMdd
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date start = null;
		try {
			start = sdf.parse(dateStr);
		} catch (ParseException e) {
			log.error("获取昨天时刻数据错误！", e);
		}
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(start);
		if( cal.getTimeInMillis() - cal2.getTimeInMillis() > 24*60*60*1000 ){
			return sdf.format(cal2.getTime())+"23";
		}else{
			if(cal.get(Calendar.HOUR_OF_DAY) > 0){
				cal.add(Calendar.HOUR_OF_DAY, -1);
			}
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHH");
			return sdf2.format(cal.getTime());
		}
	}

	/**
	 * MM统计-多天查看
	 * @param offlineMoneyCount
	 * @param model
	 * @return
	 */
	@RequestMapping("graphMMByManyDay")
	public String graphMMByManyDay(OfflineMoneyCount offlineMoneyCount,Model model){
		if (!Verdict.isAllow(offlineMoneyCount.getCkAppId())) {
			return "403";
		}
		StringBuffer title = new StringBuffer();
		StringBuffer legend_data = new StringBuffer();
		String[] xAxis_data = null;
		String startTime="";
		String endTime="";
		offlineMoneyCount.setcType("mm");
		if( StringUtils.isNotBlank(offlineMoneyCount.getStartTime()) && StringUtils.isNotBlank(offlineMoneyCount.getEndTime())){
			startTime = offlineMoneyCount.getStartTime();
			endTime = offlineMoneyCount.getEndTime();
			offlineMoneyCount.setStartTime(startTime.replace("-", ""));
			offlineMoneyCount.setEndTime(endTime.replace("-", ""));
		}else{
			endTime = DateUtils.getDate("yyyy-MM-dd");
			startTime =  DateUtils.formatDate(DateUtils.beforeNumDate(7),"yyyy-MM-dd");
			offlineMoneyCount.setTimeType(2);
			offlineMoneyCount.setStartTime(startTime.replace("-", ""));
			offlineMoneyCount.setEndTime(endTime.replace("-", ""));
		}
		legend_data.append("['MM']");
		xAxis_data = getAllGraph(offlineMoneyCount, xAxis_data);
		OfflineMoneyCount total = this.offlineMoneyCountService.statTotalMoeny(offlineMoneyCount);
		OfflineMoneyCount todayOff = null;
		try {
			todayOff = offlineMoneyCount.cloneObject();
			todayOff.setEndTime(getLastHourDayTime(todayOff.getEndTime()));
			todayOff.setStartTime(todayOff.getEndTime().substring(0, 8)+"00");
		} catch (CloneNotSupportedException e) {
			log.error("OfflineMoneyCount clone failure", e);
		}
		OfflineMoneyCount totalHour = this.offlineMoneyCountService.statTotalMoenyByHour(todayOff);
		//获取数据
		List<OfflineMoneyCount> list = this.offlineMoneyCountService.statMoeny(offlineMoneyCount);
		JSONObject today = getData(list, xAxis_data,1,"今日");
		JSONObject todaySuccess = getData(list, xAxis_data,2,"今日");
		JSONArray sercies = new JSONArray();
		JSONArray serciesSuccess = new JSONArray();
		OfflineMoneyCount yester = null;
		try {
			yester = offlineMoneyCount.cloneObject();
			yester.setEndTime(getLastWeekDayTime(yester.getEndTime()));
			yester.setStartTime(yester.getEndTime().substring(0, 8)+"00");
		} catch (CloneNotSupportedException e) {
			log.error("OfflineMoneyCount clone failure", e);
		}
		OfflineMoneyCount yesterDayTotal = this.offlineMoneyCountService.statTotalMoenyByHour(yester);
		sercies.add(today);
		serciesSuccess.add(todaySuccess);
		model.addAttribute("title", title.toString());
		model.addAttribute("legend_data", legend_data.toString());
		model.addAttribute("xAxis_data", getX(xAxis_data));
		model.addAttribute("sercies", sercies);
		model.addAttribute("serciesSuccess", serciesSuccess);
		model.addAttribute("total", total==null?new OfflineMoneyCount():total);
		model.addAttribute("totalHour", totalHour==null?new OfflineMoneyCount():totalHour);
		model.addAttribute("yesterDayTotal", yesterDayTotal==null?new OfflineMoneyCount():yesterDayTotal);
		offlineMoneyCount.setStartTime(startTime);
		offlineMoneyCount.setEndTime(endTime);
		return "modules/stats/MMByManyDayMoneyCount";
	}
	
	/**
	 * 和游戏统计-按天查看
	 * @param offlineMoneyCount
	 * @param model
	 * @return
	 */
	@RequestMapping("graphAndByDay")
	public String graphAndByDay(OfflineMoneyCount offlineMoneyCount,Model model){
		if (!Verdict.isAllow(offlineMoneyCount.getCkAppId())) {
			return "403";
		}
		StringBuffer title = new StringBuffer();
		StringBuffer legend_data = new StringBuffer();
		String[] xAxis_data = null;
		String startTime="";
		String endTime="";
		offlineMoneyCount.setcType("and");
		if( StringUtils.isNotBlank(offlineMoneyCount.getStartTime()) ){
			startTime = offlineMoneyCount.getStartTime();
			endTime = offlineMoneyCount.getStartTime();
			offlineMoneyCount.setStartTime(startTime.replace("-", ""));
			offlineMoneyCount.setEndTime(startTime.replace("-", ""));
		}else{
			startTime = DateUtils.getDate("yyyy-MM-dd");
			endTime = DateUtils.getDate("yyyy-MM-dd");
//			startTime =  DateUtils.formatDate(DateUtils.beforeNumDate(7),"yyyy-MM-dd");
			offlineMoneyCount.setTimeType(3);
			offlineMoneyCount.setStartTime(endTime.replace("-", ""));
			offlineMoneyCount.setEndTime(endTime.replace("-", ""));
		}
		legend_data.append("['今日','昨日','上周同期']");
		xAxis_data = getAllGraph(offlineMoneyCount, xAxis_data);
		//获取数据
		List<OfflineMoneyCount> list = this.offlineMoneyCountService.statMoeny(offlineMoneyCount);
		OfflineMoneyCount total = this.offlineMoneyCountService.statTotalMoeny(offlineMoneyCount);
		OfflineMoneyCount todayOff = null;
		try {
			todayOff = offlineMoneyCount.cloneObject();
			todayOff.setEndTime(getLastHourDayTime(todayOff.getStartTime()));
			todayOff.setStartTime(todayOff.getEndTime().substring(0, 8)+"00");
		} catch (CloneNotSupportedException e) {
			log.error("OfflineMoneyCount clone failure", e);
		}
		OfflineMoneyCount totalHour = this.offlineMoneyCountService.statTotalMoenyByHour(todayOff);
		JSONObject today = getData(list, xAxis_data,1,"今日");
		JSONObject todaySuccess = getData(list, xAxis_data,2,"今日");
		OfflineMoneyCount yester = null;
		try {
			yester = offlineMoneyCount.cloneObject();
			yester.setEndTime(getLastWeekDayTime(yester.getStartTime()));
			yester.setStartTime(yester.getEndTime().substring(0, 8)+"00");
		} catch (CloneNotSupportedException e) {
			log.error("OfflineMoneyCount clone failure", e);
		}
		offlineMoneyCount.setStartTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(startTime),-1),"yyyyMMdd"));
		offlineMoneyCount.setEndTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(endTime),-1),"yyyyMMdd"));
		list = this.offlineMoneyCountService.statMoeny(offlineMoneyCount);
		OfflineMoneyCount yesterDayTotal = this.offlineMoneyCountService.statTotalMoenyByHour(yester);
		JSONObject yesterday = getData(list, xAxis_data,1,"昨日");
		JSONObject yesterdaySuccess = getData(list, xAxis_data,2,"昨日");
		offlineMoneyCount.setStartTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(startTime),-7),"yyyyMMdd"));
		offlineMoneyCount.setEndTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(endTime),-7),"yyyyMMdd"));
		list = this.offlineMoneyCountService.statMoeny(offlineMoneyCount);
		JSONObject lastweek = getData(list, xAxis_data,1,"上周同期");
		JSONObject lastweekSuccess = getData(list, xAxis_data,2,"上周同期");
		JSONArray sercies = new JSONArray();
		JSONArray serciesSuccess = new JSONArray();
		sercies.add(today);
		sercies.add(yesterday);
		sercies.add(lastweek);
		serciesSuccess.add(todaySuccess);
		serciesSuccess.add(yesterdaySuccess);
		serciesSuccess.add(lastweekSuccess);
		model.addAttribute("title", title.toString());
		model.addAttribute("legend_data", legend_data.toString());
		model.addAttribute("xAxis_data", getX(xAxis_data));
		model.addAttribute("sercies", sercies);
		model.addAttribute("serciesSuccess", serciesSuccess);
		model.addAttribute("total", total==null?new OfflineMoneyCount():total);
		model.addAttribute("totalHour", totalHour==null?new OfflineMoneyCount():totalHour);
		model.addAttribute("yesterDayTotal", yesterDayTotal==null?new OfflineMoneyCount():yesterDayTotal);
		offlineMoneyCount.setStartTime(startTime);
		return "modules/stats/AndByDayMoneyCount";
	}
	
	/**
	 * 和游戏统计-多天查看
	 * @param offlineMoneyCount
	 * @param model
	 * @return
	 */
	@RequestMapping("graphAndByManyDay")
	public String graphAndByManyDay(OfflineMoneyCount offlineMoneyCount,Model model){
		if (!Verdict.isAllow(offlineMoneyCount.getCkAppId())) {
			return "403";
		}
		StringBuffer title = new StringBuffer();
		StringBuffer legend_data = new StringBuffer();
		String[] xAxis_data = null;
		String startTime="";
		String endTime="";
		offlineMoneyCount.setcType("and");
		if( StringUtils.isNotBlank(offlineMoneyCount.getStartTime()) && StringUtils.isNotBlank(offlineMoneyCount.getEndTime())){
			startTime = offlineMoneyCount.getStartTime();
			endTime = offlineMoneyCount.getEndTime();
			offlineMoneyCount.setStartTime(startTime.replace("-", ""));
			offlineMoneyCount.setEndTime(endTime.replace("-", ""));
		}else{
			endTime = DateUtils.getDate("yyyy-MM-dd");
			startTime =  DateUtils.formatDate(DateUtils.beforeNumDate(7),"yyyy-MM-dd");
			offlineMoneyCount.setTimeType(2);
			offlineMoneyCount.setStartTime(startTime.replace("-", ""));
			offlineMoneyCount.setEndTime(endTime.replace("-", ""));
		}
		legend_data.append("['和游戏']");
		xAxis_data = getAllGraph(offlineMoneyCount, xAxis_data);
		//总计
		OfflineMoneyCount total = this.offlineMoneyCountService.statTotalMoeny(offlineMoneyCount);
		OfflineMoneyCount todayOff = null;
		try {
			todayOff = offlineMoneyCount.cloneObject();
			todayOff.setEndTime(getLastHourDayTime(todayOff.getEndTime()));
			todayOff.setStartTime(todayOff.getEndTime().substring(0, 8)+"00");
		} catch (CloneNotSupportedException e) {
			log.error("OfflineMoneyCount clone failure", e);
		}
		OfflineMoneyCount totalHour = this.offlineMoneyCountService.statTotalMoenyByHour(todayOff);
		//获取数据
		List<OfflineMoneyCount> list = this.offlineMoneyCountService.statMoeny(offlineMoneyCount);
		JSONObject today = getData(list, xAxis_data,1,"今日");
		JSONObject todaySuccess = getData(list, xAxis_data,2,"今日");
		JSONArray sercies = new JSONArray();
		JSONArray serciesSuccess = new JSONArray();
		//上周统计
		OfflineMoneyCount yester = null;
		try {
			yester = offlineMoneyCount.cloneObject();
			yester.setEndTime(getLastWeekDayTime(yester.getEndTime()));
			yester.setStartTime(yester.getEndTime().substring(0, 8)+"00");
		} catch (CloneNotSupportedException e) {
			log.error("OfflineMoneyCount clone failure", e);
		}
		OfflineMoneyCount yesterDayTotal = this.offlineMoneyCountService.statTotalMoenyByHour(yester);
		
		sercies.add(today);
		serciesSuccess.add(todaySuccess);
		model.addAttribute("title", title.toString());
		model.addAttribute("legend_data", legend_data.toString());
		model.addAttribute("xAxis_data", getX(xAxis_data));
		model.addAttribute("sercies", sercies);
		model.addAttribute("serciesSuccess", serciesSuccess);
		model.addAttribute("total", total==null?new OfflineMoneyCount():total);
		model.addAttribute("totalHour", totalHour==null?new OfflineMoneyCount():totalHour);
		model.addAttribute("yesterDayTotal", yesterDayTotal==null?new OfflineMoneyCount():yesterDayTotal);
		offlineMoneyCount.setStartTime(startTime);
		offlineMoneyCount.setEndTime(endTime);
		return "modules/stats/AndByManyDayMoneyCount";
	}
	
	/**
	 * 单机游戏金额统计-多天查看
	 * @param offlineMoneyCount
	 * @param model
	 * @return
	 */
	@RequestMapping("graphMoneyByManyDay")
	public String graphByManyDay(OfflineMoneyCount offlineMoneyCount,Model model){
		if (!Verdict.isAllow(offlineMoneyCount.getCkAppId())) {
			return "403";
		}
		StringBuffer title = new StringBuffer();
		StringBuffer legend_data = new StringBuffer();
		String[] xAxis_data = null;
		String startTime="";
		String endTime="";
		if( StringUtils.isNotBlank(offlineMoneyCount.getStartTime()) && StringUtils.isNotBlank(offlineMoneyCount.getEndTime())){
			startTime = offlineMoneyCount.getStartTime();
			endTime = offlineMoneyCount.getEndTime();
			offlineMoneyCount.setStartTime(startTime.replace("-", ""));
			offlineMoneyCount.setEndTime(endTime.replace("-", ""));
		}else{
			endTime = DateUtils.getDate("yyyy-MM-dd");
			startTime =  DateUtils.formatDate(DateUtils.beforeNumDate(7),"yyyy-MM-dd");
			offlineMoneyCount.setTimeType(2);
			offlineMoneyCount.setStartTime(startTime.replace("-", ""));
			offlineMoneyCount.setEndTime(endTime.replace("-", ""));
		}
		legend_data.append("['今日','昨日','上周同期']");
		xAxis_data = getAllGraph(offlineMoneyCount, xAxis_data);
		OfflineMoneyCount todayOff = null;
		try {
			todayOff = offlineMoneyCount.cloneObject();
			todayOff.setEndTime(getLastHourDayTime(todayOff.getEndTime()));
			todayOff.setStartTime(todayOff.getEndTime().substring(0, 8)+"00");
		} catch (CloneNotSupportedException e) {
			log.error("OfflineMoneyCount clone failure", e);
		}
		OfflineMoneyCount totalHour = this.offlineMoneyCountService.statTotalOfflineMoney(todayOff);
		offlineMoneyCount.setStartTime(offlineMoneyCount.getStartTime());
		offlineMoneyCount.setEndTime(offlineMoneyCount.getEndTime()+"23");
		//总计
		OfflineMoneyCount total = this.offlineMoneyCountService.statTotalOfflineMoney(offlineMoneyCount);
		//获取数据
		List<OfflineMoneyCount> list = this.offlineMoneyCountService.statOfflineMoney(offlineMoneyCount);
		JSONObject today = getData(list, xAxis_data,1,"今日");
		JSONObject todaySuccess = getData(list, xAxis_data,2,"今日");
		
		//上周统计
		OfflineMoneyCount yester = null;
		try {
			yester = offlineMoneyCount.cloneObject();
			yester.setEndTime(getLastWeekDayTime(yester.getEndTime()));
			yester.setStartTime(yester.getEndTime().substring(0, 8)+"00");
		} catch (CloneNotSupportedException e) {
			log.error("OfflineMoneyCount clone failure", e);
		}
		OfflineMoneyCount yesterDayTotal = this.offlineMoneyCountService.statTotalOfflineMoney(yester);
		
		offlineMoneyCount.setStartTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(startTime),-1),"yyyyMMdd"));
		offlineMoneyCount.setEndTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(endTime),-1),"yyyyMMdd")+"23");
		list = this.offlineMoneyCountService.statOfflineMoney(offlineMoneyCount);
		JSONObject yesterday = getData(list, xAxis_data,1,"昨日");
		JSONObject yesterdaySuccess = getData(list, xAxis_data,2,"昨日");
		offlineMoneyCount.setStartTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(startTime),-7),"yyyyMMdd"));
		offlineMoneyCount.setEndTime(DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(endTime),-7),"yyyyMMdd")+"23");
		list = this.offlineMoneyCountService.statOfflineMoney(offlineMoneyCount);
		JSONObject lastweek = getData(list, xAxis_data,1,"上周同期");
		JSONObject lastweekSuccess = getData(list, xAxis_data,2,"上周同期");
		
		JSONArray sercies = new JSONArray();
		JSONArray serciesSuccess = new JSONArray();
		sercies.add(today);
		sercies.add(yesterday);
		sercies.add(lastweek);
		serciesSuccess.add(todaySuccess);
		serciesSuccess.add(yesterdaySuccess);
		serciesSuccess.add(lastweekSuccess);
		model.addAttribute("title", title.toString());
		model.addAttribute("legend_data", legend_data.toString());
		model.addAttribute("xAxis_data", getX(xAxis_data));
		model.addAttribute("sercies", sercies);
		model.addAttribute("serciesSuccess", serciesSuccess);
		model.addAttribute("total", total==null?new OfflineMoneyCount():total);
		model.addAttribute("totalHour", totalHour==null?new OfflineMoneyCount():totalHour);
		model.addAttribute("yesterDayTotal", yesterDayTotal==null?new OfflineMoneyCount():yesterDayTotal);
		offlineMoneyCount.setStartTime(startTime);
		offlineMoneyCount.setEndTime(endTime);
		return "modules/stats/OfflineMoneyByManyDay";
	}
}