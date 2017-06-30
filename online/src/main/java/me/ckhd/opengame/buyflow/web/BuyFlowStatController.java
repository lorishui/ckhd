package me.ckhd.opengame.buyflow.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import me.ckhd.opengame.app.utils.AppCkUtils;
import me.ckhd.opengame.app.utils.ChannelUtils;
import me.ckhd.opengame.buyflow.entity.BuyFlowRetainIncomeStats;
import me.ckhd.opengame.buyflow.entity.BuyFlowRetainStats;
import me.ckhd.opengame.buyflow.entity.BuyFlowStat;
import me.ckhd.opengame.buyflow.entity.BuyFlowTotalIncomeStats;
import me.ckhd.opengame.buyflow.service.BuyFlowRetainIncomeStatsService;
import me.ckhd.opengame.buyflow.service.BuyFlowRetainStatsService;
import me.ckhd.opengame.buyflow.service.BuyFlowStatService;
import me.ckhd.opengame.buyflow.service.BuyFlowTotalIncomeStatsService;
import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.stats.entity.StatRelated;
import me.ckhd.opengame.sys.entity.Role;
import me.ckhd.opengame.sys.utils.DictUtils;
import me.ckhd.opengame.sys.utils.UserUtils;
import me.ckhd.opengame.util.ExcelUtils;
import me.ckhd.opengame.util.ExcelUtils.Column;
import me.ckhd.opengame.util.ExcelUtils.Config;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value = "${adminPath}/buyflow/stat")
public class BuyFlowStatController extends BaseController {
	@Autowired
	private BuyFlowStatService buyFlowStatService;
	
	@Autowired
	private BuyFlowRetainStatsService buyFlowRetainStatsService;

	@Autowired
	private BuyFlowRetainIncomeStatsService buyFlowRetainIncomeStatsService;
	
	@Autowired
	private BuyFlowTotalIncomeStatsService buyFlowTotalIncomeStatsService;
	
	@RequiresPermissions("buyflow:stat:view")
	@RequestMapping(value = { "list", "" })
	public String list(BuyFlowStat buyFlowStat, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		if ( !Verdict.isAllow(buyFlowStat.getCkappId())) {
			return "403";
		}
		BuyFlowStat param = null;
		List<BuyFlowStat> list = new ArrayList<BuyFlowStat>();
		try {
			param = (BuyFlowStat) buyFlowStat.clone();
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			getSelectedCondition(buyFlowStat);
			if( StringUtils.isBlank(buyFlowStat.getStartDate()) || StringUtils.isBlank(buyFlowStat.getEndDate()) ){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
				cal.add(Calendar.DAY_OF_MONTH, 0);
				buyFlowStat.setEndDate(sdf.format(cal.getTime()));
				param.setEndDate(sdf2.format(cal.getTime()));

				cal.add(Calendar.DAY_OF_MONTH, -3);
				buyFlowStat.setStartDate(sdf.format(cal.getTime()));
				param.setStartDate(sdf2.format(cal.getTime()));
			}else{
				buyFlowStat.setStartDate(buyFlowStat.getStartDate().replace("-", ""));
				buyFlowStat.setEndDate(buyFlowStat.getEndDate().replace("-", ""));
			}
			list = buyFlowStatService.findList(buyFlowStat);
			
			
		} catch (Exception e) {
			logger.error("", e);
		}
		
		
		
		
		BuyFlowStat total = new BuyFlowStat();
		if(list.size() > 0){
			for (BuyFlowStat bfs : list) {
				total.setClickNum(total.getClickNum()+bfs.getClickNum());
				total.setDeviceClickNum(total.getDeviceClickNum()+bfs.getDeviceClickNum());
				total.setActiveNum(total.getActiveNum()+bfs.getActiveNum());
				total.setRegisterNum(total.getRegisterNum()+bfs.getRegisterNum());
			}
		}
		
		model.addAttribute("total", total);
		model.addAttribute("data", list);
		model.addAttribute("buyFlowStat",param);
		return "modules/buyflow/buyFlowStat";
	}
	
	/**
	 * 从   (20170101-20170105  xxx活动/xxx渠道)日期处点入，获取每日XXX活动数据   
	 * @param buyFlowStat
	 * @return
	 */
	@RequestMapping("data")
	@ResponseBody
	public String getData(BuyFlowStat buyFlowStat){
		int group = buyFlowStat.getGroup();
		buyFlowStat.setGroupBy("statsDate");
		buyFlowStat.setStartDate(buyFlowStat.getStartDate().replace("-", ""));
		buyFlowStat.setEndDate(buyFlowStat.getEndDate().replace("-", ""));
		List<BuyFlowStat> list = buyFlowStatService.getData(buyFlowStat);
		JSONArray jsonData = getJsonData(list,group,buyFlowStat);
		
		return jsonData.toJSONString();
	}
	
	

	/**
	 * 从(20170101  汇总) 点入   获取当日 每个  活动/渠道  数据
	 * 获取
	 * @param buyFlowStat
	 * @return
	 */
	@RequestMapping("dayData")
	@ResponseBody
	public String getDayData(BuyFlowStat buyFlowStat){
		int group = buyFlowStat.getGroup();
		if(group == 0){
			buyFlowStat.setGroupBy("media_id");
		}else if(group == 1){
			buyFlowStat.setGroupBy("buyFlowName");
		}
		List<BuyFlowStat> list = buyFlowStatService.getDayData(buyFlowStat);
		//TODO
		JSONArray jsonData = getJsonData(list,group,buyFlowStat);
		return jsonData.toJSONString();
	}
	
	
	/**
	 * 从(20170101-20170105  XXX媒体 )媒体处点入 ，获取此 时间段 此 媒体  所有  活动数据 
	 * @param buyFlowStat
	 * @return
	 */
	@RequestMapping("mediaData")
	@ResponseBody
	public String getMediaData(BuyFlowStat buyFlowStat){
		buyFlowStat.setGroupBy("buyFlowName");
		buyFlowStat.setStatsDate("-1");					//作为统计连续时段的标记
		buyFlowStat.setStartDate(buyFlowStat.getStartDate().replace("-", ""));
		buyFlowStat.setEndDate(buyFlowStat.getEndDate().replace("-", ""));
		List<BuyFlowStat> list = buyFlowStatService.getMediaData(buyFlowStat);
		JSONArray jsonData = getJsonData(list,1,buyFlowStat);
		return jsonData.toJSONString();
	}
	
	/**
	 * 从(20170101  XXX媒体)媒体处点入  获取 此日期  此媒体  所有活动数据
	 * @param buyFlowStat
	 * @return
	 */
	@RequestMapping("dayMediaData")
	@ResponseBody
	public String getDayMediaData(BuyFlowStat buyFlowStat){
		buyFlowStat.setGroupBy("buyFlowName");
		List<BuyFlowStat> list = buyFlowStatService.getDayMediaData(buyFlowStat);
		JSONArray jsonData = getJsonData(list,1,buyFlowStat);
		return jsonData.toJSONString();
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
	@RequiresPermissions("buyflow:stat:retain")
	@RequestMapping("dayMediaExport")
	public String DayMediaExport(BuyFlowStat buyFlowStat,HttpServletResponse response) throws IOException{
		
		String jsonStr = getDayMediaData(buyFlowStat);
		JSONArray jsonArr = JSONObject.parseArray(jsonStr);
		
		String[] headers = {"日期","活动","点击总数","排重点击","激活设备数","激活率","注册设备数","注册率","次留","3留","7留","30留","LTV0","LTV1","LTV7","LTV30","LTV60","60日留存付费","新增用户付费金额","总付费","新增付费设备","总付费设备"};
		XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet xss = wb.createSheet();     
        wb.setSheetName(0, "买量统计报表");
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
       for(int i=0 ;i<jsonArr.size();i++){
        	row = xss.createRow(i+1);
        	JSONObject jo = jsonArr.getJSONObject(i);
        	this.insertExcel(row, 0, jo.getString("date"));
        	this.insertExcel(row, 1, jo.getString("name"));
        	this.insertExcel(row, 2, jo.getString("clickNum"));
        	this.insertExcel(row, 3, jo.getString("deviceClickNum"));
        	this.insertExcel(row, 4, jo.getString("activeNum"));
        	this.insertExcel(row, 5, jo.getString("activeRate"));
        	this.insertExcel(row, 6, jo.getString("registerNum"));
        	this.insertExcel(row, 7, jo.getString("registerRate"));
        	this.insertExcel(row, 8, jo.getString("reten1"));
        	this.insertExcel(row, 9, jo.getString("reten3"));
        	this.insertExcel(row, 10, jo.getString("reten7"));
        	this.insertExcel(row, 11, jo.getString("reten30"));
        	this.insertExcel(row, 12, jo.getString("ltv0"));
        	this.insertExcel(row, 13, jo.getString("ltv1"));
        	this.insertExcel(row, 14, jo.getString("ltv7"));
        	this.insertExcel(row, 15, jo.getString("ltv30"));
        	this.insertExcel(row, 16, jo.getString("ltv60"));
        	this.insertExcel(row, 17, jo.getString("retainTotalIncome"));
        	this.insertExcel(row, 18, jo.getString("retainIncome"));
        	this.insertExcel(row, 19, jo.getString("totalIncome"));
        	this.insertExcel(row, 20, jo.getString("payDevice"));
        	this.insertExcel(row, 21, jo.getString("totalDevice"));
            
            Iterator it1 = row.cellIterator();
            while(it1.hasNext()){
                XSSFCell cell = (XSSFCell) it1.next();
                cell.setCellStyle(style1);
            }
        } 
        //设置列宽
        for(int j=0;j<20;j++){
        	if(j==15){
        		xss.setColumnWidth((short)15, (short)(24*120));
        	}else if(j==16){
        		xss.setColumnWidth((short)16, (short)(24*130));
        	}else{
        	    xss.setColumnWidth((short)j, (short)(24*80));
        	}
        }
      //step4:把文档输出到浏览器，关闭流对象         
        try {
        	response.reset();
        	response.setContentType("application/vnd.ms-excel; charset=utf-8");
        	response.setHeader("Content-Disposition","attachment;filename=buyFlowExcel.xlsx");
        	ServletOutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();//关闭流对象
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("导出网络支付回调信息报表异常.");
        }
        return null;
	}

	private void insertExcel(XSSFRow row, int cols, String value) {
		XSSFCell cell = row.createCell((short) cols);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		if(value != null && !"null".equals(value)){
			cell.setCellValue(value);       
		}else{
			cell.setCellValue("");
		}
	}

	/**
	 * 拼装新增活跃数据
	 * @param list
	 * @param group
	 * @param buyFlowStat 
	 * @return
	 */
	private JSONArray getJsonData(List<BuyFlowStat> list, int group, BuyFlowStat buyFlowStat) {
		Map<String, Long> ltvMap = new HashMap<String, Long>();
		Map<String, Long> retenMap = new HashMap<String, Long>();
		Map<String, Long> incomeMap = new HashMap<String, Long>();
		String flag = buyFlowStat.getStatsDate();
			
		if(StringUtils.isNotBlank(buyFlowStat.getStatsDate()) && !"-1".equals(flag)){
			buyFlowStat.setStartDate(buyFlowStat.getStatsDate());
			buyFlowStat.setEndDate(buyFlowStat.getStatsDate());
		}
		//活动
		if(group == 1){
			ltvMap = queryRetainIncome(flag, buyFlowStat.getStartDate().replace("-", ""), buyFlowStat.getEndDate().replace("-", ""));
			retenMap = queryRetainStats(flag, buyFlowStat.getStartDate().replace("-", ""), buyFlowStat.getEndDate().replace("", ""));
		}
		//渠道
		if(group == 0){
			ltvMap = queryMediaRetainIncome(flag, buyFlowStat.getStartDate().replace("-", ""), buyFlowStat.getEndDate().replace("-", ""));
			retenMap = queryMediaRetainStats(flag, buyFlowStat.getStartDate().replace("-", ""), buyFlowStat.getEndDate().replace("", ""));
		}
		incomeMap = queryIncome(group,flag,buyFlowStat.getStartDate(),buyFlowStat.getEndDate());
		DecimalFormat df = new DecimalFormat("#.##");
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			JSONObject jo = new JSONObject();
			BuyFlowStat bfs = list.get(i);
			jo.put("date", bfs.getStatsDate());
			int clickNum = bfs.getClickNum();
			int deviceClickNum = bfs.getDeviceClickNum();
			int activeNum = bfs.getActiveNum();
			int registerNum = bfs.getRegisterNum();
			jo.put("clickNum",clickNum);
			jo.put("deviceClickNum", deviceClickNum);
			jo.put("activeNum", activeNum);
			jo.put("activeRate", (deviceClickNum!=0?df.format(activeNum/(clickNum*0.01)):0) + "%");
			jo.put("registerNum", registerNum);
			jo.put("registerRate", (activeNum!=0?df.format(registerNum/(activeNum*0.01)):0) + "%");
			StringBuilder keyStr = new StringBuilder();
			StringBuilder keyIncome = new StringBuilder();
			if("-1".equals(flag)){
				if(group == 0){
					jo.put("name", bfs.getMedia());
					// 0-7日总付费，key like WeAD_7    ltv数据
					// 7留活跃数：key like WeAD_7		留存数据
					keyStr.append(bfs.getMedia()).append("_");
					keyIncome.append(bfs.getMedia()).append("_");
				}
				if(group == 1){
					jo.put("name",bfs.getBuyFlowName());
					// 0-7日总付费，key like 2000_1_WeAD_1_7    ltv数据
					// 7留活跃数：key like 2000_1_WeAD_1_7		留存数据
					keyStr.append(bfs.getCkappId()).append("_").append(bfs.getChildCkappId()).append("_")
					.append(bfs.getMedia()).append("_").append(bfs.getAdItem()).append("_");
					keyIncome.append(bfs.getBuyFlowName()).append("_");
				}
			}else{
				if(group == 0){
					jo.put("name", bfs.getMedia());
					// 0-7日总付费，key like 20170608_WeAD_7    ltv数据
					// 7留活跃数：key like 20170608_WeAD_7		留存数据
					keyStr.append(bfs.getStatsDate()).append("_").append(bfs.getMedia()).append("_");
					keyIncome.append(bfs.getStatsDate()).append("_").append(bfs.getMedia()).append("_");
				}
				if(group == 1){
					jo.put("name",bfs.getBuyFlowName());
					// 0-7日总付费，key like 2000_1_WeAD_1_20170608_7    ltv数据
					// 7留活跃数：key like 2000_1_WeAD_1_20170608_7		留存数据
					keyStr.append(bfs.getCkappId()).append("_").append(bfs.getChildCkappId()).append("_")
					.append(bfs.getMedia()).append("_").append(bfs.getAdItem()).append("_")
					.append(bfs.getStatsDate()).append("_");
					keyIncome.append(bfs.getStatsDate()).append("_").append(bfs.getBuyFlowName()).append("_");
				}
			}
				String baseKey = keyStr.toString();
				String incomeKey = keyIncome.toString();
				
				long reten0 = retenMap.containsKey(baseKey+"0")?retenMap.get(baseKey+"0"):0;
				long reten1 = retenMap.containsKey(baseKey+"1")?retenMap.get(baseKey+"1"):0;
				long reten3 = retenMap.containsKey(baseKey+"3")?retenMap.get(baseKey+"3"):0;
				long reten7 = retenMap.containsKey(baseKey+"7")?retenMap.get(baseKey+"7"):0;
				long reten30 = retenMap.containsKey(baseKey+"30")?retenMap.get(baseKey+"30"):0;
				long ltv0 = ltvMap.containsKey(baseKey+"0")?ltvMap.get(baseKey+"0"):0;
				long ltv1 = ltvMap.containsKey(baseKey+"1")?ltvMap.get(baseKey+"1"):0;
				long ltv7 = ltvMap.containsKey(baseKey+"7")?ltvMap.get(baseKey+"7"):0;
				long ltv30 = ltvMap.containsKey(baseKey+"30")?ltvMap.get(baseKey+"30"):0;
				long ltv60 = ltvMap.containsKey(baseKey+"60")?ltvMap.get(baseKey+"60"):0;
				long retainTotalIncome = incomeMap.containsKey(incomeKey+"retainTotalIncome")?incomeMap.get(incomeKey+"retainTotalIncome"):0;
				long retainIncome = incomeMap.containsKey(incomeKey+"retainIncome")?incomeMap.get(incomeKey+"retainIncome"):0;
				long totalIncome = incomeMap.containsKey(incomeKey+"totalIncome")?incomeMap.get(incomeKey+"totalIncome"):0;
				long payDevice = incomeMap.containsKey(incomeKey+"payDevice")?incomeMap.get(incomeKey+"payDevice"):0;
				long totalDevice = incomeMap.containsKey(incomeKey+"totalDevice")?incomeMap.get(incomeKey+"totalDevice"):0;
				
				jo.put("reten1", reten0!=0?df.format(reten1/(reten0*0.01)) + "%" : "--");
				jo.put("reten3", reten0!=0?df.format(reten3/(reten0*0.01)) + "%" : "--");
				jo.put("reten7", reten0!=0?df.format(reten7/(reten0*0.01)) + "%" : "--");
				jo.put("reten30", reten0!=0?df.format(reten30/(reten0*0.01)) + "%" : "--");
				jo.put("ltv0", reten0!=0?df.format((ltv0*0.01)/(reten0+0.00001)) + "" : "--");
				jo.put("ltv1", reten0!=0?df.format((ltv1*0.01)/(reten0+0.00001)) + "" : "--");
				jo.put("ltv7", reten0!=0?df.format((ltv7*0.01)/(reten0+0.00001)) + "" : "--");
				jo.put("ltv30", reten0!=0?df.format((ltv30*0.01)/(reten0+0.00001)) + "" : "--");
				jo.put("ltv60", reten0!=0?df.format((ltv60*0.01)/(reten0+0.00001)) + "" : "--");
				jo.put("retainTotalIncome", retainTotalIncome/100);
				jo.put("retainIncome", retainIncome/100);
				jo.put("totalIncome", totalIncome/100);
				jo.put("payDevice", payDevice);
				jo.put("totalDevice", totalDevice);
				
			jsonArray.add(jo);
		}
		return jsonArray;
	}
	
	

	private void getSelectedCondition(BuyFlowStat buyFlowStat) {
		if(buyFlowStat.getGroupByDay() == 0){	
			if(buyFlowStat.getGroup() == 0){
				buyFlowStat.setGroupBy("media_id");
			}else {
				buyFlowStat.setGroupBy("buyFlowName");
			}
		}
		if(buyFlowStat.getGroupByDay() == 1){
			buyFlowStat.setGroupBy("stats_date");
//			if(buyFlowStat.getGroup() == 0){
//				buyFlowStat.setGroupBy(buyFlowStat.getGroupBy() + ",media");
//			}else {
//				buyFlowStat.setGroupBy(buyFlowStat.getGroupBy() + ",buyFlowName");
//			}
		}
	}
	/**
	 * 留存1,3,7,30,60
	 */
	private Map<String, Long> queryRetainStats(String method,String startDate, String endDate){
		
		BuyFlowRetainStats vo = new BuyFlowRetainStats();
		vo.setStartDate(startDate);
		vo.setEndDate(endDate);
		if("-1".equals(method)){
			vo.setStatsDate("-1");
		}
		// 7留活跃数：key like 2000_1_WeAD_1_20170608_7
		return buyFlowRetainStatsService.queryRetain(vo);
		
	}
	
	/**
	 * 留存1,3,7,30,60
	 */
	private Map<String, Long> queryRetainStatsByDay(String method,String startDate, String endDate){
		
		BuyFlowRetainStats vo = new BuyFlowRetainStats();
		vo.setStartDate(startDate);
		vo.setEndDate(endDate);
		if("-1".equals(method)){
			vo.setStatsDate("-1");
		}
		// 7留活跃数：key like 2000_1_WeAD_1_20170608_7
		return buyFlowRetainStatsService.queryRetainByDay(vo);
		
	}
	/**
	 * 媒体   留存1,3,7,30,60
	 */
	private Map<String, Long> queryMediaRetainStats(String method,String startDate, String endDate){
		
		BuyFlowRetainStats vo = new BuyFlowRetainStats();
		vo.setStartDate(startDate);
		vo.setEndDate(endDate);
		if("-1".equals(method)){
			vo.setStatsDate("-1");
		}
		// 7留活跃数：key like 20170603_gdt_0
		return buyFlowRetainStatsService.queryMediaRetain(vo);
		
	}
	
	/**
	 * ltv
	 * @param method
	 * @param startDate
	 * @param endDate
	 */
	private Map<String, Long> queryRetainIncome(String method,String startDate, String endDate){
		BuyFlowRetainIncomeStats vo = new BuyFlowRetainIncomeStats();
		vo.setStartDate(startDate);
		vo.setEndDate(endDate);
		if("-1".equals(method)){
			vo.setStatsDate("-1");
		}
		// 0-7日总付费，key like 2000_1_WeAD_1_20170608_7
		return buyFlowRetainIncomeStatsService.queryRetainIncome(vo);
	}
	
	/**
	 * 某天的汇总  ltv
	 * @param method
	 * @param startDate
	 * @param endDate
	 */
	private Map<String, Long> queryRetainIncomeByDay(String method,String startDate, String endDate){
		BuyFlowRetainIncomeStats vo = new BuyFlowRetainIncomeStats();
		vo.setStartDate(startDate);
		vo.setEndDate(endDate);
		if("-1".equals(method)){
			vo.setStatsDate("-1");
		}
		// 0-7日总付费，key like 2000_1_WeAD_1_20170608_7
		return buyFlowRetainIncomeStatsService.queryRetainIncomeByDay(vo);
	}
	
	/**
	 * 媒体 ltv
	 * @param method
	 * @param startDate
	 * @param endDate
	 */
	private Map<String, Long> queryMediaRetainIncome(String method,String startDate, String endDate){
		BuyFlowRetainIncomeStats vo = new BuyFlowRetainIncomeStats();
		vo.setStartDate(startDate);
		vo.setEndDate(endDate);
		if("-1".equals(method)){
			vo.setStatsDate("-1");
		}
		// 0-7日总付费，key like 2000_1_WeAD_1_20170608_7
		return buyFlowRetainIncomeStatsService.queryMediaRetainIncome(vo);
	}
	
	
	/**
	 * 区间内付费总金额：在选择日期内，新增用户60天内留存付费总金额。
	 *	新增用户付费金额：在选择日期内，当日新增且当日付费的总金额。
	 *	新增付费设备数：在选择日期内，当日新增且当日付费的设备数。
	 *	总付费：在选择日期内，全部设备的付费总金额。
	 *	总付费设备数：在选择日期内，付费总设备数。
	 * @param method
	 * @param startDate
	 * @param endDate
	 */
	private Map<String, Long> queryIncome(int group,String method,String startDate, String endDate){
		BuyFlowTotalIncomeStats vo = new BuyFlowTotalIncomeStats();
		vo.setStartDate(startDate);
		vo.setEndDate(endDate);
		vo.setGroup(group);
		if("-1".equals(method)){
			vo.setStatsDate("-1");
		}
		// 0-7日总付费，key like 2000_1_WeAD_1_20170608_7
		return buyFlowTotalIncomeStatsService.queryIncomeData(vo);
	}
	
}
 