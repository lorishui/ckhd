package me.ckhd.opengame.online.entity;

import java.util.Map;

import me.ckhd.opengame.common.persistence.DataEntity;

/**
 * 网游登陆接收实体类
 * 
 * @author leo
 *
 */
public class OnlineUser  extends  DataEntity<OnlineUser> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String uid;
	private String sid;
	private String userName;
	private String ckAppId;
	private String channelId;
	private String token;
	private String appVerifyInfo;
	private String version;
	private String channelUserContent;
	private String loginType = "1";
	
	private Map<String,Object> loginParam;
	
	public OnlineUser(){
		
	}
	
	public OnlineUser(String _uid,String _token,String _channelId,String _ckappid){
		this.uid=_uid;
		this.token=_token;
		this.channelId=_channelId;
		this.ckAppId=_ckappid;
	}
	
	//错误提示
	private String errMsg;
	
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getCkAppId() {
		return ckAppId;
	}
	public void setCkAppId(String ckAppId) {
		this.ckAppId = ckAppId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	@Override
	public String toString() {
		return "OnlineUser [uid=" + uid + ", sid=" + sid + ", userName="
				+ userName + ", ckAppId=" + ckAppId + ", channelId="
				+ channelId + ", token=" + token + ", appVerifyInfo="
				+ appVerifyInfo + ", version=" + version
				+ ", channelUserContent=" + channelUserContent + ", loginType="
				+ loginType + ", loginParam=" + loginParam + ", errMsg="
				+ errMsg + "]";
	}

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getChannelUserContent() {
		return channelUserContent;
	}

	public void setChannelUserContent(String channelUserContent) {
		this.channelUserContent = channelUserContent;
	}

	public String getAppVerifyInfo() {
		return appVerifyInfo;
	}

	public void setAppVerifyInfo(String appVerifyInfo) {
		this.appVerifyInfo = appVerifyInfo;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Map<String, Object> getLoginParam() {
		return loginParam;
	}

	public void setLoginParam(Map<String, Object> loginParam) {
		this.loginParam = loginParam;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
