package me.ckhd.opengame.online.request;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseLoginCallBackRequest {
	
	Logger logger = LoggerFactory.getLogger(BaseLoginCallBackRequest.class);
	
	protected String callBackCode;
	protected HttpServletRequest httpServletRequest;
	protected Map<String,Object> map = new HashMap<String,Object>();
	
	public BaseLoginCallBackRequest(String _code,HttpServletRequest httpRequest) {
		this.callBackCode=_code;
		this.httpServletRequest=httpRequest;
	}
	
	/**
	 * 获取回调code
	 * @return
	 */
	public Map<String,Object> getCallBackCode(){
		return map;
	}
	
	public String getAppId(){
		return "";
	}
	
	
}
