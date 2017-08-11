package me.ckhd.opengame.app.entity;

import java.util.Date;

import me.ckhd.opengame.common.persistence.DataEntity;

/**
 * 错误日志
 * @author wizard
 */
public class AppErrorLog extends DataEntity<AppErrorLog> {

	private static final long serialVersionUID = -152614160937792251L;
	
	private Integer ckAppId;			// 创酷APPID
	private Integer ckAppChildId;		// 创酷子APPID（或者单机的APPID）
	
	private Integer channelId;			// 创酷统一渠道号
	private Integer channelChildId;	// 创酷统一子渠道号
	
	private String sid;			// 会话标识
	private String deviceId;		// 设备号：android:IMEI，ios：idfa
	private String imsi;			// IMSI号
	private String iccid;			// ICCID号
	private String androidId;		// android id

	private String versionName;	// 应用版本号
	
	private String netType;		// 当前网络类型：wifi、4g
	
	private String cpRoleId;		//游戏角色ID
	private String cpServerId;		//区服

	private String osLang;			// 系统语言（zh_cn）
	private String osVersion;		// 操作系统版本（android4.4,android5.0）
	private String deviceModel;	// 设备型号
	private String cpuInfo;
	private String memInfo;

	private String sdkVersionCk;		
	private String sdkVersionAccount;	
	private String sdkVersionPay;		
	
	private String ip;				// IP地址
	private String country;		// 国家
	private String province;		// 省份
	private String city;			// 城市
	
	private Date firstStartupTime;			//第一将启动时间
	private Long runtimeMillisecond;		//启动时长
	
	private String lastState;				//最后的状态
	private String lastAction;				//最后的操作
	
	private String errorClassName;			//错误的类名
	private Integer errorLineNumber;		//错误行数
	private String exceptionClassName;		//异常类名
	
	private String topic;			// 日志主题
	private String content;		// 日志内容
	
	private Date occurTime;		// 发生时间，与服务器时间差超过7天修正为服务器时间
	private Integer occurDate;			// YYYYMMDD
	private Integer insertDate;		// YYYYMMDD
	private Date insertTime;		// 入库时间
	
	public int getCkAppId() {
		return ckAppId;
	}
	public void setCkAppId(Integer ckAppId) {
		this.ckAppId = ckAppId;
	}
	public int getCkAppChildId() {
		return ckAppChildId;
	}
	public void setCkAppChildId(Integer ckAppChildId) {
		this.ckAppChildId = ckAppChildId;
	}
	public Integer getChannelId() {
		return channelId;
	}
	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
	public Integer getChannelChildId() {
		return channelChildId;
	}
	public void setChannelChildId(Integer channelChildId) {
		this.channelChildId = channelChildId;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getIccid() {
		return iccid;
	}
	public void setIccid(String iccid) {
		this.iccid = iccid;
	}
	public String getAndroidId() {
		return androidId;
	}
	public void setAndroidId(String androidId) {
		this.androidId = androidId;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getNetType() {
		return netType;
	}
	public void setNetType(String netType) {
		this.netType = netType;
	}
	public String getCpRoleId() {
		return cpRoleId;
	}
	public void setCpRoleId(String cpRoleId) {
		this.cpRoleId = cpRoleId;
	}
	public String getCpServerId() {
		return cpServerId;
	}
	public void setCpServerId(String cpServerId) {
		this.cpServerId = cpServerId;
	}
	public String getOsLang() {
		return osLang;
	}
	public void setOsLang(String osLang) {
		this.osLang = osLang;
	}
	public String getOsVersion() {
		return osVersion;
	}
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
	public String getDeviceModel() {
		return deviceModel;
	}
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	public String getCpuInfo() {
		return cpuInfo;
	}
	public void setCpuInfo(String cpuInfo) {
		this.cpuInfo = cpuInfo;
	}
	public String getMemInfo() {
		return memInfo;
	}
	public void setMemInfo(String memInfo) {
		this.memInfo = memInfo;
	}
	public String getSdkVersionCk() {
		return sdkVersionCk;
	}
	public void setSdkVersionCk(String sdkVersionCk) {
		this.sdkVersionCk = sdkVersionCk;
	}
	public String getSdkVersionAccount() {
		return sdkVersionAccount;
	}
	public void setSdkVersionAccount(String sdkVersionAccount) {
		this.sdkVersionAccount = sdkVersionAccount;
	}
	public String getSdkVersionPay() {
		return sdkVersionPay;
	}
	public void setSdkVersionPay(String sdkVersionPay) {
		this.sdkVersionPay = sdkVersionPay;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Date getFirstStartupTime() {
		return firstStartupTime;
	}
	public void setFirstStartupTime(Date firstStartupTime) {
		this.firstStartupTime = firstStartupTime;
	}
	public Long getRuntimeMillisecond() {
		return runtimeMillisecond;
	}
	public void setRuntimeMillisecond(Long runtimeMillisecond) {
		this.runtimeMillisecond = runtimeMillisecond;
	}
	public String getLastState() {
		return lastState;
	}
	public void setLastState(String lastState) {
		this.lastState = lastState;
	}
	public String getLastAction() {
		return lastAction;
	}
	public void setLastAction(String lastAction) {
		this.lastAction = lastAction;
	}
	public String getErrorClassName() {
		return errorClassName;
	}
	public void setErrorClassName(String errorClassName) {
		this.errorClassName = errorClassName;
	}
	public Integer getErrorLineNumber() {
		return errorLineNumber;
	}
	public void setErrorLineNumber(Integer errorLineNumber) {
		this.errorLineNumber = errorLineNumber;
	}
	public String getExceptionClassName() {
		return exceptionClassName;
	}
	public void setExceptionClassName(String exceptionClassName) {
		this.exceptionClassName = exceptionClassName;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getOccurTime() {
		return occurTime;
	}
	public void setOccurTime(Date occurTime) {
		this.occurTime = occurTime;
	}
	public Integer getOccurDate() {
		return occurDate;
	}
	public void setOccurDate(Integer occurDate) {
		this.occurDate = occurDate;
	}
	public Integer getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Integer insertDate) {
		this.insertDate = insertDate;
	}
	public Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	public void setInsertTime(long insertTime) {
		this.insertTime = new Date(insertTime);
	}
}