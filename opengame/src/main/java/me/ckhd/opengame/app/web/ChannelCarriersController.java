package me.ckhd.opengame.app.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.ChannelCarriers;
import me.ckhd.opengame.app.service.ChannelCarriersService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * channelCarriersController
 * @author wesley
 * @version 2015-07-01
 */
@Controller
@RequestMapping(value = "${adminPath}/app/channelcarriers")
public class ChannelCarriersController extends BaseController{
	
	
	@Autowired
	private ChannelCarriersService channelCarriersService;
	
	@ModelAttribute
	public  ChannelCarriers get(@RequestParam(required=false) String id){
		if (StringUtils.isNotBlank(id)) {
			return  channelCarriersService.get(id);
		}else{
			return  new ChannelCarriers();
		}
	}
	
	
	@RequiresPermissions("app:channelCarriers:view")
	@RequestMapping(value = {"list", ""})
	public String list(ChannelCarriers channelCarriers,HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Page<ChannelCarriers> page = channelCarriersService.findPage(new Page<ChannelCarriers>(request, response), channelCarriers);
        model.addAttribute("page", page);
		return "modules/app/channelcarriersList";
		
	}
	
	@RequiresPermissions("app:channelcarriers:view")
	@RequestMapping(value = "form")
	public String form(ChannelCarriers channelCarriers, Model model) {
		model.addAttribute("channelCarriers", channelCarriers);
		return "modules/app/channelcarriersForm";
	}
	
	@RequiresPermissions("app:channelcarriers:edit")
	@RequestMapping(value = "save")
	public String save(ChannelCarriers channelCarriers, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, channelCarriers)){
			return form(channelCarriers, model);
		}
//		if(channelCarriers.getIsNewRecord()){
//			if (!"true".equals(CheckCarriersChannelId(
//					channelCarriers.getChannelId(),
//					channelCarriers.getCarriersType()
//					))
//				)
//			{
//				addMessage(model, "保存运营商渠道信息'" + channelCarriers.getName() + "'失败，对应渠道号已存在");
//				return form(channelCarriers, model);
//			}
//		} 
		// 保存 
		channelCarriersService.save(channelCarriers); 
		addMessage(redirectAttributes, "保存运营商渠道号'" + channelCarriers.getCarriersChannelId() + "'成功");
		return "redirect:" + adminPath + "/app/channelcarriers/list?repage";
	}
	
	
	@RequestMapping(value = "delete")
	@RequiresPermissions("app:channelcarriers:edit")
	public  String delete(ChannelCarriers channelCarriers , RedirectAttributes redirectAttributes){
		channelCarriersService.delete(channelCarriers); 
		addMessage(redirectAttributes, "删除运营商渠道信息成功");
		return  "redirect:" + adminPath + "/app/channelcarriers/list?repage";
		
	}
	
	
	

	/**
	 * 验证运营商渠道是否有效
	 * @param channelId
	 * @param carriersType
	 * @param oldCarriersChannelId
	 * @param carriersChannelId
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("app:channelCarriers:edit")
	@RequestMapping(value = "checkCarriersChannelId")
	public String CheckCarriersChannelId(String channelId,String carriersType) {
		if (null == channelCarriersService.getByChannelIdAndCarriersType(channelId,carriersType) ) {
			return "true";
		}
		return "false";
	}
	
	@RequestMapping(value = "getList")
	@ResponseBody
	public Object getList(String channelId,String carriersType) {
		ChannelCarriers channel = new ChannelCarriers();
		channel.setChannelId(channelId);
		channel.setCarriersType(carriersType);
		List<ChannelCarriers> list = this.channelCarriersService.findList(channel);
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		for(ChannelCarriers cc:list){
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("id", cc.getCarriersChannelId());
			m.put("name", cc.getCarriersName());
			listMap.add(m);
		}
		return listMap;
	}
	
}