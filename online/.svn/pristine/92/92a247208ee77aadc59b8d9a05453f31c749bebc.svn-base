package me.ckhd.opengame.online.handle;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.SignContext;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.util.HttpClientUtils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("meizu")
@Scope("prototype")
public class meizuHandle extends BaseHandle{
	
	private static final String verify_url ="https://api.game.meizu.com/game/security/checksession";//接口地址
	private Map<String,Object> resquestMap = new HashMap<String, Object>();
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode",-1);
		String app_id = payInfo.getAppid();
		String appKey = payInfo.getAppkey();
		long ts = System.currentTimeMillis();
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		String session_id = verifyInfo.containsKey("sessionId")?verifyInfo.getString("sessionId").toString():"";
		String uid = verifyInfo.containsKey("uid")?verifyInfo.getString("uid").toString():""; 
		onlineUser.setSid(uid);
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("app_id", app_id);
		param.put("ts", ts);
		param.put("session_id", session_id);
		param.put("uid", uid);
		String signContent = SignContext.getMeizuSignContext(param);
		log.info("meizu sign content="+signContent);
		String content = signContent + ":" +  appKey;
		String sign = CoderUtils.md5(content, "utf-8");
		log.info("meizu sign="+sign);
		param.put("sign", sign);
		param.put("sign_type", "md5");
		String data = signContent+"&sign="+sign+"&sign_type=md5";
		String responseData =HttpClientUtils.postVivo(verify_url, data, 2000, 2000);
		if( StringUtils.isNotBlank(responseData) ){
			JSONObject responseJson = JSONObject.parseObject(responseData);
			if( responseJson.getInteger("code")==200 ){
				returnLoginSuccess(result, onlineUser);
			}else{
				result.put("errMsg", "登录验证不正确!");
			}
		}else{
			result.put("errMsg", "数据为空或者请求失败！");
		}
		return result.toJSONString();
	}

	@Override
	public String pay(OnlinePay onlinePay, JSONObject codeJson) {
		Map<String,Object> map = new HashMap<String, Object>();
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo"); 
		String userId = verifyInfo.getString("uid");
		map.put("uid", userId);
		map.put("app_id", onlinePay.getPayInfoConfig().getAppid());
		map.put("cp_order_id", onlinePay.getOrderId());
		map.put("product_id", "0");
		map.put("product_subject",verifyInfo.get("productName"));
		map.put("product_body", "");
		map.put("product_unit","");
		map.put("buy_amount", 1);
		map.put("product_per_price",((float)onlinePay.getPrices())/100);
		map.put("total_price", ((float)onlinePay.getPrices())/100);
		map.put("create_time", System.currentTimeMillis());
		map.put("pay_type", "0");
		map.put("user_info", "");
		String signContent = SignContext.getMeizuPaySignContext(map);
		log.info("meizu pay sign content="+signContent);
		String sign = CoderUtils.md5(signContent+":"+onlinePay.getPayInfoConfig().getAppkey(),"utf-8");
		map.put("sign", sign);
		JSONObject result = new JSONObject();
		result.put("resultCode",0);
		result.put("errMsg","SUCCESS");
		JSONObject data = new JSONObject();
		data.put("orderId", onlinePay.getOrderId());
		data.put("sign", sign);
		data.put("createTime", map.get("create_time"));
		data.put("productSubject",map.get("product_subject"));
		result.put("result", data);
		return result.toJSONString();
	}
	
	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		@SuppressWarnings("unchecked")
		Set<String> set = request.getParameterMap().keySet();
		for( String key : set ){
			respData.put(key, request.getParameter(key));
			resquestMap.put(key, request.getParameter(key));
		}
		if(respData.size() > 0){
			onlinePay.setOrderId(respData.getString("cp_order_id"));
			onlinePay.setActualAmount(respData.containsKey("total_price")?((int)(respData.getDouble("total_price")*100))+"":"0");
			onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
			onlinePay.setChannelOrderId(respData.getString("order_id"));
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		String signContent = SignContext.getMeizuPaySignContext(resquestMap);
		log.info("meizu callback sign content="+signContent);
		String signNew = CoderUtils.md5(signContent+":"+onlinePay.getPayInfoConfig().getAppkey(),"utf-8");
		if( respData.containsKey("sign") && respData.getString("sign").equals(signNew) ){
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
		return "{\"code\":\"200\",\"message\":\"success\"}";
	}

	@Override
	public String getReturnFailure() {
		return "{\"code\":\"200\",\"message\":\"未知异常 \"}";
	}

}
