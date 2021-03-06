package me.ckhd.opengame.online.handle;

import java.net.URLEncoder;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.util.zhangyue.DesUtils;
import me.ckhd.opengame.util.HttpClientUtils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("zhangyue")
@Scope("prototype")
public class zhangyueHandle extends BaseHandle {
	
	private final String login_url = "http://gamerh.ireader.com.cn/token/verify";
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson, PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		String access_token = verifyInfo.getString("accessToken");
		String openuid = verifyInfo.getString("openUid");
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		String timeStamp = ts.toString();
		JSONObject jo = new JSONObject();
		jo.put("access_token", access_token);
		jo.put("timestamp", timeStamp);
		String dataStr = "";
		try {
			dataStr = DesUtils.createSign(jo.toJSONString(), payInfo.getExInfoMap().get("desKey")+"");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("加密参数失败");
		}
		String dataStrEncoded = null;
		try {
		    dataStrEncoded = URLEncoder.encode(dataStr, "utf-8");
		} catch (Throwable e) {
		    log.error("zhangyue urlencode failure!", e);
		}
		StringBuilder param = new StringBuilder();
		param.append("data=").append(dataStrEncoded).append("&gameid=")
			.append(payInfo.getAppid()).append("&openuid=").append(openuid);
		log.info("登陆时发送给渠道的数据为:"+param.toString());
		String resposneData = HttpClientUtils.get(login_url, param.toString(), 10000, 10000);
		if( StringUtils.isNotBlank(resposneData) ){
			JSONObject resposneJson = JSONObject.parseObject(resposneData);
			if(StringUtils.isNotBlank(resposneJson.getString("status")) ){
				JSONObject status = resposneJson.getJSONObject("status");
				if("0".equals(status.getString("code"))){
					JSONObject data = resposneJson.getJSONObject("data");
					if(StringUtils.isNotBlank(data.getString("open_uid"))){
						String open_uid = data.getString("open_uid");
						onlineUser.setSid(open_uid);
						returnLoginSuccess(result, onlineUser);
					}
				}else if("5000".equals(status.getString("code"))){
					result.put("errMsg", "服务器内部错误");
				}else if("5001".equals(status.getString("code"))){
					result.put("errMsg", "无效的DES密钥");
				}else if("5002".equals(status.getString("code"))){
					result.put("errMsg", "授权码无效");
				}else if("5003".equals(status.getString("code"))){
					result.put("errMsg", "无效的openuid");
				}else if("5004".equals(status.getString("code"))){
					result.put("errMsg", "授权码过期");
				}
				result.put("code", status.getString("code"));
			}
		}else{
			result.put("errMsg", "数据为空或者请求失败！");
		}
		return result.toJSONString();
	}
	
	
	/**
	 * zhangyue回调需写成http://ol.haifurong.cn/netpay/callback/zhangyue/1.2.0/?ckAppId=XXXX
	 */
	@Override
	public void parseParamter(String code, HttpServletRequest request, OnlinePay onlinePay) {
		String DESkey = onlinePay.getPayInfoConfig().getExInfoMap().get("desKey")+"";
		String data = request.getParameter("data");
		String parseSign = "";
		try {
			parseSign = DesUtils.parseSign(data, DESkey);
		} catch (Exception e) {
			log.error("zhangyue回调解析错误");
		}
		respData = JSONObject.parseObject(parseSign);
		onlinePay.setOrderId(respData.getString("mer_orderid"));
		onlinePay.setChannelId(respData.getString("orderid"));
		onlinePay.setActualAmount(Integer.parseInt(respData.getString("amount"))*100+"");
		onlinePay.setCallBackContent(respData.toJSONString());
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result, HttpServletResponse response) {
		String amount = Integer.parseInt(respData.getString("amount"))*100+"";
		String price = onlinePay.getPrices()+"";
		if(price.equals(amount)){
			result.put("code", 2000);
			return getReturnSuccess();
		}else{
			result.put("code", 4000);
			result.put("errMsg", "verifyFailed");
			return getReturnFailure();
		}
	}

	@Override
	public String getReturnSuccess() {
		JSONObject jo = new JSONObject();
		jo.put("code", "0");
		jo.put("message", "success");
		return jo.toJSONString();
	}

	@Override
	public String getReturnFailure() {
		JSONObject jo = new JSONObject();
		jo.put("code", "0");
		jo.put("message", "verifyFail");
		return null;
	}

}
