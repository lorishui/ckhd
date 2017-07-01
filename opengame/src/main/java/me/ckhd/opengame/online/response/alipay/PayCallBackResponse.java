package me.ckhd.opengame.online.response.alipay;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import me.ckhd.opengame.common.config.Global;
import me.ckhd.opengame.common.utils.RSACoder;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayCallBackResponse extends BasePayCallBackResponse{
	Logger log = LoggerFactory.getLogger(PayCallBackResponse.class);

	boolean isSuccess = false;
	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
		Map<String,Object> map = onlinePay.getCallBackMap();
		if("TRADE_SUCCESS".equals(onlinePay.getCallBackMap().get("trade_status")) || "TRADE_FINISHED".equals(onlinePay.getCallBackMap().get("trade_status"))){
			String signContext = getSignContext(map);
			log.info("alipay call back sign context:"+signContext);
			String sign = (String)map.get("sign");
			log.info("alipay call back sign :"+sign);
			String publicKey = Global.getConfig("ALIPAY_PUBLIC_KEY");
			if( _onlinePay.getPayInfoConfig() != null && StringUtils.isNotBlank(_onlinePay.getPayInfoConfig().getAppid()) ){
				publicKey = (String)_onlinePay.getPayInfoConfig().getExInfoMap().get("ALIPAY_PUBLIC_KEY");
			}
			boolean is = false;
			try {
				is = RSACoder.verifySHA1(signContext, publicKey, sign);
			} catch (Exception e) {
				log.error("rsa verify error!", e);
			}
			if(is){
				isSuccess = true;
			}else{
				errMsg = "验签失败";
			}
		}else{
			if("WAIT_BUYER_PAY".equals(onlinePay.getCallBackMap().get("WAIT_BUYER_PAY"))){
				errMsg="交易创建，等待买家付款。";
			}else if("TRADE_CLOSED".equals(onlinePay.getCallBackMap().get("TRADE_CLOSED"))){
				errMsg="交易关闭";
			}
			onlinePay.setErrMsg(errMsg);
		}
	}

	@Override
	public boolean isSuccess() {
		return isSuccess;
	}

	@Override
	public String getCallBackResult() {
		if(isSuccess()){
			return "success";
		}
		return "fail";
	}

	@Override
	public String getReturnSuccess() {
		return "success";
	}
	
	/**
	 * 获取key自然排序的
	 * @param map 加密的参数集
	 * @param appKey 加密密钥
	 * @return
	 */
	public static String getSignContext(Map<String,Object> map){
		if( map!=null && map.size()>0 ){
			Set<String> keySet = map.keySet();
			Object[] obj = keySet.toArray();
			Arrays.sort(obj);
			StringBuffer sb = new StringBuffer();
			for(int i=0; i<obj.length ; i++){
				if(!obj[i].equals("sign") && !obj[i].equals("sign_type")){
					sb.append(obj[i]).append("=").append(map.get(obj[i])+"&");
				}
			}
			sb.setLength(sb.length()-1);
			return sb.toString();
		}
		return "";
	}
}