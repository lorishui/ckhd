package me.ckhd.opengame.app.web;

import java.util.List;

import me.ckhd.opengame.app.entity.AppleUser;
import me.ckhd.opengame.app.service.AppleUserService;
import me.ckhd.opengame.app.utils.AppleMsgPushUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.user.utils.ErrorCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value = "${adminPath}/app/appleUser")
public class AppleUserController {
	Logger log = LoggerFactory.getLogger(AppUserController.class);
	@Autowired
	private AppleUserService appleUserService;
	
	@RequestMapping(value="add")
	@ResponseBody
	public String user(@RequestBody String data){
		JSONObject result = new JSONObject();
		JSONObject appleUser = JSONObject.parseObject(data);
		AppleUser apple = new AppleUser(appleUser.containsKey("appleuser")?appleUser.getJSONObject("appleuser"):null);
		if( apple != null && StringUtils.isNotBlank(apple.getDeviceToken()) ){
			int n = appleUserService.exist(apple);
			if( n == 0){
				try{
					appleUserService.save(apple);
					JSONObject jdata = new JSONObject();
					jdata.put("code", 2000);
					jdata.put("msg", "SUCCESS");
					result.put("result", jdata);
				}catch(Exception e){
					log.error("apple_user保存错了！", e);
					JSONObject jdata = new JSONObject();
					jdata.put("code", ErrorCode.INTERNAL_ERROR.getCode());
					jdata.put("msg", ErrorCode.INTERNAL_ERROR.getMsg());
					result.put("result", jdata);
				}
			}else{
				JSONObject jdata = new JSONObject();
				jdata.put("code", 3002);
				jdata.put("msg", "deviceToken existed!");
				result.put("result", jdata);
			}
		}else{
			JSONObject jdata = new JSONObject();
			jdata.put("code", 3001);
			jdata.put("msg", "参数为空！");
			result.put("result", jdata);
		}
		return result.toJSONString();
	}
	
	@RequestMapping(value="clear")
	@ResponseBody
	public String clear(@RequestBody String data){
		JSONObject result = new JSONObject();
		JSONObject appleUser = JSONObject.parseObject(data);
		AppleUser apple = new AppleUser(appleUser.containsKey("appleuser")?appleUser.getJSONObject("appleuser"):null);
		if( apple != null && StringUtils.isNotBlank(apple.getDeviceToken()) ){
			Integer m = appleUserService.getBadge(apple);
			if( m != null && m > 0){
				apple.setBadge(0);
				appleUserService.update(apple);
				JSONObject jdata = new JSONObject();
				jdata.put("code", 2000);
				jdata.put("msg", "SUCCESS");
				result.put("result", jdata);
			}else{
				JSONObject jdata = new JSONObject();
				jdata.put("code", 3003);
				jdata.put("msg", "deviceToken does not exist!");
				result.put("result", jdata);
			}
		}else{
			JSONObject jdata = new JSONObject();
			jdata.put("code", 3001);
			jdata.put("msg", "参数为空！");
			result.put("result", jdata);
		}
		return result.toJSONString();
	}
	
	@RequestMapping(value="send")
	@ResponseBody
	public String send(String ckAppId,String msg,String version){
		if(StringUtils.isNotBlank(msg) && StringUtils.isNotBlank(ckAppId) && StringUtils.isNotBlank(version)){
			AppleUser apple = new AppleUser();
			apple.setCkAppId(Integer.parseInt(ckAppId));
			List<AppleUser> list = this.appleUserService.findList(apple);
			String str = AppleMsgPushUtils.push(ckAppId, list, msg,version,this.appleUserService);
			if(str.equals("SUCCESS")){
				return "SUCCESS";
			}
		}
		return "FAILURE";
	}
	
	@RequestMapping(value="iosPush")
	public String iosPush(Model model){
		model.addAttribute("apple", new AppleUser());
		return "modules/app/iosPush";
	}
	
}
