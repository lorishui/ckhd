package me.ckhd.opengame.online.response.i4;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;

public class PayCallBackResponse extends BasePayCallBackResponse{
	
	Logger log = LoggerFactory.getLogger(PayCallBackResponse.class);
	boolean isSuccess = false;
	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
		String sign = _onlinePay.getCallBackMap().get("sign")!=null?onlinePay.getCallBackMap().get("sign").toString():"";
		log.info("i4 sign sign ="+sign);
		//解密Rsa
        Map<String,String> dataMap = getDecodeData(sign);
		if(onlinePay.getOrderId().equals(dataMap.get("billno")) && "0".equals(onlinePay.getCallBackMap().get("status").toString())
				&& onlinePay.getCallBackMap().get("status").equals(dataMap.get("status")) &&
				onlinePay.getCallBackMap().get("amount").equals(dataMap.get("amount"))){
			isSuccess = true;
		}
	}

	@Override
	public boolean isSuccess() {
		return isSuccess;
	}
	
	@Override
	public String getCallBackResult() {
		if(isSuccess){
			return "success";
		}
		return "fail";
	}

	@Override
	public String getReturnSuccess() {
		return "success";
	}
	
	private Map<String,String> getDecodeData(String sign){
		//解密Rsa
		BASE64Decoder base64Decoder = new BASE64Decoder();
		try{
			byte[] dcDataStr = base64Decoder.decodeBuffer(sign);
			byte[] plainData = RSADecrypt.decryptByPublicKey(dcDataStr, RSADecrypt.DEFAULT_PUBLIC_KEY); 
			String data = new String(plainData);
			return getMap(data);
		}catch(Exception e){
			log.error("i4 rsa error!", e);
		}
		return null;
	}
	
	private Map<String,String> getMap(String data){
		Map<String,String> map = new HashMap<String, String>();
		String[] arr = data.split("&");
		for(String str:arr){
			String[] sArr = str.split("=");
			if(sArr.length > 1){
				map.put(sArr[0], sArr[1]);
			}
		}
		return map;
	}
}
