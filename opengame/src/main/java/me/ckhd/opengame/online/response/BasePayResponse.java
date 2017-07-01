package me.ckhd.opengame.online.response;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.ckhd.opengame.online.entity.OnlinePay;

public abstract class BasePayResponse {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected Map<String, Object> result = new HashMap<String, Object>();
	protected Map<String, Object> payContent = new HashMap<String, Object>();
	protected OnlinePay onlinePay;
	boolean isSuccess = true;

	// iccid
	protected String simNo;
	
	public BasePayResponse(OnlinePay _onlinePay) {
		this.onlinePay = _onlinePay;
	}
	
	public abstract boolean isSuccess();
	
	public abstract Map<String, Object> getResult();

	public String getSimNo() {
		return simNo;
	}

	public void setSimNo(String simNo) {
		this.simNo = simNo;
	}
	
}
