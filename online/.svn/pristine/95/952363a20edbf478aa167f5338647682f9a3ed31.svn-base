package me.ckhd.opengame.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSON;

public class XmlUtils {
	
	
	public static Map<String,Object> Str2Map(String xmlStr){
		Document document=null;
		try {
			document = DocumentHelper.parseText(xmlStr);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		return Dom2Map(document);
	}
	
	public static Map<String, Object> Dom2Map(Document doc){
		Map<String, Object> map = new HashMap<String, Object>();
		if(doc == null)
			return map;
		Element root = doc.getRootElement();
		for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
			Element e = (Element) iterator.next();
			List list = e.elements();
			if(list.size() > 0){
				map.put(e.getName(), Dom2Map(e));
			}else
				map.put(e.getName(), e.getText());
		}
		return map;
	}
	
	public static void main(String[] args) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><callbackReq><orderid>XXX</orderid><ordertime>XXX</ordertime><cpid>XXX</cpid><appid>XXX</appid><fid>XXX</fid><consumeCode>XXX</consumeCode><payfee>XXX</payfee><payType>XXX</payType><hRet>XXX</hRet><status>XXX</status><signMsg>XXX</signMsg></callbackReq>";
		System.out.println("xml=========="+JSON.toJSON(Str2Map(xml)).toString());
	}

	public static Map Dom2Map(Element e){
		Map	map = new HashMap();
		List list = e.elements();
		if(list.size() > 0){
			for (int i = 0;i < list.size(); i++) {
				Element iter = (Element) list.get(i);
				List mapList = new ArrayList();
				
				if(iter.elements().size() > 0){
					Map m = Dom2Map(iter);
					if(map.get(iter.getName()) != null){
						Object obj = map.get(iter.getName());
						if(!obj.getClass().getName().equals("java.util.ArrayList")){
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(m);
						}
						if(obj.getClass().getName().equals("java.util.ArrayList")){
							mapList = (List) obj;
							mapList.add(m);
						}
						map.put(iter.getName(), mapList);
					}else
						map.put(iter.getName(), m);
				}
				else{
					if(map.get(iter.getName()) != null){
						Object obj = map.get(iter.getName());
						if(!obj.getClass().getName().equals("java.util.ArrayList")){
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(iter.getText());
						}
						if(obj.getClass().getName().equals("java.util.ArrayList")){
							mapList = (List) obj;
							mapList.add(iter.getText());
						}
						map.put(iter.getName(), mapList);
					}else
						map.put(iter.getName(), iter.getText());
				}
			}
		}else
			map.put(e.getName(), e.getText());
		return map;
	}

}

