package me.ckhd.opengame.online.handle.common.tencent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.ckhd.opengame.app.entity.PayCodeConfig;
import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.Encodes;
import me.ckhd.opengame.common.utils.SignContext;
import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.TencentCallbackData;
import me.ckhd.opengame.online.sendOrder.task.OrderSenderBoot;
import me.ckhd.opengame.online.service.OnlinePayService;
import me.ckhd.opengame.online.service.OnlineService;
import me.ckhd.opengame.online.service.TencentCallbackDataService;
import me.ckhd.opengame.online.task.ExecutorServiceUtils;
import me.ckhd.opengame.online.util.OrderStatus;
import me.ckhd.opengame.sys.utils.DictUtils;
import me.ckhd.opengame.util.SnsNetwork;

import com.alibaba.fastjson.JSONObject;


public class OtherRequest{
	protected Logger logger = LoggerFactory.getLogger(OtherRequest.class);
	
	private static TencentCallbackDataService tencentCallbackDataService = null;
	
	private static OnlinePayService onlinePayService = null;
	
	private static OnlineService onlineService = null;
	
	public OnlinePay onlinePay = null;
//	public String getBalanceUrl = "https://ysdk.qq.com/mpay/get_balance_m";
	public String getBalanceUrl = "https://ysdktest.qq.com/mpay/get_balance_m";//test
//	public String cancelPayUrl = "https://ysdk.qq.com/mpay/cancel_pay_m";//test
	public String cancelPayUrl = "https://ysdktest.qq.com/mpay/cancel_pay_m";//test
//	public String payUrl = "";//test
	public String payUrl = "https://ysdktest.qq.com/mpay/pay_m";//test
//	public String presentUrl = "https://ysdk.qq.com/mpay/present_m";//test
	public String presentUrl = "https://ysdktest.qq.com/mpay/present_m";//test	
//	public String buygoodsUrl = "https://ysdk.qq.com/mpay/buy_goods_m";
	public String buygoodsUrl = "https://ysdktest.qq.com/mpay/buy_goods_m";
	
	public String key = "p8Dy51c6lCTCDcIHpNhV16A9JiKh4uuk";
	
	public JSONObject data = new JSONObject(); 
	
	public OtherRequest() {
	}
	
	public OtherRequest(OnlinePay _onlinePay,JSONObject json) {
		onlinePay = _onlinePay;
		if( json != null ){
			data = json;
		}else{
			if( StringUtils.isNotBlank(onlinePay.getCallBackContent()) ){
				data = JSONObject.parseObject(onlinePay.getCallBackContent());
			}
		}
		if (data != null && "sandbox".equalsIgnoreCase(data.getString("environment"))) {
			key = (String) _onlinePay.getPayInfoConfig().getExInfoMap().get("tAppKey");
		} else {
			getBalanceUrl = getBalanceUrl.replace("ysdktest", "ysdk");
			cancelPayUrl = cancelPayUrl.replace("ysdktest", "ysdk");
			payUrl = payUrl.replace("ysdktest", "ysdk");
			presentUrl = presentUrl.replace("ysdktest", "ysdk");
			key = (String) _onlinePay.getPayInfoConfig().getExInfoMap().get("mAppKey");
		}
		if( onlinePayService == null ){
			onlinePayService = SpringContextHolder.getBean(OnlinePayService.class);
		}
		if( tencentCallbackDataService == null ){
			tencentCallbackDataService = SpringContextHolder.getBean(TencentCallbackDataService.class);
		}
		if( onlineService == null){
			onlineService = SpringContextHolder.getBean(OnlineService.class);
		}
		if(data!= null && !data.containsKey("isResend") ){
			TencentCallbackData ten = new TencentCallbackData();
			ten.setData(data.toJSONString());
			ten.setOrderId(_onlinePay.getOrderId());
			ten.setPrice(_onlinePay.getPrices());
			ten.setStatus(0);
			tencentCallbackDataService.save(ten);
		}
	}
	
	public String call(){
		//operateType
		if( data == null || data.get("operate") == null){
			return "";
		}else if( 1 == data.getInteger("operate")){
			return get_balance_m();
		}else if(  2 == data.getInteger("operate")){
			return pay_m();
		}else if(  3 == data.getInteger("operate")){
			return cancel_pay_m();
		}else if(  4 == data.getInteger("operate")){
			return present_m();
		}else if(  5 == data.getInteger("operate")){
			ExecutorServiceUtils.execute(this);
			return "{\"resultCode\":0}";
		}
		return "";
	}
	
	public void getDataByTenCent(){
		String balance = get_balance_m();
		int i = data.containsKey("sendNum")?data.getInteger("sendNum"):0;
//		String rate = DictUtils.getDictValue(onlinePay.getCkAppId(), "tencent_rate", "10");
//		int rateVlue = StringUtils.isNotBlank(rate)?Integer.parseInt(rate):10;
		int amt = onlinePay.getPrices();
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
					if(rdata!=null){
						json.put("resultMsg", rdata.get("msg"));
					}
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
		if( data != null && data.getInteger("tencentLoginType") == 2){
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
		if( data != null && data.size()>0 ){
			map.put("openid",data.get("openId")!=null?data.get("openId").toString():"");
			map.put("openkey" , data.get("openKey")!=null?data.get("openKey").toString():"");
//			map.put("pay_token" , data.get("payToken")!=null?data.get("payToken").toString():"");
			map.put("appid" , onlinePay.getPayInfoConfig().getAppid());
			map.put("ts" , System.currentTimeMillis()/1000+"");
			map.put("pf" , data.get("pf")!=null?data.get("pf").toString():"");//平台来源
			map.put("pfkey" , data.get("pfKey")!=null?data.get("pfKey").toString():"");//
			map.put("zoneid" , data.get("tencentZoneId")!=null?data.get("tencentZoneId").toString():"");
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
	
	public static void main(String[] args) {
        String str = "{\"androidId\":\"e1e4c768e75e9045\",\"simNo\":\"89860117834013884674\",\"osVersionName\":\"5.1\",\"openKey\":\"402C8F580C98988919EA621753D16F18\",\"subCkAppId\":3,\"serverId\":\"20001\",\"tencentLoginType\":1,\"orderId\":\"ya1706260f81e641\",\"roleId\":\"1962614\",\"phone_model\":\"OPPO A59m\",\"osVersion\":\"Android22\",\"channelId\":17,\"tencentZoneId\":\"1\",\"imei\":\"862023032871419\",\"isTest\":0,\"ckChannelId\":\"17\",\"zoneId\":\"20001\",\"pf\":\"desktop_m_qq-2002-android-2002-862023032871419\",\"ckAppId\":\"2000\",\"subCkChannelId\":1,\"pfKey\":\"7d297231a0dc2cdf37a8c36ad1bad7f8\",\"operate\":\"5\",\"payType\":\"141\",\"imsi\":\"460092315002554\",\"act\":\"0\",\"openId\":\"16CFE9F0730EBB82D33B40FE706986C9\"}";
        OtherRequest other = new OtherRequest();
        JSONObject json = JSONObject.parseObject(str);
        other.get_balance_m(json);
//        other.pay_m(json, "60", "ya1706260f92c118");
    }
	
	/**
     * 腾讯余额查询
     */
    private String get_balance_m(JSONObject testData){
        JSONObject json = new JSONObject();
        json.put("resultCode", -1);
        json.put("resultMsg", "failure");
        try {
            //cookie
            Map<String,String> cookie = getCookie("/mpay/get_balance_m",testData);
            //param
            Map<String,String> param = getParam(testData);

            String header = "GET&"+URLEncoder.encode("/v3/r/mpay/get_balance_m", "utf-8")+"&";
            String sigContent = SignContext.getSignContextUrlEncode(param, header, "&");
            logger.info("tencent get_balance_m sigContent="+sigContent);
            String sig = CoderUtils.makeSig(sigContent,"p8Dy51c6lCTCDcIHpNhV16A9JiKh4uuk&");
            param.put("sig", URLEncoder.encode(sig.replace("\r", "").replace("\n", ""),"utf-8").replace("+", "%2B").replace("*", "%2A"));
            logger.info("tencent get_balance_m sig="+param.get("sig"));
            String url = "https://ysdk.qq.com/mpay/get_balance_m?" +SignContext.getSignContext(param);
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
                    if(rdata!=null){
                        json.put("resultMsg", rdata.get("msg"));
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("tencent get_balance_interface error", e);
        }
        return json.toJSONString();
    }
    
    private Map<String,String> getParam(JSONObject json) throws UnsupportedEncodingException{
        Map<String,String> map = new HashMap<String, String>();
        if( json != null && json.size()>0 ){
            map.put("openid",json.get("openId")!=null?json.get("openId").toString():"");
            map.put("openkey" , json.get("openKey")!=null?json.get("openKey").toString():"");
//          map.put("pay_token" , data.get("payToken")!=null?data.get("payToken").toString():"");
            map.put("appid" , "1106091294");
            map.put("ts" , System.currentTimeMillis()/1000+"");
            map.put("pf" , json.get("pf")!=null?json.get("pf").toString():"");//平台来源
            map.put("pfkey" , json.get("pfKey")!=null?json.get("pfKey").toString():"");//
            map.put("zoneid" , json.get("tencentZoneId")!=null?json.get("tencentZoneId").toString():"");
//          map.put("fmt" , data.get("fmt")!=null?data.get("fmt").toString():"");
        }
        return map;
    }
    
    private Map<String,String> getCookie(String uri,JSONObject testJson) throws UnsupportedEncodingException{
        Map<String,String> map = new HashMap<String, String>();
        String session_id = "openid";//
        String session_type = "kp_actoken";//
        if( testJson != null && testJson.getInteger("tencentLoginType") == 2){
            session_id = "hy_gameid";
            session_type = "wc_actoken";
        }
        map.put("session_id", session_id);
        map.put("session_type", session_type);
        map.put("org_loc", URLEncoder.encode(uri,"utf-8"));
        logger.info("tencent cookie="+map.toString());
        return map;
    }
    
    private String pay_m(JSONObject testData,String price,String orderId){
        JSONObject json = new JSONObject();
        json.put("resultCode", -1);
        json.put("resultMsg", "failure");
        try{
            //cookie
            Map<String, String> cookie = getCookie("/mpay/pay_m",testData);
            //param
            Map<String, String> param = getParam(testData);
//            String rate = DictUtils.getDictValue(onlinePay.getCkAppId(), "tencent_rate", "10");
//            int rateVlue = StringUtils.isNotBlank(rate)?Integer.parseInt(rate):10;
            param.put("amt", price);
            param.put("billno", orderId);
            String header = "GET&"+URLEncoder.encode("/v3/r/mpay/pay_m", "utf-8")+"&";
            String sigContent = SignContext.getSignContextUrlEncode(param, header, "&");
            logger.info("tencent pay_m sigContent="+sigContent);
            String sig = CoderUtils.makeSig(sigContent, "p8Dy51c6lCTCDcIHpNhV16A9JiKh4uuk&");
            logger.info("tencent pay_m sig="+sig);
            param.put("sig", URLEncoder.encode(sig.replace("\r", "").replace("\n", ""),"utf-8"));
            String url = "https://ysdk.qq.com/mpay/pay_m?"+SignContext.getSignContext(param);
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
}

