package me.ckhd.opengame.app.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.AppPayList;
import me.ckhd.opengame.app.service.AppPayListService;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.sys.entity.Dict;
import me.ckhd.opengame.sys.service.DictService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping(value = "${adminPath}/app/appPayList")
public class AppPayListController extends  BaseController{
	
	@Autowired
	private AppPayListService  appPayListService;
	@Autowired
	private DictService dictService;
	
	@RequestMapping(value = "form")
	public String form(AppPayList appPayList, Model model) {
		if (!Verdict.isAllow(appPayList.getCkAppId())) {
			return "403";
		}
		String[] arr = null;
		if(StringUtils.isNotBlank(appPayList.getId())){
			appPayList = this.appPayListService.get(appPayList.getId());
			model.addAttribute("appPayList", appPayList);
			if(appPayList != null && appPayList.getPayType() != null){
				arr = appPayList.getPayType().split(",");
			}
		}
		model.addAttribute("appPayList", appPayList);
		Dict dict = new Dict();
		dict.setType("paytype");
		dict.setDelFlag("1");
		List<Map<String,String>> data = dictService.getList(dict);
		model.addAttribute("paytype", data);
		model.addAttribute("payTypeArr", arr);
		return "modules/app/appPayListForm";
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(AppPayList appPayList,HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AppPayList> page = appPayListService.findPage(new Page<AppPayList>(request, response), appPayList);
        model.addAttribute("page", page);
		return "modules/app/appPayListList";	
	}
	
	@RequiresPermissions("app:appPayList:edit")
	@RequestMapping(value = "save")
	public String save(AppPayList appPayList, HttpServletRequest request, Model model, 
			RedirectAttributes redirectAttributes,String[] paytype) {
		if (!beanValidator(model, appPayList)){
			return form(appPayList, model);
		}
		StringBuffer sb = new StringBuffer();
		if(paytype != null){
			for(String str:paytype){
				sb.append(str+",");
			}
			sb.setLength(sb.length()-1);
			appPayList.setPayType(sb.toString());
		}
		if(appPayList.getIsUse() == null){
			appPayList.setIsUse("0");
		}
		// 保存 
		appPayListService.save(appPayList); 
		addMessage(redirectAttributes, "保存APP成功");
		return "redirect:" + adminPath + "/app/appPayList/list?repage";
	}
}
