package me.ckhd.opengame.user.model;

import me.ckhd.opengame.user.utils.JSONInterface;

import com.alibaba.fastjson.JSONObject;

public class UserInfo extends JSONInterface{
	private static String index_userAccount="a";
	private static String index_password="b";
	private static String index_type="c";
	private static String index_email="d";
	private static String index_phoneNumber="e";
	private static String index_operate="f";
	private static String index_oldPassword="g";
	private static String index_ckAppId="h";
	private static String index_showPassword="i";
	
	private String userAccount;
	private String password;
	private Integer type;
	private String email;
	private String phoneNumber;
	private Integer operate;
	private String oldPassword;
	private String ckAppId;
	private String showPassword;
	
	@Override
	public void pareJSON(Object obj) {
		JSONObject json = null;
		if(obj != null){
			if(obj.getClass().getSimpleName().equals(JSONObject.class.getSimpleName())){
				json = (JSONObject)obj;
				this.setEmail(json.getString(index_email));
				this.setOperate(json.getInteger(index_operate));
				this.setPassword(json.getString(index_password));
				this.setPhoneNumber(json.getString(index_phoneNumber));
				this.setUserAccount(json.getString(index_userAccount));
				this.setOldPassword(json.getString(index_oldPassword));
				this.setCkAppId(json.getString(index_ckAppId));
			}
			if(obj.getClass().getSimpleName().equals(String.class.getSimpleName())){
				json = JSONObject.parseObject(obj.toString());
				this.setEmail(json.getString(index_email));
				this.setOperate(json.getInteger(index_operate));
				this.setPassword(json.getString(index_password));
				this.setPhoneNumber(json.getString(index_phoneNumber));
				this.setUserAccount(json.getString(index_userAccount));
				this.setOldPassword(json.getString(index_oldPassword));
				this.setCkAppId(json.getString(index_ckAppId));
			}
		}
	}
	
	@Override
	public JSONObject buildJSON() {
		JSONObject json = new JSONObject();
		json.put(index_email, this.getEmail());
		json.put(index_operate, this.getOperate());
		json.put(index_password, this.getPassword());
		json.put(index_phoneNumber, this.getPhoneNumber());
		json.put(index_type, this.getType());
		json.put(index_userAccount, this.getUserAccount());
		json.put(index_showPassword, this.getShowPassword());
		return json;
	}

	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Integer getOperate() {
		return operate;
	}
	public void setOperate(Integer operate) {
		this.operate = operate;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getCkAppId() {
		return ckAppId;
	}

	public void setCkAppId(String ckAppId) {
		this.ckAppId = ckAppId;
	}

	public String getShortName(){
		return "user";
	}

	public String getShowPassword() {
		return showPassword;
	}

	public void setShowPassword(String showPassword) {
		this.showPassword = showPassword;
	}
}
