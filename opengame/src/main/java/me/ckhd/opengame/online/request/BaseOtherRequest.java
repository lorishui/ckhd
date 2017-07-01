package me.ckhd.opengame.online.request;

import me.ckhd.opengame.online.entity.OnlinePay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseOtherRequest {
	
	protected Logger logger = LoggerFactory.getLogger(BaseOtherRequest.class);
	
	protected OnlinePay onlinePay;
	
	public BaseOtherRequest(OnlinePay _onlinePay) {
		this.onlinePay=_onlinePay;
	}
	
	public abstract String call();
	
}
