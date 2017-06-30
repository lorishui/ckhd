package me.ckhd.opengame.online.entity;

import java.io.UnsupportedEncodingException;

import me.ckhd.opengame.common.persistence.DataEntity;
import me.ckhd.opengame.common.utils.EmojiUtils;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSONObject;

public class RoleInfo extends  DataEntity<RoleInfo>{

	private static final long serialVersionUID = -855006282826450510L;
	private String userId;
	private String ckChannelId;
	private String ckAppId;
	private String roleId;
	private String roleName;
	private String roleNameResource;
	private String serverId;
	private String serverName;
	private int roleLevel;
	private String uuid;
	//新加字段
	private String childCkAppId;
	private String childChannelId;
	private String zoneId;
	private int type;
	private String idfv;//
	
	private static String index_ckAppd="a";
	private static String index_ckChannelId="b";
	private static String index_userId="c";
	private static String index_roleId="d";
	private static String index_uuid="e";
	private static String index_roleName="f";
	private static String index_serverId="g";
	private static String index_serverName ="h";
	private static String index_roleLevel="i";
	//新加字段
	private static String index_childCkAppId="j";
	private static String index_childChannelId="k";
	private static String index_zoneId="l";
	private static String index_type="m";
	private static String index_idfv="n";

	public void pareJSON(Object obj) {
		JSONObject json = null;
		if(obj != null){
			if(obj.getClass().getSimpleName().equals(JSONObject.class.getSimpleName())){
				json = (JSONObject)obj;
				this.setUserId(json.getString(index_userId));
				this.setCkChannelId(json.getString(index_ckChannelId));
				this.setCkAppId(json.getString(index_ckAppd));
				this.setRoleId(json.getString(index_roleId));
				if(json.containsKey(index_roleLevel)){
					this.setRoleLevel(json.getInteger(index_roleLevel));
				}
				if(json.containsKey(index_roleName)){
					this.setRoleName(EmojiUtils.filterEmoji(json.getString(index_roleName),"z"));
					try {
						this.setRoleNameResource(new Base64().encodeToString(json.getString(index_roleName).getBytes("utf-8")));
					} catch (UnsupportedEncodingException e) {
					}
				}
				this.setServerId(json.getString(index_serverId));
				this.setServerName(json.getString(index_serverName));
				this.setUuid(json.getString(index_uuid));
				this.setChildCkAppId(json.containsKey(index_childCkAppId)?json.getString(index_childCkAppId):"1");
				this.setChildChannelId(json.containsKey(index_childChannelId)?json.getString(index_childChannelId):"1");
				this.setZoneId(json.containsKey(index_zoneId)?json.getString(index_zoneId):"1");
				this.setType(json.containsKey(index_type)?json.getInteger(index_type):2);
				this.setIdfv(json.get(index_idfv)!=null?json.getString(index_idfv):"");
			}
			if(obj.getClass().getSimpleName().equals(String.class.getSimpleName())){
				json = JSONObject.parseObject(obj.toString());
				this.setUserId(json.getString(index_userId));
				this.setCkChannelId(json.getString(index_ckChannelId));
				this.setCkAppId(json.getString(index_ckAppd));
				this.setRoleId(json.getString(index_roleId));
				if(json.containsKey(index_roleLevel)){
					this.setRoleLevel(json.getInteger(index_roleLevel));
				}
				if(json.containsKey(index_roleName)){
					this.setRoleName(EmojiUtils.filterEmoji(json.getString(index_roleName),""));
					try {
						this.setRoleNameResource(new Base64().encodeToString(json.getString(index_roleName).getBytes("utf-8")));
					} catch (UnsupportedEncodingException e) {
					}
				}
				this.setServerId(json.getString(index_serverId));
				this.setServerName(json.getString(index_serverName));
				this.setUuid(json.getString(index_uuid));
				this.setChildCkAppId(json.containsKey(index_childCkAppId)?json.getString(index_childCkAppId):"1");
				this.setChildChannelId(json.containsKey(index_childChannelId)?json.getString(index_childChannelId):"1");
				this.setZoneId(json.containsKey(index_zoneId)?json.getString(index_zoneId):"1");
				this.setType(json.containsKey(index_type)?json.getInteger(index_type):2);
				this.setIdfv(json.get(index_idfv)!=null?json.getString(index_idfv):"");
			}
		}
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

	public String getChildCkAppId() {
		return childCkAppId;
	}

	public void setChildCkAppId(String childCkAppId) {
		this.childCkAppId = childCkAppId;
	}

	public String getChildChannelId() {
		return childChannelId;
	}

	public void setChildChannelId(String childChannelId) {
		this.childChannelId = childChannelId;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getRoleNameResource() {
		return roleNameResource;
	}

	public void setRoleNameResource(String roleNameResource) {
		this.roleNameResource = roleNameResource;
	}

	public String getIdfv() {
		return idfv;
	}

	public void setIdfv(String idfv) {
		this.idfv = idfv;
	}
}
