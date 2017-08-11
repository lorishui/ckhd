package me.ckhd.opengame.online.handle;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.SignContext;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.handle.common.weixin.MD5;
import me.ckhd.opengame.online.handle.common.wft.HttpUtils;
import me.ckhd.opengame.online.service.OnlineService;
import me.ckhd.opengame.online.util.XmlUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("weixin")
@Scope("prototype")
public class weixinHandle extends BaseHandle{
    
    @Autowired
    private OnlineService onlineService;
	
	private static final String pay_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";//支付预付接口地址
	private static final String login_url = "https://api.weixin.qq.com/sns/oauth2/access_token?grant_type=authorization_code";

	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		String markLogin = verifyInfo.containsKey("wxLoginType") ? verifyInfo.getString("wxLoginType") : "auth";
		if ("auth".equals(markLogin)) {
    		StringBuffer url = new StringBuffer(login_url);
    		url.append("&appid=").append(payInfo.getAppid())
    			.append("&secret=").append(payInfo.getExInfoMap().get("AppSecret"))
    			.append("&code=").append(verifyInfo.getString("code"));
    		String content = HttpUtils.post(url.toString(),"", "utf-8");
    		if( StringUtils.isNoneBlank(content) ){
    			JSONObject json = JSONObject.parseObject(content);
    			if( json.containsKey("openid") ){
    				result.put("resultCode",0);
    				result.put("errMsg","SUCCESS");
    				JSONObject returnData = new JSONObject();
                    onlineUser.setSid(json.getString("openid"));
                    onlineUser.setUid(onlineUser.getSid());
                    onlineUser.setSdkLoginType(2);
                    returnData.put("uid", onlineUser.getUid());
                    returnData.put("token",getRandomStr(16));
                    returnData.put("time", System.currentTimeMillis());
                    OnlineUser user = onlineService.get(onlineUser);
                    
                    if (user == null) {
                        returnData.put("firstLogin", 1);
                    } else {
                        returnData.put("firstLogin", -1);
                    }
                    result.put("result", returnData);
    			}else{
    			    result.put("resultCode", json.getInteger("errcode"));
    			}
    		}
		} else if ("autoLogin".equals(markLogin) && verifyInfo.containsKey("wxuid")) {
		    onlineUser.setSid(verifyInfo.getString("wxuid"));
            onlineUser.setUid(onlineUser.getSid());
            onlineUser.setSdkLoginType(2);
		    OnlineUser user = onlineService.get(onlineUser);
		    JSONObject returnData = new JSONObject();
		    returnData.put("time", System.currentTimeMillis());
		    if (user != null) {
		        returnData.put("uid", verifyInfo.getString("wxuid"));
                returnData.put("token",getRandomStr(16));
		    }
		    result.put("result", returnData);
		    result.put("resultCode",0);
            result.put("errMsg","SUCCESS");
		}
		return result.toJSONString();
	}
	
	@Override
	public String pay(OnlinePay onlinePay, JSONObject codeJson) {
		JSONObject result = new JSONObject();
		result.put("resultCode",-1);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("appid", onlinePay.getPayInfoConfig().getAppid());//公众账号ID
		map.put("body", onlinePay.getPayCodeConfig().getProductName());//商品或支付单简要描述
		map.put("mch_id", onlinePay.getPayInfoConfig().getExInfoMap().get("mch_id"));//商户号
		map.put("nonce_str", getRandomStr());//随机字符串，不长于32位
		map.put("notify_url", StringUtils.isNotBlank(onlinePay.getNotifyUrl())?onlinePay.getNotifyUrl():onlinePay.getPayInfoConfig().getNotifyUrl());//接收微信支付异步通知回调地址
		map.put("out_trade_no",  onlinePay.getOrderId());//商户系统内部的订单号,32个字符内、可包含字母,
		map.put("spbill_create_ip", onlinePay.getClientIp());//APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
		map.put("total_fee",onlinePay.getPrices());//订单总金额，单位为分
		map.put("trade_type", "APP");//取值如下：JSAPI，NATIVE，APP
		map.put("sign",genPackageSign(map,onlinePay.getPayInfoConfig().getAppkey()));//签名
		String reqdate = XmlUtils.toXml(map);
		log.info("支付时发送给渠道的数据为:"+reqdate);
		String content = HttpUtils.post(pay_url,reqdate, "utf-8");
		if( StringUtils.isNotBlank(content) ){
			Map<String,Object> payContent = XmlUtils.decodeXml(content);
			//判断获取到渠道返回是否成功
			if ("SUCCESS".equals(payContent.get("return_code"))) {
				Map<String,Object> param = new HashMap<String, Object>();
				param.put("appid", onlinePay.getPayInfoConfig().getAppid());
				param.put("partnerid", onlinePay.getPayInfoConfig().getExInfoMap().get("mch_id").toString()); 
				param.put("prepayid",payContent.get("prepay_id"));
				param.put("package" , "Sign=WXPay");
				param.put("noncestr", getRandomStr());
				param.put("timestamp",System.currentTimeMillis()/1000);
				log.info("signContent="+SignContext.getSignContext(param)+"&key="+onlinePay.getPayInfoConfig().getAppkey());
				String sign = CoderUtils.md5(SignContext.getSignContext(param)+"&key="+onlinePay.getPayInfoConfig().getAppkey(),"utf-8");
				param.put("sign", sign.toUpperCase());
				param.put("mch_id", onlinePay.getPayInfoConfig().getExInfoMap().get("mch_id"));
				result.put("resultCode",0);
				result.put("errMsg","SUCCESS");
				result.put("result", param);	
			}else{
				result.put("errMsg","去微信那边下单失败！");
			}
		}else{
			result.put("errMsg","去微信那边下单超时或网络问题！");
		}
		return result.toJSONString();
	}
	
	private String getRandomStr(){
		String str = "abcdefghijklmnopqrstuvwsyzABCDEFGHIJKLMNOPQRSTUVWSYZ1234567890";
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for(int i=0;i<16;i++){
			sb.append(str.charAt(random.nextInt(61)));
		}
		return sb.toString();
	}
	
	/**
	 生成签名
	 */
	private String genPackageSign(Map<String, Object> params,String apiKey) {
		StringBuilder sb = new StringBuilder();
		String[] obj = new String[params.size()];
		params.keySet().toArray(obj);
		Arrays.sort(obj);
		for (String key:obj) {
			sb.append(key);
			sb.append('=');
			sb.append(params.get(key));
			sb.append('&');
		}
		sb.append("key=");
		sb.append(apiKey);
		String packageSign = null;
		try {
			packageSign = MD5.getMessageDigest(sb.toString().getBytes("utf-8")).toUpperCase();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return packageSign;
	}
	
	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		log.info("weixin callback content:"+code);
		Map<String,Object> map = XmlUtils.decodeXml(code);
		respData = (JSONObject) JSONObject.toJSON(map);
		if(respData.size() > 0){
			onlinePay.setOrderId(respData.getString("out_trade_no"));
			onlinePay.setActualAmount(respData.containsKey("cash_fee")?respData.getString("cash_fee"):"0");
			onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
			onlinePay.setChannelOrderId(respData.getString("transaction_id"));
		}
	}
	
	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		String signNew = CoderUtils.md5(SignContext.getSignContext(respData)+"&key="+onlinePay.getPayInfoConfig().getAppkey(),"utf-8");
		String sign = respData.getString("sign");
		if(respData.containsKey("sign") && signNew.equals(sign.toLowerCase())){
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
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("return_code", "SUCCESS");
		return XmlUtils.toXml(map);
	}

	@Override
	public String getReturnFailure() {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("return_code", "FAIL");
		map.put("return_msg", "订单不存在!");
		return XmlUtils.toXml(map);
	}

}
