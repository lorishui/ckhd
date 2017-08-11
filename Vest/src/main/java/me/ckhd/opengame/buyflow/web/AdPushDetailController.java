package me.ckhd.opengame.buyflow.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.buyflow.entity.AdPushDetail;
import me.ckhd.opengame.buyflow.service.AdPushDetailService;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "${adminPath}/app/adpushDetail")
public class AdPushDetailController extends BaseController {

	@Autowired
	private AdPushDetailService adPushDetailService;

	@ModelAttribute
	public AdPushDetail get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return adPushDetailService.get(id);
		} else {
			return new AdPushDetail();
		}
	}

	/*@RequiresPermissions("app:adPushDetail:view")
	@RequestMapping(value = { "list", "" })
	public String list(AdPushDetail adPushDetail, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Page<AdPushDetail> page = adPushDetailService.findPage(new Page<AdPushDetail>(request,
				response), adPushDetail);
		model.addAttribute("page", page);
		
		return "modules/app/adPushDetailList";

	}*/
	
	@RequiresPermissions("app:adPushDetail:view")
	@RequestMapping(value = { "list", "" })
	public String findListByAdPushId(@RequestParam("adPushId")String adPushId, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		List<AdPushDetail> list =  adPushDetailService.findListByAdPushId(adPushId);
		model.addAttribute("list", list);
		model.addAttribute("adPushId", adPushId);
		return "modules/buyflow/adPushDetailList";

	}

	@RequiresPermissions("app:adPushDetail:view")
	@RequestMapping(value = "form")
	public String form(@RequestParam("adPushId")String adPushId,@RequestParam(value="id",required = false)String adPushDetailId, Model model) {
		AdPushDetail promotion = new AdPushDetail();
		if(StringUtils.isNotEmpty(adPushDetailId)){
			promotion = adPushDetailService.get(adPushDetailId);
		}
		model.addAttribute("adPushDetail", promotion);
		model.addAttribute("adPushId", adPushId);
		/*List<Map<String, String>> allGames = adPushDetailService.getAllGames();
		
		model.addAttribute("allGames",allGames);*/
		return "modules/buyflow/adPushDetailForm";
	}

	@RequiresPermissions("app:adPushDetail:edit")
	@RequestMapping(value = "save")
	public String save(AdPushDetail adPushDetail, HttpServletRequest request, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, adPushDetail)) {
			return form(adPushDetail.getAdPush().getId(),adPushDetail.getId(), model);
		}

		// 保存
		adPushDetailService.save(adPushDetail);
		addMessage(redirectAttributes, "保存推广成功");
		return "redirect:" + adminPath + "/app/adpushDetail/list?adPushId="+adPushDetail.getAdPush().getId();
	}

	@RequestMapping(value = "delete")
	@RequiresPermissions("app:adPushDetail:edit")
	public String delete(@RequestParam("adPushId")String adPushId,AdPushDetail adPushDetail, RedirectAttributes redirectAttributes) {
		adPushDetailService.delete(adPushDetail);
		addMessage(redirectAttributes, "删除推广成功");
		return "redirect:" + adminPath + "/app/adpushDetail/list?adPushId="+adPushId;

	}
	
	

}
