package me.ckhd.opengame.online.handle;

import java.util.Arrays;
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

@Component("qihoo")
@Scope("prototype")
public class qihooHandle extends BaseHandle{

	private static final String VERIFY_LOGIN_URL="https://openapi.360.cn/user/me.json";//接口地址
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		String respnoseData =HttpClientUtils.doGet(VERIFY_LOGIN_URL,codeJson.getJSONObject("verifyInfo"));
		if( StringUtils.isNotBlank(respnoseData) ){
			JSONObject data = JSONObject.parseObject(respnoseData);
			onlineUser.setSid(data.getString("id"));
			onlineUser.setUserName(data.getString("name"));
			returnLoginSuccess(result, onlineUser);
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
			log.info("qihoo callback:"+respData.toJSONString());
			onlinePay.setOrderId(respData.getString("app_order_id"));
			onlinePay.setActualAmount(respData.containsKey("amount")?respData.getString("amount"):"0");
			onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
			onlinePay.setChannelOrderId(respData.getString("order_id"));
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		if( respData.containsKey("gateway_flag") && "success".equals(respData.getString("gateway_flag"))){
			String signContet = getSignContent();
			String key = onlinePay.getPayInfoConfig().getAppkey();
			String sign = CoderUtils.md5(signContet+key, "utf-8");
			if( sign.equals(respData.getString("sign")) ){
				result.put("code", 2000);
				return getReturnSuccess();
			}else{
				result.put("code", 4006);
				result.put("errMsg", "验证错误！");
				return getReturnFailure();
			}
		}else{
			result.put("code", 4007);
			result.put("errMsg", "数据为空！");
			return getReturnFailure();
		}
	}

	@Override
	public String getReturnSuccess() {
		return "{\"status\":\"ok\",\"delivery\":\"success\",\"msg\":\"\"}";
	}

	@Override
	public String getReturnFailure() {
		return "{\"status\":\"error\",\"delivery\":\"mismatch\",\"msg\":\"校验失败\"}";
	}
	
	private String getSignContent(){
		Object[] key = respData.keySet().toArray();
		Arrays.sort(key);
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<key.length ; i++){
			if(!key[i].equals("sign") && !key[i].equals("sign_return") ) sb.append(respData.get(key[i])).append("#");
		}
		return sb.toString();
	}
}
