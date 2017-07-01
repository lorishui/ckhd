package me.ckhd.opengame.app.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.Province;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 省份Controller
 * @author wesley
 * @version 2015-06-25
 */
@Controller
@RequestMapping(value = "${adminPath}/app/province")
public class ProvinceController extends BaseController{
	
	
	@Autowired
	private AppService appService;
	
	@ModelAttribute
	public  Province get(@RequestParam(required=false) String id){
		if (StringUtils.isNotBlank(id)) {
			return  appService.getProvince(id);
		}else{
			return  new Province();
		}
	}
	
	
	@RequiresPermissions("app:province:view")
	@RequestMapping(value = {"list", ""})
	public String list(Province province,HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Page<Province> page = appService.findProvince(new Page<Province>(request, response), province);
        model.addAttribute("page", page);
		return "modules/app/provinceList";
		
	}
	
	@RequiresPermissions("app:province:view")
	@RequestMapping(value = "form")
	public String form(Province province, Model model) {
		model.addAttribute("province", province);
		return "modules/app/provinceForm";
	}
	
	@RequiresPermissions("app:province:edit")
	@RequestMapping(value = "save")
	public String save(Province province, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, province)){
			return form(province, model);
		}
		if (!"true".equals(checkName(province.getOldName(), province.getName()))){
			addMessage(model, "保存省份'" + province.getName() + "'失败，省份名称已存在");
			return form(province, model);
		}
		// 保存 
		appService.saveProvince(province); 
		addMessage(redirectAttributes, "保存省份'" + province.getName() + "'成功");
		return "redirect:" + adminPath + "/app/province/list?repage";
	}
	
	
	@RequestMapping(value = "delete")
	@RequiresPermissions("app:province:edit")
	public  String delete(Province province , RedirectAttributes redirectAttributes){
		appService.deleteProvince(province);
		addMessage(redirectAttributes, "删除省份成功");
		return  "redirect:" + adminPath + "/app/province/list?repage";
		
	}
	
	
	

	/**
	 * 验证省份名是否有效
	 * @param oldName
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("app:province:edit")
	@RequestMapping(value = "checkName")
	public String checkName(String oldName, String name) {
		if (name !=null && name.equals(oldName)) {
			return "true";
		} else if (name !=null && null == appService.getProvinceByName(name) ) {
			return "true";
		}
		return "false";
	}
	
	
}
