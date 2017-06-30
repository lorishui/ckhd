package me.ckhd.opengame.online.response.baidu;

import java.util.Map;

import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;
import me.ckhd.opengame.online.util.baidu.Sdk;

import com.alibaba.fastjson.JSONObject;

public class PayCallBackResponse extends BasePayCallBackResponse{

	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
	}

	@Override
	public boolean isSuccess() {
		boolean flag = false;
		String sign = "";
		Map<String, Object> resultMap = onlinePay.getCallBackMap();
		if(!"2".equals(onlinePay.getSdkType())){
			Object obj = resultMap.get("Context");
			if( obj != null ){
				String jsonData = Base64.decode(resultMap.get("Context").toString());
				JSONObject json = JSONObject.parseObject(jsonData);
				if(null != json.getInteger("OrderStatus") && json.getInteger("OrderStatus") == 1 ){
					sign = Sdk.md5(onlinePay.getPayInfoConfig().getAppid()+resultMap.get("OrderSerial")+resultMap.get("CooperatorOrderSerial")+resultMap.get("Context")+onlinePay.getPayInfoConfig().getExInfoMap().get("SECRETKEY"));
				}else{
					sign = "";
				}
			}
		}else{
			String appid = resultMap.get("appid")==null?"":resultMap.get("appid").toString();
			String orderid = resultMap.get("orderid")==null?"":resultMap.get("orderid").toString();
			String amount = resultMap.get("amount")==null?"":resultMap.get("amount").toString();
			String unit = resultMap.get("unit")==null?"":resultMap.get("unit").toString();
			String status = resultMap.get("status")==null?"":resultMap.get("status").toString();
			String paychannel = resultMap.get("paychannel")==null?"":resultMap.get("paychannel").toString();
			sign = Sdk.md5(appid+orderid+amount+unit+status+paychannel+onlinePay.getPayInfoConfig().getExInfoMap().get("SECRETKEY")).toLowerCase();
		}
		if(sign.equals(resultMap.get("sign")==null?"":resultMap.get("sign").toString().toLowerCase())){
			flag = true; 
		}
		return flag;
	}
	@Override
	public String getCallBackResult() {
		Map<String, Object> resultMap = onlinePay.getCallBackMap();
		if(isSuccess()){
			if( resultMap.containsKey("CooperatorOrderSerial") ){
				JSONObject json = new JSONObject();
				json.put("AppID", onlinePay.getPayInfoConfig().getAppid());
				json.put("ResultCode", 1);
				json.put("ResultMsg", "SUCCESS");
				json.put("Sign", CoderUtils.md5(onlinePay.getPayInfoConfig().getAppid()+"1"+onlinePay.getPayInfoConfig().getExInfoMap().get("SECRETKEY"), "utf-8"));
				json.put("Content", "");
				return json.toJSONString();
			}
			return "success";
		}else{
			JSONObject json = new JSONObject();
			if( resultMap.containsKey("CooperatorOrderSerial") ){
				json.put("AppID", onlinePay.getPayInfoConfig().getAppid());
				json.put("ResultCode", 0);
				json.put("ResultMsg", "FAILURE");
				json.put("Sign", CoderUtils.md5(onlinePay.getPayInfoConfig().getAppid()+"0"+onlinePay.getPayInfoConfig().getExInfoMap().get("SECRETKEY"), "utf-8"));
				json.put("Content", "");
				return json.toJSONString();
			}
			return "failure";
		}
	}

	@Override
	public String getReturnSuccess() {
		Map<String, Object> resultMap = onlinePay.getCallBackMap();
		if( resultMap.containsKey("CooperatorOrderSerial") ){
			JSONObject json = new JSONObject();
			json.put("AppID", onlinePay.getPayInfoConfig().getAppid());
			json.put("ResultCode", 1);
			json.put("ResultMsg", "SUCCESS");
			json.put("Sign", CoderUtils.md5(onlinePay.getPayInfoConfig().getAppid()+"1"+onlinePay.getPayInfoConfig().getExInfoMap().get("SECRETKEY"), "utf-8"));
			json.put("Content", "");
			return json.toJSONString();
		}
		return "success";
	}
}
