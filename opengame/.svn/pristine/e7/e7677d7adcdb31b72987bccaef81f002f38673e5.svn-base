package me.ckhd.opengame.drds.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import me.ckhd.opengame.common.persistence.DataEntity;

public class EventEntity extends DataEntity<EventEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@MyAnnotation(name = "SID")
	private String sid;// 会话标识
	
	@MyAnnotation(name = "SID_INDEX")
	private int sidIndex;// 当前会话序号

	@MyAnnotation(name = "IMEI")
	private String imei; // IMEI号

	@MyAnnotation(name = "ACCID")
	private String iccid; // ICCID号

	@MyAnnotation(name = "TYPE")
	private long type; // 事件类型

	@MyAnnotation(name = "CKAPPID")
	private String ckappid;// 创酷APPID

	@MyAnnotation(name = "MM_APP_ID")
	private String appid;// APPID

	@MyAnnotation(name = "CHANNELID")
	private String channelid;// 创酷统一渠道号

	@MyAnnotation(name = "NET_TYPE")
	private String netType;// 当前网络类型

	@MyAnnotation(name = "OCCUR_TIME")
	private long occurTime = new Date().getTime();// 发生时间

	//	@MyAnnotation(name = "INSERT_TIME") 前端不会传这个key del 11/16/2016
	private Date insertTime = new Date();// 入库时间

	// 启动事件
	@MyAnnotation(name = "VERSION_NAME")
	private String versioName;// 版本号

	@MyAnnotation(name = "PHONE_MODEL")
	private String phoneModel;// 机型

	@MyAnnotation(name = "OS_VERSION")
	private String osVersion;// 操作系统版本

	@MyAnnotation(name = "LANG")
	private String lang; // 系统语言

	@MyAnnotation(name = "SDK_VERSION")
	private String sdkVersion;// SDK版本

	// 退出事件
	@MyAnnotation(name = "EXIT_TYPE")
	private String exitType;// 退出类型

	// 支付事件
	@MyAnnotation(name = "PAY_SDK")
	private String paySdk;// String

	@MyAnnotation(name = "PAY_CODE")
	private String payCode;// 代码点

	@MyAnnotation(name = "PAY_NUMBER")
	private String payNumber;// 购买数量

	// ADD
	@MyAnnotation(name = "IMSI")
	private String imsi;// IMSI
	
	@MyAnnotation(name = "ANDROID_ID")
	private String androidid;// ANDROID_ID
	
	@MyAnnotation(name = "SIGN_MD5")
	private String signMD5;// 签名MD5

	// 信任的签名，1信任，0不信任，默认值1
	private int trustSign;
	
	private String ip;

	// 省份
	private String proviceName;

	// 城市
	private String cityName;

	// ADD 11/16/2016
	@MyAnnotation(name = "mcc")
	private int mcc ;
	@MyAnnotation(name = "mnc")
	private int mnc ;
	@MyAnnotation(name = "lac")
	private int lac ;
	@MyAnnotation(name = "ci")
	private int ci ;
	@MyAnnotation(name = "sidx")
	private int sidx ;
	@MyAnnotation(name = "nid")
	private int nid ;
	@MyAnnotation(name = "bid")
	private int bid ;
	
	@MyAnnotation(name = "ad_plat_name")
	private String adPlatName; // 广告平台名称
	
	@MyAnnotation(name = "ad_type")
	private int adType; // 广告类型

	@MyAnnotation(name = "level_id")
	private String levelId	; // 关卡Id（1：新手，2：主线，3：直线，4：其他）
	
	@MyAnnotation(name = "level_success")
	private boolean levelSuccess; // 是否成功通关
	
	@MyAnnotation(name = "level_time")
	private long levelTime; // 关卡停留时长
	
	@MyAnnotation(name = "equipment_id")
	private String equipmentId	; // 装备id
	
	@MyAnnotation(name = "equipment_get_type")
	private int equipmentGetType; // 装备获得类型(付费获得、充值货币购买)
	
	@MyAnnotation(name = "equipment_remain_qty")
	private long equipmentRemainQty	;// 装备剩余数量
	
	@MyAnnotation(name = "coin")
	private long coin	; // 用户游戏货币
	
	@MyAnnotation(name = "diamond")
	private long diamond; // 用户充值货币
	
	@MyAnnotation(name = "vit")
	private long vit; // 用户当前体力
	
	@MyAnnotation(name = "exist_duration")
	private long existDuration; // 用户时长(第一次启动到当前时刻的时长)

	@MyAnnotation(name = "view_state")
	private int viewState; // 界面状态，所处的界面
	
	@MyAnnotation(name = "pay_err_code")
	private String payErrCode;
	
	@MyAnnotation(name = "pay_err_msg")
	private String payErrMsg;

	@MyAnnotation(name = "pay_order")
	private String payOrder;
	
	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public int getSidIndex() {
		return sidIndex;
	}

	public void setSidIndex(int sidIndex) {
		this.sidIndex = sidIndex;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getIccid() {
		return iccid;
	}

	public void setIccid(String iccid) {
		this.iccid = iccid;
	}

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}

	public String getCkappid() {
		return ckappid;
	}

	public void setCkappid(String ckappid) {
		this.ckappid = ckappid;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	public String getNetType() {
		return netType;
	}

	public void setNetType(String netType) {
		this.netType = netType;
	}

	public long getOccurTime() {
		return occurTime;
	}

	public void setOccurTime(long occurTime) {
		this.occurTime = occurTime;
	}

	public Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	public String getVersioName() {
		return versioName;
	}

	public void setVersioName(String versioName) {
		this.versioName = versioName;
	}

	public String getPhoneModel() {
		return phoneModel;
	}

	public void setPhoneModel(String phoneModel) {
		this.phoneModel = phoneModel;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getSdkVersion() {
		return sdkVersion;
	}

	public void setSdkVersion(String sdkVersion) {
		this.sdkVersion = sdkVersion;
	}

	public String getExitType() {
		return exitType;
	}

	public void setExitType(String exitType) {
		this.exitType = exitType;
	}

	public String getPaySdk() {
		return paySdk;
	}

	public void setPaySdk(String paySdk) {
		this.paySdk = paySdk;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public String getPayNumber() {
		return payNumber;
	}

	public void setPayNumber(String payNumber) {
		this.payNumber = payNumber;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getAndroidid() {
		return androidid;
	}

	public void setAndroidid(String androidid) {
		this.androidid = androidid;
	}

	public String getSignMD5() {
		return signMD5;
	}

	public void setSignMD5(String signMD5) {
		this.signMD5 = signMD5;
	}

	public int getTrustSign() {
		return trustSign;
	}

	public void setTrustSign(int trustSign) {
		this.trustSign = trustSign;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getProviceName() {
		return proviceName;
	}

	public void setProviceName(String proviceName) {
		this.proviceName = proviceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public int getMcc() {
		return mcc;
	}

	public void setMcc(int mcc) {
		this.mcc = mcc;
	}

	public int getMnc() {
		return mnc;
	}

	public void setMnc(int mnc) {
		this.mnc = mnc;
	}

	public int getLac() {
		return lac;
	}

	public void setLac(int lac) {
		this.lac = lac;
	}

	public int getCi() {
		return ci;
	}

	public void setCi(int ci) {
		this.ci = ci;
	}

	public int getSidx() {
		return sidx;
	}

	public void setSidx(int sidx) {
		this.sidx = sidx;
	}

	public int getNid() {
		return nid;
	}

	public void setNid(int nid) {
		this.nid = nid;
	}

	public int getBid() {
		return bid;
	}

	public void setBid(int bid) {
		this.bid = bid;
	}

	public long getCoin() {
		return coin;
	}

	public void setCoin(long coin) {
		this.coin = coin;
	}

	public long getDiamond() {
		return diamond;
	}

	public void setDiamond(long diamond) {
		this.diamond = diamond;
	}

	public long getVit() {
		return vit;
	}

	public void setVit(long vit) {
		this.vit = vit;
	}

	public String getAdPlatName() {
		return adPlatName;
	}

	public void setAdPlatName(String adPlatName) {
		this.adPlatName = adPlatName;
	}

	public int getAdType() {
		return adType;
	}

	public void setAdType(int adType) {
		this.adType = adType;
	}

	public String getLevelId() {
		return levelId;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public boolean getLevelSuccess() {
		return levelSuccess;
	}

	/**
	 * 转int，成功是0，失败是1
	 * @return
	 */
	public int getLevelSuccessInt() {
		return levelSuccess ? 0 : 1;
	}
	
	public void setLevelSuccess(boolean levelSuccess) {
		this.levelSuccess = levelSuccess;
	}

	public long getLevelTime() {
		return levelTime;
	}

	public void setLevelTime(long levelTime) {
		this.levelTime = levelTime;
	}

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	public int getEquipmentGetType() {
		return equipmentGetType;
	}

	public void setEquipmentGetType(int equipmentGetType) {
		this.equipmentGetType = equipmentGetType;
	}

	public long getEquipmentRemainQty() {
		return equipmentRemainQty;
	}

	public void setEquipmentRemainQty(long equipmentRemainQty) {
		this.equipmentRemainQty = equipmentRemainQty;
	}

	public long getExistDuration() {
		return existDuration;
	}

	public void setExistDuration(long existDuration) {
		this.existDuration = existDuration;
	}

	public int getViewState() {
		return viewState;
	}

	public void setViewState(int viewState) {
		this.viewState = viewState;
	}

	public String getPayErrCode() {
		return payErrCode;
	}

	public void setPayErrCode(String payErrCode) {
		this.payErrCode = payErrCode;
	}

	public String getPayErrMsg() {
		return payErrMsg;
	}

	public void setPayErrMsg(String payErrMsg) {
		this.payErrMsg = payErrMsg;
	}

	public String getPayOrder() {
		return payOrder;
	}

	public void setPayOrder(String payOrder) {
		this.payOrder = payOrder;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
