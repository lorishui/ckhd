package me.ckhd.opengame.online.request.andgame;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import me.ckhd.opengame.online.request.BasePayCallBackRequest;
import me.ckhd.opengame.online.util.XmlUtils;

public class PayCallBackRequest extends BasePayCallBackRequest {

	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		map=decodeXml(callBackCode);
	}
	
	/**
	 * 向微信sdk服务器获取预支付订单》》解析请求结果
	 */
	public static Map<String, Object> decodeXml(String content) {

		try {
			Map<String, Object> xml = new HashMap<String, Object>();
			// 获得pull解析器工厂
			XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();

			// 获取XmlPullParser的实例
			XmlPullParser parser = pullParserFactory.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String nodeName = parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if ("xml".equals(nodeName) == false && "request".equals(nodeName)==false) {
						xml.put(nodeName, parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event = parser.next();
			}
			return xml;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@Override
	public String getOrderId() {
		String orderId = map.get("cpparam")==null?null:map.get("cpparam").toString();
		return orderId;
	}

	@Override
	public String getActualAmount() {
		return null;
	}

	@Override
	public String getChannelOrderId() {
		return null;
	}
}
