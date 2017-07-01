package me.ckhd.opengame.online.response.weixin;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.SignContext;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.util.XmlUtils;

public class PayResponse extends BasePayResponse {
	
	boolean isSuccess = false;
	public PayResponse(OnlinePay onlinePay) {
		super(onlinePay);
		result.put("resultCode", -1);
		if(onlinePay!=null && onlinePay.getChannelPayContent()!=null){
			payContent = XmlUtils.decodeXml(onlinePay.getChannelPayContent());
			//判断获取到渠道返回是否成功
			if ("SUCCESS".equals(payContent.get("return_code"))) {
				isSuccess = true;
				Map<String,Object> param = new HashMap<String, Object>();
				param.put("appid", onlinePay.getPayInfoConfig().getAppid());
				param.put("partnerid", onlinePay.getPayInfoConfig().getExInfoMap().get("mch_id").toString()); 
				param.put("prepayid",payContent.get("prepay_id"));
				param.put("package" , "Sign=WXPay");
				param.put("noncestr", getRandomStr());
				param.put("timestamp",System.currentTimeMillis()/1000);
				logger.info("signContent="+SignContext.getSignContext(param)+"&key="+onlinePay.getPayInfoConfig().getAppkey());
				String sign = CoderUtils.md5(SignContext.getSignContext(param)+"&key="+onlinePay.getPayInfoConfig().getAppkey(),"utf-8");
				param.put("sign", sign.toUpperCase());
				param.put("mch_id", onlinePay.getPayInfoConfig().getExInfoMap().get("mch_id"));
				result.put("resultCode", 0);
				result.put("errMsg", "");
				result.put("result", param);
				
			}else{
				result.put("errMsg", payContent.get("return_msg"));
			}
		}else{
			result.put("errMsg", "请求失败");
		}
	}

	@Override
	public Map<String, Object> getResult() {
		return result;
	}

	
	@Override
	public boolean isSuccess() {
		return isSuccess;
	}
	
	private String getRandomStr(){
		String str = "abcdefghijklmnopqrstuvwsyzABCDEFGHIJKLMNOPQRSTUVWSYZ1234567890";
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for(int i=0;i<16;i++){
			sb.append(str.charAt(random.nextInt(61)));
		}
		return sb.toString();
	}
	
}
