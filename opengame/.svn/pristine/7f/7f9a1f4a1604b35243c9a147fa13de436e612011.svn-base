package me.ckhd.opengame.app.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.app.service.AppService;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
@Controller
@RequestMapping(value = "${adminPath}/app/payInfoConfig")
public class PayInfoConfigController  extends BaseController  {
	@Autowired
	private AppService appService;
	
	/**
	 * 配置列表界面
	 */
	@RequiresPermissions("app:payInfoConfig:view")
	@RequestMapping(value = "list")
	public String list(PayInfoConfig payInfoConfig,HttpServletRequest request, HttpServletResponse response, Model model){
		if (!Verdict.isAllow(payInfoConfig.getCkAppId())) {
			return "403";
		}
		Page<PayInfoConfig> page = appService.findPage(new Page<PayInfoConfig>(request, response), payInfoConfig);
        model.addAttribute("page", page);
		return "modules/app/payInfoConfigList";
	}
	
	/**
	 * 配置列表界面
	 */
	@RequiresPermissions("app:payInfoConfig:view")
	@RequestMapping(value = "back")
	public String back(PayInfoConfig payInfoConfig,HttpServletRequest request, HttpServletResponse response, Model model){
		if (!Verdict.isAllow(payInfoConfig.getCkAppId())) {
			return "403";
		}
		Page<PayInfoConfig> page = appService.findPage(new Page<PayInfoConfig>(request, response), new PayInfoConfig());
        model.addAttribute("page", page);
        model.addAttribute("payInfoConfig", new PayInfoConfig());
		return "modules/app/payInfoConfigList";
	}
	@RequiresPermissions("app:payInfoConfig:edit")
	@RequestMapping(value = "save")
	public String save(PayInfoConfig payInfoConfig, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
		if (!Verdict.isAllow(payInfoConfig.getCkAppId())) {
			return "403";
		}
		//处理页面传递回调地址特殊字符串被转义
		payInfoConfig.setNotifyUrl(request.getParameter("notifyUrl"));
		if (!beanValidator(model, payInfoConfig)){
			return list(payInfoConfig, request, response, model);
		}
		if (StringUtils.isBlank(payInfoConfig.getId())) {
			int count = appService.checkPayInfoOnly(payInfoConfig);
			if(count>0){
				addMessage(redirectAttributes, "保存游戏'" + payInfoConfig.getCkAppId() + "'失败,此游戏配置已存在!");
				return "redirect:" + adminPath + "/app/payInfoConfig/list?repage";
			}
		}
		
		// 保存 
		appService.savePayInfoConfig(payInfoConfig); 
		addMessage(redirectAttributes, "保存游戏版本'" + payInfoConfig.getCkAppId() + "'成功");
		return "redirect:" + adminPath + "/app/payInfoConfig/list?repage";
	}
	
	@RequiresPermissions("app:payInfoConfig:edit")
	@RequestMapping(value = "delete")
	public  String delete(PayInfoConfig payInfoConfig, RedirectAttributes redirectAttributes){
		if (!Verdict.isAllow(payInfoConfig.getCkAppId())) {
			return "403";
		}
		
		appService.delPayInfoConfig(payInfoConfig); 
		addMessage(redirectAttributes, "删除支付信息成功");
		return  "redirect:" + adminPath + "/app/payInfoConfig/list?repage";
	}
	
	@RequiresPermissions("app:payInfoConfig:view")
	@RequestMapping(value = "updateForm")
	public  String updateForm(PayInfoConfig payInfoConfig,HttpServletRequest request, HttpServletResponse response, Model model){
		payInfoConfig = appService.getPayInfoConfig(payInfoConfig);
		model.addAttribute("payInfoConfig", payInfoConfig);
		return  list(new PayInfoConfig(), request, response, model);
	}
	
}
