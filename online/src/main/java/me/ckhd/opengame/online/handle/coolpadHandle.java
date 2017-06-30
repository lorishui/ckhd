package me.ckhd.opengame.online.handle;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.handle.common.coolpad.SignHelper;
import me.ckhd.opengame.online.util.coolpad.CpTransSyncSignValid;
import me.ckhd.opengame.util.HttpClientUtils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("coolpad")
@Scope("prototype")
public class coolpadHandle extends BaseHandle{
	
	private static final String login_url = "https://openapi.coolyun.com/oauth2/token?grant_type=authorization_code";
//	private static final String pay_url = "http://pay.coolyun.com:6988/payapi/order";//支付预付接口地址
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		StringBuilder param = new StringBuilder();
		param.append("client_id=");
		param.append(payInfo.getAppid());
		param.append("&redirect_uri=");
		param.append(payInfo.getAppkey());
		param.append("&client_secret=");
		param.append(payInfo.getAppkey());
		param.append("&code=");
		param.append(verifyInfo.getString("Authcode"));
		log.info("登陆时发送给渠道的数据为:"+param.toString());
		String responseData =HttpClientUtils.get(login_url, param.toString(),2000,2000);
		if( StringUtils.isNotBlank(responseData) ){
			JSONObject responseJson = JSONObject.parseObject(responseData);
			if( responseJson!=null && responseJson.containsKey("error") ){
				result.put("errMsg", responseJson.getString("error_description"));
			}else if(responseJson!=null){
				onlineUser.setSid(responseJson.getString("openid"));
				onlineUser.setToken(responseJson.getString("access_token"));
				returnLoginSuccess(result, onlineUser);
			}
		}else{
			result.put("errMsg","数据为空或者请求失败！");
		}
		return result.toJSONString();
	}
	
	@Override
	public void returnLoginSuccess(JSONObject result, OnlineUser onlineUser) {
		result.put("resultCode",0);
		result.put("errMsg","SUCCESS");
		JSONObject returnData = new JSONObject();
		onlineUser.setUid(onlineUser.getSid()+"&"+onlineUser.getChannelId());
		returnData.put("uid", onlineUser.getUid() );
		returnData.put("token",onlineUser.getToken());
		result.put("result", returnData);
	}
	
	@Override
	public String pay(OnlinePay onlinePay, JSONObject codeJson) {
		JSONObject result = new JSONObject();
		result.put("resultCode",0);
		result.put("errMsg","SUCCESS");
		JSONObject data = new JSONObject();
		data.put("orderId", onlinePay.getOrderId());
		data.put("paykey", onlinePay.getPayInfoConfig().getExInfoMap().get("paykey").toString());
		result.put("result", data);
		return result.toJSONString();
	}
	
	/*@Override
	public String pay(OnlinePay onlinePay, JSONObject codeJson) {
		JSONObject result = new JSONObject();
		result.put("resultCode",-1);
		result.put("errMsg","下单失败!");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appid", onlinePay.getPayInfoConfig().getAppid());//公众账号ID
		map.put("waresid", onlinePay.getPayCodeConfig().getExInfoMap().get("waresid"));//商品编号
		map.put("waresname", onlinePay.getPayCodeConfig().getProductName());//商品名称
		map.put("price",onlinePay.getPrices()*0.01);
		map.put("cporderid",  onlinePay.getOrderId());//商户系统内部的订单号,32个字符内、可包含字母,
		map.put("currency","RMB");//货币类型
		map.put("appuserid", onlinePay.getUserId());//userid
		map.put("APPV_KEY", onlinePay.getPayInfoConfig().getExInfoMap().get("PRIVATE_KEY"));
		map.put("notifyurl", StringUtils.isNotBlank(onlinePay.getNotifyUrl())?onlinePay.getNotifyUrl():onlinePay.getPayInfoConfig().getNotifyUrl());
		String reqData = reqData(map);
		log.info("支付时发送给渠道的数据为:"+reqData);
		String responseData = HttpUtils.sentPost(pay_url, reqData,"UTF-8"); // 请求验证服务端
		if( StringUtils.isNotBlank(responseData) ){
			try {
				JSONObject respJson = spiltStr(responseData);
				if(respJson.containsKey("sign")){
					if (SignHelper.verify(respJson.getString("transdata"), respJson.getString("sign"),onlinePay.getPayInfoConfig().getExInfoMap().get("PLATP_KEY").toString())) {
						result.put("resultCode",0);
						result.put("errMsg","SUCCESS");
						JSONObject data = new JSONObject();
						data.put("transid", respJson.getJSONObject("transdataJson").getString("transid"));
						result.put("result", data);
					} else {
						result.put("resultCode",411);
						result.put("errMsg","伪数据");
					}
				}else{
					result.put("resultCode", respJson.getJSONObject("transdataJson").getInteger("code"));
					result.put("errMsg", respJson.getJSONObject("transdataJson").getString("errmsg"));
				}
			} catch (Exception e) {
				log.error("coopad urldecode error!", e);
				result.put("resultCode",411);
				result.put("errMsg","数据解析失败");
			}
		}
		return result.toJSONString();
	}*/
	
	/*private JSONObject spiltStr(String data){
		JSONObject json = new JSONObject();
		String[] arr = data.split("&");
		if( arr != null ){
			for(String str:arr){
				if(str.startsWith("sign")){
					json.put(str.substring(0, str.indexOf("=")), str.substring(str.indexOf("=")+1));
				}else{
					String[] key = str.split("=");
					if(key != null && "transdata".equals(key[0])){
						if(key.length > 1){
							json.put("transdata", key[1]);
							json.put("transdataJson", JSONObject.parse(key[1]));
						}
					}else{
						if(key.length > 1){
							json.put(key[0], key[1]);
						}
					}
				}
			}
		}
		return json;
	}*/

	
	public static String reqData(Map<String, Object> params) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("appid", params.get("appid"));
		jsonObject.put("waresid", Integer.valueOf(params.get("waresid").toString()));
		jsonObject.put("cporderid", params.get("cporderid"));
		jsonObject.put("currency", "RMB");
		jsonObject.put("appuserid", params.get("appuserid"));
		String waresname= params.get("waresname")==null?"":params.get("waresname").toString();
		//以下是参数列表中的可选参数
		if(!waresname.isEmpty()){
			jsonObject.put("waresname", waresname);
		}
		/*
		 * 当使用的是 开放价格策略的时候 price的值是 程序自己 设定的价格，使用其他的计费策略的时候
		 * price 不用传值
		 * */ 
		jsonObject.put("price",Float.valueOf(params.get("price")==null?"1":params.get("price").toString()));
		String cpprivateinfo=(params.get("cpprivateinfo")==null?"":params.get("cpprivateinfo").toString());
		if(!cpprivateinfo.isEmpty()){
			jsonObject.put("cpprivateinfo", cpprivateinfo);
		}
		String notifyurl = (params.get("notifyurl")==null?"":params.get("notifyurl").toString());
		if(!notifyurl.isEmpty()){
			/*
			 * 如果此处不传同步地址，则是以后台传的为准。
			 * */
			jsonObject.put("notifyurl", notifyurl);
		}
		String content = jsonObject.toString();// 组装成 json格式数据
		// 调用签名函数      重点注意： 请一定要阅读  sdk 包中的爱贝AndroidSDK3.4.4\03-接入必看-服务端接口说明及范例\爱贝服务端接入指南及示例0311\IApppayCpSyncForJava \接入必看.txt 
		String sign = SignHelper.sign(content, params.get("APPV_KEY").toString());
		String data = "transdata=" + content + "&sign=" + sign+ "&signtype=RSA";// 组装请求参数
		return data;
	}
	
	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {	
		try {
			@SuppressWarnings("unchecked")
			Set<String> set = request.getParameterMap().keySet();
			for( String key : set ){
				respData.put(key, request.getParameter(key));
			}
			if(respData.size() > 0){
				JSONObject data = JSONObject.parseObject(respData.getString("transdata"));
				onlinePay.setOrderId(data.getString("exorderno"));
				onlinePay.setActualAmount(data.containsKey("money")?data.getIntValue("money")+"":"0");
				onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
				onlinePay.setChannelOrderId(data.getString("transid"));
			}
		} catch (Exception e) {
			log.error("coolpad callback 解析数据失败!", e);
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		String transdata = respData.getString("transdata");
		String sign = respData.getString("sign");
		if( StringUtils.isNotBlank(sign) ){
//			if (SignHelper.verify(transdata, sign,onlinePay.getPayInfoConfig().getExInfoMap().get("PLATP_KEY").toString())) {
			if (CpTransSyncSignValid.validSign(transdata, sign,onlinePay.getPayInfoConfig().getExInfoMap().get("paykey").toString())) {
				result.put("code", 2000);
				return getReturnSuccess();
			}else{
				result.put("code", 4006);
				result.put("errMsg", "验证错误！");
				return getReturnFailure();
			}
		}else{
			result.put("code", 4012);
			result.put("errMsg", "数据缺失！");
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
	
}
