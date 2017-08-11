package me.ckhd.opengame.app.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import me.ckhd.opengame.app.entity.AppUser;
import me.ckhd.opengame.app.service.AppUserService;
import me.ckhd.opengame.common.web.BaseController;


@Controller
@RequestMapping(value = "${adminPath}/app/appuser")
public class AppUserController extends  BaseController{
	
	@Autowired
	private AppUserService appUserService;
	
	
	
	
	
	
	
	
	
	
	@ResponseBody
	@RequestMapping(value = "start", method = {RequestMethod.POST }) 
	public  void  start4AppUserInvoke(@RequestBody AppUser  appUser){
		appUserService.save(appUser);
	}
	
	

}
