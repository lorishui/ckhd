package me.ckhd.opengame.app.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.GameVersion;
import me.ckhd.opengame.app.service.AppService;
import me.ckhd.opengame.common.persistence.Page;
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

/**
 * 游戏版本Controller
 * @author wesley
 * @version 2015-06-25
 */
@Controller
@RequestMapping(value = "${adminPath}/app/gameversion")
public class GameVersionController extends BaseController{
	
	
	@Autowired
	private AppService appService;
	
	@ModelAttribute
	public  GameVersion get(@RequestParam(required=false) String id){
		if (StringUtils.isNotBlank(id)) {
			return  appService.getGameVersion(id);
		}else{
			return  new GameVersion();
		}
	}
	
	
	@RequiresPermissions("app:gameversion:view")
	@RequestMapping(value = {"list", ""})
	public String list(GameVersion gameversion,HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Page<GameVersion> page = appService.findGameVersion(new Page<GameVersion>(request, response), gameversion);
        model.addAttribute("page", page);
		return "modules/app/gameversionList";
		
	}
	
	@RequiresPermissions("app:gameversion:view")
	@RequestMapping(value = "form")
	public String form(GameVersion gameversion, Model model) {
		model.addAttribute("gameversion", gameversion);
		return "modules/app/gameversionForm";	
	}
	
	@RequiresPermissions("app:gameversion:edit")
	@RequestMapping(value = "save")
	public String save(GameVersion gameversion, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, gameversion)){
			return form(gameversion, model);
		}
		// 保存 
		appService.saveGameVersion(gameversion); 
		addMessage(redirectAttributes, "保存游戏版本'" + gameversion.getName() + "'成功");
		return "redirect:" + adminPath + "/app/gameversion/list?repage";
	}
	
	
	@RequestMapping(value = "delete")
	@RequiresPermissions("app:gameversion:edit")
	public  String delete(GameVersion gameversion , RedirectAttributes redirectAttributes){
		appService.deleteGameVersion(gameversion); 
		addMessage(redirectAttributes, "删除游戏版本成功");
		return  "redirect:" + adminPath + "/app/gameversion/list?repage";
		
	}
	
}
