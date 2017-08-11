package me.ckhd.opengame.user.model;

import me.ckhd.opengame.user.utils.JSONInterface;

import com.alibaba.fastjson.JSONObject;

public class Version extends JSONInterface {

    private static String index_number = "a";
    private static String index_language = "b";
    private static String index_device_id = "c";
    private static String index_ck_app_id = "d";
    private static String index_child_app_id = "e";
    private static String index_channel_id = "f";
    private static String index_child_channel_id = "h";
    private String number;
    private String language;// zh_TW,zh_CN
    private String deviceId;
    private String ckAppId;
    private String childAppId;
    private String channelId;
    private String childChannelId;

    @Override
    public void pareJSON(Object obj) {
        JSONObject json = null;
        if (obj != null) {
            if (obj.getClass().getSimpleName().equals(JSONObject.class.getSimpleName())) {
                json = (JSONObject) obj;
                this.setNumber(json.getString(index_number));
                if (json.containsKey(index_language)) {
                    this.setLanguage(json.getString(index_language));
                }
                if (json.containsKey(index_device_id)) {
                    this.setLanguage(json.getString(index_device_id));
                }
                if (json.containsKey(index_ck_app_id)) {
                    this.setLanguage(json.getString(index_ck_app_id));
                }
                if (json.containsKey(index_child_app_id)) {
                    this.setLanguage(json.getString(index_child_app_id));
                }
                if (json.containsKey(index_channel_id)) {
                    this.setLanguage(json.getString(index_channel_id));
                }
                if (json.containsKey(index_child_channel_id)) {
                    this.setLanguage(json.getString(index_child_channel_id));
                }
            }
            if (obj.getClass().getSimpleName().equals(String.class.getSimpleName())) {
                json = JSONObject.parseObject(obj.toString());
                this.setNumber(json.getString(index_number));
                if (json.containsKey(index_language)) {
                    this.setLanguage(json.getString(index_language));
                }
                if (json.containsKey(index_device_id)) {
                    this.setLanguage(json.getString(index_device_id));
                }
                if (json.containsKey(index_ck_app_id)) {
                    this.setLanguage(json.getString(index_ck_app_id));
                }
                if (json.containsKey(index_child_app_id)) {
                    this.setLanguage(json.getString(index_child_app_id));
                }
                if (json.containsKey(index_channel_id)) {
                    this.setLanguage(json.getString(index_channel_id));
                }
                if (json.containsKey(index_child_channel_id)) {
                    this.setLanguage(json.getString(index_child_channel_id));
                }
            }
        }
    }

    @Override
    public JSONObject buildJSON() {
        JSONObject json = new JSONObject();
        json.put(index_number, this.getNumber());
        return json;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCkAppId() {
        return ckAppId;
    }

    public void setCkAppId(String ckAppId) {
        this.ckAppId = ckAppId;
    }

    public String getChildAppId() {
        return childAppId;
    }

    public void setChildAppId(String childAppId) {
        this.childAppId = childAppId;
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

}
