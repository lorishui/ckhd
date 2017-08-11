package me.ckhd.opengame.buyflow.entity;

import java.util.Date;
import java.util.List;

import me.ckhd.opengame.common.persistence.DataEntity;

public class AdQueryEntity extends DataEntity<AdQueryEntity>{
	/**
     * @Field @serialVersionUID :序列化id
     */
    private static final long serialVersionUID = 7274593965725992536L;
    //推广渠道id
	private String mediaId;
	//推广渠道名称
	private String mediaName;
	//负责人id
	private String operator;
	//负责人名称
	private String operatorName;
	//游戏id
	private String appId;
	//游戏名称
	private String appName;
	//平台
	private String platform;
	//消耗
	private double cost;
	//收入
	private double earn;
	//注册设备数
	private int registNum;
	//注册成本
	@SuppressWarnings("unused")
    private double registCost;
	//平均收入
	@SuppressWarnings("unused")
    private double averageEarn;
	//开始日期
	private Date startDate;
	//结束日期
	private Date endDate;
	//日期
	private Date date;
	//链接url
	private String adUrl;
	//是否求和
	private String sum;
	

	private String permissionCkAppId;//用于过滤游戏权限
	private List<String> permissionMediaId;//用于过滤媒体平台权限
	
	
	public AdQueryEntity() {
		super();
	}
	public AdQueryEntity(String id) {
		super(id);
	}
	
	public AdQueryEntity(String mediaId, String mediaName, String operator,
			String operatorName, String appId, String appName, String platform,
			double cost, double earn, int registNum, double registCost,
			double averageEarn, Date startDate, Date endDate, Date date,
			String adUrl, String sum) {
		super();
		this.mediaId = mediaId;
		this.mediaName = mediaName;
		this.operator = operator;
		this.operatorName = operatorName;
		this.appId = appId;
		this.appName = appName;
		this.platform = platform;
		this.cost = cost;
		this.earn = earn;
		this.registNum = registNum;
		this.registCost = registCost;
		this.averageEarn = averageEarn;
		this.startDate = startDate;
		this.endDate = endDate;
		this.date = date;
		this.adUrl = adUrl;
		this.sum = sum;
	}
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public String getMediaName() {
		return mediaName;
	}
	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public int getRegistNum() {
		return registNum;
	}
	public void setRegistNum(int registNum) {
		this.registNum = registNum;
	}
	public double getRegistCost() {
		return cost / registNum;
	}
	public void setRegistCost(double registCost) {
		this.registCost = registCost;
	}
	public double getAverageEarn() {
		return earn / registNum;
	}
	public void setAverageEarn(double averageEarn) {
		this.averageEarn = averageEarn;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getAdUrl() {
		return adUrl;
	}
	public void setAdUrl(String adUrl) {
		this.adUrl = adUrl;
	}
	public double getEarn() {
		return earn;
	}
	public void setEarn(double earn) {
		this.earn = earn;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getSum() {
		return sum;
	}
	public void setSum(String sum) {
		this.sum = sum;
	}
	public String getPermissionCkAppId() {
		return permissionCkAppId;
	}
	public void setPermissionCkAppId(String permissionCkAppId) {
		this.permissionCkAppId = permissionCkAppId;
	}
	public List<String> getPermissionMediaId() {
		return permissionMediaId;
	}
	public void setPermissionMediaId(List<String> permissionMediaId) {
		this.permissionMediaId = permissionMediaId;
	}
	
}
