package me.ckhd.opengame.online.httpclient.mm;

import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.mm.LoginResponse;
import me.ckhd.opengame.online.response.mm.PayResponse;

public class HttpClient extends BaseHttpClient {

	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		return new LoginResponse(getUser("", request));
	}

	@Override
	public BasePayResponse httpPayClient(BasePayRequest request) {
		return new PayResponse(request.onlinePay);
	}

	@Override
	public BasePayResponse httpPayValidateClient(BasePayRequest request) {
		return null;
	}

}
