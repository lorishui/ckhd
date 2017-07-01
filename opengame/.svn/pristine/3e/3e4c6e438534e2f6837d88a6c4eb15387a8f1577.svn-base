package me.ckhd.opengame.online.handle;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.config.Global;
import me.ckhd.opengame.common.utils.RSACoder;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.response.alipay.SignUtils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("alipay")
@Scope("prototype")
public class alipayHandle extends BaseHandle{

	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		return null;
	}

	@Override
	public String pay(OnlinePay onlinePay, JSONObject codeJson) {
		JSONObject result = new JSONObject();
		result.put("resultCode",0);
		result.put("errMsg","SUCCESS");
		JSONObject data = new JSONObject();
		data.put("orderId", onlinePay.getOrderId());
//		data.put("callBackUrl", onlinePay.getPayInfoConfig().getNotifyUrl());
		String notifyUrl = codeJson.getString("notifyUrl");
		data.put("callBackUrl", notifyUrl);
		onlinePay.getPayInfoConfig().setNotifyUrl(notifyUrl);
		data.put("payInfo", getPayInfo(onlinePay));
		result.put("result", data);
		return result.toJSONString();
	}
	
	/*
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(OnlinePay onlinePay,String subject, String body, String price) {

		// 签约合作者身份ID
		String orderInfo = "";
		if( onlinePay.getPayInfoConfig() != null && StringUtils.isNotBlank(onlinePay.getPayInfoConfig().getAppid()) ){
			// 签约合作者身份ID
			orderInfo = "partner=" + "\"" + onlinePay.getPayInfoConfig().getAppid() + "\"";
			// 签约卖家支付宝账号
			orderInfo += "&seller_id=" + "\"" + onlinePay.getPayInfoConfig().getExInfoMap().get("ALIPAY_SELLER") + "\"";
		}else{
			// 签约合作者身份ID
			orderInfo = "partner=" + "\"" + Global.getConfig("ALIPAY_PARTNER") + "\"";
			// 签约卖家支付宝账号
			orderInfo += "&seller_id=" + "\"" + Global.getConfig("ALIPAY_SELLER") + "\"";
		}
		
		if( !"200".equals(onlinePay.getChannelId()) && onlinePay.getPayInfoConfig() != null &&
			StringUtils.isBlank(onlinePay.getPayInfoConfig().getChannelId()) ){
			// 签约合作者身份ID
			orderInfo = "partner=" + "\"" + Global.getConfig("ALIPAY_PARTNER") + "\"";
			// 签约卖家支付宝账号
			orderInfo += "&seller_id=" + "\"" + Global.getConfig("ALIPAY_SELLER") + "\"";
		}

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + onlinePay.getOrderId() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		String notifyUrl = StringUtils.isNotBlank(onlinePay.getPayInfoConfig().getNotifyUrl())?onlinePay.getPayInfoConfig().getNotifyUrl():onlinePay.getPayInfoConfig().getNotifyUrl();
		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + notifyUrl + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
//		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}
	
	private String getPayInfo(OnlinePay onlinePay){
		// 订单
		String orderInfo = getOrderInfo(onlinePay,onlinePay.getPayCodeConfig().getProductName(), onlinePay.getPayCodeConfig().getProductName(),String.valueOf(((double)onlinePay.getPrices())/100));
		// 对订单做RSA 签名
		String sign = sign(onlinePay,orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		String payInfo = orderInfo + "&sign=\"" + sign + "\"&sign_type=\"RSA\"";
		return payInfo;
	}
	
	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(OnlinePay onlinePay,String content) {
		String privateKey = "";
		if( onlinePay.getPayInfoConfig() != null && StringUtils.isNotBlank(onlinePay.getPayInfoConfig().getAppid()) ){
			privateKey = onlinePay.getPayInfoConfig().getAppkey();
		}else{
			privateKey = Global.getConfig("ALIPAY_RSA_PRIVATE");
		}
		if( !"200".equals(onlinePay.getChannelId()) && onlinePay.getPayInfoConfig() != null &&
				StringUtils.isBlank(onlinePay.getPayInfoConfig().getChannelId()) ){
			privateKey = Global.getConfig("ALIPAY_RSA_PRIVATE");
		}
		return SignUtils.sign(content, privateKey );
	}
	
	//pay end
	
	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		try {
			for(Object key:request.getParameterMap().keySet()){
				respData.put((String) key, request.getParameter(key+""));
			}
			if(respData.size() > 0){
				onlinePay.setOrderId(respData.getString("out_trade_no"));
				onlinePay.setActualAmount(respData.containsKey("total_fee")?(respData.getDouble("total_fee")*100)+"":"0");
				onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
				onlinePay.setChannelOrderId(respData.getString("trade_no"));
			}
		} catch (Exception e) {
			log.error("coolpad callback 解析数据失败!", e);
		}
	}
	
	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		if("TRADE_SUCCESS".equals(respData.getString("trade_status")) || "TRADE_FINISHED".equals(respData.getString("trade_status"))){
			String signContext = getSignContext(respData);
			log.info("alipay call back sign context:"+signContext);
			String sign = respData.getString("sign");
			log.info("alipay call back sign :"+sign);
			String publicKey = Global.getConfig("ALIPAY_PUBLIC_KEY");
			if( onlinePay.getChannelId() != null &&  onlinePay.getChannelId().equals("200") ){
				publicKey = (String)onlinePay.getPayInfoConfig().getExInfoMap().get("ALIPAY_PUBLIC_KEY");
			}
			boolean is = false;
			try {
				is = RSACoder.verifySHA1(signContext, publicKey, sign);
				if( is ){
					result.put("code", 2000);
					return getReturnSuccess();
				}else{
					result.put("code", 4006);
					result.put("errMsg", "验证错误！");
					return getReturnFailure();
				}
			} catch (Exception e) {
				log.error("rsa verify error!", e);
				result.put("code", 4015);
				result.put("errMsg", "请求参数错误或配置参数错误");
			}
		}else{
			if("WAIT_BUYER_PAY".equals(onlinePay.getCallBackMap().get("WAIT_BUYER_PAY"))){
				result.put("code", 4013);
				result.put("errMsg", "交易创建，等待买家付款。");
			}else if("TRADE_CLOSED".equals(onlinePay.getCallBackMap().get("TRADE_CLOSED"))){
				result.put("code", 4014);
				result.put("errMsg", "交易关闭");
			}else{
				result.put("code", 4016);
				result.put("errMsg", "其他错误");
			}
			return getReturnFailure();
		}
		return getReturnFailure();
	}
	
	/**
	 * 获取key自然排序的
	 * @param map 加密的参数集
	 * @param appKey 加密密钥
	 * @return
	 */
	public static String getSignContext(Map<String,Object> map){
		if( map!=null && map.size()>0 ){
			Set<String> keySet = map.keySet();
			Object[] obj = keySet.toArray();
			Arrays.sort(obj);
			StringBuffer sb = new StringBuffer();
			for(int i=0; i<obj.length ; i++){
				if(!obj[i].equals("sign") && !obj[i].equals("sign_type")){
					sb.append(obj[i]).append("=").append(map.get(obj[i])+"&");
				}
			}
			sb.setLength(sb.length()-1);
			return sb.toString();
		}
		return "";
	}

	@Override
	public String getReturnSuccess() {
		return "success";
	}

	@Override
	public String getReturnFailure() {
		return "fail";
	}

}
