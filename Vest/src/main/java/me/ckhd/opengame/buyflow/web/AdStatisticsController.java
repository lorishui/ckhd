package me.ckhd.opengame.buyflow.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.buyflow.entity.AdQueryEntity;
import me.ckhd.opengame.buyflow.service.AdPushService;
import me.ckhd.opengame.buyflow.service.AdStatisticsService;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.sys.utils.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
				Date date = DateUtils.beforeNumDate(6);
				adQueryEntity.setStartDate(date);
			}
		}
		
		//20170731，添加权限限制
		Set<String> mediaPermission = UserUtils.getMediaPermission();
		if( !mediaPermission.isEmpty() ) {
			adQueryEntity.setPermissionMediaId(new ArrayList<String>(mediaPermission));
		}
		Set<String> gamePermission = UserUtils.getGamePermission();
		if( !gamePermission.isEmpty() ) {
			adQueryEntity.setPermissionCkAppId(StringUtils.join(gamePermission, ","));
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
		
		return "modules/buyflow/adMainStatistics";
	}
	
	//根据渠道统计
	@RequestMapping(value = { "findMediaStaList", ""})
	public String findMediaStaList(AdQueryEntity adQueryEntity, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		if(adQueryEntity.getEndDate() == null){
			adQueryEntity.setEndDate(new Date());
			if(adQueryEntity.getStartDate() == null){
			    Date date = DateUtils.beforeNumDate(6);
				adQueryEntity.setStartDate(date);
			}
		}

		//20170731，添加权限限制
		Set<String> mediaPermission = UserUtils.getMediaPermission();
		if( !mediaPermission.isEmpty() ) {
			adQueryEntity.setPermissionMediaId(new ArrayList<String>(mediaPermission));
		}
		Set<String> gamePermission = UserUtils.getGamePermission();
		if( !gamePermission.isEmpty() ) {
			adQueryEntity.setPermissionCkAppId(StringUtils.join(gamePermission, ","));
		}
		
		Page<AdQueryEntity> page = adStatisticsService.findMediaStaList(request,response,adQueryEntity);
		model.addAttribute("page",page);
		List<Map<String, String>> allMedias = adPushService.getAllMedia();
		model.addAttribute("allMedias",allMedias);
		model.addAttribute("startDate", adQueryEntity.getStartDate());
		model.addAttribute("endDate", adQueryEntity.getEndDate());
		return "modules/buyflow/adMediaStatistics";
	}
	
	//根据负责人统计
	@RequestMapping(value = { "findOperatorStaList", ""})
	public String findOperatorStaList(AdQueryEntity adQueryEntity, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		if(adQueryEntity.getEndDate() == null){
			adQueryEntity.setEndDate(new Date());
			if(adQueryEntity.getStartDate() == null){
			    Date date = DateUtils.beforeNumDate(6);
				adQueryEntity.setStartDate(date);
			}
		}
		
		//20170731，添加权限限制
		Set<String> mediaPermission = UserUtils.getMediaPermission();
		if( !mediaPermission.isEmpty() ) {
			adQueryEntity.setPermissionMediaId(new ArrayList<String>(mediaPermission));
		}
		Set<String> gamePermission = UserUtils.getGamePermission();
		if( !gamePermission.isEmpty() ) {
			adQueryEntity.setPermissionCkAppId(StringUtils.join(gamePermission, ","));
		}
		
		Page<AdQueryEntity> page = adStatisticsService.findOperatorStaList(request,response,adQueryEntity);
		model.addAttribute("page",page);
		List<Map<String, String>> allOperators = adStatisticsService.getAllOperators(adQueryEntity);
		model.addAttribute("allOperators", allOperators);
		model.addAttribute("startDate", adQueryEntity.getStartDate());
		model.addAttribute("endDate", adQueryEntity.getEndDate());
		return "modules/buyflow/adOperatorStatistics";
	}
	
	//根据链接统计
	@RequestMapping(value = { "findUrlStaList", ""})
	public String findUrlStaList(AdQueryEntity adQueryEntity, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		if(adQueryEntity.getEndDate() == null){
			adQueryEntity.setEndDate(new Date());
			if(adQueryEntity.getStartDate() == null){
			    Date date = DateUtils.beforeNumDate(6);
				adQueryEntity.setStartDate(date);
			}
		}
		
		//20170731，添加权限限制
		Set<String> mediaPermission = UserUtils.getMediaPermission();
		if( !mediaPermission.isEmpty() ) {
			adQueryEntity.setPermissionMediaId(new ArrayList<String>(mediaPermission));
		}
		Set<String> gamePermission = UserUtils.getGamePermission();
		if( !gamePermission.isEmpty() ) {
			adQueryEntity.setPermissionCkAppId(StringUtils.join(gamePermission, ","));
		}
		
		Page<AdQueryEntity> page = adStatisticsService.findUrlStaList(request,response,adQueryEntity);
		model.addAttribute("page",page);
		List<Map<String, String>> allOperators = adStatisticsService.getAllOperators(adQueryEntity);
		model.addAttribute("allOperators", allOperators);
		model.addAttribute("startDate", adQueryEntity.getStartDate());
		model.addAttribute("endDate", adQueryEntity.getEndDate());
		return "modules/buyflow/adUrlStatistics";
	}
	
	
}
