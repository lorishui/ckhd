package me.ckhd.opengame.evnet.entity;

import me.ckhd.opengame.common.persistence.DataEntity;

	public class AppEventStat extends DataEntity<AppEventStat>{
	
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * id
		 */
		private String id;
		/**
		 * 创酷ckApp id
		 */
		private String ckAppId;
		/**
		 * 创酷app_id
		 */
		private String appId;
		/**
		 * statsTime
		 */
		private String statsTime;
		/**
		 * 渠道号
		 */
		private String ckChannelId;
		/**
		 * 統計开始時間
		 */
		private String startDate;
		/**
		 * 統計結束時間
		 */
		private String endDate;
		/**
		 * 統計類型
		 */
		private String type;
		/**
		 * 統計數量
		 */
		private Integer statsNum;
		/**
		 * 新增
		 */
		private String add;
		/**
		 * 活跃
		 */
		private String active;
		/**
		 * MM收入
		 */
		private String mmSuccPrice;
		/**
		 * 和游戏收入
		 */
		private String andSuccPrice;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getCkAppId() {
			return ckAppId;
		}
		public void setCkAppId(String ckAppId) {
			this.ckAppId = ckAppId;
		}
		public String getAppId() {
			return appId;
		}
		public void setAppId(String appId) {
			this.appId = appId;
		}
		public String getCkChannelId() {
			return ckChannelId;
		}
		public void setCkChannelId(String ckChannelId) {
			this.ckChannelId = ckChannelId;
		}
		public String getStartDate() {
			return startDate;
		}
		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}
		public String getEndDate() {
			return endDate;
		}
		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}
		public String getStatsTime() {
			return statsTime;
		}
		public void setStatsTime(String statsTime) {
			this.statsTime = statsTime;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public Integer getStatsNum() {
			return statsNum;
		}
		public void setStatsNum(Integer statsNum) {
			this.statsNum = statsNum;
		}
		
		public String getAdd() {
			return add;
		}
		public void setAdd(String add) {
			this.add = add;
		}
		public String getActive() {
			return active;
		}
		public void setActive(String active) {
			this.active = active;
		}
		
		public String getMmSuccPrice() {
			return mmSuccPrice;
		}
		public void setMmSuccPrice(String mmSuccPrice) {
			this.mmSuccPrice = mmSuccPrice;
		}
		public String getAndSuccPrice() {
			return andSuccPrice;
		}
		public void setAndSuccPrice(String andSuccPrice) {
			this.andSuccPrice = andSuccPrice;
		}
		@Override
		public String toString() {
			return "AppEventStat [id=" + id + ", ckAppId=" + ckAppId
					+ ", appId=" + appId + ", statsTime=" + statsTime
					+ ", ckChannelId=" + ckChannelId + ", startDate="
					+ startDate + ", endDate=" + endDate + ", type=" + type
					+ ", statsNum=" + statsNum + ", add=" + add + ", active="
					+ active + ", mmSuccPrice=" + mmSuccPrice
					+ ", andSuccPrice=" + andSuccPrice + "]";
		}
		
	
		
	}
