package me.ckhd.opengame.app.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.CfgParamFeilds;
import me.ckhd.opengame.app.service.CfgParamFeildsService;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.web.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "${adminPath}/app/CfgParamFeilds")
public class CfgParamFeildsController  extends BaseController{
	
	@Autowired
	private CfgParamFeildsService cfgParamFeildsService;
	
	@RequestMapping(value = {"list", ""})
	public String list(CfgParamFeilds cfgParamFeilds,HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CfgParamFeilds> page = cfgParamFeildsService.findPage(new Page<CfgParamFeilds>(request, response), cfgParamFeilds);
        model.addAttribute("page", page);
		return "modules/app/cfgParamFeildsList";
	}
	
	@RequestMapping(value = "form")
	public String form(CfgParamFeilds cfgParamFeilds, Model model) {
		if(null != cfgParamFeilds && null != cfgParamFeilds.getId() ){
			cfgParamFeilds = this.cfgParamFeildsService.get(cfgParamFeilds);
		}
		model.addAttribute("cfgParamFeilds", cfgParamFeilds);
		return "modules/app/cfgParamFeildsForm";
	}
	
	@RequestMapping(value = "delete")
	public String delete(CfgParamFeilds cfgParamFeilds, Model model, RedirectAttributes redirectAttributes){
		if (!Verdict.isAllow(cfgParamFeilds.getId())) {
			return "403";
		}
		cfgParamFeildsService.delete(cfgParamFeilds); 
		addMessage(redirectAttributes, "删除运营商app信息成功");
		return  "redirect:" + adminPath + "/app/CfgParamFeilds/list";
	}
	
	@RequestMapping(value = "save")
	public String save(CfgParamFeilds cfgParamFeilds, Model model, RedirectAttributes redirectAttributes) {
		this.cfgParamFeildsService.save(cfgParamFeilds);
		model.addAttribute("message", "success");
		addMessage(redirectAttributes, "保存成功");
		return "redirect:" + adminPath + "/app/CfgParamFeilds/form";
	}
}
