package me.ckhd.opengame.online.httpclient;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class BaseHttpClient {
	 protected Logger log = LoggerFactory.getLogger(this.getClass());
	/**
	 * 客户端登陆渠道后台验证
	 * 用例：用户启动游戏，向渠道登陆（某些应用是自动登陆），渠道返回UserID，然后向OpenGame请求该接口<br/>
	 * 该接口向渠道服务器验证，验证通过后返回手机端通知登陆成功。
	 * @param request
	 * @return
	 */
	 public abstract BaseLoginResponse httpLoginClient(BaseLoginRequest request);
	
	/**
	 * 客户端调用服务端支付
	 * 用例:用户调起支付,
	 * ①如果需要服务端下单/预下单,则调用渠道服务器下单,然后将渠道订单号/预付单号返回客户端再次调用渠道服务端发起支付
	 * ②无需服务端下单,则直接返回创酷orderId和回调地址到客户端发起支付
	 * @param request
	 * @return
	 */
	 public abstract BasePayResponse httpPayClient(BasePayRequest request);
	
	
	/**
	 * 封装用户信息
	 * 用例:登陆后,封装用户信息
	 * @param returnCode
	 * @param request
	 * @return
	 */
	public OnlineUser getUser(String returnCode,BaseLoginRequest request){
		OnlineUser user = new OnlineUser();
		user.setChannelId(request.channelId);
		user.setCkAppId(request.ckAppId);
		user.setChannelUserContent(returnCode);
		user.setLoginParam(request.getParam());
		user.setVersion(request.version);
		user.setAppVerifyInfo(request.verifyInfo.toJSONString());
		user.setLoginType(request.loginType);
		if(StringUtils.isNotBlank(request.loginType) && "2".equals(request.loginType)){
			user.setSid(request.getParam().get("userId") == null ? "": request.getParam().get("userId").toString());
			user.setUserName(request.getParam().get("userName") == null ? "": request.getParam().get("userName").toString());
		}
		return user;
	}
	
	/**
	 * 支付验证,暂时未使用
	 * @param request
	 * @return
	 */
	public abstract BasePayResponse httpPayValidateClient(BasePayRequest request);
		
	/**
	 * 组装订单信息
	 * ①请求订单时,封装订单信息,将渠道返回的订单信息封装到对象中
	 * @param returnCode
	 * @param request
	 * @return
	 */
	public OnlinePay getPay(String returnCode, BasePayRequest request) {
		OnlinePay onlinePay = request.onlinePay;
		onlinePay.setChannelPayContent(returnCode);
		return onlinePay;
	}
}
