package me.ckhd.opengame.adpush.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.adpush.entity.AdPush;
import me.ckhd.opengame.adpush.service.AdPushService;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "${adminPath}/app/adpush")
public class AdPushController extends BaseController {

	@Autowired
	private AdPushService adPushService;

	@ModelAttribute
	public AdPush get(@RequestParam(required = false)String id) {
		if (StringUtils.isNotBlank(id)) {
			return adPushService.get(id);
		} else {
			return new AdPush();
		}
	}

	@RequiresPermissions("app:adPush:view")
	@RequestMapping(value = { "list", "" })
	public String list(AdPush adPush, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Page<AdPush> page = adPushService.findPage(new Page<AdPush>(request,
				response), adPush);
		List<Map<String, String>> allGames = adPushService.getAllGames();
		List<Map<String, String>> allMedias = adPushService.getAllMedia();
		model.addAttribute("allGames",allGames);
		model.addAttribute("allMedias",allMedias);
		model.addAttribute("page", page);
		return "modules/app/adPushList";

	}

	@RequiresPermissions("app:adPush:view")
	@RequestMapping(value = "form")
	public String form(AdPush adPush, Model model) {
		AdPush promotion = new AdPush();
		if(StringUtils.isNotEmpty(adPush.getId())){
			promotion = adPushService.get(adPush.getId());
		}
		model.addAttribute("adPush", promotion);
		List<Map<String, String>> allGames = adPushService.getAllGames();
		model.addAttribute("allGames",allGames);
		List<Map<String, String>> allMedias = adPushService.getAllMedia();
		model.addAttribute("allMedias",allMedias);
		return "modules/app/adPushForm";
	}

	@RequiresPermissions("app:adPush:edit")
	@RequestMapping(value = "save")
	public String save(AdPush adPush, HttpServletRequest request, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, adPush)) {
			return form(adPush, model);
		}

		// 保存
		adPushService.save(adPush);
		addMessage(redirectAttributes, "保存推广成功");
		return "redirect:" + adminPath + "/app/adpush/list?repage";
	}

	@RequestMapping(value = "delete")
	@RequiresPermissions("app:adPush:edit")
	public String delete(AdPush adPush, RedirectAttributes redirectAttributes) {
		adPushService.delete(adPush);
		addMessage(redirectAttributes, "删除推广成功");
		return "redirect:" + adminPath + "/app/adpush/list?repage";

	}
	
	

}
