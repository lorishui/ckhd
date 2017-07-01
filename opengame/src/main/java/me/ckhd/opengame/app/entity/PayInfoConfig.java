package me.ckhd.opengame.app.entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.persistence.DataEntity;
import me.ckhd.opengame.common.utils.MyJsonUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class PayInfoConfig extends DataEntity<PayInfoConfig> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ckAppId; //创酷APPId
	private String carrierAppId;//MM,andgame的游戏ID
	private String channelId;//渠道Id
	private String paytype;//支付方式
	private String appid;//
	private String appkey;//游戏z
	private String notifyUrl;
	private String exInfo;
	private String remarks;
	private String[] exInfo_key;
	private String[] exInfo_value;
	private String addCkAppId;
	private Map<String,Object> exInfoMap = new HashMap<String,Object>();
	private String ckappname;
	private String payTypeName;
	private String appname;
	private String channelName;
	private double discount=1; //折扣率
	
	private String cpServerUrl; //cp服务器地址
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getExInfo() {
		return exInfo;
	}

	public void setExInfo(String exInfo) {
		this.exInfo = exInfo;
	}

	public String[] getExInfo_key() {
		return exInfo_key;
	}

	public void setExInfo_key(String[] exInfo_key) {
		this.exInfo_key = exInfo_key;
	}

	public String[] getExInfo_value() {
		return exInfo_value;
	}

	public void setExInfo_value(String[] exInfo_value) {
		this.exInfo_value = exInfo_value;
		this.setExInfo();
	}

	public void setExInfo(){
		Map<String,Object> map = new HashMap<String,Object>();
		if(exInfo==null || "".equals(exInfo)){
			String[] key = this.getExInfo_key();
			String[] value=this.getExInfo_value();
			if(key==null){
				return;
			}
			for(int i=0;i<key.length;i++){
				map.put(key[i], value[i]);
			}
			if(map!=null && map.size()>0){
				JSON json=(JSON)JSONObject.toJSON(map);
				String js=json.toString();
				this.setExInfo(js);
			}
		}
	}

	public String getCkAppId() {
		if(ckAppId==null && addCkAppId!=null){
			return addCkAppId;
		}
		return ckAppId;
	}

	public void setCkAppId(String ckAppId) {
		this.ckAppId = ckAppId;
	}


	public String getCarrierAppId() {
		return carrierAppId;
	}

	public void setCarrierAppId(String carrierAppId) {
		this.carrierAppId = carrierAppId;
	}

	@Override
	public String toString() {
		return "PayInfoConfig [ckAppId=" + ckAppId + ", carrierAppId="
				+ carrierAppId + ", channelId=" + channelId + ", paytype="
				+ paytype + ", appid=" + appid + ", appkey=" + appkey
				+ ", exInfo=" + exInfo + ", exInfo_key="
				+ Arrays.toString(exInfo_key) + ", exInfo_value="
				+ Arrays.toString(exInfo_value) + "]";
	}

	public String getCkappname() {
		return ckappname;
	}

	public void setCkappname(String ckappname) {
		this.ckappname = ckappname;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public Map<String, Object> getExInfoMap() {
		exInfoMap=MyJsonUtils.jsonStr2Map(exInfo);
		return exInfoMap;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAddCkAppId() {
		return addCkAppId;
	}

	public void setAddCkAppId(String addCkAppId) {
		this.addCkAppId = addCkAppId;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public String getCpServerUrl() {
		return cpServerUrl;
	}

	public void setCpServerUrl(String cpServerUrl) {
		this.cpServerUrl = cpServerUrl;
	}

	
}
