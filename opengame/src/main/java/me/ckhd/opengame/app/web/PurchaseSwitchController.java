package me.ckhd.opengame.app.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PurchaseSwitch;
import me.ckhd.opengame.common.web.BaseController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "${adminPath}/app/purchaseSwitch")
public class PurchaseSwitchController extends BaseController {

	@RequestMapping(value = "list")
	public String list(PurchaseSwitch PurchaseSwitch, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		
		// 新增多个值
		
		
		// 列表页面
		
		
		return "";
	}
	
	@RequestMapping(value = "add")
	public String add(PurchaseSwitch PurchaseSwitch, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		
		// 默认生成节假日，默认选项
		
		
		return "";
	}
	
	@RequestMapping(value = "cfg")
	public String cfg(PurchaseSwitch PurchaseSwitch, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		// 配置
		
		return "";
	}
}
