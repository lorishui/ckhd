package me.ckhd.opengame.stats.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ckhd.opengame.andapi.entity.AndAPPOrder;
import me.ckhd.opengame.app.entity.AppCarriers;
import me.ckhd.opengame.app.service.AppCarriersService;
import me.ckhd.opengame.stats.service.AndOrderStatsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("ck/stats")
public class AppIdController {

	@Autowired
	private AppCarriersService appCarriersService;
	@Autowired
	private AndOrderStatsService andOrderStatsService;
	@RequestMapping("queryAppCarriers")
	@ResponseBody
	public List<Map<String, String>> stats(@RequestParam("ckAppId") String ckAppId,@RequestParam("carriersType") String carriersType) {
		AppCarriers appCarriers = new AppCarriers();
		appCarriers.setCkappId(ckAppId);
		appCarriers.setCarriersType(carriersType);
		
		List<AppCarriers> queryList = appCarriersService.findList(appCarriers);
		List<Map<String, String>> rst = new ArrayList<Map<String, String>>();
		for(AppCarriers vo : queryList){
			Map<String, String> map = new HashMap<String, String>();
			
			map.put("appId", vo.getAppId());
			map.put("appName", vo.getAppName());
			
			rst.add(map);
		}
		return rst;
	}
	
	@RequestMapping("queryAppVersionId")
	@ResponseBody
	public List<Map<String, String>> statsVersionId(@RequestParam("ckAppId") String ckAppId) {
		AndAPPOrder andAPPOrder = new AndAPPOrder();
		andAPPOrder.setCkappId(ckAppId);
		List<AndAPPOrder> queryList = andOrderStatsService.findVersionId(andAPPOrder);
		List<Map<String, String>> rst = new ArrayList<Map<String, String>>();
		for(AndAPPOrder vo : queryList){
			Map<String, String> map = new HashMap<String, String>();
			map.put("versionId", vo.getVersionId());
			rst.add(map);
		}
		return rst;
	}
	
	@RequestMapping("queryAppEvent")
	@ResponseBody
	public List<Map<String, String>> eventStats(@RequestParam("ckAppId") String ckAppId) {
		AppCarriers appCarriers = new AppCarriers();
		appCarriers.setCkappId(ckAppId);
		List<AppCarriers> queryList = appCarriersService.findList(appCarriers);
		List<Map<String, String>> rst = new ArrayList<Map<String, String>>();
		for(AppCarriers vo : queryList){
			Map<String, String> map = new HashMap<String, String>();
			map.put("appId", vo.getAppId());
			map.put("appName", vo.getAppName());
			
			rst.add(map);
		}
		return rst;
	}
}
