package me.ckhd.opengame.online.handle;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import sun.misc.BASE64Encoder;

import com.alibaba.fastjson.JSONObject;

@Component("kuaikan")
@Scope("prototype")
public class KuaiKanHandle extends BaseHandle {

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
		
		TreeMap<String, Object> map = new TreeMap<String, Object>();
		String openUid = (String) codeJson.get("openId");
		map.put("app_id",onlinePay.getPayInfoConfig().getAppid());
		map.put("wares_id", Integer.parseInt((String)onlinePay.getPayCodeConfig().getExInfoMap().get("waresid")));
		map.put("out_order_id",onlinePay.getOrderId());
		map.put("open_uid",openUid);
		map.put("out_notify_url", codeJson.get("notifyUrl"));
		JSONObject jo = new JSONObject(map);
		//http://i16785s665.iok.la/offlinepay/callback/kuaikan/1.1.0/
		String key = onlinePay.getPayInfoConfig().getAppkey();
		StringBuffer sb = getData(map);
		String sign = getSign(key, sb);
		data.put("trans_data",jo.toJSONString());
		data.put("sign", sign);
		data.put("orderId", onlinePay.getOrderId());
		result.put("result", data);
		return result.toJSONString();
	}
	
	
	
	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		if(StringUtils.isNotBlank(code)){
			String data = request.getParameter("trans_data");
			JSONObject jo = (JSONObject) JSONObject.parse(data);
			onlinePay.setAppId(jo.getString("app_id"));
			onlinePay.setChannelOrderId(jo.getString("order_id"));
			onlinePay.setOrderId(jo.getString("out_order_id"));
			onlinePay.setProductId(jo.getInteger("wares_id").toString());
			onlinePay.setPrices((int)(jo.getFloatValue("trans_money")*100));
			onlinePay.setCallBackContent(code);
			respData.put("pay_status", jo.getInteger("pay_status"));
		}
		
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		int status = (int) respData.get("pay_status");
		if(2 == status){//
		 	result.put("code", 2000);
			return getReturnSuccess();
	    }else{ // 
        	result.put("code", 4000);
			result.put("errMsg", "支付失败！");
			return getReturnSuccess();
	    }
	}

	@Override
	public String getReturnSuccess() {
		return "SUCCESS";
	}

	@Override
	public String getReturnFailure() {
		return "err";
	}

	
	
	public String getSign(String key, StringBuffer sb) {
		try {
			if (StringUtils.isBlank(key) || sb == null) {
				return null;
			}
			sb.append("key=" + key);
			String md5Str = sb.toString();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			BASE64Encoder base64en = new BASE64Encoder();
			// 加密后的字符串
			String sign = base64en.encode(md5.digest(md5Str.getBytes("utf-8")));
			return sign;
		} catch (Exception e) {
			log.error("KuaikanSignUtil getServerSign error",e);
		}
		return null;
	}
	
	public StringBuffer getData(Map<String, Object> paramMap){
		StringBuffer sb = new StringBuffer();
		try {
			if (CollectionUtils.isEmpty(paramMap)) {
				return null;
			}
			TreeMap<String, Object> signMap = new TreeMap<>(paramMap);
			Set es = signMap.entrySet();
			Iterator it = es.iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String k = (String) entry.getKey();
				Object v = entry.getValue();
				if (v != null && !"".equals(v) && !"sign".equals(k)
						&& !"key".equals(k)) {
					sb.append(k + "=" + String.valueOf(v) + "&");
				}
			}
		}catch(Exception e) {
			log.error("KuaikanSignUtil getServerSign error",e);
		}
		return sb;
	}

	public boolean checkSign(String key, String sign,
			Map<String, Object> paramMap) {
		if (CollectionUtils.isEmpty(paramMap)) {
			return false;
		}
		if (StringUtils.isBlank(sign)) {
			return false;
		}
		StringBuffer sb = getData(paramMap);
		String checkSign = getSign(key, sb);
		if (StringUtils.isBlank(checkSign)) {
			return false;
		}
		if (!sign.equals(checkSign)) {
			return false;
		}
		return true;
	}

}
