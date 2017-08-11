package me.ckhd.opengame.online.handle;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.utils.XmlUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.util.HttpClientUtils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("jifeng")
@Scope("prototype")
public class jifengHandle extends BaseHandle {
	static final String verifyUrl = "http://api.gfan.com/uc1/common/verify_token";

	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson, PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		JSONObject verifyInfo = codeJson.containsKey("verifyInfo") ? codeJson.getJSONObject("verifyInfo") : null;
		if (verifyInfo != null) {
			String token = verifyInfo.containsKey("token") ? verifyInfo.getString("token") : "";
			String data = "token=" + token;
			String resultStr = HttpClientUtils.get(verifyUrl, data, 2000, 2000);
			if (StringUtils.isNotBlank(resultStr)) {
				 JSONObject resp = JSONObject.parseObject(resultStr);
				if (result != null && resp.getInteger("resultCode") == 1) {
					setOnlineUser(onlineUser, codeJson);
					onlineUser.setSid(resp.containsKey("uid") ? resp.getString("uid") : "");
					onlineUser.setUserName(resp.containsKey("uid") ? resp.getString("uid") : "");
					returnLoginSuccess(result, onlineUser);
				} else {
					result.put("resultCode", 2013);
					result.put("errMsg", "机锋token过期!");
				}
			}
		} else {
			result.put("resultCode", 2012);
			result.put("errMsg", "缺少参数verifyInfo!");
		}
		return result.toJSONString();
	}

	@Override
	public void parseParamter(String code, HttpServletRequest request, OnlinePay onlinePay) {
		Map<String, Object> map = XmlUtils.Str2Map(code);
		for (String key : map.keySet()) {
			respData.put(key, map.get(key));
		}
		respData.put("sign", request.getParameter("sign"));
		respData.put("time", request.getParameter("time"));
		log.debug("sign="+request.getParameter("sign")+"&time="+request.getParameter("time"));
		onlinePay.setOrderId(respData.getString("order_id"));
		onlinePay.setActualAmount(respData.containsKey("cost") ? respData.getIntValue("cost") * 10 + "" : "-");
		onlinePay.setCallBackContent(respData.toJSONString());
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result, HttpServletResponse response) {
		String uid = (String) onlinePay.getPayInfoConfig().getExInfoMap().get("uid");
		String time = respData.getString("time");
		String sign = respData.getString("sign");
		String serverSign = CoderUtils.md5(uid + time, "utf-8");
		if (serverSign.equals(sign)) {
			result.put("code", 2000);
			return getReturnSuccess();
		} else {
			result.put("code", 4001);
			result.put("errMsg", "jifeng验证失败");
			return getReturnFailure();
		}
	}

	@Override
	public String getReturnSuccess() {
		return "<response><ErrorCode>1</ErrorCode><ErrorDesc>Success</ErrorDesc></response>";
	}

	@Override
	public String getReturnFailure() {
		return "<response><ErrorCode>0</ErrorCode><ErrorDesc>Fail</ErrorDesc></response>";
	}

}
