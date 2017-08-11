package me.ckhd.opengame.online.handle;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.util.HttpClientUtils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("4399")
@Scope("prototype")
public class m4399Handle extends BaseHandle{
	public static final String login_url = "http://m.4399api.com/openapi/oauth-check.html";

	public int gamemoney = 0;
	public int money = 0;
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		String state = verifyInfo.getString("state");
		String uid = verifyInfo.getString("uid");
		onlineUser.setSid(uid);
		String respStr = HttpClientUtils.get(login_url, "state="+state+"&uid="+uid, 20000, 20000);
		if( StringUtils.isNotBlank(respStr) ){
			JSONObject respJson = JSONObject.parseObject(respStr);
			if( respJson.containsKey("code") &&  100 == respJson.getInteger("code") ){
				returnLoginSuccess(result, onlineUser);
			}else{
				result.put("resultCode", respJson.getInteger("code"));
				result.put("errMsg", respJson.getInteger("message"));
			}
		}else{
			result.put("errMsg", "数据为空或者请求失败！");
		}
		return result.toJSONString();
	}

	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		@SuppressWarnings("unchecked")
		Set<String> set = request.getParameterMap().keySet();
		for( String key : set ){
			respData.put(key, request.getParameter(key));
		}
		if(respData.size() > 0){
			onlinePay.setOrderId(respData.getString("mark"));
			onlinePay.setActualAmount(respData.containsKey("money")?((int)(respData.getDouble("money")*100))+"":"0");
			onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
			onlinePay.setChannelOrderId(respData.getString("orderid"));
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		money = respData.getInteger("money");
		gamemoney = respData.getInteger("gamemoney");
		String signContent = respData.get("orderid")+""+respData.get("uid")+respData.get("money")+respData.get("gamemoney")+
				(respData.containsKey("serverid")?respData.get("serverid"):"")+
				onlinePay.getPayInfoConfig().getAppkey()+(respData.containsKey("mark")?respData.get("mark"):"")
				+(respData.containsKey("roleid")?respData.get("roleid"):"")+respData.get("time");
		log.info("4399 callback sign content="+signContent);
		String sign = CoderUtils.md5(signContent, "utf-8");
		log.info("4399 callback sign new="+sign);
		log.info("4399 callback sign="+respData.get("sign"));
		if(respData.containsKey("sign") && respData.getString("sign").equals(sign) && (money * 100 == onlinePay.getPrices())){
			result.put("code", 2000);
			return getReturnSuccess();
		}else{
			result.put("code", 4006);
			result.put("errMsg", "验证错误！");
			return getReturnFailure();
		}
	}

	@Override
	public String getReturnSuccess() {
		return "{\"status\":2,\"code\":null,\"money\":\""+money+"\",\"gamemoney\":\""+gamemoney+"\",\"msg\":\"充值成功\"}";
	}

	@Override
	public String getReturnFailure() {
		return "{\"status\":1,\"code\":\"sign_error\",\"money\":\""+money+"\",\"gamemoney\":\""+gamemoney+"\",\"msg\":\"请求串的md5验证码错误\"}";
	}

}
