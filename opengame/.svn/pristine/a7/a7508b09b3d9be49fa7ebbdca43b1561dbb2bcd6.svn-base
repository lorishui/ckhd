package me.ckhd.opengame.stats.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.andapi.entity.AndAPPOrder;
import me.ckhd.opengame.app.utils.AppCarriersUtils;
import me.ckhd.opengame.app.utils.ChannelUtils;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.stats.entity.AndChannelProvince;
import me.ckhd.opengame.stats.entity.AndChannelProvince;
import me.ckhd.opengame.stats.entity.MmChannelProvince;
import me.ckhd.opengame.stats.service.AndOrderStatsService;
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

@Controller
@RequestMapping("${adminPath}/stats")
public class AndOrderStatsController extends BaseController {

	private static Logger LOG = LoggerFactory.getLogger(AndOrderStatsController.class);
	private static DateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	@Autowired
	private AndOrderStatsService andOrderStatsService;
	
	@ModelAttribute("andapporder")
	public AndAPPOrder get() {
		return new AndAPPOrder();
	}
	@RequestMapping("andorder")
	public String stats(Model model ,AndAPPOrder andapporder){
		if (!Verdict.isAllow(andapporder.getCkappId())) {
			return "403";
		}
		String ckappId = andapporder.getCkappId();
		Date beginDate = andapporder.getBeginDate();
		Date endDate = andapporder.getEndDate();
		
		List<Dict> filterRoles = DictUtils.getDictList("filter_data_role");
		andapporder.setFilterRole(UserUtils.getUser().getRoleList(),filterRoles);
		andapporder.setFilterRate(DictUtils.getFilterRate(ckappId));
		
		if(!StringUtils.isEmpty(ckappId) && beginDate != null && endDate != null){
			model.addAttribute("orderList",andOrderStatsService.stats(andapporder));
			model.addAttribute("acountList",andOrderStatsService.statsForAccout(andapporder));
			model.addAttribute("paycodeList", andOrderStatsService.statsPaycode(andapporder));
			model.addAttribute("stats_channel", andOrderStatsService.statsByChannel(andapporder));
//			model.addAttribute("stats_channelpaycode", andOrderStatsService.statsByChannelPaycode(andapporder));
			model.addAttribute("stats_province", andOrderStatsService.statsByProvince(andapporder));
			model.addAttribute("returnstatuList", andOrderStatsService.statsByReturnStatus(andapporder));
			model.addAttribute("statsByErrorProvince",andOrderStatsService.statsByErrorProvince(andapporder));
		}else{
			//默认今天
			andapporder.setBeginDate(DateUtils.parseDate(DateUtils.getDate()));
			String endDateStr = DateUtils.formatDate(andapporder.getBeginDate(),"yyyyMMdd") + "235959";
			try {
				andapporder.setEndDate(DateUtils.parseDate(endDateStr,"yyyyMMddHHmmss"));
			} catch (ParseException e) {}
		}
		model.addAttribute("andapporder", andapporder);
		
		return "modules/stats/andOrderStats";
	}
	
	@RequestMapping("anderrororders")
	public String queryErrorOrders(Model model ,HttpServletRequest request, HttpServletResponse response,AndAPPOrder andapporder){
		if (!Verdict.isAllow(andapporder.getCkappId())) {
			return "403";
		}
		
		String ckappId = andapporder.getCkappId();
		Date beginDate = andapporder.getBeginDate();
		Date endDate = andapporder.getEndDate();
		if(!StringUtils.isEmpty(ckappId) && beginDate != null && endDate != null){
			Page<AndAPPOrder> page = andOrderStatsService.findPage(new Page<AndAPPOrder>(request, response), andapporder);
			model.addAttribute("page", page);
		}else{
			//默认今天
			andapporder.setBeginDate(DateUtils.parseDate(DateUtils.getDate()));
			String endDateStr = DateUtils.formatDate(andapporder.getBeginDate(),"yyyyMMdd") + "235959";
			try {
				andapporder.setEndDate(DateUtils.parseDate(endDateStr,"yyyyMMddHHmmss"));
			} catch (ParseException e) {}
		}
		model.addAttribute("andapporder", andapporder);
		
		return "modules/stats/andErrorOrders";
	}
	
	/**
	 * 导出和游戏失败订单报表
	 * @param request
	 * @param response
	 * @param andapporder
	 * @return
	 */
	@RequestMapping("exportAnderrororders")
	public String exportErrorOrdersExcelList(HttpServletRequest request,HttpServletResponse response,AndAPPOrder andapporder){
		List<AndAPPOrder> list = new ArrayList<AndAPPOrder>();
		if(andapporder != null){
			String ckappId = andapporder.getCkappId();
			Date beginDate = andapporder.getBeginDate();
			Date endDate = andapporder.getEndDate();
			if(!StringUtils.isEmpty(ckappId) && beginDate != null && endDate != null){
				Page<AndAPPOrder> p = new Page<AndAPPOrder>();
				p.setPageNo(1);
				p.setPageSize(10000);
				Page<AndAPPOrder> page = andOrderStatsService.findPage(p, andapporder);
				list = page.getList();
			}
		}
		//step2:设置导出报表格式
		String[] headers = {"序号","计费编码说明","错误码","错误码说明","总价(元)","发生时间"};
		XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet xss = wb.createSheet();     
        wb.setSheetName(0, "和游戏失败订单报表");
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
        	AndAPPOrder andApp = list.get(i);
        	String creatDate = "";
        	if(andApp.getCreateDate() != null){
        		creatDate = format.format(andApp.getCreateDate());
        	}
        	String consumeCode = AppCarriersUtils.getPaycodeName("ANDGAME", andApp.getConsumeCode(), "");
        	if(StringUtils.isEmpty(consumeCode)){
        		consumeCode = "" ;
        	}
        	this.insertExcel(row, 0, (i+1)+"");
            this.insertExcel(row, 1,consumeCode+"("+andApp.getConsumeCode()+")");
            this.insertExcel(row, 2, andApp.getStatus());
            this.insertExcel(row, 3, DictUtils.getDictLabel(andApp.getStatus(),"andErrorCode",""));
            this.insertExcel(row, 4, Double.parseDouble(andApp.getPrice())*0.01+"");
            this.insertExcel(row, 5, creatDate);
            Iterator it1 = row.cellIterator();
            while(it1.hasNext()){
                XSSFCell cell = (XSSFCell) it1.next();
                cell.setCellStyle(style1);
            }
        } 
        //设置列宽
        for(int j=0;j<6;j++){
        	if(j==1){
        		xss.setColumnWidth((short)1, (short)(24*300));
        	} else if(j==3){
        		xss.setColumnWidth((short)3, (short)(24*200));
        	} else if(j==5){
        		xss.setColumnWidth((short)5, (short)(24*200));
        	} else {
        		xss.setColumnWidth((short)j, (short)(24*100));
        	}
        }
      //step4:把文档输出到浏览器，关闭流对象         
        try {
        	response.reset();
        	response.setContentType("application/vnd.ms-excel; charset=utf-8");
        	response.setHeader("Content-Disposition","attachment;filename=andAppOrderExcelList.xlsx");
        	ServletOutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();//关闭流对象
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("导出和游戏失败订单报表异常.");
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
	
	@RequestMapping("andSealProvince")
	public String sealProvince(AndAPPOrder andAPPOrder, Model model) {
		if (!Verdict.isAllow(andAPPOrder.getCkappId())) {
			return "403";
		}
		if(!StringUtils.isEmpty(andAPPOrder.getTime()) && !StringUtils.isEmpty(andAPPOrder.getCkappId())){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("ckAppId", andAPPOrder.getCkappId());
			if(andAPPOrder.getContentId() != null){
				map.put("contentId", andAPPOrder.getContentId());
			}
			String today = andAPPOrder.getTime();
			map.put("day", today);
			map.put("start", today+" 00:00:00");
			map.put("end", today+" 23:59:59");
			List<Map<String,Object>> list = this.andOrderStatsService.sealProvince(map);
			model.addAttribute("today", list);
			List<Map<String,Object>> total = this.andOrderStatsService.sealProvinceTotal(map);
			model.addAttribute("total", total);
			try {
				String yesterday = DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(andAPPOrder.getTime(), "yyyy-MM-dd"), -1),"yyyy-MM-dd");
				map.put("day", yesterday);
				map.put("start", yesterday+" 00:00:00");
				map.put("end", yesterday+" 23:59:59");
				List<Map<String,Object>> yes = this.andOrderStatsService.sealProvince(map);
				model.addAttribute("yeday", yes);
			} catch (ParseException e) {
				LOG.error("上传的时间格式有问题", e);
			}
		}
		model.addAttribute("andgame", andAPPOrder);
		return "modules/stats/andSealProvince";
	}
	
	@RequestMapping("andChannelProvince")
	public String stats(AndChannelProvince andChannelProvince,User user, Model model,HttpServletRequest request,
			HttpServletResponse response) {
		if (!Verdict.isAllow(andChannelProvince.getCkappId())) {
			return "403";
		}
		String ckappId = andChannelProvince.getCkappId();
		Date startDate = andChannelProvince.getStartDate();
		Date endDate = andChannelProvince.getEndDate();
		List<Dict> filterRoles = DictUtils.getDictList("filter_data_role");
		andChannelProvince.setFilterRole(UserUtils.getUser().getRoleList(),filterRoles);
		
		andChannelProvince.setFilterRate(DictUtils.getFilterRate(ckappId));
		
		if(!StringUtils.isEmpty(ckappId) && !StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)){
			
			Page<AndChannelProvince> p = new Page<AndChannelProvince>(request,response);
			setOrderCondition(andChannelProvince,p);
			
			Page<AndChannelProvince> page = andOrderStatsService.findChannelProvincePage(p, andChannelProvince);
			model.addAttribute("page", page);
			List<Object> list = andOrderStatsService.getSucc(andChannelProvince.getCkappId(),andChannelProvince.getContentId(),
					andChannelProvince.getVersionId(),andChannelProvince.getProvinceId(),
					andChannelProvince.getChannelId(),andChannelProvince.getFilterRole(),andChannelProvince.getFilterRate(),
					andChannelProvince.getStartDate(),andChannelProvince.getEndDate());
			model.addAttribute("succ", list);
		}else{
			//默认今天
			andChannelProvince.setStartDate(DateUtils.parseDate(DateUtils.getDate()));
			String endDateStr = DateUtils.formatDate(andChannelProvince.getStartDate(),"yyyyMMdd") + "235959";
			try {
				andChannelProvince.setEndDate(DateUtils.parseDate(endDateStr,"yyyyMMddHHmmss"));
			} catch (ParseException e) {}
		}

		model.addAttribute("andChannelProvince", andChannelProvince);
		
		return "modules/stats/andChannelProvinceStats";

	}
	
	private void setOrderCondition(AndChannelProvince andChannelProvince, Page<AndChannelProvince> page) {
		if(andChannelProvince.getOrderByTP() == 1){
//				mmChannelProvince.setOrderBy("totalPrice,channelId " + mmChannelProvince.getOrderDire());
			page.setOrderBy("totalPrice " + andChannelProvince.getOrderDire() + ",channelId");
		}else if(andChannelProvince.getOrderBySP() == 1){
//				mmChannelProvince.setOrderBy("succPrice,channelId " + mmChannelProvince.getOrderDire());
			page.setOrderBy("succPrice " + andChannelProvince.getOrderDire() + ",channelId ");
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
	@RequestMapping("exportAndChannelProvince")
	public String exportMMChannelProvinceList(HttpServletRequest request,HttpServletResponse response,AndChannelProvince andChannelProvince){
		List<AndChannelProvince> list = new ArrayList<AndChannelProvince>();
		List<Object> succ = new ArrayList<Object>();
		if(andChannelProvince != null){
			String ckappId = andChannelProvince.getCkappId();
			Date beginDate = andChannelProvince.getStartDate();
			Date endDate = andChannelProvince.getEndDate();
			if(!StringUtils.isEmpty(ckappId) && beginDate != null && endDate != null){
				Page<AndChannelProvince> page = andOrderStatsService.findChannelProvincePage(new Page<AndChannelProvince>(request,response), andChannelProvince);
				list = page.getList();
				succ = andOrderStatsService.getSucc(andChannelProvince.getCkappId(),andChannelProvince.getContentId(),
						andChannelProvince.getVersionId(),andChannelProvince.getProvinceId(),
						andChannelProvince.getChannelId(),andChannelProvince.getFilterRole(),andChannelProvince.getFilterRate(),
						andChannelProvince.getStartDate(),andChannelProvince.getEndDate());
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
        wb.setSheetName(0, "and渠道省份统计");
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
        	AndChannelProvince andCP = list.get(i);
        	String channelName = ChannelUtils.getChannelName(andCP.getChannelId(), "");
        	DecimalFormat df = new DecimalFormat("#.00");
        	
        	this.insertExcel(row, 0, andCP.getChannelId());			//渠道id
            this.insertExcel(row, 1, channelName);					//渠道名称
            this.insertExcel(row, 2, DictUtils.getDictLabel(andCP.getProvinceId(), "province", andCP.getProvinceId()));	//省份名称
            this.insertExcel(row, 3, andCP.getTotalNum()+"");						//充值总数量
            this.insertExcel(row, 4, df.format((andCP.getTotalNum()*0.01)/(((long)data.get("total_num"))*0.01)*100)+"%");	//充值总量占比
            this.insertExcel(row, 5, andCP.getSuccNum()+"");					//充值成功数量
            this.insertExcel(row, 6, df.format((andCP.getSuccNum()*0.01)/((long)data.get("succ_num")*0.01)*100)+"%");		//充值成功数量占比
            this.insertExcel(row, 7, df.format((andCP.getSuccNum()*0.01)/(andCP.getTotalNum()*0.01)*100)+"%");				//成功率		
            this.insertExcel(row, 8, andCP.getTotalPrice()*0.01+"");			//订购总价
            this.insertExcel(row, 9, df.format(andCP.getTotalPrice()/((BigDecimal)data.get("total_price")).doubleValue()*100)+"%");			//订购总价比例
            this.insertExcel(row, 10, andCP.getSuccPrice()*0.01+"");			//成功总价(元)
            this.insertExcel(row, 11, df.format(andCP.getSuccPrice()/((BigDecimal)data.get("succ_price")).doubleValue()*100)+"%");			//成功总价比例
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
            LOG.error("导出and渠道省份报表异常.");
        }
        return null;
      
    }
	
}



