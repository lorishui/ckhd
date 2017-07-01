package me.ckhd.opengame.online.httpclient.gionee;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.util.Map;

import javax.management.openmbean.InvalidKeyException;

import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.gionee.LoginResponse;
import me.ckhd.opengame.online.response.gionee.PayResponse;
import me.ckhd.opengame.online.util.gionee.HttpUtils;
import me.ckhd.opengame.online.util.gionee.Order;
import me.ckhd.opengame.online.util.gionee.PayUtil;
import me.ckhd.opengame.online.util.gionee.VerifyUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClient extends BaseHttpClient {
	Logger logger = LoggerFactory.getLogger(HttpClient.class);
	private static final String PORT = "443";
	private static final String GOLOGINURL = "https://id.gionee.com:" + PORT
			+ "/account/verify.do";// 登陆接口地址
	private static final String PAYURL = "https://pay.gionee.com/order/create"; // 支付接口地址
	private static final String HOST = "id.gionee.com";
	private static final String URLSTR = "/account/verify.do";
	private String errMsg = "";
	static String apiKey = ""; // 替换成商户申请获取的APIKey
	static String secretKey = ""; // 替换成商户申请获取的SecretKey

	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		Map<String, Object> msgTemp = request.getParam();
		String loginType = request.loginType;
		if("2".equals(loginType)){
			return new LoginResponse(getUser("", request)); 
		}
		apiKey = msgTemp.get("apiKey") == null ? null : msgTemp.get("apiKey")
				.toString();
		secretKey = msgTemp.get("secretKey") == null ? null : msgTemp.get(
				"secretKey").toString();
		if (StringUtils.isBlank(apiKey)) {
			errMsg = "apiKey is empty!";
			return new LoginResponse(getUser("", request));
		}
		if (StringUtils.isBlank(secretKey)) {
			errMsg = "secretKey is empty!";
			return new LoginResponse(getUser("", request));
		}
		Map<String, String> result = VerifyUtils.verify(
				msgTemp.get("amigoToken").toString(), GOLOGINURL, HOST, PORT,
				secretKey, URLSTR, apiKey);
		String verifyStr = "";
		if ("0".equals(result.get("resultCode"))) {
			verifyStr = result.get("result");
		} else {
			errMsg = result.get("result");
		}
		return new LoginResponse(getUser(verifyStr, request));
	}

	@Override
	public OnlineUser getUser(String result, BaseLoginRequest request) {
		OnlineUser user = new OnlineUser();
		user.setChannelId(request.channelId);
		user.setCkAppId(request.ckAppId);
		user.setErrMsg(errMsg);
		if (!"2".equals(request.loginType)) {
			user.setChannelUserContent(result);
			user.setUserName(request.getParam().get("loginname") == null ? ""
					: request.getParam().get("loginname").toString());
		}else{
			user.setSid(request.getParam().get("userId") == null ? ""
					: request.getParam().get("userId").toString());
			user.setUserName(request.getParam().get("userName") == null ? ""
					: request.getParam().get("userName").toString());
		}
		user.setLoginParam(request.getParam());
		user.setVersion(request.version);
		user.setLoginType(request.loginType);
		user.setAppVerifyInfo(request.verifyInfo==null?"":request.verifyInfo.toJSONString());
		return user;
	}

	// -----------------------TODO---------------支付-------------------------------------------------------
	@Override
	public BasePayResponse httpPayClient(BasePayRequest request) {
		Map<String, Object> params = request.getPayParam();
		if("2".equals(params.get("sdkType").toString())){
			return new PayResponse(getPay("", request));
		}else {
			String orderId = params.get("orderId").toString();
			String playerId = params.get("appuserid").toString();
			String subject = params.get("waresname").toString();
			String apiKey = params.get("api_key").toString();
			String totalFee = params.get("price")==null?"0":params.get("price").toString();
			double price = Double.valueOf(totalFee)/100;
			String notifyURL = params.get("notifyURL").toString();
			String privateKey = params.get("private_key").toString();
			
			Order order = new Order(orderId,playerId ,subject ,apiKey
					, new BigDecimal(price+""),
					new BigDecimal(price+""), new Timestamp(
							System.currentTimeMillis()), null, notifyURL);
			String requestBody = null;
			try {
				logger.info("支付时发送给渠道的数据为:"+order.toString());
				requestBody = PayUtil.wrapCreateOrder(order, privateKey,"1");
			} catch (InvalidKeyException | NoSuchAlgorithmException
					| InvalidKeySpecException | SignatureException | IOException | java.security.InvalidKeyException e) {
				e.printStackTrace();
			}
			
			String response = null;
			try {
				response = HttpUtils.post(PAYURL, requestBody);
			} catch (Exception e) {
				e.printStackTrace();
				errMsg=(StringUtils.isBlank(e.getMessage())?"创建订单失败":e.getMessage());
			}
			return new PayResponse(getPay(response, request));
		}
	}

	@Override
	public BasePayResponse httpPayValidateClient(BasePayRequest request) {
		return null;
	}
}
