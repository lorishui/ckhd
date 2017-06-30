package me.ckhd.opengame.online.request.vivo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.request.BasePayRequest;

public class PayRequest extends BasePayRequest {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	public PayRequest(OnlinePay _onlinePay) {
		super(_onlinePay);
	}

	@Override
	public Map<String, Object> getPayParam() {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("result", this.getsignature());
		para.put("sdkType",onlinePay.getSdkType());
		return para;
	}
	
	/**
	 * 签名信息
	 * @return
	 */
	public String getsignature() {
		Map<String, String> para = new HashMap<String, String>();
		if(!"2".equals(onlinePay.getSdkType())){
			para.put("cpId", onlinePay.getPayInfoConfig().getExInfoMap().get("CPID").toString());
			para.put("appId", onlinePay.getPayInfoConfig().getAppid());
			para.put("cpOrderNumber",onlinePay.getOrderId());
			para.put("notifyUrl", onlinePay.getPayInfoConfig().getNotifyUrl());
			para.put("orderTime", sdf.format(new Date()));
			para.put("orderAmount", onlinePay.getPrices()+"");
			para.put("orderTitle", onlinePay.getPayCodeConfig().getProductName());
			para.put("orderDesc", onlinePay.getPayCodeConfig().getProductName());
			para.put("extInfo", "");
			para.put("version", "1.0.0");
		}else{
			para.put("storeId", onlinePay.getPayInfoConfig().getExInfoMap().get("CPID").toString());
			para.put("appId", onlinePay.getPayInfoConfig().getAppid());
			para.put("storeOrder",onlinePay.getOrderId());
			para.put("notifyUrl", onlinePay.getPayInfoConfig().getNotifyUrl());
			para.put("orderTime", sdf.format(new Date()));
			para.put("orderAmount", String.format("%.2f",onlinePay.getPrices()*0.01));
			para.put("orderTitle", onlinePay.getPayCodeConfig().getProductName());
			para.put("orderDesc", onlinePay.getPayCodeConfig().getProductName());
			para.put("version", "1.0.0");
		}
    	String cpKey = onlinePay.getPayInfoConfig().getExInfoMap().get("CpKey")==null?"":onlinePay.getPayInfoConfig().getExInfoMap().get("CpKey").toString();
    	return VivoSignUtils.buildReq(para, cpKey);
		
	}
}
