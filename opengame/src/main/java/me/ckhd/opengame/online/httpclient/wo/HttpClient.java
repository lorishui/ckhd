package me.ckhd.opengame.online.httpclient.wo;

import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.wo.PayResponse;

public class HttpClient extends BaseHttpClient {

	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		return null;
	}

	@Override
	public BasePayResponse httpPayClient(BasePayRequest request) {
		return new PayResponse(request.onlinePay);
		
	}

	@Override
	public OnlineUser getUser(String returnCode, BaseLoginRequest request) {
		return null;
	}

	@Override
	public BasePayResponse httpPayValidateClient(BasePayRequest request) {
		return null;
	}

}
