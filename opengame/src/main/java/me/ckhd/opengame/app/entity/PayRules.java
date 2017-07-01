package me.ckhd.opengame.app.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.ckhd.opengame.common.persistence.DataEntity;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.excel.annotation.ExcelField;

import org.apache.commons.lang3.StringUtils;

public class PayRules  extends  DataEntity<PayRules> implements Cloneable {

	private static final long serialVersionUID = 1L;
	
	private String startTime; //时间段开始时间
	
	private String endTime; //时间段结束时间
	
	private String ckappId; //创酷游戏ID
	
	private String appid; //游戏ID
	
	private String appIds; //游戏ID
	
	private String version; //包版本
	
	private String channelId; //渠道ID
	
	private String internetPay="";//支付方式
	
	private List<PayRulesConfig> configs;
	
	/****************附加********************/
	private  String ckAppName;
	
	private String appName;
	
	private  String channelName;
	
	private String money;//金额
	
	private String totalMoney;//当日限额
	
	private String provinceIds;//省份ID集
	
	private String cmcc_provinceIds;//省份ID集
	
	private String ctcc_provinceIds;//省份ID集
	
	private String cucc_provinceIds;//省份ID集
	
	private String carriers; //运营商
	
	private boolean isLenght=true;//开始时间与结束时间相差50年以上则不显示结束时间
	
	private String simNO;
	
	private String provinceCode;
	
	private boolean isLose;
	
	private String addCkAppId;
	
	private String addAppId;
	
	private String isSave="false";
	
	private String type="0";
	
	public PayRules() {
	}
	
	public PayRules(String ckappId){
		this.ckappId=ckappId;
	}
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	@ExcelField(title="渠道名称", align=2, sort=40)
	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}


	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCkappId() {
		if(ckappId==null && addCkAppId!=null){
			return addCkAppId;
		}
		return ckappId;
	}

	public void setCkappId(String ckappId) {
		this.ckappId = ckappId;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public List<PayRulesConfig> getConfigs() {
		return configs;
	}

	public void setConfigs(List<PayRulesConfig> configs) {
		this.configs = configs;
	}



	public String getInternetPay() {
		return internetPay;
	}

	public void setInternetPay(String internetPay) {
		this.internetPay = internetPay;
	}

	
	public boolean getIsLenght(){
		if(StringUtils.isNotBlank(this.getEndTime())){
			Date start = DateUtils.parseDate(this.getStartTime());
			Date end = DateUtils.parseDate(this.getEndTime());
			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();
			c1.setTime(start);
			c2.setTime(end);
			int year1 = c1.get(Calendar.YEAR);
			int year2 = c2.get(Calendar.YEAR);
			isLenght=Math.abs(year1 - year2)>=50;
		}else{
			isLenght=true;
		}
		return isLenght;
	}
	
	@Override
    public Object clone() throws CloneNotSupportedException {
        return (PayRules)super.clone();
    }

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCarriers() {
		return carriers;
	}

	public void setCarriers(String carriers) {
		this.carriers = carriers;
	}

	public String getSimNO() {
		return simNO;
	}

	public void setSimNO(String simNO) {
		this.simNO = simNO;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}


	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}


	public String getCkAppName() {
		return ckAppName;
	}

	public void setCkAppName(String ckAppName) {
		this.ckAppName = ckAppName;
	}

	public String getAppid() {
		if(appid==null && addAppId!=null){
			return addAppId;
		}
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	//失效返回false,生效返回true
	public boolean getIsLose() {
		if(StringUtils.isBlank(this.getEndTime())){
			return true;
		}
		Double time=DateUtils.getDistanceOfTwoDate(new Date(),DateUtils.parseDate(endTime));
		if(time<0){
			return false;
		}else{
			return true;
		}
	}

	public void setLose(boolean isLose) {
		this.isLose = isLose;
	}

	public String getCmcc_provinceIds() {
		return cmcc_provinceIds;
	}

	public void setCmcc_provinceIds(String cmcc_provinceIds) {
		this.cmcc_provinceIds = cmcc_provinceIds;
	}

	public String getCtcc_provinceIds() {
		return ctcc_provinceIds;
	}

	public void setCtcc_provinceIds(String ctcc_provinceIds) {
		this.ctcc_provinceIds = ctcc_provinceIds;
	}

	public String getCucc_provinceIds() {
		return cucc_provinceIds;
	}

	public void setCucc_provinceIds(String cucc_provinceIds) {
		this.cucc_provinceIds = cucc_provinceIds;
	}

	public String getProvinceIds() {
		return provinceIds;
	}

	public void setProvinceIds(String provinceIds) {
		this.provinceIds = provinceIds;
	}

	public String getAddCkAppId() {
		return addCkAppId;
	}

	public void setAddCkAppId(String addCkAppId) {
		this.addCkAppId = addCkAppId;
	}

	public String getAddAppId() {
		return addAppId;
	}

	public void setAddAppId(String addAppId) {
		this.addAppId = addAppId;
	}

	public String getAppIds() {
		return appIds;
	}

	public void setAppIds(String appIds) {
		this.appIds = appIds;
	}

	@Override
	public String toString() {
		return "PayRules [startTime=" + startTime + ", endTime=" + endTime
				+ ", ckappId=" + ckappId + ", appid=" + appid + ", appIds="
				+ appIds + ", version=" + version + ", channelId=" + channelId
				+ ", internetPay=" + internetPay + ", configs=" + configs
				+ ", ckAppName=" + ckAppName + ", appName=" + appName
				+ ", channelName=" + channelName + ", money=" + money
				+ ", totalMoney=" + totalMoney + ", provinceIds=" + provinceIds
				+ ", cmcc_provinceIds=" + cmcc_provinceIds
				+ ", ctcc_provinceIds=" + ctcc_provinceIds
				+ ", cucc_provinceIds=" + cucc_provinceIds + ", carriers="
				+ carriers + ", isLenght=" + isLenght + ", simNO=" + simNO
				+ ", provinceCode=" + provinceCode + ", isLose=" + isLose
				+ ", addCkAppId=" + addCkAppId + ", addAppId=" + addAppId + "]";
	}

	public String getIsSave() {
		return isSave;
	}

	public void setIsSave(String isSave) {
		this.isSave = isSave;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
