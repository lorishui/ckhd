package me.ckhd.opengame.online.handle;

import java.util.Arrays;

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

@Component("letv")
@Scope("prototype")
public class letvHandle extends BaseHandle{

	private static final String login_url = "https://sso.letv.com/oauthopen/userbasic";//接口地址
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		onlineUser.setSid(verifyInfo.getString("uid"));
		StringBuilder param = new StringBuilder();
		param.append("client_id=");
		param.append(payInfo.getAppid());
		param.append("&uid=");
		param.append(verifyInfo.getString("uid"));
		param.append("&access_token=");
		param.append(verifyInfo.getString("accessToken"));
		log.info("登陆时发送给渠道的数据为:"+param.toString());
		String respStr = HttpClientUtils.get(login_url, param.toString(),2000,2000);
		try {
			if( StringUtils.isNotBlank(respStr) ){
				JSONObject data = JSONObject.parseObject(respStr);
				if( data.containsKey("status") && 1 == data.getInteger("status") ){
					JSONObject json = data.getJSONObject("result");
					onlineUser.setUserName(json.getString("nickname"));
					returnLoginSuccess(result, onlineUser);
				}else{
					result.put("errMsg", "用户验证失败！");
				}
			}else{
				result.put("errMsg", "数据为空或者请求失败！");
			}
		} catch (Exception e) {
			log.error("letv login error!", e);
			result.put("errMsg", "letv login error");
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
			onlinePay.setOrderId(respData.getString("cooperator_order_id"));
			onlinePay.setActualAmount(respData.containsKey("total_amount")?respData.getDouble("total_amount")*100+"":"0");
			onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
			onlinePay.setChannelOrderId(respData.getString("order_id"));
		}
		log.info("letv data="+respData.toJSONString());
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		if( "SUCCESS".equals(respData.getString("trade_status")) ){
			String signContent = getSignContent()+"key="+onlinePay.getPayInfoConfig().getExInfoMap().get("secretKey");
			log.info("letv sign content:"+signContent);
			String signNew = CoderUtils.md5(signContent, "utf-8");
			String sign = respData.getString("sign");
			if( signNew.equals(sign) ){
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

	private String getSignContent(){
		StringBuffer sb = new StringBuffer();
		Object[] obj = respData.keySet().toArray();
		Arrays.sort(obj);
		for(Object key:obj){
			if(!"sign".equals(key)){
				sb.append(key).append("=").append(respData.get(key)).append("&");
			}
		}
		return sb.toString();
	}
	
	@Override
	public String getReturnSuccess() {
		return "success";
	}

	@Override
	public String getReturnFailure() {
		return "fail";
	}

}
