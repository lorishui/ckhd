package me.ckhd.opengame.online.task;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.Cfgparam;
import me.ckhd.opengame.app.service.CkPlatformPaySwitchCfgService;
import me.ckhd.opengame.buyflow.task.BuyFlowTaskUtils;
import me.ckhd.opengame.common.Result;
import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.online.entity.AppDeviceInfo;
import me.ckhd.opengame.online.entity.RoleInfoOnline;
import me.ckhd.opengame.online.service.AppDeviceInfoService;
import me.ckhd.opengame.online.service.OnlineService;
import me.ckhd.opengame.online.service.RoleService;
import me.ckhd.opengame.online.version.Version;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

/**
 * sdk的流程对接
 * 
 * @author liupei
 *
 */
@Controller
@RequestMapping(value="netpay")
public class NewAppApi  extends BaseController {
	
	static Map<String,String> payMap = new HashMap<String, String>();
	
	static{
		payMap.put("121", "weixin");
		payMap.put("122", "alipay");
//		payMap.put("123", "midas");
		payMap.put("124", "unionpay");
		payMap.put("125", "wft");
		payMap.put("126", "aibei");
		payMap.put("127", "easypay");
		payMap.put("141", "appstore");
	}
	
	private final static String className = "onlineVersion";
	
	@Autowired
	private OnlineService onlineService;	
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private AppDeviceInfoService appDeviceInfoService;
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
	 * 前端调用后台获取数据 支付
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
	 * 渠道 支付回调
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

	/**
	 * sdk客户端 支付回调 腾讯 苹果
	 * 
	 * @return
	 */
	@RequestMapping(value = "callbackbc/{engName}/{version}/")
	@ResponseBody
	public String callbackByClient(@RequestBody String code,@PathVariable String engName,
			@PathVariable String version,HttpServletRequest request,HttpServletResponse response) {
		logger.info(String.format("渠道回调的数据%s:[%s]",engName, code));
		JSONObject json = new JSONObject();
		String returnStr = null;
		try {
			String patternStr = "^\\d.\\d.\\d$";
			boolean is = Pattern.matches(patternStr, version);
			if( engName.equals("tencent") ){
				if( !is ){
					json.put("resultCode", 4003);
					json.put("errMsg", "地址错误");
				}else{
					Version ver = SpringContextHolder.getBean(className+version.replace(".", ""));
					returnStr = ver.callbackbc(code, engName, request , response); 
				}
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
	
	/**
	 * 前端调用后台获取数据 支付
	 * 
	 * @return
	 */
	@RequestMapping(value = "payByCfgparam/{channelId}/",produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String payByCfgparam(@RequestBody String code,@PathVariable String channelId,HttpServletRequest request) {
		logger.info(String.format("客户端上传的数据:[%s]", code));
		JSONObject json = new JSONObject();
		String returnStr = null;
		try {
			if(StringUtils.isNotBlank(code)){
				JSONObject codeJson = JSONObject.parseObject(code);
				Cfgparam cfg = new Cfgparam();
				cfg.setCkAppId(codeJson.getString("ckAppId"));
				cfg.setCkChannelId(codeJson.getString("ckChannelId"));
				cfg.setVersionName(codeJson.getString("versionName"));
				cfg.setChildCkAppId(codeJson.getString("subCkAppId"));
				CkPlatformPaySwitchCfgService cfgService = SpringContextHolder.getBean("CkPlatformPaySwitchCfgService");
				Map<String, Object> resultMap = cfgService.getCfg(cfg);
				if( resultMap == null || "-1".equals(resultMap.get("resultCode")) ){
					json.put("resultCode", 3000);
					json.put("errMsg", "未配置参数!");
				}else{
					if( resultMap.containsKey("resultCode") && "0".equals(resultMap.get("resultCode")) ){
						if( resultMap.containsKey("Platform") && resultMap.containsKey("CKPay")	){
//							if( !resultMap.get("CKPay") && "200".equals(codeJson.getString("ckChannelId")) ){
							if( !(Boolean)resultMap.get("CKPay") && "200".equals(codeJson.getString("ckChannelId")) ){
								returnStr = pay(code,"appstore", request);
								JSONObject rData = JSONObject.parseObject(returnStr);
								rData.put("payType", "141");
								rData.put("CKPay", resultMap.get("CKPay"));
								rData.put("Platform", resultMap.get("Platform"));
								returnStr = rData.toJSONString();
							}else{
								String platform = (String)resultMap.get("Platform");
								if( StringUtils.isNotBlank(platform) && platform.indexOf(",") == -1 ){
									String engName = payMap.get(platform);
									if( codeJson != null && codeJson.size()>0 ){
										codeJson.put("payType", platform);
										returnStr = pay(codeJson.toJSONString(),engName, request);
										JSONObject rData = JSONObject.parseObject(returnStr);
										rData.put("payType", platform);
										rData.put("CKPay", resultMap.get("CKPay"));
										rData.put("Platform", resultMap.get("Platform"));
										returnStr = rData.toJSONString();
									}else{
										json.put("resultCode", 2002);
										json.put("errMsg", "参数为空!");
									}
								}else{
									returnStr = JSONObject.toJSONString(resultMap);
								}
							}
						}else{
							returnStr = JSONObject.toJSONString(resultMap);
						}
					}else{
						returnStr = JSONObject.toJSONString(resultMap);
					}
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
	@RequestMapping(value = "role",produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String roleCollect(@RequestBody String code,HttpServletRequest request) {
		logger.info(String.format("客户端上传角色信息(role)的数:[%s]",code));
		JSONObject json = new JSONObject();
		String returnStr = null;
//		String version = null;
		try {
			if(StringUtils.isNotBlank(code)){
				JSONObject codeJson = JSONObject.parseObject(code);
				if( codeJson != null && codeJson.size()>0 ){
					if(codeJson.containsKey("role")){
						RoleInfoOnline role = new RoleInfoOnline();
						role.pareJSON(codeJson.getJSONObject("role"));
						role.preInsert();
						if( StringUtils.isNotBlank(role.getRoleId()) ){
							roleService.insert(role);
						}
						//把设备信息加入设备信息表中
						if( 0 == role.getType()){
							AppDeviceInfo app = setAppDeviceInf0(role,request);
							boolean isExist = appDeviceInfoService.isExist(app);
							if(!isExist){
								appDeviceInfoService.save(app);
							}
						}
						int m = roleService.insertEvent(role);
						if( m>0){
							json.put("a", 2000);
							json.put("b", "SUCCESS");
						}else{
							json.put("a", 2004);
							json.put("b", "save失败！请重新请求");
						}
						// 激活或者新手完成调用买量业务，放到设备新增之后，回写买量维度数据
						if( 0 == role.getType() || 10 == role.getType() ){
							BuyFlowTaskUtils.buyflowHandle(role);
						}
					}
				}else{
					json.put("a", 2002);
					json.put("b", "参数为空!");
				}
			}else{
				json.put("a", 2003);
				json.put("b", "数据为空");
			}
		} catch (Exception e) {
			logger.error("角色收集发生错误：",e);
			json.put("a", 2001);
			json.put("b", "角色收集失败");
		}
		if( returnStr == null ){
			JSONObject result = new JSONObject();
			result.put("result", json);
			returnStr = result.toJSONString();
		}
		logger.info(String.format("角色信息(role)返回客户端的数据:[%s]", returnStr));
		return returnStr;
	}

	private AppDeviceInfo setAppDeviceInf0(RoleInfoOnline role,HttpServletRequest request) {
		AppDeviceInfo app = new AppDeviceInfo();
		app.setCkAppId(role.getCkAppId());
		app.setChildCkAppId(role.getChildCkAppId());
		app.setCkChannelId(role.getCkChannelId());
		app.setChildChannelId(role.getChildChannelId());
		app.setDeviceId(role.getUuid());
		app.setMD5DeviceId(CoderUtils.md5(role.getUuid(), "utf-8"));
		app.setClientIp(StringUtils.getRemoteAddr(request));
		app.setCreateTime(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		return app;
	}
	
	/**
	 * 查询订单状态
	 * @param code
	 * @param engName
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "queryOrderState/{engName}/")
	@ResponseBody
	public String queryOrderState(@RequestBody String code,@PathVariable String engName,HttpServletRequest request) {
		
		logger.info("客户端上传的数据:{}", code);
		
		Result<Void> result = new Result<Void>();
		try {
			if(StringUtils.isNotBlank(code)){
				JSONObject codeJson = JSONObject.parseObject(code);
				if( codeJson != null && !codeJson.isEmpty() ){
					codeJson.put("a", engName);
					
					String version = codeJson.getString("version");
					if( StringUtils.isBlank(version) ) {
						version = "1.1.0";
					}
					
					//处理逻辑
					Version handler = SpringContextHolder.getBean(className + version.replace(".", ""));
					result = handler.queryOrderState(codeJson, request);
				}
				else{
					result.setCode(2002);
					result.setMessage("参数为空!");
				}
			}
			else{
				result.setCode(2003);
				result.setMessage("数据为空!");
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
			
			result.setCode(2001);
			result.setMessage("支付失败");
		}

		JSONObject json = new JSONObject();
		json.put("resultCode", result.getCode());
		json.put("errMsg", result.getMessage());
		String ret = json.toJSONString();
		
		logger.info("返回客户端的数据:{}", ret);
		
		return ret;
	}
	
	
	/**
	 * myCard支付  補储
	 * 
	 * @return
	 */
	@RequestMapping(value = "addorder/{engName}/{version}/")
	@ResponseBody
	public String addorder(@RequestBody String code,@PathVariable String engName,
			@PathVariable String version,HttpServletRequest request,HttpServletResponse response) {
		logger.info(String.format("mycard補储的数据%s:[%s]",engName, code));
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
				returnStr = ver.addorder(code, engName, request , response); 
			}
		} catch (Exception e) {
			logger.error("補储发生错误：",e);
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
	
	
	/**
	 * 提供给myCard公司  查询订单接口
	 * 
	 * @return
	 */
	@RequestMapping(value = "query/{engName}/{version}/")
	@ResponseBody
	public String query(@PathVariable String engName,@RequestParam Map<String, String> param,
			@PathVariable String version,HttpServletRequest request,HttpServletResponse response) {
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
				returnStr = ver.query(param,engName, request , response); 
			}
		} catch (Exception e) {
			logger.error("查询发生错误：",e);
			if( json.size() == 0 ){
				json.put("resultCode", 4002);
				json.put("errMsg", "内部错误");
			}
		}
		if( returnStr == null ){
			returnStr = json.toJSONString();
		}
		logger.info(String.format("返回mycard的订单详情%s:[%s]",engName, returnStr));
		return returnStr;
	}

//	@RequestMapping(value = "test")
//	@ResponseBody
//	public String test(HttpServletRequest request) {
//		return hailanPayTest(request);
//	}
//	private String hailanPayTest(HttpServletRequest request) {
//		JSONObject codeJson = new JSONObject();
//		codeJson.put("act", 1002);
//		codeJson.put("userId", "unknow");
//		codeJson.put("extension", "gameData");
//		codeJson.put("notifyUrl", "http://i16785s665.iok.la/netpay/callback/rightyoo/1.1.0");
//		codeJson.put("subCkChannelId", "1");
//		codeJson.put("androidId", "aaf6bf16b7f9616a");
//		codeJson.put("roleId", "1");
//		codeJson.put("ckAppId", "2000");
//		codeJson.put("imei", "862964037987934");
//		codeJson.put("zoneId", "1");
//		codeJson.put("osVersion", "Android22");
//		codeJson.put("gameOnline", "1");
//		codeJson.put("imsi", "460022800484146");
//		codeJson.put("ckChannelId", "103");
//		codeJson.put("payNotifyUrl", "www.cp.server");
//		codeJson.put("osVersionName", "5.1");
//		codeJson.put("subCkAppId", "1");
//		codeJson.put("payType", "131");
//		codeJson.put("price", "1");
//		codeJson.put("mmAppId", "300009127531");
//		codeJson.put("serverId", "1");
//		codeJson.put("phone_model", "OPPO A37m");
//		codeJson.put("productId", "1");
//		codeJson.put("simNo", "898600c81316f1033046");
//		codeJson.put("isTest", "0");
//		
//		String code = codeJson.toString();
//		String engName = "hailan";
//		
//		return pay(code, engName, request);
//	}
}
