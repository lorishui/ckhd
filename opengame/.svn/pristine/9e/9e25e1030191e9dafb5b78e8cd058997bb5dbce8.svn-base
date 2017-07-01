package me.ckhd.opengame.woapi.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PayBeanUtils {
    
    private static final SAXParserFactory FACTORY = SAXParserFactory.newInstance();
    
    private static SAXParser parser;
    
    static {
        try {
            parser = FACTORY.newSAXParser();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
    

    static class SimpleHandler extends  DefaultHandler {

        public Map<String, String> obj;
        
        private String preTag;
        
        private String preValue;
        
        @Override
        public void startDocument() throws SAXException {
            obj = new HashMap<String, String>();
        }

        @Override
        public void startElement(String uri, String localName,
                String qName, Attributes attributes) throws SAXException {
            preTag = qName;
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            if (preTag != null && preValue != null) {
                obj.put(preTag, preValue);
                preTag = null;
                preValue = null;
            }
        }
        
        @Override  
        public void characters(char[] ch, int start, int length) throws SAXException {  
            preValue = new String(ch, start, length);
        }
        
        public Map<String, String> getObj() {
            return obj;
        }
    }
    
    public static Map<String, String> parse(InputStream in) {
        try {
            SimpleHandler handler = new SimpleHandler();
            parser.parse(in, handler);
            return handler.obj;
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String toValidateOrderidResponse(Map<String, Object> bean) {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("<paymessages>");
        for (Entry<String, Object> entry : bean.entrySet()) {
            builder.append("<")
                .append(entry.getKey())
                .append(">")
                .append(entry.getValue() == null ? "" : entry.getValue().toString())
                .append("</")
                .append(entry.getKey())
                .append(">");
        }
        builder.append("</paymessages>");
        return builder.toString();
    }
    
    public static String toPaynotifyResponse(Object rootValue) {
        StringBuilder builder = new StringBuilder();
            builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<callbackRsp>")
                .append(rootValue == null ? "" : rootValue.toString())
                .append("</callbackRsp>");
        return builder.toString();
    }
}
