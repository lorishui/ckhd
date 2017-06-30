package me.ckhd.opengame.online.handle;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.MD5Util;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.util.HttpClientUtils;

import com.alibaba.fastjson.JSONObject;

@Component("nubia")
@Scope("prototype")
public class nubiaHandle extends BaseHandle {

	private static final String login_url= "https://niugamecenter.nubia.com/VerifyAccount/CheckLogined";
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson, PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("uid", verifyInfo.getString("uid"));
		map.put("game_id", verifyInfo.getString("game_id"));
		map.put("session_id", verifyInfo.getString("session_id"));
		map.put("data_timestamp", System.currentTimeMillis()+"");
		String data = getSignData(map);
		String appId = payInfo.getAppid();
		String SecretKey = payInfo.getExInfoMap().get("AppSecret")+"";
		String sign = MD5Util.string2MD5(data+":"+appId+":"+SecretKey);
		map.put("sign", sign);
		String param = "";
		try {
			param = mapToUrl(map);
		} catch (UnsupportedEncodingException e) {
			result.put("errMsg", "生成参数错误");
			return result.toJSONString();
		}
		String responseData = HttpClientUtils.post(login_url, param, 10000,10000,"application/x-www-form-urlencoded; ");
		if(StringUtils.isNotBlank(responseData)){
			JSONObject response = JSONObject.parseObject(responseData);
			if(response.containsKey("code")){
				if(0 == response.getIntValue("code")){
					onlineUser.setSid(verifyInfo.getString("uid"));
					returnLoginSuccess(result, onlineUser);
				}else{
					result.put("errMsg", "校验失败");
				}
			}else{
				result.put("errMsg", "登录失败");
			}
		}
		return result.toJSONString();
	}
	
	@Override
	public String pay(OnlinePay onlinePay, JSONObject codeJson) {
		JSONObject result = new JSONObject();
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		
		result.put("resultCode",0);
		result.put("errMsg","SUCCESS");
		HashMap<String, String> map = new HashMap<String,String>();
		map.put("app_id", onlinePay.getPayInfoConfig().getAppid());
		map.put("uid", verifyInfo.getString("uid"));
		map.put("cp_order_id", onlinePay.getOrderId());
		DecimalFormat df = new DecimalFormat(".00");
		map.put("amount", df.format(onlinePay.getPrices()*0.01));
		map.put("product_name", verifyInfo.getString("productName"));
		map.put("product_des", verifyInfo.getString("productName"));
		map.put("number", verifyInfo.getString("number"));
		String dataTimestamp = System.currentTimeMillis()+"";
		map.put("data_timestamp", dataTimestamp);
		String signData = getSignData(map);
		String appId = onlinePay.getPayInfoConfig().getAppid();
		String SecretKey = onlinePay.getPayInfoConfig().getExInfoMap().get("AppSecret")+"";
		String sign = CoderUtils.md5(signData+":"+appId+":"+SecretKey,"utf-8");
		
		JSONObject data = new JSONObject();
		data.put("orderId", onlinePay.getOrderId());
		data.put("dataTimestamp", dataTimestamp);
		data.put("sign",sign);
		result.put("result", data);
		return result.toJSONString();
	}
	
	@SuppressWarnings("unchecked")
    @Override
	public void parseParamter(String code, HttpServletRequest request, OnlinePay onlinePay) {
		Set<String> keySet = request.getParameterMap().keySet();
		for (String key : keySet) {
			respData.put(key, request.getParameter(key));
		}
		onlinePay.setOrderId(respData.getString("order_no"));
		onlinePay.setChannelOrderId(respData.getString("order_serial"));
		onlinePay.setActualAmount(Double.parseDouble(respData.getString("amount"))*100+"");
		onlinePay.setCallBackContent(respData.toJSONString());

	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result, HttpServletResponse response) {
		if(respData.containsKey("pay_success") && "1".equals(respData.getString("pay_success"))){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("order_no", respData.getString("order_no"));
			map.put("data_timestamp", respData.getString("data_timestamp"));
			map.put("pay_success", respData.getString("pay_success"));
			map.put("app_id", respData.getString("app_id"));
			map.put("uid", respData.getString("uid"));
			map.put("amount", respData.getString("amount"));
			map.put("product_name", respData.getString("product_name"));
			map.put("product_des", respData.getString("product_des"));
			map.put("number", respData.getString("number"));
			String signData = getSignData(map);
			String appId = onlinePay.getPayInfoConfig().getAppid();
			String SecretKey = onlinePay.getPayInfoConfig().getExInfoMap().get("AppSecret")+"";
			String serverSign = CoderUtils.md5(signData+":"+appId+":"+SecretKey,"utf-8");
			if(serverSign.equals(respData.getString("order_sign"))){
				result.put("code", 2000);
				return getReturnSuccess();
			}else{
				result.put("code", 4001);
				result.put("errMsg", "验证失败");
				return getReturnFailure();
			}
		}else{
			result.put("code", 4000);
			result.put("errMsg", "支付失败");
			return getReturnFailure();
		}
	}

	@Override
	public String getReturnSuccess() {
		JSONObject jo = new JSONObject();
		jo.put("code", 0);
		jo.put("data", null);
		jo.put("message", "success");
		return jo.toJSONString();
	}

	@Override
	public String getReturnFailure() {
		JSONObject jo = new JSONObject();
		jo.put("code", 90000);
		jo.put("data", null);
		jo.put("message", "verifyFailed");
		return jo.toJSONString();
	}
	
	/**
     * 将Map组装成待签名数据。
     * 待签名的数据必须按照一定的顺序排列 这个是支付宝提供的服务的规范，否则调用支付宝的服务会通不过签名验证
     * @param params
     * @return
     */
    public static String getSignData(Map<String, String> params) {
        StringBuffer content = new StringBuffer();

        // 按照key做排序
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        boolean isFirst = true;
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            if ("sign".equals(key)||"sign_type".equals(key)) {
                continue;
            }
            String value = (String) params.get(key);
            if (value != null && value.length() >0) {
                content.append((isFirst ? "" : "&") + key + "=" + value);
                isFirst = false;
            }
        }

        return content.toString();
    }

    /**
     * 将Map中的数据组装成url
     * @param params
     * @return
     * @throws UnsupportedEncodingException 
     */
    public static String mapToUrl(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (isFirst) {
                sb.append(key + "=" + URLEncoder.encode(value, "utf-8"));
                isFirst = false;
            } else {
                if (value != null) {
                    sb.append("&" + key + "=" + URLEncoder.encode(value, "utf-8"));
                } else {
                    sb.append("&" + key + "=");
                }
            }
        }
        return sb.toString();
    }

    /**
     * 取得URL中的参数值。
     * <p>如不存在，返回空值。</p>
     * 
     * @param url
     * @param name
     * @return
     */
    public static String getParameter(String url, String name) {
        if (name == null || name.equals("")) {
            return null;
        }
        name = name + "=";
        int start = url.indexOf(name);
        if (start < 0) {
            return null;
        }
        start += name.length();
        int end = url.indexOf("&", start);
        if (end == -1) {
            end = url.length();
        }
        return url.substring(start, end);
    }
    
    public static void main(String[] args) {
		DecimalFormat df = new DecimalFormat("###");
		System.out.println(df.format(9.0));
	}
}
