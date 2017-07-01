package me.ckhd.opengame.stats.web;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.utils.AppCkUtils;
import me.ckhd.opengame.app.utils.ChannelUtils;
import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.drds.service.EventService;
import me.ckhd.opengame.evnet.entity.AppEventStat;
import me.ckhd.opengame.stats.service.AndOrderStatsService;
import me.ckhd.opengame.stats.service.MmOrderStatsService;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(value = "${adminPath}/stats/appEventStat")
@Controller
public class AppEventStatController extends BaseController {
	protected static Logger logger = LoggerFactory
			.getLogger(AppEventStatController.class);
	@Autowired
	private EventService eventService;

	@Autowired
	private MmOrderStatsService mmOrderStatsService;
	
	@Autowired
	private AndOrderStatsService andOrderStatsService;

	
	@ModelAttribute("appEventStat")
	public AppEventStat get(@RequestParam(required = false) String id) {
		return new AppEventStat();
	}

	/**@author yong
	 * 统计游戏事件信息
	 * @param appEventStat
	 * @param model
	 * @return
	 */
	@RequiresPermissions("stats:appeventstat:view")
	@RequestMapping(value = "list")
	public String countAppEventStat(AppEventStat appEventStat, Model model) {
		if (!Verdict.isAllow(appEventStat.getCkAppId())) {
			return "403";
		}
		String startDate = appEventStat.getStartDate();
		String endDate = appEventStat.getEndDate();
		String ckAppId = appEventStat.getCkAppId();
		if(StringUtils.isNotBlank(ckAppId) && StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)){
			appEventStat.setStartDate(startDate + " 00:00:00");
			appEventStat.setEndDate(endDate + " 23:59:59");
			List<AppEventStat> statsList = this.getAppEventStats(appEventStat);
			appEventStat.setStartDate(startDate);
			appEventStat.setEndDate(endDate);
			model.addAttribute("statsList",statsList);
		}else{
			Calendar c = Calendar.getInstance();
			String startDateTime = DateUtils.formatDate(c.getTime(),"yyyy-MM-dd");
			appEventStat.setStartDate(startDateTime);
			appEventStat.setEndDate(startDateTime);
		}
		model.addAttribute("appEventStat",appEventStat);
		return "modules/stats/AppEventStat";
	}
	
	/**
	 * newUser
	 */
	@RequestMapping(value = "newUser")
	public String countNewUser(AppEventStat appEventStat, Model model){
		if (!Verdict.isAllow(appEventStat.getCkAppId())) {
			return "403";
		}
		String startDate = appEventStat.getStartDate();
		String ckAppId = appEventStat.getCkAppId();
		if(StringUtils.isNotBlank(ckAppId) && StringUtils.isNotBlank(startDate)){
			appEventStat.setStartDate(startDate);
			List<Map<String,Integer>> stats = null;
			try {
				stats = this.statsByChannel(appEventStat);
			} catch (Exception e) {
				e.printStackTrace();
			}
			appEventStat.setStartDate(startDate);
			model.addAttribute("statsList",stats);
		}else{
			Calendar c = Calendar.getInstance();
			String startDateTime = DateUtils.formatDate(c.getTime(),"yyyy-MM-dd");
			appEventStat.setStartDate(startDateTime);
		}
		model.addAttribute("appEventStatNewUser",appEventStat);
		return "modules/stats/AppEventStatNewUser";
	}
	
	/**
	 * 统计留存率信息
	 * @param appEventStat
	 * @param model
	 * @return
	 */
	@RequiresPermissions("stats:appeventstat:view")
	@RequestMapping(value = "stats")
	public String countStatsRetention(AppEventStat appEventStat, Model model) {
		if (!Verdict.isAllow(appEventStat.getCkAppId())) {
			return "403";
		}
		String startDate = appEventStat.getStartDate();
		String endDate = appEventStat.getEndDate();
		String ckAppId = appEventStat.getCkAppId();
		if(StringUtils.isNotBlank(ckAppId) && StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)){
			appEventStat.setStartDate(startDate.replaceAll("-", "")+"000000");
			appEventStat.setEndDate(endDate.replaceAll("-", "")+"235959");
			try {
				List<Map<String,Object>> statRetention = eventService.getStat(appEventStat);
				model.addAttribute("statRetention",statRetention);
			} catch (Exception e) {
				e.printStackTrace();
			}
			appEventStat.setStartDate(startDate);
			appEventStat.setEndDate(endDate);
		}else{
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			String startDateTime = DateUtils.formatDate(c.getTime(),"yyyy-MM-dd");
			appEventStat.setStartDate(startDateTime);
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			String endDateTime = DateUtils.formatDate(c.getTime(),"yyyy-MM-dd");
			appEventStat.setEndDate(endDateTime);
		}
		model.addAttribute("appEventStat",appEventStat);
		return "modules/stats/StatsRetention";
		
	}
	/**
	 * 导出留存率报表
	 * @param request
	 * @param response
	 * @param appEventStat
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "exprotRetention")
	public void exprotRetention(HttpServletRequest request,HttpServletResponse response,AppEventStat appEventStat) {
		String startDate = appEventStat.getStartDate();
		String endDate = appEventStat.getEndDate();
		String ckAppId = appEventStat.getCkAppId();
		if(StringUtils.isNotBlank(ckAppId) && StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)){
			appEventStat.setStartDate(startDate.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "")+"000000");
			appEventStat.setEndDate(endDate.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "")+"235959");
			try {
				List<Map<String,Object>> statRetention = eventService.getStat(appEventStat);
				//step2:设置导出报表格式
				String[] headers = {"注册时间","游戏名称","渠道","新增用户数","1日后","3日后","7日后","30日后"};
				XSSFWorkbook wb = new XSSFWorkbook();
		        XSSFSheet xss = wb.createSheet();     
		        wb.setSheetName(0, "留存率报表");
		        XSSFCellStyle style0 = wb.createCellStyle();
		        style0.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		        style0.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		        XSSFFont front0 = wb.createFont();
		        front0.setFontName("Arial");//字体名称
		        front0.setFontHeightInPoints((short)10);          //字体大小
		        front0.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //字体变粗
		        style0.setFont(front0);
		        style0.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中    
		        XSSFCellStyle style1 = wb.createCellStyle();
		        XSSFFont front1 = wb.createFont();
		        front1.setFontName("Arial");//字体名称
		        front1.setFontHeightInPoints((short)10);          //字体大小
		        style1.setFont(front1);
		        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中   
		        XSSFRow row0 = xss.createRow(0);
		        for(int i=0;i<headers.length;i++){
		            this.insertExcel(row0, i, headers[i]);
		        }
		        Iterator it0 = row0.cellIterator();
		        while(it0.hasNext()){
		            XSSFCell cell = (XSSFCell) it0.next();
		            cell.setCellStyle(style0);
		        }
		       XSSFRow row = null;
		      if(statRetention != null && statRetention.size()>0){
		    	  for(int i=0;i<statRetention.size();i++){
		    		  row = xss.createRow(i+1);
		    		  Map<String,Object> mmApp = statRetention.get(i);
		    		  String seconDay = "";
		    		  if(mmApp.get("second_day") != null){
		    			  seconDay = mmApp.get("second_day").toString();
		    		  } else {
		    			  seconDay = "";
		    		  }
		    		  String thirdDay = "";
		    		  if(mmApp.get("third_day") != null){
		    			  thirdDay = mmApp.get("third_day").toString();
		    		  } else {
		    			  thirdDay = "";
		    		  }
		    		  String seventhDay = "";
		    		  if(mmApp.get("seventh_day") != null){
		    			  seventhDay = mmApp.get("seventh_day").toString();
		    		  } else {
		    			  seventhDay = "";
		    		  }
		    		  String thirtiethDay = "";
		    		  if(mmApp.get("thirtieth_day") != null){
		    			  thirtiethDay = mmApp.get("thirtieth_day").toString();
		    		  } else {
		    			  thirtiethDay = "";
		    		  }
		    		  this.insertExcel(row, 0,mmApp.get("stat_time").toString());
		    		  this.insertExcel(row, 1,(StringUtils.isNotBlank(AppCkUtils.getByCkAppName(mmApp.get("ckappid").toString()))
		    				  ? AppCkUtils.getByCkAppName(mmApp.get("ckappid").toString()) : "")+"("+mmApp.get("ckappid").toString()+")");
		    		  this.insertExcel(row, 2,(StringUtils.isNotBlank(ChannelUtils.findChannelName(mmApp.get("channelid").toString(), "")) 
		    				  ? ChannelUtils.findChannelName(mmApp.get("channelid").toString(), "") : "")+"("+mmApp.get("channelid").toString()+")");
		    		  this.insertExcel(row, 3,mmApp.get("num").toString());
		    		  this.insertExcel(row, 4,StringUtils.isNotBlank(seconDay) ? seconDay+"%" : "");
		    		  this.insertExcel(row, 5,StringUtils.isNotBlank(thirdDay) ? thirdDay+"%" : "");
		    		  this.insertExcel(row, 6,StringUtils.isNotBlank(seventhDay) ? seventhDay+"%" : "");
		    		  this.insertExcel(row, 7,StringUtils.isNotBlank(thirtiethDay) ? thirtiethDay+"%" : "");
		    		  Iterator it1 = row.cellIterator();
		    		  while(it1.hasNext()){
		    			  XSSFCell cell = (XSSFCell) it1.next();
		    			  cell.setCellStyle(style1);
		    		  }
		    	  }
		      }
		    //设置列宽
	        for(int j=0;j<8;j++){
	        	if(j==0){
	        		xss.setColumnWidth((short)0, (short)(30*220));
	        	} else if(j==1){
	        		xss.setColumnWidth((short)1, (short)(30*250));
	        	} else {
	        		xss.setColumnWidth((short)j, (short)(30*150));
	        	}
	        }
        	//step4:把文档输出到浏览器，关闭流对象         
        	response.reset();
        	response.setContentType("application/vnd.ms-excel; charset=utf-8");
        	response.setHeader("Content-Disposition","attachment;filename=RetentionExcel.xlsx");
        	ServletOutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();//关闭流对象
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("导出留存率报表异常.");
	        }
		}
    }
	/**
	 * 导出游戏事件统计信息
	 * @param request
	 * @param response
	 * @param appEventStat
	 * @return
	 */
	@RequestMapping(value = "exprotEventStat")
	public String exportEventStat(HttpServletRequest request,HttpServletResponse response,AppEventStat appEventStat){
		List<AppEventStat> statsList = this.getAppEventStats(appEventStat);
		Workbook workbook = new HSSFWorkbook();
	    Sheet sheet = workbook.createSheet("游戏事件统计");
	    sheet.setColumnWidth(0, 3500);
		sheet.setColumnWidth(1, 2500);
		sheet.setColumnWidth(2, 2500);
	    Row titleRow = sheet.createRow(0);
	    titleRow.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("日期/小时");
        titleRow.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("新增用户数");
        titleRow.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("活跃用户数");
        int rowNum = 1;
        if(statsList != null && statsList.size()>0){
	    	  for(AppEventStat appStat :statsList){
                Row contentRow = sheet.createRow(rowNum++);
                contentRow.createCell(0, Cell.CELL_TYPE_STRING).setCellValue(appStat.getStatsTime());
                contentRow.createCell(1, Cell.CELL_TYPE_NUMERIC).setCellValue(appStat.getAdd());
                contentRow.createCell(2, Cell.CELL_TYPE_NUMERIC).setCellValue(appStat.getActive());
            }
        }
        //输出Excel到客户端
        response.reset();
        response.setContentType("application/octet-stream");
        String fileName = "导出游戏事件统计报表";
        try {
			fileName = new String(fileName.getBytes("gb2312"), "ISO8859_1");
			response.setHeader("Content-Disposition", "attachment;filename=\""+fileName+".xls"+"\"");
	        OutputStream outputStream;
			outputStream = response.getOutputStream();
			workbook.write(outputStream);
		    outputStream.flush();
	        outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导出游戏事件统计报表异常.");
		}
		return null;
	}
	
	private void insertExcel(XSSFRow row, int cols, String value) {
	       XSSFCell cell = row.createCell((short) cols);
	       cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	       if(value != null && !"null".equals(value)){
	           cell.setCellValue(value);       
	       } else {
	           cell.setCellValue("");
	       }
	}
	/**
	 * 整合游戏事件数据信息
	 * @param appEventStat
	 * @return
	 */
	private List<AppEventStat> getAppEventStats(AppEventStat appEventStat){
		List<AppEventStat> statsList = new ArrayList<AppEventStat>();
		List<AppEventStat> list = new ArrayList<AppEventStat>();
		List<Map<String,Object>> newAccout;
		List<Map<String,Object>> activeAccount;
		try {
			newAccout = eventService.getNewAccout(appEventStat);
			activeAccount = eventService.getActiveAccount(appEventStat);
			if(newAccout != null && newAccout.size()>0 ){
				for(Map<String,Object> map1 : newAccout){
					AppEventStat aes1 = new AppEventStat();
					aes1.setStatsTime(map1.get("day").toString());
					aes1.setAdd(map1.get("num").toString());
					statsList.add(aes1);
				}
			}
			if(activeAccount != null && activeAccount.size()>0){
				for(Map<String,Object> map2 : activeAccount){
					AppEventStat aes2 = new AppEventStat();
					aes2.setStatsTime(map2.get("day").toString());
					aes2.setActive(map2.get("num").toString());
					list.add(aes2);
				}
			}
			statsList.addAll(list);
			if(statsList.size()>1){
				for(int i=0;i<statsList.size();i++){
					AppEventStat aes3 = statsList.get(i);
					for(int j=statsList.size()-1;j>i;j--){
						AppEventStat aes4 = statsList.get(j);
						if(aes3.getStatsTime().equals(aes4.getStatsTime()) && StringUtils.isBlank(aes3.getActive()) && StringUtils.isNotBlank(aes4.getActive())){
							aes3.setActive(aes4.getActive());
							statsList.remove(j);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statsList;
	}

	private List<Map<String,Integer>> statsByChannel(AppEventStat appEventStat) throws Exception{

		Map<Integer,Map<String,Integer>> channelDatas = new ConcurrentSkipListMap<Integer,Map<String,Integer>>();
		
		for(Map<String,Object> newUserNum : this.newUserNum(appEventStat)){
			Map<String,Integer> item = new HashMap<String,Integer>();
			int channelid = 0;
			try {
				channelid = Integer.parseInt("" + newUserNum.get("channelid"));
			} catch (Throwable t) {
				//
			}
			item.put("channelid", channelid);
			item.put("newUserNum", Integer.parseInt("" + newUserNum.get("num")));
			channelDatas.put(channelid, item);
		}
		for(Map<String,Object> activeUserNum : this.activeUserNum(appEventStat)){
			int channelid = 0;
			try {
				channelid = Integer.parseInt("" + activeUserNum.get("channelid"));
			} catch (Throwable t) {
				//
			}
			Map<String,Integer> item = channelDatas.get(channelid);
			if(item == null){
				item = new HashMap<String,Integer>();
				item.put("channelid", channelid);
			}
			item.put("activeUserNum", Integer.parseInt("" + activeUserNum.get("num")));
			channelDatas.put(channelid, item);
		}
		List<Map<String,Integer>> rst = new ArrayList<Map<String,Integer>>();
		for(int key : channelDatas.keySet()){
			rst.add(channelDatas.get(key));
		}
		return rst;
	}
	
	private List<Map<String,Object>> newUserNum(AppEventStat appEventStat) throws Exception{
		List<Map<String,Object>> newAccout = eventService.getNewAccoutChannelId(appEventStat);
		return newAccout;
	}
	
	private List<Map<String,Object>> activeUserNum(AppEventStat appEventStat) throws Exception{
		List<Map<String,Object>> newAccout = eventService.getActiveUserNumChannelId(appEventStat);
		return newAccout;
	}
}
