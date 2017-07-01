package me.ckhd.opengame.adpush.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.adpush.entity.AdQueryEntity;
import me.ckhd.opengame.adpush.service.AdPushService;
import me.ckhd.opengame.adpush.service.AdStatisticsService;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import freemarker.template.SimpleDate;

@Controller
@RequestMapping(value = "${adminPath}/app/adStatistics")
public class AdStatisticsController extends BaseController{
	@Autowired
	private AdStatisticsService adStatisticsService;
	@Autowired
	private AdPushService adPushService;

	//总统计
	@RequestMapping(value = { "mainList", ""})
	public String mainList(AdQueryEntity adQueryEntity, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		if(adQueryEntity.getEndDate() == null){
			adQueryEntity.setEndDate(new Date());
			if(adQueryEntity.getStartDate() == null){
				Date date = new Date();
				date.setDate(date.getDate()-6);
				adQueryEntity.setStartDate(date);
			}
		}
		List<AdQueryEntity> list = adStatisticsService.findMainPage(adQueryEntity);
		int registNum = 0;
		double cost = 0.0,registCost = 0.0,earn = 0.0,averageEarn = 0.0;
		for (AdQueryEntity ad : list) {
			registNum += ad.getRegistNum();
			cost += ad.getCost();
			registCost += ad.getRegistCost();
			earn += ad.getEarn();
			averageEarn += ad.getAverageEarn();
		}
		model.addAttribute("registNum", registNum);
		model.addAttribute("cost", cost);
		model.addAttribute("registCost", registCost / (list.size()==0?1:list.size()));
		model.addAttribute("earn", earn);
		model.addAttribute("averageEarn", averageEarn / (list.size()==0?1:list.size()));
		
		model.addAttribute("list",list);
		model.addAttribute("startDate", adQueryEntity.getStartDate());
		model.addAttribute("endDate", adQueryEntity.getEndDate());
		
		return "modules/app/adMainStatistics";
	}
	
	//根据渠道统计
	@RequestMapping(value = { "findMediaStaList", ""})
	public String findMediaStaList(AdQueryEntity adQueryEntity, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		if(adQueryEntity.getEndDate() == null){
			adQueryEntity.setEndDate(new Date());
			if(adQueryEntity.getStartDate() == null){
				Date date = new Date();
				date.setDate(date.getDate()-6);
				adQueryEntity.setStartDate(date);
			}
		}
		Page<AdQueryEntity> page = adStatisticsService.findMediaStaList(request,response,adQueryEntity);
		model.addAttribute("page",page);
		List<Map<String, String>> allMedias = adPushService.getAllMedia();
		model.addAttribute("allMedias",allMedias);
		model.addAttribute("startDate", adQueryEntity.getStartDate());
		model.addAttribute("endDate", adQueryEntity.getEndDate());
		return "modules/app/adMediaStatistics";
	}
	
	//根据负责人统计
	@RequestMapping(value = { "findOperatorStaList", ""})
	public String findOperatorStaList(AdQueryEntity adQueryEntity, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		if(adQueryEntity.getEndDate() == null){
			adQueryEntity.setEndDate(new Date());
			if(adQueryEntity.getStartDate() == null){
				Date date = new Date();
				date.setDate(date.getDate()-6);
				adQueryEntity.setStartDate(date);
			}
		}
		Page<AdQueryEntity> page = adStatisticsService.findOperatorStaList(request,response,adQueryEntity);
		model.addAttribute("page",page);
		List<Map<String, String>> allOperators = adStatisticsService.getAllOperators();
		model.addAttribute("allOperators", allOperators);
		model.addAttribute("startDate", adQueryEntity.getStartDate());
		model.addAttribute("endDate", adQueryEntity.getEndDate());
		return "modules/app/adOperatorStatistics";
	}
	
	//根据链接统计
	@RequestMapping(value = { "findUrlStaList", ""})
	public String findUrlStaList(AdQueryEntity adQueryEntity, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		if(adQueryEntity.getEndDate() == null){
			adQueryEntity.setEndDate(new Date());
			if(adQueryEntity.getStartDate() == null){
				Date date = new Date();
				date.setDate(date.getDate()-6);
				adQueryEntity.setStartDate(date);
			}
		}
		Page<AdQueryEntity> page = adStatisticsService.findUrlStaList(request,response,adQueryEntity);
		model.addAttribute("page",page);
		List<Map<String, String>> allMedias = adPushService.getAllMedia();
		model.addAttribute("allMedias",allMedias);
		List<Map<String, String>> allGames = adPushService.getAllGames();
		model.addAttribute("allGames",allGames);
		List<Map<String, String>> allOperators = adStatisticsService.getAllOperators();
		model.addAttribute("allOperators", allOperators);
		model.addAttribute("startDate", adQueryEntity.getStartDate());
		model.addAttribute("endDate", adQueryEntity.getEndDate());
		return "modules/app/adUrlStatistics";
	}
	
	
}
