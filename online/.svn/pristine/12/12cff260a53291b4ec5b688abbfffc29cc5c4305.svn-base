package me.ckhd.opengame.online.response.gionee;

import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;
import me.ckhd.opengame.online.util.gionee.RSASignature;

import org.apache.commons.codec.CharEncoding;

public class PayCallBackResponse extends BasePayCallBackResponse{

	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
	}

	@Override
	public boolean isSuccess() {
		boolean check = false;
		String sign = onlinePay.getCallBackMap().get("sign")==null?null:onlinePay.getCallBackMap().get("sign").toString();
		String content = onlinePay.getCallBackMap().get("content")==null?null:onlinePay.getCallBackMap().get("content").toString();
		String public_key = onlinePay.getPayInfoConfig().getExInfoMap().get("publicKey")==null?"":onlinePay.getPayInfoConfig().getExInfoMap().get("publicKey").toString();
		if (StringUtils.isBlank(sign)) {
			return check;
		}

		/****************************** 签名验证 *******************************************/
		boolean isValid = false;
		try {
			isValid = RSASignature.doCheck(content, sign, public_key, CharEncoding.UTF_8);
		} catch (Exception e) {
			return check; 
		}

		if (isValid) {
			check=true;
			return check;
		}
		return check;
	}

	@Override
	public String getCallBackResult() {
		if(isSuccess()){
			return "success";
		}else{
			return "fail";
		}
	}

	@Override
	public String getReturnSuccess() {
		return "success";
	}
}
