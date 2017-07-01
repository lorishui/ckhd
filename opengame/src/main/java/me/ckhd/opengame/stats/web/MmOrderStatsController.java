package me.ckhd.opengame.stats.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.utils.AppCarriersUtils;
import me.ckhd.opengame.app.utils.ChannelUtils;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.mmapi.entity.MmAppOrder;
import me.ckhd.opengame.stats.entity.MmChannelProvince;
import me.ckhd.opengame.stats.entity.OrderCnt;
import me.ckhd.opengame.stats.service.MmOrderStatsService;
import me.ckhd.opengame.sys.entity.Dict;
import me.ckhd.opengame.sys.entity.User;
import me.ckhd.opengame.sys.utils.DictUtils;
import me.ckhd.opengame.sys.utils.UserUtils;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("ck/stats")
public class MmOrderStatsController {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Logger LOG = LoggerFactory.getLogger(MmOrderStatsController.class);
	
	@Autowired
	private MmOrderStatsService mmOrderStatsService;

	@ModelAttribute("mmapporder")
	public MmAppOrder get(@RequestParam(required=false) String id) {
		return new MmAppOrder();
	}

	@RequestMapping("mmorder")
	public String stats(MmAppOrder mmAppOrder,User user, Model model) {
		if (!Verdict.isAllow(mmAppOrder.getCkappId())) {
			return "403";
		}
		String ckappId = mmAppOrder.getCkappId();
		String startDate = mmAppOrder.getStartDate();
		String endDate = mmAppOrder.getEndDate();
		List<Dict> filterRoles = DictUtils.getDictList("filter_data_role");
		mmAppOrder.setFilterRole(UserUtils.getUser().getRoleList(),filterRoles);
		
		mmAppOrder.setFilterRate(DictUtils.getFilterRate(ckappId));
		
		if(!StringUtils.isEmpty(ckappId) && !StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)){
			String startDateTime = mmAppOrder.getStartDate();
			String endDateTime = mmAppOrder.getEndDate();
			mmAppOrder.setStartDate(startDateTime.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""));
			mmAppOrder.setEndDate(endDateTime.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""));
			
			long start = System.currentTimeMillis();
			
			model.addAttribute("orderList",mmOrderStatsService.stats(mmAppOrder));
			LOG.error("mm统计时间1:" + (System.currentTimeMillis() - start)/1000);
			model.addAttribute("acountList",mmOrderStatsService.statsForAccout(mmAppOrder));
			LOG.error("mm统计时间2:" + (System.currentTimeMillis() - start)/1000);
			model.addAttribute("stats_paycode", mmOrderStatsService.statsByPaycode(mmAppOrder));
			LOG.error("mm统计时间3:" + (System.currentTimeMillis() - start)/1000);
			model.addAttribute("stats_returnstatus", mmOrderStatsService.statsByReturnStatus(mmAppOrder));
			LOG.error("mm统计时间4:" + (System.currentTimeMillis() - start)/1000);
			model.addAttribute("stats_channel", mmOrderStatsService.statsByChannel(mmAppOrder));
			LOG.error("mm统计时间5:" + (System.currentTimeMillis() - start)/1000);
//			model.addAttribute("stats_channelpaycode", mmOrderStatsService.statsByChannelPaycode(mmAppOrder));
			LOG.error("mm统计时间6:" + (System.currentTimeMillis() - start)/1000);
			model.addAttribute("stats_province", mmOrderStatsService.statsByProvince(mmAppOrder));
			LOG.error("mm统计时间7:" + (System.currentTimeMillis() - start)/1000);
			model.addAttribute("statsByErrorProvince",mmOrderStatsService.statsByErrorProvince(mmAppOrder));
			LOG.error("mm统计时间8:" + (System.currentTimeMillis() - start)/1000);
			model.addAttribute("normal_statsByReturnStatus",mmOrderStatsService.normal_statsByReturnStatus(mmAppOrder));
			LOG.error("mm统计时间9:" + (System.currentTimeMillis() - start)/1000);
			mmAppOrder.setStartDate(startDateTime);
			mmAppOrder.setEndDate(endDateTime);
		}else{
			mmAppOrder.setOrderType(1);
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			String startDateTime = DateUtils.formatDate(c.getTime(),"yyyy-MM-dd HH:mm:ss");
			mmAppOrder.setStartDate(startDateTime);
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			String endDateTime = DateUtils.formatDate(c.getTime(),"yyyy-MM-dd HH:mm:ss");
			mmAppOrder.setEndDate(endDateTime);
		}

		model.addAttribute("mmapporder", mmAppOrder);
		
		return "modules/stats/mmOrderStats";

	}
	
	@RequestMapping("mmerrororders")
	public String queryErrorOrders(Model model ,HttpServletRequest request, HttpServletResponse response,MmAppOrder mmAppOrder){
		if (!Verdict.isAllow(mmAppOrder.getCkappId())) {
			return "403";
		}
		
		String ckappId = mmAppOrder.getCkappId();
		String beginDate = mmAppOrder.getStartDate();
		String endDate = mmAppOrder.getEndDate();
		if(!StringUtils.isEmpty(ckappId) && beginDate != null && endDate != null){
			mmAppOrder.setStartDate(beginDate.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""));
			mmAppOrder.setEndDate(endDate.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""));
			
			Page<MmAppOrder> page = mmOrderStatsService.findPage(new Page<MmAppOrder>(request, response), mmAppOrder);
			model.addAttribute("page", page);
			
			mmAppOrder.setStartDate(beginDate);
			mmAppOrder.setEndDate(endDate);
		}else{
			mmAppOrder.setOrderType(1);
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			String startDateTime = DateUtils.formatDate(c.getTime(),"yyyy-MM-dd HH:mm:ss");
			mmAppOrder.setStartDate(startDateTime);
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			String endDateTime = DateUtils.formatDate(c.getTime(),"yyyy-MM-dd HH:mm:ss");
			mmAppOrder.setEndDate(endDateTime);
		}
		model.addAttribute("mmapporder", mmAppOrder);
		
		return "modules/stats/mmErrorOrders";
	}
	/**
	 * 导出MM失败订单流水
	 * @param model
	 * @param response
	 * @param mmAppOrder
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("exportMmerrororders")
	public String exportErrorOrdersExcelList(HttpServletRequest request,HttpServletResponse response,MmAppOrder mmAppOrder){
		List<MmAppOrder> list = new ArrayList<MmAppOrder>();
		if(mmAppOrder != null){
			String ckappId = mmAppOrder.getCkappId();
			String beginDate = mmAppOrder.getStartDate();
			String endDate = mmAppOrder.getEndDate();
			if(!StringUtils.isEmpty(ckappId) && beginDate != null && endDate != null){
				mmAppOrder.setStartDate(beginDate.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""));
				mmAppOrder.setEndDate(endDate.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""));
				Page<MmAppOrder> p = new Page<MmAppOrder>();
				p.setPageNo(1);
				p.setPageSize(10000);
				Page<MmAppOrder> page = mmOrderStatsService.findPage(p, mmAppOrder);
				list = page.getList();
			}
		}
		//step2:设置导出报表格式
		String[] headers = {"序号","省份","渠道","计费编码说明","错误码","错误码说明","总价(元)","发生时间"};
		XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet xss = wb.createSheet();     
        wb.setSheetName(0, "mm失败订单报表");
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
       for(int i=0 ;i<list.size();i++){
        	row = xss.createRow(i+1);
        	MmAppOrder mmApp = list.get(i);
        	String ChannelName = AppCarriersUtils.getPaycodeName("mm", mmApp.getPaycode(),"");
        	if(StringUtils.isEmpty(ChannelName)){
        		ChannelName = "";
        	}
        	String paycodeName = AppCarriersUtils.getPaycodeName("mm", mmApp.getPaycode(),""); 
        	if(StringUtils.isEmpty(paycodeName)){
        		paycodeName = "";
        	}
        	this.insertExcel(row, 0, (i+1)+"");
            this.insertExcel(row, 1,DictUtils.getDictLabel(mmApp.getProvinceID(), "province", mmApp.getProvinceID()));
            this.insertExcel(row, 2, ChannelName+"("+mmApp.getChannelId()+")");
            this.insertExcel(row, 3, paycodeName+"("+mmApp.getPaycode()+")");
            this.insertExcel(row, 4, mmApp.getReturnStatus());
            this.insertExcel(row, 5, DictUtils.getDictLabels(mmApp.getReturnStatus(), "mmErrorCode", ""));
            this.insertExcel(row, 6, mmApp.getTotalPrice()*0.01+"");
            this.insertExcel(row, 7, mmApp.getActionTime().substring(0,4)+"-"+mmApp.getActionTime().substring(4,6)+
            		"-"+mmApp.getActionTime().substring(6,8)+" "+mmApp.getActionTime().substring(8,10)
            		+":"+mmApp.getActionTime().substring(10,12)+":"+mmApp.getActionTime().substring(12,14));
            Iterator it1 = row.cellIterator();
            while(it1.hasNext()){
                XSSFCell cell = (XSSFCell) it1.next();
                cell.setCellStyle(style1);
            }
        } 
        //设置列宽
        for(int j=0;j<8;j++){
        	if(j==2){
        		xss.setColumnWidth((short)2, (short)(24*220));
        	} else if(j==3){
        		xss.setColumnWidth((short)3, (short)(24*300));
        	} else if(j==5){
        		xss.setColumnWidth((short)5, (short)(24*200));
        	}  else if(j==7){
        		xss.setColumnWidth((short)7, (short)(24*220));
        	} else {
        		xss.setColumnWidth((short)j, (short)(24*100));
        	}
        }
      //step4:把文档输出到浏览器，关闭流对象         
        try {
        	response.reset();
        	response.setContentType("application/vnd.ms-excel; charset=utf-8");
        	response.setHeader("Content-Disposition","attachment;filename=mmAppOrderExcelList.xlsx");
        	ServletOutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();//关闭流对象
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("导出mm失败订单报表异常.");
        }
        return null;
      
    }
	
	private void insertExcel(XSSFRow row, int cols, String value) {
	       XSSFCell cell = row.createCell((short) cols);
	       cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	       if(value != null && !"null".equals(value))
	           cell.setCellValue(value);       
	       else
	           cell.setCellValue("");
	}
	/**
	 * 游戏概况展示
	 * @param mmAppOrder
	 * @param model
	 * @return
	 */
	@RequestMapping("andmmorder")
	public String andMmOrderStats(MmAppOrder mmAppOrder, Model model) {
		if (!Verdict.isAllow(mmAppOrder.getCkappId())) {
			return "403";
		}
		
		String startDate = mmAppOrder.getStartDate();
		if(!StringUtils.isEmpty(startDate)){
			model.addAttribute("result", this.getMmAppOrder(mmAppOrder));
			mmAppOrder.setStartDate(startDate);
		}else{
			Date date = new Date();
			Long fTime = date.getTime(); 
			date.setTime(fTime);
			mmAppOrder.setStartDate(sdf.format(date));
			model.addAttribute("result", this.getMmAppOrder(mmAppOrder));
			mmAppOrder.setStartDate(sdf.format(date));
		}
		model.addAttribute("mmapporder", mmAppOrder);
		return "modules/stats/andMmOrderStats";
	}
	
	/**
	 * 整合两个list
	 * @param mmAppOrder
	 * @return
	 */
	private List<OrderCnt> getMmAppOrder(MmAppOrder mmAppOrder){
		long start = System.currentTimeMillis();
		String startDate = mmAppOrder.getStartDate();
		Date date = new Date();
		mmAppOrder.setStartDate(startDate+" 00:00:00");
		if(startDate.equals(sdf.format(date))){
			mmAppOrder.setEndDate(sdft.format(date));
		}else{
			mmAppOrder.setEndDate(startDate+" 23:59:59");
		}
		List<Map<String, Object>> mmOrder = mmOrderStatsService.statsMmOrder(mmAppOrder);
		
		LOG.error("时间1:" + (System.currentTimeMillis() - start)/1000);
		
		List<Map<String, Object>> andOrder = mmOrderStatsService.statsAndOrder(mmAppOrder);
		
		LOG.error("时间2:" + (System.currentTimeMillis() - start)/1000);
		
		Set<String> gamePermission = UserUtils.getGamePermission();
		
		LOG.error("时间3:" + (System.currentTimeMillis() - start)/1000);
		
		LinkedHashMap<String, OrderCnt> orderCnts = new LinkedHashMap<String, OrderCnt>();
		for (Map<String, Object> item : mmOrder) {
			if(gamePermission.contains(item.get("ckapp_id").toString())){
				OrderCnt orderCnt = new OrderCnt();
				orderCnt.setCkAppId(item.get("ckapp_id").toString());
				orderCnt.setMmPrice(((BigDecimal) item.get("succ_mmprice")).intValue());
				orderCnt.setMmUserNum((long) item.get("user_num"));
				orderCnt.setMmSuccUserNum((long) item.get("succ_user_num"));
				orderCnts.put(orderCnt.getCkAppId(), orderCnt);
			}
		}
		
		LOG.error("时间4:" + (System.currentTimeMillis() - start)/1000);
		
		for (Map<String, Object> item : andOrder) {
			if(gamePermission.contains(item.get("ckapp_id").toString())){
				OrderCnt orderCnt = orderCnts.get(item.get("ckapp_id").toString());
				if(orderCnt == null){
					orderCnt = new OrderCnt();
					orderCnt.setCkAppId(item.get("ckapp_id").toString());
					orderCnts.put(orderCnt.getCkAppId(), orderCnt);
				}
				if(item.get("succ_andprice") != null){
					orderCnt.setAndPrice(((BigDecimal) item.get("succ_andprice")).intValue());
				} else {
					orderCnt.setAndPrice(0);
				}
				orderCnt.setAndUserNum((long) item.get("user_num"));
				orderCnt.setAndSuccUserNum((long) item.get("succ_user_num"));
			}
		}
		LOG.error("时间5:" + (System.currentTimeMillis() - start)/1000);
		
		Date currentDate;
		try {
			if(startDate.equals(sdf.format(date))){
				currentDate = sdft.parse(sdft.format(new Date()));
				Long fTime = currentDate.getTime() - 7 * 24 * 3600000; 
				date.setTime(fTime);
				mmAppOrder.setStartDate(sdf.format(date)+" 00:00:00");
				mmAppOrder.setEndDate(sdft.format(date));
			}else{
				currentDate = sdf.parse(startDate);
				Long fTime = currentDate.getTime() - 7 * 24 * 3600000; 
				date.setTime(fTime);
				String day = sdf.format(date);
				mmAppOrder.setStartDate(day + " 00:00:00");
				mmAppOrder.setEndDate(day + " 23:59:59");
			}
		} catch (java.text.ParseException e) {
			LOG.info("时间转换错误",e.getMessage());
		}
		
		LOG.error("时间6:" + (System.currentTimeMillis() - start)/1000);
		
		List<Map<String, Object>> mmStats = mmOrderStatsService.statsMmOrder(mmAppOrder);
		
		LOG.error("时间7:" + (System.currentTimeMillis() - start)/1000);
		
		List<Map<String, Object>> andOrderStats = mmOrderStatsService.statsAndOrder(mmAppOrder);
		
		LOG.error("时间8:" + (System.currentTimeMillis() - start)/1000);
		
		for(Map<String, Object> item: mmStats){
			OrderCnt orderCnt = orderCnts.get(item.get("ckapp_id").toString());
			if(orderCnt != null){
				orderCnt.setLastWeekMmPrice(((BigDecimal)item.get("succ_mmprice")).intValue());
			}
		}
		LOG.error("时间9:" + (System.currentTimeMillis() - start)/1000);
		
		for(Map<String, Object> item: andOrderStats){
			OrderCnt orderCnt = orderCnts.get(item.get("ckapp_id").toString());
			if(orderCnt != null){
				try{
					orderCnt.setLastWeekAndPrice(((BigDecimal)item.get("succ_andprice")).intValue());
				}catch(Throwable t){
					//
					LOG.error("orderCnt.setLastWeekAndPrice(...) error", t);
					orderCnt.setLastWeekAndPrice(0);
				}
			}
		}
		LOG.error("时间10:" + (System.currentTimeMillis() - start)/1000);
		
		List<OrderCnt> result = new ArrayList<OrderCnt>();
		OrderCnt all = new OrderCnt();
		all.setCkAppId("游戏总计");
		for(String key:orderCnts.keySet()){
			OrderCnt orderCnt = orderCnts.get(key);
			result.add(orderCnt);
			all.setMmPrice(all.getMmPrice() + orderCnt.getMmPrice());
			all.setAndPrice(all.getAndPrice() + orderCnt.getAndPrice());
			all.setMmUserNum(all.getMmUserNum() + orderCnt.getMmUserNum());
			all.setMmSuccUserNum(all.getMmSuccUserNum() + orderCnt.getMmSuccUserNum());
			
			all.setAndUserNum(all.getAndUserNum() + orderCnt.getAndUserNum());
			all.setAndSuccUserNum(all.getAndSuccUserNum() + orderCnt.getAndSuccUserNum());
			
			all.setLastWeekMmPrice(all.getLastWeekMmPrice() + orderCnt.getLastWeekMmPrice());
			all.setLastWeekAndPrice(all.getLastWeekAndPrice() + orderCnt.getLastWeekAndPrice());
		}
		result.add(0, all);
		LOG.error("时间11:" + (System.currentTimeMillis() - start)/1000);
		
		return result;
	}
	
	@RequestMapping("zzorderstats")
	public String statsZZOrderNum(MmAppOrder mmAppOrder, Model model) {
		if (!Verdict.isAllow(mmAppOrder.getCkappId())) {
			return "403";
		}
		String startDate = mmAppOrder.getStartDate();
		if(!StringUtils.isEmpty(startDate)){
			mmAppOrder.setEndDate(DateUtils.formatDate(
					DateUtils.nextDate(DateUtils.parseDate(startDate)),
					"yyyy-MM-dd"));
			model.addAttribute("result", mmOrderStatsService.statsZZOrderNum(mmAppOrder));
		}else{
			Date date = new Date();
			Long fTime = date.getTime(); 
			date.setTime(fTime);
			mmAppOrder.setStartDate(sdf.format(date));
//			model.addAttribute("result", mmOrderStatsService.statsZZOrderNum(mmAppOrder));
//			mmAppOrder.setStartDate(sdf.format(date));
		}
		model.addAttribute("mmapporder", mmAppOrder);
		return "modules/stats/zzOrderStats";
	}
	
	@RequestMapping("sealProvince")
	public String sealProvince(MmAppOrder mmAppOrder, Model model) {
		if (!Verdict.isAllow(mmAppOrder.getCkappId())) {
			return "403";
		}
		if(!StringUtils.isEmpty(mmAppOrder.getActionTime()) && !StringUtils.isEmpty(mmAppOrder.getCkappId())){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("ckAppId", mmAppOrder.getCkappId());
			if(mmAppOrder.getAppId() != null){
				map.put("appId", mmAppOrder.getAppId());
			}
			String today = mmAppOrder.getActionTime();
			map.put("day", today);
			map.put("start", today.replace("-", "")+"000000");
			map.put("end", today.replace("-", "")+"235959");
			List<Map<String,Object>> list = this.mmOrderStatsService.sealProvince(map);
			model.addAttribute("today", list);
			List<Map<String,Object>> total = this.mmOrderStatsService.sealProvinceTotal(map);
			model.addAttribute("total", total);
			try {
				String yesterday = DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(mmAppOrder.getActionTime(), "yyyy-MM-dd"), -1),"yyyy-MM-dd");
				map.put("day", yesterday);
				map.put("start", yesterday.replace("-", "")+"000000");
				map.put("end", yesterday.replace("-", "")+"235959");
				List<Map<String,Object>> yes = this.mmOrderStatsService.sealProvince(map);
				model.addAttribute("yesday", yes);
			} catch (ParseException e) {
				LOG.error("上传的时间格式有问题", e);
			}
		}
		model.addAttribute("mm", mmAppOrder);
		return "modules/stats/sealProvince";
	}
	
	@RequestMapping("mmChannelProvince")
	public String statsChannelProvince(MmChannelProvince mmChannelProvince,User user, Model model,HttpServletRequest request,
			HttpServletResponse response) {
		if (!Verdict.isAllow(mmChannelProvince.getCkappId())) {
			return "403";
		}
		String ckappId = mmChannelProvince.getCkappId();
		String startDate = mmChannelProvince.getStartDate();
		String endDate = mmChannelProvince.getEndDate();
		List<Dict> filterRoles = DictUtils.getDictList("filter_data_role");
		mmChannelProvince.setFilterRole(UserUtils.getUser().getRoleList(),filterRoles);
		
		mmChannelProvince.setFilterRate(DictUtils.getFilterRate(ckappId));
		
		if(!StringUtils.isEmpty(ckappId) && !StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)){
			String startDateTime = mmChannelProvince.getStartDate();
			String endDateTime = mmChannelProvince.getEndDate();
			mmChannelProvince.setStartDate(startDateTime.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""));
			mmChannelProvince.setEndDate(endDateTime.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""));
			
			long start = System.currentTimeMillis();
			Page<MmChannelProvince> p = new Page<MmChannelProvince>(request,response);
			setOrderCondition(mmChannelProvince,p);
			
			Page<MmChannelProvince> page = mmOrderStatsService.findChannelProvincePage(p, mmChannelProvince);
			model.addAttribute("page", page);

			
			List<Object> list = mmOrderStatsService.getSucc(mmChannelProvince.getCkappId(),mmChannelProvince.getOrderType(),
					mmChannelProvince.getAppId(),mmChannelProvince.getProvinceId(),
					mmChannelProvince.getChannelId(),mmChannelProvince.getFilterRole(),mmChannelProvince.getFilterRate(),
					mmChannelProvince.getStartDate(),mmChannelProvince.getEndDate());
			model.addAttribute("succ", list);
			mmChannelProvince.setStartDate(startDateTime);
			mmChannelProvince.setEndDate(endDateTime);
		}else{
			mmChannelProvince.setOrderType(1);
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			String startDateTime = DateUtils.formatDate(c.getTime(),"yyyy-MM-dd HH:mm:ss");
			mmChannelProvince.setStartDate(startDateTime);
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			String endDateTime = DateUtils.formatDate(c.getTime(),"yyyy-MM-dd HH:mm:ss");
			mmChannelProvince.setEndDate(endDateTime);
		}

		model.addAttribute("mmChannelProvince", mmChannelProvince);
		
		return "modules/stats/mmChannelProvinceStats";

	}
	
	private void setOrderCondition(MmChannelProvince mmChannelProvince,Page<MmChannelProvince> page) {
		if(mmChannelProvince.getOrderByTP() == 1){
//			mmChannelProvince.setOrderBy("totalPrice,channelId " + mmChannelProvince.getOrderDire());
			page.setOrderBy("totalPrice " + mmChannelProvince.getOrderDire() + ",channelId");
		}else if(mmChannelProvince.getOrderBySP() == 1){
//			mmChannelProvince.setOrderBy("succPrice,channelId " + mmChannelProvince.getOrderDire());
			page.setOrderBy("succPrice " + mmChannelProvince.getOrderDire() + ",channelId ");
		}
		
	}

	/**
	 * 导出MM渠道省份订单统计
	 * @param model
	 * @param response
	 * @param mmAppOrder
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("exportMmChannelProvince")
	public String exportMMChannelProvinceList(HttpServletRequest request,HttpServletResponse response,MmChannelProvince mmChannelProvince){
		List<MmChannelProvince> list = new ArrayList<MmChannelProvince>();
		List<Object> succ = new ArrayList<Object>();
		if(mmChannelProvince != null){
			String ckappId = mmChannelProvince.getCkappId();
			String beginDate = mmChannelProvince.getStartDate();
			String endDate = mmChannelProvince.getEndDate();
			if(!StringUtils.isEmpty(ckappId) && beginDate != null && endDate != null){
				mmChannelProvince.setStartDate(beginDate.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""));
				mmChannelProvince.setEndDate(endDate.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""));
				Page<MmChannelProvince> p = new Page<MmChannelProvince>();
				p.setPageNo(1);
				p.setPageSize(10000);
				Page<MmChannelProvince> page = mmOrderStatsService.findChannelProvincePage(p, mmChannelProvince);
				list = page.getList();
				
				succ = mmOrderStatsService.getSucc(mmChannelProvince.getCkappId(),mmChannelProvince.getOrderType(),
						mmChannelProvince.getAppId(),mmChannelProvince.getProvinceId(),
						mmChannelProvince.getChannelId(),mmChannelProvince.getFilterRole(),mmChannelProvince.getFilterRate(),
						mmChannelProvince.getStartDate(),mmChannelProvince.getEndDate());
				
			}
		}
		HashMap<String, Object> data = new HashMap<String, Object>();
		if(succ.size() != 0){
			data = (HashMap<String, Object>) succ.get(0);
		}
		//step2:设置导出报表格式
		String[] headers = {"渠道号","渠道名称","省份","数量","数量比例","成功数量","成功数量占比","成功率","订购总价(元)","订购总价比例","成功总价(元)","成功金额占比"};
		XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet xss = wb.createSheet();     
        wb.setSheetName(0, "mm渠道省份统计");
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
       for(int i=0 ;i<list.size();i++){
        	row = xss.createRow(i+1);
        	MmChannelProvince mmCP = list.get(i);
        	String channelName = ChannelUtils.getChannelName(mmCP.getChannelId(), "");
        	DecimalFormat df = new DecimalFormat("#.00");
        	
        	this.insertExcel(row, 0, mmCP.getChannelId());			//渠道id
            this.insertExcel(row, 1, channelName);					//渠道名称
            this.insertExcel(row, 2, DictUtils.getDictLabel(mmCP.getProvinceId(), "province", mmCP.getProvinceId()));	//省份名称
            this.insertExcel(row, 3, mmCP.getTotalNum()+"");						//充值总数量
            this.insertExcel(row, 4, df.format((mmCP.getTotalNum()*0.01)/(((long)data.get("total_num"))*0.01)*100)+"%");	//充值总量占比
            this.insertExcel(row, 5, (mmCP.getSuccNum())+"");					//充值成功数量
            this.insertExcel(row, 6, df.format(((mmCP.getSuccNum())*0.01)/((long)data.get("succ_num")*0.01)*100)+"%");		//充值成功数量占比
            this.insertExcel(row, 7, df.format(((mmCP.getSuccNum())*0.01)/(mmCP.getTotalNum()*0.01)*100)+"%");				//成功率		
            this.insertExcel(row, 8, mmCP.getTotalPrice()*0.01+"");			//订购总价
            this.insertExcel(row, 9, df.format(mmCP.getTotalPrice()/((BigDecimal)data.get("total_price")).doubleValue())+"%");			//订购总价比例
            this.insertExcel(row, 10, (mmCP.getSuccPrice())*0.01+"");			//成功总价(元)
            this.insertExcel(row, 11, df.format((mmCP.getSuccPrice())/((BigDecimal)data.get("succ_price")).doubleValue())+"%");		//成功总价比例
            Iterator it1 = row.cellIterator();
            while(it1.hasNext()){
                XSSFCell cell = (XSSFCell) it1.next();
                cell.setCellStyle(style1);
            }
        } 
        //设置列宽
        for(int j=0;j<8;j++){
        	if(j==0){
        		xss.setColumnWidth((short)0, (short)(24*150));
        	} else if(j==1){
        		xss.setColumnWidth((short)1, (short)(24*170));
        	}else if(j==6){
        		xss.setColumnWidth((short)6, (short)(24*150));
        	} else if(j==8){
        		xss.setColumnWidth((short)8, (short)(24*150));
        	}  else if(j==9){
        		xss.setColumnWidth((short)9, (short)(24*160));
        	}else if(j==10){
        		xss.setColumnWidth((short)10, (short)(24*150));
        	}else if(j==11){
        		xss.setColumnWidth((short)11, (short)(24*160));
        	} else {
        		xss.setColumnWidth((short)j, (short)(24*100));
        	}
        }
      //step4:把文档输出到浏览器，关闭流对象         
        try {
        	response.reset();
        	response.setContentType("application/vnd.ms-excel; charset=utf-8");
        	response.setHeader("Content-Disposition","attachment;filename=andChannelProvinceList.xlsx");
        	ServletOutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();//关闭流对象
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("导出mm渠道省份报表异常.");
        }
        return null;
      
    }
	
}