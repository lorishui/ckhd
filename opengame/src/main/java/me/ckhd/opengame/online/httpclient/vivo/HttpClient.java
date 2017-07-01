package me.ckhd.opengame.online.httpclient.vivo;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.vivo.LoginResponse;
import me.ckhd.opengame.online.response.vivo.PayResponse;
import me.ckhd.opengame.util.HttpClientUtils;

public class HttpClient extends BaseHttpClient {
	Logger logger = LoggerFactory.getLogger(HttpClient.class);
	private static final String GOURL="https://usrsys.inner.bbk.com/auth/user/info";//接口地址
	private static final String GOPAYURL="https://pay.vivo.com.cn/vcoin/trade";//网游支付预付接口地址
	private static final String PAYURL="https://pay.vivo.com.cn/vivoPay/getVivoOrderNum";//单机支付预付接口地址
	
	String errMsg="";
	String uid=""; 
	String orderNumber=""; 
	String accessKey=""; 
	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		Map<String, Object> map = request.getParam();
		if(StringUtils.isNotBlank(request.loginType) && "2".equals(request.loginType)){
			return new LoginResponse(getUser("", request)); 
		}else {
			String param = "access_token="+map.get("access_token")+"";
			logger.info(String.format("登录请求参数:[%s]",param));
			String result=HttpClientUtils.postVivo(GOURL, param, 2000, 2000);
			logger.info(String.format("登录返回的参数:[%s]",result));
			if(StringUtils.isNotEmpty(result)){
				Map<String, Object> resultMap = MyJsonUtils.jsonStr2Map(result);
				String stat = resultMap.get("stat")==null?"":resultMap.get("stat").toString();
				if(stat.equals("440")){
					errMsg = "请求参数错误";
				}else if(stat.equals("441")){
					errMsg = "非法：无对应用户、过期或失效";
				}else if(stat.equals("442")){
					errMsg = "未知错误";
				}else{
					uid = resultMap.get("uid")==null?"":resultMap.get("uid").toString();
				}
			}else{
				errMsg="未获取到用户信息";
			}
			return new LoginResponse(getUser(result,request));
		}
	}

	
	@Override
	public OnlineUser getUser(String result,BaseLoginRequest request) {
		OnlineUser user = new OnlineUser();
		if(StringUtils.isNotBlank(request.loginType) && "2".equals(request.loginType)){
			user.setSid(request.getParam().get("userId") == null ? ""
					: request.getParam().get("userId").toString());
			user.setUserName(request.getParam().get("userName") == null ? ""
					: request.getParam().get("userName").toString());
		}else if(StringUtils.isBlank(request.loginType) || "1".equals(request.loginType)){
			user.setSid(uid);
		}
		user.setChannelId(request.channelId);
		user.setCkAppId(request.ckAppId);
		user.setErrMsg(errMsg);
		user.setChannelUserContent(result);
		user.setVersion(request.version);
		user.setAppVerifyInfo(request.verifyInfo.toJSONString());
		return user;
	}

	
	@Override
	public BasePayResponse httpPayClient(BasePayRequest request) {
		String result = "";
		Map<String, Object> param = request.getPayParam();
		String paramVivo = param.get("result")==null?"":param.get("result").toString();
		logger.info(String.format("支付时发送给渠道的数据为:[%s]",paramVivo));
		if("2".equals(param.get("sdkType")==null?"":param.get("sdkType").toString())){
			result=HttpClientUtils.postVivo(PAYURL, paramVivo, 2000, 2000);
		}else {
			result=HttpClientUtils.postVivo(GOPAYURL, paramVivo, 2000, 2000);
		}
		logger.info(String.format("支付返回的数据为:[%s]",result));
		if(StringUtils.isNotEmpty(result)){
			Map<String, Object> resultMap = MyJsonUtils.jsonStr2Map(result);
			String respCode = resultMap.get("respCode")==null?"":resultMap.get("respCode").toString();
			if(respCode.equals("200")){
				if("2".equals(param.get("sdkType")==null?"":param.get("sdkType").toString())){
					orderNumber = resultMap.get("vivoOrder")==null?"":resultMap.get("vivoOrder").toString();
					accessKey = resultMap.get("vivoSignature")==null?"":resultMap.get("vivoSignature").toString();
				}else {
					orderNumber = resultMap.get("orderNumber")==null?"":resultMap.get("orderNumber").toString();
					accessKey = resultMap.get("accessKey")==null?"":resultMap.get("accessKey").toString();
				}
			}else if(respCode.equals("400")){
				errMsg = "参数为空或者格式不正确";
			}else if(respCode.equals("402")){
				errMsg = "商户id非法，请检查";
			}else if(respCode.equals("403")){
				errMsg = "验签失败，请检查";
			}else if(respCode.equals("405")){
				errMsg = "cp订单号不唯一";
			}else if(respCode.equals("406")){
				errMsg = "appId非法，请检查";
			}else if(respCode.equals("500")){
				errMsg = "服务器内部错误，请稍后再试";
			}
		}
		return  new PayResponse(getPay(result, request));
	}
	@Override
	public OnlinePay getPay(String returnCode, BasePayRequest request) {
		OnlinePay onlinePay = request.onlinePay;
		onlinePay.setChannelPayContent(returnCode);
		onlinePay.setPrepayid(orderNumber);
		onlinePay.setAccessKey(accessKey);
		onlinePay.setErrMsg(errMsg);
		return onlinePay;
	}
	
	@Override
	public BasePayResponse httpPayValidateClient(BasePayRequest request) {
		return null;
	}

}
