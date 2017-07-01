package me.ckhd.opengame.online.request.unionpay;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.request.BasePayCallBackRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayCallBackRequest extends BasePayCallBackRequest {
	Logger log = LoggerFactory.getLogger(BasePayCallBackRequest.class);
	
	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		//获取支付宝POST过来反馈信息
		log.info("unionpay callback start");

		try {
			httpRequest.setCharacterEncoding("ISO-8859-1");
			String encoding = httpRequest.getParameter(SDKConstants.param_encoding);
			// 获取银联通知服务器发送的后台通知参数
			Map<String, String> reqParam = new HashMap<String, String>();//去除空值的key
			
			Enumeration<?> temp = httpRequest.getParameterNames();
			if (null != temp) {
				while (temp.hasMoreElements()) {
					String en = (String) temp.nextElement();
					String value = httpRequest.getParameter(en);
					if( StringUtils.isNotBlank(value)){
						if(!en.equals("channelid") && !en.equals("ckappid")){
							reqParam.put(en, new String(value.getBytes("ISO-8859-1"), encoding)	);
						}
					}
				}
			}
			map.putAll(reqParam);//赋值
			log.info("unionpay callback content:"+reqParam.toString());
	
			//重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
			if (!SDKUtil.validate(reqParam, encoding)) {
				log.info("unionpay callback validation failure.");
				map.put("statusCode", "01");
			} else {
				log.info("unionpay callback validation success.");
				if(map.get("respCode") != null && map.get("respCode").equals("00")){
					map.put("statusCode", "00");
				}
			}
		} catch (Exception e) {
			log.info("unionpay callback throw exception",e);
			map.put("statusCode", "02");
		}
	}

	@Override
	public String getOrderId() {
		return map.get("orderId")==null?"":map.get("orderId").toString();
	}

	@Override
	public String getActualAmount() {
		int total_fee = map.get("txnAmt")==null?0:Integer.parseInt(map.get("txnAmt").toString());
		return String.valueOf(total_fee);
	}

	@Override
	public String getChannelOrderId() {
		return map.get("queryId")==null?"":map.get("queryId").toString();
	}
}
