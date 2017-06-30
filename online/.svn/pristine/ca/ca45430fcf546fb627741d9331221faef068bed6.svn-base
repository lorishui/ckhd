package me.ckhd.opengame.app.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.CfgZoneLevel;
import me.ckhd.opengame.app.entity.MetaCellinfoZone;
import me.ckhd.opengame.app.service.CfgZoneLevelService;
import me.ckhd.opengame.app.service.MetaCellinfoZoneService;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.web.BaseController;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;

@Controller
@RequestMapping(value = "${adminPath}/app/zonetree")
public class ZoneTreeController extends BaseController {

	@Autowired
	private MetaCellinfoZoneService metaCellinfoZoneService;

	@Autowired
	private CfgZoneLevelService cfgZoneLevelService;

	@RequestMapping(value = "list")
	public String list(CfgZoneLevel cfgZoneLevel, HttpServletRequest request,
			HttpServletResponse response, Model model) {

		if ("insert".equals(request.getParameter("type"))) {
			//
			CfgZoneLevel vo = new CfgZoneLevel();
			String ckappid = cfgZoneLevel.getCkappid();
			if (StringUtils.isBlank(ckappid)) {
				ckappid = "#";
			}
			vo.setCkappid(ckappid);
			String channelid = cfgZoneLevel.getChannelid();
			if (StringUtils.isBlank(channelid)) {
				channelid = "#";
			}
			vo.setChannelid(channelid);
			vo.setBiztype(cfgZoneLevel.getBiztype());
			// 渠道的配置所有游戏共用，渠道是额外增加的配置
			if(StringUtils.isBlank(cfgZoneLevel.getBiztype())){
				model.addAttribute("warning", "新增时类型必填");
			}else if(!"#".equals(channelid) && !"#".equals(ckappid)){
				model.addAttribute("warning", "针对渠道只需要一份所有游戏通用的配置");
			}else if (!cfgZoneLevelService.checkExist(vo)) {
				cfgZoneLevelService.save(vo);
				model.addAttribute("message", "新增成功");
			} else {
				model.addAttribute("warning", "新增失败，已经存在配置");
			}
		}

		Page<CfgZoneLevel> page = cfgZoneLevelService.findPage(
				new Page<CfgZoneLevel>(request, response), cfgZoneLevel);
		model.addAttribute("page", page);

		return "modules/app/zoneCfg";

	}

	@RequestMapping(value = "cfg")
	public String zoneTree(HttpServletRequest request, Model model) {

		String type = request.getParameter("type");

		String id = request.getParameter("id");

		String zonelist = request.getParameter("zonelist");

		if (zonelist != null) {
			// save zonelist
			CfgZoneLevel vo = new CfgZoneLevel();
			vo.setId(id);
			if ("1".equals(type)) {
				vo.setRedzonelist(zonelist);
				cfgZoneLevelService.updateRedzone(vo);
			} else {
				vo.setYellowzonelist(zonelist);
				cfgZoneLevelService.updateYellowzone(vo);
			}
		}

		CfgZoneLevel vo = cfgZoneLevelService.get(id);
		String redzonelist = null;
		String yellowzonelist = null;
		if (vo != null) {
			redzonelist = vo.getRedzonelist();
			yellowzonelist = vo.getYellowzonelist();
		}
		if(redzonelist == null){
			redzonelist = "";
		}
		if(yellowzonelist == null){
			yellowzonelist = "";
		}
		List<MetaCellinfoZone> zones = metaCellinfoZoneService.findAllList();

		List<Map<String, Object>> zTree = new ArrayList<Map<String, Object>>();
		for (MetaCellinfoZone zone : zones) {

			boolean nocheck = false;
			if("1".equals(zone.getLeaf())){
				if ("1".equals(type)
						&& yellowzonelist.indexOf("," + zone.getId() + ",") >= 0) {
					// 红区，已经选为黄区的不显示
					nocheck = true;
				} else if ("2".equals(type)
						&& redzonelist.indexOf("," + zone.getId() + ",") >= 0) {
					nocheck = true;
				}
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", zone.getId());
			map.put("pId", zone.getPid());
			map.put("name", zone.getName());
			map.put("open", false);
			map.put("nocheck", nocheck);
			if ("1".equals(type)) {
				map.put("checked",
						redzonelist.indexOf("," + zone.getId() + ",") >= 0 );
			} else if ("2".equals(type)) {
				map.put("checked",
						yellowzonelist.indexOf("," + zone.getId() + ",")  >= 0 );
			}
			zTree.add(map);
		}

		request.setAttribute("zTree", JSONArray.toJSONString(zTree));

		return "modules/app/ztree";

	}

}