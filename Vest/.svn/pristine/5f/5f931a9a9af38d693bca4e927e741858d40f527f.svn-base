/*
 * 
 */
package me.ckhd.opengame.app.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.app.entity.WebAccess;
import me.ckhd.opengame.app.service.WebAccessService;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.ipip.IP;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * web访问统计功能
 * 
 * @author qibiao
 */
@Controller
public class WebAccessController extends BaseController {

	@Autowired
	private WebAccessService webAccessService;

	@ModelAttribute("webAccess")
	public WebAccess get(@RequestParam(required=false) String id) {
		return new WebAccess();
	}
	
	@RequestMapping(value = "webaccess")
	@ResponseBody
	public Map<String, Object> access(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("resultCode", 0);
			
			WebAccess webAccess = new WebAccess();
			webAccess.setCkappid(request.getParameter("ckappid"));
			webAccess.setChannelid(request.getParameter("channelid"));
			webAccess.setSource(request.getParameter("source"));
			webAccess.setItem(request.getParameter("item"));
			
			try{
				String ip = StringUtils.getRemoteAddr(request);
				webAccess.setIp(ip);
				String[] ipDatas = IP.find(ip);
				webAccess.setCountry(ipDatas[0]);
				webAccess.setProvince(ipDatas[1]);
				webAccess.setCity(ipDatas[2]);
			}catch(Throwable t){
				// NOP
			}
			
			webAccessService.save(webAccess);
		} catch (Throwable t) {
			result.put("resultCode", -1);
			result.put("msg", "save fail");
			logger.error("", t);
		}
		return result;
	}
	
	@RequestMapping(value = "${adminPath}/stats/webAccessStats")
	public String stats(WebAccess webAccess, Model model, HttpServletRequest request) {
		//
		if (webAccess.getStartDate() == null) {
			webAccess.setStartDate(DateUtils.getDate());
		} else {
			webAccess.setEndDate(DateUtils.formatDate(DateUtils
					.nextDate(DateUtils.parseDate(webAccess.getStartDate())),
					"yyyy-MM-dd"));
			model.addAttribute("result",
					webAccessService.statsWebAccessNum(webAccess));
		}
		return "modules/stats/webAccessStats";
	}
	
}
