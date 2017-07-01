package me.ckhd.opengame.online.request.unionpay;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.ckhd.opengame.common.config.Global;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.request.BasePayRequest;

public class PayRequest extends BasePayRequest {
	Logger log = LoggerFactory.getLogger(PayRequest.class);

	public PayRequest(OnlinePay _onlinePay) {
		super(_onlinePay);
		getPayParam();
	}

	@Override
	public Map<String, Object> getPayParam() {
		
		Map<String, String> contentData = getParamMap();

		Map<String, String> submitFromData = SDKUtil.signData(contentData,SDKUtil.encoding_UTF8);//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		String requestAppUrl = SDKConfig.getConfig().getAppRequestUrl();//交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
//		String signature = "";//签名	signature	M	填写对报文摘要的签名
		Map<String, String> resmap = SDKUtil.submitUrl(submitFromData,requestAppUrl,SDKUtil.encoding_UTF8);
		
		String respCode = resmap.get("respCode");
		if(respCode != null && ("00").equals(respCode)){
			String tn = resmap.get("tn");
			onlinePay.setPrepayid(tn);
		}else{
			log.info("银联错误信息："+resmap.get("respMsg"));
		}
		return null;
	}

	private Map<String, String> getParamMap() {
		/**银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改**/
		String version = "5.0.0";//版本号	version	M	固定填写
		String encoding = "UTF-8";//编码方式	encoding 
		String signMethod = "01";//签名方法	signMethod	M	01-RSA 02-MD5(暂不支持)
		String txnType = "01";//交易类型	txnType	M	取值：01 
		String txnSubType = "01";//交易子类	txnSubType	
		String bizType = "000201";//产品类型	bizType	M
		String channelType = "07";//渠道类型	channelType	M
		/**商户接入参数*/	
		String merId = "";
		if( onlinePay.getPayInfoConfig() == null || StringUtils.isBlank(merId) ){
			merId = Global.getConfig("UNIONPAY_MERID");//商户代码	merId	M
		}else{
			merId = onlinePay.getPayInfoConfig().getAppid();
		}
		String accessType = "0";//接入类型，商户接入填0 ，不需修改（0：直连商户， 1： 收单机构 2：平台商户）
		String orderId = onlinePay.getOrderId();//orderId
		String txnTime = DateUtils.getDate("yyyyMMddHHmmss");//订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		String accType = "01";////账号类型 01：银行卡02：存折03：IC卡帐号类型(卡介质)
		/**处理子字段*/
		String encryptCertId = "";
		String path = "";
		if( onlinePay.getPayInfoConfig() != null && StringUtils.isNotBlank(path) ){
			path = (String)onlinePay.getPayInfoConfig().getExInfoMap().get("acpsdk.encryptCert.path");
			encryptCertId = CertUtil.getEncryptCertId(path);//证书ID	 certId	M	通过MPI插件获取
		}else{
			encryptCertId = CertUtil.getEncryptCertId();//证书ID	 certId	M	通过MPI插件获取
		}
		String txnAmt = onlinePay.getPrices()+"";//交易金额	txnAmt	M	交易单位为分
		String currencyCode = "156";//交易币种	currencyCode	M	默认为156
		//String accNo = SDKUtil.encryptPan(verifyInfo.get("accNo")!=null?verifyInfo.get("accNo").toString():"", "UTF-8"); //这里测试的时候使用的是测试卡号，正式环境请使用真实卡号
		//callBack/unionpay/1029/52
		String backUrl = Global.getConfig("UNIONPAY_NOTIFY_URL")+onlinePay.getCkAppId()+"/"+onlinePay.getChannelId();//回掉地址
		
		Map<String,String> contentData = new HashMap<String, String>();
		contentData.put("version",version);
		contentData.put("encoding",encoding);
		contentData.put("signMethod",signMethod);
		contentData.put("txnType",txnType);
		contentData.put("txnSubType",txnSubType);
		contentData.put("bizType",bizType);
		contentData.put("channelType",channelType);
		contentData.put("merId",merId);
		contentData.put("accessType",accessType);
		contentData.put("orderId",orderId);
		contentData.put("txnTime",txnTime);
		contentData.put("accType",accType);
		contentData.put("encryptCertId",encryptCertId);
		contentData.put("txnAmt",txnAmt);
		contentData.put("currencyCode",currencyCode);
		contentData.put("backUrl",backUrl);
		/*contentData.put("accNo", accNo);*/
		return contentData;
	}

}