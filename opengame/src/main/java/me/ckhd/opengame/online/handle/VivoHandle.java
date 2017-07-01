package me.ckhd.opengame.online.handle;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.util.vivo.VivoSignUtils;
import me.ckhd.opengame.util.HttpClientUtils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("vivo")
@Scope("prototype")
public class VivoHandle extends BaseHandle {
	
	private final String PAY_URL = "https://pay.vivo.com.cn/vivoPay/getVivoOrderNum";
	private Map<String,String> resquestMap = new HashMap<String, String>();
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String pay(OnlinePay onlinePay, JSONObject codeJson) {
		JSONObject result = new JSONObject();
		result.put("resultCode",-1);
		result.put("errMsg","FAIl");
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo"); 
		Map<String,String> para = new HashMap<String, String>();
    	para.clear();
    	para.put("version", "1.0.0");
    	para.put("storeId", onlinePay.getPayInfoConfig().getExInfoMap().get("cpid").toString());
    	para.put("appId", onlinePay.getPayInfoConfig().getAppid());
    	para.put("storeOrder", onlinePay.getOrderId());
    	para.put("notifyUrl", codeJson.getString("notifyUrl"));
    	para.put("orderTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
    	DecimalFormat df = new DecimalFormat("######0.00");
    	para.put("orderAmount", df.format(onlinePay.getPrices()*0.01));
    	para.put("orderTitle", verifyInfo.getString("productName"));
    	para.put("orderDesc", verifyInfo.getString("productDes"));
    	String requestStr = VivoSignUtils.buildReq(para, onlinePay.getPayInfoConfig().getAppkey());
    	String responseStr =HttpClientUtils.postVivo(PAY_URL, requestStr, 2000, 2000);
    	if(StringUtils.isNotEmpty(responseStr)){
			JSONObject resultJson = JSONObject.parseObject(responseStr);
//			HashMap<String, String> m = new HashMap<String, String>();
//			m.put("vivoSignature", resultJson.getString("vivoSignature"));
//			m.put("vivoOrder", resultJson.getString("vivoOrder"));
//			m.put("orderAmount", resultJson.getString("orderAmount"));
//			m.put("signature", resultJson.getString("signature"));
//			m.put("respCode", resultJson.getInteger("respCode")+"");
//			m.put("respMsg", resultJson.getString("respMsg")+"");
//			boolean b = VivoSignUtils.verifySignature(m,  onlinePay.getPayInfoConfig().getAppkey());
			String respCode = resultJson.getString("respCode");
			if( StringUtils.isNotBlank(respCode) && "200".equals(respCode)){
				JSONObject data = new JSONObject();
				result.put("resultCode",0);
				result.put("errMsg","SUCCESS");
				data.put("vivoSignature", resultJson.getString("vivoSignature"));
				data.put("vivoOrder", resultJson.getString("vivoOrder"));
				data.put("orderAmount", resultJson.getString("orderAmount"));
				result.put("result", data);
			}else if(respCode.equals("400")){
				result.put("resultCode", 400);
				result.put("errMsg", "参数为空或者格式不正确");
			}else if(respCode.equals("402")){
				result.put("resultCode", 402);
				result.put("errMsg", "商户id非法，请检查");
			}else if(respCode.equals("403")){
				result.put("resultCode", 403);
				result.put("errMsg", "验签失败，请检查");
			}else if(respCode.equals("405")){
				result.put("resultCode", 405);
				result.put("errMsg", "cp订单号不唯一");
			}else if(respCode.equals("406")){
				result.put("resultCode", 406);
				result.put("errMsg", "appId非法，请检查");
			}else if(respCode.equals("500")){
				result.put("resultCode", 500);
				result.put("errMsg", "服务器内部错误，请稍后再试");
			}
    	
    	}
		return result.toJSONString();
	}
	
	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		Set<String> set = request.getParameterMap().keySet();
		for( String key : set ){
			respData.put(key, request.getParameter(key));
			resquestMap.put(key, (String)request.getParameter(key));
		}
		if(respData.size() > 0){
			onlinePay.setOrderId(respData.getString("storeOrder"));
			onlinePay.setActualAmount((int)(Double.parseDouble(respData.getString("orderAmount"))*100) + "");
			onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
			onlinePay.setChannelOrderId(respData.getString("vivoOrder"));
		}

	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		boolean isSign = false;
		try {
			String cpKey = onlinePay.getPayInfoConfig().getAppkey();
			isSign = VivoSignUtils.verifySignature(resquestMap, cpKey);
		} catch (Throwable e) {
			log.error("gionee RSA docheck ERROR!", e);
		}
		if(isSign){
			result.put("code", 2000);
			return getReturnSuccess();
		}else{
			result.put("code", 4006);
			result.put("errMsg", "验证错误！");
			return getReturnFailure();
		}
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
