package me.ckhd.opengame.online.response;

import java.util.Map;

import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.request.BaseLoginCallBackRequest;

public abstract class BaseLoginCallBackResponse {

	protected OnlineUser onlineUser;
	protected BaseLoginCallBackRequest baseLoginCallBackRequest;
	protected String errMsg;
	protected Map<String, Object> map;
	public BaseLoginCallBackResponse(BaseLoginCallBackRequest _baseLoginCallBackRequest) {
		this.baseLoginCallBackRequest=_baseLoginCallBackRequest;
		if(baseLoginCallBackRequest!=null){
			map = baseLoginCallBackRequest.getCallBackCode();
		}
	}
	
	//判断回调是否成功
	public abstract boolean isSuccess();
	
	//返回回调信息
	public abstract String getCallBackResult();
	
	//返回正确信息
	public abstract String getReturnSuccess();
	
	public OnlineUser getOnlineUser(){
		return onlineUser;
	}
}
