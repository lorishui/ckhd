package me.ckhd.opengame.online.response.meizu;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.SignContext;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

public class PayCallBackResponse extends BasePayCallBackResponse{

	Logger log = LoggerFactory.getLogger(PayCallBackResponse.class);
	boolean isSuccess = false;
	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
		Map<String,Object> map = onlinePay.getCallBackMap();
		if(map.get("trade_status")!=null && map.get("trade_status").toString().equals("3")){
			String sign = map.get("sign")!=null?map.get("sign").toString():"";
			map.remove("ckappid");
			map.remove("channelid");
			String signContent = SignContext.getMeizuPaySignContext(map);
			log.info("meizu callback sign content="+signContent);
			String signNew = CoderUtils.md5(signContent+":"+onlinePay.getPayInfoConfig().getAppkey(),"utf-8");
			if(sign.equals(signNew)){
				isSuccess = true;
			}
		}
	}

	@Override
	public boolean isSuccess() {
		return isSuccess;
	}

	@Override
	public String getCallBackResult() {
		JSONObject json = new JSONObject();
		if(isSuccess()){
			json.put("code", 200);
			json.put("message", "success");
			return json.toJSONString();
		}
		json.put("code", 900000);
		json.put("message", "未知异常");
		return json.toJSONString();
	}

	@Override
	public String getReturnSuccess() {
		return "{\"code\":200,\"message\":\"success\"}";
	}

}
