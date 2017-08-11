package me.ckhd.opengame.app.entity;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import me.ckhd.opengame.common.persistence.DataEntity;
import me.ckhd.opengame.common.utils.MyJsonUtils;

public class PayCodeConfig  extends  DataEntity<PayCodeConfig> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String channelId;
	private String appid;//MM,andgame的游戏ID
	private String ckAppId;
	private String childCkAppId;
	private String version;
	private String productId;
	private String productName;
	private String price;
	private String paytype;
	private String remark;
	private String exInfo;
	
	
	private String[] exInfo_key;
	private String[] exInfo_value;
	/**
	 * channel 's  NAME.
	 */
	private String channelName;
	
	/**
	 * paytype 's  NAME.
	 */
	private String paytypeName;

	/**
	 * mm app name
	 */
	private String mmAppName;
	/**
	 * app 's NAME
	 */
	private String appName;
	
	private String addckAppId;
	
	public Map<String,Object> exInfoMap = new HashMap<String, Object>();
	
	public PayCodeConfig() {
	}
	
	public PayCodeConfig(String ckAppId) {
		this.ckAppId=ckAppId;
	}
	
	public PayCodeConfig(String ckAppId,String appid,String channelid,String paytype) {
		this.ckAppId=ckAppId;
		this.appid = appid;
		this.channelId = channelid;
		this.paytype = paytype;
	}
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getCkAppId() {
		if(ckAppId==null && addckAppId!=null){
			return addckAppId;
		}
		return ckAppId;
	}

	public void setCkAppId(String ckAppId) {
		this.ckAppId = ckAppId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getExInfo() {
		return exInfo;
	}

	public void setExInfo(String exInfo) {
		this.exInfo = exInfo;
	}

	@Override
	public String toString() {
		return "PayInfoConfig [channelId=" + channelId + ", ckAppId=" + ckAppId
				+ ", version=" + version + ", productId=" + productId
				+ ", name=" + channelName + ", price=" + price + ", remark=" + remark
				+ ", exInfo=" + exInfo + "]";
	}

	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
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
		}
		if(map!=null && map.size()>0){
			JSON json=(JSON)JSONObject.toJSON(map);
			String js=json.toString();
			this.setExInfo(js);
		}
	}

	public String getAddckAppId() {
		return addckAppId;
	}

	public void setAddckAppId(String addckAppId) {
		this.addckAppId = addckAppId;
	}

	public Map<String, Object> getExInfoMap() {
		exInfoMap=MyJsonUtils.jsonStr2Map(exInfo);
		return exInfoMap;
	}

	public void setExInfoMap(Map<String, Object> exInfoMap) {
		this.exInfoMap = exInfoMap;
	}


	public String getPaytypeName() {
		return paytypeName;
	}

	public void setPaytypeName(String paytypeName) {
		this.paytypeName = paytypeName;
	}

	public String getMmAppName() {
		return mmAppName;
	}

	public void setMmAppName(String mmAppName) {
		this.mmAppName = mmAppName;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getChildCkAppId() {
		return childCkAppId;
	}

	public void setChildCkAppId(String childCkAppId) {
		this.childCkAppId = childCkAppId;
	}

}
