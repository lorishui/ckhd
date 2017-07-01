package me.ckhd.opengame.game.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.game.entity.GiftDesc;
import me.ckhd.opengame.game.service.GiftCodeService;
import me.ckhd.opengame.game.service.GiftDescService;
import me.ckhd.opengame.game.service.GiftService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "${adminPath}/game/giftDesc")
public class GiftDescController extends BaseController{
	@Autowired
	private GiftDescService giftDescService;
	@Autowired
	private GiftService giftService;
	@Autowired
	private GiftCodeService giftCodeService;
	
	@ModelAttribute
	public  GiftDesc get(@RequestParam(required=false) String id){
		if (StringUtils.isNotBlank(id)) {
			return  giftDescService.get(id);
		}else{
			return  new GiftDesc();
		}
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(GiftDesc giftDesc,HttpServletRequest request, HttpServletResponse response, Model model) {
		if (!Verdict.isAllow(giftDesc.getCkAppId())) {
			return "403";
		}
		Page<GiftDesc> page = giftDescService.findPage(new Page<GiftDesc>(request, response), giftDesc);
        model.addAttribute("page", page);
		return "modules/game/giftDescList";
		
	}
	
	@RequiresPermissions("game:giftDesc:edit")
	@RequestMapping(value = "save")
	public String save(GiftDesc giftDesc, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (!Verdict.isAllow(giftDesc.getCkAppId())) {
			return "403";
		}
		if (!beanValidator(model, giftDesc)){
			return form(giftDesc, model);
		}
		giftDesc.setGiftDesc(giftService.getDesc(giftDesc.getGiftId(),giftDesc.getCkAppId()));
		// 保存 
		giftDescService.save(giftDesc); 
		addMessage(redirectAttributes, "保存成功");
		return "redirect:" + adminPath + "/game/giftDesc/list?repage";
	}
	
	@RequestMapping(value = "delete")
	@RequiresPermissions("game:giftDesc:edit")
	public  String delete(GiftDesc giftDesc , RedirectAttributes redirectAttributes){
		if (!Verdict.isAllow(giftDesc.getCkAppId())) {
			return "403";
		}
		giftDescService.delete(giftDesc); 
		addMessage(redirectAttributes, "删除运营商app信息成功");
		return  "redirect:" + adminPath + "/game/giftDesc/list?repage";
		
	}
	
	@RequiresPermissions("game:giftDesc:view")
	@RequestMapping(value = "form")
	public String form(GiftDesc giftDesc, Model model) {
		model.addAttribute("giftDesc", giftDesc);
		return "modules/game/giftDescForm";
	}
	
	@RequiresPermissions("game:giftDesc:edit")
	@RequestMapping(value = "generate")
	public String generate(GiftDesc giftDesc, Model model) {
		if(StringUtils.isNotBlank(giftDesc.getId())){
			giftCodeService.batchInsert(giftDesc);
		}
		return "redirect:" + adminPath + "/game/giftDesc/list?repage";
	}
	
}
