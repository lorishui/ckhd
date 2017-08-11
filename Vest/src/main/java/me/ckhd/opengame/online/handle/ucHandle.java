package me.ckhd.opengame.online.handle;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.Encodes;
import me.ckhd.opengame.common.utils.MD5Util;
import me.ckhd.opengame.common.utils.SignContext;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.util.HttpClientUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("uc")
@Scope("prototype")
public class ucHandle extends BaseHandle{

//	private static final String verifyUrl = "http://sdk.g.uc.cn/cp/account.verifySession";//正式接口地址
//	private static final String verifyUrl_test = "http://sdk.test4.g.uc.cn/cp/account.verifySession";//测试接口地址
	
	//2017-05-08
	private static final String verifyUrl = "http://sdk.9game.cn/cp/account.verifySession";//正式接口地址
	private static final String verifyUrl_test = "http://sdk.test4.9game.cn/cp/account.verifySession";//测试接口地址
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		String url = verifyUrl;
		if( codeJson.containsKey("isTest") && codeJson.getInteger("isTest") == 1 ){
			url = verifyUrl_test;
		}
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		String sid = verifyInfo.containsKey("sid")?verifyInfo.getString("sid"):"";
		String gameid = payInfo.getAppid();
		//加密规则 ：MD5(sid=...+apiKey)
		//数据格式  {key:value,key;{},key;{}}
		JSONObject jsonData = new JSONObject();
		JSONObject sidJ = new JSONObject();
		sidJ.put("sid", sid);
		JSONObject gameidJ = new JSONObject();
		gameidJ.put("gameId", gameid);
		jsonData.put("id", System.currentTimeMillis()/1000L);
		jsonData.put("data", sidJ);
		jsonData.put("game", gameidJ);
		jsonData.put("sign", MD5Util.string2MD5("sid="+sid+payInfo.getAppkey()));
		String resposeData = HttpClientUtils.postVivo(url, jsonData.toJSONString(), 2000, 2000);
		//响应数据格式 
		if( StringUtils.isNotBlank(resposeData) ){
			try {
				resposeData = new String(resposeData.getBytes("gbk"),"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			JSONObject resultData = JSONObject.parseObject(resposeData);
			JSONObject state = JSONObject.parseObject(resultData.getString("state"));
			if(state.getInteger("code") == 1){
				JSONObject data = JSONObject.parseObject(resultData.getString("data"));
				String uid = data.getString("accountId");
				String username = data.getString("nickName");
				onlineUser.setSid(uid);
				onlineUser.setUserName(username);
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
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		JSONObject data = new JSONObject();
		data.put("callbackInfo", onlinePay.getOrderId());
		data.put("amount", getDoubleNumber(onlinePay.getPrices()*0.01d));
		data.put("serverId", 0);
		data.put("notifyUrl", StringUtils.isNotBlank(onlinePay.getNotifyUrl())?onlinePay.getNotifyUrl():onlinePay.getPayInfoConfig().getNotifyUrl());
		data.put("cpOrderId", onlinePay.getOrderId());
		data.put("accountId", verifyInfo.get("accountId"));
		String apiKey = onlinePay.getPayInfoConfig().getAppkey();
		String md5Str = SignContext.getSignContext(data,apiKey);
		log.info("uc pay sign context="+md5Str);
		String sign = CoderUtils.md5(md5Str,"utf-8");
		JSONObject result = new JSONObject();
		result.put("resultCode",0);
		result.put("errMsg","SUCCESS");
		JSONObject child = new JSONObject();
		child.put("orderId", onlinePay.getOrderId());
		child.put("sign", sign);
		child.put("callBackUrl", onlinePay.getPayInfoConfig().getNotifyUrl());
		result.put("result", child);
		return result.toJSONString();
	}
	
	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		JSONObject requestData = JSONObject.parseObject(code);
		if( requestData.containsKey("data") ){
			respData = requestData.getJSONObject("data");
			respData.put("sign", requestData.getString("sign"));
		}
		if( respData != null && respData.size() > 0){
			onlinePay.setOrderId(respData.getString("cpOrderId"));
			onlinePay.setActualAmount(respData.containsKey("amount")?((int)(respData.getDouble("amount")*100))+"":"0");
			onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:requestData.toJSONString());
			onlinePay.setChannelOrderId(respData.getString("orderId"));
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		if ( respData.containsKey("sign") && StringUtils.isNotBlank(respData.getString("sign")) ) {
			log.info("uc online sign respData="+respData.toJSONString());
			if(respData.containsKey("orderStatus") && "S".equals(respData.getString("orderStatus"))){
				String apiKey = onlinePay.getPayInfoConfig().getAppkey();
				String md5Str = SignContext.getSignContext(respData,apiKey);
				log.info("uc online sign context="+md5Str);
				log.info("uc online sign ="+respData.get("sign"));
				String sign = Encodes.string2MD5(md5Str).toLowerCase();
				String returnSign= respData.getString("sign");
				int money = (int)(Double.parseDouble(respData.get("amount").toString())*100);
				if(sign.equals(returnSign.toLowerCase()) && onlinePay.getPrices() == money){
					result.put("code", 2000);
					return getReturnSuccess();
				}else{
					result.put("code", 4006);
					result.put("errMsg", "验证错误！");
					return getReturnFailure();
				}
			}else{
				result.put("code", 4008);
				result.put("errMsg", "回掉数据中标识失败订单！");
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
		return "SUCCESS";
	}

	@Override
	public String getReturnFailure() {
		return "FAILURE";
	}

	private String getDoubleNumber(Double d){
		DecimalFormat df = new DecimalFormat("#0.00");
        return  df.format(d);
	}
}
