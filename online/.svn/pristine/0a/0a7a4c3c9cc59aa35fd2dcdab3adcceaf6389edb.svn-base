package me.ckhd.opengame.online.request;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.app.entity.PayInfoConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BasePayCallBackRequest {
	Logger logger = LoggerFactory.getLogger(BasePayCallBackRequest.class);
	
	
	protected String callBackCode;
	protected HttpServletRequest httpServletRequest;
	protected Map<String,Object> map = new HashMap<String,Object>();
	protected PayInfoConfig payInfoConfig=null;
	public BasePayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		this.callBackCode=_code;
		this.httpServletRequest=httpRequest;
	}
	
	//获取订单Id,验证数据库此订单信息
	public abstract String getOrderId();
	/**
	 * 获取渠道实际支付金额
	 * @return
	 */
	public abstract String getActualAmount();
	
	/**
	 * 获取渠道订单号
	 * @return
	 */
	public abstract String getChannelOrderId();
	
	/**
	 * 获取回调code
	 * @return
	 */
	public Map<String,Object> getCallBackCode(){
		return map;
	}
	
	public void setMap(){
		
	}
	
	public void setPayInfoConfig(PayInfoConfig _payInfoConfig){
		this.payInfoConfig=_payInfoConfig;
		setMap();
	}
	
	/**
	 * 透传到CP设置的就回调地址
	 */
	public String redirect(String redirectUrl){
		logger.info("无订单信息时,默认不做后续处理");
		return "";
	}
}
