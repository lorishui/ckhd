package me.ckhd.opengame.online.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.APPCk;
import me.ckhd.opengame.app.entity.Cfgparam;
import me.ckhd.opengame.app.entity.Channel;
import me.ckhd.opengame.app.entity.PayCodeConfig;
import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.service.OnlineService;
import me.ckhd.opengame.online.util.OrderStatus;
import me.ckhd.opengame.sys.entity.Dict;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 与前端对接
 * 
 * @author leo
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/online/app")
public class AppOnlineApi extends BaseController {

	@Autowired
	private OnlineService onlineService;
	
	private String getChannel(Map<String, Object> map) {
		// 通过参数获取到渠道ID
		String channelId = map.get("ckChannelId").toString();
		// 通过渠道ID获取到渠道信息
		Channel channel = onlineService.getChannelById(channelId);
		return channel.getEngName();
	}

	private String getDict(String paytype) {
		// 通过渠道ID获取到渠道信息
		Dict dict = onlineService.getpayTypeDict(paytype);
		return dict.getDescription();
	}
	
	/**
	 * TODO 前端调用后台获取支付数据/订单号
	 * 
	 * @return
	 */
	@RequestMapping(value = "init")
	@ResponseBody
	public String init(@RequestBody String code, HttpServletRequest request,HttpServletResponse response) {
		String returnStr="";
		JSONObject json = new JSONObject();
		JSONObject result = new JSONObject();
		result.put("code", "-1");
		result.put("msg", "Internal server error");
		json.put("result", result);
		logger.info(String.format("客户端上传的支付数据:[%s]", code));
		// 根据客户端上传数据解析成Map
		Map<String, Object> codeMap = MyJsonUtils.jsonStr2Map(code);
		PayInfoConfig payInfoConfig = getPayInfo(codeMap);
		
		List<PayCodeConfig> payCodeConfig = null;
		if( codeMap != null && codeMap.get("isPayCode") != null && codeMap.get("isPayCode").toString().equals("1")){
			//获取已配置的计费点信息
			payCodeConfig = getPayCodeConfigArray(codeMap);
			if(payCodeConfig != null){
				JSONArray payArray = new JSONArray();
				for(PayCodeConfig payCode:payCodeConfig){
					JSONObject pay = new JSONObject();
					pay.put("productid", payCode.getProductId());
					if( payCode.getExInfoMap() != null ){
						pay.put("payCode", payCode.getExInfoMap().get("payCode"));
						if(payCode.getExInfoMap().containsKey("useOtherPay")){
							pay.put("useOtherPay", 1);
						}
					}
					pay.put("price", payCode.getPrice());
					payArray.add(pay);
				}
				json.put("payCode", payArray);
				result.put("code", 0);
				result.put("msg", "success");
			}
		}
		
		JSONObject payInfo = new JSONObject();
		if(payInfoConfig != null ){
			payInfo.put("appId",payInfoConfig.getAppid());
			payInfo.put("appKey",payInfoConfig.getAppkey());
			result.put("code", 0);
			result.put("msg", "success");
		}
		json.put("payInfo", payInfo);
		returnStr = json.toJSONString();
		logger.info(String.format("返回客户端的支付数据:[%s]", returnStr));
		return returnStr;
	}
	
	public OnlinePay getOnliePay(Map<String, Object> map,Integer orderId,String year,APPCk appCk) throws Exception{
		OnlinePay onlinePay = new OnlinePay();
		String channelId= map.get("ckChannelId")==null?"":map.get("ckChannelId").toString();
		onlinePay.setChannelId(channelId);
		onlinePay.setCkAppId(map.get("ckAppId")==null?"":map.get("ckAppId").toString());
		onlinePay.setAppId(map.get("mmAppId")==null?"":map.get("mmAppId").toString());
		onlinePay.setVersion(map.get("version")==null?"":map.get("version").toString());
		onlinePay.setPayType(map.get("payType")==null?"":map.get("payType").toString());
		onlinePay.setExtension(map.get("extension")==null?"":map.get("extension").toString());
		onlinePay.setGameOnline(map.get("gameOnline")==null?0:Integer.valueOf(map.get("gameOnline").toString()));
		if(map.get("price") != null){
			int price = Integer.valueOf(map.get("price").toString());
			double d = price*appCk.getDiscount();
			onlinePay.setPrices((int)d);
		}else{
			onlinePay.setPrices(0);
		}
		onlinePay.setUserId(map.get("userId")==null?null:map.get("userId").toString());
		onlinePay.setProductId(map.get("productId")==null?"0":map.get("productId").toString());
		onlinePay.setAppPayContent(map.get("verifyInfo")==null?"":JSONObject.toJSONString(map.get("verifyInfo")));
		onlinePay.setSdkType(map.get("sdkType")==null?"":map.get("sdkType").toString());
		if(map.containsKey("payNotifyUrl")){
			onlinePay.setCallBackUrl(map.get("payNotifyUrl")==null?"":map.get("payNotifyUrl").toString());
		}else if(map.containsKey("callBackUrl")){
			onlinePay.setCallBackUrl(map.get("callBackUrl")==null?"":map.get("callBackUrl").toString());
		}
		if( map.containsKey("isTest") ){
			onlinePay.setIsTest(map.get("isTest")==null?0:(Integer)map.get("isTest"));
		}
		onlinePay.setOrderStatus(OrderStatus.NON_PAYMENT);
		onlinePay.setOrderId(genOderId(orderId,onlinePay.getCkAppId(),year));
		onlineService.savePayInfo(onlinePay);
		if(channelId != null && "2".equals(onlinePay.getSdkType()) && 
				("22".equals(channelId) || "23".equals(channelId) || 
						"24".equals(channelId) || "25".equals(channelId) )){
			onlinePay.setOrderIndex(saveIndex(channelId, 0, onlinePay.getOrderId())+"");
		}
		return onlinePay;
	}
	
	private int saveIndex(String channelId,int index,String orderId) throws Exception{
		int orderIndex=0;
		try {
			orderIndex = onlineService.saveOrderIndex(orderId);
		} catch (Exception e) {
			if (index<3) {
				orderIndex=saveIndex(channelId,index++,orderId);
			}else{
				throw e;
			}
		}
		return orderIndex;
	}
	
	private String genOderId(int orderId,String ckAppId,String year) {
		StringBuffer strBuff = new StringBuffer();
		// 游戏编号超过1099修改，改成36进制，相当于1099对应"99",1100对应"9a" update@2016-12-26
		String appCode = null;
		if (ckAppId.startsWith("10")) {
			appCode = ckAppId.substring(ckAppId.length()-2);
		} else {
			int y = Integer.parseInt(ckAppId);
			// 36进制100等于334（增加334-100 = 234）
			y = y - 1000 + 234;
			appCode = Integer.toString(y, Character.MAX_RADIX);
		}
		// update@2016-12-26 end
		strBuff.append(appCode);
		strBuff.append(year);

		String orderStr = "000000" + Integer.toHexString(orderId);
		orderStr = orderStr.substring(orderStr.length() - 6);
		strBuff.append(orderStr);//序号 
		
		Random random = new Random();
		strBuff.append(String.valueOf(10+random.nextInt(90)));
		return strBuff.toString();
	}
	
	
	public PayInfoConfig getLoginInfo(Map<String, Object> map) {
		PayInfoConfig payInfo = new PayInfoConfig();
		payInfo.setCkAppId(map.get("ckAppId")==null?"":map.get("ckAppId").toString());
		payInfo.setChannelId(map.get("ckChannelId")==null?"":map.get("ckChannelId").toString());
		payInfo.setCarrierAppId(map.get("mmAppId")==null?"":map.get("mmAppId").toString());
		return  onlineService.getLoginInfo(payInfo);
	}
	
	public PayInfoConfig getPayInfo(Map<String, Object> map) {
		PayInfoConfig payInfo = new PayInfoConfig();
		payInfo.setCkAppId(map.get("ckAppId")==null?"":map.get("ckAppId").toString());
		payInfo.setPaytype(map.get("payType")==null?"":map.get("payType").toString());
		payInfo.setChannelId(map.get("ckChannelId")==null?"":map.get("ckChannelId").toString());
		payInfo.setCarrierAppId(map.get("mmAppId")==null?"":map.get("mmAppId").toString());
		return  onlineService.getPayInfo(payInfo);
	}

	public PayCodeConfig getPayCodeConfig(Map<String, Object> map){
		PayCodeConfig payCodeConfig = new PayCodeConfig();
		payCodeConfig.setCkAppId(map.get("ckAppId")==null?"":map.get("ckAppId").toString());
		payCodeConfig.setChannelId(map.get("ckChannelId")==null?"":map.get("ckChannelId").toString());
		if(map.containsKey("payType") && StringUtils.isNotBlank((String)map.get("payType"))){
			Integer payType = Integer.valueOf(map.get("payType").toString());
			if( payType > 120){
				payCodeConfig.setPaytype("141");
			}else{
				payCodeConfig.setPaytype(map.get("payType")==null?"":map.get("payType").toString());
			}
		}else{
			payCodeConfig.setPaytype("");
		}
		payCodeConfig.setProductId(map.get("productId")==null?"":map.get("productId").toString());
		return onlineService.getPayCode(payCodeConfig);
	}

	public List<PayCodeConfig> getPayCodeConfigArray(Map<String, Object> map){
		PayCodeConfig payCodeConfig = new PayCodeConfig();
		payCodeConfig.setCkAppId(map.get("ckAppId")==null?"":map.get("ckAppId").toString());
		payCodeConfig.setChannelId(map.get("ckChannelId")==null?"":map.get("ckChannelId").toString());
		payCodeConfig.setPaytype(map.get("payType")==null?"":map.get("payType").toString());
//		return onlineService.getPayCode(payCodeConfig);
		return onlineService.getPayCodeArray(payCodeConfig);
	}
	/**
	 * 获取到pay包名需替换的字符串
	 * @param code
	 * @return
	 */
	public String getPayType(Map<String, Object> map) {
		String paytype = map.get("payType").toString();
		if ("141".equals(paytype)) {
			String engName = getChannel(map);
			return engName;
		} else {
			String description = getDict(paytype);
			return description;
		}
	}
	
	/**
	 * 前端获取支付折扣
	 * 
	 * @return
	 */
	@RequestMapping(value = "getPayTypeConvert")
	@ResponseBody
	public Map<String, Object> getPayTypeConvert(PayInfoConfig payInfoConfig) {
		logger.info(String.format("客户端上传的数据:[%s]", payInfoConfig.toString()));
		
		// 获取已配置支付信息
		PayInfoConfig payInfo =onlineService.getPayInfo(payInfoConfig); 
		Map<String, Object> map = new HashMap<String, Object>();
		if(payInfo==null){
			map.put("resultCode", -1);
			map.put("errMsg", "未配置支付折扣信息!");
		}else{
			map.put("resultCode", 0);
			map.put("result",payInfo);
		}
		return map;
	}
	
	/**
	 * 更新
	 * @param cfgparam
	 * @param map
	 */
	public void updateCfgparamCellinfo(Cfgparam cfgparam, Map<String,Object> map){
		
		int mcc;
		int mnc = 0;
		int lac = 0;
		int ci = 0;
		
		// 基站数据
		String dataMcc = (String)map.get("mcc");
		String dataMnc = (String)map.get("mnc");
		String dataLac = (String)map.get("lac");
		String dataCi = (String)map.get("ci");
		String dataSid = (String)map.get("sid");
		String dataNid = (String)map.get("nid");
		String dataBid = (String)map.get("bid");
		if (dataMcc != null || dataMnc != null || dataSid != null) {
			try {
				mcc = Integer.parseInt(dataMcc);
				mnc = Integer.parseInt(dataMnc);
				lac = Integer.parseInt(dataLac);
				ci = Integer.parseInt(dataCi);

				if (dataSid != null && dataNid != null && dataBid != null) {
					mcc = 460;
					mnc = Integer.parseInt(dataSid);
					lac = Integer.parseInt(dataNid);
					ci = Integer.parseInt(dataBid);
				}

			} catch (Throwable t) {
				// 新包，异常数据或者用户未授权
				mcc = -1;
				mnc = 0;
				lac = 0;
				ci = 0;
			}
		} else {
			// 旧包
			mcc = -100;
		}
		
		cfgparam.setMcc(mcc);
		cfgparam.setMnc(mnc);
		cfgparam.setLac(lac);
		cfgparam.setCi(ci);
	}

}
