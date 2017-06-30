package me.ckhd.opengame.online.handle;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.handle.common.vivo.VivoSignUtils;
import me.ckhd.opengame.util.HttpClientUtils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("vivo")
@Scope("prototype")
public class vivoHandle extends BaseHandle{
	
	public static String verifyUrl = "https://usrsys.vivo.com.cn/sdk/user/auth.do";
	public static String payUrl = "https://pay.vivo.com.cn/vcoin/trade";
	private Map<String,String> resquestMap = new HashMap<String, String>();
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		JSONObject verifyinfo = codeJson.getJSONObject("verifyInfo");
		String authtoken = "";
		if( verifyinfo != null){
			authtoken = verifyinfo.containsKey("authtoken")?verifyinfo.getString("authtoken"):"";
		}
		String responseData =  HttpClientUtils.get(verifyUrl, "authtoken="+authtoken, 20000, 20000);
		if( StringUtils.isNotBlank(responseData)  ){
			JSONObject responseJson = JSONObject.parseObject(responseData);
			if( responseJson.containsKey("retcode") && 0 == responseJson.getInteger("retcode") ){
				JSONObject data = responseJson.getJSONObject("data");
				onlineUser.setSid( data.getString("openid") );
				returnLoginSuccess(result, onlineUser);
			}
		}
		return result.toJSONString();
	}
	
	//pay start
	public String pay(OnlinePay onlinePay,JSONObject codeJson){
		JSONObject result = new JSONObject();
		try{
			result.put("resultCode",-1);
			result.put("errMsg","下单失败!");
			Map<String, String> param = getPayMap(onlinePay);
			param.put("signMethod", "MD5");
//			param.put("signature", getPaySign(onlinePay));
			String requestStr = getPaySign(onlinePay);
			log.info(String.format("支付时发送给渠道的数据为:[%s]",requestStr));
			String responseStr =HttpClientUtils.postVivo(payUrl, requestStr, 2000, 2000);
			log.info(String.format("支付返回的数据为:[%s]",responseStr));
			if(StringUtils.isNotEmpty(responseStr)){
				JSONObject resultJson = JSONObject.parseObject(responseStr);
				String respCode = resultJson.getString("respCode");
				if( StringUtils.isNotBlank(respCode) && "200".equals(respCode)){
					JSONObject data = new JSONObject();
					result.put("resultCode",0);
					result.put("errMsg","SUCCESS");
					data.put("transNo", resultJson.getString("orderNumber"));
					data.put("accessKey", resultJson.getString("accessKey"));
	//				data.put("orderAmount", resultJson.getString("orderAmount"));
					result.put("result", data);
				}else if(respCode.equals("400")){
					result.put("resultCode", 400);
					result.put("errMsg", "参数为空或者格式不正确");
				}else if(respCode.equals("402")){
					result.put("resultCode", 402);
					result.put("errMsg", "商户id非法，请检查");
				}else if(respCode.equals("403")){
					result.put("resultCode", 403);
					result.put("errMsg", "验签失败，请检查");
				}else if(respCode.equals("405")){
					result.put("resultCode", 405);
					result.put("errMsg", "cp订单号不唯一");
				}else if(respCode.equals("406")){
					result.put("resultCode", 406);
					result.put("errMsg", "appId非法，请检查");
				}else if(respCode.equals("500")){
					result.put("resultCode", 500);
					result.put("errMsg", "服务器内部错误，请稍后再试");
				}
			}else{
				
			}
		}catch (Throwable e) {
			log.error("vivo pay fail!", e);
			result.put("resultCode",407);
			result.put("errMsg","下单出现异常");
		}
		return result.toJSONString();
	}
	
	private String getPaySign(OnlinePay onlinePay){
		Map<String, String> para = getPayMap(onlinePay);
		String cpKey = onlinePay.getPayInfoConfig().getExInfoMap().get("CpKey")==null?"":onlinePay.getPayInfoConfig().getExInfoMap().get("CpKey").toString();
    	return VivoSignUtils.buildReq(para, cpKey);
	}
	
	private Map<String,String> getPayMap(OnlinePay onlinePay){
		Map<String, String> para = new HashMap<String, String>();
		para.put("cpId", onlinePay.getPayInfoConfig().getExInfoMap().get("CPID").toString());
		para.put("appId", onlinePay.getPayInfoConfig().getAppid());
		para.put("cpOrderNumber",onlinePay.getOrderId());
		para.put("notifyUrl", StringUtils.isNotBlank(onlinePay.getNotifyUrl())?onlinePay.getNotifyUrl():onlinePay.getPayInfoConfig().getNotifyUrl());
		para.put("orderTime", DateUtils.formatDateToStr("yyyyMMddHHmmss") );
		para.put("orderAmount", onlinePay.getPrices()+"");
		para.put("orderTitle", onlinePay.getPayCodeConfig().getProductName());
		para.put("orderDesc", onlinePay.getPayCodeConfig().getProductName());
		para.put("extInfo", "");
		para.put("version", "1.0.0");
		return para;
	}
	
	/**
	 * 获取请求数据
	 * @param map
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getSignContext(Map<String,String> map) throws UnsupportedEncodingException{
		if( map!=null && map.size()>0 ){
			Set<String> keySet = map.keySet();
			Object[] obj = keySet.toArray();
			Arrays.sort(obj);
			StringBuffer sb = new StringBuffer();
			for(int i=0; i<obj.length ; i++){
//				if( !"signature".equals(obj[i]) || "signMethod".equals(obj[i])){
				sb.append(obj[i]).append("=").append(URLEncoder.encode(map.get(obj[i]),"utf-8")).append("&");
//				}
			}
			sb.setLength(sb.length()-1);
			return sb.toString();
		}
		return "";
	}
	//pay end
	
	//callback start
	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		@SuppressWarnings("unchecked")
		Set<String> set = request.getParameterMap().keySet();
		for( String key : set ){
			respData.put(key, request.getParameter(key));
			resquestMap.put(key, (String)request.getParameter(key));
		}
		if(respData.size() > 0){
			onlinePay.setOrderId(respData.getString("cpOrderNumber"));
			onlinePay.setActualAmount(respData.containsKey("orderAmount")?respData.getString("orderAmount"):"0");
			onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
			onlinePay.setChannelOrderId(respData.getString("orderNumber"));
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay,JSONObject result,HttpServletResponse response) {
		boolean isSign = false;
		try {
			String cpKey = onlinePay.getPayInfoConfig().getExInfoMap().containsKey("CpKey")?onlinePay.getPayInfoConfig().getExInfoMap().get("CpKey").toString():"";
			isSign = VivoSignUtils.verifySignature(resquestMap, cpKey);
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
		return "success";
	}

	@Override
	public String getReturnFailure() {
		return "fail";
	}
	//callback end
}
