package me.ckhd.opengame.andapi.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 响应和游戏支付服务器bean
 */
@XmlRootElement(name = "response")
public class OrderResponse implements  Serializable{

	private static final long serialVersionUID = 1L;
	public  static final String SUCCESS_MSG = "Successful";
	/**
	 * 0   -通知成功；
	 * 其它-其他错误

	 */
	private int hRet;
	/**
	 * 响应的消息，比如“Successful”或是合作方自定义的失败原因
	 */
	private String message;
	
	public int gethRet() {
		return hRet;
	}
	@XmlElement(name="hRet")
	public void sethRet(int hRet) {
		this.hRet = hRet;
	}
	public String getMessage() {
		return message;
	}
	@XmlElement(name="message")
	public void setMessage(String message) {
		this.message = message;
	}

	
	public String toPaynotifyResponse() {
        StringBuilder builder = new StringBuilder();
            builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<response>")
                .append("<hRet>"+this.gethRet()+"</hRet>")
                .append("<message>"+(this.getMessage()!=null?this.getMessage():(this.gethRet() == 0 ? "successful" : "failure"))+"</message>")
                .append("</response>");
        return builder.toString();
    }
}
