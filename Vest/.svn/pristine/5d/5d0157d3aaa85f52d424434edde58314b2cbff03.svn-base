package me.ckhd.opengame.app.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.AppVersion;
import me.ckhd.opengame.app.entity.Channel;
import me.ckhd.opengame.app.service.AppVersionService;
import me.ckhd.opengame.app.service.ChannelService;
import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.utils.CacheUtils;
import me.ckhd.opengame.common.web.BaseController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "${adminPath}/app/appversion")
public class AppVersionController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(AppVersionController.class);
	@Autowired
	private AppVersionService appVersionService;
	@Autowired
	private ChannelService channelService;
	// 缓存锁
	private static ConcurrentMap<String, byte[]> lockMap = new ConcurrentHashMap<String, byte[]>();

	@ModelAttribute("appVersion")
	public AppVersion get(@RequestParam(required = false) String id) {
		return new AppVersion();
	}
	/**
	 * 查询APP强更数据
	 * @param request
	 * @param response
	 * @param appVersion
	 * @param model
	 * @return
	 */
	@RequiresPermissions("app:appversion:view")
	@RequestMapping(value = "list")
	public String list(HttpServletRequest request,
			HttpServletResponse response, AppVersion appVersion, Model model) {
		if (!Verdict.isAllow(appVersion.getCkAppId())) {
			return "403";
		}
		List<AppVersion> list = appVersionService.getAppVersionAll(appVersion);
		model.addAttribute("list", list);
		model.addAttribute("appversion", appVersion);
		return "modules/app/appversionList";
	}
	
	@RequiresPermissions("app:appversion:edit")
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request,AppVersion appVersion, Model model) {
		if (!Verdict.isAllow(appVersion.getCkAppId())) {
			return "403";
		}
		Map<String,String> map = new HashMap<String,String>();
		List<Channel> list = channelService.findChannelType("MM");
		if(list!= null && list.size()>0){
			for(Channel cn : list){
				map.put(cn.getId(), cn.getName());
			}
		}
		request.setAttribute("map", map);
		model.addAttribute("appversion", appVersion);
		return "modules/app/appVersionForm";
	}
	
	@RequiresPermissions("app:appversion:edit")
	@RequestMapping(value = "update")
	public String update(HttpServletRequest request,AppVersion appVersion, Model model) {
		if (!Verdict.isAllow(appVersion.getCkAppId())) {
			return "403";
		}
		AppVersion av = appVersionService.get(appVersion);
		Map<String,String> map = new HashMap<String,String>();
		List<Channel> list = channelService.findChannelType("MM");
		for(Channel cn : list){
			map.put(cn.getId(), cn.getName());
		}
		request.setAttribute("map", map);
		model.addAttribute("appversion", av);
		return "modules/app/appVersionForm";
	}
	/**
	 * 根据ID删除APP强更相应数据
	 * @param appVersion
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("app:appversion:edit")
	@RequestMapping(value = "delete")
	public String delete(AppVersion appVersion,RedirectAttributes redirectAttributes) {
		if (!Verdict.isAllow(appVersion.getCkAppId())) {
			return "403";
		}
		appVersionService.delete(appVersion); 
		addMessage(redirectAttributes, "删除强更app信息成功");
		return  "redirect:" + adminPath + "/app/appversion/list?repage";
	}
	/**
	 * 根据传入参数新增或修改强更APP数据
	 * @param request
	 * @param response
	 * @param appVersion
	 * @return
	 */
	@RequiresPermissions("app:appversion:edit")
	@RequestMapping(value = "save")
	public String save(HttpServletRequest request,
			HttpServletResponse response, AppVersion appVersion){
			if(StringUtils.isBlank(appVersion.getId())){
				appVersionService.save(appVersion);
			} else {
				appVersionService.update(appVersion);
			}
		return "redirect:" + adminPath + "/app/appversion/list?repage";
	}
	/**
	 * 强更APP版本接口入口
	 * @param appVersion
	 * @return
	 */
	@RequestMapping(value = "version")
	@ResponseBody
	public Map<String, Object> getAppVersion(AppVersion appVersion) {
		Map<String, Object> result = new HashMap<String, Object>();
		String cacheKey = "APPVERSION" + appVersion.getCkAppId();
		String indexKey = "ckChannelId_" + appVersion.getCkChannelId()
				+ ",mmAppId_" + appVersion.getMmAppId();
		String[] versionApp = getCacheAppVersion(appVersion, cacheKey, indexKey);
		Map<String, String> map = new HashMap<String, String>();
		try {
			if (versionApp != null && versionApp.length == 7) {
				result.put("resultCode", "1");
				map.put("CMCC_MM_VERSION", versionApp[0]);
				map.put("CMCC_ANDGAME_VERSION", versionApp[1]);
				map.put("CTCC_EGAME_VERSION", versionApp[2]);
				map.put("CTCC_CTE_VERSION", versionApp[3]);
				map.put("CUCC_WO_VERSION", versionApp[4]);
				map.put("CUCC_KD_VERSION", versionApp[5]);
				map.put("URL", versionApp[6]);
				result.put("result", map);
				logger.debug(String.format("强更游戏版本:[%s]", result));
			} else {
				result.put("resultCode", "-1");
				result.put("errMsg", "没有数据");
				logger.debug(String.format("强更游戏版本:[%s]", result));
			}
		} catch (Throwable t) {
			result.put("resultCode", "-1");
			result.put("errMsg", "发生异常");
			logger.error("强更游戏版本发生异常:[%s]",t);
		}
		return result;
	}
	/**
	 * 定义强更APP缓存
	 * @param appVersion
	 * @param cacheKey
	 * @param indexKey
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	private String[] getCacheAppVersion(AppVersion appVersion, String cacheKey,
			String indexKey) {
		Map<String, String[]> map = (Map<String, String[]>) CacheUtils
				.get(cacheKey);
		if (map != null) {
			return map.get(indexKey);
		}
		if (lockMap.get(cacheKey) == null) {
			lockMap.putIfAbsent(cacheKey, new byte[0]);
		}
		synchronized (lockMap.get(cacheKey)) {
			map = (Map<String, String[]>) CacheUtils.get(cacheKey);
			if (map != null) {
				return map.get(indexKey);
			}
			List<Map<String, String>> appVersionList = appVersionService
					.appVersionList(appVersion);
			if (appVersionList == null) {
				map = MapUtils.EMPTY_MAP;
			} else {
				map = new ConcurrentHashMap<String, String[]>();
				for (Map<String, String> version : appVersionList) {
					map.put(version.get("key"),
							new String[] {version.get("cmcc_mm_version"),
									version.get("cmcc_andgame_version"),
									version.get("ctcc_egame_version"),
									version.get("ctcc_cte_version"),
									version.get("cucc_wo_version"),
									version.get("cucc_kd_version"),
									version.get("url")});
				}
			}
			CacheUtils.put(cacheKey, map);
			return map.get(indexKey);
		}
	}
}
