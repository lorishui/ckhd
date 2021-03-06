package me.ckhd.opengame.online.handle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.handle.common.wft.HttpUtils;
import me.ckhd.opengame.online.util.anzhi.Base64;
import me.ckhd.opengame.online.util.anzhi.Des3Util;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("anzhi")
@Scope("prototype")
public class anzhiHandle extends BaseHandle {
	
//	private static final String login_url_old="http://user.anzhi.com/web/api/sdk/third/1/queryislogin";//接口地址
//	private static final String login_url="http://user.anzhi.com/web/cp/4/queryislogin";
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		String url = verifyInfo.getString("requestUrl");
//		String account = verifyInfo.getString("loginName");
		String appsecret = payInfo.getExInfoMap().get("app_secret").toString();
		try{
			String data = "time="+System.currentTimeMillis()+"&appkey="+payInfo.getAppkey()
					+"&cptoken="+verifyInfo.getString("cptoken")+"&sign="+CoderUtils.md5(payInfo.getAppkey()+verifyInfo.getString("cptoken")+appsecret,"utf-8")
					+"&deviceId="+verifyInfo.getString("deviceId");
			String respStr  = HttpUtils.post(url, data,"application/x-www-form-urlencoded;charset=UTF-8", "utf-8");
			log.info("anzhi Data:"+respStr);
			JSONObject obj = JSONObject.parseObject(respStr);
			if( obj.containsKey("code") && 1 == obj.getInteger("code") ){
				String uidStr = Base64.decode(obj.getString("data"), "utf-8");
				JSONObject uidJson = JSONObject.parseObject(uidStr);
				onlineUser.setSid(uidJson.getString("uid"));
				onlineUser.setUserName(uidJson.getString("nickname"));
				returnLoginSuccess(result, onlineUser);
			}else{
				result.put("errMsg", "用户验证失败！");
			}
		}catch (Exception e) {
			log.error("anzhi login error!", e);
			result.put("errMsg", "连接失败");
		}	
		return result.toJSONString();
	}

	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		String data = Des3Util.decrypt(request.getParameter("data"),onlinePay.getPayInfoConfig().getExInfoMap().get("app_secret").toString());
		respData = JSONObject.parseObject(data);
		if (respData.size() > 0) {
			onlinePay.setOrderId(respData.getString("cpInfo"));
			Integer price = 0;
			if (respData.containsKey("orderAmount")) {
			    price = respData.getInteger("orderAmount");
			    if (respData.containsKey("redBagMoney")) {
			        price += respData.getInteger("redBagMoney");
			    }
			}
			onlinePay.setActualAmount(price.toString());//redBagMoney
			onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
			onlinePay.setChannelOrderId(respData.getString("orderId"));
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		if( respData.containsKey("code") && 1 == respData.getInteger("code") ){
			result.put("code", 2000);
			return getReturnSuccess();
		}
		result.put("code", 4006);
		result.put("errMsg", "验证错误！");
		return getReturnFailure();
	}

	@Override
	public String getReturnSuccess() {
		return "success";
	}

	@Override
	public String getReturnFailure() {
		return "order_error";
	}

}
