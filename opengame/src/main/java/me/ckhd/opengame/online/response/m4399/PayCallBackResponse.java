package me.ckhd.opengame.online.response.m4399;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

public class PayCallBackResponse extends BasePayCallBackResponse{
	
	Logger log = LoggerFactory.getLogger(PayCallBackResponse.class);
	boolean isSucces = false;
	int money = 0;
	int gamemoney = 0;
	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
		//$sign = md5($orderid . $uid . $money . $gamemoney . $serverid . $secrect . $mark . $roleid.$time); 
		Map<String,Object> map = onlinePay.getCallBackMap();
		money = Integer.parseInt(map.get("money").toString());
		gamemoney = Integer.parseInt(map.get("gamemoney").toString());
		String signContent = map.get("orderid")+""+map.get("uid")+map.get("money")+map.get("gamemoney")+(map.get("serverid")==null?"":map.get("serverid"))+
				onlinePay.getPayInfoConfig().getAppkey()+(map.get("mark")==null?"":map.get("mark").toString())+(map.get("roleid")==null?"":map.get("roleid").toString())+map.get("time");
		log.info("4399 callback sign content="+signContent);
		String sign = CoderUtils.md5(signContent, "utf-8");
		log.info("4399 callback sign new="+sign);
		log.info("4399 callback sign="+map.get("sign"));
		if(map.containsKey("sign") && map.get("sign").toString().equals(sign) && (money * 100 == _onlinePay.getPrices())){
			isSucces = true;
		}
	}

	@Override
	public boolean isSuccess() {
		return isSucces;
	}

	@Override
	public String getCallBackResult() {
		if(isSuccess()){
			return "{\"status\":2,\"code\":null,\"money\":\""+money+"\",\"gamemoney\":\""+gamemoney+"\",\"msg\":\"充值成功\"}";
		}
		return "{\"status\":1,\"code\":\"sign_error\",\"money\":\""+money+"\",\"gamemoney\":\""+gamemoney+"\",\"msg\":\"请求串的md5验证码错误\"}";
	}

	@Override
	public String getReturnSuccess() {
		return "{\"status\":2,\"code\":null,\"money\":\""+money+"\",\"gamemoney\":\""+gamemoney+"\",\"msg\":\"充值成功\"}";
	}

}
