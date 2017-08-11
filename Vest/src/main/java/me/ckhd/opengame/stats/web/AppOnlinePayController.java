package me.ckhd.opengame.stats.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.utils.AppCkUtils;
import me.ckhd.opengame.app.utils.ChannelUtils;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.service.OnlinePayService;
import me.ckhd.opengame.sys.utils.DictUtils;
import me.ckhd.opengame.sys.utils.UserUtils;

import org.apache.commons.lang3.StringUtils;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;
/**
 * 
 * @author yong
 *
 */
@RequestMapping(value = "${adminPath}/stats/appOnlinePay")
@Controller
public class AppOnlinePayController extends BaseController {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Logger LOG = LoggerFactory.getLogger(AppOnlinePayController.class);
	@Autowired
	private OnlinePayService onlinePayService;
	/**
	 * 网络支付订单回调信息统计
	 * @param onlinePay
	 * @param model
	 * @return
	 */
	@RequiresPermissions("stats:appOnlinePay:view")
	@RequestMapping(value = "list")
	public String getOnliePayCunot(OnlinePay onlinePay,Model model){
		if (!Verdict.isAllow(onlinePay.getCkAppId())) {
			return "403";
		}
		String startDate = onlinePay.getStartDate();
		String endDate = onlinePay.getEndDate();
		//String ckAppId = onlinePay.getCkAppId();
		
		setPermission(onlinePay);
		
		if( StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)){
			onlinePay.setStartDate(startDate.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""));
			onlinePay.setEndDate(endDate.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""));
			model.addAttribute("onlinePayList", onlinePayService.stats(onlinePay));
			onlinePay.setIsTotal(1);
			model.addAttribute("total", onlinePayService.stats(onlinePay));//总计
			model.addAttribute("onlineChannelList", onlinePayService.statsByChannel(onlinePay));
			model.addAttribute("onlinePaycodeList", onlinePayService.statsByOnlinePaycode(onlinePay));
			model.addAttribute("onlineChannelPaycodeList", onlinePayService.statsByChannelPaycode(onlinePay));
			onlinePay.setStartDate(startDate);
			onlinePay.setEndDate(endDate);
		}else{
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			String startDateTime = DateUtils.formatDate(c.getTime(),"yyyy-MM-dd HH:mm:ss");
			onlinePay.setStartDate(startDateTime);
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			String endDateTime = DateUtils.formatDate(c.getTime(),"yyyy-MM-dd HH:mm:ss");
			onlinePay.setEndDate(endDateTime);
		}
		model.addAttribute("onlinePay",onlinePay);
		return "modules/stats/appOnlinePay";
	}

	//20170731，添加权限限制
	private void setPermission(OnlinePay onlinePay) {
		Set<String> channelPermission = UserUtils.getChannelPermission();
		if( !channelPermission.isEmpty() ) {
			onlinePay.setPermissionChannelId(StringUtils.join(channelPermission, ","));
		}
		Set<String> gamePermission = UserUtils.getGamePermission();
		if( !gamePermission.isEmpty() ) {
			onlinePay.setPermissionCkAppId(StringUtils.join(gamePermission, ","));
		}
		Set<String> gameChildPermission = UserUtils.getGameChildPermission();
		if( !gameChildPermission.isEmpty() ) {
			onlinePay.setPermissionCkAppChildId(new ArrayList<String>(gameChildPermission));
		}
	}
	/**
	 * 网络支付流水展示
	 * @param request
	 * @param response
	 * @param onlinePay
	 * @param model
	 * @return
	 */
	@RequiresPermissions("stats:appOnlinePay:view")
	@RequestMapping(value = "form")
	public String getOnliePayForm(HttpServletRequest request,HttpServletResponse response,OnlinePay onlinePay,Model model){
		if (!Verdict.isAllow(onlinePay.getCkAppId())) {
			return "403";
		}
		// 渠道的userid会有&，去除转义及增加%左匹配 add 2017-07-31
		if (StringUtils.isNotBlank(onlinePay.getUserId())) {
			onlinePay.setUserId(HtmlUtils.htmlUnescape(onlinePay.getUserId()) + "%");
		}

		setPermission(onlinePay);
		
		String startDate = onlinePay.getStartDate();
		String endDate = onlinePay.getEndDate();
		String ckAppId = onlinePay.getCkAppId();
		if(StringUtils.isNotBlank(ckAppId) && StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)){
			onlinePay.setStartDate(startDate.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""));
			onlinePay.setEndDate(endDate.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""));
			Page<OnlinePay> page = onlinePayService.findPage(new Page<OnlinePay>(request, response), onlinePay);
			onlinePay.setStartDate(startDate);
			onlinePay.setEndDate(endDate);
			model.addAttribute("page",page);
		}else{
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			String startDateTime = DateUtils.formatDate(c.getTime(),"yyyy-MM-dd HH:mm:ss");
			onlinePay.setStartDate(startDateTime);
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			String endDateTime = DateUtils.formatDate(c.getTime(),"yyyy-MM-dd HH:mm:ss");
			onlinePay.setEndDate(endDateTime);
		}
		// 删除%
		if (StringUtils.isNotBlank(onlinePay.getUserId())
				&& onlinePay.getUserId()
						.substring(onlinePay.getUserId().length() - 1)
						.equals("%")) {
			onlinePay.setUserId(onlinePay.getUserId().substring(0,
					onlinePay.getUserId().length() - 1));
		}
		model.addAttribute("onlinePay",onlinePay);
		return "modules/stats/onlinePayStats";
	}
	/**
	 * 导出网络支付流水信息
	 * @param onlinePay
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "exprotOnlinePay")
	public String exprotOnlinePay(HttpServletRequest request,HttpServletResponse response,OnlinePay onlinePay){
		String startDate = onlinePay.getStartDate();
		String endDate = onlinePay.getEndDate();
		String ckAppId = onlinePay.getCkAppId();
		if(StringUtils.isNotBlank(ckAppId) && StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)){
			onlinePay.setStartDate(startDate.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""));
			onlinePay.setEndDate(endDate.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""));
			Page<OnlinePay> p = new Page<OnlinePay>();
			p.setPageNo(1);
			p.setPageSize(10000);
			Page<OnlinePay> page = onlinePayService.findPage(p, onlinePay);
			List<OnlinePay> list = page.getList();
		    //step2:设置导出报表格式
			String[] headers = {"序号","游戏","渠道","版本号","预订单号","订单号","渠道订单号","支付方式","价格(元)","订单状态","下发状态","支付金额","下单时间","订单时间","是否网游"};
			XSSFWorkbook wb = new XSSFWorkbook();
	        XSSFSheet xss = wb.createSheet();     
	        wb.setSheetName(0, "网络支付流水信息报表");
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
	        	OnlinePay online = list.get(i);
	        	Double actualAmount = 0.00;
	        	if(StringUtils.isNotBlank(online.getActualAmount())){
	        		actualAmount = Double.parseDouble(online.getActualAmount())/100;
	        	}
	        	String upDate = "";
	        	if(online.getUpdateDate() != null){
	        		upDate = sdf.format(online.getUpdateDate());
	        	} else {
	        		upDate = sdf.format(online.getCreateDate());
	        	}
	        	this.insertExcel(row, 0, (i+1)+"");
	            this.insertExcel(row, 1,AppCkUtils.getByCkAppName(online.getCkAppId())+"("+online.getCkAppId()+")");
	            this.insertExcel(row, 2, ChannelUtils.findChannelName(online.getChannelId(), "")+"("+online.getChannelId()+")");
	            this.insertExcel(row, 3, online.getVersion());
	            this.insertExcel(row, 4, online.getPrepayid());
	            this.insertExcel(row, 5, online.getOrderId());
	            this.insertExcel(row, 6, online.getChannelOrderId());
	            this.insertExcel(row, 7, DictUtils.findLabel(online.getPayType(),""));
	            this.insertExcel(row, 8, (double) (online.getPrices()/100)+"");
	            this.insertExcel(row, 9, "-1".equals(online.getOrderStatus()) ? "创建订单失败" : 
	            	"0".equals(online.getOrderStatus()) ? "未支付" : "1".equals(online.getOrderStatus()) ? "订单申请成功" : 
	            		"2" .equals(online.getOrderStatus()) ? "订单申请失败" : "3".equals(online.getOrderStatus()) ? 
	            				"支付成功" : "4".equals(online.getOrderStatus()) ? "支付失败" : "");
	            this.insertExcel(row, 10, "1".equals(online.getSendStatus()) ? "下发中" : 
	            	"2".equals(online.getSendStatus()) ? "下发成功" : "3".equals(online.getSendStatus()) ? "下发失败" : 
	            		"4" .equals(online.getSendStatus()) ? "发货成功" : "5".equals(online.getSendStatus()) ? 
	            				"发货失败" : "");
	            this.insertExcel(row, 11, actualAmount+"");
	            this.insertExcel(row, 12, sdf.format(online.getCreateDate()));
                this.insertExcel(row, 13, upDate);
                this.insertExcel(row, 14, "1".equals(online.getGameOnline()+"") ? "网游" : "非网游");
	            Iterator it1 = row.cellIterator();
	            while(it1.hasNext()){
	                XSSFCell cell = (XSSFCell) it1.next();
	                cell.setCellStyle(style1);
	            }
	        } 
	        //设置列宽
	        for(int j=0;j<14;j++){
	        	if(j==0){
	        		xss.setColumnWidth((short)0, (short)(24*80));
	        	}else if(j==1){
	        		xss.setColumnWidth((short)1, (short)(24*180));
	        	}else if(j==2){
	        		xss.setColumnWidth((short)2, (short)(24*180));
	        	}else if(j==4){
	        		xss.setColumnWidth((short)4, (short)(24*220));
	        	}else if(j==5){
	        		xss.setColumnWidth((short)5, (short)(24*200));
	        	}else if(j==6){
	        		xss.setColumnWidth((short)6, (short)(24*200));
	        	}else if(j==12){
	        		xss.setColumnWidth((short)12, (short)(24*240));
	        	}else if(j==13){
	        		xss.setColumnWidth((short)13, (short)(24*240));
	        	}else{
	        	    xss.setColumnWidth((short)j, (short)(24*160));
	        	}
	        }
	      //step4:把文档输出到浏览器，关闭流对象         
	        try {
	        	response.reset();
	        	response.setContentType("application/vnd.ms-excel; charset=utf-8");
	        	response.setHeader("Content-Disposition","attachment;filename=onlinePayExcelList.xlsx");
	        	ServletOutputStream out = response.getOutputStream();
	            wb.write(out);
	            out.flush();
	            out.close();//关闭流对象
	        } catch (IOException e) {
	            e.printStackTrace();
	            LOG.error("导出网络支付回调信息报表异常.");
	        }
		}
		return null;
	}
	/**
	 * 根据渠道查询网络回调信息
	 * @param onlinePay
	 * @param model
	 * @return
	 */
	@RequiresPermissions("stats:appOnlinePay:view")
	@RequestMapping(value = "channel")
	public String getOnlieChannel(OnlinePay onlinePay,Model model){
		if (!Verdict.isAllow(onlinePay.getCkAppId())) {
			return "403";
		}
		String startDate = onlinePay.getStartDate();
		String endDate = onlinePay.getEndDate();
		String ckAppId = onlinePay.getCkAppId();
		if(StringUtils.isNotBlank(ckAppId) && StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)){
			onlinePay.setStartDate(startDate.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""));
			onlinePay.setEndDate(endDate.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""));
			onlinePay.setStartDate(startDate);
			onlinePay.setEndDate(endDate);
			model.addAttribute("onlinePayList", onlinePayService.stats(onlinePay));
		}else{
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			String startDateTime = DateUtils.formatDate(c.getTime(),"yyyy-MM-dd HH:mm:ss");
			onlinePay.setStartDate(startDateTime);
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			String endDateTime = DateUtils.formatDate(c.getTime(),"yyyy-MM-dd HH:mm:ss");
			onlinePay.setEndDate(endDateTime);
		}
		model.addAttribute("onlinePay",onlinePay);
		return "modules/stats/onlineChannelStats";
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
	
	@RequiresPermissions("stats:appOnlinePay:resend")
	@RequestMapping(value = "resend")
	@ResponseBody
	public String reSendOrder(@RequestParam("orderId")String orderId){
		OnlinePay onlinePay = onlinePayService.get(orderId);
		if(onlinePay != null){
			String orderStatus = onlinePay.getOrderStatus();
			String sendStatus = onlinePay.getSendStatus();
			Integer sendNum = onlinePay.getSendNum();
			if("3".equals(orderStatus) && ("5".equals(sendStatus) || ("3".equals(sendStatus)&&sendNum.intValue() > 3))){
				onlinePay.setSendStatus("3");
				onlinePay.setSendNum(0);
				onlinePayService.save(onlinePay);
				return "success";
			}
		}
		return "fail";
	}
	
	
	
}
