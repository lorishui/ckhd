package me.ckhd.opengame.online.task;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.service.MmextendCfgService;
import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.online.service.OnlineService;
import me.ckhd.opengame.online.version.Version;
import me.ckhd.opengame.user.utils.RedisClientTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

/**
 * sdk的流程对接
 * 
 * @author liupei
 *
 */
@Controller
@RequestMapping(value="offlinepay")
public class NewAppApi  extends BaseController {
	
	private final static String className = "onlineVersion";
	
	@Autowired
	private OnlineService onlineService;
	
	@Autowired
	private MmextendCfgService mmextendCfgService;
	
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	
	/**
	 * 前端调用后台获取数据 登陆
	 * 
	 * @return
	 */
	@RequestMapping(value = "login/{engName}/",produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String login(@RequestBody String code,@PathVariable String engName,HttpServletRequest request) {
		logger.info(String.format("客户端上传的数据%s:[%s]", engName,code));
		JSONObject json = new JSONObject();
		String returnStr = null;
		String version = null;
		try {
			if(StringUtils.isNotBlank(code)){
				JSONObject codeJson = JSONObject.parseObject(code);
				if( codeJson != null && codeJson.size()>0 ){
					codeJson.put("a", engName);
					if( codeJson.containsKey("version") && StringUtils.isNotBlank(codeJson.getString("version")) ){
						version = codeJson.getString("version");
					}else{
						version = "1.1.0";
					}
					//处理逻辑
					Version ver = SpringContextHolder.getBean(className+version.replace(".", ""));
					returnStr = ver.login(codeJson, request);
				}else{
					json.put("resultCode", 2002);
					json.put("errMsg", "参数为空!");
				}
			}else{
				json.put("resultCode", 2003);
				json.put("errMsg", "数据为空");
			}
		} catch (Exception e) {
			logger.error("登陆发生错误：",e);
			json.put("resultCode", 2001);
			json.put("errMsg", "登陆失败");
		}
		if( returnStr == null ){
			returnStr = json.toJSONString();
		}
		logger.info(String.format("返回客户端的数据:[%s]", returnStr));
		return returnStr;
	}

	/**
	 * 前端调用后台获取数据 登陆
	 * 
	 * @return
	 */
	@RequestMapping(value = "pay/{engName}/",produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String pay(@RequestBody String code,@PathVariable String engName,HttpServletRequest request) {
		logger.info(String.format("客户端上传的数据:[%s]", code));
		JSONObject json = new JSONObject();
		String returnStr = null;
		String version = null;
		try {
			if(StringUtils.isNotBlank(code)){
				JSONObject codeJson = JSONObject.parseObject(code);
				if( codeJson != null && codeJson.size()>0 ){
					codeJson.put("a", engName);
					if( codeJson.containsKey("version") && StringUtils.isNotBlank(codeJson.getString("version")) ){
						version = codeJson.getString("version");
					}else{
						version = "1.1.0";
					}
					//处理逻辑
					Version ver = SpringContextHolder.getBean(className+version.replace(".", ""));
					returnStr = ver.pay(codeJson, request);
				}else{
					json.put("resultCode", 2002);
					json.put("errMsg", "参数为空!");
				}
			}else{
				json.put("resultCode", 2003);
				json.put("errMsg", "数据为空");
			}
		} catch (Exception e) {
			logger.error("支付发生错误：",e);
			json.put("resultCode", 2001);
			json.put("errMsg", "支付失败");
		}
		if( returnStr == null ){
			returnStr = json.toJSONString();
		}
		logger.info(String.format("返回客户端的数据:[%s]", returnStr));
		return returnStr;
	}
	
	/**
	 * 前端调用后台获取数据 登陆
	 * 
	 * @return
	 */
	@RequestMapping(value = "callback/{engName}/{version}/")
	@ResponseBody
	public String callback(@RequestBody String code,@PathVariable String engName,
			@PathVariable String version,HttpServletRequest request,HttpServletResponse response) {
		logger.info(String.format("渠道回调的数据%s:[%s]",engName, code));
		JSONObject json = new JSONObject();
		String returnStr = null;
		try {
			String patternStr = "^\\d.\\d.\\d$";
			boolean is = Pattern.matches(patternStr, version);
			if( !is ){
				json.put("resultCode", 4003);
				json.put("errMsg", "地址错误");
			}else{
				Version ver = SpringContextHolder.getBean(className+version.replace(".", ""));
				returnStr = ver.callback(code, engName, request , response); 
			}
		} catch (Exception e) {
			logger.error("支付发生错误：",e);
			if( json.size() == 0 ){
				json.put("resultCode", 4002);
				json.put("errMsg", "内部错误");
			}
		}
		if( returnStr == null ){
			returnStr = json.toJSONString();
		}
		logger.info(String.format("返回渠道的数据%s:[%s]",engName, returnStr));
		return returnStr;
	}

}