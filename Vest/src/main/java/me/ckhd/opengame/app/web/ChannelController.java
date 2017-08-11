package me.ckhd.opengame.app.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.Channel;
import me.ckhd.opengame.app.service.ChannelService;
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
 * ChannelController
 * @author wesley
 * @version 2015-07-01
 */
@Controller
@RequestMapping(value = "${adminPath}/app/channel")
public class ChannelController extends BaseController{
	
	
	@Autowired
	private ChannelService channelService;
	
	@ModelAttribute
	public  Channel get(@RequestParam(required=false) String id){
		if (StringUtils.isNotBlank(id)) {
			return  channelService.get(id);
		}else{
			return  new Channel();
		}
	}
	
	/**
	 * 分页查询渠道数据
	 * @param channel
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("app:channel:view")
	@RequestMapping(value = {"list", ""})
	public String list(Channel channel,HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Page<Channel> page = channelService.findPage(new Page<Channel>(request, response), channel);
        model.addAttribute("page", page);
		return "modules/app/channelList";
		
	}
	
	@RequiresPermissions("app:channel:view")
	@RequestMapping(value = "form")
	public String form(Channel channel, Model model) {
		model.addAttribute("channel", channel);
		return "modules/app/channelForm";
	}
	
	@RequiresPermissions("app:channel:edit")
	@RequestMapping(value = "save")
	public String save(Channel channel, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, channel)){
			return form(channel, model);
		}
		if (!"true".equals(checkName(channel.getOldName(), channel.getName()))){
			addMessage(model, "保存渠道'" + channel.getName() + "'失败，渠道名称已存在");
			return form(channel, model);
		}
		// 保存 
		channelService.saveChanne(channel); 
		addMessage(redirectAttributes, "保存渠道'" + channel.getName() + "'成功");
		return "redirect:" + adminPath + "/app/channel/list?repage";
	}
	
	@RequestMapping(value = "delete")
	@RequiresPermissions("app:channel:edit")
	public  String delete(Channel channel , RedirectAttributes redirectAttributes){
		channelService.delete(channel); 
		addMessage(redirectAttributes, "删除渠道成功");
		return  "redirect:" + adminPath + "/app/channel/list?repage";
		
	}
	
	/**
	 * 验证渠道名是否有效
	 * @param oldName
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("app:channel:edit")
	@RequestMapping(value = "checkName")
	public String checkName(String oldName, String name) {
		
		if (name !=null && name.equals(oldName)) {
			return "true";
		} else if (name !=null && null == channelService.getByName(name) ) {
			return "true";
		}
		return "false";
	}
	
	/**
	 * 获取有效的渠道
	 * @param key
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "getList")
	@ResponseBody
	public Object getList(String key) {
		Channel channel = new Channel();
		channel.setName(key);
		List<Map<String,String>> data = channelService.getList(channel);
		return data;
	}
	
}
