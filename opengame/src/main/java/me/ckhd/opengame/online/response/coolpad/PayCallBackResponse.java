package me.ckhd.opengame.online.response.coolpad;

import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.request.lenovo.sign.SignHelper;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayCallBackResponse extends BasePayCallBackResponse{
	
	Logger logger = LoggerFactory.getLogger(PayCallBackResponse.class);
	
	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
	}

	@Override
	public boolean isSuccess() {
		if(Integer.valueOf(onlinePay.getCallBackMap().get("result")==null?"1":onlinePay.getCallBackMap().get("result").toString())==0){
			if(!verify()){
				onlinePay.setErrMsg("回调验签失败");
				return false;
			}
			return true;
		}else{
			onlinePay.setErrMsg("支付失败");
			return false;
		}
	}

	
	public boolean verify(){
		try {
			String transdata = onlinePay.getHttpServletRequest().getParameter("transdata");
			String sign= onlinePay.getHttpServletRequest().getParameter("sign");
			if(StringUtils.isBlank(sign)){
				return false;
			}
			
			/*
			 * 调用验签接口
			 * 主要 目的 确定 收到的数据是我们 发的数据，是没有被非法改动的
			 */
			if (SignHelper.verify(transdata, sign,onlinePay.getPayInfoConfig().getExInfoMap().get("PLATP_KEY").toString())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public String getCallBackResult() {
		if(isSuccess()){
			return "SUCCESS";
		}else{
			return "FAILURE";
		}
	}

	@Override
	public String getReturnSuccess() {
		return "SUCCESS";
	}
}
