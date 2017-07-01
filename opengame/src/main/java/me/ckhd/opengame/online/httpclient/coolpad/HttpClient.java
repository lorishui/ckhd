package me.ckhd.opengame.online.httpclient.coolpad;

import java.util.Map;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.httpclient.lenovo.HttpUtils;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.request.coolpad.sign.SignHelper;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.coolpad.LoginResponse;
import me.ckhd.opengame.online.response.coolpad.PayResponse;
import me.ckhd.opengame.util.HttpClientUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class HttpClient extends BaseHttpClient {
	private static final String GOURL="https://openapi.coolyun.com/oauth2/token?grant_type=authorization_code";//接口地址
	private static final String GOPAYURL="http://pay.coolyun.com:6988/payapi/order";//支付预付接口地址
	Logger logger = LoggerFactory.getLogger(HttpClient.class);
	
	String errMsg="";
	String sign=""; 
	Map<String, Object> resultMap;
	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		Map<String, Object> map = request.getParam();
		StringBuilder param = new StringBuilder();
		param.append("client_id=");
		param.append(request.payInfoConfig.getAppid());
		param.append("&redirect_uri=");
		param.append(request.payInfoConfig.getAppkey());
		param.append("&client_secret=");
		param.append(request.payInfoConfig.getAppkey());
		param.append("&code=");
		param.append(map.get("code").toString());
		logger.info("登陆时发送给渠道的数据为:"+param.toString());
		String result =HttpClientUtils.get(GOURL, param.toString(),2000,2000);
		try {
			if(result!=null){
				resultMap = MyJsonUtils.jsonStr2Map(result);
			}else{
				errMsg="未获取到用户信息";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg="未获取到用户信息";
		}
		return new LoginResponse(getUser(result,request));
	}
	
	@Override
	public OnlineUser getUser(String result,BaseLoginRequest request) {
		OnlineUser user = new OnlineUser();
		if(resultMap!=null && resultMap.containsKey("error")){
			String errorMsg = resultMap.get("error_description").toString();
			errMsg=StringUtils.isBlank(errorMsg)?"登陆失败":errorMsg;
		}else if(resultMap!=null){
			user.setSid(resultMap.containsKey("openid")?"":resultMap.get("openid").toString());
		}
		user.setChannelUserContent(result);
		user.setChannelId(request.channelId);
		user.setCkAppId(request.ckAppId);
		user.setErrMsg(errMsg);
		user.setVersion(request.version);
		user.setAppVerifyInfo(request.verifyInfo.toJSONString());
		user.setLoginType(request.loginType);
		return user;
	}

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
		// TODO Auto-generated method stub
		return null;
	}
}
