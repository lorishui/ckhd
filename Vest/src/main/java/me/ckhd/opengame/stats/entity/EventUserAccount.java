package me.ckhd.opengame.stats.entity;

import me.ckhd.opengame.common.persistence.DataEntity;

/**
 * @ClassName EventUserAccount
 * @Description 用户交互事件类
 * @author liupei
 * @Date 2017年7月7日 上午10:06:49
 * @version 1.0.0
 */
public class EventUserAccount extends DataEntity<EventUserAccount> implements Cloneable {

    /**
     * @Field @serialVersionUID : 序列化id
     */
    private static final long serialVersionUID = -1575485042499539318L;

    private String ckappId;
    private String childAppId;
    private String userAccount;
    /**
     * 1登录 5注册 16一键注册-获取用户账号和密码 17一键注册-注册用户
     */
    private String type;
    /** 请求串 */
    private String requestData;
    private String responseData;
    /** 客户端ip **/
    private String clientIp;
    /** 平台0:android;1:ios **/
    private String platform;

    private String osVersion;

    private String channelId;
    private String childChannelId;
    private String phoneModel;
    private String packageName;
    private String idfv;
    private String sessionId;
    private String deviceId;
    private String executeTime;

    public String getCkappId() {
        return ckappId;
    }

    public void setCkappId(String ckappId) {
        this.ckappId = ckappId;
    }

    public String getChildAppId() {
        return childAppId;
    }

    public void setChildAppId(String childAppId) {
        this.childAppId = childAppId;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChildChannelId() {
        return childChannelId;
    }

    public void setChildChannelId(String childChannelId) {
        this.childChannelId = childChannelId;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getIdfv() {
        return idfv;
    }

    public void setIdfv(String idfv) {
        this.idfv = idfv;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(String executeTime) {
        this.executeTime = executeTime;
    }
}
