package me.ckhd.opengame.online.httpclient.lenovo;

import java.util.Map;

import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.request.lenovo.sign.SignHelper;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.lenovo.LoginResponse;
import me.ckhd.opengame.online.response.lenovo.PayResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;


public class HttpClient extends BaseHttpClient {
	Logger logger = LoggerFactory.getLogger(HttpClient.class);
	private static final String GOPAYURL="http://ipay.iapppay.com:9999/payapi/order";//支付预付接口地址
	private static final String GOLOGINURL="http://ipay.iapppay.com:9999/openid/openidcheck";//接口地址

	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		String respData="";
		if (StringUtils.isBlank(request.loginType) || "1".equals(request.loginType)) {
			Map<String, Object> map=request.getParam();
			String reqData = reqData(map.get("appid").toString(), map.get("loginToken").toString(),map.get("APPV_KEY").toString());
			logger.info("登陆时发送给渠道的数据为:"+reqData);
			respData = HttpUtils.sentPost(GOLOGINURL, reqData,"UTF-8"); // 请求验证服务端
		}else if (StringUtils.isNotBlank(request.loginType) && "2".equals(request.loginType)) {
			respData="";
		} 
		return  new LoginResponse(getUser(respData,request));
	}

	
	public static String reqData(String appid, String logintoken,final String appv_key) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("appid", appid);
		jsonObject.put("logintoken", logintoken);
		String content = jsonObject.toString();// 组装成 json格式数据
		String sign = SignHelper.sign(content, appv_key);// 调用签名函数
		String data = "transdata=" + content + "&sign=" + sign
				+ "&signtype=RSA";// 组装请求参数
		return data;
	}
	
	@Override
	public OnlineUser getUser(String result,BaseLoginRequest request) {
		OnlineUser user = new OnlineUser();
		user.setChannelId(request.channelId);
		user.setCkAppId(request.ckAppId);
		user.setChannelUserContent(result);
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
	
	
//-----------------------TODO---------------支付-------------------------------------------------------
	@Override
	public BasePayResponse httpPayClient(BasePayRequest request) {
		String reqData = reqData(request.getPayParam());
		logger.info("支付时发送给渠道的数据为:"+reqData);
		String respData = HttpUtils.sentPost(GOPAYURL, reqData,"UTF-8"); // 请求验证服务端
		return  new PayResponse(getPay(respData, request));
	}
	
	
	public static String reqData(Map<String, Object> params) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("appid", params.get("appid"));
		jsonObject.put("waresid", Integer.valueOf(params.get("waresid").toString()));
		jsonObject.put("cporderid", params.get("cporderid"));
		jsonObject.put("currency", "RMB");
		jsonObject.put("appuserid", params.get("appuserid"));
		String waresname= params.get("waresname")==null?"":params.get("waresname").toString();
		//以下是参数列表中的可选参数
		if(!waresname.isEmpty()){
			jsonObject.put("waresname", waresname);
		}
		/*
		 * 当使用的是 开放价格策略的时候 price的值是 程序自己 设定的价格，使用其他的计费策略的时候
		 * price 不用传值
		 * */ 
		jsonObject.put("price",Float.valueOf(params.get("price")==null?"1":params.get("price").toString()));
		String cpprivateinfo=(params.get("cpprivateinfo")==null?"":params.get("cpprivateinfo").toString());
		if(!cpprivateinfo.isEmpty()){
			jsonObject.put("cpprivateinfo", cpprivateinfo);
		}
		String notifyurl = (params.get("notifyurl")==null?"":params.get("notifyurl").toString());
		if(!notifyurl.isEmpty()){
			/*
			 * 如果此处不传同步地址，则是以后台传的为准。
			 * */
			jsonObject.put("notifyurl", notifyurl);
		}
		String content = jsonObject.toString();// 组装成 json格式数据
		// 调用签名函数      重点注意： 请一定要阅读  sdk 包中的爱贝AndroidSDK3.4.4\03-接入必看-服务端接口说明及范例\爱贝服务端接入指南及示例0311\IApppayCpSyncForJava \接入必看.txt 
		String sign = SignHelper.sign(content, params.get("APPV_KEY").toString());
		String data = "transdata=" + content + "&sign=" + sign+ "&signtype=RSA";// 组装请求参数
		return data;
	}
	
	

	@Override
	public BasePayResponse httpPayValidateClient(BasePayRequest request) {
		return null;
	}	
}
