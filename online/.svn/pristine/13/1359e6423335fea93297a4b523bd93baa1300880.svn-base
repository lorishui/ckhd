package me.ckhd.opengame.online.handle;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.handle.common.xiaomi.HmacSHA1Encryption;
import me.ckhd.opengame.util.HttpClientUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("xiaomi")
@Scope("prototype")
public class xiaomiHandle extends BaseHandle{

	private static final String GOURL="http://mis.migc.xiaomi.com/api/biz/service/verifySession.do";//接口地址
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		onlineUser.setSid(verifyInfo.getString("userId"));
		onlineUser.setUid(verifyInfo.getString("userId")+"&"+codeJson.getString("ckChannelId"));
		Map<String, String> map= new HashMap<String, String>();
		map.put("appId",payInfo.getAppid());
		map.put("session",verifyInfo.getString("sessionId"));
		map.put("uid",verifyInfo.getString("userId"));
		String signature = "";
		try {
			String param = encryptData(map);
			String encryptKey  = payInfo.getExInfoMap().get("encryptKey")==null?"":payInfo.getExInfoMap().get("encryptKey").toString();
			signature = HmacSHA1Encryption.HmacSHA1Encrypt(param,encryptKey);
			map.put("signature",signature);
		} catch (Exception e) {
			log.error("xiaomi login 加密流程错误",e);
			result.put("code", 4009);
			result.put("errMsg", "加密流程错误！");
		}
		String params = "appId="+payInfo.getAppid()+"&session="+verifyInfo.getString("sessionId")+"&uid="+
				verifyInfo.getString("userId")+"&signature="+signature;
		String responseData =HttpClientUtils.doGet(GOURL,params,null);
		if( StringUtils.isNotBlank(responseData) ){
			JSONObject data = JSONObject.parseObject(responseData);
			if( data.containsKey("errcode") && 200 == data.getInteger("errcode") ){;
				returnLoginSuccess(result, onlineUser);
			}else{
				result.put("errMsg", "登录验证不正确!");
			}
		}else{
			result.put("errMsg", "数据为空或者请求失败！");
		}
		return result.toJSONString();
	}

	private String encryptData(Map<String, String> map){
		StringBuffer parStr=new StringBuffer();
		parStr.append("appId="+map.get("appId"));
		parStr.append("&session="+map.get("session"));
		parStr.append("&uid="+map.get("uid"));
		return parStr.toString();
	}
	
	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		@SuppressWarnings("unchecked")
		Set<String> set = request.getParameterMap().keySet();
		for( String key : set ){
			try{
				respData.put(key, new String(request.getParameter(key).getBytes("iso-8859-1"),"utf-8"));
			}catch(Exception e){
				log.error("小米转码出现错误！",e);
			}
		}
		if(respData.size() > 0){
			onlinePay.setOrderId(respData.getString("cpOrderId"));
			onlinePay.setActualAmount(respData.containsKey("payFee")?respData.getString("payFee"):"0");
			onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
			onlinePay.setChannelOrderId(respData.getString("orderId"));
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		if( respData.containsKey("orderStatus") && "TRADE_SUCCESS".equals(respData.getString("orderStatus"))){
			try {
				String signContent = getSignContent();
				String signature = respData.getString("signature");
				String encryptKey  = onlinePay.getPayInfoConfig().getExInfoMap().get("encryptKey")==null?"":onlinePay.getPayInfoConfig().getExInfoMap().get("encryptKey").toString();
				String sign = HmacSHA1Encryption.HmacSHA1Encrypt(signContent,encryptKey);
				if( sign.equals(signature) ){
					result.put("code", 2000);
					return getReturnSuccess();
				}else{
					result.put("code", 4006);
					result.put("errMsg", "验证错误！");
					return getReturnFailure();
				}
			} catch (Exception e) {
				log.error("xiaomi urldecode error!",e);
				result.put("code", 4009);
				result.put("errMsg", "加密流程错误！");
			}
		}else{
			result.put("code", 4007);
			result.put("errMsg", "数据为空！");
			return getReturnFailure();
		}
		return result.toJSONString();
	}

	@Override
	public String getReturnSuccess() {
		return "{\"errcode\":200}";
	}

	@Override
	public String getReturnFailure() {
		return "{\"errcode\":1515}";
	}
	
	private String getSignContent() throws UnsupportedEncodingException{
		Object[] key = respData.keySet().toArray();
		Arrays.sort(key);
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<key.length ; i++){
			if(!key[i].equals("signature")) sb.append(key[i]).append("=").append(URLDecoder.decode(respData.getString(key[i].toString()),"utf-8")).append("&");
		}
		sb.setLength(sb.length()>0?sb.length()-1:0);
		return sb.toString();
	}

}
