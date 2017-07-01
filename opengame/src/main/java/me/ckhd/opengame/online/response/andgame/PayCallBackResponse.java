package me.ckhd.opengame.online.response.andgame;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;
import me.ckhd.opengame.online.util.XmlUtils;
@Service("AndGamePayCallBackResponse")
public class PayCallBackResponse extends BasePayCallBackResponse{

	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
	}

	@Override
	public boolean isSuccess() {
		boolean check = false;
		if("0".equals(onlinePay.getCallBackMap().get("hRet")==null?"0":onlinePay.getCallBackMap().get("hRet").toString())){
			check=true;
		}else{
			onlinePay.setErrMsg(onlinePay.getCallBackMap().get("status")==null?"":onlinePay.getCallBackMap().get("status").toString());
		}
		return check;
	}

	@Override
	public String getCallBackResult() {
		Map<String,Object> map = new HashMap<String, Object>();
		if(isSuccess()){
			map.put("hRet", "0");
		}else{
			map.put("hRet", "1");
		}
		return XmlUtils.toXml(map);
	}

	@Override
	public String getReturnSuccess() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("hRet", "0");
		return XmlUtils.toXml(map);
	}
}
