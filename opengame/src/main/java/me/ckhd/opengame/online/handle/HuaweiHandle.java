package me.ckhd.opengame.online.handle;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.util.huawei.CommonUtil;
import me.ckhd.opengame.online.util.huawei.RSA;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("huawei")
@Scope("prototype")
public class HuaweiHandle extends BaseHandle {
	

	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String pay(OnlinePay onlinePay, JSONObject codeJson) {
		Map<String,Object> map = new HashMap<String, Object>();
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo"); 
		map.put("userID", verifyInfo.getString("userID"));
		map.put("productName", verifyInfo.getString("productName"));
		map.put("productDesc", verifyInfo.getString("productDesc"));
		map.put("applicationID", onlinePay.getPayInfoConfig().getAppid());
		map.put("amount", getDoubleNumber(onlinePay.getPrices()*0.01d));//金额单位转换
		map.put("requestId", onlinePay.getOrderId());
//		map.put("time", System.currentTimeMillis()+"");
		//获取需要加密的串
		String signContent = RSA.getSignData(map);
		String sign = RSA.sign(signContent, onlinePay.getPayInfoConfig().getAppkey());
		JSONObject result = new JSONObject();
		result.put("resultCode",0);
		result.put("errMsg","SUCCESS");
		JSONObject data = new JSONObject();
		data.put("orderId", onlinePay.getOrderId());
		data.put("sign", sign);
		data.put("callBackUrl", codeJson.get("notifyUrl"));
		result.put("result", data);
		return result.toJSONString();
	}
	
	private String getDoubleNumber(Double d){
		DecimalFormat df = new DecimalFormat("#0.00");
        return  df.format(d);
	}

	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        Set<String> set = request.getParameterMap().keySet();
		for( String key : set ){
			respData.put(key, request.getParameter(key));
		}
        if (null == respData)
            return;
        onlinePay.setOrderId((String)respData.get("requestId"));
        onlinePay.setProductName((String) respData.get("productName"));
        onlinePay.setPrices((int)(respData.getDouble("amount")*100));
        onlinePay.setActualAmount(String.valueOf(respData.getDouble("amount")*100));
        onlinePay.setChannelOrderId((String) respData.get("orderId"));
        onlinePay.setCallBackContent(code);
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		String sign = (String)respData.get("sign");
		 if (CommonUtil.rsaDoCheck(respData, sign, onlinePay.getPayInfoConfig().getExInfoMap().get("publicKey").toString()))
	        {//验签成功
			 	result.put("code", 2000);
				return getReturnSuccess();
	        }
	        else
	        { // 验签失败
	        	result.put("code", 4000);
				result.put("errMsg", "验签失败！");
				return getReturnFailure();
	        }
	}

	@Override
	public String getReturnSuccess() {
		JSONObject result = new JSONObject();
		 result.put("result", 0);
		return result.toJSONString();
	}

	@Override
	public String getReturnFailure() {
		JSONObject result = new JSONObject();
		 result.put("result", 3);
		return result.toJSONString();
	}
	
}
