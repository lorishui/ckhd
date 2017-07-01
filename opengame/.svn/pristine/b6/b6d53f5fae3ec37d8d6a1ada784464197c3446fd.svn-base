package me.ckhd.opengame.online.request.tencent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.app.entity.PayCodeConfig;
import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.Encodes;
import me.ckhd.opengame.common.utils.SignContext;
import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.TencentCallbackData;
import me.ckhd.opengame.online.request.BaseOtherRequest;
import me.ckhd.opengame.online.sendOrder.task.OrderSenderBoot;
import me.ckhd.opengame.online.service.OnlinePayService;
import me.ckhd.opengame.online.service.OnlineService;
import me.ckhd.opengame.online.service.TencentCallbackDataService;
import me.ckhd.opengame.online.task.ExecutorServiceUtils;
import me.ckhd.opengame.online.util.OrderStatus;
import me.ckhd.opengame.sys.utils.DictUtils;
import me.ckhd.opengame.util.SnsNetwork;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

public class OtherRequest extends BaseOtherRequest{
	
	private TencentCallbackDataService tencentCallbackDataService = null;
	
	private OnlinePayService onlinePayService = null;
	
	private OnlineService onlineService = null;
	
//	public String getBalanceUrl = "https://ysdk.qq.com/mpay/get_balance_m";
	public String getBalanceUrl = "https://ysdktest.qq.com/mpay/get_balance_m";//test
//	public String cancelPayUrl = "https://ysdk.qq.com/mpay/cancel_pay_m";//test
	public String cancelPayUrl = "https://ysdktest.qq.com/mpay/cancel_pay_m";//test
//	public String payUrl = "https://ysdk.qq.com/mpay/pay_m";//test
	public String payUrl = "https://ysdktest.qq.com/mpay/pay_m";//test
//	public String presentUrl = "https://ysdk.qq.com/mpay/present_m";//test
	public String presentUrl = "https://ysdktest.qq.com/mpay/present_m";//test	
//	public String buygoodsUrl = "https://ysdk.qq.com/mpay/buy_goods_m";
	public String buygoodsUrl = "https://ysdktest.qq.com/mpay/buy_goods_m";
	
	public String key = "";
	
	public OtherRequest(OnlinePay _onlinePay) {
		super(_onlinePay);
		Map<String,Object> map = _onlinePay.getPayMap();
		if (map != null
				&& "sandbox".equalsIgnoreCase((String) map.get("environment"))) {
			key = (String) _onlinePay.getPayInfoConfig().getExInfoMap().get("tAppKey");
		} else {
			getBalanceUrl = getBalanceUrl.replace("ysdktest", "ysdk");
			cancelPayUrl = cancelPayUrl.replace("ysdktest", "ysdk");
			payUrl = payUrl.replace("ysdktest", "ysdk");
			presentUrl = presentUrl.replace("ysdktest", "ysdk");
			key = (String) _onlinePay.getPayInfoConfig().getExInfoMap().get("mAppKey");
		}
		String data = JSONObject.toJSONString(_onlinePay.getPayMap());
		if( onlinePayService == null ){
			onlinePayService = SpringContextHolder.getBean(OnlinePayService.class);
		}
		if( tencentCallbackDataService == null ){
			tencentCallbackDataService = SpringContextHolder.getBean(TencentCallbackDataService.class);
		}
		if( onlineService == null){
			onlineService = SpringContextHolder.getBean(OnlineService.class);
		}
		if(_onlinePay.getPayMap()!= null && _onlinePay.getPayMap().get("isResend") == null){
			TencentCallbackData ten = new TencentCallbackData();
			ten.setData(data);
			ten.setOrderId(onlinePay.getOrderId());
			ten.setPrice(onlinePay.getPrices());
			ten.setStatus(0);
			tencentCallbackDataService.save(ten);
		}
	}
	
	public String call(){
		//operateType
		if( onlinePay.getPayMap() == null || onlinePay.getPayMap().get("operate") == null){
			return "";
		}else if(onlinePay.getPayMap().get("operate").toString().equals("1")){
			return get_balance_m();
		}else if(onlinePay.getPayMap().get("operate").toString().equals("2")){
			return pay_m();
		}else if(onlinePay.getPayMap().get("operate").toString().equals("3")){
			return cancel_pay_m();
		}else if(onlinePay.getPayMap().get("operate").toString().equals("4")){
			return present_m();
		}else if(onlinePay.getPayMap().get("operate").toString().equals("5")){
			ExecutorServiceUtils.execute(this);
			return "{\"resultCode\":0}";
		}
		return "";
	}
	
	public void getDataByTenCent(){
		String balance = get_balance_m();
		int i = onlinePay.getPayMap().get("sendNum")!=null?Integer.parseInt(onlinePay.getPayMap().get("sendNum").toString()):0;
		String rate = DictUtils.getDictValue(onlinePay.getCkAppId(), "tencent_rate", "10");
		int rateVlue = StringUtils.isNotBlank(rate)?Integer.parseInt(rate):10;
		int amt = onlinePay.getPrices()/rateVlue;
		JSONObject json = JSONObject.parseObject(balance);
		logger.info("tencent get_balance_m json="+balance);
		if(json.getInteger("resultCode") == 0){
			JSONObject data = JSONObject.parseObject(json.getString("data"));
			int total = data.getInteger("balance");
			if( amt <= total ){
				String payInfo = pay_m();
				JSONObject pay = JSONObject.parseObject(payInfo);
				logger.info("tencent pay_m payInfo="+payInfo);
				if(pay.getInteger("resultCode") == 0){
					JSONObject payData = JSONObject.parseObject(pay.getString("data"));
					
					TencentCallbackData ten = new TencentCallbackData();
					ten.setOrderId(onlinePay.getOrderId());
					ten.setSendNum(i+1);
					ten.setStatus(1);
					tencentCallbackDataService.update(ten);
					
					String billno = payData.getString("billno");
					onlinePay.setOrderStatus(OrderStatus.PAY_SUCCESS);
					////通知cp参数设置
					onlinePay.setSendNum(0);
					onlinePay.setSendStatus(OrderStatus.SEND_DOWN_ING);
					onlinePay.setErrMsg("");
					onlinePay.setActualAmount(onlinePay.getPrices()+"");
					onlinePay.setChannelOrderId(billno);
					
					Map<String, Object> map= new HashMap<String, Object>();
					map.put("orderId", onlinePay.getOrderId());
					map.put("uid", onlinePay.getUserId());
					map.put("prices", onlinePay.getActualAmount());
					map.put("productId", onlinePay.getProductId());
					
					PayCodeConfig payCode = getPayCodeConfig(onlinePay);
					map.put("productName", payCode.getProductName());
					map.put("channelId", onlinePay.getChannelId());
					map.put("gameId", onlinePay.getCkAppId());
					map.put("create_time", onlinePay.getCreateDate().getTime());
					map.put("attach", onlinePay.getExtension());
					if(StringUtils.isNotBlank(onlinePay.getSercetKey())){
						map.put("sign", Encodes.string2MD5(onlinePay.getSercetStr()));
					}
					onlinePay.setSendNum(Integer.valueOf(map.get("sendNum")==null?"0":map.get("sendNum").toString()));
					onlinePay.setOrderStatus(OrderStatus.PAY_SUCCESS);
					onlinePay.setSendStatus(OrderStatus.SEND_DOWN_ING);
					onlinePay.setErrMsg("");
					onlinePay.setContent(JSONObject.toJSONString(map));
					
					onlinePayService.save(onlinePay);
					
					OrderSenderBoot.getInstance().add(onlinePay);
					return;
				}
			}
		}
		TencentCallbackData ten = new TencentCallbackData();
		ten.setOrderId(onlinePay.getOrderId());
		ten.setSendNum(i+1);
		ten.setStatus(0);
		tencentCallbackDataService.update(ten);
	}
	
	/**
	 * 腾讯余额查询
	 */
	private String get_balance_m(){
		JSONObject json = new JSONObject();
		json.put("resultCode", -1);
		json.put("resultMsg", "failure");
		try {
			//cookie
			Map<String,String> cookie = getCookie("/mpay/get_balance_m");
			//param
			Map<String,String> param = getParam();

			String header = "GET&"+URLEncoder.encode("/v3/r/mpay/get_balance_m", "utf-8")+"&";
			String sigContent = SignContext.getSignContextUrlEncode(param, header, "&");
			logger.info("tencent get_balance_m sigContent="+sigContent);
			String sig = CoderUtils.makeSig(sigContent,key+"&");
			param.put("sig", URLEncoder.encode(sig.replace("\r", "").replace("\n", ""),"utf-8").replace("+", "%2B").replace("*", "%2A"));
			logger.info("tencent get_balance_m sig="+param.get("sig"));
			String url = getBalanceUrl+"?" +SignContext.getSignContext(param);
			String result = SnsNetwork.getRequest(url, (HashMap<String, String>)cookie, "https");
			//返回格式：{"ret":0,"balance":200,"gen_balance":0, "first_save":1,”save_amt”:200}
			if(result != null ){
				logger.info(" get_balance_m result="+result);
				JSONObject rdata = JSONObject.parseObject(result);
				if( rdata != null && rdata.getInteger("ret") == 0){
					json.put("resultCode", 0);
					json.put("resultMsg", "success");
					JSONObject jsonResp = new JSONObject();
					jsonResp.put("balance", rdata.get("balance"));
					jsonResp.put("gen_balance", rdata.get("gen_balance"));
					jsonResp.put("first_save", rdata.get("first_save"));
					jsonResp.put("save_amt", rdata.get("save_amt"));
					json.put("data", jsonResp);
				}else{
					json.put("resultMsg", rdata.get("msg"));
				}
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("tencent get_balance_interface error", e);
		}
		return json.toJSONString();
	}
	
	/**
	 * 退款接口
	 * @return
	 */
	private String cancel_pay_m(){
		JSONObject json = new JSONObject();
		json.put("resultCode", -1);
		json.put("resultMsg", "failure");
		try{
			//cookie
			Map<String, String> cookie = getCookie("/mpay/cancel_pay_m");
			//param
			Map<String, String> param = getParam();
			String rate = DictUtils.getDictValue(onlinePay.getCkAppId(), "tencent_rate", "10");
			int rateVlue = StringUtils.isNotBlank(rate)?Integer.parseInt(rate):10;
			param.put("amt", onlinePay.getPrices()/rateVlue+"");
			param.put("billno", onlinePay.getOrderId());
			
			String header = "GET&"+URLEncoder.encode("/v3/r/mpay/cancel_pay_m", "utf-8")+"&";
			String sigContent = SignContext.getSignContextUrlEncode(param, header, "&");
			logger.info("tencent cancel_pay_m sigContent="+sigContent);
			String sig = CoderUtils.makeSig(sigContent, key);
			logger.info("tencent cancel_pay_m sig="+sig);
			param.put("sig", URLEncoder.encode(sig.replace("\n", "").replace("\r", ""),"utf-8"));
			String url = cancelPayUrl +"?"+ SignContext.getSignContext(param);
			String result = SnsNetwork.getRequest(url, (HashMap<String, String>)cookie, "https");
			//返回格式：{"ret":0,msg:""}
			if(result != null ){
				JSONObject rdata = JSONObject.parseObject(result);
				if( rdata != null && rdata.getInteger("ret") == 0){
					json.put("resultCode", 0);
					json.put("resultMsg", "success");
				}else{
					json.put("resultMsg", rdata.get("msg"));
				}
			}
		}catch(Exception e){
			logger.error("tencent cancel_pay_m error", e);
		}
		return json.toJSONString();
	}
	
	
	/**
	 * 扣除渠道币接口
	 * @return
	 */
	private String pay_m(){
		JSONObject json = new JSONObject();
		json.put("resultCode", -1);
		json.put("resultMsg", "failure");
		try{
			//cookie
			Map<String, String> cookie = getCookie("/mpay/pay_m");
			//param
			Map<String, String> param = getParam();
			String rate = DictUtils.getDictValue(onlinePay.getCkAppId(), "tencent_rate", "10");
			int rateVlue = StringUtils.isNotBlank(rate)?Integer.parseInt(rate):10;
			param.put("amt", onlinePay.getPrices()/rateVlue+"");
			param.put("billno", onlinePay.getOrderId());
			String header = "GET&"+URLEncoder.encode("/v3/r/mpay/pay_m", "utf-8")+"&";
			String sigContent = SignContext.getSignContextUrlEncode(param, header, "&");
			logger.info("tencent pay_m sigContent="+sigContent);
			String sig = CoderUtils.makeSig(sigContent, key+"&");
			logger.info("tencent pay_m sig="+sig);
			param.put("sig", URLEncoder.encode(sig.replace("\r", "").replace("\n", ""),"utf-8"));
			String url = payUrl +"?"+SignContext.getSignContext(param);
			String result = SnsNetwork.getRequest(url, (HashMap<String, String>)cookie, "https");
			//返回格式：{"ret":0,"billno":"45353","balance":210}
			if(result != null ){
				JSONObject rdata = JSONObject.parseObject(result);
				logger.info("tencent pay_m result="+rdata);
				if( rdata != null && rdata.getInteger("ret") == 0){
					json.put("resultCode", 0);
					json.put("resultMsg", "success");
					JSONObject jsonResp = new JSONObject();
					jsonResp.put("billno", rdata.get("billno"));
					jsonResp.put("balance", rdata.get("balance"));
					json.put("data", jsonResp);
				}else{
					json.put("resultMsg", rdata.get("msg"));
				}
			}
		}catch(Exception e){
			logger.error("tencent pay_m error", e);
		}
		return json.toJSONString();
	}
	
	/**
	 * 直接赠送接口
	 * @return
	 */
	private String present_m(){
		JSONObject json = new JSONObject();
		json.put("resultCode", -1);
		json.put("resultMsg", "failure");
		try{
			//cookie
			Map<String, String> cookie = getCookie("/mpay/present_m");
			//param
			Map<String, String> param = getParam();
			param.put("billno", onlinePay.getOrderId());
			String rate = DictUtils.getDictValue(onlinePay.getCkAppId(), "tencent_rate", "10");
			int rateVlue = StringUtils.isNotBlank(rate)?Integer.parseInt(rate):10;
			param.put("presenttimes", onlinePay.getPrices()/rateVlue+"");
			
			String header = "GET&"+URLEncoder.encode("/v3/r/mpay/present_m", "utf-8")+"&";
			String sigContent = SignContext.getSignContextUrlEncode(param, header, "&");
			logger.info("tencent present_m sigContent="+sigContent);
			String sig = CoderUtils.makeSig(sigContent, key+"&");
			logger.info("tencent present_m sig="+sig);
			param.put("sig", URLEncoder.encode(sig.replace("\n", "").replace("\r", ""),"utf-8"));
			String url = presentUrl +"?"+ SignContext.getSignContext(param);
			String result = SnsNetwork.getRequest(url, (HashMap<String, String>)cookie, "https");
			//返回格式：{"ret":1018,"msg":"请先登录"}
			if(result != null ){
				JSONObject rdata = JSONObject.parseObject(result);
				if( rdata != null && rdata.getInteger("ret") == 0){
					json.put("resultCode", 0);
					json.put("resultMsg", "success");
					JSONObject jsonResp = new JSONObject();
					json.put("data", jsonResp);
				}else{
					json.put("resultMsg", rdata.get("msg"));
				}
			}
		}catch(Exception e){
			logger.error("tencent present_m error", e);
		}
		return json.toJSONString();
	}
	
	private Map<String,String> getCookie(String uri) throws UnsupportedEncodingException{
		Map<String,String> map = new HashMap<String, String>();
		String session_id = "openid";//
		String session_type = "kp_actoken";//
		if( onlinePay.getPayMap() != null && onlinePay.getPayMap().get("tencentLoginType").toString().equals("2")){
			session_id = "hy_gameid";
			session_type = "wc_actoken";
		}
		map.put("session_id", session_id);
		map.put("session_type", session_type);
		map.put("org_loc", URLEncoder.encode(uri,"utf-8"));
		logger.info("tencent cookie="+map.toString());
		return map;
	}
	
	private Map<String,String> getParam() throws UnsupportedEncodingException{
		Map<String,String> map = new HashMap<String, String>();
		Map<String,Object> data = onlinePay.getPayMap();
		if( data != null && data.size()>0 ){
			map.put("openid",data.get("openId")!=null?data.get("openId").toString():"");
			map.put("openkey" , data.get("openKey")!=null?data.get("openKey").toString():"");
//			map.put("pay_token" , data.get("payToken")!=null?data.get("payToken").toString():"");
			map.put("appid" , onlinePay.getPayInfoConfig().getAppid());
			map.put("ts" , System.currentTimeMillis()/1000+"");
			map.put("pf" , data.get("pf")!=null?data.get("pf").toString():"");//平台来源
			map.put("pfkey" , data.get("pfKey")!=null?data.get("pfKey").toString():"");//
			map.put("zoneid" , data.get("zoneId")!=null?data.get("zoneId").toString():"");
//			map.put("fmt" , data.get("fmt")!=null?data.get("fmt").toString():"");
		}
		return map;
	}
	
	private PayCodeConfig getPayCodeConfig(OnlinePay onlinePay){
		PayCodeConfig payCodeConfig = new PayCodeConfig();
		payCodeConfig.setCkAppId(onlinePay.getCkAppId());
		payCodeConfig.setChannelId(onlinePay.getChannelId());
		payCodeConfig.setPaytype(onlinePay.getPayType());
		payCodeConfig.setProductId(onlinePay.getProductId());
		return onlineService.getPayCode(payCodeConfig);
	}
	
	/**
	 * 直购模式接口
	 * @return
	 */
	private String buy_goods_m (){
		JSONObject json = new JSONObject();
		json.put("resultCode", -1);
		json.put("resultMsg", "failure");
		try{
			//cookie
			Map<String, String> cookie = getCookie("/mpay/buy_goods_m");
			//param
			Map<String, String> param = getParam();
			param.put("billno", onlinePay.getOrderId());
			String rate = DictUtils.getDictValue(onlinePay.getCkAppId(), "tencent_rate", "10");
			int rateVlue = StringUtils.isNotBlank(rate)?Integer.parseInt(rate):10;
			param.put("presenttimes", onlinePay.getPrices()/rateVlue+"");
			
			String header = "GET&"+URLEncoder.encode("/v3/r/mpay/present_m", "utf-8")+"&";
			String sigContent = SignContext.getSignContextUrlEncode(param, header, "&");
			logger.info("tencent present_m sigContent="+sigContent);
			String sig = CoderUtils.makeSig(sigContent, key+"&");
			logger.info("tencent present_m sig="+sig);
			param.put("sig", URLEncoder.encode(sig.replace("\n", "").replace("\r", ""),"utf-8"));
			String url = presentUrl +"?"+ SignContext.getSignContext(param);
			String result = SnsNetwork.getRequest(url, (HashMap<String, String>)cookie, "https");
			//返回格式：{"ret":1018,"msg":"请先登录"}
			if(result != null ){
				JSONObject rdata = JSONObject.parseObject(result);
				if( rdata != null && rdata.getInteger("ret") == 0){
					json.put("resultCode", 0);
					json.put("resultMsg", "success");
					JSONObject jsonResp = new JSONObject();
					json.put("data", jsonResp);
				}else{
					json.put("resultMsg", rdata.get("msg"));
				}
			}
		}catch(Exception e){
			logger.error("tencent present_m error", e);
		}
		return json.toJSONString();
	}
	
}

