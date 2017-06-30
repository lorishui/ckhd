package me.ckhd.opengame.online.handle;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;

/**
 * 怪猫
 * @author ASUS
 *
 */
@Component("guaimao")
@Scope(value=WebApplicationContext.SCOPE_REQUEST)
public class guaimaoHandle extends BaseHandle{

	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson, PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		onlineUser.setSid(verifyInfo.getString("uid"));
		returnLoginSuccess(result, onlineUser);
		return result.toJSONString();
	}

	@Override
	public void parseParamter(String code, HttpServletRequest request, OnlinePay onlinePay) {
		@SuppressWarnings("unchecked")
		Set<String> keys = request.getParameterMap().keySet();
		for(String key:keys){
			respData.put(key, request.getParameter(key));
		}
		if(respData.size()>0){
			onlinePay.setOrderId(respData.containsKey("developerinfo")?respData.getString("developerinfo"):"");
			onlinePay.setActualAmount(respData.containsKey("coin")?respData.getDouble("coin")*100+"":"");
			onlinePay.setChannelOrderId(respData.containsKey("order_id")?respData.getString("order_id"):"");
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result, HttpServletResponse response) {
		String signContent = "order_id="+respData.getString("order_id")+"&server_id="+respData.getString("server_id")+
				"&role_id="+respData.getString("role_id")+"&developerinfo="+respData.getString("developerinfo")+
				"&coin="+respData.getString("coin")+"&"+onlinePay.getPayInfoConfig().getAppkey();
		log.info("怪猫验证串："+signContent);
		String sign = respData.getString("signature");
		String signNew = CoderUtils.md5(signContent, "utf-8");
		if( StringUtils.isNotBlank(sign) && sign.equals(signNew) ){
			result.put("code", 2000);
			return getReturnSuccess();
		}else{
			result.put("code", 2002);
			result.put("errMsg", "验证错误");
			return getReturnFailure();
		}
	}

	@Override
	public String getReturnSuccess() {
		return "ok";
	}

	@Override
	public String getReturnFailure() {
		return "fail";
	}
	
}
