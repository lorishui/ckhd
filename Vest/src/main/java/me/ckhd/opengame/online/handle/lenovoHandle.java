package me.ckhd.opengame.online.handle;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.utils.XmlUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.handle.common.lenovo.CpTransSyncSignValid;
import me.ckhd.opengame.online.handle.common.wft.HttpUtils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("lenovo")
@Scope("prototype")
public class lenovoHandle extends BaseHandle{

	private static final String login_url ="http://passport.lenovo.com/interserver/authen/1.2/getaccountid";//接口地址
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		String data="lpsust="+verifyInfo.getString("accessToken")+"&realm="+payInfo.getAppid();
		String respData = HttpUtils.post(login_url+"?"+data, "", "utf-8");
		log.info(" lenovo login result="+respData);
		if( StringUtils.isNotBlank(respData) ){
			Map<String,Object> map = XmlUtils.Str2Map(respData);
			if(map.containsKey("AccountID")){
				onlineUser.setSid(map.get("AccountID") == null ? "": map.get("AccountID").toString());
				onlineUser.setUserName(map.get("Username") == null ? "": map.get("Username").toString());
				returnLoginSuccess(result, onlineUser);
			}else{
				onlineUser.setErrMsg(map.get("Code").toString());
				result.put("errMsg", "用户验证失败！");
			}
		}else{
			result.put("errMsg", "数据为空或者请求失败！");
		}
		return result.toJSONString();
	}

	public String pay(OnlinePay onlinePay,JSONObject codeJson){
		JSONObject result = new JSONObject();
		result.put("resultCode",0);
		result.put("errMsg","SUCCESS");
		JSONObject data = new JSONObject();
		data.put("orderId", onlinePay.getOrderId());
		data.put("payCode", onlinePay.getPayCodeConfig().getExInfoMap().containsKey("payCode")?onlinePay.getPayCodeConfig().getExInfoMap().get("payCode"):onlinePay.getPayCodeConfig().getProductId());
		data.put("callBackUrl", StringUtils.isNotBlank(onlinePay.getNotifyUrl())?onlinePay.getNotifyUrl():onlinePay.getPayInfoConfig().getNotifyUrl());
		result.put("result", data);
		return result.toJSONString();
	}
	
	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		respData.put("sign", request.getParameter("sign"));
		respData.put("transdata", request.getParameter("transdata"));
		respData.put("transdataJson", JSONObject.parse(request.getParameter("transdata")));
		log.info(" lenovo paramters="+respData.toString());
		if(respData.size() > 0){
			onlinePay.setOrderId(respData.getJSONObject("transdataJson").getString("exorderno"));
			onlinePay.setActualAmount(respData.getJSONObject("transdataJson").containsKey("money")?respData.getJSONObject("transdataJson").getString("money"):"0");
			onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
			onlinePay.setChannelOrderId(respData.getJSONObject("transdataJson").getString("transid"));
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		JSONObject json = respData.getJSONObject("transdataJson");
		if( json.containsKey("result") && 0 == json.getInteger("result") ){
			boolean isSuccess =  CpTransSyncSignValid.validSign(respData.getString("transdata"), respData.getString("sign"), onlinePay.getPayInfoConfig().getAppkey());
			if(isSuccess){
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
		return "SUCCESS";
	}

	@Override
	public String getReturnFailure() {
		return "FAILURE";
	}

}
