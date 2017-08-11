package me.ckhd.opengame.online.handle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.MD5Util;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.util.HttpClientUtils;

import com.alibaba.fastjson.JSONObject;

@Component("fenghuang")
@Scope("prototype")
public class fenghuangHandle extends BaseHandle{
	
	private static final String loginUrl = "http://union.play.ifeng.com/mservice2";

	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson, PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		String service = "user.validate";
		String partner_id = payInfo.getExInfoMap().get("partnerId")+"";
		String game_id = payInfo.getAppid();
		String server_id = payInfo.getExInfoMap().get("serverId")+"";
		String ticket = verifyInfo.containsKey("ticket")?verifyInfo.getString("ticket"):"";
		String sign = MD5Util.string2MD5((partner_id+game_id+server_id+ticket+payInfo.getAppkey())).toUpperCase();
		String formart = "json";
		String data = "service="+service+"&partner_id="+partner_id+"&game_id="+game_id+"&server_id="+server_id
				+"&ticket="+ticket+"&sign="+sign+"&format="+formart;
		String respStr = HttpClientUtils.get(loginUrl, data, 2000, 2000);
		if ( StringUtils.isNotBlank(respStr) ) {
			JSONObject resp  = JSONObject.parseObject(respStr);
			JSONObject json = resp.getJSONObject("data");
			if ( resp.containsKey("code") && 1 == resp.getInteger("code") ) {
				result.put("resultCode",0);
				result.put("errMsg","SUCCESS");
				result.put("adult", json.getInteger("adult"));	//0 未成年 ，1 成年
				onlineUser.setSid(json.getString("user_id"));
				onlineUser.setUserName(json.getString("nick_name"));
				JSONObject returnData = new JSONObject();
				onlineUser.setUid(onlineUser.getSid()+"&"+onlineUser.getChannelId());
				returnData.put("uid", onlineUser.getUid() );
				returnData.put("nick_name",json.getString("nick_name"));
				result.put("result", returnData);
			}else{
				log.info("iFeng login failure! return str="+respStr);
				result.put("errMsg", "用户验证失败！");
			}
		}else{
			result.put("errMsg", "数据为空或者请求失败！");
		}
		return result.toJSONString();
	}

	@Override
	public void parseParamter(String code, HttpServletRequest request, OnlinePay onlinePay) {
		for(Object key:request.getParameterMap().keySet()){
			respData.put(key.toString(), request.getParameter(key.toString()));
		}
		if(respData.size() > 0){
			onlinePay.setOrderId(respData.getString("extra"));
			onlinePay.setActualAmount(respData.containsKey("price")?respData.getDouble("price")*100+"":"0");
			onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
			onlinePay.setChannelOrderId(respData.getString("bill_no"));
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result, HttpServletResponse response) {
		if ( respData.containsKey("trade_status") && "TRADE_SUCCESS".equals(respData.getString("trade_status")) ){
			//Upper(MD5(partner_id+game_id+server_id+user_id+bill_no+price+trade_status+partner_key))
			String context = respData.getString("partner_id")
							+respData.getString("game_id")
							+respData.getString("server_id")
							+respData.getString("user_id")
							+respData.getString("bill_no")
							+respData.getString("price")
							+respData.getString("trade_status")
							+onlinePay.getPayInfoConfig().getAppkey();
			String sign = respData.getString("sign");
			String signServer = MD5Util.string2MD5(context).toUpperCase();
			log.info("iFeng signServer ="+signServer);
			log.info("iFeng sign ="+sign);
			if( sign.equals(signServer) ){
				result.put("code", 2000);
				return getReturnSuccess();
			}else{
				result.put("code", 4006);
				result.put("errMsg", "验证错误！");
				return getReturnFailure();
			}
		}else{
			result.put("code", 4007);
			result.put("errMsg", "支付失败！");
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
