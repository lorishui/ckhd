package me.ckhd.opengame.online.handle;

import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.Encodes;
import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.handle.common.tencent.OtherRequest;
import me.ckhd.opengame.online.service.TencentCallbackDataService;
import me.ckhd.opengame.util.HttpClientUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("tencent")
@Scope("prototype")
public class tencentHandle extends BaseHandle{
	public static String qqTestVerifyUrl = "http://ysdktest.qq.com/auth/qq_check_token?timestamp=%s&appid=%s&sig=%s&openid=%s&openkey=%s";//测试接口地址
	public static String wxTestVerifyUrl = "http://ysdktest.qq.com/auth/wx_check_token?timestamp=%s&appid=%s&sig=%s&openid=%s&openkey=%s";//测试接口地址
	public static String qqVerifyUrl = "http://ysdk.qq.com/auth/qq_check_token?timestamp=%s&appid=%s&sig=%s&openid=%s&openkey=%s";//测试接口地址
	public static String wxVerifyUrl = "http://ysdk.qq.com/auth/wx_check_token?timestamp=%s&appid=%s&sig=%s&openid=%s&openkey=%s";//测试接口地址
	
	public TencentCallbackDataService tencentCallbackDataService = null;
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		try{
			JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
			String qqUrl = qqTestVerifyUrl;
			String weixinUrl = wxTestVerifyUrl;
			if( verifyInfo != null && (!verifyInfo.containsKey("environment") || !"sandbox".equalsIgnoreCase(verifyInfo.getString("environment"))) ){
				qqUrl = qqVerifyUrl;
				weixinUrl = wxVerifyUrl;
			}
			String appid = payInfo.getAppid();
			String appKey = payInfo.getAppkey();
			if( verifyInfo.containsKey("tencentLoginType") && 2 == verifyInfo.getInteger("tencentLoginType") ){
				Map<String,Object> exinfo = payInfo.getExInfoMap();
				appid = exinfo.get("appId") != null?exinfo.get("appId").toString() : "";
				appKey = exinfo.get("appKey").toString() != null ? exinfo.get("appKey").toString() : "";
			}
			String openid = verifyInfo.getString("openId");
			String openkey = verifyInfo.getString("accessToken");
			String userip = verifyInfo.getString("ip");
			
			onlineUser.setSid(openid);
			//数据格式  json
			JSONObject jsonData = new JSONObject();
			jsonData.put("appid", appid);
			jsonData.put("openid", openid);
			jsonData.put("accessToken", openkey);
			jsonData.put("openkey", openkey);
			jsonData.put("userip", userip);
			String time = System.currentTimeMillis()+"";
			String sig = Encodes.string2MD5(appKey+time);
			String verifyUrl = String.format(qqUrl,time,appid,sig,openid,URLEncoder.encode(openkey,"utf-8"));
			if( verifyInfo.containsKey("tencentLoginType") && 2 == verifyInfo.getInteger("tencentLoginType")){
				verifyUrl = String.format(weixinUrl, time,appid,sig,openid,URLEncoder.encode(openkey,"utf-8"));
			}
			String respStr = HttpClientUtils.get(verifyUrl, null, 2000, 2000);
			//响应数据格式 
			if( StringUtils.isBlank(respStr) ){
	//			errMsg="未获取到相关用户信息";
				result.put("errMsg", "未获取到相关用户信息");
			}else{
				JSONObject resultData = JSONObject.parseObject(respStr);
				if( resultData.containsKey("ret") && 0 == resultData.getInteger("ret") ){
					returnLoginSuccess(result, onlineUser);
				}else{
					result.put("errMsg", "腾讯返回的code非零");
					result.put("resultCode", -100);
				}
			}
		}catch(Throwable e){
			log.error("腾讯登录发生错误！", e);
			result.put("errMsg", "腾讯登录发生错误！");
		}
		return result.toJSONString();
	}

	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		if( StringUtils.isNotBlank(code) ){
			respData = JSONObject.parseObject(code);
		}
		if( tencentCallbackDataService == null ){
			tencentCallbackDataService = SpringContextHolder.getBean(TencentCallbackDataService.class);
		}
		/*if( respData != null && !respData.containsKey("isResend")){
			TencentCallbackData ten = new TencentCallbackData();
			ten.setData(code);
			ten.setOrderId(respData.getString("orderId"));
			ten.setPrice(respData.getInteger(""));
			ten.setStatus(0);
			ten.setIsNewRecord(false);
			tencentCallbackDataService.save(ten);
		}*/
		if(respData.size() > 0){
			onlinePay.setOrderId(respData.getString("orderId"));
			onlinePay.setActualAmount(respData.containsKey("prices")?respData.getString("prices"):"0");
			onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		OtherRequest other = new OtherRequest(onlinePay, respData);
		other.call();
		result.put("code", 2000);
		return getReturnSuccess();
	}

	@Override
	public String getReturnSuccess() {
		return "{\"resultCode\":0}";
	}

	@Override
	public String getReturnFailure() {
		return "{\"resultCode\":-1}";
	}

}
