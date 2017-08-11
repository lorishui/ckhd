package me.ckhd.opengame.online.handle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.handle.common.youku.HmacMD5;
import me.ckhd.opengame.util.HttpClientUtils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("youku")
@Scope("prototype")
public class youkuHandle extends BaseHandle{

	public static final String login_url = "http://sdk.api.gamex.mobile.youku.com/game/user/infomation";
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		String sessionid = codeJson.containsKey("sessionid")?codeJson.getString("sessionid"):"";
		String appkey = payInfo.getAppid();
		//appkey=xxxxxxxx&sessionid=xxxxxxxxx
		String sign = CoderUtils.makeSig("appkey="+appkey+"&sessionid="+sessionid, payInfo.getAppkey());
		String respStr = HttpClientUtils.post(login_url, "sessionid="+sessionid+"&appkey="+appkey+"&sign="+sign, 2000, 2000);
		if( StringUtils.isNotBlank(respStr) ){
			JSONObject json = JSONObject.parseObject(respStr);
			if(json.containsKey("status") && json.getString("status").equals("success")){
				onlineUser.setSid(json.getString("uid"));
				returnLoginSuccess(result, onlineUser);
			}else{
				result.put("errMsg", "用户验证失败！");
			}
		}else{
			result.put("errMsg", "数据为空或者请求失败！");
		}
		return result.toJSONString();
	}

	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		for(Object key:request.getParameterMap().keySet()){
			respData.put(key+"", request.getParameter(key+""));
		}
		if(respData.size() > 0){
			onlinePay.setOrderId(respData.getString("passthrough"));
			onlinePay.setActualAmount(respData.containsKey("price")?respData.getString("price"):"0");
			onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
			onlinePay.setChannelOrderId(respData.getString("apporderID"));
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		if( respData.containsKey("result") && 1 == respData.getInteger("result") ){
			PayInfoConfig payInfoConfig = onlinePay.getPayInfoConfig();
			//http://www.callbackurl.com?apporderID=112233&price=100&uid=001
			StringBuffer sb = new StringBuffer();
			sb.append(onlinePay.getPayInfoConfig().getNotifyUrl()).append("?apporderID=").append(respData.getString("apporderID"))
				.append("&price=").append(respData.getString("price")).append("&uid=").append(respData.getString("uid"));
			String payKey = payInfoConfig.getExInfoMap().get("payKey").toString();
			String sign = HmacMD5.hmac(sb.toString(), payKey);
			if( respData.containsKey("sign") && respData.getString("sign").equals(sign) ){
				result.put("code", 2000);
				return getReturnSuccess();
			}else{
				result.put("code", 4006);
				result.put("errMsg", "验证错误！");
				return getReturnFailure();
			}
		}else{
			result.put("code", 4006);
			result.put("errMsg", "验证错误！");
			return getReturnFailure();
		}
	}

	@Override
	public String getReturnSuccess() {
		JSONObject json = new JSONObject();
		json.put("status", "success");
		json.put("desc", "通知成功");
		return json.toJSONString();
	}

	@Override
	public String getReturnFailure() {
		JSONObject json = new JSONObject();
		json.put("status", "failed");
		json.put("desc", "数字签名错误");
		return json.toJSONString();
	}
	
}
