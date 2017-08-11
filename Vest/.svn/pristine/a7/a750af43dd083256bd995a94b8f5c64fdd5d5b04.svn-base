package me.ckhd.opengame.buyflow.web;

import java.util.ArrayList;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import me.ckhd.opengame.buyflow.entity.AdPush;
import me.ckhd.opengame.buyflow.service.AdPushService;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.sys.utils.UserUtils;

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

		//20170731，添加权限限制
		Set<String> mediaPermission = UserUtils.getMediaPermission();
		if( !mediaPermission.isEmpty() ) {
			adPush.setPermissionMediaId(new ArrayList<String>(mediaPermission));
		}
		Set<String> gamePermission = UserUtils.getGamePermission();
		if( !gamePermission.isEmpty() ) {
			adPush.setPermissionCkAppId(StringUtils.join(gamePermission, ","));
		}
		
		Page<AdPush> page = adPushService.findPage(new Page<AdPush>(request,
				response), adPush);
		model.addAttribute("page", page);
		return "modules/buyflow/adPushList";

	}

	@RequiresPermissions("app:adPush:view")
	@RequestMapping(value = "form")
	public String form(AdPush adPush, Model model) {
		AdPush promotion = new AdPush();
		if(StringUtils.isNotEmpty(adPush.getId())){
			promotion = adPushService.get(adPush.getId());
		}
		model.addAttribute("adPush", promotion);
		return "modules/buyflow/adPushForm";
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
