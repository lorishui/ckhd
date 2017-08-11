package me.ckhd.opengame.app.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayCodeConfig;
import me.ckhd.opengame.app.service.AppService;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping(value = "${adminPath}/app/payCodeConfig")
public class PayCodeConfigController extends BaseController {
	
	@Autowired
	private AppService appService;
	
	/**
	 * 配置列表界面
	 */
	@RequiresPermissions("app:payCodeConfig:view")
	@RequestMapping(value = "list")
	public String list(PayCodeConfig payCodeConfig,HttpServletRequest request, HttpServletResponse response, Model model){
		return "modules/app/payCodeConfigList";
	}
	
	/**
	 * 获取配置列表界面
	 */
	@RequiresPermissions("app:payCodeConfig:view")
	@RequestMapping(value = "search")
	public String search(PayCodeConfig payCodeConfig,HttpServletRequest request, HttpServletResponse response, Model model){
		Page<PayCodeConfig> page = appService.findPage(new Page<PayCodeConfig>(request, response), payCodeConfig);
		model.addAttribute("page", page);
        model.addAttribute("payCodeConfig",payCodeConfig);
		return "modules/app/payCodeConfigList";
	}
	
	/**
	 * 配置列表界面
	 */
	@RequiresPermissions("app:payCodeConfig:view")
	@RequestMapping(value = "back")
	public String back(PayCodeConfig payCodeConfig,HttpServletRequest request, HttpServletResponse response, Model model){
		Page<PayCodeConfig> page = appService.findPage(new Page<PayCodeConfig>(request, response), new PayCodeConfig());
        model.addAttribute("page", page);
        return search(new PayCodeConfig(payCodeConfig.getCkAppId(),payCodeConfig.getAppid(),payCodeConfig.getChannelId(),payCodeConfig.getPaytype()), request, response, model);
	}
	
	@RequiresPermissions("app:payCodeConfig:edit")
	@RequestMapping(value = "save")
	public String save(PayCodeConfig payCodeConfig, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
		if (!Verdict.isAllow(payCodeConfig.getCkAppId())) {
			return "403";
		}
		if (!beanValidator(model, payCodeConfig)){
	        return search(new PayCodeConfig(payCodeConfig.getCkAppId()), request, response, model);
		}

		if (StringUtils.isBlank(payCodeConfig.getId())) {
			int count = appService.checkPayCodeOnly(payCodeConfig);
			if(count>0){
				request.setAttribute("message","保存游戏'" + payCodeConfig.getCkAppId() + "'计费点失败,此游戏配置已存在!");
		        return search(new PayCodeConfig(payCodeConfig.getCkAppId()), request, response, model);
			}
		}
		// 保存 
		appService.savePayCodeConfig(payCodeConfig);
		request.setAttribute("message", "保存游戏'" + payCodeConfig.getCkAppId() + "'计费点成功");
        return search(new PayCodeConfig(payCodeConfig.getCkAppId()), request, response, model);
	}
	
	
	@RequiresPermissions("app:payCodeConfig:edit")
	@RequestMapping(value = "delete")
	public  String delete(PayCodeConfig payCodeConfig, RedirectAttributes redirectAttributes, HttpServletResponse response, Model model,HttpServletRequest request){
		String ckappid = payCodeConfig.getCkAppId();
		if (!Verdict.isAllow(payCodeConfig.getCkAppId())) {
			return "403";
		}
		appService.delPayCodeConfig(payCodeConfig); 
		request.setAttribute("message","删除支付计费点成功");
        return search(new PayCodeConfig(ckappid), request, response, model);
	}
	
	@RequiresPermissions("app:payCodeConfig:view")
	@RequestMapping(value = "updateForm")
	public  String updateForm(PayCodeConfig payCodeConfig,HttpServletRequest request, HttpServletResponse response, Model model){
		payCodeConfig = appService.getPayCodeConfig(payCodeConfig);
        return search(payCodeConfig, request, response, model);
	}
	
}
