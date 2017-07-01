/*
 * www.ckhd.me
 */
package me.ckhd.opengame.mmapi.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 响应MMIAP支付服务器bean
 */
@XmlRootElement(name="SyncAppOrderResp")
public class SyncAppOrderResp implements Serializable {
	private static final long serialVersionUID = 7123473855192948887L;

	private String TransactionID;
	
	private String MsgType;// 消息类型
	
	private String Version;// 版本号
	
	private Integer hRet;// 返回值

	public String getTransactionID() {
		return TransactionID;
	}

	@XmlElement(name="TransactionID")
	public void setTransactionID(String transactionID) {
		TransactionID = transactionID;
	}

	public String getMsgType() {
		return MsgType;
	}

	@XmlElement(name="MsgType")
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public String getVersion() {
		return Version;
	}

	@XmlElement(name="Version")
	public void setVersion(String version) {
		Version = version;
	}

	public Integer gethRet() {
		return hRet;
	}

	@XmlElement(name="hRet")
	public void sethRet(Integer hRet) {
		this.hRet = hRet;
	}
}
