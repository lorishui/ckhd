package me.ckhd.opengame.online.response.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.config.Global;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayResponse;

public class PayResponse extends BasePayResponse{

	public PayResponse(OnlinePay onlinePay) {
		super(onlinePay);
	}

	@Override
	public Map<String, Object> getResult() {
		if (!isSuccess()) {
			return result;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("payInfo", getPayInfo());
		result.put("result", resultMap);
		return result;
	}
	
	private String getPayInfo(){
		// 订单
		String orderInfo = getOrderInfo(onlinePay.getPayCodeConfig().getProductName(), onlinePay.getPayCodeConfig().getProductName(),String.valueOf((double)onlinePay.getPrices()/100));
		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		String payInfo = orderInfo + "&sign=\"" + sign + "\"&"+ getSignType();
		return payInfo;
	}
	
	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}	
	
	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {

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

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + onlinePay.getOrderId() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" +  Global.getConfig("ALIPAY_NOTIFYURL")+ "\"";

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
	
	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		String privateKey = "";
		if( onlinePay.getChannelId() != null &&  onlinePay.getChannelId().equals("200")  ){
			privateKey = onlinePay.getPayInfoConfig().getAppkey();
		}else{
			privateKey = Global.getConfig("ALIPAY_RSA_PRIVATE");
		}
		return SignUtils.sign(content, privateKey );
	}
	
	
	@Override
	public boolean isSuccess() {
		boolean flag=true;
		if(onlinePay!=null){
			result.put("resultCode", 0);
			result.put("errMsg", "");
		} else {
			result.put("resultCode", -1);
			result.put("errMsg", "未获取到支付信息");
			flag = false;
		}
		return flag;
	}
}
