package me.ckhd.opengame.app.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.QQActivity;
import me.ckhd.opengame.app.service.QQActivityService;
import me.ckhd.opengame.app.utils.AppCkUtils;
import me.ckhd.opengame.common.utils.MD5Util;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.utils.excel.ExportExcel;
import me.ckhd.opengame.common.web.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value = "${adminPath}/app")
public class QQActivityController extends BaseController{
	private String key = "b4c862c6d7aadb6da3fcc80187603651";
	
	@Autowired
	private QQActivityService qqActivityService;

	@RequestMapping(value = "qqactivity",produces="text/plain;charset=UTF-8")
	@ResponseBody
	public Object activity(@RequestParam(required=false) String ckAppId, @RequestParam(required=false) String qq, @RequestParam(required=false) String imsi,
			@RequestParam(required=false) String sign,HttpServletRequest request,HttpServletResponse response){
		logger.info("data="+ckAppId+qq+sign+imsi);
		if(StringUtils.isNotBlank(ckAppId) && StringUtils.isNotBlank(qq) && StringUtils.isNotBlank(imsi)){
			Pattern pattern = Pattern.compile("[1-9]\\d{4,}");
			Matcher matcher = pattern.matcher(qq);
			if(matcher.matches()){
				QQActivity qqAct = new QQActivity();
				qqAct.setCkAppId(ckAppId);
				qqAct.setImsi(imsi);
				qqAct.setSign(sign);
				qqAct.setQq(qq);
				int isExistImsi = qqActivityService.existImsi(imsi,ckAppId);
				if(isExistImsi>0){
					return "手机号已经参加过活动!";
				}
				int isExistQQ = qqActivityService.existQQ(qq,ckAppId);
				if(isExistQQ>0){
					return "QQ号已经参加过活动!";
				}
				try{
					int n = qqActivityService.add(qqAct);
					if(n > 0) return "SUCCESS";
				}catch(Exception e){
					logger.error("qq activity insert!", e);
				}
			}else{
				return "QQ号不正确";
			}
		}else if(StringUtils.isBlank(imsi)){
			return "请插入电话卡";
		}else if(StringUtils.isBlank(qq)){
			return "QQ号不能为空!";
		}
		return "其他错误！联系客服！";
	}
	
	@RequestMapping(value = "qqactivityNew")
	@ResponseBody
	public Object qqactivityNew(@RequestParam(required=false) String ckAppId, @RequestParam(required=false) String qq, @RequestParam(required=false) String imsi,
			@RequestParam(required=false) String sign,HttpServletRequest request,HttpServletResponse response){
		response.setContentType("application/json;charset=utf-8");
		logger.info("data="+ckAppId+qq+sign+imsi);
		JSONObject result = new JSONObject();
		if(StringUtils.isNotBlank(ckAppId) && StringUtils.isNotBlank(qq) && 
				StringUtils.isNotBlank(sign) && sign.equals(MD5Util.string2MD5(key+qq))
				&& StringUtils.isNotBlank(imsi)){
			Pattern pattern = Pattern.compile("[1-9]\\d{4,}");
			Matcher matcher = pattern.matcher(qq);
			if(matcher.matches()){
				QQActivity qqAct = new QQActivity();
				qqAct.setCkAppId(ckAppId);
				qqAct.setImsi(imsi);
				qqAct.setSign(sign);
				qqAct.setQq(qq);
				int isExistImsi = qqActivityService.existImsi(imsi,ckAppId);
				if(isExistImsi>0){
					result.put("resultCode", 2001);
					result.put("msg", "手机号已经参加过活动!");
					return result;
				}
				int isExistQQ = qqActivityService.existQQ(qq,ckAppId);
				if(isExistQQ>0){
					result.put("resultCode", 2002);
					result.put("msg", "QQ号已经参加过活动!");
					return result;
				}
				try{
					int n = qqActivityService.add(qqAct);
					if(n > 0) {
						result.put("resultCode", 2000);
						result.put("msg", "成功参加活动");
						return result;
					}
				}catch(Exception e){
					logger.error("qq activity insert!", e);
				}
			}else{
				result.put("resultCode", 2006);
				result.put("msg", "qq号不正确");
				return result;
			}
		}else if(StringUtils.isBlank(imsi)){
			result.put("resultCode", 2003);
			result.put("msg", "请插入电话卡");
			return result;
		}else if(StringUtils.isBlank(qq)){
			result.put("resultCode", 2004);
			result.put("msg", "QQ号不能为空!");
			return result;
		}
		result.put("resultCode", 2005);
		result.put("msg", "其他错误！联系客服！");
		return result;
	}
	
	/*@RequestMapping(value = "qqActivityForm")
	public String form(@RequestParam(required=false) String ckAppId,@RequestParam(required=false) String qq, Model model) {
		if(StringUtils.isNotBlank(ckAppId) && StringUtils.isNotBlank(qq)  ){
			QQActivity qqAct = new QQActivity();
			qqAct.setCkAppId(ckAppId);
			qqAct.setQq(qq);
			try{
				int n = qqActivityService.add(qqAct);
				if(n > 0){
					model.addAttribute("resultCode", "0");
					model.addAttribute("errMsg", "成功");
					return "modules/app/qqActivityResult";
				}
			}catch(Exception e){
				logger.error("qq activity insert!", e);
			}
		}
		return "modules/app/qqActivityForm";
	}*/
	
	@RequestMapping("down")
	public void down(String startTime,String endTime,String ckAppId,HttpServletResponse response){
		if( StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime) && StringUtils.isNotBlank(ckAppId) ){
			String ckAppName = AppCkUtils.getByCkAppName(ckAppId);
			if( ckAppName != null ){
				String fileName = startTime+"到"+endTime+ckAppName+"的qq活动数据.xlsx";
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("startTime", startTime+" 00:00:00");
				map.put("endTime", endTime + " 23:59:59");
				map.put("ckAppId", ckAppId);
				String[] title = {"qq"};
				String sheetName = ckAppName+"QQ活动数据";
				List<Map<String,Object>> exportData = this.qqActivityService.getQQDataByTimeAndCkAppId(map);
				ExportExcel export = new ExportExcel("", title,sheetName.toString());
				export.setDataList(exportData, title);
				try {
					export.write(response, fileName);
				} catch (IOException e) {
					logger.error("导出出问题!!!", e);
				}
			}
		}
	}
	
	@RequestMapping("export")
	public String export(Model model){
		model.addAttribute("qqAct", new QQActivity());
		return "modules/app/QQActivtyExport";
	}

}
