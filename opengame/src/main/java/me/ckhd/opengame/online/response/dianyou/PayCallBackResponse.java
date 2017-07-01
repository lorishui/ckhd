package me.ckhd.opengame.online.response.dianyou;

import java.util.Map;

import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.SignContext;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayCallBackResponse extends BasePayCallBackResponse{
	
	Logger log = LoggerFactory.getLogger(PayCallBackResponse.class);
	boolean isSuccess = false;
	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
		if(onlinePay.getCallBackMap().containsKey("st")&&onlinePay.getCallBackMap().get("st").toString().equals("1")){
			Map<String,Object> map = onlinePay.getCallBackMap();
			map.remove("ckappid");
			map.remove("channelid");
			String signContext = SignContext.getSignContext(onlinePay.getCallBackMap())+onlinePay.getPayInfoConfig().getExInfoMap().get("publicKey");
			log.info("sign context="+signContext);
			log.info("sign="+map.get("sign"));
			if(map.containsKey("sign") && map.get("sign").equals(getHexSting(CoderUtils.md5(signContext, "utf-8")))){
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
		if(isSuccess){
			return "SUCCESS";
		}
		return "FAILURE";
	}

	@Override
	public String getReturnSuccess() {
		return "SUCCESS";
	}
	
	/**
	 * 取十六进制子串
	 * @param str
	 * @return
	 */
	public static String getHexSting(String str){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			sb.append(Integer.toHexString(str.charAt(i)));
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(getHexSting("594e3431a0191d16b53e3fd15229b701"));
	}
}
