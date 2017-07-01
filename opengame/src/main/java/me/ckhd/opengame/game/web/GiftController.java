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
import me.ckhd.opengame.game.entity.Gift;
import me.ckhd.opengame.game.entity.Goods;
import me.ckhd.opengame.game.service.GiftService;
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
@RequestMapping(value = "${adminPath}/game/gift")
public class GiftController extends BaseController{
	@Autowired
	private GiftService giftService;
	@Autowired
	private GoodsService goodsService;
	
	@RequestMapping(value = {"list", ""})
	public String list(Gift gift,HttpServletRequest request, HttpServletResponse response, Model model) {
		if (!Verdict.isAllow(gift.getCkAppId())) {
			return "403";
		}
		Page<Gift> page = giftService.findPage(new Page<Gift>(request, response), gift);
        model.addAttribute("page", page);
		return "modules/game/giftList";
		
	}
	
	@RequiresPermissions("game:gift:edit")
	@RequestMapping(value = "save")
	public String save(Gift gift, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (!Verdict.isAllow(gift.getCkAppId())) {
			return "403";
		}
		if (!beanValidator(model, gift)){
			return form(gift, model);
		}
		// 保存 
		
		giftService.save(gift); 
		addMessage(redirectAttributes, "保存成功");
		return "redirect:" + adminPath + "/game/gift/list?repage";
	}
	
	@RequestMapping(value = "delete")
	@RequiresPermissions("game:gift:edit")
	public  String delete(Gift gift , RedirectAttributes redirectAttributes){
		if (!Verdict.isAllow(gift.getCkAppId())) {
			return "403";
		}
		giftService.delete(gift); 
		addMessage(redirectAttributes, "删除信息成功");
		return  "redirect:" + adminPath + "/game/gift/list?repage";
		
	}
	
	@RequiresPermissions("game:gift:view")
	@RequestMapping(value = "form")
	public String form(Gift gift, Model model) {
		List<Map<String,Object>> data = null;
		List<Map<String,Object>> goods = new ArrayList<Map<String,Object>>();
		if(StringUtils.isNotBlank(gift.getId())){
			gift = giftService.get(gift);
			Gift one = new Gift();
			one.setParentNode(gift.getId());
			one.setCkAppId(gift.getCkAppId());
			data = giftService.getSub(one);
			Goods goodsO = new Goods();
			goodsO.setCkAppId(gift.getCkAppId());
			List<Goods> list = goodsService.findList(goodsO);
			for(Goods obj:list){
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("id", obj.getId());
				map.put("name", obj.getName());
				goods.add(map);
			}
		}
		model.addAttribute("gift", gift);
		model.addAttribute("data", data);
		model.addAttribute("goods",goods);
		return "modules/game/giftForm";
	}

	@RequestMapping(value = "getList")
	@ResponseBody
	public Object getList(Gift gift, Model model) {
		List<Gift> list = new ArrayList<Gift>();
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		if(StringUtils.isNotBlank(gift.getCkAppId())){
			list = this.giftService.findList(gift);
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
