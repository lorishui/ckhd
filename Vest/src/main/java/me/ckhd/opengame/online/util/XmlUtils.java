package me.ckhd.opengame.online.util;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;


public class XmlUtils {
	
	static Logger logger = LoggerFactory.getLogger(XmlUtils.class);
	/**请求参数Map转xml
	 *2015-9-8 下午6:02:11
	 */
	public static String toXml(Map<String,Object> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (String key:params.keySet()) {
			sb.append("<"+key+">");
			sb.append(params.get(key));
			sb.append("</"+key+">");
		}
		sb.append("</xml>");
		logger.info(String.format("调用接口数据:[%s]", sb.toString()));
		return sb.toString();
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
					if ("xml".equals(nodeName) == false) {
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
}
