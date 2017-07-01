package me.ckhd.opengame.online.response.andgame;

import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.request.BaseLoginCallBackRequest;
import me.ckhd.opengame.online.response.BaseLoginCallBackResponse;

public class LoginCallBackResponse extends BaseLoginCallBackResponse{

	public LoginCallBackResponse(BaseLoginCallBackRequest _baseLoginCallBackRequest) {
		super(_baseLoginCallBackRequest);
	}
	
	public void setUser() {
		if(map!=null && map.size()>0){
			OnlineUser user = new OnlineUser();
			user.setSid(map.containsKey("userId")?map.get("userId").toString():"");
		}
	}
	
	@Override
	public boolean isSuccess() {
		return true;
	}

	@Override
	public String getCallBackResult() {
		if(isSuccess()){
			return "Content-Type: text/plain; charset=UTF8\r Content-Length: xx\r \r0";
		}
		return "Content-Type: text/plain; charset=UTF8\r Content-Length: xx\r \r1";
	}

	@Override
	public String getReturnSuccess() {
		return "Content-Type: text/plain; charset=UTF8\r Content-Length: xx\r \r0";
	}
}
