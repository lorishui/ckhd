package me.ckhd.opengame.user.model;

import me.ckhd.opengame.common.utils.EmojiUtils;
import me.ckhd.opengame.user.utils.JSONInterface;

import com.alibaba.fastjson.JSONObject;

public class Role extends JSONInterface{
	private static String index_ckAppd="a";
	private static String index_ckChannelId="b";
	private static String index_userId="c";
	private static String index_roleId="d";
	private static String index_uuid="e";
	private static String index_roleName="f";
	private static String index_serverId="g";
	private static String index_serverName ="h";
	private static String index_roleLevel="i";
	
	private String userId;
	private String ckChannelId;
	private String ckAppId;
	private String roleId;
	private String roleName;
	private String serverId;
	private String serverName;
	private int roleLevel;
	private String uuid;
	@Override
	public void pareJSON(Object obj) {
		JSONObject json = null;
		if(obj != null){
			if(obj.getClass().getSimpleName().equals(JSONObject.class.getSimpleName())){
				json = (JSONObject)obj;
				this.setUserId(json.getString(index_userId));
				this.setCkChannelId(json.getString(index_ckChannelId));
				this.setCkAppId(json.getString(index_ckAppd));
				this.setRoleId(json.getString(index_roleId));
				this.setRoleLevel(json.getInteger(index_roleLevel));
				this.setRoleName(EmojiUtils.filterEmoji(json.getString(index_roleName),""));
				this.setServerId(json.getString(index_serverId));
				this.setServerName(json.getString(index_serverName));
				this.setUuid(json.getString(index_uuid));
			}
			if(obj.getClass().getSimpleName().equals(String.class.getSimpleName())){
				json = JSONObject.parseObject(obj.toString());
				this.setUserId(json.getString(index_userId));
				this.setCkChannelId(json.getString(index_ckChannelId));
				this.setCkAppId(json.getString(index_ckAppd));
				this.setRoleId(json.getString(index_roleId));
				this.setRoleLevel(json.getInteger(index_roleLevel));
				this.setRoleName(EmojiUtils.filterEmoji(json.getString(index_roleName),""));
				this.setServerId(json.getString(index_serverId));
				this.setServerName(json.getString(index_serverName));
				this.setUuid(json.getString(index_uuid));
			}
		}
	}

	@Override
	public JSONObject buildJSON() {
		return null;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getCkAppId() {
		return ckAppId;
	}

	public void setCkAppId(String ckAppId) {
		this.ckAppId = ckAppId;
	}

	public String getCkChannelId() {
		return ckChannelId;
	}

	public void setCkChannelId(String ckChannelId) {
		this.ckChannelId = ckChannelId;
	}

	public int getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(int roleLevel) {
		this.roleLevel = roleLevel;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
