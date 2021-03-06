package me.ckhd.opengame.user.entity;

import me.ckhd.opengame.common.persistence.DataEntity;


public class UserInfo extends  DataEntity<UserInfo> implements Cloneable{

	private static final long serialVersionUID = 1L;
	private String userId;
	private String userAccount;
	private String pwd;
	private String bindMobile;
	private String bindEmail;
	private int isUse;
	private String ckAppId;
	private String index;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getBindMobile() {
		return bindMobile;
	}
	public void setBindMobile(String bindMobile) {
		this.bindMobile = bindMobile;
	}
	public String getBindEmail() {
		return bindEmail;
	}
	public void setBindEmail(String bindEmail) {
		this.bindEmail = bindEmail;
	}
	public int getIsUse() {
		return isUse;
	}
	public void setIsUse(int isUse) {
		this.isUse = isUse;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getCkAppId() {
		return ckAppId;
	}
	public void setCkAppId(String ckAppId) {
		this.ckAppId = ckAppId;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
	    return super.clone();
	}
	
    public UserInfo cloneUser() throws CloneNotSupportedException {
        return (UserInfo)clone();
    }
}
