package me.ckhd.opengame.app.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.APPCk;
import me.ckhd.opengame.app.service.AppService;
import me.ckhd.opengame.app.utils.AppCkUtils;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.utils.IdGen;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * APPController
 * @author wesley
 * @version 2015-06-29
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appck")
public class APPCkController extends BaseController{
	
	
	@Autowired
	private AppService appService;
	
	@ModelAttribute
	public  APPCk get(@RequestParam(required=false) String id){
		if (StringUtils.isNotBlank(id)) {
			return  appService.getAPPCk(id);
		}else{
			return  new APPCk();
		}
	}
	
	
	@RequiresPermissions("app:appck:view")
	@RequestMapping(value = {"list", ""})
	public String list(APPCk aPPCk,HttpServletRequest request, HttpServletResponse response, Model model) {
		if (!Verdict.isAllow(aPPCk.getCkappId())) {
			return "403";
		}
		Page<APPCk> page = appService.findAPPCk(new Page<APPCk>(request, response), aPPCk);
        model.addAttribute("page", page);
		return "modules/app/appckList";
				
	}
	
	@RequiresPermissions("app:appck:view")
	@RequestMapping(value = "form")
	public String form(APPCk appck, Model model) {
		if (!Verdict.isAllow(appck.getCkappId())) {
			return "403";
		}
		model.addAttribute("appck", appck);
		return "modules/app/appckForm";
	}
	
	@RequiresPermissions("app:appck:edit")
	@RequestMapping(value = "save")
	public String save(APPCk appck, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (!Verdict.isAllow(appck.getCkappId())) {
			return "403";
		}
		if (!beanValidator(model, appck)){
			return form(appck, model);
		}
		
//		if (!"true".equals(checkName(appck.getOldName(), appck.getName()))){
//			addMessage(model, "保存APP'" + appck.getName() + "'失败，APP名称已存在");
//			return form(appck, model);
//		}
		if (StringUtils.isBlank(appck.getId())) {
    		if (!"true".equals(checkAppId(appck.getOldAppId(), appck.getCkappId(), appck.getChildId()))){
    			addMessage(model, "保存APP'" + appck.getCkappId() + "'失败，CHILDAPPID已存在");
    			return form(appck, model);
    		}
		}
		// 保存 
		appService.saveAPPCk(appck); 
		addMessage(redirectAttributes, "保存APP'" + appck.getName() + "'成功");
		return "redirect:" + adminPath + "/app/appck/list?repage";
	}
	
	
	@RequestMapping(value = "delete")
	@RequiresPermissions("app:appck:edit")
	public  String delete(APPCk aPPCk , RedirectAttributes redirectAttributes){
		if (!Verdict.isAllow(aPPCk.getCkappId())) {
			return "403";
		}
		appService.deleteAPPCk(aPPCk); 
		addMessage(redirectAttributes, "删除APP成功");
		return  "redirect:" + adminPath + "/app/appck/list?repage";
		
	}
	
	
	/**
	 * 验证app名是否有效
	 * @param oldName
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("app:appck:edit")
	@RequestMapping(value = "checkName")
	public String checkName(String oldName, String name) {
		if (name !=null && name.equals(oldName)) {
			return "true";
		} else if (name !=null && null == appService.getAPPCkByName(name) ) {
			return "true";
		}
		return "false";
	}
	
	
	
	/**
	 * 验证appid是否有效
	 * @param oldName
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("app:appck:edit")
	@RequestMapping(value = "checkAppId")
	public String checkAppId(String oldAppId, String appid,String childId) {
	    if (StringUtils.isNotBlank(appid) && StringUtils.isNotBlank(childId)) {
	        String name = appService.getCkAppNameByChild(appid, childId);
    		if (oldAppId!=null) {
    		    if ( oldAppId.equals(appid) && StringUtils.isBlank(name)) {
    		        return "true";
    		    }
    		} else {
    		    if (StringUtils.isBlank(name)) {
    		        return "true";
    		    }
    		}
	    }
		return "false";
	}
	/**
	 * 获取密钥
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("app:appck:edit")
	@RequestMapping(value = "createSecretKey")
	public String createSecretKey() {
		return IdGen.uuid();
	}
	
	@RequiresPermissions("app:appck:view")
	@RequestMapping(value = "addChild")
	public String addChild(APPCk appck, Model model,String ckappId) {
		if (!Verdict.isAllow(ckappId)) {
			return "403";
		}
		if(appck == null){
			appck = new APPCk();
		}
		appck.setCkappId(ckappId);
		model.addAttribute("appck", appck);
		return "modules/app/appckForm";
	}
	
	@RequestMapping(value = "getChildIdList")
	@ResponseBody
	public Object getChildIdList(String ckappId) {
		/*
		if (!Verdict.isAllow(ckappId)) {
			return "403";
		}
		List<Map<String,Object>> list = appService.getChildIdList(ckappId);
		for (Map<String,Object> map : list) {
		    if (map.containsKey("childName")) {
		       map.put("name", map.get("childName"));
		    }
		}
		return list;
		*/
		return getMineChildCkappList(ckappId);
	}

	@RequestMapping(value = "getMineChildCkappList")
	@ResponseBody
	public Object getMineChildCkappList(String ckappId) {
		if (!Verdict.isAllow(ckappId)) {
			return "403";
		}
		
		List<Map<String, Object>> rets = new ArrayList<Map<String, Object>>();
		List<APPCk> apps = AppCkUtils.getMineChildCkappList();
		for( APPCk app : apps ) {
			if( app.getCkappId().equals(ckappId) ) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ckappId", app.getCkappId());
				map.put("ckappName", app.getName());
				map.put("ckappChildId", app.getChildId());
				map.put("ckappChildName", app.getChildName());
				map.put("childId", app.getChildId());
				map.put("name", app.getChildName());
				rets.add(map);
			}
		}
		
		return rets;
	}
}
