package me.ckhd.opengame.online.response.vivo;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.request.vivo.VivoSignUtils;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

public class PayCallBackResponse extends BasePayCallBackResponse{
	private static final String RESULT_STR = "resultCode=%s&resultMsg=%s";
	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
	}

	@Override
	public boolean isSuccess() {
		String cpKey = onlinePay.getPayInfoConfig().getExInfoMap().get("CpKey")==null?"":onlinePay.getPayInfoConfig().getExInfoMap().get("CpKey").toString(); 
		boolean check = false;
		check = VivoSignUtils.verifySignature(getBaseString(), cpKey);
		return check;
	}

	@Override
	public String getCallBackResult() {
		Map<String,String> map = new HashMap<String, String>();
		if(isSuccess()){
			map.put("return_code", "OK");
			map.put("return_msg", "成功");
		}else{
			map.put("return_code", "FAIL");
			map.put("return_msg", "解析失败");
		}
		return String.format(RESULT_STR, map.get("return_code"),map.get("return_msg"));
	}
	
	@Override
	public String getReturnSuccess() {
		return String.format(RESULT_STR, "OK","成功");
	}
	private Map<String, String> getBaseString() {
		Map<String, String> mapVivo = new HashMap<String, String>();
		Map<String, Object> resultMap = onlinePay.getCallBackMap();
		String respCode = resultMap.get("respCode")==null?"":resultMap.get("respCode").toString();
		if(respCode.equals("200") || respCode.equals("0000")){
			for(String key:resultMap.keySet()){
				if(!"signMethod".equals(key) && !"ckappid".equals(key) && !"channelid".equals(key)){
					mapVivo.put(key, resultMap.get(key)==null?null:resultMap.get(key).toString());
				}
			}
		}
		return mapVivo;
	}
}
