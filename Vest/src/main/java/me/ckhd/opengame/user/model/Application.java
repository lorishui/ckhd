package me.ckhd.opengame.user.model;

import com.alibaba.fastjson.JSONObject;

import me.ckhd.opengame.user.utils.JSONInterface;

/**
 * @ClassName Application
 * @Description app环境变量类,所有请求自带
 * @author liupei
 * @Date 2017年8月4日 上午11:38:44
 * @version 1.0.0
 */
public class Application extends JSONInterface {

    private static String index_ckAppId = "a";
    private static String index_childAppid = "b";
    private static String index_channelId = "c";
    private static String index_childChannelId = "d";
    private static String index_platform = "e";
    private static String index_deviceId = "f";
    private static String index_phoneModel = "g";
    private static String index_packageName = "h";
    private static String index_idfv = "i";
    private static String index_osVersion = "j";

    private String ckAppId;
    private String childAppid;
    private String channelId;
    private String childChannelId;
    private String platform;
    private String deviceId;
    private String phoneModel;
    private String packageName;
    private String idfv;
    private String osVersion;

    @Override
    public void pareJSON(Object obj) {
        JSONObject json = null;
        if (obj != null) {
            if (obj.getClass().getSimpleName().equals(JSONObject.class.getSimpleName())) {
                json = (JSONObject)obj;
            }
            if (obj.getClass().getSimpleName().equals(String.class.getSimpleName())) {
                json = JSONObject.parseObject(obj.toString());
            }
            this.setCkAppId(json.getString(index_ckAppId));
            this.setChildAppid(json.getString(index_childAppid));
            this.setChannelId(json.getString(index_channelId));
            this.setChildChannelId(json.getString(index_childChannelId));
            this.setDeviceId(json.getString(index_deviceId));
            this.setIdfv(json.getString(index_idfv));
            this.setOsVersion(json.getString(index_osVersion));
            this.setPackageName(json.getString(index_packageName));
            this.setPhoneModel(json.getString(index_phoneModel));
            this.setPlatform(json.getString(index_platform));
        }
    }

    @Override
    public JSONObject buildJSON() {
        return null;
    }

    public String getCkAppId() {
        return ckAppId;
    }

    public void setCkAppId(String ckAppId) {
        this.ckAppId = ckAppId;
    }

    public String getChildAppid() {
        return childAppid;
    }

    public void setChildAppid(String childAppid) {
        this.childAppid = childAppid;
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

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

}
