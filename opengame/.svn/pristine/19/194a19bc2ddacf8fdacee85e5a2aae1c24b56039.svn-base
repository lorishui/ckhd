package me.ckhd.opengame.game.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.game.entity.Goods;
import me.ckhd.opengame.game.service.GoodsService;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "${adminPath}/game/goods")
public class GoodsController extends BaseController{
	@Autowired
	private GoodsService goodsService;
	
	@RequestMapping(value = {"list", ""})
	public String list(Goods goods,HttpServletRequest request, HttpServletResponse response, Model model) {
		if (!Verdict.isAllow(goods.getCkAppId())) {
			return "403";
		}
		Page<Goods> page = goodsService.findPage(new Page<Goods>(request, response), goods);
        model.addAttribute("page", page);
		return "modules/game/goodsList";
		
	}
	
	@RequiresPermissions("game:goods:edit")
	@RequestMapping(value = "save")
	public String save(Goods goods, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (!Verdict.isAllow(goods.getCkAppId())) {
			return "403";
		}
		if (!beanValidator(model, goods)){
			return form(goods, model);
		}
		// 保存 
		goodsService.save(goods); 
		addMessage(redirectAttributes, "保存成功");
		return "redirect:" + adminPath + "/game/goods/list?repage";
	}
	
	@RequestMapping(value = "delete")
	@RequiresPermissions("game:goods:edit")
	public  String delete(Goods goods , RedirectAttributes redirectAttributes){
		if (!Verdict.isAllow(goods.getCkAppId())) {
			return "403";
		}
		goodsService.delete(goods); 
		addMessage(redirectAttributes, "删除信息成功");
		return  "redirect:" + adminPath + "/game/goods/list?repage";
		
	}
	
	@RequiresPermissions("game:goods:view")
	@RequestMapping(value = "form")
	public String form(Goods goods, Model model) {
		if(StringUtils.isNotBlank(goods.getId())){
			goods = goodsService.get(goods);
		}
		model.addAttribute("goods", goods);
		return "modules/game/goodsForm";
	}
	
	@RequestMapping(value = "use")
	@RequiresPermissions("game:goods:edit")
	public  String use(Goods goods , RedirectAttributes redirectAttributes){
		if (!Verdict.isAllow(goods.getCkAppId())) {
			return "403";
		}
		goodsService.use(goods); 
		addMessage(redirectAttributes, "启用成功");
		return  "redirect:" + adminPath + "/game/goods/list?repage";
		
	}
	
	@RequestMapping(value = "getList")
	@ResponseBody
	public Object getList(Goods goods, Model model) {
		List<Goods> list = new ArrayList<Goods>();
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		if(StringUtils.isNotBlank(goods.getCkAppId())){
			list = this.goodsService.findList(goods);
			if(list != null){
				for(int i=0;i<list.size();i++){
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("id", list.get(i).getId());
					map.put("name", list.get(i).getName());
					data.add(map);
				}
			}
		}
		return data;
	}
}
