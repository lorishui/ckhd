package me.ckhd.opengame.online.handle;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.handle.common.huawei.RSA;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("huawei")
@Scope("prototype")
public class huaweiHandle  extends BaseHandle{
	
	private Map<String,Object> resquestMap = new HashMap<String, Object>();
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", 0);
		JSONObject verifyinfo = codeJson.getJSONObject("verifyInfo");
		onlineUser.setSid( verifyinfo.getString("playerId") );
		returnLoginSuccess(result, onlineUser);
		return result.toJSONString();
	}
	//pay
	@Override
	public String pay(OnlinePay onlinePay, JSONObject codeJson) {
		Map<String,Object> map = new HashMap<String, Object>();
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo"); 
		map.put("userID", verifyInfo.getString("userID"));
		map.put("productName", verifyInfo.getString("productName"));
		map.put("productDesc", verifyInfo.getString("productDesc"));
		map.put("applicationID", onlinePay.getPayInfoConfig().getAppid());
		map.put("amount", getDoubleNumber(onlinePay.getPrices()*0.01d));//金额单位转换
		map.put("requestId", onlinePay.getOrderId());
//		map.put("time", System.currentTimeMillis());
		//获取需要加密的串
		String signContent = RSA.getSignData(map);
		String sign = RSA.sign(signContent, onlinePay.getPayInfoConfig().getAppkey());
		JSONObject result = new JSONObject();
		result.put("resultCode",0);
		result.put("errMsg","SUCCESS");
		JSONObject data = new JSONObject();
		data.put("orderId", onlinePay.getOrderId());
		data.put("sign", sign);
		result.put("result", data);
		return result.toJSONString();
	}
	
	private String getDoubleNumber(Double d){
		DecimalFormat df = new DecimalFormat("#0.00");
        return  df.format(d);
	}
	//pay end
	//callback start
	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		/*Set<String> set = request.getParameterMap().keySet();
		for( String key : set ){
			try {
				respData.put(key, new String(request.getParameter(key).getBytes("iso8859-1"),"utf-8"));
				resquestMap.put(key, new String(request.getParameter(key).getBytes("iso8859-1"),"utf-8"));
			} catch (UnsupportedEncodingException e) {
				log.error("huawei change encode error!", e);
			}
		}*/
		if( StringUtils.isNotBlank(code) ){
			String[] strArr = code.split("&");
			for( String key : strArr ){
				String[] keyValue = key.split("=");
				if( keyValue.length>0 ){
					try {
						respData.put(keyValue[0], URLDecoder.decode(keyValue[1], "utf-8"));
						resquestMap.put(keyValue[0], URLDecoder.decode(keyValue[1], "utf-8"));
					} catch (UnsupportedEncodingException e) {
						log.error("huawei change encode error!", e);
					}
				}
			}
		}
		if(respData.size() > 0){
			onlinePay.setOrderId(respData.getString("requestId"));
			onlinePay.setActualAmount(respData.containsKey("amount")?((int)(respData.getDouble("amount")*100))+"":"0");
			onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
			onlinePay.setChannelOrderId(respData.getString("orderId"));
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay,JSONObject result,HttpServletResponse response) {
		boolean isSign = false;
		try {
			String cpKey = onlinePay.getPayInfoConfig().getExInfoMap().containsKey("publicKey")?onlinePay.getPayInfoConfig().getExInfoMap().get("publicKey").toString():"";
			//获取待签名字符串
	        String content = RSA.getSignData(resquestMap);
	        log.info("huawei sign content:"+content);
	        if( respData.containsKey("result") && 0 == respData.getInteger("result")){
	        	//验签 
	        	isSign = RSA.doCheck(content, respData.getString("sign"), cpKey);
	        }
		} catch (Throwable e) {
			log.error("gionee RSA docheck ERROR!", e);
		}
		if(isSign){
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
		JSONObject json = new JSONObject();
		json.put("result", 0);
		return json.toJSONString();
	}

	@Override
	public String getReturnFailure() {
		JSONObject json = new JSONObject();
		json.put("result", 1);
		return json.toJSONString();
	}
	//callback end
}
