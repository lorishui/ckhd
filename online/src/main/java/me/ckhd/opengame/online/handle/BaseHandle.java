package me.ckhd.opengame.online.handle;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.Result;
import me.ckhd.opengame.common.utils.IdGen;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;

import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public abstract class BaseHandle {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	public JSONObject respData = new JSONObject();
	
	public abstract String login(OnlineUser onlineUser,JSONObject codeJson,PayInfoConfig payInfo);
	
	public void returnLoginSuccess(JSONObject result,OnlineUser onlineUser){
		result.put("resultCode",0);
		result.put("errMsg","SUCCESS");
		JSONObject returnData = new JSONObject();
		onlineUser.setUid(onlineUser.getSid()+"&"+onlineUser.getChannelId());
		returnData.put("uid", onlineUser.getUid() );
		returnData.put("token",getRandomStr(16));
		result.put("result", returnData);
	}
	
	public String pay(OnlinePay onlinePay,JSONObject codeJson){
		JSONObject result = new JSONObject();
		result.put("resultCode",0);
		result.put("errMsg","SUCCESS");
		JSONObject data = new JSONObject();
		data.put("orderId", onlinePay.getOrderId());
		data.put("callBackUrl", StringUtils.isNotBlank(onlinePay.getNotifyUrl())?onlinePay.getNotifyUrl():onlinePay.getPayInfoConfig().getNotifyUrl());
		result.put("result", data);
		return result.toJSONString();
	}
	
	public Result<Void> queryOrderState(OnlinePay onlinePay,JSONObject codeJson) {
//		Result<Void> result = new Result<Void>();
//		result.setCode(1);
//		result.setMessage("waiting");
//		return result;
		throw new java.lang.UnsupportedOperationException();
	}
	
	public void setOnlineUser(OnlineUser onlineUser,JSONObject codeJson){
		onlineUser.setChannelId(codeJson.getString("ckChannelId"));
		onlineUser.setCkAppId(codeJson.getString("ckAppId"));
		onlineUser.setVersion(codeJson.getString("version"));
		onlineUser.setAppVerifyInfo(codeJson.getJSONObject("verifyInfo").toJSONString());
		onlineUser.setLoginType(codeJson.getString("loginType"));
		onlineUser.setToken(IdGen.uuid());
	}
	//回调处理数据
	public abstract void parseParamter(String code,HttpServletRequest request,OnlinePay onlinePay);
	//验证数据
	public abstract String verifyData(OnlinePay onlinePay,JSONObject result,HttpServletResponse response);
	//返回正确信息
	public abstract String getReturnSuccess();	
	//返回错误信息
	public abstract String getReturnFailure();
	
	//组装发货数据
	public Map<String, Object> getSendOrder(OnlinePay onlinePay){
		Map<String, Object> map = new HashMap<String, Object>();
		String content = JSONObject.toJSONString(onlinePay.getSenderMap());
		map.put("sendNum",0);
		map.put("content",content);
		map.put("sendStatus",1);
		return map;
	}
	
	/**随机生成16位*/
	public static String getRandomStr(int n){
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<n;i++){
			int m = RandomUtils.nextInt(62);
			sb.append(str.charAt(m));
		}
		return sb.toString();
	}
}
