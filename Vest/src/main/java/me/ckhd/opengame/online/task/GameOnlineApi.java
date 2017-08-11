package me.ckhd.opengame.online.task;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.online.service.OnlineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

/**
 * 与游戏服务器对接
 * @author leo
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/online/game")
public class GameOnlineApi extends BaseController {

	@Autowired
	private OnlineService onlineService;
	
	/**
	 * 前端调用后台获取数据
	 * @return
	 */
	@RequestMapping(value = "loginValidate")
	@ResponseBody
	public String loginValidate(@RequestBody String validateCode){
		//1.获取到游戏发送的数据
		logger.info(String.format("游戏上传的消息:[%s]", validateCode));
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try {
			if(StringUtils.isBlank(validateCode)){
				result.put("resultCode", -1);
				result.put("errMsg", "传递的数据为空!");
				return JSONObject.toJSONString(result);
			}
			//2.根据CP传输的数据拼装user对象
			Map<String, Object> map = MyJsonUtils.jsonStr2Map(validateCode);
			String uid=map.get("uid").toString();
//			String token = map.get("token").toString();
//			String channelId = map.get("channelId").toString();
//			String ckappid = map.get("gameId").toString();
			
					
//			OnlineUser validateUser =new OnlineUser(uid,token,channelId,ckappid);
//			//3.在数据库中查找是否有相关的数据
//			OnlineUser user = onlineService.getValidateUser(validateUser);
//			if(user==null){
//				result.put("resultCode", -1);
//				result.put("errMsg", "用户不存在!");
//				return JSONObject.toJSONString(result);
//			}
//			
//			if(!user.getToken().equals(token)){
//				result.put("resultCode", -1);
//				result.put("errMsg", "验证失败,此用户为非法用户,请重新登录!");
//				return JSONObject.toJSONString(result);
//			}
//			
//			Date date = user.getUpdateDate();
//			if(DateUtils.pastMinutes(date)>10){
//				result.put("resultCode", -1);
//				result.put("errMsg", "用户信息已过期,请重新登录!");
//				return JSONObject.toJSONString(result);
//			}
			//4.返回验证成功与否
			result.put("resultCode",1);
			result.put("errMsg", "");
			resultMap.put("uid", uid);
			result.put("result", resultMap);
			return JSONObject.toJSONString(result);
		} catch (Exception e) {
			result.put("resultCode", -1);
			result.put("errMsg", "验证失败!");
			return JSONObject.toJSONString(result);
		}
	}
}
