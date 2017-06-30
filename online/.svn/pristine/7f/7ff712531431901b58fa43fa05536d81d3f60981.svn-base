package me.ckhd.opengame.online.response.letv;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

public class LetvVerify {
	/**
	 * 验证签名
	 * @param request
	 * @return
	 */
	public static boolean verify(HttpServletRequest request,String secretKey) {
		Map<String, String> paramSortMap = getParamSortMap(request);
		String sign = request.getParameter("sign");
		String source = getSourceFromMap(paramSortMap)+"&key="+secretKey;
		String mysign = MD5Utils.MD5Encode(source, "utf-8");
		System.out.println("mysign:"+mysign);
		
		String payResult = paramSortMap.get("trade_result");
		if("TRADE_SUCCESS".equals(payResult)) {
//			System.out.println("支付成功");
		}
		return mysign.equals(sign);
	}
	
	/**
	 * 获取根据key排序的MAP
	 * @param request
	 * @return
	 */
	public static Map<String, String> getParamSortMap(HttpServletRequest request) {
		Map<String, String[]> params = request.getParameterMap();
		Map<String, String> param = new TreeMap<String, String>();
		for (String key : params.keySet()) {
			if("ckappid".equals(key) || "channelid".equals(key)){
				continue;
			}
			String[] values = params.get(key);
			for (int i = 0; i < values.length; i++) {
				String value = null;
				try {
					value = URLDecoder.decode(values[i], "utf-8");
				} catch (UnsupportedEncodingException e) {
				}
				param.put(key, value);
			}
		}
		
		return param;
	}
	
	/**
	 * 生成签名原串
	 * @param queryMap
	 * @return
	 */
	public static String getSourceFromMap(Map<String, String> queryMap) {
		if (null == queryMap || queryMap.isEmpty()) {
			return null;
		}
		Object[] objArr = queryMap.keySet().toArray();

		StringBuilder buf = new StringBuilder();
		int i = 0;
		for (Object key : objArr) {
			if((!"sign".equals(key)) && (!"cooperator_order_no".equals(key)) && (!"extra_info".equals(key))&&(!"original_price".equals(key))) {
				buf.append(i++ == 0 ? "" : "&").append(key).append("=").append(queryMap.get(key));
			}
		}
		return buf.toString();
	}
	
	/**
	 * 生成签名原串
	 * @param queryMap
	 * @return
	 */
	public static String getSourceFromMapV2(Map<String, String> queryMap) {
		if (null == queryMap || queryMap.isEmpty()) {
			return null;
		}
		Object[] objArr = queryMap.keySet().toArray();
		Arrays.sort(objArr);

		StringBuilder buf = new StringBuilder();
		for (int i=0;i<objArr.length;i++) {
			if((!"sign".equals(objArr[i])) && (!"channelid".equals(objArr[i])) && (!"ckappid".equals(objArr[i])) ) {
				buf.append(objArr[i]).append("=").append(queryMap.get(objArr[i])+"&");
			}
		}
		buf.setLength(buf.length()-1);
		return buf.toString();
	}
	
	/**
	 * 验证签名
	 * @param request
	 * @return
	 */
	public static boolean verifyV2(HttpServletRequest request,String secretKey) {
		Map<String, String> paramSortMap = getParamSortMap(request);
		String sign = request.getParameter("sign");
		String source = getSourceFromMapV2(paramSortMap)+"&key="+secretKey;
		String mysign = MD5Utils.MD5Encode(source, "utf-8");
		System.out.println("mysign:"+mysign);
		
		String payResult = paramSortMap.get("trade_result");
		if("TRADE_SUCCESS".equals(payResult)) {
//			System.out.println("支付成功");
		}
		return mysign.equals(sign);
	}
}
