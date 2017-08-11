package me.ckhd.opengame.app.entity;

import java.util.Date;

import me.ckhd.opengame.common.persistence.DataEntity;


/**
 * AppUser  Entity app用户实体
 * @author wesley
 * @version 2015-07-15
 */
public class AppUser extends  DataEntity<AppUser>{
	 
		private static final long serialVersionUID = 1L;
		
		/**
		 * 用户识别码
		 */
		private String  imsi;
		
		/**
		 * 机器识别码
		 */
		private String  imei;
		/**
		 * 创酷APPID
		 */
		private String ckappId; 
		
		/**
		 * 应用ID
		 */
		private String appId;
		/**
		 * 渠道ID
		 */
		private String channelId;
		/**
		 * 事件发生时间（启动时间）
		 */
		private Date occurTime;
		
		
	    /****************附加 ***********************************/
		private String ckappName;
		private String appName;
		private String channelName;
		public String getImsi() {
			return imsi;
		}
		public void setImsi(String imsi) {
			this.imsi = imsi;
		}
		public String getImei() {
			return imei;
		}
		public void setImei(String imei) {
			this.imei = imei;
		}
		public String getCkappId() {
			return ckappId;
		}
		public void setCkappId(String ckappId) {
			this.ckappId = ckappId;
		}
		public String getAppId() {
			return appId;
		}
		public void setAppId(String appId) {
			this.appId = appId;
		}
		public String getChannelId() {
			return channelId;
		}
		public void setChannelId(String channelId) {
			this.channelId = channelId;
		}
		public Date getOccurTime() {
			return occurTime;
		}
		public void setOccurTime(Date occurTime) {
			this.occurTime = occurTime;
		}
		public String getCkappName() {
			return ckappName;
		}
		public void setCkappName(String ckappName) {
			this.ckappName = ckappName;
		}
		public String getAppName() {
			return appName;
		}
		public void setAppName(String appName) {
			this.appName = appName;
		}
		public String getChannelName() {
			return channelName;
		}
		public void setChannelName(String channelName) {
			this.channelName = channelName;
		}
		
}
